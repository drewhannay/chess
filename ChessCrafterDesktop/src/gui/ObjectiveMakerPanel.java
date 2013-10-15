package gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import rules.EndOfGame;
import rules.Rules;
import utility.GuiUtility;

public class ObjectiveMakerPanel extends JPanel
{
	public ObjectiveMakerPanel(CustomSetupPanel customSetupMenu, JFrame optionsFrame)
	{
		mFrame = optionsFrame;
		mFrame.setVisible(true);
		mFrame.add(this);
		mFrame.setVisible(true);
		mFrame.setSize(400, 300);
		mFrame.setLocationRelativeTo(Driver.getInstance());
		mWhiteRules = customSetupMenu.mWhiteRules;
		mBlackRules = customSetupMenu.mBlackRules;
		initGUIComponents(customSetupMenu);
	}

	public void initGUIComponents(final CustomSetupPanel customSetupMenu)
	{
		setLayout(new GridBagLayout());
		setSize(600, 600);
		setBorder(BorderFactory.createLoweredBevelBorder());
		GridBagConstraints constraints = new GridBagConstraints();

		final JPanel objectiveWhitePanel = new JPanel();
		objectiveWhitePanel.setLayout(new GridLayout(5, 1));
		final JRadioButton whiteCaptureAllButton = new JRadioButton(Messages.getString("ObjectiveMakerPanel.captureAll"), false); //$NON-NLS-1$
		GuiUtility.requestFocus(whiteCaptureAllButton);
		whiteCaptureAllButton.setToolTipText(Messages.getString("ObjectiveMakerPanel.pressForCaptureAll")); //$NON-NLS-1$
		objectiveWhitePanel.add(whiteCaptureAllButton);
		if (customSetupMenu.mWhiteRules.getEndOfGame() == EndOfGame.CAPTURE_ALL_PIECES)
			whiteCaptureAllButton.setSelected(true);

		final JRadioButton whiteCaptureAllTypeButton = new JRadioButton(
				Messages.getString("ObjectiveMakerPanel.captureAllOfType"), false); //$NON-NLS-1$
		whiteCaptureAllTypeButton.setToolTipText(Messages.getString("ObjectiveMakerPanel.pressForCaptureAllOfType")); //$NON-NLS-1$
		objectiveWhitePanel.add(whiteCaptureAllTypeButton);
		if (customSetupMenu.mWhiteRules.getEndOfGame() == EndOfGame.CAPTURE_ALL_OF_TYPE)
			whiteCaptureAllTypeButton.setSelected(true);

		final JRadioButton whiteProtectObjectiveButton = new JRadioButton(
				Messages.getString("ObjectiveMakerPanel.protectObjective"), false); //$NON-NLS-1$
		whiteProtectObjectiveButton.setToolTipText(Messages.getString("ObjectiveMakerPanel.pressForProtectObjective")); //$NON-NLS-1$
		objectiveWhitePanel.add(whiteProtectObjectiveButton);
		if (customSetupMenu.mWhiteRules.getEndOfGame() == EndOfGame.CLASSIC)
			whiteProtectObjectiveButton.setSelected(true);

		final JRadioButton whiteLoseAllButton = new JRadioButton(Messages.getString("ObjectiveMakerPanel.loseAllPieces"), false); //$NON-NLS-1$
		whiteLoseAllButton.setToolTipText(Messages.getString("ObjectiveMakerPanel.pressForLoseAllPieces")); //$NON-NLS-1$
		objectiveWhitePanel.add(whiteLoseAllButton);
		if (customSetupMenu.mWhiteRules.getEndOfGame() == EndOfGame.LOSE_ALL_PIECES)
			whiteLoseAllButton.setSelected(true);

		final JRadioButton whiteCheckTimesButton = new JRadioButton(Messages.getString("ObjectiveMakerPanel.checkNumTimes"), false); //$NON-NLS-1$
		whiteCheckTimesButton.setToolTipText(Messages.getString("ObjectiveMakerPanel.pressForCheckNumTimes")); //$NON-NLS-1$
		objectiveWhitePanel.add(whiteCheckTimesButton);
		if (customSetupMenu.mWhiteRules.getEndOfGame() == EndOfGame.CHECK_N_TIMES)
			whiteCheckTimesButton.setSelected(true);

		final ButtonGroup whiteButtonGroup = new ButtonGroup();
		whiteButtonGroup.add(whiteCaptureAllButton);
		whiteButtonGroup.add(whiteCaptureAllTypeButton);
		whiteButtonGroup.add(whiteProtectObjectiveButton);
		whiteButtonGroup.add(whiteLoseAllButton);
		whiteButtonGroup.add(whiteCheckTimesButton);

		final JPanel objectiveBlackPanel = new JPanel();
		objectiveBlackPanel.setLayout(new GridLayout(5, 1));
		final JRadioButton blackCaptureAllButton = new JRadioButton(Messages.getString("ObjectiveMakerPanel.captureAll"), false); //$NON-NLS-1$
		blackCaptureAllButton.setToolTipText(Messages.getString("ObjectiveMakerPanel.pressForCaptureAll")); //$NON-NLS-1$
		objectiveBlackPanel.add(blackCaptureAllButton);
		if (customSetupMenu.mBlackRules.getEndOfGame() == EndOfGame.CAPTURE_ALL_PIECES)
			blackCaptureAllButton.setSelected(true);

		final JRadioButton blackCaptureAllTypeButton = new JRadioButton(
				Messages.getString("ObjectiveMakerPanel.captureAllOfType"), false); //$NON-NLS-1$
		blackCaptureAllTypeButton.setToolTipText(Messages.getString("ObjectiveMakerPanel.pressForCaptureAllOfType")); //$NON-NLS-1$
		objectiveBlackPanel.add(blackCaptureAllTypeButton);
		if (customSetupMenu.mBlackRules.getEndOfGame() == EndOfGame.CAPTURE_ALL_OF_TYPE)
			blackCaptureAllTypeButton.setSelected(true);

		final JRadioButton blackProtectObjectiveButton = new JRadioButton(
				Messages.getString("ObjectiveMakerPanel.protectObjective"), false); //$NON-NLS-1$
		blackProtectObjectiveButton.setToolTipText(Messages.getString("ObjectiveMakerPanel.pressForProtect")); //$NON-NLS-1$
		objectiveBlackPanel.add(blackProtectObjectiveButton);
		if (customSetupMenu.mBlackRules.getEndOfGame() == EndOfGame.CLASSIC)
			blackProtectObjectiveButton.setSelected(true);

		final JRadioButton blackLoseAllButton = new JRadioButton(Messages.getString("ObjectiveMakerPanel.loseAllPieces"), false); //$NON-NLS-1$
		blackLoseAllButton.setToolTipText(Messages.getString("ObjectiveMakerPanel.pressForLoseAllPieces")); //$NON-NLS-1$
		objectiveBlackPanel.add(blackLoseAllButton);
		if (customSetupMenu.mBlackRules.getEndOfGame() == EndOfGame.LOSE_ALL_PIECES)
			blackLoseAllButton.setSelected(true);

		final JRadioButton blackCheckTimesButton = new JRadioButton(Messages.getString("ObjectiveMakerPanel.checkNumTimes"), false); //$NON-NLS-1$
		blackCheckTimesButton.setToolTipText(Messages.getString("ObjectiveMakerPanel.pressForCheckNumTimes")); //$NON-NLS-1$
		objectiveBlackPanel.add(blackCheckTimesButton);
		if (customSetupMenu.mBlackRules.getEndOfGame() == EndOfGame.CHECK_N_TIMES)
			blackCheckTimesButton.setSelected(true);

		final ButtonGroup blackButtonGroup = new ButtonGroup();
		blackButtonGroup.add(blackCaptureAllButton);
		blackButtonGroup.add(blackCaptureAllTypeButton);
		blackButtonGroup.add(blackProtectObjectiveButton);
		blackButtonGroup.add(blackLoseAllButton);
		blackButtonGroup.add(blackCheckTimesButton);

		final JButton cancelButton = new JButton(Messages.getString("ObjectiveMakerPanel.cancel")); //$NON-NLS-1$
		cancelButton.setToolTipText(Messages.getString("ObjectiveMakerPanel.pressToReturn")); //$NON-NLS-1$
		GuiUtility.setupVariantCancelButton(cancelButton, this, mFrame);

		JButton saveButton = new JButton(Messages.getString("ObjectiveMakerPanel.save")); //$NON-NLS-1$
		saveButton.setToolTipText(Messages.getString("ObjectiveMakerPanel.pressToSave")); //$NON-NLS-1$
		saveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (whiteProtectObjectiveButton.isSelected() || blackProtectObjectiveButton.isSelected())
				{
					if (!(whiteProtectObjectiveButton.isSelected() && blackProtectObjectiveButton.isSelected()))
					{
						if (JOptionPane.showConfirmDialog(
								Driver.getInstance(),
								Messages.getString("ObjectiveMakerPanel.usingProtectNotRecommended") //$NON-NLS-1$
										+ Messages.getString("ObjectiveMakerPanel.continueAnyway"), Messages.getString("ObjectiveMakerPanel.continueQ"), JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) //$NON-NLS-1$ //$NON-NLS-2$
							return;
					}
				}

				else if ((whiteCaptureAllButton.isSelected() && blackLoseAllButton.isSelected())
						|| (blackCaptureAllButton.isSelected() && whiteLoseAllButton.isSelected()))
				{
					JOptionPane.showMessageDialog(Driver.getInstance(), Messages.getString("ObjectiveMakerPanel.captureAllAndLoseAll") //$NON-NLS-1$
							+ Messages.getString("ObjectiveMakerPanel.chooseAnotherCombo")); //$NON-NLS-1$
					return;
				}

				else if (whiteCheckTimesButton.isSelected() || blackCheckTimesButton.isSelected())
				{
					if (!(whiteCheckTimesButton.isSelected() && blackCheckTimesButton.isSelected()))
					{
						if (JOptionPane.showConfirmDialog(
								Driver.getInstance(),
								Messages.getString("ObjectiveMakerPanel.checkNumTimesCombo") //$NON-NLS-1$
										+ Messages.getString("ObjectiveMakerPanel.continueAnyways"), Messages.getString("ObjectiveMakerPanel.continueQ"), JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) //$NON-NLS-1$ //$NON-NLS-2$
							return;
					}
				}

				if (whiteCaptureAllButton.isSelected())
				{
					mWhiteRules.addEndOfGame(EndOfGame.CAPTURE_ALL_PIECES.init(0,
							Messages.getString("ObjectiveMakerPanel.empty"), false)); //$NON-NLS-1$
				}
				if (whiteCaptureAllTypeButton.isSelected())
				{
					mWhiteRules.addEndOfGame(EndOfGame.CAPTURE_ALL_OF_TYPE.init(0,
							Messages.getString("ObjectiveMakerPanel.knight"), false)); //$NON-NLS-1$
				}
				if (whiteProtectObjectiveButton.isSelected())
				{
					mWhiteRules.addEndOfGame(EndOfGame.CLASSIC.init(0, Messages.getString("ObjectiveMakerPanel.empty"), false)); //$NON-NLS-1$
					m_needsObjectivePiece = true;
				}
				if (whiteLoseAllButton.isSelected())
				{
					mWhiteRules.addEndOfGame(EndOfGame.LOSE_ALL_PIECES.init(0, Messages.getString("ObjectiveMakerPanel.empty"), false)); //$NON-NLS-1$
				}
				if (whiteCheckTimesButton.isSelected())
				{
					mWhiteRules.addEndOfGame(EndOfGame.CHECK_N_TIMES.init(3, Messages.getString("ObjectiveMakerPanel.empty"), false)); //$NON-NLS-1$
					m_needsObjectivePiece = true;
				}

				if (blackCaptureAllButton.isSelected())
				{
					mBlackRules.addEndOfGame(EndOfGame.CAPTURE_ALL_PIECES.init(0,
							Messages.getString("ObjectiveMakerPanel.empty"), true)); //$NON-NLS-1$
				}
				if (blackCaptureAllTypeButton.isSelected())
				{
					mBlackRules.addEndOfGame(EndOfGame.CAPTURE_ALL_OF_TYPE.init(0,
							Messages.getString("ObjectiveMakerPanel.knight"), true)); //$NON-NLS-1$
					m_needsObjectivePiece = false;
				}
				if (blackProtectObjectiveButton.isSelected())
				{
					mBlackRules.addEndOfGame(EndOfGame.CLASSIC.init(0, Messages.getString("ObjectiveMakerPanel.empty"), true)); //$NON-NLS-1$
				}
				if (blackLoseAllButton.isSelected())
				{
					mBlackRules.addEndOfGame(EndOfGame.LOSE_ALL_PIECES.init(0, Messages.getString("ObjectiveMakerPanel.empty"), true)); //$NON-NLS-1$
				}
				if (blackCheckTimesButton.isSelected())
				{
					mBlackRules.addEndOfGame(EndOfGame.CHECK_N_TIMES.init(3, Messages.getString("ObjectiveMakerPanel.empty"), true)); //$NON-NLS-1$
					m_needsObjectivePiece = true;
				}

				customSetupMenu.mWhiteRules = mWhiteRules;
				customSetupMenu.mBlackRules = mBlackRules;
				ObjectiveMakerPanel.this.removeAll();
				mFrame.setVisible(false);
			}
		});

