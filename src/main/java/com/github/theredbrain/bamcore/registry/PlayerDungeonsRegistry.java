package com.github.theredbrain.bamcore.registry;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.BetterAdventureModeCoreClient;
import com.github.theredbrain.bamcore.api.dimensions.PlayerDungeon;
import com.github.theredbrain.bamcore.api.dimensions.PlayerDungeonHelper;
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

public class PlayerDungeonsRegistry {

    static Map<Identifier, PlayerDungeon> registeredPlayerDungeons = new HashMap<>();

    public static void register(Identifier itemId, PlayerDungeon playerDungeon) {
        registeredPlayerDungeons.put(itemId, playerDungeon);
    }

    public static PlayerDungeon getDungeon(Identifier dungeonId) {
        return registeredPlayerDungeons.get(dungeonId);
    }

    public static void init() {
        ServerLifecycleEvents.SERVER_STARTED.register((minecraftServer) -> {
            loadDungeons(minecraftServer.getResourceManager());
            encodeRegistry();
        });
    }

    private static void loadDungeons(ResourceManager resourceManager) {
        var gson = new Gson();
        Map<Identifier, PlayerDungeon> registeredPlayerDungeons = new HashMap();
        // Reading all attribute files
        for (var entry : resourceManager.findResources("player_dimensions/player_dungeons", fileName -> fileName.getPath().endsWith(".json")).entrySet()) {
            var identifier = entry.getKey();
            var resource = entry.getValue();
            try {
                // System.out.println("Checking resource: " + identifier);
                JsonReader reader = new JsonReader(new InputStreamReader(resource.getInputStream()));
                PlayerDungeon playerDungeon = PlayerDungeonHelper.decode(reader);
                var id = identifier
                        .toString().replace("player_dimensions/player_dungeons/", "");
                id = id.substring(0, id.lastIndexOf('.'));
                registeredPlayerDungeons.put(new Identifier(id), playerDungeon);
            } catch (Exception e) {
                System.err.println("Failed to parse: " + identifier);
                e.printStackTrace();
            }
        }
        PlayerDungeonsRegistry.registeredPlayerDungeons = registeredPlayerDungeons;
    }

    // NETWORK SYNC

    private static PacketByteBuf encodedRegisteredPlayerDungeons = PacketByteBufs.create();

    public static void encodeRegistry() {
        PacketByteBuf buffer = PacketByteBufs.create();
        var gson = new Gson();
        var json = gson.toJson(registeredPlayerDungeons);
        if (BetterAdventureModeCoreClient.clientConfig.show_debug_log) {
            BetterAdventureModeCore.LOGGER.info("Player Houses registry loaded: " + json);
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

        if (BetterAdventureModeCoreClient.clientConfig.show_debug_log) {
            BetterAdventureModeCore.LOGGER.info("Encoded Player Dungeons registry size (with package overhead): " + buffer.readableBytes()
                    + " bytes (in " + chunks.size() + " string chunks with the size of " + chunkSize + ")");
        }
        encodedRegisteredPlayerDungeons = buffer;
    }

    public static void decodeRegistry(PacketByteBuf buffer) {
        var chunkCount = buffer.readInt();
        String json = "";
        for (int i = 0; i < chunkCount; ++i) {
            json = json.concat(buffer.readString());
        }
        if (BetterAdventureModeCoreClient.clientConfig.show_debug_log) {
            BetterAdventureModeCore.LOGGER.info("Decoded Player Dungeons registry in " + chunkCount + " string chunks");
            BetterAdventureModeCore.LOGGER.info("Player Dungeons registry received: " + json);
        }
        var gson = new Gson();
        Type mapType = new TypeToken<Map<String, PlayerDungeon>>() {}.getType();
        Map<String, PlayerDungeon> readRegisteredPlayerDungeons = gson.fromJson(json, mapType);
        Map<Identifier, PlayerDungeon> newRegisteredPlayerDungeons = new HashMap();
        readRegisteredPlayerDungeons.forEach((key, value) -> {
            newRegisteredPlayerDungeons.put(new Identifier(key), value);
        });
        registeredPlayerDungeons = newRegisteredPlayerDungeons;
    }

    public static PacketByteBuf getEncodedRegistry() {
        return encodedRegisteredPlayerDungeons;
    }
}
