package gui;

import gui.PieceMakerPanel.PieceListChangedListener;
import gui.PreferenceUtility.PieceToolTipPreferenceChangedListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import logic.GameBuilder;
import logic.PieceBuilder;
import models.Board;
import models.Game;
import models.Piece;
import models.Rules;
import models.Square;
import rules.EndOfGame;
import rules.ObjectivePiece;
import rules.ObjectivePiece.ObjectivePieceTypes;
import utility.FileUtility;
import utility.GuiUtility;
import utility.PieceIconUtility;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dragNdrop.AbstractDropManager;
import dragNdrop.DropAdapter;
import dragNdrop.DropEvent;
import dragNdrop.GlassPane;
import dragNdrop.MotionAdapter;

public class VariantCreationPanel extends ChessPanel implements PieceListChangedListener
{
	public VariantCreationPanel(String variantName)
	{
		mScrollPane = new JScrollPane();
		mVariantNameField = new JTextField(25);
		mDropManager = new DropManager();
		mGlobalGlassPane = new GlassPane();
		mGlobalGlassPane.setOpaque(false);
		Driver.getInstance().setGlassPane(mGlobalGlassPane);
		m_motionAdapter = new MotionAdapter(mGlobalGlassPane);
		mPieceListPanel = new JPanel();
		mPieceListPanel.setOpaque(false);
		mPieceListBoardPanel = new JPanel();
		mPieceListBoardPanel.setOpaque(false);

		Game gameToEdit = null;
		if (variantName != null)
		{
			// FIXME: we shouldn't be constructing an actual Game object here;
			// we should be able to get everything we need from the GameBuilder
			gameToEdit = GameBuilder.newGame(variantName);

			mWhiteTeam = gameToEdit.getWhiteTeam();
			mBlackTeam = gameToEdit.getBlackTeam();

			mWhiteRules = gameToEdit.getWhiteRules();
			mBlackRules = gameToEdit.getBlackRules();
			mWhitePromotionMap = gameToEdit.getWhitePromotionMap();
			if (mWhitePromotionMap == null)
				mWhitePromotionMap = Maps.newHashMap();

			mBlackPromotionMap = gameToEdit.getBlackPromotionMap();
			if (mBlackPromotionMap == null)
				mBlackPromotionMap = Maps.newHashMap();

			mBoardPanels = new JPanel[gameToEdit.getBoards().length];

			for (int i = 0; i < mBoardPanels.length; i++)
				mBoardPanels[i] = new JPanel();

			mBuilder = new GameBuilder(variantName, gameToEdit.getBoards(), mWhiteTeam, mBlackTeam, mWhiteRules, mBlackRules);
		}
		else
		{
			mBuilder = new GameBuilder(Messages.getString("VariantCreationPanel.newVariant")); //$NON-NLS-1$

			mWhiteTeam = Lists.newArrayList();
			mBlackTeam = Lists.newArrayList();

			mWhiteRules = new Rules(false);
			mBlackRules = new Rules(true);

			mWhitePromotionMap = Maps.newHashMap();
			mBlackPromotionMap = Maps.newHashMap();
			mBoardPanels = new JPanel[] { new JPanel(), new JPanel() };

			mBuilder.setBlackTeam(mBlackTeam);
			mBuilder.setWhiteTeam(mWhiteTeam);
		}

		mOptionsFrame = new JFrame();
		mOptionsFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		initGUIComponents(gameToEdit, variantName);
	}

