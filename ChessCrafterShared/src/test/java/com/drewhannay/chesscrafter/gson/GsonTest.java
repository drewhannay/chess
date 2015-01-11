package com.drewhannay.chesscrafter.gson;

import com.drewhannay.chesscrafter.logic.GameBuilder;
import com.drewhannay.chesscrafter.models.Game;
import com.drewhannay.chesscrafter.utility.GsonUtility;
import com.google.gson.Gson;
import junit.framework.Assert;
import org.junit.Test;

public class GsonTest {
    @Test
    public final void test() {
        Gson gson = GsonUtility.getGson();

        Game game = GameBuilder.buildClassic();
        Assert.assertTrue(game != null);

        String json = gson.toJson(game);
        Assert.assertTrue(json != null);

        Game parsedGame = gson.fromJson(json, Game.class);
        Assert.assertEquals(game, parsedGame);
    }
}
