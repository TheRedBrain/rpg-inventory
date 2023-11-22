package com.github.theredbrain.bamcore.api.dimensions;

import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public final class PlayerDungeon {
    private final int controlBlockPosX;
    private final int controlBlockPosY;
    private final int controlBlockPosZ;

    public PlayerDungeon(int controlBlockPosX, int controlBlockPosY, int controlBlockPosZ) {
        this.controlBlockPosX = controlBlockPosX;
        this.controlBlockPosY = controlBlockPosY;
        this.controlBlockPosZ = controlBlockPosZ;
    }

    public BlockPos controlBlockPos() {
        return new BlockPos(controlBlockPosX, controlBlockPosY, controlBlockPosZ);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (PlayerDungeon) obj;
        return this.controlBlockPosX == that.controlBlockPosX
                && this.controlBlockPosY == that.controlBlockPosY
                && this.controlBlockPosZ == that.controlBlockPosZ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(controlBlockPosX, controlBlockPosY, controlBlockPosZ);
    }

    @Override
    public String toString() {
        return "Dungeon[" +
                "controlBlockPosX=" + controlBlockPosX + ", " +
                "controlBlockPosY=" + controlBlockPosY + ", " +
                "controlBlockPosZ=" + controlBlockPosZ + "]";
    }
}
