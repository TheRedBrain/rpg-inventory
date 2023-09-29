package com.github.theredbrain.bamcore.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public interface DuckItemStackMixin {
    TypedActionResult<ItemStack> adventureUse(World world, PlayerEntity user);
}
