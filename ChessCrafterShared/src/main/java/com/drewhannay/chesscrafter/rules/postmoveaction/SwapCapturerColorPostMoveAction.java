package com.drewhannay.chesscrafter.rules.postmoveaction;

import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.Move;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.models.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class SwapCapturerColorPostMoveAction extends PostMoveAction {
    @Override
    public void perform(@NotNull Board board, @NotNull Team team, @NotNull Move move, @Nullable Piece capturedPiece) {
        // TODO Auto-generated method stub
        swapColorOfCapturingPiece(move);
    }

    @Override
    public void undo(@NotNull Board board, @NotNull Team team, @NotNull Move lastMove, @NotNull Move opponentsLastMove) {
        // TODO Auto-generated method stub
        swapColorOfCapturingPiece(lastMove);
    }

    private void swapColorOfCapturingPiece(Move move) {
        // if (move.getCaptured() == null)
        // return;
        // PieceController toSwap = move.getPiece();
        // toSwap.getLegalDests().clear();
        // toSwap.getGuardSquares().clear();
        // toSwap.setPinnedBy(null);
        // (toSwap.isBlack() ? mGame.getBlackTeam() :
        // mGame.getWhiteTeam()).remove(toSwap);
        // (!toSwap.isBlack() ? mGame.getBlackTeam() :
        // mGame.getWhiteTeam()).add(toSwap);
        // toSwap.setBlack(!toSwap.isBlack());
    }
}
