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
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.NetworkClient;
import net.NetworkServer;

import logic.AlgebraicConverter;
import logic.Board;
import logic.Builder;
import logic.Game;
import logic.Move;
import logic.Piece;
import logic.Result;
import logic.Square;
import timer.ChessTimer;
import timer.NoTimer;

/**
 * PlayGame.java
 * 
 * GUI to manipulate Board.
 * 
 * @author Drew Hannay & Daniel Opdyke & John McCormick
 * 
 * CSCI 335, Wheaton College, Spring 2011
 * Phase 1
 * February 25, 2011
 */
public class PlayGame extends JPanel {
	/**
	 * If the next move will place a piece.
	 */
	static boolean mustPlace;
	/**
	 * ButtonListener
	 * 
	 * Class that implements ActionListener and controls the
	 * behavior of Squares when clicked.
	 * @author Drew Hannay & Daniel Opdyke
	 *
	 */
	class ButtonListener implements ActionListener {

		/**
		 * The Square attached to this ButtonListener
		 */
		private Square clickedSquare;
		/**
		 * The board, for reference to everything else, that the game is on.
		 */
		private Board b;


		/**
		 * Constructor.
		 * Attaches a Square to this ButtonListener
		 * @param s The Square which is attached to the ButtonListener.
		 * @param b The board that is being played on.
		 */
		public ButtonListener(Square s, Board b) {
			clickedSquare = s;
			this.b = b;
		}

		/**
		 * Control movement of pieces.
		 * Check if the Square is occupied and either highlight possible destinations
		 * or move the piece.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if(mustPlace){
				mustPlace = false;
				getGame().nextTurn();
				if(!clickedSquare.isOccupied()&&clickedSquare.isHabitable()&&placePiece!=null){
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
			} else if (mustMove && clickedSquare.getColor() == Square.HIGHLIGHT_COLOR) {
				try {
					getGame().playMove(new Move(b, storedSquare, clickedSquare));
					mustMove = false;
					boardRefresh(getGame().getBoards());
				} catch (Exception e1) {
					System.out.println(e1.getMessage());
					e1.printStackTrace();
				}
			} else if (!mustMove && clickedSquare.getPiece() != null
					&& clickedSquare.getPiece().isBlack() == getGame().isBlackMove()) {
				List<Square> dests = clickedSquare.getPiece().getLegalDests();
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

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -2507232401817253688L;

	/**
	 * Reference to the current Game being played.
	 */
	protected static Game g;

	/**
	 * Reference to the Square which will be moved to
	 */
	protected Square storedSquare;

	/**
	 * Boolean indicating if this piece must move before another may be selected
	 */
	protected static boolean mustMove;
	/**
	 * Timer for the white team
	 */
	protected static ChessTimer whiteTimer;

	/**
	 * Timer for the black team.
	 */
	protected static ChessTimer blackTimer;
	/**
	 * Display message for being in check. Invisible when not in check.
	 */
	protected static JLabel inCheck;
	/**
	 * The label for the white team.
	 */
	protected static JLabel whiteLabel;
	/**
	 * The label for the black team.
	 */
	protected static JLabel blackLabel;
	/**
	 * The Panel to hold any black pieces that have been captured by white.
	 */
	protected static JPanel whiteCaptures;
	/**
	 * The Panel to hold any white pieces that have been captured by black.
	 */
	protected static JPanel blackCaptures;
	/**
	 * The Jail that holds black pieces white has taken.
	 */
	protected static Jail whiteCapturesBox;
	/**
	 * The Jail that holds white pieces black
	 */
	protected static Jail blackCapturesBox;
	/**
	 * Defines the state of PlayGame to be in a game or a play back of a completed game.
	 */
	protected static boolean isPlayback;
	/**
	 * Used for the play back and undo functions. This hold the history of moves in an array of moves.
	 */
	protected static Move[] history;
	/**
	 * Button for undoing moves
	 */
	protected JButton undoItem;
	/**
	 * This keeps the current place in the Move[] array during games.
	 */
	protected static int index;
	/**
	 * The piece to place upon clicking.
	 */
	protected static Piece placePiece;
	/**
	 * Menu item for saving games
	 */
	protected JMenuItem saveItem;
	/**
	 * Menu item to declare a draw
	 */
	protected JMenuItem drawItem;
	/**
	 * Menu to hold the options
	 */
	protected static JMenu menu;
	
