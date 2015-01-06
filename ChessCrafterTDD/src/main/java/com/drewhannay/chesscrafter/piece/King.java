package com.drewhannay.chesscrafter.piece;

import com.drewhannay.chesscrafter.board.BoardCoordinate;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    public King() {
        this(true);
    }

    public King(boolean isFirstPlayerPiece) {
        super(isFirstPlayerPiece);
    }

    @Override
    public List<BoardCoordinate> getMovesFrom(BoardCoordinate startingLocation, int boardSize) {
        List<BoardCoordinate> moves = new ArrayList<>();

        List<BoardCoordinate> oneSquareAwayMoves = getAllRadialMovesFrom(startingLocation, 1);
        for (BoardCoordinate move : oneSquareAwayMoves) {
            if (move.isCoordinateValidForBoardSize(boardSize)) {
                moves.add(move);
            }
        }

        return moves;
    }
}
