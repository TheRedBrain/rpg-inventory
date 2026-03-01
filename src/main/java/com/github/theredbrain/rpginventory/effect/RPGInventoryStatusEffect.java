package com.github.theredbrain.rpginventory.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class RPGInventoryStatusEffect extends StatusEffect {

	public RPGInventoryStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
		super(statusEffectCategory, color);
	}

	public RPGInventoryStatusEffect(StatusEffectCategory statusEffectCategory) {
		this(statusEffectCategory, 3381504);
	}
}
