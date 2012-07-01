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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import logic.Board;
import logic.Builder;
import logic.PieceBuilder;
import rules.AdjustTeamDests;
import rules.EndOfGame;
import rules.GetBoard;
import rules.ObjectivePiece;
import rules.Rules;

/**
 * @author John McCormick Window to set up the special rules for the variants
 */
public class RuleMaker extends JPanel
{

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 8365806731061105370L;
	/**
	 * The Builder reference
	 */
	private Builder b;
	/**
	 * Rules for the White Team
	 */
	private Rules whiteRules;
	/**
	 * Rules for the Black Team
	 */
	private Rules blackRules;
	/**
	 * This is a boolean to see if we need to force a piece to be set as an
	 * Objective
	 */
	static boolean needsObj = false;

	/**
	 * Constructor for setting up the rules window
	 * 
	 * @param b the builder containing everything
	 * @param whiteRules The container for the white Rules
	 * @param blackRules the container for the black rules
	 */
	public RuleMaker(Builder b, Rules whiteRules, Rules blackRules)
	{
		this.b = b;
		this.whiteRules = whiteRules;
		this.blackRules = blackRules;
		initComponents();
	}

	/**
	 * Setting up the window and rules
	 */
	public void initComponents()
	{

		// Setting up the panel
		setLayout(new GridBagLayout());
		setSize(600, 600);
		setBorder(BorderFactory.createLoweredBevelBorder());
		GridBagConstraints c = new GridBagConstraints();

		final Board[] boards = b.getBoards();

		// Legal Destination Rules for White
		final JPanel legalDestWhiteCheckBox = new JPanel();
		legalDestWhiteCheckBox.setLayout(new GridLayout(2, 1));
		final JCheckBox wCaptureMand = new JCheckBox("Capture Mandatory");
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				wCaptureMand.requestFocus();
			}
		});
		wCaptureMand.setToolTipText("Capturing moves must be performed");
		legalDestWhiteCheckBox.add(wCaptureMand);
		final JCheckBox wNoMoveObj = new JCheckBox("Can't Move Objective");
		wNoMoveObj.setToolTipText("Moving the objective piece is illegal");
		if (whiteRules.theEndIsNigh().equals("classic") || whiteRules.theEndIsNigh().equals("checkNTimes"))
		{
			legalDestWhiteCheckBox.add(wNoMoveObj);
		}

		// Capturing pieces rules for White
		final JPanel afterCaptureWhiteCheckBox = new JPanel();
		afterCaptureWhiteCheckBox.setLayout(new GridLayout(4, 1));
		final JCheckBox wChangeColor = new JCheckBox("Capturer changes Color");
		wChangeColor.setToolTipText("The capturing piece changes color after performing a capture");
		if (!whiteRules.theEndIsNigh().equals("classic"))
		{
			afterCaptureWhiteCheckBox.add(wChangeColor);
		}
		final JCheckBox wPieceReturn = new JCheckBox("Captured piece returns to start");
		wPieceReturn.setToolTipText("Captured pieces return to their starting squares");
		afterCaptureWhiteCheckBox.add(wPieceReturn);
		final JCheckBox wDrop = new JCheckBox("Captured Pieces Drop");
		wDrop.setToolTipText("Captured pieces are placed in any open square on the board by the capturer");
		if (!whiteRules.theEndIsNigh().equals("captureAllOfType") || !whiteRules.theEndIsNigh().equals("loseAllPieces"))
		{
			afterCaptureWhiteCheckBox.add(wDrop);
		}
		final JCheckBox wCapturedColorAndDrop = new JCheckBox("Captured Piece Changes Color and Drops");
		wCapturedColorAndDrop
				.setToolTipText("Captured pieces change teams and are placed in any open square on the board by the capturer");
		afterCaptureWhiteCheckBox.add(wCapturedColorAndDrop);

		// Legal Destination Rules for Black
		final JPanel legalDestBlackCheckBox = new JPanel();
		legalDestBlackCheckBox.setLayout(new GridLayout(2, 1));
		final JCheckBox bCaptureMand = new JCheckBox("Capture Mandatory");
		bCaptureMand.setToolTipText("Capturing moves must be performed");
		legalDestBlackCheckBox.add(bCaptureMand);
		final JCheckBox bNoMoveObj = new JCheckBox("Can't Move Objective");
		bNoMoveObj.setToolTipText("Moving the objective piece is illegal");
		if (blackRules.theEndIsNigh().equals("classic") || blackRules.theEndIsNigh().equals("checkNTimes"))
		{
			legalDestBlackCheckBox.add(bNoMoveObj);
		}

		// Capturing pieces rules for Black
		final JPanel afterCapturepBlackCheckBox = new JPanel();
		afterCapturepBlackCheckBox.setLayout(new GridLayout(4, 1));
		final JCheckBox bChangeColor = new JCheckBox("Capturer changes Color");
		bChangeColor.setToolTipText("The capturing piece changes color after performing a capture");
		if (!blackRules.theEndIsNigh().equals("classic"))
		{
			afterCapturepBlackCheckBox.add(bChangeColor);
		}
		final JCheckBox bPieceReturn = new JCheckBox("Captured piece returns to start");
		bPieceReturn.setToolTipText("Captured pieces return to their starting squares");
		afterCapturepBlackCheckBox.add(bPieceReturn);
		final JCheckBox bDrop = new JCheckBox("Captured Pieces Drop");
		bDrop.setToolTipText("Captured pieces are placed in any open square on the board by the capturer");
		if (!blackRules.theEndIsNigh().equals("captureAllOfType") || !blackRules.theEndIsNigh().equals("loseAllPieces"))
		{
			afterCapturepBlackCheckBox.add(bDrop);
		}
		final JCheckBox bCapturedColorAndDrop = new JCheckBox("Captured Piece Changes Color and Drops");
		bCapturedColorAndDrop
				.setToolTipText("Captured pieces change teams and are placed in any open square on the board by the capturer");
		afterCapturepBlackCheckBox.add(bCapturedColorAndDrop);

		// Special overall rules
		final JPanel sCheckBox = new JPanel();
		sCheckBox.setLayout(new GridLayout(2, 1));
		final JCheckBox atomic = new JCheckBox("Atomic Chess");
		atomic.setToolTipText("Capture removes from play both capturer and the captured piece,\nas well as the pieces in the 8 surrounding squares (except for pawns)");
		sCheckBox.add(atomic);
		final JCheckBox switchBoard = new JCheckBox("Move to other board");
		switchBoard.setToolTipText("Each piece moves to the opposite board every time it moves.");
		switchBoard.setEnabled(false);
		sCheckBox.add(switchBoard);

		final JPanel wExtras = new JPanel();
		wExtras.setLayout(new GridBagLayout());
		final JTextField wNumChecks = new JTextField(2);
		Object[] allPieces = PieceBuilder.getSet().toArray();
		final JComboBox wPiecesList = new JComboBox(allPieces);

		JLabel wChecksLabel = new JLabel("How many times for check?");
		JLabel wPiecesLabel = new JLabel("Which piece is the objective?");

		wNumChecks.setVisible(false);
		wPiecesList.setVisible(false);

		if (whiteRules.theEndIsNigh().equals("classic") || whiteRules.theEndIsNigh().equals("captureAllOfType"))
		{
			if (whiteRules.theEndIsNigh().equals("captureAllOfType"))
				wPiecesLabel.setText("Which Piece type will be captured?");
			wPiecesList.setVisible(true);
			wPiecesList.setSelectedIndex(4);
			c.gridx = 0;
			c.gridy = 1;
			c.insets = new Insets(1, 1, 1, 1);
			wExtras.add(wPiecesLabel, c);
			c.gridx = 1;
			c.gridy = 1;
			wExtras.add(wPiecesList, c);
		}
		if (whiteRules.theEndIsNigh().equals("checkNTimes"))
		{
			wNumChecks.setVisible(true);
			wPiecesList.setVisible(true);
			wPiecesList.setSelectedIndex(4);
			c.gridx = 0;
			c.gridy = 0;
			wExtras.add(wChecksLabel, c);
			c.gridx = 1;
			c.gridy = 0;
			wExtras.add(wNumChecks, c);
			c.gridx = 0;
			c.gridy = 1;
			c.insets = new Insets(1, 1, 1, 1);
			wExtras.add(wPiecesLabel, c);
			c.gridx = 1;
			c.gridy = 1;
			wExtras.add(wPiecesList, c);
		}

		final JPanel bExtras = new JPanel();
		bExtras.setLayout(new GridBagLayout());
		final JTextField bNumChecks = new JTextField(2);
		final JComboBox bPiecesList = new JComboBox(allPieces);

		JLabel bChecksLabel = new JLabel("How many times for check?");
		JLabel bPiecesLabel = new JLabel("Which piece is the objective?");

		bNumChecks.setVisible(false);
		bPiecesList.setVisible(false);

		if (blackRules.theEndIsNigh().equals("classic") || blackRules.theEndIsNigh().equals("captureAllOfType"))
		{
			if (blackRules.theEndIsNigh().equals("captureAllOfType"))
				bPiecesLabel.setText("Which Piece type will be captured?");
			bPiecesList.setVisible(true);
			bPiecesList.setSelectedIndex(4);
			c.gridx = 0;
			c.gridy = 1;
			c.insets = new Insets(1, 1, 1, 1);
			bExtras.add(bPiecesLabel, c);
			c.gridx = 1;
			c.gridy = 1;
			bExtras.add(bPiecesList, c);
		}
		if (blackRules.theEndIsNigh().equals("checkNTimes"))
		{
			bNumChecks.setVisible(true);
			bPiecesList.setVisible(true);
			bPiecesList.setSelectedIndex(4);
			c.gridx = 0;
			c.gridy = 0;
			bExtras.add(bChecksLabel, c);
			c.gridx = 1;
			c.gridy = 0;
			bExtras.add(bNumChecks, c);
			c.gridx = 0;
			c.gridy = 1;
			c.insets = new Insets(1, 1, 1, 1);
			bExtras.add(bPiecesLabel, c);
			c.gridx = 1;
			c.gridy = 1;
			bExtras.add(bPiecesList, c);
		}

		// Create button and add ActionListener for going back
		final JButton back = new JButton("Back");
		back.setToolTipText("Press me to return to the Objective setup window");
		back.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				Driver.getInstance().setPanel(new ObjectiveMaker(b));
			}
		});

		// Button to move to save and move on
		JButton save = new JButton("Next");
		save.setToolTipText("Press me to save these rules");
		save.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (wCaptureMand.isSelected())
				{
					whiteRules.addAdjustTeamDests(new AdjustTeamDests("mustCapture"));
				}
				if (wNoMoveObj.isSelected())
				{
					whiteRules.addCropLegalDests("stationaryObjective");
				}
				if (wChangeColor.isSelected())
				{
					whiteRules.addAfterMove("captureTeamSwap");
				}
				if (wPieceReturn.isSelected())
				{
					whiteRules.addAfterMove("goHome");
				}
				if (wDrop.isSelected())
				{
					whiteRules.addAfterMove("placeCaptured");
				}
				if (wCapturedColorAndDrop.isSelected())
				{
					whiteRules.addAfterMove("placeCapturedSwitch");
				}
				if (bCaptureMand.isSelected())
				{
					blackRules.addAdjustTeamDests(new AdjustTeamDests("mustCapture"));
				}
				if (bNoMoveObj.isSelected())
				{
					blackRules.addCropLegalDests("stationaryObjective");
				}
				if (bChangeColor.isSelected())
				{
					blackRules.addAfterMove("captureTeamSwap");
				}
				if (bPieceReturn.isSelected())
				{
					blackRules.addAfterMove("goHome");
				}
				if (bDrop.isSelected())
				{
					blackRules.addAfterMove("placeCaptured");
				}
				if (bCapturedColorAndDrop.isSelected())
				{
					blackRules.addAfterMove("placeCapturedSwitch");
				}
				if (switchBoard.isSelected() && boards.length == 2)
				{
					whiteRules.setGetBoard(new GetBoard("oppositeBoard"));
					blackRules.setGetBoard(new GetBoard("oppositeBoard"));
				}
				if (atomic.isSelected())
				{
					whiteRules.addAfterMove("atomicCapture");
					blackRules.addAfterMove("atomicCapture");
				}

				if (wNumChecks.isVisible())
				{
					String wNumChecked = wNumChecks.getText();
					try
					{
						int answer = Integer.parseInt(wNumChecked);
						if (answer < 1)
						{
							JOptionPane.showMessageDialog(null, "Please enter a number greater than 1 into the Number of Checks box.");
							return;
						}
						whiteRules.addEndOfGame(new EndOfGame("checkNTimes", answer, "", false));
					}
					catch (Exception ne)
					{
						JOptionPane.showMessageDialog(null, "Please enter a number into the Number of Checks box.");
						return;
					}
				}
				else if (wPiecesList.isVisible())
				{
					if (whiteRules.theEndIsNigh().equals("classic"))
					{
						whiteRules.addEndOfGame(new EndOfGame("classic", 0, "", false));
						whiteRules.setObjectivePiece(new ObjectivePiece("custom objective", wPiecesList.getSelectedItem().toString()));
					}
					else
					{
						whiteRules.addEndOfGame(new EndOfGame("captureAllOfType", 0, wPiecesList.getSelectedItem().toString(), false));
					}
				}
				if (bNumChecks.isVisible())
				{
					String bNumChecked = bNumChecks.getText();
					try
					{
						int answer = Integer.parseInt(bNumChecked);
						if (answer < 1)
						{
							JOptionPane.showMessageDialog(null, "Please enter a number greater than 1 into the Number of Checks box.");
							return;
						}
						blackRules.addEndOfGame(new EndOfGame("checkNTimes", answer, "", true));
					}
					catch (Exception ne)
					{
						JOptionPane.showMessageDialog(null, "Please enter a number into the Number of Checks box.");
						return;
					}
				}
				else if (bPiecesList.isVisible())
				{
					bPiecesList.getSelectedItem();
					if (blackRules.theEndIsNigh().equals("classic"))
					{
						blackRules.addEndOfGame(new EndOfGame("classic", 0, "", true));
						blackRules.setObjectivePiece(new ObjectivePiece("custom objective", bPiecesList.getSelectedItem().toString()));
					}
					else
					{
						blackRules.addEndOfGame(new EndOfGame("captureAllOfType", 0, bPiecesList.getSelectedItem().toString(), true));
					}
				}

				Driver.getInstance().setPanel(new PlayerCustomMenu(b, whiteRules, blackRules));
			}
		});

		// Setting up the window

		// Setting up the white team
		JPanel whiteTeam = new JPanel();
		whiteTeam.setBorder(BorderFactory.createTitledBorder("White Team"));
		whiteTeam.setLayout(new GridBagLayout());

		JPanel whiteLegalDests = new JPanel();
		whiteLegalDests.setLayout(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 1;
		whiteLegalDests.add(new JLabel("<html><u> Legal Destination </u></br></html>"), c);
		c.gridx = 0;
		c.gridy = 2;
		whiteLegalDests.add(legalDestWhiteCheckBox, c);

		c.gridx = 0;
		c.gridy = 0;
		whiteTeam.add(whiteLegalDests, c);

		c.insets = new Insets(1, 3, 3, 3);
		c.gridx = 0;
		c.gridy = 1;
		whiteTeam.add(wExtras, c);

		JPanel whiteCapture = new JPanel();
		whiteCapture.setLayout(new GridBagLayout());
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 1;
		whiteCapture.add(new JLabel("<html><u>After Capturing a piece</u></br></html>"), c);
		c.gridx = 0;
		c.gridy = 2;
		whiteCapture.add(afterCaptureWhiteCheckBox, c);

		c.gridx = 0;
		c.gridy = 2;
		whiteTeam.add(whiteCapture, c);

		// Setting up the black team
		JPanel blackTeam = new JPanel();
		blackTeam.setBorder(BorderFactory.createTitledBorder("Black Team"));
		blackTeam.setLayout(new GridBagLayout());

		JPanel blackLegalDests = new JPanel();
		blackLegalDests.setLayout(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 1;
		blackLegalDests.add(new JLabel("<html><u> Legal Destination </u></br></html>"), c);
		c.gridx = 0;
		c.gridy = 2;
		blackLegalDests.add(legalDestBlackCheckBox, c);

		c.gridx = 0;
		c.gridy = 0;
		blackTeam.add(blackLegalDests, c);

		JPanel blackCapture = new JPanel();
		blackCapture.setLayout(new GridBagLayout());
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 1;
		blackCapture.add(new JLabel("<html><u>After Capturing a piece</u></br></html>"), c);
		c.gridx = 0;
		c.gridy = 2;
		blackCapture.add(afterCapturepBlackCheckBox, c);

		c.insets = new Insets(1, 3, 3, 3);
		c.gridx = 0;
		c.gridy = 1;
		blackTeam.add(bExtras, c);

		c.gridx = 0;
		c.gridy = 2;
		blackTeam.add(blackCapture, c);

		// Adding White team to window
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 0;
		add(whiteTeam, c);

		// Adding black team to the window
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 1;
		c.gridy = 0;
		add(blackTeam, c);

		// Adding special rules to the window
		JPanel specialRules = new JPanel();
		specialRules.setBorder(BorderFactory.createTitledBorder("Special rules"));
		specialRules.setLayout(new GridBagLayout());

		c.gridx = 0;
		c.gridy = 1;
		specialRules.add(sCheckBox, c);
		c.gridx = 0;
		c.gridy = 2;

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		add(specialRules, c);

		// Adding Buttons to the window
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout());
		buttons.add(back);
		buttons.add(save);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		add(buttons, c);

		// Setting up which buttons turn off others
		// Capture All

		// Capturer Changes color
		wChangeColor.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (wChangeColor.isSelected())
				{
					atomic.setEnabled(false);
					atomic.setSelected(false);
				}
				else
				{
					atomic.setEnabled(true);
				}
			}
		});
		bChangeColor.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (bChangeColor.isSelected())
				{
					atomic.setEnabled(false);
					atomic.setSelected(false);
				}
				else
				{
					atomic.setEnabled(true);
				}
			}
		});

		// Captured returns to start
		wPieceReturn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (wPieceReturn.isSelected())
				{
					atomic.setEnabled(false);
					atomic.setSelected(false);
					wCapturedColorAndDrop.setEnabled(false);
					wCapturedColorAndDrop.setSelected(false);
					wDrop.setEnabled(false);
					wDrop.setSelected(false);
				}
				else
				{
					atomic.setEnabled(true);
					wCapturedColorAndDrop.setEnabled(true);
					wDrop.setEnabled(true);
				}
			}
		});
		bPieceReturn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (bPieceReturn.isSelected())
				{
					atomic.setEnabled(false);
					atomic.setSelected(false);
					bCapturedColorAndDrop.setEnabled(false);
					bCapturedColorAndDrop.setSelected(false);
					bDrop.setEnabled(false);
					bDrop.setSelected(false);
				}
				else
				{
					atomic.setEnabled(true);
					bCapturedColorAndDrop.setEnabled(true);
					bDrop.setEnabled(true);
				}
			}
		});

		// Atomic chess
		atomic.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (atomic.isSelected())
				{
					wChangeColor.setEnabled(false);
					wChangeColor.setSelected(false);
					bChangeColor.setEnabled(false);
					bChangeColor.setSelected(false);
					wCapturedColorAndDrop.setEnabled(false);
					wCapturedColorAndDrop.setSelected(false);
					bCapturedColorAndDrop.setEnabled(false);
					bCapturedColorAndDrop.setSelected(false);
					wDrop.setEnabled(false);
					wDrop.setSelected(false);
					bDrop.setEnabled(false);
					bDrop.setSelected(false);
					wPieceReturn.setEnabled(false);
					wPieceReturn.setSelected(false);
					bPieceReturn.setEnabled(false);
					bPieceReturn.setSelected(false);
				}
				else
				{
					wChangeColor.setEnabled(true);
					bChangeColor.setEnabled(true);
					wCapturedColorAndDrop.setEnabled(true);
					bCapturedColorAndDrop.setEnabled(true);
					wDrop.setEnabled(true);
					bDrop.setEnabled(true);
					wPieceReturn.setEnabled(true);
					bPieceReturn.setEnabled(true);
				}
			}
		});

		// Captured Pieces color change and drop
		wCapturedColorAndDrop.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (wCapturedColorAndDrop.isSelected())
				{
					atomic.setEnabled(false);
					atomic.setSelected(false);
					wDrop.setEnabled(false);
					wDrop.setSelected(false);
					wPieceReturn.setEnabled(false);
					wPieceReturn.setSelected(false);
				}
				else
				{
					atomic.setEnabled(true);
					wDrop.setEnabled(true);
					wPieceReturn.setEnabled(true);
				}
			}
		});
		bCapturedColorAndDrop.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (bCapturedColorAndDrop.isSelected())
				{
					atomic.setEnabled(false);
					atomic.setSelected(false);
					bDrop.setEnabled(false);
					bDrop.setSelected(false);
					bPieceReturn.setEnabled(false);
					bPieceReturn.setSelected(false);
				}
				else
				{
					atomic.setEnabled(true);
					bDrop.setEnabled(true);
					bPieceReturn.setEnabled(true);
				}
			}
		});

		// Captured Pieces dropped
		wDrop.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (wDrop.isSelected())
				{
					atomic.setEnabled(false);
					atomic.setSelected(false);
					wCapturedColorAndDrop.setEnabled(false);
					wCapturedColorAndDrop.setSelected(false);
					wPieceReturn.setEnabled(false);
					wPieceReturn.setSelected(false);
				}
				else
				{
					atomic.setEnabled(true);
					wCapturedColorAndDrop.setEnabled(true);
					wPieceReturn.setEnabled(true);
				}
			}
		});
		bDrop.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (bDrop.isSelected())
				{
					atomic.setEnabled(false);
					atomic.setSelected(false);
					bCapturedColorAndDrop.setEnabled(false);
					bCapturedColorAndDrop.setSelected(false);
					bPieceReturn.setEnabled(false);
					bPieceReturn.setSelected(false);
				}
				else
				{
					atomic.setEnabled(true);
					bCapturedColorAndDrop.setEnabled(true);
					bPieceReturn.setEnabled(true);
				}
			}
		});

		// Move to other Board
		if (boards.length == 2)
		{
			switchBoard.setEnabled(true);
		}
	}
}
