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
 *         Class for playing network or AI games
 */
public class PlayNetGame extends PlayGame {

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
	 * @param g
	 *            the game to be played
	 * @param isPlayback
	 *            if this is a playback (always false)
	 * @param isBlack
	 *            if the player is black
	 * @throws Exception
	 *             Needed for dealing with super()
	 */
	public PlayNetGame(Game g, boolean isPlayback, boolean isBlack)
			throws Exception {
		super(g, isPlayback);
		PlayNetGame.isBlack = isBlack;
		initComponents(isPlayback);
	}

	/**
	 * Getter method for isBlack boolean
	 * 
	 * @return If the player of this game controls the white or black team
	 */
	public static boolean isBlack() {
		return isBlack;
	}

	/**
	 * Class for button listeners
	 */
	class ButtonListener implements MouseListener {

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
		 * @param s
		 *            The Square which is attached to the ButtonListener.
		 * @param b
		 *            The board that is being played on.
		 */
		public ButtonListener(Square s, Board b) {
			clickedSquare = s;
			this.b = b;
		}

		/**
		 * Control movement of pieces. Check if the Square is occupied and
		 * either highlight possible destinations or move the piece.
		 */
		@Override
		public void mouseClicked(MouseEvent arg0) {
			if (getGame().isBlackMove() == isBlack) {
				if (mustPlace) {
					mustPlace = false;
					getGame().nextTurn();
					if (!clickedSquare.isOccupied()
							&& clickedSquare.isHabitable()
							&& placePiece != null) {
						placePiece.setSquare(clickedSquare);
						clickedSquare.setPiece(placePiece);
						placePiece = null;
						mustPlace = false;
						boardRefresh(getGame().getBoards());
						getGame().genLegalDests();
					}

					return;
				}
				if (mustMove && clickedSquare == storedSquare) {
					boardRefresh(getGame().getBoards());
					mustMove = false;
				} else if (mustMove
						&& clickedSquare.getColor() == Square.HIGHLIGHT_COLOR) {
					try {
						Move m = new Move(b, storedSquare, clickedSquare);
						getGame().playMove(m);

						netMove = g.moveToFakeMove(m);

						mustMove = false;
						boardRefresh(getGame().getBoards());
					} catch (Exception e1) {
						System.out.println(e1.getMessage());
						e1.printStackTrace();
					}
				} else if (!mustMove
						&& clickedSquare.getPiece() != null
						&& clickedSquare.getPiece().isBlack() == getGame()
								.isBlackMove()) {
					List<Square> dests = clickedSquare.getPiece()
							.getLegalDests();
					if (dests.size() > 0) {
						for (Square dest : dests) {
							dest.setBackgroundColor(Square.HIGHLIGHT_COLOR);
						}
						storedSquare = clickedSquare;
						mustMove = true;
					}
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
	}

	/**
	 * @param b
	 *            The board that the game is being played on.
	 * @param isPlayback
	 *            whether PlayGame is in playback mode
	 * @return the grid being created.
	 */
	private JPanel createGrid(Board b, boolean isPlayback) {

		final JPanel grid = new JPanel();
		// grid.setBorder(BorderFactory.createEtchedBorder());

		// Create a JPanel to hold the grid and set the layout to the number of
		// squares in the board.
		// final JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(b.numRows() + 1, b.numCols()));
		// Set the size of the grid to the number of rows and columns, scaled by
		// 48, the size of the images.
		grid.setPreferredSize(new Dimension((b.numCols() + 1) * 48, (b
				.numRows() + 1) * 48));

		// Loop through the board, initializing each Square and adding it's
		// ActionListener.
		int numRows = b.numRows();
		int numCols = b.numCols();
		for (int i = numRows; i > 0; i--) {
			JLabel temp = new JLabel("" + i);
			temp.setHorizontalAlignment(SwingConstants.CENTER);
			grid.add(temp);
			for (int j = 1; j <= numCols; j++) {

				// grid.add(new JLabel(""+(j-1+'a')));
				if (!isPlayback) {
					b.getSquare(i, j).addMouseListener(
							new ButtonListener(b.getSquare(i, j), b));
				}
				grid.add(b.getSquare(i, j));// Add the button to the grid.

			}

		}
		for (int k = 0; k <= numCols; k++) {
			if (k != 0) {
				JLabel temp = new JLabel("" + (char) (k - 1 + 'A'));
				temp.setHorizontalAlignment(SwingConstants.CENTER);
				grid.add(temp);

			} else {
				grid.add(new JLabel(""));
			}
		}
		return grid;
	}

	/**
	 * Setups up the window
	 * 
	 * @param isPlayback
	 *            whether this is just a review or not
	 */
	@SuppressWarnings("static-access")
	private void initComponents(boolean isPlayback) {

		super.removeAll();
		menu.setVisible(false);

		Driver.getInstance().fileMenu.setVisible(false);

		// Has spaces to hax0r fix centering.
		inCheck = new JLabel("You're In Check!");
		inCheck.setHorizontalTextPosition(inCheck.CENTER);
		inCheck.setForeground(Color.RED);

		int ifDouble = 0;
		Driver.getInstance().setMenu(createMenu());

		// Set the layout of the JPanel.
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		// Get the Board[] from the Game.
		final Board[] boards = getGame().getBoards();
		setBorder(BorderFactory.createLoweredBevelBorder());
		// Adds the grid

		// Adds the inCheck notification.
		inCheck.setHorizontalTextPosition(SwingConstants.CENTER);
		inCheck.setHorizontalAlignment(SwingConstants.CENTER);
		c.fill = GridBagConstraints.NONE;
		c.gridy = 0;
		c.gridx = 9;
		inCheck.setVisible(false);
		this.add(inCheck, c);

		if (boards.length == 1) {
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
		} else {
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
		nextButt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (index + 1 == history.length)
					return;
				try {
					history[++index].execute();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		JButton prevButt = new JButton("Previous");
		prevButt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (index == -1)
					return;
				try {
					history[index--].undo();
				} catch (Exception e1) {

				}
			}
		});

		// I made name1 (White) & name2 (Black) instance variables so that I can
		// highlight them
		// when it's their turn.

		whiteLabel = new JLabel("WHITE");
		whiteLabel.setHorizontalAlignment(SwingConstants.CENTER);

		whiteLabel.setBorder(BorderFactory.createTitledBorder(""));

		blackLabel = new JLabel("BLACK");
		blackLabel.setHorizontalAlignment(SwingConstants.CENTER);
		blackLabel.setBorder(BorderFactory.createTitledBorder(""));

		// Needed for highlighting the names when it's their turn.
		whiteLabel.setOpaque(true);
		blackLabel.setOpaque(true);

		whiteLabel.setBackground(getGame().isBlackMove() ? null
				: Square.HIGHLIGHT_COLOR);

		/**
		 * int to hold the size of the jail board.
		 */
		int k;

		/**
		 * This sets k to either the size of how many pieces white has or how
		 * many pieces black has. If neither team has any pieces then
		 */
		if (getGame().getWhiteTeam().size() <= 4
				&& getGame().getBlackTeam().size() <= 4) {
			k = 4;
		} else {
			double o = getGame().getWhiteTeam().size() > getGame()
					.getBlackTeam().size() ? Math.sqrt(getGame().getWhiteTeam()
					.size()) : Math.sqrt(getGame().getBlackTeam().size());
			k = (int) Math.ceil(o);
		}

		/**
		 * Makes Black's jail
		 */
		whiteCaptures = new JPanel();
		whiteCaptures.setBorder(BorderFactory
				.createTitledBorder("Captured Pieces"));
		if (k < 4) {
			whiteCapturesBox = new Jail(4, 4);
			whiteCaptures.setLayout(new GridLayout(4, 4));
		} else {
			whiteCapturesBox = new Jail(k, k);
			whiteCaptures.setLayout(new GridLayout(k, k));
		}
		whiteCaptures.setPreferredSize(new Dimension((whiteCapturesBox
				.numCols() + 1) * 25, (whiteCapturesBox.numRows() + 1) * 25));
		for (int i = k; i > 0; i--) {
			for (int j = 1; j <= k; j++) {
				Square square = new Square(i, j);
				whiteCaptures.add(square);
			}
		}

		/**
		 * Makes White's jail
		 */
		blackCaptures = new JPanel();
		blackCaptures.setBorder(BorderFactory
				.createTitledBorder("Captured Pieces"));
		if (k < 4) {
			blackCapturesBox = new Jail(4, 4);
			blackCaptures.setLayout(new GridLayout(4, 4));
		} else {
			blackCapturesBox = new Jail(k, k);
			blackCaptures.setLayout(new GridLayout(k, k));
		}
		blackCaptures.setPreferredSize(new Dimension((blackCapturesBox
				.numCols() + 1) * 25, (blackCapturesBox.numRows() + 1) * 25));
		for (int i = k; i > 0; i--) {
			for (int j = 1; j <= k; j++) {
				Square square = new Square(i, j);
				blackCaptures.add(square);
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
		this.add(blackLabel, c);

		// Adds the Black Jail
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.BASELINE;
		c.gridwidth = 3;
		c.gridheight = 3;
		c.ipadx = 0;
		c.insets = new Insets(0, 25, 10, 25);
		c.gridx = 11 + ifDouble;
		c.gridy = 1;
		this.add(blackCaptures, c);

		// If it is playback then we do not want timers.
		if (!isPlayback) {
			// Adds the Black timer
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.BASELINE;
			c.gridwidth = 3;
			c.gridheight = 1;
			c.ipadx = 100;
			c.gridx = 11 + ifDouble;
			c.gridy = 4;
			this.add(blackTimer.getLabel(), c);

			// Adds the White timer
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.BASELINE;
			c.gridwidth = 3;
			c.gridheight = 1;
			c.ipadx = 100;
			c.gridx = 11 + ifDouble;
			c.gridy = 6;
			this.add(whiteTimer.getLabel(), c);
		} else {
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
		if (whiteTimer instanceof NoTimer) {
			c.gridy = 6;
			c.insets = new Insets(10, 25, 0, 25);
		} else {
			c.gridy = 7;
			c.insets = new Insets(0, 25, 0, 25);
		}
		this.add(whiteCaptures, c);

		// Adds the White Name
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.BASELINE;
		c.gridwidth = 3;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.insets = new Insets(10, 0, 10, 0);
		// Changes spacing and adds space to the bottom of the window if there
		// is a timer.
		if (whiteTimer instanceof NoTimer) {
			c.gridheight = 1;
			c.gridy = 9;
		} else {
			c.gridheight = 2;
			c.gridy = 11;
		}
		c.ipadx = 100;
		c.gridx = 11 + ifDouble;
		this.add(whiteLabel, c);
	}

	@Override
	public JMenu createMenu() {
		menu = new JMenu("Menu");
		if (!isPlayback) {
			drawItem = new JMenuItem("Request Draw", KeyEvent.VK_R);
			drawItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (getGame().isBlackMove() == isBlack) {
						if (AIGame) {
							int surrender = requestAIDraw();
							if (surrender != 0)
								return;
							Result result = new Result(Result.DRAW);
							result.setText("You have declared a draw. What would you like to do?");
							PlayGame.endOfGame(result);
						} else {
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

			menu.add(drawItem);
		}

		return menu;
	}

	/**
	 * @return The user's choice to send a draw request, 0 or 1
	 */
	public int requestDraw() {
		return JOptionPane.showConfirmDialog(null,
				"Would you like to send the other player a draw request?",
				"Draw", JOptionPane.YES_NO_OPTION);
	}

	/**
	 * @return The user's choice to request a draw vs an AI player.
	 */
	public int requestAIDraw() {
		return JOptionPane.showConfirmDialog(null,
				"Are you sure you would like to declare a draw?", "Draw",
				JOptionPane.YES_NO_OPTION);
	}

	/**
	 * Setter method for boolean indicating if the current game is an AIGame.
	 * 
	 * @param AIGame
	 *            If this game is an AIGame
	 */
	public void setAIGame(boolean AIGame) {
		this.AIGame = AIGame;
		Driver.getInstance().fileMenu.setVisible(true);
		drawItem.setText("Declare Draw");
	}

}
