package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import logic.AlgebraicConverter;
import logic.Board;
import logic.Builder;
import logic.Game;
import logic.Move;
import logic.Piece;
import logic.Result;
import logic.Square;
import timer.ChessTimer;
import timer.TimerTypes;
import utility.AppConstants;
import utility.FileUtility;

import com.google.common.collect.ImmutableList;

import dragNdrop.AbstractDropManager;
import dragNdrop.DropAdapter;
import dragNdrop.DropEvent;
import dragNdrop.GlassPane;
import dragNdrop.MotionAdapter;

public class PlayGamePanel extends JPanel
{
	public PlayGamePanel(boolean isPlayback, File acnFile) throws Exception
	{
		m_dropManager = new DropManager();
		m_globalGlassPane = new GlassPane();
		m_globalGlassPane.setOpaque(false);
		Driver.getInstance().setGlassPane(m_globalGlassPane);

		setGame(Builder.newGame("Classic"));
		PlayGamePanel.m_isPlayback = isPlayback;
		PlayGamePanel.m_whiteTimer = getGame().getWhiteTimer();
		PlayGamePanel.m_blackTimer = getGame().getBlackTimer();
		m_whiteTimer.restart();
		m_blackTimer.restart();
		turn(getGame().getBoards()[0].isBlackTurn());
		initComponents(isPlayback);
		setGame(AlgebraicConverter.convert(getGame(), acnFile));
		m_history = new Move[getGame().getHistory().size()];
		getGame().getHistory().toArray(m_history);
		m_historyIndex = m_history.length - 1;

		while (m_historyIndex >= 0)
			m_history[m_historyIndex--].undo();

		boardRefresh(getGame().getBoards());
	}

	public PlayGamePanel(Game game, boolean isPlayback) throws Exception
	{
		PlayGamePanel.setGame(game);
		PlayGamePanel.m_isPlayback = isPlayback;
		m_dropManager = new DropManager();
		if (isPlayback)
		{
			PlayGamePanel.m_whiteTimer = ChessTimer.createTimer(TimerTypes.NO_TIMER, null, 0, 0, false);
			PlayGamePanel.m_blackTimer = ChessTimer.createTimer(TimerTypes.NO_TIMER, null, 0, 0, true);
			m_history = new Move[game.getHistory().size()];
			game.getHistory().toArray(m_history);
			initComponents(isPlayback);
			m_historyIndex = m_history.length - 1;

			while (m_historyIndex >= 0)
			{
				m_history[m_historyIndex].undo();
				m_historyIndex--;
			}
		}
		else
		{
			m_globalGlassPane = new GlassPane();
			m_globalGlassPane.setOpaque(false);
			Driver.getInstance().setGlassPane(m_globalGlassPane);

			PlayGamePanel.m_whiteTimer = game.getWhiteTimer();
			PlayGamePanel.m_blackTimer = game.getBlackTimer();
			m_whiteTimer.restart();
			m_blackTimer.restart();
			turn(game.isBlackMove());
			m_history = null;
			m_historyIndex = -3;
			initComponents(isPlayback);
		}
		boardRefresh(game.getBoards());
	}

	public static void boardRefresh(Board[] boards)
	{
		refreshSquares(boards);

		Piece objectivePiece = getGame().isBlackMove() ? getGame().getBlackRules().objectivePiece(true) : getGame().getWhiteRules()
				.objectivePiece(false);

		if (objectivePiece != null && objectivePiece.isInCheck())
		{
			m_inCheckLabel.setVisible(true);
			if (getGame().getBlackRules().objectivePiece(true).isInCheck())
				m_inCheckLabel.setBorder(BorderFactory.createTitledBorder("Black Team"));
			else
				m_inCheckLabel.setBorder(BorderFactory.createTitledBorder("White Team"));

			for (Piece piece : getGame().getThreats(objectivePiece))
				piece.getSquare().setColor(Color.red);
		}
		else
		{
			m_inCheckLabel.setVisible(false);
		}

		int index = 0;
		Piece[] blackCapturedPieces = getGame().getCapturedPieces(true);
		for (int i = m_whiteCapturesJail.getMaxRow(); i >= 1; i--)
		{
			for (int j = 1; j <= m_whiteCapturesJail.getMaxCol(); j++)
			{
				if (blackCapturedPieces != null && index < blackCapturedPieces.length)
				{
					m_whiteCapturesJail.getSquare(i, j).setPiece(blackCapturedPieces[index]);
					index++;
				}
				m_whiteCapturesJail.getSquare(i, j).refreshJail();
			}
		}
		
		index = 0;
		Piece[] whiteCapturedPieces = getGame().getCapturedPieces(false);
		for (int i = m_blackCapturesJail.getMaxRow(); i >= 1; i--)
		{
			for (int j = 1; j <= m_blackCapturesJail.getMaxCol(); j++)
			{
				if (whiteCapturedPieces != null && index < whiteCapturedPieces.length)
				{
					m_blackCapturesJail.getSquare(i, j).setPiece(whiteCapturedPieces[index]);
					index++;
				}
				m_blackCapturesJail.getSquare(i, j).refreshJail();
			}
		}

		m_whiteLabel.setBackground(getGame().isBlackMove() ? null : Square.HIGHLIGHT_COLOR);
		m_whiteLabel.setForeground(getGame().isBlackMove() ? Color.black : Color.white);
		m_blackLabel.setBackground(getGame().isBlackMove() ? Square.HIGHLIGHT_COLOR : null);
		m_blackLabel.setForeground(getGame().isBlackMove() ? Color.white : Color.black);
	}

