package gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import logic.Game;
import timer.ChessTimer;

/**
 * Driver.java
 * 
 * Driver to initiate the chess program.
 * 
 * @author Drew Hannay, Daniel Opdyke, Andrew Wolfe & John McCormick
 * 
 * CSCI 335, Wheaton College, Spring 2011 Phase 2 April 7, 2011
 */

final public class Driver extends JFrame
{

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
	 * JMenuBar to hold menu items.
	 */
	private JMenuBar menuBar;
	/**
	 * Menu Item for explaining gameplay
	 */
	public JMenuItem gamePlayHelp;
	/**
	 * Menu item to explain making variants
	 */
	public JMenuItem variantHelp;
	/**
	 * Menu item to display help on the completed games window
	 */
	public JMenuItem completedHelp;
	/**
	 * Menu to diplay the help items
	 */
	public JMenu helpMenu;
	/**
	 * JPanel representing another arbitrary panel to be displayed in this
	 * window.
	 */
	private JPanel otherPanel;
	/**
	 * Menu to hold the options created in playGame for saving and such in game.
	 */
	public JMenu gameOptions;
	/**
	 * Menu item for the File options
	 */
	public JMenu fileMenu;
	
	/**
	 * Menu item for returning to main menu
	 */
	public JMenuItem mainMenu;

	/**
	 * Initiate the program by creating a new Driver.
	 * 
	 * @param args Arguments passed on the command line.
	 */
	public static void main(String[] args)
	{
		d.initComponents();
	}

	/**
	 * Constructor Private, to make Driver a singleton
	 */
	private Driver()
	{
	}

	/**
	 * Initialize components of the GUI Create all the GUI components, set their
	 * specific properties and add them to the window. Also add any necessary
	 * ActionListeners.
	 */
	private void initComponents()
	{

		setTitle("Chess Master 9001");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(325, 340);
		setLocationRelativeTo(null);// This line makes the window show up in the
									// center of the user's screen, regardless
									// of resolution.

		setResizable(false);
		setLayout(new FlowLayout());
		setResizable(true);

		// This section makes the app match the look and feel of whatever system
		// it's running on.
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e)
		{
		}

