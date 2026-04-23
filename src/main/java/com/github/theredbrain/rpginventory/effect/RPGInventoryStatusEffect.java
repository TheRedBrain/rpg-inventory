package com.github.theredbrain.rpginventory.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class RPGInventoryStatusEffect extends MobEffect {

	public RPGInventoryStatusEffect(MobEffectCategory statusEffectCategory, int color) {
		super(statusEffectCategory, color);
	}

	public RPGInventoryStatusEffect(MobEffectCategory statusEffectCategory) {
		this(statusEffectCategory, 3381504);
	}
}
