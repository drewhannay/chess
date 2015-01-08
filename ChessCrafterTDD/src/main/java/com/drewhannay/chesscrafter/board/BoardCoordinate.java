package com.drewhannay.chesscrafter.board;

import java.util.Objects;

public final class BoardCoordinate {
    public final int x;
    public final int y;

    private BoardCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static BoardCoordinate at(int x, int y) {
        return new BoardCoordinate(x, y);
    }

    public boolean isCoordinateValidForBoardSize(int boardSize) {
        return isDimensionValidForBoardSize(x, boardSize) && isDimensionValidForBoardSize(y, boardSize);
    }

    public boolean isOnSameVerticalPathAs(BoardCoordinate other) {
        return x == other.x;
    }

    public boolean isOnSameHorizontalPathAs(BoardCoordinate other) {
        return y == other.y;
    }

    public boolean isOnSameDiagonalPathAs(BoardCoordinate other) {
        return Math.abs(x - other.x) == Math.abs(y - other.y);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BoardCoordinate)) {
            return false;
        }

        BoardCoordinate other = (BoardCoordinate) obj;
        return other.x == x && other.y == y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "BoardCoordinate{x=" + x + ", y=" + y + "}";
    }

    private static boolean isDimensionValidForBoardSize(int dimensionValue, int boardSize) {
        return dimensionValue > 0 && dimensionValue <= boardSize;
    }
}
