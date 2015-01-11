package com.drewhannay.chesscrafter.logic;

import com.drewhannay.chesscrafter.models.BidirectionalMovement;
import com.drewhannay.chesscrafter.models.PieceMovements;
import com.drewhannay.chesscrafter.models.PieceType;
import com.drewhannay.chesscrafter.utility.GsonUtility;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static com.drewhannay.chesscrafter.models.PieceMovements.MovementDirection;
import static com.drewhannay.chesscrafter.models.PieceMovements.UNLIMITED;

public class PieceBuilder {
    public PieceBuilder() {
        mMovements = Maps.newHashMap();
        mBidirectionalMovements = Sets.newHashSet();
    }

    public static PieceType parsePieceType(String json) {
        return GsonUtility.getGson().fromJson(json, PieceType.class);
    }

    public static PieceType getBishopPieceType() {
        Map<PieceMovements.MovementDirection, Integer> movements = Maps.newHashMap();
        movements.put(MovementDirection.NORTHEAST, UNLIMITED);
        movements.put(MovementDirection.NORTHEAST, UNLIMITED);
        movements.put(MovementDirection.SOUTHEAST, UNLIMITED);
        movements.put(MovementDirection.NORTHWEST, UNLIMITED);
        movements.put(MovementDirection.SOUTHWEST, UNLIMITED);

        return new PieceType("Bishop", new PieceMovements(movements, Collections.<BidirectionalMovement>emptySet()), false);
    }

    public static PieceType getKingPieceType() {
        Map<MovementDirection, Integer> movements = Maps.newHashMap();
        movements.put(MovementDirection.NORTH, 1);
        movements.put(MovementDirection.SOUTH, 1);
        movements.put(MovementDirection.EAST, 1);
        movements.put(MovementDirection.WEST, 1);
        movements.put(MovementDirection.NORTHEAST, 1);
        movements.put(MovementDirection.SOUTHEAST, 1);
        movements.put(MovementDirection.NORTHWEST, 1);
        movements.put(MovementDirection.SOUTHWEST, 1);

        return new PieceType("King", new PieceMovements(movements, Collections.<BidirectionalMovement>emptySet()), false);
    }

    public static PieceType getKnightPieceType() {
        Set<BidirectionalMovement> bidirectionalMovements = Sets.newHashSet(new BidirectionalMovement(1, 2));

        return new PieceType("Knight",
                new PieceMovements(Collections.<MovementDirection, Integer>emptyMap(), bidirectionalMovements), true);
    }

    public static PieceType getPawnPieceType() {
        return new PieceType(PieceType.PAWN_NAME, new PieceMovements(Collections.<MovementDirection, Integer>emptyMap(),
                Collections.<BidirectionalMovement>emptySet()), false);
    }

    public static PieceType getQueenPieceType() {
        Map<MovementDirection, Integer> movements = Maps.newHashMap();
        movements.put(MovementDirection.NORTH, -1);
        movements.put(MovementDirection.SOUTH, -1);
        movements.put(MovementDirection.WEST, -1);
        movements.put(MovementDirection.EAST, -1);
        movements.put(MovementDirection.NORTHEAST, -1);
        movements.put(MovementDirection.SOUTHEAST, -1);
        movements.put(MovementDirection.NORTHWEST, -1);
        movements.put(MovementDirection.SOUTHWEST, -1);

        return new PieceType("Queen", new PieceMovements(movements, Collections.<BidirectionalMovement>emptySet()), false);
    }

    public static PieceType getRookPieceType() {
        Map<MovementDirection, Integer> movements = Maps.newHashMap();
        movements.put(MovementDirection.NORTH, -1);
        movements.put(MovementDirection.SOUTH, -1);
        movements.put(MovementDirection.WEST, -1);
        movements.put(MovementDirection.EAST, -1);

        return new PieceType("Rook", new PieceMovements(movements, Collections.<BidirectionalMovement>emptySet()), false);
    }

    public void addMovement(MovementDirection direction, int distance) {
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

    public void addBidirectionalMovement(BidirectionalMovement movement) {
        mBidirectionalMovements.add(movement);
    }

    public void clearBidirectionalMovements() {
        mBidirectionalMovements.clear();
    }

    private final Map<MovementDirection, Integer> mMovements;
    private final Set<BidirectionalMovement> mBidirectionalMovements;

    private String mName;
    private boolean mIsLeaper;
}
