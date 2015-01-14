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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PieceType {
    public static final int UNLIMITED = Integer.MAX_VALUE;

    // TODO: this should eventually be removed, when our variant creation is good enough to create pawns
    public static final String PAWN_NAME = "Pawn";

    private final String mName;
    private final ImmutableMap<Direction, Integer> mMovements;
    private final ImmutableSet<TwoHopMovement> mTwoHopMovements;

    public PieceType(@NotNull String name, @Nullable Map<Direction, Integer> movements,
                     @Nullable Set<TwoHopMovement> twoHopMovements) {
        Preconditions.checkArgument(!name.isEmpty());

        mName = name;
        mMovements = movements != null ? ImmutableMap.copyOf(movements) : ImmutableMap.<Direction, Integer>of();
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

    public List<ChessCoordinate> getMovesFrom(@NotNull ChessCoordinate startLocation, @NotNull BoardSize boardSize) {
        if (getName().equals(PAWN_NAME)) {
            return getPawnMovesFrom(startLocation, boardSize);
        }

        List<ChessCoordinate> moves = new ArrayList<>();

        for (Direction direction : mMovements.keySet()) {
            PathMaker pathMaker = new PathMaker(startLocation, direction.getFurthestPoint(startLocation, boardSize));
            moves.addAll(pathMaker.getPathToDestination(mMovements.get(direction)));
        }

        for (TwoHopMovement twoHopMovement : mTwoHopMovements) {
            List<ChessCoordinate> allMoves = new ArrayList<>();

            for (int quadrant = 1; quadrant <= 4; quadrant++) {
                allMoves.addAll(getQuadrantMoves(startLocation, twoHopMovement, quadrant));
            }

            for (ChessCoordinate move : allMoves) {
                if (move.isValid(boardSize)) {
                    moves.add(move);
                }
            }
        }

        return moves;
    }

    private List<ChessCoordinate> getPawnMovesFrom(@NotNull ChessCoordinate startLocation, @NotNull BoardSize boardSize) {
        List<ChessCoordinate> moves = new ArrayList<>();
        // TODO: should only be able to move two spaces if it's the first move
        moves.add(ChessCoordinate.at(startLocation.x, startLocation.y + 1));
        moves.add(ChessCoordinate.at(startLocation.x, startLocation.y + 2));
        return moves;
    }


    // TODO: should be a Set? don't want duplicate results
    private List<ChessCoordinate> getQuadrantMoves(@NotNull ChessCoordinate coordinate,
                                                   @NotNull TwoHopMovement movement,
                                                   int quadrant) {
        int xMultiplier = quadrant == 1 || quadrant == 4 ? 1 : -1;
        int yMultiplier = quadrant == 1 || quadrant == 2 ? 1 : -1;

        List<ChessCoordinate> moves = new ArrayList<>(2);
        moves.add(ChessCoordinate.at(coordinate.x + movement.x * xMultiplier, coordinate.y + movement.y * yMultiplier));
        moves.add(ChessCoordinate.at(coordinate.x + movement.y * xMultiplier, coordinate.y + movement.x * yMultiplier));
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

        return new PieceType("Knight", null, twoHopMovements);
    }

    public static PieceType getPawnPieceType() {
        return new PieceType(PieceType.PAWN_NAME, null, null);
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
