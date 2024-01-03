package com.github.theredbrain.betteradventuremode.api.json_files_backend;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.Reader;
import java.lang.reflect.Type;

public class DialogueHelper {

    private static Type registeredDialoguesFileFormat = new TypeToken<Dialogue>() {}.getType();

    public static Dialogue decode(Reader reader) {
        var gson = new Gson();
        Dialogue dialogue = gson.fromJson(reader, registeredDialoguesFileFormat);
        return dialogue;
    }

    public static Dialogue decode(JsonReader json) {
        var gson = new Gson();
        Dialogue dialogue = gson.fromJson(json, registeredDialoguesFileFormat);
        return dialogue;
    }

    public static String encode(Dialogue dialogue) {
        var gson = new Gson();
        return gson.toJson(dialogue);
    }
}
