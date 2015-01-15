package com.drewhannay.chesscrafter.models.turnkeeper;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class TurnKeeper {

    private final int[] mTeamIds;
    private final int[] mTurnCounts;
    private final int[] mTurnIncrements;

    private int mActiveTeamIndex;
    private int mMovesMadeCount;

    TurnKeeper(@NotNull int[] teamIds,
               @NotNull int[] turnCounts,
               @NotNull int[] turnIncrements) {
        Preconditions.checkArgument(teamIds.length > 1);
        Preconditions.checkArgument(teamIds.length == turnCounts.length);
        Preconditions.checkArgument(teamIds.length == turnIncrements.length);

        mTeamIds = teamIds;
        mTurnCounts = turnCounts;
        mTurnIncrements = turnIncrements;
        mMovesMadeCount = 0;
    }

    public static TurnKeeper createClassic(@NotNull int... teamIds) {
        int[] turnCounts = new int[teamIds.length];
        int[] turnIncrements = new int[teamIds.length];

        Arrays.fill(turnCounts, 1);
        Arrays.fill(turnIncrements, 0);

        return new TurnKeeper(teamIds, turnCounts, turnIncrements);
    }

    public static TurnKeeper createDifferentTurnCount(@NotNull int[] teamIds, @NotNull int[] turnCounts) {
        int[] turnIncrements = new int[teamIds.length];

        Arrays.fill(turnIncrements, 0);

        return new TurnKeeper(teamIds, turnCounts, turnIncrements);
    }

    public static TurnKeeper createIncreasingTogetherTurnKeeper(@NotNull int[] teamIds, int turnIncrement) {
        int[] turnCounts = new int[teamIds.length];
        int[] turnIncrements = new int[teamIds.length];

        Arrays.fill(turnCounts, 1);
        Arrays.fill(turnIncrements, turnIncrement);

        return new TurnKeeper(teamIds, turnCounts, turnIncrements);
    }

    public static TurnKeeper createIncreasingSeparatelyTurnKeeper(@NotNull int[] teamIds, @NotNull int[] turnIncrements) {
        int[] turnCounts = new int[teamIds.length];

        Arrays.fill(turnCounts, 1);

        return new TurnKeeper(teamIds, turnCounts, turnIncrements);
    }

    public int getActiveTeamId() {
        return mTeamIds[mActiveTeamIndex];
    }

    public void finishTurn() {
        mMovesMadeCount++;
        if (mMovesMadeCount >= mTurnCounts[getActiveTeamIndex()]) {
            mTurnCounts[getActiveTeamIndex()] += mTurnIncrements[getActiveTeamIndex()];
            incrementActiveTeamIndex();
            mMovesMadeCount = 0;
        }
    }

    public void undoFinishTurn() {
        mMovesMadeCount--;
        if (mMovesMadeCount < 0) {
            decrementActiveTeamIndex();
            mTurnCounts[getActiveTeamIndex()] -= mTurnIncrements[getActiveTeamIndex()];
            mMovesMadeCount = mTurnCounts[getActiveTeamIndex()] - 1;
        }
    }

    private int getActiveTeamIndex() {
        return mActiveTeamIndex;
    }

    private void incrementActiveTeamIndex() {
        mActiveTeamIndex = (getActiveTeamIndex() + 1) % mTeamIds.length;
    }

    private void decrementActiveTeamIndex() {
        int newActiveTeamIndex = (getActiveTeamIndex() - 1) % mTeamIds.length;
        // account for Java's handling of negative modulus
        if (newActiveTeamIndex < 0)
            newActiveTeamIndex += mTeamIds.length;
        mActiveTeamIndex = newActiveTeamIndex;
    }
}