	/**
	 * @param isPlayback whether PlayGame is in playback mode
	 * @param file The file holding the ACN of the game move history.
	 */
	public PlayGame(boolean isPlayback, File file) throws Exception {
		setGame(Builder.newGame("Classic"));
		PlayGame.isPlayback = isPlayback;
		mustMove = false;
		PlayGame.whiteTimer = getGame().getWhiteTimer();
		PlayGame.blackTimer = getGame().getBlackTimer();
		whiteTimer.restart();
		blackTimer.restart();
		turn(getGame().getBoards()[0].isBlackTurn());
		initComponents(isPlayback);
		setGame(AlgebraicConverter.convert(getGame(), file));
		history = new Move[getGame().getHistory().size()];
		getGame().getHistory().toArray(history);
		index = history.length - 1;
		while (index >= 0) {
			history[index--].undo();
		}
		boardRefresh(getGame().getBoards());
	}

	/**
	 * Constructor.
	 * Call initComponents to initialize the GUI.
	 * @param g The reference to the game being played.
	 * @param isPlayback whether PlayGame is in play back mode
	 */
	public PlayGame(Game g, boolean isPlayback) throws Exception{
		PlayGame.setGame(g);
		PlayGame.isPlayback = isPlayback;
		if(isPlayback){
			PlayGame.whiteTimer = new NoTimer();
			PlayGame.blackTimer = new NoTimer();
			history = new Move[g.getHistory().size()];
			g.getHistory().toArray(history);
			initComponents(isPlayback);
			index = history.length-1;
			while(index>=0){
				history[index].undo();
				index--;
			}
		}
		else{
			mustMove = false;
			PlayGame.whiteTimer = g.getWhiteTimer();
			PlayGame.blackTimer = g.getBlackTimer();
			whiteTimer.restart();
			blackTimer.restart();
			turn(g.isBlackMove());
			history = null;
			index = -3;
			initComponents(isPlayback);
		}
		boardRefresh(g.getBoards());
	}

	/**
	 * 
	 * @param b The array of boards objects
	 */
	public static void boardRefresh(Board[] b) {
		
		for (int k = 0; k < b.length; k++) {
			for (int i = 1; i <= b[k].getMaxRow(); i++) {
				for (int j = 1; j <= b[k].getMaxCol(); j++) {
					b[k].getSquare(i, j).refresh();
				}
			}
		}
		
		Piece objective = getGame().isBlackMove() ? getGame().getBlackRules().objectivePiece(true) : getGame().getWhiteRules().objectivePiece(
				false);
		
		if (objective != null && objective.isInCheck()) {
			inCheck.setVisible(true);
			if (getGame().getBlackRules().objectivePiece(true).isInCheck()) {
				inCheck.setBorder(BorderFactory.createTitledBorder("Black Team"));
			} else {
				inCheck.setBorder(BorderFactory.createTitledBorder("White Team"));
			}
			
			for(Piece p : getGame().getThreats(objective))
				p.getSquare().setColor(Color.red);
			
		} else {
			inCheck.setVisible(false);
		}
		for (int i = 1; i <= whiteCapturesBox.getMaxRow(); i++) {
			for (int j = 1; j <= whiteCapturesBox.maxCol; j++) {
				whiteCapturesBox.getSquare(i, j).setPiece(null);
			}
		}
		int index = 0;
		Piece[] blackCaptured = getGame().getCapturedPieces(true);
		for (int i = 1; i <= whiteCapturesBox.getMaxRow(); i++) {
			for (int j = 1; j <= whiteCapturesBox.maxCol; j++) {
				if (blackCaptured != null && index < blackCaptured.length) {
					whiteCapturesBox.getSquare(i, j).setPiece(blackCaptured[index]);
					index++;
				}
				whiteCapturesBox.getSquare(i, j).refresh();
			}
		}
		for (int i = 1; i <= blackCapturesBox.getMaxRow(); i++) {
			for (int j = 1; j <= blackCapturesBox.maxCol; j++) {
				blackCapturesBox.getSquare(i, j).setPiece(null);
			}
		}
		index = 0;
		Piece[] whiteCaptured = getGame().getCapturedPieces(false);
		for (int i = 1; i <= blackCapturesBox.getMaxRow(); i++) {
			for (int j = 1; j <= blackCapturesBox.maxCol; j++) {
				if (whiteCaptured != null && index < whiteCaptured.length) {
					blackCapturesBox.getSquare(i, j).setPiece(whiteCaptured[index]);
					index++;
				}
				blackCapturesBox.getSquare(i, j).refresh();
			}
		}
		// Highlight the name labels if it's their turn.
		whiteLabel.setBackground(getGame().isBlackMove() ? null : Square.HIGHLIGHT_COLOR);
		whiteLabel.setForeground(getGame().isBlackMove() ? Color.black : Color.white); 
		blackLabel.setBackground(getGame().isBlackMove() ? Square.HIGHLIGHT_COLOR : null);
		blackLabel.setForeground(getGame().isBlackMove() ? Color.white : Color.black);

	}

