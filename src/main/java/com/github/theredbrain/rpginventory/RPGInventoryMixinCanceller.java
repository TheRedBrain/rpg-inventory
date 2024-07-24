package com.github.theredbrain.rpginventory;

import com.bawnorton.mixinsquared.api.MixinCanceller;

import java.util.List;

public class RPGInventoryMixinCanceller implements MixinCanceller {
	@Override
	public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
		if (mixinClassName.equals("dev.emi.trinkets.mixin.PlayerScreenHandlerMixin")) {
			return true;
		}
		if (mixinClassName.equals("dev.emi.trinkets.mixin.CreativeInventoryScreenMixin")) {
			return true;
		}
		if (mixinClassName.equals("dev.emi.trinkets.mixin.LivingEntityMixin")) {
			return true;
		}
		return false;
	}
}
