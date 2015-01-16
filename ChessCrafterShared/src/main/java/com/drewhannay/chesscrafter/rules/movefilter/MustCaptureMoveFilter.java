package com.drewhannay.chesscrafter.rules.movefilter;

import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.ChessCoordinate;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public final class MustCaptureMoveFilter implements MoveFilter {
    @Override
    public Set<ChessCoordinate> filterMoves(@NotNull Board board, @NotNull ChessCoordinate start, @NotNull Set<ChessCoordinate> moves) {
        Preconditions.checkArgument(board.doesPieceExistAt(start));

        Set<ChessCoordinate> filteredMoves = new HashSet<>(moves.size());
        for (ChessCoordinate move : moves) {
            if (board.doesPieceExistAt(move)) {
                filteredMoves.add(move);
            }
        }
        return filteredMoves;
    }
}
