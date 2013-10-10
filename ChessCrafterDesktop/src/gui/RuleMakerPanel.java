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
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import logic.Board;
import logic.Builder;
import logic.PieceBuilder;
import rules.AdjustTeamLegalDestinations;
import rules.AfterMove;
import rules.CropLegalDestinations;
import rules.EndOfGame;
import rules.GetBoard;
import rules.ObjectivePiece;
import rules.ObjectivePiece.ObjectivePieceTypes;
import rules.Rules;
import utility.GuiUtility;

public class RuleMakerPanel extends JPanel
{
	public RuleMakerPanel(CustomSetupPanel customSetupMenu, JFrame optionsFrame)
	{
		mFrame = optionsFrame;
		mFrame.setVisible(true);
		mFrame.add(this);
		mFrame.setVisible(true);
		mFrame.setSize(600, 500);
		mFrame.setLocationRelativeTo(Driver.getInstance());
		mBuilder = customSetupMenu.getBuilder();
		mWhiteRules = customSetupMenu.mWhiteRules;
		mBlackRules = customSetupMenu.mBlackRules;
		initGUIComponents(customSetupMenu);
	}

	private void initGUIComponents(final CustomSetupPanel customSetupMenu)
	{
		revalidate();
		repaint();
		setLayout(new GridBagLayout());
		setSize(600, 600);
		setBorder(BorderFactory.createLoweredBevelBorder());
		GridBagConstraints constraints = new GridBagConstraints();

		final Board[] boards = mBuilder.getBoards();

		// capture mandatory check boxes
		final JPanel whiteLegalDestinationPanel = new JPanel();
		whiteLegalDestinationPanel.setLayout(new GridLayout(2, 1));
		final JCheckBox whiteCaptureMandatoryCheckBox = new JCheckBox(Messages.getString("RuleMakerPanel.captureMandatory")); //$NON-NLS-1$
		whiteCaptureMandatoryCheckBox.setToolTipText(Messages.getString("RuleMakerPanel.capturingMustBe")); //$NON-NLS-1$
		whiteCaptureMandatoryCheckBox.setSelected(customSetupMenu.mWhiteRules
				.getCaptureMandatory());
		whiteLegalDestinationPanel.add(whiteCaptureMandatoryCheckBox);

		final JPanel blackLegalDestinationPanel = new JPanel();
		blackLegalDestinationPanel.setLayout(new GridLayout(2, 1));
		final JCheckBox blackCaptureMandatoryCheckBox = new JCheckBox(
				Messages.getString("RuleMakerPanel.captureMandatory")); //$NON-NLS-1$
		blackCaptureMandatoryCheckBox
				.setToolTipText(Messages.getString("RuleMakerPanel.capturingMustBe")); //$NON-NLS-1$
		blackCaptureMandatoryCheckBox.setSelected(customSetupMenu.mBlackRules
				.getCaptureMandatory());
		blackLegalDestinationPanel.add(blackCaptureMandatoryCheckBox);

		// can't move objective check boxes
		final JCheckBox whiteNoMoveObjectiveCheckBox = new JCheckBox(
				Messages.getString("RuleMakerPanel.cantMoveObj")); //$NON-NLS-1$
		whiteNoMoveObjectiveCheckBox
				.setToolTipText(Messages.getString("RuleMakerPanel.movingObjIllegal")); //$NON-NLS-1$
		if (mWhiteRules.getEndOfGame() == EndOfGame.CLASSIC
				|| mWhiteRules.getEndOfGame() == EndOfGame.CHECK_N_TIMES) {
			whiteNoMoveObjectiveCheckBox
					.setSelected(customSetupMenu.mWhiteRules
							.getObjectiveIsStationary());
			whiteLegalDestinationPanel.add(whiteNoMoveObjectiveCheckBox);
		}

		final JCheckBox blackNoMoveObjectiveCheckBox = new JCheckBox(Messages.getString("RuleMakerPanel.cantMoveObj")); //$NON-NLS-1$
		blackNoMoveObjectiveCheckBox.setToolTipText(Messages.getString("RuleMakerPanel.movingObjIllegal")); //$NON-NLS-1$
		if (mBlackRules.getEndOfGame() == EndOfGame.CLASSIC
				|| mBlackRules.getEndOfGame() == EndOfGame.CHECK_N_TIMES) {
			blackNoMoveObjectiveCheckBox
					.setSelected(customSetupMenu.mBlackRules
							.getObjectiveIsStationary());
			blackLegalDestinationPanel.add(blackNoMoveObjectiveCheckBox);
		}

		final JPanel whiteAfterCapturePanel = new JPanel();
		whiteAfterCapturePanel.setLayout(new GridLayout(4, 1));

		final ButtonGroup whiteAfterOptions = new ButtonGroup();
		final JRadioButton whiteChangeColorRadioButton = new JRadioButton(
				Messages.getString("RuleMakerPanel.captererChangesColor")); //$NON-NLS-1$
		whiteChangeColorRadioButton
				.setToolTipText(Messages.getString("RuleMakerPanel.capturingPieceChanges")); //$NON-NLS-1$

		if (mWhiteRules.getEndOfGame() != EndOfGame.CLASSIC) {
			whiteAfterOptions.add(whiteChangeColorRadioButton);
			whiteAfterCapturePanel.add(whiteChangeColorRadioButton);
		}

		final JRadioButton whitePieceReturnRadioButton = new JRadioButton(
				Messages.getString("RuleMakerPanel.capturedReturns")); //$NON-NLS-1$
		whitePieceReturnRadioButton
				.setToolTipText(Messages.getString("RuleMakerPanel.capturedReturnToTheirStart")); //$NON-NLS-1$
		whitePieceReturnRadioButton.setSelected(mWhiteRules
				.getCapturedReturnToStart());
		whiteAfterOptions.add(whitePieceReturnRadioButton);
		whiteAfterCapturePanel.add(whitePieceReturnRadioButton);

		final JRadioButton whiteDropPiecesRadioButton = new JRadioButton(
				Messages.getString("RuleMakerPanel.capturedPiecesDrop")); //$NON-NLS-1$
		if (mWhiteRules.getEndOfGame() != EndOfGame.CAPTURE_ALL_OF_TYPE
				|| mWhiteRules.getEndOfGame() != EndOfGame.LOSE_ALL_PIECES) {
			whiteDropPiecesRadioButton
					.setToolTipText(Messages.getString("RuleMakerPanel.capturedArePlaced")); //$NON-NLS-1$
			whiteDropPiecesRadioButton.setSelected(mWhiteRules.getPiecesDrop());
			whiteAfterOptions.add(whiteDropPiecesRadioButton);
			whiteAfterCapturePanel.add(whiteDropPiecesRadioButton);
		}

		final JRadioButton whiteCapturedColorAndDropRadioButton = new JRadioButton(
				Messages.getString("RuleMakerPanel.capturedChangesAnd")); //$NON-NLS-1$
		whiteCapturedColorAndDropRadioButton
				.setToolTipText(Messages.getString("RuleMakerPanel.capturedChangesTeams")); //$NON-NLS-1$
		whiteCapturedColorAndDropRadioButton.setSelected(mWhiteRules
				.getPiecesDropAndSwitch());
		whiteAfterOptions.add(whiteCapturedColorAndDropRadioButton);
		whiteAfterCapturePanel.add(whiteCapturedColorAndDropRadioButton);

		final JRadioButton whiteNoAfterMoveRadioButton = new JRadioButton(
				Messages.getString("RuleMakerPanel.doNothing")); //$NON-NLS-1$
		whiteNoAfterMoveRadioButton.setSelected(mWhiteRules
				.getNoAfterMovesSelected());
		whiteAfterOptions.add(whiteNoAfterMoveRadioButton);
		whiteAfterCapturePanel.add(whiteNoAfterMoveRadioButton);

		final JPanel blackAfterPanel = new JPanel();
		final ButtonGroup blackAfterOptions = new ButtonGroup();
		
		blackAfterPanel.setLayout(new GridLayout(4, 1));
		final JRadioButton blackChangeColorRadioButton = new JRadioButton(
				Messages.getString("RuleMakerPanel.capturerChanges")); //$NON-NLS-1$
		blackChangeColorRadioButton
				.setToolTipText(Messages.getString("RuleMakerPanel.capturingPieceChanges")); //$NON-NLS-1$

		if (mBlackRules.getEndOfGame() != EndOfGame.CLASSIC)
		{
			blackAfterOptions.add(blackChangeColorRadioButton);
			blackAfterPanel.add(blackChangeColorRadioButton);
		}

		final JRadioButton blackPieceReturnRadioButton = new JRadioButton(
				Messages.getString("RuleMakerPanel.capturedReturns")); //$NON-NLS-1$
		blackPieceReturnRadioButton
				.setToolTipText(Messages.getString("RuleMakerPanel.capturedReturnToTheirStart")); //$NON-NLS-1$
		blackPieceReturnRadioButton.setSelected(mBlackRules
				.getCapturedReturnToStart());
		blackAfterPanel.add(blackPieceReturnRadioButton);
		blackAfterOptions.add(blackPieceReturnRadioButton);
		
		final JRadioButton blackDropPiecesRadioButton = new JRadioButton(
				Messages.getString("RuleMakerPanel.capturedPiecesDrop")); //$NON-NLS-1$
		blackDropPiecesRadioButton
				.setToolTipText(Messages.getString("RuleMakerPanel.capturedArePlaced")); //$NON-NLS-1$
		if (mBlackRules.getEndOfGame() != EndOfGame.CAPTURE_ALL_OF_TYPE
				|| mBlackRules.getEndOfGame() != EndOfGame.LOSE_ALL_PIECES) {
			blackDropPiecesRadioButton.setSelected(mBlackRules.getPiecesDrop());
			blackAfterPanel.add(blackDropPiecesRadioButton);
			blackAfterOptions.add(blackDropPiecesRadioButton);
		}

		final JRadioButton blackCapturedColorAndDropRadioButton = new JRadioButton(
				Messages.getString("RuleMakerPanel.capturedChangesAnd")); //$NON-NLS-1$
		blackCapturedColorAndDropRadioButton
				.setToolTipText(Messages.getString("RuleMakerPanel.capturedChangesTeams")); //$NON-NLS-1$
		blackCapturedColorAndDropRadioButton.setSelected(mBlackRules
				.getPiecesDropAndSwitch());
		blackAfterPanel.add(blackCapturedColorAndDropRadioButton);
		blackAfterOptions.add(blackCapturedColorAndDropRadioButton);

		final JRadioButton blackNoAfterMoveRadioButton = new JRadioButton(
				Messages.getString("RuleMakerPanel.doNothing")); //$NON-NLS-1$
		blackNoAfterMoveRadioButton.setSelected(mBlackRules
				.getNoAfterMovesSelected());
		blackAfterOptions.add(blackNoAfterMoveRadioButton);
		blackAfterPanel.add(blackNoAfterMoveRadioButton);
		
		final JPanel specialRulesPanel = new JPanel();
		specialRulesPanel.setLayout(new GridLayout(2, 1));

		final JCheckBox atomicChessCheckBox = new JCheckBox(Messages.getString("RuleMakerPanel.atomicChess")); //$NON-NLS-1$
		atomicChessCheckBox
				.setToolTipText(Messages.getString("RuleMakerPanel.captureRemovesBoth")); //$NON-NLS-1$
		atomicChessCheckBox.setEnabled(mWhiteRules.isAtomic() || mWhiteRules.getNoAfterMovesSelected());
		atomicChessCheckBox.setSelected(mWhiteRules.isAtomic());
		specialRulesPanel.add(atomicChessCheckBox);

		final JCheckBox switchBoardsCheckBox = new JCheckBox(
				Messages.getString("RuleMakerPanel.moveToOtherBoard")); //$NON-NLS-1$
		switchBoardsCheckBox
				.setToolTipText(Messages.getString("RuleMakerPanel.eachPiecesMoves")); //$NON-NLS-1$
		switchBoardsCheckBox.setEnabled(mWhiteRules.switchBoards());
		specialRulesPanel.add(switchBoardsCheckBox);

		final JPanel whiteExtrasPanel = new JPanel();
		whiteExtrasPanel.setLayout(new GridBagLayout());
		final JTextField whiteNumberOfChecksField = new JTextField(2);
		Object[] allPieces = PieceBuilder.getSet().toArray();
		final JComboBox whitePieceList = new JComboBox(allPieces);

		int whiteObjectiveIndex = 0;
		int blackObjectiveIndex = 0;
		for (int i = 0; i < allPieces.length; i++) {
			if (allPieces[i].equals(mWhiteRules.getObjectiveName()))
				whiteObjectiveIndex = i;
			if (allPieces[i].equals(mBlackRules.getObjectiveName()))
				blackObjectiveIndex = i;
		}

		JLabel whiteNumberOfChecksLabel = new JLabel(
				Messages.getString("RuleMakerPanel.howManyTimesForCheck")); //$NON-NLS-1$
		JLabel whiteObjectivePieceLabel = new JLabel(
				Messages.getString("RuleMakerPanel.whichIsObjective")); //$NON-NLS-1$

		whiteNumberOfChecksField.setVisible(false);
		whitePieceList.setVisible(false);

		if (mWhiteRules.getEndOfGame() == EndOfGame.CLASSIC
				|| mWhiteRules.getEndOfGame() == EndOfGame.CAPTURE_ALL_OF_TYPE)
		{
			if (mWhiteRules.getEndOfGame() == EndOfGame.CAPTURE_ALL_OF_TYPE)
				whiteObjectivePieceLabel
						.setText(Messages.getString("RuleMakerPanel.whichPieceTypeCaptured")); //$NON-NLS-1$

			whitePieceList.setVisible(true);
			whitePieceList.setSelectedIndex(whiteObjectiveIndex);
			constraints.gridx = 0;
			constraints.gridy = 1;
			constraints.insets = new Insets(1, 1, 1, 1);
			whiteExtrasPanel.add(whiteObjectivePieceLabel, constraints);
			constraints.gridx = 1;
			constraints.gridy = 1;
			whiteExtrasPanel.add(whitePieceList, constraints);
		}

		if (mWhiteRules.getEndOfGame() == EndOfGame.CHECK_N_TIMES)
		{
			whiteNumberOfChecksField.setVisible(true);
			whitePieceList.setVisible(true);
			whitePieceList.setSelectedIndex(whiteObjectiveIndex);
			constraints.gridx = 0;
			constraints.gridy = 0;
			whiteExtrasPanel.add(whiteNumberOfChecksLabel, constraints);
			constraints.gridx = 1;
			constraints.gridy = 0;
			whiteExtrasPanel.add(whiteNumberOfChecksField, constraints);
			constraints.gridx = 0;
			constraints.gridy = 1;
			constraints.insets = new Insets(1, 1, 1, 1);
			whiteExtrasPanel.add(whiteObjectivePieceLabel, constraints);
			constraints.gridx = 1;
			constraints.gridy = 1;
			whiteExtrasPanel.add(whitePieceList, constraints);
		}

		final JPanel blackExtrasPanel = new JPanel();
		blackExtrasPanel.setLayout(new GridBagLayout());

		final JTextField blackNumberOfChecksField = new JTextField(2);
		final JComboBox blackPiecesList = new JComboBox(allPieces);

		JLabel blackNumberOfChecksLabel = new JLabel(
				Messages.getString("RuleMakerPanel.howManyTimesForCheck")); //$NON-NLS-1$
		JLabel blackObjectivePieceLabel = new JLabel(
				Messages.getString("RuleMakerPanel.whichIsObjective")); //$NON-NLS-1$

		blackNumberOfChecksField.setVisible(false);
		blackPiecesList.setVisible(false);

		if (mBlackRules.getEndOfGame() == EndOfGame.CLASSIC
				|| mBlackRules.getEndOfGame() == EndOfGame.CAPTURE_ALL_OF_TYPE)
		{
			if (mBlackRules.getEndOfGame() == EndOfGame.CAPTURE_ALL_OF_TYPE)
				blackObjectivePieceLabel
						.setText(Messages.getString("RuleMakerPanel.whichPieceTypeCaptured")); //$NON-NLS-1$

			blackPiecesList.setVisible(true);
			blackPiecesList.setSelectedIndex(blackObjectiveIndex);
			constraints.gridx = 0;
			constraints.gridy = 1;
			constraints.insets = new Insets(1, 1, 1, 1);
			blackExtrasPanel.add(blackObjectivePieceLabel, constraints);
			constraints.gridx = 1;
			constraints.gridy = 1;
			blackExtrasPanel.add(blackPiecesList, constraints);
		}

		if (mBlackRules.getEndOfGame() == EndOfGame.CHECK_N_TIMES)
		{
			blackNumberOfChecksField.setVisible(true);
			blackPiecesList.setVisible(true);
			blackPiecesList.setSelectedIndex(blackObjectiveIndex);
			constraints.gridx = 0;
			constraints.gridy = 0;
			blackExtrasPanel.add(blackNumberOfChecksLabel, constraints);
			constraints.gridx = 1;
			constraints.gridy = 0;
			blackExtrasPanel.add(blackNumberOfChecksField, constraints);
			constraints.gridx = 0;
			constraints.gridy = 1;
			constraints.insets = new Insets(1, 1, 1, 1);
			blackExtrasPanel.add(blackObjectivePieceLabel, constraints);
			constraints.gridx = 1;
			constraints.gridy = 1;
			blackExtrasPanel.add(blackPiecesList, constraints);
		}

		final JButton cancelButton = new JButton(Messages.getString("RuleMakerPanel.cancel")); //$NON-NLS-1$
		cancelButton
				.setToolTipText(Messages.getString("RuleMakerPanel.pressToReturnToVariant")); //$NON-NLS-1$
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

				mWhiteRules
						.addAdjustTeamDestinations(whiteCaptureMandatoryCheckBox
								.isSelected() ? AdjustTeamLegalDestinations.MUST_CAPTURE
								: AdjustTeamLegalDestinations.CLASSIC);
				mBlackRules
						.addAdjustTeamDestinations(blackCaptureMandatoryCheckBox
								.isSelected() ? AdjustTeamLegalDestinations.MUST_CAPTURE
								: AdjustTeamLegalDestinations.CLASSIC);

				mWhiteRules.addCropLegalDests(whiteNoMoveObjectiveCheckBox
						.isSelected() ? CropLegalDestinations.STATIONARY_OBJECTIVE
						: CropLegalDestinations.CLASSIC);

				mBlackRules.addCropLegalDests(blackNoMoveObjectiveCheckBox
						.isSelected() ? CropLegalDestinations.STATIONARY_OBJECTIVE
						: CropLegalDestinations.CLASSIC);

				if (whitePieceReturnRadioButton.isSelected())
					mWhiteRules
							.addAfterMove(AfterMove.CAPTURED_PIECE_TO_ORIGIN);

				if (blackPieceReturnRadioButton.isSelected())
					mBlackRules
							.addAfterMove(AfterMove.CAPTURED_PIECE_TO_ORIGIN);

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

				if (whiteNumberOfChecksField.isVisible())
				{
					String whiteNumberOfChecks = whiteNumberOfChecksField
							.getText();
					try
					{
						int answer = Integer.parseInt(whiteNumberOfChecks);
						if (answer < 1)
						{
							JOptionPane.showMessageDialog(
									Driver.getInstance(),
									Messages.getString("RuleMakerPanel.enterGreaterThanOne"), //$NON-NLS-1$
									Messages.getString("RuleMakerPanel.numberOfChecks"), //$NON-NLS-1$
									JOptionPane.PLAIN_MESSAGE);
							return;
						}
						mWhiteRules.addEndOfGame(EndOfGame.CHECK_N_TIMES.init(
								answer, "", false)); //$NON-NLS-1$
					}
					catch (Exception e)
					{
						JOptionPane.showMessageDialog(
								Driver.getInstance(),
								Messages.getString("RuleMakerPanel.enterIntoNumberOfChecks"), //$NON-NLS-1$
								Messages.getString("RuleMakerPanel.numberOfChecks"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
						return;
					}
				}
				else if (whitePieceList.isVisible())
				{
					if (mWhiteRules.getEndOfGame() == EndOfGame.CLASSIC)
					{
						mWhiteRules.addEndOfGame(EndOfGame.CLASSIC.init(0, "", //$NON-NLS-1$
								false));
						mWhiteRules.setObjectivePiece(new ObjectivePiece(
								ObjectivePieceTypes.CUSTOM_OBJECTIVE,
								whitePieceList.getSelectedItem().toString()));
					}
					else
					{
						mWhiteRules.addEndOfGame(EndOfGame.CAPTURE_ALL_OF_TYPE
								.init(0, whitePieceList.getSelectedItem()
										.toString(), false));
					}
				}
				if (blackNumberOfChecksField.isVisible())
				{
					String blackNumberOfChecks = blackNumberOfChecksField
							.getText();
					try
					{
						int answer = Integer.parseInt(blackNumberOfChecks);
						if (answer < 1)
						{
							JOptionPane.showMessageDialog(
									Driver.getInstance(),
									Messages.getString("RuleMakerPanel.enterGreaterThanOne"), //$NON-NLS-1$
									Messages.getString("RuleMakerPanel.numberOfChecks"), //$NON-NLS-1$
									JOptionPane.PLAIN_MESSAGE);
							return;
						}
						mBlackRules.addEndOfGame(EndOfGame.CHECK_N_TIMES.init(
								answer, "", true)); //$NON-NLS-1$
					}
					catch (Exception ne)
					{
						JOptionPane.showMessageDialog(
								Driver.getInstance(),
								Messages.getString("RuleMakerPanel.enterIntoNumberOfChecks"), //$NON-NLS-1$
								Messages.getString("RuleMakerPanel.numberOfChecks"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
						return;
					}
				}
				else if (blackPiecesList.isVisible())
				{
					blackPiecesList.getSelectedItem();
					if (mBlackRules.getEndOfGame() == EndOfGame.CLASSIC)
					{
						mBlackRules.addEndOfGame(EndOfGame.CLASSIC.init(0, "", //$NON-NLS-1$
								true));
						mBlackRules.setObjectivePiece(new ObjectivePiece(
								ObjectivePieceTypes.CUSTOM_OBJECTIVE,
								blackPiecesList.getSelectedItem().toString()));
					}
					else
					{
						mBlackRules.addEndOfGame(EndOfGame.CAPTURE_ALL_OF_TYPE
								.init(0, blackPiecesList.getSelectedItem()
										.toString(), true));
					}
				}

				customSetupMenu.mWhiteRules = mWhiteRules;
				customSetupMenu.mBlackRules = mBlackRules;
				RuleMakerPanel.this.removeAll();
				mFrame.setVisible(false);
			}
		});

		JPanel whiteTeamPanel = new JPanel();
		whiteTeamPanel
				.setBorder(BorderFactory.createTitledBorder(Messages.getString("RuleMakerPanel.whiteTeam"))); //$NON-NLS-1$
		whiteTeamPanel.setLayout(new GridBagLayout());

		JPanel whiteLegalDestinationsPanel = new JPanel();
		whiteLegalDestinationsPanel.setLayout(new GridBagLayout());
		constraints.gridx = 0;
		constraints.gridy = 1;
		whiteLegalDestinationsPanel.add(new JLabel(
				Messages.getString("RuleMakerPanel.legalDestinationHTML")), constraints); //$NON-NLS-1$
		constraints.gridx = 0;
		constraints.gridy = 2;
		whiteLegalDestinationsPanel
				.add(whiteLegalDestinationPanel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 0;
		whiteTeamPanel.add(whiteLegalDestinationsPanel, constraints);

		constraints.insets = new Insets(1, 3, 3, 3);
		constraints.gridx = 0;
		constraints.gridy = 1;
		whiteTeamPanel.add(whiteExtrasPanel, constraints);

		JPanel whiteCapturePanel = new JPanel();
		whiteCapturePanel.setLayout(new GridBagLayout());
		constraints.gridheight = 1;
		constraints.gridx = 0;
		constraints.gridy = 1;
		whiteCapturePanel.add(new JLabel(
				Messages.getString("RuleMakerPanel.afterCapturingHTML")), //$NON-NLS-1$
				constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		whiteCapturePanel.add(whiteAfterCapturePanel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		whiteTeamPanel.add(whiteCapturePanel, constraints);

		JPanel blackTeamPanel = new JPanel();
		blackTeamPanel
				.setBorder(BorderFactory.createTitledBorder(Messages.getString("RuleMakerPanel.blackTeam"))); //$NON-NLS-1$
		blackTeamPanel.setLayout(new GridBagLayout());

		JPanel blackLegalDestinationsPanel = new JPanel();
		blackLegalDestinationsPanel.setLayout(new GridBagLayout());
		constraints.gridx = 0;
		constraints.gridy = 1;
		blackLegalDestinationsPanel.add(new JLabel(
				Messages.getString("RuleMakerPanel.legalDestinationHTML")), constraints); //$NON-NLS-1$
		constraints.gridx = 0;
		constraints.gridy = 2;
		blackLegalDestinationsPanel
				.add(blackLegalDestinationPanel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 0;
		blackTeamPanel.add(blackLegalDestinationsPanel, constraints);

		JPanel blackCapturePanel = new JPanel();
		blackCapturePanel.setLayout(new GridBagLayout());
		constraints.gridheight = 1;
		constraints.gridx = 0;
		constraints.gridy = 1;
		blackCapturePanel.add(new JLabel(
				Messages.getString("RuleMakerPanel.afterCapturingHTML")), //$NON-NLS-1$
				constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		blackCapturePanel.add(blackAfterPanel, constraints);

		constraints.insets = new Insets(1, 3, 3, 3);
		constraints.gridx = 0;
		constraints.gridy = 1;
		blackTeamPanel.add(blackExtrasPanel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		blackTeamPanel.add(blackCapturePanel, constraints);

		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(whiteTeamPanel, constraints);

		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 0;
		add(blackTeamPanel, constraints);

		JPanel specialRules = new JPanel();
		specialRules.setBorder(BorderFactory
				.createTitledBorder(Messages.getString("RuleMakerPanel.specialRules"))); //$NON-NLS-1$
		specialRules.setLayout(new GridBagLayout());

		constraints.gridx = 0;
		constraints.gridy = 1;
		specialRules.add(specialRulesPanel, constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;

		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		add(specialRules, constraints);

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout());
		buttonsPanel.add(saveButton);
		buttonsPanel.add(cancelButton);

		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		add(buttonsPanel, constraints);

		ActionListener afterRadioButtonListener = new ActionListener() {
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
		
		ActionListener doNothingListener = new ActionListener() {	
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

	private Builder mBuilder;
	private Rules mWhiteRules = new Rules(false);
	private Rules mBlackRules = new Rules(true);
	private JFrame mFrame;
}
