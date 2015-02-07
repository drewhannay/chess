package com.drewhannay.chesscrafter.panel;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.event.ActionListener;

public class AddRemoveEditPanel extends ChessPanel {

    public final JButton mAdd;
    public final JButton mRemove;
    public final JButton mEdit;

    public AddRemoveEditPanel() {
        super(false);

        mAdd = new JButton("+");
        mRemove = new JButton("-");
        mEdit = new JButton("Edit");

        initComponents();
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        add(Box.createRigidArea(new Dimension(10, 0)));
        add(mAdd);
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(mRemove);
        add(Box.createHorizontalGlue());
        add(mEdit);
        add(Box.createRigidArea(new Dimension(10, 0)));
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
}
