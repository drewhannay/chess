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

    public Set<ChessCoordinate> getMovesFrom(@NotNull ChessCoordinate coordinate, @NotNull BoardSize boardSize) {
        return mPieceType.getMovesFrom(coordinate, boardSize, mMoveCount);
    }

    @Override
    public String toString() {
        return mPieceType.toString();
    }

    public static Piece newBishop() {
        return newBishop(TEAM_ONE);
    }

    public static Piece newBishop(int teamId) {
        return new Piece(teamId, PieceType.getBishopPieceType());
    }

    public static Piece newKing(int teamId) {
        return newKing(teamId, false);
    }

    public static Piece newKing(int teamId, boolean isObjectivePiece) {
        return new Piece(teamId, PieceType.getKingPieceType(), isObjectivePiece);
    }

    public static Piece newKnight() {
        return newKnight(TEAM_ONE);
    }

    public static Piece newKnight(int teamId) {
        return new Piece(teamId, PieceType.getKnightPieceType());
    }

    public static Piece newPawn() {
        return newPawn(TEAM_ONE);
    }

    public static Piece newPawn(int teamId) {
        return new Piece(teamId, PieceType.getPawnPieceType());
    }

    public static Piece newQueen() {
        return newQueen(TEAM_ONE);
    }

    public static Piece newQueen(int teamId) {
        return new Piece(teamId, PieceType.getQueenPieceType());
    }

    public static Piece newRook() {
        return newQueen(TEAM_ONE);
    }

    public static Piece newRook(int teamId) {
        return new Piece(teamId, PieceType.getRookPieceType());
    }
}
