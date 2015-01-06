package com.drewhannay.chesscrafter.piece;

import com.drewhannay.chesscrafter.board.BoardCoordinate;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PawnTest {

    Pawn mTarget;
    List<BoardCoordinate> mMovesFrom2_2;

    @Before
    public void setup() {
        mTarget = new Pawn();
        mMovesFrom2_2 = mTarget.getMovesFrom(BoardCoordinate.at(2, 2));
    }

    public static class GetMovesFrom extends PawnTest {
        @Test
        public void returns2_3AsOneResultWhenPassed2_2() {
            assertTrue(mMovesFrom2_2.contains(BoardCoordinate.at(2, 3)));
        }

        @Test
        public void returns2_4AsOneResultWhenPassed2_2WhenPieceHasNotMoved() {
            assertTrue(mMovesFrom2_2.contains(BoardCoordinate.at(2, 4)));
        }

        @Test
        public void doesNotReturns2_4WhenPassed2_2IfPieceHasAlreadyMoved() {
            mTarget.setHasMoved(true);
            mMovesFrom2_2 = mTarget.getMovesFrom(BoardCoordinate.at(2, 2));
            assertFalse(mMovesFrom2_2.contains(BoardCoordinate.at(2, 4)));
        }
    }
}
