package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.dragNdrop.DropManager;
import com.drewhannay.chesscrafter.dragNdrop.GlassPane;
import com.drewhannay.chesscrafter.dragNdrop.SquareConfig;
import com.drewhannay.chesscrafter.models.BoardSize;
import com.drewhannay.chesscrafter.models.Direction;
import com.drewhannay.chesscrafter.models.PieceType;
import com.drewhannay.chesscrafter.models.TwoHopMovement;
import com.drewhannay.chesscrafter.utility.GuiUtility;
import com.drewhannay.chesscrafter.utility.Messages;
import com.google.common.collect.ImmutableSet;
import javafx.util.Pair;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class PieceCrafterDetailPanel extends ChessPanel {

    private final JTextField mPieceNameField;

    private final DefaultListModel<Pair<Direction, Integer>> mMovementListModel;
    private final DefaultListModel<Pair<Direction, Integer>> mCapturingListModel;
    private final DefaultListModel<TwoHopMovement> mTwoHopListModel;

    private final GlassPane mGlassPane;

    public PieceCrafterDetailPanel(GlassPane glassPane) {
        mPieceNameField = new JTextField(15);

        mMovementListModel = new DefaultListModel<>();
        mCapturingListModel = new DefaultListModel<>();
        mTwoHopListModel = new DefaultListModel<>();

        mGlassPane = glassPane;

        initComponents();
    }

    public void newPieceType() {
        clearPieceData();
    }

    public void loadPieceType(PieceType pieceType) {
        clearPieceData();

        mPieceNameField.setText(pieceType.getName());
        pieceType.getMovements().forEach((direction, value) -> mMovementListModel.addElement(new Pair<>(direction, value)));
        pieceType.getCapturingMovements().forEach((direction, value) -> mCapturingListModel.addElement(new Pair<>(direction, value)));
        pieceType.getTwoHopMovements().forEach(mTwoHopListModel::addElement);
    }

    private void clearPieceData() {
        mPieceNameField.setText("");
        mMovementListModel.clear();
        mCapturingListModel.clear();
        mTwoHopListModel.clear();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        JPanel namePanel = new JPanel();
        namePanel.setOpaque(false);

        namePanel.add(GuiUtility.createJLabel(Messages.getString("PieceCrafterDetailPanel.pieceName")));
        namePanel.add(mPieceNameField);

        JButton imageButton = new JButton(Messages.getString("PieceCrafterDetailPanel.image"));
        imageButton.setToolTipText(Messages.getString("PieceCrafterDetailPanel.pieceIcon"));
        imageButton.setPreferredSize(new Dimension(75, 75));
        namePanel.add(imageButton);

        JPanel allMovementsPanel = new JPanel();
        allMovementsPanel.setOpaque(false);
        allMovementsPanel.setLayout(new GridBagLayout());

        JList<Pair<Direction, Integer>> movementList = new JList<>(mMovementListModel);

        JScrollPane cardinalScrollPane = new JScrollPane(movementList);
        cardinalScrollPane.setPreferredSize(new Dimension(150, 140));
        cardinalScrollPane.setBorder(BorderFactory.createTitledBorder(Messages.getString("PieceCrafterDetailPanel.movements")));
        cardinalScrollPane.getVerticalScrollBar().setUnitIncrement(25);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridwidth = 3;
        constraints.insets = new Insets(0, 10, 10, 10);
        allMovementsPanel.add(cardinalScrollPane, constraints);

        JButton addMovement = new JButton("+");
        addMovement.addActionListener(event -> createMovementPopup(movementList.getSelectedValue()));
        constraints = new GridBagConstraints();
        constraints.gridy = 1;
        constraints.insets = new Insets(0, 10, 0, 0);
        allMovementsPanel.add(addMovement, constraints);

        JButton removeMovement = new JButton("-");
        removeMovement.addActionListener(event -> mMovementListModel.remove(movementList.getSelectedIndex()));
        constraints.gridx = 1;
        allMovementsPanel.add(removeMovement, constraints);

        JButton editMovement = new JButton("Edit");
        editMovement.addActionListener(event -> createMovementPopup(movementList.getSelectedValue()));
        constraints.gridx = 2;
        allMovementsPanel.add(editMovement, constraints);

        JList<Pair<Direction, Integer>> capturingList = new JList<>(mMovementListModel);

        JScrollPane captureScrollPane = new JScrollPane(capturingList);
        captureScrollPane.setPreferredSize(new Dimension(150, 140));
        captureScrollPane.setBorder(BorderFactory.createTitledBorder(Messages.getString("PieceCrafterDetailPanel.capturing")));
        captureScrollPane.getVerticalScrollBar().setUnitIncrement(25);

        constraints = new GridBagConstraints();
        constraints.gridwidth = 3;
        constraints.gridx = 3;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.insets = new Insets(0, 10, 10, 10);
        allMovementsPanel.add(captureScrollPane, constraints);

        JButton addCapture = new JButton("+");
        addCapture.addActionListener(event -> createMovementPopup(capturingList.getSelectedValue()));
        constraints = new GridBagConstraints();
        constraints.gridy = 1;
        constraints.gridx = 3;

        constraints.insets = new Insets(0, 10, 0, 0);
        allMovementsPanel.add(addCapture, constraints);

        JButton removeCapture = new JButton("-");
        removeCapture.addActionListener(event -> mCapturingListModel.remove(capturingList.getSelectedIndex()));
        constraints.gridx = 4;
        allMovementsPanel.add(removeCapture, constraints);

        JButton editCapture = new JButton("Edit");
        editCapture.addActionListener(event -> createMovementPopup(capturingList.getSelectedValue()));
        constraints.gridx = 5;
        allMovementsPanel.add(editCapture, constraints);

        JList<TwoHopMovement> twoHopList = new JList<>(mTwoHopListModel);

        JScrollPane twoHopScrollPane = new JScrollPane(twoHopList);
        twoHopScrollPane.setPreferredSize(new Dimension(150, 140));
        twoHopScrollPane.setBorder(BorderFactory.createTitledBorder(Messages.getString("PieceCrafterDetailPanel.twoHop")));
        twoHopScrollPane.getVerticalScrollBar().setUnitIncrement(25);

        constraints = new GridBagConstraints();
        constraints.gridwidth = 3;
        constraints.gridx = 6;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.insets = new Insets(0, 10, 10, 10);
        allMovementsPanel.add(twoHopScrollPane, constraints);

        JButton addTwoHop = new JButton("+");
        addTwoHop.addActionListener(event -> createMovementPopup(twoHopList.getSelectedValue()));
        constraints = new GridBagConstraints();
        constraints.gridy = 1;
        constraints.gridx = 6;

        constraints.insets = new Insets(0, 10, 0, 0);
        allMovementsPanel.add(addTwoHop, constraints);

        JButton removeTwoHop = new JButton("-");
        removeTwoHop.addActionListener(event -> mCapturingListModel.remove(twoHopList.getSelectedIndex()));
        constraints.gridx = 7;
        allMovementsPanel.add(removeTwoHop, constraints);

        JButton editTwoHop = new JButton("Edit");
        editTwoHop.addActionListener(event -> createMovementPopup(twoHopList.getSelectedValue()));
        constraints.gridx = 8;
        allMovementsPanel.add(editTwoHop, constraints);

        JPanel boardAndSave = new JPanel();
        boardAndSave.setOpaque(false);
        boardAndSave.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        BoardPanel board = new BoardPanel(BoardSize.CLASSIC_SIZE, new SquareConfig(new DropManager(() -> {
        }, pair -> {
        }), mGlassPane), bc -> ImmutableSet.of());
        boardAndSave.add(board, gbc);

        JButton save = new JButton(Messages.getString("PieceMakerPanel.saveAndReturn"));
        save.addActionListener(event -> {
            //TODO save piece here
        });
        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.LAST_LINE_END;
        gbc.insets = new Insets(25, 0, 25, 25);
        boardAndSave.add(save, gbc);

        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.7;
        add(namePanel, gbc);

        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.ipady = 0;
        add(allMovementsPanel, gbc);

        gbc.gridy = 2;
        add(boardAndSave, gbc);
    }

    private void createMovementPopup(Pair<Direction, Integer> movement) {
        // TODO:
    }

    private void createMovementPopup(TwoHopMovement movement) {
        // TODO:
    }
}
