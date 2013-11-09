
package gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import logic.GameBuilder;
import logic.Result;
import models.Game;
import timer.ChessTimer;
import timer.TimerTypes;
import utility.DesktopGuiUtility;
import utility.RunnableOfT;

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
		add(DesktopGuiUtility.createJLabel(Messages.getString("NewGamePanel.howToPlay")), constraints); //$NON-NLS-1$

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		buttonPanel.setOpaque(false);

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
			dropdown = new JComboBox(GameBuilder.getVariantFileArray());
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
		mPopupPanel.add(DesktopGuiUtility.createJLabel(Messages.getString("NewGamePanel.type")), constraints); //$NON-NLS-1$

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		mPopupPanel.add(dropdown, constraints);

		// total time and increment fields
		final JLabel totalTimeLabel = DesktopGuiUtility.createJLabel(Messages.getString("NewGamePanel.totalTime")); //$NON-NLS-1$
		totalTimeLabel.setEnabled(false);
		totalTimeLabel.setForeground(Color.white);
		final JTextField totalTimeField = new JTextField("120", 3); //$NON-NLS-1$
		totalTimeField.setEnabled(false);

		final JLabel incrementLabel = DesktopGuiUtility.createJLabel(Messages.getString("NewGamePanel.increment")); //$NON-NLS-1$
		incrementLabel.setEnabled(false);
		incrementLabel.setForeground(Color.white);
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
		mPopupPanel.add(DesktopGuiUtility.createJLabel(Messages.getString("NewGamePanel.timer")), constraints); //$NON-NLS-1$

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

				final Game gameToPlay = GameBuilder.newGame(dropdown.getSelectedItem().toString());

				RunnableOfT<Boolean> timeElapsedCallback = new RunnableOfT<Boolean>()
				{
					@Override
					public void run(Boolean isBlackTimer)
					{
						Result result = isBlackTimer ? Result.WHITE_WIN : Result.BLACK_WIN;
						result.setGuiText(Messages.getString("NewGamePanel.timeHasRunOut") + result.winText() + Messages.getString("NewGamePanel.newLine")); //$NON-NLS-1$ //$NON-NLS-2$
						DesktopGuiUtility.getChessCrafter().getPlayGameScreen(gameToPlay).endOfGame(result);
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
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.setOpaque(false);
		buttonPanel.add(doneButton);
		buttonPanel.add(cancelButton);

		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		mPopupPanel.add(buttonPanel, constraints);

		mPopupFrame.add(mPopupPanel);
		mPopupFrame.setVisible(true);
	}

	private static final long serialVersionUID = -6371389704966320508L;

	private JFrame mPopupFrame;
	private ChessPanel mPopupPanel;
}
