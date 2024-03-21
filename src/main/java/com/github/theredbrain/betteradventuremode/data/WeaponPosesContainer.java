package com.github.theredbrain.betteradventuremode.data;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class WeaponPosesContainer {

    @Nullable
    private final String parent;

    @Nullable
    private final WeaponPoses weaponPoses;

    public WeaponPosesContainer(@Nullable String parent, @Nullable WeaponPoses weaponPoses) {
        this.parent = parent;
        this.weaponPoses = weaponPoses;
    }

    public String parent() {
        return parent;
    }

    public WeaponPoses weaponPoses() {
        return weaponPoses;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (WeaponPosesContainer) obj;
        return Objects.equals(this.parent, that.parent) &&
                Objects.equals(this.weaponPoses, that.weaponPoses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, weaponPoses);
    }

    @Override
    public String toString() {
        return "WeaponPosesContainer[" + "parent=" + parent + ", " + "weaponPoses=" + weaponPoses + ']';
    }

}