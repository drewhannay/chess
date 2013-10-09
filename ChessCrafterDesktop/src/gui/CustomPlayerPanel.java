package gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import rules.NextTurn;
import rules.Rules;
import rules.NextTurn.NextTurnOption;
import utility.GuiUtility;

public class CustomPlayerPanel extends JPanel
{
	public CustomPlayerPanel(CustomSetupPanel customSetupMenu, JFrame optionsFrame)
	{
		mWhiteRules = customSetupMenu.mWhiteRules;
		mBlackRules = customSetupMenu.mBlackRules;
		mFrame = optionsFrame;
		mFrame.setVisible(true);
		mFrame.add(this);
		mFrame.setVisible(true);
		mFrame.setSize(300, 200);
		mFrame.setLocationRelativeTo(Driver.getInstance());
		initGUIComponents(customSetupMenu);
	}

	private void initGUIComponents(final CustomSetupPanel customSetupMenu)
	{
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		setBorder(BorderFactory.createLoweredBevelBorder());

		JLabel playerOneTurnsLabel = new JLabel("How many turns in a row for White? ");
		JLabel playerTwoTurnsLabel = new JLabel("How many turns in a row for Black? ");
		mPlayerOneTurnsField = new JTextField(4);
		GuiUtility.requestFocus(mPlayerOneTurnsField);
		mPlayerOneTurnsField.setText(Integer.toString(mWhiteRules.getNextTurn().getWhiteMoves()));
		mPlayerOneTurnsField.setToolTipText("This will be the amount of turns for the First Player (white in classic)");
		mPlayerTwoTurnsField = new JTextField(4);
		mPlayerTwoTurnsField.setText(Integer.toString(mWhiteRules.getNextTurn().getBlackMoves()));
		mPlayerTwoTurnsField.setToolTipText("This will be the amount of turns for the First Player (white in classic)");
		mIncrementTurnsLabel = new JLabel("Increase turns by how many each round? ");
		mIncrementTurnsField = new JTextField(4);
		mIncrementTurnsField.setText(Integer.toString(mWhiteRules.getNextTurn().getIncrement()));
		mIncrementTurnsField.setToolTipText("This will be the number of turns each player gains for each time their turn occurs");

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setToolTipText("Press to return to the main Variant window");
		GuiUtility.setupVariantCancelButton(cancelButton, this, mFrame);

		JButton submitButton = new JButton("Save");
		submitButton.setToolTipText("Press to save turn settings");
		submitButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (formIsComplete(mWhiteRules, mBlackRules))
				{
					customSetupMenu.setWhiteRules(mWhiteRules);
					customSetupMenu.setBlackRules(mBlackRules);
					CustomPlayerPanel.this.removeAll();
					mFrame.setVisible(false);
				}
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(submitButton);
		buttonPanel.add(cancelButton);

		constraints.insets = new Insets(3, 3, 3, 3);
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(playerOneTurnsLabel, constraints);

		constraints.insets = new Insets(3, 3, 3, 3);
		constraints.gridx = 2;
		constraints.gridy = 0;
		add(mPlayerOneTurnsField, constraints);

		constraints.insets = new Insets(3, 3, 3, 3);
		constraints.gridx = 0;
		constraints.gridy = 1;
		add(playerTwoTurnsLabel, constraints);

		constraints.insets = new Insets(3, 3, 3, 3);
		constraints.gridx = 2;
		constraints.gridy = 1;
		add(mPlayerTwoTurnsField, constraints);

		constraints.insets = new Insets(3, 3, 3, 3);
		constraints.gridx = 0;
		constraints.gridy = 2;
		add(mIncrementTurnsLabel, constraints);

		constraints.insets = new Insets(3, 3, 3, 3);
		constraints.gridx = 2;
		constraints.gridy = 2;
		add(mIncrementTurnsField, constraints);

		constraints.gridwidth = 3;
		constraints.gridx = 0;
		constraints.gridy = 3;
		add(buttonPanel, constraints);

	}

	private boolean formIsComplete(Rules whiteRules, Rules blackRules)
	{
		mWhiteRules = whiteRules;
		mBlackRules = blackRules;
		
		try
		{
			int whiteTurns = Integer.parseInt(mPlayerOneTurnsField.getText());
			int blackTurns = Integer.parseInt(mPlayerTwoTurnsField.getText());
			int increment = Integer.parseInt(mIncrementTurnsField.getText());
			if (whiteTurns < 0)
			{
				JOptionPane.showMessageDialog(this, "The White team's turns cannot be negative", "No Negatives",
						JOptionPane.PLAIN_MESSAGE);
				return false;
			}
			else if (blackTurns < 0)
			{
				JOptionPane.showMessageDialog(this, "The Black team's turns cannot be negative", "No Negatives",
						JOptionPane.PLAIN_MESSAGE);
				return false;
			}
			else
			{
				NextTurn next;
				if (whiteTurns == blackTurns)
				{
					if (increment > 0)
						next = new NextTurn(NextTurnOption.INCREASING_TOGETHER, whiteTurns, blackTurns, increment);
					else
						next = new NextTurn(NextTurnOption.DIFFERENT_NUMBER_OF_TURNS, whiteTurns, blackTurns, increment);
				}
				else if (increment > 0)
					next = new NextTurn(NextTurnOption.INCREASING_SEPARATELY, whiteTurns, blackTurns, increment);
				else
					next = new NextTurn(NextTurnOption.DIFFERENT_NUMBER_OF_TURNS, whiteTurns, blackTurns, increment);

				mWhiteRules.setNextTurn(next);
				mBlackRules.setNextTurn(next);
			}
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(Driver.getInstance(), "Enter a valid number of turns for both players.", "Incomplete Form",
					JOptionPane.PLAIN_MESSAGE);
			return false;
		}
		return true;
	}

	private static final long serialVersionUID = -5035641594159934814L;

	private Rules mWhiteRules;
	private Rules mBlackRules;
	private JTextField mPlayerOneTurnsField;
	private JTextField mPlayerTwoTurnsField;
	private JLabel mIncrementTurnsLabel;
	private JTextField mIncrementTurnsField;
	private JFrame mFrame;
}
