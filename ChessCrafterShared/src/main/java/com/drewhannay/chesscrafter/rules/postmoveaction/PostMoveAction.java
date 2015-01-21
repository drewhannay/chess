package com.drewhannay.chesscrafter.rules.postmoveaction;

import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.Move;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.models.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PostMoveAction {
    public void perform(@NotNull Board board, @NotNull Team team, @NotNull Move move, @Nullable Piece capturedPiece);

    public void undo(@NotNull Board board, @NotNull Team team, @NotNull Move lastMove, @Nullable Move opponentsLastMove);
}
