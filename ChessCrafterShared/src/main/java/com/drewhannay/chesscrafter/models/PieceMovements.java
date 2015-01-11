package com.drewhannay.chesscrafter.models;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;

import java.util.Map;
import java.util.Set;

public final class PieceMovements {
    public static final int UNLIMITED = -1;

    public static enum MovementDirection {
        NORTH('n'),
        SOUTH('s'),
        EAST('e'),
        WEST('w'),
        NORTHWEST('f'),
        NORTHEAST('g'),
        SOUTHWEST('a'),
        SOUTHEAST('d');

        private MovementDirection(char direction) {
            mDirection = direction;
        }

        public char getDirectionChar() {
            return mDirection;
        }

        private final char mDirection;
    }

    public PieceMovements(Map<MovementDirection, Integer> movements, Set<BidirectionalMovement> bidirectionalMovements) {
        mMovements = movements;
        mBidirectionalMovements = bidirectionalMovements;
    }

    public int getDistance(MovementDirection direction) {
        return mMovements.containsKey(direction) ? mMovements.get(direction) : 0;
    }

    public ImmutableSet<BidirectionalMovement> getBidirectionalMovements() {
        return ImmutableSet.copyOf(mBidirectionalMovements);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof PieceMovements))
            return false;

        PieceMovements otherMovements = (PieceMovements) other;

        return Objects.equal(mMovements, otherMovements.mMovements)
                && Objects.equal(mBidirectionalMovements, otherMovements.mBidirectionalMovements);
    }

    private final Map<MovementDirection, Integer> mMovements;
    private final Set<BidirectionalMovement> mBidirectionalMovements;
}
