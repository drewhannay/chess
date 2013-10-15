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
		mCancelButton = new JButton(Messages.getString("CustomBoardPanel.cancel")); //$NON-NLS-1$
		mCancelButton.setToolTipText(Messages.getString("CustomBoardPanel.returnToVariantSetup")); //$NON-NLS-1$
		GuiUtility.setupVariantCancelButton(mCancelButton, this, mFrame);

		// Create JLabels and JRadioButtons.
		mNumberOfBoardsLabel = new JLabel(Messages.getString("CustomBoardPanel.howManyBoards")); //$NON-NLS-1$
		mOneBoardButton = new JRadioButton(Messages.getString("CustomBoardPanel.1")); //$NON-NLS-1$
		GuiUtility.requestFocus(mOneBoardButton);
		mOneBoardButton.setToolTipText(Messages.getString("CustomBoardPanel.selectForOneBoard")); //$NON-NLS-1$
		// Set oneBoard to be initially selected.
		mTwoBoardsButton = new JRadioButton(Messages.getString("CustomBoardPanel.2")); //$NON-NLS-1$
		mTwoBoardsButton.setToolTipText(Messages.getString("CustomBoardPanel.selectForTwoBoards")); //$NON-NLS-1$

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
		mDimensionsLabel = new JLabel(Messages.getString("CustomBoardPanel.dimensions")); //$NON-NLS-1$
		mNumberOfRowsLabel = new JLabel(Messages.getString("CustomBoardPanel.rows")); //$NON-NLS-1$
		mNumberOfRowsTextField = new JTextField(5);
		mNumberOfRowsTextField.setText(board[0].numRows() + Messages.getString("CustomBoardPanel.empty")); //$NON-NLS-1$
		mNumberOfRowsTextField.setToolTipText(Messages.getString("CustomBoardPanel.enterRowAmount")); //$NON-NLS-1$
		mNumberOfColumnsLabel = new JLabel(Messages.getString("CustomBoardPanel.columns")); //$NON-NLS-1$
		mNumberOfColumnsTextField = new JTextField(5);
		mNumberOfColumnsTextField.setText(board[0].numCols() + Messages.getString("CustomBoardPanel.empty")); //$NON-NLS-1$
		mNumberOfColumnsTextField.setToolTipText(Messages.getString("CustomBoardPanel.amountOfColumns")); //$NON-NLS-1$

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
		mWrapAroundLabel = new JLabel(Messages.getString("CustomBoardPanel.shouldWrap") //$NON-NLS-1$
				+ Messages.getString("CustomBoardPanel.aroundHorizontally")); //$NON-NLS-1$
		mWrapAroundCheckBox = new JCheckBox(Messages.getString("CustomBoardPanel.yes")); //$NON-NLS-1$
		mWrapAroundCheckBox.setSelected(variant.getBuilder().getBoards()[0].isWrapAround());
		mWrapAroundCheckBox.setToolTipText(Messages.getString("CustomBoardPanel.pressToHaveWrapAround")); //$NON-NLS-1$

		// Create button and add ActionListener
		mSubmitButton = new JButton(Messages.getString("CustomBoardPanel.save")); //$NON-NLS-1$
		mSubmitButton.setToolTipText(Messages.getString("CustomBoardPanel.pressToSave")); //$NON-NLS-1$
		mSubmitButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (formIsValid())
				{
					int changeCode = boardsChanged();
					if (changeCode != NO_CHANGES)
					{
						// create Board[] based on which radio button is
						// selected.
						Board[] boards = (mOneBoardButton.isSelected()) ? new Board[1] : new Board[2];
						for (int i = 0; i < boards.length; i++)
						{
							// initialize each board with the given rows and
							// columns
							// and wraparound boolean.
							if (changeCode == WRAP_ONLY)
								boards[i] = variant.getBuilder().getBoards()[i].makeCopyWithWrapSelection(mWrapAroundCheckBox
										.isSelected());
							else
								boards[i] = new Board(Integer.parseInt(mNumberOfRowsTextField.getText()), Integer
										.parseInt(mNumberOfColumnsTextField.getText()), mWrapAroundCheckBox.isSelected());
						}

						variant.drawBoards(boards, mTwoBoardsButton.isSelected());
						mHolder.removeAll();
						mFrame.setVisible(false);
					}
				}
			}

			private int boardsChanged()
			{
				Board[] oldBoards = variant.getBuilder().getBoards();
				int change = NO_CHANGES;

				if (oldBoards[0].isWrapAround() != mWrapAroundCheckBox.isSelected())
					change = WRAP_ONLY;

				if (mTwoBoardsButton.isSelected() && oldBoards.length != 2)
					change = SHAPE_CHANGE;
				else if (!mTwoBoardsButton.isSelected() && oldBoards.length != 1)
					change = SHAPE_CHANGE;
				else if (oldBoards[0].numCols() != Integer.parseInt(mNumberOfColumnsTextField.getText()))
					change = SHAPE_CHANGE;
				else if (oldBoards[0].numRows() != Integer.parseInt(mNumberOfRowsTextField.getText()))
					change = SHAPE_CHANGE;

				return change;
			}

			private final static int NO_CHANGES = 0;
			private final static int WRAP_ONLY = 1;
			private final static int SHAPE_CHANGE = 2;
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
			JOptionPane
					.showMessageDialog(
							Driver.getInstance(),
							Messages.getString("CustomBoardPanel.selectNumberOfBoards"), Messages.getString("CustomBoardPanel.incompleteForm"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
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
			JOptionPane.showMessageDialog(Driver.getInstance(), Messages.getString("CustomBoardPanel.enterValidNumber"), //$NON-NLS-1$
					Messages.getString("CustomBoardPanel.invalidSubmission"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
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
			JOptionPane.showMessageDialog(Driver.getInstance(), Messages.getString("CustomBoardPanel.enterValidNumber"), //$NON-NLS-1$
					Messages.getString("CustomBoardPanel.invalidSubmission"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
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
