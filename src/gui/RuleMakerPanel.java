package gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
import rules.Rules;
import utility.GuiUtility;

public class RuleMakerPanel extends JPanel
{
	public RuleMakerPanel(CustomSetupPanel customSetupMenu, JFrame optionsFrame)
	{
		m_frame = optionsFrame;
		m_frame.setVisible(true);
		m_frame.add(this);
		m_frame.setVisible(true);
		m_frame.setSize(600, 500);
		m_frame.setLocationRelativeTo(Driver.getInstance());
		m_builder = customSetupMenu.getBuilder();
		m_whiteRules = customSetupMenu.m_whiteRules;
		m_blackRules = customSetupMenu.m_blackRules;
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

		final Board[] boards = m_builder.getBoards();

		final JPanel whiteLegalDestinationPanel = new JPanel();
		whiteLegalDestinationPanel.setLayout(new GridLayout(2, 1));
		final JCheckBox whiteCaptureMandatoryCheckBox = new JCheckBox("Capture Mandatory");
		GuiUtility.requestFocus(whiteCaptureMandatoryCheckBox);
		whiteCaptureMandatoryCheckBox.setToolTipText("Capturing moves must be performed");
		whiteLegalDestinationPanel.add(whiteCaptureMandatoryCheckBox);

		final JCheckBox whiteNoMoveObjectiveCheckBox = new JCheckBox("Can't Move Objective");
		whiteNoMoveObjectiveCheckBox.setToolTipText("Moving the objective piece is illegal");
		if (m_whiteRules.getEndOfGame() == EndOfGame.CLASSIC || m_whiteRules.getEndOfGame() == EndOfGame.CHECK_N_TIMES)
			whiteLegalDestinationPanel.add(whiteNoMoveObjectiveCheckBox);

		final JPanel whiteAfterCaptureCheckBox = new JPanel();
		whiteAfterCaptureCheckBox.setLayout(new GridLayout(4, 1));
		final JCheckBox whiteChangeColorCheckBox = new JCheckBox("Capturer changes Color");
		whiteChangeColorCheckBox.setToolTipText("The capturing piece changes color after performing a capture");

		if (m_whiteRules.getEndOfGame() != EndOfGame.CLASSIC)
			whiteAfterCaptureCheckBox.add(whiteChangeColorCheckBox);

		final JCheckBox whitePieceReturnCheckBox = new JCheckBox("Captured piece returns to start");
		whitePieceReturnCheckBox.setToolTipText("Captured pieces return to their starting squares");
		whiteAfterCaptureCheckBox.add(whitePieceReturnCheckBox);
		final JCheckBox whiteDropPiecesCheckBox = new JCheckBox("Captured Pieces Drop");
		whiteDropPiecesCheckBox.setToolTipText("Captured pieces are placed in any open square on the board by the capturer");
		if (m_whiteRules.getEndOfGame() != EndOfGame.CAPTURE_ALL_OF_TYPE || m_whiteRules.getEndOfGame() != EndOfGame.LOSE_ALL_PIECES)
			whiteAfterCaptureCheckBox.add(whiteDropPiecesCheckBox);

		final JCheckBox whiteCapturedColorAndDropCheckBox = new JCheckBox("Captured Piece Changes Color and Drops");
		whiteCapturedColorAndDropCheckBox
				.setToolTipText("Captured pieces change teams and are placed in any open square on the board by the capturer");
		whiteAfterCaptureCheckBox.add(whiteCapturedColorAndDropCheckBox);

		final JPanel blackLegalDestinationPanel = new JPanel();
		blackLegalDestinationPanel.setLayout(new GridLayout(2, 1));
		final JCheckBox blackCaptureMandatoryCheckBox = new JCheckBox("Capture Mandatory");
		blackCaptureMandatoryCheckBox.setToolTipText("Capturing moves must be performed");
		blackLegalDestinationPanel.add(blackCaptureMandatoryCheckBox);
		final JCheckBox blackNoMoveObjectiveCheckBox = new JCheckBox("Can't Move Objective");
		blackNoMoveObjectiveCheckBox.setToolTipText("Moving the objective piece is illegal");
		if (m_blackRules.getEndOfGame() == EndOfGame.CLASSIC || m_blackRules.getEndOfGame() == EndOfGame.CHECK_N_TIMES)
			blackLegalDestinationPanel.add(blackNoMoveObjectiveCheckBox);

		final JPanel blackAfterCaptureCheckBox = new JPanel();
		blackAfterCaptureCheckBox.setLayout(new GridLayout(4, 1));
		final JCheckBox blackChangeColorCheckBox = new JCheckBox("Capturer changes Color");
		blackChangeColorCheckBox.setToolTipText("The capturing piece changes color after performing a capture");

		if (m_blackRules.getEndOfGame() != EndOfGame.CLASSIC)
			blackAfterCaptureCheckBox.add(blackChangeColorCheckBox);

		final JCheckBox blackPieceReturnCheckBox = new JCheckBox("Captured piece returns to start");
		blackPieceReturnCheckBox.setToolTipText("Captured pieces return to their starting squares");
		blackAfterCaptureCheckBox.add(blackPieceReturnCheckBox);
		final JCheckBox blackDropPiecesCheckBox = new JCheckBox("Captured Pieces Drop");
		blackDropPiecesCheckBox.setToolTipText("Captured pieces are placed in any open square on the board by the capturer");
		if (m_blackRules.getEndOfGame() != EndOfGame.CAPTURE_ALL_OF_TYPE || m_blackRules.getEndOfGame() != EndOfGame.LOSE_ALL_PIECES)
			blackAfterCaptureCheckBox.add(blackDropPiecesCheckBox);

		final JCheckBox blackCapturedColorAndDropCheckBox = new JCheckBox("Captured Piece Changes Color and Drops");
		blackCapturedColorAndDropCheckBox
				.setToolTipText("Captured pieces change teams and are placed in any open square on the board by the capturer");
		blackAfterCaptureCheckBox.add(blackCapturedColorAndDropCheckBox);

		final JPanel specialRulesPanel = new JPanel();
		specialRulesPanel.setLayout(new GridLayout(2, 1));

		final JCheckBox atomicChessCheckBox = new JCheckBox("Atomic Chess");
		atomicChessCheckBox
				.setToolTipText("Capture removes from play both capturer and the captured piece,\nas well as the pieces in the 8 surrounding squares (except for pawns)");
		specialRulesPanel.add(atomicChessCheckBox);

		final JCheckBox switchBoardsCheckBox = new JCheckBox("Move to other board");
		switchBoardsCheckBox.setToolTipText("Each piece moves to the opposite board every time it moves.");
		switchBoardsCheckBox.setEnabled(false);
		specialRulesPanel.add(switchBoardsCheckBox);

		final JPanel whiteExtrasPanel = new JPanel();
		whiteExtrasPanel.setLayout(new GridBagLayout());
		final JTextField whiteNumberOfChecksField = new JTextField(2);
		Object[] allPieces = PieceBuilder.getSet().toArray();
		final JComboBox whitePieceList = new JComboBox(allPieces);

		int whiteObjectiveIndex = 0;
		int blackObjectiveIndex = 0;
		for (int i = 0; i < allPieces.length; i++)
		{
			if (allPieces[i].equals(m_whiteRules.getObjectiveName()))
				whiteObjectiveIndex = i;
			if (allPieces[i].equals(m_blackRules.getObjectiveName()))
				blackObjectiveIndex = i;
		}

		JLabel whiteNumberOfChecksLabel = new JLabel("How many times for check?");
		JLabel whiteObjectivePieceLabel = new JLabel("Which piece is the objective?");

		whiteNumberOfChecksField.setVisible(false);
		whitePieceList.setVisible(false);

		if (m_whiteRules.getEndOfGame() == EndOfGame.CLASSIC || m_whiteRules.getEndOfGame() == EndOfGame.CAPTURE_ALL_OF_TYPE)
		{
			if (m_whiteRules.getEndOfGame() == EndOfGame.CAPTURE_ALL_OF_TYPE)
				whiteObjectivePieceLabel.setText("Which Piece type will be captured?");

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

		if (m_whiteRules.getEndOfGame() == EndOfGame.CHECK_N_TIMES)
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

		JLabel blackNumberOfChecksLabel = new JLabel("How many times for check?");
		JLabel blackObjectivePieceLabel = new JLabel("Which piece is the objective?");

		blackNumberOfChecksField.setVisible(false);
		blackPiecesList.setVisible(false);

		if (m_blackRules.getEndOfGame() == EndOfGame.CLASSIC || m_blackRules.getEndOfGame() == EndOfGame.CAPTURE_ALL_OF_TYPE)
		{
			if (m_blackRules.getEndOfGame() == EndOfGame.CAPTURE_ALL_OF_TYPE)
				blackObjectivePieceLabel.setText("Which Piece type will be captured?");

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

		if (m_blackRules.getEndOfGame() == EndOfGame.CHECK_N_TIMES)
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
		cancelButton.setToolTipText("Press me to return to the main Variant window");
		GuiUtility.setupVariantCancelButton(cancelButton, this, m_frame);

		JButton saveButton = new JButton("Save");
		saveButton.setToolTipText("Press me to save these rules");
		saveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (whiteCaptureMandatoryCheckBox.isSelected())
				{
					m_whiteRules.addAdjustTeamDestinations(AdjustTeamLegalDestinations.MUST_CAPTURE);
				}
				if (whiteNoMoveObjectiveCheckBox.isSelected())
				{
					m_whiteRules.addCropLegalDests(CropLegalDestinations.STATIONARY_OBJECTIVE);
				}
				if (whiteChangeColorCheckBox.isSelected())
				{
					m_whiteRules.addAfterMove(AfterMove.SWAP_COLOR_OF_CAPTURER);
				}
				if (whitePieceReturnCheckBox.isSelected())
				{
					m_whiteRules.addAfterMove(AfterMove.CAPTURED_PIECE_TO_ORIGIN);
				}
				if (whiteDropPiecesCheckBox.isSelected())
				{
					m_whiteRules.addAfterMove(AfterMove.CAPTURER_PLACES_CAPTURED);
				}
				if (whiteCapturedColorAndDropCheckBox.isSelected())
				{
					m_whiteRules.addAfterMove(AfterMove.CAPTURER_STEALS_CAPTURED);
				}
				if (blackCaptureMandatoryCheckBox.isSelected())
				{
					m_blackRules.addAdjustTeamDestinations(AdjustTeamLegalDestinations.MUST_CAPTURE);
				}
				if (blackNoMoveObjectiveCheckBox.isSelected())
				{
					m_blackRules.addCropLegalDests(CropLegalDestinations.STATIONARY_OBJECTIVE);
				}
				if (blackChangeColorCheckBox.isSelected())
				{
					m_blackRules.addAfterMove(AfterMove.SWAP_COLOR_OF_CAPTURER);
				}
				if (blackPieceReturnCheckBox.isSelected())
				{
					m_blackRules.addAfterMove(AfterMove.CAPTURED_PIECE_TO_ORIGIN);
				}
				if (blackDropPiecesCheckBox.isSelected())
				{
					m_blackRules.addAfterMove(AfterMove.CAPTURER_PLACES_CAPTURED);
				}
				if (blackCapturedColorAndDropCheckBox.isSelected())
				{
					m_blackRules.addAfterMove(AfterMove.CAPTURER_STEALS_CAPTURED);
				}
				if (switchBoardsCheckBox.isSelected() && boards.length == 2)
				{
					m_whiteRules.setGetBoard(GetBoard.OPPOSITE_BOARD);
					m_blackRules.setGetBoard(GetBoard.OPPOSITE_BOARD);
				}
				if (atomicChessCheckBox.isSelected())
				{
					m_whiteRules.addAfterMove(AfterMove.ATOMIC_CAPTURE);
					m_blackRules.addAfterMove(AfterMove.ATOMIC_CAPTURE);
				}

				if (whiteNumberOfChecksField.isVisible())
				{
					String whiteNumberOfChecks = whiteNumberOfChecksField.getText();
					try
					{
						int answer = Integer.parseInt(whiteNumberOfChecks);
						if (answer < 1)
						{
							JOptionPane.showMessageDialog(Driver.getInstance(), "Please enter a number greater than 1 into the Number of Checks box.",
									"Number of Checks", JOptionPane.PLAIN_MESSAGE);
							return;
						}
						m_whiteRules.addEndOfGame(EndOfGame.CHECK_N_TIMES.init(answer, "", false));
					}
					catch (Exception e)
					{
						JOptionPane.showMessageDialog(Driver.getInstance(), "Please enter a number into the Number of Checks box.",
								"Number of Checks", JOptionPane.PLAIN_MESSAGE);
						return;
					}
				}
				else if (whitePieceList.isVisible())
				{
					if (m_whiteRules.getEndOfGame() == EndOfGame.CLASSIC)
					{
						m_whiteRules.addEndOfGame(EndOfGame.CLASSIC.init(0, "", false));
						m_whiteRules.setObjectivePiece(ObjectivePiece.CUSTOM_OBJECTIVE.setObjectivePieceName(whitePieceList.getSelectedItem()
								.toString()));
					}
					else
					{
						m_whiteRules.addEndOfGame(EndOfGame.CAPTURE_ALL_OF_TYPE.init(0, whitePieceList.getSelectedItem().toString(),
								false));
					}
				}
				if (blackNumberOfChecksField.isVisible())
				{
					String blackNumberOfChecks = blackNumberOfChecksField.getText();
					try
					{
						int answer = Integer.parseInt(blackNumberOfChecks);
						if (answer < 1)
						{
							JOptionPane.showMessageDialog(Driver.getInstance(), "Please enter a number greater than 1 into the Number of Checks box.",
									"Number of Checks", JOptionPane.PLAIN_MESSAGE);
							return;
						}
						m_blackRules.addEndOfGame(EndOfGame.CHECK_N_TIMES.init(answer, "", true));
					}
					catch (Exception ne)
					{
						JOptionPane.showMessageDialog(Driver.getInstance(), "Please enter a number into the Number of Checks box.",
								"Number of Checks", JOptionPane.PLAIN_MESSAGE);
						return;
					}
				}
				else if (blackPiecesList.isVisible())
				{
					blackPiecesList.getSelectedItem();
					if (m_blackRules.getEndOfGame() == EndOfGame.CLASSIC)
					{
						m_blackRules.addEndOfGame(EndOfGame.CLASSIC.init(0, "", true));
						m_blackRules.setObjectivePiece(ObjectivePiece.CUSTOM_OBJECTIVE.setObjectivePieceName(blackPiecesList.getSelectedItem()
								.toString()));
					}
					else
					{
						m_blackRules.addEndOfGame(EndOfGame.CAPTURE_ALL_OF_TYPE.init(0, blackPiecesList.getSelectedItem().toString(),
								true));
					}
				}

				customSetupMenu.m_whiteRules = m_whiteRules;
				customSetupMenu.m_blackRules = m_blackRules;
				RuleMakerPanel.this.removeAll();
				m_frame.setVisible(false);
			}
		});