	private static void refreshSquares(Board[] boards)
	{
		for (int k = 0; k < boards.length; k++)
		{
			for (int i = 1; i <= boards[k].getMaxRow(); i++)
			{
				for (int j = 1; j <= boards[k].getMaxCol(); j++)
					boards[k].getSquare(i, j).refresh();
			}
		}
	}

	public static void endOfGame(Result result)
	{
		PlayNetGamePanel.m_isRunning = false;
		if (m_game.getHistory().size() != 0)
		{
			PlayNetGamePanel.m_netMove = m_game.moveToFakeMove(m_game.getHistory().get(m_game.getHistory().size() - 1));
		}
		else if (result != Result.DRAW)
		{
			JOptionPane.showMessageDialog(null, "No moves were made and the time ran out. Returning to the Main Menu.",
					"Time Ran Out", JOptionPane.PLAIN_MESSAGE);
			PlayNetGamePanel.m_isRunning = false;
			Driver.getInstance().revertToMainPanel();
			Driver.getInstance().setFileMenuVisibility(true);
			return;
		}

		if (m_isPlayback)
			return;

		Object[] options = new String[] { "Save Record of Game", "New Game", "Quit" };
		m_optionsMenu.setVisible(false);
		switch (JOptionPane.showOptionDialog(Driver.getInstance(), result.getGUIText(), result.winText(), JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[0]))
		{
		case JOptionPane.YES_OPTION:
			File preferencesFile = FileUtility.getPreferencesFile();
			if (!preferencesFile.exists())
			{
				try
				{
					preferencesFile.createNewFile();
					BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(preferencesFile, true));
					bufferedWriter.write("DefaultPreferencesSet = false");
					bufferedWriter.newLine();
					bufferedWriter.write("DefaultSaveLocation = " + FileUtility.getDefaultCompletedLocation());
					bufferedWriter.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}

			try
			{
				FileInputStream fileInputStream = new FileInputStream(preferencesFile);
				DataInputStream in = new DataInputStream(fileInputStream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String line;
				line = br.readLine();
				fileInputStream.close();
				in.close();
				br.close();
				if (line.contains("false"))
				{
					PrintWriter printWriter = new PrintWriter(preferencesFile);
					printWriter.print("");
					printWriter.close();
					JOptionPane.showMessageDialog(Driver.getInstance(), "Since this is your first time playing " + AppConstants.APP_NAME
							+ ", please choose a default completed game save location.\n"
							+ "Pressing cancel will use the default save location.", "Save Location", JOptionPane.PLAIN_MESSAGE);
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int returnVal = fileChooser.showOpenDialog(Driver.getInstance());
					BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(preferencesFile, true));
					bufferedWriter.write("DefaultPreferencesSet = true");
					bufferedWriter.newLine();
					if (returnVal == JFileChooser.APPROVE_OPTION)
					{
						bufferedWriter.write("DefaultSaveLocation = " + fileChooser.getSelectedFile().getAbsolutePath());
						bufferedWriter.close();
					}
					else
					{
						bufferedWriter.write("DefaultSaveLocation = " + FileUtility.getDefaultCompletedLocation());
						bufferedWriter.close();
					}
				}
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(null, "That is not a valid location to save your completed games.", "Invalid Location",
						JOptionPane.PLAIN_MESSAGE);
				e.printStackTrace();
			}

			String saveFileName = JOptionPane.showInputDialog(Driver.getInstance(), "Enter a name for the save file:", "Saving...",
					JOptionPane.PLAIN_MESSAGE);
			getGame().saveGame(saveFileName, getGame().isClassicChess());
			m_game.setBlackMove(false);
			Driver.getInstance().setFileMenuVisibility(true);
			Driver.getInstance().revertToMainPanel();
			break;
		case JOptionPane.NO_OPTION:
			m_game.setBlackMove(false);
			Driver.getInstance().setUpNewGame();
			break;
		case JOptionPane.CANCEL_OPTION:
			m_game.setBlackMove(false);
			System.exit(0);
			break;
		}
	}

