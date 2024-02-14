package com.github.theredbrain.betteradventuremode.data;

import com.github.theredbrain.betteradventuremode.util.ItemUtils;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public final class Location {
    private final int controlBlockPosX;
    private final int controlBlockPosY;
    private final int controlBlockPosZ;
    private final String structureIdentifier;
    private final String displayName;
    private final String unlockAdvancement;
    private final String lockAdvancement;
    private final boolean showLockedLocation;
    private final boolean showUnlockAdvancement;
    private final boolean showLockAdvancement;
    private final boolean publicLocation;
    private final boolean playerLocation;
    private final boolean canOwnerBeChosen;
    private final boolean stayInCurrentDimension;
    private final boolean showAdventureScreen;
    private final boolean consumeKey;
    private final @Nullable ItemUtils.VirtualItemStack key;
    private final @Nullable Map<String, SideEntrance> side_entrances;

    public Location(int controlBlockPosX, int controlBlockPosY, int controlBlockPosZ, String structureIdentifier, String unlockAdvancement, String lockAdvancement, String displayName, boolean showLockedLocation, boolean showUnlockAdvancement, boolean showLockAdvancement, boolean publicLocation, boolean playerLocation, boolean canOwnerBeChosen, boolean stayInCurrentDimension, boolean showAdventureScreen, boolean consumeKey, @Nullable ItemUtils.VirtualItemStack key, @Nullable Map<String, SideEntrance> side_entrances) {
        this.controlBlockPosX = controlBlockPosX;
        this.controlBlockPosY = controlBlockPosY;
        this.controlBlockPosZ = controlBlockPosZ;
        this.structureIdentifier = structureIdentifier;
        this.displayName = displayName;
        this.unlockAdvancement = unlockAdvancement;
        this.lockAdvancement = lockAdvancement;
        this.showLockedLocation = showLockedLocation;
        this.showUnlockAdvancement = showUnlockAdvancement;
        this.showLockAdvancement = showLockAdvancement;
        this.publicLocation = publicLocation;
        this.playerLocation = playerLocation;
        this.canOwnerBeChosen = canOwnerBeChosen;
        this.stayInCurrentDimension = stayInCurrentDimension;
        this.showAdventureScreen = showAdventureScreen;
        this.consumeKey = consumeKey;
        this.key = key;
        this.side_entrances = side_entrances;
    }

    public BlockPos controlBlockPos() {
        return new BlockPos(this.controlBlockPosX, this.controlBlockPosY, this.controlBlockPosZ);
    }

    public String getStructureIdentifier() {
        return this.structureIdentifier;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String unlockAdvancement() {
//        return new Identifier(this.unlockAdvancement);
        if (Identifier.isValid(this.unlockAdvancement)) {
            return this.unlockAdvancement;
        }
        return "";
    }

    public String lockAdvancement() {
//        return new Identifier(this.lockAdvancement);
        if (Identifier.isValid(this.lockAdvancement)) {
            return this.lockAdvancement;
        }
        return "";
    }

    public boolean showLockedDungeon() {
        return this.showLockedLocation;
    }

    public boolean showUnlockAdvancement() {
        return this.showUnlockAdvancement;
    }

    public boolean showLockAdvancement() {
        return this.showLockAdvancement;
    }

    public boolean publicLocation() {
        return this.publicLocation;
    }

    public boolean playerLocation() {
        return playerLocation;
    }

    public boolean canOwnerBeChosen() {
        return canOwnerBeChosen;
    }

    public boolean stayInCurrentDimension() {
        return stayInCurrentDimension;
    }

    public boolean showAdventureScreen() {
        return showAdventureScreen;
    }

    public boolean consumeKey() {
        return consumeKey;
    }

    @Nullable
    public ItemUtils.VirtualItemStack getKey() {
        return this.key;
    }

    @Nullable
    public ItemUtils.VirtualItemStack getKeyForEntrance(String entrance) {
        if (entrance.equals("")) {
            return this.key;
        }
        if (this.side_entrances != null && this.side_entrances.get(entrance) != null) {
            return this.side_entrances.get(entrance).getKey();
        }
        return null;
    }

    public boolean consumeKeyAtEntrance(String entrance) {
        if (entrance.equals("")) {
            return this.consumeKey;
        }
        if (this.side_entrances != null && this.side_entrances.get(entrance) != null) {
            return this.side_entrances.get(entrance).consumeKey();
        }
        return false;
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
            entranceDisplayName = this.side_entrances.get(entrance).getName();
        }
        return entranceDisplayName;
    }

    public final class SideEntrance {

        private final String name;
        private final boolean consumeKey;

        private final @Nullable ItemUtils.VirtualItemStack key;

        public SideEntrance(String name, boolean consumeKey, @Nullable ItemUtils.VirtualItemStack key) {
            this.name = name;
            this.consumeKey = consumeKey;
            this.key = key;
        }

        public String getName() {
            return this.name;
        }

        public boolean consumeKey() {
            return consumeKey;
        }

        public ItemUtils.VirtualItemStack getKey() {
            return this.key;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Location) obj;
        return this.controlBlockPosX == that.controlBlockPosX
                && this.controlBlockPosY == that.controlBlockPosY
                && this.controlBlockPosZ == that.controlBlockPosZ
                && Objects.equals(this.structureIdentifier, that.structureIdentifier)
                && Objects.equals(this.displayName, that.displayName)
                && Objects.equals(this.unlockAdvancement, that.unlockAdvancement)
                && Objects.equals(this.lockAdvancement, that.lockAdvancement)
                && this.showLockedLocation == that.showLockedLocation
                && this.showUnlockAdvancement == that.showUnlockAdvancement
                && this.showLockAdvancement == that.showLockAdvancement
                && this.publicLocation == that.publicLocation
                && this.playerLocation == that.playerLocation
                && this.canOwnerBeChosen == that.canOwnerBeChosen
                && this.stayInCurrentDimension == that.stayInCurrentDimension
                && this.showAdventureScreen == that.showAdventureScreen
                && this.consumeKey == that.consumeKey;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.controlBlockPosX, this.controlBlockPosY, this.controlBlockPosZ, this.structureIdentifier, this.displayName, this.unlockAdvancement, this.lockAdvancement, this.showLockedLocation, this.showUnlockAdvancement, this.showLockAdvancement, this.publicLocation, this.playerLocation, this.canOwnerBeChosen, this.stayInCurrentDimension, this.showAdventureScreen, this.consumeKey);
    }

    @Override
    public String toString() {
        return "Location[" +
                "controlBlockPosX=" + this.controlBlockPosX + ", " +
                "controlBlockPosY=" + this.controlBlockPosY + ", " +
                "controlBlockPosZ=" + this.controlBlockPosZ + ", " +
                "structureIdentifier=" + this.structureIdentifier + ", " +
                "displayName=" + this.displayName + ", " +
                "unlockAdvancement=" + this.unlockAdvancement + ", " +
                "lockAdvancement=" + this.lockAdvancement + ", " +
                "showLockedDungeon=" + this.showLockedLocation + ", " +
                "showUnlockAdvancement=" + this.showUnlockAdvancement + ", " +
                "showLockAdvancement=" + this.showLockAdvancement + ", " +
                "publicLocation=" + this.publicLocation + ", " +
                "playerLocation=" + this.playerLocation + ", " +
                "canOwnerBeChosen=" + this.canOwnerBeChosen + ", " +
                "stayInCurrentDimension=" + this.stayInCurrentDimension + ", " +
                "showAdventureScreen=" + this.showAdventureScreen + ", " +
                "consumeKey=" + this.consumeKey + "]";
    }
}
