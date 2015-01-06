package com.drewhannay.chesscrafter.board;

import com.drewhannay.chesscrafter.piece.Piece;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardMovePieceGivenChessboardSetupWithoutPawnsShould {

    Board mTarget;
    BoardCoordinate mOriginCoordinate;
    BoardCoordinate mDestinationCoordinate;

    @Before
    public void setup() {
        mTarget = new Board();
        mOriginCoordinate = BoardCoordinate.at(1, 1);
        mDestinationCoordinate = BoardCoordinate.at(1, 2);

        BoardGetMovesFromGivenNormalChessboardSetupShould.setStandardPieces(mTarget, 1, Board.FIRST_TEAM_ID);
        BoardGetMovesFromGivenNormalChessboardSetupShould.setStandardPieces(mTarget, 8, Board.SECOND_TEAM_ID);
    }

    @Test
    public void resultInPieceAt1_2WhenRookAt1_1IsMovedTo1_2() {
        mTarget.movePiece(mOriginCoordinate, mDestinationCoordinate);

        assertTrue(mTarget.doesPieceExistAt(mDestinationCoordinate));
    }

    @Test
    public void resultInNoPieceAt1_1WhenRookAt1_1IsMovedTo1_2() {
        mTarget.movePiece(mOriginCoordinate, mDestinationCoordinate);

        assertFalse(mTarget.doesPieceExistAt(mOriginCoordinate));
    }

    @Test
    public void resultInPieceAtOriginRelocatedToDestination() {
        Piece pieceThatWillBeMoved = mTarget.getPiece(mOriginCoordinate);

        mTarget.movePiece(mOriginCoordinate, mDestinationCoordinate);

        Piece pieceAtDestination = mTarget.getPiece(mDestinationCoordinate);

        assertEquals(pieceThatWillBeMoved, pieceAtDestination);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionForOutOfBoundsOriginArgument() {
        mTarget.movePiece(BoardCoordinate.at(1, 9), mDestinationCoordinate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionForOutOfBoundsDestinationArgument() {
        mTarget.movePiece(mOriginCoordinate, BoardCoordinate.at(10, 10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionWhenAttemptIsMadeToMoveNonExistentPiece() {
        mTarget.movePiece(mDestinationCoordinate, mOriginCoordinate);
    }
}