package com.github.theredbrain.bamcore.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class StaggeredStatusEffect extends StatusEffect {

    public StaggeredStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 3381504); // TODO better colour
    }
}
