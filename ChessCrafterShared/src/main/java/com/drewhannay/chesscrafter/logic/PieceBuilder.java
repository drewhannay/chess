package com.drewhannay.chesscrafter.logic;

import com.drewhannay.chesscrafter.models.TwoHopMovement;
import com.drewhannay.chesscrafter.models.Direction;
import com.drewhannay.chesscrafter.models.PieceMovements;
import com.drewhannay.chesscrafter.models.PieceType;
import com.drewhannay.chesscrafter.utility.GsonUtility;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static com.drewhannay.chesscrafter.models.PieceMovements.UNLIMITED;

public class PieceBuilder {
    public PieceBuilder() {
        mMovements = Maps.newHashMap();
        mTwoHopMovements = Sets.newHashSet();
    }

    public static PieceType parsePieceType(String json) {
        return GsonUtility.getGson().fromJson(json, PieceType.class);
    }

    public static PieceType getBishopPieceType() {
        Map<Direction, Integer> movements = Maps.newHashMap();
        for (Direction direction : Direction.DIAGONAL_DIRECTIONS) {
            movements.put(direction, UNLIMITED);
        }

        return new PieceType("Bishop", new PieceMovements(movements, Collections.<TwoHopMovement>emptySet()));
    }

    public static PieceType getKingPieceType() {
        Map<Direction, Integer> movements = Maps.newHashMap();
        for (Direction direction : Direction.values()) {
            movements.put(direction, 1);
        }

        return new PieceType("King", new PieceMovements(movements, Collections.<TwoHopMovement>emptySet()));
    }

    public static PieceType getKnightPieceType() {
        Set<TwoHopMovement> twoHopMovements = Sets.newHashSet(TwoHopMovement.with(2, 1));

        return new PieceType("Knight",
                new PieceMovements(Collections.<Direction, Integer>emptyMap(), twoHopMovements));
    }

    public static PieceType getPawnPieceType() {
        return new PieceType(PieceType.PAWN_NAME, new PieceMovements(Collections.<Direction, Integer>emptyMap(),
                Collections.<TwoHopMovement>emptySet()));
    }

    public static PieceType getQueenPieceType() {
        Map<Direction, Integer> movements = Maps.newHashMap();
        for (Direction direction : Direction.values()) {
            movements.put(direction, UNLIMITED);
        }

        return new PieceType("Queen", new PieceMovements(movements, Collections.<TwoHopMovement>emptySet()));
    }

    public static PieceType getRookPieceType() {
        Map<Direction, Integer> movements = Maps.newHashMap();
        for (Direction direction : Direction.ADJACENT_DIRECTIONS) {
            movements.put(direction, UNLIMITED);
        }

        return new PieceType("Rook", new PieceMovements(movements, Collections.<TwoHopMovement>emptySet()));
    }

    public void addMovement(Direction direction, int distance) {
        Preconditions.checkState(distance >= 0 || distance == UNLIMITED);

        mMovements.put(direction, distance);
    }

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public boolean isLeaper() {
        return mIsLeaper;
    }

    public void setCanJump(boolean mCanJump) {
        this.mIsLeaper = mCanJump;
    }

    public void addBidirectionalMovement(TwoHopMovement movement) {
        mTwoHopMovements.add(movement);
    }

    public void clearBidirectionalMovements() {
        mTwoHopMovements.clear();
    }

    private final Map<Direction, Integer> mMovements;
    private final Set<TwoHopMovement> mTwoHopMovements;

    private String mName;
    private boolean mIsLeaper;
}
