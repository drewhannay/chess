package com.drewhannay.chesscrafter.models;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import java.util.Arrays;

public final class Board {
    public static final int NO_ENPASSANT = 0;

    public Board(int rowCount, int columnCount, boolean wrapsAround) {
        mRowCount = rowCount;
        mColumnCount = columnCount;
        mIsWrapAroundBoard = wrapsAround;

        mSquares = new Square[rowCount][columnCount];

        for (int row = 0, column = 0; row < rowCount; row++) {
            // add one to the row and column to ignore counting from zero
            for (column = 0; column < columnCount; column++)
                mSquares[row][column] = new Square((row + 1), (column + 1));
        }
    }

    public int getRowCount() {
        return mRowCount;
    }

    public int getColumnCount() {
        return mColumnCount;
    }

    public boolean isWrapAroundBoard() {
        return mIsWrapAroundBoard;
    }

    public Square getSquare(int row, int column) {
        Preconditions.checkArgument(isRowValid(row) && isColumnValid(column));

        // use x-1 and y-1 so we can maintain the illusion of counting from 1
        return mSquares[row - 1][column - 1];
    }

    public boolean isRowValid(int row) {
        return row <= getRowCount() && row > 0;
    }

    public boolean isColumnValid(int column) {
        return column <= getColumnCount() && column > 0;
    }

    public int getEnpassantCol() {
        return mEnPassantColumn;
    }

    public void setEnpassantCol(int enpassantCol) {
        mEnPassantColumn = enpassantCol;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Board))
            return false;

        Board otherBoard = (Board) other;

        // TODO: need to compare the mutable state (mEnPassantColumn) as well, but how to do that?
        return Objects.equal(mRowCount, otherBoard.mRowCount)
                && Objects.equal(mColumnCount, otherBoard.mColumnCount)
                && Objects.equal(mIsWrapAroundBoard, otherBoard.mIsWrapAroundBoard)
                && Arrays.deepEquals(mSquares, otherBoard.mSquares);
    }

    /**
     * Debugging method to print the state of the Board
     *
     * @param game       The Game state
     * @param boardIndex The index of the Board
     */
    public void printBoard(Game game, int boardIndex) {
        for (int i = mSquares.length - 1; i >= 0; i--) {
            for (Square square : mSquares[i]) {
                System.out.print(square.printSquareState(game, boardIndex) + "\t"); //$NON-NLS-1$
            }
            System.out.println();
        }
    }

    private final int mRowCount;
    private final int mColumnCount;
    private final boolean mIsWrapAroundBoard;
    private final Square mSquares[][];

    private int mEnPassantColumn;
}
