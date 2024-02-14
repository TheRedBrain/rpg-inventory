package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.BetterAdventureModeClient;
import com.github.theredbrain.betteradventuremode.data.Shop;
import com.github.theredbrain.betteradventuremode.data.ShopHelper;
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

public class ShopsRegistry {

    static Map<Identifier, Shop> registeredShops = new HashMap<>();

    public static void register(Identifier shopId, Shop shop) {
        registeredShops.put(shopId, shop);
    }

    public static Shop getShop(Identifier shopId) {
        return registeredShops.get(shopId);
    }

    public static void init() {
        ServerLifecycleEvents.SERVER_STARTED.register((minecraftServer) -> {
            loadShops(minecraftServer.getResourceManager());
            encodeRegistry();
        });
    }

    private static void loadShops(ResourceManager resourceManager) {
        var gson = new Gson();
        Map<Identifier, Shop> registeredShops = new HashMap();
        // Reading all attribute files
        for (var entry : resourceManager.findResources("shops", fileName -> fileName.getPath().endsWith(".json")).entrySet()) {
            var identifier = entry.getKey();
            var resource = entry.getValue();
            try {
                // System.out.println("Checking resource: " + identifier);
                JsonReader reader = new JsonReader(new InputStreamReader(resource.getInputStream()));
                Shop shop = ShopHelper.decode(reader);
                var id = identifier
                        .toString().replace("shops/", "");
                id = id.substring(0, id.lastIndexOf('.'));
                registeredShops.put(new Identifier(id), shop);
            } catch (Exception e) {
                System.err.println("Failed to parse: " + identifier);
                e.printStackTrace();
            }
        }
        ShopsRegistry.registeredShops = registeredShops;
    }

    // NETWORK SYNC

    private static PacketByteBuf encodedRegisteredShops = PacketByteBufs.create();

    public static void encodeRegistry() {
        PacketByteBuf buffer = PacketByteBufs.create();
        var gson = new Gson();
        var json = gson.toJson(registeredShops);
        if (BetterAdventureModeClient.clientConfig.show_debug_log) {
            BetterAdventureMode.LOGGER.info("Shops registry loaded: " + json);
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
            BetterAdventureMode.LOGGER.info("Encoded Shops registry size (with package overhead): " + buffer.readableBytes()
                    + " bytes (in " + chunks.size() + " string chunks with the size of " + chunkSize + ")");
        }
        encodedRegisteredShops = buffer;
    }

    public static void decodeRegistry(PacketByteBuf buffer) {
        var chunkCount = buffer.readInt();
        String json = "";
        for (int i = 0; i < chunkCount; ++i) {
            json = json.concat(buffer.readString());
        }
        if (BetterAdventureModeClient.clientConfig.show_debug_log) {
            BetterAdventureMode.LOGGER.info("Decoded Shops registry in " + chunkCount + " string chunks");
            BetterAdventureMode.LOGGER.info("Shops registry received: " + json);
        }
        var gson = new Gson();
        Type mapType = new TypeToken<Map<String, Shop>>() {}.getType();
        Map<String, Shop> readRegisteredShops = gson.fromJson(json, mapType);
        Map<Identifier, Shop> newRegisteredShops = new HashMap();
        readRegisteredShops.forEach((key, value) -> {
            newRegisteredShops.put(new Identifier(key), value);
        });
        registeredShops = newRegisteredShops;
    }

    public static PacketByteBuf getEncodedRegistry() {
        return encodedRegisteredShops;
    }
}
