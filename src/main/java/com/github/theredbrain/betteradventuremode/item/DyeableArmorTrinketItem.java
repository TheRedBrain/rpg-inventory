package com.github.theredbrain.betteradventuremode.item;

import net.minecraft.item.DyeableItem;
import net.minecraft.util.Identifier;

public class DyeableArmorTrinketItem extends ArmorTrinketItem implements DyeableItem {
    public DyeableArmorTrinketItem(double armor, double armorToughness, double weight, Identifier assetSubpath, Settings settings) {
        super(armor, armorToughness, weight, assetSubpath, settings);
    }
}