	public void saveGame()
	{
		String fileName = JOptionPane.showInputDialog(Driver.getInstance(), "Enter a name for the save file:", "Saving...", JOptionPane.PLAIN_MESSAGE);
		if (fileName == null)
			return;
		getGame().saveGame(fileName, false);
	}

	public static void turn(boolean isBlackTurn)
	{
		if (m_whiteTimer != null && m_blackTimer != null)
		{
			(!isBlackTurn ? m_whiteTimer : m_blackTimer).startTimer();
			(isBlackTurn ? m_whiteTimer : m_blackTimer).stopTimer();
		}
	}

	private JPanel createGrid(Board board, boolean isPlayback, boolean isJail)
	{
		final JPanel gridPanel = new JPanel();

		gridPanel.setLayout(new GridLayout(board.numRows() + 1, board.numCols()));
		gridPanel.setPreferredSize(new Dimension((board.numCols() + 1) * 48, (board.numRows() + 1) * 48));

		int numberOfRows = board.numRows();
		int numOfColumns = board.numCols();
		for (int i = numberOfRows; i > 0; i--)
		{
			if(!isJail){
				JLabel label = new JLabel("" + i);
				label.setHorizontalAlignment(SwingConstants.CENTER);
				gridPanel.add(label);
			}

			for (int j = 1; j <= numOfColumns; j++)
			{
				Square square = board.getSquare(i, j);
				if (!isPlayback)
				{
					if(!isJail){
						square.addMouseListener(new SquareListener(square, board));
						square.addMouseMotionListener(new MotionAdapter(m_globalGlassPane));
					}
				}

				gridPanel.add(square);
			}

		}
		if(!isJail){
			for (int k = 0; k <= numOfColumns; k++)
			{
				if (k != 0)
				{
					JLabel label = new JLabel("" + (char) (k - 1 + 'A'));
					label.setHorizontalAlignment(SwingConstants.CENTER);
					gridPanel.add(label);
				}
				else
				{
					gridPanel.add(new JLabel(""));
				}
			}
		}
		return gridPanel;
	}

