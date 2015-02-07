package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.dragNdrop.DropManager;
import com.drewhannay.chesscrafter.dragNdrop.GlassPane;
import com.drewhannay.chesscrafter.dragNdrop.SquareConfig;
import com.drewhannay.chesscrafter.label.SquareJLabel;
import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.BoardSize;
import com.drewhannay.chesscrafter.models.CardinalMovement;
import com.drewhannay.chesscrafter.models.Direction;
import com.drewhannay.chesscrafter.models.Movement;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.models.PieceType;
import com.drewhannay.chesscrafter.models.TwoHopMovement;
import com.drewhannay.chesscrafter.utility.GuiUtility;
import com.drewhannay.chesscrafter.utility.Messages;
import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PieceCrafterDetailPanel extends ChessPanel {

    private static class ListData<T extends Movement> {
        public final String title;
        public final DefaultListModel<T> model;
        public final JList<T> list;
        public final AddRemoveEditPanel buttons;
        public final Consumer<Boolean> showInputDialog;
        public final Supplier<Boolean> isFull;

        private ListData(String title, DefaultListModel<T> model, JList<T> list, AddRemoveEditPanel buttons,
                         BiConsumer<ListData<T>, Boolean> showInputDialog, Supplier<Boolean> isFull) {
            this.title = title;
            this.model = model;
            this.list = list;
            this.buttons = buttons;
            this.showInputDialog = isEdit -> showInputDialog.accept(this, isEdit);
            this.isFull = isFull;
        }
    }

    private final JTextField mPieceNameField;

    private final ListData<CardinalMovement> mMovementData;
    private final ListData<CardinalMovement> mCapturingData;
    private final ListData<TwoHopMovement> mTwoHopData;

    private final BoardPanel mBoardPanel;

    private Board mBoard;

    public PieceCrafterDetailPanel(GlassPane glassPane) {
        mPieceNameField = new JTextField(15);

        DefaultListModel<CardinalMovement> movementListModel = new DefaultListModel<>();
        JList<CardinalMovement> movementList = new JList<>(movementListModel);
        movementList.setCellRenderer(createMovementRenderer());
        movementList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        AddRemoveEditPanel mMovementButtons = new AddRemoveEditPanel();

        DefaultListModel<CardinalMovement> capturingListModel = new DefaultListModel<>();
        JList<CardinalMovement> capturingList = new JList<>(capturingListModel);
        capturingList.setCellRenderer(createMovementRenderer());
        capturingList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        AddRemoveEditPanel capturingButtons = new AddRemoveEditPanel();

        DefaultListModel<TwoHopMovement> twoHopListModel = new DefaultListModel<>();
        JList<TwoHopMovement> twoHopList = new JList<>(twoHopListModel);
        twoHopList.setCellRenderer(createTwoHopRenderer());
        twoHopList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        AddRemoveEditPanel twoHopButtons = new AddRemoveEditPanel();

        mMovementData = new ListData<>(Messages.getString("PieceCrafterDetailPanel.movements"),
                movementListModel, movementList, mMovementButtons, this::showCardinalInputDialog,
                () -> getUnusedDirections(movementListModel).isEmpty());
        mCapturingData = new ListData<>(Messages.getString("PieceCrafterDetailPanel.capturing"),
                capturingListModel, capturingList, capturingButtons, this::showCardinalInputDialog,
                () -> getUnusedDirections(capturingListModel).isEmpty());
        mTwoHopData = new ListData<>(Messages.getString("PieceCrafterDetailPanel.twoHop"),
                twoHopListModel, twoHopList, twoHopButtons, this::showTwoHopInputDialog, () -> false);

        mBoard = new Board(BoardSize.CLASSIC_SIZE);
        mBoardPanel = new BoardPanel(BoardSize.CLASSIC_SIZE,
                new SquareConfig(new DropManager(this::refreshBoard, this::movePiece), glassPane), this::getMovesFrom);

        initComponents();
        newPieceType();
    }

    public void newPieceType() {
        clearPieceData();
    }

    public void loadPieceType(PieceType pieceType) {
        clearPieceData();

        mPieceNameField.setText(pieceType.getName());
        pieceType.getMovements().forEach(mMovementData.model::addElement);
        pieceType.getCapturingMovements().forEach(mCapturingData.model::addElement);
        pieceType.getTwoHopMovements().forEach(mTwoHopData.model::addElement);

        mBoard.addPiece(new Piece(Piece.TEAM_ONE, pieceType), BoardCoordinate.at(4, 4));
        dataStream().forEach(this::refreshButtonState);
        refreshBoard();
    }

    private void refreshBoard() {
        mBoardPanel.updatePieceLocations(mBoard, i -> Color.WHITE);
    }

    private Stream<ListData<?>> dataStream() {
        return Stream.of(mMovementData, mCapturingData, mTwoHopData);
    }

    private void clearPieceData() {
        mPieceNameField.setText("");
        dataStream().forEach(data -> data.model.clear());
        dataStream().forEach(this::refreshButtonState);

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

        dataStream().forEachOrdered(data -> {
            createScrollPane(allMovementsPanel, data.list, data.buttons, data.title);
            data.buttons.addAddActionListener(e -> data.showInputDialog.accept(false));
            data.buttons.addEditActionListener(e -> data.showInputDialog.accept(true));
            data.buttons.addRemoveActionListener(e -> data.model.remove(data.list.getSelectedIndex()));
            data.list.addListSelectionListener(e -> refreshButtonState(data));
        });

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
        gbc.insets = new Insets(10, 0, 25, 25);
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

    private void refreshButtonState(@NotNull ListData<?> data) {
        data.buttons.mAdd.setEnabled(!data.isFull.get());
        data.buttons.mRemove.setEnabled(data.list.getSelectedIndex() >= 0);
        data.buttons.mEdit.setEnabled(data.list.getSelectedIndex() >= 0);
    }

    private void showCardinalInputDialog(@NotNull ListData<CardinalMovement> data, boolean isEdit) {
        List<Direction> unusedDirections = getUnusedDirections(data.model);

        JDialog movementDialog = new JDialog();
        movementDialog.setSize(new Dimension(300, 200));
        movementDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        movementDialog.setLocationRelativeTo(this);

        ChessPanel movementPopupPanel = new ChessPanel();
        movementPopupPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(5, 5, 5, 5);
        movementPopupPanel.add(GuiUtility.createJLabel(Messages.getString("PieceCrafterDetailPanel.direction")), gbc);

        JComboBox<Direction> directions;
        if (isEdit) {
            directions = new JComboBox<>(Direction.values());
            directions.setSelectedItem(data.list.getSelectedValue().direction);
            directions.setEnabled(false);
        } else {
            Direction[] directionArray = new Direction[unusedDirections.size()];
            unusedDirections.toArray(directionArray);
            directions = new JComboBox<>(directionArray);
        }

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        movementPopupPanel.add(directions, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        movementPopupPanel.add(GuiUtility.createJLabel(Messages.getString("PieceCrafterDetailPanel.distance")), gbc);

        JTextField distance = new JTextField(4);
        if (isEdit) {
            int value = data.list.getSelectedValue().distance;
            if (value == Integer.MAX_VALUE) {
                distance.setText(Messages.getString("PieceCrafterDetailPanel.unlimited"));
            } else {
                distance.setText(String.valueOf(value));
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
            if (isEdit) {
                int selectedIndex = data.list.getSelectedIndex();
                data.model.remove(selectedIndex);
                data.model.add(selectedIndex, CardinalMovement.with((Direction) directions.getSelectedItem(), movementDistance));
            } else {
                data.model.addElement(CardinalMovement.with((Direction) directions.getSelectedItem(), movementDistance));
            }
            refreshButtonState(data);
            movementDialog.dispose();
        });
        gbc.gridy = 2;
        gbc.weightx = 0.5;
        movementPopupPanel.add(saveMovement, gbc);

        movementDialog.add(movementPopupPanel);
        movementDialog.setVisible(true);
    }

    private void showTwoHopInputDialog(@NotNull ListData<TwoHopMovement> data, boolean isEdit) {
        JDialog twoHopDialog = new JDialog();
        twoHopDialog.setSize(new Dimension(300, 200));
        twoHopDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        twoHopDialog.setLocationRelativeTo(this);

        ChessPanel twoHopPopupPanel = new ChessPanel();
        twoHopPopupPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(5, 5, 5, 5);
        twoHopPopupPanel.add(GuiUtility.createJLabel(Messages.getString("PieceCrafterDetailPanel.xDirection")), gbc);

        JTextField xDirection = new JTextField(4);
        xDirection.setText("1");

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        twoHopPopupPanel.add(xDirection, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        twoHopPopupPanel.add(GuiUtility.createJLabel(Messages.getString("PieceCrafterDetailPanel.yDirection")), gbc);

        JTextField yDirection = new JTextField(4);
        yDirection.setText("2");

        if (isEdit) {
            TwoHopMovement movement = data.list.getSelectedValue();
            xDirection.setText(String.valueOf(movement.x));
            yDirection.setText(String.valueOf(movement.y));
        }

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        twoHopPopupPanel.add(yDirection, gbc);

        JButton saveMovement = new JButton(Messages.getString("PieceCrafterDetailPanel.saveAndClose"));
        saveMovement.addActionListener(event -> {
            int xMovementDistance = Integer.parseInt(xDirection.getText());
            int yMovementDistance = Integer.parseInt(yDirection.getText());
            if (isEdit) {
                int selectedIndex = data.list.getSelectedIndex();
                data.model.remove(selectedIndex);
                data.model.add(selectedIndex, TwoHopMovement.with(xMovementDistance, yMovementDistance));
            } else {
                data.model.addElement(TwoHopMovement.with(xMovementDistance, yMovementDistance));
            }
            twoHopDialog.dispose();
        });
        gbc.gridy = 2;
        gbc.weightx = 0.5;
        twoHopPopupPanel.add(saveMovement, gbc);

        twoHopDialog.add(twoHopPopupPanel);
        twoHopDialog.setVisible(true);
    }

    private void createScrollPane(@NotNull JPanel movementPanel, @NotNull JList<?> list,
                                  @NotNull AddRemoveEditPanel buttons, @NotNull String title) {
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(150, 140));
        scrollPane.setBorder(BorderFactory.createTitledBorder(title));
        scrollPane.getVerticalScrollBar().setUnitIncrement(25);

        int c = movementPanel.getComponentCount() / 2;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = c;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 0, 10);
        movementPanel.add(scrollPane, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(0, 0, 0, 0);
        movementPanel.add(buttons, gbc);
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

        Set<CardinalMovement> movements = new HashSet<>(mMovementData.model.size());
        IntStream.range(0, mMovementData.model.size()).forEach(i -> movements.add(mMovementData.model.get(i)));
        Set<CardinalMovement> capturingMovements = new HashSet<>(mCapturingData.model.size());
        IntStream.range(0, mCapturingData.model.size()).forEach(i -> capturingMovements.add(mCapturingData.model.get(i)));
        Set<TwoHopMovement> twoHopMovements = new HashSet<>(mTwoHopData.model.size());
        IntStream.range(0, mTwoHopData.model.size()).forEach(i -> twoHopMovements.add(mTwoHopData.model.get(i)));

        PieceType pieceType = new PieceType(mPieceNameField.getText(), mPieceNameField.getText(),
                movements, capturingMovements, twoHopMovements);
        return pieceType.getMovesFrom(coordinate, mBoard.getBoardSize(), 0);
    }

    private ListCellRenderer<CardinalMovement> createMovementRenderer() {
        return (list, value, index, isSelected, cellHasFocus) -> {
            String direction = String.valueOf(value.direction);
            String distance = value.distance == Integer.MAX_VALUE ? Messages.getString("PieceCrafterDetailPanel.unlimited")
                    : String.valueOf(value.distance);
            JLabel label = new JLabel(Messages.getString("PieceCrafterDetailPanel.movementItem", direction, distance));
            label.setOpaque(true);
            label.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            label.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            return label;
        };
    }

    private ListCellRenderer<TwoHopMovement> createTwoHopRenderer() {
        return (list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(Messages.getString("PieceCrafterDetailPanel.twoHopItem", value.x, value.y));
            label.setOpaque(true);
            label.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            label.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            return label;
        };
    }

    private static List<Direction> getUnusedDirections(@NotNull ListModel<CardinalMovement> model) {
        return Stream.of(Direction.values())
                .filter(d -> IntStream.range(0, model.getSize()).allMatch(i -> model.getElementAt(i).direction != d))
                .collect(Collectors.toList());
    }
}
