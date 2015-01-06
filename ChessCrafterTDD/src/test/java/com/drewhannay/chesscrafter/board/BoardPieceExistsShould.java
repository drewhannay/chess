package com.drewhannay.chesscrafter.board;

import com.drewhannay.chesscrafter.piece.Rook;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoardPieceExistsShould {

    Board mTarget;
    BoardCoordinate mCoordinate;

    @Before
    public void setup() {
        mTarget = new Board();
        mCoordinate = BoardCoordinate.at(1, 1);
    }

    @Test
    public void returnFalseForEmptySquare() {
        assertFalse(mTarget.doesPieceExistAt(mCoordinate));
    }

    @Test
    public void returnTrueForPopulatedSquare() {
        mTarget.addPiece(new Rook(), mCoordinate);
        assertTrue(mTarget.doesPieceExistAt(mCoordinate));
    }
}