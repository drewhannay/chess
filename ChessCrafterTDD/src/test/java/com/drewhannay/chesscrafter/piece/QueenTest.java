package com.drewhannay.chesscrafter.piece;

import com.drewhannay.chesscrafter.board.BoardCoordinate;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class QueenTest {

    Queen mTarget;
    List<BoardCoordinate> mMovesFrom1_1;

    @Before
    public void setup() {
        mTarget = new Queen();
        mMovesFrom1_1 = mTarget.getMovesFrom(BoardCoordinate.at(1, 1));
    }

    public static class GetMovesFrom extends QueenTest {
        @Test
        public void returns1_2For1_1() {
            assertTrue(mMovesFrom1_1.contains(BoardCoordinate.at(1, 2)));
        }

        @Test
        public void returns2_2For1_1() {
            assertTrue(mMovesFrom1_1.contains(BoardCoordinate.at(2, 2)));
        }

        @Test
        public void returns3_3For1_1() {
            assertTrue(mMovesFrom1_1.contains(BoardCoordinate.at(3, 3)));
        }

        @Test
        public void doesNotReturn0_0For1_1() {
            assertFalse(mMovesFrom1_1.contains(BoardCoordinate.at(0, 0)));
        }
    }
}