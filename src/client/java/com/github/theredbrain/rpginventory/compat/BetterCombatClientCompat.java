package com.github.theredbrain.rpginventory.compat;

import net.minecraft.client.Minecraft;

public class BetterCombatClientCompat {
	public static boolean doesCurrentPlayerStatusPreventHandSlotAction(Minecraft minecraftClient) {
//		return ((MinecraftClient_BetterCombat) minecraftClient).isWeaponSwingInProgress();
		return false;
	}
}
