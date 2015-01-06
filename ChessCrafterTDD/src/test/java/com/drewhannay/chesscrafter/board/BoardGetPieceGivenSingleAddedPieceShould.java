package com.drewhannay.chesscrafter.board;

import com.drewhannay.chesscrafter.piece.Pawn;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BoardGetPieceGivenSingleAddedPieceShould {

    Board mTarget;
    Pawn mPiece;

    @Before
    public void setup() {
        mTarget = new Board();
        mPiece = new Pawn();

        mTarget.addPiece(mPiece, BoardCoordinate.at(1, 1));
    }

    @Test
    public void retrievesPieceAddedToLocation() {
        assertEquals(mPiece, mTarget.getPiece(BoardCoordinate.at(1, 1)));
    }
}
