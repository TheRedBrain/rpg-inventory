package com.github.theredbrain.betteradventuremode.api.json_files_backend;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.Reader;
import java.lang.reflect.Type;

public class CraftingRecipeHelper {

    private static Type registeredCraftingRecipesFileFormat = new TypeToken<CraftingRecipe>() {}.getType();

    public static CraftingRecipe decode(Reader reader) {
        var gson = new Gson();
        CraftingRecipe craftingRecipe = gson.fromJson(reader, registeredCraftingRecipesFileFormat);
        return craftingRecipe;
    }

    public static CraftingRecipe decode(JsonReader json) {
        var gson = new Gson();
        CraftingRecipe craftingRecipe = gson.fromJson(json, registeredCraftingRecipesFileFormat);
        return craftingRecipe;
    }

    public static String encode(CraftingRecipe craftingRecipe) {
        var gson = new Gson();
        return gson.toJson(craftingRecipe);
    }
}
