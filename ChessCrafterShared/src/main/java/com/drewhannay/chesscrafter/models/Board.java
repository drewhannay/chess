package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.logic.PathMaker;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public final class Board {
    private final Piece[][] mPieces;
    private final BoardSize mBoardSize;

    public Board(@NotNull BoardSize boardSize) {
        mBoardSize = boardSize;

        mPieces = new Piece[boardSize.width][boardSize.height];
    }

    public BoardSize getBoardSize() {
        return mBoardSize;
    }

    public void addPiece(@NotNull Piece piece, @NotNull ChessCoordinate location) {
        //noinspection ConstantConditions
        Preconditions.checkArgument(piece != null);
        Preconditions.checkArgument(location.isValid(mBoardSize));

        setPiece(piece, location);
    }

    public void removePiece(@NotNull ChessCoordinate coordinateForRemoval) {
        Preconditions.checkState(doesPieceExistAt(coordinateForRemoval));

        setPiece(null, coordinateForRemoval);
    }

    @Nullable
    public Piece undoMovePiece(@NotNull ChessCoordinate origin, @NotNull ChessCoordinate destination) {
        return movePieceImpl(origin, destination, false);
    }

    @Nullable
    public Piece movePiece(@NotNull ChessCoordinate origin, @NotNull ChessCoordinate destination) {
        return movePieceImpl(origin, destination, true);
    }

    private Piece movePieceImpl(@NotNull ChessCoordinate origin, @NotNull ChessCoordinate destination, boolean incrementMoveCount) {
        verifyCoordinatesOrThrow(origin, destination);

        Piece pieceToCapture = getPiece(destination);
        Piece pieceToMove = getPiece(origin);

        addPiece(pieceToMove, destination);
        removePiece(origin);

        if (incrementMoveCount) {
            pieceToMove.incrementMoveCount();
        } else {
            pieceToMove.decrementMoveCount();
        }

        return pieceToCapture;
    }

    public boolean doesPieceExistAt(@NotNull ChessCoordinate coordinateToCheck) {
        return getPiece(coordinateToCheck) != null;
    }

    private boolean doesFriendlyPieceExistAt(ChessCoordinate origin, ChessCoordinate destination) {
        Piece piece = getPiece(destination);
        return piece != null && piece.getTeamId() == getPiece(origin).getTeamId();
    }

    public Piece getPiece(@NotNull ChessCoordinate coordinateToRetrieve) {
        return mPieces[coordinateToRetrieve.x - 1][coordinateToRetrieve.y - 1];
    }

    private void setPiece(@Nullable Piece piece, @NotNull ChessCoordinate location) {
        mPieces[location.x - 1][location.y - 1] = piece;
    }

    public Set<ChessCoordinate> getMovesFrom(ChessCoordinate originCoordinate) {
        Piece piece = getPiece(originCoordinate);
        Set<ChessCoordinate> allPossibleMoves = Sets.newHashSet(piece.getMovesFrom(originCoordinate, mBoardSize));

        Set<ChessCoordinate> validMoves = Sets.newHashSetWithExpectedSize(allPossibleMoves.size());
        for (ChessCoordinate move : allPossibleMoves) {
            if (!isBlocked(originCoordinate, move) && !doesFriendlyPieceExistAt(originCoordinate, move)) {
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    private boolean isBlocked(ChessCoordinate origin, ChessCoordinate destination) {
        PathMaker pathMaker = new PathMaker(origin, destination);
        List<ChessCoordinate> spacesAlongPath = pathMaker.getPathToDestination();
        ChessCoordinate lastSpace = spacesAlongPath.isEmpty() ? null : spacesAlongPath.get(spacesAlongPath.size() - 1);
        for (ChessCoordinate space : spacesAlongPath) {
            if (doesFriendlyPieceExistAt(origin, space) || (doesPieceExistAt(space) && !space.equals(lastSpace))) {
                return true;
            }
        }
        return false;
    }

    private void verifyCoordinatesOrThrow(@NotNull ChessCoordinate... coordinates) {
        for (ChessCoordinate coordinate : coordinates) {
            Preconditions.checkArgument(coordinate.isValid(mBoardSize));
        }
    }

    @Deprecated
    public boolean isWrapAroundBoard() {
        return false;
    }

    @Deprecated
    public int getRowCount() {
        return mBoardSize.height;
    }

    @Deprecated
    public int getColumnCount() {
        return mBoardSize.width;
    }
}
