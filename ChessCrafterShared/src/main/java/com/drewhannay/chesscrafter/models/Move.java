package com.drewhannay.chesscrafter.models;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public final class Move {

    public final BoardCoordinate origin;
    public final BoardCoordinate destination;
    public final PieceType promotionType;

    private Move(BoardCoordinate origin, BoardCoordinate destination) {
        Preconditions.checkArgument(origin != null);
        Preconditions.checkArgument(destination != null);

        this.origin = origin;
        this.destination = destination;
        this.promotionType = null;
    }

    static Move from(@NotNull BoardCoordinate origin, @NotNull BoardCoordinate destination) {
        return new Move(origin, destination);
    }
}
