package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import logic.Board;
import logic.Game;
import logic.Move;
import logic.Result;
import logic.Square;
import timer.NoTimer;
import ai.FakeMove;

public class PlayNetGame extends PlayGame
{
	public PlayNetGame(Game game, boolean isPlayback, boolean isBlack) throws Exception
	{
		super(game, isPlayback);
		PlayNetGame.m_isBlackPlayer = isBlack;
		initGUIComponents(isPlayback);
	}

	private void initGUIComponents(boolean isPlayback)
	{
		super.removeAll();
		m_optionsMenu.setVisible(false);

		Driver.getInstance().m_fileMenu.setVisible(false);

		m_inCheckLabel = new JLabel("You're In Check!");
		m_inCheckLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		m_inCheckLabel.setForeground(Color.RED);

		int twoBoardGridBagOffset = 0;
		Driver.getInstance().setMenu(createMenuBar());

		Driver.m_gameOptionsMenu.setVisible(true);

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

			add(createGrid(boards[0], isPlayback), constraints);
		}
		else
		{
			constraints.gridheight = 12;
			constraints.gridy = 2;
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.gridwidth = 10;
			constraints.insets = new Insets(10, 0, 0, 0);
			constraints.gridx = 0;

			add(createGrid(boards[0], isPlayback), constraints);

			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.gridwidth = 10;
			constraints.insets = new Insets(10, 0, 0, 0);
			constraints.gridx = 11;
			add(createGrid(boards[1], isPlayback), constraints);

			twoBoardGridBagOffset += 10;
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

		m_whiteLabel.setBackground(getGame().isBlackMove() ? null : Square.HIGHLIGHT_COLOR);

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

		m_whiteCapturePanel = new JPanel();
		m_whiteCapturePanel.setBorder(BorderFactory.createTitledBorder("Captured Pieces"));
		m_whiteCapturesJail = new Jail(jailBoardSize, jailBoardSize);
		m_whiteCapturePanel.setLayout(new GridLayout(jailBoardSize, jailBoardSize));
		m_whiteCapturePanel.setPreferredSize(new Dimension((m_whiteCapturesJail.getMaxColumn() + 1) * 25, (m_whiteCapturesJail
				.getMaxRow() + 1) * 25));
		for (int i = jailBoardSize; i > 0; i--)
		{
			for (int j = 1; j <= jailBoardSize; j++)
				m_whiteCapturePanel.add(new Square(i, j));
		}

		m_blackCapturePanel = new JPanel();
		m_blackCapturePanel.setBorder(BorderFactory.createTitledBorder("Captured Pieces"));
		m_blackCapturesJail = new Jail(jailBoardSize, jailBoardSize);
		m_blackCapturePanel.setLayout(new GridLayout(jailBoardSize, jailBoardSize));
		m_blackCapturePanel.setPreferredSize(new Dimension((m_blackCapturesJail.getMaxColumn() + 1) * 25, (m_blackCapturesJail
				.getMaxRow() + 1) * 25));
		for (int i = jailBoardSize; i > 0; i--)
		{
			for (int j = 1; j <= jailBoardSize; j++)
				m_blackCapturePanel.add(new Square(i, j));
		}

		// add the Black name
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.BASELINE;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.insets = new Insets(10, 10, 10, 0);
		constraints.ipadx = 100;
		constraints.gridx = 11 + twoBoardGridBagOffset;
		constraints.gridy = 0;
		add(m_blackLabel, constraints);

		// add the Black jail
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.BASELINE;
		constraints.gridwidth = 3;
		constraints.gridheight = 3;
		constraints.ipadx = 0;
		constraints.insets = new Insets(0, 25, 10, 25);
		constraints.gridx = 11 + twoBoardGridBagOffset;
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
			constraints.gridx = 11 + twoBoardGridBagOffset;
			constraints.gridy = 4;
			add(m_blackTimer.getDisplayLabel(), constraints);

			// add the White timer
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.anchor = GridBagConstraints.BASELINE;
			constraints.gridwidth = 3;
			constraints.gridheight = 1;
			constraints.ipadx = 100;
			constraints.gridx = 11 + twoBoardGridBagOffset;
			constraints.gridy = 6;
			add(m_whiteTimer.getDisplayLabel(), constraints);
		}
		else
		{
			// add the Black timer
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.anchor = GridBagConstraints.BASELINE;
			constraints.gridwidth = 3;
			constraints.gridheight = 1;
			constraints.ipadx = 100;
			constraints.gridx = 11 + twoBoardGridBagOffset;
			constraints.gridy = 4;
			add(nextButton, constraints);

			// add the White timer
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.anchor = GridBagConstraints.BASELINE;
			constraints.gridwidth = 3;
			constraints.gridheight = 1;
			constraints.ipadx = 100;
			constraints.gridx = 11 + twoBoardGridBagOffset;
			constraints.gridy = 5;
			add(prevButton, constraints);
		}

