package com.drewhannay.chesscrafter.rules.movefilter;

import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public final class MustCaptureMoveFilter extends MoveFilter {
    @Override
    public Set<BoardCoordinate> filterMoves(@NotNull Board board, @NotNull BoardCoordinate start, @NotNull Set<BoardCoordinate> moves) {
        Preconditions.checkArgument(board.doesPieceExistAt(start));

        Set<BoardCoordinate> filteredMoves = new HashSet<>(moves.size());
        for (BoardCoordinate move : moves) {
            if (board.doesPieceExistAt(move)) {
                filteredMoves.add(move);
            }
        }
        return filteredMoves;
    }
}
