package com.drewhannay.chesscrafter.utility;

import com.drewhannay.chesscrafter.models.BoardSize;
import com.drewhannay.chesscrafter.models.ChessCoordinate;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class TestUtility {
    public static void printMovesOnBoard(@NotNull List<ChessCoordinate> moves, @NotNull BoardSize boardSize) {
        System.out.print("-----------------");
        System.out.println();
        for (int y = boardSize.height; y >= 1; y--) {
            System.out.print('|');
            for (int x = 1; x <= boardSize.width; x++) {
                if (moves.contains(ChessCoordinate.at(x, y))) {
                    System.out.print("x|");
                } else {
                    System.out.print(" |");
                }
            }
            System.out.println();
            System.out.print("-----------------");
            System.out.println();
        }
    }
}
