package com.github.theredbrain.bamcore.api.dimensions;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.Reader;
import java.lang.reflect.Type;

public class PlayerDungeonHelper {

    private static Type registeredPlayerDungeonsFileFormat = new TypeToken<PlayerDungeon>() {}.getType();

    public static PlayerDungeon decode(Reader reader) {
        var gson = new Gson();
        PlayerDungeon playerDungeon = gson.fromJson(reader, registeredPlayerDungeonsFileFormat);
        return playerDungeon;
    }

    public static PlayerDungeon decode(JsonReader json) {
        var gson = new Gson();
        PlayerDungeon playerDungeon = gson.fromJson(json, registeredPlayerDungeonsFileFormat);
        return playerDungeon;
    }

    public static String encode(PlayerDungeon playerDungeon) {
        var gson = new Gson();
        return gson.toJson(playerDungeon);
    }
}
