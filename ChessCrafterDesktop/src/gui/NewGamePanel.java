package gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
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
import logic.Result;
import net.NetworkPlayManager;
import timer.ChessTimer;
import timer.TimerTypes;
import utility.FileUtility;
import utility.GuiUtility;
import utility.RunnableOfT;
import ai.AIAdapter;
import ai.AIManager;
import ai.AIPlugin;

import com.google.common.collect.Lists;

public class NewGamePanel extends ChessPanel
{
	public NewGamePanel()
	{
		initGuiComponents();
	}

	private void initGuiComponents()
	{
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridy = 0;
		constraints.ipadx = 0;
		constraints.insets = new Insets(5, 50, 5, 50);
		constraints.anchor = GridBagConstraints.CENTER;
		add(new JLabel("<html><font color=\"#FFFFFF\">" + Messages.getString("NewGamePanel.howToPlay") + "</font></html>"), constraints); //$NON-NLS-1$

		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setLayout(new GridBagLayout());

		JButton humanPlayButton = new JButton(Messages.getString("NewGamePanel.humanPlay")); //$NON-NLS-1$
		humanPlayButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (mPopupFrame == null)
				{
					createNewGamePopup();
				}
			}
		});
		constraints.gridy = 1;
		constraints.ipadx = 7;
		constraints.insets = new Insets(5, 5, 0, 5);
		buttonPanel.add(humanPlayButton, constraints);

		if (NetworkPlayManager.getInstance().networkPlayIsAvailable())
		{
			JButton networkPlayButton = new JButton(Messages.getString("NewGamePanel.networkPlay")); //$NON-NLS-1$
			networkPlayButton.addActionListener(NetworkPlayManager.getInstance().createNetworkPlayActionListener());

			constraints.gridy = 2;
			constraints.ipadx = 0;
			constraints.insets = new Insets(2, 5, 0, 5);
			buttonPanel.add(networkPlayButton, constraints);
		}

		JButton aiPlayButton = new JButton(Messages.getString("NewGamePanel.aiPlay")); //$NON-NLS-1$
		aiPlayButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				createAIPopup();
			}
		});
		constraints.gridy = 3;
		constraints.ipadx = 28;
		constraints.insets = new Insets(2, 5, 5, 5);
		buttonPanel.add(aiPlayButton, constraints);

		constraints.gridy = 1;
		add(buttonPanel, constraints);

		JButton backButton = new JButton(Messages.getString("NewGamePanel.returnToMenu")); //$NON-NLS-1$
		backButton.setToolTipText(Messages.getString("NewGamePanel.returnToMenu")); //$NON-NLS-1$
		backButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				Driver.getInstance().revertToMainPanel();
			}
		});

		constraints.gridy = 2;
		add(backButton, constraints);
	}

	private void initPopup()
	{
		mPopupFrame = new JFrame(Messages.getString("NewGamePanel.newGame")); //$NON-NLS-1$
		mPopupFrame.setSize(325, 225);
		mPopupFrame.setResizable(false);
		Driver.centerFrame();
		mPopupFrame.setLocationRelativeTo(Driver.getInstance());
		mPopupFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		mPopupFrame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				mPopupFrame.setVisible(false);
				mPopupFrame.dispose();
				mPopupFrame = null;
			}
		});
		
		mPopupPanel = new ChessPanel();
		mPopupPanel.setLayout(new GridBagLayout());
	}

	private void createNewGamePopup()
	{
		initPopup();

		
		GridBagConstraints constraints = new GridBagConstraints();

		final JComboBox dropdown;
		try
		{
			dropdown = new JComboBox(Builder.getVariantFileArray());
		}
		catch (IOException e1)
		{
			JOptionPane.showMessageDialog(Driver.getInstance(), Messages.getString("NewGamePanel.errorCouldntLoadVariantFiles")); //$NON-NLS-1$
			e1.printStackTrace();
			return;
		}

		// variant type selector
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		mPopupPanel.add(new JLabel("<html><font color=\"#FFFFFF\">" + Messages.getString("NewGamePanel.type") + "</font></html>"), constraints); //$NON-NLS-1$

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		mPopupPanel.add(dropdown, constraints);

		// total time and increment fields
		final JLabel totalTimeLabel = new JLabel("<html><font color=\"#FFFFFF\">" + Messages.getString("NewGamePanel.totalTime") + "</font></html>"); //$NON-NLS-1$
		totalTimeLabel.setEnabled(false);
		final JTextField totalTimeField = new JTextField("120", 3); //$NON-NLS-1$
		totalTimeField.setEnabled(false);

		final JLabel incrementLabel = new JLabel("<html><font color=\"#FFFFFF\">" + Messages.getString("NewGamePanel.increment") + "</font></html>"); //$NON-NLS-1$
		incrementLabel.setEnabled(false);
		final JTextField incrementField = new JTextField("10", 3); //$NON-NLS-1$
		incrementField.setEnabled(false);

		// combo box for selecting a timer
		final JComboBox timerComboBox = new JComboBox(TimerTypes.values());
		timerComboBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				TimerTypes timerType = (TimerTypes) timerComboBox.getSelectedItem();
				if (timerType != TimerTypes.NO_TIMER)
				{
					totalTimeLabel.setEnabled(true);
					totalTimeField.setEnabled(true);
					incrementLabel.setEnabled(true);
					incrementField.setEnabled(true);
				}
				else
				{
					totalTimeLabel.setEnabled(false);
					totalTimeField.setEnabled(false);
					incrementLabel.setEnabled(false);
					incrementField.setEnabled(false);
				}
			}
		});

		// add the combo box to the frame
		constraints.gridx = 0;
		constraints.gridy = 1;
		mPopupPanel.add(new JLabel("<html><font color=\"#FFFFFF\">" + Messages.getString("NewGamePanel.timer") + "</font></html>"), constraints); //$NON-NLS-1$

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		mPopupPanel.add(timerComboBox, constraints);

		// add the total time field to the frame
		constraints.gridx = 0;
		constraints.gridy = 2;
		mPopupPanel.add(totalTimeLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		mPopupPanel.add(totalTimeField, constraints);

		// add the increment field to the frame
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.CENTER;
		mPopupPanel.add(incrementLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 3;
		mPopupPanel.add(incrementField, constraints);

		// set up the done button
		final JButton doneButton = new JButton(Messages.getString("NewGamePanel.start")); //$NON-NLS-1$
		doneButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				TimerTypes timerType = (TimerTypes) timerComboBox.getSelectedItem();
				long startTime = Integer.parseInt(totalTimeField.getText()) * 1000;
				long increment = Integer.parseInt(incrementField.getText()) * 1000;

				final Game gameToPlay = Builder.newGame(dropdown.getSelectedItem().toString());

				RunnableOfT<Boolean> timeElapsedCallback = new RunnableOfT<Boolean>()
				{
					@Override
					public void run(Boolean isBlackTimer)
					{
						Result result = isBlackTimer ? Result.WHITE_WIN : Result.BLACK_WIN;
						result.setGuiText(Messages.getString("NewGamePanel.timeHasRunOut") + result.winText() + Messages.getString("NewGamePanel.newLine")); //$NON-NLS-1$ //$NON-NLS-2$
						GuiUtility.getChessCrafter().getPlayGameScreen(gameToPlay).endOfGame(result);
					}
				};
				ChessTimer blackTimer = ChessTimer.createTimer(timerType, timeElapsedCallback, increment, startTime, true);
				ChessTimer whiteTimer = ChessTimer.createTimer(timerType, timeElapsedCallback, increment, startTime, false);

				gameToPlay.setTimers(whiteTimer, blackTimer);
				PlayGamePanel gamePanel = new PlayGamePanel(gameToPlay);
				Driver.getInstance().setPanel(gamePanel);
				mPopupFrame.dispose();
				mPopupFrame = null;
			}
		});

		// set up the cancel button
		final JButton cancelButton = new JButton(Messages.getString("NewGamePanel.cancel")); //$NON-NLS-1$
		cancelButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				mPopupFrame.dispose();
				mPopupFrame = null;
			}
		});

		// add the done and cancel buttons to the panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(doneButton);
		buttonPanel.add(cancelButton);

		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(20, 0, 10, 0);
		mPopupPanel.add(buttonPanel, constraints);

		mPopupFrame.add(mPopupPanel);
		
		mPopupFrame.setVisible(true);
	}

	private void createAIPopup()
	{
		if (!validAIFilesExist())
			return;

		initPopup();

		GridBagConstraints constraints = new GridBagConstraints();

		// add the variant type selector to the frame
		constraints.gridx = 0;
		constraints.gridy = 0;
		mPopupPanel.add(new JLabel("<html><font color=\"#FFFFFF\">" + Messages.getString("NewGamePanel.type") + "</font></html>"), constraints); //$NON-NLS-1$

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.insets = new Insets(3, 0, 3, 0);
		try
		{
			mPopupPanel.add(new JComboBox(Builder.getVariantFileArray()), constraints);
		}
		catch (IOException e1)
		{
			JOptionPane.showMessageDialog(Driver.getInstance(), Messages.getString("NewGamePanel.errorDidYouDeleteSomething")); //$NON-NLS-1$
			e1.printStackTrace();
		}

		// add the AI selector to the frame
		constraints.gridx = 0;
		constraints.gridy = 1;
		mPopupPanel.add(new JLabel("<html><font color=\"#FFFFFF\">" + Messages.getString("NewGamePanel.ai") + "</font></html>"), constraints); //$NON-NLS-1$

		final JComboBox aiComboBox = new JComboBox(AIManager.getInstance().getAIFiles());
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		mPopupPanel.add(aiComboBox, constraints);

		// add a button to the frame for installing a new AI
		JButton addAIFileButton = new JButton(Messages.getString("NewGamePanel.installNewAI")); //$NON-NLS-1$
		addAIFileButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (GuiUtility.tryAIFileInstall(NewGamePanel.this))
				{
					aiComboBox.removeAllItems();
					for (String fileName : AIManager.getInstance().getAIFiles())
						aiComboBox.addItem(fileName);
				}
			}
		});

		JButton nextButton = new JButton(Messages.getString("NewGamePanel.next")); //$NON-NLS-1$
		nextButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				final String aiFileName = (String) aiComboBox.getSelectedItem();
				File aiFile = FileUtility.getAIFile(aiFileName);
				if (aiComboBox.getSelectedItem() == null)
				{
					JOptionPane.showMessageDialog(Driver.getInstance(),
							Messages.getString("NewGamePanel.youHaveNotSelectedAI"), Messages.getString("NewGamePanel.noAIFile"), //$NON-NLS-1$ //$NON-NLS-2$
							JOptionPane.PLAIN_MESSAGE);
					return;
				}
				Game gameToPlay;
				try
				{
					gameToPlay = Builder.newGame((String) new JComboBox(Builder.getVariantFileArray()).getSelectedItem());
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
					JOptionPane.showMessageDialog(Driver.getInstance(), Messages.getString("NewGamePanel.errorDidYouDeleteSomething")); //$NON-NLS-1$
					return;
				}

				JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
				StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, Locale.getDefault(), null);

				String[] compileOptions = new String[] { "-d", "bin" }; //$NON-NLS-1$ //$NON-NLS-2$
				Iterable<String> compilationOptions = Arrays.asList(compileOptions);

				List<File> sourceFileList = Lists.newArrayList();
				sourceFileList.add(aiFile);
				Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(sourceFileList);
				CompilationTask task = compiler.getTask(null, fileManager, null, compilationOptions, null, compilationUnits);

				if (!task.call())
				{
					JOptionPane.showMessageDialog(
							Driver.getInstance(),
							Messages.getString("NewGamePanel.compilationFailed") //$NON-NLS-1$
									+ Messages.getString("NewGamePanel.makeSureClassImplementsAIPlugin") //$NON-NLS-1$
									+ Messages.getString("NewGamePanel.makeSureClassIncludes") + "import ai.*;\n" //$NON-NLS-1$ //$NON-NLS-2$
									+ "import ai.AIAdapter.*;\n", Messages.getString("NewGamePanel.compilationFailure"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
					return;
				}

				try
				{
					fileManager.close();

					final AIPlugin aiPlugin;
					final AIAdapter aiAdapter = new AIAdapter(gameToPlay);
					ClassLoader classLoader = ClassLoader.getSystemClassLoader();
					Class<?> klazz = classLoader.loadClass(aiFileName.substring(0, aiFileName.indexOf(".java"))); //$NON-NLS-1$
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

					PlayNetGamePanel playNetGame = new PlayNetGamePanel(gameToPlay, false, false);
					playNetGame.setIsAIGame(true);
					Driver.getInstance().setPanel(playNetGame);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

				mPopupFrame.dispose();
				mPopupFrame = null;
			}
		});

		JButton cancelButton = new JButton(Messages.getString("NewGamePanel.cancel")); //$NON-NLS-1$
		cancelButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				mPopupFrame.dispose();
				mPopupFrame = null;
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(nextButton);
		buttonPanel.add(cancelButton);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.insets = new Insets(5, 0, 5, 0);
		mPopupPanel.add(addAIFileButton, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.insets = new Insets(20, 0, 10, 0);
		mPopupPanel.add(buttonPanel, constraints);

		mPopupFrame.add(mPopupPanel);
		
		mPopupFrame.setVisible(true);
	}

	public boolean validAIFilesExist()
	{
		if (AIManager.getInstance().getAIFiles().length == 0)
		{
			switch (JOptionPane.showConfirmDialog(Driver.getInstance(),
					Messages.getString("NewGamePanel.noAIFilesInstalled"), Messages.getString("NewGamePanel.installAIFiles"), //$NON-NLS-1$ //$NON-NLS-2$
					JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE))
			{
			case JOptionPane.YES_OPTION:
				return GuiUtility.tryAIFileInstall(NewGamePanel.this);
			case JOptionPane.NO_OPTION:
			default:
				return false;
			}
		}
		else
		{
			return true;
		}
	}

	private static final long serialVersionUID = -6371389704966320508L;

	private ChessPanel mPopupPanel;
	private JFrame mPopupFrame;
}
