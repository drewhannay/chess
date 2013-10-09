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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import logic.PieceBuilder;
import utility.GuiUtility;

import com.google.common.collect.Lists;

public class PiecePromotionPanel extends JPanel
{
	/**
	 * 
	 * @param pieceName
	 * @param customSetupMenu
	 */
	public PiecePromotionPanel(String pieceName, CustomSetupPanel customSetupMenu, JFrame optionsFrame)
	{
		mPieceBeingPromotedName = pieceName;
		mCustomSetupMenu = customSetupMenu;
		mFrame = optionsFrame;
		mFrame.setVisible(true);
		mFrame.add(this);
		mFrame.setVisible(true);
		mFrame.setSize(600, 500);
		mFrame.setLocationRelativeTo(Driver.getInstance());
		initComponents();
	}

	private void initComponents()
	{
		setLayout(new GridBagLayout());

		final DefaultListModel canPromoteList = new DefaultListModel();
		final DefaultListModel cantPromoteList = new DefaultListModel();
		
		List<String> promotions = mCustomSetupMenu.getPromotionMap().get(mPieceBeingPromotedName);
		
		Object[] allPieces = PieceBuilder.getSet().toArray();
		for (int i = 0; i < allPieces.length; i++)
		{
			if (promotions != null && promotions.contains(allPieces[i]))
				canPromoteList.addElement(allPieces[i]);
			else if (!allPieces[i].equals(mPieceBeingPromotedName))
				cantPromoteList.addElement(allPieces[i]);
		}
		final JList cantPromoteToDisplay = new JList(cantPromoteList);
		final JList canPromoteToDisplay = new JList(canPromoteList);

		final JButton moveLeftButton = new JButton();
		moveLeftButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					int index = canPromoteToDisplay.getSelectedIndex();

					cantPromoteList.addElement(canPromoteList.elementAt(index));
					canPromoteList.remove(index);
				}
				catch (Exception e)
				{
				}
			}
		});
		moveLeftButton.setText("<---"); //$NON-NLS-1$

		final JButton moveRightButton = new JButton();
		moveRightButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				int index = cantPromoteToDisplay.getSelectedIndex();

				try
				{
					canPromoteList.addElement(cantPromoteList.elementAt(index));
					cantPromoteList.remove(index);
				}
				catch (Exception e)
				{
				}

			}
		});
		moveRightButton.setText("--->"); //$NON-NLS-1$

		cantPromoteToDisplay.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cantPromoteToDisplay.setLayoutOrientation(JList.VERTICAL);
		cantPromoteToDisplay.setVisibleRowCount(-1);
		cantPromoteToDisplay.setSelectedIndex(0);

		canPromoteToDisplay.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		canPromoteToDisplay.setLayoutOrientation(JList.VERTICAL);
		canPromoteToDisplay.setVisibleRowCount(-1);
		canPromoteToDisplay.setSelectedIndex(0);

		// LIST - CANT PROMOTE TO
		JScrollPane cantPromoteScrollPane = new JScrollPane(cantPromoteToDisplay);
		cantPromoteScrollPane.setPreferredSize(new Dimension(200, 200));

		ListSelectionModel cantPromoteButtonDisabler = cantPromoteToDisplay.getSelectionModel();
		cantPromoteButtonDisabler.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent event)
			{
				if (event.getValueIsAdjusting() == false)
				{

					if (cantPromoteToDisplay.getSelectedIndex() == -1)
					{
						moveLeftButton.setEnabled(false);
						moveRightButton.setEnabled(false);
					}
					else
					{
						moveLeftButton.setEnabled(true);
						moveRightButton.setEnabled(true);
					}
				}
			}
		});

		JScrollPane canPromoteScrollPane = new JScrollPane(canPromoteToDisplay);
		canPromoteScrollPane.setPreferredSize(new Dimension(200, 200));

		ListSelectionModel canPromoteDisplayButtonDisabler = canPromoteToDisplay.getSelectionModel();
		canPromoteDisplayButtonDisabler.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				if (e.getValueIsAdjusting() == false)
				{ // If the user is still selecting.

					// If the user has not selected anything yet.
					if (canPromoteToDisplay.getSelectedIndex() == -1)
					{
						// No selection, disable the buttons.
						moveLeftButton.setEnabled(false);
						moveRightButton.setEnabled(false);
					}
					else
					{
						// Selection, enable the buttons.
						moveLeftButton.setEnabled(true);
						moveRightButton.setEnabled(true);
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
				List<String> promotesTo = Lists.newArrayList();
				for (int i = 0; i < canPromoteList.size(); i++)
					promotesTo.add((String) canPromoteList.get(i));

				mCustomSetupMenu.putPromotionMap(mPieceBeingPromotedName, promotesTo);
				PiecePromotionPanel.this.removeAll();
				mFrame.setVisible(false);
			}

		});
		JButton cancelButton = new JButton(Messages.getString("PiecePromotionPanel.cancel")); //$NON-NLS-1$
		GuiUtility.setupVariantCancelButton(cancelButton, this, mFrame);

		JPanel optionsPanel = new JPanel();
		optionsPanel.add(saveButton);
		optionsPanel.add(cancelButton);

		JPanel arrowsPanel = new JPanel();
		arrowsPanel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 1;
		arrowsPanel.add(moveRightButton, constraints);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 2;
		arrowsPanel.add(moveLeftButton, constraints);

		constraints.fill = GridBagConstraints.NONE;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets(10, 0, 10, 0);
		add(new JLabel(Messages.getString("PiecePromotionPanel.cantPromoteHTML")), constraints); //$NON-NLS-1$
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.insets = new Insets(0, 10, 10, 0);
		add(cantPromoteScrollPane, constraints);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.insets = new Insets(0, 10, 0, 10);
		add(arrowsPanel, constraints);
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.insets = new Insets(10, 0, 10, 0);
		add(new JLabel(Messages.getString("PiecePromotionPanel.canPromoteHTML")), constraints); //$NON-NLS-1$
		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.insets = new Insets(0, 0, 10, 10);
		add(canPromoteScrollPane, constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 3;
		constraints.insets = new Insets(0, 0, 10, 0);
		add(optionsPanel, constraints);

		setVisible(true);
		mFrame.pack();
	}

	private static final long serialVersionUID = -3940357256712099377L;

	private String mPieceBeingPromotedName;
	private CustomSetupPanel mCustomSetupMenu;
	private JFrame mFrame;
}
