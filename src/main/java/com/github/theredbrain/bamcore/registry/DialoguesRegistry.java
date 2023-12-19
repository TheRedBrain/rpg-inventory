package com.github.theredbrain.bamcore.registry;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.BetterAdventureModeCoreClient;
import com.github.theredbrain.bamcore.api.json_files_backend.Dialogue;
import com.github.theredbrain.bamcore.api.json_files_backend.DialogueHelper;
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

public class DialoguesRegistry {

    static Map<Identifier, Dialogue> registeredDialogues = new HashMap<>();

    public static void register(Identifier itemId, Dialogue playerLocation) {
        registeredDialogues.put(itemId, playerLocation);
    }

    public static Dialogue getDialogue(Identifier dialogueId) {
        return registeredDialogues.get(dialogueId);
    }

    public static void init() {
        ServerLifecycleEvents.SERVER_STARTED.register((minecraftServer) -> {
            loadDialogues(minecraftServer.getResourceManager());
            encodeRegistry();
        });
    }

    private static void loadDialogues(ResourceManager resourceManager) {
        var gson = new Gson();
        Map<Identifier, Dialogue> registeredDialogues = new HashMap();
        // Reading all attribute files
        for (var entry : resourceManager.findResources("dialogues", fileName -> fileName.getPath().endsWith(".json")).entrySet()) {
            var identifier = entry.getKey();
            var resource = entry.getValue();
            try {
                // System.out.println("Checking resource: " + identifier);
                JsonReader reader = new JsonReader(new InputStreamReader(resource.getInputStream()));
                Dialogue playerLocation = DialogueHelper.decode(reader);
                var id = identifier
                        .toString().replace("dialogues/", "");
                id = id.substring(0, id.lastIndexOf('.'));
                registeredDialogues.put(new Identifier(id), playerLocation);
            } catch (Exception e) {
                System.err.println("Failed to parse: " + identifier);
                e.printStackTrace();
            }
        }
        DialoguesRegistry.registeredDialogues = registeredDialogues;
    }

    // NETWORK SYNC

    private static PacketByteBuf encodedRegisteredDialogues = PacketByteBufs.create();

    public static void encodeRegistry() {
        PacketByteBuf buffer = PacketByteBufs.create();
        var gson = new Gson();
        var json = gson.toJson(registeredDialogues);
        if (BetterAdventureModeCoreClient.clientConfig.show_debug_log) {
            BetterAdventureModeCore.LOGGER.info("Dialogues registry loaded: " + json);
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
            BetterAdventureModeCore.LOGGER.info("Encoded Dialogues registry size (with package overhead): " + buffer.readableBytes()
                    + " bytes (in " + chunks.size() + " string chunks with the size of " + chunkSize + ")");
        }
        encodedRegisteredDialogues = buffer;
    }

    public static void decodeRegistry(PacketByteBuf buffer) {
        var chunkCount = buffer.readInt();
        String json = "";
        for (int i = 0; i < chunkCount; ++i) {
            json = json.concat(buffer.readString());
        }
        if (BetterAdventureModeCoreClient.clientConfig.show_debug_log) {
            BetterAdventureModeCore.LOGGER.info("Decoded Dialogues registry in " + chunkCount + " string chunks");
            BetterAdventureModeCore.LOGGER.info("Dialogues registry received: " + json);
        }
        var gson = new Gson();
        Type mapType = new TypeToken<Map<String, Dialogue>>() {}.getType();
        Map<String, Dialogue> readRegisteredDialogues = gson.fromJson(json, mapType);
        Map<Identifier, Dialogue> newRegisteredDialogues = new HashMap();
        readRegisteredDialogues.forEach((key, value) -> {
            newRegisteredDialogues.put(new Identifier(key), value);
        });
        registeredDialogues = newRegisteredDialogues;
    }

    public static PacketByteBuf getEncodedRegistry() {
        return encodedRegisteredDialogues;
    }
}
