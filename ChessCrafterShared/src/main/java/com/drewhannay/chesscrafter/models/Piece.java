package com.drewhannay.chesscrafter.models;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Piece {
    private final long mId;
    private final int mTeamId;
    private final PieceType mPieceType;

    private int mMoveCount;
    private ChessCoordinate mCoordinates;

    public Piece(long id, int teamId, PieceType pieceType, ChessCoordinate coordinates) {
        mId = id;
        mTeamId = teamId;
        mPieceType = pieceType;
        mCoordinates = coordinates;
    }

    @Deprecated
    public int getTeamId(Game game) {
        return mTeamId;
    }

    @Deprecated
    public PieceType getPieceType() {
        return mPieceType;
    }

    @Deprecated
    public ChessCoordinate getCoordinates() {
        return mCoordinates;
    }

    @Deprecated
    public void setCoordinates(ChessCoordinate coordinates) {
        mCoordinates = coordinates;
    }

    public long getId() {
        return mId;
    }

    public int getTeamId() {
        return mTeamId;
    }

    public int getMoveCount() {
        return mMoveCount;
    }

    public List<ChessCoordinate> getMovesFrom(@NotNull ChessCoordinate coordinate, @NotNull BoardSize boardSize) {
        return mPieceType.getMovesFrom(coordinate, boardSize);
    }

    @Override
    public String toString() {
        return mPieceType.toString();
    }
}
