package com.drewhannay.chesscrafter.rules.conditionalmovegenerator;

import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.Move;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.models.PieceType;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;
import java.util.Stack;

public final class EnPassantMoveGenerator implements ConditionalMoveGenerator {

    @NotNull
    @Override
    public Set<BoardCoordinate> generateMoves(@NotNull Board board, @NotNull BoardCoordinate start,
                                              @NotNull Stack<Move> history) {
        // must not be the first turn
        if (history.isEmpty()) {
            return Collections.emptySet();
        }

        Move lastMove = history.peek();
        Piece movingPiece = board.getPiece(start);

        // must be moving a pawn
        if (!isPawn(movingPiece)) {
            return Collections.emptySet();
        }

        // must have moved a pawn on the last turn
        Piece lastMovedPiece = board.getPiece(lastMove.destination);
        if (lastMovedPiece == null || !isPawn(lastMovedPiece)) {
            return Collections.emptySet();
        }

        // must have moved two spaces
        if (Math.abs(lastMove.destination.y - lastMove.origin.y) != 2) {
            return Collections.emptySet();
        }

        // must be on the same row as the opposing pawn
        if (!lastMove.destination.isOnSameHorizontalPathAs(start)) {
            return Collections.emptySet();
        }

        // must be directly next to the opposing pawn
        if (Math.abs(lastMove.destination.x - start.x) != 1) {
            return Collections.emptySet();
        }

        int xDirection = lastMove.destination.x - start.x;
        int yDirection = (int) Math.signum(lastMove.origin.y - lastMove.destination.y);

        return Sets.newHashSet(BoardCoordinate.at(start.x + xDirection, start.y + yDirection));
    }

    private boolean isPawn(@NotNull Piece piece) {
        return piece.getName().equals(PieceType.getNorthFacingPawnPieceType().getName())
                || piece.getName().equals(PieceType.getSouthFacingPawnPieceType().getName());
    }
}
