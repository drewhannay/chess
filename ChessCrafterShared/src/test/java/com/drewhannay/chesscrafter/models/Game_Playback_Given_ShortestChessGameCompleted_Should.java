package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.logic.GameBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Game_Playback_Given_ShortestChessGameCompleted_Should {

    Game mTarget;

    @Before
    public void setUp() {
        mTarget = GameBuilder.buildGame(GameBuilder.getClassicConfiguration());
        MoveBuilder moveBuilder = mTarget.newMoveBuilder(BoardCoordinate.at(6, 2), BoardCoordinate.at(6, 3));
        mTarget.executeMove(moveBuilder.build());
        moveBuilder = mTarget.newMoveBuilder(BoardCoordinate.at(5, 7), BoardCoordinate.at(5, 5));
        mTarget.executeMove(moveBuilder.build());
        moveBuilder = mTarget.newMoveBuilder(BoardCoordinate.at(7, 2), BoardCoordinate.at(7, 4));
        mTarget.executeMove(moveBuilder.build());
        moveBuilder = mTarget.newMoveBuilder(BoardCoordinate.at(4, 8), BoardCoordinate.at(8, 4));
        mTarget.executeMove(moveBuilder.build());
    }

    @Test
    public void cannotUndoMove() {
        assertFalse(mTarget.canUndoMove());
    }

    @Test
    public void hasPreviousMove() {
        assertTrue(mTarget.hasPreviousMove());
    }

    @Test
    public void doesNotHaveNextMove() {
        assertFalse(mTarget.hasNextMove());
    }

    @Test
    public void hasNextMoveAfter1StepBack() {
        mTarget.previousMove();
        assertTrue(mTarget.hasNextMove());
    }

    @Test
    public void doesNotHaveNextMoveAfter1StepBackAnd1StepForward() {
        mTarget.previousMove();
        mTarget.nextMove();
        assertFalse(mTarget.hasNextMove());
    }

    @Test
    public void doesNotHavePreviousMoveAfter4StepsBack() {
        mTarget.previousMove();
        mTarget.previousMove();
        mTarget.previousMove();
        mTarget.previousMove();
        assertFalse(mTarget.hasPreviousMove());
    }
}