		// add the White Jail
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.BASELINE;
		constraints.gridwidth = 3;
		constraints.gridheight = 3;
		constraints.ipadx = 0;
		constraints.gridx = 11 + twoBoardGridBagOffset;
		// change spacing and location if there is a timer or not.
		if (m_whiteTimer instanceof NoTimer)
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
		if (m_whiteTimer instanceof NoTimer)
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
		constraints.gridx = 11 + twoBoardGridBagOffset;
		add(m_whiteLabel, constraints);
	}

	public static boolean isBlackPlayer()
	{
		return m_isBlackPlayer;
	}

	class ButtonListener implements MouseListener
	{
		public ButtonListener(Square square, Board board)
		{
			m_clickedSquare = square;
			m_board = board;
		}

		@Override
		public void mouseClicked(MouseEvent event)
		{
			if (getGame().isBlackMove() == m_isBlackPlayer)
			{
				if (m_nextMoveMustPlacePiece)
				{
					m_nextMoveMustPlacePiece = false;
					getGame().nextTurn();
					if (!m_clickedSquare.isOccupied() && m_clickedSquare.isHabitable() && m_pieceToPlace != null)
					{
						m_pieceToPlace.setSquare(m_clickedSquare);
						m_clickedSquare.setPiece(m_pieceToPlace);
						m_pieceToPlace = null;
						m_nextMoveMustPlacePiece = false;
						boardRefresh(getGame().getBoards());
						getGame().genLegalDests();
					}

					return;
				}
				if (m_mustMove && m_clickedSquare == m_storedSquare)
				{
					boardRefresh(getGame().getBoards());
					m_mustMove = false;
				}
				else if (m_mustMove && m_clickedSquare.getColor() == Square.HIGHLIGHT_COLOR)
				{
					try
					{
						Move move = new Move(m_board, m_storedSquare, m_clickedSquare);
						getGame().playMove(move);

						m_netMove = m_game.moveToFakeMove(move);

						m_mustMove = false;
						boardRefresh(getGame().getBoards());
					}
					catch (Exception e)
					{
						System.out.println(e.getMessage());
						e.printStackTrace();
					}
				}
				else if (!m_mustMove && m_clickedSquare.getPiece() != null
						&& m_clickedSquare.getPiece().isBlack() == getGame().isBlackMove())
				{
					List<Square> destinationList = m_clickedSquare.getPiece().getLegalDests();
					if (destinationList.size() > 0)
					{
						for (Square destination : destinationList)
							destination.setBackgroundColor(Square.HIGHLIGHT_COLOR);

						m_storedSquare = m_clickedSquare;
						m_mustMove = true;
					}
				}
			}
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
		}

		@Override
		public void mouseReleased(MouseEvent event)
		{
		}

		private Square m_clickedSquare;
		private Board m_board;
	}

	private JPanel createGrid(Board board, boolean isPlayback)
	{
		final JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(board.numRows() + 1, board.numCols()));
		grid.setPreferredSize(new Dimension((board.numCols() + 1) * 48, (board.numRows() + 1) * 48));

		int numberOfRows = board.numRows();
		int numberOfColumns = board.numCols();
		for (int i = numberOfRows; i > 0; i--)
		{
			JLabel label = new JLabel("" + i);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			grid.add(label);
			for (int j = 1; j <= numberOfColumns; j++)
			{
				if (!isPlayback)
					board.getSquare(i, j).addMouseListener(new ButtonListener(board.getSquare(i, j), board));
				grid.add(board.getSquare(i, j));
			}
		}
		for (int k = 0; k <= numberOfColumns; k++)
		{
			if (k != 0)
			{
				JLabel label = new JLabel("" + (char) (k - 1 + 'A'));
				label.setHorizontalAlignment(SwingConstants.CENTER);
				grid.add(label);
			}
			else
			{
				grid.add(new JLabel(""));
			}
		}
		return grid;
	}

	@Override
	public JMenu createMenuBar()
	{
		m_optionsMenu = new JMenu("Menu");
		if (!m_isPlayback)
		{
			m_drawMenuItem = new JMenuItem("Request Draw", KeyEvent.VK_R);
			m_drawMenuItem.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent event)
				{
					if (getGame().isBlackMove() == m_isBlackPlayer)
					{
						if (m_isAIGame)
						{
							if (requestAIDraw())
								return;

							Result result = Result.DRAW;
							result.setText("You have declared a draw. What would you like to do?");
							PlayGame.endOfGame(result);
						}
						else
						{
							if (requestDraw())
								return;

							// send move indicating surrender request
							m_netMove = new FakeMove(-1, -1, -1, -1, -1, null);
						}
					}
				}
			});

			m_optionsMenu.add(m_drawMenuItem);
		}

		return m_optionsMenu;
	}

	private boolean requestDraw()
	{
		int result = JOptionPane.showConfirmDialog(null, "Would you like to send the other player a draw request?", "Draw",
				JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
		return result == JOptionPane.YES_OPTION;
	}

	private boolean requestAIDraw()
	{
		int result = JOptionPane.showConfirmDialog(null, "Are you sure you would like to declare a draw?", "Draw",
				JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
		return result == JOptionPane.YES_OPTION;
	}

	public void setIsAIGame(boolean isAIGame)
	{
		m_isAIGame = isAIGame;
		Driver.getInstance().m_fileMenu.setVisible(true);
		m_drawMenuItem.setText("Declare Draw");
	}

	private static final long serialVersionUID = -4220208356045682711L;

	private static boolean m_isBlackPlayer;
	public static boolean m_isRunning = true;
	public static FakeMove m_netMove = null;

	private boolean m_isAIGame;
	public boolean m_drawRequested = false;
}
