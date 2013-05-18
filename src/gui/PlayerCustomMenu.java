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
import utility.GuiUtility;

public class PlayerCustomMenu extends JPanel
{
	public PlayerCustomMenu(CustomSetupMenu customSetupMenu, JFrame optionsFrame)
	{
		whiteRules = customSetupMenu.m_whiteRules;
		blackRules = customSetupMenu.m_blackRules;
		m_frame = optionsFrame;
		m_frame.setVisible(true);
		m_frame.add(this);
		m_frame.setVisible(true);
		m_frame.setSize(300, 200);
		m_frame.setLocationRelativeTo(Driver.getInstance());
		initGUIComponents(customSetupMenu);
	}

	private void initGUIComponents(final CustomSetupMenu customSetupMenu)
	{
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		setBorder(BorderFactory.createLoweredBevelBorder());

		JLabel playerOneTurnsLabel = new JLabel("How many turns in a row for White? ");
		JLabel playerTwoTurnsLabel = new JLabel("How many turns in a row for Black? ");
		m_playerOneTurnsField = new JTextField(4);
		GuiUtility.requestFocus(m_playerOneTurnsField);
		m_playerOneTurnsField.setText(Integer.toString(whiteRules.getNextTurn().getWhiteMoves()));
		m_playerOneTurnsField.setToolTipText("This will be the amount of turns for the First Player (white in classic)");
		m_playerTwoTurnsField = new JTextField(4);
		m_playerTwoTurnsField.setText(Integer.toString(whiteRules.getNextTurn().getBlackMoves()));
		m_playerTwoTurnsField.setToolTipText("This will be the amount of turns for the First Player (white in classic)");
		m_incrementTurnsLabel = new JLabel("Increase turns by how many each round? ");
		m_incrementTurnsField = new JTextField(4);
		m_incrementTurnsField.setText(Integer.toString(whiteRules.getNextTurn().getIncrement()));
		m_incrementTurnsField.setToolTipText("This will be the number of turns each player gains for each time their turn occurs");

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setToolTipText("Press me to return to the main Variant window");
		GuiUtility.setupVariantCancelButton(cancelButton, this, m_frame);

		JButton submitButton = new JButton("Save");
		submitButton.setToolTipText("Press me to save these turn settings");
		submitButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (formIsComplete(whiteRules, blackRules))
				{
					customSetupMenu.m_whiteRules = whiteRules;
					customSetupMenu.m_blackRules = blackRules;
					PlayerCustomMenu.this.removeAll();
					m_frame.setVisible(false);
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
		add(m_playerOneTurnsField, constraints);

		constraints.insets = new Insets(3, 3, 3, 3);
		constraints.gridx = 0;
		constraints.gridy = 1;
		add(playerTwoTurnsLabel, constraints);

		constraints.insets = new Insets(3, 3, 3, 3);
		constraints.gridx = 2;
		constraints.gridy = 1;
		add(m_playerTwoTurnsField, constraints);

		constraints.insets = new Insets(3, 3, 3, 3);
		constraints.gridx = 0;
		constraints.gridy = 2;
		add(m_incrementTurnsLabel, constraints);

		constraints.insets = new Insets(3, 3, 3, 3);
		constraints.gridx = 2;
		constraints.gridy = 2;
		add(m_incrementTurnsField, constraints);

		constraints.gridwidth = 3;
		constraints.gridx = 0;
		constraints.gridy = 3;
		add(buttonPanel, constraints);

	}

	private boolean formIsComplete(Rules whiteRules, Rules blackRules)
	{
		try
		{
			int whiteTurns = Integer.parseInt(m_playerOneTurnsField.getText());
			int blackTurns = Integer.parseInt(m_playerTwoTurnsField.getText());
			int increment = Integer.parseInt(m_incrementTurnsField.getText());
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
				if (whiteTurns == blackTurns)
				{
					if (increment > 0)
					{
						whiteRules.setNextTurn(NextTurn.INCREASING_TOGETHER.init(whiteTurns, blackTurns, increment));
						blackRules.setNextTurn(NextTurn.INCREASING_TOGETHER.init(whiteTurns, blackTurns, increment));
					}
					else
					{
						whiteRules.setNextTurn(NextTurn.DIFFERENT_NUMBER_OF_TURNS.init(whiteTurns, blackTurns, increment));
						blackRules.setNextTurn(NextTurn.DIFFERENT_NUMBER_OF_TURNS.init(whiteTurns, blackTurns, increment));
					}
				}
				else if (increment > 0)
				{
					whiteRules.setNextTurn(NextTurn.INCREASING_SEPARATELY.init(whiteTurns, blackTurns, increment));
					blackRules.setNextTurn(NextTurn.INCREASING_SEPARATELY.init(whiteTurns, blackTurns, increment));
				}
				else
				{
					whiteRules.setNextTurn(NextTurn.DIFFERENT_NUMBER_OF_TURNS.init(whiteTurns, blackTurns, increment));
					blackRules.setNextTurn(NextTurn.DIFFERENT_NUMBER_OF_TURNS.init(whiteTurns, blackTurns, increment));
				}
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

	private Rules whiteRules = new Rules(false);
	private Rules blackRules = new Rules(true);
	private JTextField m_playerOneTurnsField;
	private JTextField m_playerTwoTurnsField;
	private JLabel m_incrementTurnsLabel;
	private JTextField m_incrementTurnsField;
	private JFrame m_frame;
}
