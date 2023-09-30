package com.github.theredbrain.bamcore.components;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import net.minecraft.nbt.NbtCompound;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.*;

public class NonEmptyStringMapComponent implements IStringMapComponent, PlayerComponent {
    private HashMap<String, MutablePair<String, Boolean>> values = new HashMap<>(Map.of());

    @Override
    public String getValue(String key) {
        if (values.containsKey(key)) {
            return values.get(key).getLeft();
        }
        return "";
    }

    @Override
    public void setValue(String key, String value) {
        if (!value.equals("")) {
            values.get(key).setLeft(value);
        } else {
            BetterAdventureModeCore.LOGGER.warn("Tried to put empty string with key '" + key + "' into NonEmptyStringMap");
        }
    }

    @Override
    public void setPair(String key, String value, boolean status) {
        if (!value.equals("")) {
            values.put(key, new MutablePair<>(value, status));
        } else {
            BetterAdventureModeCore.LOGGER.warn("Tried to put empty string with key '" + key + "' into NonEmptyStringMap");
        }
    }

    @Override
    public boolean getStatus(String key) {
        if (values.containsKey(key)) {
            return values.get(key).getRight();
        }
        return false;
    }

    @Override
    public void setStatus(String key, boolean status) {
        values.get(key).setRight(status);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.values.clear();
        Set<String> tagKeys = tag.getKeys();
        for (String key : tagKeys) {
            if (!tag.getString(key).equals("")) {
                values.put(key, new MutablePair<>(tag.getString(key + "_value"), tag.getBoolean(key + "_status")));
            }
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        Set<String> tagKeys = tag.getKeys();
        for (String key : tagKeys) {
            tag.putString(key, "");
        }
        if (!values.isEmpty()) {
            Set<String> valuesKeys = values.keySet();
            for (String key : valuesKeys) {
                tag.putString(key + "_value", values.get(key).getLeft());
                tag.putBoolean(key + "_status", values.get(key).getRight());
            }
        }
    }
}
