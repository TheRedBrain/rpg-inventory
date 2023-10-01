package com.github.theredbrain.bamcore.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class OverburdenedStatusEffect extends StatusEffect {

    public OverburdenedStatusEffect() {
        super(StatusEffectCategory.NEUTRAL, 3381504); // TODO better colour
    }
}
