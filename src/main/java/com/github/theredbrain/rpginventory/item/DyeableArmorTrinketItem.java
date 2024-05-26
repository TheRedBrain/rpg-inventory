package com.github.theredbrain.rpginventory.item;

import net.minecraft.item.DyeableItem;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class DyeableArmorTrinketItem extends ArmorTrinketItem implements DyeableItem {
    public DyeableArmorTrinketItem(double armor, double armorToughness, double weight, @Nullable SoundEvent equipSound, Identifier assetSubpath, Settings settings) {
        super(armor, armorToughness, weight, equipSound, assetSubpath, settings);
    }

    public DyeableArmorTrinketItem(double armor, double armorToughness, double weight, Identifier assetSubpath, Settings settings) {
        super(armor, armorToughness, weight, null, assetSubpath, settings);
    }
}
