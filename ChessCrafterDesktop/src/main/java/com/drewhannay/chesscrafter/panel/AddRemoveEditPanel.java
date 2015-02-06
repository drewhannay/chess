package com.drewhannay.chesscrafter.panel;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import java.awt.Dimension;

public class AddRemoveEditPanel extends JPanel {

    public <T> AddRemoveEditPanel(DefaultListModel<T> movementListModel, ListCellRenderer<T> movementRenderer) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        add(Box.createRigidArea(new Dimension(10, 0)));

        JButton addMovement = new JButton("+");
        add(addMovement);

        add(Box.createRigidArea(new Dimension(5, 0)));

        JButton removeMovement = new JButton("-");
        add(removeMovement);

        add(Box.createHorizontalGlue());

        JButton editMovement = new JButton("Edit");
        add(editMovement);

        add(Box.createRigidArea(new Dimension(10, 0)));
    }
}
