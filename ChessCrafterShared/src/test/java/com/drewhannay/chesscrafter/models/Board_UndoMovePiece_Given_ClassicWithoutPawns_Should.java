package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.logic.GameBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Board_UndoMovePiece_Given_ClassicWithoutPawns_Should {
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
    public void resultInPieceAt1_1WhenRookAt1_1IsMovedTo1_2AndUnmoved() {
        Piece capturedPiece = mTarget.movePiece(mOriginCoordinate, mDestinationCoordinate);
        mTarget.undoMovePiece(mOriginCoordinate, mDestinationCoordinate, capturedPiece);

        assertTrue(mTarget.doesPieceExistAt(mOriginCoordinate));
    }

    @Test
    public void resultInNoPieceAt1_2WhenRookAt1_1IsMovedTo1_2AndUnmoved() {
        Piece capturedPiece = mTarget.movePiece(mOriginCoordinate, mDestinationCoordinate);
        mTarget.undoMovePiece(mOriginCoordinate, mDestinationCoordinate, capturedPiece);

        assertFalse(mTarget.doesPieceExistAt(mDestinationCoordinate));
    }

    @Test
    public void resultInPieceAtOriginRelocatedToDestination() {
        Piece pieceThatWillBeMoved = mTarget.getPiece(mOriginCoordinate);

        Piece capturedPiece = mTarget.movePiece(mOriginCoordinate, mDestinationCoordinate);
        mTarget.undoMovePiece(mOriginCoordinate, mDestinationCoordinate, capturedPiece);

        Piece pieceAtOrigin = mTarget.getPiece(mOriginCoordinate);

        assertEquals(pieceThatWillBeMoved, pieceAtOrigin);
    }

    @Test
    public void replacePieceWhenRookAt1_1IsMovedTo1_8AndUnmoved() {
        BoardCoordinate destination = BoardCoordinate.at(1, 8);
        Piece capturedPiece = mTarget.movePiece(mOriginCoordinate, destination);
        mTarget.undoMovePiece(mOriginCoordinate, destination, capturedPiece);
        assertEquals(capturedPiece, mTarget.getPiece(destination));
    }

    @Test
    public void resultInHasMovedReturningFalseAfterMovingAndUnmovingRookAt1_1() {
        Piece capturedPiece = mTarget.movePiece(mOriginCoordinate, mDestinationCoordinate);
        mTarget.undoMovePiece(mOriginCoordinate, mDestinationCoordinate, capturedPiece);

        assertFalse(mTarget.getPiece(mOriginCoordinate).hasMoved());
    }
}
