package com.github.theredbrain.bamcore.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class NoAttackItemsStatusEffect extends StatusEffect {

    public NoAttackItemsStatusEffect() {
        super(StatusEffectCategory.NEUTRAL, 3381504); // TODO better colour
    }
}
