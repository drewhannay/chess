package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import logic.GameBuilder;
import logic.PieceBuilder;
import utility.GuiUtility;

import com.google.common.collect.Lists;

public class PiecePromotionPanel extends ChessPanel
{
	/**
	 * 
	 * @param pieceName
	 * @param customSetupMenu
	 */
	public PiecePromotionPanel(String pieceName, VariantCreationPanel customSetupMenu, JFrame optionsFrame)
	{
		mPieceBeingPromotedName = pieceName;
		mCustomSetupMenu = customSetupMenu;
		mFrame = optionsFrame;

		mFrame.setSize(600, 800);
		mFrame.add(this);
		initComponents();
		mFrame.setLocationRelativeTo(null);
		mFrame.setVisible(true);
	}

	private void initComponents()
	{
		setLayout(new GridBagLayout());

		final DefaultListModel whiteCanPromoteList = new DefaultListModel();
		final DefaultListModel whiteCantPromoteList = new DefaultListModel();
		final DefaultListModel blackCanPromoteList = new DefaultListModel();
		final DefaultListModel blackCantPromoteList = new DefaultListModel();

		List<String> whitePromotions = mCustomSetupMenu.getWhitePromotionMap().get(mPieceBeingPromotedName);
		List<String> blackPromotions = mCustomSetupMenu.getBlackPromotionMap().get(mPieceBeingPromotedName);

		String[] pieces = PieceBuilder.getSortedArrayWithCustomPieces();

		for (int i = 0; i < pieces.length; i++)
		{
			if (!pieces[i].equals(mPieceBeingPromotedName))
			{
				if (!mCustomSetupMenu.mWhiteRules.getObjectiveName().equals(mPieceBeingPromotedName))
				{
					if (whitePromotions != null && whitePromotions.contains(pieces[i]))
						whiteCanPromoteList.addElement(pieces[i]);
					else
						whiteCantPromoteList.addElement(pieces[i]);
				}

				if (!mCustomSetupMenu.mBlackRules.getObjectiveName().equals(mPieceBeingPromotedName))
				{
					if (blackPromotions != null && blackPromotions.contains(pieces[i]))
						blackCanPromoteList.addElement(pieces[i]);
					else
						blackCantPromoteList.addElement(pieces[i]);
				}
			}
		}
		final JList whiteCantPromoteToDisplay = new JList(whiteCantPromoteList);
		final JList whiteCanPromoteToDisplay = new JList(whiteCanPromoteList);
		final JList blackCantPromoteToDisplay = new JList(blackCantPromoteList);
		final JList blackCanPromoteToDisplay = new JList(blackCanPromoteList);

		final JButton whiteMoveLeftButton = new JButton();
		whiteMoveLeftButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					int index = whiteCanPromoteToDisplay.getSelectedIndex();

					whiteCantPromoteList.addElement(whiteCanPromoteList.elementAt(index));
					whiteCanPromoteList.remove(index);
				}
				catch (Exception e)
				{
				}
			}
		});
		whiteMoveLeftButton.setText("<---"); //$NON-NLS-1$

		final JButton whiteMoveRightButton = new JButton();
		whiteMoveRightButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				int index = whiteCantPromoteToDisplay.getSelectedIndex();

				try
				{
					whiteCanPromoteList.addElement(whiteCantPromoteList.elementAt(index));
					whiteCantPromoteList.remove(index);
				}
				catch (Exception e)
				{
				}

			}
		});
		whiteMoveRightButton.setText("--->"); //$NON-NLS-1$

		whiteCantPromoteToDisplay.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		whiteCantPromoteToDisplay.setLayoutOrientation(JList.VERTICAL);
		whiteCantPromoteToDisplay.setVisibleRowCount(-1);
		whiteCantPromoteToDisplay.setSelectedIndex(0);

		whiteCanPromoteToDisplay.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		whiteCanPromoteToDisplay.setLayoutOrientation(JList.VERTICAL);
		whiteCanPromoteToDisplay.setVisibleRowCount(-1);
		whiteCanPromoteToDisplay.setSelectedIndex(0);

		final JButton blackMoveLeftButton = new JButton();
		blackMoveLeftButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					int index = blackCanPromoteToDisplay.getSelectedIndex();

					blackCantPromoteList.addElement(blackCanPromoteList.elementAt(index));
					blackCanPromoteList.remove(index);
				}
				catch (Exception e)
				{
				}
			}
		});
		blackMoveLeftButton.setText("<---"); //$NON-NLS-1$

		final JButton blackMoveRightButton = new JButton();
		blackMoveRightButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				int index = blackCantPromoteToDisplay.getSelectedIndex();

				try
				{
					blackCanPromoteList.addElement(blackCantPromoteList.elementAt(index));
					blackCantPromoteList.remove(index);
				}
				catch (Exception e)
				{
				}

			}
		});
		blackMoveRightButton.setText("--->"); //$NON-NLS-1$

		blackCantPromoteToDisplay.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		blackCantPromoteToDisplay.setLayoutOrientation(JList.VERTICAL);
		blackCantPromoteToDisplay.setVisibleRowCount(-1);
		blackCantPromoteToDisplay.setSelectedIndex(0);

		blackCanPromoteToDisplay.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		blackCanPromoteToDisplay.setLayoutOrientation(JList.VERTICAL);
		blackCanPromoteToDisplay.setVisibleRowCount(-1);
		blackCanPromoteToDisplay.setSelectedIndex(0);

		// LIST - CANT PROMOTE TO
		JScrollPane whiteCantPromoteScrollPane = new JScrollPane(whiteCantPromoteToDisplay);
		whiteCantPromoteScrollPane.setPreferredSize(new Dimension(200, 200));

		ListSelectionModel whiteCantPromoteButtonDisabler = whiteCantPromoteToDisplay.getSelectionModel();
		whiteCantPromoteButtonDisabler.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent event)
			{
				if (event.getValueIsAdjusting() == false)
				{

					if (whiteCantPromoteToDisplay.getSelectedIndex() == -1)
					{
						whiteMoveLeftButton.setEnabled(false);
						whiteMoveRightButton.setEnabled(false);
					}
					else
					{
						whiteMoveLeftButton.setEnabled(true);
						whiteMoveRightButton.setEnabled(true);
					}
				}
			}
		});

		JScrollPane whiteCanPromoteScrollPane = new JScrollPane(whiteCanPromoteToDisplay);
		whiteCanPromoteScrollPane.setPreferredSize(new Dimension(200, 200));

		ListSelectionModel whiteCanPromoteDisplayButtonDisabler = whiteCanPromoteToDisplay.getSelectionModel();
		whiteCanPromoteDisplayButtonDisabler.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				if (e.getValueIsAdjusting() == false)
				{ // If the user is still selecting.

					// If the user has not selected anything yet.
					if (whiteCanPromoteToDisplay.getSelectedIndex() == -1)
					{
						// No selection, disable the buttons.
						whiteMoveLeftButton.setEnabled(false);
						whiteMoveRightButton.setEnabled(false);
					}
					else
					{
						// Selection, enable the buttons.
						whiteMoveLeftButton.setEnabled(true);
						whiteMoveRightButton.setEnabled(true);
					}
				}
			}
		});

		JScrollPane blackCantPromoteScrollPane = new JScrollPane(blackCantPromoteToDisplay);
		blackCantPromoteScrollPane.setPreferredSize(new Dimension(200, 200));

		ListSelectionModel blackCantPromoteButtonDisabler = blackCantPromoteToDisplay.getSelectionModel();
		blackCantPromoteButtonDisabler.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent event)
			{
				if (event.getValueIsAdjusting() == false)
				{

					if (blackCantPromoteToDisplay.getSelectedIndex() == -1)
					{
						blackMoveLeftButton.setEnabled(false);
						blackMoveRightButton.setEnabled(false);
					}
					else
					{
						blackMoveLeftButton.setEnabled(true);
						blackMoveRightButton.setEnabled(true);
					}
				}
			}
		});

		JScrollPane blackCanPromoteScrollPane = new JScrollPane(blackCanPromoteToDisplay);
		blackCanPromoteScrollPane.setPreferredSize(new Dimension(200, 200));

		ListSelectionModel blackCanPromoteDisplayButtonDisabler = blackCanPromoteToDisplay.getSelectionModel();
		blackCanPromoteDisplayButtonDisabler.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				if (e.getValueIsAdjusting() == false)
				{ // If the user is still selecting.

					// If the user has not selected anything yet.
					if (blackCanPromoteToDisplay.getSelectedIndex() == -1)
					{
						// No selection, disable the buttons.
						blackMoveLeftButton.setEnabled(false);
						blackMoveRightButton.setEnabled(false);
					}
					else
					{
						// Selection, enable the buttons.
						blackMoveLeftButton.setEnabled(true);
						blackMoveRightButton.setEnabled(true);
					}
				}
			}
		});

		JButton saveButton = new JButton(Messages.getString("PiecePromotionPanel.save")); //$NON-NLS-1$
		saveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				List<String> whitePromotesTo = Lists.newArrayList();
				for (int i = 0; i < whiteCanPromoteList.size(); i++)
					whitePromotesTo.add((String) whiteCanPromoteList.get(i));

				mCustomSetupMenu.putPromotionMap(mPieceBeingPromotedName, whitePromotesTo, GameBuilder.WHITE);

				List<String> blackPromotesTo = Lists.newArrayList();
				for (int i = 0; i < blackCanPromoteList.size(); i++)
					blackPromotesTo.add((String) blackCanPromoteList.get(i));

				mCustomSetupMenu.putPromotionMap(mPieceBeingPromotedName, blackPromotesTo, GameBuilder.BLACK);

				PiecePromotionPanel.this.removeAll();
				mFrame.setVisible(false);
			}

		});
		JButton cancelButton = new JButton(Messages.getString("PiecePromotionPanel.cancel")); //$NON-NLS-1$
		GuiUtility.setupVariantCancelButton(cancelButton, this, mFrame);

		JPanel optionsPanel = new JPanel();
		optionsPanel.setOpaque(false);
		optionsPanel.add(saveButton);
		optionsPanel.add(cancelButton);

		JPanel whiteArrowsPanel = new JPanel();
		whiteArrowsPanel.setOpaque(false);
		whiteArrowsPanel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.insets = new Insets(5, 0, 5, 0);
		whiteArrowsPanel.add(whiteMoveRightButton, constraints);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 2;
		whiteArrowsPanel.add(whiteMoveLeftButton, constraints);

		constraints.fill = GridBagConstraints.NONE;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets(10, 0, 10, 0);
		add(GuiUtility.createJLabel(Messages.getString("PiecePromotionPanel.whiteCantPromoteHTML")), constraints); //$NON-NLS-1$
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.insets = new Insets(0, 10, 10, 0);
		add(whiteCantPromoteScrollPane, constraints);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.insets = new Insets(0, 10, 0, 10);
		add(whiteArrowsPanel, constraints);
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.insets = new Insets(10, 0, 10, 0);
		add(GuiUtility.createJLabel(Messages.getString("PiecePromotionPanel.whiteCanPromoteHTML")), constraints); //$NON-NLS-1$
		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.insets = new Insets(0, 0, 10, 10);
		add(whiteCanPromoteScrollPane, constraints);

		JPanel blackArrowsPanel = new JPanel();
		blackArrowsPanel.setOpaque(false);
		blackArrowsPanel.setLayout(new GridBagLayout());
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(5, 0, 5, 0);
		constraints.gridx = 0;
		constraints.gridy = 3;
		blackArrowsPanel.add(blackMoveRightButton, constraints);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 4;
		blackArrowsPanel.add(blackMoveLeftButton, constraints);

		constraints.fill = GridBagConstraints.NONE;
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.insets = new Insets(10, 0, 10, 0);
		add(GuiUtility.createJLabel(Messages.getString("PiecePromotionPanel.blackCantPromoteHTML")), constraints); //$NON-NLS-1$
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.insets = new Insets(0, 10, 10, 0);
		add(blackCantPromoteScrollPane, constraints);
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.insets = new Insets(0, 10, 0, 10);
		add(blackArrowsPanel, constraints);
		constraints.gridx = 2;
		constraints.gridy = 2;
		constraints.insets = new Insets(10, 0, 10, 0);
		add(GuiUtility.createJLabel(Messages.getString("PiecePromotionPanel.blackCanPromoteHTML")), constraints); //$NON-NLS-1$
		constraints.gridx = 2;
		constraints.gridy = 3;
		constraints.insets = new Insets(0, 0, 10, 10);
		add(blackCanPromoteScrollPane, constraints);
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 3;
		constraints.insets = new Insets(0, 0, 10, 0);

		add(optionsPanel, constraints);

		boolean isBlackObjective = mPieceBeingPromotedName.equals(mCustomSetupMenu.mBlackRules.getObjectiveName());
		boolean isWhiteObjective = mPieceBeingPromotedName.equals(mCustomSetupMenu.mWhiteRules.getObjectiveName());

		blackCanPromoteScrollPane.setEnabled(!isBlackObjective);
		blackMoveLeftButton.setEnabled(!isBlackObjective);
		blackMoveRightButton.setEnabled(!isBlackObjective);

		whiteCanPromoteScrollPane.setEnabled(!isWhiteObjective);
		whiteMoveLeftButton.setEnabled(!isWhiteObjective);
		whiteMoveRightButton.setEnabled(!isWhiteObjective);

		whiteArrowsPanel.setEnabled(!mPieceBeingPromotedName.equals(mCustomSetupMenu.mWhiteRules.getObjectiveName()));

		setVisible(true);
		mFrame.pack();
	}

	private static final long serialVersionUID = -3940357256712099377L;

	private String mPieceBeingPromotedName;
	private VariantCreationPanel mCustomSetupMenu;
	private JFrame mFrame;
}
