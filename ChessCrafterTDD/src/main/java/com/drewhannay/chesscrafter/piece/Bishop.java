package com.drewhannay.chesscrafter.piece;

import com.drewhannay.chesscrafter.board.Board;
import com.drewhannay.chesscrafter.board.BoardCoordinate;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

    public Bishop() {
        this(Board.FIRST_TEAM_ID);
    }

    public Bishop(int teamId) {
        super(teamId);
    }

    @Override
    public List<BoardCoordinate> getMovesFrom(BoardCoordinate startingLocation, int boardSize) {
        List<BoardCoordinate> moves = new ArrayList<>();

        for (int index = 1; index <= boardSize; index++) {
            for (BoardCoordinate move : getRadialDiagonalFrom(startingLocation, index)) {
                if (move.isCoordinateValidForBoardSize(boardSize)) {
                    moves.add(move);
                }
            }
        }

        return moves;
    }
}
