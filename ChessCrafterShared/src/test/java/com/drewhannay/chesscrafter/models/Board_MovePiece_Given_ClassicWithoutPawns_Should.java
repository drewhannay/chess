package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.logic.GameBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Board_MovePiece_Given_ClassicWithoutPawns_Should {
    Board mTarget;
    ChessCoordinate mOriginCoordinate;
    ChessCoordinate mDestinationCoordinate;

    @Before
    public void setup() {
        mTarget = new Board(BoardSize.CLASSIC_SIZE);
        mOriginCoordinate = ChessCoordinate.at(1, 1);
        mDestinationCoordinate = ChessCoordinate.at(1, 2);

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

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionForOutOfBoundsOriginArgument() {
        mTarget.movePiece(ChessCoordinate.at(1, 9), mDestinationCoordinate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionForOutOfBoundsDestinationArgument() {
        mTarget.movePiece(mOriginCoordinate, ChessCoordinate.at(10, 10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionWhenAttemptIsMadeToMoveNonExistentPiece() {
        mTarget.movePiece(mDestinationCoordinate, mOriginCoordinate);
    }
}
