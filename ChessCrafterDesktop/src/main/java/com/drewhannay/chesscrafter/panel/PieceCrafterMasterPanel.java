package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.files.AbstractChessFileListener;
import com.drewhannay.chesscrafter.files.ChessFileListener;
import com.drewhannay.chesscrafter.files.FileManager;
import com.drewhannay.chesscrafter.logic.PieceTypeManager;
import com.drewhannay.chesscrafter.models.PieceType;
import com.drewhannay.chesscrafter.utility.Messages;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
        mPieceList.addListSelectionListener(mListSelectionListener);

        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JScrollPane scrollPane = new JScrollPane(mPieceList);
        scrollPane.setFont(new Font(mPieceList.getName(), Font.PLAIN, 26));
        scrollPane.setPreferredSize(new Dimension(190, 500));

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(scrollPane, gbc);

        mButtons.mEdit.setVisible(false);
        mButtons.mAdd.addActionListener(e -> createPiece());
        mButtons.mRemove.addActionListener(e -> deletePiece());
        gbc.gridy = 2;
        gbc.weighty = 0.1;
        add(mButtons, gbc);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        FileManager.INSTANCE.addChessFileListener(mFileListener);
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        FileManager.INSTANCE.removeChessFileListener(mFileListener);
    }

    private void refreshList() {
        int selectedIndex = mPieceList.getSelectedIndex();
        String selectedInternalId = selectedIndex < 0 ? null : mPieceList.getSelectedValue().getInternalId();
        mPieceList.removeListSelectionListener(mListSelectionListener);

        mPieceListModel.clear();
        PieceTypeManager.INSTANCE.getAllPieceTypes().stream()
                .sorted((p1, p2) -> p1.getName().compareTo(p2.getName()))
                .forEach(mPieceListModel::addElement);

        mPieceList.addListSelectionListener(mListSelectionListener);
        if (selectedInternalId != null && PieceTypeManager.INSTANCE.hasPieceTypeWithId(selectedInternalId)) {
            mPieceList.setSelectedValue(PieceTypeManager.INSTANCE.getPieceTypeById(selectedInternalId), true);
        } else {
            mPieceList.setSelectedIndex(selectedIndex < 0 ? 0 : selectedIndex);
        }
    }

    private void createPiece() {
        PieceType pieceType = new PieceType(UUID.randomUUID().toString(), Messages.getString("PieceType.newPiece"), null, null);

        if (FileManager.INSTANCE.writePiece(pieceType)) {
            mPieceList.setSelectedValue(pieceType, true);
        } else {
            // TODO: notify user of failure
        }
    }

    private void deletePiece() {
        PieceType pieceType = mPieceList.getSelectedValue();
        Preconditions.checkState(!PieceTypeManager.INSTANCE.isSystemPiece(pieceType.getInternalId()));

        if (!FileManager.INSTANCE.deletePiece(pieceType)) {
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

    private final ListSelectionListener mListSelectionListener = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) {
                return;
            }
            PieceType pieceType = mPieceList.getSelectedValue();
            mButtons.mRemove.setEnabled(pieceType != null && !PieceTypeManager.INSTANCE.isSystemPiece(pieceType.getInternalId()));
            if (pieceType != null) {
                mPieceTypeSelectedCallback.accept(pieceType);
            }
        }
    };

    private final ChessFileListener mFileListener = new AbstractChessFileListener() {
        @Override
        public void onPieceFileChanged(@NotNull String internalId) {
            refreshList();
        }
    };
}
