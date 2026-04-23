package com.github.theredbrain.rpginventory.compat;

public class BetterCombatExtensionCompat {
	public static boolean isAlternativeHandSwapAlgorithmActive() {
//		return BetterCombatExtension.SERVER_CONFIG.enable_experimental_swap_hand_attributes_algorithm.get();
		return false;
	}
}
