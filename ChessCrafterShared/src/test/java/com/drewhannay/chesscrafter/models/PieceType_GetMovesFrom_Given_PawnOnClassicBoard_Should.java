package com.drewhannay.chesscrafter.models;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PieceType_GetMovesFrom_Given_PawnOnClassicBoard_Should {

    PieceType mTarget;
    BoardSize mBoardSize;
    Set<ChessCoordinate> mMovesFrom2_2;

    @Before
    public void setup() {
        mTarget = PieceType.getPawnPieceType();
        mBoardSize = BoardSize.withDimensions(8, 8);
        mMovesFrom2_2 = mTarget.getMovesFrom(ChessCoordinate.at(2, 2), mBoardSize, 0);
    }

    @Test
    public void return2_3For2_2() {
        assertTrue(mMovesFrom2_2.contains(ChessCoordinate.at(2, 3)));
    }

    @Test
    public void return2_4For2_2WhenPieceHasNotMoved() {
        assertTrue(mMovesFrom2_2.contains(ChessCoordinate.at(2, 4)));
    }

    @Test
    public void notReturn2_5For2_3WhenPieceHasMoved() {
        Set<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(2, 3), mBoardSize, 1);
        assertFalse(moves.contains(ChessCoordinate.at(2, 3)));
    }
}
