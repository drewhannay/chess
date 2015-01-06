package com.drewhannay.chesscrafter.piece;

import com.drewhannay.chesscrafter.board.BoardCoordinate;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class KnightTest {

    Knight mTarget;
    List<BoardCoordinate> mMovesFrom3_3;

    List<BoardCoordinate> getMoves(int x, int y) {
        return mTarget.getMovesFrom(BoardCoordinate.at(x, y));
    }

    @Before
    public void setup() {
        mTarget = new Knight();
        mMovesFrom3_3 = getMoves(3, 3);
    }

    public static class GetMovesFrom extends KnightTest {

        @Test
        public void returns3_2For1_1() {
            List<BoardCoordinate> moves = getMoves(1, 1);

            assertTrue(moves.contains(BoardCoordinate.at(3, 2)));
        }

        @Test
        public void returns2_3For1_1() {
            List<BoardCoordinate> moves = getMoves(1, 1);

            assertTrue(moves.contains(BoardCoordinate.at(2, 3)));
        }

        @Test
        public void returns4_3For2_2() {
            List<BoardCoordinate> moves = getMoves(2, 2);

            assertTrue(moves.contains(BoardCoordinate.at(4, 3)));
        }

        @Test
        public void returns3_4For2_2() {
            List<BoardCoordinate> moves = getMoves(2, 2);

            assertTrue(moves.contains(BoardCoordinate.at(3, 4)));
        }

        @Test
        public void returns4_1For2_2() {
            List<BoardCoordinate> moves = getMoves(2, 2);

            assertTrue(moves.contains(BoardCoordinate.at(4, 1)));
        }

        @Test
        public void returns1_2For3_3() {
            assertTrue(mMovesFrom3_3.contains(BoardCoordinate.at(1, 2)));
        }

        @Test
        public void returns1_4For3_3() {
            assertTrue(mMovesFrom3_3.contains(BoardCoordinate.at(1, 4)));
        }

        @Test
        public void returns2_1For3_3() {
            assertTrue(mMovesFrom3_3.contains(BoardCoordinate.at(2, 1)));
        }

        @Test
        public void returns2_5For3_3() {
            assertTrue(mMovesFrom3_3.contains(BoardCoordinate.at(2, 5)));
        }

        @Test
        public void returns4_1For3_3() {
            assertTrue(mMovesFrom3_3.contains(BoardCoordinate.at(4, 1)));
        }

        @Test
        public void doesNotReturnIllegalMoves() {
            List<BoardCoordinate> moves = getMoves(1, 1);

            assertFalse(moves.contains(BoardCoordinate.at(-1, 0)));
        }
    }
}