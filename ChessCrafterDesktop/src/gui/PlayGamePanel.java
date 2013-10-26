package gui;

import gui.PreferenceUtility.PieceToolTipPreferenceChangedListener;

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

import logic.Board;
import logic.Game;
import logic.Move;
import logic.Piece;
import logic.Result;
import logic.Square;
import timer.ChessTimer;
import utility.AppConstants;
import utility.FileUtility;
import utility.GuiUtility;
import utility.Preference;

import com.google.common.collect.ImmutableList;

import dragNdrop.AbstractDropManager;
import dragNdrop.DropAdapter;
import dragNdrop.DropEvent;
import dragNdrop.GlassPane;
import dragNdrop.MotionAdapter;

public class PlayGamePanel extends ChessPanel implements PlayGameScreen
{
	public PlayGamePanel(Game game)
	{
		if (game != null)
			setGame(game);
		else
			game = getGame();

		m_dropManager = new DropManager();
		m_globalGlassPane = new GlassPane();
		m_globalGlassPane.setOpaque(false);
		Driver.getInstance().setGlassPane(m_globalGlassPane);

		PlayGamePanel.mWhiteTimer = game.getWhiteTimer();
		PlayGamePanel.mBlackTimer = game.getBlackTimer();
		mWhiteTimer.restart();
		mBlackTimer.restart();
		turn(game.isBlackMove());
		mHistory = null;
		mHistoryIndex = -3;
		try
		{
			initComponents();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		s_instance = this;
		boardRefresh(game.getBoards());
	}

	public void boardRefresh(Board[] boards)
	{
		refreshSquares(boards);

		Piece objectivePiece = getGame().isBlackMove() ? getGame().getBlackRules().objectivePiece(true) : getGame().getWhiteRules()
				.objectivePiece(false);

		if (objectivePiece != null && objectivePiece.isInCheck())
		{
			mInCheckLabel.setVisible(true);
			if (getGame().getBlackRules().objectivePiece(true).isInCheck())
				mInCheckLabel.setBorder(BorderFactory.createTitledBorder(Messages.getString("PlayGamePanel.blackTeam"))); //$NON-NLS-1$
			else
				mInCheckLabel.setBorder(BorderFactory.createTitledBorder(Messages.getString("PlayGamePanel.whiteTeam"))); //$NON-NLS-1$

			for (Piece piece : getGame().getThreats(objectivePiece))
				piece.getSquare().setColor(Color.red);
		}
		else
		{
			mInCheckLabel.setVisible(false);
		}

		int index = 0;
		Piece[] blackCapturedPieces = getGame().getCapturedPieces(true);
		for (int i = mWhiteCapturesJail.getMaxRow(); i >= 1; i--)
		{
			for (int j = 1; j <= mWhiteCapturesJail.getMaxCol(); j++)
			{
				if (blackCapturedPieces != null && index < blackCapturedPieces.length)
				{
					mWhiteCapturesJail.getSquare(i, j).setPiece(blackCapturedPieces[index]);
					index++;
				}
				mWhiteCapturesJail.getSquare(i, j).refreshJail();
			}
		}

		index = 0;
		Piece[] whiteCapturedPieces = getGame().getCapturedPieces(false);
		for (int i = mBlackCapturesJail.getMaxRow(); i >= 1; i--)
		{
			for (int j = 1; j <= mBlackCapturesJail.getMaxCol(); j++)
			{
				if (whiteCapturedPieces != null && index < whiteCapturedPieces.length)
				{
					mBlackCapturesJail.getSquare(i, j).setPiece(whiteCapturedPieces[index]);
					index++;
				}
				mBlackCapturesJail.getSquare(i, j).refreshJail();
			}
		}

		mWhiteLabel.setBackground(getGame().isBlackMove() ? null : Square.HIGHLIGHT_COLOR);
		mWhiteLabel.setForeground(getGame().isBlackMove() ? Color.black : Color.white);
		mBlackLabel.setBackground(getGame().isBlackMove() ? Square.HIGHLIGHT_COLOR : null);
		mBlackLabel.setForeground(getGame().isBlackMove() ? Color.white : Color.black);
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

	public void endOfGame(Result result)
	{
		PlayNetGamePanel.mIsRunning = false;
		if (mGame.getHistory().size() != 0)
		{
			PlayNetGamePanel.mNetMove = mGame.moveToFakeMove(mGame.getHistory().get(mGame.getHistory().size() - 1));
		}
		else if (result != Result.DRAW)
		{
			JOptionPane.showMessageDialog(null, Messages.getString("PlayGamePanel.noMovesMade"), //$NON-NLS-1$
					Messages.getString("PlayGamePanel.timeRanOut"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
			PlayNetGamePanel.mIsRunning = false;
			Driver.getInstance().revertToMainPanel();
			Driver.getInstance().setFileMenuVisibility(true);
			return;
		}

		Object[] options = new String[] {
				Messages.getString("PlayGamePanel.saveRecord"), Messages.getString("PlayGamePanel.newGame"), Messages.getString("PlayGamePanel.quit") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		mOptionsMenu.setVisible(false);
		switch (JOptionPane.showOptionDialog(Driver.getInstance(), result.getGUIText(), result.winText(),
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]))
		{
		case JOptionPane.YES_OPTION:

			m_preference = PreferenceUtility.getPreference();

			if (!m_preference.isPathSet())
			{
				JOptionPane
						.showMessageDialog(
								Driver.getInstance(),
								Messages.getString("PlayGamePanel.sinceFirstTime") + AppConstants.APP_NAME //$NON-NLS-1$
										+ Messages.getString("PlayGamePanel.pleaseChooseDefault") //$NON-NLS-1$
										+ Messages.getString("PlayGamePanel.pressingCancel"), Messages.getString("PlayGamePanel.saveLocation"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fileChooser.showOpenDialog(Driver.getInstance());
				if (returnVal == JFileChooser.APPROVE_OPTION)
					m_preference.setSaveLocation(fileChooser.getSelectedFile().getAbsolutePath());
				else
					m_preference.setSaveLocation(FileUtility.getDefaultCompletedLocation());
			}

			String saveFileName = JOptionPane.showInputDialog(Driver.getInstance(),
					Messages.getString("PlayGamePanel.enterAName"), Messages.getString("PlayGamePanel.saving"), //$NON-NLS-1$ //$NON-NLS-2$
					JOptionPane.PLAIN_MESSAGE);
			getGame().saveGame(saveFileName, getGame().isClassicChess(), false);
			mGame.setBlackMove(false);
			Driver.getInstance().setFileMenuVisibility(true);
			Driver.getInstance().revertToMainPanel();
			break;
		case JOptionPane.NO_OPTION:
			mGame.setBlackMove(false);
			Driver.getInstance().setUpNewGame();
			break;
		case JOptionPane.CANCEL_OPTION:
			mGame.setBlackMove(false);
			System.exit(0);
			break;
		}
	}

	public void saveGame()
	{
		String fileName = JOptionPane.showInputDialog(Driver.getInstance(),
				Messages.getString("PlayGamePanel.enterAName"), Messages.getString("PlayGamePanel.saving"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
		if (fileName == null)
			return;
		getGame().saveGame(fileName, false, true);
	}

	public void turn(boolean isBlackTurn)
	{
		if (mWhiteTimer != null && mBlackTimer != null)
		{
			(!isBlackTurn ? mWhiteTimer : mBlackTimer).startTimer();
			(isBlackTurn ? mWhiteTimer : mBlackTimer).stopTimer();
		}
	}

	private JPanel createGrid(Board board, boolean isJail)
	{
		final JPanel gridPanel = new JPanel();
		gridPanel.setOpaque(false);
		gridPanel.setLayout(new GridLayout(board.numRows() + 1, board.numCols()));
		gridPanel.setPreferredSize(new Dimension((board.numCols() + 1) * 48, (board.numRows() + 1) * 48));

		int numberOfRows = board.numRows();
		int numOfColumns = board.numCols();
		for (int i = numberOfRows; i > 0; i--)
		{
			if (!isJail)
			{
				JLabel label = GuiUtility.createJLabel("" + i); //$NON-NLS-1$
				label.setHorizontalAlignment(SwingConstants.CENTER);
				gridPanel.add(label);
			}

			for (int j = 1; j <= numOfColumns; j++)
			{
				Square square = board.getSquare(i, j);
				if (!isJail)
				{
					square.addMouseListener(new SquareListener(square, board));
					square.addMouseMotionListener(new MotionAdapter(m_globalGlassPane));
				}

				gridPanel.add(square);
			}

		}
		if (!isJail)
		{
			for (int k = 0; k <= numOfColumns; k++)
			{
				if (k != 0)
				{
					JLabel label = GuiUtility.createJLabel("" + (char) (k - 1 + 'A')); //$NON-NLS-1$
					label.setHorizontalAlignment(SwingConstants.CENTER);
					gridPanel.add(label);
				}
				else
				{
					gridPanel.add(GuiUtility.createJLabel("")); //$NON-NLS-1$
				}
			}
		}
		return gridPanel;
	}

	public JMenu createMenuBar()
	{
		mOptionsMenu = new JMenu(Messages.getString("PlayGamePanel.menu")); //$NON-NLS-1$

		JMenuItem drawMenuItem = new JMenuItem(Messages.getString("PlayGamePanel.declareDraw"), KeyEvent.VK_D); //$NON-NLS-1$
		JMenuItem saveMenuItem = new JMenuItem(Messages.getString("PlayGamePanel.saveAndQuit"), KeyEvent.VK_S); //$NON-NLS-1$

		drawMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (getGame().getLastMove() == null)
					return;

				mOptionsMenu.setVisible(false);
				Result result = Result.DRAW;
				result.setGuiText(Messages.getString("PlayGamePanel.drawWhatNow")); //$NON-NLS-1$
				getGame().getLastMove().setResult(result);
				endOfGame(result);
			}
		});

		saveMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				mWhiteTimer.stopTimer();
				mBlackTimer.stopTimer();
				saveGame();

				mOptionsMenu.setVisible(false);
				Driver.getInstance().revertToMainPanel();
			}
		});

		mOptionsMenu.add(drawMenuItem);
		mOptionsMenu.add(saveMenuItem);

		return mOptionsMenu;
	}

	private void initComponents() throws Exception
	{
		mInCheckLabel = GuiUtility.createJLabel(Messages.getString("PlayGamePanel.youreInCheck")); //$NON-NLS-1$
		mInCheckLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		mInCheckLabel.setForeground(Color.RED);

		JButton undoButton = new JButton(Messages.getString("PlayGamePanel.undo")); //$NON-NLS-1$
		undoButton.addActionListener(new ActionListener()
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
		if (mOptionsMenu == null || !mOptionsMenu.isVisible())
			Driver.getInstance().setMenu(createMenuBar());

		Driver.getInstance().setOptionsMenuVisibility(true);

		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		final Board[] boards = getGame().getBoards();
		setBorder(BorderFactory.createLoweredBevelBorder());

		mInCheckLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		mInCheckLabel.setHorizontalAlignment(SwingConstants.CENTER);
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridy = 0;
		constraints.gridx = 9;
		mInCheckLabel.setVisible(false);
		add(mInCheckLabel, constraints);

		if (boards.length == 1)
		{
			constraints.gridheight = 12;
			constraints.gridy = 0;
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.gridwidth = 10;
			constraints.gridheight = 10;
			constraints.insets = new Insets(10, 0, 0, 0);
			constraints.gridx = 0;

			add(createGrid(boards[0], false), constraints);
		}
		else
		{
			constraints.gridheight = 12;
			constraints.gridy = 0;
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.gridwidth = 10;
			constraints.insets = new Insets(10, 0, 0, 0);
			constraints.gridx = 0;

			add(createGrid(boards[0], false), constraints);

			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.gridwidth = 10;
			constraints.insets = new Insets(10, 0, 0, 0);
			constraints.gridx = 11;
			add(createGrid(boards[1], false), constraints);

			twoBoardsGridBagOffset += 10;
		}

		mWhiteLabel = GuiUtility.createJLabel(Messages.getString("PlayGamePanel.whiteCaps")); //$NON-NLS-1$
		mWhiteLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mWhiteLabel.setBorder(BorderFactory.createTitledBorder("")); //$NON-NLS-1$

		mBlackLabel = GuiUtility.createJLabel(Messages.getString("PlayGamePanel.blackCaps")); //$NON-NLS-1$
		mBlackLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mBlackLabel.setBorder(BorderFactory.createTitledBorder("")); //$NON-NLS-1$

		mWhiteLabel.setOpaque(true);
		mBlackLabel.setOpaque(true);
		mWhiteLabel.setVisible(true);
		mBlackLabel.setVisible(true);

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

		mWhiteCapturesJail = new Board(jailBoardSize, jailBoardSize, false);
		mWhiteCapturePanel = createGrid(mWhiteCapturesJail, true);
		mWhiteCapturePanel.setBorder(BorderFactory.createTitledBorder("<html><font color=#FFFFFF>" + Messages.getString("PlayGamePanel.capturedPieces") + "</font></html>")); //$NON-NLS-1$
		mWhiteCapturePanel.setLayout(new GridLayout(jailBoardSize, jailBoardSize));

		mWhiteCapturePanel.setPreferredSize(new Dimension((mWhiteCapturesJail.getMaxCol() + 1) * 25,
				(mWhiteCapturesJail.getMaxRow() + 1) * 25));

		mBlackCapturesJail = new Board(jailBoardSize, jailBoardSize, false);
		mBlackCapturePanel = createGrid(mBlackCapturesJail, true);
		mBlackCapturePanel.setBorder(BorderFactory.createTitledBorder("<html><font color=#FFFFFF>" + Messages.getString("PlayGamePanel.capturedPieces") + "</font></html>")); //$NON-NLS-1$
		mBlackCapturePanel.setLayout(new GridLayout(jailBoardSize, jailBoardSize));

		mBlackCapturePanel.setPreferredSize(new Dimension((mBlackCapturesJail.getMaxCol() + 1) * 25,
				(mBlackCapturesJail.getMaxRow() + 1) * 25));

		// add the Black Name
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.BASELINE;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.insets = new Insets(10, 0, 10, 0);
		constraints.ipadx = 100;
		constraints.gridx = 11 + twoBoardsGridBagOffset;
		constraints.gridy = 0;
		add(mBlackLabel, constraints);

		// add the Black Jail
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.BASELINE;
		constraints.gridwidth = 3;
		constraints.gridheight = 3;
		constraints.ipadx = 0;
		constraints.insets = new Insets(0, 25, 10, 25);
		constraints.gridx = 11 + twoBoardsGridBagOffset;
		constraints.gridy = 1;
		add(mBlackCapturePanel, constraints);

		// add the Black timer
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.BASELINE;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.ipadx = 100;
		constraints.gridx = 11 + twoBoardsGridBagOffset;
		constraints.gridy = 4;
		add(mBlackTimer.getDisplayLabel(), constraints);

		// adds the undo button
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.BASELINE;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.ipadx = 100;
		constraints.gridx = 11 + twoBoardsGridBagOffset;
		constraints.gridy = 5;
		add(undoButton, constraints);

		// adds the White timer
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.BASELINE;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.ipadx = 100;
		constraints.gridx = 11 + twoBoardsGridBagOffset;
		constraints.gridy = 6;
		add(mWhiteTimer.getDisplayLabel(), constraints);

		// adds the White Jail
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.BASELINE;
		constraints.gridwidth = 3;
		constraints.gridheight = 3;
		constraints.ipadx = 0;
		constraints.gridx = 11 + twoBoardsGridBagOffset;

		// change spacing and location if there is a timer or not.
		if (ChessTimer.isNoTimer(mWhiteTimer))
		{
			constraints.gridy = 6;
			constraints.insets = new Insets(10, 25, 0, 25);
		}
		else
		{
			constraints.gridy = 7;
			constraints.insets = new Insets(0, 25, 0, 25);
		}
		add(mWhiteCapturePanel, constraints);

		// add the White Name
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.BASELINE;
		constraints.gridwidth = 3;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.insets = new Insets(10, 0, 10, 0);

		// change spacing if there is a timer
		if (ChessTimer.isNoTimer(mWhiteTimer))
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
		add(mWhiteLabel, constraints);
	}

	public void setNextMoveMustPlacePiece(boolean nextMoveMustPlacePiece)
	{
		mNextMoveMustPlacePiece = nextMoveMustPlacePiece;
	}

	public boolean getNextMoveMustPlacePiece()
	{
		return mNextMoveMustPlacePiece;
	}

	public void setPieceToPlace(Piece piece)
	{
		mPieceToPlace = piece;
	}

	public static void setGame(Game game)
	{
		mGame = game;
	}

	public static Game getGame()
	{
		return mGame;
	}

	public void resetTimers()
	{
		mWhiteTimer.reset();
		mBlackTimer.reset();
	}

	protected class SquareListener extends DropAdapter implements MouseListener, PieceToolTipPreferenceChangedListener
	{
		public SquareListener(Square square, Board board)
		{
			super(m_globalGlassPane);
			m_square = square;
			m_board = board;
			addDropListener(m_dropManager);
			if (PreferenceUtility.getPreference().showPieceToolTips() && m_square.isOccupied())
				m_square.setToolTipText(m_square.getPiece().getToolTipText());
			else
				m_square.setToolTipText(null);
			PreferenceUtility.addPieceToolTipListener(this);
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
			// if (m_nextMoveMustPlacePiece)
			// {
			// m_nextMoveMustPlacePiece = false;
			// getGame().nextTurn();
			// if (!m_clickedSquare.isOccupied() &&
			// m_clickedSquare.isHabitable() && m_pieceToPlace != null)
			// {
			// m_pieceToPlace.setSquare(m_clickedSquare);
			// m_clickedSquare.setPiece(m_pieceToPlace);
			// m_pieceToPlace = null;
			// m_nextMoveMustPlacePiece = false;
			// boardRefresh(getGame().getBoards());
			// getGame().genLegalDests();
			// }
			//
			// return;
			// }

			if (m_square.getPiece() == null || m_square.getPiece().isBlack() != getGame().isBlackMove())
			{
				return;
			}

			List<Square> destinations = m_square.getPiece().getLegalDests();
			if (destinations.size() > 0)
			{
				m_preference = PreferenceUtility.getPreference();
				if (m_preference.isHighlightMoves())
				{
					for (Square destination : destinations)
					{
						destination.setBackgroundColor(Square.HIGHLIGHT_COLOR);
					}
				}
			}
			m_dropManager.setComponentList(destinations);
			m_dropManager.setBoard(m_board);

			if (m_square.getPiece() == null)
				return;
			else
				m_square.hideIcon();

			Driver.getInstance().setGlassPane(mGlassPane);
			Component component = event.getComponent();

			mGlassPane.setVisible(true);

			Point point = (Point) event.getPoint().clone();
			SwingUtilities.convertPointToScreen(point, component);
			SwingUtilities.convertPointFromScreen(point, mGlassPane);

			mGlassPane.setPoint(point);

			BufferedImage image = null;
			ImageIcon imageIcon = m_square.getPiece().getIcon();
			int width = imageIcon.getIconWidth();
			int height = imageIcon.getIconHeight();
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D graphics2D = (Graphics2D) image.getGraphics();
			imageIcon.paintIcon(null, graphics2D, 0, 0);
			graphics2D.dispose();

			mGlassPane.setImage(image);
			mGlassPane.repaint();
		}

		@Override
		public void mouseReleased(MouseEvent event)
		{
			Point point = (Point) event.getPoint().clone();
			SwingUtilities.convertPointToScreen(point, event.getComponent());

			mGlassPane.setImage(null);
			mGlassPane.setVisible(false);

			fireDropEvent(new DropEvent(point, m_square), false);
		}

		private Square m_square;
		private Board m_board;

		@Override
		public void onPieceToolTipPreferenceChanged()
		{
			if (PreferenceUtility.getPreference().showPieceToolTips() && m_square.isOccupied())
				m_square.setToolTipText(m_square.getPiece().getToolTipText());
			else
				m_square.setToolTipText(null);
		}
	}

	private static class DropManager extends AbstractDropManager
	{
		public void setBoard(Board board)
		{
			m_board = board;
		}

		@Override
		public void dropped(DropEvent event, boolean fromDisplayBoard)
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
				s_instance.boardRefresh(getGame().getBoards());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		private Board m_board;
	};

	private static final long serialVersionUID = -2507232401817253688L;

	protected static boolean mNextMoveMustPlacePiece;
	protected static Game mGame;
	protected static ChessTimer mWhiteTimer;
	protected static ChessTimer mBlackTimer;
	protected static JLabel mInCheckLabel;
	protected static JLabel mWhiteLabel;
	protected static JLabel mBlackLabel;
	protected static JPanel mWhiteCapturePanel;
	protected static JPanel mBlackCapturePanel;
	protected static Board mWhiteCapturesJail;
	protected static Board mBlackCapturesJail;
	protected static Piece mPieceToPlace;
	protected static JMenu mOptionsMenu;
	protected static Move[] mHistory;
	protected static int mHistoryIndex;

	private Preference m_preference;

	private final DropManager m_dropManager;

	protected GlassPane m_globalGlassPane;

	private static PlayGamePanel s_instance;
}
