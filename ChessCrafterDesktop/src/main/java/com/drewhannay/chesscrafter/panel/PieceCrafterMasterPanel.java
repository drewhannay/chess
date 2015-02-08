package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.logic.PieceTypeManager;
import com.drewhannay.chesscrafter.models.PieceType;
import org.jetbrains.annotations.NotNull;

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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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

        setBorder(BorderFactory.createEmptyBorder(25,25,25,25));

        JScrollPane scrollPane = new JScrollPane(mPieceList);
        scrollPane.setFont(new Font(mPieceList.getName(), Font.PLAIN, 26));
        scrollPane.setPreferredSize(new Dimension(190, 500));

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(scrollPane);
        add(Box.createRigidArea(new Dimension(0, 10)));
        AddRemoveEditPanel buttons = new AddRemoveEditPanel();
        //TODO hookup the buttons to actual actions
        add(buttons);
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
        JLabel label = new JLabel(value.getName());
        label.setOpaque(true);
        label.setFont(new Font(label.getName(), Font.PLAIN, 16));
        label.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
        label.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());

        // TODO: render custom pieceType view here
        return label;
    };
}
