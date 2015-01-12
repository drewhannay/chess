package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.logic.PathMaker;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public final class Board {
    public static final int NO_ENPASSANT = 0;

    private final Piece[][] mPieces;
    private final BoardSize mBoardSize;
    private final boolean mWrapAround;
    private final Set<ChessCoordinate> mUninhabitableSquares;

    private int mEnPassantColumn;

    public Board(@NotNull BoardSize boardSize, boolean wrapAround) {
        mBoardSize = boardSize;
        mWrapAround = wrapAround;

        mPieces = new Piece[boardSize.width][boardSize.height];
        mUninhabitableSquares = Sets.newHashSet();
    }

    public BoardSize getBoardSize() {
        return mBoardSize;
    }

    // ---------------------------------

    public void addPiece(@NotNull Piece piece, @NotNull ChessCoordinate location) {
        Preconditions.checkArgument(location.isValid(mBoardSize));

        setPiece(piece, location);
    }

    public void removePiece(@NotNull ChessCoordinate coordinateForRemoval) {
        Preconditions.checkState(doesPieceExistAt(coordinateForRemoval));

        setPiece(null, coordinateForRemoval);
    }

    public void movePiece(@NotNull ChessCoordinate origin, @NotNull ChessCoordinate destination) {
        verifyCoordinatesOrThrow(origin, destination);

        Piece pieceToMove = getPiece(origin);
        addPiece(pieceToMove, destination);
        removePiece(origin);
    }

    public boolean doesPieceExistAt(@NotNull ChessCoordinate coordinateToCheck) {
        return getPiece(coordinateToCheck) != null;
    }

    public boolean doesFriendlyPieceExistAt(ChessCoordinate origin, ChessCoordinate destination) {
        Piece piece = getPiece(destination);
        return piece != null; // TODO: && piece.getTeamId() == getPiece(origin).getTeamId();
    }

    public Piece getPiece(@NotNull ChessCoordinate coordinateToRetrieve) {
        return mPieces[coordinateToRetrieve.x - 1][coordinateToRetrieve.y - 1];
    }

    private void setPiece(@Nullable Piece piece, @NotNull ChessCoordinate location) {
        mPieces[location.x - 1][location.y - 1] = piece;
    }

    public List<ChessCoordinate> getMovesFrom(ChessCoordinate originCoordinate) {
        Piece piece = getPiece(originCoordinate);
        List<ChessCoordinate> allPossibleMoves = piece.getMovesFrom(originCoordinate);

        List<ChessCoordinate> validMoves = Lists.newArrayListWithCapacity(allPossibleMoves.size());
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

    // ---------------------------------

    public boolean isWrapAroundBoard() {
        return mWrapAround;
    }

    public boolean isSquareHabitable(ChessCoordinate coordinate) {
        Preconditions.checkArgument(coordinate.isValid(mBoardSize));

        return !mUninhabitableSquares.contains(coordinate);
    }

    public int getEnpassantCol() {
        return mEnPassantColumn;
    }

    public void setEnpassantCol(int enpassantCol) {
        mEnPassantColumn = enpassantCol;
    }

    /**
     * Debugging method to print the state of the Board
     *
     * @param game       The Game state
     * @param boardIndex The index of the Board
     */
    public void printBoard(Game game, int boardIndex) {
//        for (int i = mUninhabitableSquares.length - 1; i >= 0; i--) {
//            for (Square square : mUninhabitableSquares[i]) {
//                System.out.print(square.printSquareState(game, boardIndex) + "\t"); //$NON-NLS-1$
//            }
//            System.out.println();
//        }
    }

    @Deprecated
    public int getRowCount() {
        return mBoardSize.height;
    }

    @Deprecated
    public int getColumnCount() {
        return mBoardSize.width;
    }

    @Deprecated
    public boolean isRowValid(int row) {
        return row <= getRowCount() && row > 0;
    }

    @Deprecated
    public boolean isColumnValid(int column) {
        return column <= getColumnCount() && column > 0;
    }
}
