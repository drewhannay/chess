package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import utility.AppConstants;
import utility.GuiUtility;

public class HelpFrame extends JFrame
{
	public HelpFrame()
	{
		initGUIComponents();
	}

	private void initGUIComponents()
	{
		setTitle(Messages.getString("HelpFrame.help")); //$NON-NLS-1$
		// setSize(825, 525);
		setResizable(false);
		setLocationRelativeTo(Driver.getInstance());
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		GridBagConstraints constraints = new GridBagConstraints();

		mHelpPanel = new ChessPanel();
		mHelpPanel.setLayout(new GridBagLayout());

		JPanel gamePlayHelpPanel = new ChessPanel();
		JPanel variantMakingHelpPanel = new ChessPanel();
		JPanel generalHelpPanel = new ChessPanel();
		JPanel pieceMakingHelpPanel = new ChessPanel();

		JTabbedPane helpTypesTabbedPane = new JTabbedPane();
		helpTypesTabbedPane.setOpaque(false);
		helpTypesTabbedPane
				.addTab(Messages.getString("HelpFrame.generalHelp"), null, generalHelpPanel, Messages.getString("HelpFrame.clickForGeneralHelp") + AppConstants.APP_NAME); //$NON-NLS-1$ //$NON-NLS-2$
		helpTypesTabbedPane
				.addTab(Messages.getString("HelpFrame.gamePlayHelp"), null, gamePlayHelpPanel, Messages.getString("HelpFrame.clickForHelpPlaying")); //$NON-NLS-1$ //$NON-NLS-2$
		helpTypesTabbedPane
				.addTab(Messages.getString("HelpFrame.variantHelp"), null, variantMakingHelpPanel, Messages.getString("HelpFrame.clickForNewGameHelp")); //$NON-NLS-1$ //$NON-NLS-2$
		helpTypesTabbedPane
				.addTab(Messages.getString("HelpFrame.pieceMakingHelp"), null, pieceMakingHelpPanel, Messages.getString("HelpFrame.clickForPieceMakingHelp")); //$NON-NLS-1$ //$NON-NLS-2$
		helpTypesTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		mHelpPanel.add(helpTypesTabbedPane, constraints);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridy = 1;

		JLabel gamePlayHelpText = GuiUtility.createJLabel(Messages.getString("HelpFrame.gamePlayText")); //$NON-NLS-1$
		gamePlayHelpPanel.add(gamePlayHelpText);

		JLabel generalHelpText = GuiUtility.createJLabel(Messages.getString("HelpFrame.generalHelpText")); //$NON-NLS-1$
		generalHelpPanel.add(generalHelpText);

		JLabel variantMakingHelpText = GuiUtility.createJLabel(Messages.getString("HelpFrame.variantHelpText")); //$NON-NLS-1$
		variantMakingHelpPanel.add(variantMakingHelpText);

		JLabel pieceMakingHelpText = GuiUtility.createJLabel(Messages.getString("HelpFrame.pieceOptionsText")); //$NON-NLS-1$
		pieceMakingHelpPanel.add(pieceMakingHelpText);

		add(mHelpPanel);

		pack();

		setVisible(true);
	}

	private static final long serialVersionUID = -3375921014569944071L;
	private ChessPanel mHelpPanel;
}
