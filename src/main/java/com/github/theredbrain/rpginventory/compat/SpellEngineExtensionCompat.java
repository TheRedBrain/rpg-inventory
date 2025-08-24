package com.github.theredbrain.rpginventory.compat;

import com.github.theredbrain.spellengineextension.entity.player.DuckPlayerEntityMixin;
import net.minecraft.server.network.ServerPlayerEntity;

public class SpellEngineExtensionCompat {

	public static boolean doesCurrentPlayerStatusPreventHandSlotAction(ServerPlayerEntity serverPlayerEntity) {
		return ((DuckPlayerEntityMixin) serverPlayerEntity).spellengineextension$getMovementLockingTicks() > 0;
	}
}
