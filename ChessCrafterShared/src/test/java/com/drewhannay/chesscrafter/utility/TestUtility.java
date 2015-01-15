package com.drewhannay.chesscrafter.utility;

import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.ChessCoordinate;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class TestUtility {
    public static void printMovesOnBoard(@NotNull Board board, @NotNull Set<ChessCoordinate> moves) {
        printHorizontalBorder(board.getBoardSize().width);
        for (int y = board.getBoardSize().height; y >= 1; y--) {
            System.out.print('|');
            for (int x = 1; x <= board.getBoardSize().width; x++) {
                ChessCoordinate space = ChessCoordinate.at(x, y);
                String piece = board.doesPieceExistAt(space) ? String.valueOf(board.getPiece(space).getName().charAt(0)) : " ";
                String move = moves.contains(space) ? "x" : " ";
                System.out.print(piece + move + "|");
            }
            System.out.println();
            printHorizontalBorder(board.getBoardSize().width);
        }
    }

    private static void printHorizontalBorder(int boardWidth) {
        for (int x = 1; x <= boardWidth * 3 + 1; x++) {
            System.out.print('-');
        }
        System.out.println();
    }
}
