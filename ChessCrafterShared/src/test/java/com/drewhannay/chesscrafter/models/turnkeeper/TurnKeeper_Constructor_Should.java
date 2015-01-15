package com.drewhannay.chesscrafter.models.turnkeeper;

import com.drewhannay.chesscrafter.models.Piece;
import org.junit.Before;
import org.junit.Test;

public class TurnKeeper_Constructor_Should {

    int[] mTeamIds;
    int[] mTurnCounts;
    int[] mTurnIncrements;

    @Before
    public void setup() {
        mTeamIds = new int[]{Piece.TEAM_ONE, Piece.TEAM_TWO};
        mTurnCounts = new int[]{1, 1};
        mTurnIncrements = new int[]{0, 0};
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionWhenPassedZeroTeams() {
        int[] invalidTeamIds = new int[0];
        new TurnKeeper(invalidTeamIds, mTurnCounts, mTurnIncrements);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionWhenPassedOneTeam() {
        int[] invalidTeamIds = new int[]{Piece.TEAM_ONE};
        new TurnKeeper(invalidTeamIds, mTurnCounts, mTurnIncrements);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionWhenPassedInvalidTurnCounts() {
        int[] invalidTurnCounts = new int[]{1};
        new TurnKeeper(mTeamIds, invalidTurnCounts, mTurnIncrements);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExeceptionWhenPassedInvalidTurnIncrements() {
        int[] invalidTurnIncrements = new int[]{0};
        new TurnKeeper(mTeamIds, mTurnCounts, invalidTurnIncrements);
    }
}
