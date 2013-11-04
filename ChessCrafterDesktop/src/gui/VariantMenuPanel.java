package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import utility.FileUtility;
import utility.GuiUtility;

public class VariantMenuPanel extends ChessPanel
{
	public VariantMenuPanel()
	{
		mNoCustomPiecesLabel = GuiUtility.createJLabel(Messages.getString("VariantMenuPanel.noVariants")); //$NON-NLS-1$
		mNoCustomLabelConstraints = new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.CENTER,
				new Insets(50, 20, 50, 20), 5, 5);

		mEditPanelConstraints = new GridBagConstraints();
		mEditPanelConstraints.insets = new Insets(0, 0, 0, 0);
		mEditPanelConstraints.gridheight = 3;
		mEditPanelConstraints.ipadx = 7;
		mEditPanelConstraints.gridy = 1;

		mEditDeletePanel = new JPanel();
		mVariantListModel = new DefaultListModel();
		
		initGuiComponents();
	}

	private void initGuiComponents()
	{
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridy = 0;
		constraints.ipadx = 0;
		constraints.insets = new Insets(5, 5, 0, 5);
		constraints.anchor = GridBagConstraints.CENTER;

		JButton createNewVariantButton = new JButton(Messages.getString("VariantMenuPanel.createNew")); //$NON-NLS-1$
		createNewVariantButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				Driver.getInstance().pushPanel(new VariantCreationPanel(null));
			}
		});
		add(createNewVariantButton, constraints);

		mEditDeletePanel.setLayout(new GridBagLayout());
		mEditDeletePanel.setOpaque(false);

		constraints.gridy = 1;
		constraints.ipadx = 7;
		constraints.insets = new Insets(5, 5, 0, 5);
		constraints.fill = GridBagConstraints.HORIZONTAL;

		final JList variantList = new JList();
		JScrollPane scrollPane = new JScrollPane();
		variantList.setModel(mVariantListModel);
		scrollPane.setViewportView(variantList);

		mEditDeletePanel.add(scrollPane, constraints);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		buttonPanel.setOpaque(false);

		constraints.gridy = 2;
		constraints.ipadx = 7;
		constraints.fill = GridBagConstraints.NONE;
		final JButton editButton = new JButton(Messages.getString("VariantMenuPanel.edit")); //$NON-NLS-1$
		editButton.setEnabled(false);
		editButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				Driver.getInstance().pushPanel(
						new VariantCreationPanel(((DefaultListModel) variantList.getModel()).get(variantList.getSelectedIndex())
								.toString()));
			}
		});
		buttonPanel.add(editButton, constraints);

		final JButton deleteButton = new JButton(Messages.getString("VariantMenuPanel.delete")); //$NON-NLS-1$
		deleteButton.setEnabled(false);
		deleteButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (variantList.getSelectedIndices().length > 0)
				{
					int[] selectedIndices = variantList.getSelectedIndices();
					for (int i = selectedIndices.length - 1; i >= 0; i--)
					{
						FileUtility.deleteVariant(mVariantListModel.get(selectedIndices[i]).toString());
						mVariantListModel.removeElementAt(selectedIndices[i]);
					}
				}
				refreshList();
			}
		});
		constraints.ipadx = 8;
		buttonPanel.add(deleteButton, constraints);

		mEditDeletePanel.add(buttonPanel, constraints);

		variantList.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent arg0)
			{
				deleteButton.setEnabled(true);
				editButton.setEnabled(true);
			}
		});

		JButton backButton = new JButton(Messages.getString("VariantMenuPanel.return")); //$NON-NLS-1$
		backButton.setToolTipText(Messages.getString("VariantMenuPanel.return")); //$NON-NLS-1$
		backButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				Driver.getInstance().revertToMainPanel();
			}
		});

		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.insets = new Insets(10, 5, 10, 5);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(backButton, constraints);
		refreshList();
	}

	private void refreshList()
	{
		mVariantListModel.clear();

		List<String> variants = FileUtility.getVariantsFileArrayNoClassic();
		for (String variant: variants)
		{
			mVariantListModel.addElement(variant);
		}

		if (mVariantListModel.size() == 0)
		{
			remove(mEditDeletePanel);
			add(mNoCustomPiecesLabel, mNoCustomLabelConstraints);
		}
		else
		{
			remove(mNoCustomPiecesLabel);
			add(mEditDeletePanel, mEditPanelConstraints);
		}
		
		Driver.getInstance().pack();
		Driver.centerFrame(Driver.getInstance());
	}
	
	private static final long serialVersionUID = -6371389704966320508L;
	
	private DefaultListModel mVariantListModel;
	private final JPanel mEditDeletePanel;
	private final JLabel mNoCustomPiecesLabel;
	private final GridBagConstraints mNoCustomLabelConstraints;
	private final GridBagConstraints mEditPanelConstraints;
}