	public JMenu createMenuBar()
	{
		m_optionsMenu = new JMenu("Menu");

		if (!m_isPlayback)
		{
			m_drawMenuItem = new JMenuItem("Declare Draw", KeyEvent.VK_D);
			m_saveMenuItem = new JMenuItem("Save & Quit", KeyEvent.VK_S);

			m_drawMenuItem.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent event)
				{
					if (getGame().getLastMove() == null)
						return;

					m_optionsMenu.setVisible(false);
					Result result = Result.DRAW;
					result.setGuiText("Draw! \nWhat would you like to do? \n");
					getGame().getLastMove().setResult(result);
					endOfGame(result);
				}
			});

			m_saveMenuItem.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent event)
				{
					m_whiteTimer.stopTimer();
					m_blackTimer.stopTimer();
					saveGame();

					m_optionsMenu.setVisible(false);
					Driver.getInstance().revertToMainPanel();
				}
			});

			m_optionsMenu.add(m_drawMenuItem);
			m_optionsMenu.add(m_saveMenuItem);
		}

		return m_optionsMenu;
	}

	private void initComponents(boolean isPlayback) throws Exception
	{
		m_inCheckLabel = new JLabel("You're In Check!");
		m_inCheckLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		m_inCheckLabel.setForeground(Color.RED);

		m_undoButton = new JButton("Undo");
		m_undoButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (getGame().getHistory().size() == 0)
					return;

				try
				{
					getGame().getHistory().get(getGame().getHistory().size() - 1).undo();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

				getGame().getHistory().remove(getGame().getHistory().size() - 1);
				(getGame().isBlackMove() ? getGame().getBlackRules() : getGame().getWhiteRules()).undoEndOfGame();
				boardRefresh(getGame().getBoards());
			}
		});

		int twoBoardsGridBagOffset = 0;
		if (m_optionsMenu == null || !m_optionsMenu.isVisible())
			Driver.getInstance().setMenu(createMenuBar());

		Driver.getInstance().setOptionsMenuVisibility(!isPlayback);

		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		final Board[] boards = getGame().getBoards();
		setBorder(BorderFactory.createLoweredBevelBorder());

		m_inCheckLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		m_inCheckLabel.setHorizontalAlignment(SwingConstants.CENTER);
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridy = 0;
		constraints.gridx = 9;
		m_inCheckLabel.setVisible(false);
		add(m_inCheckLabel, constraints);

		if (boards.length == 1)
		{
			constraints.gridheight = 12;
			constraints.gridy = 2;
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.gridwidth = 10;
			constraints.gridheight = 10;
			constraints.insets = new Insets(10, 0, 0, 0);
			constraints.gridx = 0;

			add(createGrid(boards[0], isPlayback, false), constraints);
		}
		else
		{
			constraints.gridheight = 12;
			constraints.gridy = 2;
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.gridwidth = 10;
			constraints.insets = new Insets(10, 0, 0, 0);
			constraints.gridx = 0;

			add(createGrid(boards[0], isPlayback, false), constraints);

			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.gridwidth = 10;
			constraints.insets = new Insets(10, 0, 0, 0);
			constraints.gridx = 11;
			add(createGrid(boards[1], isPlayback, false), constraints);

			twoBoardsGridBagOffset += 10;
		}

		JButton nextButton = new JButton("Next");
		nextButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (m_historyIndex + 1 == m_history.length)
					return;

				try
				{
					m_history[++m_historyIndex].execute();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

		JButton prevButton = new JButton("Previous");
		prevButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (m_historyIndex == -1)
					return;

				try
				{
					m_history[m_historyIndex--].undo();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

		m_whiteLabel = new JLabel("WHITE");
		m_whiteLabel.setHorizontalAlignment(SwingConstants.CENTER);
		m_whiteLabel.setBorder(BorderFactory.createTitledBorder(""));

		m_blackLabel = new JLabel("BLACK");
		m_blackLabel.setHorizontalAlignment(SwingConstants.CENTER);
		m_blackLabel.setBorder(BorderFactory.createTitledBorder(""));

		m_whiteLabel.setOpaque(true);
		m_blackLabel.setOpaque(true);
		m_whiteLabel.setVisible(true);
		m_blackLabel.setVisible(true);

		int jailBoardSize;
		if (getGame().getWhiteTeam().size() <= 4 && getGame().getBlackTeam().size() <= 4)
		{
			jailBoardSize = 4;
		}
		else
		{
			double size = getGame().getWhiteTeam().size() > getGame().getBlackTeam().size() ? Math.sqrt(getGame().getWhiteTeam()
					.size()) : Math.sqrt(getGame().getBlackTeam().size());
			jailBoardSize = (int) Math.ceil(size);
		}

		m_whiteCapturesJail = new Board(jailBoardSize, jailBoardSize, false);
		m_whiteCapturePanel = createGrid(m_whiteCapturesJail, isPlayback, true);
		m_whiteCapturePanel.setBorder(BorderFactory.createTitledBorder("Captured Pieces"));
		m_whiteCapturePanel.setLayout(new GridLayout(jailBoardSize, jailBoardSize));

		m_whiteCapturePanel.setPreferredSize(new Dimension((m_whiteCapturesJail.getMaxCol() + 1) * 25, (m_whiteCapturesJail
				.getMaxRow() + 1) * 25));

		m_blackCapturesJail = new Board(jailBoardSize, jailBoardSize, false);
		m_blackCapturePanel = createGrid(m_blackCapturesJail, isPlayback, true);
		m_blackCapturePanel.setBorder(BorderFactory.createTitledBorder("Captured Pieces"));
		m_blackCapturePanel.setLayout(new GridLayout(jailBoardSize, jailBoardSize));

		m_blackCapturePanel.setPreferredSize(new Dimension((m_blackCapturesJail.getMaxCol() + 1) * 25, (m_blackCapturesJail
				.getMaxRow() + 1) * 25));

		// add the Black Name
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.BASELINE;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.insets = new Insets(10, 10, 10, 0);
		constraints.ipadx = 100;
		constraints.gridx = 11 + twoBoardsGridBagOffset;
		constraints.gridy = 0;
		add(m_blackLabel, constraints);

		// add the Black Jail
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.BASELINE;
		constraints.gridwidth = 3;
		constraints.gridheight = 3;
		constraints.ipadx = 0;
		constraints.insets = new Insets(0, 25, 10, 25);
		constraints.gridx = 11 + twoBoardsGridBagOffset;
		constraints.gridy = 1;
		add(m_blackCapturePanel, constraints);

		if (!isPlayback)
		{
			// add the Black timer
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.anchor = GridBagConstraints.BASELINE;
			constraints.gridwidth = 3;
			constraints.gridheight = 1;
			constraints.ipadx = 100;
			constraints.gridx = 11 + twoBoardsGridBagOffset;
			constraints.gridy = 4;
			add(m_blackTimer.getDisplayLabel(), constraints);

			// adds the undo button
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.anchor = GridBagConstraints.BASELINE;
			constraints.gridwidth = 3;
			constraints.gridheight = 1;
			constraints.ipadx = 100;
			constraints.gridx = 11 + twoBoardsGridBagOffset;
			constraints.gridy = 5;
			add(m_undoButton, constraints);

			// adds the White timer
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.anchor = GridBagConstraints.BASELINE;
			constraints.gridwidth = 3;
			constraints.gridheight = 1;
			constraints.ipadx = 100;
			constraints.gridx = 11 + twoBoardsGridBagOffset;
			constraints.gridy = 6;
			add(m_whiteTimer.getDisplayLabel(), constraints);
		}
		else
		{
			// adds the Black timer
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.anchor = GridBagConstraints.BASELINE;
			constraints.gridwidth = 3;
			constraints.gridheight = 1;
			constraints.ipadx = 100;
			constraints.gridx = 11 + twoBoardsGridBagOffset;
			constraints.gridy = 4;
			add(nextButton, constraints);

			// adds the White timer
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.anchor = GridBagConstraints.BASELINE;
			constraints.gridwidth = 3;
			constraints.gridheight = 1;
			constraints.ipadx = 100;
			constraints.gridx = 11 + twoBoardsGridBagOffset;
			constraints.gridy = 5;
			add(prevButton, constraints);
		}

		// adds the White Jail
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.BASELINE;
		constraints.gridwidth = 3;
		constraints.gridheight = 3;
		constraints.ipadx = 0;
		constraints.gridx = 11 + twoBoardsGridBagOffset;

		// change spacing and location if there is a timer or not.
		if (ChessTimer.isNoTimer(m_whiteTimer))
		{
			constraints.gridy = 6;
			constraints.insets = new Insets(10, 25, 0, 25);
		}
		else
		{
			constraints.gridy = 7;
			constraints.insets = new Insets(0, 25, 0, 25);
		}
		add(m_whiteCapturePanel, constraints);

		// add the White Name
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.BASELINE;
		constraints.gridwidth = 3;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.insets = new Insets(10, 0, 10, 0);

		// change spacing if there is a timer
		if (ChessTimer.isNoTimer(m_whiteTimer))
		{
			constraints.gridheight = 1;
			constraints.gridy = 9;
		}
		else
		{
			constraints.gridheight = 2;
			constraints.gridy = 11;
		}
		constraints.ipadx = 100;
		constraints.gridx = 11 + twoBoardsGridBagOffset;
		add(m_whiteLabel, constraints);
	}

	public static void setNextMoveMustPlacePiece(boolean nextMoveMustPlacePiece)
	{
		m_nextMoveMustPlacePiece = nextMoveMustPlacePiece;
	}

	public static boolean getNextMoveMustPlacePiece()
	{
		return m_nextMoveMustPlacePiece;
	}

	public static void setPieceToPlace(Piece piece)
	{
		m_pieceToPlace = piece;
	}

	public static void setGame(Game game)
	{
		PlayGamePanel.m_game = game;
	}

	public static Game getGame()
	{
		return m_game;
	}

	public static void resetTimers()
	{
		m_whiteTimer.reset();
		m_blackTimer.reset();
	}

	protected class SquareListener extends DropAdapter implements MouseListener
	{
		public SquareListener(Square square, Board board)
		{
			super(m_globalGlassPane);
			m_square = square;
			m_board = board;
			addDropListener(m_dropManager);
		}

		@Override
		public void mouseClicked(MouseEvent event)
		{
		}

		@Override
		public void mouseEntered(MouseEvent event)
		{
		}

		@Override
		public void mouseExited(MouseEvent event)
		{
		}

		@Override
		public void mousePressed(MouseEvent event)
		{
			// TODO: dropping from a jail currently doesn't work
//			if (m_nextMoveMustPlacePiece)
//			{
//				m_nextMoveMustPlacePiece = false;
//				getGame().nextTurn();
//				if (!m_clickedSquare.isOccupied() && m_clickedSquare.isHabitable() && m_pieceToPlace != null)
//				{
//					m_pieceToPlace.setSquare(m_clickedSquare);
//					m_clickedSquare.setPiece(m_pieceToPlace);
//					m_pieceToPlace = null;
//					m_nextMoveMustPlacePiece = false;
//					boardRefresh(getGame().getBoards());
//					getGame().genLegalDests();
//				}
//
//				return;
//			}

			if (m_square.getPiece() == null || 
					m_square.getPiece().isBlack() != getGame().isBlackMove())
			{
				return;
			}

			List<Square> destinations = m_square.getPiece().getLegalDests();
			if (destinations.size() > 0)
			{
				for (Square destination : destinations)
					destination.setBackgroundColor(Square.HIGHLIGHT_COLOR);
			}
			m_dropManager.setComponentList(destinations);
			m_dropManager.setBoard(m_board);

			if(m_square.getPiece() == null)
				return;
			else
				m_square.hideIcon();

			Driver.getInstance().setGlassPane(m_glassPane);
			Component component = event.getComponent();

			m_glassPane.setVisible(true);

			Point point = (Point) event.getPoint().clone();
			SwingUtilities.convertPointToScreen(point, component);
			SwingUtilities.convertPointFromScreen(point, m_glassPane);

			m_glassPane.setPoint(point);

			BufferedImage image = null;
			ImageIcon imageIcon = m_square.getPiece().getIcon();
			int width = imageIcon.getIconWidth();
			int height = imageIcon.getIconHeight();
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D graphics2D = (Graphics2D) image.getGraphics();
			imageIcon.paintIcon(null, graphics2D, 0, 0);
			graphics2D.dispose();

			m_glassPane.setImage(image);
			m_glassPane.repaint();
		}

		@Override
		public void mouseReleased(MouseEvent event)
		{
			Point point = (Point) event.getPoint().clone();
			SwingUtilities.convertPointToScreen(point, event.getComponent());

			m_glassPane.setImage(null);
			m_glassPane.setVisible(false);

			fireDropEvent(new DropEvent(point, m_square));
		}

		private Square m_square;
		private Board m_board;
	}

	private static class DropManager extends AbstractDropManager
	{
		public void setBoard(Board board)
		{
			m_board = board;
		}

		@Override
		public void dropped(DropEvent event)
		{
			Square originSquare = (Square) event.getOriginComponent();
			Square destinationSquare = (Square) isInTarget(event.getDropLocation());

			refreshSquares(getGame().getBoards());
			final List<JComponent> dummyList = ImmutableList.of();
			setComponentList(dummyList);

			if (destinationSquare == null)
				return;

			try
			{
				getGame().playMove(new Move(m_board, originSquare, destinationSquare));
				boardRefresh(getGame().getBoards());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		private Board m_board;
	};

	private static final long serialVersionUID = -2507232401817253688L;

	protected static boolean m_nextMoveMustPlacePiece;
	protected static boolean m_isPlayback;
	protected static Game m_game;
	protected static ChessTimer m_whiteTimer;
	protected static ChessTimer m_blackTimer;
	protected static JLabel m_inCheckLabel;
	protected static JLabel m_whiteLabel;
	protected static JLabel m_blackLabel;
	protected static JPanel m_whiteCapturePanel;
	protected static JPanel m_blackCapturePanel;
	protected static Board m_whiteCapturesJail;
	protected static Board m_blackCapturesJail;
	protected static Piece m_pieceToPlace;
	protected static JMenu m_optionsMenu;
	protected static Move[] m_history;
	protected static int m_historyIndex;

	private final DropManager m_dropManager;

	protected GlassPane m_globalGlassPane;
	protected JButton m_undoButton;
	protected JMenuItem m_saveMenuItem;
	protected JMenuItem m_drawMenuItem;
}
