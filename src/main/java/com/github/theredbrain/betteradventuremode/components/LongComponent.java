package com.github.theredbrain.betteradventuremode.components;

import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import net.minecraft.nbt.NbtCompound;

public class LongComponent implements ILongComponent, PlayerComponent {
    private long value = 0;

    @Override
    public long getValue() {
        return this.value;
    }

    @Override
    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.value = 0;
        if (tag.getKeys().contains("value")) {
            value = tag.getLong("value");
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putLong("value", this.value);
    }
}
