package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.logic.GameBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Board_GetMovesFrom_Given_ClassicWithoutPawns_Should {

    Board mTarget;

    @Before
    public void setup() {
        mTarget = new Board(BoardSize.CLASSIC_SIZE);
        GameBuilder.setupClassicPieces(mTarget, 1, 1);
        GameBuilder.setupClassicPieces(mTarget, 8, 2);
    }

    @Test
    public void returnRookMovesContaining1_2() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(1, 1));
        assertTrue(moves.contains(ChessCoordinate.at(1, 2)));
    }

    @Test
    public void returnRookMovesNotContaining2_1() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(1, 1));
        assertFalse(moves.contains(ChessCoordinate.at(2, 1)));
    }

    @Test
    public void returnKingMovesContaining5_2() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(5, 1));
        assertTrue(moves.contains(ChessCoordinate.at(5, 2)));
    }

    @Test
    public void returnEmptyForBishopAfterBlockingPawnsAreAdded() {
        mTarget.addPiece(Piece.newPawn(), ChessCoordinate.at(2, 2));
        mTarget.addPiece(Piece.newPawn(), ChessCoordinate.at(4, 2));

        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(3, 1));
        assertTrue(moves.isEmpty());
    }

    @Test
    public void returnMovesForBishopThatIsNotBlocked() {
        mTarget.addPiece(Piece.newPawn(), ChessCoordinate.at(2, 2));
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(3, 1));
        assertFalse(moves.isEmpty());
    }

    @Test
    public void returnNotEmptyForBishopAt3_8() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(3, 8));
        assertFalse(moves.isEmpty());
    }

    @Test
    public void returnEmptyForBishopAt3_8WhenBlocked() {
        mTarget.addPiece(Piece.newPawn(Piece.TEAM_TWO), ChessCoordinate.at(2, 7));
        mTarget.addPiece(Piece.newPawn(Piece.TEAM_TWO), ChessCoordinate.at(4, 7));

        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(3, 8));
        assertTrue(moves.isEmpty());
    }

    @Test
    public void allowRookAt1_1ToMoveToCaptureRookAt1_8() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(1, 1));
        assertTrue(moves.contains(ChessCoordinate.at(1, 8)));
    }

    @Test
    public void notAllowRookAt1_1ToCaptureRookAt1_8WhenAPawnIsInFrontOfIt() {
        mTarget.addPiece(Piece.newPawn(Piece.TEAM_TWO), ChessCoordinate.at(1, 7));
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(1, 1));
        assertFalse(moves.contains(ChessCoordinate.at(1, 8)));
    }
}
