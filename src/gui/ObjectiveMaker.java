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
import javax.swing.SwingUtilities;

import rules.EndOfGame;
import rules.Rules;

/**
 * @author jmccormi Window to set up the special rules for the variants
 */

public class ObjectiveMaker extends JPanel
{

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 8365806731061105372L;
	/**
	 * Rules for the White Team
	 */
	private Rules whiteRules = new Rules(false, false);
	/**
	 * Rules for the Black Team
	 */
	private Rules blackRules = new Rules(false, true);
	/**
	 * This is a boolean to see if we need to force a piece to be set as an
	 * Objective
	 */
	static boolean needsObj = false;
	/**
	 * Frame to hold the window
	 */
	private JFrame frame;
	/**
	 * holder for this ObjectiveMaker
	 */
	private ObjectiveMaker holder = this;

	/**
	 * Constructor for setting up the rules window
	 * 
	 * @param b the builder containing everything
	 */
	/*
	 * public ObjectiveMaker(Builder b) { this.b = b; initComponents(); }
	 */

	public ObjectiveMaker(CustomSetupMenu variant, JFrame optionsFrame)
	{
		frame = optionsFrame;
		frame.setVisible(true);
		frame.add(this);
		frame.setVisible(true);
		frame.setSize(400, 300);
		frame.setLocationRelativeTo(Driver.getInstance());
		whiteRules = variant.whiteRules;
		blackRules = variant.blackRules;
		initComponents(variant);
	}

