package com.drewhannay.chesscrafter.rules.postmoveaction;

import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.Move;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.models.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PostMoveAction {
    public abstract void perform(@NotNull Board board, @NotNull Team team, @NotNull Move move, @Nullable Piece capturedPiece);

    public abstract void undo(@NotNull Board board, @NotNull Team team, @NotNull Move lastMove, @Nullable Move opponentsLastMove);

    public static PostMoveAction from(@NotNull String name) {
        switch (name) {
            case CastlingPostMoveAction.NAME:
                return new CastlingPostMoveAction();
            case EnPassantPostMoveAction.NAME:
                return new EnPassantPostMoveAction();
        }

        throw new IllegalArgumentException("Unknown PostMoveAction name:" + name);
    }
}
