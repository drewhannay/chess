package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import logic.Board;
import logic.Builder;
import logic.Piece;
import logic.PieceBuilder;
import logic.Square;
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
			//TODO Hello!!!
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

	class SetUpMouseListener implements MouseListener {
		//TODO
		/**
		 * The Squares we are setting up.
		 */
		private Square square, option;
		/**
		 * The Board on which the Square we are setting up resides.
		 */
		private Board board;
		
		public SetUpMouseListener(Square square, Board board, Square option){
			this.square = square;
			this.board = board;
			this.option = option;
		}
		
		@Override
		public void mouseClicked(MouseEvent e){
			
			int mods = e.getModifiers();
					if (mods == 16){
						setPieceOnBoard(false);
					}
					else if (mods == 4){
						setPieceOnBoard(true);
					}
		}

		private void setPieceOnBoard(boolean isBlack){
			System.out.println(option.isHabitable());
			if (option.isOccupied() == false){
				square.setBackgroundColor(option.getColor());
				square.setHabitable(option.isHabitable());
				square.refresh();
				square.setPiece(null);
				System.out.println(option.getColor());
			}
			else{
				if (square.isHabitable() == true){
					Piece p = PieceBuilder.makePiece(option.getPiece().getName(), isBlack, square, board);
					if (isBlack)
						blackTeam.add(p);
					else
						whiteTeam.add(p);
					square.setPiece(p);
					square.refresh();
				}
				else{
					JOptionPane.showMessageDialog(null,
						    "Eggs are not supposed to be green.",
						    "Inane warning",
						    JOptionPane.WARNING_MESSAGE);

				}
			}
		}
		
		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
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
	 * Constructor.
	 * Initialize the ArrayLists and call initComponents to initialize the GUI.
	 * @param b The builder which is creating the new game type.
	 * @param whiteRules The whiteRules object.
	 * @param blackRules The blackRules object.
	 * @return 
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

		final Board bShowPiece = new Board(1,1,false);
		//final Square sShowPiece = new Square(1,1);
		final JPanel showPiece = new JPanel();
		final Piece p = PieceBuilder.makePiece("Pawn", true, bShowPiece.getSquare(1,1), bShowPiece);
		showPiece.setLayout(new GridLayout(1,1));
		showPiece.setPreferredSize(new Dimension(50,50));
		
		JButton jb1 = new JButton();
		jb1.addActionListener(new SetUpListener(bShowPiece.getSquare(1, 1), bShowPiece));
		bShowPiece.getSquare(1, 1).setButton(jb1);//Let the Square know which button it owns.
		showPiece.add(jb1);//Add the button to the grid.
		bShowPiece.getSquare(1, 1).refresh();
		
		 // Create a List with a vertical ScrollBar
		final DefaultListModel list = new DefaultListModel();
		Object[] allPieces = PieceBuilder.getSet().toArray();
		for (int i = 0; i<allPieces.length; i++){
			list.addElement(allPieces[i]);
		}
		list.addElement("Square Options");
	    JList piecesList = new JList(list);
	    
	    piecesList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	    piecesList.setLayoutOrientation(JList.VERTICAL);
	    piecesList.setVisibleRowCount(-1);
	    
	    ListSelectionModel selectList = piecesList.getSelectionModel();
	    final Color original = bShowPiece.getSquare(1, 1).getColor();
	    selectList.addListSelectionListener(new ListSelectionListener(){
	    	//TODO
			@Override
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = (ListSelectionModel) e.getSource();
				
		        int firstIndex = e.getFirstIndex();
		        int lastIndex = e.getLastIndex();
		        int selection = lsm.getAnchorSelectionIndex();
		        if (!lsm.getValueIsAdjusting()){
			       	if (((String) list.elementAt(selection)).equals("Square Options")){
			       			bShowPiece.getSquare(1, 1).setPiece(null);
			       			bShowPiece.getSquare(1, 1).setBackgroundColor(original);
			       			bShowPiece.getSquare(1, 1).setHabitable(true);
			       			bShowPiece.getSquare(1, 1).refresh();
			       	}
			        else{
			        	if (bShowPiece.getSquare(1, 1).isHabitable() == false)
			        		bShowPiece.getSquare(1, 1).setHabitable(true);
			        	if (bShowPiece.getSquare(1, 1).getColor().equals(original) == false)
			        		bShowPiece.getSquare(1, 1).setBackgroundColor(original);	
			        	Piece toAdd = PieceBuilder.makePiece((String) list.elementAt(selection), true, bShowPiece.getSquare(1,1), bShowPiece);
			        	bShowPiece.getSquare(1, 1).setPiece(toAdd);
			        	bShowPiece.getSquare(1, 1).refresh();
		       		}
		        }
			}
	    });

	    JScrollPane scrollPane = new JScrollPane(piecesList);
	    scrollPane.setPreferredSize(new Dimension(200, 200));
	    
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
					jb.addMouseListener(new SetUpMouseListener(boards[n].getSquare(i, j), boards[n], bShowPiece.getSquare(1, 1)));
					boards[n].getSquare(i, j).setButton(jb);//Let the Square know which button it owns.
					grid.add(jb);//Add the button to the grid.
					boards[n].getSquare(i, j).refresh(); //Have the square refresh all it's properties now that they're created.
				}
			}
			add(grid);//Add the grid to the main JPanel.
		}

	    add(scrollPane);
	    add(showPiece);
	    
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

		JPanel options = new JPanel();
		options.setBorder(BorderFactory.createTitledBorder("Options"));
		options.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
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
