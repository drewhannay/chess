package com.drewhannay.chesscrafter.models;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public final class BidirectionalMovement {
    public final int x;
    public final int y;

    private BidirectionalMovement(int x, int y) {
        Preconditions.checkArgument(x > 0);
        Preconditions.checkArgument(y > 0);
        
        this.x = x;
        this.y = y;
    }

    public static BidirectionalMovement with(int x, int y) {
        return new BidirectionalMovement(x, y);
    }

    @Deprecated
    public int getRowDistance() {
        return y;
    }

    @Deprecated
    public int getColumnDistance() {
        return x;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass())
            return false;

        BidirectionalMovement other = (BidirectionalMovement) obj;

        return (Objects.equal(x, other.x) && Objects.equal(y, other.y)) ||
                (Objects.equal(x, other.y) && Objects.equal(y, other.x));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(x, y);
    }

    @Override
    public String toString() {
        return "BidirectionalMovement{x=" + x + ", y=" + y + "}";
    }
}
