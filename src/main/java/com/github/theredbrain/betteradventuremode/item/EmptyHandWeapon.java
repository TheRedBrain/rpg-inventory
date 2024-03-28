package com.github.theredbrain.betteradventuremode.item;

import com.github.theredbrain.betteradventuremode.registry.DamageTypesRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EmptyHandWeapon extends BasicWeaponItem {

    public EmptyHandWeapon(RegistryKey<DamageType> damageTypeRegistryKey, float attackDamage, float attackSpeed, int staminaCost, Settings settings) {
        super(damageTypeRegistryKey, attackDamage, attackSpeed, staminaCost, 0, settings);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }
}
