package com.drewhannay.chesscrafter.gui;

import com.drewhannay.chesscrafter.utility.FileUtility;

import javax.swing.*;
import java.awt.*;

public class PieceMenuPanel extends ChessPanel {

    public PieceMenuPanel() {
        this(null);
    }

    public PieceMenuPanel(JFrame frame) {
        mFrame = frame;
        mPieceListModel = new DefaultListModel<>();
        initGuiComponents();
    }

    private void initGuiComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridy = 0;
        constraints.ipadx = 0;
        constraints.insets = new Insets(5, 10, 0, 10);
        constraints.anchor = GridBagConstraints.CENTER;

        JButton createNewPieceButton = new JButton(Messages.getString("PieceMenuPanel.createNew")); //$NON-NLS-1$
        createNewPieceButton.addActionListener(event -> new PieceMakerPanel(PieceMenuPanel.this));
        add(createNewPieceButton, constraints);

        final JPanel editDeletePanel = new JPanel();
        editDeletePanel.setSize(500, 500);
        editDeletePanel.setOpaque(false);

        editDeletePanel.setLayout(new GridBagLayout());

        constraints.gridy = 1;
        constraints.ipadx = 7;
        constraints.insets = new Insets(5, 5, 0, 5);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        JScrollPane scrollPane = new JScrollPane();
        final JList<String> pieceList = new JList<>();
        scrollPane.setSize(500, 500);
        pieceList.setSize(500, 500);
        scrollPane.setViewportView(pieceList);
        refreshList();
        pieceList.setModel(mPieceListModel);
        pieceList.doLayout();

        editDeletePanel.add(scrollPane, constraints);
        editDeletePanel.setVisible(mPieceListModel.size() != 0);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setOpaque(false);

        constraints.gridy = 2;
        constraints.ipadx = 7;
        final JButton editButton = new JButton(Messages.getString("PieceMenuPanel.edit")); //$NON-NLS-1$
        editButton.setEnabled(false);
        editButton.addActionListener(event -> new PieceMakerPanel(mPieceListModel.get(pieceList.getSelectedIndex()), PieceMenuPanel.this));
        buttonPanel.add(editButton, constraints);

        final JButton deleteButton = new JButton(Messages.getString("PieceMenuPanel.delete")); //$NON-NLS-1$
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(event -> {
            if (pieceList.getSelectedIndices().length > 0) {
                int[] selectedIndices = pieceList.getSelectedIndices();
                for (int i = selectedIndices.length - 1; i >= 0; i--) {
                    FileUtility.deletePiece(mPieceListModel.get(selectedIndices[i]));
                    mPieceListModel.removeElementAt(selectedIndices[i]);
                }
            }

            if (mPieceListModel.size() == 0) {
                editButton.setEnabled(false);
                deleteButton.setEnabled(false);
                editDeletePanel.setVisible(false);
            }
        });
        constraints.ipadx = 8;
        buttonPanel.add(deleteButton, constraints);

        constraints.gridy = 2;
        editDeletePanel.add(buttonPanel, constraints);

        constraints.gridy = 1;
        add(editDeletePanel, constraints);

        pieceList.addListSelectionListener(arg0 -> {
            boolean isSelected = pieceList.getSelectedIndex() != -1;
            deleteButton.setEnabled(isSelected);
            editButton.setEnabled(isSelected);
        });

        if (mFrame == null) {
            JButton backButton = new JButton(Messages.getString("PieceMenuPanel.returnToMenu")); //$NON-NLS-1$
            backButton.setToolTipText(Messages.getString("PieceMenuPanel.returnToMenu")); //$NON-NLS-1$
            backButton.addActionListener(event -> Driver.getInstance().revertToMainPanel());

            constraints.gridy = 2;
            constraints.insets = new Insets(15, 5, 10, 5);
            add(backButton, constraints);
        } else {
            JButton doneButton = new JButton(Messages.getString("PieceMenuPanel.done")); //$NON-NLS-1$
            doneButton.setToolTipText(Messages.getString("PieceMenuPanel.returnToVariant")); //$NON-NLS-1$
            doneButton.addActionListener(arg0 -> mFrame.dispose());

            constraints.gridy = 2;
            constraints.insets = new Insets(15, 5, 10, 5);
            add(doneButton, constraints);
        }

        if (mFrame != null) {
            mFrame.setTitle(Messages.getString("PieceMenuPanel.pieceMenu")); //$NON-NLS-1$
            mFrame.setSize(225, 300);
            mFrame.add(this);
            mFrame.setLocationRelativeTo(Driver.getInstance());
            mFrame.setVisible(true);
        }
    }

    private static final long serialVersionUID = -6371389704966320508L;

    private DefaultListModel<String> mPieceListModel;
    private JFrame mFrame;

    private PieceMakerPanel.PieceListChangedListener mListener;

    public void setPieceListChangedListener(PieceMakerPanel.PieceListChangedListener listener) {
        mListener = listener;
    }

    public void refreshList() {
        mPieceListModel.clear();

        String[] pieceArray = FileUtility.getCustomPieceArray();
        for (String aPieceArray : pieceArray) {
            mPieceListModel.addElement(aPieceArray);
        }

        if (mListener != null)
            mListener.onPieceListChanged();
    }
}
