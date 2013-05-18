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

public class BoardCustomMenu extends JPanel
{
	public BoardCustomMenu(CustomSetupMenu variant, JFrame optionsFrame)
	{
		m_frame = optionsFrame;
		m_frame.setVisible(true);
		m_frame.add(this);
		m_frame.setVisible(true);
		m_frame.setSize(300, 250);
		m_frame.setLocationRelativeTo(Driver.getInstance());
		initGUIComponents(variant);
	}

	private void initGUIComponents(final CustomSetupMenu variant)
	{
		setBorder(BorderFactory.createLoweredBevelBorder());
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		revalidate();
		repaint();
		// create button and add ActionListener
		m_cancelButton = new JButton("Cancel");
		m_cancelButton.setToolTipText("Press me to go back to the main variant setup");
		GuiUtility.setupVariantCancelButton(m_cancelButton, this, m_frame);

		// Create JLabels and JRadioButtons.
		m_numberOfBoardsLabel = new JLabel("How many boards?");
		m_oneBoardButton = new JRadioButton("1");
		GuiUtility.requestFocus(m_oneBoardButton);
		m_oneBoardButton.setToolTipText("Choose me for one Board");
		// Set oneBoard to be initially selected.
		m_twoBoardsButton = new JRadioButton("2");
		m_twoBoardsButton.setToolTipText("Choose me for two boards");

		JPanel boards = new JPanel();
		boards.add(m_oneBoardButton);
		boards.add(m_twoBoardsButton);

		Board[] board = variant.getBuilder().getBoards();

		if (board.length == 1)
			m_oneBoardButton.setSelected(true);
		else
			m_twoBoardsButton.setSelected(true);

		// important: add the buttons to a group so only one can be selected at
		// a time
		ButtonGroup group = new ButtonGroup();
		group.add(m_oneBoardButton);
		group.add(m_twoBoardsButton);

		// Create JLabels and JTextFields. Default size 8*8
		m_dimensionsLabel = new JLabel("Dimensions?");
		m_numberOfRowsLabel = new JLabel("Rows:");
		m_numberOfRowsTextField = new JTextField(5);
		m_numberOfRowsTextField.setText(board[0].numRows() + "");
		m_numberOfRowsTextField.setToolTipText("Enter the amount of rows you would like");
		m_numberOfColumnsLabel = new JLabel("Columns:");
		m_numberOfColumnsTextField = new JTextField(5);
		m_numberOfColumnsTextField.setText(board[0].numCols() + "");
		m_numberOfColumnsTextField.setToolTipText("Enter the amount of columns you would like");

		JPanel rowCol = new JPanel();
		rowCol.setLayout(new GridBagLayout());
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets(2, 15, 1, 5);
		rowCol.add(m_numberOfRowsLabel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.BOTH;
		rowCol.add(m_numberOfRowsTextField, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.fill = GridBagConstraints.NONE;
		rowCol.add(m_numberOfColumnsLabel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.fill = GridBagConstraints.BOTH;
		rowCol.add(m_numberOfColumnsTextField, constraints);

		// Create JLabel and JCheckBox
		m_wrapAroundLabel = new JLabel("<html>Should boards wrap <br />" + "around horizontally?</html>");
		m_wrapAroundCheckBox = new JCheckBox("Yes");
		m_wrapAroundCheckBox.setToolTipText("Press me to have boards that wrap around on the edges");

		// Create button and add ActionListener
		m_submitButton = new JButton("Save");
		m_submitButton.setToolTipText("Press me to save these board settings");
		m_submitButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (formIsValid())
				{
					// create Board[] based on which radio button is selected.
					Board[] boards = (m_oneBoardButton.isSelected()) ? new Board[1] : new Board[2];
					for (int i = 0; i < boards.length; i++)
					{
						// initialize each board with the given rows and columns
						// and wraparound boolean.
						boards[i] = new Board(Integer.parseInt(m_numberOfRowsTextField.getText()), Integer
								.parseInt(m_numberOfColumnsTextField.getText()), m_wrapAroundCheckBox.isSelected());
					}
					if (m_twoBoardsButton.isSelected())
						variant.drawBoards(boards, true);
					else
						variant.drawBoards(boards, false);
					m_holder.removeAll();
					m_frame.setVisible(false);
				}
			}

		});

		JPanel buttons = new JPanel();
		buttons.add(m_submitButton);
		buttons.add(m_cancelButton);

		constraints.gridx = 0;
		constraints.gridy = 0;
		add(m_numberOfBoardsLabel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 0;
		add(boards, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		add(m_dimensionsLabel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 1;
		add(rowCol, constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		add(m_wrapAroundLabel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		add(m_wrapAroundCheckBox, constraints);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.insets = new Insets(10, 0, 10, 0);
		add(buttons, constraints);

		m_frame.pack();
	}

	private boolean formIsValid()
	{
		if (!m_oneBoardButton.isSelected() && !m_twoBoardsButton.isSelected())
		{
			JOptionPane.showMessageDialog(Driver.getInstance(), "Please select the number of boards.", "Incomplete Form", JOptionPane.PLAIN_MESSAGE);
			return false;
		}
		try
		{
			int numberOfRows = Integer.parseInt(m_numberOfRowsTextField.getText());
			int numberOfColumns = Integer.parseInt(m_numberOfColumnsTextField.getText());
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
			int numberOfRows = Integer.parseInt(m_numberOfRowsTextField.getText());
			int numberOfColumns = Integer.parseInt(m_numberOfColumnsTextField.getText());
			if (m_twoBoardsButton.isSelected()
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

	private JLabel m_numberOfBoardsLabel;
	private JRadioButton m_oneBoardButton;
	private JRadioButton m_twoBoardsButton;
	private JLabel m_dimensionsLabel;
	private JLabel m_numberOfRowsLabel;
	private JTextField m_numberOfRowsTextField;
	private JLabel m_numberOfColumnsLabel;
	private JTextField m_numberOfColumnsTextField;
	private JLabel m_wrapAroundLabel;
	private JCheckBox m_wrapAroundCheckBox;
	private JButton m_cancelButton;
	private JButton m_submitButton;
	private JFrame m_frame;
	private BoardCustomMenu m_holder = this;
}
