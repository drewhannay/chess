
package gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import logic.GameBuilder;
import logic.PieceBuilder;
import models.Board;
import utility.GuiUtility;

public class RuleMakerPanel extends ChessPanel
{
	public RuleMakerPanel(VariantCreationPanel variantCreationPanel, JFrame optionsFrame)
	{
		mFrame = optionsFrame;
		mFrame.setVisible(true);
		mFrame.add(this);
		mFrame.setVisible(true);
		mFrame.setSize(600, 500);
		mFrame.setLocationRelativeTo(Driver.getInstance());
		mBuilder = variantCreationPanel.getBuilder();
		mWhiteRules = variantCreationPanel.mWhiteRules;
		mBlackRules = variantCreationPanel.mBlackRules;
		initGUIComponents(variantCreationPanel);
	}

	private void initGUIComponents(final VariantCreationPanel variantCreationPanel)
	{
		revalidate();
		repaint();
		setLayout(new GridBagLayout());
		setSize(600, 600);
		setBorder(BorderFactory.createLoweredBevelBorder());
		GridBagConstraints constraints = new GridBagConstraints();

		final Board[] boards = mBuilder.getBoards();

		final JPanel whiteExtrasPanel = new JPanel();
		whiteExtrasPanel.setLayout(new GridBagLayout());
		whiteExtrasPanel.setOpaque(false);
		final JTextField whiteNumberOfChecksField = new JTextField(5);
		whiteNumberOfChecksField.setText(variantCreationPanel.mBlackRules.getEndOfGame().getMaxNumberOfChecks() + "");
		whiteNumberOfChecksField.setEnabled(false);
		Object[] allPieces = PieceBuilder.getSet().toArray();
		final JComboBox whitePiecesList = new JComboBox(allPieces);

		int whiteObjectiveIndex = 0;
		int blackObjectiveIndex = 0;
		for (int i = 0; i < allPieces.length; i++)
		{
			if (allPieces[i].equals(mWhiteRules.getObjectiveName()))
				whiteObjectiveIndex = i;
			if (allPieces[i].equals(mBlackRules.getObjectiveName()))
				blackObjectiveIndex = i;
		}

		JLabel whiteNumberOfChecksLabel = GuiUtility.createJLabel(Messages.getString("RuleMakerPanel.howManyTimesForCheck")); //$NON-NLS-1$
		JLabel whiteObjectivePieceLabel = GuiUtility.createJLabel(Messages.getString("RuleMakerPanel.whichIsObjective")); //$NON-NLS-1$

		whitePiecesList.setSelectedIndex(whiteObjectiveIndex);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets(0, 0, 0, 10);
		whiteExtrasPanel.add(whiteObjectivePieceLabel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.insets = new Insets(0, 0, 5, 0);
		whiteExtrasPanel.add(whitePiecesList, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.insets = new Insets(0, 0, 0, 10);
		whiteExtrasPanel.add(whiteNumberOfChecksLabel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.insets = new Insets(0, 0, 10, 0);
		whiteExtrasPanel.add(whiteNumberOfChecksField, constraints);

		final JPanel blackExtrasPanel = new JPanel();
		blackExtrasPanel.setLayout(new GridBagLayout());
		blackExtrasPanel.setOpaque(false);

		final JTextField blackNumberOfChecksField = new JTextField(5);
		blackNumberOfChecksField.setText(variantCreationPanel.mBlackRules.getEndOfGame().getMaxNumberOfChecks() + "");
		blackNumberOfChecksField.setEnabled(false);
		final JComboBox blackPiecesList = new JComboBox(allPieces);

		JLabel blackNumberOfChecksLabel = GuiUtility.createJLabel(Messages.getString("RuleMakerPanel.howManyTimesForCheck")); //$NON-NLS-1$
		JLabel blackObjectivePieceLabel = GuiUtility.createJLabel(Messages.getString("RuleMakerPanel.whichIsObjective")); //$NON-NLS-1$

		blackPiecesList.setSelectedIndex(blackObjectiveIndex);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets(0, 0, 0, 10);
		blackExtrasPanel.add(blackObjectivePieceLabel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.insets = new Insets(0, 0, 5, 0);
		blackExtrasPanel.add(blackPiecesList, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.insets = new Insets(0, 0, 0, 10);
		blackExtrasPanel.add(blackNumberOfChecksLabel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.insets = new Insets(0, 0, 10, 0);
		blackExtrasPanel.add(blackNumberOfChecksField, constraints);

		// capture mandatory check boxes
		final JPanel whiteLegalDestinationPanel = new JPanel();
		whiteLegalDestinationPanel.setLayout(new GridBagLayout());
		whiteLegalDestinationPanel.setOpaque(false);
		final JCheckBox whiteCaptureMandatoryCheckBox = new JCheckBox(Messages.getString("RuleMakerPanel.captureMandatory")); //$NON-NLS-1$
		whiteCaptureMandatoryCheckBox.setToolTipText(Messages.getString("RuleMakerPanel.capturingMustBe")); //$NON-NLS-1$
		whiteCaptureMandatoryCheckBox.setSelected(variantCreationPanel.mWhiteRules.getCaptureMandatory());
		whiteCaptureMandatoryCheckBox.setOpaque(false);
		whiteCaptureMandatoryCheckBox.setForeground(Color.white);
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(10, 0, 5, 0);
		whiteLegalDestinationPanel
				.add(GuiUtility.createJLabel(Messages.getString("RuleMakerPanel.legalDestinationHTML")), constraints); //$NON-NLS-1
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridy = 1;
		constraints.insets = new Insets(0, 0, 0, 0);
		whiteLegalDestinationPanel.add(whiteCaptureMandatoryCheckBox, constraints);

		final JPanel blackLegalDestinationPanel = new JPanel();
		blackLegalDestinationPanel.setLayout(new GridBagLayout());
		blackLegalDestinationPanel.setOpaque(false);
		final JCheckBox blackCaptureMandatoryCheckBox = new JCheckBox(Messages.getString("RuleMakerPanel.captureMandatory")); //$NON-NLS-1$
		blackCaptureMandatoryCheckBox.setToolTipText(Messages.getString("RuleMakerPanel.capturingMustBe")); //$NON-NLS-1$
		blackCaptureMandatoryCheckBox.setSelected(variantCreationPanel.mBlackRules.getCaptureMandatory());
		blackCaptureMandatoryCheckBox.setOpaque(false);
		blackCaptureMandatoryCheckBox.setForeground(Color.white);
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(10, 0, 5, 0);
		blackLegalDestinationPanel
				.add(GuiUtility.createJLabel(Messages.getString("RuleMakerPanel.legalDestinationHTML")), constraints); //$NON-NLS-1
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridy = 1;
		constraints.insets = new Insets(0, 0, 0, 0);
		blackLegalDestinationPanel.add(blackCaptureMandatoryCheckBox, constraints);

		// can't move objective check boxes
		final JCheckBox whiteNoMoveObjectiveCheckBox = new JCheckBox(Messages.getString("RuleMakerPanel.cantMoveObj")); //$NON-NLS-1$
		whiteNoMoveObjectiveCheckBox.setToolTipText(Messages.getString("RuleMakerPanel.movingObjIllegal")); //$NON-NLS-1$
		whiteNoMoveObjectiveCheckBox.setOpaque(false);
		whiteNoMoveObjectiveCheckBox.setForeground(Color.white);
		constraints.gridy = 2;
		whiteLegalDestinationPanel.add(whiteNoMoveObjectiveCheckBox, constraints);

		final JCheckBox blackNoMoveObjectiveCheckBox = new JCheckBox(Messages.getString("RuleMakerPanel.cantMoveObj")); //$NON-NLS-1$
		blackNoMoveObjectiveCheckBox.setToolTipText(Messages.getString("RuleMakerPanel.movingObjIllegal")); //$NON-NLS-1$
		blackNoMoveObjectiveCheckBox.setOpaque(false);
		blackNoMoveObjectiveCheckBox.setForeground(Color.white);
		constraints.gridy = 2;
		blackLegalDestinationPanel.add(blackNoMoveObjectiveCheckBox, constraints);

		final JPanel whiteAfterCapturePanel = new JPanel();
		whiteAfterCapturePanel.setLayout(new GridBagLayout());
		whiteAfterCapturePanel.setOpaque(false);
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(10, 0, 10, 0);
		whiteAfterCapturePanel.add(GuiUtility.createJLabel(Messages.getString("RuleMakerPanel.afterCapturingHTML")), constraints); //$NON-NLS-1$
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(0, 0, 0, 0);

		final ButtonGroup whiteAfterOptions = new ButtonGroup();
		final JRadioButton whiteChangeColorRadioButton = new JRadioButton(Messages.getString("RuleMakerPanel.captererChangesColor")); //$NON-NLS-1$
		whiteChangeColorRadioButton.setToolTipText(Messages.getString("RuleMakerPanel.capturingPieceChanges")); //$NON-NLS-1$
		whiteChangeColorRadioButton.setOpaque(false);
		whiteChangeColorRadioButton.setForeground(Color.white);
		whiteAfterOptions.add(whiteChangeColorRadioButton);
		constraints.gridy = 1;
		whiteAfterCapturePanel.add(whiteChangeColorRadioButton, constraints);

		final JRadioButton whitePieceReturnRadioButton = new JRadioButton(Messages.getString("RuleMakerPanel.capturedReturns")); //$NON-NLS-1$
		whitePieceReturnRadioButton.setToolTipText(Messages.getString("RuleMakerPanel.capturedReturnToTheirStart")); //$NON-NLS-1$
		whitePieceReturnRadioButton.setSelected(mWhiteRules.getCapturedReturnToStart());
		whitePieceReturnRadioButton.setOpaque(false);
		whitePieceReturnRadioButton.setForeground(Color.white);
		whiteAfterOptions.add(whitePieceReturnRadioButton);
		constraints.gridy = 2;
		whiteAfterCapturePanel.add(whitePieceReturnRadioButton, constraints);

		final JRadioButton whiteDropPiecesRadioButton = new JRadioButton(Messages.getString("RuleMakerPanel.capturedPiecesDrop")); //$NON-NLS-1$
		whiteDropPiecesRadioButton.setOpaque(false);
		whiteDropPiecesRadioButton.setForeground(Color.white);
		whiteDropPiecesRadioButton.setToolTipText(Messages.getString("RuleMakerPanel.capturedArePlaced")); //$NON-NLS-1$
		whiteDropPiecesRadioButton.setSelected(mWhiteRules.getPiecesDrop());
		whiteAfterOptions.add(whiteDropPiecesRadioButton);
		constraints.gridy = 3;
		whiteAfterCapturePanel.add(whiteDropPiecesRadioButton, constraints);

		final JRadioButton whiteCapturedColorAndDropRadioButton = new JRadioButton(
				Messages.getString("RuleMakerPanel.capturedChangesAnd")); //$NON-NLS-1$
		whiteCapturedColorAndDropRadioButton.setToolTipText(Messages.getString("RuleMakerPanel.capturedChangesTeams")); //$NON-NLS-1$
		whiteCapturedColorAndDropRadioButton.setSelected(mWhiteRules.getPiecesDropAndSwitch());
		whiteCapturedColorAndDropRadioButton.setOpaque(false);
		whiteCapturedColorAndDropRadioButton.setForeground(Color.white);
		whiteAfterOptions.add(whiteCapturedColorAndDropRadioButton);
		constraints.gridy = 4;
		whiteAfterCapturePanel.add(whiteCapturedColorAndDropRadioButton, constraints);

		final JRadioButton whiteNoAfterMoveRadioButton = new JRadioButton(Messages.getString("RuleMakerPanel.doNothing")); //$NON-NLS-1$
		whiteNoAfterMoveRadioButton.setSelected(mWhiteRules.getNoAfterMovesSelected());
		whiteNoAfterMoveRadioButton.setOpaque(false);
		whiteNoAfterMoveRadioButton.setForeground(Color.white);
		whiteAfterOptions.add(whiteNoAfterMoveRadioButton);
		constraints.gridy = 5;
		whiteAfterCapturePanel.add(whiteNoAfterMoveRadioButton, constraints);

		final JPanel blackAfterPanel = new JPanel();
		blackAfterPanel.setOpaque(false);
		blackAfterPanel.setLayout(new GridBagLayout());
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(10, 0, 10, 0);
		blackAfterPanel.add(GuiUtility.createJLabel(Messages.getString("RuleMakerPanel.afterCapturingHTML")), constraints); //$NON-NLS-1$
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.anchor = GridBagConstraints.WEST;

		final ButtonGroup blackAfterOptions = new ButtonGroup();

		final JRadioButton blackChangeColorRadioButton = new JRadioButton(Messages.getString("RuleMakerPanel.capturerChanges")); //$NON-NLS-1$
		blackChangeColorRadioButton.setToolTipText(Messages.getString("RuleMakerPanel.capturingPieceChanges")); //$NON-NLS-1$
		blackChangeColorRadioButton.setOpaque(false);
		blackChangeColorRadioButton.setForeground(Color.white);
		blackAfterOptions.add(blackChangeColorRadioButton);
		constraints.gridy = 1;
		blackAfterPanel.add(blackChangeColorRadioButton, constraints);

		final JRadioButton blackPieceReturnRadioButton = new JRadioButton(Messages.getString("RuleMakerPanel.capturedReturns")); //$NON-NLS-1$
		blackPieceReturnRadioButton.setToolTipText(Messages.getString("RuleMakerPanel.capturedReturnToTheirStart")); //$NON-NLS-1$
		blackPieceReturnRadioButton.setSelected(mBlackRules.getCapturedReturnToStart());
		blackPieceReturnRadioButton.setOpaque(false);
		blackPieceReturnRadioButton.setForeground(Color.white);
		constraints.gridy = 2;
		blackAfterPanel.add(blackPieceReturnRadioButton, constraints);
		blackAfterOptions.add(blackPieceReturnRadioButton);

		final JRadioButton blackDropPiecesRadioButton = new JRadioButton(Messages.getString("RuleMakerPanel.capturedPiecesDrop")); //$NON-NLS-1$
		blackDropPiecesRadioButton.setToolTipText(Messages.getString("RuleMakerPanel.capturedArePlaced")); //$NON-NLS-1$
		blackDropPiecesRadioButton.setOpaque(false);
		blackDropPiecesRadioButton.setForeground(Color.white);
		blackDropPiecesRadioButton.setSelected(mBlackRules.getPiecesDrop());
		constraints.gridy = 3;
		blackAfterPanel.add(blackDropPiecesRadioButton, constraints);
		blackAfterOptions.add(blackDropPiecesRadioButton);

		final JRadioButton blackCapturedColorAndDropRadioButton = new JRadioButton(
				Messages.getString("RuleMakerPanel.capturedChangesAnd")); //$NON-NLS-1$
		blackCapturedColorAndDropRadioButton.setToolTipText(Messages.getString("RuleMakerPanel.capturedChangesTeams")); //$NON-NLS-1$
		blackCapturedColorAndDropRadioButton.setSelected(mBlackRules.getPiecesDropAndSwitch());
		blackCapturedColorAndDropRadioButton.setOpaque(false);
		blackCapturedColorAndDropRadioButton.setForeground(Color.white);
		constraints.gridy = 4;
		blackAfterPanel.add(blackCapturedColorAndDropRadioButton, constraints);
		blackAfterOptions.add(blackCapturedColorAndDropRadioButton);

		final JRadioButton blackNoAfterMoveRadioButton = new JRadioButton(Messages.getString("RuleMakerPanel.doNothing")); //$NON-NLS-1$
		blackNoAfterMoveRadioButton.setSelected(mBlackRules.getNoAfterMovesSelected());
		blackNoAfterMoveRadioButton.setOpaque(false);
		blackNoAfterMoveRadioButton.setForeground(Color.white);
		blackAfterOptions.add(blackNoAfterMoveRadioButton);
		constraints.gridy = 5;
		blackAfterPanel.add(blackNoAfterMoveRadioButton, constraints);

		final JPanel specialRulesPanel = new JPanel();
		specialRulesPanel.setLayout(new GridBagLayout());
		specialRulesPanel.setOpaque(false);
		specialRulesPanel.setBorder(GuiUtility.createBorder(Messages.getString("RuleMakerPanel.specialRules"))); //$NON-NLS-1$

		final JCheckBox atomicChessCheckBox = new JCheckBox(Messages.getString("RuleMakerPanel.atomicChess")); //$NON-NLS-1$
		atomicChessCheckBox.setToolTipText(Messages.getString("RuleMakerPanel.captureRemovesBoth")); //$NON-NLS-1$
		atomicChessCheckBox.setEnabled(mWhiteRules.isAtomic() || mWhiteRules.getNoAfterMovesSelected());
		atomicChessCheckBox.setSelected(mWhiteRules.isAtomic());
		atomicChessCheckBox.setOpaque(false);
		atomicChessCheckBox.setForeground(Color.white);
		constraints.gridy = 0;
		specialRulesPanel.add(atomicChessCheckBox, constraints);

		final JCheckBox switchBoardsCheckBox = new JCheckBox(Messages.getString("RuleMakerPanel.moveToOtherBoard")); //$NON-NLS-1$
		switchBoardsCheckBox.setToolTipText(Messages.getString("RuleMakerPanel.eachPiecesMoves")); //$NON-NLS-1$
		switchBoardsCheckBox.setEnabled(mWhiteRules.switchBoards());
		switchBoardsCheckBox.setOpaque(false);
		switchBoardsCheckBox.setForeground(Color.white);
		constraints.gridy = 1;
		specialRulesPanel.add(switchBoardsCheckBox, constraints);

		final JPanel objectiveWhitePanel = new JPanel();
		objectiveWhitePanel.setLayout(new GridBagLayout());
		objectiveWhitePanel.setOpaque(false);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(0, 0, 5, 0);
		objectiveWhitePanel.add(GuiUtility.createJLabel(Messages.getString("RuleMakerPanel.possibleObjectives")), constraints); //$NON-NLS-1$
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.anchor = GridBagConstraints.WEST;

		final JRadioButton whiteProtectObjectiveButton = new JRadioButton(Messages.getString("RuleMakerPanel.protectObjective"), false); //$NON-NLS-1$
		whiteProtectObjectiveButton.setToolTipText(Messages.getString("RuleMakerPanel.pressForProtectObjective")); //$NON-NLS-1$
		whiteProtectObjectiveButton.setOpaque(false);
		whiteProtectObjectiveButton.setForeground(Color.white);
		constraints.gridy = 1;
		objectiveWhitePanel.add(whiteProtectObjectiveButton, constraints);
		if (variantCreationPanel.mWhiteRules.getEndOfGame() == EndOfGame.CLASSIC)
		{
			whiteDropPiecesRadioButton.setEnabled(true);
			whiteChangeColorRadioButton.setEnabled(false);
			whiteNoMoveObjectiveCheckBox.setEnabled(true);
			whitePiecesList.setEnabled(true);
			whiteNumberOfChecksField.setEnabled(false);
			whiteProtectObjectiveButton.setSelected(true);
		}
		whiteProtectObjectiveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				whiteDropPiecesRadioButton.setEnabled(true);
				whiteChangeColorRadioButton.setEnabled(false);
				whiteNoMoveObjectiveCheckBox.setEnabled(true);
				whitePiecesList.setEnabled(true);
				whiteNumberOfChecksField.setEnabled(false);
			}
		});

		final JRadioButton whiteCaptureAllButton = new JRadioButton(Messages.getString("RuleMakerPanel.captureAll"), false); //$NON-NLS-1$
		GuiUtility.requestFocus(whiteCaptureAllButton);
		whiteCaptureAllButton.setOpaque(false);
		whiteCaptureAllButton.setForeground(Color.white);
		whiteCaptureAllButton.setToolTipText(Messages.getString("RuleMakerPanel.pressForCaptureAll")); //$NON-NLS-1$
		constraints.gridy = 2;
		objectiveWhitePanel.add(whiteCaptureAllButton, constraints);
		if (variantCreationPanel.mWhiteRules.getEndOfGame() == EndOfGame.CAPTURE_ALL_PIECES)
		{
			whiteDropPiecesRadioButton.setEnabled(true);
			whiteChangeColorRadioButton.setEnabled(true);
			whiteNoMoveObjectiveCheckBox.setEnabled(false);
			whitePiecesList.setEnabled(false);
			whiteNumberOfChecksField.setEnabled(false);
			whiteCaptureAllButton.setSelected(true);
		}
		whiteCaptureAllButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				whiteDropPiecesRadioButton.setEnabled(true);
				whiteChangeColorRadioButton.setEnabled(true);
				whiteNoMoveObjectiveCheckBox.setEnabled(false);
				whitePiecesList.setEnabled(false);
				whiteNumberOfChecksField.setEnabled(false);
			}
		});

		final JRadioButton whiteCaptureAllTypeButton = new JRadioButton(Messages.getString("RuleMakerPanel.captureAllOfType"), false); //$NON-NLS-1$
		whiteCaptureAllTypeButton.setToolTipText(Messages.getString("RuleMakerPanel.pressForCaptureAllOfType")); //$NON-NLS-1$
		whiteCaptureAllTypeButton.setOpaque(false);
		whiteCaptureAllTypeButton.setForeground(Color.white);
		constraints.gridy = 3;
		objectiveWhitePanel.add(whiteCaptureAllTypeButton, constraints);
		if (variantCreationPanel.mWhiteRules.getEndOfGame() == EndOfGame.CAPTURE_ALL_OF_TYPE)
		{
			whiteDropPiecesRadioButton.setEnabled(false);
			whiteChangeColorRadioButton.setEnabled(true);
			whiteNoMoveObjectiveCheckBox.setEnabled(false);
			whitePiecesList.setEnabled(true);
			whiteNumberOfChecksField.setEnabled(false);
			whiteCaptureAllTypeButton.setSelected(true);
		}
		whiteCaptureAllTypeButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				whiteDropPiecesRadioButton.setEnabled(false);
				whiteChangeColorRadioButton.setEnabled(true);
				whiteNoMoveObjectiveCheckBox.setEnabled(false);
				whitePiecesList.setEnabled(true);
				whiteNumberOfChecksField.setEnabled(false);
			}
		});

		final JRadioButton whiteLoseAllButton = new JRadioButton(Messages.getString("RuleMakerPanel.loseAllPieces"), false); //$NON-NLS-1$
		whiteLoseAllButton.setToolTipText(Messages.getString("RuleMakerPanel.pressForLoseAllPieces")); //$NON-NLS-1$
		whiteLoseAllButton.setOpaque(false);
		whiteLoseAllButton.setForeground(Color.white);
		constraints.gridy = 4;
		objectiveWhitePanel.add(whiteLoseAllButton, constraints);
		if (variantCreationPanel.mWhiteRules.getEndOfGame() == EndOfGame.LOSE_ALL_PIECES)
		{
			whiteDropPiecesRadioButton.setEnabled(false);
			whiteChangeColorRadioButton.setEnabled(true);
			whiteNoMoveObjectiveCheckBox.setEnabled(false);
			whitePiecesList.setEnabled(false);
			whiteNumberOfChecksField.setEnabled(false);
			whiteLoseAllButton.setSelected(true);
		}
		whiteLoseAllButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				whiteDropPiecesRadioButton.setEnabled(false);
				whiteChangeColorRadioButton.setEnabled(true);
				whiteNoMoveObjectiveCheckBox.setEnabled(false);
				whitePiecesList.setEnabled(false);
				whiteNumberOfChecksField.setEnabled(false);
			}
		});

		final JRadioButton whiteCheckTimesButton = new JRadioButton(Messages.getString("RuleMakerPanel.checkNumTimes"), false); //$NON-NLS-1$
		whiteCheckTimesButton.setToolTipText(Messages.getString("RuleMakerPanel.pressForCheckNumTimes")); //$NON-NLS-1$
		whiteCheckTimesButton.setOpaque(false);
		whiteCheckTimesButton.setForeground(Color.white);
		constraints.gridy = 5;
		objectiveWhitePanel.add(whiteCheckTimesButton, constraints);
		if (variantCreationPanel.mWhiteRules.getEndOfGame() == EndOfGame.CHECK_N_TIMES)
		{
			whiteDropPiecesRadioButton.setEnabled(true);
			whiteChangeColorRadioButton.setEnabled(true);
			whiteNoMoveObjectiveCheckBox.setEnabled(true);
			whitePiecesList.setEnabled(true);
			whiteNumberOfChecksField.setEnabled(true);
			whiteCheckTimesButton.setSelected(true);
		}
		whiteCheckTimesButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				whiteDropPiecesRadioButton.setEnabled(true);
				whiteChangeColorRadioButton.setEnabled(true);
				whiteNoMoveObjectiveCheckBox.setEnabled(true);
				whitePiecesList.setEnabled(true);
				whiteNumberOfChecksField.setEnabled(true);
			}
		});

		final ButtonGroup whiteButtonGroup = new ButtonGroup();
		whiteButtonGroup.add(whiteCaptureAllButton);
		whiteButtonGroup.add(whiteCaptureAllTypeButton);
		whiteButtonGroup.add(whiteProtectObjectiveButton);
		whiteButtonGroup.add(whiteLoseAllButton);
		whiteButtonGroup.add(whiteCheckTimesButton);

		final JPanel objectiveBlackPanel = new JPanel();
		objectiveBlackPanel.setLayout(new GridBagLayout());
		objectiveBlackPanel.setOpaque(false);
		constraints.gridy = 0;
		constraints.gridx = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(0, 0, 5, 0);
		objectiveBlackPanel.add(GuiUtility.createJLabel(Messages.getString("RuleMakerPanel.possibleObjectives")), constraints); //$NON-NLS-1$
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(0, 0, 0, 0);

		final JRadioButton blackProtectObjectiveButton = new JRadioButton(Messages.getString("RuleMakerPanel.protectObjective"), false); //$NON-NLS-1$
		blackProtectObjectiveButton.setToolTipText(Messages.getString("RuleMakerPanel.pressForProtect")); //$NON-NLS-1$
		blackProtectObjectiveButton.setOpaque(false);
		blackProtectObjectiveButton.setForeground(Color.white);
		constraints.gridy = 1;
		objectiveBlackPanel.add(blackProtectObjectiveButton, constraints);
		if (variantCreationPanel.mBlackRules.getEndOfGame() == EndOfGame.CLASSIC)
		{
			blackDropPiecesRadioButton.setEnabled(true);
			blackChangeColorRadioButton.setEnabled(false);
			blackNoMoveObjectiveCheckBox.setEnabled(true);
			blackPiecesList.setEnabled(true);
			blackNumberOfChecksField.setEnabled(false);
			blackProtectObjectiveButton.setSelected(true);
		}
		blackProtectObjectiveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				blackDropPiecesRadioButton.setEnabled(true);
				blackChangeColorRadioButton.setEnabled(false);
				blackNoMoveObjectiveCheckBox.setEnabled(true);
				blackPiecesList.setEnabled(true);
				blackNumberOfChecksField.setEnabled(false);
			}
		});

		final JRadioButton blackCaptureAllButton = new JRadioButton(Messages.getString("RuleMakerPanel.captureAll"), false); //$NON-NLS-1$
		blackCaptureAllButton.setToolTipText(Messages.getString("RuleMakerPanel.pressForCaptureAll")); //$NON-NLS-1$
		blackCaptureAllButton.setOpaque(false);
		blackCaptureAllButton.setForeground(Color.white);
		constraints.gridy = 2;
		objectiveBlackPanel.add(blackCaptureAllButton, constraints);
		if (variantCreationPanel.mBlackRules.getEndOfGame() == EndOfGame.CAPTURE_ALL_PIECES)
		{
			blackDropPiecesRadioButton.setEnabled(true);
			blackChangeColorRadioButton.setEnabled(true);
			blackNoMoveObjectiveCheckBox.setEnabled(false);
			blackPiecesList.setEnabled(false);
			blackNumberOfChecksField.setEnabled(false);
			blackCaptureAllButton.setSelected(true);
		}
		blackCaptureAllButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				blackDropPiecesRadioButton.setEnabled(true);
				blackChangeColorRadioButton.setEnabled(true);
				blackNoMoveObjectiveCheckBox.setEnabled(false);
				blackPiecesList.setEnabled(false);
				blackNumberOfChecksField.setEnabled(false);
			}
		});

		final JRadioButton blackCaptureAllTypeButton = new JRadioButton(Messages.getString("RuleMakerPanel.captureAllOfType"), false); //$NON-NLS-1$
		blackCaptureAllTypeButton.setToolTipText(Messages.getString("RuleMakerPanel.pressForCaptureAllOfType")); //$NON-NLS-1$
		blackCaptureAllTypeButton.setOpaque(false);
		blackCaptureAllTypeButton.setForeground(Color.white);
		constraints.gridy = 3;
		objectiveBlackPanel.add(blackCaptureAllTypeButton, constraints);
		if (variantCreationPanel.mBlackRules.getEndOfGame() == EndOfGame.CAPTURE_ALL_OF_TYPE)
		{
			blackDropPiecesRadioButton.setEnabled(false);
			blackChangeColorRadioButton.setEnabled(true);
			blackNoMoveObjectiveCheckBox.setEnabled(false);
			blackPiecesList.setEnabled(true);
			blackNumberOfChecksField.setEnabled(false);
			blackCaptureAllTypeButton.setSelected(true);
		}
		blackCaptureAllTypeButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				blackDropPiecesRadioButton.setEnabled(false);
				blackChangeColorRadioButton.setEnabled(true);
				blackNoMoveObjectiveCheckBox.setEnabled(false);
				blackPiecesList.setEnabled(true);
				blackNumberOfChecksField.setEnabled(false);
			}
		});

		final JRadioButton blackLoseAllButton = new JRadioButton(Messages.getString("RuleMakerPanel.loseAllPieces"), false); //$NON-NLS-1$
		blackLoseAllButton.setToolTipText(Messages.getString("RuleMakerPanel.pressForLoseAllPieces")); //$NON-NLS-1$
		blackLoseAllButton.setOpaque(false);
		blackLoseAllButton.setForeground(Color.white);
		constraints.gridy = 4;
		objectiveBlackPanel.add(blackLoseAllButton, constraints);
		if (variantCreationPanel.mBlackRules.getEndOfGame() == EndOfGame.LOSE_ALL_PIECES)
		{
			blackDropPiecesRadioButton.setEnabled(false);
			blackChangeColorRadioButton.setEnabled(true);
			blackNoMoveObjectiveCheckBox.setEnabled(false);
			blackPiecesList.setEnabled(false);
			blackNumberOfChecksField.setEnabled(false);
			blackLoseAllButton.setSelected(true);
		}
		blackLoseAllButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				blackDropPiecesRadioButton.setEnabled(false);
				blackChangeColorRadioButton.setEnabled(true);
				blackNoMoveObjectiveCheckBox.setEnabled(false);
				blackPiecesList.setEnabled(false);
				blackNumberOfChecksField.setEnabled(false);
			}
		});

		final JRadioButton blackCheckTimesButton = new JRadioButton(Messages.getString("RuleMakerPanel.checkNumTimes"), false); //$NON-NLS-1$
		blackCheckTimesButton.setToolTipText(Messages.getString("RuleMakerPanel.pressForCheckNumTimes")); //$NON-NLS-1$
		blackCheckTimesButton.setOpaque(false);
		blackCheckTimesButton.setForeground(Color.white);
		constraints.gridy = 5;
		objectiveBlackPanel.add(blackCheckTimesButton, constraints);
		if (variantCreationPanel.mBlackRules.getEndOfGame() == EndOfGame.CHECK_N_TIMES)
		{
			blackDropPiecesRadioButton.setEnabled(true);
			blackChangeColorRadioButton.setEnabled(true);
			blackNoMoveObjectiveCheckBox.setEnabled(true);
			blackPiecesList.setEnabled(true);
			blackNumberOfChecksField.setEnabled(true);
			blackCheckTimesButton.setSelected(true);
		}
		blackCheckTimesButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				blackDropPiecesRadioButton.setEnabled(true);
				blackChangeColorRadioButton.setEnabled(true);
				blackNoMoveObjectiveCheckBox.setEnabled(true);
				blackPiecesList.setEnabled(true);
				blackNumberOfChecksField.setEnabled(true);
			}
		});

		final ButtonGroup blackButtonGroup = new ButtonGroup();
		blackButtonGroup.add(blackCaptureAllButton);
		blackButtonGroup.add(blackCaptureAllTypeButton);
		blackButtonGroup.add(blackProtectObjectiveButton);
		blackButtonGroup.add(blackLoseAllButton);
		blackButtonGroup.add(blackCheckTimesButton);

		final JButton cancelButton = new JButton(Messages.getString("RuleMakerPanel.cancel")); //$NON-NLS-1$
		cancelButton.setToolTipText(Messages.getString("RuleMakerPanel.pressToReturnToVariant")); //$NON-NLS-1$
		GuiUtility.setupVariantCancelButton(cancelButton, this, mFrame);

		JButton saveButton = new JButton(Messages.getString("RuleMakerPanel.save")); //$NON-NLS-1$
		saveButton.setToolTipText(Messages.getString("RuleMakerPanel.pressToSave")); //$NON-NLS-1$
		saveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				mWhiteRules.clearCropLegalDests();
				mBlackRules.clearCropLegalDests();
				mWhiteRules.clearAfterMoves();
				mBlackRules.clearAfterMoves();

				mWhiteRules.addAdjustTeamDestinations(whiteCaptureMandatoryCheckBox.isSelected() ? AdjustTeamLegalDestinations.MUST_CAPTURE
						: AdjustTeamLegalDestinations.CLASSIC);
				mBlackRules.addAdjustTeamDestinations(blackCaptureMandatoryCheckBox.isSelected() ? AdjustTeamLegalDestinations.MUST_CAPTURE
						: AdjustTeamLegalDestinations.CLASSIC);

				mWhiteRules.addCropLegalDests(whiteNoMoveObjectiveCheckBox.isSelected() ? CropLegalDestinations.STATIONARY_OBJECTIVE
						: CropLegalDestinations.CLASSIC);

				mBlackRules.addCropLegalDests(blackNoMoveObjectiveCheckBox.isSelected() ? CropLegalDestinations.STATIONARY_OBJECTIVE
						: CropLegalDestinations.CLASSIC);

				if (whiteProtectObjectiveButton.isSelected() || blackProtectObjectiveButton.isSelected())
				{
					if (!(whiteProtectObjectiveButton.isSelected() && blackProtectObjectiveButton.isSelected()))
					{
						if (JOptionPane.showConfirmDialog(
								Driver.getInstance(),
								Messages.getString("RuleMakerPanel.usingProtectNotRecommended") //$NON-NLS-1$
										+
										Messages.getString("RuleMakerPanel.continueAnyway"), Messages.getString("RuleMakerPanel.continueQ"), JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) //$NON-NLS-1$ //$NON-NLS-2$
							return;
					}
				}

				else if ((whiteCaptureAllButton.isSelected() && blackLoseAllButton.isSelected())
						|| (blackCaptureAllButton.isSelected() && whiteLoseAllButton.isSelected()))
				{
					JOptionPane.showMessageDialog(Driver.getInstance(), Messages.getString("RuleMakerPanel.captureAllAndLoseAll") //$NON-NLS-1$
							+
							Messages.getString("RuleMakerPanel.chooseAnotherCombo")); //$NON-NLS-1$
					return;
				}

				else if (whiteCheckTimesButton.isSelected() || blackCheckTimesButton.isSelected())
				{
					if (!(whiteCheckTimesButton.isSelected() && blackCheckTimesButton.isSelected()))
					{
						if (JOptionPane.showConfirmDialog(
								Driver.getInstance(),
								Messages.getString("RuleMakerPanel.checkNumTimesCombo") //$NON-NLS-1$
										+
										Messages.getString("RuleMakerPanel.continueAnyways"), Messages.getString("RuleMakerPanel.continueQ"), JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) //$NON-NLS-1$ //$NON-NLS-2$
							return;
					}
				}

				if (whiteCaptureAllButton.isSelected())
				{
					mWhiteRules.addEndOfGame(EndOfGame.CAPTURE_ALL_PIECES.init(0, Messages.getString("RuleMakerPanel.empty"), false)); //$NON-NLS-1$
				}
				if (whiteCaptureAllTypeButton.isSelected())
				{
					mWhiteRules.addEndOfGame(EndOfGame.CAPTURE_ALL_OF_TYPE
							.init(0, whitePiecesList.getSelectedItem().toString(), false));
				}
				if (whiteProtectObjectiveButton.isSelected())
				{
					mWhiteRules.addEndOfGame(EndOfGame.CLASSIC.init(0, whitePiecesList.getSelectedItem().toString(), false));
				}
				if (whiteLoseAllButton.isSelected())
				{
					mWhiteRules.addEndOfGame(EndOfGame.LOSE_ALL_PIECES.init(0, Messages.getString("RuleMakerPanel.empty"), false)); //$NON-NLS-1$
				}
				if (whiteCheckTimesButton.isSelected())
				{
					String whiteNumberOfChecks = whiteNumberOfChecksField.getText();
					try
					{
						int answer = Integer.parseInt(whiteNumberOfChecks);
						if (answer < 1)
						{
							JOptionPane.showMessageDialog(Driver.getInstance(),
									Messages.getString("RuleMakerPanel.enterGreaterThanOne"), //$NON-NLS-1$
									Messages.getString("RuleMakerPanel.numberOfChecks"), //$NON-NLS-1$
									JOptionPane.PLAIN_MESSAGE);
							return;
						}
						mWhiteRules.addEndOfGame(EndOfGame.CHECK_N_TIMES.init(answer,
								Messages.getString("RuleMakerPanel.empty"), false)); //$NON-NLS-1$
					}
					catch (Exception e)
					{
						JOptionPane.showMessageDialog(Driver.getInstance(),
								Messages.getString("RuleMakerPanel.enterIntoNumberOfChecks"), //$NON-NLS-1$
								Messages.getString("RuleMakerPanel.numberOfChecks"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
						return;
					}
				}

				if (blackCaptureAllButton.isSelected())
				{
					mBlackRules.addEndOfGame(EndOfGame.CAPTURE_ALL_PIECES.init(0, Messages.getString("RuleMakerPanel.empty"), true)); //$NON-NLS-1$
				}
				if (blackCaptureAllTypeButton.isSelected())
				{
					mBlackRules.addEndOfGame(EndOfGame.CAPTURE_ALL_OF_TYPE.init(0, blackPiecesList.getSelectedItem().toString(), true));
				}
				if (blackProtectObjectiveButton.isSelected())
				{
					mBlackRules.addEndOfGame(EndOfGame.CLASSIC.init(0, blackPiecesList.getSelectedItem().toString(), true));
				}
				if (blackLoseAllButton.isSelected())
				{
					mBlackRules.addEndOfGame(EndOfGame.LOSE_ALL_PIECES.init(0, Messages.getString("RuleMakerPanel.empty"), true)); //$NON-NLS-1$
				}
				if (blackCheckTimesButton.isSelected())
				{
					String blackNumberOfChecks = blackNumberOfChecksField.getText();
					try
					{
						int answer = Integer.parseInt(blackNumberOfChecks);
						if (answer < 1)
						{
							JOptionPane.showMessageDialog(Driver.getInstance(),
									Messages.getString("RuleMakerPanel.enterGreaterThanOne"), //$NON-NLS-1$
									Messages.getString("RuleMakerPanel.numberOfChecks"), //$NON-NLS-1$
									JOptionPane.PLAIN_MESSAGE);
							return;
						}
						mBlackRules.addEndOfGame(EndOfGame.CHECK_N_TIMES.init(answer, Messages.getString("RuleMakerPanel.empty"), true)); //$NON-NLS-1$
					}
					catch (Exception ne)
					{
						JOptionPane.showMessageDialog(Driver.getInstance(),
								Messages.getString("RuleMakerPanel.enterIntoNumberOfChecks"), //$NON-NLS-1$
								Messages.getString("RuleMakerPanel.numberOfChecks"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
						return;
					}
				}

				if (whitePieceReturnRadioButton.isSelected())
					mWhiteRules.addAfterMove(AfterMove.CAPTURED_PIECE_TO_ORIGIN);

				if (blackPieceReturnRadioButton.isSelected())
					mBlackRules.addAfterMove(AfterMove.CAPTURED_PIECE_TO_ORIGIN);

				if (whiteChangeColorRadioButton.isSelected())
				{
					mWhiteRules.addAfterMove(AfterMove.SWAP_COLOR_OF_CAPTURER);
				}

				if (whiteDropPiecesRadioButton.isSelected())
				{
					mWhiteRules.addAfterMove(AfterMove.CAPTURER_PLACES_CAPTURED);
				}
				if (whiteCapturedColorAndDropRadioButton.isSelected())
				{
					mWhiteRules.addAfterMove(AfterMove.CAPTURER_STEALS_CAPTURED);
				}
				if (blackChangeColorRadioButton.isSelected())
				{
					mBlackRules.addAfterMove(AfterMove.SWAP_COLOR_OF_CAPTURER);
				}

				if (blackDropPiecesRadioButton.isSelected())
				{
					mBlackRules.addAfterMove(AfterMove.CAPTURER_PLACES_CAPTURED);
				}
				if (blackCapturedColorAndDropRadioButton.isSelected())
				{
					mBlackRules.addAfterMove(AfterMove.CAPTURER_STEALS_CAPTURED);
				}
				if (switchBoardsCheckBox.isSelected() && boards.length == 2)
				{
					mWhiteRules.setGetBoard(GetBoard.OPPOSITE_BOARD);
					mBlackRules.setGetBoard(GetBoard.OPPOSITE_BOARD);
				}
				if (atomicChessCheckBox.isSelected())
				{
					mWhiteRules.addAfterMove(AfterMove.ATOMIC_CAPTURE);
					mBlackRules.addAfterMove(AfterMove.ATOMIC_CAPTURE);
				}
				if (mWhiteRules.getEndOfGame() == EndOfGame.CLASSIC || mWhiteRules.getEndOfGame() == EndOfGame.CAPTURE_ALL_OF_TYPE
						|| mWhiteRules.getEndOfGame() == EndOfGame.CHECK_N_TIMES)
				{
					mWhiteRules.setObjectivePiece(new ObjectivePieceType(ObjectivePieceTypes.CUSTOM_OBJECTIVE, whitePiecesList
							.getSelectedItem().toString()));
					// mBuilder.addToPromotionMap(mWhiteRules.getObjectiveName(),
					// null, GameBuilder.WHITE);
				}
				if (mBlackRules.getEndOfGame() == EndOfGame.CLASSIC || mBlackRules.getEndOfGame() == EndOfGame.CAPTURE_ALL_OF_TYPE
						|| mBlackRules.getEndOfGame() == EndOfGame.CHECK_N_TIMES)
				{
					mBlackRules.setObjectivePiece(new ObjectivePieceType(ObjectivePieceTypes.CUSTOM_OBJECTIVE, blackPiecesList
							.getSelectedItem().toString()));
					// mBuilder.addToPromotionMap(mBlackRules.getObjectiveName(),
					// null, GameBuilder.BLACK);
				}

				variantCreationPanel.mWhiteRules = mWhiteRules;
				variantCreationPanel.mBlackRules = mBlackRules;
				variantCreationPanel.setBuilder(mBuilder);
				RuleMakerPanel.this.removeAll();
				mFrame.setVisible(false);
			}
		});

		JPanel whiteTeamPanel = new JPanel();
		whiteTeamPanel.setBorder(GuiUtility.createBorder(Messages.getString("RuleMakerPanel.whiteTeam"))); //$NON-NLS-1$
		whiteTeamPanel.setLayout(new GridBagLayout());
		whiteTeamPanel.setOpaque(false);

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		whiteTeamPanel.add(objectiveWhitePanel, constraints);

		constraints.gridy = 1;
		whiteTeamPanel.add(whiteExtrasPanel, constraints);

		constraints.gridy = 2;
		whiteTeamPanel.add(whiteLegalDestinationPanel, constraints);

		constraints.gridy = 3;
		whiteTeamPanel.add(whiteAfterCapturePanel, constraints);

		JPanel blackTeamPanel = new JPanel();
		blackTeamPanel.setBorder(GuiUtility.createBorder(Messages.getString("RuleMakerPanel.blackTeam"))); //$NON-NLS-1$
		blackTeamPanel.setLayout(new GridBagLayout());
		blackTeamPanel.setOpaque(false);

		constraints.gridy = 0;
		blackTeamPanel.add(objectiveBlackPanel, constraints);

		constraints.gridy = 1;
		blackTeamPanel.add(blackExtrasPanel, constraints);

		constraints.gridy = 2;
		blackTeamPanel.add(blackLegalDestinationPanel, constraints);

		constraints.gridy = 3;
		blackTeamPanel.add(blackAfterPanel, constraints);

		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridy = 0;
		add(whiteTeamPanel, constraints);

		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 0;
		add(blackTeamPanel, constraints);

		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		add(specialRulesPanel, constraints);

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout());
		buttonsPanel.setOpaque(false);
		buttonsPanel.add(saveButton);
		buttonsPanel.add(cancelButton);

		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		add(buttonsPanel, constraints);

		ActionListener afterRadioButtonListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				atomicChessCheckBox.setSelected(false);
				atomicChessCheckBox.setEnabled(false);
			}
		};

		whiteChangeColorRadioButton.addActionListener(afterRadioButtonListener);
		blackChangeColorRadioButton.addActionListener(afterRadioButtonListener);

		whitePieceReturnRadioButton.addActionListener(afterRadioButtonListener);
		blackPieceReturnRadioButton.addActionListener(afterRadioButtonListener);

		whiteCapturedColorAndDropRadioButton.addActionListener(afterRadioButtonListener);
		blackCapturedColorAndDropRadioButton.addActionListener(afterRadioButtonListener);

		whiteDropPiecesRadioButton.addActionListener(afterRadioButtonListener);
		blackDropPiecesRadioButton.addActionListener(afterRadioButtonListener);

		ActionListener doNothingListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (blackNoAfterMoveRadioButton.isSelected() && whiteNoAfterMoveRadioButton.isSelected())
					atomicChessCheckBox.setEnabled(true);
			}
		};

		whiteNoAfterMoveRadioButton.addActionListener(doNothingListener);
		blackNoAfterMoveRadioButton.addActionListener(doNothingListener);

		atomicChessCheckBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				whiteNoAfterMoveRadioButton.setSelected(true);
				blackNoAfterMoveRadioButton.setSelected(true);
			}
		});

		if (boards.length == 2)
		{
			switchBoardsCheckBox.setEnabled(true);
		}
		mFrame.pack();
	}

	private static final long serialVersionUID = 8365806731061105370L;
	static boolean mNeedsObjectivePiece = false;

	private GameBuilder mBuilder;
	private RulesController mWhiteRules = new RulesController(false);
	private RulesController mBlackRules = new RulesController(true);
	private JFrame mFrame;
}
