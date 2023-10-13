package com.github.theredbrain.bamcore.item;

import com.github.theredbrain.bamcore.api.item.BetterAdventureMode_BasicWeaponItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EmptyHandWeapon extends BetterAdventureMode_BasicWeaponItem {

    public EmptyHandWeapon(float attackDamage, float attackSpeed, int staminaCost, Settings settings) {
        super(DamageTypes.PLAYER_ATTACK, null, attackDamage, attackSpeed, staminaCost, 0, settings);
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
