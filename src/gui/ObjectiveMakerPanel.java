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
		final JRadioButton whiteCaptureAllButton = new JRadioButton("Capture All", false);
		GuiUtility.requestFocus(whiteCaptureAllButton);
		whiteCaptureAllButton.setToolTipText("Press me if you want the objective to be capturing all enemy pieces");
		objectiveWhitePanel.add(whiteCaptureAllButton);
		if (customSetupMenu.mWhiteRules.getEndOfGame() == EndOfGame.CAPTURE_ALL_PIECES)
			whiteCaptureAllButton.setSelected(true);

		final JRadioButton whiteCaptureAllTypeButton = new JRadioButton("Capture All of Type", false);
		whiteCaptureAllTypeButton
				.setToolTipText("Press me if you want the objective to be capturing all enemy pieces of a certain type");
		objectiveWhitePanel.add(whiteCaptureAllTypeButton);
		if (customSetupMenu.mWhiteRules.getEndOfGame() == EndOfGame.CAPTURE_ALL_OF_TYPE)
			whiteCaptureAllTypeButton.setSelected(true);

		final JRadioButton whiteProtectObjectiveButton = new JRadioButton("Protect Objective", false);
		whiteProtectObjectiveButton.setToolTipText("Press me if you want the objective to be protecting your own objective");
		objectiveWhitePanel.add(whiteProtectObjectiveButton);
		if (customSetupMenu.mWhiteRules.getEndOfGame() == EndOfGame.CLASSIC)
			whiteProtectObjectiveButton.setSelected(true);

		final JRadioButton whiteLoseAllButton = new JRadioButton("Lose All Pieces", false);
		whiteLoseAllButton.setToolTipText("Press me if you want the objective to be losing all of your pieces");
		objectiveWhitePanel.add(whiteLoseAllButton);
		if (customSetupMenu.mWhiteRules.getEndOfGame() == EndOfGame.LOSE_ALL_PIECES)
			whiteLoseAllButton.setSelected(true);

		final JRadioButton whiteCheckTimesButton = new JRadioButton("Check # Times", false);
		whiteCheckTimesButton
				.setToolTipText("Press me if you want the objective to be putting the other team in check a certain amount of times");
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
		final JRadioButton blackCaptureAllButton = new JRadioButton("Capture All", false);
		blackCaptureAllButton.setToolTipText("Press me if you want the objective to be capturing all enemy pieces");
		objectiveBlackPanel.add(blackCaptureAllButton);
		if (customSetupMenu.mBlackRules.getEndOfGame() == EndOfGame.CAPTURE_ALL_PIECES)
			blackCaptureAllButton.setSelected(true);

		final JRadioButton blackCaptureAllTypeButton = new JRadioButton("Capture All of Type", false);
		blackCaptureAllTypeButton
				.setToolTipText("Press me if you want the objective to be capturing all enemy pieces of a certain type");
		objectiveBlackPanel.add(blackCaptureAllTypeButton);
		if (customSetupMenu.mBlackRules.getEndOfGame() == EndOfGame.CAPTURE_ALL_OF_TYPE)
			blackCaptureAllTypeButton.setSelected(true);

		final JRadioButton blackProtectObjectiveButton = new JRadioButton("Protect Objective", false);
		blackProtectObjectiveButton.setToolTipText("Press me if you want the objective to be protecting your own objective");
		objectiveBlackPanel.add(blackProtectObjectiveButton);
		if (customSetupMenu.mBlackRules.getEndOfGame() == EndOfGame.CLASSIC)
			blackProtectObjectiveButton.setSelected(true);

		final JRadioButton blackLoseAllButton = new JRadioButton("Lose All Pieces", false);
		blackLoseAllButton.setToolTipText("Press me if you want the objective to be losing all of your pieces");
		objectiveBlackPanel.add(blackLoseAllButton);
		if (customSetupMenu.mBlackRules.getEndOfGame() == EndOfGame.LOSE_ALL_PIECES)
			blackLoseAllButton.setSelected(true);

		final JRadioButton blackCheckTimesButton = new JRadioButton("Check # Times", false);
		blackCheckTimesButton
				.setToolTipText("Press me if you want the objective to be putting the other team in check a certain amount of times");
		objectiveBlackPanel.add(blackCheckTimesButton);
		if (customSetupMenu.mBlackRules.getEndOfGame() == EndOfGame.CHECK_N_TIMES)
			blackCheckTimesButton.setSelected(true);

		final ButtonGroup blackButtonGroup = new ButtonGroup();
		blackButtonGroup.add(blackCaptureAllButton);
		blackButtonGroup.add(blackCaptureAllTypeButton);
		blackButtonGroup.add(blackProtectObjectiveButton);
		blackButtonGroup.add(blackLoseAllButton);
		blackButtonGroup.add(blackCheckTimesButton);

		final JButton cancelButton = new JButton("Cancel");
		cancelButton.setToolTipText("Press me to return to the piece creation window");
		GuiUtility.setupVariantCancelButton(cancelButton, this, mFrame);

		JButton saveButton = new JButton("Save");
		saveButton.setToolTipText("Press me to save these objective settings");
		saveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (whiteProtectObjectiveButton.isSelected() || blackProtectObjectiveButton.isSelected())
				{
					if (!(whiteProtectObjectiveButton.isSelected() && blackProtectObjectiveButton.isSelected()))
					{
						if (JOptionPane.showConfirmDialog(Driver.getInstance(),
								"Using Protect Objective combined with another objective style is not recommended.\n"
										+ "Do you want to continue anyways?", "Continue?", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION)
							return;
					}
				}

				else if ((whiteCaptureAllButton.isSelected() && blackLoseAllButton.isSelected())
						|| (blackCaptureAllButton.isSelected() && whiteLoseAllButton.isSelected()))
				{
					JOptionPane.showMessageDialog(Driver.getInstance(), "Capture All and Lose All is not a valid combination of objectives.\n"
							+ "Please choose another combination.");
					return;
				}

				else if (whiteCheckTimesButton.isSelected() || blackCheckTimesButton.isSelected())
				{
					if (!(whiteCheckTimesButton.isSelected() && blackCheckTimesButton.isSelected()))
					{
						if (JOptionPane.showConfirmDialog(Driver.getInstance(),
								"Using Check # Times combined with another objective style is not recommended.\n"
										+ "Do you want to continue anyways?", "Continue?", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION)
							return;
					}
				}

				if (whiteCaptureAllButton.isSelected())
				{
					mWhiteRules.addEndOfGame(EndOfGame.CAPTURE_ALL_PIECES.init(0, "", false));
				}
				if (whiteCaptureAllTypeButton.isSelected())
				{
					mWhiteRules.addEndOfGame(EndOfGame.CAPTURE_ALL_OF_TYPE.init(0, "Knight", false));
				}
				if (whiteProtectObjectiveButton.isSelected())
				{
					mWhiteRules.addEndOfGame(EndOfGame.CLASSIC.init(0, "", false));
					m_needsObjectivePiece = true;
				}
				if (whiteLoseAllButton.isSelected())
				{
					mWhiteRules.addEndOfGame(EndOfGame.LOSE_ALL_PIECES.init(0, "", false));
				}
				if (whiteCheckTimesButton.isSelected())
				{
					mWhiteRules.addEndOfGame(EndOfGame.CHECK_N_TIMES.init(3, "", false));
					m_needsObjectivePiece = true;
				}

				if (blackCaptureAllButton.isSelected())
				{
					mBlackRules.addEndOfGame(EndOfGame.CAPTURE_ALL_PIECES.init(0, "", true));
				}
				if (blackCaptureAllTypeButton.isSelected())
				{
					mBlackRules.addEndOfGame(EndOfGame.CAPTURE_ALL_OF_TYPE.init(0, "Knight", true));
					m_needsObjectivePiece = false;
				}
				if (blackProtectObjectiveButton.isSelected())
				{
					mBlackRules.addEndOfGame(EndOfGame.CLASSIC.init(0, "", true));
				}
				if (blackLoseAllButton.isSelected())
				{
					mBlackRules.addEndOfGame(EndOfGame.LOSE_ALL_PIECES.init(0, "", true));
				}
				if (blackCheckTimesButton.isSelected())
				{
					mBlackRules.addEndOfGame(EndOfGame.CHECK_N_TIMES.init(3, "", true));
					m_needsObjectivePiece = true;
				}

				customSetupMenu.mWhiteRules = mWhiteRules;
				customSetupMenu.mBlackRules = mBlackRules;
				ObjectiveMakerPanel.this.removeAll();
				mFrame.setVisible(false);
			}
		});

		JPanel whiteTeamPanel = new JPanel();
		whiteTeamPanel.setBorder(BorderFactory.createTitledBorder("White Team"));
		whiteTeamPanel.setLayout(new GridBagLayout());

		JPanel whiteObjectivePanel = new JPanel();
		whiteObjectivePanel.setLayout(new GridBagLayout());
		constraints.gridheight = 1;
		constraints.gridx = 0;
		constraints.gridy = 1;
		whiteObjectivePanel.add(new JLabel("<html><u> Objective </u></br></html>"), constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		whiteObjectivePanel.add(objectiveWhitePanel, constraints);
		constraints.gridx = 0;
		constraints.gridy = 3;
		whiteObjectivePanel.add(new JLabel(" "), constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		whiteTeamPanel.add(whiteObjectivePanel, constraints);

		JPanel blackTeamPanel = new JPanel();
		blackTeamPanel.setBorder(BorderFactory.createTitledBorder("Black Team"));
		blackTeamPanel.setLayout(new GridBagLayout());

		JPanel blackObjectivePanel = new JPanel();
		blackObjectivePanel.setLayout(new GridBagLayout());
		constraints.gridheight = 1;
		constraints.gridx = 0;
		constraints.gridy = 1;
		blackObjectivePanel.add(new JLabel("<html><u> Objective </u></br></html>"), constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		blackObjectivePanel.add(objectiveBlackPanel, constraints);
		constraints.gridx = 0;
		constraints.gridy = 3;
		blackObjectivePanel.add(new JLabel(" "), constraints);

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
