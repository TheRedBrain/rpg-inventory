package com.github.theredbrain.bamcore.api.dimensions;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public final class PlayerHouse {
    private final int controlBlockPosX;
    private final int controlBlockPosY;
    private final int controlBlockPosZ;
    private final String unlockAdvancement;
    private final boolean showLockedHouse;
    private final boolean showUnlockAdvancement;

    public PlayerHouse(int controlBlockPosX, int controlBlockPosY, int controlBlockPosZ, String unlockAdvancement, boolean showLockedHouse, boolean showUnlockAdvancement) {
        this.controlBlockPosX = controlBlockPosX;
        this.controlBlockPosY = controlBlockPosY;
        this.controlBlockPosZ = controlBlockPosZ;
        this.unlockAdvancement = unlockAdvancement;
        this.showLockedHouse = showLockedHouse;
        this.showUnlockAdvancement = showUnlockAdvancement;
    }

    public BlockPos controlBlockPos() {
        return new BlockPos(controlBlockPosX, controlBlockPosY, controlBlockPosZ);
    }

    public Identifier unlockAdvancement() {
        return new Identifier(unlockAdvancement);
    }

    public boolean showLockedHouse() {
        return showLockedHouse;
    }

    public boolean showUnlockAdvancement() {
        return showUnlockAdvancement;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (PlayerHouse) obj;
        return this.controlBlockPosX == that.controlBlockPosX
                && this.controlBlockPosY == that.controlBlockPosY
                && this.controlBlockPosZ == that.controlBlockPosZ
                && Objects.equals(this.unlockAdvancement, that.unlockAdvancement)
                && this.showLockedHouse == that.showLockedHouse
                && this.showUnlockAdvancement == that.showUnlockAdvancement;
    }

    @Override
    public int hashCode() {
        return Objects.hash(controlBlockPosX, controlBlockPosY, controlBlockPosZ, unlockAdvancement, showLockedHouse, showUnlockAdvancement);
    }

    @Override
    public String toString() {
        return "PlayerHouse[" +
                "controlBlockPosX=" + controlBlockPosX + ", " +
                "controlBlockPosY=" + controlBlockPosY + ", " +
                "controlBlockPosZ=" + controlBlockPosZ + ", " +
                "unlockAdvancement=" + unlockAdvancement + ", " +
                "showLockedHouse=" + showLockedHouse + ", " +
                "showUnlockAdvancement=" + showUnlockAdvancement + "]";
    }
}
