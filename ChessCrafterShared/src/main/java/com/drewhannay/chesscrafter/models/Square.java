package com.drewhannay.chesscrafter.models;

import com.google.common.base.Objects;

public final class Square {
    public Square(int row, int column) {
        this(row, column, true);
    }

    public Square(int row, int column, boolean isHabitable) {
        mRow = row;
        mColumn = column;
        mIsHabitable = true;
    }

    public int getCol() {
        return mColumn;
    }

    public int getRow() {
        return mRow;
    }

    public boolean isHabitable() {
        return mIsHabitable;
    }

    @Override
    public String toString() {
        return toString(new boolean[]{false, false});
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Square))
            return false;

        Square otherSquare = (Square) other;
        return Objects.equal(mRow, otherSquare.mRow)
                && Objects.equal(mColumn, otherSquare.mColumn)
                && Objects.equal(mIsHabitable, otherSquare.mIsHabitable);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mRow, mColumn, mIsHabitable);
    }

    /**
     * Get a String representation of this Square
     *
     * @param unique If the row and/or column of this square must be shown
     * @return The String representation of this Square
     */
    public String toString(boolean[] unique) {
        String files = "-abcdefgh"; //$NON-NLS-1$
        String toReturn = ""; //$NON-NLS-1$

        if (!unique[0])
            toReturn += files.charAt(mColumn);
        if (!unique[1])
            toReturn += mRow;

        return toReturn;
    }

    /**
     * Debugging method to assist in printing Game state.
     *
     * @param game       The Game state
     * @param boardIndex The index of the Board that this Square is on
     * @return
     */
    public String printSquareState(Game game, int boardIndex) {
        ChessCoordinate coordinates = ChessCoordinate.at(mRow, mColumn, boardIndex);
        if (game.getPieceOnSquare(coordinates) != null)
            return game.getPieceOnSquare(ChessCoordinate.at(mRow, mColumn, boardIndex)).getPieceType().toString().charAt(0) + "";
        else
            return "___";
    }

    private final int mRow;
    private final int mColumn;
    private final boolean mIsHabitable;
}
