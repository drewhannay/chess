package com.drewhannay.chesscrafter.rules.movefilter;

import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class MoveFilter {
    public abstract Set<BoardCoordinate> filterMoves(@NotNull Board board, @NotNull BoardCoordinate start,
                                                     @NotNull Set<BoardCoordinate> moves);

    public static MoveFilter from(@NotNull String name) {
        switch (name) {
            case ClassicMoveFilter.NAME:
                return new ClassicMoveFilter();
        }

        throw new IllegalArgumentException("Unknown MoveFilter name:" + name);
    }
}
