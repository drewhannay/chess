package com.drewhannay.chesscrafter.models.turnkeeper;

import com.drewhannay.chesscrafter.models.Piece;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TurnKeeper_Given_ClassicWithTwoTeams_Should {

    TurnKeeper mTarget;

    @Before
    public void setup() {
        mTarget = TurnKeeper.createClassic(Piece.TEAM_ONE, Piece.TEAM_TWO);
        // Expected order:
        // 1, 2, 1, 2, 1, 2, 1, 2
    }

    @Test
    public void returnTeamOneAsFirstActiveTeam() {
        assertEquals(Piece.TEAM_ONE, mTarget.getActiveTeamId());
    }

    @Test
    public void returnTeamTwoAfterOneTurnPasses() {
        mTarget.finishTurn();
        assertEquals(Piece.TEAM_TWO, mTarget.getActiveTeamId());
    }

    @Test
    public void returnTeamOneAfterTwoTurnsPass() {
        mTarget.finishTurn();
        mTarget.finishTurn();
        assertEquals(Piece.TEAM_ONE, mTarget.getActiveTeamId());
    }

    @Test
    public void returnTeamOneAfterUndoingOneTurn() {
        mTarget.finishTurn();
        mTarget.undoFinishTurn();
        assertEquals(Piece.TEAM_ONE, mTarget.getActiveTeamId());
    }
}
