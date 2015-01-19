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

    public void addPiece(@NotNull Piece piece, @NotNull BoardCoordinate location) {
        //noinspection ConstantConditions
        Preconditions.checkArgument(piece != null);
        Preconditions.checkArgument(location.isValid(mBoardSize));

        setPiece(piece, location);
    }

    public void removePiece(@NotNull BoardCoordinate coordinateForRemoval) {
        Preconditions.checkState(doesPieceExistAt(coordinateForRemoval));

        setPiece(null, coordinateForRemoval);
    }

    @Nullable
    public Piece movePiece(@NotNull BoardCoordinate origin, @NotNull BoardCoordinate destination) {
        return movePieceImpl(origin, destination, true);
    }

    public void undoMovePiece(@NotNull BoardCoordinate originalOrigin, @NotNull BoardCoordinate originalDestination,
                              @Nullable Piece capturedPiece) {
        movePieceImpl(originalDestination, originalOrigin, false);
        if (capturedPiece != null) {
            addPiece(capturedPiece, originalDestination);
        }
    }

    private Piece movePieceImpl(@NotNull BoardCoordinate origin, @NotNull BoardCoordinate destination, boolean incrementMoveCount) {
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

    public boolean doesPieceExistAt(@NotNull BoardCoordinate coordinateToCheck) {
        return getPiece(coordinateToCheck) != null;
    }

    private boolean doesFriendlyPieceExistAt(BoardCoordinate origin, BoardCoordinate destination) {
        Piece piece = getPiece(destination);
        return piece != null && piece.getTeamId() == getPiece(origin).getTeamId();
    }

    public Piece getPiece(@NotNull BoardCoordinate coordinateToRetrieve) {
        return mPieces[coordinateToRetrieve.x - 1][coordinateToRetrieve.y - 1];
    }

    private void setPiece(@Nullable Piece piece, @NotNull BoardCoordinate location) {
        mPieces[location.x - 1][location.y - 1] = piece;
    }

    public Set<BoardCoordinate> getMovesFrom(BoardCoordinate originCoordinate) {
        Piece piece = getPiece(originCoordinate);
        Set<BoardCoordinate> allPossibleMoves = Sets.newHashSet(piece.getMovesFrom(originCoordinate, mBoardSize));

        Set<BoardCoordinate> validMoves = Sets.newHashSetWithExpectedSize(allPossibleMoves.size());
        for (BoardCoordinate move : allPossibleMoves) {
            if (!isBlocked(originCoordinate, move) && !doesFriendlyPieceExistAt(originCoordinate, move)) {
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    private boolean isBlocked(BoardCoordinate origin, BoardCoordinate destination) {
        PathMaker pathMaker = new PathMaker(origin, destination);
        List<BoardCoordinate> spacesAlongPath = pathMaker.getPathToDestination();
        BoardCoordinate lastSpace = spacesAlongPath.isEmpty() ? null : spacesAlongPath.get(spacesAlongPath.size() - 1);
        for (BoardCoordinate space : spacesAlongPath) {
            if (doesFriendlyPieceExistAt(origin, space) || (doesPieceExistAt(space) && !space.equals(lastSpace))) {
                return true;
            }
        }
        return false;
    }

    private void verifyCoordinatesOrThrow(@NotNull BoardCoordinate... coordinates) {
        for (BoardCoordinate coordinate : coordinates) {
            Preconditions.checkArgument(coordinate.isValid(mBoardSize));
        }
    }
}
