package gui;

import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;

import logic.Builder;
import logic.Game;
import timer.BronsteinDelay;
import timer.ChessTimer;
import timer.Fischer;
import timer.HourGlass;
import timer.NoTimer;
import timer.SimpleDelay;
import timer.Word;

/**
 * NewGameMenu.java
 * 
 * GUI to initiate the a new game.
 * 
 * @author Drew Hannay & Daniel Opdyke
 * 
 * CSCI 335, Wheaton College, Spring 2011
 * Phase 1
 * February 24, 2011
 */
public class NewGameMenu extends JPanel {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -6371389704966320508L;

	// Variables declaration - do not modify
	/**
	 * JButton to initiate a new AI game.
	 */
	private JButton AIPlay;
	/**
	 * JButton to initiate a new Human versus Human game.
	 */
	private JButton humanPlay;
	/**
	 * JButton to initiate a new network game.
	 */
	private JButton networkPlay;
	/**
	 * JButton to return to previous screen.
	 */
	private JButton backButton;

	// End of variables declaration

	/**
	 * Constructor
	 * Call initComponents to initialize the GUI.
	 */
	public NewGameMenu() {
		initComponents();
	}

	/**
	 * Initialize components of the GUI
	 * Create all the GUI components, set their specific properties and add them to the 
	 * window. Also add any necessary ActionListeners.
	 */
	public void initComponents() {

		//Create button and add ActionListener
		humanPlay = new JButton("Human Play");
		humanPlay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				final JFrame popup = new JFrame("New Game");
				popup.setLayout(new FlowLayout());
				popup.setSize(370, 150); //TODO Figure out if there's a better way to set the size of the window.
				popup.setResizable(false);
				popup.setLocationRelativeTo(null);//This line makes the window show up in the center of the user's screen, regardless of resolution.

				//Make a JComboBox drop down filled with the names of all the saved game types.
				final JComboBox dropdown = new JComboBox(Builder.getArray());
				popup.add(new JLabel("Type: "));
				popup.add(dropdown);

				//Create button and add ActionListener
				final JButton done = new JButton("Start");
				String[] timerNames = { "No timer", "Bronstein Delay", "Fischer", "Fischer After", "Hour Glass",
						"Simple Delay", "Word" };
				final JComboBox timers = new JComboBox(timerNames);
				final JLabel totalTimeText = new JLabel("Total Time (sec): ");
				totalTimeText.setVisible(false);
				final TextField totalTime = new TextField("120", 3);
				totalTime.setVisible(false);
				final JLabel increaseText = new JLabel("Increment/delay (sec): ");
				increaseText.setVisible(false);
				final TextField increase = new TextField("10", 3);
				increase.setVisible(false);

				timers.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						String timerName = (String) timers.getSelectedItem();
						if (timerName.equals("No timer") == false) {
							totalTimeText.setVisible(true);
							totalTime.setVisible(true);
							increaseText.setVisible(true);
							increase.setVisible(true);
						}
						else {
							totalTimeText.setVisible(false);
							totalTime.setVisible(false);
							increaseText.setVisible(false);
							increase.setVisible(false);
						}
					}
				});

				popup.add(new JLabel("Timer: "));
				popup.add(timers);
				popup.add(totalTimeText);
				popup.add(totalTime);
				popup.add(increaseText);
				popup.add(increase);

				done.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						String timerName = (String) timers.getSelectedItem();
						long startTime = Integer.parseInt(totalTime.getText()) * 1000;
						long increment = Integer.parseInt(increase.getText()) * 1000;
						ChessTimer blackTimer = null;
						ChessTimer whiteTimer = null;
						//TODO something else here - possibly reflection? Make this better.
						if (timerName.equals("No timer")) {
							blackTimer = new NoTimer();
							whiteTimer = new NoTimer();
						}
						else if (timerName.equals("Bronstein Delay")) {
							blackTimer = new BronsteinDelay(increment, startTime, true);
							whiteTimer = new BronsteinDelay(increment, startTime, false);
						}
						else if (timerName.equals("Fischer")) {
							blackTimer = new Fischer(increment, startTime, false, true);
							whiteTimer = new Fischer(increment, startTime, false, false);
						}
						else if (timerName.equals("Fischer After")) {
							blackTimer = new Fischer(increment, startTime, true, true);
							whiteTimer = new Fischer(increment, startTime, true, false);
						}
						else if (timerName.equals("Hour Glass")) {
							blackTimer = new HourGlass(startTime / 2, true); //time is halved since this is actually
							//the timer the player is not allowed to exceed.
							whiteTimer = new HourGlass(startTime / 2, false);
						}
						else if (timerName.equals("Simple Delay")) {
							blackTimer = new SimpleDelay(increment, startTime, true);
							whiteTimer = new SimpleDelay(increment, startTime, false);
						}
						else {
							blackTimer = new Word(startTime);
							whiteTimer = new Word(startTime);
						}
						//Create the new panel and display it, then get rid of this pop up.

						Game toPlay = Builder.newGame((String) dropdown.getSelectedItem());
						toPlay.setTimers(whiteTimer, blackTimer);
						PlayGame game = new PlayGame(toPlay, false);
						Driver.getInstance().setPanel(game);

						popup.dispose();
					}
				});
				popup.add(done);

				//Create button and add ActionListener
				final JButton back = new JButton("Back");
				back.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						//Get rid of this pop up.
						popup.dispose();
					}
				});
				popup.add(back);

				popup.setVisible(true);
			}
		});

		//Create button and add ActionListener
		networkPlay = new JButton("Network Play");
		//TODO Add ActionListener to this button.

		//Create button and add ActionListener
		AIPlay = new JButton("AI Play");
		//TODO Add ActionListener to this button.

		//Create button and add ActionListener
		backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Return to the main screen.
				Driver.getInstance().revertPanel();
			}
		});

		//Layout stuff. Make it better later.
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout
				.setHorizontalGroup(
				layout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								layout
										.createSequentialGroup()
										.addGroup(
												layout
														.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addGroup(
																layout
																		.createSequentialGroup()
																		.addGap(20, 20, 20)
																		.addGroup(
																				layout
																						.createParallelGroup(
																								GroupLayout.Alignment.LEADING)
																						.addComponent(
																								AIPlay,
																								GroupLayout.PREFERRED_SIZE,
																								234,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								networkPlay,
																								GroupLayout.PREFERRED_SIZE,
																								234,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								humanPlay,
																								GroupLayout.PREFERRED_SIZE,
																								234,
																								GroupLayout.PREFERRED_SIZE)))
														.addGroup(layout.createSequentialGroup()
																.addGap(112, 112, 112)
																.addComponent(backButton)))
														.addContainerGap(20, Short.MAX_VALUE))
				);
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
								//.addContainerGap(162, Short.MAX_VALUE)
								.addComponent(humanPlay)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(networkPlay)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(AIPlay)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(backButton)
								.addGap(4, 4, 4))
				);
	}
}
