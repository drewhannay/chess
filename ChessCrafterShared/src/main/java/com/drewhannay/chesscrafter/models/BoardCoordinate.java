package com.drewhannay.chesscrafter.models;

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

    public boolean isValid(BoardSize boardSize) {
        return isDimensionValid(x, boardSize.width) && isDimensionValid(y, boardSize.height);
    }

    private static boolean isDimensionValid(int dimensionValue, int boardSize) {
        return dimensionValue > 0 && dimensionValue <= boardSize;
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
        if (obj == null || obj.getClass() != this.getClass())
            return false;

        BoardCoordinate other = (BoardCoordinate) obj;

        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "BoardCoordinate{x=" + x + ", y=" + y + "}";
    }
}
