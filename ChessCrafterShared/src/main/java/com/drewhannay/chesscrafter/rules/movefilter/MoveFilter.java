package com.drewhannay.chesscrafter.rules.movefilter;

import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.ChessCoordinate;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface MoveFilter {
    public Set<ChessCoordinate> filterMoves(@NotNull Board board, @NotNull ChessCoordinate start, @NotNull Set<ChessCoordinate> moves);
}
