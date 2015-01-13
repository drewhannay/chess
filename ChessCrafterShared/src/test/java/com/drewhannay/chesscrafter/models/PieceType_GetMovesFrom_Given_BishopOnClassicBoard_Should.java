package com.drewhannay.chesscrafter.models;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PieceType_GetMovesFrom_Given_BishopOnClassicBoard_Should {

    PieceType mTarget;
    BoardSize mBoardSize;
    List<ChessCoordinate> mMovesFrom1_1;

    @Before
    public void setup() {
        mTarget = PieceType.getBishopPieceType();
        mBoardSize = BoardSize.withDimensions(8, 8);
        mMovesFrom1_1 = mTarget.getMovesFrom(ChessCoordinate.at(1, 1), mBoardSize);
    }

    @Test
    public void return2_2For1_1() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(1, 1), mBoardSize);
        assertTrue(moves.contains(ChessCoordinate.at(2, 2)));
    }

    @Test
    public void return1_1For2_2() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(2, 2), mBoardSize);
        assertTrue(moves.contains(ChessCoordinate.at(1, 1)));
    }

    @Test
    public void return2_1For1_2() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(1, 2), mBoardSize);
        assertTrue(moves.contains(ChessCoordinate.at(2, 1)));
    }

    @Test
    public void return8_8For1_1() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(1, 1), mBoardSize);
        assertTrue(moves.contains(ChessCoordinate.at(8, 8)));
    }

    @Test
    public void return8_1For1_8() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(1, 8), mBoardSize);
        assertTrue(moves.contains(ChessCoordinate.at(8, 1)));
    }

    @Test
    public void notReturn1_2For1_1() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(1, 1), mBoardSize);
        assertFalse(moves.contains(ChessCoordinate.at(1, 2)));
    }
}
