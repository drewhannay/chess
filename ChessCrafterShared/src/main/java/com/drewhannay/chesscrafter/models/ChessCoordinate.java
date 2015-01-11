package com.drewhannay.chesscrafter.models;

import java.util.Objects;

public final class ChessCoordinate {
    public final int row;
    public final int column;
    public final int boardIndex;

    private ChessCoordinate(int row, int column, int boardIndex) {
        this.row = row;
        this.column = column;
        this.boardIndex = boardIndex;
    }

    public static ChessCoordinate at(int row, int column, int boardIndex) {
        return new ChessCoordinate(row, column, boardIndex);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ChessCoordinate))
            return false;

        ChessCoordinate otherCoordinates = (ChessCoordinate) other;

        return row == otherCoordinates.row &&
                column == otherCoordinates.column &&
                boardIndex == otherCoordinates.boardIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column, boardIndex);
    }

    @Override
    public String toString() {
        return "ChessCoordinate{row=" + row + ", column=" + column + ", boardIndex=" + boardIndex + "}";
    }
}
