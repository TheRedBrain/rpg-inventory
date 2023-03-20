package com.github.theredbrain.rpgmod.client.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

public interface DuckClientPlayerInteractionManagerMixin {
    public ActionResult consumeItem(PlayerEntity player, World world, ItemStack itemStack);
}
