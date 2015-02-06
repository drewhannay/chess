package com.drewhannay.chesscrafter.utility;

import com.drewhannay.chesscrafter.models.Direction;
import com.drewhannay.chesscrafter.models.TwoHopMovement;
import com.drewhannay.chesscrafter.panel.ChessPanel;
import javafx.util.*;
import javafx.util.Pair;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PieceCrafterUtility {

    public static void createMovementButtonPanel(JPanel movementPanel,
                                                 DefaultListModel<Pair<Direction, Integer>> movementListModel,
                                                 ListCellRenderer<Pair<Direction, Integer>> movementRenderer, int row,
                                                 String title){
        JList<Pair<Direction, Integer>> movementList = new JList<>(movementListModel);
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
        constraints.gridx = row;
        movementPanel.add(cardinalScrollPane, constraints);

        JButton addMovement = new JButton("+");
        addMovement.addActionListener(event -> createMovementPopup(movementListModel, null, 0));
        constraints = new GridBagConstraints();
        constraints.gridy = 1;
        constraints.insets = new Insets(0, 10, 0, 0);
        movementPanel.add(addMovement, constraints);

        JButton removeMovement = new JButton("-");
        removeMovement.addActionListener(event -> {
            if(movementList.getSelectedIndex() == -1){
                JOptionPane.showMessageDialog(null, Messages.getString("PieceCrafterDetailPanel.selectItem"),
                        Messages.getString("PieceCrafterDetailPanel.error"), 0);
                return;
            }
            movementListModel.remove(movementList.getSelectedIndex());
        });
        constraints.gridx = row + 1;
        movementPanel.add(removeMovement, constraints);

        JButton editMovement = new JButton("Edit");
        editMovement.addActionListener(event -> {
            if(movementList.getSelectedIndex() == -1){
                JOptionPane.showMessageDialog(null, Messages.getString("PieceCrafterDetailPanel.selectItem"),
                        Messages.getString("PieceCrafterDetailPanel.error"), 0);
                return;
            }
            createMovementPopup(movementListModel, movementList.getSelectedValue(), movementList.getSelectedIndex());
        });
        constraints.gridx = row + 2;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(0, 10, 0, 10);
        movementPanel.add(editMovement, constraints);
    }

    public static void createTwoHopButtonPanel(JPanel movementPanel,
                                                 DefaultListModel<TwoHopMovement> twoHopListModel,
                                                 ListCellRenderer<TwoHopMovement> twoHopRenderer) {
        JList<TwoHopMovement> twoHopList = new JList<>(twoHopListModel);
        twoHopList.setCellRenderer(twoHopRenderer);

        JScrollPane twoHopScrollPane = new JScrollPane(twoHopList);
        twoHopScrollPane.setPreferredSize(new Dimension(150, 140));
        twoHopScrollPane.setBorder(BorderFactory.createTitledBorder(Messages.getString("PieceCrafterDetailPanel.twoHop")));
        twoHopScrollPane.getVerticalScrollBar().setUnitIncrement(25);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = 3;
        constraints.gridx = 6;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.insets = new Insets(0, 10, 10, 10);
        movementPanel.add(twoHopScrollPane, constraints);

        JButton addTwoHop = new JButton("+");
        addTwoHop.addActionListener(event -> createTwoHopMovementPopup(null, null, 0));
        constraints = new GridBagConstraints();
        constraints.gridy = 1;
        constraints.gridx = 6;

        constraints.insets = new Insets(0, 10, 0, 0);
        movementPanel.add(addTwoHop, constraints);

        JButton removeTwoHop = new JButton("-");
        removeTwoHop.addActionListener(event -> {
            if(twoHopList.getSelectedIndex() == -1){
                JOptionPane.showMessageDialog(null, Messages.getString("PieceCrafterDetailPanel.selectItem"),
                        Messages.getString("PieceCrafterDetailPanel.error"), 0);
                return;
            }
            twoHopListModel.remove(twoHopList.getSelectedIndex());
        });
        constraints.gridx = 7;
        movementPanel.add(removeTwoHop, constraints);

        JButton editTwoHop = new JButton("Edit");
        editTwoHop.addActionListener(event -> {
            if(twoHopList.getSelectedIndex() == -1){
                JOptionPane.showMessageDialog(null, Messages.getString("PieceCrafterDetailPanel.selectItem"),
                        Messages.getString("PieceCrafterDetailPanel.error"), 0);
                return;
            }
            createTwoHopMovementPopup(twoHopListModel, twoHopList.getSelectedValue(),
                    twoHopList.getSelectedIndex());
        });
        constraints.gridx = 8;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(0, 10, 0, 10);
        movementPanel.add(editTwoHop, constraints);
    }

    public static void createMovementPopup(DefaultListModel<javafx.util.Pair<Direction, Integer>> model,
                                           javafx.util.Pair<Direction, Integer> movement, int selectedIndex) {
        List<Direction> unusedDirections = getUnusedDirections(model);

        if (movement == null && unusedDirections.size() == 0) {
            JOptionPane.showMessageDialog(null, Messages.getString("PieceCrafterDetailPanel.noMoreDirections"),
                    Messages.getString("PieceCrafterDetailPanel.error"), 0);
            return;
        }

        JFrame movementPopupFrame = new JFrame();
        movementPopupFrame.setSize(new Dimension(300, 200));
        movementPopupFrame.setVisible(false);
        movementPopupFrame.setLocationRelativeTo(null);

        ChessPanel movementPopupPanel = new ChessPanel();
        movementPopupPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(5, 5, 5, 5);
        movementPopupPanel.add(GuiUtility.createJLabel(Messages.getString("PieceCrafterDetailPanel.direction")), gbc);

        JComboBox directions;
        if (movement != null) {
            directions = new JComboBox(Direction.values());
            directions.setSelectedItem(movement.getKey());
            directions.setEnabled(false);
        } else {
            directions = new JComboBox(unusedDirections.toArray());
        }

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        movementPopupPanel.add(directions, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        movementPopupPanel.add(GuiUtility.createJLabel(Messages.getString("PieceCrafterDetailPanel.distance")), gbc);

        JTextField distance = new JTextField(4);
        if (movement != null) {
            if (movement.getValue() == Integer.MAX_VALUE) {
                distance.setText(Messages.getString("PieceCrafterDetailPanel.unlimited"));
            } else {
                distance.setText(movement.getValue().toString());
            }
        }
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        movementPopupPanel.add(distance, gbc);

        JButton saveMovement = new JButton(Messages.getString("PieceCrafterDetailPanel.saveAndClose"));
        saveMovement.addActionListener(event -> {
            int movementDistance = Integer.parseInt(distance.getText());
            if (distance.getText().toLowerCase().equals("unlimited")) {
                movementDistance = Integer.MAX_VALUE;
            }
            if (movement != null) {
                model.remove(selectedIndex);
                model.add(selectedIndex, new Pair<Direction, Integer>((Direction) directions.getSelectedItem(), movementDistance));
            } else {
                model.addElement(new Pair<Direction, Integer>((Direction) directions.getSelectedItem(),
                        movementDistance));
            }
            movementPopupFrame.dispose();
        });
        gbc.gridy = 2;
        gbc.weightx = 0.5;
        movementPopupPanel.add(saveMovement, gbc);

        movementPopupFrame.add(movementPopupPanel);
        movementPopupFrame.setVisible(true);
    }

    public static void createTwoHopMovementPopup(DefaultListModel<TwoHopMovement> model, TwoHopMovement movement,
                                                 int selectedIndex) {
        JFrame twoHopPopupFrame = new JFrame();
        twoHopPopupFrame.setSize(new Dimension(300, 200));
        twoHopPopupFrame.setVisible(false);
        twoHopPopupFrame.setLocationRelativeTo(null);

        ChessPanel twoHopPopupPanel = new ChessPanel();
        twoHopPopupPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(5, 5, 5, 5);
        twoHopPopupPanel.add(GuiUtility.createJLabel(Messages.getString("PieceCrafterDetailPanel.xDirection")), gbc);

        JTextField xDirection = new JTextField(4);
        xDirection.setText("0");

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        twoHopPopupPanel.add(xDirection, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        twoHopPopupPanel.add(GuiUtility.createJLabel(Messages.getString("PieceCrafterDetailPanel.yDirection")), gbc);

        JTextField yDirection = new JTextField(4);
        yDirection.setText("0");
        if (movement != null) {
            xDirection.setText(movement.x + "");
            yDirection.setText(movement.y + "");
        }
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        twoHopPopupPanel.add(yDirection, gbc);

        JButton saveMovement = new JButton(Messages.getString("PieceCrafterDetailPanel.saveAndClose"));
        saveMovement.addActionListener(event -> {
            int xMovementDistance = Integer.parseInt(xDirection.getText());
            int yMovementDistance = Integer.parseInt(yDirection.getText());
            if (movement != null) {
                model.remove(selectedIndex);
                model.add(selectedIndex, TwoHopMovement.with(xMovementDistance, yMovementDistance));
            } else {
                model.addElement(TwoHopMovement.with(xMovementDistance, yMovementDistance));
            }
            twoHopPopupFrame.dispose();
        });
        gbc.gridy = 2;
        gbc.weightx = 0.5;
        twoHopPopupPanel.add(saveMovement, gbc);

        twoHopPopupFrame.add(twoHopPopupPanel);
        twoHopPopupFrame.setVisible(true);
    }

    public static List getUnusedDirections(DefaultListModel<javafx.util.Pair<Direction, Integer>> model) {
        List<Direction> unusedDirections =
                Stream.of(Direction.values())
                        .filter(d -> IntStream.range(0, model.size()).allMatch(i -> model.get(i).getKey() != d))
                        .collect(Collectors.toList());
        return unusedDirections;
    }
}
