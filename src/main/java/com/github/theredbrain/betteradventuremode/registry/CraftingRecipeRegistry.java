package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.BetterAdventureModeClient;
import com.github.theredbrain.betteradventuremode.api.json_files_backend.CraftingRecipe;
import com.github.theredbrain.betteradventuremode.api.json_files_backend.CraftingRecipeHelper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CraftingRecipeRegistry {

    static Map<Identifier, CraftingRecipe> registeredCraftingRecipes = new HashMap<>();

    public static void register(Identifier itemId, CraftingRecipe craftingRecipe) {
        registeredCraftingRecipes.put(itemId, craftingRecipe);
    }

    public static CraftingRecipe getCraftingRecipe(Identifier craftingRecipeId) {
        return registeredCraftingRecipes.get(craftingRecipeId);
    }

    public static Map<Identifier, CraftingRecipe> getCraftingRecipes() {
        return registeredCraftingRecipes;
    }

    public static List<Identifier> getCraftingRecipeIdentifiers() {
        return registeredCraftingRecipes.keySet().stream().toList();
    }

    public static void init() {
        ServerLifecycleEvents.SERVER_STARTED.register((minecraftServer) -> {
            loadCraftingRecipes(minecraftServer.getResourceManager());
            encodeRegistry();
        });
    }

    private static void loadCraftingRecipes(ResourceManager resourceManager) {
        var gson = new Gson();
        Map<Identifier, CraftingRecipe> registeredCraftingRecipes = new HashMap();
        // Reading all attribute files
        for (var entry : resourceManager.findResources("crafting_recipes", fileName -> fileName.getPath().endsWith(".json")).entrySet()) {
            var identifier = entry.getKey();
            var resource = entry.getValue();
            try {
                // System.out.println("Checking resource: " + identifier);
                JsonReader reader = new JsonReader(new InputStreamReader(resource.getInputStream()));
                CraftingRecipe craftingRecipe = CraftingRecipeHelper.decode(reader);
                var id = identifier
                        .toString().replace("crafting_recipes/", "");
                id = id.substring(0, id.lastIndexOf('.'));
                registeredCraftingRecipes.put(new Identifier(id), craftingRecipe);
            } catch (Exception e) {
                System.err.println("Failed to parse: " + identifier);
                e.printStackTrace();
            }
        }
        CraftingRecipeRegistry.registeredCraftingRecipes = registeredCraftingRecipes;
    }

    // NETWORK SYNC

    private static PacketByteBuf encodedRegisteredCraftingRecipes = PacketByteBufs.create();

    public static void encodeRegistry() {
        PacketByteBuf buffer = PacketByteBufs.create();
        var gson = new Gson();
        var json = gson.toJson(registeredCraftingRecipes);
        if (BetterAdventureModeClient.clientConfig.show_debug_log) {
            BetterAdventureMode.LOGGER.info("Crafting Recipes registry loaded: " + json);
        }

        List<String> chunks = new ArrayList<>();
        var chunkSize = 10000;
        for (int i = 0; i < json.length(); i += chunkSize) {
            chunks.add(json.substring(i, Math.min(json.length(), i + chunkSize)));
        }

        buffer.writeInt(chunks.size());
        for (var chunk: chunks) {
            buffer.writeString(chunk);
        }

        if (BetterAdventureModeClient.clientConfig.show_debug_log) {
            BetterAdventureMode.LOGGER.info("Encoded Crafting Recipes registry size (with package overhead): " + buffer.readableBytes()
                    + " bytes (in " + chunks.size() + " string chunks with the size of " + chunkSize + ")");
        }
        encodedRegisteredCraftingRecipes = buffer;
    }

    public static void decodeRegistry(PacketByteBuf buffer) {
        var chunkCount = buffer.readInt();
        String json = "";
        for (int i = 0; i < chunkCount; ++i) {
            json = json.concat(buffer.readString());
        }
        if (BetterAdventureModeClient.clientConfig.show_debug_log) {
            BetterAdventureMode.info("Decoded Crafting Recipes registry in " + chunkCount + " string chunks");
            BetterAdventureMode.info("Crafting Recipes registry received: " + json);
        }
        var gson = new Gson();
        Type mapType = new TypeToken<Map<String, CraftingRecipe>>() {}.getType();
        Map<String, CraftingRecipe> readRegisteredCraftingRecipes = gson.fromJson(json, mapType);
        Map<Identifier, CraftingRecipe> newRegisteredCraftingRecipes = new HashMap();
        readRegisteredCraftingRecipes.forEach((key, value) -> {
            newRegisteredCraftingRecipes.put(new Identifier(key), value);
        });
        registeredCraftingRecipes = newRegisteredCraftingRecipes;
    }

    public static PacketByteBuf getEncodedRegistry() {
        return encodedRegisteredCraftingRecipes;
    }
}
