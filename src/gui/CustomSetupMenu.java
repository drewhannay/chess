package gui;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import logic.Board;
import logic.Builder;
import logic.Piece;
import logic.PieceBuilder;
import logic.Square;
import rules.AdjustTeamDests;
import rules.EndOfGame;
import rules.GetBoard;
import rules.ObjectivePiece;
import rules.Rules;

/**
 * PieceCustomMenu.java
 * 
 * GUI to handle setup of pieces on the board and special square
 * properties.
 * 
 * @author Drew Hannay & Daniel Opdyke
 * 
 * CSCI 335, Wheaton College, Spring 2011
 * Phase 1
 * February 25, 2011
 */
public class CustomSetupMenu extends JPanel {

	/**
	 * MakeNewPieceType
	 * 
	 * ActionListener to handle board setup.
	 * 
	 * @author Drew Hannay & Daniel Opdyke
	 */
	class MakeNewPieceType {

		/**
		 * The Square we are setting up.
		 */
		private Square square;
		/**
		 * The color of the piece we are adding.
		 */
		private boolean isBlack;
		/**
		 * The Board on which the Square we are setting up resides.
		 */
		private Board board;
		/**
		 * The PieceBuilder which will set up the new Piece type.
		 */
		private PieceBuilder builder;

		/**
		 * Constructor
		 * Set instance variables to passed parameters
		 * @param square The square we are setting up.
		 * @param isBlack The color of the piece we are creating.
		 * @param board The board on which the square resides.
		 */
		public MakeNewPieceType(Square square, boolean isBlack, Board board) {
			this.square = square;
			this.isBlack = isBlack;
			this.board = board;
		}

