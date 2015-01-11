package com.drewhannay.chesscrafter.models;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import java.util.Set;

public final class Board {
    public static final int NO_ENPASSANT = 0;

    public Board(int rowCount, int columnCount, boolean wrapsAround) {
        mRowCount = rowCount;
        mColumnCount = columnCount;
        mIsWrapAroundBoard = wrapsAround;

        mUninhabitableSquares = Sets.newHashSet();
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

    public boolean isSquareHabitable(int row, int column) {
        Preconditions.checkArgument(isRowValid(row) && isColumnValid(column));

        return !mUninhabitableSquares.contains(ChessCoordinate.at(row, column, 0));
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
        // TODO: Board is not a value type, so it shouldn't need equals()
        if (!(other instanceof Board))
            return false;

        Board otherBoard = (Board) other;

        // TODO: need to compare the mutable state (mEnPassantColumn) as well, but how to do that?
        return Objects.equal(mRowCount, otherBoard.mRowCount)
                && Objects.equal(mColumnCount, otherBoard.mColumnCount)
                && Objects.equal(mIsWrapAroundBoard, otherBoard.mIsWrapAroundBoard);
//                && Arrays.deepEquals(mUninhabitableSquares, otherBoard.mUninhabitableSquares);
    }

    /**
     * Debugging method to print the state of the Board
     *
     * @param game       The Game state
     * @param boardIndex The index of the Board
     */
    public void printBoard(Game game, int boardIndex) {
//        for (int i = mUninhabitableSquares.length - 1; i >= 0; i--) {
//            for (Square square : mUninhabitableSquares[i]) {
//                System.out.print(square.printSquareState(game, boardIndex) + "\t"); //$NON-NLS-1$
//            }
//            System.out.println();
//        }
    }

    private final int mRowCount;
    private final int mColumnCount;
    private final boolean mIsWrapAroundBoard;
    private final Set<ChessCoordinate> mUninhabitableSquares;

    private int mEnPassantColumn;
}
