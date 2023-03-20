package com.github.theredbrain.rpgmod.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;

public class MountProvidingItem extends Item { // TODO wait for 1.19.4
    public MountProvidingItem(Settings settings, LivingEntity mountEntity) {
        super(settings);
    }
}
