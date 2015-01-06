package com.drewhannay.chesscrafter.piece;

import com.drewhannay.chesscrafter.board.Board;
import com.drewhannay.chesscrafter.board.BoardCoordinate;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    public King() {
        this(Board.FIRST_TEAM_ID);
    }

    public King(int teamId) {
        super(teamId);
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
