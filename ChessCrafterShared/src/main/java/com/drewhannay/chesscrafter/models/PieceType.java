package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.logic.PathMaker;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PieceType {
    public static final int UNLIMITED = Integer.MAX_VALUE;

    private final String mName;
    private final ImmutableMap<Direction, Integer> mMovements;
    private final ImmutableMap<Direction, Integer> mCapturingMovements;
    private final ImmutableSet<TwoHopMovement> mTwoHopMovements;

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
            Preconditions.checkState(Objects.equal(mTwoHopMovements, other.mTwoHopMovements));
        }

        return equal;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mName, mMovements, mTwoHopMovements);
    }

    @Override
    public String toString() {
        return mName;
    }

    public static PieceType getBishopPieceType() {
        Map<Direction, Integer> movements = Maps.newHashMap();
        for (Direction direction : Direction.DIAGONAL_DIRECTIONS) {
            movements.put(direction, UNLIMITED);
        }

        return new PieceType("Bishop", movements, null);
    }

    public static PieceType getKingPieceType() {
        Map<Direction, Integer> movements = Maps.newHashMap();
        for (Direction direction : Direction.values()) {
            movements.put(direction, 1);
        }

        return new PieceType("King", movements, null);
    }

    public static PieceType getKnightPieceType() {
        Set<TwoHopMovement> twoHopMovements = Sets.newHashSet(TwoHopMovement.with(2, 1));

        return new PieceType("Night", null, twoHopMovements);
    }

    public static PieceType getNorthFacingPawnPieceType() {
        Map<Direction, Integer> movements = Maps.newHashMapWithExpectedSize(1);
        movements.put(Direction.NORTH, 1);

        Map<Direction, Integer> capturingMovements = Maps.newHashMapWithExpectedSize(2);
        capturingMovements.put(Direction.NORTHEAST, 1);
        capturingMovements.put(Direction.NORTHWEST, 1);

        return new PieceType("NorthFacingPawn", movements, capturingMovements, null);
    }

    public static PieceType getSouthFacingPawnPieceType() {
        Map<Direction, Integer> movements = Maps.newHashMapWithExpectedSize(1);
        movements.put(Direction.SOUTH, 1);

        Map<Direction, Integer> capturingMovements = Maps.newHashMapWithExpectedSize(2);
        capturingMovements.put(Direction.SOUTHEAST, 1);
        capturingMovements.put(Direction.SOUTHWEST, 1);

        return new PieceType("SouthFacingPawn", movements, capturingMovements, null);
    }

    public static PieceType getQueenPieceType() {
        Map<Direction, Integer> movements = Maps.newHashMap();
        for (Direction direction : Direction.values()) {
            movements.put(direction, UNLIMITED);
        }

        return new PieceType("Queen", movements, null);
    }

    public static PieceType getRookPieceType() {
        Map<Direction, Integer> movements = Maps.newHashMap();
        for (Direction direction : Direction.ADJACENT_DIRECTIONS) {
            movements.put(direction, UNLIMITED);
        }

        return new PieceType("Rook", movements, null);
    }
}
