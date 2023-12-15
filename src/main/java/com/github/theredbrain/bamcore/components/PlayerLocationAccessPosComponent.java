package com.github.theredbrain.bamcore.components;

import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

public class PlayerLocationAccessPosComponent implements IPlayerLocationAccessPosComponent, PlayerComponent {
    private Pair<Pair<String, BlockPos>, Boolean> value = new Pair<>(new Pair<>("", new BlockPos(0, 0, 0)), false);

    @Override
    public Pair<Pair<String, BlockPos>, Boolean> getValue() {
        return value;
    }

    @Override
    public void setValue(Pair<Pair<String, BlockPos>, Boolean> value) {
        this.value = value;
    }

    @Override
    public void deactivate() {
        this.value = new Pair<>(this.value.getLeft(),false);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        String dimension = "";
        int x = 0;
        int y = 0;
        int z = 0;
        boolean active = false;
        if (tag.getKeys().contains("dimension")) {
            dimension = tag.getString("dimension");
        }
        if (tag.getKeys().contains("x")) {
            x = tag.getInt("x");
        }
        if (tag.getKeys().contains("y")) {
            y = tag.getInt("y");
        }
        if (tag.getKeys().contains("z")) {
            z = tag.getInt("z");
        }
        if (tag.getKeys().contains("active")) {
            active = tag.getBoolean("active");
        }
        this.value = new Pair<>(new Pair<>(dimension, new BlockPos(x, y, z)), active);
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putString("dimension", this.value.getLeft().getLeft());
        BlockPos blockPos = this.value.getLeft().getRight();
        tag.putInt("x", blockPos.getX());
        tag.putInt("y", blockPos.getY());
        tag.putInt("z", blockPos.getZ());
        tag.putBoolean("active", this.value.getRight());
    }
}
