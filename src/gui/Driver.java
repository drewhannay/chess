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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import logic.Game;
import timer.ChessTimer;
import utility.AppConstants;
import utility.FileUtility;
import utility.GuiUtility;

public final class Driver extends JFrame
{
	public static void main(String[] args)
	{
		try
		{
			getInstance().initGuiComponents();
		}
		catch (Exception e)
		{
			System.out.println("Error setting up initial GUI screen");
			e.printStackTrace();
		}
	}

	private Driver()
	{
	}

	public static Driver getInstance()
	{
		if (sInstance == null)
			sInstance = new Driver();

		return sInstance;
	}

	public void setFileMenuVisibility(boolean isVisible)
	{
		mFileMenu.setVisible(isVisible);
	}

	public void setOptionsMenuVisibility(boolean isVisible)
	{
		mOptionsMenu.setVisible(isVisible);
	}

	public void setUpNewGame()
	{
		if (PlayGamePanel.mOptionsMenu != null)
			PlayGamePanel.mOptionsMenu.setVisible(false);
		ChessTimer.stopTimers();
		setPanel(new NewGamePanel());
	}

	public void setPanel(JPanel panel)
	{
		remove(mMainPanel);
		if (mOtherPanel != null)
			remove(mOtherPanel);
		mMainMenuItem.setVisible(true);
		add(panel);
		// FIXME
		if (panel.toString().contains("PlayGame") || panel.toString().contains("PlayNetGame"))
			activateWindowListener();
		mOtherPanel = panel;
		pack();
	}

	public void revertToMainPanel()
	{
		remove(mOtherPanel);
		mMainMenuItem.setVisible(false);
		add(mMainPanel);
		deactivateWindowListener();
		pack();
	}

	public void setMenu(JMenu menu)
	{
		mOptionsMenu = menu;
		mMenuBar.add(mOptionsMenu);
		mOptionsMenu.setVisible(true);
		mOptionsMenu.setMnemonic('O');
		mOptionsMenu.setText("Options");
		mOptionsMenu.setToolTipText("Use me to access game options");
	}

	private void initGuiComponents() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException
	{
		setTitle(AppConstants.APP_NAME);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(325, 340);
		setLayout(new FlowLayout());
		setResizable(true);

		// put the window in the center of the screen, regardless of resolution
		setLocationRelativeTo(null);

		// make the app match the look and feel of the user's system
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		createWindowsTrayIconIfNecessary();

		// create the menu bar
		createMenuBar();

		// set up a new panel to hold everything in the main window
		mMainPanel = new JPanel();
		mMainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		mMainPanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		// home screen image
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.0;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 0;
		mMainPanel.add(new JLabel(GuiUtility.createImageIcon(300, 200, GuiConstants.HOME_SCREEN_IMAGE_PATH)), c);

		// new game
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		mMainPanel.add(createNewGameButton(), c);

		// create new variant
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		mMainPanel.add(createVariantButton(), c);

		// continue
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		mMainPanel.add(createContinueGameButton(), c);

		// view completed game
		c.gridx = 2;
		c.gridy = 2;
		c.gridwidth = 1;
		mMainPanel.add(createViewCompletedGamesButton(), c);

		add(mMainPanel);
		setVisible(true);
	}

	private void activateWindowListener()
	{
		if (mWindowListener == null)
		{
			mWindowListener = new WindowAdapter()
			{
				@Override
				public void windowClosing(WindowEvent e)
				{
					Object[] options = new String[] { "Save Game", "Don't Save", "Cancel" };
					switch (JOptionPane.showOptionDialog(Driver.getInstance(), "Would you like to save your game before you quit?",
							"Quit?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]))
					{
					case JOptionPane.YES_OPTION:
						((PlayGamePanel) mOtherPanel).saveGame();
						break;
					case JOptionPane.NO_OPTION:
						System.exit(0);
						break;
					case JOptionPane.CANCEL_OPTION:
						break;
					}
				}
			};
		}
		else
		{
			removeWindowListener(mWindowListener);
		}

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(mWindowListener);
	}

	private void deactivateWindowListener()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		removeWindowListener(mWindowListener);
	}

	private void createWindowsTrayIconIfNecessary()
	{
		try
		{
			BufferedImage frontPageImage = ImageIO.read(getClass().getResource(GuiConstants.HOME_SCREEN_IMAGE_PATH));
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
	}

	private void createMenuBar()
	{
		mMenuBar = new JMenuBar();

		mMenuBar.add(createFileMenu());
		mMenuBar.add(createHelpMenu());

		setJMenuBar(mMenuBar);
	}

	private JMenu createFileMenu()
	{
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');

		JMenuItem newGameItem = new JMenuItem("New Game", KeyEvent.VK_N);
		newGameItem.setToolTipText("Start a new Game");
		newGameItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				setPanel(new NewGamePanel());
			}
		});

		fileMenu.add(newGameItem);

