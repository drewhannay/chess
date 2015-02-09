package com.drewhannay.chesscrafter.dialog;

import com.drewhannay.chesscrafter.models.TwoHopMovement;
import com.drewhannay.chesscrafter.panel.ChessPanel;
import com.drewhannay.chesscrafter.utility.GuiUtility;
import com.drewhannay.chesscrafter.utility.Messages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.function.Consumer;

public final class TwoHopInputDialog extends ChessDialog {

    private final TwoHopMovement mEditingMovement;
    private final Consumer<TwoHopMovement> mCallback;

    public TwoHopInputDialog(@Nullable TwoHopMovement editingMovement, @NotNull Consumer<TwoHopMovement> callback) {
        super(true);
        mEditingMovement = editingMovement;
        mCallback = callback;

        initComponents();
    }

    private void initComponents() {
        boolean isEdit = mEditingMovement != null;

        setSize(new Dimension(300, 200));

        ChessPanel twoHopPopupPanel = new ChessPanel();
        twoHopPopupPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(5, 5, 5, 5);
        twoHopPopupPanel.add(GuiUtility.createJLabel(Messages.getString("PieceCrafterDetailPanel.xDirection")), gbc);

        JTextField xDirection = new JTextField(4);
        xDirection.setText("1");

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        twoHopPopupPanel.add(xDirection, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        twoHopPopupPanel.add(GuiUtility.createJLabel(Messages.getString("PieceCrafterDetailPanel.yDirection")), gbc);

        JTextField yDirection = new JTextField(4);
        yDirection.setText("2");

        if (isEdit) {
            xDirection.setText(String.valueOf(mEditingMovement.x));
            yDirection.setText(String.valueOf(mEditingMovement.y));
        }

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        twoHopPopupPanel.add(yDirection, gbc);

        JButton saveMovement = new JButton(Messages.getString("PieceCrafterDetailPanel.saveAndClose"));
        saveMovement.addActionListener(event -> {
            int x = Integer.parseInt(xDirection.getText());
            int y = Integer.parseInt(yDirection.getText());
            mCallback.accept(TwoHopMovement.with(x, y));
            dispose();
        });
        gbc.gridy = 2;
        gbc.weightx = 0.5;
        twoHopPopupPanel.add(saveMovement, gbc);

        add(twoHopPopupPanel);
    }
}
