package com.drewhannay.chesscrafter.models;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class Board {
    public static final int NO_ENPASSANT = 0;

    public Board(@NotNull BoardSize boardSize, boolean wrapsAround) {
        mBoardSize = boardSize;
        mIsWrapAroundBoard = wrapsAround;

        mUninhabitableSquares = Sets.newHashSet();
    }

    public BoardSize getBoardSize() {
        return mBoardSize;
    }

    @Deprecated
    public int getRowCount() {
        return mBoardSize.height;
    }

    @Deprecated
    public int getColumnCount() {
        return mBoardSize.width;
    }

    public boolean isWrapAroundBoard() {
        return mIsWrapAroundBoard;
    }

    public boolean isSquareHabitable(int row, int column) {
        Preconditions.checkArgument(isRowValid(row) && isColumnValid(column));

        return !mUninhabitableSquares.contains(ChessCoordinate.at(row, column, 0));
    }

    @Deprecated
    public boolean isRowValid(int row) {
        return row <= getRowCount() && row > 0;
    }

    @Deprecated
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
        return Objects.equal(mBoardSize, otherBoard.mBoardSize)
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

    private final BoardSize mBoardSize;
    private final boolean mIsWrapAroundBoard;
    private final Set<ChessCoordinate> mUninhabitableSquares;

    private int mEnPassantColumn;
}
