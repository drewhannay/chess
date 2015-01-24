package com.drewhannay.chesscrafter.rules.conditionalmovegenerator;

import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.Move;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.Stack;

public abstract class ConditionalMoveGenerator {
    @NotNull
    public abstract Set<BoardCoordinate> generateMoves(@NotNull Board board, @NotNull BoardCoordinate start,
                                                       @NotNull Stack<Move> history);

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
