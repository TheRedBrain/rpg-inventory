package com.github.theredbrain.bamcore.components;

import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class BlockPosComponent implements IBlockPosComponent, PlayerComponent {
    private BlockPos value = new BlockPos(0, 0, 0);

    @Override
    public BlockPos getValue() {
        return this.value;
    }

    @Override
    public void setValue(BlockPos value) {
        this.value = value;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        int x = 0;
        int y = 0;
        int z = 0;
        if (tag.getKeys().contains("x")) {
            x = tag.getInt("x");
        }
        if (tag.getKeys().contains("y")) {
            y = tag.getInt("y");
        }
        if (tag.getKeys().contains("z")) {
            z = tag.getInt("z");
        }
        this.value = new BlockPos(x, y, z);
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("x", this.value.getX());
        tag.putInt("y", this.value.getY());
        tag.putInt("z", this.value.getZ());
    }
}
