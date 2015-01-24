package com.drewhannay.chesscrafter.utility;

import com.drewhannay.chesscrafter.logic.GameBuilder;
import com.drewhannay.chesscrafter.logic.GameConfiguration;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.Game;
import com.drewhannay.chesscrafter.models.History;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GsonUtilityTest {

    GameConfiguration mGameConfig;
    History mHistory;

    @Before
    public void setUp() {
        mGameConfig = GameBuilder.getClassicConfiguration();

        Game game = GameBuilder.buildGame(mGameConfig);
        game.executeMove(game.newMoveBuilder(BoardCoordinate.at(1, 2), BoardCoordinate.at(1, 3)).build());
        game.executeMove(game.newMoveBuilder(BoardCoordinate.at(1, 7), BoardCoordinate.at(1, 6)).build());
        game.executeMove(game.newMoveBuilder(BoardCoordinate.at(1, 3), BoardCoordinate.at(1, 4)).build());
        game.executeMove(game.newMoveBuilder(BoardCoordinate.at(1, 6), BoardCoordinate.at(1, 5)).build());

        mHistory = game.getHistory();
    }

    public static class ToJson extends GsonUtilityTest {
        @Test
        public void returnsValidJsonForClassicConfiguration() {
            String json = GsonUtility.toJson(mGameConfig);
            assertNotNull(json);
        }

        @Test
        public void returnsValidJsonForHistory() {
            String json = GsonUtility.toJson(mHistory);
            assertNotNull(json);
        }
    }

    public static class FromJson extends GsonUtilityTest {
        @Test
        public void returnsNotNullGameConfigurationForClassicChess() {
            String json = GsonUtility.toJson(mGameConfig);
            GameConfiguration gameConfig = GsonUtility.fromJson(json, GameConfiguration.class);
            assertNotNull(gameConfig);
        }

        @Test
        public void returnsNotNullHistory() {
            String json = GsonUtility.toJson(mHistory);
            History history = GsonUtility.fromJson(json, History.class);
            assertNotNull(history);
        }

        @Test
        public void returnsSameVariantNameForHistory() {
            String json = GsonUtility.toJson(mHistory);
            History history = GsonUtility.fromJson(json, History.class);
            assertEquals(mHistory.variantName, history.variantName);
        }

        @Test
        public void returnsSameMoveListForHistory() {
            String json = GsonUtility.toJson(mHistory);
            History history = GsonUtility.fromJson(json, History.class);
            assertEquals(mHistory.moves, history.moves);
        }
    }
}