		// Setting up a new panel to hold everything on the main window
		mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());

		// Adding the image to the front page
		BufferedImage temp = null;
		try
		{
			temp = ImageIO.read(new File("./images/front_page_image.jpeg")); // Gets
																				// it
																				// from
																				// the
																				// image
																				// folder
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
		// Makes the image an icon and ands it to a JLabel
		ImageIcon picture = new ImageIcon(temp);
		JLabel pictureHolder = new JLabel();
		picture.setImage(picture.getImage().getScaledInstance(300, 200,
				Image.SCALE_SMOOTH));
		pictureHolder.setIcon(picture);

		// Creates the button to make a new game
		newGame = new JButton("New Game");
		newGame.setToolTipText("Press to start a new game of Chess");
		newGame.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Remove the current panel, set otherPanel, repaint.
				helpMenu.setText("Game Help"); // Sets the help menu to say Game
												// Help
				gamePlayHelp.setVisible(true); // Shows the help for general
												// game play
				// gameOptions.setVisible(true); //Turns on the game options
				remove(mainPanel);
				otherPanel = new NewGameMenu();
				setPanel(otherPanel);
				pack();
			}
		});

		// Creates the button to load a saved game
		continueGame = new JButton("Load Game");
		continueGame.setToolTipText("Press to load a saved game");
		continueGame.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					File dir = new File("gamesInProgress");
					String[] files = dir.list();

					if (files == null)
					{
						JOptionPane
								.showMessageDialog(
										null,
										"There are no saved games. Try starting a new game instead.",
										"No Completed Games",
										JOptionPane.ERROR_MESSAGE);
						return;
					}

					String choice = (String) JOptionPane.showInputDialog(
							Driver.getInstance(), "Please select a save file:",
							"Save File Select", JOptionPane.PLAIN_MESSAGE,
							null, files, null);
					if (choice == null)
						return;
					FileInputStream f_in = new FileInputStream(new File(
							"gamesInProgress/" + choice));
					ObjectInputStream obj_in = new ObjectInputStream(f_in);
					Game toPlay = (Game) obj_in.readObject();
					// Sets the help menu info to be specific for game play
					helpMenu.setText("Game Help");
					gamePlayHelp.setVisible(true);
					if (gameOptions != null)
						gameOptions.setVisible(true);

					// Changes panels
					remove(mainPanel);
					otherPanel = new PlayGame(toPlay, false);
					setPanel(otherPanel);
					pack();
				} catch (Exception e)
				{
					e.printStackTrace();
					JOptionPane
							.showMessageDialog(
									null,
									"There is no saved game. Start a New Game instead.",
									"No Saved Game", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// Creates a button to load completed games
		viewGame = new JButton("View Completed Games");
		viewGame.setToolTipText("Press to review a finished game");
		viewGame.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					File dir = new File("completedGames");
					String[] files = dir.list();
					if (files == null)
					{
						JOptionPane
								.showMessageDialog(
										null,
										"There are no completed games to display.",
										"No Completed Games",
										JOptionPane.ERROR_MESSAGE);
						return;
					}

					String choice = (String) JOptionPane.showInputDialog(
							Driver.getInstance(), "Please select a file:",
							"File Select", JOptionPane.PLAIN_MESSAGE, null,
							files, null);
					if (choice == null)
						return;

					File file = new File("completedGames/" + choice);

					Game toView;
					if (choice.endsWith(".acn"))
					{
						try
						{
							otherPanel = new PlayGame(true, file);
						} catch (Exception e)
						{
							JOptionPane
									.showMessageDialog(
											null,
											"This file contains invalid ACN notation. Please check the format and try again");
							return;
						}
					} else
					{
						FileInputStream f_in = new FileInputStream(file);
						ObjectInputStream obj_in = new ObjectInputStream(f_in);
						toView = (Game) obj_in.readObject();
						otherPanel = new PlayGame(toView, true);
					}
					// Sets up the help for playng back and changes the window
					completedHelp.setVisible(true);
					helpMenu.setText("Playback Help");
					;
					remove(mainPanel);
					setPanel(otherPanel);
					pack();
				} catch (Exception e)
				{
					e.printStackTrace();
					JOptionPane
							.showMessageDialog(
									null,
									"Either there are no completed games or the game file is missing.",
									"No Completed Games",
									JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// Creates a button to allow creation of variants
		createType = new JButton("Create New Game Type");
		createType.setToolTipText("Press to make up a new kind of chess game");
		createType.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Remove the current panel, set otherPanel, repaint.
				variantHelp.setVisible(true);
				helpMenu.setText("Variant Help");
				remove(mainPanel);
				otherPanel = new NewTypeMenu();
				setPanel(otherPanel);
				pack();
			}
		});

		// Create MenuBar and add MenuItems
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		fileMenu.setForeground(Color.WHITE);
		fileMenu.setMnemonic('F');

		// Making the help menu
		helpMenu = new JMenu("Help");
		helpMenu.setForeground(Color.white);
		helpMenu.setMnemonic('H');

		// Adding Help to menu with xkcd.com picture. will remove later
		JMenuItem helpMenuItem = new JMenuItem("General Help", KeyEvent.VK_H);
		helpMenuItem.setToolTipText("Press me to get general help");

		BufferedImage help = null;
		try
		{
			help = ImageIO.read(new File(
					"./images/tech_support_cheat_sheet.png"));
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
		// Making the icon and adding it to the appropriate menu item
		final ImageIcon helpPicture = new ImageIcon(help);
		helpPicture.setImage(helpPicture.getImage().getScaledInstance(600, 600,
				Image.SCALE_SMOOTH));
		helpMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				JOptionPane.showMessageDialog(Driver.this, "", "Help", 0,
						helpPicture);
			}

		});
		helpMenu.add(helpMenuItem);

		BufferedImage helpGame = null;
		try
		{
			helpGame = ImageIO.read(new File("./images/gameplay_help.png")); // Gets
																				// it
																				// from
																				// the
																				// image
																				// folder
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
		// Makes the image an icon and ands it to a JLabel
		final ImageIcon gameHelpPicture = new ImageIcon(helpGame);
		gameHelpPicture.setImage(gameHelpPicture.getImage().getScaledInstance(
				700, 375, Image.SCALE_SMOOTH));

		// Adding the menu item to display help specific for normal game play
		gamePlayHelp = new JMenuItem("Game Play Help", KeyEvent.VK_G);
		gamePlayHelp.setToolTipText("Press if you need help with game play");
		gamePlayHelp.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				JOptionPane.showMessageDialog(null, "", "Game Play Help", 0,
						gameHelpPicture);
			}

		});
		helpMenu.add(gamePlayHelp);
		gamePlayHelp.setVisible(false);

		BufferedImage helpMe = null;
		try
		{
			helpMe = ImageIO.read(new File("./images/variant_help.png")); // Gets
																			// it
																			// from
																			// the
																			// image
																			// folder
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
		// Makes the image an icon and ands it to a JLabel
		final ImageIcon variantPicture = new ImageIcon(helpMe);
		variantPicture.setImage(variantPicture.getImage().getScaledInstance(
				650, 300, Image.SCALE_SMOOTH));

		// Adds the menu item to help for specific instructions on making
		// variants
		variantHelp = new JMenuItem("Variant Making Help", KeyEvent.VK_V);
		variantHelp
				.setToolTipText("Press me for help setting up your chess game");
		variantHelp.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				JOptionPane.showMessageDialog(null, "", "Variant Help", 0,
						variantPicture);
			}

		});
		helpMenu.add(variantHelp);
		variantHelp.setVisible(false);

		// Menu item for making the help item to explain playing back a game
		completedHelp = new JMenuItem("Game Review Help", KeyEvent.VK_R);
		completedHelp
				.setToolTipText("Press me for help playing back completed games");
		completedHelp.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane
						.showMessageDialog(
								Driver.this,
								"To play back a move press the Next Move button\n"
										+ "or press the Last Move button to rewind one move.\n");
			}
		});
		helpMenu.add(completedHelp);
		completedHelp.setVisible(false);

		// Adds menu item to load a new game
		JMenuItem newGameItem = new JMenuItem("New Game", KeyEvent.VK_N);
		newGameItem.setToolTipText("Press to start a new Game");
		newGameItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Remove the current panel, set otherPanel, repaint.
				helpMenu.setText("Game Help"); //Sets the help menu to say Game Help
				gamePlayHelp.setVisible(true); //Shows the help for general game play
				//gameOptions.setVisible(true); //Turns on the game options
				remove(mainPanel);
				otherPanel = new NewGameMenu();
				setPanel(otherPanel);
				pack();
			}
		});

		fileMenu.add(newGameItem);

		// Adding Main Menu Option
		mainMenu = new JMenuItem("Main Menu", KeyEvent.VK_M);
		mainMenu.setToolTipText("Press to return to the Main Menu");
		mainMenu.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// Changes help to only be the general help and displays the
				// main panel
				helpMenu.setText("Help");
				variantHelp.setVisible(false);
				gamePlayHelp.setVisible(false);
				completedHelp.setVisible(false);
				if (gameOptions != null)
					gameOptions.setVisible(false);
				if (otherPanel != null)
					remove(otherPanel);
				ChessTimer.stopTimers();
				add(mainPanel);
				pack();
			}

		});
		mainMenu.setVisible(false);
		fileMenu.add(mainMenu);

		// Adds menu item to quit the program
		JMenuItem exitMenuItem = new JMenuItem("Quit", KeyEvent.VK_Q);
		exitMenuItem.setToolTipText("Press to close the program");
		exitMenuItem.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				int answer = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to Quit?", "Quit?",
						JOptionPane.YES_NO_OPTION);
				if (answer == 0)
					System.exit(0);
			}
		});
		fileMenu.add(exitMenuItem);

		menuBar.add(fileMenu);
		menuBar.add(helpMenu);

		// Set the menuBar for the window.
		setJMenuBar(menuBar);

		// Setting up the layout for the main panel
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.0;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 0;
		mainPanel.add(pictureHolder, c); // adding the front page image

		// New Game
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		mainPanel.add(newGame, c); // adding the New Game button

		// Create new game type
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		mainPanel.add(createType, c); // Adding variant creation button

		// Continue
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		mainPanel.add(continueGame, c); // Adds the load game button

		// View Completed
		c.gridx = 2; // aligned with button 2
		c.gridy = 2; // third row
		c.gridwidth = 1;
		mainPanel.add(viewGame, c); // Adds the completed games button

		add(mainPanel);
		setVisible(true);
	}

	/**
	 * Sets up a new game and clears out any old game being played
	 */
	public void newGame()
	{

		variantHelp.setVisible(false); // Makes sure that the appropriate help
										// menus are displayed
		helpMenu.setText("Game Help");
		gamePlayHelp.setVisible(true);

		if (PlayGame.menu != null)
			PlayGame.menu.setVisible(false);
		// Resets the panels being displayed to only contain the new game
		if (otherPanel != null)
			remove(otherPanel);
		if (mainPanel != null)
			remove(mainPanel);
		otherPanel = new NewGameMenu();
		add(otherPanel);
		ChessTimer.stopTimers();
		gamePlayHelp.setVisible(true);
		pack();
	}

	/**
	 * Display a given JPanel in this window. Remove otherPanel, change what
	 * otherPanel is, replace it, repaint.
	 * 
	 * @param panel The panel to be set.
	 */
	public void setPanel(JPanel panel)
	{
		remove(otherPanel);
		mainMenu.setVisible(true);
		otherPanel = panel;
		add(otherPanel);
		pack();
	}

	/**
	 * Revert the display to the main screen.
	 */
	public void revertPanel()
	{
		remove(otherPanel);
		mainMenu.setVisible(false);
		add(mainPanel);
		pack();
	}

	/**
	 * Get the singleton instance of the Driver.
	 * 
	 * @return The singleton instance of the Driver
	 */
	public static Driver getInstance()
	{
		return d;
	}

	/**
	 * Method to add options menu for in the middle of the game
	 * 
	 * @param menu the menu being added
	 */
	public void setMenu(JMenu menu)
	{
		gameOptions = menu;
		menuBar.add(gameOptions);
		gameOptions.setVisible(true);
		gameOptions.setMnemonic('O');
		gameOptions.setText("Options");
		gameOptions.setToolTipText("Use me to access game options");
		gameOptions.setForeground(Color.white);

	}

}
