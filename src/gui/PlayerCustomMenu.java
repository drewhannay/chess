package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import logic.Builder;
import rules.NextTurn;
import rules.Rules;

/**
 * PlayerCustomMenu.java
 * 
 * GUI to handle customization of Players.
 * 
 * @author Drew Hannay & Daniel Opdyke
 * 
 * CSCI 335, Wheaton College, Spring 2011
 * Phase 1
 * February 24, 2011
 */
public class PlayerCustomMenu extends JPanel {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -5035641594159934814L;

	// Variables declaration - do not modify
	/**
	 * Builder used to progressively create the new game type.
	 */
	private Builder b;
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

	// End of variables declaration

	/**
	 * Constructor.
	 * Call initComponents to initialize the GUI.
	 * @param b The builder which is creating the new game type.
	 * @param blackRules The whiteRules object.
	 * @param whiteRules The blackRules object.
	 */
	public PlayerCustomMenu(Builder b, Rules whiteRules, Rules blackRules) {
		this.b = b;
		initComponents(whiteRules, blackRules);
	}

	/**
	 * Determine if this form has been correctly filled out.
	 * Check that valid integers have been selected for the 
	 * number of turns for each Player.
	 * @param whiteRules The whiteRules object.
	 * @param blackRules The blackRules object.
	 * @return Whether or not the form is complete.
	 */
	private boolean formComplete(Rules whiteRules, Rules blackRules) {
		try {
			//TODO Set a lower bound here...no negatives. Maybe an upper bound too?
			int whiteTurns = Integer.parseInt(numTurnsOne.getText());
			int blackTurns = Integer.parseInt(numTurnsTwo.getText());
			int increment = Integer.parseInt(incrementTurns.getText());
			if (whiteTurns == blackTurns) {
				if (increment > 0) {
					whiteRules.setNextTurn(new NextTurn("increasing together", whiteTurns, blackTurns, increment));
					blackRules.setNextTurn(new NextTurn("increasing together", whiteTurns, blackTurns, increment));
				} else {
					whiteRules.setNextTurn(new NextTurn("different turns", whiteTurns, blackTurns, increment));
					blackRules.setNextTurn(new NextTurn("different turns", whiteTurns, blackTurns, increment));
				}
			} else if (increment > 0) {
				whiteRules.setNextTurn(new NextTurn("increasing separately", whiteTurns, blackTurns, increment));
				blackRules.setNextTurn(new NextTurn("increasing separately", whiteTurns, blackTurns, increment));
			} else {
				whiteRules.setNextTurn(new NextTurn("different turns", whiteTurns, blackTurns, increment));
				blackRules.setNextTurn(new NextTurn("different turns", whiteTurns, blackTurns, increment));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Enter a valid number of turns for both players.",
					"Incomplete Form", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		return true;
	}

	/**
	 * Initialize components of the GUI
	 * Create all the GUI components, set their specific properties and add them to the 
	 * window. Also add any necessary ActionListeners.
	 * @param whiteRules The whiteRules object.
	 * @param blackRules The blackRules object.
	 */
	private void initComponents(final Rules whiteRules, final Rules blackRules) {

		//Set the layout and size of this JPanel.
		setLayout(new FlowLayout());
		setSize(100, 200);

		//Create JLabels and JTextFields.
		numTurnsOneLabel = new JLabel("<html>How many turns in a <br/> " +
				"row for White?</html>");
		numTurnsTwoLabel = new JLabel("<html>How many turns in a <br/> " +
				"row for Black?</html>");
		numTurnsOne = new JTextField(4);
		numTurnsTwo = new JTextField(4);
		incrementTurnsLabel = new JLabel("<html>Increase turns by <br/>" +
				"how much each round?");
		incrementTurns = new JTextField(4);

		//Create button and add ActionListener
		backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Return to the BoardCustomMenu screen.
				Driver.getInstance().setPanel(new BoardCustomMenu(b));
			}
		});

		//Create button and add ActionListener
		submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (formComplete(whiteRules, blackRules)) {//Make sure the form is complete.
					//TODO parse form here
					Driver.getInstance().setPanel(new CustomSetupMenu(b, whiteRules, blackRules));
				}
			}
		});

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		// Maybe put borders around all of the pages.
		//this.setBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.BLACK));
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								layout.createSequentialGroup()
										.addGap(45, 45, 45)
										.addGroup(
												layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(numTurnsTwoLabel,
																javax.swing.GroupLayout.DEFAULT_SIZE, 141,
																Short.MAX_VALUE)
														.addComponent(numTurnsOneLabel,
																javax.swing.GroupLayout.DEFAULT_SIZE, 141,
																Short.MAX_VALUE)
														.addComponent(incrementTurnsLabel,
																javax.swing.GroupLayout.DEFAULT_SIZE, 141,
																Short.MAX_VALUE))
										.addGap(29, 29, 29)
										.addGroup(
												layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING,
														false)
														.addComponent(numTurnsOne,
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(numTurnsTwo,
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(incrementTurns,
																javax.swing.GroupLayout.Alignment.LEADING))
										.addGap(91, 91, 91))
										.addGroup(layout.createSequentialGroup()
												.addGap(114, 114, 114)
												.addComponent(submitButton)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
												.addComponent(backButton)
												.addGap(130, 130, 130))
				);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								layout.createSequentialGroup()
										.addGap(90, 90, 90)
										.addGroup(
												layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(numTurnsOne,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(numTurnsOneLabel,
																javax.swing.GroupLayout.PREFERRED_SIZE, 47,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(numTurnsTwo,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(numTurnsTwoLabel,
																javax.swing.GroupLayout.PREFERRED_SIZE, 50,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(incrementTurns,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(incrementTurnsLabel,
																javax.swing.GroupLayout.PREFERRED_SIZE, 50,
																javax.swing.GroupLayout.PREFERRED_SIZE))
											.addGap(40, 40, 40)
											.addGroup(
													layout.createParallelGroup(
															javax.swing.GroupLayout.Alignment.BASELINE)
															.addComponent(submitButton)
															.addComponent(backButton))
													.addGap(43, 43, 43))
				);

	}

}
