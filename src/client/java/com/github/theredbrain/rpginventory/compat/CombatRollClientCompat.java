package com.github.theredbrain.rpginventory.compat;

import net.combat_roll.internals.RollingEntity;
import net.minecraft.client.MinecraftClient;

public class CombatRollClientCompat {
	public static boolean doesCurrentPlayerStatusPreventHandSlotAction(MinecraftClient minecraftClient) {
		boolean bl = false;
		if (minecraftClient.player != null) {
			bl = ((RollingEntity) minecraftClient.player).getRollManager().isRolling();
		}
		return bl;
	}
}
