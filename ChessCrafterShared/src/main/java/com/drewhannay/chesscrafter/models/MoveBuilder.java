package com.drewhannay.chesscrafter.models;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public final class MoveBuilder {

    private final ChessCoordinate mOrigin;
    private final ChessCoordinate mDestination;

    public MoveBuilder(@NotNull ChessCoordinate origin, @NotNull ChessCoordinate destination) {
        Preconditions.checkArgument(origin != null);
        Preconditions.checkArgument(destination != null);

        mOrigin = origin;
        mDestination = destination;
    }

    public Move build() {
        return Move.from(mOrigin, mDestination);
    }
}
