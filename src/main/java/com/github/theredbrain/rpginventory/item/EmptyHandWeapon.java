package com.github.theredbrain.rpginventory.item;

import net.minecraft.item.Item;

public class EmptyHandWeapon extends Item {

    public EmptyHandWeapon(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }
}
