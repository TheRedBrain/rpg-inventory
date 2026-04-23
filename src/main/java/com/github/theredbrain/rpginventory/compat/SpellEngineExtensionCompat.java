package com.github.theredbrain.rpginventory.compat;

import net.minecraft.server.level.ServerPlayer;

public class SpellEngineExtensionCompat {

	public static boolean doesCurrentPlayerStatusPreventHandSlotAction(ServerPlayer serverPlayerEntity) {
//		return ((DuckPlayerEntityMixin) serverPlayerEntity).spellengineextension$getMovementLockingTicks() > 0;
		return false;
	}
}
