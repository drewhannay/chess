package com.drewhannay.chesscrafter.utility;

import com.drewhannay.chesscrafter.logic.GameBuilder;
import com.drewhannay.chesscrafter.logic.GameConfiguration;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class GsonUtilityTest {

    GameConfiguration mGameConfig;

    @Before
    public void setUp() {
        mGameConfig = GameBuilder.getClassicConfiguration();
    }

    public static class ToJson extends GsonUtilityTest {
        @Test
        public void returnsValidJsonForClassicChess() {
            String json = GsonUtility.toJson(mGameConfig);
            assertNotNull(json);
        }
    }

    public static class FromJson extends GsonUtilityTest {
        @Test
        public void returnsNotNullGameConfigurationForClassicChess() {
            String json = GsonUtility.toJson(mGameConfig);
            GameConfiguration gameConfig = GsonUtility.fromJson(json);
            assertNotNull(gameConfig);
        }
    }
}
