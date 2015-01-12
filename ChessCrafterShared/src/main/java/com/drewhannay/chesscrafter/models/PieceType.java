package com.drewhannay.chesscrafter.models;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public class PieceType {
    // TODO: this should eventually be removed, when our variant creation is good enough to create pawns
    public static final String PAWN_NAME = "Pawn";

    private final String mName;
    private final PieceMovements mPieceMovements;

    @Deprecated
    public PieceType(String name, PieceMovements pieceMovements, boolean isLeaper) {
        this(name, pieceMovements);
    }

    @Deprecated
    public boolean isLeaper() {
        return false;
    }

    public PieceType(@NotNull String name, @NotNull PieceMovements pieceMovements) {
        Preconditions.checkArgument(!name.isEmpty());

        mName = name;
        mPieceMovements = pieceMovements;
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

    public PieceMovements getPieceMovements() {
        return mPieceMovements;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof PieceType))
            return false;

        PieceType otherPieceType = (PieceType) other;
        boolean equal = Objects.equal(mName, otherPieceType.mName);
        if (equal) {
            // do not allow PieceTypes with the same name but different movement attributes
            Preconditions.checkState(Objects.equal(mPieceMovements, otherPieceType.mPieceMovements));
        }

        return equal;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mName, mPieceMovements);
    }

    @Override
    public String toString() {
        return mName;
    }
}
