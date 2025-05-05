package com.github.theredbrain.rpginventory;

import com.bawnorton.mixinsquared.api.MixinCanceller;

import java.util.List;

public class RPGInventoryMixinCanceller implements MixinCanceller {
	@Override
	public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
//		if (RPGInventory.isTrinketsLoaded) {
			if (mixinClassName.equals("dev.emi.trinkets.mixin.PlayerScreenHandlerMixin")) {
				return true;
			}
			if (mixinClassName.equals("dev.emi.trinkets.mixin.CreativeInventoryScreenMixin")) {
				return true;
			}
//			if (mixinClassName.equals("dev.emi.trinkets.mixin.LivingEntityMixin")) {
//				return true;
//			}
//		} else {
//			if (mixinClassName.equals("com.github.theredbrain.rpginventory.mixin.trinkets.SurvivalTrinketSlotMixin")) {
//				return true;
//			}
//			if (mixinClassName.equals("com.github.theredbrain.rpginventory.mixin.screen.PlayerScreenHandlerMixin_TrinketsReplacement")) {
//				return true;
//			}
//			if (mixinClassName.equals("com.github.theredbrain.rpginventory.mixin.client.gui.screen.ingame.CreativeInventoryScreenMixin_TrinketsReplacement")) {
//				return true;
//			}
//		}
		return false;
	}
}
