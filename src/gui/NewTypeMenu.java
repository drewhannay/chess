package gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import logic.Builder;

/**
 * NewTypeMenu.java
 * 
 * GUI to initiate the process of creating a new game type.
 * 
 * @author Drew Hannay & Daniel Opdyke & John McCormick & Andrew Wolfe & Becca
 * Meloy
 * 
 * CSCI 335, Wheaton College, Spring 2011 Phase 2 April 7, 2011
 */
public class NewTypeMenu extends JPanel
{

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 8365806731061105369L;

	/**
	 * JButton to return to previous screen.
	 */
	private JButton backButton;
	/**
	 * JLabel with instructions for the name field.
	 */
	private JLabel nameLabel;
	/**
	 * JTextField to enter the name of the new game type.
	 */
	private JTextField nameField;
	/**
	 * JButton to submit name and move to next screen.
	 */
	private JButton submitButton;

	/**
	 * Constructor. Call initComponents to initialize the GUI.
	 */
	public NewTypeMenu()
	{
		initComponents();
	}

	/**
	 * Initialize components of the GUI Create all the GUI components, set their
	 * specific properties and add them to the window. Also add any necessary
	 * ActionListeners.
	 */
	public void initComponents()
	{

		setBorder(BorderFactory.createLoweredBevelBorder());

		// Create button and add ActionListener for going back to main menu
		backButton = new JButton("Back");
		backButton.setToolTipText("Press me to go back to the Main Menu");
		backButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Return to the main screen.
				Driver.getInstance().variantHelp.setVisible(false);
				Driver.getInstance().helpMenu.setText("Help");
				Driver.getInstance().revertPanel();
			}
		});

		// Create JLabel and JTextField.
		nameLabel = new JLabel(" Please enter a name for your new game type: ");
		nameField = new JTextField(20);
		nameField.setToolTipText("Please enter a name for this variant here");

		// Create button and add ActionListener
		submitButton = new JButton("Next");
		submitButton.setToolTipText("Press me to save this name");
		submitButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (!nameField.getText().equals("")
						&& !nameField.getText().equals(" "))
				{
					// Make a new Builder with the given name and send it on to
					// the next GUI class.
					Builder b = new Builder(nameField.getText());
					Driver.getInstance().setPanel(new BoardCustomMenu(b));
				} else
				{
					JOptionPane.showMessageDialog(NewTypeMenu.this,
							"Please enter a name for this game");
				}
			}
		});

		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout());
		buttons.add(backButton);
		buttons.add(submitButton);

		// Layout stuff. Don't. Ask.
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.insets = new Insets(3, 3, 3, 3);
		c.gridx = 0;
		c.gridy = 0;
		add(nameLabel, c);

		c.insets = new Insets(3, 3, 3, 3);
		c.gridx = 0;
		c.gridy = 1;
		add(nameField, c);

		c.gridx = 0;
		c.gridy = 2;
		add(buttons, c);

	}
}
