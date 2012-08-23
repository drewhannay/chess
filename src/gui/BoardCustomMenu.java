package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import logic.Board;

/**
 * BoardCustomMenu.java
 * 
 * GUI to handle customization of game boards.
 * 
 * @author Drew Hannay & Daniel Opdyke & John McCormick
 * 
 * CSCI 335, Wheaton College, Spring 2011 Phase 1 February 24, 2011
 */
public class BoardCustomMenu extends JPanel
{

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 5842202724028685933L;

	// Variables declaration - do not modify
	/**
	 * JLabel with instructions for the number of boards fields.
	 */
	private JLabel numBoardsLabel;
	/**
	 * JRadioButton to select the one board option
	 */
	private JRadioButton oneBoard;
	/**
	 * JRadioButton to select the two board option
	 */
	private JRadioButton twoBoards;
	/**
	 * JLabel with instructions for the dimensions fields.
	 */
	private JLabel dimensionsLabel;
	/**
	 * JLabel to indicate which field is for the x dimension.
	 */
	private JLabel numRowsLabel;
	/**
	 * JTextField to collect an integer for the x dimension.
	 */
	private JTextField numRows;
	/**
	 * JLabel to indicate which field is for the y dimension.
	 */
	private JLabel numColsLabel;
	/**
	 * JTextField to collect an integer for the y dimension.
	 */
	private JTextField numCols;
	/**
	 * JLabel to indicate whether the boards should wrap horizontally.
	 */
	private JLabel wraparoundLabel;
	/**
	 * JCheckBox to decide if boards should wrap horizontally.
	 */
	private JCheckBox wraparound;
	/**
	 * JButton to return to previous screen.
	 */
	private JButton backButton;
	/**
	 * JButton to submit board customizations and move to next screen.
	 */
	private JButton submitButton;
	/**
	 * Frame to hold the window
	 */
	private JFrame frame;
	/**
	 * holder for this BoardCustomMenu
	 */
	private BoardCustomMenu holder = this;

	// End of variables declaration

	/**
	 * Constructor. Call initComponents to initialize the GUI.
	 * 
	 * @param b The builder which is creating the new game type.
	 */
	/*
	 * public BoardCustomMenu(Builder b) { this.b = b; initComponents(); }
	 */

	public BoardCustomMenu(CustomSetupMenu variant, JFrame optionsFrame)
	{
		frame = optionsFrame;
		frame.setVisible(true);
		frame.add(this);
		frame.setVisible(true);
		frame.setSize(300, 250);
		frame.setLocationRelativeTo(Driver.getInstance());
		initComponents(variant);
	}

