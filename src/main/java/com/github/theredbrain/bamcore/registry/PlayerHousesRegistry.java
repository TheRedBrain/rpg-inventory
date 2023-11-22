package com.github.theredbrain.bamcore.registry;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.BetterAdventureModeCoreClient;
import com.github.theredbrain.bamcore.api.dimensions.PlayerHouse;
import com.github.theredbrain.bamcore.api.dimensions.PlayerHouseHelper;
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

public class PlayerHousesRegistry {

    static Map<Identifier, PlayerHouse> registeredHouses = new HashMap<>();

    public static void register(Identifier itemId, PlayerHouse playerHouse) {
        registeredHouses.put(itemId, playerHouse);
    }

    public static PlayerHouse getHouse(Identifier houseId) {
        return registeredHouses.get(houseId);
    }

    public static void init() {
        ServerLifecycleEvents.SERVER_STARTED.register((minecraftServer) -> {
            loadHouses(minecraftServer.getResourceManager());
            encodeRegistry();
        });
    }

    private static void loadHouses(ResourceManager resourceManager) {
        var gson = new Gson();
        Map<Identifier, PlayerHouse> registeredHouses = new HashMap();
        // Reading all attribute files
        for (var entry : resourceManager.findResources("player_dimensions/player_houses", fileName -> fileName.getPath().endsWith(".json")).entrySet()) {
            var identifier = entry.getKey();
            var resource = entry.getValue();
            try {
                // System.out.println("Checking resource: " + identifier);
                JsonReader reader = new JsonReader(new InputStreamReader(resource.getInputStream()));
                PlayerHouse playerHouse = PlayerHouseHelper.decode(reader);
                var id = identifier
                        .toString().replace("player_dimensions/player_houses/", "");
                id = id.substring(0, id.lastIndexOf('.'));
                registeredHouses.put(new Identifier(id), playerHouse);
            } catch (Exception e) {
                System.err.println("Failed to parse: " + identifier);
                e.printStackTrace();
            }
        }
        PlayerHousesRegistry.registeredHouses = registeredHouses;
    }

    // NETWORK SYNC

    private static PacketByteBuf encodedRegisteredHouses = PacketByteBufs.create();

    public static void encodeRegistry() {
        PacketByteBuf buffer = PacketByteBufs.create();
        var gson = new Gson();
        var json = gson.toJson(registeredHouses);
//        if (BetterCombat.config.weapon_registry_logging) {
//            LOGGER.info("Weapon Attribute registry loaded: " + json);
//        }

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
            BetterAdventureModeCore.LOGGER.info("Encoded Weapon Attribute registry size (with package overhead): " + buffer.readableBytes()
                    + " bytes (in " + chunks.size() + " string chunks with the size of " + chunkSize + ")");
        }
        encodedRegisteredHouses = buffer;
    }

    public static void decodeRegistry(PacketByteBuf buffer) {
        var chunkCount = buffer.readInt();
        String json = "";
        for (int i = 0; i < chunkCount; ++i) {
            json = json.concat(buffer.readString());
        }
        if (BetterAdventureModeCoreClient.clientConfig.show_debug_log) {
            BetterAdventureModeCore.LOGGER.info("Decoded Weapon Attribute registry in " + chunkCount + " string chunks");
            BetterAdventureModeCore.LOGGER.info("Weapon Attribute registry received: " + json);
        }
        var gson = new Gson();
        Type mapType = new TypeToken<Map<String, PlayerHouse>>() {}.getType();
        Map<String, PlayerHouse> readRegisteredHouses = gson.fromJson(json, mapType);
        Map<Identifier, PlayerHouse> newRegisteredHouses = new HashMap();
        readRegisteredHouses.forEach((key, value) -> {
            newRegisteredHouses.put(new Identifier(key), value);
        });
        registeredHouses = newRegisteredHouses;
    }

    public static PacketByteBuf getEncodedRegistry() {
        return encodedRegisteredHouses;
    }
}
