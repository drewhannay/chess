package com.drewhannay.chesscrafter.models;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PieceType_GetMovesFrom_Given_KnightOnClassicBoard_Should {

    PieceType mTarget;
    BoardSize mBoardSize;
    List<ChessCoordinate> mMovesFrom3_3;

    @Before
    public void setup() {
        mTarget = PieceType.getKnightPieceType();
        mBoardSize = BoardSize.withDimensions(8, 8);
        mMovesFrom3_3 = mTarget.getMovesFrom(ChessCoordinate.at(3, 3), mBoardSize, 0);
    }

    @Test
    public void returns3_2For1_1() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(1, 1), mBoardSize, 0);
        assertTrue(moves.contains(ChessCoordinate.at(3, 2)));
    }

    @Test
    public void returns2_3For1_1() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(1, 1), mBoardSize, 0);
        assertTrue(moves.contains(ChessCoordinate.at(2, 3)));
    }

    @Test
    public void returns4_3For2_2() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(2, 2), mBoardSize, 0);
        assertTrue(moves.contains(ChessCoordinate.at(4, 3)));
    }

    @Test
    public void returns3_4For2_2() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(2, 2), mBoardSize, 0);
        assertTrue(moves.contains(ChessCoordinate.at(3, 4)));
    }

    @Test
    public void returns4_1For2_2() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(2, 2), mBoardSize, 0);
        assertTrue(moves.contains(ChessCoordinate.at(4, 1)));
    }

    @Test
    public void returns1_2For3_3() {
        assertTrue(mMovesFrom3_3.contains(ChessCoordinate.at(1, 2)));
    }

    @Test
    public void returns1_4For3_3() {
        assertTrue(mMovesFrom3_3.contains(ChessCoordinate.at(1, 4)));
    }

    @Test
    public void returns2_1For3_3() {
        assertTrue(mMovesFrom3_3.contains(ChessCoordinate.at(2, 1)));
    }

    @Test
    public void returns2_5For3_3() {
        assertTrue(mMovesFrom3_3.contains(ChessCoordinate.at(2, 5)));
    }

    @Test
    public void returns4_1For3_3() {
        assertTrue(mMovesFrom3_3.contains(ChessCoordinate.at(4, 1)));
    }

    @Test
    public void doesNotReturnIllegalMoves() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(1, 1), mBoardSize, 0);
        assertFalse(moves.contains(ChessCoordinate.at(-1, 0)));
    }
}
