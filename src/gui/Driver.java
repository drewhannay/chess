package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;

import logic.Game;

/**
 * Driver.java
 * 
 * Driver to initiate the chess program.
 * 
 * @author Drew Hannay & Daniel Opdyke
 * 
 * CSCI 335, Wheaton College, Spring 2011
 * Phase 1
 * February 24, 2011
 */
final public class Driver extends JFrame {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -3533604157531154757L;

	/**
	 * Singleton reference to the Driver.
	 */
	private final static Driver d = new Driver();
	/**
	 * Button with listener to create a new Game.
	 */
	private JButton newGame;
	/**
	 * Button with listener to continue a saved Game.
	 */
	private JButton continueGame;
	/**
	 * Button with listener to view completed Games.
	 */
	private JButton viewGame;
	/**
	 * Button with listener to create new Game type.
	 */
	private JButton createType;
	/**
	 * JPanel to hold the contents of the main window.
	 */
	private JPanel mainPanel;
	/**
	 * A JPanel to hold any pop-up inquiry contents.
	 */
	JPanel otherPanel;

	/**
	 * Constructor
	 * Private, to make Driver a singleton
	 */
	private Driver() {
	}

	/**
	 * Get the singleton instance of the Driver.
	 * @return The singleton instance of the Driver
	 */
	public static Driver getInstance() {
		return d;
	}

	/**
	 * Initiate the program by creating a new Driver.
	 * @param args Arguments passed on the command line.
	 */
	public static void main(String[] args) {
		d.initComponents();
	}

	/**
	 * @return the menu bar for the window that "is" the main menu.
	 */
	public JMenuBar createMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		JMenuItem exitItem = new JMenuItem("Close Program");
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu.add(exitItem);
		menuBar.add(menu);
		return menuBar;
	}

	/**
	 * Initialize components of the GUI
	 * Create all the GUI components, set their specific properties and add them to the 
	 * window. Also add any necessary ActionListeners.
	 */
	private void initComponents() {

		setTitle("Chess Game Editor");//TODO Come up with better name for this game.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(450, 300);//TODO Figure out if there's a better way to set the size of the window.
		setLocationRelativeTo(null);//This line makes the window show up in the center of the user's screen, regardless of resolution.

		setLayout(new FlowLayout());
		setResizable(false);

		//This section makes the program match the look and feel of whatever system it's running on.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Error setting native LAF: " + e);
		}

		mainPanel = new JPanel();

		//Create button and add ActionListener
		newGame = new JButton("New Game");
		newGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Remove the current panel, set otherPanel, repaint.
				remove(mainPanel);
				otherPanel = new NewGameMenu();
				add(otherPanel);
				pack();
			}
		});

		//Create button and add ActionListener
		continueGame = new JButton("Continue Game");
		continueGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					File dir = new File("gamesInProgress");
					String[] files = dir.list();
					
					if(files == null){
						JOptionPane.showMessageDialog(null,
								"There are no saved games. Try starting a new game instead.",
								"No Completed Games", JOptionPane.ERROR_MESSAGE);
						return;
					}

					String choice = (String) JOptionPane.showInputDialog(Driver.getInstance(),
							"Please select a save file:",
							"Save File Select", JOptionPane.PLAIN_MESSAGE, null,
							files, null);
					if (choice == null)
						return;
					FileInputStream f_in = new FileInputStream(new File("gamesInProgress/" + choice));
					ObjectInputStream obj_in = new ObjectInputStream(f_in);
					Game toPlay = (Game) obj_in.readObject();
					remove(mainPanel);
					otherPanel = new PlayGame(toPlay, false);
					add(otherPanel);
					pack();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "There is no saved game. Start a New Game instead.",
							"No Saved Game", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		});

		//Create button and add ActionListener
		viewGame = new JButton("View Completed Games");
		viewGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					File dir = new File("completedGames");
					String[] files = dir.list();
					if(files == null){
						JOptionPane.showMessageDialog(null,
								"There are no completed games to display.",
								"No Completed Games", JOptionPane.ERROR_MESSAGE);
						return;
					}

					String choice = (String) JOptionPane.showInputDialog(Driver.getInstance(), "Please select a file:",
							"File Select", JOptionPane.PLAIN_MESSAGE, null,
							files, null);
					if (choice == null)
						return;

					File file = new File("completedGames/" + choice);

					Game toView;
					if (choice.endsWith(".acn")) {
						otherPanel = new PlayGame(true, file);
					}
					else {
						FileInputStream f_in = new FileInputStream(file);
						ObjectInputStream obj_in = new ObjectInputStream(f_in);
						toView = (Game) obj_in.readObject();
						otherPanel = new PlayGame(toView, true);
					}
					remove(mainPanel);
					add(otherPanel);
					pack();

				} catch (Exception e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,
							"Either there are no completed games or the game file is missing.",
							"No Completed Games", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		//Create button and add ActionListener
		createType = new JButton("Create New Game Type");
		createType.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Remove the current panel, set otherPanel, repaint.
				remove(mainPanel);
				otherPanel = new NewTypeMenu();
				add(otherPanel);
				pack();
			}
		});

		//Set the menuBar for the window.
		setJMenuBar(createMenu());

		//Layout stuff. Make it better later.
		GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
		mainPanel.setLayout(mainPanelLayout);
		mainPanelLayout.setHorizontalGroup(
				mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								mainPanelLayout.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(createType, GroupLayout.DEFAULT_SIZE, 166,
																Short.MAX_VALUE)
														.addComponent(newGame, GroupLayout.DEFAULT_SIZE, 166,
																Short.MAX_VALUE))
										.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(
												mainPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING,
														false)
														.addComponent(viewGame, GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(continueGame, GroupLayout.DEFAULT_SIZE, 168,
																Short.MAX_VALUE))
										.addContainerGap())
				);
		mainPanelLayout
				.setVerticalGroup(
				mainPanelLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								GroupLayout.Alignment.TRAILING,
								mainPanelLayout
										.createSequentialGroup()
										.addContainerGap(127, Short.MAX_VALUE)
										.addGroup(
												mainPanelLayout
														.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addGroup(
																mainPanelLayout
																		.createSequentialGroup()
																		.addComponent(newGame,
																				GroupLayout.PREFERRED_SIZE, 37,
																				GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				LayoutStyle.ComponentPlacement.UNRELATED)
																		.addComponent(viewGame,
																				GroupLayout.PREFERRED_SIZE, 37,
																				GroupLayout.PREFERRED_SIZE))
														.addGroup(
																mainPanelLayout
																		.createSequentialGroup()
																		.addComponent(continueGame,
																				GroupLayout.PREFERRED_SIZE, 37,
																				GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				LayoutStyle.ComponentPlacement.UNRELATED)
																		.addComponent(createType,
																				GroupLayout.PREFERRED_SIZE, 37,
																				GroupLayout.PREFERRED_SIZE)))
												.addGap(31, 31, 31))

				);
		add(mainPanel);
		setVisible(true);
	}

	/**
	 * Revert the display to the main screen.
	 */
	public void revertPanel() {
		remove(otherPanel);
		add(mainPanel);
		setJMenuBar(createMenu());
		pack();
	}

	/**
	 * @param menu the JMenuBar to add onto the window.
	 */
	public void setMenu(JMenuBar menu) {
		setJMenuBar(menu);
	}

	/**
	 * Display a given JPanel in this window.
	 * Remove otherPanel, change what otherPanel is, replace it, repaint.
	 * @param panel The panel to be set.
	 */
	public void setPanel(JPanel panel) {
		remove(otherPanel);
		otherPanel = panel;
		add(otherPanel);
		pack();
	}

}
