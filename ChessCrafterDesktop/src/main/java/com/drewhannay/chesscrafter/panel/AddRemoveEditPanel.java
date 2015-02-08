package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.utility.GuiUtility;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionListener;

public class AddRemoveEditPanel extends ChessPanel {

    public final JButton mAdd;
    public final JButton mRemove;
    public final JButton mEdit;

    public AddRemoveEditPanel() {
        super(false);

        mAdd = new JButton();
        mRemove = new JButton();
        mEdit = new JButton();

        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        mAdd.putClientProperty("JButton.buttonType", "gradient");
        mAdd.setDisabledIcon(GuiUtility.createSystemImageIcon(25, 25, "/add_button.png"));
        mAdd.setIcon(GuiUtility.createSystemImageIcon(25, 25, "/add_button.png"));
        mAdd.setMinimumSize(new Dimension(10, 10));
        mAdd.setPreferredSize(new Dimension(25, 25));
        mAdd.setMaximumSize(new Dimension(25, 25));
        mAdd.setMargin(new Insets(0, 0, 0, 0));
        mRemove.putClientProperty("JButton.buttonType", "gradient");
        mRemove.setDisabledIcon(GuiUtility.createSystemImageIcon(25, 25, "/remove_button.png"));
        mRemove.setIcon(GuiUtility.createSystemImageIcon(25, 25, "/remove_button.png"));
        mRemove.setMinimumSize(new Dimension(10, 10));
        mRemove.setPreferredSize(new Dimension(25, 25));
        mRemove.setMaximumSize(new Dimension(25, 25));
        mRemove.setMargin(new Insets(0, 0, 0, 0));
        mEdit.putClientProperty("JButton.buttonType", "gradient");
        mEdit.setDisabledIcon(GuiUtility.createSystemImageIcon(27, 27, "/edit_button.png"));
        mEdit.setIcon(GuiUtility.createSystemImageIcon(27, 27, "/edit_button.png"));
        mEdit.setMinimumSize(new Dimension(10, 10));
        mEdit.setPreferredSize(new Dimension(25, 25));
        mEdit.setMaximumSize(new Dimension(25, 25));
        mEdit.setMargin(new Insets(0, 0, 0, 0));

        add(mAdd);
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(mRemove);
        add(Box.createHorizontalGlue());
        add(Box.createRigidArea(new Dimension(5, 0)));
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
}
