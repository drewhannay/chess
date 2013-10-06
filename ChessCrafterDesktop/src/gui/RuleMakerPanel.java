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
		final JCheckBox whiteCaptureMandatoryCheckBox = new JCheckBox("Capture Mandatory");
		whiteCaptureMandatoryCheckBox.setToolTipText("Capturing moves must be performed");
		whiteCaptureMandatoryCheckBox.setSelected(customSetupMenu.mWhiteRules
				.getCaptureMandatory());
		whiteLegalDestinationPanel.add(whiteCaptureMandatoryCheckBox);

		final JPanel blackLegalDestinationPanel = new JPanel();
		blackLegalDestinationPanel.setLayout(new GridLayout(2, 1));
		final JCheckBox blackCaptureMandatoryCheckBox = new JCheckBox(
				"Capture Mandatory");
		blackCaptureMandatoryCheckBox
				.setToolTipText("Capturing moves must be performed");
		blackCaptureMandatoryCheckBox.setSelected(customSetupMenu.mBlackRules
				.getCaptureMandatory());
		blackLegalDestinationPanel.add(blackCaptureMandatoryCheckBox);

		// can't move objective check boxes
		final JCheckBox whiteNoMoveObjectiveCheckBox = new JCheckBox(
				"Can't Move Objective");
		whiteNoMoveObjectiveCheckBox
				.setToolTipText("Moving the objective piece is illegal");
		if (mWhiteRules.getEndOfGame() == EndOfGame.CLASSIC
				|| mWhiteRules.getEndOfGame() == EndOfGame.CHECK_N_TIMES) {
			whiteNoMoveObjectiveCheckBox
					.setSelected(customSetupMenu.mWhiteRules
							.getObjectiveIsStationary());
			whiteLegalDestinationPanel.add(whiteNoMoveObjectiveCheckBox);
		}

		final JCheckBox blackNoMoveObjectiveCheckBox = new JCheckBox("Can't Move Objective");
		blackNoMoveObjectiveCheckBox.setToolTipText("Moving the objective piece is illegal");
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
				"Capturer changes Color");
		whiteChangeColorRadioButton
				.setToolTipText("The capturing piece changes color after performing a capture");

		if (mWhiteRules.getEndOfGame() != EndOfGame.CLASSIC) {
			whiteAfterOptions.add(whiteChangeColorRadioButton);
			whiteAfterCapturePanel.add(whiteChangeColorRadioButton);
		}

		final JRadioButton whitePieceReturnRadioButton = new JRadioButton(
				"Captured piece returns to start");
		whitePieceReturnRadioButton
				.setToolTipText("Captured pieces return to their starting squares");
		whitePieceReturnRadioButton.setSelected(mWhiteRules
				.getCapturedReturnToStart());
		whiteAfterOptions.add(whitePieceReturnRadioButton);
		whiteAfterCapturePanel.add(whitePieceReturnRadioButton);

		final JRadioButton whiteDropPiecesRadioButton = new JRadioButton(
				"Captured Pieces Drop");
		if (mWhiteRules.getEndOfGame() != EndOfGame.CAPTURE_ALL_OF_TYPE
				|| mWhiteRules.getEndOfGame() != EndOfGame.LOSE_ALL_PIECES) {
			whiteDropPiecesRadioButton
					.setToolTipText("Captured pieces are placed in any open square on the board by the capturer");
			whiteDropPiecesRadioButton.setSelected(mWhiteRules.getPiecesDrop());
			whiteAfterOptions.add(whiteDropPiecesRadioButton);
			whiteAfterCapturePanel.add(whiteDropPiecesRadioButton);
		}

		final JRadioButton whiteCapturedColorAndDropRadioButton = new JRadioButton(
				"Captured Piece Changes Color and Drops");
		whiteCapturedColorAndDropRadioButton
				.setToolTipText("Captured pieces change teams and are placed in any open square on the board by the capturer");
		whiteCapturedColorAndDropRadioButton.setSelected(mWhiteRules
				.getPiecesDropAndSwitch());
		whiteAfterOptions.add(whiteCapturedColorAndDropRadioButton);
		whiteAfterCapturePanel.add(whiteCapturedColorAndDropRadioButton);

		final JRadioButton whiteNoAfterMoveRadioButton = new JRadioButton(
				"Do Nothing");
		whiteNoAfterMoveRadioButton.setSelected(mWhiteRules
				.getNoAfterMovesSelected());
		whiteAfterOptions.add(whiteNoAfterMoveRadioButton);
		whiteAfterCapturePanel.add(whiteNoAfterMoveRadioButton);

		final JPanel blackAfterPanel = new JPanel();
		final ButtonGroup blackAfterOptions = new ButtonGroup();
		
		blackAfterPanel.setLayout(new GridLayout(4, 1));
		final JRadioButton blackChangeColorRadioButton = new JRadioButton(
				"Capturer changes Color");
		blackChangeColorRadioButton
				.setToolTipText("The capturing piece changes color after performing a capture");

		if (mBlackRules.getEndOfGame() != EndOfGame.CLASSIC)
		{
			blackAfterOptions.add(blackChangeColorRadioButton);
			blackAfterPanel.add(blackChangeColorRadioButton);
		}

		final JRadioButton blackPieceReturnRadioButton = new JRadioButton(
				"Captured piece returns to start");
		blackPieceReturnRadioButton
				.setToolTipText("Captured pieces return to their starting squares");
		blackPieceReturnRadioButton.setSelected(mBlackRules
				.getCapturedReturnToStart());
		blackAfterPanel.add(blackPieceReturnRadioButton);
		blackAfterOptions.add(blackPieceReturnRadioButton);
		
		final JRadioButton blackDropPiecesRadioButton = new JRadioButton(
				"Captured Pieces Drop");
		blackDropPiecesRadioButton
				.setToolTipText("Captured pieces are placed in any open square on the board by the capturer");
		if (mBlackRules.getEndOfGame() != EndOfGame.CAPTURE_ALL_OF_TYPE
				|| mBlackRules.getEndOfGame() != EndOfGame.LOSE_ALL_PIECES) {
			blackDropPiecesRadioButton.setSelected(mBlackRules.getPiecesDrop());
			blackAfterPanel.add(blackDropPiecesRadioButton);
			blackAfterOptions.add(blackDropPiecesRadioButton);
		}

		final JRadioButton blackCapturedColorAndDropRadioButton = new JRadioButton(
				"Captured Piece Changes Color and Drops");
		blackCapturedColorAndDropRadioButton
				.setToolTipText("Captured pieces change teams and are placed in any open square on the board by the capturer");
		blackCapturedColorAndDropRadioButton.setSelected(mBlackRules
				.getPiecesDropAndSwitch());
		blackAfterPanel.add(blackCapturedColorAndDropRadioButton);
		blackAfterOptions.add(blackCapturedColorAndDropRadioButton);

		final JRadioButton blackNoAfterMoveRadioButton = new JRadioButton(
				"Do Nothing");
		blackNoAfterMoveRadioButton.setSelected(mBlackRules
				.getNoAfterMovesSelected());
		blackAfterOptions.add(blackNoAfterMoveRadioButton);
		blackAfterPanel.add(blackNoAfterMoveRadioButton);
		
		final JPanel specialRulesPanel = new JPanel();
		specialRulesPanel.setLayout(new GridLayout(2, 1));

		final JCheckBox atomicChessCheckBox = new JCheckBox("Atomic Chess");
		atomicChessCheckBox
				.setToolTipText("Capture removes from play both capturer and the captured piece,\nas well as the pieces in the 8 surrounding squares (except for pawns)");
		atomicChessCheckBox.setEnabled(mWhiteRules.isAtomic() || mWhiteRules.getNoAfterMovesSelected());
		atomicChessCheckBox.setSelected(mWhiteRules.isAtomic());
		specialRulesPanel.add(atomicChessCheckBox);

		final JCheckBox switchBoardsCheckBox = new JCheckBox(
				"Move to other board");
		switchBoardsCheckBox
				.setToolTipText("Each piece moves to the opposite board every time it moves.");
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
				"How many times for check?");
		JLabel whiteObjectivePieceLabel = new JLabel(
				"Which piece is the objective?");

		whiteNumberOfChecksField.setVisible(false);
		whitePieceList.setVisible(false);

		if (mWhiteRules.getEndOfGame() == EndOfGame.CLASSIC
				|| mWhiteRules.getEndOfGame() == EndOfGame.CAPTURE_ALL_OF_TYPE)
		{
			if (mWhiteRules.getEndOfGame() == EndOfGame.CAPTURE_ALL_OF_TYPE)
				whiteObjectivePieceLabel
						.setText("Which Piece type will be captured?");

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
				"How many times for check?");
		JLabel blackObjectivePieceLabel = new JLabel(
				"Which piece is the objective?");

		blackNumberOfChecksField.setVisible(false);
		blackPiecesList.setVisible(false);

		if (mBlackRules.getEndOfGame() == EndOfGame.CLASSIC
				|| mBlackRules.getEndOfGame() == EndOfGame.CAPTURE_ALL_OF_TYPE)
		{
			if (mBlackRules.getEndOfGame() == EndOfGame.CAPTURE_ALL_OF_TYPE)
				blackObjectivePieceLabel
						.setText("Which Piece type will be captured?");

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

		final JButton cancelButton = new JButton("Cancel");
		cancelButton
				.setToolTipText("Press me to return to the main Variant window");
		GuiUtility.setupVariantCancelButton(cancelButton, this, mFrame);

		JButton saveButton = new JButton("Save");
		saveButton.setToolTipText("Press to save rules");
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
									"Please enter a number greater than 1 into the Number of Checks box.",
									"Number of Checks",
									JOptionPane.PLAIN_MESSAGE);
							return;
						}
						mWhiteRules.addEndOfGame(EndOfGame.CHECK_N_TIMES.init(
								answer, "", false));
					}
					catch (Exception e)
					{
						JOptionPane.showMessageDialog(
								Driver.getInstance(),
								"Please enter a number into the Number of Checks box.",
								"Number of Checks", JOptionPane.PLAIN_MESSAGE);
						return;
					}
				}
				else if (whitePieceList.isVisible())
				{
					if (mWhiteRules.getEndOfGame() == EndOfGame.CLASSIC)
					{
						mWhiteRules.addEndOfGame(EndOfGame.CLASSIC.init(0, "",
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
									"Please enter a number greater than 1 into the Number of Checks box.",
									"Number of Checks",
									JOptionPane.PLAIN_MESSAGE);
							return;
						}
						mBlackRules.addEndOfGame(EndOfGame.CHECK_N_TIMES.init(
								answer, "", true));
					}
					catch (Exception ne)
					{
						JOptionPane.showMessageDialog(
								Driver.getInstance(),
								"Please enter a number into the Number of Checks box.",
								"Number of Checks", JOptionPane.PLAIN_MESSAGE);
						return;
					}
				}
				else if (blackPiecesList.isVisible())
				{
					blackPiecesList.getSelectedItem();
					if (mBlackRules.getEndOfGame() == EndOfGame.CLASSIC)
					{
						mBlackRules.addEndOfGame(EndOfGame.CLASSIC.init(0, "",
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
				.setBorder(BorderFactory.createTitledBorder("White Team"));
		whiteTeamPanel.setLayout(new GridBagLayout());

		JPanel whiteLegalDestinationsPanel = new JPanel();
		whiteLegalDestinationsPanel.setLayout(new GridBagLayout());
		constraints.gridx = 0;
		constraints.gridy = 1;
		whiteLegalDestinationsPanel.add(new JLabel(
				"<html><u> Legal Destination </u></br></html>"), constraints);
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
				"<html><u>After Capturing a piece</u></br></html>"),
				constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		whiteCapturePanel.add(whiteAfterCapturePanel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		whiteTeamPanel.add(whiteCapturePanel, constraints);

		JPanel blackTeamPanel = new JPanel();
		blackTeamPanel
				.setBorder(BorderFactory.createTitledBorder("Black Team"));
		blackTeamPanel.setLayout(new GridBagLayout());

		JPanel blackLegalDestinationsPanel = new JPanel();
		blackLegalDestinationsPanel.setLayout(new GridBagLayout());
		constraints.gridx = 0;
		constraints.gridy = 1;
		blackLegalDestinationsPanel.add(new JLabel(
				"<html><u> Legal Destination </u></br></html>"), constraints);
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
				"<html><u>After Capturing a piece</u></br></html>"),
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
				.createTitledBorder("Special rules"));
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
