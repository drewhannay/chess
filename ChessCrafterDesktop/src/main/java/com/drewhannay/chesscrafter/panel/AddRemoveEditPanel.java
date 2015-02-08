package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.utility.GuiUtility;
import org.jetbrains.annotations.NotNull;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionListener;

public class AddRemoveEditPanel extends ChessPanel {

    public final JButton mAdd;
    public final JButton mRemove;
    public final JButton mEdit;

    private static final int WIDTH = GuiUtility.isMac() ? -4 : 4;

    public AddRemoveEditPanel() {
        super(false);

        mAdd = new JButton();
        mRemove = new JButton();
        mEdit = new JButton();

        initComponents();
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        configureButton(mAdd, GuiUtility.createSystemImageIcon(25, 25, "/add_button.png"));
        configureButton(mRemove, GuiUtility.createSystemImageIcon(25, 25, "/remove_button.png"));
        configureButton(mEdit, GuiUtility.createSystemImageIcon(27, 27, "/edit_button.png"));

        add(mAdd);
        add(Box.createRigidArea(new Dimension(WIDTH, 0)));
        add(mRemove);
        add(Box.createHorizontalGlue());
        add(Box.createRigidArea(new Dimension(WIDTH, 0)));
        add(mEdit);
    }

    public void addAddActionListener(ActionListener actionListener) {
        mAdd.addActionListener(actionListener);
    }

    public void addRemoveActionListener(ActionListener actionListener) {
        mRemove.addActionListener(actionListener);
    }

    public void addEditActionListener(ActionListener actionListener) {
        mEdit.addActionListener(actionListener);
    }

    private void configureButton(@NotNull JButton button, @NotNull Icon icon) {
        button.setDisabledIcon(icon);
        button.setIcon(icon);
        button.setPressedIcon(icon);

        button.putClientProperty("JButton.buttonType", "square");
        button.setMinimumSize(new Dimension(10, 10));
        button.setPreferredSize(new Dimension(25, 25));
        button.setMaximumSize(new Dimension(25, 25));
        button.setMargin(new Insets(0, 0, 0, 0));
    }
}
