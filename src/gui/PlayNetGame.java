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

/**
 * @author John McCormick
 * 
 * Class for playing network or AI games
 */
public class PlayNetGame extends PlayGame
{

	/**
	 * Generated Serial ID
	 */
	private static final long serialVersionUID = -4220208356045682711L;

	/**
	 * If the player is black
	 */
	private static boolean isBlack;

	/**
	 * If the game is an AIGame
	 */
	private boolean AIGame;

	/**
	 * If the game is running.
	 */
	public static boolean running = true;

	/**
	 * Move that is being made
	 */
	public static FakeMove netMove = null;

	/**
	 * If one play hits the 'draw' button to end game.
	 */
	public boolean drawRequested = false;

	/**
	 * @param g the game to be played
	 * @param isPlayback if this is a playback (always false)
	 * @param isBlack if the player is black
	 * @throws Exception Needed for dealing with super()
	 */
	public PlayNetGame(Game g, boolean isPlayback, boolean isBlack) throws Exception
	{
		super(g, isPlayback);
		PlayNetGame.isBlack = isBlack;
		initComponents(isPlayback);
	}

	/**
	 * Getter method for isBlack boolean
	 * 
	 * @return If the player of this game controls the white or black team
	 */
	public static boolean isBlack()
	{
		return isBlack;
	}

	/**
	 * Class for button listeners
	 */
	class ButtonListener implements MouseListener
	{

		/**
		 * The Square attached to this ButtonListener
		 */
		private Square clickedSquare;
		/**
		 * The board, for reference to everything else, that the game is on.
		 */
		private Board b;

		/**
		 * Constructor. Attaches a Square to this ButtonListener
		 * 
		 * @param s The Square which is attached to the ButtonListener.
		 * @param b The board that is being played on.
		 */
		public ButtonListener(Square s, Board b)
		{
			clickedSquare = s;
			this.b = b;
		}

