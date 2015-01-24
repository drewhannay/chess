package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.logic.PieceTypeManager;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PieceType_GetMovesFrom_Given_BishopOnClassicBoard_Should {

    PieceType mTarget;
    BoardSize mBoardSize;
    Set<BoardCoordinate> mMovesFrom1_1;

    @Before
    public void setup() {
        mTarget = PieceTypeManager.getBishopPieceType();
        mBoardSize = BoardSize.withDimensions(8, 8);
        mMovesFrom1_1 = mTarget.getMovesFrom(BoardCoordinate.at(1, 1), mBoardSize, 0);
    }

    @Test
    public void return2_2For1_1() {
        Set<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(1, 1), mBoardSize, 0);
        assertTrue(moves.contains(BoardCoordinate.at(2, 2)));
    }

    @Test
    public void return1_1For2_2() {
        Set<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(2, 2), mBoardSize, 0);
        assertTrue(moves.contains(BoardCoordinate.at(1, 1)));
    }

    @Test
    public void return2_1For1_2() {
        Set<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(1, 2), mBoardSize, 0);
        assertTrue(moves.contains(BoardCoordinate.at(2, 1)));
    }

    @Test
    public void return8_8For1_1() {
        Set<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(1, 1), mBoardSize, 0);
        assertTrue(moves.contains(BoardCoordinate.at(8, 8)));
    }

    @Test
    public void return8_1For1_8() {
        Set<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(1, 8), mBoardSize, 0);
        assertTrue(moves.contains(BoardCoordinate.at(8, 1)));
    }

    @Test
    public void notReturn1_2For1_1() {
        Set<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(1, 1), mBoardSize, 0);
        assertFalse(moves.contains(BoardCoordinate.at(1, 2)));
    }
}
