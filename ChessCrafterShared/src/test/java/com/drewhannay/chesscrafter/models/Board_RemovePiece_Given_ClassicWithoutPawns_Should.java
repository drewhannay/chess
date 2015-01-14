package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.logic.GameBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNull;

public class Board_RemovePiece_Given_ClassicWithoutPawns_Should {

    Board mTarget;

    @Before
    public void setup() {
        mTarget = new Board(BoardSize.CLASSIC_SIZE);

        GameBuilder.setupClassicPieces(mTarget, 1, Piece.TEAM_ONE);
        GameBuilder.setupClassicPieces(mTarget, 8, Piece.TEAM_TWO);
    }

    @Test
    public void resultInGetPieceFor1_1BeingNullWhenCalledWith1_1() {
        mTarget.removePiece(ChessCoordinate.at(1, 1));

        assertNull(mTarget.getPiece(ChessCoordinate.at(1, 1)));
    }

    @Test(expected = IllegalStateException.class)
    public void throwExceptionWhenCalledWith1_2() {
        mTarget.removePiece(ChessCoordinate.at(1, 2));
    }
}
