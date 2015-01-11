package com.drewhannay.chesscrafter.models.turnkeeper;

import com.google.common.base.Objects;

public final class IncreasingSeparatelyTurnKeeper extends TurnKeeper {
    @Override
    public int getTeamIndexForNextTurn() {
        // TODO Auto-generated method stub
        // if (++mCurrentNumberOfMovesMade >= (mIsBlackMove ?
        // mNumberOfBlackMovesBeforeTurnChange :
        // mNumberOfWhiteMovesBeforeTurnChange))
        // {
        // mIsBlackMove = !mIsBlackMove;
        // GuiUtility.getChessCrafter().getPlayGameScreen(null).turn(mIsBlackMove);
        // mNumberOfBlackMovesBeforeTurnChange += mTurnIncrement;
        // mNumberOfWhiteMovesBeforeTurnChange += mTurnIncrement;
        // mCurrentNumberOfMovesMade = 0;
        // }
        // return mIsBlackMove;
        return 0;
    }

    @Override
    public int undo() {
        // TODO Auto-generated method stub
        // if (--mCurrentNumberOfMovesMade < 0)
        // {
        // mIsBlackMove = !mIsBlackMove;
        // mNumberOfBlackMovesBeforeTurnChange -= mTurnIncrement;
        // mNumberOfWhiteMovesBeforeTurnChange -= mTurnIncrement;
        // GuiUtility.getChessCrafter().getPlayGameScreen(null).turn(mIsBlackMove);
        //
        // mCurrentNumberOfMovesMade = mIsBlackMove ?
        // mNumberOfBlackMovesBeforeTurnChange :
        // mNumberOfWhiteMovesBeforeTurnChange;
        // }
        // return mIsBlackMove;
        return 0;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof IncreasingSeparatelyTurnKeeper && Objects.equal(mCurrentTeamIndex, ((IncreasingSeparatelyTurnKeeper) other).mCurrentTeamIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mCurrentTeamIndex);
    }
}
