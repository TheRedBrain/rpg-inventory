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
    private final boolean showLocationName;
    private final boolean showLocationOwner;
    private final boolean isPublic;
    private final boolean canOwnerBeChosen;
    private final boolean consumeKey;
    private final @Nullable ItemUtils.VirtualItemStack key;
    private final @Nullable Map<String, SideEntrance> side_entrances;

    public Location(int controlBlockPosX, int controlBlockPosY, int controlBlockPosZ, String structureIdentifier, String displayName, String unlockAdvancement, String lockAdvancement, boolean showLockedLocation, boolean showUnlockAdvancement, boolean showLockAdvancement, boolean showLocationName, boolean showLocationOwner, boolean isPublic, boolean canOwnerBeChosen, boolean consumeKey, @Nullable ItemUtils.VirtualItemStack key, @Nullable Map<String, SideEntrance> side_entrances) {
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
        this.showLocationName = showLocationName;
        this.showLocationOwner = showLocationOwner;
        this.isPublic = isPublic;
        this.canOwnerBeChosen = canOwnerBeChosen;
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

    public String unlockAdvancementForEntrance(String entrance) {
        String unlockAdvancementIdString = "";
        if (entrance.equals("")) {
            unlockAdvancementIdString = this.unlockAdvancement;
        }
        if (this.side_entrances != null && this.side_entrances.get(entrance) != null) {
            unlockAdvancementIdString = this.side_entrances.get(entrance).unlockAdvancement();
        }
        if (Identifier.isValid(unlockAdvancementIdString)) {
            return unlockAdvancementIdString;
        }
        return "";
    }

    public String lockAdvancementForEntrance(String entrance) {
        String lockAdvancementIdString = "";
        if (entrance.equals("")) {
            lockAdvancementIdString = this.lockAdvancement;
        }
        if (this.side_entrances != null && this.side_entrances.get(entrance) != null) {
            lockAdvancementIdString = this.side_entrances.get(entrance).lockAdvancement();
        }
        if (Identifier.isValid(lockAdvancementIdString)) {
            return lockAdvancementIdString;
        }
        return "";
    }

    public boolean showLockedLocationForEntrance(String entrance) {
        if (entrance.equals("")) {
            return this.showLockedLocation;
        }
        if (this.side_entrances != null && this.side_entrances.get(entrance) != null) {
            return this.side_entrances.get(entrance).showLockedLocation();
        }
        return false;
    }

    public boolean showUnlockAdvancementForEntrance(String entrance) {
        if (entrance.equals("")) {
            return this.showUnlockAdvancement;
        }
        if (this.side_entrances != null && this.side_entrances.get(entrance) != null) {
            return this.side_entrances.get(entrance).showUnlockAdvancement();
        }
        return false;
    }

    public boolean showLockAdvancementForEntrance(String entrance) {
        if (entrance.equals("")) {
            return this.showLockAdvancement;
        }
        if (this.side_entrances != null && this.side_entrances.get(entrance) != null) {
            return this.side_entrances.get(entrance).showLockAdvancement();
        }
        return false;
    }

    public boolean showLocationNameForEntrance(String entrance) {
        if (entrance.equals("")) {
            return this.showLocationName;
        }
        if (this.side_entrances != null && this.side_entrances.get(entrance) != null) {
            return this.side_entrances.get(entrance).showLocationName();
        }
        return false;
    }

    public boolean showLocationOwnerForEntrance(String entrance) {
        if (entrance.equals("")) {
            return this.showLocationOwner;
        }
        if (this.side_entrances != null && this.side_entrances.get(entrance) != null) {
            return this.side_entrances.get(entrance).showLocationOwner();
        }
        return false;
    }

    public boolean isPublic() {
        return this.isPublic;
    }

    public boolean canOwnerBeChosen() {
        return canOwnerBeChosen;
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
        private final String unlockAdvancement;
        private final String lockAdvancement;
        private final boolean showLockedLocation;
        private final boolean showUnlockAdvancement;
        private final boolean showLockAdvancement;
        private final boolean showLocationName;
        private final boolean showLocationOwner;
        private final boolean consumeKey;

        private final @Nullable ItemUtils.VirtualItemStack key;

        public SideEntrance(String name, String unlockAdvancement, String lockAdvancement, boolean showLockedLocation, boolean showUnlockAdvancement, boolean showLockAdvancement, boolean showLocationName, boolean showLocationOwner, boolean consumeKey, @Nullable ItemUtils.VirtualItemStack key) {
            this.name = name;
            this.unlockAdvancement = unlockAdvancement;
            this.lockAdvancement = lockAdvancement;
            this.showLockedLocation = showLockedLocation;
            this.showUnlockAdvancement = showUnlockAdvancement;
            this.showLockAdvancement = showLockAdvancement;
            this.showLocationName = showLocationName;
            this.showLocationOwner = showLocationOwner;
            this.consumeKey = consumeKey;
            this.key = key;
        }

        public String getName() {
            return this.name;
        }

        public String unlockAdvancement() {
            if (Identifier.isValid(this.unlockAdvancement)) {
                return this.unlockAdvancement;
            }
            return "";
        }

        public String lockAdvancement() {
            if (Identifier.isValid(this.lockAdvancement)) {
                return this.lockAdvancement;
            }
            return "";
        }

        public boolean showLockedLocation() {
            return this.showLockedLocation;
        }

        public boolean showUnlockAdvancement() {
            return this.showUnlockAdvancement;
        }

        public boolean showLockAdvancement() {
            return this.showLockAdvancement;
        }

        public boolean showLocationName() {
            return this.showLocationName;
        }

        public boolean showLocationOwner() {
            return this.showLocationOwner;
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
                && this.showLocationName == that.showLocationName
                && this.showLocationOwner == that.showLocationOwner
                && this.isPublic == that.isPublic
                && this.canOwnerBeChosen == that.canOwnerBeChosen
                && this.consumeKey == that.consumeKey;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.controlBlockPosX, this.controlBlockPosY, this.controlBlockPosZ, this.structureIdentifier, this.displayName, this.unlockAdvancement, this.lockAdvancement, this.showLockedLocation, this.showUnlockAdvancement, this.showLockAdvancement, this.showLocationName, this.showLocationOwner, this.isPublic, this.canOwnerBeChosen, this.consumeKey);
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
                "showLocationName=" + this.showLocationName + ", " +
                "showLocationOwner=" + this.showLocationOwner + ", " +
                "publicLocation=" + this.isPublic + ", " +
                "canOwnerBeChosen=" + this.canOwnerBeChosen + ", " +
                "consumeKey=" + this.consumeKey + "]";
    }
}
