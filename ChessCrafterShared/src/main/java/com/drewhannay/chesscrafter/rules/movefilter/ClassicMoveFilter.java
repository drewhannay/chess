package com.drewhannay.chesscrafter.rules.movefilter;

import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.PieceType;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class ClassicMoveFilter implements MoveFilter {

    private final PieceType mObjectivePieceType;

    public ClassicMoveFilter(@NotNull PieceType objectivePieceType) {
        mObjectivePieceType = objectivePieceType;
    }

    @Override
    public Set<BoardCoordinate> filterMoves(@NotNull Board board, @NotNull BoardCoordinate start, @NotNull Set<BoardCoordinate> moves) {
        Preconditions.checkArgument(board.doesPieceExistAt(start));

        int teamId = board.getPiece(start).getTeamId();

        return moves;
    }
}
