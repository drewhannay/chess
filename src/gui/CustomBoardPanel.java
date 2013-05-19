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

import logic.Board;
import utility.GuiUtility;

public class CustomBoardPanel extends JPanel
{
	public CustomBoardPanel(CustomSetupPanel variant, JFrame optionsFrame)
	{
		mFrame = optionsFrame;
		mFrame.setVisible(true);
		mFrame.add(this);
		mFrame.setVisible(true);
		mFrame.setSize(300, 250);
		mFrame.setLocationRelativeTo(Driver.getInstance());
		initGUIComponents(variant);
	}

	private void initGUIComponents(final CustomSetupPanel variant)
	{
		setBorder(BorderFactory.createLoweredBevelBorder());
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		revalidate();
		repaint();
		// create button and add ActionListener
		mCancelButton = new JButton("Cancel");
		mCancelButton.setToolTipText("Press me to go back to the main variant setup");
		GuiUtility.setupVariantCancelButton(mCancelButton, this, mFrame);

		// Create JLabels and JRadioButtons.
		mNumberOfBoardsLabel = new JLabel("How many boards?");
		mOneBoardButton = new JRadioButton("1");
		GuiUtility.requestFocus(mOneBoardButton);
		mOneBoardButton.setToolTipText("Choose me for one Board");
		// Set oneBoard to be initially selected.
		mTwoBoardsButton = new JRadioButton("2");
		mTwoBoardsButton.setToolTipText("Choose me for two boards");

		JPanel boards = new JPanel();
		boards.add(mOneBoardButton);
		boards.add(mTwoBoardsButton);

		Board[] board = variant.getBuilder().getBoards();

		if (board.length == 1)
			mOneBoardButton.setSelected(true);
		else
			mTwoBoardsButton.setSelected(true);

		// important: add the buttons to a group so only one can be selected at
		// a time
		ButtonGroup group = new ButtonGroup();
		group.add(mOneBoardButton);
		group.add(mTwoBoardsButton);

		// Create JLabels and JTextFields. Default size 8*8
		mDimensionsLabel = new JLabel("Dimensions?");
		mNumberOfRowsLabel = new JLabel("Rows:");
		mNumberOfRowsTextField = new JTextField(5);
		mNumberOfRowsTextField.setText(board[0].numRows() + "");
		mNumberOfRowsTextField.setToolTipText("Enter the amount of rows you would like");
		mNumberOfColumnsLabel = new JLabel("Columns:");
		mNumberOfColumnsTextField = new JTextField(5);
		mNumberOfColumnsTextField.setText(board[0].numCols() + "");
		mNumberOfColumnsTextField.setToolTipText("Enter the amount of columns you would like");

		JPanel rowCol = new JPanel();
		rowCol.setLayout(new GridBagLayout());
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets(2, 15, 1, 5);
		rowCol.add(mNumberOfRowsLabel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.BOTH;
		rowCol.add(mNumberOfRowsTextField, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.fill = GridBagConstraints.NONE;
		rowCol.add(mNumberOfColumnsLabel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.fill = GridBagConstraints.BOTH;
		rowCol.add(mNumberOfColumnsTextField, constraints);

		// Create JLabel and JCheckBox
		mWrapAroundLabel = new JLabel("<html>Should boards wrap <br />" + "around horizontally?</html>");
		mWrapAroundCheckBox = new JCheckBox("Yes");
		mWrapAroundCheckBox.setToolTipText("Press me to have boards that wrap around on the edges");

		// Create button and add ActionListener
		mSubmitButton = new JButton("Save");
		mSubmitButton.setToolTipText("Press me to save these board settings");
		mSubmitButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (formIsValid())
				{
					// create Board[] based on which radio button is selected.
					Board[] boards = (mOneBoardButton.isSelected()) ? new Board[1] : new Board[2];
					for (int i = 0; i < boards.length; i++)
					{
						// initialize each board with the given rows and columns
						// and wraparound boolean.
						boards[i] = new Board(Integer.parseInt(mNumberOfRowsTextField.getText()), Integer
								.parseInt(mNumberOfColumnsTextField.getText()), mWrapAroundCheckBox.isSelected());
					}
					if (mTwoBoardsButton.isSelected())
						variant.drawBoards(boards, true);
					else
						variant.drawBoards(boards, false);
					mHolder.removeAll();
					mFrame.setVisible(false);
				}
			}

		});

		JPanel buttons = new JPanel();
		buttons.add(mSubmitButton);
		buttons.add(mCancelButton);

		constraints.gridx = 0;
		constraints.gridy = 0;
		add(mNumberOfBoardsLabel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 0;
		add(boards, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		add(mDimensionsLabel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 1;
		add(rowCol, constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		add(mWrapAroundLabel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		add(mWrapAroundCheckBox, constraints);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.insets = new Insets(10, 0, 10, 0);
		add(buttons, constraints);

		mFrame.pack();
	}

	private boolean formIsValid()
	{
		if (!mOneBoardButton.isSelected() && !mTwoBoardsButton.isSelected())
		{
			JOptionPane.showMessageDialog(Driver.getInstance(), "Please select the number of boards.", "Incomplete Form", JOptionPane.PLAIN_MESSAGE);
			return false;
		}
		try
		{
			int numberOfRows = Integer.parseInt(mNumberOfRowsTextField.getText());
			int numberOfColumns = Integer.parseInt(mNumberOfColumnsTextField.getText());
			if (numberOfRows < 3 || numberOfColumns < 3 || numberOfRows > 16 || numberOfColumns > 16)
				throw new Exception();
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(Driver.getInstance(), "Enter a valid number between 3 and 16 for the dimensions of the board.",
					"Invalid Submission", JOptionPane.PLAIN_MESSAGE);
			return false;
		}
		try
		{
			int numberOfRows = Integer.parseInt(mNumberOfRowsTextField.getText());
			int numberOfColumns = Integer.parseInt(mNumberOfColumnsTextField.getText());
			if (mTwoBoardsButton.isSelected()
					&& (numberOfRows < 3 || numberOfColumns < 3 || numberOfRows > 10 || numberOfColumns > 10))
				throw new Exception();
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(Driver.getInstance(), "Enter a valid number between 3 and 10 for the dimensions of the board.",
					"Invalid Submission", JOptionPane.PLAIN_MESSAGE);
			return false;
		}
		return true;
	}

	private static final long serialVersionUID = 5842202724028685933L;

	private JLabel mNumberOfBoardsLabel;
	private JRadioButton mOneBoardButton;
	private JRadioButton mTwoBoardsButton;
	private JLabel mDimensionsLabel;
	private JLabel mNumberOfRowsLabel;
	private JTextField mNumberOfRowsTextField;
	private JLabel mNumberOfColumnsLabel;
	private JTextField mNumberOfColumnsTextField;
	private JLabel mWrapAroundLabel;
	private JCheckBox mWrapAroundCheckBox;
	private JButton mCancelButton;
	private JButton mSubmitButton;
	private JFrame mFrame;
	private CustomBoardPanel mHolder = this;
}
