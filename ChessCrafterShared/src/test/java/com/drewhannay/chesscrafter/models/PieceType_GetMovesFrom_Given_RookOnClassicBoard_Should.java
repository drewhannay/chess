package com.drewhannay.chesscrafter.models;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class PieceType_GetMovesFrom_Given_RookOnClassicBoard_Should {

    PieceType mTarget;
    BoardSize mBoardSize;
    Set<BoardCoordinate> mMovesFrom1_1;

    @Before
    public void setup() {
        mTarget = PieceType.getRookPieceType();
        mBoardSize = BoardSize.withDimensions(8, 8);
        mMovesFrom1_1 = mTarget.getMovesFrom(BoardCoordinate.at(1, 1), mBoardSize, 0);
    }

    @Test
    public void return7VerticalMovesWithBoardSize8() {
        int count = 0;
        for (BoardCoordinate move : mMovesFrom1_1) {
            if (move.x == 1) {
                count++;
            }
        }
        assertEquals(7, count);
    }

    @Test
    public void return7HorizontalMovesWithBoardSize8() {
        int count = 0;
        for (BoardCoordinate move : mMovesFrom1_1) {
            if (move.y == 1) {
                count++;
            }
        }
        assertEquals(7, count);
    }

    @Test
    public void returnNoMovesThatContainAZero() {
        int count = 0;
        for (BoardCoordinate move : mMovesFrom1_1) {
            if (move.x == 0 || move.y == 0) {
                count++;
            }
        }
        assertEquals(0, count);
    }

    @Test
    public void return7_4From7_1() {
        Set<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(7, 1), mBoardSize, 0);

        assertTrue(moves.contains(BoardCoordinate.at(7, 4)));
    }

    @Test
    public void notReturn2_2From1_1() {
        assertFalse(mMovesFrom1_1.contains(BoardCoordinate.at(2, 2)));
    }
}
