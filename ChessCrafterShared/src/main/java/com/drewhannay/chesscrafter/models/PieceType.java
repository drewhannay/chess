package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.logic.PathMaker;
import com.drewhannay.chesscrafter.logic.PieceTypeManager;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class PieceType {
    public static final int UNLIMITED = Integer.MAX_VALUE;

    private final String mInternalId;
    private final String mName;
    private final Set<CardinalMovement> mMovements;
    private final Set<CardinalMovement> mCapturingMovements;
    private final Set<TwoHopMovement> mTwoHopMovements;

    public PieceType(@NotNull String internalId, @NotNull String name, @Nullable Set<CardinalMovement> movements,
                     @Nullable Set<TwoHopMovement> twoHopMovements) {
        this(internalId, name, movements, movements, twoHopMovements);
    }

    public PieceType(@NotNull String internalId, @NotNull String name, @Nullable Set<CardinalMovement> movements,
                     @Nullable Set<CardinalMovement> capturingMovements, @Nullable Set<TwoHopMovement> twoHopMovements) {
        Preconditions.checkArgument(!internalId.isEmpty());
        Preconditions.checkArgument(!name.isEmpty());

        mInternalId = internalId;
        mName = name;
        mMovements = movements != null ? ImmutableSet.copyOf(movements) : ImmutableSet.<CardinalMovement>of();
        mCapturingMovements = capturingMovements != null ? ImmutableSet.copyOf(capturingMovements) : ImmutableSet.<CardinalMovement>of();
        mTwoHopMovements = twoHopMovements != null ? ImmutableSet.copyOf(twoHopMovements) : ImmutableSet.<TwoHopMovement>of();
    }

    @NotNull
    public String getInternalId() {
        return mInternalId;
    }

    @NotNull
    public String getName() {
        return mName;
    }

    @NotNull
    public Set<CardinalMovement> getMovements() {
        return ImmutableSet.copyOf(mMovements);
    }

    @NotNull
    public Set<CardinalMovement> getCapturingMovements() {
        return ImmutableSet.copyOf(mCapturingMovements);
    }

    @NotNull
    public Set<TwoHopMovement> getTwoHopMovements() {
        return ImmutableSet.copyOf(mTwoHopMovements);
    }

    public Set<BoardCoordinate> getMovesFrom(@NotNull BoardCoordinate startLocation,
                                             @NotNull BoardSize boardSize, int moveCount) {
        Set<BoardCoordinate> moves = getMovesFromImpl(startLocation, boardSize, mMovements);

        if (moveCount == 0) {
            if (getInternalId().equals(PieceTypeManager.NORTH_FACING_PAWN_ID)) {
                moves.add(BoardCoordinate.at(startLocation.x, startLocation.y + 2));
            } else if (getInternalId().equals(PieceTypeManager.SOUTH_FACING_PAWN_ID)) {
                moves.add(BoardCoordinate.at(startLocation.x, startLocation.y - 2));
            }
        }

        return moves;
    }

    public Set<BoardCoordinate> getCapturingMovesFrom(@NotNull BoardCoordinate startLocation,
                                                      @NotNull BoardSize boardSize) {
        return getMovesFromImpl(startLocation, boardSize, mCapturingMovements);
    }

    private Set<BoardCoordinate> getMovesFromImpl(@NotNull BoardCoordinate startLocation,
                                                  @NotNull BoardSize boardSize,
                                                  @NotNull Set<CardinalMovement> movements) {
        Set<BoardCoordinate> moves = new HashSet<>();

        for (CardinalMovement movement : movements) {
            PathMaker pathMaker = new PathMaker(startLocation, movement.direction.getFurthestPoint(startLocation, boardSize));
            moves.addAll(pathMaker.getPathToDestination(movement.distance));
        }

        for (TwoHopMovement twoHopMovement : mTwoHopMovements) {
            Set<BoardCoordinate> allMoves = new HashSet<>();

            for (int quadrant = 1; quadrant <= 4; quadrant++) {
                allMoves.addAll(getQuadrantMoves(startLocation, twoHopMovement, quadrant));
            }

            for (BoardCoordinate move : allMoves) {
                if (move.isValid(boardSize)) {
                    moves.add(move);
                }
            }
        }

        return moves;
    }

    private Set<BoardCoordinate> getQuadrantMoves(@NotNull BoardCoordinate coordinate,
                                                  @NotNull TwoHopMovement movement,
                                                  int quadrant) {
        int xMultiplier = quadrant == 1 || quadrant == 4 ? 1 : -1;
        int yMultiplier = quadrant == 1 || quadrant == 2 ? 1 : -1;

        Set<BoardCoordinate> moves = new HashSet<>(2);
        moves.add(BoardCoordinate.at(coordinate.x + movement.x * xMultiplier, coordinate.y + movement.y * yMultiplier));
        moves.add(BoardCoordinate.at(coordinate.x + movement.y * xMultiplier, coordinate.y + movement.x * yMultiplier));
        return moves;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass())
            return false;

        PieceType other = (PieceType) obj;
        boolean equal = Objects.equal(mInternalId, other.mInternalId);
        if (equal) {
            // do not allow PieceTypes with the same name but different attributes
            Preconditions.checkState(Objects.equal(mName, other.mName));
            Preconditions.checkState(Objects.equal(mMovements, other.mMovements));
            Preconditions.checkState(Objects.equal(mCapturingMovements, other.mCapturingMovements));
            Preconditions.checkState(Objects.equal(mTwoHopMovements, other.mTwoHopMovements));
        }

        return equal;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mInternalId, mName, mMovements, mCapturingMovements, mTwoHopMovements);
    }

    @Override
    public String toString() {
        return mInternalId;
    }
}
