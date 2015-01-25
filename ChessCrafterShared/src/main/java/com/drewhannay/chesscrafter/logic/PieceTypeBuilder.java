package com.drewhannay.chesscrafter.logic;

import com.drewhannay.chesscrafter.models.Direction;
import com.drewhannay.chesscrafter.models.PieceType;
import com.drewhannay.chesscrafter.models.TwoHopMovement;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

public class PieceTypeBuilder {
    public PieceTypeBuilder() {
        mMovements = Maps.newHashMap();
        mTwoHopMovements = Sets.newHashSet();
    }

    public static PieceType parsePieceType(String json) {
        return null;//GsonUtility.getGson().fromJson(json, PieceType.class);
    }

    public void addMovement(Direction direction, int distance) {
        Preconditions.checkState(distance >= 0 || distance == PieceType.UNLIMITED);

        mMovements.put(direction, distance);
    }

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
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
}
