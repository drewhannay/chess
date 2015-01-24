package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.logic.PieceTypeManager;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PieceType_GetMovesFrom_Given_SouthFacingPawnOnClassicBoard_Should {
    PieceType mTarget;
    BoardSize mBoardSize;
    Set<BoardCoordinate> mMovesFrom2_7;

    @Before
    public void setup() {
        mTarget = PieceTypeManager.getSouthFacingPawnPieceType();
        mBoardSize = BoardSize.withDimensions(8, 8);
        mMovesFrom2_7 = mTarget.getMovesFrom(BoardCoordinate.at(2, 7), mBoardSize, 0);
    }

    @Test
    public void return2_6For2_7() {
        assertTrue(mMovesFrom2_7.contains(BoardCoordinate.at(2, 6)));
    }

    @Test
    public void return2_5For2_7WhenPieceHasNotMoved() {
        assertTrue(mMovesFrom2_7.contains(BoardCoordinate.at(2, 5)));
    }

    @Test
    public void notReturn2_5For2_7WhenPieceHasMoved() {
        Set<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(2, 7), mBoardSize, 1);
        assertFalse(moves.contains(BoardCoordinate.at(2, 5)));
    }
}