		/**
		 * GUI to collect the options desired for a Piece type.
		 * Make the pop up, create the components and add their
		 * ActionListeners to collect information.
		 */
		public void makePiece() {
			//Create a new PieceBuilder.
			builder = new PieceBuilder();

			//Create the pop up and set the size, location and layout.
			final JFrame popup = new JFrame("Create Piece");
			popup.setSize(450,500);
			popup.setLocationRelativeTo(null);
			popup.setLayout(new FlowLayout());
			popup.setResizable(false);

			//Add a JLabel and JTextField for the Piece name.
			popup.add(new JLabel("Piece Name:"));
			final JTextField name = new JTextField(15);
			popup.add(name);
			
			final JPanel lightIconPanel = new JPanel();
			final JButton lightIconButton = new JButton();
			lightIconButton.setSize(48, 48);
			lightIconPanel.add(lightIconButton);

			//Add JButtons for choosing the images for the new type.
			final JButton chooseLightImage = new JButton("Choose image for light piece");
			chooseLightImage.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					//Create the JFileChooser and add image for the new piece
					final JFileChooser fc = new JFileChooser("./images"); //default directory is in the images folder
					int returnVal = fc.showOpenDialog(CustomSetupMenu.this);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						ImageIcon icon = makeIcon(fc, builder);
						lightIconButton.setIcon(icon); //Adds icon to button
						builder.setLightImage(icon);
					}
				}
			});
			popup.add(chooseLightImage);
			popup.add(lightIconPanel);

			final JPanel darkIconPanel = new JPanel();
			final JButton darkIconButton = new JButton();
			darkIconButton.setSize(48, 48);
			darkIconPanel.add(darkIconButton);
			
			final JButton chooseDarkImage = new JButton("Choose image for dark piece");
			chooseDarkImage.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					//Create the JFileChooser and add image for the new piece
					final JFileChooser fc = new JFileChooser("./images"); //default directory is in the images folder
					int returnVal = fc.showOpenDialog(CustomSetupMenu.this);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						ImageIcon icon = makeIcon(fc, builder);
						darkIconButton.setIcon(icon); //Adds icon to button
						builder.setDarkImage(icon);
					}
				}
			});
			popup.add(chooseDarkImage);
			popup.add(darkIconPanel);

			//Add components for collecting the directions of movement.

			final JComboBox dropdown = new JComboBox(new String[]{"North","Northeast","East","Southeast", "South","Southwest","West","Northwest"});

			//Collect max distance of movement, -1 for infinity.
			final JTextField dist = new JTextField(3);
			dist.setToolTipText("Greatest amount of spaces piece can travel in chosen direction");
			
			//Create button and add ActionListener for adding movement directions to a piece
			final JButton addInstruction = new JButton("Add Movement Directions to this Piece");
			addInstruction.setToolTipText("Pressing this will add movement direction and max distance in that direction.");
			addInstruction.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (isIntDist()) {//Make sure their number is an int.
						if (dropdown.getSelectedItem() != null) {//Make sure there's directions left in the drop down.				
							//Add the move to the piece type and remove that direction from the drop down.
							builder.addMove(stringToChar((String) dropdown.getSelectedItem()), Integer.parseInt(dist
									.getText()));
							dropdown.removeItemAt(dropdown.getSelectedIndex());
							dist.setText(""); //Clear their int distance.
						}
					}
				}

				/**
				 * Determine if the user entered a valid integer.
				 * @return If the text is a valid integer
				 */
				private boolean isIntDist() {
					try {
						Integer.parseInt(dist.getText());
						return true;
					} catch (Exception e) {
						return false;
					}
				}
				
				/**
				 * Translate a direction string to it's corresponding char.
				 * @param s The string to translate
				 * @return The char corresponding to the given String.
				 */
				private char stringToChar(String s) {
					//TODO Can this be any better?
					if (s.equals("North"))
						return 'N';
					if (s.equals("South"))
						return 'S';
					if (s.equals("East"))
						return 'E';
					if (s.equals("West"))
						return 'W';
					if (s.equals("Northeast"))
						return 'R';
					if (s.equals("Northwest"))
						return 'L';
					if (s.equals("Southeast"))
						return 'r';
					else
						return 'l';
				}
			});
			popup.add(addInstruction);
			
			//Setting up a panel handle movement instructions 
			JPanel movementSetup = new JPanel();
			movementSetup.setLayout(new BoxLayout(movementSetup,BoxLayout.Y_AXIS));
			movementSetup.setBorder(BorderFactory.createTitledBorder("Movement Setup")); //Setting up border for movement panel
			movementSetup.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			
			final JTextField knight = new JTextField(2);
			knight.setToolTipText("Enter the knight-like directions you would like");
			knight.setEnabled(false);
			
			final JTextField knightSecond = new JTextField(2);
			knightSecond.setToolTipText("Enter the other direction for the knight-like piece");
			knightSecond.setEnabled(false);
			
			final JCheckBox knightOn = new JCheckBox("Knight-like Movements", false);
			knightOn.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					knight.setEnabled(true);
					knightSecond.setEnabled(true);
				}
			});
			
			final JCheckBox leaper = new JCheckBox("Can jump other Pieces?", false);
			
			final JPanel knightMoving = new JPanel();
			knightMoving.setLayout(new FlowLayout());
			knightMoving.add(knight);
			knightMoving.add(new JLabel("x"));
			knightMoving.add(knightSecond);
			
			
			//Adds options and labels for setting up movement for the new piece
			c.insets = new Insets(5, 0, 5, 0);
			c.gridx = 0;
			c.gridy = 0;
			movementSetup.add(new JLabel("<html><u>Normal Movement Setup:</u></br></html>"), c);
			c.insets = new Insets(5, 0, 0, 0);
			c.gridx =0;
			c.gridy = 1;
			movementSetup.add(new JLabel("Direction of Movement:"), c);
			c.insets = new Insets(5, 0, 0, 0);
			c.gridx = 1;
			c.gridy = 1;
			movementSetup.add(dropdown, c);
			c.insets = new Insets(5, 0, 0, 0);
			c.gridx = 0;
			c.gridy = 2;
			movementSetup.add(new JLabel("Max Distance of Movement:"), c);
			c.insets = new Insets(5, 0, 0, 0);
			c.gridx = 1;
			c.gridy = 2;
			movementSetup.add(dist, c);
			c.insets = new Insets(5, 0, 0, 0);
			c.gridx = 0;
			c.gridy = 3;
			c.gridwidth = 3;
			movementSetup.add(addInstruction, c);
			c.insets = new Insets(5, 0, 0, 0);
			c.gridx = 0;
			c.gridy = 4;
			movementSetup.add(leaper, c);
			c.insets = new Insets(5, 0, 0, 0);
			c.gridx = 0;
			c.gridy = 5;
			movementSetup.add(knightOn, c);
			c.insets = new Insets(5, 0, 5, 0);
			c.gridx = 0;
			c.gridy = 6;
			movementSetup.add(new JLabel("<html><u>Knight-like Movement Directions:</u></br></html>"), c);
			c.gridx = 0;
			c.gridy = 7;
			movementSetup.add(knightMoving, c);

			popup.add(movementSetup);
			
			//Create button and add ActionListener
			final JButton done = new JButton("Save Piece Type & Quit");
			done.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (name.getText() == "" || PieceBuilder.isPieceType(name.getText())) { //Make sure the name is valid.
						JOptionPane.showMessageDialog(null, "Please enter a unique piece name.",
								"Invalid Piece Name", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					if(knight.isEnabled()){
						if(isIntKnights()){
							builder.addMove('x', Integer.parseInt(knight.getText())); //Knight movements stored in builder.
							builder.addMove('y', Integer.parseInt(knightSecond.getText()));
						}
					}
					builder.setName(name.getText());//Set the name in the PieceBuilder
					PieceBuilder.savePieceType(builder);//Save the piece type in the PieceBuilder class.
					//Put the piece on it's square, refresh the square, dispose the pop up.
					Piece toAdd = PieceBuilder.makePiece(name.getText(), isBlack, square, board);
					(isBlack ? blackTeam : whiteTeam).add(toAdd);
					if(leaper.isSelected()) //Add leaper movement style
						toAdd.setLeaper();
					square.setPiece(toAdd);
					square.refresh();
					popup.dispose();
				}
				
				/**
				 * Determine if the user entered a valid integer.
				 * @return If the text is a valid integer
				 */
				private boolean isIntKnights() {
					try {
						Integer.parseInt(knight.getText());
						Integer.parseInt(knightSecond.getText());
						return true;
					} catch (Exception e) {
						return false;
					}
				}
			});
			popup.add(done);
			final JButton help = new JButton("Help");
			help.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent arg0) {
					JOptionPane.showMessageDialog(CustomSetupMenu.this, 
							"Please enter the specific movement capabilities that you want for this piece.\n" +
							"To add instructions for one direction choose that direction from the drop down menu,\n" +
							"then fill in the farthest distance you would like it to move in that direction.\n" + 
							"When you are finished press the \"Add Movement\" button to add it to the piece.\n" + 
							"Repeat this process for each direction you would like the piece to move.\n" +
							"When you have finished adding movements to the piece press Save and Quit.\n"+
							"This piece will then be available for reuse from the piece selection screen.\n", "Help", 1);
				}
				
			});
			popup.add(help);

			//Create button and add ActionListener
			final JButton cancel = new JButton("Cancel");
			cancel.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					//Dispose of the pop up.
					popup.dispose();
				}
			});
			popup.add(cancel);

			//Finally, set the pop up to visible.
			popup.setVisible(true);
		}

	}

	/**
	 * SetUpListener
	 * 
	 * ActionListener to handle board setup.
	 * 
	 * @author Drew Hannay & Daniel Opdyke
	 */
	class SetUpListener implements ActionListener {

		/**
		 * The Square we are setting up.
		 */
		private Square square;
		/**
		 * The Board on which the Square we are setting up resides.
		 */
		private Board board;

		/**
		 * Constructor
		 * Set instance variables to passed parameters
		 * @param square The square we are setting up.
		 * @param board The board on which the square resides.
		 */
		public SetUpListener(Square square, Board board) {
			this.square = square;
			this.board = board;
		}

		/**
		 * Upon clicking, call the options method passing the square
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			options();
		}

		/**
		 * GUI to set the piece desired for a Square.
		 * Make the pop up, create the components and add their
		 * ActionListeners to collect information.
		 */
		private void askForPiece() {
			//Create the pop up and set the size, location and layout.
			final JFrame popup = new JFrame("Choose Piece");
			popup.setSize(370, 300);
			popup.setLocationRelativeTo(null);
			popup.setLayout(new FlowLayout());

			//Add a JLabel asking which team this Piece will be on.
			popup.add(new JLabel("Which Team?"));

			//Create a ButtonGroup and add the radio buttons to the group.
			ButtonGroup group = new ButtonGroup();
			final JRadioButton white = new JRadioButton("White");
			final JRadioButton black = new JRadioButton("Black");
			group.add(white);
			group.add(black);

			//Add the buttons to the window.
			popup.add(white);
			popup.add(black);

			//Add a JLabel asking for the Piece type, along with a drop down box.
			popup.add(new JLabel("What kind of Piece?"));
			final JComboBox dropdown = new JComboBox(PieceBuilder.getSet().toArray());//Get the list of Piece types.
			dropdown.addItem("New Piece Type");//Also add the option of creating a new Piece type.
			popup.add(dropdown);

			//Create button and add ActionListener
			final JButton done = new JButton("Done");
			done.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					//Make sure they've picked a team.
					if (!white.isSelected() && !black.isSelected()) {
						JOptionPane.showMessageDialog(null, "Please choose a team for this piece.",
								"Incomplete Form", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					//If they want to make a new Piece type, instantiate that class and dispose of this pop up.
					if (dropdown.getSelectedItem().equals("New Piece Type")) {
						new MakeNewPieceType(square, black.isSelected(), board).makePiece();
						popup.dispose();
						return;
					}
					//They must have selected a Piece type that's already defined. Create the Piece.
					Piece p = PieceBuilder.makePiece((String) dropdown.getSelectedItem(), black.isSelected(), square,
							board);
					//Add the Piece to the correct team.
					if (p.isBlack()) {
						blackTeam.add(p);
					} else {
						whiteTeam.add(p);
					}

					//Add the Piece to the square and then refresh the square.
					square.setPiece(p);
					square.refresh();
					popup.dispose();
				}
			});
			popup.add(done);

			//Create button and add ActionListener
			final JButton cancel = new JButton("Cancel");
			cancel.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					//Dispose of the window
					popup.dispose();
				}
			});
			popup.add(cancel);

			//Finally, set the pop up to visible.
			popup.setVisible(true);

		}

		/**
		 * Create a pop up window with customization options.
		 * Let the user choose between adding a piece to the square or
		 * customizing options of the piece.
		 */
		private void options() {
			//Create the pop up and set the size, location and layout.
			final JFrame popup = new JFrame("Customize");
			popup.setSize(300, 160);
			popup.setLocationRelativeTo(null);
			popup.setLayout(new FlowLayout());
			popup.setResizable(false);

			//Create button and add ActionListener
			final JButton squareButton = new JButton("Customize options for this square");
			squareButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					squareOptions();
					popup.dispose();
				}

			});
			popup.add(squareButton);
			//Create button and add ActionListener
			final JButton makePiece = new JButton("Set a piece on this square");
			makePiece.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					//Ask the player for a Piece for the Square the dispose of the pop up.
					if (square.isHabitable()) {
						askForPiece();
					} else {
						JOptionPane.showMessageDialog(popup,
								"This square is uninhabitable, please make it habitable first");
					}
					popup.dispose();
				}
			});
			popup.add(makePiece);
			
			//Button to remove a piece that has been placed on the board
			final JButton removePiece = new JButton("Remove Piece");
			removePiece.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
						square.setPiece(null);
						square.refresh();
						popup.dispose();
				}
			});
			//Only lets the button work if there is a piece there
			if(square.isOccupied()) removePiece.setEnabled(true);
			else removePiece.setEnabled(false);
			popup.add(removePiece);

			//Finally, set the pop up to visible.
			popup.setVisible(true);
		}

		/**
		 * GUI to collect the options desired for a Square.
		 * Make the pop up, create the components and add their
		 * ActionListeners to collect information.
		 */
		private void squareOptions() {
			//Create the pop up and set the size, location and layout.
			final JFrame popup = new JFrame("Square Options");
			popup.setSize(370, 300);
			popup.setLocationRelativeTo(null);
			popup.setLayout(new FlowLayout());

			//Create a JButton to hold the JColorChooser.
			final JButton pickColor = new JButton("Set Square Color");
			pickColor.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					Color color = JColorChooser.showDialog(popup, "Choose Color", square.getColor());
					if (color == null) return;
					if (color != Square.HIGHLIGHT_COLOR) {//Can't let them pick exactly the highlight color, or they could move to that space from anywhere.
						square.setBackgroundColor(color);
						pickColor.setBackground(color);
						//TODO Weird issue if you hit cancel when selecting a color.
					}
					else {
						//The chances of this happening is EXTREMELY small...
						JOptionPane.showMessageDialog(popup, "That color cannot be selected.",
								"Invalid Color", JOptionPane.INFORMATION_MESSAGE);
					}

				}
			});
			popup.add(pickColor);

			//Create the JCheckBox and add it to the board.
			final JCheckBox uninhabitable = new JCheckBox("Uninhabitable", !square.isHabitable());
			popup.add(uninhabitable);

			//Create button and add ActionListener
			final JButton done = new JButton("Done");
			done.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					//Set the Square as habitable or not, then dispose of the pop up.
					square.setHabitable(!uninhabitable.isSelected());
					popup.dispose();
				}
			});
			popup.add(done);

			//Finally, set the pop up as visible.
			popup.setVisible(true);

		}

	}

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 7830479492072657640L;
	// Variables declaration - do not modify
	/**
	 * Builder used to progressively create the new game type.
	 */
	private Builder b;
	/**
	 * ArrayList to hold the pieces on the white team.
	 */
	private ArrayList<Piece> whiteTeam;
	/**
	 * ArrayList to hold the pieces on the black team.
	 */
	private ArrayList<Piece> blackTeam;

	/**
	 * JButton to return to previous screen.
	 */
	private JButton backButton;

	// End of variables declaration

	/**
	 * JButton to submit Board setup and return to the main screen.
	 */
	private JButton submitButton;

	/**
	 * JButton for advanced options.
	 */
	private JButton advancedButton;

	/**
	 * Constructor.
	 * Initialize the ArrayLists and call initComponents to initialize the GUI.
	 * @param b The builder which is creating the new game type.
	 * @param whiteRules The whiteRules object.
	 * @param blackRules The blackRules object.
	 */
	public CustomSetupMenu(Builder b, Rules whiteRules, Rules blackRules) {
		this.b = b;
		whiteTeam = new ArrayList<Piece>();
		blackTeam = new ArrayList<Piece>();
		initComponents(whiteRules, blackRules);
	}

	/**
	 * Initialize components of the GUI
	 * Create all the GUI components, set their specific properties and add them to the 
	 * window. Also add any necessary ActionListeners.
	 * @param whiteRules the rules for white team.
	 * @param blackRules the rules for black team.
	 */
	private void initComponents(final Rules whiteRules, final Rules blackRules) {

		//Set the layout of this JPanel.
		setLayout(new FlowLayout());
		
		setBorder(BorderFactory.createLoweredBevelBorder());
		
		//Get the array of boards from the builder so we can modify it.
		final Board[] boards = b.getBoards();

		//Loop through the array of boards for setup.
		for (int n = 0; n < boards.length; n++) {
			//Create a JPanel to hold the grid and set the layout to the number of squares in the board.
			final JPanel grid = new JPanel();
			grid.setLayout(new GridLayout(boards[n].numRows(), boards[n].numCols()));
			//Set the size of the grid to the number of rows and columns, scaled by 48, the size of the images.
			grid.setPreferredSize(new Dimension(boards[n].numCols() * 48, boards[n].numRows() * 48));

			//Loop through the board, initializing each Square and adding it's ActionListener.
			int numRows = boards[n].numRows();
			int numCols = boards[n].numCols();
			for (int i = numRows; i > 0; i--) {
				for (int j = 1; j <= numCols; j++) {
					JButton jb = new JButton();
					jb.addActionListener(new SetUpListener(boards[n].getSquare(i, j), boards[n]));
					boards[n].getSquare(i, j).setButton(jb);//Let the Square know which button it owns.
					grid.add(jb);//Add the button to the grid.
					boards[n].getSquare(i, j).refresh(); //Have the square refresh all it's properties now that they're created.
				}
			}
			add(grid);//Add the grid to the main JPanel.
		}

		//Create button and add ActionListener
		backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Return to the PlayerCustomMenu screen.
				Driver.getInstance().setPanel(new PlayerCustomMenu(b, whiteRules, blackRules));
			}
		});

		//Create button and add ActionListener
		submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				b.whiteTeam = whiteTeam;
				boolean set = false;
				for (Piece p : whiteTeam) {
					if (p.getName().equals("King")) {
						whiteRules.setObjectivePiece(new ObjectivePiece("classic", "King"));
						set = true;
						break;
					}
					//TODO Fix this.
//					else if (p.isObjective()) {
//						whiteRules.setObjectivePiece(new ObjectivePiece("custom objective", p.getName()));
//						System.out.println(p.getName());
//						set = true;
//						break;
//					}
				}
				if (!set) {
					whiteRules.setObjectivePiece(new ObjectivePiece("no objective", ""));
				}
				b.blackTeam = blackTeam;
				set = false;
				for (Piece p : blackTeam) {
					if (p.getName().equals("King")) {
						blackRules.setObjectivePiece(new ObjectivePiece("classic", "King"));
						set = true;
						break;
					}
					//TODO Fix this
//					else if (p.isObjective()) {
//						blackRules.setObjectivePiece(new ObjectivePiece("custom objective", p.getName()));
//						set = true;
//						break;
//					}
				}
				if (!set) {
					blackRules.setObjectivePiece(new ObjectivePiece("no objective", ""));
				}
				b.writeFile(whiteRules, blackRules);
				//Return to the main screen.
				Driver.getInstance().revertPanel();
			}

		});

		advancedButton = new JButton("Advanced Rules Options");
		advancedButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				final JPanel ldwCheckBox = new JPanel();
				ldwCheckBox.setLayout(new GridLayout(3, 1));
				ldwCheckBox.add(new JCheckBox("Capture Mandatory"));
				ldwCheckBox.add(new JCheckBox("Can't Move Objective"));

				final JPanel obwCheckBox = new JPanel();
				obwCheckBox.setLayout(new GridLayout(5, 1));
				obwCheckBox.add(new JCheckBox("Capture All", false));
				obwCheckBox.add(new JCheckBox("Capture All of Type", false));
				obwCheckBox.add(new JCheckBox("Protect Objective", false));
				obwCheckBox.add(new JCheckBox("Lose All Pieces", false));
				obwCheckBox.add(new JCheckBox("Check # Times", false));

				final JPanel acpwCheckBox = new JPanel();
				acpwCheckBox.setLayout(new GridLayout(4, 1));
				acpwCheckBox.add(new JCheckBox("Capturer changes Color"));
				acpwCheckBox.add(new JCheckBox("Captured piece returns to start"));

				final JPanel ldbCheckBox = new JPanel();
				ldbCheckBox.setLayout(new GridLayout(3, 1));
				ldbCheckBox.add(new JCheckBox("Capture Mandatory"));
				ldbCheckBox.add(new JCheckBox("Can't Move Objective"));

				final JPanel obbCheckBox = new JPanel();
				obbCheckBox.setLayout(new GridLayout(5, 1));
				obbCheckBox.add(new JCheckBox("Capture All", false));
				obbCheckBox.add(new JCheckBox("Capture All of Type", false));
				obbCheckBox.add(new JCheckBox("Protect Objective", false));
				obbCheckBox.add(new JCheckBox("Lose All Pieces", false));
				obbCheckBox.add(new JCheckBox("Check # Times", false));

				final JPanel acpbCheckBox = new JPanel();
				acpbCheckBox.setLayout(new GridLayout(4, 1));
				acpbCheckBox.add(new JCheckBox("Capturer changes Color"));
				acpbCheckBox.add(new JCheckBox("Captured piece returns to start"));

				final JPanel sCheckBox = new JPanel();
				sCheckBox.setLayout(new GridLayout(3, 1));
				sCheckBox.add(new JCheckBox("Pawn Promotion"));
				sCheckBox.add(new JCheckBox("Move to other board"));

				
				final JFrame popup = new JFrame("New Game");
				popup.setLayout(new GridBagLayout());
				popup.setSize(600, 600); //TODO Figure out if there's a better way to set the size of the window.
				popup.setResizable(false);
				popup.setLocationRelativeTo(null);//This line makes the window show up in the center of the user's screen, regardless of resolution.
				GridBagConstraints c = new GridBagConstraints();

				
				//Create button and add ActionListener
				final JButton back = new JButton("Back");
				back.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						//Get rid of this pop up.
						popup.dispose();
					}
				});
				final JButton save = new JButton("Save Settings");
				save.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						if (((Checkbox) ldwCheckBox.getComponent(0)).getState()) {
							whiteRules.addAdjustTeamDests(new AdjustTeamDests("mustCapture"));
						}
						if (((Checkbox) ldwCheckBox.getComponent(1)).getState()) {
							whiteRules.addCropLegalDests("stationaryObjective");
						}
						if (((Checkbox) obwCheckBox.getComponent(0)).getState()) {
							whiteRules.addEndOfGame(new EndOfGame("loseAllPieces", true, 0, ""));
						}
						if (((Checkbox) obwCheckBox.getComponent(1)).getState()) {
							whiteRules.addEndOfGame(new EndOfGame("captureAllOfType", false, 0, "Knight")); //TODO - let the user define this. Currently not supported by GUI.
						}
						if (((Checkbox) obwCheckBox.getComponent(2)).getState()) {
							whiteRules.addEndOfGame(new EndOfGame("classic", false, 0, ""));
						}
						if (((Checkbox) obwCheckBox.getComponent(3)).getState()) {
							whiteRules.addEndOfGame(new EndOfGame("loseAllPieces", false, 0, ""));
						}
						if (((Checkbox) obwCheckBox.getComponent(4)).getState()) {
							whiteRules.addEndOfGame(new EndOfGame("checkNTimes", false, 3, "")); //TODO let the user define this. Currently not supported by GUI.
						}
						if (((Checkbox) acpwCheckBox.getComponent(0)).getState()) {
							whiteRules.addAfterMove("captureTeamSwap");
						}
						if (((Checkbox) acpwCheckBox.getComponent(1)).getState()) {
							whiteRules.addAfterMove("goHome");
						}

						if (((Checkbox) ldbCheckBox.getComponent(0)).getState()) {
							blackRules.addAdjustTeamDests(new AdjustTeamDests("mustCapture"));
						}
						if (((Checkbox) ldbCheckBox.getComponent(1)).getState()) {
							blackRules.addCropLegalDests("stationaryObjective");
						}
						if (((Checkbox) obbCheckBox.getComponent(0)).getState()) {
							blackRules.addEndOfGame(new EndOfGame("loseAllPieces", true, 0, ""));
						}
						if (((Checkbox) obbCheckBox.getComponent(1)).getState()) {
							blackRules.addEndOfGame(new EndOfGame("captureAllOfType", false, 0, "Knight")); //TODO - let the user define this. Currently not supported by GUI.
						}
						if (((Checkbox) obbCheckBox.getComponent(2)).getState()) {
							blackRules.addEndOfGame(new EndOfGame("classic", false, 0, ""));
						}
						if (((Checkbox) obbCheckBox.getComponent(3)).getState()) {
							blackRules.addEndOfGame(new EndOfGame("loseAllPieces", false, 0, ""));
						}
						if (((Checkbox) obbCheckBox.getComponent(4)).getState()) {
							blackRules.addEndOfGame(new EndOfGame("checkNTimes", false, 3, "")); //TODO let the user define this. Currently not supported by GUI.
						}
						if (((Checkbox) acpbCheckBox.getComponent(0)).getState()) {
							blackRules.addAfterMove("captureTeamSwap");
						}
						if (((Checkbox) acpbCheckBox.getComponent(1)).getState()) {
							blackRules.addAfterMove("goHome");
						}
						if (!((Checkbox) sCheckBox.getComponent(0)).getState()) {
							whiteRules.addGetPromotionSquares("noPromos");
							whiteRules.addPromote("noPromos");
							blackRules.addGetPromotionSquares("noPromos");
							blackRules.addPromote("noPromos");
						}
						if (((Checkbox) sCheckBox.getComponent(1)).getState() && boards.length == 2) {
							whiteRules.setGetBoard(new GetBoard("oppositeBoard"));
							blackRules.setGetBoard(new GetBoard("oppositeBoard"));
						}

						//						sCheckBox.add(new Checkbox("Move to other board"));
						popup.dispose();

					}
				}
			);

				JPanel whiteTeam = new JPanel();
				whiteTeam.setBorder(BorderFactory.createTitledBorder("White Team"));
				whiteTeam.setLayout(new GridBagLayout());
				
					JPanel whiteLegalDests = new JPanel();
					whiteLegalDests.setLayout(new GridBagLayout());
						c.gridx = 0;
						c.gridy = 1;
						whiteLegalDests.add(new JLabel("<html><u> Legal Destination </u></br></html>"), c);
						c.gridx = 0;
						c.gridy = 2;
						whiteLegalDests.add(ldwCheckBox, c);
						
				c.gridx = 0;
				c.gridy = 0;
				whiteTeam.add(whiteLegalDests, c);
					
					JPanel whiteObj = new JPanel();
					whiteObj.setLayout(new GridBagLayout());
						c.gridheight = 1;
						c.gridx = 0;
						c.gridy = 1;
						whiteObj.add(new JLabel("<html><u> Objective </u></br></html>"), c);
						c.gridx = 0;
						c.gridy = 2;
						whiteObj.add(obwCheckBox, c);
						c.gridx = 0;
						c.gridy = 3;
						whiteObj.add(new JLabel(" "), c);
						
				c.gridx = 0;
				c.gridy = 1;
				whiteTeam.add(whiteObj, c);
						
					JPanel whiteCapture = new JPanel();
					whiteCapture.setLayout(new GridBagLayout());
						c.gridheight = 1;
						c.gridx = 0;
						c.gridy = 1;
						whiteCapture.add(new JLabel("<html><u>After Capturing a piece</u></br></html>"), c);
						c.gridx = 0;
						c.gridy = 2;
						whiteCapture.add(acpwCheckBox, c);
					
				c.gridx = 0;
				c.gridy = 2;
				whiteTeam.add(whiteCapture, c);
				
				
				JPanel blackTeam = new JPanel();
				blackTeam.setBorder(BorderFactory.createTitledBorder("Black Team"));
				blackTeam.setLayout(new GridBagLayout());
				
					JPanel blackLegalDests = new JPanel();
					blackLegalDests.setLayout(new GridBagLayout());
						c.gridx = 0;
						c.gridy = 1;
						blackLegalDests.add(new JLabel("<html><u> Legal Destination </u></br></html>"), c);
						c.gridx = 0;
						c.gridy = 2;
						blackLegalDests.add(ldbCheckBox, c);
						
				c.gridx = 0;
				c.gridy = 0;
				blackTeam.add(blackLegalDests, c);	
					
					JPanel blackObj = new JPanel();
					blackObj.setLayout(new GridBagLayout());
						c.gridheight = 1;
						c.gridx = 0;
						c.gridy = 1;
						blackObj.add(new JLabel("<html><u> Objective </u></br></html>"), c);
						c.gridx = 0;
						c.gridy = 2;
						blackObj.add(obbCheckBox, c);
						c.gridx = 0;
						c.gridy = 3;
						blackObj.add(new JLabel(" "), c);
						
				c.gridx = 0;
				c.gridy = 1;
				blackTeam.add(blackObj, c);
				
					JPanel blackCapture = new JPanel();
					blackCapture.setLayout(new GridBagLayout());
						c.gridheight = 1;
						c.gridx = 0;
						c.gridy = 1;
						blackCapture.add(new JLabel("<html><u>After Capturing a piece</u></br></html>"), c);
						c.gridx = 0;
						c.gridy = 2;
						blackCapture.add(acpbCheckBox, c);
				
				c.gridx = 0;
				c.gridy = 2;
				blackTeam.add(blackCapture, c);
					
					
				c.insets = new Insets(10, 10, 10, 10);
				c.gridx = 0;
				c.gridy = 0;
				popup.add(whiteTeam, c);
				
				c.insets = new Insets(10, 10, 10, 10);
				c.gridx = 1;
				c.gridy = 0;
				popup.add(blackTeam, c);
				
				
				JPanel specialRules = new JPanel();
				specialRules.setBorder(BorderFactory.createTitledBorder("Special rules"));
				specialRules.setLayout(new GridBagLayout());
				
					c.gridx = 0;
					c.gridy = 1;
					specialRules.add(sCheckBox, c);
					c.gridx = 0;
					c.gridy = 2;
				
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.gridy = 1;
				c.gridwidth = 2;
				popup.add(specialRules, c);
				
				
				JPanel buttons = new JPanel();
				buttons.setLayout(new FlowLayout());
				buttons.add(back);
				buttons.add(save);
					
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.gridy = 2;
				c.gridwidth = 2;
				popup.add(buttons, c);
				
				
				popup.setVisible(true);
			}
		});

		JPanel options = new JPanel();
		options.setBorder(BorderFactory.createTitledBorder("Options"));
		options.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		//Add the buttons to the JPanel.
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		options.add(advancedButton, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		options.add(submitButton, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		options.add(backButton, c);
		
		add(options);
	}
	/**
	 * Makes the icon for the for the new piece
	 * @param fc The file chooser to select the piece
	 * @param builder The builder that is building the piece
	 * @return the icon to add to the button displaying the icon for the new piece
	 */
	public ImageIcon makeIcon(JFileChooser fc, PieceBuilder builder){
		//If a valid File was chosen, make an ImageIcon from the file path.
		String file = fc.getSelectedFile().getAbsolutePath();
		
		// default center section
		ImageIcon icon = new ImageIcon(file);
		//Scale the image to 48x48.
		icon.setImage(icon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH));
		builder.setLightImage(icon);
		return icon;
	}
}
