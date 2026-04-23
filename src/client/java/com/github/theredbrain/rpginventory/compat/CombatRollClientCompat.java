package com.github.theredbrain.rpginventory.compat;

import net.minecraft.client.Minecraft;

public class CombatRollClientCompat {
	public static boolean doesCurrentPlayerStatusPreventHandSlotAction(Minecraft minecraftClient) {
		boolean bl = false;
//		if (minecraftClient.player != null) {
//			bl = ((RollingEntity) minecraftClient.player).getRollManager().isRolling();
//		}
		return bl;
	}
}
