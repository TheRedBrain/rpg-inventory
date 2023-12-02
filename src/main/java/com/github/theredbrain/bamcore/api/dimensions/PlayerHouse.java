package com.github.theredbrain.bamcore.api.dimensions;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public final class PlayerHouse {
    private final int housingBlockPosX;
    private final int housingBlockPosY;
    private final int housingBlockPosZ;
    private final String displayName;
    private final String unlockAdvancement;
    private final boolean showLockedHouse;
    private final boolean showUnlockAdvancement;

    public PlayerHouse(int housingBlockPosX, int housingBlockPosY, int housingBlockPosZ, String unlockAdvancement, String displayName, boolean showLockedHouse, boolean showUnlockAdvancement) {
        this.housingBlockPosX = housingBlockPosX;
        this.housingBlockPosY = housingBlockPosY;
        this.housingBlockPosZ = housingBlockPosZ;
        this.displayName = displayName;
        this.unlockAdvancement = unlockAdvancement;
        this.showLockedHouse = showLockedHouse;
        this.showUnlockAdvancement = showUnlockAdvancement;
    }

    public BlockPos housingBlockPos() {
        return new BlockPos(this.housingBlockPosX, this.housingBlockPosY, this.housingBlockPosZ);
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public Identifier unlockAdvancement() {
        return new Identifier(this.unlockAdvancement);
    }

    public boolean showLockedHouse() {
        return this.showLockedHouse;
    }

    public boolean showUnlockAdvancement() {
        return this.showUnlockAdvancement;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (PlayerHouse) obj;
        return this.housingBlockPosX == that.housingBlockPosX
                && this.housingBlockPosY == that.housingBlockPosY
                && this.housingBlockPosZ == that.housingBlockPosZ
                && Objects.equals(this.displayName, that.displayName)
                && Objects.equals(this.unlockAdvancement, that.unlockAdvancement)
                && this.showLockedHouse == that.showLockedHouse
                && this.showUnlockAdvancement == that.showUnlockAdvancement;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.housingBlockPosX, this.housingBlockPosY, this.housingBlockPosZ, this.displayName, this.unlockAdvancement, this.showLockedHouse, this.showUnlockAdvancement);
    }

    @Override
    public String toString() {
        return "PlayerHouse[" +
                "controlBlockPosX=" + this.housingBlockPosX + ", " +
                "controlBlockPosY=" + this.housingBlockPosY + ", " +
                "controlBlockPosZ=" + this.housingBlockPosZ + ", " +
                "displayName=" + this.displayName + ", " +
                "unlockAdvancement=" + this.unlockAdvancement + ", " +
                "showLockedHouse=" + this.showLockedHouse + ", " +
                "showUnlockAdvancement=" + this.showUnlockAdvancement + "]";
    }
}
