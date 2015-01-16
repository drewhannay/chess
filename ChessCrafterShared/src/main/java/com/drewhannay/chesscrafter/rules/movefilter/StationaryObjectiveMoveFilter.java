package com.drewhannay.chesscrafter.rules.movefilter;

import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.ChessCoordinate;
import com.drewhannay.chesscrafter.models.PieceType;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public final class StationaryObjectiveMoveFilter implements MoveFilter {

    private final PieceType mObjectivePieceType;

    public StationaryObjectiveMoveFilter(@NotNull PieceType objectivePieceType) {
        mObjectivePieceType = objectivePieceType;
    }

    @Override
    public Set<ChessCoordinate> filterMoves(@NotNull Board board, @NotNull ChessCoordinate start, @NotNull Set<ChessCoordinate> moves) {
        Preconditions.checkArgument(board.doesPieceExistAt(start));

        if (board.getPiece(start).getName().equals(mObjectivePieceType.getName())) {
            return Collections.emptySet();
        }

        return moves;
    }
}
