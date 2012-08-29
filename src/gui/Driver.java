package gui;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import logic.Game;
import timer.ChessTimer;
import utility.FileUtility;

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
	public static JMenu gameOptions;
	/**
	 * Menu item for the File options
	 */
	public JMenu fileMenu;
	/**
	 * Menu item for returning to main menu
	 */
	public JMenuItem mainMenu;
	/**
	 * Window Listener to prevent closing during playgame
	 */
	public WindowListener windowListener;

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
		setResizable(false);
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
		}
		catch (Exception e)
		{
		}

		// Setting up a new panel to hold everything on the main window
		mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());

		// Adding the image to the front page
		BufferedImage temp = null;
		try
		{
			// read the image from the class resources
			temp = ImageIO.read(getClass().getResource("/chess_picture.png"));
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		// Makes the image an icon and ands it to a JLabel
		ImageIcon picture = new ImageIcon(temp);
		JLabel pictureHolder = new JLabel();
		picture.setImage(picture.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH));
		pictureHolder.setIcon(picture);

		// Code for adding System Tray icon for Windows
		final SystemTray sysTray = SystemTray.getSystemTray();
		TrayIcon tray = new TrayIcon(picture.getImage().getScaledInstance(25, 18, Image.SCALE_SMOOTH));
		try
		{
			sysTray.add(tray);
		}
		catch (AWTException e1)
		{
			e1.printStackTrace();
		}

		// Set Icon Program icon to be Main picture
		setIconImage(temp);

		// Creates the button to make a new game
		newGame = new JButton("New Game");
		newGame.setToolTipText("Press to start a new game of Chess");
		newGame.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Remove the current panel, set otherPanel, repaint.
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
				String[] files = FileUtility.getGamesInProgressFileArray();

				if (files.length == 0)
				{
					JOptionPane.showMessageDialog(null, "There are no saved games. Try starting a new game instead.",
							"No Completed Games", JOptionPane.ERROR_MESSAGE);
					return;
				}

				final JFrame popped = new JFrame("Load Saved Game");
				popped.setLayout(new GridBagLayout());
				popped.setSize(225, 200);
				popped.setResizable(false);
				popped.setLocationRelativeTo(null);
				GridBagConstraints c = new GridBagConstraints();

				final JList list = new JList(FileUtility.getGamesInProgressFileArray());
				final JScrollPane scrollPane = new JScrollPane(list);
				scrollPane.setPreferredSize(new Dimension(200, 200));
				list.setSelectedIndex(0);

				JButton next = new JButton("Next");
				next.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						FileInputStream f_in;
						ObjectInputStream obj_in;
						Game toPlay;
						try
						{
							if (list.getSelectedValue() == null)
							{
								JOptionPane.showMessageDialog(Driver.getInstance(), "Please select a game", "Error", -1);
								return;
							}
							f_in = new FileInputStream(FileUtility.getGamesInProgressFile(list.getSelectedValue().toString()));
							obj_in = new ObjectInputStream(f_in);
							toPlay = (Game) obj_in.readObject();
							// Sets the help menu info to be specific for game
							// play
							if (gameOptions != null)
								gameOptions.setVisible(true);

							// Changes panels
							remove(mainPanel);
							otherPanel = new PlayGame(toPlay, false);
							setPanel(otherPanel);
							pack();
							popped.dispose();
						}
						catch (Exception e)
						{
							e.printStackTrace();
							JOptionPane.showMessageDialog(null, "There are no valid saved games. Start a New Game instead.",
									"Invalid Saved Games", JOptionPane.ERROR_MESSAGE);
						}
					}
				});

				JButton cancel = new JButton("Cancel");
				cancel.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						popped.dispose();
					}
				});

				JButton delete = new JButton("Delete Saved Game");
				delete.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e)
					{
						if (list.getSelectedValue() != null)
						{
							boolean success = FileUtility.getGamesInProgressFile(list.getSelectedValue().toString()).delete();
							if (!success)
							{
								JOptionPane.showMessageDialog(Driver.getInstance(), "Saved game was not deleted successfully",
										"Error", -1);
							}
							else
							{
								list.setListData(FileUtility.getGamesInProgressFileArray());
								list.setSelectedIndex(0);
								if (list.getSelectedValue() == null)
								{
									JOptionPane.showMessageDialog(Driver.getInstance(),
											"There are no more completed games. Returning to Main Menu", "No Completed Games",
											JOptionPane.PLAIN_MESSAGE);
									popped.dispose();
								}
								scrollPane.getViewport().add(list, null);
							}
						}
						else
						{
							JOptionPane.showMessageDialog(null, "There are currently no save files!", "No save file selected!",
									JOptionPane.PLAIN_MESSAGE);
						}
					}
				});

				c.gridx = 0;
				c.gridy = 0;
				c.gridwidth = 2;
				c.insets = new Insets(5, 5, 5, 5);
				popped.add(scrollPane, c);

				c.gridx = 0;
				c.gridy = 1;
				popped.add(delete, c);

				c.weighty = 1.0;
				c.weightx = 1.0;
				c.gridx = 0;
				c.gridy = 2;
				c.gridwidth = 1;
				c.anchor = GridBagConstraints.EAST;
				popped.add(next, c);

				c.gridx = 1;
				c.gridy = 2;
				c.anchor = GridBagConstraints.WEST;
				popped.add(cancel, c);

				popped.setVisible(true);
				popped.pack();
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
					String[] files = FileUtility.getCompletedGamesFileArray();
					if (files.length == 0)
					{
						JOptionPane.showMessageDialog(null, "There are no completed games to display.", "No Completed Games",
								JOptionPane.PLAIN_MESSAGE);
						return;
					}

					final JFrame popped = new JFrame("View Completed Game");
					popped.setLayout(new GridBagLayout());
					popped.setSize(225, 200);
					popped.setResizable(false);
					popped.setLocationRelativeTo(null);
					GridBagConstraints c = new GridBagConstraints();

					final JList list = new JList(FileUtility.getCompletedGamesFileArray());
					final JScrollPane scrollPane = new JScrollPane(list);
					scrollPane.setPreferredSize(new Dimension(200, 200));
					list.setSelectedIndex(0);

					JButton next = new JButton("Next");
					next.addActionListener(new ActionListener()
					{

						@Override
						public void actionPerformed(ActionEvent arg0)
						{
							if (list.getSelectedValue() == null)
							{
								JOptionPane.showMessageDialog(Driver.getInstance(), "Please select a game", "Error",
										JOptionPane.PLAIN_MESSAGE);
								return;
							}
							File file = FileUtility.getCompletedGamesFile(list.getSelectedValue().toString());
							FileInputStream f_in;
							ObjectInputStream obj_in;
							Game toView;
							if (list.getSelectedValue().toString().endsWith(".acn"))
							{
								try
								{
									otherPanel = new PlayGame(true, file);
								}
								catch (Exception e)
								{
									JOptionPane.showMessageDialog(null,
											"This file contains invalid ACN notation. Please check the format and try again",
											"Invalid Notation", JOptionPane.PLAIN_MESSAGE);
									return;
								}
							}
							else
							{
								try
								{
									f_in = new FileInputStream(file);
									obj_in = new ObjectInputStream(f_in);
									toView = (Game) obj_in.readObject();

									// Changes panels
									remove(mainPanel);
									otherPanel = new PlayGame(toView, false);
									setPanel(otherPanel);
									pack();
									popped.dispose();
								}
								catch (Exception e)
								{
									e.printStackTrace();
									JOptionPane.showMessageDialog(null,
											"This game is corrupted, please choose another or start a New Game instead.",
											"Invalid Completed Game", JOptionPane.PLAIN_MESSAGE);
								}
							}
							// Changes panels
							remove(mainPanel);
							setPanel(otherPanel);
							deactivateWindowListener();
							pack();
							popped.dispose();
						}
					});

					JButton cancel = new JButton("Cancel");
					cancel.addActionListener(new ActionListener()
					{

						@Override
						public void actionPerformed(ActionEvent arg0)
						{
							popped.dispose();
						}
					});

					JButton delete = new JButton("Delete Completed Game");
					delete.addActionListener(new ActionListener()
					{

						@Override
						public void actionPerformed(ActionEvent e)
						{
							if (list.getSelectedValue() != null)
							{
								boolean success = FileUtility.getCompletedGamesFile(list.getSelectedValue().toString()).delete();
								if (!success)
								{
									JOptionPane.showMessageDialog(Driver.getInstance(), "Completed game was not deleted successfully",
											"Error", JOptionPane.PLAIN_MESSAGE);
								}
								else
								{
									list.removeAll();
									list.setListData(FileUtility.getCompletedGamesFileArray());
									list.setSelectedIndex(0);
									if (list.getSelectedValue() == null)
									{
										JOptionPane.showMessageDialog(Driver.getInstance(),
												"There are no more completed games. Returning to Main Menu", "No Completed Games",
												JOptionPane.PLAIN_MESSAGE);
										popped.dispose();
									}
									scrollPane.getViewport().add(list, null);
								}
							}
							else
							{
								JOptionPane.showMessageDialog(null, "There are currently no completed games!",
										"No completed game selected!", JOptionPane.PLAIN_MESSAGE);
							}
						}
					});

					c.gridx = 0;
					c.gridy = 0;
					c.gridwidth = 2;
					c.insets = new Insets(5, 5, 5, 5);
					popped.add(scrollPane, c);

					c.gridx = 0;
					c.gridy = 1;
					popped.add(delete, c);

					c.weighty = 1.0;
					c.weightx = 1.0;
					c.gridx = 0;
					c.gridy = 2;
					c.gridwidth = 1;
					c.anchor = GridBagConstraints.EAST;
					popped.add(next, c);

					c.gridx = 1;
					c.gridy = 2;
					c.anchor = GridBagConstraints.WEST;
					popped.add(cancel, c);

					popped.setVisible(true);
					popped.pack();
				}
				catch (Exception e)
				{
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Either there are no completed games or the game file is missing.",
							"No Completed Games", JOptionPane.PLAIN_MESSAGE);
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
				remove(mainPanel);
				otherPanel = new CustomSetupMenu();
				setPanel(otherPanel);
				pack();
			}
		});

		// Create MenuBar and add MenuItems
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');

		// Making the help menu
		helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');

		// Sets up the Help Menu items
		final JFrame help = setUpHelp();
		final JFrame about = setUpAbout();

		// Adding Help to menu
		JMenuItem helpMenuItem = new JMenuItem("Browse Help", KeyEvent.VK_H);
		helpMenuItem.setToolTipText("Click on me to get help");
		helpMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				help.setVisible(true);
			}
		});
		helpMenu.add(helpMenuItem);

		JMenuItem aboutItem = new JMenuItem("About Chess Master 9001", KeyEvent.VK_A);
		aboutItem.setToolTipText("Information about Chess Master 9001");
		aboutItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				about.setVisible(true);
			}
		});
		helpMenu.add(aboutItem);

		// Adds menu item to load a new game
		JMenuItem newGameItem = new JMenuItem("New Game", KeyEvent.VK_N);
		newGameItem.setToolTipText("Press to start a new Game");
		newGameItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Remove the current panel, set otherPanel, repaint.
				remove(mainPanel);
				if (otherPanel != null)
					remove(otherPanel);
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
				if (gameOptions != null)
					gameOptions.setVisible(false);
				if (otherPanel != null)
					remove(otherPanel);
				ChessTimer.stopTimers();
				revertPanel();
				pack();
			}

		});
		mainMenu.setVisible(false);
		fileMenu.add(mainMenu);

		JMenuItem preferences = new JMenuItem("Preferences", KeyEvent.VK_P);
		preferences.setToolTipText("Press me to change your preferences");
		preferences.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				final JFrame popup = new JFrame("Square Options");
				popup.setSize(370, 120);
				popup.setLocationRelativeTo(null);
				popup.setLayout(new GridBagLayout());
				popup.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				GridBagConstraints c = new GridBagConstraints();

				final JPanel holder = new JPanel();
				holder.setBorder(BorderFactory.createTitledBorder("Default Completed Game Save Location"));
				final JLabel currentLabel = new JLabel("Current Save Location");
				final JTextField current = new JTextField(FileUtility.getDefaultCompletedLocation());
				current.setEditable(false);
				final JButton changeLocation = new JButton("Choose New Save Location");
				final JButton reset = new JButton("Reset to Default Location");
				final JButton cancel = new JButton("Cancel");
				cancel.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e)
					{
						popup.dispose();
					}
				});

				holder.add(currentLabel);
				holder.add(current);

				try
				{
					final File pref = FileUtility.getPreferencesFile();
					if (!pref.exists())
					{
						try
						{
							pref.createNewFile();
							BufferedWriter bw = new BufferedWriter(new FileWriter(pref, true));
							bw.write("DefaultPreferencesSet = true");
							bw.newLine();
							bw.write("DefaultSaveLocation = " + FileUtility.getDefaultCompletedLocation());
							bw.close();
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}
					FileInputStream fstream = null;
					fstream = new FileInputStream(pref);
					DataInputStream in = new DataInputStream(fstream);
					BufferedReader br = new BufferedReader(new InputStreamReader(in));
					String line;
					line = br.readLine();
					line = br.readLine();
					fstream.close();
					in.close();
					br.close();
					current.setText(line.substring(22));

					reset.addActionListener(new ActionListener()
					{

						@Override
						public void actionPerformed(ActionEvent arg0)
						{
							try
							{
								BufferedWriter bw = new BufferedWriter(new FileWriter(pref, true));
								bw.write("DefaultPreferencesSet = true");
								bw.newLine();
								bw.write("DefaultSaveLocation = " + FileUtility.getDefaultCompletedLocation());
								bw.close();
							}
							catch (IOException ae)
							{
								ae.printStackTrace();
							}
						}
					});

					changeLocation.addActionListener(new ActionListener()
					{

						@Override
						public void actionPerformed(ActionEvent e)
						{
							if (!pref.exists())
							{
								try
								{
									pref.createNewFile();
									BufferedWriter bw = new BufferedWriter(new FileWriter(pref, true));
									bw.write("DefaultPreferencesSet = false");
									bw.newLine();
									bw.write("DefaultSaveLocation = " + FileUtility.getDefaultCompletedLocation());
									bw.close();
								}
								catch (IOException ae)
								{
									ae.printStackTrace();
								}
							}
							try
							{
								PrintWriter writer = new PrintWriter(pref);
								JFileChooser fc = new JFileChooser();
								fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
								int returnVal = fc.showOpenDialog(Driver.getInstance());
								BufferedWriter bw = new BufferedWriter(new FileWriter(pref, true));
								bw.write("DefaultPreferencesSet = true");
								bw.newLine();
								if (returnVal == JFileChooser.APPROVE_OPTION)
								{
									writer.print("");
									writer.close();
									bw.write("DefaultSaveLocation = " + fc.getSelectedFile().getAbsolutePath());
									bw.close();
									current.setText(fc.getSelectedFile().getAbsolutePath());
								}
								else
								{
									bw.close();
									return;
								}
							}
							catch (Exception ae)
							{
								ae.printStackTrace();
							}
						}
					});
				}
				catch (Exception e)
				{
					JOptionPane.showMessageDialog(null, "That is not a valid location to save your completed games.",
							"Invalid Location", JOptionPane.PLAIN_MESSAGE);
					e.printStackTrace();
				}

				c.gridx = 0;
				c.gridy = 0;
				c.gridwidth = 2;
				c.insets = new Insets(5, 5, 5, 5);
				popup.add(holder, c);
				c.gridx = 0;
				c.gridy = 1;
				c.weightx = 1;
				c.gridwidth = 1;
				c.anchor = GridBagConstraints.EAST;
				popup.add(changeLocation, c);
				c.gridx = 1;
				c.gridy = 1;
				c.anchor = GridBagConstraints.WEST;
				popup.add(reset, c);
				c.gridx = 0;
				c.gridy = 2;
				c.gridwidth = 2;
				c.anchor = GridBagConstraints.CENTER;
				popup.add(cancel, c);

				popup.pack();
				popup.setVisible(true);
			}
		});
		fileMenu.add(preferences);

		// Adds menu item to quit the program
		JMenuItem exitMenuItem = new JMenuItem("Quit", KeyEvent.VK_Q);
		exitMenuItem.setToolTipText("Press to close the program");
		exitMenuItem.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to Quit?", "Quit?", JOptionPane.YES_NO_OPTION,
						JOptionPane.PLAIN_MESSAGE);
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
		if (otherPanel.toString().contains("PlayGame") || otherPanel.toString().contains("PlayNetGame"))
			activateWindowListener();
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
		deactivateWindowListener();
		pack();
		setLocationRelativeTo(null);
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
	}

	/**
	 * Method for setting up the Help window
	 */
	private JFrame setUpHelp()
	{
		JFrame helpPop = new JFrame();
		helpPop.setTitle("Help");
		helpPop.setSize(825, 525);
		helpPop.setResizable(false);
		helpPop.setLocationRelativeTo(this);
		helpPop.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		helpPop.setLayout(new GridBagLayout());
		GridBagConstraints h = new GridBagConstraints();

		JPanel gamePlayHelp = new JPanel();
		JPanel variantMakingHelp = new JPanel();
		JPanel generalHelp = new JPanel();
		JPanel pieceMakingHelp = new JPanel();

		JTabbedPane helpTypes = new JTabbedPane();
		helpTypes.addTab("General Help", null, generalHelp, "Click for General Help with Chess Master 9001");
		helpTypes.addTab("Game Play Help", null, gamePlayHelp, "Click for help playing Chess");
		helpTypes.addTab("New Game Type Help", null, variantMakingHelp, "Click for help making a new Game Type");
		helpTypes.addTab("Piece Making Help", null, pieceMakingHelp, "Click for help making new Game Pieces");
		helpTypes.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		h.gridy = 0;
		h.fill = GridBagConstraints.HORIZONTAL;
		helpPop.add(helpTypes, h);
		h.fill = GridBagConstraints.HORIZONTAL;
		h.gridy = 1;

		JTextArea gamePlayText = new JTextArea();
		gamePlayText.setEditable(false);
		gamePlayText.setText("To move a piece simply click on it and the places you can move it will be highlighted.\n"
				+ "\tIf that piece has no moves, no squares will be highlighted.\n\n" + "For games with an objective piece:\n"
				+ "\tIf the Objective is in check the attacking piece will be highlighted in Red\n"
				+ "\tWhile the piece is in check, only moves that break check are allowed\n\n"
				+ "If you want to undo a move simply press Undo\n" + "\tThis feature is unavailable in Network Play\n\n"
				+ "Options specific to the game can be found under Options:\n"
				+ "\t1. Save and Quit will save your current game and return to the main menu\n"
				+ "\t\t- This feature is unavailable in Network Play\n" + "\t2. Declare Draw will declare the game to be a draw\n"
				+ "\t\t- In network Play this feature will request a draw from the other player\n\n"
				+ "Once a game has been completed or decalred a draw it can no longer be played,\n"
				+ "but it can be reviewed from the View Completed Games on the Main Menu");
		gamePlayHelp.add(gamePlayText);

		JTextArea generalHelpText = new JTextArea();
		generalHelpText.setEditable(false);
		generalHelpText.setText("Thanks for playing Chess Master 9001!\n\n"
				+ "Our Main Menu is designed to quickly guide you to the major 4 features of Chess Master 9001:\n"
				+ "\t1. New Game - This will start a new game of Chess which you can play against\n"
				+ "\t\t\tanother person or against an AI\n"
				+ "\t2. Create New Game Type - This will allow you create a Chess Variant of your own\n"
				+ "\t3. View Completed Games - This will let you watch games you've already completed\n"
				+ "\t4. Load Game - This will load a game you were previously playing\n" + "\t\t\tso you can keep playing\n\n"
				+ "Also at any time you can use the File menu or the Options menu to see what\n" + "options are available to you.");
		generalHelp.add(generalHelpText);

		JTextArea variantMakingHelpText = new JTextArea();
		variantMakingHelpText.setEditable(false);
		variantMakingHelpText.setText("Once you choose to create a new Game Type you will need to choose a few options:\n"
				+ "\t1. Create a Name for your game\n" + "\t2. Set up what the board will look like\n"
				+ "\t3. Create any custom pieces you need\n" + "\t4. Determine the rules and how to win of your game\n"
				+ "\t5. Determine how many turns each player should get per round\n\n" + "To place a piece on the board:\n"
				+ "\t1. Click on the piece name from the list of piece types\n"
				+ "\t2. Click on the Square containing the piece color type you want to place\n"
				+ "\t3. Click anywhere on the board to drop the piece\n" + "\t\t- You can keep clicking other squares to drop more\n"
				+ "\t4. To change what piece you are dropping choose a new one from the list\n\n"
				+ "To Remove pieces or change squares on the board:\n" + "\t1. Clear your piece selection\n"
				+ "\t\t - You can do this by clicking on the piece that has a Blue background\n"
				+ "\t2. Click on any square with a piece in it to remove the piece\n"
				+ "\t3. Click on any empty square to change it's color or whether it is habitable\n\n"
				+ "To allow a piece to promote to something else. Simply click on the piece in the list\n"
				+ "and choose Promote This Piece\n\n"
				+ "When you are completly finished press the Save button to save your variant.\n"
				+ "You can then play it from the New Game option on the home page just like normal chess.");
		variantMakingHelp.add(variantMakingHelpText);

		JTextArea pieceMakingHelpText = new JTextArea();
		pieceMakingHelpText.setEditable(false);
		pieceMakingHelpText.setText("When you make a new Chess variant you can make your own custom pieces! Follow these steps:\n"
				+ "\t1. Create a name and choose the icons for your new piece\n"
				+ "\t2. Enter the specific movement directions/distances you would like it to have\n"
				+ "\t\t - To add instructions for a direction choose it from the drop down, then fill in\n"
				+ "\t\t the farthest distance that piece should move in that direction into the box\n"
				+ "\t\t - When you are finished with that particular direction press\n"
				+ "\t\t the \"Add Movement\" button to add it to the piece\n"
				+ "\t\t - Repeat this process for each direction the piece needs\n"
				+ "\t 3. If you would like your piece to be able to jump over other pieces, check the box\n"
				+ "\t 4. If you want this piece to move like a Knight does enter the directions in this format:\n\n"
				+ "\t\t\t\t\t\t 3 x 2\n\n" + "\t\t - Enter movements either by column and then row or vice versa.\n"
				+ "\t\t As an example a normal knight moves 2 x 1 squares\n"
				+ "\t\t - Entering one combination (e.g. 3 x 2) will allow the piece\n" + "\t\t to move both 3 x 2 and 2 x 3 spaces\n"
				+ "\t 5. When you have finished making your piece press \"Save Piece\"\n"
				+ "\t\t - This piece will then be available for you to use in your variant\n"
				+ "\t 6. Repeat this process for all pieces that you need and then press\n" + "\t the \"Done\" button at the bottom");
		pieceMakingHelp.add(pieceMakingHelpText);

		return helpPop;
	}

	private JFrame setUpAbout()
	{

		JFrame aboutPop = new JFrame("About Chess Master 9001");
		aboutPop.setTitle("Help");
		aboutPop.setSize(350, 375);
		aboutPop.setResizable(false);
		aboutPop.setLocationRelativeTo(this);
		aboutPop.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		aboutPop.setLayout(new GridBagLayout());
		GridBagConstraints a = new GridBagConstraints();

		BufferedImage temp = null;
		BufferedImage iconPiece = null;
		try
		{
			// read the image from the class resources
			temp = ImageIO.read(getClass().getResource("/front_page_image.jpeg"));
			iconPiece = ImageIO.read(getClass().getResource("/king_dark.png"));
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		// Makes the image an icon and ands it to a JLabel
		ImageIcon picture = new ImageIcon(temp);
		JLabel pictureHolder = new JLabel();
		picture.setImage(picture.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH));
		pictureHolder.setIcon(picture);

		ImageIcon piecePicture = new ImageIcon(iconPiece);
		piecePicture.setImage(piecePicture.getImage());

		Font font = new Font("Verdana", Font.BOLD, 18);
		JLabel title = new JLabel("Chess Master 9001\n");
		title.setFont(font);

		JPanel top = new JPanel();
		top.add(title);
		top.add(pictureHolder);

		JTextArea info = new JTextArea();
		info.setEditable(false);
		info.setText("Version 1.1\n\n" + "Visit our project site");

		JButton site = new JButton();
		site.setIcon(piecePicture);
		site.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (Desktop.isDesktopSupported())
				{
					try
					{
						URI uri = new URI("https://github.com/drewhannay/chess");
						Desktop.getDesktop().browse(uri);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					catch (URISyntaxException e)
					{
						e.printStackTrace();
					}
				}
			}
		});

		a.gridy = 0;
		aboutPop.add(title, a);
		a.gridy = 1;
		aboutPop.add(pictureHolder, a);
		a.gridy = 2;
		aboutPop.add(info, a);
		a.gridy = 3;
		aboutPop.add(site, a);

		return aboutPop;
	}

	public void activateWindowListener()
	{
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		removeWindowListener(windowListener);
		windowListener = new WindowListener()
		{

			@Override
			public void windowOpened(WindowEvent e)
			{
			}

			@Override
			public void windowIconified(WindowEvent e)
			{
			}

			@Override
			public void windowDeiconified(WindowEvent e)
			{
			}

			@Override
			public void windowDeactivated(WindowEvent e)
			{
			}

			@Override
			public void windowClosing(WindowEvent e)
			{
				Object[] options = new String[] { "Save Game", "Don't Save", "Cancel" };
				int answer = JOptionPane.showOptionDialog(null, "Would you like to save your game before you quit?", "Quit?",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				switch (answer)
				{
				case 0:
					((PlayGame) otherPanel).saveGame();
					break;
				case 2:
					break;
				default:
					System.exit(0);
					break;
				}
			}

			@Override
			public void windowClosed(WindowEvent e)
			{
			}

			@Override
			public void windowActivated(WindowEvent e)
			{
			}
		};
		addWindowListener(windowListener);
	}

	public void deactivateWindowListener()
	{
		removeWindowListener(windowListener);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
