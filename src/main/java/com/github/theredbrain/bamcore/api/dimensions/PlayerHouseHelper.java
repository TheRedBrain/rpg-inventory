package com.github.theredbrain.bamcore.api.dimensions;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.Reader;
import java.lang.reflect.Type;

public class PlayerHouseHelper {

    private static Type registeredHousesFileFormat = new TypeToken<PlayerHouse>() {}.getType();

    public static PlayerHouse decode(Reader reader) {
        var gson = new Gson();
        PlayerHouse playerHouse = gson.fromJson(reader, registeredHousesFileFormat);
        return playerHouse;
    }

    public static PlayerHouse decode(JsonReader json) {
        var gson = new Gson();
        PlayerHouse playerHouse = gson.fromJson(json, registeredHousesFileFormat);
        return playerHouse;
    }

    public static String encode(PlayerHouse container) {
        var gson = new Gson();
        return gson.toJson(container);
    }
}
