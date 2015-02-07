package com.drewhannay.chesscrafter.models;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public final class TwoHopMovement implements Movement {
    public final int x;
    public final int y;

    private TwoHopMovement(int x, int y) {
        Preconditions.checkArgument(x > 0);
        Preconditions.checkArgument(y > 0);

        this.x = x;
        this.y = y;
    }

    public static TwoHopMovement with(int x, int y) {
        return new TwoHopMovement(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass())
            return false;

        TwoHopMovement other = (TwoHopMovement) obj;

        return (Objects.equal(x, other.x) && Objects.equal(y, other.y)) ||
                (Objects.equal(x, other.y) && Objects.equal(y, other.x));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(x, y);
    }

    @Override
    public String toString() {
        return "TwoHopMovement{x=" + x + ", y=" + y + "}";
    }
}
