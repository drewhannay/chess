package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.logic.GameBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Board_GetMovesFrom_Given_ClassicChess_Should {

    Board mTarget;

    @Before
    public void setup() {
        mTarget = new Board(BoardSize.CLASSIC_SIZE);

        GameBuilder.setupClassicPieces(mTarget, 1, Piece.TEAM_ONE);
        GameBuilder.setupClassicNorthFacingPawns(mTarget, 2, Piece.TEAM_ONE);

        GameBuilder.setupClassicSouthFacingPawns(mTarget, 7, Piece.TEAM_TWO);
        GameBuilder.setupClassicPieces(mTarget, 8, Piece.TEAM_TWO);
    }

    @Test
    public void returnASetOfMovesForAPawnContainingOneSpaceAhead() {
        Set<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(1, 2));
        assertTrue(moves.contains(BoardCoordinate.at(1, 3)));
    }

    @Test
    public void returnASetOfMovesForAPawnContainingTwoSpacesAhead() {
        Set<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(1, 2));
        assertTrue(moves.contains(BoardCoordinate.at(1, 4)));
    }

    @Test
    public void returnEmptySetForRookAt1_1() {
        Set<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(1, 1));
        assertTrue(moves.isEmpty());
    }

    @Test
    public void returnEmptySetForRookAt8_1() {
        Set<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(8, 1));
        assertTrue(moves.isEmpty());
    }

    @Test
    public void returnEmptySetForRookAt8_8() {
        Set<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(8, 8));
        assertTrue(moves.isEmpty());
    }

    @Test
    public void returnSetWith3_3ForKnightAt2_1() {
        Set<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(2, 1));
        assertTrue(moves.contains(BoardCoordinate.at(3, 3)));
    }

    @Test
    public void returnSetWith1_3ForKnightAt2_1() {
        Set<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(2, 1));
        assertTrue(moves.contains(BoardCoordinate.at(1, 3)));
    }

    @Test
    public void notReturn4_2ForKnightAt2_1() {
        Set<BoardCoordinate> moves = mTarget.getMovesFrom(BoardCoordinate.at(2, 1));
        assertFalse(moves.contains(BoardCoordinate.at(4, 2)));
    }
}
