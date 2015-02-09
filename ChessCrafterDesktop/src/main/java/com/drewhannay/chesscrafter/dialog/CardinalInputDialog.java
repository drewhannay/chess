package com.drewhannay.chesscrafter.dialog;

import com.drewhannay.chesscrafter.models.CardinalMovement;
import com.drewhannay.chesscrafter.models.Direction;
import com.drewhannay.chesscrafter.panel.ChessPanel;
import com.drewhannay.chesscrafter.utility.GuiUtility;
import com.drewhannay.chesscrafter.utility.Messages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JTextField;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Set;
import java.util.function.Consumer;

public final class CardinalInputDialog extends ChessDialog {

    private final Set<Direction> mDirections;
    private final Consumer<CardinalMovement> mCallback;

    private final CardinalMovement mEditingMovement;

    public CardinalInputDialog(@NotNull Set<Direction> directions, @Nullable CardinalMovement editingMovement,
                               @NotNull Consumer<CardinalMovement> callback) {
        super(true);
        mDirections = directions;
        mEditingMovement = editingMovement;
        mCallback = callback;

        initComponents();
    }

    private void initComponents() {
        boolean isEdit = mEditingMovement != null;

        setSize(new Dimension(200, 200));
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        ChessPanel panel = new ChessPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(GuiUtility.createJLabel(Messages.getString("PieceCrafterDetailPanel.direction")), gbc);

        JComboBox<Direction> directions = new JComboBox<>();
        mDirections.forEach(directions::addItem);

        if (isEdit) {
            directions.setSelectedItem(mEditingMovement.direction);
            directions.setEnabled(false);
        }

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(directions, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        panel.add(GuiUtility.createJLabel(Messages.getString("PieceCrafterDetailPanel.distance")), gbc);

        JTextField distanceField = new JTextField(4);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(distanceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.5;
        panel.add(GuiUtility.createJLabel(Messages.getString("PieceCrafterDetailPanel.unlimited")), gbc);

        JCheckBox unlimited = new JCheckBox();
        unlimited.setOpaque(false);
        unlimited.addActionListener(event -> distanceField.setEnabled(!unlimited.isSelected()));
        if (isEdit && mEditingMovement.distance == Integer.MAX_VALUE) {
            unlimited.setSelected(true);
            distanceField.setEnabled(false);
        }

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 0, 5, 5);
        panel.add(unlimited, gbc);

        JButton saveMovement = new JButton(Messages.getString("PieceCrafterDetailPanel.saveAndClose"));
        saveMovement.addActionListener(event -> {
            int distance;
            if (unlimited.isSelected()) {
                distance = Integer.MAX_VALUE;
            } else {
                distance = Integer.parseInt(distanceField.getText().trim());
            }
            mCallback.accept(CardinalMovement.with(directions.getItemAt(directions.getSelectedIndex()), distance));
            dispose();
        });
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.weightx = 0.5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(saveMovement, gbc);

        add(panel);
    }
}