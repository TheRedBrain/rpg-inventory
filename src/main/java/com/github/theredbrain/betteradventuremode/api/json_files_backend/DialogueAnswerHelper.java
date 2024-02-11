package com.github.theredbrain.betteradventuremode.api.json_files_backend;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.Reader;
import java.lang.reflect.Type;

public class DialogueAnswerHelper {

    private static Type registeredDialogueAnswersFileFormat = new TypeToken<DialogueAnswer>() {}.getType();

    public static DialogueAnswer decode(Reader reader) {
        var gson = new Gson();
        DialogueAnswer dialogueAnswer = gson.fromJson(reader, registeredDialogueAnswersFileFormat);
        return dialogueAnswer;
    }

    public static DialogueAnswer decode(JsonReader json) {
        var gson = new Gson();
        DialogueAnswer dialogueAnswer = gson.fromJson(json, registeredDialogueAnswersFileFormat);
        return dialogueAnswer;
    }

    public static String encode(DialogueAnswer dialogueAnswer) {
        var gson = new Gson();
        return gson.toJson(dialogueAnswer);
    }
}
