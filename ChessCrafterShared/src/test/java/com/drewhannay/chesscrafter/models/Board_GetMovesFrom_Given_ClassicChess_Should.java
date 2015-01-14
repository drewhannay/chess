package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.logic.GameBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Board_GetMovesFrom_Given_ClassicChess_Should {

    Board mTarget;

    @Before
    public void setup() {
        mTarget = new Board(BoardSize.CLASSIC_SIZE);

        GameBuilder.setupClassicPieces(mTarget, 1, Piece.TEAM_ONE);
        GameBuilder.setupClassicPawns(mTarget, 2, Piece.TEAM_ONE);

        GameBuilder.setupClassicPawns(mTarget, 7, Piece.TEAM_TWO);
        GameBuilder.setupClassicPieces(mTarget, 8, Piece.TEAM_TWO);
    }

    @Test
    public void returnASetOfMovesForAPawnContainingOneSpaceAhead() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(1, 2));
        assertTrue(moves.contains(ChessCoordinate.at(1, 3)));
    }

    @Test
    public void returnASetOfMovesForAPawnContainingTwoSpacesAhead() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(1, 2));
        assertTrue(moves.contains(ChessCoordinate.at(1, 4)));
    }

    @Test
    public void returnEmptySetForRookAt1_1() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(1, 1));
        assertTrue(moves.isEmpty());
    }

    @Test
    public void returnEmptySetForRookAt8_1() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(8, 1));
        assertTrue(moves.isEmpty());
    }

    @Test
    public void returnEmptySetForRookAt8_8() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(8, 8));
        assertTrue(moves.isEmpty());
    }

    @Test
    public void returnSetWith3_3ForKnightAt2_1() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(2, 1));
        assertTrue(moves.contains(ChessCoordinate.at(3, 3)));
    }

    @Test
    public void returnSetWith1_3ForKnightAt2_1() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(2, 1));
        assertTrue(moves.contains(ChessCoordinate.at(1, 3)));
    }

    @Test
    public void notReturn4_2ForKnightAt2_1() {
        List<ChessCoordinate> moves = mTarget.getMovesFrom(ChessCoordinate.at(2, 1));
        assertFalse(moves.contains(ChessCoordinate.at(4, 2)));
    }
}
