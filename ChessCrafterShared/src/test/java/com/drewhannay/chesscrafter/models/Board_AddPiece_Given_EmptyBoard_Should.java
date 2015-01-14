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
        mTarget.addPiece(mPawn, ChessCoordinate.at(2, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionWhenChessCoordinateHasLargerXValueThanBoardSize() {
        ChessCoordinate coordinate = ChessCoordinate.at(9, 1);
        mTarget.addPiece(mPawn, coordinate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionWhenChessCoordinateHasLargerYValueThanBoardSize() {
        ChessCoordinate coordinate = ChessCoordinate.at(1, 9);
        mTarget.addPiece(mPawn, coordinate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionWhenChessCoordinateHasZeroXValue() {
        ChessCoordinate coordinate = ChessCoordinate.at(0, 3);
        mTarget.addPiece(mPawn, coordinate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionOnNullArguments() {
        //noinspection ConstantConditions
        mTarget.addPiece(null, ChessCoordinate.at(2, 3));
    }

    @Test
    public void resultInValidPieceAt9_9WhenBoardSizeIs9() {
        mTarget = new Board(BoardSize.withDimensions(9, 9));
        mTarget.addPiece(mPawn, ChessCoordinate.at(9, 9));

        assertTrue(mTarget.doesPieceExistAt(ChessCoordinate.at(9, 9)));
    }
}
