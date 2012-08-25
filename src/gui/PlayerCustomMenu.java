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
import javax.swing.SwingUtilities;

import rules.NextTurn;
import rules.Rules;

/**
 * PlayerCustomMenu.java
 * 
 * GUI to handle customization of Players.
 * 
 * @author Drew Hannay & Daniel Opdyke & John McCormick
 * 
 * CSCI 335, Wheaton College, Spring 2011 Phase 1 February 24, 2011
 */
public class PlayerCustomMenu extends JPanel
{

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -5035641594159934814L;

	// Variables declaration - do not modify
	/**
	 * Rules for the White Team
	 */
	private Rules whiteRules = new Rules(false, false);
	/**
	 * Rules for the Black Team
	 */
	private Rules blackRules = new Rules(false, false);
	/**
	 * JLabel with instructions for the number of turns for Player 1.
	 */
	private JLabel numTurnsOneLabel;
	/**
	 * JTextField to collect the number of turns for Player 1.
	 */
	private JTextField numTurnsOne;
	/**
	 * JLabel with instructions for the number of turns for Player 1.
	 */
	private JLabel numTurnsTwoLabel;
	/**
	 * A label for the incrementing turns option.
	 */
	private JLabel incrementTurnsLabel;
	/**
	 * A label for the incrementTurns field.
	 */
	private JTextField incrementTurns;
	/**
	 * JTextField to collect the number of turns for Player 1.
	 */
	private JTextField numTurnsTwo;
	/**
	 * JButton to return to previous screen.
	 */
	private JButton backButton;
	/**
	 * JButton to submit Player customizations and move to next screen.
	 */
	private JButton submitButton;
	/**
	 * Frame to hold the window
	 */
	private JFrame frame;
	/**
	 * holder for this PlayerCustomMenu
	 */
	private PlayerCustomMenu holder = this;

	// End of variables declaration

	/**
	 * Constructor. Call initComponents to initialize the GUI.
	 * 
	 * @param b The builder which is creating the new game type.
	 * @param blackRules The whiteRules object.
	 * @param whiteRules The blackRules object.
	 */
	/*
	 * public PlayerCustomMenu(Builder b, Rules whiteRules, Rules blackRules) {
	 * this.b = b; initComponents(whiteRules, blackRules); }
	 */

	public PlayerCustomMenu(CustomSetupMenu variant, JFrame optionsFrame)
	{
		whiteRules = variant.whiteRules;
		blackRules = variant.blackRules;
		frame = optionsFrame;
		frame.setVisible(true);
		frame.add(this);
		frame.setVisible(true);
		frame.setSize(300, 200);
		frame.setLocationRelativeTo(Driver.getInstance());
		initComponents(variant);
	}

	/**
	 * Determine if this form has been correctly filled out. Check that valid
	 * integers have been selected for the number of turns for each Player.
	 * 
	 * @param whiteRules The whiteRules object.
	 * @param blackRules The blackRules object.
	 * @return Whether or not the form is complete.
	 */
	private boolean formComplete(Rules whiteRules, Rules blackRules)
	{
		try
		{
			int whiteTurns = Integer.parseInt(numTurnsOne.getText());
			int blackTurns = Integer.parseInt(numTurnsTwo.getText());
			int increment = Integer.parseInt(incrementTurns.getText());
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
						whiteRules.setNextTurn(new NextTurn("increasing together", whiteTurns, blackTurns, increment));
						blackRules.setNextTurn(new NextTurn("increasing together", whiteTurns, blackTurns, increment));
					}
					else
					{
						whiteRules.setNextTurn(new NextTurn("different turns", whiteTurns, blackTurns, increment));
						blackRules.setNextTurn(new NextTurn("different turns", whiteTurns, blackTurns, increment));
					}
				}
				else if (increment > 0)
				{
					whiteRules.setNextTurn(new NextTurn("increasing separately", whiteTurns, blackTurns, increment));
					blackRules.setNextTurn(new NextTurn("increasing separately", whiteTurns, blackTurns, increment));
				}
				else
				{
					whiteRules.setNextTurn(new NextTurn("different turns", whiteTurns, blackTurns, increment));
					blackRules.setNextTurn(new NextTurn("different turns", whiteTurns, blackTurns, increment));
				}
			}
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Enter a valid number of turns for both players.", "Incomplete Form",
					JOptionPane.PLAIN_MESSAGE);
			return false;
		}
		return true;
	}

	/**
	 * Initialize components of the GUI Create all the GUI components, set their
	 * specific properties and add them to the window. Also add any necessary
	 * ActionListeners.
	 * 
	 * @param whiteRules The whiteRules object.
	 * @param blackRules The blackRules object.
	 */
	private void initComponents(final CustomSetupMenu variant)
	{

		// Set the layout and size of this JPanel.
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		setBorder(BorderFactory.createLoweredBevelBorder());

		// Create JLabels and JTextFields.
		numTurnsOneLabel = new JLabel("How many turns in a row for White? ");
		numTurnsTwoLabel = new JLabel("How many turns in a row for Black? ");
		numTurnsOne = new JTextField(4);
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				numTurnsOne.requestFocus();
			}
		});
		numTurnsOne.setText(Integer.toString(whiteRules.getNextTurn().getWhiteMoves()));
		numTurnsOne.setToolTipText("This will be the amount of turns for the First Player (white in classic)");
		numTurnsTwo = new JTextField(4);
		numTurnsTwo.setText(Integer.toString(whiteRules.getNextTurn().getBlackMoves()));
		numTurnsTwo.setToolTipText("This will be the amount of turns for the First Player (white in classic)");
		incrementTurnsLabel = new JLabel("Increase turns by how many each round? ");
		incrementTurns = new JTextField(4);
		incrementTurns.setText(Integer.toString(whiteRules.getNextTurn().getIncrement()));
		incrementTurns.setToolTipText("This will be the number of turns each player gains for each time their turn occurs");

		// Create button and add ActionListener
		backButton = new JButton("Cancel");
		backButton.setToolTipText("Press me to return to the main Variant window");
		backButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				holder.removeAll();
				frame.setVisible(false);
			}
		});

		// Create button and add ActionListener
		submitButton = new JButton("Save");
		submitButton.setToolTipText("Press me to save these turn settings");
		submitButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (formComplete(whiteRules, blackRules))
				{// Make sure the form is complete.
					variant.whiteRules = whiteRules;
					variant.blackRules = blackRules;
					holder.removeAll();
					frame.setVisible(false);
				}
			}
		});

		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout());
		buttons.add(submitButton);
		buttons.add(backButton);

		c.insets = new Insets(3, 3, 3, 3);
		c.gridx = 0;
		c.gridy = 0;
		add(numTurnsOneLabel, c);

		c.insets = new Insets(3, 3, 3, 3);
		c.gridx = 2;
		c.gridy = 0;
		add(numTurnsOne, c);

		c.insets = new Insets(3, 3, 3, 3);
		c.gridx = 0;
		c.gridy = 1;
		add(numTurnsTwoLabel, c);

		c.insets = new Insets(3, 3, 3, 3);
		c.gridx = 2;
		c.gridy = 1;
		add(numTurnsTwo, c);

		c.insets = new Insets(3, 3, 3, 3);
		c.gridx = 0;
		c.gridy = 2;
		add(incrementTurnsLabel, c);

		c.insets = new Insets(3, 3, 3, 3);
		c.gridx = 2;
		c.gridy = 2;
		add(incrementTurns, c);

		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 3;
		add(buttons, c);

	}

}
