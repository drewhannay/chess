package com.drewhannay.chesscrafter.board;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNull;

public class BoardRemovePieceGivenChessboardSetupWithoutPawnsShould {

    Board mTarget;

    @Before
    public void setup() {
        mTarget = new Board();

        BoardGetMovesFromGivenNormalChessboardSetupShould.setStandardPieces(mTarget, 1, true);
        BoardGetMovesFromGivenNormalChessboardSetupShould.setStandardPieces(mTarget, 8, false);
    }

    @Test
    public void resultInGetPieceFor1_1BeingNullWhenCalledWith1_1() {
        mTarget.removePiece(BoardCoordinate.at(1, 1));

        assertNull(mTarget.getPiece(BoardCoordinate.at(1, 1)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionWhenCalledWith1_2() {
        mTarget.removePiece(BoardCoordinate.at(1, 2));
    }
}