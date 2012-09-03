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
import utility.GUIUtility;

public class ObjectiveMaker extends JPanel
{
	public ObjectiveMaker(CustomSetupMenu customSetupMenu, JFrame optionsFrame)
	{
		m_frame = optionsFrame;
		m_frame.setVisible(true);
		m_frame.add(this);
		m_frame.setVisible(true);
		m_frame.setSize(400, 300);
		m_frame.setLocationRelativeTo(Driver.getInstance());
		m_whiteRules = customSetupMenu.m_whiteRules;
		m_blackRules = customSetupMenu.m_blackRules;
		initGUIComponents(customSetupMenu);
	}

	public void initGUIComponents(final CustomSetupMenu customSetupMenu)
	{
		setLayout(new GridBagLayout());
		setSize(600, 600);
		setBorder(BorderFactory.createLoweredBevelBorder());
		GridBagConstraints constraints = new GridBagConstraints();

		final JPanel objectiveWhitePanel = new JPanel();
		objectiveWhitePanel.setLayout(new GridLayout(5, 1));
		final JRadioButton whiteCaptureAllButton = new JRadioButton("Capture All", false);
		GUIUtility.requestFocus(whiteCaptureAllButton);
		whiteCaptureAllButton.setToolTipText("Press me if you want the objective to be capturing all enemy pieces");
		objectiveWhitePanel.add(whiteCaptureAllButton);
		if (customSetupMenu.m_whiteRules.theEndIsNigh().equals("captureAllPieces"))
			whiteCaptureAllButton.setSelected(true);

		final JRadioButton whiteCaptureAllTypeButton = new JRadioButton("Capture All of Type", false);
		whiteCaptureAllTypeButton
				.setToolTipText("Press me if you want the objective to be capturing all enemy pieces of a certain type");
		objectiveWhitePanel.add(whiteCaptureAllTypeButton);
		if (customSetupMenu.m_whiteRules.theEndIsNigh().equals("captureAllOfType"))
			whiteCaptureAllTypeButton.setSelected(true);

		final JRadioButton whiteProtectObjectiveButton = new JRadioButton("Protect Objective", false);
		whiteProtectObjectiveButton.setToolTipText("Press me if you want the objective to be protecting your own objective");
		objectiveWhitePanel.add(whiteProtectObjectiveButton);
		if (customSetupMenu.m_whiteRules.theEndIsNigh().equals("classic"))
			whiteProtectObjectiveButton.setSelected(true);

		final JRadioButton whiteLoseAllButton = new JRadioButton("Lose All Pieces", false);
		whiteLoseAllButton.setToolTipText("Press me if you want the objective to be losing all of your pieces");
		objectiveWhitePanel.add(whiteLoseAllButton);
		if (customSetupMenu.m_whiteRules.theEndIsNigh().equals("loseAllPieces"))
			whiteLoseAllButton.setSelected(true);

		final JRadioButton whiteCheckTimesButton = new JRadioButton("Check # Times", false);
		whiteCheckTimesButton
				.setToolTipText("Press me if you want the objective to be putting the other team in check a certain amount of times");
		objectiveWhitePanel.add(whiteCheckTimesButton);
		if (customSetupMenu.m_whiteRules.theEndIsNigh().equals("checkNTimes"))
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
		if (customSetupMenu.m_blackRules.theEndIsNigh().equals("captureAllPieces"))
			blackCaptureAllButton.setSelected(true);

		final JRadioButton blackCaptureAllTypeButton = new JRadioButton("Capture All of Type", false);
		blackCaptureAllTypeButton
				.setToolTipText("Press me if you want the objective to be capturing all enemy pieces of a certain type");
		objectiveBlackPanel.add(blackCaptureAllTypeButton);
		if (customSetupMenu.m_blackRules.theEndIsNigh().equals("captureAllOfType"))
			blackCaptureAllTypeButton.setSelected(true);

		final JRadioButton blackProtectObjectiveButton = new JRadioButton("Protect Objective", false);
		blackProtectObjectiveButton.setToolTipText("Press me if you want the objective to be protecting your own objective");
		objectiveBlackPanel.add(blackProtectObjectiveButton);
		if (customSetupMenu.m_blackRules.theEndIsNigh().equals("classic"))
			blackProtectObjectiveButton.setSelected(true);

		final JRadioButton blackLoseAllButton = new JRadioButton("Lose All Pieces", false);
		blackLoseAllButton.setToolTipText("Press me if you want the objective to be losing all of your pieces");
		objectiveBlackPanel.add(blackLoseAllButton);
		if (customSetupMenu.m_blackRules.theEndIsNigh().equals("loseAllPieces"))
			blackLoseAllButton.setSelected(true);

		final JRadioButton blackCheckTimesButton = new JRadioButton("Check # Times", false);
		blackCheckTimesButton
				.setToolTipText("Press me if you want the objective to be putting the other team in check a certain amount of times");
		objectiveBlackPanel.add(blackCheckTimesButton);
		if (customSetupMenu.m_blackRules.theEndIsNigh().equals("checkNTimes"))
			blackCheckTimesButton.setSelected(true);

		final ButtonGroup blackButtonGroup = new ButtonGroup();
		blackButtonGroup.add(blackCaptureAllButton);
		blackButtonGroup.add(blackCaptureAllTypeButton);
		blackButtonGroup.add(blackProtectObjectiveButton);
		blackButtonGroup.add(blackLoseAllButton);
		blackButtonGroup.add(blackCheckTimesButton);

		final JButton cancelButton = new JButton("Cancel");
		cancelButton.setToolTipText("Press me to return to the piece creation window");
		GUIUtility.setupVariantCancelButton(cancelButton, this, m_frame);

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
						int answer = JOptionPane.showConfirmDialog(null,
								"Using Protect Objective combined with another objective style is not recommended.\n"
										+ "Do you want to continue anyways?", "Continue?", JOptionPane.YES_NO_OPTION);
						// TODO: remove the magic number
						if (answer != 0)
							return;
					}
				}

				if ((whiteCaptureAllButton.isSelected() && blackLoseAllButton.isSelected())
						|| (blackCaptureAllButton.isSelected() && whiteLoseAllButton.isSelected()))
				{
					JOptionPane.showMessageDialog(null, "Capture All and Lose All is not a valid combination of objectives.\n"
							+ "Please choose another combination.");
					return;
				}

				if (whiteCheckTimesButton.isSelected() || blackCheckTimesButton.isSelected())
				{
					if (!(whiteCheckTimesButton.isSelected() && blackCheckTimesButton.isSelected()))
					{
						int answer = JOptionPane.showConfirmDialog(null,
								"Using Check # Times combined with another objective style is not recommended.\n"
										+ "Do you want to continue anyways?", "Continue?", JOptionPane.YES_NO_OPTION);
						// TODO: remove the magic number
						if (answer != 0)
							return;
					}
				}

				if (whiteCaptureAllButton.isSelected())
				{
					m_whiteRules.addEndOfGame(new EndOfGame("captureAllPieces", 0, "", false));
				}
				if (whiteCaptureAllTypeButton.isSelected())
				{
					m_whiteRules.addEndOfGame(new EndOfGame("captureAllOfType", 0, "Knight", false));
				}
				if (whiteProtectObjectiveButton.isSelected())
				{
					m_whiteRules.addEndOfGame(new EndOfGame("classic", 0, "", false));
					m_needsObjectivePiece = true;
				}
				if (whiteLoseAllButton.isSelected())
				{
					m_whiteRules.addEndOfGame(new EndOfGame("loseAllPieces", 0, "", false));
				}
				if (whiteCheckTimesButton.isSelected())
				{
					m_whiteRules.addEndOfGame(new EndOfGame("checkNTimes", 3, "", false));
					m_needsObjectivePiece = true;
				}

				if (blackCaptureAllButton.isSelected())
				{
					m_blackRules.addEndOfGame(new EndOfGame("captureAllPieces", 0, "", true));
				}
				if (blackCaptureAllTypeButton.isSelected())
				{
					m_blackRules.addEndOfGame(new EndOfGame("captureAllOfType", 0, "Knight", true));
					m_needsObjectivePiece = false;
				}
				if (blackProtectObjectiveButton.isSelected())
				{
					m_blackRules.addEndOfGame(new EndOfGame("classic", 0, "", true));
				}
				if (blackLoseAllButton.isSelected())
				{
					m_blackRules.addEndOfGame(new EndOfGame("loseAllPieces", 0, "", true));
				}
				if (blackCheckTimesButton.isSelected())
				{
					m_blackRules.addEndOfGame(new EndOfGame("checkNTimes", 3, "", true));
					m_needsObjectivePiece = true;
				}

				customSetupMenu.m_whiteRules = m_whiteRules;
				customSetupMenu.m_blackRules = m_blackRules;
				ObjectiveMaker.this.removeAll();
				m_frame.setVisible(false);
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

		m_frame.pack();
	}

	private static final long serialVersionUID = 8365806731061105372L;

	static boolean m_needsObjectivePiece = false;

	private Rules m_whiteRules = new Rules(false, false);
	private Rules m_blackRules = new Rules(false, true);
	private JFrame m_frame;
}
