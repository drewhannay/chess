package com.drewhannay.chesscrafter.piece;

import com.drewhannay.chesscrafter.board.BoardCoordinate;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class BishopTest {

    Bishop mTarget;

    @Before
    public void setup() {
        mTarget = new Bishop();
    }

    public static class GetMovesFrom extends BishopTest {

        @Test
        public void returnsNonEmptyList() {
            List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(1, 1));

            assertFalse(moves.isEmpty());
        }

        @Test
        public void returns2_2For1_1() {
            List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(1, 1));

            assertTrue(moves.contains(BoardCoordinate.at(2, 2)));
        }

        @Test
        public void returns1_1For2_2() {
            List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(2, 2));

            assertTrue(moves.contains(BoardCoordinate.at(1, 1)));
        }

        @Test
        public void returns2_1For1_2() {
            List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(1, 2));

            assertTrue(moves.contains(BoardCoordinate.at(2, 1)));
        }

        @Test
        public void doesNotReturnNegativeValuesForBoardCoordinates() {
            List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(1, 2));

            int count = 0;
            for (BoardCoordinate move : moves) {
                if (move.x <= 0 || move.y <= 0) {
                    count++;
                }
            }

            assertEquals(0, count);
        }

        @Test
        public void returns8_8For1_1() {
            List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(1, 1));

            assertTrue(moves.contains(BoardCoordinate.at(8, 8)));
        }

        @Test
        public void returns8_1For1_8() {
            List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(1, 8));

            assertTrue(moves.contains(BoardCoordinate.at(8, 1)));
        }
    }
}