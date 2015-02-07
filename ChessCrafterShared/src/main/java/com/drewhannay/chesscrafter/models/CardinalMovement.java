package com.drewhannay.chesscrafter.models;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public final class CardinalMovement implements Movement {
    public final Direction direction;
    public final int distance;

    private CardinalMovement(Direction direction, int distance) {
        Preconditions.checkArgument(direction != null);
        Preconditions.checkArgument(distance > 0);

        this.direction = direction;
        this.distance = distance;
    }

    public static CardinalMovement with(@NotNull Direction direction, int distance) {
        return new CardinalMovement(direction, distance);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass())
            return false;

        CardinalMovement other = (CardinalMovement) obj;

        return Objects.equal(distance, other.distance) && Objects.equal(direction, other.direction);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(distance, direction);
    }

    @Override
    public String toString() {
        return "CardinalMovement{" + direction + ":" + distance + "}";
    }
}
