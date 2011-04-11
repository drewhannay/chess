package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;

import logic.Builder;

/**
 * NewTypeMenu.java
 * 
 * GUI to initiate the process of creating a new game type.
 * 
 * @author Drew Hannay & Daniel Opdyke
 * 
 * CSCI 335, Wheaton College, Spring 2011
 * Phase 1
 * February 24, 2011
 */
public class NewTypeMenu extends JPanel {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 8365806731061105369L;

	// Variables declaration - do not modify
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

	// End of variables declaration

	/**
	 * Constructor.
	 * Call initComponents to initialize the GUI.
	 */
	public NewTypeMenu() {
		initComponents();
	}

	/**
	 * Initialize components of the GUI
	 * Create all the GUI components, set their specific properties and add them to the 
	 * window. Also add any necessary ActionListeners.
	 */
	public void initComponents() {

		//Create button and add ActionListener
		backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Return to the main screen.
				Driver.getInstance().revertPanel();
			}
		});

		//Create JLabel and JTextField.
		nameLabel = new JLabel("Please enter a name for your new game type:");
		nameField = new JTextField(40);

		//Create button and add ActionListener
		submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO Increase the robustness here...they shouldn't be able to enter spaces as names.
				if (!nameField.getText().equals("")) {
					//Make a new Builder with the given name and send it on to the next GUI class.
					Builder b = new Builder(nameField.getText());
					Driver.getInstance().setPanel(new BoardCustomMenu(b));
				}
			}
		});

		//Layout stuff. Make it better later.
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap(49, Short.MAX_VALUE)
								.addComponent(nameLabel, GroupLayout.PREFERRED_SIZE, 330, GroupLayout.PREFERRED_SIZE)
								.addGap(42, 42, 42))
						.addGroup(layout.createSequentialGroup()
								.addGap(59, 59, 59)
								.addComponent(nameField, GroupLayout.PREFERRED_SIZE, 269, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(72, Short.MAX_VALUE))
								.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
										.addContainerGap(133, Short.MAX_VALUE)
										.addComponent(submitButton)
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(backButton)
										.addGap(160, 160, 160))
				);
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								GroupLayout.Alignment.TRAILING,
								layout.createSequentialGroup()
										.addContainerGap(141, Short.MAX_VALUE)
										.addComponent(nameLabel)
										.addGap(18, 18, 18)
										.addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(submitButton)
												.addComponent(backButton))
										.addGap(56, 56, 56))
				);

	}
}
