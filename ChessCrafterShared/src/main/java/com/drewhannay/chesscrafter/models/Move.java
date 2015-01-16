package com.drewhannay.chesscrafter.models;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public final class Move {

    public final ChessCoordinate origin;
    public final ChessCoordinate destination;
    public final PieceType promotionType;

    private Move(ChessCoordinate origin, ChessCoordinate destination) {
        Preconditions.checkArgument(origin != null);
        Preconditions.checkArgument(destination != null);

        this.origin = origin;
        this.destination = destination;
        this.promotionType = null;
    }

    static Move from(@NotNull ChessCoordinate origin, @NotNull ChessCoordinate destination) {
        return new Move(origin, destination);
    }
}
