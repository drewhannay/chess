package com.drewhannay.chesscrafter.board;

import com.drewhannay.chesscrafter.piece.Pawn;
import com.drewhannay.chesscrafter.piece.Rook;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BoardAddPieceGivenEmptyBoardShould {

    Board mTarget;
    Pawn mPiece;

    @Before
    public void setup() {
        mTarget = new Board();
        mPiece = new Pawn();
    }

    @Test
    public void throwExceptionWhenAddingPieceToUnoccupiedSquare() {
        mTarget.addPiece(mPiece, BoardCoordinate.at(2, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionWhenBoardCoordinateHasLargerXValueThanBoardSize() {
        BoardCoordinate coordinate = BoardCoordinate.at(9, 1);
        mTarget.addPiece(mPiece, coordinate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionWhenBoardCoordinateHasLargerYValueThanBoardSize() {
        BoardCoordinate coordinate = BoardCoordinate.at(1, 9);
        mTarget.addPiece(mPiece, coordinate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionWhenBoardCoordinateHasZeroXValue() {
        BoardCoordinate coordinate = BoardCoordinate.at(0, 3);
        mTarget.addPiece(mPiece, coordinate);
    }

    @Test
    public void acceptRookAsArgumentForPiece() {
        BoardCoordinate coordinate = BoardCoordinate.at(1, 2);
        mTarget.addPiece(new Rook(), coordinate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionOnNullArguments() {
        mTarget.addPiece(null, BoardCoordinate.at(2, 3));
    }

    @Test
    public void resultInValidPieceAt9_9WhenBoardSizeIs9() {
        mTarget = new Board(9);

        mTarget.addPiece(new Rook(), BoardCoordinate.at(9, 9));

        assertTrue(mTarget.doesPieceExistAt(BoardCoordinate.at(9, 9)));
    }
}