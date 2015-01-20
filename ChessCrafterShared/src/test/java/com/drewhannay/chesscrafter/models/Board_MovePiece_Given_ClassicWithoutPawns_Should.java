package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.logic.GameBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class Board_MovePiece_Given_ClassicWithoutPawns_Should {
    Board mTarget;
    BoardCoordinate mOriginCoordinate;
    BoardCoordinate mDestinationCoordinate;

    @Before
    public void setup() {
        mTarget = new Board(BoardSize.CLASSIC_SIZE);
        mOriginCoordinate = BoardCoordinate.at(1, 1);
        mDestinationCoordinate = BoardCoordinate.at(1, 2);

        GameBuilder.setupClassicPieces(mTarget, 1, Piece.TEAM_ONE);
        GameBuilder.setupClassicPieces(mTarget, 8, Piece.TEAM_TWO);
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

    @Test
    public void returnNullWhenMovingRookFrom1_1To1_2() {
        Piece capturedPiece = mTarget.movePiece(mOriginCoordinate, mDestinationCoordinate);
        assertNull(capturedPiece);
    }

    @Test
    public void returnPieceWhenMovingRookFrom1_1To1_8() {
        Piece capturedPiece = mTarget.movePiece(mOriginCoordinate, BoardCoordinate.at(1, 8));
        assertNotNull(capturedPiece);
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

    @Test
    public void resultInHasMovedReturningTrueAfterMovingRookAt1_1() {
        mTarget.movePiece(mOriginCoordinate, mDestinationCoordinate);
        assertTrue(mTarget.getPiece(mDestinationCoordinate).hasMoved());
    }
}
