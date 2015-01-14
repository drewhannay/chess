package com.drewhannay.chesscrafter.models;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PieceType_GetMovesFrom_Given_QueenOnClassicBoard_Should {

    PieceType mTarget;
    BoardSize mBoardSize;
    List<ChessCoordinate> mMovesFrom1_1;

    @Before
    public void setup() {
        mTarget = PieceType.getQueenPieceType();
        mBoardSize = BoardSize.withDimensions(8, 8);
        mMovesFrom1_1 = mTarget.getMovesFrom(ChessCoordinate.at(1, 1), mBoardSize, 0);
    }

    @Test
    public void return1_2For1_1() {
        assertTrue(mMovesFrom1_1.contains(ChessCoordinate.at(1, 2)));
    }

    @Test
    public void return2_2For1_1() {
        assertTrue(mMovesFrom1_1.contains(ChessCoordinate.at(2, 2)));
    }

    @Test
    public void return3_3For1_1() {
        assertTrue(mMovesFrom1_1.contains(ChessCoordinate.at(3, 3)));
    }

    @Test
    public void notReturn0_0For1_1() {
        assertFalse(mMovesFrom1_1.contains(ChessCoordinate.at(0, 0)));
    }
}
