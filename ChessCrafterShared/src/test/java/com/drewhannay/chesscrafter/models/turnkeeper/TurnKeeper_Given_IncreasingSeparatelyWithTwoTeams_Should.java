package com.drewhannay.chesscrafter.models.turnkeeper;

import com.drewhannay.chesscrafter.models.Piece;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TurnKeeper_Given_IncreasingSeparatelyWithTwoTeams_Should {
    TurnKeeper mTarget;

    private void finishTurns(int numberOfTurns) {
        while (numberOfTurns > 0) {
            mTarget.finishTurn();
            numberOfTurns--;
        }
    }

    private void undoFinishTurns(int numberOfTurns) {
        while (numberOfTurns > 0) {
            mTarget.undoFinishTurn();
            numberOfTurns--;
        }
    }

    @Before
    public void setup() {
        int[] teamIds = new int[]{Piece.TEAM_ONE, Piece.TEAM_TWO};
        int[] turnIncrements = new int[]{2, 3};
        mTarget = TurnKeeper.createIncreasingSeparatelyTurnKeeper(teamIds, turnIncrements);
        // Expected order:
        // 1, 2, 1, 1, 1, 2, 2, 2, 2, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2
    }

    @Test
    public void returnTeamOneAsFirstActiveTeam() {
        assertEquals(Piece.TEAM_ONE, mTarget.getActiveTeamId());
    }

    @Test
    public void returnTeamTwoAfter1Turn() {
        finishTurns(1);
        assertEquals(Piece.TEAM_TWO, mTarget.getActiveTeamId());
    }

    @Test
    public void returnTeamOneAfter2Turns() {
        finishTurns(2);
        assertEquals(Piece.TEAM_ONE, mTarget.getActiveTeamId());
    }

    @Test
    public void returnTeamOneAfter3Turns() {
        finishTurns(3);
        assertEquals(Piece.TEAM_ONE, mTarget.getActiveTeamId());
    }

    @Test
    public void returnTeamOneAfter4Turns() {
        finishTurns(4);
        assertEquals(Piece.TEAM_ONE, mTarget.getActiveTeamId());
    }

    @Test
    public void returnTeamTwoAfter5Turns() {
        finishTurns(5);
        assertEquals(Piece.TEAM_TWO, mTarget.getActiveTeamId());
    }

    @Test
    public void returnTeamTwoAfter14Turns() {
        finishTurns(14);
        assertEquals(Piece.TEAM_TWO, mTarget.getActiveTeamId());
    }

    @Test
    public void returnTeamOneAfter13TurnsAnd1Undo() {
        finishTurns(14);
        undoFinishTurns(1);
        assertEquals(Piece.TEAM_ONE, mTarget.getActiveTeamId());
    }

    @Test
    public void returnTeamOneAfter1TurnAnd1Undo() {
        finishTurns(1);
        undoFinishTurns(1);
        assertEquals(Piece.TEAM_ONE, mTarget.getActiveTeamId());
    }

    @Test
    public void returnTeamTwoAfter2TurnsAnd1Undo() {
        finishTurns(2);
        undoFinishTurns(1);
        assertEquals(Piece.TEAM_TWO, mTarget.getActiveTeamId());
    }
}
