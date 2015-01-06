package com.drewhannay.chesscrafter.board;

import com.drewhannay.chesscrafter.piece.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoardGetMovesFromGivenNormalChessboardSetupShould {

    Board mTarget;

    @Before
    public void setup() {
        mTarget = new Board();

        setupStandardPawns(mTarget, 2, true);
        setStandardPieces(mTarget, 1, true);

        setupStandardPawns(mTarget, 7, false);
        setStandardPieces(mTarget, 8, false);
    }

    public static void setupStandardPawns(Board target, int row, boolean isFirstPlayerPiece) {
        for (int x = 1; x <= Board.DEFAULT_BOARD_SIZE; x++)
            target.addPiece(new Pawn(isFirstPlayerPiece), BoardCoordinate.at(x, row));
    }

    public static void setStandardPieces(Board target, int row, boolean isFirstPlayerPiece) {
        target.addPiece(new Rook(isFirstPlayerPiece), BoardCoordinate.at(1, row));
        target.addPiece(new Rook(isFirstPlayerPiece), BoardCoordinate.at(8, row));

        target.addPiece(new Knight(isFirstPlayerPiece), BoardCoordinate.at(2, row));
        target.addPiece(new Knight(isFirstPlayerPiece), BoardCoordinate.at(7, row));

        target.addPiece(new Bishop(isFirstPlayerPiece), BoardCoordinate.at(3, row));
        target.addPiece(new Bishop(isFirstPlayerPiece), BoardCoordinate.at(6, row));

        target.addPiece(new Queen(isFirstPlayerPiece), BoardCoordinate.at(4, row));
        target.addPiece(new King(isFirstPlayerPiece), BoardCoordinate.at(5, row));
    }

    @Test
    public void returnASetOfMovesForAPawnContainingOneSpaceAhead() {
        List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(1, 2));

        assertTrue(moves.contains(BoardCoordinate.at(1, 3)));
    }

    @Test
    public void returnASetOfMovesForAPawnContainingTwoSpacesAhead() {
        List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(1, 2));

        assertTrue(moves.contains(BoardCoordinate.at(1, 4)));
    }

    @Test
    public void returnEmptySetForRookAt1_1() {
        List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(1, 1));

        assertTrue(moves.isEmpty());
    }

    @Test
    public void returnEmptySetForRookAt8_1() {
        List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(8, 1));

        assertTrue(moves.isEmpty());
    }

    @Test
    public void returnEmptySetForRookAt8_8() {
        List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(8, 8));

        assertTrue(moves.isEmpty());
    }

    @Test
    public void returnSetWith3_3ForKnightAt2_1() {
        List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(2, 1));

        assertTrue(moves.contains(BoardCoordinate.at(3, 3)));
    }

    @Test
    public void returnSetWith1_3ForKnightAt2_1() {
        List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(2, 1));

        assertTrue(moves.contains(BoardCoordinate.at(1, 3)));
    }

    @Test
    public void notReturn4_2ForKnightAt2_1() {
        List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(2, 1));

        assertFalse(moves.contains(BoardCoordinate.at(4, 2)));
    }
}