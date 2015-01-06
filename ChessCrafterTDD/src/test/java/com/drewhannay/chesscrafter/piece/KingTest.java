package com.drewhannay.chesscrafter.piece;

import com.drewhannay.chesscrafter.board.BoardCoordinate;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class KingTest {

    King mTarget;
    List<BoardCoordinate> mMovesFrom3_3;

    @Before
    public void setup() {
        mTarget = new King();
        mMovesFrom3_3 = mTarget.getMovesFrom(BoardCoordinate.at(3, 3));
    }

    public static class GetMovesFrom extends KingTest {
        @Test
        public void returns1_2For2_1() {
            List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(2, 1));

            assertTrue(moves.contains(BoardCoordinate.at(1, 2)));
        }

        @Test
        public void returns2_2For1_1() {
            List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(1, 1));

            assertTrue(moves.contains(BoardCoordinate.at(2, 2)));
        }

        @Test
        public void returns3_4For3_3() {
            assertTrue(mMovesFrom3_3.contains(BoardCoordinate.at(3, 4)));
        }

        @Test
        public void returns3_2For3_3() {
            assertTrue(mMovesFrom3_3.contains(BoardCoordinate.at(3, 2)));
        }

        @Test
        public void returns2_3For3_3() {
            assertTrue(mMovesFrom3_3.contains(BoardCoordinate.at(2, 3)));
        }

        @Test
        public void doesNotReturn0_0For1_1() {
            List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(1, 1));

            assertFalse(moves.contains(BoardCoordinate.at(0, 0)));
        }
    }
}
