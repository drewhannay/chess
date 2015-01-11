package com.drewhannay.chesscrafter.gson;

import com.drewhannay.chesscrafter.logic.GameBuilder;
import com.drewhannay.chesscrafter.models.Game;
import com.drewhannay.chesscrafter.utility.GsonUtility;
import com.google.gson.Gson;
import junit.framework.Assert;
import org.junit.*;

public class GsonTest {
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

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
