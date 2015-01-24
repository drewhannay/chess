package com.drewhannay.chesscrafter.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;

public final class GsonUtility {

    private static final Gson mGson = new GsonBuilder().create();

    private GsonUtility() {
    }

    public static String toJson(@NotNull Object object) {
        return mGson.toJson(object);
    }

    public static <T> T fromJson(@NotNull String json, Class<T> klazz) {
        return mGson.fromJson(json, klazz);
    }
}