		JPanel whiteTeamPanel = new JPanel();
		whiteTeamPanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("ObjectiveMakerPanel.whiteTeam"))); //$NON-NLS-1$
		whiteTeamPanel.setLayout(new GridBagLayout());

		JPanel whiteObjectivePanel = new JPanel();
		whiteObjectivePanel.setLayout(new GridBagLayout());
		constraints.gridheight = 1;
		constraints.gridx = 0;
		constraints.gridy = 1;
		whiteObjectivePanel.add(new JLabel(Messages.getString("ObjectiveMakerPanel.objectiveHTML")), constraints); //$NON-NLS-1$
		constraints.gridx = 0;
		constraints.gridy = 2;
		whiteObjectivePanel.add(objectiveWhitePanel, constraints);
		constraints.gridx = 0;
		constraints.gridy = 3;
		whiteObjectivePanel.add(new JLabel(Messages.getString("ObjectiveMakerPanel.emptySpace")), constraints); //$NON-NLS-1$

		constraints.gridx = 0;
		constraints.gridy = 1;
		whiteTeamPanel.add(whiteObjectivePanel, constraints);

		JPanel blackTeamPanel = new JPanel();
		blackTeamPanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("ObjectiveMakerPanel.blackTeam"))); //$NON-NLS-1$
		blackTeamPanel.setLayout(new GridBagLayout());

		JPanel blackObjectivePanel = new JPanel();
		blackObjectivePanel.setLayout(new GridBagLayout());
		constraints.gridheight = 1;
		constraints.gridx = 0;
		constraints.gridy = 1;
		blackObjectivePanel.add(new JLabel(Messages.getString("ObjectiveMakerPanel.objectiveHTML")), constraints); //$NON-NLS-1$
		constraints.gridx = 0;
		constraints.gridy = 2;
		blackObjectivePanel.add(objectiveBlackPanel, constraints);
		constraints.gridx = 0;
		constraints.gridy = 3;
		blackObjectivePanel.add(new JLabel(Messages.getString("ObjectiveMakerPanel.emptySpace")), constraints); //$NON-NLS-1$

		constraints.gridx = 0;
		constraints.gridy = 1;
		blackTeamPanel.add(blackObjectivePanel, constraints);

		constraints.insets = new Insets(10, 10, 10, 10);
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(whiteTeamPanel, constraints);

		constraints.insets = new Insets(10, 10, 10, 10);
		constraints.gridx = 1;
		constraints.gridy = 0;
		add(blackTeamPanel, constraints);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		add(buttonPanel, constraints);

		mFrame.pack();
	}

	private static final long serialVersionUID = 8365806731061105372L;

	static boolean m_needsObjectivePiece = false;

	private Rules mWhiteRules = new Rules(false);
	private Rules mBlackRules = new Rules(true);
	private JFrame mFrame;
}
