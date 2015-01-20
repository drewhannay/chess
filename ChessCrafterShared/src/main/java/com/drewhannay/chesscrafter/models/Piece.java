package com.drewhannay.chesscrafter.models;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class Piece {
    public static final int TEAM_ONE = 1;
    public static final int TEAM_TWO = 2;

    private final int mTeamId;
    private final PieceType mPieceType;
    private final boolean mIsObjectivePiece;

    private int mMoveCount;

    public Piece(int teamId, PieceType pieceType) {
        this(teamId, pieceType, false);
    }

    public Piece(int teamId, PieceType pieceType, boolean isObjectivePiece) {
        mTeamId = teamId;
        mPieceType = pieceType;
        mIsObjectivePiece = isObjectivePiece;

        mMoveCount = 0;
    }

    public String getName() {
        return mPieceType.getName();
    }

    public int getTeamId() {
        return mTeamId;
    }

    public boolean isObjectivePiece() {
        return mIsObjectivePiece;
    }

    public boolean hasMoved() {
        return mMoveCount != 0;
    }

    public void incrementMoveCount() {
        mMoveCount++;
    }

    public void decrementMoveCount() {
        mMoveCount--;
    }

    public Set<BoardCoordinate> getMovesFrom(@NotNull BoardCoordinate coordinate, @NotNull BoardSize boardSize) {
        return mPieceType.getMovesFrom(coordinate, boardSize, mMoveCount);
    }

    public Set<BoardCoordinate> getCapturingMovesFrom(@NotNull BoardCoordinate coordinate, @NotNull BoardSize boardSize) {
        return mPieceType.getCapturingMovesFrom(coordinate, boardSize);
    }

    @Override
    public String toString() {
        return mPieceType.toString();
    }

    public static Piece newBishop(int teamId) {
        return new Piece(teamId, PieceType.getBishopPieceType());
    }

    public static Piece newKing(int teamId, boolean isObjectivePiece) {
        return new Piece(teamId, PieceType.getKingPieceType(), isObjectivePiece);
    }

    public static Piece newKnight(int teamId) {
        return new Piece(teamId, PieceType.getKnightPieceType());
    }

    public static Piece newNorthFacingPawn() {
        return newNorthFacingPawn(TEAM_ONE);
    }

    public static Piece newNorthFacingPawn(int teamId) {
        return new Piece(teamId, PieceType.getNorthFacingPawnPieceType());
    }

    public static Piece newSouthFacingPawn(int teamId) {
        return new Piece(teamId, PieceType.getSouthFacingPawnPieceType());
    }

    public static Piece newQueen(int teamId) {
        return new Piece(teamId, PieceType.getQueenPieceType());
    }

    public static Piece newRook(int teamId) {
        return new Piece(teamId, PieceType.getRookPieceType());
    }
}
