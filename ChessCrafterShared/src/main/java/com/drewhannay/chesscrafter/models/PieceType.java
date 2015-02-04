package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.logic.PathMaker;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PieceType {
    public static final int UNLIMITED = Integer.MAX_VALUE;

    private final String mName;
    private final Map<Direction, Integer> mMovements;
    private final Map<Direction, Integer> mCapturingMovements;
    private final Set<TwoHopMovement> mTwoHopMovements;

    public PieceType(@NotNull String name, @Nullable Map<Direction, Integer> movements,
                     @Nullable Set<TwoHopMovement> twoHopMovements) {
        this(name, movements, movements, twoHopMovements);
    }

    public PieceType(@NotNull String name, @Nullable Map<Direction, Integer> movements,
                     @Nullable Map<Direction, Integer> capturingMovements,
                     @Nullable Set<TwoHopMovement> twoHopMovements) {
        Preconditions.checkArgument(!name.isEmpty());

        mName = name;
        mMovements = movements != null ? ImmutableMap.copyOf(movements) : ImmutableMap.<Direction, Integer>of();
        mCapturingMovements = capturingMovements != null ? ImmutableMap.copyOf(capturingMovements) : ImmutableMap.<Direction, Integer>of();
        mTwoHopMovements = twoHopMovements != null ? ImmutableSet.copyOf(twoHopMovements) : ImmutableSet.<TwoHopMovement>of();
    }

    /**
     * NOTE: this value should NEVER be displayed to the user!
     * Clients are responsible for maintaining a mapping between PieceTypes and user-facing names
     *
     * @return a unique identifier for this PieceType
     */
    public String getName() {
        return mName;
    }

    @NotNull
    public Map<Direction, Integer> getMovements() {
        return ImmutableMap.copyOf(mMovements);
    }

    @NotNull
    public Set<TwoHopMovement> getTwoHopMovements() {
        return ImmutableSet.copyOf(mTwoHopMovements);
    }

    public Set<BoardCoordinate> getMovesFrom(@NotNull BoardCoordinate startLocation,
                                             @NotNull BoardSize boardSize, int moveCount) {
        Set<BoardCoordinate> moves = getMovesFromImpl(startLocation, boardSize, mMovements);

        if (moveCount == 0) {
            if (getName().equals("NorthFacingPawn")) {
                moves.add(BoardCoordinate.at(startLocation.x, startLocation.y + 2));
            } else if (getName().equals("SouthFacingPawn")) {
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
                                                  @NotNull Map<Direction, Integer> movements) {
        Set<BoardCoordinate> moves = new HashSet<>();

        for (Direction direction : movements.keySet()) {
            PathMaker pathMaker = new PathMaker(startLocation, direction.getFurthestPoint(startLocation, boardSize));
            moves.addAll(pathMaker.getPathToDestination(movements.get(direction)));
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
        boolean equal = Objects.equal(mName, other.mName);
        if (equal) {
            // do not allow PieceTypes with the same name but different movement attributes
            Preconditions.checkState(Objects.equal(mMovements, other.mMovements));
            Preconditions.checkState(Objects.equal(mCapturingMovements, other.mCapturingMovements));
            Preconditions.checkState(Objects.equal(mTwoHopMovements, other.mTwoHopMovements));
        }

        return equal;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mName, mMovements, mCapturingMovements, mTwoHopMovements);
    }

    @Override
    public String toString() {
        return mName;
    }
}
