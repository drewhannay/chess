package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.utility.Messages;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class AddRemoveEditPanel extends JPanel{

    public <T> AddRemoveEditPanel(DefaultListModel<T> movementListModel,ListCellRenderer<T> movementRenderer,
                                  String title){
        setOpaque(false);
        setLayout(new GridBagLayout());

        JList<T> movementList = new JList<>(movementListModel);
        movementList.setCellRenderer(movementRenderer);

        JScrollPane cardinalScrollPane = new JScrollPane(movementList);
        cardinalScrollPane.setPreferredSize(new Dimension(150, 140));
        cardinalScrollPane.setBorder(BorderFactory.createTitledBorder(title));
        cardinalScrollPane.getVerticalScrollBar().setUnitIncrement(25);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridwidth = 3;
        constraints.insets = new Insets(0, 10, 10, 10);
        constraints.gridx = 0;
        add(cardinalScrollPane, constraints);

        JButton addMovement = new JButton("+");
        //addMovement.addActionListener(event -> createMovementPopup(movementListModel, null, 0));
        constraints = new GridBagConstraints();
        constraints.gridy = 1;
        constraints.insets = new Insets(0, 10, 0, 0);
        add(addMovement, constraints);

        JButton removeMovement = new JButton("-");
        removeMovement.addActionListener(event -> {
            if(movementList.getSelectedIndex() == -1){
                JOptionPane.showMessageDialog(null, Messages.getString("PieceCrafterDetailPanel.selectItem"),
                        Messages.getString("PieceCrafterDetailPanel.error"), 0);
                return;
            }
            movementListModel.remove(movementList.getSelectedIndex());
        });
        constraints.gridx = 1;
        add(removeMovement, constraints);

        JButton editMovement = new JButton("Edit");
        editMovement.addActionListener(event -> {
            if(movementList.getSelectedIndex() == -1){
                JOptionPane.showMessageDialog(null, Messages.getString("PieceCrafterDetailPanel.selectItem"),
                        Messages.getString("PieceCrafterDetailPanel.error"), 0);
                return;
            }
            //createMovementPopup(movementListModel, movementList.getSelectedValue(), movementList.getSelectedIndex());
        });
        constraints.gridx = 2;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(0, 10, 0, 10);
        add(editMovement, constraints);
    }
}
