package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class PieceMenuPanel extends ChessPanel
{
	public interface PieceMenuManager
	{
		public void onPieceMenuClosed();

		public void openPieceMakerPanel(String pieceName, PieceMenuPanel panel);

		public void openPieceMakerPanel(PieceMenuPanel panel);

		public String getReturnButtonText();

		public void onPieceListChanged();
	}

	public PieceMenuPanel(PieceMenuManager manager)
	{
		mEditDeletePanel = new JPanel();
		mNoCustomPiecesLabel = GuiUtility.createJLabel(Messages.getString("PieceMenuPanel.noCustomPieces")); //$NON-NLS-1$
		mNoCustomLabelConstraints = new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.CENTER,
				new Insets(50, 20, 50, 20), 5, 5);

		mEditPanelConstraints = new GridBagConstraints();
		mEditPanelConstraints.insets = new Insets(0, 0, 0, 0);
		mEditPanelConstraints.gridheight = 3;
		mEditPanelConstraints.ipadx = 7;
		mEditPanelConstraints.gridy = 2;
		
		mManager = manager;
		mPieceListModel = new DefaultListModel();
		initGuiComponents();
	}

	private void initGuiComponents()
	{
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		JButton createNewPieceButton = new JButton(Messages.getString("PieceMenuPanel.createNew")); //$NON-NLS-1$
		createNewPieceButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				mManager.openPieceMakerPanel(PieceMenuPanel.this);
			}
		});

		constraints.gridy = 0;
		constraints.ipadx = 0;
		constraints.gridheight = 1;
		constraints.insets = new Insets(5, 10, 0, 10);
		constraints.anchor = GridBagConstraints.CENTER;
		add(createNewPieceButton, constraints);

		mEditDeletePanel.setOpaque(false);

		mEditDeletePanel.setLayout(new GridBagLayout());

		JScrollPane scrollPane = new JScrollPane();
		final JList pieceList = new JList();
		scrollPane.setViewportView(pieceList);
		refreshList();
		pieceList.setModel(mPieceListModel);
		pieceList.doLayout();

		constraints.gridy = 1;
		constraints.ipadx = 7;
		constraints.insets = new Insets(5, 5, 0, 5);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		mEditDeletePanel.add(scrollPane, constraints);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		buttonPanel.setOpaque(false);

		final JButton editButton = new JButton(Messages.getString("PieceMenuPanel.edit")); //$NON-NLS-1$
		editButton.setEnabled(false);
		editButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				mManager.openPieceMakerPanel(mPieceListModel.get(pieceList.getSelectedIndex()).toString(), PieceMenuPanel.this);
			}
		});

		constraints.gridy = 2;
		constraints.ipadx = 7;
		buttonPanel.add(editButton, constraints);

		final JButton deleteButton = new JButton(Messages.getString("PieceMenuPanel.delete")); //$NON-NLS-1$
		deleteButton.setEnabled(false);
		deleteButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (pieceList.getSelectedIndices().length > 0)
				{
					int[] selectedIndices = pieceList.getSelectedIndices();
					for (int i = selectedIndices.length - 1; i >= 0; i--)
					{
						FileUtility.deletePiece(mPieceListModel.get(selectedIndices[i]).toString());
						mPieceListModel.removeElementAt(selectedIndices[i]);
					}
				}
				refreshList();
			}
		});
		constraints.ipadx = 8;
		buttonPanel.add(deleteButton, constraints);

		constraints.gridy = 2;
		mEditDeletePanel.add(buttonPanel, constraints);

		if (mPieceListModel.size() == 0)
			add(mNoCustomPiecesLabel, mNoCustomLabelConstraints);
		else
			add(mEditDeletePanel, mEditPanelConstraints);

		pieceList.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent arg0)
			{
				boolean isSelected = pieceList.getSelectedIndex() != -1;
				deleteButton.setEnabled(isSelected);
				editButton.setEnabled(isSelected);
			}
		});

		JButton doneButton = new JButton(mManager.getReturnButtonText());
		doneButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				mManager.onPieceMenuClosed();
			}
		});

		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		constraints.insets = new Insets(15, 5, 10, 5);
		add(doneButton, constraints);
	}

	public void refreshList()
	{
		mPieceListModel.clear();

		String[] pieceArray = FileUtility.getCustomPieceArray();
		for (int i = 0; i < pieceArray.length; i++)
		{
			mPieceListModel.addElement(pieceArray[i]);
		}

		if (mPieceListModel.size() == 0)
		{
			remove(mEditDeletePanel);
			add(mNoCustomPiecesLabel, mNoCustomLabelConstraints);
		}
		else
		{
			remove(mNoCustomPiecesLabel);
			add(mEditDeletePanel, mEditPanelConstraints);
		}
		mManager.onPieceListChanged();
	}

	private static final long serialVersionUID = -6371389704966320508L;

	private DefaultListModel mPieceListModel;
	private PieceMenuManager mManager;
	private final JLabel mNoCustomPiecesLabel;
	final JPanel mEditDeletePanel;
	private final GridBagConstraints mNoCustomLabelConstraints;
	private final GridBagConstraints mEditPanelConstraints;
}
