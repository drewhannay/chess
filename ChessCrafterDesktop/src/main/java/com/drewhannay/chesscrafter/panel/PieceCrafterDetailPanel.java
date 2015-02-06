package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.dragNdrop.DropManager;
import com.drewhannay.chesscrafter.dragNdrop.GlassPane;
import com.drewhannay.chesscrafter.dragNdrop.SquareConfig;
import com.drewhannay.chesscrafter.label.SquareJLabel;
import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.BoardSize;
import com.drewhannay.chesscrafter.models.Direction;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.models.PieceType;
import com.drewhannay.chesscrafter.models.TwoHopMovement;
import com.drewhannay.chesscrafter.utility.GuiUtility;
import com.drewhannay.chesscrafter.utility.Messages;
import com.google.common.collect.ImmutableSet;
import javafx.util.Pair;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class PieceCrafterDetailPanel extends ChessPanel {

    private final JTextField mPieceNameField;

    private final DefaultListModel<Pair<Direction, Integer>> mMovementListModel;
    private final DefaultListModel<Pair<Direction, Integer>> mCapturingListModel;
    private final DefaultListModel<TwoHopMovement> mTwoHopListModel;

    private final BoardPanel mBoardPanel;

    private Board mBoard;

    public PieceCrafterDetailPanel(GlassPane glassPane) {
        mPieceNameField = new JTextField(15);

        mMovementListModel = new DefaultListModel<>();
        mCapturingListModel = new DefaultListModel<>();
        mTwoHopListModel = new DefaultListModel<>();

        mBoard = new Board(BoardSize.CLASSIC_SIZE);
        mBoardPanel = new BoardPanel(BoardSize.CLASSIC_SIZE,
                new SquareConfig(new DropManager(this::refreshBoard, this::movePiece), glassPane), this::getMovesFrom);

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

        mBoard.addPiece(new Piece(Piece.TEAM_ONE, pieceType), BoardCoordinate.at(4, 4));
        refreshBoard();
    }

    private void refreshBoard() {
        mBoardPanel.updatePieceLocations(mBoard, i -> Color.WHITE);
    }

    private void clearPieceData() {
        mPieceNameField.setText("");
        mMovementListModel.clear();
        mCapturingListModel.clear();
        mTwoHopListModel.clear();

        mBoard = new Board(BoardSize.CLASSIC_SIZE);
        refreshBoard();
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
        movementList.setCellRenderer(mMovementRenderer);

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

        JList<Pair<Direction, Integer>> capturingList = new JList<>(mCapturingListModel);
        capturingList.setCellRenderer(mMovementRenderer);

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
        twoHopList.setCellRenderer(mTwoHopRenderer);

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
        removeTwoHop.addActionListener(event -> mTwoHopListModel.remove(twoHopList.getSelectedIndex()));
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
        boardAndSave.add(mBoardPanel, gbc);

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

    private void movePiece(com.drewhannay.chesscrafter.utility.Pair<JComponent, JComponent> pair) {
        SquareJLabel origin = (SquareJLabel) pair.first;
        SquareJLabel destination = (SquareJLabel) pair.second;
        Piece piece = mBoard.getPiece(origin.getCoordinates());
        if (piece != null) {
            mBoard.removePiece(origin.getCoordinates());
            mBoard.addPiece(piece, destination.getCoordinates());
        }
        refreshBoard();
    }

    private Set<BoardCoordinate> getMovesFrom(BoardCoordinate coordinate) {
        if (!mBoard.doesPieceExistAt(coordinate)) {
            return ImmutableSet.of();
        }

        Map<Direction, Integer> movements = new HashMap<>(mMovementListModel.size());
        IntStream.range(0, mMovementListModel.size()).forEach(i -> {
            Pair<Direction, Integer> pair = mMovementListModel.get(i);
            movements.put(pair.getKey(), pair.getValue());
        });
        Map<Direction, Integer> capturingMovements = new HashMap<>(mCapturingListModel.size());
        IntStream.range(0, mCapturingListModel.size()).forEach(i -> {
            Pair<Direction, Integer> pair = mCapturingListModel.get(i);
            capturingMovements.put(pair.getKey(), pair.getValue());
        });
        Set<TwoHopMovement> twoHopMovements = new HashSet<>(mTwoHopListModel.size());
        IntStream.range(0, mTwoHopListModel.size()).forEach(i -> twoHopMovements.add(mTwoHopListModel.get(i)));

        PieceType pieceType = new PieceType(mPieceNameField.getText(), mPieceNameField.getText(),
                movements, capturingMovements, twoHopMovements);
        return pieceType.getMovesFrom(coordinate, mBoard.getBoardSize(), 0);
    }

    private final ListCellRenderer<Pair<Direction, Integer>> mMovementRenderer = (list, value, index, isSelected, cellHasFocus) -> {
        String direction = String.valueOf(value.getKey());
        String distance = value.getValue() == Integer.MAX_VALUE ? Messages.getString("PieceCrafterDetailPanel.unlimited")
                : String.valueOf(value.getValue());
        JLabel label = new JLabel(Messages.getString("PieceCrafterDetailPanel.movementItem", direction, distance));
        label.setOpaque(true);
        label.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
        label.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
        return label;
    };

    private final ListCellRenderer<TwoHopMovement> mTwoHopRenderer = (list, value, index, isSelected, cellHasFocus) -> {
        JLabel label = new JLabel(Messages.getString("PieceCrafterDetailPanel.twoHopItem", value.x, value.y));
        label.setOpaque(true);
        label.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
        label.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
        return label;
    };
}
