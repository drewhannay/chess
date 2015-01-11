package com.drewhannay.chesscrafter.rules.endconditions;

public final class CheckNTimesEndCondition extends EndCondition {

    @Override
    public void checkEndCondition() {
        // TODO Auto-generated method stub
        // if (mGame.getLastMove() != null && mGame.getLastMove().isVerified()
        // && mGame.getLastMove().isCheck()
        // && mGame.getLastMove().getPiece().isBlack() == mIsBlackRuleSet)
        // {
        // if (++mNumberOfChecks == mMaxNumberOfChecks)
        // {
        // Result result = !mIsBlackRuleSet ? Result.WHITE_WIN :
        // Result.BLACK_WIN;
        //				result.setGuiText(Messages.getString("gameOverExcSpace") + result.winText() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
        // GuiUtility.getChessCrafter().getPlayGameScreen(mGame).endOfGame(result);
        // }
        // }
        // mMove = mGame.getLastMove();
    }

    @Override
    public void undo() {
        // TODO Auto-generated method stub
        // if (mMove != null && mMove.isVerified() && mMove.isCheck())
        // mNumberOfChecks--;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof CheckNTimesEndCondition;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