		/**
		 * Control movement of pieces. Check if the Square is occupied and
		 * either highlight possible destinations or move the piece.
		 */
		@Override
		public void mouseClicked(MouseEvent arg0)
		{
			if (getGame().isBlackMove() == isBlack)
			{
				if (m_nextMoveMustPlacePiece)
				{
					m_nextMoveMustPlacePiece = false;
					getGame().nextTurn();
					if (!clickedSquare.isOccupied() && clickedSquare.isHabitable() && m_pieceToPlace != null)
					{
						m_pieceToPlace.setSquare(clickedSquare);
						clickedSquare.setPiece(m_pieceToPlace);
						m_pieceToPlace = null;
						m_nextMoveMustPlacePiece = false;
						boardRefresh(getGame().getBoards());
						getGame().genLegalDests();
					}

					return;
				}
				if (m_mustMove && clickedSquare == m_storedSquare)
				{
					boardRefresh(getGame().getBoards());
					m_mustMove = false;
				}
				else if (m_mustMove && clickedSquare.getColor() == Square.HIGHLIGHT_COLOR)
				{
					try
					{
						Move m = new Move(b, m_storedSquare, clickedSquare);
						getGame().playMove(m);

						netMove = m_game.moveToFakeMove(m);

						m_mustMove = false;
						boardRefresh(getGame().getBoards());
					}
					catch (Exception e1)
					{
						System.out.println(e1.getMessage());
						e1.printStackTrace();
					}
				}
				else if (!m_mustMove && clickedSquare.getPiece() != null
						&& clickedSquare.getPiece().isBlack() == getGame().isBlackMove())
				{
					List<Square> dests = clickedSquare.getPiece().getLegalDests();
					if (dests.size() > 0)
					{
						for (Square dest : dests)
						{
							dest.setBackgroundColor(Square.HIGHLIGHT_COLOR);
						}
						m_storedSquare = clickedSquare;
						m_mustMove = true;
					}
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0)
		{
		}

		@Override
		public void mouseExited(MouseEvent arg0)
		{
		}

		@Override
		public void mousePressed(MouseEvent arg0)
		{
		}

		@Override
		public void mouseReleased(MouseEvent arg0)
		{
		}
	}

	/**
	 * @param b The board that the game is being played on.
	 * @param isPlayback whether PlayGame is in playback mode
	 * @return the grid being created.
	 */
	private JPanel createGrid(Board b, boolean isPlayback)
	{

		final JPanel grid = new JPanel();
		// grid.setBorder(BorderFactory.createEtchedBorder());

		// Create a JPanel to hold the grid and set the layout to the number of
		// squares in the board.
		// final JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(b.numRows() + 1, b.numCols()));
		// Set the size of the grid to the number of rows and columns, scaled by
		// 48, the size of the images.
		grid.setPreferredSize(new Dimension((b.numCols() + 1) * 48, (b.numRows() + 1) * 48));

		// Loop through the board, initializing each Square and adding it's
		// ActionListener.
		int numRows = b.numRows();
		int numCols = b.numCols();
		for (int i = numRows; i > 0; i--)
		{
			JLabel temp = new JLabel("" + i);
			temp.setHorizontalAlignment(SwingConstants.CENTER);
			grid.add(temp);
			for (int j = 1; j <= numCols; j++)
			{

				// grid.add(new JLabel(""+(j-1+'a')));
				if (!isPlayback)
				{
					b.getSquare(i, j).addMouseListener(new ButtonListener(b.getSquare(i, j), b));
				}
				grid.add(b.getSquare(i, j));// Add the button to the grid.

			}

		}
		for (int k = 0; k <= numCols; k++)
		{
			if (k != 0)
			{
				JLabel temp = new JLabel("" + (char) (k - 1 + 'A'));
				temp.setHorizontalAlignment(SwingConstants.CENTER);
				grid.add(temp);

			}
			else
			{
				grid.add(new JLabel(""));
			}
		}
		return grid;
	}

	/**
	 * Setups up the window
	 * 
	 * @param isPlayback whether this is just a review or not
	 */
	@SuppressWarnings("static-access")
	private void initComponents(boolean isPlayback)
	{

		super.removeAll();
		m_optionsMenu.setVisible(false);

		Driver.getInstance().m_fileMenu.setVisible(false);

		// Has spaces to hax0r fix centering.
		m_inCheckLabel = new JLabel("You're In Check!");
		m_inCheckLabel.setHorizontalTextPosition(m_inCheckLabel.CENTER);
		m_inCheckLabel.setForeground(Color.RED);

		int ifDouble = 0;
		Driver.getInstance().setMenu(createMenuBar());

		Driver.m_gameOptionsMenu.setVisible(true); // Turns on the game options

		// Set the layout of the JPanel.
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		// Get the Board[] from the Game.
		final Board[] boards = getGame().getBoards();
		setBorder(BorderFactory.createLoweredBevelBorder());
		// Adds the grid

		// Adds the inCheck notification.
		m_inCheckLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		m_inCheckLabel.setHorizontalAlignment(SwingConstants.CENTER);
		c.fill = GridBagConstraints.NONE;
		c.gridy = 0;
		c.gridx = 9;
		m_inCheckLabel.setVisible(false);
		this.add(m_inCheckLabel, c);

		if (boards.length == 1)
		{
			c.gridheight = 12;
			c.gridy = 2;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = 10;
			c.gridheight = 10;
			// Insets(top,left,bottom,right) << This is to show how to format.
			// Insets are blank space outside of the object to buffer around it.
			c.insets = new Insets(10, 0, 0, 0);
			c.gridx = 0;

			this.add(createGrid(boards[0], isPlayback), c);
		}
		else
		{
			c.gridheight = 12;
			c.gridy = 2;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = 10;
			// Insets(top,left,bottom,right) << This is to show how to format.
			// Insets are blank space outside of the object to buffer around it.
			c.insets = new Insets(10, 0, 0, 0);
			c.gridx = 0;

			this.add(createGrid(boards[0], isPlayback), c);

			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = 10;
			// Insets(top,left,bottom,right) << This is to show how to format.
			// Insets are blank space outside of the object to buffer around it.
			c.insets = new Insets(10, 0, 0, 0);
			c.gridx = 11;
			this.add(createGrid(boards[1], isPlayback), c);

			ifDouble += 10;
		}

		JButton nextButt = new JButton("Next");
		nextButt.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (m_historyIndex + 1 == m_history.length)
					return;
				try
				{
					m_history[++m_historyIndex].execute();
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}
		});
		JButton prevButt = new JButton("Previous");
		prevButt.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (m_historyIndex == -1)
					return;
				try
				{
					m_history[m_historyIndex--].undo();
				}
				catch (Exception e1)
				{

				}
			}
		});

		// I made name1 (White) & name2 (Black) instance variables so that I can
		// highlight them
		// when it's their turn.

		m_whiteLabel = new JLabel("WHITE");
		m_whiteLabel.setHorizontalAlignment(SwingConstants.CENTER);

		m_whiteLabel.setBorder(BorderFactory.createTitledBorder(""));

		m_blackLabel = new JLabel("BLACK");
		m_blackLabel.setHorizontalAlignment(SwingConstants.CENTER);
		m_blackLabel.setBorder(BorderFactory.createTitledBorder(""));

		// Needed for highlighting the names when it's their turn.
		m_whiteLabel.setOpaque(true);
		m_blackLabel.setOpaque(true);

		m_whiteLabel.setBackground(getGame().isBlackMove() ? null : Square.HIGHLIGHT_COLOR);

		/**
		 * int to hold the size of the jail board.
		 */
		int k;

		/**
		 * This sets k to either the size of how many pieces white has or how
		 * many pieces black has. If neither team has any pieces then
		 */
		if (getGame().getWhiteTeam().size() <= 4 && getGame().getBlackTeam().size() <= 4)
		{
			k = 4;
		}
		else
		{
			double o = getGame().getWhiteTeam().size() > getGame().getBlackTeam().size() ? Math.sqrt(getGame().getWhiteTeam().size())
					: Math.sqrt(getGame().getBlackTeam().size());
			k = (int) Math.ceil(o);
		}

		/**
		 * Makes Black's jail
		 */
		m_whiteCapturePanel = new JPanel();
		m_whiteCapturePanel.setBorder(BorderFactory.createTitledBorder("Captured Pieces"));
		if (k < 4)
		{
			m_whiteCapturesJail = new Jail(4, 4);
			m_whiteCapturePanel.setLayout(new GridLayout(4, 4));
		}
		else
		{
			m_whiteCapturesJail = new Jail(k, k);
			m_whiteCapturePanel.setLayout(new GridLayout(k, k));
		}
		m_whiteCapturePanel.setPreferredSize(new Dimension((m_whiteCapturesJail.getMaxColumn() + 1) * 25, (m_whiteCapturesJail.getMaxRow() + 1) * 25));
		for (int i = k; i > 0; i--)
		{
			for (int j = 1; j <= k; j++)
			{
				Square square = new Square(i, j);
				m_whiteCapturePanel.add(square);
			}
		}

		/**
		 * Makes White's jail
		 */
		m_blackCapturePanel = new JPanel();
		m_blackCapturePanel.setBorder(BorderFactory.createTitledBorder("Captured Pieces"));
		if (k < 4)
		{
			m_blackCapturesJail = new Jail(4, 4);
			m_blackCapturePanel.setLayout(new GridLayout(4, 4));
		}
		else
		{
			m_blackCapturesJail = new Jail(k, k);
			m_blackCapturePanel.setLayout(new GridLayout(k, k));
		}
		m_blackCapturePanel.setPreferredSize(new Dimension((m_blackCapturesJail.getMaxColumn() + 1) * 25, (m_blackCapturesJail.getMaxRow() + 1) * 25));
		for (int i = k; i > 0; i--)
		{
			for (int j = 1; j <= k; j++)
			{
				Square square = new Square(i, j);
				m_blackCapturePanel.add(square);
			}
		}

		/*
		 * This is the section that adds all of the peripheral GUI components It
		 * adds them in the order that they are displayed from top to bottom.
		 * 
		 * This is for reference for editing Insets //
		 * Insets(top,left,bottom,right) << This is to show how to format. //
		 * Insets are blank space outside of the object to buffer around it.
		 */

		// Adds the Black Name
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.BASELINE;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.insets = new Insets(10, 10, 10, 0);
		c.ipadx = 100;
		c.gridx = 11 + ifDouble;
		c.gridy = 0;
		this.add(m_blackLabel, c);

		// Adds the Black Jail
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.BASELINE;
		c.gridwidth = 3;
		c.gridheight = 3;
		c.ipadx = 0;
		c.insets = new Insets(0, 25, 10, 25);
		c.gridx = 11 + ifDouble;
		c.gridy = 1;
		this.add(m_blackCapturePanel, c);

		// If it is playback then we do not want timers.
		if (!isPlayback)
		{
			// Adds the Black timer
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.BASELINE;
			c.gridwidth = 3;
			c.gridheight = 1;
			c.ipadx = 100;
			c.gridx = 11 + ifDouble;
			c.gridy = 4;
			this.add(m_blackTimer.getDisplayLabel(), c);

			// Adds the White timer
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.BASELINE;
			c.gridwidth = 3;
			c.gridheight = 1;
			c.ipadx = 100;
			c.gridx = 11 + ifDouble;
			c.gridy = 6;
			this.add(m_whiteTimer.getDisplayLabel(), c);
		}
		else
		{
			// Adds the Black timer
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.BASELINE;
			c.gridwidth = 3;
			c.gridheight = 1;
			c.ipadx = 100;
			c.gridx = 11 + ifDouble;
			c.gridy = 4;
			this.add(nextButt, c);

			// Adds the White timer
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.BASELINE;
			c.gridwidth = 3;
			c.gridheight = 1;
			c.ipadx = 100;
			c.gridx = 11 + ifDouble;
			c.gridy = 5;
			this.add(prevButt, c);
		}

		// Adds the White Jail
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.BASELINE;
		c.gridwidth = 3;
		c.gridheight = 3;
		c.ipadx = 0;
		c.gridx = 11 + ifDouble;
		// Changes spacing and location if there is a timer or not.
		if (m_whiteTimer instanceof NoTimer)
		{
			c.gridy = 6;
			c.insets = new Insets(10, 25, 0, 25);
		}
		else
		{
			c.gridy = 7;
			c.insets = new Insets(0, 25, 0, 25);
		}
		this.add(m_whiteCapturePanel, c);

		// Adds the White Name
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.BASELINE;
		c.gridwidth = 3;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.insets = new Insets(10, 0, 10, 0);
		// Changes spacing and adds space to the bottom of the window if there
		// is a timer.
		if (m_whiteTimer instanceof NoTimer)
		{
			c.gridheight = 1;
			c.gridy = 9;
		}
		else
		{
			c.gridheight = 2;
			c.gridy = 11;
		}
		c.ipadx = 100;
		c.gridx = 11 + ifDouble;
		this.add(m_whiteLabel, c);
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
				public void actionPerformed(ActionEvent e)
				{
					if (getGame().isBlackMove() == isBlack)
					{
						if (AIGame)
						{
							int surrender = requestAIDraw();
							if (surrender != 0)
								return;
							Result result = new Result(Result.DRAW);
							result.setText("You have declared a draw. What would you like to do?");
							PlayGame.endOfGame(result);
						}
						else
						{
							int surrender = requestDraw();

							if (surrender != 0)
								return;
							netMove = new FakeMove(-1, -1, -1, -1, -1, null); // Send
																				// move
																				// indicating
																				// surrender
																				// request.
						}
					}
				}
			});

			m_optionsMenu.add(m_drawMenuItem);
		}

		return m_optionsMenu;
	}

	/**
	 * @return The user's choice to send a draw request, 0 or 1
	 */
	public int requestDraw()
	{
		return JOptionPane.showConfirmDialog(null, "Would you like to send the other player a draw request?", "Draw",
				JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * @return The user's choice to request a draw vs an AI player.
	 */
	public int requestAIDraw()
	{
		return JOptionPane.showConfirmDialog(null, "Are you sure you would like to declare a draw?", "Draw",
				JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Setter method for boolean indicating if the current game is an AIGame.
	 * 
	 * @param AIGame If this game is an AIGame
	 */
	public void setAIGame(boolean AIGame)
	{
		this.AIGame = AIGame;
		Driver.getInstance().m_fileMenu.setVisible(true);
		m_drawMenuItem.setText("Declare Draw");
	}

}
