package com.github.theredbrain.bamcore.api.json_files_backend;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.Reader;
import java.lang.reflect.Type;

public class PlayerLocationHelper {

    private static Type registeredPlayerLocationsFileFormat = new TypeToken<PlayerLocation>() {}.getType();

    public static PlayerLocation decode(Reader reader) {
        var gson = new Gson();
        PlayerLocation playerLocation = gson.fromJson(reader, registeredPlayerLocationsFileFormat);
        return playerLocation;
    }

    public static PlayerLocation decode(JsonReader json) {
        var gson = new Gson();
        PlayerLocation playerLocation = gson.fromJson(json, registeredPlayerLocationsFileFormat);
        return playerLocation;
    }

    public static String encode(PlayerLocation playerLocation) {
        var gson = new Gson();
        return gson.toJson(playerLocation);
    }
}
