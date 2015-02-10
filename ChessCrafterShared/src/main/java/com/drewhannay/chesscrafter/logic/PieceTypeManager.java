package com.drewhannay.chesscrafter.logic;

import com.drewhannay.chesscrafter.models.CardinalMovement;
import com.drewhannay.chesscrafter.models.Direction;
import com.drewhannay.chesscrafter.models.PieceType;
import com.drewhannay.chesscrafter.models.TwoHopMovement;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
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

    public boolean hasPieceTypeWithId(@NotNull String internalId) {
        for (PieceType pieceType : mPieceTypes) {
            if (internalId.equals(pieceType.getInternalId())) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    public Set<PieceType> getAllPieceTypes() {
        return ImmutableSet.copyOf(mPieceTypes);
    }

    public void registerPieceType(@NotNull PieceType pieceType) {
        Preconditions.checkArgument(!mPieceTypes.contains(pieceType), "Duplicate PieceType");

        mPieceTypes.add(pieceType);
    }

    public void unregisterPieceType(@NotNull String internalId) {
        Preconditions.checkArgument(hasPieceTypeWithId(internalId), "Unknown PieceType");

        mPieceTypes.remove(getPieceTypeById(internalId));
    }

    public static PieceType getBishopPieceType() {
        Set<CardinalMovement> movements = new HashSet<>(Direction.values().length);
        for (Direction direction : Direction.DIAGONAL_DIRECTIONS) {
            movements.add(CardinalMovement.with(direction, PieceType.UNLIMITED));
        }

        return new PieceType(BISHOP_ID, "Bishop", movements, null);
    }

    public static PieceType getKingPieceType() {
        Set<CardinalMovement> movements = new HashSet<>(Direction.values().length);
        for (Direction direction : Direction.values()) {
            movements.add(CardinalMovement.with(direction, 1));
        }

        return new PieceType(KING_ID, "King", movements, null);
    }

    public static PieceType getKnightPieceType() {
        Set<TwoHopMovement> twoHopMovements = Sets.newHashSet(TwoHopMovement.with(2, 1));

        return new PieceType(KNIGHT_ID, "Knight", null, twoHopMovements);
    }

    public static PieceType getNorthFacingPawnPieceType() {
        Set<CardinalMovement> movements = new HashSet<>(1);
        movements.add(CardinalMovement.with(Direction.NORTH, 1));

        Set<CardinalMovement> capturingMovements = new HashSet<>(2);
        capturingMovements.add(CardinalMovement.with(Direction.NORTHEAST, 1));
        capturingMovements.add(CardinalMovement.with(Direction.NORTHWEST, 1));

        return new PieceType(NORTH_FACING_PAWN_ID, "Pawn", movements, capturingMovements, null);
    }

    public static PieceType getSouthFacingPawnPieceType() {
        Set<CardinalMovement> movements = new HashSet<>(1);
        movements.add(CardinalMovement.with(Direction.SOUTH, 1));

        Set<CardinalMovement> capturingMovements = new HashSet<>(2);
        capturingMovements.add(CardinalMovement.with(Direction.SOUTHEAST, 1));
        capturingMovements.add(CardinalMovement.with(Direction.SOUTHWEST, 1));

        return new PieceType(SOUTH_FACING_PAWN_ID, "Pawn", movements, capturingMovements, null);
    }

    public static PieceType getQueenPieceType() {
        Set<CardinalMovement> movements = new HashSet<>(Direction.values().length);
        for (Direction direction : Direction.values()) {
            movements.add(CardinalMovement.with(direction, PieceType.UNLIMITED));
        }

        return new PieceType(QUEEN_ID, "Queen", movements, null);
    }

    public static PieceType getRookPieceType() {
        Set<CardinalMovement> movements = new HashSet<>(Direction.ADJACENT_DIRECTIONS.size());
        for (Direction direction : Direction.ADJACENT_DIRECTIONS) {
            movements.add(CardinalMovement.with(direction, PieceType.UNLIMITED));
        }

        return new PieceType(ROOK_ID, "Rook", movements, null);
    }

    public boolean isSystemPiece(@NotNull String internalId) {
        return mSystemPieceIds.contains(internalId);
    }
}
