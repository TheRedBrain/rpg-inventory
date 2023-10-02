package com.github.theredbrain.bamcore.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class ManaRegenerationStatusEffect extends StatusEffect {

    public ManaRegenerationStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, 3381504); // TODO better colour
    }
}
