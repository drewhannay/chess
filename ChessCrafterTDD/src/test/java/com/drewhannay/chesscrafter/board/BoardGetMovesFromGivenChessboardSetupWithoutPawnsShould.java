package com.drewhannay.chesscrafter.board;

import com.drewhannay.chesscrafter.piece.Pawn;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoardGetMovesFromGivenChessboardSetupWithoutPawnsShould {

    Board mTarget;

    @Before
    public void setup() {
        mTarget = new Board();
        BoardGetMovesFromGivenNormalChessboardSetupShould.setStandardPieces(mTarget, 1, Board.FIRST_TEAM_ID);
        BoardGetMovesFromGivenNormalChessboardSetupShould.setStandardPieces(mTarget, 8, Board.SECOND_TEAM_ID);
    }

    @Test
    public void returnRookMovesContaining1_2() {
        List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(1, 1));

        assertTrue(moves.contains(BoardCoordinate.at(1, 2)));
    }

    @Test
    public void returnRookMovesNotContaining2_1() {
        List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(1, 1));

        assertFalse(moves.contains(BoardCoordinate.at(2, 1)));
    }

    @Test
    public void returnKingMovesContaining5_2() {
        List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(5, 1));

        assertTrue(moves.contains(BoardCoordinate.at(5, 2)));
    }

    @Test
    public void returnEmptyForBishopAfterBlockingPawnsAreAdded() {
        mTarget.addPiece(new Pawn(), BoardCoordinate.at(2, 2));
        mTarget.addPiece(new Pawn(), BoardCoordinate.at(4, 2));

        List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(3, 1));

        assertTrue(moves.isEmpty());
    }

    @Test
    public void returnMovesForBishopThatIsNotBlocked() {
        mTarget.addPiece(new Pawn(), BoardCoordinate.at(2, 2));

        List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(3, 1));

        assertFalse(moves.isEmpty());
    }

    @Test
    public void returnNotEmptyForBishopAt3_8() {
        List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(3, 8));

        assertFalse(moves.isEmpty());
    }

    @Test
    public void returnEmptyForBishopAt3_8WhenBlocked() {
        mTarget.addPiece(new Pawn(Board.SECOND_TEAM_ID), BoardCoordinate.at(2, 7));
        mTarget.addPiece(new Pawn(Board.SECOND_TEAM_ID), BoardCoordinate.at(4, 7));

        List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(3, 8));

        assertTrue(moves.isEmpty());
    }

    @Test
    public void allowRookAt1_1ToMoveToCaptureRookAt1_8() {
        List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(1, 1));

        assertTrue(moves.contains(BoardCoordinate.at(1, 8)));
    }

    @Test
    public void notAllowRookAt1_1ToCaptureRookAt1_8WhenAPawnIsInFrontOfIt() {
        mTarget.addPiece(new Pawn(Board.SECOND_TEAM_ID), BoardCoordinate.at(1, 7));

        List<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(1, 1));

        assertFalse(moves.contains(BoardCoordinate.at(1, 8)));
    }
}