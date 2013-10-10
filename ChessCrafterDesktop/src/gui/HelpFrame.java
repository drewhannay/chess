package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
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
		helpTypesTabbedPane.addTab(Messages.getString("HelpFrame.newGameHelp"), null, variantMakingHelpPanel, Messages.getString("HelpFrame.clickForNewGameHelp")); //$NON-NLS-1$ //$NON-NLS-2$
		helpTypesTabbedPane.addTab(Messages.getString("HelpFrame.pieceMakingHelp"), null, pieceMakingHelpPanel, Messages.getString("HelpFrame.clickForPieceMakingHelp")); //$NON-NLS-1$ //$NON-NLS-2$
		helpTypesTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(helpTypesTabbedPane, constraints);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridy = 1;

		JTextArea gamePlayTextArea = new JTextArea();
		gamePlayTextArea.setEditable(false);
		gamePlayTextArea.setText(Messages.getString("HelpFrame.toMoveAPiece") //$NON-NLS-1$
				+ Messages.getString("HelpFrame.ifNoMovesNoHighlight") + Messages.getString("HelpFrame.forGamesWithObj") //$NON-NLS-1$ //$NON-NLS-2$
				+ Messages.getString("HelpFrame.ifObjectiveInCheck") //$NON-NLS-1$
				+ Messages.getString("HelpFrame.whilePieceInCheck") //$NON-NLS-1$
				+ Messages.getString("HelpFrame.ifWantToUndo") + Messages.getString("HelpFrame.featureUnavailableInNetwork") //$NON-NLS-1$ //$NON-NLS-2$
				+ Messages.getString("HelpFrame.optionsSpecificToGame") //$NON-NLS-1$
				+ Messages.getString("HelpFrame.saveAndQuitWill") //$NON-NLS-1$
				+ Messages.getString("HelpFrame.tabFeatureUnavailableNetwork") + Messages.getString("HelpFrame.declareDrawWill") //$NON-NLS-1$ //$NON-NLS-2$
				+ Messages.getString("HelpFrame.inNetworkRequestDraw") //$NON-NLS-1$
				+ Messages.getString("HelpFrame.onceGameCompleted") //$NON-NLS-1$
				+ Messages.getString("HelpFrame.canBeReviewedFromViewCompleted")); //$NON-NLS-1$
		gamePlayHelpPanel.add(gamePlayTextArea);

		JTextArea generalHelpText = new JTextArea();
		generalHelpText.setEditable(false);
		generalHelpText.setText(Messages.getString("HelpFrame.thanksForPlaying") + AppConstants.APP_NAME + Messages.getString("HelpFrame.exclNewlineNewline") //$NON-NLS-1$ //$NON-NLS-2$
				+ Messages.getString("HelpFrame.ourMenuIsDesigned") + AppConstants.APP_NAME + Messages.getString("HelpFrame.colonNewline") //$NON-NLS-1$ //$NON-NLS-2$
				+ Messages.getString("HelpFrame.newGameExp") //$NON-NLS-1$
				+ Messages.getString("HelpFrame.anotherPersonOrAI") //$NON-NLS-1$
				+ Messages.getString("HelpFrame.variants") //$NON-NLS-1$
				+ Messages.getString("HelpFrame.viewCompleted") //$NON-NLS-1$
				+ Messages.getString("HelpFrame.loadGame") + Messages.getString("HelpFrame.soYouCanKeepPlaying") //$NON-NLS-1$ //$NON-NLS-2$
				+ Messages.getString("HelpFrame.alsoAtAnyTime") + Messages.getString("HelpFrame.optionsAreAvailable")); //$NON-NLS-1$ //$NON-NLS-2$
		generalHelpPanel.add(generalHelpText);

		JTextArea variantMakingHelpTextArea = new JTextArea();
		variantMakingHelpTextArea.setEditable(false);
		variantMakingHelpTextArea.setText(Messages.getString("HelpFrame.onceYouChoose") //$NON-NLS-1$
				+ Messages.getString("HelpFrame.createAName") + Messages.getString("HelpFrame.setUpWhatBoardLooksLike") //$NON-NLS-1$ //$NON-NLS-2$
				+ Messages.getString("HelpFrame.createCustomPieces") + Messages.getString("HelpFrame.determineRules") //$NON-NLS-1$ //$NON-NLS-2$
				+ Messages.getString("HelpFrame.determineHowManyTurns") + Messages.getString("HelpFrame.toPlaceAPiece") //$NON-NLS-1$ //$NON-NLS-2$
				+ Messages.getString("HelpFrame.clickPieceName") //$NON-NLS-1$
				+ Messages.getString("HelpFrame.clickOnColorSquare") //$NON-NLS-1$
				+ Messages.getString("HelpFrame.dragAndRelease") + Messages.getString("HelpFrame.keepClickingOtherSquares") //$NON-NLS-1$ //$NON-NLS-2$
				+ Messages.getString("HelpFrame.toChangePiece") //$NON-NLS-1$
				+ Messages.getString("HelpFrame.toRemovePieces") + Messages.getString("HelpFrame.clearPieceSelection") //$NON-NLS-1$ //$NON-NLS-2$
				+ Messages.getString("HelpFrame.empty") //$NON-NLS-1$
				+ Messages.getString("HelpFrame.empty") //$NON-NLS-1$
				+ Messages.getString("HelpFrame.clickEmptyToChange") //$NON-NLS-1$
				+ Messages.getString("HelpFrame.toAllowPromotion") //$NON-NLS-1$
				+ Messages.getString("HelpFrame.andChoosePromote") //$NON-NLS-1$
				+ Messages.getString("HelpFrame.whenYouAreFinished") //$NON-NLS-1$
				+ Messages.getString("HelpFrame.youCanThenPlay")); //$NON-NLS-1$
		variantMakingHelpPanel.add(variantMakingHelpTextArea);

		JTextArea pieceMakingHelpTextArea = new JTextArea();
		pieceMakingHelpTextArea.setEditable(false);
		pieceMakingHelpTextArea
				.setText(Messages.getString("HelpFrame.canMakeCustomPieces") //$NON-NLS-1$
						+ Messages.getString("HelpFrame.selectPieces") //$NON-NLS-1$
						+ Messages.getString("HelpFrame.enterNameAndIcons") //$NON-NLS-1$
						+ Messages.getString("HelpFrame.empty") //$NON-NLS-1$
						+ Messages.getString("HelpFrame.empty") //$NON-NLS-1$
						+ Messages.getString("HelpFrame.empty") //$NON-NLS-1$
						+ Messages.getString("HelpFrame.empty") //$NON-NLS-1$
						+ Messages.getString("HelpFrame.empty") //$NON-NLS-1$
						+ Messages.getString("HelpFrame.ifWantJump") //$NON-NLS-1$
						+ Messages.getString("HelpFrame.ifWantKnightLike") //$NON-NLS-1$
						+ "\t\t\t\t\t\t 3 x 2\n\n" + Messages.getString("HelpFrame.enterMovements") //$NON-NLS-1$ //$NON-NLS-2$
						+ Messages.getString("HelpFrame.asAnExample") //$NON-NLS-1$
						+ Messages.getString("HelpFrame.enteringOneCombo") //$NON-NLS-1$
						+ Messages.getString("HelpFrame.toMoveBoth") //$NON-NLS-1$
						+ Messages.getString("HelpFrame.whenFinished") //$NON-NLS-1$
						+ Messages.getString("HelpFrame.thisPieceWill") //$NON-NLS-1$
						+ Messages.getString("HelpFrame.repeateProcess") //$NON-NLS-1$
						+ Messages.getString("HelpFrame.empty")); //$NON-NLS-1$
		pieceMakingHelpPanel.add(pieceMakingHelpTextArea);

		setVisible(true);
	}
	
	private static final long serialVersionUID = -3375921014569944071L;
}
