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
import logic.Result;
import logic.Square;
import timer.ChessTimer;
import utility.GuiUtility;
import ai.FakeMove;

public class PlayNetGamePanel extends PlayGamePanel implements PlayNetGameScreen
{
	public PlayNetGamePanel(Game game, boolean isPlayback, boolean isBlack) throws Exception
	{
		super(game);
		PlayNetGamePanel.mIsBlackPlayer = isBlack;
		initGUIComponents(isPlayback);
	}

	private void initGUIComponents(boolean isPlayback)
	{
		super.removeAll();
		mOptionsMenu.setVisible(false);

		Driver.getInstance().setFileMenuVisibility(false);

		mInCheckLabel = new JLabel(Messages.getString("PlayNetGamePanel.youreInCheck")); //$NON-NLS-1$
		mInCheckLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		mInCheckLabel.setForeground(Color.RED);

		int twoBoardGridBagOffset = 0;
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

		JButton nextButton = new JButton(Messages.getString("PlayNetGamePanel.next")); //$NON-NLS-1$
		nextButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (mHistoryIndex + 1 == mHistory.length)
					return;

				try
				{
					mHistory[++mHistoryIndex].execute();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

		JButton prevButton = new JButton(Messages.getString("PlayNetGamePanel.previous")); //$NON-NLS-1$
		prevButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (mHistoryIndex == -1)
					return;

				try
				{
					mHistory[mHistoryIndex--].undo();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

		mWhiteLabel = new JLabel(Messages.getString("PlayNetGamePanel.whiteCaps")); //$NON-NLS-1$
		mWhiteLabel.setHorizontalAlignment(SwingConstants.CENTER);

		mWhiteLabel.setBorder(BorderFactory.createTitledBorder("")); //$NON-NLS-1$

		mBlackLabel = new JLabel(Messages.getString("PlayNetGamePanel.blackCaps")); //$NON-NLS-1$
		mBlackLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mBlackLabel.setBorder(BorderFactory.createTitledBorder("")); //$NON-NLS-1$

		mWhiteLabel.setOpaque(true);
		mBlackLabel.setOpaque(true);

		mWhiteLabel.setBackground(getGame().isBlackMove() ? null : SquareJLabel.HIGHLIGHT_COLOR);

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

		mWhiteCapturePanel = new JPanel();
		mWhiteCapturePanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("PlayNetGamePanel.capturedPieces"))); //$NON-NLS-1$
		mWhiteCapturesJail = new Board(jailBoardSize, jailBoardSize, isPlayback);
		mWhiteCapturePanel.setLayout(new GridLayout(jailBoardSize, jailBoardSize));
		mWhiteCapturePanel.setPreferredSize(new Dimension((mWhiteCapturesJail.getMaxCol() + 1) * 25,
				(mWhiteCapturesJail.getMaxRow() + 1) * 25));
		for (int i = jailBoardSize; i > 0; i--)
		{
			for (int j = 1; j <= jailBoardSize; j++)
				mWhiteCapturePanel.add(new SquareJLabel(new Square(i, j)));
		}

		mBlackCapturePanel = new JPanel();
		mBlackCapturePanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("PlayNetGamePanel.captuedPieces"))); //$NON-NLS-1$
		mBlackCapturesJail = new Board(jailBoardSize, jailBoardSize, isPlayback);
		mBlackCapturePanel.setLayout(new GridLayout(jailBoardSize, jailBoardSize));
		mBlackCapturePanel.setPreferredSize(new Dimension((mBlackCapturesJail.getMaxCol() + 1) * 25,
				(mBlackCapturesJail.getMaxRow() + 1) * 25));
		for (int i = jailBoardSize; i > 0; i--)
		{
			for (int j = 1; j <= jailBoardSize; j++)
				mBlackCapturePanel.add(new SquareJLabel(new Square(i, j)));
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
		add(mBlackLabel, constraints);

		// add the Black jail
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.BASELINE;
		constraints.gridwidth = 3;
		constraints.gridheight = 3;
		constraints.ipadx = 0;
		constraints.insets = new Insets(0, 25, 10, 25);
		constraints.gridx = 11 + twoBoardGridBagOffset;
		constraints.gridy = 1;
		add(mBlackCapturePanel, constraints);

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
			add(mBlackTimer.getDisplayLabel(), constraints);

			// add the White timer
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.anchor = GridBagConstraints.BASELINE;
			constraints.gridwidth = 3;
			constraints.gridheight = 1;
			constraints.ipadx = 100;
			constraints.gridx = 11 + twoBoardGridBagOffset;
			constraints.gridy = 6;
			add(mWhiteTimer.getDisplayLabel(), constraints);
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
		constraints.gridx = 11 + twoBoardGridBagOffset;
		add(mWhiteLabel, constraints);
	}

	@Override
	public boolean isBlackPlayer()
	{
		return mIsBlackPlayer;
	}

	// class ButtonListener implements MouseListener
	// {
	// public ButtonListener(Square square, Board board)
	// {
	// m_clickedSquare = square;
	// m_board = board;
	// }
	//
	// @Override
	// public void mouseClicked(MouseEvent event)
	// {
	// if (getGame().isBlackMove() == m_isBlackPlayer)
	// {
	// if (m_nextMoveMustPlacePiece)
	// {
	// m_nextMoveMustPlacePiece = false;
	// getGame().nextTurn();
	// if (!m_clickedSquare.isOccupied() && m_clickedSquare.isHabitable() &&
	// m_pieceToPlace != null)
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
	// if (m_mustMove && m_clickedSquare == m_storedSquare)
	// {
	// boardRefresh(getGame().getBoards());
	// m_mustMove = false;
	// }
	// else if (m_mustMove && m_clickedSquare.getColor() ==
	// Square.HIGHLIGHT_COLOR)
	// {
	// try
	// {
	// Move move = new Move(m_board, m_storedSquare, m_clickedSquare);
	// getGame().playMove(move);
	//
	// m_netMove = m_game.moveToFakeMove(move);
	//
	// m_mustMove = false;
	// boardRefresh(getGame().getBoards());
	// }
	// catch (Exception e)
	// {
	// System.out.println(e.getMessage());
	// e.printStackTrace();
	// }
	// }
	// else if (!m_mustMove && m_clickedSquare.getPiece() != null
	// && m_clickedSquare.getPiece().isBlack() == getGame().isBlackMove())
	// {
	// List<Square> destinationList =
	// m_clickedSquare.getPiece().getLegalDests();
	// if (destinationList.size() > 0)
	// {
	// for (Square destination : destinationList)
	// destination.setBackgroundColor(Square.HIGHLIGHT_COLOR);
	//
	// m_storedSquare = m_clickedSquare;
	// m_mustMove = true;
	// }
	// }
	// }
	// }
	//
	// @Override
	// public void mouseEntered(MouseEvent event)
	// {
	// }
	//
	// @Override
	// public void mouseExited(MouseEvent event)
	// {
	// }
	//
	// @Override
	// public void mousePressed(MouseEvent event)
	// {
	// }
	//
	// @Override
	// public void mouseReleased(MouseEvent event)
	// {
	// }
	//
	// private Square m_clickedSquare;
	// private Square m_storedSquare;
	// private Board m_board;
	// }

	private JPanel createGrid(Board board, boolean isPlayback)
	{
		final JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(board.numRows() + 1, board.numCols()));
		grid.setPreferredSize(new Dimension((board.numCols() + 1) * 48, (board.numRows() + 1) * 48));

		int numberOfRows = board.numRows();
		int numberOfColumns = board.numCols();
		for (int i = numberOfRows; i > 0; i--)
		{
			JLabel label = new JLabel("" + i); //$NON-NLS-1$
			label.setHorizontalAlignment(SwingConstants.CENTER);
			grid.add(label);
			for (int j = 1; j <= numberOfColumns; j++)
			{
				SquareJLabel squareLabel = new SquareJLabel(board.getSquare(i, j));
				if (!isPlayback)
					squareLabel.addMouseListener(new SquareListener(squareLabel, board));
				grid.add(squareLabel);
			}
		}
		for (int k = 0; k <= numberOfColumns; k++)
		{
			if (k != 0)
			{
				JLabel label = new JLabel("" + (char) (k - 1 + 'A')); //$NON-NLS-1$
				label.setHorizontalAlignment(SwingConstants.CENTER);
				grid.add(label);
			}
			else
			{
				grid.add(new JLabel("")); //$NON-NLS-1$
			}
		}
		return grid;
	}

	@Override
	public JMenu createMenuBar()
	{
		mOptionsMenu = new JMenu(Messages.getString("PlayNetGamePanel.menu")); //$NON-NLS-1$
		JMenuItem m_drawMenuItem = new JMenuItem(Messages.getString("PlayNetGamePanel.requestDraw"), KeyEvent.VK_R); //$NON-NLS-1$
		m_drawMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (getGame().isBlackMove() == mIsBlackPlayer)
				{
					if (mIsAIGame)
					{
						if (requestAIDraw())
							return;

						Result result = Result.DRAW;
						result.setGuiText(Messages.getString("PlayNetGamePanel.youDeclaredDraw")); //$NON-NLS-1$
						GuiUtility.getChessCrafter().getPlayGameScreen(getGame()).endOfGame(result);
					}
					else
					{
						if (requestDraw())
							return;

						// send move indicating surrender request
						mNetMove = new FakeMove(-1, -1, -1, -1, -1, null);
					}
				}
			}
		});

		if (mIsAIGame)
			m_drawMenuItem.setText(Messages.getString("PlayNetGamePanel.declareDraw")); //$NON-NLS-1$

		mOptionsMenu.add(m_drawMenuItem);

		return mOptionsMenu;
	}

	private boolean requestDraw()
	{
		int result = JOptionPane.showConfirmDialog(Driver.getInstance(),
				Messages.getString("PlayNetGamePanel.sendDrawRequest"), Messages.getString("PlayNetGamePanel.draw"), //$NON-NLS-1$ //$NON-NLS-2$
				JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
		return result == JOptionPane.YES_OPTION;
	}

	private boolean requestAIDraw()
	{
		int result = JOptionPane.showConfirmDialog(Driver.getInstance(),
				Messages.getString("PlayNetGamePanel.sureAboutDraw"), Messages.getString("PlayNetGamePanel.draw"), //$NON-NLS-1$ //$NON-NLS-2$
				JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
		return result == JOptionPane.YES_OPTION;
	}

	@Override
	public void setIsAIGame(boolean isAIGame)
	{
		mIsAIGame = isAIGame;
		Driver.getInstance().setFileMenuVisibility(true);
	}

	@Override
	public void setIsRunning(boolean isRunning)
	{
		mIsRunning = isRunning;
	}

	@Override
	public boolean isRunning()
	{
		return mIsRunning;
	}

	@Override
	public FakeMove getNetMove()
	{
		return mNetMove;
	}

	@Override
	public boolean drawRequested()
	{
		return mDrawRequested;
	}

	@Override
	public void setDrawRequested(boolean drawRequested)
	{
		mDrawRequested = drawRequested;
	}

	@Override
	public void setNetMove(FakeMove fakeMove)
	{
		mNetMove = fakeMove;
	}

	private static final long serialVersionUID = -4220208356045682711L;

	private static boolean mIsBlackPlayer;
	public static boolean mIsRunning = true;
	public static FakeMove mNetMove = null;

	private boolean mIsAIGame;
	public boolean mDrawRequested = false;

}
