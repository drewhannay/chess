package com.drewhannay.chesscrafter.controllers;

import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.Game;
import com.drewhannay.chesscrafter.models.Move;
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

    public static Set<BoardCoordinate> getLegalDestinations(Board board, BoardCoordinate coordinate) {
        return board.getMovesFrom(coordinate);
    }

    public static void playMove(Move move) {
        sGame.executeMove(move);
    }

    private static Game sGame;
}
