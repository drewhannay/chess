package com.drewhannay.chesscrafter.logic;

import com.drewhannay.chesscrafter.models.Direction;
import com.drewhannay.chesscrafter.models.PieceType;
import com.drewhannay.chesscrafter.models.TwoHopMovement;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum PieceTypeManager {
    INSTANCE;

    private final Set<PieceType> mPieceTypes;

    private PieceTypeManager() {
        mPieceTypes = new HashSet<>();
        mPieceTypes.add(getBishopPieceType());
        mPieceTypes.add(getKingPieceType());
        mPieceTypes.add(getKnightPieceType());
        mPieceTypes.add(getNorthFacingPawnPieceType());
        mPieceTypes.add(getSouthFacingPawnPieceType());
        mPieceTypes.add(getQueenPieceType());
        mPieceTypes.add(getRookPieceType());
    }

    @NotNull
    public PieceType getPieceTypeByName(@NotNull String name) {
        for (PieceType pieceType : mPieceTypes) {
            if (name.equals(pieceType.getName())) {
                return pieceType;
            }
        }
        throw new IllegalArgumentException("Unknown PieceType:" + name);
    }

    public void registerPieceType(@NotNull PieceType pieceType) {
        Preconditions.checkArgument(!mPieceTypes.contains(pieceType), "Duplicate PieceType");

        mPieceTypes.add(pieceType);
    }

    public void unregisterPieceType(@NotNull PieceType pieceType) {
        Preconditions.checkArgument(mPieceTypes.contains(pieceType), "Unknown PieceType");

        mPieceTypes.remove(pieceType);
    }

    public static PieceType getBishopPieceType() {
        Map<Direction, Integer> movements = Maps.newHashMap();
        for (Direction direction : Direction.DIAGONAL_DIRECTIONS) {
            movements.put(direction, PieceType.UNLIMITED);
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
            movements.put(direction, PieceType.UNLIMITED);
        }

        return new PieceType("Queen", movements, null);
    }

    public static PieceType getRookPieceType() {
        Map<Direction, Integer> movements = Maps.newHashMap();
        for (Direction direction : Direction.ADJACENT_DIRECTIONS) {
            movements.put(direction, PieceType.UNLIMITED);
        }

        return new PieceType("Rook", movements, null);
    }
}
