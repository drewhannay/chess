package com.drewhannay.chesscrafter.rules.endconditions;

public final class CaptureObjectiveEndCondition extends EndCondition {
    @Override
    public void checkEndCondition() {
        // TODO Auto-generated method stub
        // if (mGame.getLegalMoveCount() == 0 || objectivePiece.isCaptured())
        // {
        // // if the King is threatened, it's check mate.
        // if (objectivePiece == null || objectivePiece.isInCheck() ||
        // objectivePiece.isCaptured())
        // {
        // if (mGame.getLastMove() != null)
        // {
        // mGame.getLastMove().setCheckmate(true);
        // Result result = mGame.isBlackMove() ? Result.WHITE_WIN :
        // Result.BLACK_WIN;
        //					String resultText = Messages.getString("gameOverExc") + result.winText() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
        //
        // if (mGame.getThreats(objectivePiece) != null)
        // {
        //						resultText += Messages.getString("pieceCausedFinalCheck") //$NON-NLS-1$
        //								+ Messages.getString("piecePlacedWasThe") //$NON-NLS-1$
        //								+ mGame.getThreats(objectivePiece)[0].getName() + Messages.getString("atLocation") //$NON-NLS-1$
        //								+ mGame.getThreats(objectivePiece)[0].getSquare().toString(new boolean[] { false, false }) + "\n"; //$NON-NLS-1$
        // }
        //					result.setGuiText(resultText + Messages.getString("whatWouldYouLikeToDo")); //$NON-NLS-1$
        // mGame.getLastMove().setResult(result);
        // if (!mGame.getHistory().contains(mGame.getLastMove()))
        // mGame.getHistory().add(mGame.getLastMove());
        //
        // // let the user see the final move
        // GuiUtility.getChessCrafter().getPlayGameScreen(mGame).boardRefresh(mGame.getBoards());
        // GuiUtility.getChessCrafter().getPlayGameScreen(mGame).endOfGame(result);
        // }
        // }
        // // if the King isn't threatened, then it's stalemate
        // else
        // {
        // if (mGame.getLastMove() != null)
        // {
        // mGame.getLastMove().setStalemate(true);
        // Result result = Result.DRAW;
        //					result.setGuiText(Messages.getString("drawExc") + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
        // mGame.getLastMove().setResult(result);
        // if (!mGame.getHistory().contains(mGame.getLastMove()))
        // {
        // mGame.getHistory().add(mGame.getLastMove());
        // }
        // // let the user see the final move
        // GuiUtility.getChessCrafter().getPlayGameScreen(mGame).boardRefresh(mGame.getBoards());
        // GuiUtility.getChessCrafter().getPlayGameScreen(mGame).endOfGame(result);
        // }
        // }
        // }
    }

    @Override
    public void undo() {
    }
}
