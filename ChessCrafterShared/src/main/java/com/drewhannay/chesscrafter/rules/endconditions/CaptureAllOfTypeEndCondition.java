package com.drewhannay.chesscrafter.rules.endconditions;

import com.drewhannay.chesscrafter.logic.Status;
import com.drewhannay.chesscrafter.models.Game;
import org.jetbrains.annotations.NotNull;

public final class CaptureAllOfTypeEndCondition extends EndCondition {
    @Override
    public Status checkEndCondition(@NotNull Game game) {
        // TODO Auto-generated method stub
        // List<PieceController> team = (!mIsBlackRuleSet ? mGame.getBlackTeam()
        // : mGame.getWhiteTeam());
        // for (PieceController piece : team)
        // {
        // if (piece.getName().equals(mPieceName) && !piece.isCaptured())
        // return;
        // }
        // Result result = mIsBlackRuleSet ? Result.BLACK_WIN :
        // Result.WHITE_WIN;
        //		result.setGuiText(Messages.getString("gameOverExcSpace") + result.winText() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
        // GuiUtility.getChessCrafter().getPlayGameScreen(mGame).endOfGame(result);
        return null;
    }

    @Override
    public void undo() {
    }
}
