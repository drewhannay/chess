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
import com.drewhannay.chesscrafter.utility.PieceCrafterUtility;
import com.google.common.collect.ImmutableSet;
import javafx.util.Pair;
import sun.plugin2.message.Message;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
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

        PieceCrafterUtility.createMovementButtonPanel(allMovementsPanel, mMovementListModel, mMovementRenderer, 0,
                Messages.getString("PieceCrafterDetailPanel.movements"));

        PieceCrafterUtility.createMovementButtonPanel(allMovementsPanel, mCapturingListModel, mMovementRenderer, 3,
                Messages.getString("PieceCrafterDetailPanel.capturing"));

        PieceCrafterUtility.createTwoHopButtonPanel(allMovementsPanel, mTwoHopListModel, mTwoHopRenderer);

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
