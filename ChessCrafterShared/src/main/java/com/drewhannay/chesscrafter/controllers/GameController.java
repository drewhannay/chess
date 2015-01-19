package com.drewhannay.chesscrafter.controllers;

import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.Game;
import com.drewhannay.chesscrafter.models.Move;
import com.drewhannay.chesscrafter.models.Team;
import com.drewhannay.chesscrafter.rules.movefilter.MoveFilter;
import com.google.common.base.Preconditions;

import java.util.Set;

public final class GameController {
    private GameController() {
    }

    public static void setGame(Game game) {
        sGame = game;
    }

    public static Game getGame() {
        verifyGameIsSet();

        return sGame;
    }

    private static void verifyGameIsSet() {
        Preconditions.checkArgument(sGame != null);
    }

    public static Set<BoardCoordinate> getLegalDestinations(int teamId, int boardIndex, BoardCoordinate coordinate) {
        Board board = sGame.getBoards()[boardIndex];

        Set<BoardCoordinate> moves = board.getMovesFrom(coordinate);

        for (MoveFilter moveFilter : getTeam(teamId).getRules().getMoveFilters()) {
            moves = moveFilter.filterMoves(board, coordinate, moves);
        }

        return moves;
    }

    public static void playMove(Move move) {
        sGame.executeMove(move);
    }

    // TODO: this method is duplicated in Game; find a better home for it
    private static Team getTeam(int teamId) {
        for (Team team : sGame.getTeams()) {
            if (team.getTeamId() == teamId) {
                return team;
            }
        }
        throw new IllegalArgumentException("invalid teamId");
    }

    private static Game sGame;
}
