package com.github.theredbrain.rpgmod.server.network;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;

public interface DuckServerPlayerInteractionManagerMixin {
    public ActionResult consumeItem(ServerPlayerEntity serverPlayerEntity, ServerWorld serverWorld, ItemStack itemStack);
}
