package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import utility.AppConstants;

public class HelpFrame extends JFrame
{
	public HelpFrame()
	{
		initGUIComponents();
	}

	private void initGUIComponents()
	{
		setTitle(Messages.getString("HelpFrame.help")); //$NON-NLS-1$
		setSize(825, 525);
		setResizable(false);
		setLocationRelativeTo(Driver.getInstance());
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		JPanel gamePlayHelpPanel = new JPanel();
		JPanel variantMakingHelpPanel = new JPanel();
		JPanel generalHelpPanel = new JPanel();
		JPanel pieceMakingHelpPanel = new JPanel();

		JTabbedPane helpTypesTabbedPane = new JTabbedPane();
		helpTypesTabbedPane.addTab(Messages.getString("HelpFrame.generalHelp"), null, generalHelpPanel, Messages.getString("HelpFrame.clickForGeneralHelp") + AppConstants.APP_NAME); //$NON-NLS-1$ //$NON-NLS-2$
		helpTypesTabbedPane.addTab(Messages.getString("HelpFrame.gamePlayHelp"), null, gamePlayHelpPanel, Messages.getString("HelpFrame.clickForHelpPlaying")); //$NON-NLS-1$ //$NON-NLS-2$
		helpTypesTabbedPane.addTab(Messages.getString("HelpFrame.variantHelp"), null, variantMakingHelpPanel, Messages.getString("HelpFrame.clickForNewGameHelp")); //$NON-NLS-1$ //$NON-NLS-2$
		helpTypesTabbedPane.addTab(Messages.getString("HelpFrame.pieceMakingHelp"), null, pieceMakingHelpPanel, Messages.getString("HelpFrame.clickForPieceMakingHelp")); //$NON-NLS-1$ //$NON-NLS-2$
		helpTypesTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(helpTypesTabbedPane, constraints);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridy = 1;

		JLabel gamePlayHelpText = new JLabel();
		gamePlayHelpText.setText(Messages.getString("HelpFrame.gamePlayText")); //$NON-NLS-1$
		gamePlayHelpPanel.add(gamePlayHelpText);

		JLabel generalHelpText = new JLabel();
		generalHelpText.setText(Messages.getString("HelpFrame.generalHelpText")); //$NON-NLS-1$ //$NON-NLS-2$
		generalHelpPanel.add(generalHelpText);

		JLabel variantMakingHelpText = new JLabel();
		variantMakingHelpText.setText(Messages.getString("HelpFrame.variantHelpText")); //$NON-NLS-1$
		variantMakingHelpPanel.add(variantMakingHelpText);

		JLabel pieceMakingHelpText = new JLabel();
		pieceMakingHelpText.setText(Messages.getString("HelpFrame.pieceOptionsText")); //$NON-NLS-1$
		pieceMakingHelpPanel.add(pieceMakingHelpText);

		setVisible(true);
	}
	
	private static final long serialVersionUID = -3375921014569944071L;
}
