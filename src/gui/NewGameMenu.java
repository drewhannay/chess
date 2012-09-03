package gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import logic.Builder;
import logic.Game;
import net.NetworkClient;
import net.NetworkServer;
import timer.BronsteinDelayTimer;
import timer.ChessTimer;
import timer.FischerTimer;
import timer.HourGlassTimer;
import timer.NoTimer;
import timer.SimpleDelayTimer;
import timer.WordTimer;
import utility.FileUtility;
import utility.GUIUtility;
import ai.AIAdapter;
import ai.AIPlugin;

import com.google.common.collect.Lists;

public class NewGameMenu extends JPanel
{
	public NewGameMenu()
	{
		initGUIComponents();
	}

	private void initGUIComponents()
	{
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		m_humanPlayButton = new JButton("Human Play");
		m_humanPlayButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (!m_isWorking)
				{
					m_isWorking = true;
					setUpNewGamePopup(false);
				}
			}
		});

		m_networkPlayButton = new JButton("Network Play");
		m_networkPlayButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				final JFrame popupFrame = new JFrame("New Game");
				popupFrame.setLayout(new FlowLayout());
				popupFrame.setSize(350, 150);
				popupFrame.setResizable(false);
				popupFrame.setLocationRelativeTo(null);

				JPanel optionsPanel = new JPanel();
				final JLabel hostOrConnectLabel = new JLabel("Would you like to host a game or connect to one?");
				final JButton clientButton = new JButton("Connect");
				clientButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent event)
					{
						final JFrame poppedFrame = new JFrame("New Game");
						poppedFrame.setLayout(new GridBagLayout());
						poppedFrame.setSize(370, 150);
						poppedFrame.setResizable(false);
						poppedFrame.setLocationRelativeTo(null);
						// TODO: this needs a better name
						GridBagConstraints c = new GridBagConstraints();

						final JLabel connectToLabel = new JLabel("Which computer would you like to connect to?");
						final JTextField connectToField = new JTextField("", 2);

						final JButton nextButton = new JButton("Next");
						nextButton.addActionListener(new ActionListener()
						{
							@Override
							public void actionPerformed(ActionEvent event)
							{
								if (connectToField.getText().equals(""))
								{
									JOptionPane.showMessageDialog(null, "Please enter a number", "Number Needed",
											JOptionPane.PLAIN_MESSAGE);
									return;
								}
								else if (connectToField.getText().length() < 2)
								{
									try
									{
										int hostNumber = Integer.parseInt(connectToField.getText());
										if (hostNumber > 25 || hostNumber < 1)
											throw new Exception();
										m_hostName = "cslab0" + hostNumber;
									}
									catch (Exception e)
									{
										JOptionPane.showMessageDialog(null, "Please enter a number between 1-25 in the box",
												"Number Needed", JOptionPane.PLAIN_MESSAGE);
										return;
									}
								}
								else
								{
									try
									{
										int hostNumber = Integer.parseInt(connectToField.getText());
										if (hostNumber > 25 || hostNumber < 1)
											throw new Exception();
										m_hostName = "cslab" + hostNumber;
									}
									catch (Exception e)
									{
										JOptionPane.showMessageDialog(null, "Please enter a number between 1-25 in the box",
												"Number Needed", JOptionPane.PLAIN_MESSAGE);
										return;
									}
								}
								NewGameMenu.m_isCancelled = false;
								try
								{
									Thread clientThread = new Thread(new Runnable()
									{
										@Override
										public void run()
										{
											try
											{
												new NetworkClient().join(m_hostName);
											}
											catch (Exception e)
											{
												e.printStackTrace();
											}
										}
									});
									clientThread.start();
									Driver.getInstance().setPanel(new NetLoading());
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}

								poppedFrame.dispose();
								popupFrame.dispose();
							}
						});

						final JButton cancelButton = new JButton("Cancel");
						GUIUtility.setupCancelButton(cancelButton, popupFrame);

						JPanel topLevelPanel = new JPanel();
						topLevelPanel.setLayout(new GridBagLayout());

						c.gridx = 0;
						c.gridy = 0;
						c.gridwidth = 2;
						c.insets = new Insets(3, 3, 3, 3);
						poppedFrame.add(connectToLabel, c);
						c.gridx = 0;
						c.gridy = 1;
						c.gridwidth = 1;
						topLevelPanel.add(new JLabel("cslab: "), c);
						c.gridx = 1;
						c.gridy = 1;
						c.gridwidth = 1;
						topLevelPanel.add(connectToField, c);
						c.gridx = 0;
						c.gridy = 2;
						c.gridwidth = 1;
						topLevelPanel.add(nextButton, c);
						c.gridx = 1;
						c.gridy = 2;
						c.gridwidth = 1;
						topLevelPanel.add(cancelButton, c);

						c.gridx = 0;
						c.gridy = 1;
						c.gridwidth = 2;
						poppedFrame.add(topLevelPanel, c);
						poppedFrame.setVisible(true);
					}
				});

				final JButton hostButton = new JButton("Host");
				hostButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent event)
					{
						setUpNewGamePopup(true);
						popupFrame.dispose();
					}
				});
				optionsPanel.add(hostOrConnectLabel);
				popupFrame.add(optionsPanel);
				popupFrame.add(hostButton);
				popupFrame.add(clientButton);

				popupFrame.setVisible(true);
			}
		});

		m_aiPlayButton = new JButton("AI Play");
		m_aiPlayButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				final JFrame poppedFrame = new JFrame("New Game");
				poppedFrame.setLayout(new GridBagLayout());
				poppedFrame.setSize(225, 200);
				poppedFrame.setResizable(false);
				poppedFrame.setLocationRelativeTo(null);
				GridBagConstraints constraints = new GridBagConstraints();

				final JComboBox dropdown = new JComboBox(Builder.getVariantFileArray());
				constraints.gridx = 0;
				constraints.gridy = 0;
				poppedFrame.add(new JLabel("Game Type: "), constraints);
				constraints.gridx = 1;
				constraints.gridy = 0;
				constraints.insets = new Insets(3, 0, 3, 0);
				poppedFrame.add(dropdown, constraints);
				constraints.gridx = 0;
				constraints.gridy = 1;
				poppedFrame.add(new JLabel("AI: "), constraints);

				if (getAIFiles().length == 0)
				{
					int answer = JOptionPane.showConfirmDialog(null,
							"There are no AI files installed. Would you like to install one?", "Install AI Files?",
							JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
					if (answer == 0)
					{
						JFileChooser fileChooser = new JFileChooser();
						int returnVal = fileChooser.showOpenDialog(Driver.getInstance());
						File aiFile = fileChooser.getSelectedFile();

						File destinationDirectory = new File(FileUtility.getHiddenDir() + "/AI");

						if (returnVal == JFileChooser.APPROVE_OPTION)
						{
							if (!aiFile.renameTo(new File(destinationDirectory, aiFile.getName())))
							{
								JOptionPane.showMessageDialog(Driver.getInstance(), "File was not installed successfully", "Error",
										JOptionPane.PLAIN_MESSAGE);
							}
						}
						else
						{
							return;
						}
					}
					else
					{
						return;
					}
				}

				final JComboBox aiComboBox = new JComboBox(getAIFiles());
				constraints.gridx = 1;
				constraints.gridy = 1;
				constraints.fill = GridBagConstraints.HORIZONTAL;
				poppedFrame.add(aiComboBox, constraints);

				// TODO: this code is almost the same as the code above. we should be able to extract it to a method
				JButton addAIFileButton = new JButton("Install New AI");
				addAIFileButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent event)
					{
						JFileChooser fileChooser = new JFileChooser();
						int returnVal = fileChooser.showOpenDialog(Driver.getInstance());
						File file = fileChooser.getSelectedFile();

						File destinationDirectory = new File(System.getProperty("user.home") + "/chess/AI");

						if (returnVal == JFileChooser.APPROVE_OPTION)
						{
							if (!file.renameTo(new File(destinationDirectory, file.getName())))
							{
								JOptionPane.showMessageDialog(Driver.getInstance(), "File was not installed successfully", "Error",
										JOptionPane.PLAIN_MESSAGE);
							}
							else
							{
								aiComboBox.removeAllItems();
								for (String fileName : getAIFiles())
									aiComboBox.addItem(fileName);
							}
						}
						else
						{
							return;
						}
					}
				});

				JButton nextButton = new JButton("Next");
				nextButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent event)
					{
						final String aiFileName = (String) aiComboBox.getSelectedItem();
						File aiFile = FileUtility.getAIFile(aiFileName);
						if (aiComboBox.getSelectedItem() == null)
						{
							JOptionPane.showMessageDialog(null, "You have not selected an AI file", "No AI file",
									JOptionPane.PLAIN_MESSAGE);
							return;
						}
						Game gameToPlay = Builder.newGame((String) dropdown.getSelectedItem());

						JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
						StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, Locale.getDefault(), null);

						String[] compileOptions = new String[] { "-d", "bin" };
						Iterable<String> compilationOptions = Arrays.asList(compileOptions);

						List<File> sourceFileList = Lists.newArrayList();
						sourceFileList.add(aiFile);
						Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(sourceFileList);
						CompilationTask task = compiler.getTask(null, fileManager, null, compilationOptions, null, compilationUnits);

						if (!task.call())
						{
							JOptionPane.showMessageDialog(null, "Compilation failed\n"
									+ "Make sure your class implements the AIPlugin interface\n"
									+ "Make sure your class includes the following imports:\n" + "import ai.*;\n"
									+ "import ai.AIAdapter.*;\n", "Compilation Failure", JOptionPane.PLAIN_MESSAGE);
							return;
						}
						try
						{
							fileManager.close();
						}
						catch (IOException e)
						{
						}

						final AIPlugin aiPlugin;
						final AIAdapter aiAdapter = new AIAdapter(gameToPlay);
						try
						{
							ClassLoader classLoader = ClassLoader.getSystemClassLoader();
							Class<?> klazz = classLoader.loadClass(aiFileName.substring(0, aiFileName.indexOf(".java")));
							Constructor<?> constructor = klazz.getConstructor();
							aiPlugin = (AIPlugin) constructor.newInstance();

							Thread aiThread;
							aiThread = new Thread(new Runnable()
							{
								@Override
								public void run()
								{
									try
									{
										aiAdapter.runGame(aiPlugin);
									}
									catch (Exception e)
									{
										e.printStackTrace();
									}
								}
							});
							aiThread.start();
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}

						try
						{
							PlayNetGame playNetGame = new PlayNetGame(gameToPlay, false, false);
							playNetGame.setIsAIGame(true);
							Driver.getInstance().setPanel(playNetGame);
						}
						catch (Exception e)
						{
							return;
						}
						poppedFrame.dispose();
					}
				});

				JButton cancelButton = new JButton("Cancel");
				GUIUtility.setupCancelButton(cancelButton, poppedFrame);

				JPanel buttonPanel = new JPanel();
				buttonPanel.setLayout(new FlowLayout());
				buttonPanel.add(nextButton);
				buttonPanel.add(cancelButton);

				constraints.gridx = 0;
				constraints.gridy = 2;
				constraints.gridwidth = 2;
				constraints.insets = new Insets(5, 0, 5, 0);
				poppedFrame.add(addAIFileButton, constraints);

				constraints.gridx = 0;
				constraints.gridy = 3;
				poppedFrame.add(buttonPanel, constraints);

				poppedFrame.setVisible(true);
			}
		});

		GUIUtility.setupReturnToMainButton(m_returnToMainButton);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		buttonPanel.setLayout(new GridBagLayout());
		constraints.gridy = 1;
		constraints.ipadx = 7;
		constraints.insets = new Insets(5, 5, 0, 5);
		buttonPanel.add(m_humanPlayButton, constraints);
		constraints.gridy = 2;
		constraints.ipadx = 0;
		constraints.insets = new Insets(2, 5, 0, 5);
		buttonPanel.add(m_networkPlayButton, constraints);
		constraints.gridy = 3;
		constraints.ipadx = 28;
		constraints.insets = new Insets(2, 5, 5, 5);
		buttonPanel.add(m_aiPlayButton, constraints);

		try
		{
			if (InetAddress.getLocalHost().getHostName().contains("cslab"))
			{
				constraints.gridy = 0;
				constraints.ipadx = 0;
				constraints.insets = new Insets(5, 50, 5, 50);
				constraints.anchor = GridBagConstraints.CENTER;
				add(new JLabel("How would you like to play?"), constraints);
				constraints.gridy = 1;
				add(buttonPanel, constraints);
				constraints.gridy = 2;
				add(m_returnToMainButton, constraints);
			}
			else
			{
				constraints.gridy = 0;
				constraints.ipadx = 0;
				constraints.insets = new Insets(5, 50, 5, 50);
				constraints.anchor = GridBagConstraints.CENTER;
				add(new JLabel("How would you like to play?"), constraints);
				constraints.gridy = 1;
				constraints.ipadx = 0;
				add(buttonPanel, constraints);
				constraints.gridy = 2;
				add(m_returnToMainButton, constraints);

				m_networkPlayButton.setVisible(false);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private String[] getAIFiles()
	{
		String[] allFiles = FileUtility.getAIFileList();
		allFiles = FileUtility.getAIFileList();

		List<String> tempFiles = Lists.newArrayList();

		for (String fileName : allFiles)
		{
			if (fileName.endsWith(".java"))
				tempFiles.add(fileName);
		}

		return tempFiles.toArray(new String[tempFiles.size()]);
	}

	private void setUpNewGamePopup(final boolean isNetworkPlay)
	{
		m_isWorking = true;
		final JFrame popupFrame = new JFrame("New Game");
		popupFrame.setLayout(new GridBagLayout());
		popupFrame.setSize(325, 225);
		popupFrame.setResizable(false);
		// make the window show up in the center of the screen regardless of
		// resolution
		popupFrame.setLocationRelativeTo(null);
		GridBagConstraints constraints = new GridBagConstraints();

		String[] variantTypes = Builder.getVariantFileArray();
		if (isNetworkPlay)
		{
			List<String> filteredList = Lists.newArrayList();
			for (String variant : variantTypes)
			{
				Game game = Builder.newGame(variant);
				if (game.getWhiteRules().networkable() && game.getBlackRules().networkable())
					filteredList.add(variant);
			}
			variantTypes = new String[filteredList.size()];
			int i = 0;
			for (String variantName : filteredList)
				variantTypes[i++] = variantName;
		}
		final JComboBox dropdown = new JComboBox(variantTypes);

		// TODO restrict game types here.

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		popupFrame.add(new JLabel("Type: "), constraints);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		popupFrame.add(dropdown, constraints);

		final JButton doneButton = new JButton("Start");
		final JComboBox timerComboBox = new JComboBox(TIMER_NAMES);

		final JLabel totalTimeLabel = new JLabel("Total Time (sec): ");
		totalTimeLabel.setVisible(false);
		final JTextField totalTimeField = new JTextField("120", 3);
		totalTimeField.setVisible(false);

		final JLabel increaseLabel = new JLabel("Increment/delay (sec): ");
		increaseLabel.setVisible(false);
		final JTextField increaseField = new JTextField("10", 3);
		increaseField.setVisible(false);

		timerComboBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				String timerName = (String) timerComboBox.getSelectedItem();
				if (!timerName.equals("No timer"))
				{
					totalTimeLabel.setVisible(true);
					totalTimeField.setVisible(true);
					increaseLabel.setVisible(true);
					increaseField.setVisible(true);
				}
				else
				{
					totalTimeLabel.setVisible(false);
					totalTimeField.setVisible(false);
					increaseLabel.setVisible(false);
					increaseField.setVisible(false);
				}
			}
		});

		constraints.gridx = 0;
		constraints.gridy = 1;
		popupFrame.add(new JLabel("Timer: "), constraints);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		popupFrame.add(timerComboBox, constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		popupFrame.add(totalTimeLabel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 2;
		popupFrame.add(totalTimeField, constraints);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.CENTER;
		popupFrame.add(increaseLabel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 3;
		popupFrame.add(increaseField, constraints);
		popupFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		popupFrame.addWindowListener(new WindowListener()
		{
			@Override
			public void windowActivated(WindowEvent event)
			{
			}

			@Override
			public void windowClosed(WindowEvent event)
			{
			}

			@Override
			public void windowClosing(WindowEvent event)
			{
				m_isWorking = false;
				popupFrame.setVisible(false);
				popupFrame.dispose();
			}

			@Override
			public void windowDeactivated(WindowEvent event)
			{
			}

			@Override
			public void windowDeiconified(WindowEvent event)
			{
			}

			@Override
			public void windowIconified(WindowEvent event)
			{
			}

			@Override
			public void windowOpened(WindowEvent event)
			{
			}
		});

		doneButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				m_isWorking = false;
				String timerName = (String) timerComboBox.getSelectedItem();
				long startTime = Integer.parseInt(totalTimeField.getText()) * 1000;
				long increment = Integer.parseInt(increaseField.getText()) * 1000;

				ChessTimer blackTimer = null;
				ChessTimer whiteTimer = null;

				if (timerName.equals("No timer"))
				{
					blackTimer = new NoTimer();
					whiteTimer = new NoTimer();
				}
				else if (timerName.equals("Bronstein Delay"))
				{
					blackTimer = new BronsteinDelayTimer(increment, startTime, true);
					whiteTimer = new BronsteinDelayTimer(increment, startTime, false);
				}
				else if (timerName.equals("Fischer"))
				{
					blackTimer = new FischerTimer(increment, startTime, false, true);
					whiteTimer = new FischerTimer(increment, startTime, false, false);
				}
				else if (timerName.equals("Fischer After"))
				{
					blackTimer = new FischerTimer(increment, startTime, true, true);
					whiteTimer = new FischerTimer(increment, startTime, true, false);
				}
				else if (timerName.equals("Hour Glass"))
				{
					// time is halved since it is actually the time the player
					// is not allowed to exceed
					blackTimer = new HourGlassTimer(startTime / 2, true);
					whiteTimer = new HourGlassTimer(startTime / 2, false);
				}
				else if (timerName.equals("Simple Delay"))
				{
					blackTimer = new SimpleDelayTimer(increment, startTime, true);
					whiteTimer = new SimpleDelayTimer(increment, startTime, false);
				}
				else
				{
					blackTimer = new WordTimer(startTime);
					whiteTimer = new WordTimer(startTime);
				}

				if (isNetworkPlay)
				{
					Game gameToPlay = Builder.newGame((String) dropdown.getSelectedItem());
					gameToPlay.setTimers(whiteTimer, blackTimer);
					final PlayNetGame game;
					if (m_hostName.equals(event))
					{
						try
						{
							game = new PlayNetGame(gameToPlay, false, true);
						}
						catch (Exception e)
						{
							return;
						}
					}
					else
					{
						try
						{
							game = new PlayNetGame(gameToPlay, false, false);
						}
						catch (Exception e)
						{
							return;
						}
					}
					try
					{
						NewGameMenu.m_isCancelled = false;
						Thread host = new Thread(new Runnable()
						{
							@Override
							public void run()
							{
								try
								{
									new NetworkServer().host(game);
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
							}
						});
						Driver.getInstance().setPanel(new NetLoading());
						host.start();
					}
					catch (Exception e)
					{
						System.out.println("Host");
						e.printStackTrace();
					}
				}
				else
				{
					Game gameToPlay = Builder.newGame((String) dropdown.getSelectedItem());
					gameToPlay.setTimers(whiteTimer, blackTimer);
					PlayGame game = null;
					try
					{
						game = new PlayGame(gameToPlay, false);
					}
					catch (Exception e)
					{
						e.printStackTrace();
						return;
					}
					Driver.getInstance().setPanel(game);
				}
				popupFrame.dispose();
			}
		});

		final JButton Button = new JButton("Back");
		Button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				m_isWorking = false;
				popupFrame.dispose();
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(doneButton);
		buttonPanel.add(Button);

		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		popupFrame.add(buttonPanel, constraints);
		popupFrame.setVisible(true);
	}

	private static final long serialVersionUID = -6371389704966320508L;
	private static final String[] TIMER_NAMES = { "No timer", "Bronstein Delay", "Fischer", "Fischer After", "Hour Glass",
			"Simple Delay", "Word" };

	public static boolean m_isCancelled = false;

	private JButton m_aiPlayButton;
	private JButton m_humanPlayButton;
	private JButton m_networkPlayButton;
	private JButton m_returnToMainButton;
	private String m_hostName = "";
	private boolean m_isWorking;
}
