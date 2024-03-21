package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.data.WeaponPosesContainer;
import com.github.theredbrain.betteradventuremode.data.WeaponPoses;
import com.github.theredbrain.betteradventuremode.data.WeaponPosesHelper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeaponPosesRegistry {

    static Map<Identifier, WeaponPoses> registeredWeaponPoses = new HashMap<>();
    static Map<Identifier, WeaponPosesContainer> weaponPosesContainers = new HashMap();

    public static void register(Identifier itemId, WeaponPoses weaponPoses) {
        registeredWeaponPoses.put(itemId, weaponPoses);
    }

    public static WeaponPoses getWeaponPoses(Identifier itemId) {
        return registeredWeaponPoses.get(itemId);
    }

    public static void init() {
        ServerLifecycleEvents.SERVER_STARTED.register((minecraftServer) -> {
            loadWeaponPoses(minecraftServer.getResourceManager());
            encodeRegistry();
        });
    }

    public static void loadWeaponPoses(ResourceManager resourceManager) {
        loadWeaponPosesContainers(resourceManager);

        // Resolving parents
        weaponPosesContainers.forEach( (itemId, container) -> {
            if (!Registries.ITEM.containsId(itemId)) {
                return;
            }
            resolveAndRegisterAttributes(itemId, container);
        });
    }

    private static void loadWeaponPosesContainers(ResourceManager resourceManager) {
        var gson = new Gson();
        Map<Identifier, WeaponPosesContainer> weaponPosesContainers = new HashMap();
        // Reading all attribute files
        for (var entry : resourceManager.findResources("weapon_poses", fileName -> fileName.getPath().endsWith(".json")).entrySet()) {
            var identifier = entry.getKey();
            var resource = entry.getValue();
            try {
                // System.out.println("Checking resource: " + identifier);
                JsonReader reader = new JsonReader(new InputStreamReader(resource.getInputStream()));
                WeaponPosesContainer container = WeaponPosesHelper.decode(reader);
                var id = identifier
                        .toString().replace("weapon_poses/", "");
                id = id.substring(0, id.lastIndexOf('.'));
                weaponPosesContainers.put(new Identifier(id), container);
            } catch (Exception e) {
                System.err.println("Failed to parse: " + identifier);
                e.printStackTrace();
            }
        }
        WeaponPosesRegistry.weaponPosesContainers = weaponPosesContainers;
    }

    public static WeaponPoses resolveWeaponPoses(Identifier itemId, WeaponPosesContainer weaponPosesContainer) {
        try {
            ArrayList<WeaponPoses> resolutionChain = new ArrayList();
            WeaponPosesContainer current = weaponPosesContainer;
            while (current != null) {
                resolutionChain.add(0, current.weaponPoses());
                if (current.parent() != null) {
                    current = WeaponPosesRegistry.weaponPosesContainers.get(new Identifier(current.parent()));
                } else {
                    current = null;
                }
            }

            var empty = new WeaponPoses(null, null, null,null,null);
            var resolvedWeaponPoses = resolutionChain
                    .stream()
                    .reduce(empty, (a, b) -> {
                        if (b == null) { // I'm not sure why null can enter as `b`
                            return a;
                        }
                        return WeaponPosesHelper.override(a, b);
                    });

            return resolvedWeaponPoses;
        } catch (Exception e) {
            BetterAdventureMode.error("Failed to resolve weapon attributes for: " + itemId + ". Reason: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static void resolveAndRegisterAttributes(Identifier itemId, WeaponPosesContainer container) {
        var resolvedWeaponPoses = resolveWeaponPoses(itemId, container);
        if (resolvedWeaponPoses != null) {
            register(itemId, resolvedWeaponPoses);
        }
    }

    // NETWORK SYNC

    private static PacketByteBuf encodedRegisteredWeaponPoses = PacketByteBufs.create();

    public static void encodeRegistry() {
        PacketByteBuf buffer = PacketByteBufs.create();
        var gson = new Gson();
        var json = gson.toJson(registeredWeaponPoses);
        if (BetterAdventureMode.serverConfig.show_debug_log) {
            BetterAdventureMode.LOGGER.info("WeaponPoses registry loaded: " + json);
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
            BetterAdventureMode.LOGGER.info("Encoded WeaponPoses registry size (with package overhead): " + buffer.readableBytes()
                    + " bytes (in " + chunks.size() + " string chunks with the size of " + chunkSize + ")");
        }
        encodedRegisteredWeaponPoses = buffer;
    }

    public static void decodeRegistry(PacketByteBuf buffer) {
        var chunkCount = buffer.readInt();
        String json = "";
        for (int i = 0; i < chunkCount; ++i) {
            json = json.concat(buffer.readString());
        }
        if (BetterAdventureMode.serverConfig.show_debug_log) {
            BetterAdventureMode.info("Decoded WeaponPoses registry in " + chunkCount + " string chunks");
            BetterAdventureMode.info("WeaponPoses registry received: " + json);
        }
        var gson = new Gson();
        Type mapType = new TypeToken<Map<String, WeaponPoses>>() {}.getType();
        Map<String, WeaponPoses> readRegisteredWeaponPoses = gson.fromJson(json, mapType);
        Map<Identifier, WeaponPoses> newRegisteredWeaponPoses = new HashMap();
        readRegisteredWeaponPoses.forEach((key, value) -> {
            newRegisteredWeaponPoses.put(new Identifier(key), value);
        });
        registeredWeaponPoses = newRegisteredWeaponPoses;
    }

    public static PacketByteBuf getEncodedRegistry() {
        return encodedRegisteredWeaponPoses;
    }
}
