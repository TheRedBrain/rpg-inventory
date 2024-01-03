package com.github.theredbrain.betteradventuremode.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class NeutralStatusEffect extends StatusEffect {

    public NeutralStatusEffect() {
        super(StatusEffectCategory.NEUTRAL, 3381504); // TODO better colour
    }
}
