package com.drewhannay.chesscrafter.rules.postmoveaction;

import com.drewhannay.chesscrafter.models.Move;

public final class CapturerStealsCapturedPostMoveAction extends PostMoveAction {
    @Override
    public void perform(Move move) {
        // TODO Auto-generated method stub
        // if (move.getCaptured() == null)
        // return;
        // PieceController toPlace = move.getCaptured();
        // toPlace.getLegalDests().clear();
        // toPlace.getGuardSquares().clear();
        // toPlace.setPinnedBy(null);
        // (toPlace.isBlack() ? mGame.getBlackTeam() :
        // mGame.getWhiteTeam()).remove(toPlace);
        // (!toPlace.isBlack() ? mGame.getBlackTeam() :
        // mGame.getWhiteTeam()).add(toPlace);
        // toPlace.setBlack(!toPlace.isBlack());
        // toPlace.setIsCaptured(false);
        // move.setOldPos(toPlace.getSquare());
        // if (move.isVerified())
        // {
        //			JOptionPane.showMessageDialog(null, Messages.getString("pieceIsOnYourSide")); //$NON-NLS-1$
        // GuiUtility.getChessCrafter().getPlayGameScreen(mGame).setNextMoveMustPlacePiece(true);
        // GuiUtility.getChessCrafter().getPlayGameScreen(mGame).setPieceToPlace(toPlace);
        // }
    }

    @Override
    public void undo(Move move) {
        // TODO Auto-generated method stub
        // if (move.getOldPos() == null)
        // return;
        //
        // PieceController toPlace = move.getCaptured();
        // toPlace.getLegalDests().clear();
        // toPlace.getGuardSquares().clear();
        // toPlace.setPinnedBy(null);
        // (toPlace.isBlack() ? mGame.getBlackTeam() :
        // mGame.getWhiteTeam()).remove(toPlace);
        // (!toPlace.isBlack() ? mGame.getBlackTeam() :
        // mGame.getWhiteTeam()).add(toPlace);
        // toPlace.setBlack(!toPlace.isBlack());
        // toPlace.setIsCaptured(false);
        // toPlace.getSquare().setPiece(null);
        // toPlace.setSquare(move.getOldPos());
        // move.getOldPos().setPiece(toPlace);
        // toPlace.setIsCaptured(false);
        // GuiUtility.getChessCrafter().getPlayGameScreen(mGame).setNextMoveMustPlacePiece(false);
        // GuiUtility.getChessCrafter().getPlayGameScreen(mGame).setPieceToPlace(null);
        // move.setOldPos(null);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof CapturerStealsCapturedPostMoveAction;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