		JPanel whiteTeamPanel = new JPanel();
		whiteTeamPanel.setBorder(BorderFactory.createTitledBorder("White Team"));
		whiteTeamPanel.setLayout(new GridBagLayout());

		JPanel whiteLegalDestinationsPanel = new JPanel();
		whiteLegalDestinationsPanel.setLayout(new GridBagLayout());
		constraints.gridx = 0;
		constraints.gridy = 1;
		whiteLegalDestinationsPanel.add(new JLabel("<html><u> Legal Destination </u></br></html>"), constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		whiteLegalDestinationsPanel.add(whiteLegalDestinationPanel, constraints);

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
		whiteCapturePanel.add(new JLabel("<html><u>After Capturing a piece</u></br></html>"), constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		whiteCapturePanel.add(whiteAfterCaptureCheckBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		whiteTeamPanel.add(whiteCapturePanel, constraints);

		JPanel blackTeamPanel = new JPanel();
		blackTeamPanel.setBorder(BorderFactory.createTitledBorder("Black Team"));
		blackTeamPanel.setLayout(new GridBagLayout());

		JPanel blackLegalDestinationsPanel = new JPanel();
		blackLegalDestinationsPanel.setLayout(new GridBagLayout());
		constraints.gridx = 0;
		constraints.gridy = 1;
		blackLegalDestinationsPanel.add(new JLabel("<html><u> Legal Destination </u></br></html>"), constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		blackLegalDestinationsPanel.add(blackLegalDestinationPanel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 0;
		blackTeamPanel.add(blackLegalDestinationsPanel, constraints);

		JPanel blackCapturePanel = new JPanel();
		blackCapturePanel.setLayout(new GridBagLayout());
		constraints.gridheight = 1;
		constraints.gridx = 0;
		constraints.gridy = 1;
		blackCapturePanel.add(new JLabel("<html><u>After Capturing a piece</u></br></html>"), constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		blackCapturePanel.add(blackAfterCaptureCheckBox, constraints);

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
		specialRules.setBorder(BorderFactory.createTitledBorder("Special rules"));
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

		whiteChangeColorCheckBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (whiteChangeColorCheckBox.isSelected())
				{
					atomicChessCheckBox.setEnabled(false);
					atomicChessCheckBox.setSelected(false);
				}
				else
				{
					atomicChessCheckBox.setEnabled(true);
				}
			}
		});
		blackChangeColorCheckBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (blackChangeColorCheckBox.isSelected())
				{
					atomicChessCheckBox.setEnabled(false);
					atomicChessCheckBox.setSelected(false);
				}
				else
				{
					atomicChessCheckBox.setEnabled(true);
				}
			}
		});

		whitePieceReturnCheckBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (whitePieceReturnCheckBox.isSelected())
				{
					atomicChessCheckBox.setEnabled(false);
					atomicChessCheckBox.setSelected(false);
					whiteCapturedColorAndDropCheckBox.setEnabled(false);
					whiteCapturedColorAndDropCheckBox.setSelected(false);
					whiteDropPiecesCheckBox.setEnabled(false);
					whiteDropPiecesCheckBox.setSelected(false);
				}
				else
				{
					atomicChessCheckBox.setEnabled(true);
					whiteCapturedColorAndDropCheckBox.setEnabled(true);
					whiteDropPiecesCheckBox.setEnabled(true);
				}
			}
		});
		blackPieceReturnCheckBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (blackPieceReturnCheckBox.isSelected())
				{
					atomicChessCheckBox.setEnabled(false);
					atomicChessCheckBox.setSelected(false);
					blackCapturedColorAndDropCheckBox.setEnabled(false);
					blackCapturedColorAndDropCheckBox.setSelected(false);
					blackDropPiecesCheckBox.setEnabled(false);
					blackDropPiecesCheckBox.setSelected(false);
				}
				else
				{
					atomicChessCheckBox.setEnabled(true);
					blackCapturedColorAndDropCheckBox.setEnabled(true);
					blackDropPiecesCheckBox.setEnabled(true);
				}
			}
		});

		atomicChessCheckBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (atomicChessCheckBox.isSelected())
				{
					whiteChangeColorCheckBox.setEnabled(false);
					whiteChangeColorCheckBox.setSelected(false);
					blackChangeColorCheckBox.setEnabled(false);
					blackChangeColorCheckBox.setSelected(false);
					whiteCapturedColorAndDropCheckBox.setEnabled(false);
					whiteCapturedColorAndDropCheckBox.setSelected(false);
					blackCapturedColorAndDropCheckBox.setEnabled(false);
					blackCapturedColorAndDropCheckBox.setSelected(false);
					whiteDropPiecesCheckBox.setEnabled(false);
					whiteDropPiecesCheckBox.setSelected(false);
					blackDropPiecesCheckBox.setEnabled(false);
					blackDropPiecesCheckBox.setSelected(false);
					whitePieceReturnCheckBox.setEnabled(false);
					whitePieceReturnCheckBox.setSelected(false);
					blackPieceReturnCheckBox.setEnabled(false);
					blackPieceReturnCheckBox.setSelected(false);
				}
				else
				{
					whiteChangeColorCheckBox.setEnabled(true);
					blackChangeColorCheckBox.setEnabled(true);
					whiteCapturedColorAndDropCheckBox.setEnabled(true);
					blackCapturedColorAndDropCheckBox.setEnabled(true);
					whiteDropPiecesCheckBox.setEnabled(true);
					blackDropPiecesCheckBox.setEnabled(true);
					whitePieceReturnCheckBox.setEnabled(true);
					blackPieceReturnCheckBox.setEnabled(true);
				}
			}
		});

		whiteCapturedColorAndDropCheckBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (whiteCapturedColorAndDropCheckBox.isSelected())
				{
					atomicChessCheckBox.setEnabled(false);
					atomicChessCheckBox.setSelected(false);
					whiteDropPiecesCheckBox.setEnabled(false);
					whiteDropPiecesCheckBox.setSelected(false);
					whitePieceReturnCheckBox.setEnabled(false);
					whitePieceReturnCheckBox.setSelected(false);
				}
				else
				{
					atomicChessCheckBox.setEnabled(true);
					whiteDropPiecesCheckBox.setEnabled(true);
					whitePieceReturnCheckBox.setEnabled(true);
				}
			}
		});
		blackCapturedColorAndDropCheckBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (blackCapturedColorAndDropCheckBox.isSelected())
				{
					atomicChessCheckBox.setEnabled(false);
					atomicChessCheckBox.setSelected(false);
					blackDropPiecesCheckBox.setEnabled(false);
					blackDropPiecesCheckBox.setSelected(false);
					blackPieceReturnCheckBox.setEnabled(false);
					blackPieceReturnCheckBox.setSelected(false);
				}
				else
				{
					atomicChessCheckBox.setEnabled(true);
					blackDropPiecesCheckBox.setEnabled(true);
					blackPieceReturnCheckBox.setEnabled(true);
				}
			}
		});

		whiteDropPiecesCheckBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (whiteDropPiecesCheckBox.isSelected())
				{
					atomicChessCheckBox.setEnabled(false);
					atomicChessCheckBox.setSelected(false);
					whiteCapturedColorAndDropCheckBox.setEnabled(false);
					whiteCapturedColorAndDropCheckBox.setSelected(false);
					whitePieceReturnCheckBox.setEnabled(false);
					whitePieceReturnCheckBox.setSelected(false);
				}
				else
				{
					atomicChessCheckBox.setEnabled(true);
					whiteCapturedColorAndDropCheckBox.setEnabled(true);
					whitePieceReturnCheckBox.setEnabled(true);
				}
			}
		});
		blackDropPiecesCheckBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (blackDropPiecesCheckBox.isSelected())
				{
					atomicChessCheckBox.setEnabled(false);
					atomicChessCheckBox.setSelected(false);
					blackCapturedColorAndDropCheckBox.setEnabled(false);
					blackCapturedColorAndDropCheckBox.setSelected(false);
					blackPieceReturnCheckBox.setEnabled(false);
					blackPieceReturnCheckBox.setSelected(false);
				}
				else
				{
					atomicChessCheckBox.setEnabled(true);
					blackCapturedColorAndDropCheckBox.setEnabled(true);
					blackPieceReturnCheckBox.setEnabled(true);
				}
			}
		});

		if (boards.length == 2)
		{
			switchBoardsCheckBox.setEnabled(true);
		}
		m_frame.pack();
	}

	private static final long serialVersionUID = 8365806731061105370L;
	static boolean m_needsObjectivePiece = false;

	private Builder m_builder;
	private Rules m_whiteRules = new Rules(false);
	private Rules m_blackRules = new Rules(true);
	private JFrame m_frame;
}
