package com.drewhannay.chesscrafter.models;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public final class PieceMovements {
    public static final int UNLIMITED = -1;

    private final ImmutableMap<Direction, Integer> mMovements;
    private final ImmutableSet<BidirectionalMovement> mBidirectionalMovements;

    public PieceMovements(@NotNull Map<Direction, Integer> movements,
                          @NotNull Set<BidirectionalMovement> bidirectionalMovements) {
        mMovements = ImmutableMap.copyOf(movements);
        mBidirectionalMovements = ImmutableSet.copyOf(bidirectionalMovements);
    }

    public int getDistance(Direction direction) {
        return mMovements.containsKey(direction) ? mMovements.get(direction) : 0;
    }

    public ImmutableSet<BidirectionalMovement> getBidirectionalMovements() {
        return mBidirectionalMovements;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof PieceMovements))
            return false;

        PieceMovements otherMovements = (PieceMovements) other;

        return Objects.equal(mMovements, otherMovements.mMovements)
                && Objects.equal(mBidirectionalMovements, otherMovements.mBidirectionalMovements);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(mMovements, mBidirectionalMovements);
    }

    // TODO: Needs toString method
}
