package com.drewhannay.chesscrafter.models;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public final class MoveBuilder {

    private final BoardCoordinate mOrigin;
    private final BoardCoordinate mDestination;

    public MoveBuilder(@NotNull BoardCoordinate origin, @NotNull BoardCoordinate destination) {
        Preconditions.checkArgument(origin != null);
        Preconditions.checkArgument(destination != null);

        mOrigin = origin;
        mDestination = destination;
    }

    public Move build() {
        return Move.from(mOrigin, mDestination);
    }
}
