package com.drewhannay.chesscrafter.models;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

@Deprecated
public final class PieceMovements {
    @Deprecated
    public static final int UNLIMITED = Integer.MAX_VALUE;

    private final ImmutableMap<Direction, Integer> mMovements;
    private final ImmutableSet<BidirectionalMovement> mBidirectionalMovements;

    public PieceMovements(@Nullable Map<Direction, Integer> movements,
                          @Nullable Set<BidirectionalMovement> bidirectionalMovements) {
        mMovements = movements != null ? ImmutableMap.copyOf(movements) : ImmutableMap.<Direction, Integer>of();
        mBidirectionalMovements = bidirectionalMovements != null ? ImmutableSet.copyOf(bidirectionalMovements) : ImmutableSet.<BidirectionalMovement>of();
    }

    public int getDistance(Direction direction) {
        return mMovements.containsKey(direction) ? mMovements.get(direction) : 0;
    }

    public ImmutableSet<BidirectionalMovement> getBidirectionalMovements() {
        return mBidirectionalMovements;
    }

    public ImmutableMap<Direction, Integer> getMovements() {
        return mMovements;
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
