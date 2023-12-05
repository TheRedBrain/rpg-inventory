package com.github.theredbrain.bamcore.api.dimensions;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public final class PlayerDungeon {
    private final int controlBlockPosX;
    private final int controlBlockPosY;
    private final int controlBlockPosZ;
    private final String displayName;
    private final String unlockAdvancement;
    private final boolean showLockedDungeon;
    private final boolean showUnlockAdvancement;
    private final @Nullable Map<String, String> side_entrances;

    public PlayerDungeon(int controlBlockPosX, int controlBlockPosY, int controlBlockPosZ, String unlockAdvancement, String displayName, boolean showLockedDungeon, boolean showUnlockAdvancement, @Nullable Map<String, String> side_entrances) {
        this.controlBlockPosX = controlBlockPosX;
        this.controlBlockPosY = controlBlockPosY;
        this.controlBlockPosZ = controlBlockPosZ;
        this.displayName = displayName;
        this.unlockAdvancement = unlockAdvancement;
        this.showLockedDungeon = showLockedDungeon;
        this.showUnlockAdvancement = showUnlockAdvancement;
        this.side_entrances = side_entrances;
    }

    public BlockPos controlBlockPos() {
        return new BlockPos(this.controlBlockPosX, this.controlBlockPosY, this.controlBlockPosZ);
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public Identifier unlockAdvancement() {
        return new Identifier(this.unlockAdvancement);
    }

    public boolean showLockedDungeon() {
        return this.showLockedDungeon;
    }

    public boolean showUnlockAdvancement() {
        return this.showUnlockAdvancement;
    }

    public boolean hasEntrance(String entrance) {
        if (this.side_entrances != null) {
            return this.side_entrances.containsKey(entrance);
        }
        return false;
    }

    public boolean hasSideEntrances() {
        return this.side_entrances != null;
    }

    public String getEntranceDisplayName(String entrance) {
        String entranceDisplayName = "";
        if (this.side_entrances != null && this.side_entrances.get(entrance) != null) {
            entranceDisplayName = this.side_entrances.get(entrance);
        }
        return entranceDisplayName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (PlayerDungeon) obj;
        return this.controlBlockPosX == that.controlBlockPosX
                && this.controlBlockPosY == that.controlBlockPosY
                && this.controlBlockPosZ == that.controlBlockPosZ
                && Objects.equals(this.displayName, that.displayName)
                && Objects.equals(this.unlockAdvancement, that.unlockAdvancement)
                && this.showLockedDungeon == that.showLockedDungeon
                && this.showUnlockAdvancement == that.showUnlockAdvancement;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.controlBlockPosX, this.controlBlockPosY, this.controlBlockPosZ, this.displayName, this.unlockAdvancement, this.showLockedDungeon, this.showUnlockAdvancement);
    }

    @Override
    public String toString() {
        return "Dungeon[" +
                "controlBlockPosX=" + this.controlBlockPosX + ", " +
                "controlBlockPosY=" + this.controlBlockPosY + ", " +
                "controlBlockPosZ=" + this.controlBlockPosZ + ", " +
                "displayName=" + this.displayName + ", " +
                "unlockAdvancement=" + this.unlockAdvancement + ", " +
                "showLockedDungeon=" + this.showLockedDungeon + ", " +
                "showUnlockAdvancement=" + this.showUnlockAdvancement + "]";
    }
}
