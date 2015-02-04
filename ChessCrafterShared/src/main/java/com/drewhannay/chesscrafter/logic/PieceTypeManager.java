package com.drewhannay.chesscrafter.logic;

import com.drewhannay.chesscrafter.models.Direction;
import com.drewhannay.chesscrafter.models.PieceType;
import com.drewhannay.chesscrafter.models.TwoHopMovement;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum PieceTypeManager {
    INSTANCE;

    public static final String BISHOP_ID = "Bishop";
    public static final String KING_ID = "King";
    public static final String KNIGHT_ID = "Night";
    public static final String NORTH_FACING_PAWN_ID = "NorthFacingPawn";
    public static final String SOUTH_FACING_PAWN_ID = "SouthFacingPawn";
    public static final String QUEEN_ID = "Queen";
    public static final String ROOK_ID = "Rook";


    private final Set<PieceType> mPieceTypes;
    private final Set<String> mSystemPieceIds;

    private PieceTypeManager() {
        mPieceTypes = new HashSet<>();
        mPieceTypes.add(getBishopPieceType());
        mPieceTypes.add(getKingPieceType());
        mPieceTypes.add(getKnightPieceType());
        mPieceTypes.add(getNorthFacingPawnPieceType());
        mPieceTypes.add(getSouthFacingPawnPieceType());
        mPieceTypes.add(getQueenPieceType());
        mPieceTypes.add(getRookPieceType());

        mSystemPieceIds = new HashSet<>(mPieceTypes.size());
        for (PieceType pieceType : mPieceTypes) {
            mSystemPieceIds.add(pieceType.getInternalId());
        }
    }

    @NotNull
    public PieceType getPieceTypeById(@NotNull String internalId) {
        for (PieceType pieceType : mPieceTypes) {
            if (internalId.equals(pieceType.getInternalId())) {
                return pieceType;
            }
        }
        throw new IllegalArgumentException("Unknown PieceType:" + internalId);
    }

    @NotNull
    public Set<PieceType> getAllPieceTypes() {
        return ImmutableSet.copyOf(mPieceTypes);
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

        return new PieceType(BISHOP_ID, "Bishop", movements, null);
    }

    public static PieceType getKingPieceType() {
        Map<Direction, Integer> movements = Maps.newHashMap();
        for (Direction direction : Direction.values()) {
            movements.put(direction, 1);
        }

        return new PieceType(KING_ID, "King", movements, null);
    }

    public static PieceType getKnightPieceType() {
        Set<TwoHopMovement> twoHopMovements = Sets.newHashSet(TwoHopMovement.with(2, 1));

        return new PieceType(KNIGHT_ID, "Knight", null, twoHopMovements);
    }

    public static PieceType getNorthFacingPawnPieceType() {
        Map<Direction, Integer> movements = Maps.newHashMapWithExpectedSize(1);
        movements.put(Direction.NORTH, 1);

        Map<Direction, Integer> capturingMovements = Maps.newHashMapWithExpectedSize(2);
        capturingMovements.put(Direction.NORTHEAST, 1);
        capturingMovements.put(Direction.NORTHWEST, 1);

        return new PieceType(NORTH_FACING_PAWN_ID, "Pawn", movements, capturingMovements, null);
    }

    public static PieceType getSouthFacingPawnPieceType() {
        Map<Direction, Integer> movements = Maps.newHashMapWithExpectedSize(1);
        movements.put(Direction.SOUTH, 1);

        Map<Direction, Integer> capturingMovements = Maps.newHashMapWithExpectedSize(2);
        capturingMovements.put(Direction.SOUTHEAST, 1);
        capturingMovements.put(Direction.SOUTHWEST, 1);

        return new PieceType(SOUTH_FACING_PAWN_ID, "Pawn", movements, capturingMovements, null);
    }

    public static PieceType getQueenPieceType() {
        Map<Direction, Integer> movements = Maps.newHashMap();
        for (Direction direction : Direction.values()) {
            movements.put(direction, PieceType.UNLIMITED);
        }

        return new PieceType(QUEEN_ID, "Queen", movements, null);
    }

    public static PieceType getRookPieceType() {
        Map<Direction, Integer> movements = Maps.newHashMap();
        for (Direction direction : Direction.ADJACENT_DIRECTIONS) {
            movements.put(direction, PieceType.UNLIMITED);
        }

        return new PieceType(ROOK_ID, "Rook", movements, null);
    }

    public boolean isSystemPiece(@NotNull String internalId) {
        return mSystemPieceIds.contains(internalId);
    }
}