	/**
	 * Determine if this form has been correctly filled out. Check that a number
	 * of boards is selected, and that the dimension fields contain valid
	 * integers.
	 * 
	 * @return Whether or not the form is complete.
	 */
	private boolean formComplete()
	{
		if (!oneBoard.isSelected() && !twoBoards.isSelected())
		{
			JOptionPane.showMessageDialog(null, "Please select the number of boards.", "Incomplete Form",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		try
		{
			int a = Integer.parseInt(numRows.getText());
			int b = Integer.parseInt(numCols.getText());
			if (a < 3 || b < 3 || a > 16 || b > 16)
				throw new Exception();
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Enter a valid number between 3 and 16 for the dimensions of the board.",
					"Invalid Submission", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		try
		{
			int a = Integer.parseInt(numRows.getText());
			int b = Integer.parseInt(numCols.getText());
			if (twoBoards.isSelected() && (a < 3 || b < 3 || a > 10 || b > 10))
				throw new Exception();
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Enter a valid number between 3 and 10 for the dimensions of the board.",
					"Invalid Submission", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		return true;
	}

	/**
	 * Initialize components of the GUI Create all the GUI components, set their
	 * specific properties and add them to the window. Also add any necessary
	 * ActionListeners.
	 */
	private void initComponents(final CustomSetupMenu variant)
	{

		setBorder(BorderFactory.createLoweredBevelBorder());
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		revalidate();
		repaint();
		// Create button and add ActionListener
		backButton = new JButton("Cancel");
		backButton.setToolTipText("Press me to go back to the main variant setup");
		backButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				holder.removeAll();
				frame.setVisible(false);
			}
		});

		// Create JLabels and JRadioButtons.
		numBoardsLabel = new JLabel("How many boards?");
		oneBoard = new JRadioButton("1");
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				oneBoard.requestFocus();
			}
		});
		oneBoard.setToolTipText("Choose me for one Board");
		// Set oneBoard to be initially selected.
		twoBoards = new JRadioButton("2");
		twoBoards.setToolTipText("Choose me for two boards");

		JPanel boards = new JPanel();
		boards.add(oneBoard);
		boards.add(twoBoards);

		Board[] board = variant.getBuilder().getBoards();

		if (board.length == 1)
		{
			oneBoard.setSelected(true);
		}
		else
		{
			twoBoards.setSelected(true);
		}

		// This is important! Add the buttons to a group so only one can be
		// selected at a time.
		ButtonGroup group = new ButtonGroup();
		group.add(oneBoard);
		group.add(twoBoards);

		// Create JLabels and JTextFields. Default size 8*8
		dimensionsLabel = new JLabel("Dimensions?");
		numRowsLabel = new JLabel("Rows:");
		numRows = new JTextField(5);
		numRows.setText(board[0].numRows() + "");
		numRows.setToolTipText("Enter the amount of rows you would like");
		numColsLabel = new JLabel("Columns:");
		numCols = new JTextField(5);
		numCols.setText(board[0].numCols() + "");
		numCols.setToolTipText("Enter the amount of columns you would like");

		JPanel rowCol = new JPanel();
		rowCol.setLayout(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(2, 15, 1, 5);
		rowCol.add(numRowsLabel, c);
		c.gridx = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		rowCol.add(numRows, c);
		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.NONE;
		rowCol.add(numColsLabel, c);
		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		rowCol.add(numCols, c);

		// Create JLabel and JCheckBox
		wraparoundLabel = new JLabel("<html>Should boards wrap <br />" + "around horizontally?</html>");
		wraparound = new JCheckBox("Yes");
		wraparound.setToolTipText("Press me to have boards that wrap around on the edges");

		// Create button and add ActionListener
		submitButton = new JButton("Save");
		submitButton.setToolTipText("Press me to save these board settings");
		submitButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (formComplete())
				{// Make sure the form is complete.
					// Create Board[] based on which radio button is selected.
					Board[] boards = (oneBoard.isSelected()) ? new Board[1] : new Board[2];
					for (int i = 0; i < boards.length; i++)
					{
						// Initialize each board with the given rows and columns
						// and wraparound boolean.
						boards[i] = new Board(Integer.parseInt(numRows.getText()), Integer.parseInt(numCols.getText()), wraparound
								.isSelected());
					}
					if (twoBoards.isSelected())
						variant.drawBoard(boards, true);
					else
						variant.drawBoard(boards, false);
					holder.removeAll();
					frame.setVisible(false);
				}
			}

		});

		JPanel buttons = new JPanel();
		buttons.add(submitButton);
		buttons.add(backButton);

		c.gridx = 0;
		c.gridy = 0;
		add(numBoardsLabel, c);
		c.gridx = 1;
		c.gridy = 0;
		add(boards, c);
		c.gridx = 0;
		c.gridy = 1;
		add(dimensionsLabel, c);
		c.gridx = 1;
		c.gridy = 1;
		add(rowCol, c);
		c.gridx = 0;
		c.gridy = 2;
		add(wraparoundLabel, c);
		c.gridx = 1;
		c.gridy = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		add(wraparound, c);
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		c.insets = new Insets(10, 0, 10, 0);
		add(buttons, c);

		frame.pack();
	}

}
