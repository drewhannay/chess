package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.dragNdrop.DropManager;
import com.drewhannay.chesscrafter.dragNdrop.GlassPane;
import com.drewhannay.chesscrafter.dragNdrop.SquareConfig;
import com.drewhannay.chesscrafter.label.SquareJLabel;
import com.drewhannay.chesscrafter.logic.PieceTypeManager;
import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.BoardSize;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.models.PieceType;
import com.drewhannay.chesscrafter.panel.TeamCreationPanel.TeamInfo;
import com.drewhannay.chesscrafter.utility.Messages;
import com.drewhannay.chesscrafter.utility.Pair;
import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;

import javax.swing.JButton;
import javax.swing.JComponent;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GameCrafterPanel extends ChessPanel {

    private final Board mBoard;
    private final BoardPanel mBoardPanel;
    private final TeamCreationPanel mTeamPanel;

    public GameCrafterPanel(@NotNull GlassPane glassPane) {
        DropManager boardDropManager = new DropManager(this::boardRefresh, getBoardDragDropConsumer());
        DropManager teamDropManager = new DropManager(this::boardRefresh, getTeamDragDropConsumer());

        mBoard = new Board(BoardSize.CLASSIC_SIZE);
        mBoardPanel = new BoardPanel(mBoard.getBoardSize(), new SquareConfig(boardDropManager, glassPane), getLegalMovesCallback());

        SquareConfig squareConfig = new SquareConfig(teamDropManager, glassPane, mBoardPanel::getAllSquares);
        squareConfig.setHideIcon(false);
        mTeamPanel = new TeamCreationPanel(squareConfig, this::boardRefresh);

        initComponents();
    }

    public void onPieceTypeSelected(PieceType pieceType) {
        mTeamPanel.setPieceType(pieceType);
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc;

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        add(mTeamPanel, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.8;
        gbc.fill = GridBagConstraints.BOTH;
        add(mBoardPanel, gbc);

        JButton save = new JButton(Messages.getString("GameCrafterPanel.save"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(save, gbc);
    }

    private void boardRefresh() {
        List<TeamInfo> teamInfos = mTeamPanel.getTeamInfos();
        mBoardPanel.getAllSquares().forEach(square -> {
            Piece piece = mBoard.getPiece(square.getCoordinates());
            Integer teamId = piece != null ? piece.getTeamId() : null;
            if (piece != null && !teamInfos.stream().anyMatch(teamInfo -> teamInfo.teamId == teamId)) {
                mBoard.removePiece(square.getCoordinates());
                piece = null;
            }

            square.setPiece(piece, piece != null ? teamInfos.stream()
                    .filter(teamInfo -> teamInfo.teamId == teamId)
                    .findFirst()
                    .orElseThrow(IllegalStateException::new).color : null);
        });
    }

    private void setPiece(Piece piece, BoardCoordinate coordinate) {
        mBoard.addPiece(piece, coordinate);
        boardRefresh();
    }

    private Consumer<Pair<JComponent, JComponent>> getTeamDragDropConsumer() {
        return pair -> {
            Piece piece = ((SquareJLabel) pair.first).getPiece();
            if (piece != null) {
                BoardCoordinate destination = ((SquareJLabel) pair.second).getCoordinates();
                setPiece(new Piece(piece.getTeamId(), PieceTypeManager.INSTANCE.getPieceTypeById(piece.getInternalId())), destination);
            }
        };
    }

    private Consumer<Pair<JComponent, JComponent>> getBoardDragDropConsumer() {
        return pair -> {
            BoardCoordinate origin = ((SquareJLabel) pair.first).getCoordinates();
            BoardCoordinate destination = ((SquareJLabel) pair.second).getCoordinates();
            mBoard.movePiece(origin, destination);
            mBoard.getPiece(destination).decrementMoveCount();
            boardRefresh();
        };
    }

    private Function<BoardCoordinate, Set<BoardCoordinate>> getLegalMovesCallback() {
        return bc -> {
            if (!mBoard.doesPieceExistAt(bc)) {
                return ImmutableSet.of();
            }
            return mBoardPanel.getAllSquares().stream()
                    .map(SquareJLabel::getCoordinates)
                    .collect(Collectors.toSet());
        };
    }
}
