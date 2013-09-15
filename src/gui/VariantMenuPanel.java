package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import utility.FileUtility;

public class VariantMenuPanel extends JPanel
{
	public VariantMenuPanel()
	{
		initGuiComponents();
	}

	private void initGuiComponents()
	{
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridy = 0;
		constraints.ipadx = 0;
		constraints.insets = new Insets(5, 50, 5, 50);
		constraints.anchor = GridBagConstraints.CENTER;

		JButton createNewVariantButton = new JButton("Create New Variant");
		createNewVariantButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (mPopupFrame == null)
				{
					Driver.getInstance().setPanel(new CustomSetupPanel());
				}
			}
		});
		add(createNewVariantButton, constraints);

		final JPanel editDeletePanel = new JPanel();
		editDeletePanel.setBorder(BorderFactory.createLoweredBevelBorder());
		editDeletePanel.setLayout(new GridBagLayout());

		constraints.gridy = 1;
		constraints.ipadx = 7;
		constraints.insets = new Insets(5, 5, 0, 5);

		final JList variantList = new JList();
		DefaultListModel variantListModel = new DefaultListModel();
		String[] variantArray = FileUtility.getVariantsFileArrayNoClassic();
		for (int i = 0; i < variantArray.length; i++)
		{
			variantListModel.addElement(variantArray[i]);
		}
		variantList.setModel(variantListModel);

		editDeletePanel.add(variantList, constraints);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());

		constraints.gridy = 2;
		constraints.ipadx = 7;
		final JButton editButton = new JButton("Edit");
		editButton.setEnabled(false);
		editButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				
			}
		});
		buttonPanel.add(editButton, constraints);

		final JButton deleteButton = new JButton("Delete");
		deleteButton.setEnabled(false);
		deleteButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				File variantFile = new File(System.getProperty("user.dir") + "\\variants\\"
						+ variantList.getSelectedValue().toString());
				((DefaultListModel) (variantList.getModel())).removeElementAt(variantList.getSelectedIndex());
				variantFile.delete();
						
				if (((DefaultListModel) (variantList.getModel())).size() == 1)
				{
					editButton.setEnabled(false);
					deleteButton.setEnabled(false);
					editDeletePanel.setVisible(false);
				}
			}
		});
		constraints.ipadx = 8;
		buttonPanel.add(deleteButton, constraints);

		constraints.gridy = 2;
		editDeletePanel.add(buttonPanel, constraints);

		constraints.gridy = 1;
		add(editDeletePanel, constraints);

		variantList.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent arg0)
			{
				deleteButton.setEnabled(true);
				//TODO: enable this button once editing variants works
//				editButton.setEnabled(true);
			}
		});

		JButton backButton = new JButton("Return to Main Menu");
		backButton.setToolTipText("Return to the Main Menu");
		backButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				Driver.getInstance().revertToMainPanel();
			}
		});

		constraints.gridy = 2;
		add(backButton, constraints);
	}

	private static final long serialVersionUID = -6371389704966320508L;

	private JFrame mPopupFrame;
}
