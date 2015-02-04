package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.logic.PieceTypeManager;
import com.drewhannay.chesscrafter.models.PieceType;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class PieceCrafterMasterPanel extends ChessPanel {

    private final Runnable mNewPieceCallback;
    private final Consumer<PieceType> mPieceTypeSelectedCallback;

    private final JList<PieceType> mPieceList;
    private final DefaultListModel<PieceType> mPieceListModel;

    public PieceCrafterMasterPanel(Runnable newPieceCallback, Consumer<PieceType> pieceTypeSelectedCallback) {
        mNewPieceCallback = newPieceCallback;
        mPieceTypeSelectedCallback = pieceTypeSelectedCallback;

        mPieceListModel = new DefaultListModel<>();
        mPieceList = new JList<>(mPieceListModel);
        refreshList();

        initComponents();
        validate();
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
            mPieceTypeSelectedCallback.accept(mPieceList.getSelectedValue());
        });

        JScrollPane scrollPane = new JScrollPane(mPieceList);
        scrollPane.setFont(new Font(mPieceList.getName(), Font.PLAIN, 26));
        scrollPane.setPreferredSize(new Dimension(190, 500));

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = .5;
        constraints.weighty = 1.0;
        constraints.insets = new Insets(25, 25, 25, 25);
        add(scrollPane, constraints);
    }

    public void refreshList() {
        mPieceListModel.clear();

        // TODO: currently adding duplicates to force the list to scroll
        IntStream.range(0, 6).forEach((i) -> PieceTypeManager.INSTANCE.getAllPieceTypes().forEach(mPieceListModel::addElement));
    }

    private void deletePiece() {
        // TODO: need to delete the actual custom piece
        // TODO: don't allow deleting classic pieces
        int index = mPieceList.getSelectedIndex();
        if (index >= 0) {
            mPieceListModel.remove(index);
        }
    }

    private final ListCellRenderer<PieceType> mCellRenderer = (JList<? extends PieceType> list, PieceType value,
                                                               int index, boolean isSelected, boolean cellHasFocus) -> {
        JLabel label = new JLabel(value.getInternalId());
        label.setOpaque(true);
        label.setFont(new Font(label.getName(), Font.PLAIN, 16));
        label.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
        label.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());

        // TODO: render custom pieceType view here
        return label;
    };
}
