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

        setupStandardPawns(mTarget, 2, Board.FIRST_TEAM_ID);
        setStandardPieces(mTarget, 1, Board.FIRST_TEAM_ID);

        setupStandardPawns(mTarget, 7, Board.SECOND_TEAM_ID);
        setStandardPieces(mTarget, 8, Board.SECOND_TEAM_ID);
    }

    public static void setupStandardPawns(Board target, int row, int teamId) {
        for (int x = 1; x <= Board.DEFAULT_BOARD_SIZE; x++)
            target.addPiece(new Pawn(teamId), BoardCoordinate.at(x, row));
    }

    public static void setStandardPieces(Board target, int row, int teamId) {
        target.addPiece(new Rook(teamId), BoardCoordinate.at(1, row));
        target.addPiece(new Rook(teamId), BoardCoordinate.at(8, row));

        target.addPiece(new Knight(teamId), BoardCoordinate.at(2, row));
        target.addPiece(new Knight(teamId), BoardCoordinate.at(7, row));

        target.addPiece(new Bishop(teamId), BoardCoordinate.at(3, row));
        target.addPiece(new Bishop(teamId), BoardCoordinate.at(6, row));

        target.addPiece(new Queen(teamId), BoardCoordinate.at(4, row));
        target.addPiece(new King(teamId), BoardCoordinate.at(5, row));
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