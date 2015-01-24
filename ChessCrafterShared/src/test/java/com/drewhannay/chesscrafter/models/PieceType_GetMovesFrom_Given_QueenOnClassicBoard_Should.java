package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.logic.PieceTypeManager;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PieceType_GetMovesFrom_Given_QueenOnClassicBoard_Should {

    PieceType mTarget;
    BoardSize mBoardSize;
    Set<BoardCoordinate> mMovesFrom1_1;

    @Before
    public void setup() {
        mTarget = PieceTypeManager.getQueenPieceType();
        mBoardSize = BoardSize.withDimensions(8, 8);
        mMovesFrom1_1 = mTarget.getMovesFrom(BoardCoordinate.at(1, 1), mBoardSize, 0);
    }

    @Test
    public void return1_2For1_1() {
        assertTrue(mMovesFrom1_1.contains(BoardCoordinate.at(1, 2)));
    }

    @Test
    public void return2_2For1_1() {
        assertTrue(mMovesFrom1_1.contains(BoardCoordinate.at(2, 2)));
    }

    @Test
    public void return3_3For1_1() {
        assertTrue(mMovesFrom1_1.contains(BoardCoordinate.at(3, 3)));
    }

    @Test
    public void notReturn0_0For1_1() {
        assertFalse(mMovesFrom1_1.contains(BoardCoordinate.at(0, 0)));
    }
}
