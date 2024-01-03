package com.github.theredbrain.betteradventuremode.item;

import com.github.theredbrain.betteradventuremode.api.item.BetterAdventureMode_BasicWeaponItem;
import com.github.theredbrain.betteradventuremode.registry.DamageTypesRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EmptyHandWeapon extends BetterAdventureMode_BasicWeaponItem {

    public EmptyHandWeapon(float attackDamage, float attackSpeed, int staminaCost, Settings settings) {
        super(DamageTypesRegistry.PLAYER_UNARMED_DAMAGE_TYPE, null, attackDamage, attackSpeed, staminaCost, 0, settings);
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return false;
    }

    @Override
    public boolean isDamageable() {
        return false;
    }
}
