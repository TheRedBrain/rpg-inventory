package com.github.theredbrain.betteradventuremode.item;

import com.github.theredbrain.betteradventuremode.registry.DamageTypesRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EmptyHandWeapon extends BasicWeaponItem {

    public EmptyHandWeapon(float attackDamage, float attackSpeed, int staminaCost, Settings settings) {
        super(DamageTypesRegistry.PLAYER_UNARMED_DAMAGE_TYPE, attackDamage, attackSpeed, staminaCost, 0, settings);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }
}
