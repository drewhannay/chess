package com.drewhannay.chesscrafter.models;

import com.google.common.base.Objects;

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

        return Objects.equal(row, otherCoordinates.row) && Objects.equal(column, otherCoordinates.column)
                && Objects.equal(boardIndex, otherCoordinates.boardIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(row, column, boardIndex);
    }

    @Override
    public String toString() {
        return row + " " + column + " " + boardIndex; //$NON-NLS-1$//$NON-NLS-2$
    }

}
