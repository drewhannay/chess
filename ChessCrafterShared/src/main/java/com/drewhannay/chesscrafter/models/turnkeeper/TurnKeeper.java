package com.drewhannay.chesscrafter.models.turnkeeper;

public abstract class TurnKeeper {
    public abstract int getTeamIndexForNextTurn();

    public abstract int undo();

    public int getCurrentTeamIndex() {
        return mCurrentTeamIndex;
    }

    protected int mCurrentTeamIndex;
}