	/**
	 * Setting up the window and rules
	 */
	public void initComponents(final CustomSetupMenu variant)
	{

		// Setting up the panel
		setLayout(new GridBagLayout());
		setSize(600, 600);
		setBorder(BorderFactory.createLoweredBevelBorder());
		GridBagConstraints c = new GridBagConstraints();

		// Objectives rules for White
		final JPanel objectiveWhiteCheckBox = new JPanel();
		objectiveWhiteCheckBox.setLayout(new GridLayout(5, 1));
		final JRadioButton wCaptureAll = new JRadioButton("Capture All", false);
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				wCaptureAll.requestFocus();
			}
		});
		wCaptureAll.setToolTipText("Press me if you want the objective to be capturing all enemy pieces");
		objectiveWhiteCheckBox.add(wCaptureAll);
		if (variant.whiteRules.theEndIsNigh().equals("captureAllPieces"))
			wCaptureAll.setSelected(true);

		final JRadioButton wCaptureAllType = new JRadioButton("Capture All of Type", false);
		wCaptureAllType.setToolTipText("Press me if you want the objective to be capturing all enemy pieces of a certain type");
		objectiveWhiteCheckBox.add(wCaptureAllType);
		if (variant.whiteRules.theEndIsNigh().equals("captureAllOfType"))
			wCaptureAllType.setSelected(true);

		final JRadioButton wProtectObj = new JRadioButton("Protect Objective", false);
		wProtectObj.setToolTipText("Press me if you want the objective to be protecting your own objective");
		objectiveWhiteCheckBox.add(wProtectObj);
		if (variant.whiteRules.theEndIsNigh().equals("classic"))
			wProtectObj.setSelected(true);

		final JRadioButton wLoseAll = new JRadioButton("Lose All Pieces", false);
		wLoseAll.setToolTipText("Press me if you want the objective to be losing all of your pieces");
		objectiveWhiteCheckBox.add(wLoseAll);
		if (variant.whiteRules.theEndIsNigh().equals("loseAllPieces"))
			wLoseAll.setSelected(true);

		final JRadioButton wCheckTimes = new JRadioButton("Check # Times", false);
		wCheckTimes
				.setToolTipText("Press me if you want the objective to be putting the other team in check a certain amount of times");
		objectiveWhiteCheckBox.add(wCheckTimes);
		if (variant.whiteRules.theEndIsNigh().equals("checkNTimes"))
			wCheckTimes.setSelected(true);

		final ButtonGroup white = new ButtonGroup();
		white.add(wCaptureAll);
		white.add(wCaptureAllType);
		white.add(wProtectObj);
		white.add(wLoseAll);
		white.add(wCheckTimes);

		// Objectives rules for Black
		final JPanel objectiveBlackCheckBox = new JPanel();
		objectiveBlackCheckBox.setLayout(new GridLayout(5, 1));
		final JRadioButton bCaptureAll = new JRadioButton("Capture All", false);
		bCaptureAll.setToolTipText("Press me if you want the objective to be capturing all enemy pieces");
		objectiveBlackCheckBox.add(bCaptureAll);
		if (variant.blackRules.theEndIsNigh().equals("captureAllPieces"))
			bCaptureAll.setSelected(true);

		final JRadioButton bCaptureAllType = new JRadioButton("Capture All of Type", false);
		bCaptureAllType.setToolTipText("Press me if you want the objective to be capturing all enemy pieces of a certain type");
		objectiveBlackCheckBox.add(bCaptureAllType);
		if (variant.blackRules.theEndIsNigh().equals("captureAllOfType"))
			bCaptureAllType.setSelected(true);

		final JRadioButton bProtectObj = new JRadioButton("Protect Objective", false);
		bProtectObj.setToolTipText("Press me if you want the objective to be protecting your own objective");
		objectiveBlackCheckBox.add(bProtectObj);
		if (variant.blackRules.theEndIsNigh().equals("classic"))
			bProtectObj.setSelected(true);

		final JRadioButton bLoseAll = new JRadioButton("Lose All Pieces", false);
		bLoseAll.setToolTipText("Press me if you want the objective to be losing all of your pieces");
		objectiveBlackCheckBox.add(bLoseAll);
		if (variant.blackRules.theEndIsNigh().equals("loseAllPieces"))
			bLoseAll.setSelected(true);

		final JRadioButton bCheckTimes = new JRadioButton("Check # Times", false);
		bCheckTimes
				.setToolTipText("Press me if you want the objective to be putting the other team in check a certain amount of times");
		objectiveBlackCheckBox.add(bCheckTimes);
		if (variant.blackRules.theEndIsNigh().equals("checkNTimes"))
			bCheckTimes.setSelected(true);

		final ButtonGroup black = new ButtonGroup();
		black.add(bCaptureAll);
		black.add(bCaptureAllType);
		black.add(bProtectObj);
		black.add(bLoseAll);
		black.add(bCheckTimes);

		// Create button and add ActionListener for going back
		final JButton back = new JButton("Cancel");
		back.setToolTipText("Press me to return to the piece creation window");
		back.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				holder.removeAll();
				frame.setVisible(false);
			}
		});

		// Button to move to save and move on
		JButton save = new JButton("Save");
		save.setToolTipText("Press me to save these objective settings");
		save.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{

				if (wProtectObj.isSelected() || bProtectObj.isSelected())
				{
					if (!(wProtectObj.isSelected() && bProtectObj.isSelected()))
					{
						int answer = JOptionPane.showConfirmDialog(null,
								"Using Protect Objective combined with another objective style is not recommended.\n"
										+ "Do you want to continue anyways?", "Continue?", JOptionPane.YES_NO_OPTION);
						if (answer != 0)
							return;
					}
				}
				if ((wCaptureAll.isSelected() && bLoseAll.isSelected()) || (bCaptureAll.isSelected() && wLoseAll.isSelected()))
				{
					JOptionPane.showMessageDialog(null, "Capture All and Lose All is not a valid combination of objectives.\n"
							+ "Please choose another combination.");
					return;
				}

				if (wCheckTimes.isSelected() || bCheckTimes.isSelected())
				{
					if (!(wCheckTimes.isSelected() && bCheckTimes.isSelected()))
					{
						int answer = JOptionPane.showConfirmDialog(null,
								"Using Check # Times combined with another objective style is not recommended.\n"
										+ "Do you want to continue anyways?", "Continue?", JOptionPane.YES_NO_OPTION);
						if (answer != 0)
							return;
					}
				}

				if (wCaptureAll.isSelected())
				{
					whiteRules.addEndOfGame(new EndOfGame("captureAllPieces", 0, "", false));
				}
				if (wCaptureAllType.isSelected())
				{
					whiteRules.addEndOfGame(new EndOfGame("captureAllOfType", 0, "Knight", false));
				}
				if (wProtectObj.isSelected())
				{
					whiteRules.addEndOfGame(new EndOfGame("classic", 0, "", false));
					needsObj = true;
				}
				if (wLoseAll.isSelected())
				{
					whiteRules.addEndOfGame(new EndOfGame("loseAllPieces", 0, "", false));
				}
				if (wCheckTimes.isSelected())
				{
					whiteRules.addEndOfGame(new EndOfGame("checkNTimes", 3, "", false));
					needsObj = true;
				}

				if (bCaptureAll.isSelected())
				{
					blackRules.addEndOfGame(new EndOfGame("captureAllPieces", 0, "", true));
				}
				if (bCaptureAllType.isSelected())
				{
					blackRules.addEndOfGame(new EndOfGame("captureAllOfType", 0, "Knight", true));
					needsObj = false;
				}
				if (bProtectObj.isSelected())
				{
					blackRules.addEndOfGame(new EndOfGame("classic", 0, "", true));
				}
				if (bLoseAll.isSelected())
				{
					blackRules.addEndOfGame(new EndOfGame("loseAllPieces", 0, "", true));
				}
				if (bCheckTimes.isSelected())
				{
					blackRules.addEndOfGame(new EndOfGame("checkNTimes", 3, "", true));
					needsObj = true;
				}

				variant.whiteRules = whiteRules;
				variant.blackRules = blackRules;
				holder.removeAll();
				frame.setVisible(false);
			}
		});

		// Setting up the window

		// Setting up the white team
		JPanel whiteTeam = new JPanel();
		whiteTeam.setBorder(BorderFactory.createTitledBorder("White Team"));
		whiteTeam.setLayout(new GridBagLayout());

		JPanel whiteObj = new JPanel();
		whiteObj.setLayout(new GridBagLayout());
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 1;
		whiteObj.add(new JLabel("<html><u> Objective </u></br></html>"), c);
		c.gridx = 0;
		c.gridy = 2;
		whiteObj.add(objectiveWhiteCheckBox, c);
		c.gridx = 0;
		c.gridy = 3;
		whiteObj.add(new JLabel(" "), c);

		c.gridx = 0;
		c.gridy = 1;
		whiteTeam.add(whiteObj, c);

		// Setting up the black team
		JPanel blackTeam = new JPanel();
		blackTeam.setBorder(BorderFactory.createTitledBorder("Black Team"));
		blackTeam.setLayout(new GridBagLayout());

		JPanel blackObj = new JPanel();
		blackObj.setLayout(new GridBagLayout());
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 1;
		blackObj.add(new JLabel("<html><u> Objective </u></br></html>"), c);
		c.gridx = 0;
		c.gridy = 2;
		blackObj.add(objectiveBlackCheckBox, c);
		c.gridx = 0;
		c.gridy = 3;
		blackObj.add(new JLabel(" "), c);

		c.gridx = 0;
		c.gridy = 1;
		blackTeam.add(blackObj, c);

		// Adding White team to window
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 0;
		add(whiteTeam, c);

		// Adding black team to the window
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 1;
		c.gridy = 0;
		add(blackTeam, c);

		// Adding Buttons to the window
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout());
		buttons.add(save);
		buttons.add(back);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		add(buttons, c);

		frame.pack();
	}
}
