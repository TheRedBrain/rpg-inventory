package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.BetterAdventureModeClient;
import com.github.theredbrain.betteradventuremode.api.json_files_backend.Dialogue;
import com.github.theredbrain.betteradventuremode.api.json_files_backend.DialogueAnswer;
import com.github.theredbrain.betteradventuremode.api.json_files_backend.DialogueAnswerHelper;
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

public class DialogueAnswersRegistry {

    static Map<Identifier, DialogueAnswer> registeredDialogueAnswers = new HashMap<>();

    public static void register(Identifier itemId, DialogueAnswer dialogueAnswer) {
        registeredDialogueAnswers.put(itemId, dialogueAnswer);
    }

    public static DialogueAnswer getDialogueAnswer(Identifier dialogueAnswerId) {
        return registeredDialogueAnswers.get(dialogueAnswerId);
    }

    public static void init() {
        ServerLifecycleEvents.SERVER_STARTED.register((minecraftServer) -> {
            loadDialogueAnswers(minecraftServer.getResourceManager());
            encodeRegistry();
        });
    }

    private static void loadDialogueAnswers(ResourceManager resourceManager) {
        var gson = new Gson();
        Map<Identifier, DialogueAnswer> registeredDialogues = new HashMap();
        // Reading all attribute files
        for (var entry : resourceManager.findResources("dialogues/answers", fileName -> fileName.getPath().endsWith(".json")).entrySet()) {
            var identifier = entry.getKey();
            var resource = entry.getValue();
            try {
                JsonReader reader = new JsonReader(new InputStreamReader(resource.getInputStream()));
                DialogueAnswer dialogue = DialogueAnswerHelper.decode(reader);
                var id = identifier
                        .toString().replace("dialogues/answers/", "");
                id = id.substring(0, id.lastIndexOf('.'));
                registeredDialogues.put(new Identifier(id), dialogue);
            } catch (Exception e) {
                System.err.println("Failed to parse: " + identifier);
                e.printStackTrace();
            }
        }
        DialogueAnswersRegistry.registeredDialogueAnswers = registeredDialogues;
    }

    // NETWORK SYNC

    private static PacketByteBuf encodedRegisteredDialogueAnswers = PacketByteBufs.create();

    public static void encodeRegistry() {
        PacketByteBuf buffer = PacketByteBufs.create();
        var gson = new Gson();
        var json = gson.toJson(registeredDialogueAnswers);
        if (BetterAdventureModeClient.clientConfig.show_debug_log) {
            BetterAdventureMode.LOGGER.info("Dialogue Answers registry loaded: " + json);
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
            BetterAdventureMode.LOGGER.info("Encoded Dialogue Answers registry size (with package overhead): " + buffer.readableBytes()
                    + " bytes (in " + chunks.size() + " string chunks with the size of " + chunkSize + ")");
        }
        encodedRegisteredDialogueAnswers = buffer;
    }

    public static void decodeRegistry(PacketByteBuf buffer) {
        var chunkCount = buffer.readInt();
        String json = "";
        for (int i = 0; i < chunkCount; ++i) {
            json = json.concat(buffer.readString());
        }
        if (BetterAdventureModeClient.clientConfig.show_debug_log) {
            BetterAdventureMode.LOGGER.info("Decoded Dialogue Answers registry in " + chunkCount + " string chunks");
            BetterAdventureMode.LOGGER.info("Dialogue Answers registry received: " + json);
        }
        var gson = new Gson();
        Type mapType = new TypeToken<Map<String, DialogueAnswer>>() {}.getType();
        Map<String, DialogueAnswer> readRegisteredDialogueAnswers = gson.fromJson(json, mapType);
        Map<Identifier, DialogueAnswer> newRegisteredDialogueAnswers = new HashMap();
        readRegisteredDialogueAnswers.forEach((key, value) -> {
            newRegisteredDialogueAnswers.put(new Identifier(key), value);
        });
        registeredDialogueAnswers = newRegisteredDialogueAnswers;
    }

    public static PacketByteBuf getEncodedRegistry() {
        return encodedRegisteredDialogueAnswers;
    }
}
