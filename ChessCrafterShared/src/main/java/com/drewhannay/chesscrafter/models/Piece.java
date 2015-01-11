package com.drewhannay.chesscrafter.models;

import com.google.common.base.Objects;

public final class Piece {
    public static final int TEAMLESS_PIECE = -1;

    public Piece(long id, PieceType pieceType, ChessCoordinates coordinates) {
        mId = id;
        mPieceType = pieceType;
        mOriginalCoordinates = coordinates;

        mCoordinates = coordinates;
    }

    public long getId() {
        return mId;
    }

    public PieceType getPieceType() {
        return mPieceType;
    }

    public ChessCoordinates getOriginalCoordinates() {
        return mOriginalCoordinates;
    }

    public ChessCoordinates getCoordinates() {
        return mCoordinates;
    }

    public int getMoveCount() {
        return mMoveCount;
    }

    public void setCoordinates(ChessCoordinates coordinates) {
        mCoordinates = coordinates;
    }

    public int getTeamId(Game game) {
        for (int teamIndex = 0; teamIndex < game.getTeams().length; teamIndex++) {
            if (game.getTeams()[teamIndex].getCapturedPieces().contains(this) || game.getTeams()[teamIndex].getPieces().contains(this))
                return teamIndex;
        }

        return TEAMLESS_PIECE;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Piece))
            return false;

        Piece otherPiece = (Piece) other;

        // TODO: we shouldn't include mutable state in an equals method, but we still need to somehow check the other fields of the piece...
        return Objects.equal(mId, otherPiece.mId)
                && Objects.equal(mPieceType, otherPiece.mPieceType)
                && Objects.equal(mOriginalCoordinates, otherPiece.mOriginalCoordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId, mPieceType, mOriginalCoordinates);
    }

    @Override
    public String toString() {
        return getPieceType().toString();
    }

    private final long mId;
    private final PieceType mPieceType;
    private final ChessCoordinates mOriginalCoordinates;

    private int mMoveCount;
    private ChessCoordinates mCoordinates;
}