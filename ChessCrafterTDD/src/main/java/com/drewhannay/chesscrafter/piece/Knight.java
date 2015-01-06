package com.drewhannay.chesscrafter.piece;

import com.drewhannay.chesscrafter.board.Board;
import com.drewhannay.chesscrafter.board.BoardCoordinate;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    public Knight() {
        this(Board.FIRST_TEAM_ID);
    }

    public Knight(int teamId) {
        super(teamId);
    }

    @Override
    public List<BoardCoordinate> getMovesFrom(BoardCoordinate startingLocation, int boardSize) {
        List<BoardCoordinate> allMoves = new ArrayList<>();

        for (int quadrant = 1; quadrant <= 4; quadrant++) {
            allMoves.addAll(getQuadrantMoves(startingLocation, quadrant));
        }

        List<BoardCoordinate> validMoves = new ArrayList<>(allMoves.size());

        for (BoardCoordinate move : allMoves) {
            if (move.isCoordinateValidForBoardSize(boardSize)) {
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    private List<BoardCoordinate> getQuadrantMoves(BoardCoordinate coordinate, int quadrant) {
        int xMultiplier = quadrant == 1 || quadrant == 4 ? 1 : -1;
        int yMultiplier = quadrant == 1 || quadrant == 2 ? 1 : -1;

        List<BoardCoordinate> moves = new ArrayList<>(2);
        moves.add(BoardCoordinate.at(coordinate.x + 2 * xMultiplier, coordinate.y + 1 * yMultiplier));
        moves.add(BoardCoordinate.at(coordinate.x + 1 * xMultiplier, coordinate.y + 2 * yMultiplier));
        return moves;
    }
}
