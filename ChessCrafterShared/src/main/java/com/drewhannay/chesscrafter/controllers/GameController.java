package com.drewhannay.chesscrafter.controllers;

import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.Game;
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

    public static Set<BoardCoordinate> getLegalDestinations(int boardIndex, BoardCoordinate coordinate) {
        return sGame.getMovesFrom(boardIndex, coordinate);
    }

    private static Game sGame;
}
