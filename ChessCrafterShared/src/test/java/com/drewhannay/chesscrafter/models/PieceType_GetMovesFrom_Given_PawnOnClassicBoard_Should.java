package com.drewhannay.chesscrafter.models;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class PieceType_GetMovesFrom_Given_PawnOnClassicBoard_Should {

    PieceType mTarget;
    BoardSize mBoardSize;
    List<ChessCoordinate> mMovesFrom2_2;

    @Before
    public void setup() {
        mTarget = PieceType.getPawnPieceType();
        mBoardSize = BoardSize.withDimensions(8, 8);
        mMovesFrom2_2 = mTarget.getMovesFrom(ChessCoordinate.at(2, 2), mBoardSize);
    }

    @Test
    public void returns2_3AsOneResultWhenPassed2_2() {
        assertTrue(mMovesFrom2_2.contains(ChessCoordinate.at(2, 3)));
    }

    @Test
    public void returns2_4AsOneResultWhenPassed2_2WhenPieceHasNotMoved() {
        assertTrue(mMovesFrom2_2.contains(ChessCoordinate.at(2, 4)));
    }
}
