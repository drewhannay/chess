package com.drewhannay.chesscrafter.rules.conditionalmovegenerator;

import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.History;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class ConditionalMoveGenerator {
    @NotNull
    public abstract Set<BoardCoordinate> generateMoves(@NotNull Board board, @NotNull BoardCoordinate start,
                                                       @NotNull History history);

    public static ConditionalMoveGenerator from(@NotNull String name) {
        switch (name) {
            case CastlingMoveGenerator.NAME:
                return new CastlingMoveGenerator();
            case EnPassantMoveGenerator.NAME:
                return new EnPassantMoveGenerator();
        }

        throw new IllegalArgumentException("Unknown ConditionalMoveGenerator name:" + name);
    }
}
