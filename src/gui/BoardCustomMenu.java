package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;

import logic.Board;
import logic.Builder;

/**
 * BoardCustomMenu.java
 * 
 * GUI to handle customization of game boards.
 * 
 * @author Drew Hannay & Daniel Opdyke
 * 
 * CSCI 335, Wheaton College, Spring 2011
 * Phase 1
 * February 24, 2011
 */
public class BoardCustomMenu extends JPanel {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 5842202724028685933L;

	// Variables declaration - do not modify
	/**
	 * Builder used to progressively create the new game type.
	 */
	private Builder b;
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

	// End of variables declaration	

	/**
	 * Constructor.
	 * Call initComponents to initialize the GUI.
	 * @param b The builder which is creating the new game type.
	 */
	public BoardCustomMenu(Builder b) {
		this.b = b;
		initComponents();
	}

	/**
	 * Determine if this form has been correctly filled out.
	 * Check that a number of boards is selected, and that the
	 * dimension fields contain valid integers.
	 * @return Whether or not the form is complete.
	 */
	private boolean formComplete() {
		if (!oneBoard.isSelected() && !twoBoards.isSelected()) {
			JOptionPane.showMessageDialog(null, "Please select the number of boards.",
					"Incomplete Form", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		try {
			int a = Integer.parseInt(numRows.getText());
			int b = Integer.parseInt(numCols.getText());
			if (a < 3 || b < 3 || a > 16 || b > 16)
				throw new Exception();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Enter a valid number between 3 and 16 for the dimensions of the board.",
					"Invalid Submission", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		try{
			int a = Integer.parseInt(numRows.getText());
			int b = Integer.parseInt(numCols.getText());
			if (twoBoards.isSelected() && (a < 3 || b < 3 || a > 10 || b > 10))
				throw new Exception();
		}catch(Exception e){
			JOptionPane.showMessageDialog(null,
					"Enter a valid number between 3 and 10 for the dimensions of the board.",
					"Invalid Submission", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		return true;
	}

	/**
	 * Initialize components of the GUI
	 * Create all the GUI components, set their specific properties and add them to the 
	 * window. Also add any necessary ActionListeners.
	 */
	private void initComponents() {

		setBorder(BorderFactory.createLoweredBevelBorder());
		
		//Create button and add ActionListener
		backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Return to the NewTypeMenu screen.
				Driver.getInstance().setPanel(new NewTypeMenu());
			}
		});

		//Create JLabels and JRadioButtons.
		numBoardsLabel = new JLabel("How many boards?");
		oneBoard = new JRadioButton("1");
		// Set oneBoard to be initially selected.
		oneBoard.setSelected(true);
		twoBoards = new JRadioButton("2");

		//This is important! Add the buttons to a group so only one can be selected at a time.
		ButtonGroup group = new ButtonGroup();
		group.add(oneBoard);
		group.add(twoBoards);

		//Create JLabels and JTextFields. Default size 8*8
		dimensionsLabel = new JLabel("Dimensions?");
		numRowsLabel = new JLabel("Rows:");
		numRows = new JTextField("8");
		numColsLabel = new JLabel("Columns:");
		numCols = new JTextField("8");

		//Create JLabel and JCheckBox
		wraparoundLabel = new JLabel("<html>Should boards wrap <br />" +
		"around horizontally?</html>");
		wraparound = new JCheckBox("Yes");

		//Create button and add ActionListener
		submitButton = new JButton("Next");
		submitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (formComplete()) {//Make sure the form is complete.
					//Create Board[] based on which radio button is selected.
					Board[] boards = (oneBoard.isSelected()) ? new Board[1] : new Board[2];
					for (int i = 0; i < boards.length; i++) {
						//Initialize each board with the given rows and columns and wraparound boolean.
						boards[i] = new Board(Integer.parseInt(numRows.getText()),
								Integer.parseInt(numCols.getText()), wraparound.isSelected());
					}
					b.setBoards(boards);//Add the Board[] to the Builder and pass it on to the next step.
					
					Driver.getInstance().setPanel(new PieceMaker(b));
				}
			}

		});

		//Layout stuff. Don't. Ask.
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
										.addContainerGap()
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
												.addGroup(layout.createSequentialGroup()
														.addComponent(numBoardsLabel, GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
														.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(oneBoard))
														.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
																.addComponent(dimensionsLabel, GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
																.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
																.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
																		.addComponent(numColsLabel)
																		.addComponent(numRowsLabel))))
																		.addGap(20, 20, 20))
																		.addGroup(layout.createSequentialGroup()
																				.addGap(105, 105, 105)
																				.addComponent(backButton)
																				.addComponent(submitButton))
																				.addGroup(layout.createSequentialGroup()
																						.addContainerGap()
																						.addComponent(wraparoundLabel, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)))
																						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
																								.addGroup(layout.createSequentialGroup()
																										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
																												.addGroup(layout.createSequentialGroup()
																														.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
																														.addComponent(wraparound)))
																														.addContainerGap(10, Short.MAX_VALUE))
																														.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
																																.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
																																		.addComponent(numCols, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
																																		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
																																				.addComponent(twoBoards)
																																				.addComponent(numRows, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)))
																																				.addGap(10, 10, 10))))
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addGap(15, 15, 15)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(numBoardsLabel)
								.addComponent(oneBoard)
								.addComponent(twoBoards))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(dimensionsLabel)
										.addComponent(numRowsLabel)
										.addComponent(numRows, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
												.addGroup(layout.createSequentialGroup()
														.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
																.addComponent(numColsLabel)
																.addComponent(numCols, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
																.addGap(5, 5, 5)
																.addComponent(wraparound)
																.addGap(5, 5, 5))
																.addComponent(wraparoundLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
																.addGap(20, 20, 20)
																.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
																		.addComponent(backButton)
																		.addComponent(submitButton))
																		.addContainerGap(15, Short.MAX_VALUE))
		);
	}

}
