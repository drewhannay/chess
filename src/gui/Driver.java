package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
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

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
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
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import logic.Game;
import timer.ChessTimer;
import utility.AppConstants;
import utility.FileUtility;
import utility.GUIUtility;

final public class Driver extends JFrame
{
	public static void main(String[] args)
	{
		m_driver.initGUIComponents();
	}

	private Driver()
	{
	}

	private void initGUIComponents()
	{
		setTitle(AppConstants.APP_NAME);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(325, 340);
		setResizable(false);

		// make the window show up in the center of the screen, regardless of
		// resolution
		setLocationRelativeTo(null);

		setResizable(false);
		setLayout(new FlowLayout());
		setResizable(true);

		// make the app match the look and feel of the user's system
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		// set up a new panel to hold everything in the main window
		m_mainPanel = new JPanel();
		m_mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		
		JLabel iconHolder = new JLabel(GUIUtility.createImageIcon(300, 200, "/chess_picture.png", m_mainPanel));
		BufferedImage frontPageImage;
		try
		{
			frontPageImage = ImageIO.read(getClass().getResource("/chess_picture.png"));
			if (System.getProperty("os.name").startsWith("Windows"))
			{
				final SystemTray sysTray = SystemTray.getSystemTray();
				TrayIcon tray = new TrayIcon(frontPageImage.getScaledInstance(25, 18, Image.SCALE_SMOOTH));
				sysTray.add(tray);
			}
			setIconImage(frontPageImage);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		m_newGameButton = new JButton("New Game");
		m_newGameButton.setToolTipText("Press to start a new game of Chess");
		m_newGameButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				setPanel(new NewGameMenu());
			}
		});

		m_continueGameButton = new JButton("Load Game");
		m_continueGameButton.setToolTipText("Press to load a saved game");
		m_continueGameButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				String[] files = FileUtility.getGamesInProgressFileArray();

				if (files.length == 0)
				{
					JOptionPane.showMessageDialog(Driver.getInstance(), "There are no saved games. Try starting a new game instead.",
							"No Completed Games", JOptionPane.ERROR_MESSAGE);
					return;
				}

				final JFrame poppedFrame = new JFrame("Load Saved Game");
				poppedFrame.setLayout(new GridBagLayout());
				poppedFrame.setSize(225, 200);
				poppedFrame.setResizable(false);
				poppedFrame.setLocationRelativeTo(Driver.this);
				GridBagConstraints constraints = new GridBagConstraints();

				final JList gamesInProgressList = new JList(FileUtility.getGamesInProgressFileArray());
				final JScrollPane scrollPane = new JScrollPane(gamesInProgressList);
				scrollPane.setPreferredSize(new Dimension(200, 200));
				gamesInProgressList.setSelectedIndex(0);

				JButton nextButton = new JButton("Next");
				nextButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent event)
					{
						FileInputStream fileInputStream;
						ObjectInputStream objectInputStream;
						Game gameToPlay;
						try
						{
							if (gamesInProgressList.getSelectedValue() == null)
							{
								JOptionPane.showMessageDialog(Driver.getInstance(), "Please select a game", "Error", JOptionPane.PLAIN_MESSAGE);
								return;
							}
							fileInputStream = new FileInputStream(FileUtility.getGamesInProgressFile(gamesInProgressList
									.getSelectedValue().toString()));
							objectInputStream = new ObjectInputStream(fileInputStream);
							gameToPlay = (Game) objectInputStream.readObject();

							// set the help menu info to be specific for game
							// play
							if (m_gameOptionsMenu != null)
								m_gameOptionsMenu.setVisible(true);

							setPanel(new PlayGame(gameToPlay, false));
							poppedFrame.dispose();
						}
						catch (Exception e)
						{
							e.printStackTrace();
							JOptionPane.showMessageDialog(Driver.getInstance(), "There are no valid saved games. Start a New Game instead.",
									"Invalid Saved Games", JOptionPane.PLAIN_MESSAGE);
						}
					}
				});

				JButton cancelButton = new JButton("Cancel");
				GUIUtility.setupCancelButton(cancelButton, poppedFrame);

				JButton deleteButton = new JButton("Delete Saved Game");
				deleteButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent event)
					{
						if (gamesInProgressList.getSelectedValue() != null)
						{
							boolean didDeleteSuccessfully = FileUtility.getGamesInProgressFile(
									gamesInProgressList.getSelectedValue().toString()).delete();
							if (!didDeleteSuccessfully)
							{
								JOptionPane.showMessageDialog(Driver.getInstance(), "Saved game was not deleted successfully",
										"Error", JOptionPane.ERROR_MESSAGE);
							}
							else
							{
								gamesInProgressList.setListData(FileUtility.getGamesInProgressFileArray());
								gamesInProgressList.setSelectedIndex(0);
								if (gamesInProgressList.getSelectedValue() == null)
								{
									JOptionPane.showMessageDialog(Driver.getInstance(),
											"There are no more completed games. Returning to Main Menu", "No Completed Games",
											JOptionPane.PLAIN_MESSAGE);
									poppedFrame.dispose();
								}
								scrollPane.getViewport().add(gamesInProgressList, null);
							}
						}
						else
						{
							JOptionPane.showMessageDialog(Driver.getInstance(), "There are currently no save files!", "No save file selected!",
									JOptionPane.PLAIN_MESSAGE);
						}
					}
				});

				constraints.gridx = 0;
				constraints.gridy = 0;
				constraints.gridwidth = 2;
				constraints.insets = new Insets(5, 5, 5, 5);
				poppedFrame.add(scrollPane, constraints);

				constraints.gridx = 0;
				constraints.gridy = 1;
				poppedFrame.add(deleteButton, constraints);

				constraints.weighty = 1.0;
				constraints.weightx = 1.0;
				constraints.gridx = 0;
				constraints.gridy = 2;
				constraints.gridwidth = 1;
				constraints.anchor = GridBagConstraints.EAST;
				poppedFrame.add(nextButton, constraints);

				constraints.gridx = 1;
				constraints.gridy = 2;
				constraints.anchor = GridBagConstraints.WEST;
				poppedFrame.add(cancelButton, constraints);

				poppedFrame.setVisible(true);
				poppedFrame.pack();
			}
		});

		m_viewCompletedGameButton = new JButton("View Completed Games");
		m_viewCompletedGameButton.setToolTipText("Press to review a finished game");
		m_viewCompletedGameButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					String[] files = FileUtility.getCompletedGamesFileArray();
					if (files.length == 0)
					{
						JOptionPane.showMessageDialog(Driver.getInstance(), "There are no completed games to display.", "No Completed Games",
								JOptionPane.PLAIN_MESSAGE);
						return;
					}

					final JFrame poppedFrame = new JFrame("View Completed Game");
					poppedFrame.setLayout(new GridBagLayout());
					poppedFrame.setSize(225, 200);
					poppedFrame.setResizable(false);
					poppedFrame.setLocationRelativeTo(Driver.this);
					GridBagConstraints constraints = new GridBagConstraints();

					final JList completedGamesList = new JList(FileUtility.getCompletedGamesFileArray());
					final JScrollPane scrollPane = new JScrollPane(completedGamesList);
					scrollPane.setPreferredSize(new Dimension(200, 200));
					completedGamesList.setSelectedIndex(0);

					JButton nextButton = new JButton("Next");
					nextButton.addActionListener(new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent event)
						{
							if (completedGamesList.getSelectedValue() == null)
							{
								JOptionPane.showMessageDialog(Driver.getInstance(), "Please select a game", "Error",
										JOptionPane.PLAIN_MESSAGE);
								return;
							}

							File file = FileUtility.getCompletedGamesFile(completedGamesList.getSelectedValue().toString());
							FileInputStream fileInputStream;
							ObjectInputStream objectInputStream;
							Game gameToView;
							if (completedGamesList.getSelectedValue().toString().endsWith(".acn"))
							{
								try
								{
									m_otherPanel = new PlayGame(true, file);
								}
								catch (Exception e)
								{
									JOptionPane.showMessageDialog(Driver.getInstance(),
											"This file contains invalid ACN notation. Please check the format and try again",
											"Invalid Notation", JOptionPane.PLAIN_MESSAGE);
									return;
								}
							}
							else
							{
								try
								{
									fileInputStream = new FileInputStream(file);
									objectInputStream = new ObjectInputStream(fileInputStream);
									gameToView = (Game) objectInputStream.readObject();

									setPanel(new PlayGame(gameToView, false));
									poppedFrame.dispose();
								}
								catch (Exception e)
								{
									e.printStackTrace();
									JOptionPane.showMessageDialog(Driver.getInstance(),
											"This game is corrupted, please choose another or start a New Game instead.",
											"Invalid Completed Game", JOptionPane.PLAIN_MESSAGE);
								}
							}
							deactivateWindowListener();
							poppedFrame.dispose();
						}
					});

					JButton cancelButton = new JButton("Cancel");
					GUIUtility.setupCancelButton(cancelButton, poppedFrame);

					JButton deleteButton = new JButton("Delete Completed Game");
					deleteButton.addActionListener(new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent event)
						{
							if (completedGamesList.getSelectedValue() != null)
							{
								boolean didDeleteCompletedGameSuccessfully = FileUtility.getCompletedGamesFile(
										completedGamesList.getSelectedValue().toString()).delete();
								if (!didDeleteCompletedGameSuccessfully)
								{
									JOptionPane.showMessageDialog(Driver.getInstance(), "Completed game was not deleted successfully",
											"Error", JOptionPane.PLAIN_MESSAGE);
								}
								else
								{
									completedGamesList.removeAll();
									completedGamesList.setListData(FileUtility.getCompletedGamesFileArray());
									completedGamesList.setSelectedIndex(0);
									if (completedGamesList.getSelectedValue() == null)
									{
										JOptionPane.showMessageDialog(Driver.getInstance(),
												"There are no more completed games. Returning to Main Menu", "No Completed Games",
												JOptionPane.PLAIN_MESSAGE);
										poppedFrame.dispose();
									}
									scrollPane.getViewport().add(completedGamesList, null);
								}
							}
							else
							{
								JOptionPane.showMessageDialog(Driver.getInstance(), "There are currently no completed games!",
										"No completed game selected!", JOptionPane.PLAIN_MESSAGE);
							}
						}
					});

					constraints.gridx = 0;
					constraints.gridy = 0;
					constraints.gridwidth = 2;
					constraints.insets = new Insets(5, 5, 5, 5);
					poppedFrame.add(scrollPane, constraints);

					constraints.gridx = 0;
					constraints.gridy = 1;
					poppedFrame.add(deleteButton, constraints);

					constraints.weighty = 1.0;
					constraints.weightx = 1.0;
					constraints.gridx = 0;
					constraints.gridy = 2;
					constraints.gridwidth = 1;
					constraints.anchor = GridBagConstraints.EAST;
					poppedFrame.add(nextButton, constraints);

					constraints.gridx = 1;
					constraints.gridy = 2;
					constraints.anchor = GridBagConstraints.WEST;
					poppedFrame.add(cancelButton, constraints);

					poppedFrame.setVisible(true);
					poppedFrame.pack();
				}
				catch (Exception e)
				{
					e.printStackTrace();
					JOptionPane.showMessageDialog(Driver.getInstance(), "Either there are no completed games or the game file is missing.",
							"No Completed Games", JOptionPane.PLAIN_MESSAGE);
				}
			}
		});

		m_createVariantButton = new JButton("Create New Game Type");
		m_createVariantButton.setToolTipText("Press to craft a new kind of chess game");
		m_createVariantButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setPanel(new CustomSetupMenu());
			}
		});

		m_menuBar = new JMenuBar();
		m_fileMenu = new JMenu("File");
		m_fileMenu.setMnemonic('F');

		m_helpMenu = new JMenu("Help");
		m_helpMenu.setMnemonic('H');

		JMenuItem helpMenuItem = new JMenuItem("Browse Help", KeyEvent.VK_H);
		helpMenuItem.setToolTipText("Click on me to get help");
		helpMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				new HelpMenu();
			}
		});
		m_helpMenu.add(helpMenuItem);

		JMenuItem aboutItem = new JMenuItem("About " + AppConstants.APP_NAME, KeyEvent.VK_A);
		aboutItem.setToolTipText("Information about " + AppConstants.APP_NAME);
		aboutItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				new AboutMenu(m_mainPanel);
			}
		});
		m_helpMenu.add(aboutItem);

		JMenuItem newGameItem = new JMenuItem("New Game", KeyEvent.VK_N);
		newGameItem.setToolTipText("Press to start a new Game");
		newGameItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setPanel(new NewGameMenu());
			}
		});

		m_fileMenu.add(newGameItem);

		m_mainMenu = new JMenuItem("Main Menu", KeyEvent.VK_M);
		m_mainMenu.setToolTipText("Press to return to the Main Menu");
		m_mainMenu.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (m_gameOptionsMenu != null)
					m_gameOptionsMenu.setVisible(false);
				if (m_otherPanel != null)
					remove(m_otherPanel);
				ChessTimer.stopTimers();
				revertToMainPanel();
			}
		});
		m_mainMenu.setVisible(false);
		m_fileMenu.add(m_mainMenu);

		JMenuItem preferences = new JMenuItem("Preferences", KeyEvent.VK_P);
		preferences.setToolTipText("Press me to change your preferences");
		preferences.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				final JFrame popupFrame = new JFrame("Square Options");
				popupFrame.setSize(370, 120);
				popupFrame.setLocationRelativeTo(Driver.this);
				popupFrame.setLayout(new GridBagLayout());
				popupFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				GridBagConstraints constraints = new GridBagConstraints();

				final JPanel holder = new JPanel();
				holder.setBorder(BorderFactory.createTitledBorder("Default Completed Game Save Location"));
				final JLabel currentSaveLocationLabel = new JLabel("Current Save Location");
				final JTextField currentSaveLocationField = new JTextField(FileUtility.getDefaultCompletedLocation());
				currentSaveLocationField.setEditable(false);
				final JButton changeLocationButton = new JButton("Choose New Save Location");
				final JButton resetButton = new JButton("Reset to Default Location");
				final JButton cancelButton = new JButton("Cancel");
				GUIUtility.setupCancelButton(cancelButton, popupFrame);

				holder.add(currentSaveLocationLabel);
				holder.add(currentSaveLocationField);

				try
				{
					final File preferencesFile = FileUtility.getPreferencesFile();
					if (!preferencesFile.exists())
					{
						try
						{
							preferencesFile.createNewFile();
							BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(preferencesFile, true));
							bufferedWriter.write("DefaultPreferencesSet = true");
							bufferedWriter.newLine();
							bufferedWriter.write("DefaultSaveLocation = " + FileUtility.getDefaultCompletedLocation());
							bufferedWriter.close();
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}
					FileInputStream fileInputStream = null;
					fileInputStream = new FileInputStream(preferencesFile);
					DataInputStream dataInputStream = new DataInputStream(fileInputStream);
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));
					String line;
					line = bufferedReader.readLine();
					line = bufferedReader.readLine();
					fileInputStream.close();
					dataInputStream.close();
					bufferedReader.close();
					currentSaveLocationField.setText(line.substring(22));

					resetButton.addActionListener(new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent event)
						{
							try
							{
								BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(preferencesFile, true));
								bufferedWriter.write("DefaultPreferencesSet = true");
								bufferedWriter.newLine();
								bufferedWriter.write("DefaultSaveLocation = " + FileUtility.getDefaultCompletedLocation());
								bufferedWriter.close();
							}
							catch (IOException e)
							{
								e.printStackTrace();
							}
						}
					});

					changeLocationButton.addActionListener(new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent event)
						{
							if (!preferencesFile.exists())
							{
								try
								{
									preferencesFile.createNewFile();
									BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(preferencesFile, true));
									bufferedWriter.write("DefaultPreferencesSet = false");
									bufferedWriter.newLine();
									bufferedWriter.write("DefaultSaveLocation = " + FileUtility.getDefaultCompletedLocation());
									bufferedWriter.close();
								}
								catch (IOException ae)
								{
									ae.printStackTrace();
								}
							}
							try
							{
								PrintWriter printWriter = new PrintWriter(preferencesFile);
								JFileChooser fileChooser = new JFileChooser();
								fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
								int returnVal = fileChooser.showOpenDialog(Driver.getInstance());
								BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(preferencesFile, true));
								bufferedWriter.write("DefaultPreferencesSet = true");
								bufferedWriter.newLine();
								if (returnVal == JFileChooser.APPROVE_OPTION)
								{
									printWriter.print("");
									printWriter.close();
									bufferedWriter.write("DefaultSaveLocation = " + fileChooser.getSelectedFile().getAbsolutePath());
									bufferedWriter.close();
									currentSaveLocationField.setText(fileChooser.getSelectedFile().getAbsolutePath());
								}
								else
								{
									bufferedWriter.close();
									return;
								}
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					});
				}
				catch (Exception e)
				{
					JOptionPane.showMessageDialog(Driver.getInstance(), "That is not a valid location to save your completed games.",
							"Invalid Location", JOptionPane.PLAIN_MESSAGE);
					e.printStackTrace();
				}

				constraints.gridx = 0;
				constraints.gridy = 0;
				constraints.gridwidth = 2;
				constraints.insets = new Insets(5, 5, 5, 5);
				popupFrame.add(holder, constraints);
				constraints.gridx = 0;
				constraints.gridy = 1;
				constraints.weightx = 1;
				constraints.gridwidth = 1;
				constraints.anchor = GridBagConstraints.EAST;
				popupFrame.add(changeLocationButton, constraints);
				constraints.gridx = 1;
				constraints.gridy = 1;
				constraints.anchor = GridBagConstraints.WEST;
				popupFrame.add(resetButton, constraints);
				constraints.gridx = 0;
				constraints.gridy = 2;
				constraints.gridwidth = 2;
				constraints.anchor = GridBagConstraints.CENTER;
				popupFrame.add(cancelButton, constraints);

				popupFrame.pack();
				popupFrame.setVisible(true);
			}
		});
		m_fileMenu.add(preferences);

		JMenuItem exitMenuItem = new JMenuItem("Quit", KeyEvent.VK_Q);
		exitMenuItem.setToolTipText("Press to close the program");
		exitMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				int answer = JOptionPane.showConfirmDialog(Driver.getInstance(), "Are you sure you want to Quit?", "Quit?", JOptionPane.YES_NO_OPTION,
						JOptionPane.PLAIN_MESSAGE);
				if (answer == 0)
					System.exit(0);
			}
		});
		m_fileMenu.add(exitMenuItem);

		m_menuBar.add(m_fileMenu);
		m_menuBar.add(m_helpMenu);

		setJMenuBar(m_menuBar);

		m_mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.0;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 0;
		m_mainPanel.add(iconHolder, c);

		// new game
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		m_mainPanel.add(m_newGameButton, c);

		// create new variant
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		m_mainPanel.add(m_createVariantButton, c);

		// continue
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		m_mainPanel.add(m_continueGameButton, c);

		// view completed game
		c.gridx = 2;
		c.gridy = 2;
		c.gridwidth = 1;
		m_mainPanel.add(m_viewCompletedGameButton, c);

		add(m_mainPanel);
		setVisible(true);
	}

	public void setUpNewGame()
	{
		if (PlayGame.m_optionsMenu != null)
			PlayGame.m_optionsMenu.setVisible(false);
		ChessTimer.stopTimers();
		setPanel(new NewGameMenu());
	}

	public void setPanel(JPanel panel)
	{
		remove(m_mainPanel);
		if (m_otherPanel != null)
			remove(m_otherPanel);
		m_mainMenu.setVisible(true);
		add(panel);
		if (panel.toString().contains("PlayGame") || panel.toString().contains("PlayNetGame"))
			activateWindowListener();
		m_otherPanel = panel;
		pack();
	}

	public void revertToMainPanel()
	{
		remove(m_otherPanel);
		m_mainMenu.setVisible(false);
		add(m_mainPanel);
		deactivateWindowListener();
		pack();
	}

	public static Driver getInstance()
	{
		return m_driver;
	}

	public void setMenu(JMenu menu)
	{
		m_gameOptionsMenu = menu;
		m_menuBar.add(m_gameOptionsMenu);
		m_gameOptionsMenu.setVisible(true);
		m_gameOptionsMenu.setMnemonic('O');
		m_gameOptionsMenu.setText("Options");
		m_gameOptionsMenu.setToolTipText("Use me to access game options");
	}
	
	public void activateWindowListener()
	{
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		removeWindowListener(m_windowListener);
		m_windowListener = new WindowListener()
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
				switch (JOptionPane.showOptionDialog(Driver.getInstance(), "Would you like to save your game before you quit?", "Quit?",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]))
				{
				case JOptionPane.YES_OPTION:
					((PlayGame) m_otherPanel).saveGame();
					break;
				case JOptionPane.NO_OPTION:
					System.exit(0);
					break;
				case JOptionPane.CANCEL_OPTION:
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
		addWindowListener(m_windowListener);
	}

	public void deactivateWindowListener()
	{
		removeWindowListener(m_windowListener);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private static final long serialVersionUID = -3533604157531154757L;
	private static final Driver m_driver = new Driver();

	public static JMenu m_gameOptionsMenu;

	public JMenu m_helpMenu;
	public JMenu m_fileMenu;
	public JMenuItem m_mainMenu;
	public WindowListener m_windowListener;

	private JButton m_newGameButton;
	private JButton m_continueGameButton;
	private JButton m_viewCompletedGameButton;
	private JButton m_createVariantButton;
	private JPanel m_mainPanel;
	private JMenuBar m_menuBar;
	private JPanel m_otherPanel;
}
