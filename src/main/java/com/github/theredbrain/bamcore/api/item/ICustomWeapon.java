package com.github.theredbrain.bamcore.api.item;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;

public interface ICustomWeapon {

    int getStaminaCost();

    RegistryKey<DamageType> getDamageTypeRegistryKey(boolean twoHanded);
}