		mMainMenuItem = new JMenuItem("Main Menu", KeyEvent.VK_M);
		mMainMenuItem.setToolTipText("Return to the Main Menu");
		mMainMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (mOptionsMenu != null)
					mOptionsMenu.setVisible(false);
				if (mOtherPanel != null)
					remove(mOtherPanel);
				// FIXME Shouldn't need to deal with timers from here
				ChessTimer.stopTimers();
				revertToMainPanel();
			}
		});
		mMainMenuItem.setVisible(false);
		fileMenu.add(mMainMenuItem);

		JMenuItem preferences = new JMenuItem("Preferences", KeyEvent.VK_P);
		preferences.setToolTipText("Change preferences");
		preferences.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				PreferenceUtility.createPreferencePopup(Driver.this);
			}
		});
		fileMenu.add(preferences);

		JMenuItem exitMenuItem = new JMenuItem("Quit", KeyEvent.VK_Q);
		exitMenuItem.setToolTipText("Close the program");
		exitMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				int answer = JOptionPane.showConfirmDialog(Driver.getInstance(), "Are you sure you want to Quit?", "Quit?",
						JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (answer == 0)
					System.exit(0);
			}
		});
		fileMenu.add(exitMenuItem);

		// keep a reference to the file menu so we can toggle its visibility
		mFileMenu = fileMenu;
		return fileMenu;
	}

	private JMenu createHelpMenu()
	{
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');

		JMenuItem helpMenuItem = new JMenuItem("Browse Help", KeyEvent.VK_H);
		helpMenuItem.setToolTipText("Click on me to get help");
		helpMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				new HelpFrame();
			}
		});
		helpMenu.add(helpMenuItem);

		JMenuItem aboutItem = new JMenuItem("About " + AppConstants.APP_NAME, KeyEvent.VK_A);
		aboutItem.setToolTipText("Information about " + AppConstants.APP_NAME);
		aboutItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				new AboutFrame(mMainPanel);
			}
		});
		helpMenu.add(aboutItem);

		return helpMenu;
	}

	private JButton createNewGameButton()
	{
		JButton newGameButton = new JButton("New Game");

		newGameButton.setToolTipText("Start a new game of Chess");
		newGameButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				setPanel(new NewGamePanel());
			}
		});

		return newGameButton;
	}

	private JButton createContinueGameButton()
	{
		JButton continueGameButton = new JButton("Load Game");
		continueGameButton.setToolTipText("Load a saved game");
		continueGameButton.addActionListener(new ActionListener()
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
								JOptionPane.showMessageDialog(Driver.getInstance(), "Please select a game", "Error",
										JOptionPane.PLAIN_MESSAGE);
								return;
							}
							fileInputStream = new FileInputStream(FileUtility.getGamesInProgressFile(gamesInProgressList
									.getSelectedValue().toString()));
							objectInputStream = new ObjectInputStream(fileInputStream);
							gameToPlay = (Game) objectInputStream.readObject();

							// set the help menu info to be specific for game
							// play
							if (mOptionsMenu != null)
								mOptionsMenu.setVisible(true);

							setPanel(new PlayGamePanel(gameToPlay, false));
							poppedFrame.dispose();
						}
						catch (Exception e)
						{
							e.printStackTrace();
							JOptionPane.showMessageDialog(Driver.getInstance(),
									"There are no valid saved games. Start a New Game instead.", "Invalid Saved Games",
									JOptionPane.PLAIN_MESSAGE);
						}
					}
				});

				JButton cancelButton = new JButton("Cancel");
				GuiUtility.setupCancelButton(cancelButton, poppedFrame);

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
							JOptionPane.showMessageDialog(Driver.getInstance(), "There are currently no save files!",
									"No save file selected!", JOptionPane.PLAIN_MESSAGE);
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

		return continueGameButton;
	}

	private JButton createViewCompletedGamesButton()
	{

		JButton viewCompletedGameButton = new JButton("View Completed Games");
		viewCompletedGameButton.setToolTipText("Review a finished game");
		viewCompletedGameButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				try
				{
					String[] files = FileUtility.getCompletedGamesFileArray();
					if (files.length == 0)
					{
						JOptionPane.showMessageDialog(Driver.getInstance(), "There are no completed games to display.",
								"No Completed Games", JOptionPane.PLAIN_MESSAGE);
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
									mOtherPanel = new PlayGamePanel(true, file);
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

									setPanel(new PlayGamePanel(gameToView, false));
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
					GuiUtility.setupCancelButton(cancelButton, poppedFrame);

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
					JOptionPane.showMessageDialog(Driver.getInstance(),
							"Either there are no completed games or the game file is missing.", "No Completed Games",
							JOptionPane.PLAIN_MESSAGE);
				}
			}
		});

		return viewCompletedGameButton;
	}

	private JButton createVariantButton()
	{
		JButton variantButton = new JButton("Create New Game Type");
		variantButton.setToolTipText("Craft a new kind of chess game");
		variantButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setPanel(new CustomSetupPanel());
			}
		});

		return variantButton;
	}

	private static final long serialVersionUID = -3533604157531154757L;

	private static Driver sInstance;

	private JMenuBar mMenuBar;
	private JMenu mFileMenu;
	private JMenu mOptionsMenu;
	private JMenuItem mMainMenuItem;
	private JPanel mMainPanel;
	private JPanel mOtherPanel;
	private WindowListener mWindowListener;
}
