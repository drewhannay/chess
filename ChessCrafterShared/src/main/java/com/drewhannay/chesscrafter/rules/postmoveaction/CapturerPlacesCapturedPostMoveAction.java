package com.drewhannay.chesscrafter.rules.postmoveaction;

import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.Move;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.models.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CapturerPlacesCapturedPostMoveAction implements PostMoveAction {
    @Override
    public void perform(@NotNull Board board, @NotNull Team team, @NotNull Move move, @Nullable Piece capturedPiece) {
        // TODO Auto-generated method stub
        // if (move.getCaptured() == null)
        // return;
        // PieceController toPlace = move.getCaptured();
        // toPlace.getLegalDests().clear();
        // toPlace.getGuardSquares().clear();
        // toPlace.setPinnedBy(null);
        //
        // move.setOldPos(toPlace.getSquare());
        // PieceController objectivePiece = toPlace.isBlack() ?
        // mGame.getBlackRules().objectivePiece(toPlace.isBlack()) : mGame
        // .getWhiteRules().objectivePiece(toPlace.isBlack());
        // if (move.isVerified() && !(objectivePiece == toPlace))
        // {
        // toPlace.setIsCaptured(false);
        //			JOptionPane.showMessageDialog(null, Messages.getString("youCapturedPiece")); //$NON-NLS-1$
        // GuiUtility.getChessCrafter().getPlayGameScreen(mGame).setNextMoveMustPlacePiece(true);
        // GuiUtility.getChessCrafter().getPlayGameScreen(mGame).setPieceToPlace(toPlace);
        // }
    }

    @Override
    public void undo(@NotNull Board board, @NotNull Team team, @NotNull Move lastMove, @NotNull Move opponentsLastMove) {
        // TODO Auto-generated method stub
        // if (move.getOldPos() == null)
        // return;
        //
        // PieceController toPlace = move.getCaptured();
        // toPlace.getLegalDests().clear();
        // toPlace.getGuardSquares().clear();
        // toPlace.setPinnedBy(null);
        // toPlace.setIsCaptured(false);
        // toPlace.getSquare().setPiece(null);
        // toPlace.setSquare(move.getOldPos());
        // move.getOldPos().setPiece(toPlace);
        // toPlace.setIsCaptured(false);
        // GuiUtility.getChessCrafter().getPlayGameScreen(mGame).setNextMoveMustPlacePiece(false);
        // GuiUtility.getChessCrafter().getPlayGameScreen(mGame).setPieceToPlace(null);
        // move.setOldPos(null);
    }
}
