package com.github.theredbrain.bamcore.api.dimensions;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public final class PlayerHouse {
    private final int housingBlockPosX;
    private final int housingBlockPosY;
    private final int housingBlockPosZ;
    private final String unlockAdvancement;
    private final boolean showLockedHouse;
    private final boolean showUnlockAdvancement;

    public PlayerHouse(int housingBlockPosX, int housingBlockPosY, int housingBlockPosZ, String unlockAdvancement, boolean showLockedHouse, boolean showUnlockAdvancement) {
        this.housingBlockPosX = housingBlockPosX;
        this.housingBlockPosY = housingBlockPosY;
        this.housingBlockPosZ = housingBlockPosZ;
        this.unlockAdvancement = unlockAdvancement;
        this.showLockedHouse = showLockedHouse;
        this.showUnlockAdvancement = showUnlockAdvancement;
    }

    public BlockPos housingBlockPos() {
        return new BlockPos(housingBlockPosX, housingBlockPosY, housingBlockPosZ);
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
        return this.housingBlockPosX == that.housingBlockPosX
                && this.housingBlockPosY == that.housingBlockPosY
                && this.housingBlockPosZ == that.housingBlockPosZ
                && Objects.equals(this.unlockAdvancement, that.unlockAdvancement)
                && this.showLockedHouse == that.showLockedHouse
                && this.showUnlockAdvancement == that.showUnlockAdvancement;
    }

    @Override
    public int hashCode() {
        return Objects.hash(housingBlockPosX, housingBlockPosY, housingBlockPosZ, unlockAdvancement, showLockedHouse, showUnlockAdvancement);
    }

    @Override
    public String toString() {
        return "PlayerHouse[" +
                "controlBlockPosX=" + housingBlockPosX + ", " +
                "controlBlockPosY=" + housingBlockPosY + ", " +
                "controlBlockPosZ=" + housingBlockPosZ + ", " +
                "unlockAdvancement=" + unlockAdvancement + ", " +
                "showLockedHouse=" + showLockedHouse + ", " +
                "showUnlockAdvancement=" + showUnlockAdvancement + "]";
    }
}
