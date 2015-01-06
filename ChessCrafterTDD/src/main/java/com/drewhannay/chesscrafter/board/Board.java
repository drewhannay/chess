package com.drewhannay.chesscrafter.board;

import com.drewhannay.chesscrafter.piece.Piece;

import java.util.ArrayList;
import java.util.List;

public class Board {

    public static final int DEFAULT_BOARD_SIZE = 8;

    private final int mBoardSize;
    private final Piece[][] mPieces;

    public Board() {
        this(DEFAULT_BOARD_SIZE);
    }

    public Board(int boardSize) {
        verifyBoardSizeOrThrow(boardSize);

        mBoardSize = boardSize;
        mPieces = new Piece[mBoardSize][mBoardSize];
    }

    public void addPiece(Piece piece, BoardCoordinate location) {
        if (piece == null) {
            throw new IllegalArgumentException("piece");
        }

        if (!location.isCoordinateValidForBoardSize(mBoardSize)) {
            throw new IllegalArgumentException("moveTarget");
        }

        setPiece(piece, location);
    }

    public void removePiece(BoardCoordinate coordinateForRemoval) {
        if (!doesPieceExistAt(coordinateForRemoval)) {
            throw new IllegalArgumentException("no piece to remove");
        }

        setPiece(null, coordinateForRemoval);
    }

    public void movePiece(BoardCoordinate origin, BoardCoordinate destination) {
        verifyCoordinatesOrThrow(origin, destination);

        Piece pieceToMove = getPiece(origin);
        addPiece(pieceToMove, destination);
        removePiece(origin);
    }

    public boolean doesPieceExistAt(BoardCoordinate coordinateToCheck) {
        return getPiece(coordinateToCheck) != null;
    }

    public boolean doesFriendlyPieceExistAt(BoardCoordinate origin, BoardCoordinate destination) {
        Piece piece = getPiece(destination);
        return piece != null && piece.isFirstPlayerPiece() == getPiece(origin).isFirstPlayerPiece();
    }

    public Piece getPiece(BoardCoordinate coordinateToRetrieve) {
        return mPieces[coordinateToRetrieve.x - 1][coordinateToRetrieve.y - 1];
    }

    private void setPiece(Piece piece, BoardCoordinate location) {
        mPieces[location.x - 1][location.y - 1] = piece;
    }

    public List<BoardCoordinate> getMovesFrom(BoardCoordinate originCoordinate) {
        Piece piece = getPiece(originCoordinate);
        List<BoardCoordinate> allPossibleMoves = piece.getMovesFrom(originCoordinate);

        List<BoardCoordinate> validMoves = new ArrayList<>(allPossibleMoves.size());
        for (BoardCoordinate move : allPossibleMoves) {
            if (!isBlocked(originCoordinate, move) && !doesFriendlyPieceExistAt(originCoordinate, move)) {
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    private boolean isBlocked(BoardCoordinate origin, BoardCoordinate destination) {
        PathMaker checker = new PathMaker(origin, destination);
        List<BoardCoordinate> spacesAlongPath = checker.getPathToDestination();
        BoardCoordinate lastSpace = spacesAlongPath.isEmpty() ? null : spacesAlongPath.get(spacesAlongPath.size() - 1);
        for (BoardCoordinate space : spacesAlongPath) {
            if (doesFriendlyPieceExistAt(origin, space) || (doesPieceExistAt(space) && !space.equals(lastSpace))) {
                return true;
            }
        }
        return false;
    }

    private static void verifyBoardSizeOrThrow(int boardSize) {
        if (boardSize <= 0) {
            throw new IllegalArgumentException("boardSize");
        }
    }

    private void verifyCoordinatesOrThrow(BoardCoordinate... coordinates) {
        for (BoardCoordinate coordinate : coordinates) {
            if (!coordinate.isCoordinateValidForBoardSize(mBoardSize)) {
                throw new IllegalArgumentException("destination");
            }
        }
    }
}
