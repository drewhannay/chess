package com.drewhannay.chesscrafter.models;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class Move {

    public final BoardCoordinate origin;
    public final BoardCoordinate destination;
    public final String promotionType;

    private Move(BoardCoordinate origin, BoardCoordinate destination, String promotionType) {
        Preconditions.checkArgument(origin != null);
        Preconditions.checkArgument(destination != null);

        this.origin = origin;
        this.destination = destination;
        this.promotionType = promotionType;
    }

    static Move from(@NotNull BoardCoordinate origin, @NotNull BoardCoordinate destination,
                     @Nullable String promotionType) {
        return new Move(origin, destination, promotionType);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        Move other = (Move) obj;
        return Objects.equals(origin, other.origin)
                && Objects.equals(destination, other.destination)
                && Objects.equals(promotionType, other.promotionType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, destination, promotionType);
    }
}
