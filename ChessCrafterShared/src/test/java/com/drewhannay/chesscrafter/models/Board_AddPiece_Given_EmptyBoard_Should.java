package com.drewhannay.chesscrafter.models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class Board_AddPiece_Given_EmptyBoard_Should {
    Board mTarget;
    Piece mPawn;

    @Before
    public void setup() {
        mTarget = new Board(BoardSize.withDimensions(8, 8));
        mPawn = Piece.newPawn();
    }

    @Test
    public void notThrowExceptionWhenAddingPieceToUnoccupiedSquare() {
        mTarget.addPiece(mPawn, BoardCoordinate.at(2, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionWhenBoardCoordinateHasLargerXValueThanBoardSize() {
        BoardCoordinate coordinate = BoardCoordinate.at(9, 1);
        mTarget.addPiece(mPawn, coordinate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionWhenBoardCoordinateHasLargerYValueThanBoardSize() {
        BoardCoordinate coordinate = BoardCoordinate.at(1, 9);
        mTarget.addPiece(mPawn, coordinate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionWhenBoardCoordinateHasZeroXValue() {
        BoardCoordinate coordinate = BoardCoordinate.at(0, 3);
        mTarget.addPiece(mPawn, coordinate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionOnNullArguments() {
        //noinspection ConstantConditions
        mTarget.addPiece(null, BoardCoordinate.at(2, 3));
    }

    @Test
    public void resultInValidPieceAt9_9WhenBoardSizeIs9() {
        mTarget = new Board(BoardSize.withDimensions(9, 9));
        mTarget.addPiece(mPawn, BoardCoordinate.at(9, 9));

        assertTrue(mTarget.doesPieceExistAt(BoardCoordinate.at(9, 9)));
    }
}
