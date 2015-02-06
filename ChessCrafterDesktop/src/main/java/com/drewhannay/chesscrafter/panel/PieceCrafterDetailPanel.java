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

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

        AddRemoveEditPanel movementPanel = new AddRemoveEditPanel(mMovementListModel, mMovementRenderer,
                Messages.getString("PieceCrafterDetailPanel.movements"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        allMovementsPanel.add(movementPanel, gbc);

        AddRemoveEditPanel capturePanel = new AddRemoveEditPanel(mCapturingListModel, mMovementRenderer,
                Messages.getString("PieceCrafterDetailPanel.capturing"));

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        allMovementsPanel.add(capturePanel, gbc);

        AddRemoveEditPanel twoHopPanel = new AddRemoveEditPanel(mTwoHopListModel, mTwoHopRenderer,
                Messages.getString("PieceCrafterDetailPanel.twoHop"));

        gbc.gridx = 2;
        allMovementsPanel.add(twoHopPanel, gbc);

        JPanel boardAndSave = new JPanel();
        boardAndSave.setOpaque(false);
        boardAndSave.setLayout(new GridBagLayout());

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

    public static void createMovementPopup(DefaultListModel<Pair<Direction, Integer>> model,
                                        Pair<Direction, Integer> movement, int selectedIndex) {
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

    public static List getUnusedDirections(DefaultListModel<javafx.util.Pair<Direction, Integer>> model) {
        List<Direction> unusedDirections =
                Stream.of(Direction.values())
                        .filter(d -> IntStream.range(0, model.size()).allMatch(i -> model.get(i).getKey() != d))
                        .collect(Collectors.toList());
        return unusedDirections;
    }
}
