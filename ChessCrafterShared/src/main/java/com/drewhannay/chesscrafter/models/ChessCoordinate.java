package com.drewhannay.chesscrafter.models;

import java.util.Objects;

public final class ChessCoordinate {
    public final int x;
    public final int y;

    private ChessCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static ChessCoordinate at(int x, int y) {
        return new ChessCoordinate(x, y);
    }

    public boolean isValid(BoardSize boardSize) {
        return isDimensionValid(x, boardSize.width) && isDimensionValid(y, boardSize.height);
    }

    private static boolean isDimensionValid(int dimensionValue, int boardSize) {
        return dimensionValue > 0 && dimensionValue <= boardSize;
    }

    public boolean isOnSameVerticalPathAs(ChessCoordinate other) {
        return x == other.x;
    }

    public boolean isOnSameHorizontalPathAs(ChessCoordinate other) {
        return y == other.y;
    }

    public boolean isOnSameDiagonalPathAs(ChessCoordinate other) {
        return Math.abs(x - other.x) == Math.abs(y - other.y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass())
            return false;

        ChessCoordinate other = (ChessCoordinate) obj;

        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "ChessCoordinate{x=" + x + ", y=" + y + "}";
    }
}
