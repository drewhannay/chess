package com.drewhannay.chesscrafter.models;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PieceType_GetMovesFrom_Given_KingOnClassicBoard_Should {

    PieceType mTarget;
    BoardSize mBoardSize;
    List<ChessCoordinate> mMovesFrom3_3;

    @Before
    public void setup() {
        mTarget = PieceType.getKingPieceType();
        mBoardSize = BoardSize.withDimensions(8, 8);
        mMovesFrom3_3 = mTarget.getMovesFrom(ChessCoordinate.at(3, 3), mBoardSize);
    }

    @Test
    public void return1_2For2_1() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(2, 1), mBoardSize);
        assertTrue(moves.contains(ChessCoordinate.at(1, 2)));
    }

    @Test
    public void return2_2For1_1() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(1, 1), mBoardSize);
        assertTrue(moves.contains(ChessCoordinate.at(2, 2)));
    }

    @Test
    public void return3_4For3_3() {
        assertTrue(mMovesFrom3_3.contains(ChessCoordinate.at(3, 4)));
    }

    @Test
    public void return3_2For3_3() {
        assertTrue(mMovesFrom3_3.contains(ChessCoordinate.at(3, 2)));
    }

    @Test
    public void return2_3For3_3() {
        assertTrue(mMovesFrom3_3.contains(ChessCoordinate.at(2, 3)));
    }

    @Test
    public void notReturn0_0For1_1() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(1, 1), mBoardSize);
        assertFalse(moves.contains(ChessCoordinate.at(0, 0)));
    }

    @Test
    public void notReturn3_3For1_1() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(1, 1), mBoardSize);
        assertFalse(moves.contains(ChessCoordinate.at(3, 3)));
    }
}