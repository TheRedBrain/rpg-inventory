package com.github.theredbrain.rpginventory.compat;

import net.bettercombat.api.MinecraftClient_BetterCombat;
import net.minecraft.client.MinecraftClient;

public class BetterCombatClientCompat {
	public static boolean doesCurrentPlayerStatusPreventHandSlotAction(MinecraftClient minecraftClient) {
		return ((MinecraftClient_BetterCombat) minecraftClient).isWeaponSwingInProgress();
	}
}
