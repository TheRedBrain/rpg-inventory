package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.BetterAdventureModeClient;
import com.github.theredbrain.betteradventuremode.data.Location;
import com.github.theredbrain.betteradventuremode.data.LocationHelper;
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

public class LocationsRegistry {

    static Map<Identifier, Location> registeredLocations = new HashMap<>();

    public static void register(Identifier itemId, Location location) {
        registeredLocations.put(itemId, location);
    }

    public static Location getLocation(Identifier locationId) {
        return registeredLocations.get(locationId);
    }

    public static void init() {
        ServerLifecycleEvents.SERVER_STARTED.register((minecraftServer) -> {
            loadLocations(minecraftServer.getResourceManager());
            encodeRegistry();
        });
    }

    private static void loadLocations(ResourceManager resourceManager) {
        var gson = new Gson();
        Map<Identifier, Location> registeredLocations = new HashMap();
        // Reading all attribute files
        for (var entry : resourceManager.findResources("locations", fileName -> fileName.getPath().endsWith(".json")).entrySet()) {
            var identifier = entry.getKey();
            var resource = entry.getValue();
            try {
                // System.out.println("Checking resource: " + identifier);
                JsonReader reader = new JsonReader(new InputStreamReader(resource.getInputStream()));
                Location location = LocationHelper.decode(reader);
                var id = identifier
                        .toString().replace("locations/", "");
                id = id.substring(0, id.lastIndexOf('.'));
                registeredLocations.put(new Identifier(id), location);
            } catch (Exception e) {
                System.err.println("Failed to parse: " + identifier);
                e.printStackTrace();
            }
        }
        LocationsRegistry.registeredLocations = registeredLocations;
    }

    // NETWORK SYNC

    private static PacketByteBuf encodedRegisteredLocations = PacketByteBufs.create();

    public static void encodeRegistry() {
        PacketByteBuf buffer = PacketByteBufs.create();
        var gson = new Gson();
        var json = gson.toJson(registeredLocations);
        if (BetterAdventureMode.serverConfig.show_debug_log) {
            BetterAdventureMode.LOGGER.info("Locations registry loaded: " + json);
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

        if (BetterAdventureMode.serverConfig.show_debug_log) {
            BetterAdventureMode.LOGGER.info("Encoded Locations registry size (with package overhead): " + buffer.readableBytes()
                    + " bytes (in " + chunks.size() + " string chunks with the size of " + chunkSize + ")");
        }
        encodedRegisteredLocations = buffer;
    }

    public static void decodeRegistry(PacketByteBuf buffer) {
        var chunkCount = buffer.readInt();
        String json = "";
        for (int i = 0; i < chunkCount; ++i) {
            json = json.concat(buffer.readString());
        }
        if (BetterAdventureMode.serverConfig.show_debug_log) {
            BetterAdventureMode.info("Decoded Locations registry in " + chunkCount + " string chunks");
            BetterAdventureMode.info("Locations registry received: " + json);
        }
        var gson = new Gson();
        Type mapType = new TypeToken<Map<String, Location>>() {}.getType();
        Map<String, Location> readRegisteredLocations = gson.fromJson(json, mapType);
        Map<Identifier, Location> newRegisteredLocations = new HashMap();
        readRegisteredLocations.forEach((key, value) -> {
            newRegisteredLocations.put(new Identifier(key), value);
        });
        registeredLocations = newRegisteredLocations;
    }

    public static PacketByteBuf getEncodedRegistry() {
        return encodedRegisteredLocations;
    }
}
