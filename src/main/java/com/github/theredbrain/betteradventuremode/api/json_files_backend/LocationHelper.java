package com.github.theredbrain.betteradventuremode.api.json_files_backend;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.Reader;
import java.lang.reflect.Type;

public class LocationHelper {

    private static Type registeredLocationsFileFormat = new TypeToken<Location>() {}.getType();

    public static Location decode(Reader reader) {
        var gson = new Gson();
        Location location = gson.fromJson(reader, registeredLocationsFileFormat);
        return location;
    }

    public static Location decode(JsonReader json) {
        var gson = new Gson();
        Location location = gson.fromJson(json, registeredLocationsFileFormat);
        return location;
    }

    public static String encode(Location location) {
        var gson = new Gson();
        return gson.toJson(location);
    }
}
