package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
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
 * GUI to handle setup of pieces on the board and special square properties.
 * 
 * @author Drew Hannay & Daniel Opdyke & Yelemis Soung & Cheney Hester & John
 * McCormick
 * 
 * CSCI 335, Wheaton College, Spring 2011 Phase 1 February 25, 2011
 */
public class CustomSetupMenu extends JPanel
{

	/**
	 * @param dragged This is the piece getting dragged to a new location
	 */
	private Square dragged;

	/**
	 * @param bShowPiece Holder squares for placing pieces
	 */
	final Board bShowPiece;

	/**
	 * Creates a pop up box for the Promotion Options.
	 * 
	 * @param type The type of piece that can be promoted.
	 * 
	 */
	private void promotion(final String type)
	{
		// Create the pop up and set the size, location, and layout.
		final JFrame popup = new JFrame("Promotion Options");
		popup.setSize(500, 300);
		popup.setLocationRelativeTo(null);
		popup.setLayout(new GridBagLayout());
		popup.setResizable(false);

		// EMPTY LIST
		final DefaultListModel emptyList = new DefaultListModel();
		// LIST - CANT PROMOTE TO
		final DefaultListModel list = new DefaultListModel();
		Object[] allPieces = PieceBuilder.getSet().toArray();
		for (int i = 0; i < allPieces.length; i++)
		{
			if (!allPieces[i].equals(type))
				list.addElement(allPieces[i]);
		}
		final JList piecesList = new JList(list);
		// EMPTY LIST - CAN PROMOTE TO
		final JList piecesList2 = new JList(emptyList);

		/*
		 * ARROW PANEL
		 */

		// moveLeft This button will move a selected piece from the right list
		// to the left list.
		final JButton moveLeft = new JButton();
		moveLeft.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					int index = piecesList2.getSelectedIndex();

					// Add the element of the right list to the left list.
					list.addElement(emptyList.elementAt(index));
					emptyList.remove(index);
				}
				catch (Exception e)
				{
				}
			}
		});
		moveLeft.setText("<---");

		// moveRight This button will move a selected piece from the left list
		// to the right list.
		final JButton moveRight = new JButton();
		moveRight.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				int index = piecesList.getSelectedIndex();

				// Add the element of the left list to the right list.
				emptyList.addElement(list.elementAt(index));
				list.remove(index);

			}
		});
		moveRight.setText("--->");

		// LIST - CANT PROMOTE TO
		piecesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		piecesList.setLayoutOrientation(JList.VERTICAL);
		piecesList.setVisibleRowCount(-1);
		piecesList.setSelectedIndex(0);
		// EMPTY LIST - CAN PROMOTE TO
		piecesList2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		piecesList2.setLayoutOrientation(JList.VERTICAL);
		piecesList2.setVisibleRowCount(-1);
		piecesList2.setSelectedIndex(0);

		// LIST - CANT PROMOTE TO
		JScrollPane scrollPane = new JScrollPane(piecesList);
		scrollPane.setPreferredSize(new Dimension(200, 200));
		ListSelectionModel selectList = piecesList.getSelectionModel();
		selectList.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				if (e.getValueIsAdjusting() == false)
				{ // If the user is still selecting.

					// If the user has not selected anything yet.
					if (piecesList.getSelectedIndex() == -1)
					{
						// No selection, disable fire button.
						moveLeft.setEnabled(false);
						moveRight.setEnabled(false);

					}
					else
					{
						// Selection, enable the fire button.
						moveLeft.setEnabled(true);
						moveRight.setEnabled(true);
					}
				}
			}
		});

		// EMPTY LIST - CAN PROMOTE TO
		JScrollPane scrollPane2 = new JScrollPane(piecesList2);
		scrollPane2.setPreferredSize(new Dimension(200, 200));
		ListSelectionModel selectList2 = piecesList2.getSelectionModel();
		selectList2.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				if (e.getValueIsAdjusting() == false)
				{ // If the user is still selecting.

					// If the user has not selected anything yet.
					if (piecesList2.getSelectedIndex() == -1)
					{
						// No selection, disable the buttons.
						moveLeft.setEnabled(false);
						moveRight.setEnabled(false);

					}
					else
					{
						// Selection, enable the buttons.
						moveLeft.setEnabled(true);
						moveRight.setEnabled(true);
					}
				}
			}
		});
		// //////////////////////////SHOWING GUI/////////////////////////////
		/*
		 * Add the Submit and Back Buttons.
		 */
		JButton submitButton = new JButton("Save");
		submitButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				ArrayList<String> promotesTo = new ArrayList<String>();
				for (int i = 0; i < emptyList.size(); i++)
				{
					promotesTo.add((String) emptyList.get(i));
				}
				promotions.put(type, promotesTo);
				popup.dispose();
			}

		});
		JButton backButton = new JButton("Cancel");
		backButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				popup.dispose();
			}
		});

		// This adds a new Panel with the option buttons.
		JPanel options = new JPanel();
		options.add(backButton);
		options.add(submitButton);

		// This panel holds just the arrows. This will be placed
		// in between the two lists.
		JPanel otherCrap = new JPanel();
		otherCrap.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		otherCrap.add(moveRight, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		otherCrap.add(moveLeft, c);
		// LIST - CANT PROMOTE TO
		c.gridx = 0;
		c.gridy = 0;
		popup.add(new JLabel("Can't Promote To"), c);
		c.gridx = 0;
		c.gridy = 1;
		popup.add(scrollPane, c);
		c.gridx = 1;
		c.gridy = 1;
		popup.add(otherCrap, c);
		// EMPTY LIST - CAN PROMOTE TO
		c.gridx = 2;
		c.gridy = 0;
		popup.add(new JLabel("Can Promote To"), c);
		c.gridx = 2;
		c.gridy = 1;
		popup.add(scrollPane2, c);
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 3;
		popup.add(options, c);
		// Finally, set the pop up to visible.
		popup.setVisible(true);
	}

	/**
	 * Rules holder for the white rules
	 */
	private Rules whiteRules;
	/**
	 * Rules holder for the black rules
	 */
	private Rules blackRules;
	/**
	 * Hashmap for keeping track of what types promote to which pieces.
	 */
	private HashMap<String, ArrayList<String>> promotions = new HashMap<String, ArrayList<String>>();

	/**
	 * Constructor. Initialize the ArrayLists and call initComponents to
	 * initialize the GUI.
	 * 
	 * @param b The builder which is creating the new game type.
	 * @param whiteRules The whiteRules object.
	 * @param blackRules The blackRules object.
	 */
	public CustomSetupMenu(Builder b, Rules whiteRules, Rules blackRules)
	{
		this.b = b;
		whiteTeam = new ArrayList<Piece>();
		blackTeam = new ArrayList<Piece>();
		this.whiteRules = whiteRules;
		this.blackRules = blackRules;
		bShowPiece = new Board(2, 1, false);
		dragged = new Square(0, 0);
		initComponents();
	}

	class DragMouseAdapter extends MouseAdapter
	{

		/**
		 * The Square we are setting up.
		 */
		private Square square;
		/**
		 * The Board on which the Square we are setting up resides.
		 */
		private Board board;

		/**
		 * @param square The square this is attached to
		 * @param board The current board
		 */
		public DragMouseAdapter(Square square, Board board)
		{
			this.square = square;
			this.board = board;
		}

		private void squareOptions()
		{
			// Create the pop up and set the size, location and layout.
			final JFrame popup = new JFrame("Square Options");
			popup.setSize(370, 120);
			popup.setLocationRelativeTo(null);
			popup.setLayout(new FlowLayout());

			final ImageIcon uninhabIcon = new ImageIcon("./images/Uninhabitable.png");

			// Create a JButton to hold the JColorChooser.
			final JButton pickColor = new JButton("Set Square Color");
			pickColor.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					Color color = JColorChooser.showDialog(popup, "Choose Color", square.getColor());
					if (color == null)
						return;
					if (color != Square.HIGHLIGHT_COLOR)
					{// Can't let them pick exactly the highlight color, or they
						// could move to that space from anywhere.
						square.setBackgroundColor(color);
						pickColor.setBackground(color);
					}
					else
					{
						// The chances of this happening is EXTREMELY small...
						JOptionPane.showMessageDialog(popup, "That color cannot be selected.", "Invalid Color",
								JOptionPane.INFORMATION_MESSAGE);
					}

				}
			});
			popup.add(pickColor);

			// Create the JCheckBox and add it to the board.
			final JCheckBox uninhabitable = new JCheckBox("Uninhabitable", !square.isHabitable());
			popup.add(uninhabitable);

			// Create button and add ActionListener
			final JButton done = new JButton("Done");
			done.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					// Set the Square as habitable or not, then dispose of the
					// pop up.
					if (uninhabitable.isSelected())
					{
						square.setHabitable(false);
						square.setIcon(uninhabIcon);
					}
					else
					{
						square.setHabitable(true);
						square.setIcon(null);
					}
					popup.dispose();
				}
			});
			popup.add(done);

			// Finally, set the pop up as visible.
			popup.setVisible(true);

		}

		/**
		 * Place the piece on the board.
		 * 
		 * @param isBlack Is this piece black?
		 */
		private void setPieceOnBoard(boolean isBlack)
		{
			if (square.isHabitable())
			{
				Piece p = PieceBuilder.makePiece(dragged.getPiece().getName(), isBlack, square, board);
				if (isBlack)
					blackTeam.add(p);
				else
					whiteTeam.add(p);
				square.setPiece(p);
				square.refresh();
				dragged.setPiece(null);
				bShowPiece.getSquare(1, 1).resetColor();
				bShowPiece.getSquare(2, 1).resetColor();
			}
			else
			{
				JOptionPane.showMessageDialog(null, "This square is uninhabitable.", "Warning", JOptionPane.WARNING_MESSAGE);
			}
		}

		/**
		 * Pulls up square options if no piece has been choosen to add or no
		 * piece is present. Deletes any pieces currently on the square
		 */
		@Override
		public void mousePressed(MouseEvent e)
		{
		}

		@Override
		public void mouseClicked(MouseEvent e)
		{
			if (square.isOccupied())
			{
				Piece toRemove = square.getPiece();
				(toRemove.isBlack() ? blackTeam : whiteTeam).remove(toRemove);
				square.setPiece(null);
				square.refresh();
			}
			else if (dragged.isOccupied())
			{
				setPieceOnBoard(dragged.getPiece().isBlack());
			}
			else
				squareOptions();
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
	 * Initialize components of the GUI Create all the GUI components, set their
	 * specific properties and add them to the window. Also add any necessary
	 * ActionListeners.
	 */
	private void initComponents()
	{

		// Set the layout of this JPanel.
		setLayout(new FlowLayout());

		setBorder(BorderFactory.createLoweredBevelBorder());

		// Get the array of boards from the builder so we can modify it.
		final Board[] boards = b.getBoards();

		// Create a List with a vertical ScrollBar
		final DefaultListModel list = new DefaultListModel();
		Object[] allPieces = PieceBuilder.getSet().toArray();
		for (int i = 0; i < allPieces.length; i++)
		{
			list.addElement(allPieces[i]);
		}
		final JList piecesList = new JList(list);

		final JPanel showPiece = new JPanel();
		showPiece.setLayout(new GridLayout(2, 1));
		showPiece.setPreferredSize(new Dimension(50, 100));

		bShowPiece.getSquare(1, 1).addMouseListener(new MouseListener()
		{
			@Override
			public void mousePressed(MouseEvent arg0)
			{
				if (dragged.getPiece() == null)
				{
					bShowPiece.getSquare(1, 1).setBackgroundColor(Square.HIGHLIGHT_COLOR);
					dragged.setPiece(bShowPiece.getSquare(1, 1).getPiece());
				}
				else
				{
					dragged.setPiece(null);
					bShowPiece.getSquare(1, 1).resetColor();
				}
			}

			@Override
			public void mouseReleased(MouseEvent arg0)
			{
			}

			@Override
			public void mouseExited(MouseEvent arg0)
			{
			}

			@Override
			public void mouseEntered(MouseEvent arg0)
			{
			}

			@Override
			public void mouseClicked(MouseEvent arg0)
			{
			}
		});
		bShowPiece.getSquare(2, 1).addMouseListener(new MouseListener()
		{
			@Override
			public void mousePressed(MouseEvent arg0)
			{
				if (dragged.getPiece() == null)
				{
					bShowPiece.getSquare(2, 1).setBackgroundColor(Square.HIGHLIGHT_COLOR);
					dragged.setPiece(bShowPiece.getSquare(2, 1).getPiece());
				}
				else
				{
					dragged.setPiece(null);
					bShowPiece.getSquare(2, 1).resetColor();
				}
			}

			@Override
			public void mouseReleased(MouseEvent arg0)
			{
			}

			@Override
			public void mouseExited(MouseEvent arg0)
			{
			}

			@Override
			public void mouseEntered(MouseEvent arg0)
			{
			}

			@Override
			public void mouseClicked(MouseEvent arg0)
			{
			}
		});

		showPiece.add(bShowPiece.getSquare(1, 1));
		showPiece.add(bShowPiece.getSquare(2, 1));

		piecesList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		piecesList.setLayoutOrientation(JList.VERTICAL);
		piecesList.setVisibleRowCount(-1);
		piecesList.setSelectedIndex(0);
		Piece toAdd = PieceBuilder.makePiece((String) list.elementAt(0), false, bShowPiece.getSquare(1, 1), bShowPiece);
		Piece toAdd1 = PieceBuilder.makePiece((String) list.elementAt(0), true, bShowPiece.getSquare(2, 1), bShowPiece);
		bShowPiece.getSquare(1, 1).setPiece(toAdd);
		bShowPiece.getSquare(2, 1).setPiece(toAdd1);
		bShowPiece.getSquare(1, 1).refresh();
		bShowPiece.getSquare(2, 1).refresh();

		ListSelectionModel selectList = piecesList.getSelectionModel();
		final Color original = bShowPiece.getSquare(1, 1).getColor();

		JScrollPane scrollPane = new JScrollPane(piecesList);
		scrollPane.setPreferredSize(new Dimension(200, 200));
		// Loop through the array of boards for setup.
		for (int n = 0; n < boards.length; n++)
		{
			// Create a JPanel to hold the grid and set the layout to the number
			// of squares in the board.
			final JPanel grid = new JPanel();
			grid.setLayout(new GridLayout(boards[n].numRows(), boards[n].numCols()));
			// Set the size of the grid to the number of rows and columns,
			// scaled by 48, the size of the images.
			grid.setPreferredSize(new Dimension(boards[n].numCols() * 48, boards[n].numRows() * 48));

			// Loop through the board, initializing each Square and adding it's
			// ActionListener.
			int numRows = boards[n].numRows();
			int numCols = boards[n].numCols();
			for (int i = numRows; i > 0; i--)
			{
				for (int j = 1; j <= numCols; j++)
				{
					boards[n].getSquare(i, j).addMouseListener(new DragMouseAdapter(boards[n].getSquare(i, j), boards[n]));
					grid.add(boards[n].getSquare(i, j));
					boards[n].getSquare(i, j).refresh(); // Have the square
															// refresh all it's
															// properties now
															// that they're
															// created.
				}
			}
			add(grid);// Add the grid to the main JPanel.
		}

		final JButton changePromote = new JButton("Promote This Piece");
		changePromote.setToolTipText("Press me to set up promotion for the above selected piece");
		changePromote.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				promotion((String) piecesList.getSelectedValue());
			}

		});
		selectList.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				ListSelectionModel lsm = (ListSelectionModel) e.getSource();

				int selection = lsm.getAnchorSelectionIndex();
				if (!lsm.getValueIsAdjusting())
				{
					if (((String) list.elementAt(selection)).equals("Square Options"))
					{
						bShowPiece.getSquare(1, 1).setPiece(null);
						bShowPiece.getSquare(1, 1).setBackgroundColor(original);
						bShowPiece.getSquare(1, 1).setHabitable(true);
						bShowPiece.getSquare(1, 1).refresh();
						bShowPiece.getSquare(1, 1).setVisible(true);
						bShowPiece.getSquare(2, 1).setVisible(false);
						changePromote.setEnabled(false);
					}
					else
					{
						bShowPiece.getSquare(2, 1).setVisible(true);
						bShowPiece.getSquare(1, 1).setVisible(true);
						changePromote.setEnabled(true);
						if (bShowPiece.getSquare(1, 1).getColor().equals(original) == false)
							bShowPiece.getSquare(1, 1).setBackgroundColor(original);
						Piece toAdd = PieceBuilder.makePiece((String) list.elementAt(selection), false, bShowPiece.getSquare(1, 1),
								bShowPiece);
						Piece toAdd1 = PieceBuilder.makePiece((String) list.elementAt(selection), true, bShowPiece.getSquare(2, 1),
								bShowPiece);
						bShowPiece.getSquare(1, 1).setPiece(toAdd);
						bShowPiece.getSquare(2, 1).setPiece(toAdd1);
						bShowPiece.getSquare(1, 1).refresh();
						bShowPiece.getSquare(2, 1).refresh();
					}
				}
			}
		});

		GridBagConstraints c = new GridBagConstraints();
		JPanel pieceHolder = new JPanel();
		pieceHolder.setLayout(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		pieceHolder.add(scrollPane, c);
		c.gridx = 0;
		c.gridy = 1;
		pieceHolder.add(changePromote, c);

		add(showPiece);
		add(pieceHolder);

		// Create button and add ActionListener
		backButton = new JButton("Back");
		backButton.setToolTipText("Press me to go back to the Turn setup window");
		backButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				Driver.getInstance().setPanel(new PlayerCustomMenu(b, whiteRules, blackRules));
			}
		});

		// Create button and add ActionListener
		submitButton = new JButton("Save and Quit");
		submitButton.setToolTipText("Press me to save your finished variant");
		submitButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				for (Piece p : whiteTeam)
				{
					p.setPromotesTo(promotions.get(p.getName()));
				}
				for (Piece p : blackTeam)
				{
					p.setPromotesTo(promotions.get(p.getName()));
				}
				int numObjectives = 0;
				if (!whiteRules.getObjectiveName().equals(""))
				{
					for (Piece p : whiteTeam)
					{
						if (p.getName().equals(whiteRules.getObjectiveName()))
							numObjectives++;
					}
					if (numObjectives != 1)
					{
						JOptionPane.showMessageDialog(null, "Please place exactly one White Objective Piece");
						return;
					}
				}
				numObjectives = 0;
				if (!blackRules.getObjectiveName().equals(""))
				{
					for (Piece p : blackTeam)
					{
						if (p.getName().equals(blackRules.getObjectiveName()))
							numObjectives++;
					}
					if (numObjectives != 1)
					{
						JOptionPane.showMessageDialog(null, "Please place exactly one Black Objective Piece");
						return;
					}
				}
				b.whiteTeam = whiteTeam;
				boolean set = false;
				for (Piece p : whiteTeam)
				{
					if (p.getName().equals("King"))
					{
						whiteRules.setObjectivePiece(new ObjectivePiece("classic", "King"));
						set = true;
						break;
					}
					// TODO Fix this.
					// else if (p.isObjective()) {
					// whiteRules.setObjectivePiece(new
					// ObjectivePiece("custom objective", p.getName()));
					// System.out.println(p.getName());
					// set = true;
					// break;
					// }
				}
				if (!set)
				{
					whiteRules.setObjectivePiece(new ObjectivePiece("no objective", ""));
				}
				b.blackTeam = blackTeam;
				set = false;
				for (Piece p : blackTeam)
				{
					if (p.getName().equals("King"))
					{
						blackRules.setObjectivePiece(new ObjectivePiece("classic", "King"));
						set = true;
						break;
					}
					// TODO Fix this
					// else if (p.isObjective()) {
					// blackRules.setObjectivePiece(new
					// ObjectivePiece("custom objective", p.getName()));
					// set = true;
					// break;
					// }
				}
				if (!set)
				{
					blackRules.setObjectivePiece(new ObjectivePiece("no objective", ""));
				}
				b.writeFile(whiteRules, blackRules);
				// Return to the main screen.
				Driver.getInstance().revertPanel();
			}

		});

		JPanel options = new JPanel();
		options.setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		options.add(submitButton, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		options.add(backButton, c);

		add(options);
	}
}
