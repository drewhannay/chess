package com.drewhannay.chesscrafter.models;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PieceType_GetMovesFrom_Given_KingOnClassicBoard_Should {

    PieceType mTarget;
    BoardSize mBoardSize;
    Set<BoardCoordinate> mMovesFrom3_3;

    @Before
    public void setup() {
        mTarget = PieceType.getKingPieceType();
        mBoardSize = BoardSize.withDimensions(8, 8);
        mMovesFrom3_3 = mTarget.getMovesFrom(BoardCoordinate.at(3, 3), mBoardSize, 0);
    }

    @Test
    public void return1_2For2_1() {
        Set<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(2, 1), mBoardSize, 0);
        assertTrue(moves.contains(BoardCoordinate.at(1, 2)));
    }

    @Test
    public void notReturn2_1For2_1() {
        Set<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(2, 1), mBoardSize, 0);
        assertFalse(moves.contains(BoardCoordinate.at(2, 1)));
    }

    @Test
    public void return1_1For2_1() {
        Set<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(2, 1), mBoardSize, 0);
        assertTrue(moves.contains(BoardCoordinate.at(1, 1)));
    }

    @Test
    public void return2_2For1_1() {
        Set<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(1, 1), mBoardSize, 0);
        assertTrue(moves.contains(BoardCoordinate.at(2, 2)));
    }

    @Test
    public void return3_4For3_3() {
        assertTrue(mMovesFrom3_3.contains(BoardCoordinate.at(3, 4)));
    }

    @Test
    public void return3_2For3_3() {
        assertTrue(mMovesFrom3_3.contains(BoardCoordinate.at(3, 2)));
    }

    @Test
    public void return2_3For3_3() {
        assertTrue(mMovesFrom3_3.contains(BoardCoordinate.at(2, 3)));
    }

    @Test
    public void notReturn0_0For1_1() {
        Set<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(1, 1), mBoardSize, 0);
        assertFalse(moves.contains(BoardCoordinate.at(0, 0)));
    }

    @Test
    public void notReturn3_3For1_1() {
        Set<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(1, 1), mBoardSize, 0);
        assertFalse(moves.contains(BoardCoordinate.at(3, 3)));
    }

    @Test
    public void return4_7For4_8() {
        Set<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(4, 8), mBoardSize, 0);
        assertTrue(moves.contains(BoardCoordinate.at(4, 7)));
    }

    @Test
    public void notReturn4_8For4_8() {
        Set<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(4, 8), mBoardSize, 0);
        assertFalse(moves.contains(BoardCoordinate.at(4, 8)));
    }
}
