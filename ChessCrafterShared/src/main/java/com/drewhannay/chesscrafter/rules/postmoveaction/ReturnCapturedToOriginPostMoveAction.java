package com.drewhannay.chesscrafter.rules.postmoveaction;

import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.Move;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.models.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ReturnCapturedToOriginPostMoveAction implements PostMoveAction {
    @Override
    public void perform(@NotNull Board board, @NotNull Team team, @NotNull Move move, @Nullable Piece capturedPiece) {
        // TODO Auto-generated method stub
        // if (move.getCaptured() == null)
        // return;
        // PieceController toHome = move.getCaptured();
        // // if you captured a piece on it's original square, let capturing
        // work
        // // as normal
        // if (toHome.getOriginalSquare() == toHome.getSquare())
        // return;
        // // otherwise, move the captured piece to it's original square
        // toHome.getLegalDests().clear();
        // toHome.getGuardSquares().clear();
        // toHome.setPinnedBy(null);
        //
        // // if there was a piece on the original square, save it so we can put
        // it
        // // back if we undo
        // move.setRemoved(toHome.getOriginalSquare().getPiece());
        // if (move.getRemoved() != null)
        // (move.getRemoved().isBlack() ? mGame.getBlackTeam() :
        // mGame.getWhiteTeam()).remove(move.getRemoved());
        //
        // // actually set the captured piece on it's home square
        // toHome.getOriginalSquare().setPiece(toHome);
        // toHome.setIsCaptured(false);
    }

    @Override
    public void undo(@NotNull Board board, @NotNull Team team, @NotNull Move lastMove, @NotNull Move opponentsLastMove) {
        // TODO Auto-generated method stub
        // if (move.getCaptured() == null)
        // return;
        // PieceController restore = move.getCaptured();
        // restore.setIsCaptured(false);
        // if (move.getRemoved() != null)
        // {
        // (move.getRemoved().isBlack() ? mGame.getBlackTeam() :
        // mGame.getWhiteTeam()).add(move.getRemoved());
        // move.getRemoved().setIsCaptured(false);
        // }
        // move.getCaptured().getSquare().setPiece(move.getRemoved());
        // move.getDest().setPiece(restore);
    }
}
