package com.drewhannay.chesscrafter.piece;

import com.drewhannay.chesscrafter.board.BoardCoordinate;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RookTest {

    Rook mTarget;
    List<BoardCoordinate> mMovesFrom1_1;

    @Before
    public void setup() {
        mTarget = new Rook();
        mMovesFrom1_1 = mTarget.getMovesFrom(BoardCoordinate.at(1, 1));
    }

    public static class GetMovesFrom extends RookTest {
        @Test
        public void returns7VerticalMovesWithBoardSize8() {
            int count = 0;
            for (BoardCoordinate move : mMovesFrom1_1) {
                if (move.x == 1) {
                    count++;
                }
            }
            assertEquals(7, count);
        }

        @Test
        public void returns7HorizontalMovesWithBoardSize8() {
            int count = 0;
            for (BoardCoordinate move : mMovesFrom1_1) {
                if (move.y == 1) {
                    count++;
                }
            }
            assertEquals(7, count);
        }

        @Test
        public void returnsNoMovesThatContainAZero() {
            int count = 0;
            for (BoardCoordinate move : mMovesFrom1_1) {
                if (move.x == 0 || move.y == 0) {
                    count++;
                }
            }
            assertEquals(0, count);
        }

        @Test
        public void returns7_4From7_1() {
            List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(7, 1));

            assertTrue(moves.contains(BoardCoordinate.at(7, 4)));
        }
    }
}