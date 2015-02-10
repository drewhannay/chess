package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.logic.PieceTypeManager;
import com.drewhannay.chesscrafter.models.PieceType;
import com.drewhannay.chesscrafter.utility.FileManager;
import com.drewhannay.chesscrafter.utility.Messages;
import com.google.common.base.Preconditions;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import java.awt.Dimension;
import java.awt.Font;
import java.util.UUID;
import java.util.function.Consumer;

public class PieceCrafterMasterPanel extends ChessPanel {

    private final Consumer<PieceType> mPieceTypeSelectedCallback;

    private final JList<PieceType> mPieceList;
    private final DefaultListModel<PieceType> mPieceListModel;
    private final AddRemoveEditPanel mButtons;

    public PieceCrafterMasterPanel(Consumer<PieceType> pieceTypeSelectedCallback) {
        mPieceTypeSelectedCallback = pieceTypeSelectedCallback;

        mPieceListModel = new DefaultListModel<>();
        mPieceList = new JList<>(mPieceListModel);
        mButtons = new AddRemoveEditPanel();
        refreshList();

        initComponents();
        validate();

        // select the first piece by default
        mPieceList.setSelectedIndex(0);
    }

    private void initComponents() {
        mPieceList.setCellRenderer(mCellRenderer);
        mPieceList.setLayoutOrientation(JList.VERTICAL);
        mPieceList.setVisibleRowCount(-1);
        mPieceList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mPieceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mPieceList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            PieceType pieceType = mPieceList.getSelectedValue();
            mButtons.mRemove.setEnabled(!PieceTypeManager.INSTANCE.isSystemPiece(pieceType.getInternalId()));
            mPieceTypeSelectedCallback.accept(pieceType);
        });

        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JScrollPane scrollPane = new JScrollPane(mPieceList);
        scrollPane.setFont(new Font(mPieceList.getName(), Font.PLAIN, 26));
        scrollPane.setPreferredSize(new Dimension(190, 500));

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(scrollPane);
        add(Box.createRigidArea(new Dimension(0, 10)));

        mButtons.mEdit.setVisible(false);
        mButtons.mAdd.addActionListener(e -> createPiece());
        mButtons.mRemove.addActionListener(e -> deletePiece());

        add(mButtons);
    }

    public void refreshList() {
        mPieceListModel.clear();

        PieceTypeManager.INSTANCE.getAllPieceTypes().forEach(mPieceListModel::addElement);
    }

    private void createPiece() {
        PieceType pieceType = new PieceType(UUID.randomUUID().toString(), Messages.getString("PieceType.newPiece"), null, null);

        if (FileManager.INSTANCE.writePiece(pieceType)) {
            PieceTypeManager.INSTANCE.registerPieceType(pieceType);
            mPieceListModel.addElement(pieceType);
            mPieceList.setSelectedValue(pieceType, true);
        } else {
            // TODO: notify user of failure
        }
    }

    private void deletePiece() {
        PieceType pieceType = mPieceList.getSelectedValue();
        Preconditions.checkState(!PieceTypeManager.INSTANCE.isSystemPiece(pieceType.getInternalId()));

        if (FileManager.INSTANCE.deletePiece(pieceType)) {
            PieceTypeManager.INSTANCE.unregisterPieceType(pieceType);
            mPieceListModel.removeElement(pieceType);
        } else {
            // TODO: notify user of failure
        }
    }

    private final ListCellRenderer<PieceType> mCellRenderer = (JList<? extends PieceType> list, PieceType value,
                                                               int index, boolean isSelected, boolean cellHasFocus) -> {
        JLabel label = new JLabel(value.getName());
        label.setOpaque(true);
        label.setFont(new Font(label.getName(), Font.PLAIN, 16));
        label.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
        label.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());

        // TODO: render custom pieceType view here
        return label;
    };
}
