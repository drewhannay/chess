package com.drewhannay.chesscrafter.rules.postmoveaction;

import com.drewhannay.chesscrafter.logic.PieceTypeManager;
import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.Move;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.models.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CastlingPostMoveAction extends PostMoveAction {

    public static final String NAME = "CastlingPostMoveAction";

    @Override
    public void perform(@NotNull Board board, @NotNull Team team, @NotNull Move move, @Nullable Piece capturedPiece) {
        Piece piece = board.getPiece(move.destination);

        // must have moved a piece
        if (piece == null) {
            return;
        }

        // must have moved a king
        if (!isKing(piece)) {
            return;
        }

        int distance = move.origin.x - move.destination.x;

        // must have moved the king two spaces
        if (Math.abs(distance) != 2) {
            return;
        }

        if (distance == -2) {
            board.movePiece(BoardCoordinate.at(board.getBoardSize().width, move.destination.y), BoardCoordinate.at(move.destination.x - 1, move.destination.y));
        } else if (distance == 2) {
            board.movePiece(BoardCoordinate.at(1, move.destination.y), BoardCoordinate.at(move.destination.x + 1, move.destination.y));
        }
    }

    @Override
    public void undo(@NotNull Board board, @NotNull Team team, @NotNull Move lastMove, @Nullable Move opponentsLastMove) {
        Piece piece = board.getPiece(lastMove.destination);

        // must have moved a piece
        if (piece == null) {
            return;
        }

        // must have moved the king
        if (!isKing(piece)) {
            return;
        }

        int distance = lastMove.origin.x - lastMove.destination.x;

        // must have moved the king two spaces
        if (Math.abs(distance) != 2) {
            return;
        }

        if (distance == -2) {
            board.undoMovePiece(BoardCoordinate.at(board.getBoardSize().width, lastMove.destination.y), BoardCoordinate.at(lastMove.destination.x - 1, lastMove.destination.y), null);
        } else if (distance == 2) {
            board.undoMovePiece(BoardCoordinate.at(1, lastMove.destination.y), BoardCoordinate.at(lastMove.destination.x + 1, lastMove.destination.y), null);
        }
    }

    private boolean isKing(Piece piece) {
        return piece.getInternalId().equals(PieceTypeManager.getKingPieceType().getInternalId());
    }
}