	/**
	 * @param r Reference to which button the user clicked for the end game. "Save" "New Game" or "Quit"
	 */
	public static void endOfGame(Result r) {
		PlayNetGame.running = false;
		if(g.getHistory().size() != 0)
			PlayNetGame.netMove = g.moveToFakeMove(g.getHistory().get(g.getHistory().size()-1));
		else{
			JOptionPane.showMessageDialog(null, "No moves were made and the time ran out. Returning to the Main Menu.");
			NetworkClient.closeSocket();
			NetworkServer.closeSocket();
			Driver.getInstance().revertPanel();
			Driver.getInstance().gamePlayHelp.setVisible(false);
			Driver.getInstance().helpMenu.setText("Help");
			Driver.getInstance().fileMenu.setVisible(true);
			return;
		}
		if (isPlayback)
			return;
		Object[] options = new String[] { "Save Record of Game", "New Game", "Quit" };
		menu.setVisible(false);
		int answer = JOptionPane.showOptionDialog(null,
				r.text(), r.winText(),
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
						options, options[0]);
		switch (answer) {
		case 0:
			String fileName = JOptionPane.showInputDialog(null, "Enter a name for the save file:",
					"Saving...", JOptionPane.PLAIN_MESSAGE);
			getGame().saveGame("completedGames", fileName, getGame().isClassicChess());
			g.setBlackMove(false);
			Driver.getInstance().gamePlayHelp.setVisible(false);
			Driver.getInstance().helpMenu.setText("Help");
			Driver.getInstance().fileMenu.setVisible(true);
			Driver.getInstance().revertPanel();
			break;
		case 2:
			g.setBlackMove(false);
			System.exit(0);
			break;
		default:
			g.setBlackMove(false);
			Driver.getInstance().gamePlayHelp.setVisible(false);
			Driver.getInstance().helpMenu.setText("Help");
			Driver.getInstance().revertPanel();
		}
	}

	/**
	 * @param b Whose turn it is for which timer need to be running.
	 */
	public static void turn(boolean b) {
		if (whiteTimer != null && blackTimer != null) {
			(!b ? whiteTimer : blackTimer).start();
			(b ? whiteTimer : blackTimer).stop();
		}
	}

	/**
	 * @param b The board that the game is being played on.
	 * @param isPlayback whether PlayGame is in playback mode
	 * @return the grid being created.
	 */
	private JPanel createGrid(Board b, boolean isPlayback) {

		final JPanel grid = new JPanel();
		//grid.setBorder(BorderFactory.createEtchedBorder());

		//Create a JPanel to hold the grid and set the layout to the number of squares in the board.
		//final JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(b.numRows() + 1, b.numCols()));
		//Set the size of the grid to the number of rows and columns, scaled by 48, the size of the images.
		grid.setPreferredSize(new Dimension((b.numCols() + 1) * 48, (b.numRows() + 1) * 48));

		//Loop through the board, initializing each Square and adding it's ActionListener.
		int numRows = b.numRows();
		int numCols = b.numCols();
		for (int i = numRows; i > 0; i--) {
			JLabel temp = new JLabel("" + i);
			temp.setHorizontalAlignment(SwingConstants.CENTER);
			grid.add(temp);
			for (int j = 1; j <= numCols; j++) {

				//grid.add(new JLabel(""+(j-1+'a')));
				JButton jb = new JButton();
				if (!isPlayback) {
					jb.addActionListener(new ButtonListener(b.getSquare(i, j), b));
				}
				b.getSquare(i, j).setButton(jb);//Let the Square know which button it owns.
				grid.add(jb);//Add the button to the grid.

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
	 * @return The Menu bar for the GUI
	 */
	public JMenu createMenu() {
		menu = new JMenu("Menu");

		if (!isPlayback) {

			drawItem = new JMenuItem("Declare Draw", KeyEvent.VK_D);
			saveItem = new JMenuItem("Save & Quit", KeyEvent.VK_S);

			drawItem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (getGame().getLastMove() == null)
						return;
					menu.setVisible(false);
					Result r = new Result(Result.DRAW);
					r.setText("Draw! \n" + "What would you like to do? \n");
					getGame().getLastMove().setResult(r);
					endOfGame(r);
				}
			});

			saveItem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					whiteTimer.stop();
					blackTimer.stop();
					String fileName = JOptionPane.showInputDialog(null, "Enter a name for the save file:",
							"Saving...", JOptionPane.PLAIN_MESSAGE);
					if (fileName == null)
						return;
					getGame().saveGame("gamesInProgress", fileName, false);
					
					menu.setVisible(false);
					Driver.getInstance().revertPanel();
				}
			});

