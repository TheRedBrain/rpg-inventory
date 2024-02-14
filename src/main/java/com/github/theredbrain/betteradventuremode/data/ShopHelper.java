package com.github.theredbrain.betteradventuremode.data;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.Reader;
import java.lang.reflect.Type;

public class ShopHelper {

    private static Type registeredShopsFileFormat = new TypeToken<Shop>() {}.getType();

    public static Shop decode(Reader reader) {
        var gson = new Gson();
        Shop shop = gson.fromJson(reader, registeredShopsFileFormat);
        return shop;
    }

    public static Shop decode(JsonReader json) {
        var gson = new Gson();
        Shop shop = gson.fromJson(json, registeredShopsFileFormat);
        return shop;
    }

    public static String encode(Shop shop) {
        var gson = new Gson();
        return gson.toJson(shop);
    }
}
