package com.drewhannay.chesscrafter.piece;

import com.drewhannay.chesscrafter.board.Board;
import com.drewhannay.chesscrafter.board.BoardCoordinate;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece {

    private final boolean mIsFirstPlayerPiece;

    public Piece(boolean isFirstPlayerPiece) {
        mIsFirstPlayerPiece = isFirstPlayerPiece;
    }

    public abstract List<BoardCoordinate> getMovesFrom(BoardCoordinate startingLocation, int boardSize);

    public List<BoardCoordinate> getMovesFrom(BoardCoordinate startingLocation) {
        return getMovesFrom(startingLocation, Board.DEFAULT_BOARD_SIZE);
    }

    public boolean isFirstPlayerPiece() {
        return mIsFirstPlayerPiece;
    }

    protected static List<BoardCoordinate> getAllRadialMovesFrom(BoardCoordinate startingLocation, int distance) {
        List<BoardCoordinate> moves = new ArrayList<>();

        moves.addAll(getRadialAdjacentFromInclusive(startingLocation, distance));
        moves.addAll(getRadialDiagonalFromInclusive(startingLocation, distance));

        return moves;
    }

    protected static List<BoardCoordinate> getRadialDiagonalFromInclusive(BoardCoordinate startingLocation, int distance) {
        List<BoardCoordinate> moves = new ArrayList<>();

        for (int index = 1; index <= distance; index++) {
            moves.addAll(getRadialDiagonalFrom(startingLocation, index));
        }

        return moves;
    }

    protected static List<BoardCoordinate> getRadialDiagonalFrom(BoardCoordinate startingLocation, int distance) {
        List<BoardCoordinate> moves = new ArrayList<>(4);

        moves.add(BoardCoordinate.at(startingLocation.x + distance, startingLocation.y + distance));
        moves.add(BoardCoordinate.at(startingLocation.x + distance, startingLocation.y - distance));
        moves.add(BoardCoordinate.at(startingLocation.x - distance, startingLocation.y + distance));
        moves.add(BoardCoordinate.at(startingLocation.x - distance, startingLocation.y - distance));

        return moves;
    }

    protected static List<BoardCoordinate> getRadialAdjacentFromInclusive(BoardCoordinate startingLocation, int distance) {
        List<BoardCoordinate> moves = new ArrayList<>();

        for (int index = 1; index <= distance; index++) {
            moves.addAll(getRadialAdjacentFrom(startingLocation, index));
        }

        return moves;
    }

    protected static List<BoardCoordinate> getRadialAdjacentFrom(BoardCoordinate startingLocation, int distance) {
        List<BoardCoordinate> moves = new ArrayList<>(4);

        moves.add(BoardCoordinate.at(startingLocation.x + distance, startingLocation.y));
        moves.add(BoardCoordinate.at(startingLocation.x - distance, startingLocation.y));
        moves.add(BoardCoordinate.at(startingLocation.x, startingLocation.y + distance));
        moves.add(BoardCoordinate.at(startingLocation.x, startingLocation.y - distance));

        return moves;
    }
}
