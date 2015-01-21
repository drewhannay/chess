package com.drewhannay.chesscrafter.rules.conditionalmovegenerator;

import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.Move;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.Stack;

public interface ConditionalMoveGenerator {
    @NotNull
    public Set<BoardCoordinate> generateMoves(@NotNull Board board, @NotNull BoardCoordinate start,
                                              @NotNull Stack<Move> history);
}
