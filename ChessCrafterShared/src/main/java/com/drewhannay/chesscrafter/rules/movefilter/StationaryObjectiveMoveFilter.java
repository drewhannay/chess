package com.drewhannay.chesscrafter.rules.movefilter;

import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.PieceType;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public final class StationaryObjectiveMoveFilter extends MoveFilter {

    private final PieceType mObjectivePieceType;

    public StationaryObjectiveMoveFilter(@NotNull PieceType objectivePieceType) {
        mObjectivePieceType = objectivePieceType;
    }

    @Override
    public Set<BoardCoordinate> filterMoves(@NotNull Board board, @NotNull BoardCoordinate start, @NotNull Set<BoardCoordinate> moves) {
        Preconditions.checkArgument(board.doesPieceExistAt(start));

        if (board.getPiece(start).getInternalId().equals(mObjectivePieceType.getInternalId())) {
            return Collections.emptySet();
        }

        return moves;
    }
}
