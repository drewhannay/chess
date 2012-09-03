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
import utility.GUIUtility;

import com.google.common.collect.Lists;

public class PiecePromotion extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3940357256712099377L;

	/**
	 * 
	 * @param pieceName
	 * @param customSetupMenu
	 */
	public PiecePromotion(String pieceName, CustomSetupMenu customSetupMenu, JFrame optionsFrame)
	{
		m_pieceBeingPromotedName = pieceName;
		m_customSetupMenu = customSetupMenu;
		m_frame = optionsFrame;
		m_frame.setVisible(true);
		m_frame.add(this);
		m_frame.setVisible(true);
		m_frame.setSize(600, 500);
		m_frame.setLocationRelativeTo(Driver.getInstance());
		initComponents();
	}

	private void initComponents()
	{
		setLayout(new GridBagLayout());

		final DefaultListModel canPromoteList = new DefaultListModel();
		final DefaultListModel cantPromoteList = new DefaultListModel();
		Object[] allPieces = PieceBuilder.getSet().toArray();
		for (int i = 0; i < allPieces.length; i++)
		{
			if (!allPieces[i].equals(m_pieceBeingPromotedName))
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
		moveLeftButton.setText("<---");

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
		moveRightButton.setText("--->");

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
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				List<String> promotesTo = Lists.newArrayList();
				for (int i = 0; i < canPromoteList.size(); i++)
					promotesTo.add((String) canPromoteList.get(i));

				m_customSetupMenu.putPromotionMap(m_pieceBeingPromotedName, promotesTo);
				PiecePromotion.this.removeAll();
				m_frame.setVisible(false);
			}

		});
		JButton cancelButton = new JButton("Cancel");
		GUIUtility.setupVariantCancelButton(cancelButton, this, m_frame);

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
		add(new JLabel("<HTML><b>Can't Promote To</b></HTML>"), constraints);
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
		add(new JLabel("<HTML><b>Can Promote To</b></HTML>"), constraints);
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
		m_frame.pack();
	}

	private String m_pieceBeingPromotedName;
	private CustomSetupMenu m_customSetupMenu;
	private JFrame m_frame;
}
