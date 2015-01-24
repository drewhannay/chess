package com.drewhannay.chesscrafter.utility;

import com.drewhannay.chesscrafter.logic.GameConfiguration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;

public final class GsonUtility {

    private static final Gson mGson = new GsonBuilder().create();

    private GsonUtility() {
    }

    public static String toJson(@NotNull GameConfiguration game) {
        return mGson.toJson(game);
    }

    public static GameConfiguration fromJson(@NotNull String json) {
        return mGson.fromJson(json, GameConfiguration.class);
    }
}
