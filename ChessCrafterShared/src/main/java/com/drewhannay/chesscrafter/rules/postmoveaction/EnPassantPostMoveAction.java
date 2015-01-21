package com.drewhannay.chesscrafter.rules.postmoveaction;

import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.Move;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.models.PieceType;
import com.drewhannay.chesscrafter.models.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class EnPassantPostMoveAction implements PostMoveAction {

    @Override
    public void perform(@NotNull Board board, @NotNull Team team, @NotNull Move move, @Nullable Piece capturedPiece) {
        Piece piece = board.getPiece(move.destination);

        // must have moved a pawn
        if (!isPawn(piece)) {
            return;
        }

        // must not have captured a piece
        if (capturedPiece != null) {
            return;
        }

        // must have moved diagonally
        if (!move.origin.isOnSameDiagonalPathAs(move.destination)) {
            return;
        }

        BoardCoordinate enPassantCoordinate = BoardCoordinate.at(move.destination.x, move.origin.y);
        Piece capturedPawn = board.getPiece(enPassantCoordinate);
        board.removePiece(enPassantCoordinate);

        team.capturePiece(move, capturedPawn);
    }

    @Override
    public void undo(@NotNull Board board, @NotNull Team team, @NotNull Move lastMove, @Nullable Move opponentsLastMove) {
        // must have at least two previous moves
        if (opponentsLastMove == null) {
            return;
        }

        // must not have moved to same space as opponent's last move
        if (lastMove.destination.equals(opponentsLastMove.destination)) {
            return;
        }

        // must have moved diagonally
        if (!lastMove.origin.isOnSameDiagonalPathAs(lastMove.destination)) {
            return;
        }

        Piece piece = board.getPiece(lastMove.destination);

        // must have moved a pawn
        if (!isPawn(piece)) {
            return;
        }

        Piece capturedPiece = team.undoCapturePiece(lastMove);

        // must have captured a piece
        if (capturedPiece == null) {
            return;
        }

        board.addPiece(capturedPiece, opponentsLastMove.destination);
    }

    private boolean isPawn(Piece piece) {
        return piece.getName().equals(PieceType.getNorthFacingPawnPieceType().getName())
                || piece.getName().equals(PieceType.getSouthFacingPawnPieceType().getName());
    }
}
