package com.drewhannay.chesscrafter.piece;

import com.drewhannay.chesscrafter.board.BoardCoordinate;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {

    public Rook() {
        this(true);
    }

    public Rook(boolean isFirstPlayerPiece) {
        super(isFirstPlayerPiece);
    }

    @Override
    public List<BoardCoordinate> getMovesFrom(BoardCoordinate startingLocation, int boardSize) {
        List<BoardCoordinate> moves = new ArrayList<>();

        for (int index = 1; index <= boardSize; index++) {
            if (index != startingLocation.y) {
                moves.add(BoardCoordinate.at(startingLocation.x, index));
            }
            if (index != startingLocation.x) {
                moves.add(BoardCoordinate.at(index, startingLocation.y));
            }
        }

        return moves;
    }
}
