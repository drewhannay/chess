package com.drewhannay.chesscrafter.models;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Move {

    public final BoardCoordinate origin;
    public final BoardCoordinate destination;
    public final PieceType promotionType;

    private Move(BoardCoordinate origin, BoardCoordinate destination, PieceType promotionType) {
        Preconditions.checkArgument(origin != null);
        Preconditions.checkArgument(destination != null);

        this.origin = origin;
        this.destination = destination;
        this.promotionType = promotionType;
    }

    static Move from(@NotNull BoardCoordinate origin, @NotNull BoardCoordinate destination,
                     @Nullable PieceType promotionType) {
        return new Move(origin, destination, promotionType);
    }
}
