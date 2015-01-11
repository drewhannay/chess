package com.drewhannay.chesscrafter.rules.postmoveaction;

import com.drewhannay.chesscrafter.models.Move;

public abstract class PostMoveAction {
    public abstract void perform(Move move);

    public abstract void undo(Move move);
}