			menu.add(drawItem);
			menu.add(saveItem);

		}

		return menu;
	}

	/**
	 * Initialize components of the GUI
	 * Create all the GUI components, set their specific properties and add them to the 
	 * window. Also add any necessary ActionListeners.
	 * @param isPlayback whether PlayGame is in playback mode
	 */
	@SuppressWarnings("static-access")
	private void initComponents(boolean isPlayback) throws Exception{
		// Has spaces to hax0r fix centering.
		inCheck = new JLabel("You're In Check!");
		inCheck.setHorizontalTextPosition(inCheck.CENTER);
		inCheck.setForeground(Color.RED);
		
		undoItem = new JButton("Undo");
		undoItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mustMove = false;
				if (getGame().getHistory().size() == 0)
					return;
				try{
					getGame().getHistory().get(getGame().getHistory().size() - 1).undo();
				}catch(Exception e){
					
				}
				getGame().getHistory().remove(getGame().getHistory().size() - 1);
				(getGame().isBlackMove() ? getGame().getBlackRules() : getGame().getWhiteRules()).undoEndOfGame();
				boardRefresh(getGame().getBoards());
			}
		});

		int ifDouble = 0;
		if(menu == null)
			Driver.getInstance().setMenu(createMenu());
		else if(!menu.isVisible())
			Driver.getInstance().setMenu(createMenu());
		
		//Set the layout of the JPanel.
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		//Get the Board[] from the Game.
		final Board[] boards = getGame().getBoards();
		this.setBorder(BorderFactory.createLoweredBevelBorder());
		//Adds the grid

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
				} catch (Exception e1) {}
			}
		});

		//I made name1 (White) & name2 (Black) instance variables so that I can highlight them
		//when it's their turn.

		whiteLabel = new JLabel("WHITE");
		whiteLabel.setHorizontalAlignment(SwingConstants.CENTER);

		whiteLabel.setBorder(BorderFactory.createTitledBorder(""));

		blackLabel = new JLabel("BLACK");
		blackLabel.setHorizontalAlignment(SwingConstants.CENTER);
		blackLabel.setBorder(BorderFactory.createTitledBorder(""));

		//Needed for highlighting the names when it's their turn.
		whiteLabel.setOpaque(true);
		blackLabel.setOpaque(true);

		/**
		 *  int to hold the size of the jail board.
		 */
		int k;

		/**
		 * This sets k to either the size of how many pieces white has or how many pieces black has.
		 * If neither team has any pieces then 
		 */
		if (getGame().getWhiteTeam().size() <= 4 && getGame().getBlackTeam().size() <= 4) {
			k = 4;
		} else {
			double o = getGame().getWhiteTeam().size() > getGame().getBlackTeam().size() ? Math.sqrt(getGame().getWhiteTeam().size()) : Math
					.sqrt(getGame().getBlackTeam().size());
			k = (int) Math.ceil(o);
		}

		/**
		 * Makes Black's jail
		 */
		whiteCaptures = new JPanel();
		whiteCaptures.setBorder(BorderFactory.createTitledBorder("Captured Pieces"));
		if(k < 4){
			whiteCapturesBox = new Jail(4, 4);
			whiteCaptures.setLayout(new GridLayout(4, 4));
		}
		else{
			whiteCapturesBox = new Jail(k, k);
			whiteCaptures.setLayout(new GridLayout(k, k));
		}
		whiteCaptures.setPreferredSize(new Dimension((whiteCapturesBox.numCols() + 1) * 25,
				(whiteCapturesBox.numRows() + 1) * 25));
		for (int i = k; i > 0; i--) {
			for (int j = 1; j <= k; j++) {
				JButton jb = new JButton();
				whiteCapturesBox.getSquare(i, j).setButton(jb);//Let the Square know which button it owns.
				whiteCaptures.add(jb);
			}
		}

		/**
		 * Makes White's jail
		 */
		blackCaptures = new JPanel();
		blackCaptures.setBorder(BorderFactory.createTitledBorder("Captured Pieces"));
		if(k < 4){
			blackCapturesBox = new Jail(4, 4);
			blackCaptures.setLayout(new GridLayout(4, 4));
		}
		else{
			blackCapturesBox = new Jail(k, k);
			blackCaptures.setLayout(new GridLayout(k, k));
		}
		blackCaptures.setPreferredSize(new Dimension((blackCapturesBox.numCols() + 1) * 25,
				(blackCapturesBox.numRows() + 1) * 25));
		for (int i = k; i > 0; i--) {
			for (int j = 1; j <= k; j++) {
				JButton jb = new JButton();
				blackCapturesBox.getSquare(i, j).setButton(jb);//Let the Square know which button it owns.
				blackCaptures.add(jb);
			}
		}

		/*
		 * This is the section that adds all of the peripheral GUI components
		 * It adds them in the order that they are displayed from top to bottom.
		 * 
		 * This is for reference for editing Insets
		 * 		// Insets(top,left,bottom,right) << This is to show how to format.
		 *		// Insets are blank space outside of the object to buffer around it.
		 */

		//Adds the Black Name
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.BASELINE;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.insets = new Insets(10, 10, 10, 0);
		c.ipadx = 100;
		c.gridx = 11 + ifDouble;
		c.gridy = 0;
		this.add(blackLabel, c);

		//Adds the Black Jail
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
			//Adds the Black timer
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.BASELINE;
			c.gridwidth = 3;
			c.gridheight = 1;
			c.ipadx = 100;
			c.gridx = 11 + ifDouble;
			c.gridy = 4;
			this.add(blackTimer.getLabel(), c);

			//Adds the UNDO button
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.BASELINE;
			c.gridwidth = 3;
			c.gridheight = 1;
			c.ipadx = 100;
			c.gridx = 11 + ifDouble;
			c.gridy = 5;
			this.add(undoItem, c);

			//Adds the White timer
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.BASELINE;
			c.gridwidth = 3;
			c.gridheight = 1;
			c.ipadx = 100;
			c.gridx = 11 + ifDouble;
			c.gridy = 6;
			this.add(whiteTimer.getLabel(), c);
		} else {
			//Adds the Black timer
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.BASELINE;
			c.gridwidth = 3;
			c.gridheight = 1;
			c.ipadx = 100;
			c.gridx = 11 + ifDouble;
			c.gridy = 4;
			this.add(nextButt, c);

			//Adds the White timer
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.BASELINE;
			c.gridwidth = 3;
			c.gridheight = 1;
			c.ipadx = 100;
			c.gridx = 11 + ifDouble;
			c.gridy = 5;
			this.add(prevButt, c);
		}

		//Adds the White Jail
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

		//Adds the White Name
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.BASELINE;
		c.gridwidth = 3;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.insets = new Insets(10, 0, 10, 0);
		// Changes spacing and adds space to the bottom of the window if there is a timer.
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
	/**
	 * Set the mustPlace variable.
	 * @param b The new value for mustPlace.
	 */
	public static void setMustPlace(boolean b){
		mustPlace = b;
	}
	/**
	 * Getter for mustPlace
	 * @return mustPlace
	 */
	public static boolean getMustPlace() {
		return mustPlace;
	}
	/**
	 * Setter for placePiece
	 * @param p The new value for Piece
	 */
	public static void setPlacePiece(Piece p){
		placePiece = p;
	}

	/**
	 * @param g the g to set
	 */
	public static void setGame(Game g) {
		PlayGame.g = g;
	}

	/**
	 * @return the g
	 */
	public static Game getGame() {
		return g;
	}
	public static void resetTimers(){
		whiteTimer.reset();
		blackTimer.reset();
	}

}