	private void initGUIComponents(final Game game, String variantName)
	{

		setupPiecesList();

		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		setBorder(BorderFactory.createLoweredBevelBorder());

		if (game == null)
		{
			mWhiteRules.addEndOfGame(EndOfGame.CLASSIC.init(0, Messages.getString("VariantCreationPanel.empty"), false)); //$NON-NLS-1$
			mBlackRules.addEndOfGame(EndOfGame.CLASSIC.init(0, Messages.getString("VariantCreationPanel.empty"), true)); //$NON-NLS-1$

			mWhiteRules.setObjectivePiece(new ObjectivePiece(ObjectivePieceTypes.CLASSIC));
			mBlackRules.setObjectivePiece(new ObjectivePiece(ObjectivePieceTypes.CLASSIC));
		}

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(10, 10, 10, 5);
		constraints.anchor = GridBagConstraints.CENTER;
		add(GuiUtility.createJLabel(Messages.getString("VariantCreationPanel.variantName")), constraints); //$NON-NLS-1$

		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(0, 0, 0, 0);
		add(mVariantNameField, constraints);

		Board[] temp = new Board[game == null ? 1 : game.getBoards().length];

		if (game == null)
		{
			GuiUtility.requestFocus(mVariantNameField);
			temp[0] = new Board(8, 8, false);
		}
		else
		{
			mVariantNameField.setText(variantName);
			for (int i = 0; i < game.getBoards().length; i++)
			{
				temp[i] = game.getBoards()[i];
			}
		}

		drawBoards(temp);

		// main menu button
		JButton returnToMainButton = new JButton(Messages.getString("VariantCreationPanel.returnToMainMenu")); //$NON-NLS-1$
		// returnToMainButton.setToolTipText("Press to return to the Main Menu");
		returnToMainButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				PreferenceUtility.clearTooltipListeners();
				Driver.getInstance().revertToMainPanel();
			}
		});

		// Create button and add ActionListener
		JButton submitButton = new JButton(Messages.getString("VariantCreationPanel.saveAndQuit")); //$NON-NLS-1$
		// submitButton.setToolTipText("Save a finished variant");
		submitButton.addActionListener(submitButtonActionListener);

		JButton boardSetupButton = new JButton(Messages.getString("VariantCreationPanel.customizeGameBoard")); //$NON-NLS-1$
		boardSetupButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				mOptionsFrame.dispose();
				mOptionsFrame = new JFrame(Messages.getString("VariantCreationPanel.customBoardSetup")); //$NON-NLS-1$

				new CustomBoardPanel(VariantCreationPanel.this, mOptionsFrame);
			}
		});

		JButton ruleSetupButton = new JButton(Messages.getString("VariantCreationPanel.setUpGameRules")); //$NON-NLS-1$
		ruleSetupButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				mOptionsFrame.dispose();
				mOptionsFrame = new JFrame();

				new RuleMakerPanel(VariantCreationPanel.this, mOptionsFrame);
			}
		});

		JButton playerSetupButton = new JButton(Messages.getString("VariantCreationPanel.setUpPlayerRules")); //$NON-NLS-1$
		playerSetupButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				mOptionsFrame.dispose();
				mOptionsFrame = new JFrame();
				new CustomPlayerPanel(VariantCreationPanel.this, mOptionsFrame);
			}
		});

		JButton pieceSetupButton = new JButton(Messages.getString("VariantCreationPanel.newPiece")); //$NON-NLS-1$
		pieceSetupButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				openPieceMakerPanel(null);
			}
		});

		GridBagConstraints mainPanelConstraints = new GridBagConstraints();

		mPieceListPanel.setLayout(new GridBagLayout());
		mainPanelConstraints.gridx = 0;
		mainPanelConstraints.gridy = 0;
		mainPanelConstraints.gridwidth = 2;
		mainPanelConstraints.insets = new Insets(5, 3, 3, 3);
		mScrollPane.setPreferredSize(new Dimension(270, 350));
		mScrollPane.setHorizontalScrollBar(null);
		mScrollPane.setMaximumSize(new Dimension(270, 350));
		mPieceListPanel.add(mScrollPane, mainPanelConstraints);

		mainPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
		mainPanelConstraints.gridwidth = 1;
		mainPanelConstraints.insets = new Insets(5, 3, 3, 3);
		mainPanelConstraints.gridx = 0;
		mainPanelConstraints.gridy = 2;
		mPieceListPanel.add(boardSetupButton, mainPanelConstraints);

		mainPanelConstraints.gridx = 1;
		mainPanelConstraints.gridy = 2;
		mPieceListPanel.add(playerSetupButton, mainPanelConstraints);

		mainPanelConstraints.gridx = 0;
		mainPanelConstraints.gridy = 3;
		mPieceListPanel.add(ruleSetupButton, mainPanelConstraints);

		mainPanelConstraints.gridx = 1;
		mainPanelConstraints.gridy = 3;
		mPieceListPanel.add(pieceSetupButton, mainPanelConstraints);

		JPanel optionsPanel = new JPanel();
		optionsPanel.setOpaque(false);
		optionsPanel.setLayout(new GridBagLayout());

		mainPanelConstraints.fill = GridBagConstraints.CENTER;
		mainPanelConstraints.gridx = 0;
		mainPanelConstraints.gridy = 1;
		mainPanelConstraints.gridwidth = 1;
		optionsPanel.add(submitButton, mainPanelConstraints);

		mainPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
		mainPanelConstraints.gridx = 1;
		mainPanelConstraints.gridy = 1;
		optionsPanel.add(returnToMainButton, mainPanelConstraints);

		constraints.gridy = 2;
		constraints.gridx = 1;
		constraints.gridwidth = 2;
		constraints.insets = new Insets(10, 0, 10, 5);
		add(optionsPanel, constraints);

		mainPanelConstraints.fill = GridBagConstraints.NONE;
		mainPanelConstraints.insets = new Insets(0, 0, 0, 0);
		mainPanelConstraints.gridwidth = 1;
		mainPanelConstraints.gridx = 6;
		mainPanelConstraints.gridy = 1;
		add(mPieceListPanel, mainPanelConstraints);

	}

	public void drawBoards(Board[] boards)
	{
		// Get the set the board into the builder.
		mBuilder.setBoards(boards);
		mGameBoards = boards;

		for (JPanel panel : mBoardPanels)
		{
			panel.removeAll();
			remove(panel);
		}

		mBoardPanels = new JPanel[boards.length];

		GridBagConstraints constraints = new GridBagConstraints();

		List<SquareJLabel> squareLabelList = Lists.newArrayListWithExpectedSize(boards[0].getMaxRow() * boards[0].getMaxCol() * 2);
		for (int boardIndex = 0, gridxConstraint = 1; boardIndex < boards.length; boardIndex++, gridxConstraint += 2)
		{
			// create a JPanel to hold the grid and set the layout to the number
			// of squares in the board
			mBoardPanels[boardIndex] = new JPanel();
			mBoardPanels[boardIndex].setLayout(new GridLayout(boards[boardIndex].numRows(), boards[boardIndex].numCols()));

			int numberOfRows = boards[boardIndex].numRows();
			int numberOfColumns = boards[boardIndex].numCols();
			for (int row = numberOfRows; row > 0; row--)
			{
				for (int column = 1; column <= numberOfColumns; column++)
				{
					Square square = boards[boardIndex].getSquare(row, column);
					SquareJLabel squareLabel = new SquareJLabel(square);
					if (square.isOccupied())
					{
						boolean isValidPieceType = false;
						for (int i = 0; i < mAllPieceNames.length; i++)
						{
							if (!mAllPieceNames[i].equals(square.getPiece().getName()))
								continue;

							isValidPieceType = true;

							Piece platonicIdeal = null;
							try
							{
								platonicIdeal = PieceBuilder.makePiece(mAllPieceNames[i], square.getPiece().isBlack(), square,
										boards[boardIndex]);
							}
							catch (IOException e)
							{
								e.printStackTrace();
							}
							if (!square.getPiece().equals(platonicIdeal))
								square.setPiece(platonicIdeal);

						}
						if (!isValidPieceType)
						{
							square.setPiece(null);
						}

						squareLabel.addMouseListener(new PieceNormalBoardListener(squareLabel));
						squareLabel.addMouseMotionListener(m_motionAdapter);
					}
					else
					{
						squareLabel.addMouseListener(new SquareSetupMouseListener(squareLabel));
					}
					squareLabelList.add(squareLabel);
					mBoardPanels[boardIndex].add(squareLabel);
					squareLabel.refresh();
				}
			}

			mBoardPanels[boardIndex].setBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.GRAY));
			mBoardPanels[boardIndex].setLayout(new GridLayout(boards[boardIndex].numRows(), boards[boardIndex].numCols()));
			// set the size of the grid to the number of rows and columns,
			// scaled by 48, the size of the images
			mBoardPanels[boardIndex].setPreferredSize(new Dimension(boards[boardIndex].numCols() * 48,
					boards[boardIndex].numRows() * 48));

			constraints.insets = new Insets(3, 5, 3, 10);
			constraints.gridx = gridxConstraint;
			constraints.gridy = 1;
			constraints.gridwidth = 2;

			add(mBoardPanels[boardIndex], constraints);
		}
		mDropManager.setComponentList(squareLabelList);

		for (JPanel panel : mBoardPanels)
		{
			panel.revalidate();
			panel.repaint();
		}

		Driver.getInstance().pack();
	}

	public void setupPiecesList()
	{
		mAllPieceNames = PieceBuilder.getSortedArrayWithCustomPieces();

		JPanel pieceListBoardPanel = new JPanel(new GridBagLayout());

		GridBagConstraints outerConstraints = new GridBagConstraints();
		outerConstraints.anchor = GridBagConstraints.CENTER;

		mDisplayBoard = new Board(mAllPieceNames.length, 2, false);

		Icon editIcon = null;
		Icon deleteIcon = null;
		Icon promoteIcon = null;
		try
		{
			editIcon = GuiUtility.createImageIcon(14, 14, "/edit_icon.png"); //$NON-NLS-1$
			deleteIcon = GuiUtility.createImageIcon(14, 14, "/delete_icon.png"); //$NON-NLS-1$
			promoteIcon = GuiUtility.createImageIcon(14, 14, "/promote_icon.png"); //$NON-NLS-1$
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		int rowIndex = 0;
		boolean gray = true;
		for (String pieceName : mAllPieceNames)
		{
			Square whiteSquare = mDisplayBoard.getSquare(rowIndex + 1, 1);
			Square blackSquare = mDisplayBoard.getSquare(rowIndex + 1, 2);

			SquareJLabel whiteSquareLabel = new SquareJLabel(whiteSquare);
			SquareJLabel blackSquareLabel = new SquareJLabel(blackSquare);

			try
			{
				whiteSquare.setPiece(PieceBuilder.makePiece(pieceName, false, whiteSquare, mDisplayBoard));
				blackSquare.setPiece(PieceBuilder.makePiece(pieceName, true, blackSquare, mDisplayBoard));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			whiteSquareLabel.setBackgroundColor(gray ? Color.LIGHT_GRAY : Color.getHSBColor(30, 70, 70));
			blackSquareLabel.setBackgroundColor(!gray ? Color.LIGHT_GRAY : Color.getHSBColor(30, 70, 70));
			gray = !gray;

			whiteSquareLabel.setMaximumSize(new Dimension(48, 48));
			blackSquareLabel.setMaximumSize(new Dimension(48, 48));

			whiteSquareLabel.addMouseListener(new PieceDisplayBoardListener(whiteSquareLabel));
			blackSquareLabel.addMouseListener(new PieceDisplayBoardListener(blackSquareLabel));

			whiteSquareLabel.addMouseMotionListener(m_motionAdapter);
			blackSquareLabel.addMouseMotionListener(m_motionAdapter);

			JPanel namePanel = new JPanel(new GridBagLayout());
			namePanel.setMaximumSize(new Dimension(150, 50));

			GridBagConstraints constraints = new GridBagConstraints();

			constraints.anchor = GridBagConstraints.CENTER;
			constraints.gridy = 0;
			constraints.gridx = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			constraints.insets = new Insets(0, 5, 0, 5);
			JLabel nameLabel = new JLabel(pieceName);
			nameLabel.setHorizontalAlignment(JLabel.CENTER);
			namePanel.add(nameLabel, constraints);

			JPanel editDeletePanel = new JPanel(new GridBagLayout());

			constraints.gridx = 0;
			constraints.gridy = 1;
			constraints.gridheight = 1;
			constraints.gridwidth = 1;
			constraints.insets = new Insets(0, 0, 0, 0);
			JButton promoteButton = new JButton(promoteIcon);
			promoteButton.setToolTipText(Messages.getString("VariantCreationPanel.setupPromotions")); //$NON-NLS-1$
			promoteButton.addActionListener(new PromotePieceActionListener(pieceName));
			editDeletePanel.add(promoteButton, constraints);

			if (!PieceBuilder.isClassicPieceType(pieceName))
			{
				constraints.gridx = 1;
				JButton editButton = new JButton(editIcon);
				editButton.setToolTipText(Messages.getString("VariantCreationPanel.editPiece")); //$NON-NLS-1$
				editButton.addActionListener(new EditPieceActionListener(pieceName));
				editDeletePanel.add(editButton, constraints);

				constraints.gridx = 2;
				constraints.gridy = 1;
				constraints.gridheight = 1;
				constraints.gridwidth = 1;
				JButton deleteButton = new JButton(deleteIcon);
				deleteButton.setToolTipText(Messages.getString("VariantCreationPanel.deletePiece")); //$NON-NLS-1$
				deleteButton.addActionListener(new DeletePieceActionListener(pieceName));
				editDeletePanel.add(deleteButton, constraints);
			}
			constraints.gridx = 0;
			constraints.gridy = 1;
			namePanel.add(editDeletePanel, constraints);

			outerConstraints.gridheight = 1;
			outerConstraints.gridwidth = 1;
			outerConstraints.gridx = 0;
			outerConstraints.gridy = rowIndex;
			outerConstraints.weightx = 0.5;
			pieceListBoardPanel.add(namePanel, outerConstraints);

			JPanel squareHolder = new JPanel(new GridLayout(1, 2));
			squareHolder.add(whiteSquareLabel);
			squareHolder.add(blackSquareLabel);

			outerConstraints.gridx = 1;
			outerConstraints.gridy = rowIndex;
			outerConstraints.gridheight = 1;
			outerConstraints.gridwidth = 1;
			pieceListBoardPanel.add(squareHolder, outerConstraints);

			whiteSquareLabel.refresh();
			blackSquareLabel.refresh();
			rowIndex++;
		}

		int oldScrollValue = mScrollPane.getVerticalScrollBar().getValue();
		mScrollPane.setViewportView(pieceListBoardPanel);
		mScrollPane.getVerticalScrollBar().setValue(oldScrollValue);
	}

	public GameBuilder getBuilder()
	{
		return mBuilder;
	}

	public void setBuilder(GameBuilder builder)
	{
		mBuilder = builder;
	}

	public void setWhiteRules(Rules whiteRules)
	{
		mWhiteRules = whiteRules;
	}

	public void setBlackRules(Rules blackRules)
	{
		mBlackRules = blackRules;
	}

	private class EditPieceActionListener implements ActionListener
	{
		public EditPieceActionListener(String pieceName)
		{
			mPieceName = pieceName;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			openPieceMakerPanel(mPieceName);
		}

		private String mPieceName;
	}

	private class DeletePieceActionListener implements ActionListener
	{
		public DeletePieceActionListener(String pieceName)
		{
			mPieceName = pieceName;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			switch (JOptionPane.showConfirmDialog(Driver.getInstance(), Messages
					.getString("VariantCreationPanel.sureAboutDeletingPiece"), Messages.getString("VariantCreationPanel.warning"), //$NON-NLS-1$ //$NON-NLS-2$
					JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE))
			{
			case JOptionPane.YES_OPTION:
				FileUtility.deletePiece(mPieceName);
				setupPiecesList();
				drawBoards(mGameBoards);
			case JOptionPane.NO_OPTION:
			default:
				return;
			}
		}

		private String mPieceName;
	}

	private class PromotePieceActionListener implements ActionListener
	{
		public PromotePieceActionListener(String name)
		{
			mPieceName = name;
		}

		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			openPiecePromotePanel(mPieceName);
		}

		private String mPieceName;

	}

	private final class SquareSetupMouseListener extends MouseAdapter
	{
		public SquareSetupMouseListener(SquareJLabel squareLabel)
		{
			mSquareLabel = squareLabel;
			if (mSquareLabel.getSquare().isOccupied())
				mSquareLabel.refresh();
		}

		@Override
		public void mouseClicked(MouseEvent event)
		{
			if (mSquareLabel.getSquare().isOccupied())
			{
				Piece toRemove = mSquareLabel.getSquare().setPiece(null);
				(toRemove.isBlack() ? mBlackTeam : mWhiteTeam).remove(toRemove);

				mSquareLabel.refresh();
			}
			else
			{
				showSquareOptions();
			}
		}

		private void showSquareOptions()
		{
			final JFrame popupFrame = new JFrame(Messages.getString("VariantCreationPanel.squareOptions")); //$NON-NLS-1$
			popupFrame.setLocationRelativeTo(Driver.getInstance());
			popupFrame.setLayout(new FlowLayout());

			final ChessPanel popupPanel = new ChessPanel();
			popupPanel.setLayout(new GridBagLayout());

			GridBagConstraints constraints = new GridBagConstraints();

			final JButton colorChooserButton = new JButton(Messages.getString("VariantCreationPanel.setSquareColor")); //$NON-NLS-1$
			colorChooserButton.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent event)
				{
					Color color = JColorChooser.showDialog(popupFrame,
							Messages.getString("VariantCreationPanel.chooseColor"), mSquareLabel.getColor()); //$NON-NLS-1$
					if (color == null)
						return;
					// TODO: verify that this can be removed
					// can't let them pick exactly the highlight color, or they
					// could move to that space from anywhere
					if (color != SquareJLabel.HIGHLIGHT_COLOR)
					{
						mSquareLabel.setBackgroundColor(color);
						colorChooserButton.setBackground(color);
					}
					else
					{
						// the chances of this happening is EXTREMELY small...
						JOptionPane.showMessageDialog(
								popupFrame,
								Messages.getString("VariantCreationPanel.colorCannotBeSelected"), Messages.getString("VariantCreationPanel.invalidColor"), //$NON-NLS-1$ //$NON-NLS-2$
								JOptionPane.PLAIN_MESSAGE);
					}

				}
			});
			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.insets = new Insets(5, 5, 5, 5);
			popupPanel.add(colorChooserButton, constraints);

			final JCheckBox uninhabitableButton = new JCheckBox(
					"<html><font color=#FFFFFF>" + Messages.getString("VariantCreationPanel.uninhabited") + "</font></html>", !mSquareLabel.getSquare().isHabitable()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			uninhabitableButton.setOpaque(false);

			constraints.gridy = 1;
			popupPanel.add(uninhabitableButton, constraints);

			final JButton doneButton = new JButton(Messages.getString("VariantCreationPanel.done")); //$NON-NLS-1$
			doneButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent event)
				{
					if (uninhabitableButton.isSelected())
						mSquareLabel.getSquare().setIsHabitable(false);
					else
						mSquareLabel.getSquare().setIsHabitable(true);
					popupFrame.dispose();
				}
			});
			constraints.gridy = 2;
			constraints.insets = new Insets(10, 5, 10, 5);
			popupPanel.add(doneButton, constraints);

			popupFrame.add(popupPanel);

			popupFrame.pack();
			popupFrame.setVisible(true);
		}

		private SquareJLabel mSquareLabel;
	}

	public void putPromotionMap(String pieceName, List<String> promotesTo, int colorCode)
	{
		if (colorCode == GameBuilder.BLACK || colorCode == GameBuilder.BOTH)
		{
			mBlackPromotionMap.put(pieceName, promotesTo);
			if (mBuilder.getBlackPromotionMap() == null)
				mBuilder.setBlackPromotionMap(mBlackPromotionMap);
			mBuilder.addToPromotionMap(pieceName, promotesTo, colorCode);
		}
		if (colorCode == GameBuilder.WHITE || colorCode == GameBuilder.BOTH)
		{
			mWhitePromotionMap.put(pieceName, promotesTo);
			if (mBuilder.getWhitePromotionMap() == null)
				mBuilder.setWhitePromotionMap(mWhitePromotionMap);
			mBuilder.addToPromotionMap(pieceName, promotesTo, colorCode);
		}
	}

	public Map<String, List<String>> getWhitePromotionMap()
	{
		return mWhitePromotionMap;
	}

	public Map<String, List<String>> getBlackPromotionMap()
	{
		return mBlackPromotionMap;
	}

	private ActionListener submitButtonActionListener = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			if (mVariantNameField.getText().trim().isEmpty())
			{
				JOptionPane.showMessageDialog(VariantCreationPanel.this,
						Messages.getString("VariantCreationPanel.enterAName"), Messages.getString("VariantCreationPanel.enterName"), //$NON-NLS-1$ //$NON-NLS-2$
						JOptionPane.PLAIN_MESSAGE);
				return;
			}

			mBuilder.setName(mVariantNameField.getText());

			for (Piece piece : mWhiteTeam)
				piece.setPromotesTo(mWhitePromotionMap.get(piece.getName()));
			for (Piece piece : mBlackTeam)
				piece.setPromotesTo(mBlackPromotionMap.get(piece.getName()));

			int numberOfObjectives = 0;

			if (mWhiteRules.getObjectiveName() != null && !mWhiteRules.getObjectiveName().isEmpty())
			{
				for (Piece piece : mWhiteTeam)
				{
					if (piece.getName().equals(mWhiteRules.getObjectiveName()))
						numberOfObjectives++;
				}
				if (numberOfObjectives != 1)
				{
					JOptionPane.showMessageDialog(Driver.getInstance(),
							Messages.getString("VariantCreationPanel.placeOneWhiteObjective"), //$NON-NLS-1$
							Messages.getString("VariantCreationPanel.objectiveMissing"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
					return;
				}
			}

			numberOfObjectives = 0;
			if (mBlackRules.getObjectiveName() != null && !mBlackRules.getObjectiveName().isEmpty())
			{
				for (Piece piece : mBlackTeam)
				{
					if (piece.getName().equals(mBlackRules.getObjectiveName()))
						numberOfObjectives++;
				}
				if (numberOfObjectives != 1)
				{
					JOptionPane.showMessageDialog(Driver.getInstance(),
							Messages.getString("VariantCreationPanel.placeOneBlackObjective"), //$NON-NLS-1$
							Messages.getString("VariantCreationPanel.objectiveMissing"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
					return;
				}
			}

			mBuilder.setWhiteTeam(mWhiteTeam);

			if (mWhiteRules.getObjectiveName() == null)
				mWhiteRules.setObjectivePiece(new ObjectivePiece(ObjectivePieceTypes.CLASSIC));

			mBuilder.setBlackTeam(mBlackTeam);

			if (mBlackRules.getObjectiveName() == null)
				mBlackRules.setObjectivePiece(new ObjectivePiece(ObjectivePieceTypes.CLASSIC));

			mBuilder.writeFile(mWhiteRules, mBlackRules);

			PreferenceUtility.clearTooltipListeners();
			Driver.getInstance().revertToMainPanel();
		}
	};

	private final class PieceDisplayBoardListener extends DropAdapter implements MouseListener, PieceToolTipPreferenceChangedListener
	{
		public PieceDisplayBoardListener(SquareJLabel squareLabel)
		{
			super(mGlobalGlassPane);
			mSquareLabel = squareLabel;
			addDropListener(mDropManager);
			PreferenceUtility.addPieceToolTipListener(this);
		}

		@Override
		public void mousePressed(MouseEvent event)
		{
			mGlassPane.setVisible(true);

			Point point = (Point) event.getPoint().clone();
			SwingUtilities.convertPointToScreen(point, event.getComponent());
			SwingUtilities.convertPointFromScreen(point, mGlassPane);

			mGlassPane.setPoint(point);

			BufferedImage image = null;
			Piece piece = mSquareLabel.getSquare().getPiece();
			ImageIcon imageIcon = PieceIconUtility.getPieceIcon(piece.getName(), piece.isBlack());
			int width = imageIcon.getIconWidth();
			int height = imageIcon.getIconHeight();
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D graphics2D = (Graphics2D) image.getGraphics();
			imageIcon.paintIcon(null, graphics2D, 0, 0);
			graphics2D.dispose();

			mGlassPane.setImage(image);
			mGlassPane.repaint();
		}

		@Override
		public void mouseReleased(MouseEvent event)
		{
			Point point = (Point) event.getPoint().clone();
			SwingUtilities.convertPointToScreen(point, event.getComponent());

			mGlassPane.setImage(null);
			mGlassPane.setVisible(false);

			fireDropEvent(new DropEvent(point, mSquareLabel), true);
		}

		@Override
		public void mouseExited(MouseEvent event)
		{
		}

		@Override
		public void mouseEntered(MouseEvent event)
		{
		}

		@Override
		public void mouseClicked(MouseEvent event)
		{
		}

		private final SquareJLabel mSquareLabel;

		@Override
		public void onPieceToolTipPreferenceChanged()
		{
			mSquareLabel.refresh();
		}
	};

	private final class PieceNormalBoardListener extends DropAdapter implements MouseListener, PieceToolTipPreferenceChangedListener
	{
		public PieceNormalBoardListener(SquareJLabel squareLabel)
		{
			super(mGlobalGlassPane);
			mSquareLabel = squareLabel;
			addDropListener(mDropManager);
			if (mSquareLabel.getSquare().getPiece() != null && PreferenceUtility.getPreference().showPieceToolTips())
				mSquareLabel.setToolTipText(mSquareLabel.getSquare().getPiece().getToolTipText());
			PreferenceUtility.addPieceToolTipListener(this);
		}

		@Override
		public void mousePressed(MouseEvent event)
		{
			mGlassPane.setVisible(true);

			Point point = (Point) event.getPoint().clone();
			SwingUtilities.convertPointToScreen(point, event.getComponent());
			SwingUtilities.convertPointFromScreen(point, mGlassPane);

			mGlassPane.setPoint(point);

			BufferedImage image = null;
			String pieceName = mSquareLabel.getSquare().getPiece().getName();

			FileUtility.getPieceFile(pieceName);

			Piece piece = mSquareLabel.getSquare().getPiece();
			ImageIcon imageIcon = PieceIconUtility.getPieceIcon(piece.getName(), piece.isBlack());
			int width = imageIcon.getIconWidth();
			int height = imageIcon.getIconHeight();
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D graphics2D = (Graphics2D) image.getGraphics();
			imageIcon.paintIcon(null, graphics2D, 0, 0);
			graphics2D.dispose();
			mGlassPane.setImage(image);
			mSquareLabel.setIcon(null);
			mGlassPane.repaint();
		}

		@Override
		public void mouseReleased(MouseEvent event)
		{
			Point point = (Point) event.getPoint().clone();
			SwingUtilities.convertPointToScreen(point, event.getComponent());

			mGlassPane.setImage(null);
			mGlassPane.setVisible(false);

			fireDropEvent(new DropEvent(point, mSquareLabel), false);
		}

		@Override
		public void mouseExited(MouseEvent event)
		{
		}

		@Override
		public void mouseEntered(MouseEvent event)
		{
		}

		@Override
		public void mouseClicked(MouseEvent event)
		{
		}

		@Override
		public void onPieceToolTipPreferenceChanged()
		{
			if (mSquareLabel.getSquare().getPiece() != null && PreferenceUtility.getPreference().showPieceToolTips())
				mSquareLabel.setToolTipText(mSquareLabel.getSquare().getPiece().getToolTipText());
			else
				mSquareLabel.setToolTipText(null);
		}

		private final SquareJLabel mSquareLabel;
	};

	private final class DropManager extends AbstractDropManager
	{
		@Override
		public void dropped(DropEvent event, boolean fromDisplayBoard)
		{
			SquareJLabel originSquareLabel = (SquareJLabel) event.getOriginComponent();
			SquareJLabel destinationSquareLabel = (SquareJLabel) isInTarget(event.getDropLocation());

			Piece originPiece = originSquareLabel.getSquare().getPiece();

			if (originPiece == null)
				return;

			if (!fromDisplayBoard)
			{
				(originPiece.isBlack() ? mBlackTeam : mWhiteTeam).remove(originPiece);

				// drag piece off board to remove it
				if (destinationSquareLabel == null)
				{
					originPiece = null;
					originSquareLabel.getSquare().setPiece(null);

					for (MouseListener listener : originSquareLabel.getMouseListeners())
						originSquareLabel.removeMouseListener(listener);

					originSquareLabel.addMouseListener(new SquareSetupMouseListener(originSquareLabel));

					originSquareLabel.refresh();
					return;
				}
			}

			if (destinationSquareLabel != null && destinationSquareLabel.getSquare().isHabitable())
			{
				Piece oldPiece = destinationSquareLabel.getSquare().getPiece();
				if (oldPiece != null)
					(oldPiece.isBlack() ? mBlackTeam : mWhiteTeam).remove(oldPiece);

				int boardNumber = event.getDropLocation().getX() < mBoardPanels[0].getLocationOnScreen().getX()
						+ mBoardPanels[0].getWidth() ? 0 : 1;

				Piece piece = null;
				try
				{
					piece = PieceBuilder.makePiece(originPiece.getName(), originPiece.isBlack(), destinationSquareLabel.getSquare(),
							mBuilder.getBoards()[boardNumber]);
				}
				catch (IOException e)
				{
					JOptionPane.showMessageDialog(Driver.getInstance(),
							Messages.getString("VariantCreationPanel.errorCouldNotLoadPiece")); //$NON-NLS-1$
					e.printStackTrace();
					return;
				}

				if (originPiece.isBlack())
					mBlackTeam.add(piece);
				else
					mWhiteTeam.add(piece);

				if (!fromDisplayBoard)
				{
					originSquareLabel.getSquare().setPiece(null);

					originSquareLabel.refresh();

					// why is there no "clear listeners"?
					for (MouseListener mouseListener : originSquareLabel.getMouseListeners())
					{
						originSquareLabel.removeMouseListener(mouseListener);
					}
					originSquareLabel.addMouseListener(new SquareSetupMouseListener(originSquareLabel));
				}
				for (MouseListener mouseListener : destinationSquareLabel.getMouseListeners())
				{
					destinationSquareLabel.removeMouseListener(mouseListener);
				}
				destinationSquareLabel.addMouseListener(new PieceNormalBoardListener(destinationSquareLabel));
				destinationSquareLabel.addMouseMotionListener(m_motionAdapter);

				destinationSquareLabel.getSquare().setPiece(piece);
				destinationSquareLabel.refresh();

			}
			// else
			// {
			// JOptionPane
			// .showMessageDialog(
			// Driver.getInstance(),
			//                                                                Messages.getString("VariantCreationPanel.squareIsUninhabitable"), Messages.getString("VariantCreationPanel.warning"), //$NON-NLS-1$ //$NON-NLS-2$
			// JOptionPane.PLAIN_MESSAGE);
			// }
		}
	};

	@Override
	public void onPieceListChanged()
	{
		setupPiecesList();
		drawBoards(mGameBoards);
	}

	private void openPieceMakerPanel(String pieceName)
	{
		mOptionsFrame.dispose();
		mOptionsFrame = new JFrame();
		PieceMakerPanel pieceMakerPanel = new PieceMakerPanel(pieceName, mOptionsFrame);
		pieceMakerPanel.addPieceListListener(this);
		Driver.centerFrame(mOptionsFrame);
	}

	private void openPiecePromotePanel(String pieceName)
	{
		mOptionsFrame.dispose();
		mOptionsFrame = new JFrame();
		new PiecePromotionPanel(pieceName, VariantCreationPanel.this, mOptionsFrame);
		Driver.centerFrame(mOptionsFrame);
	}

	private static final long serialVersionUID = 7830479492072657640L;

	private final GlassPane mGlobalGlassPane;
	private final DropManager mDropManager;

	public Rules mWhiteRules;
	public Rules mBlackRules;
	private JPanel[] mBoardPanels;
	private JPanel mPieceListPanel;
	private JPanel mPieceListBoardPanel;
	private Map<String, List<String>> mWhitePromotionMap;
	private Map<String, List<String>> mBlackPromotionMap;
	public GameBuilder mBuilder;
	private List<Piece> mWhiteTeam;
	private List<Piece> mBlackTeam;
	private JFrame mOptionsFrame;
	private JScrollPane mScrollPane;
	private MotionAdapter m_motionAdapter;
	private Board mDisplayBoard;
	private Board[] mGameBoards;
	private final JTextField mVariantNameField;
	private String[] mAllPieceNames;
}
