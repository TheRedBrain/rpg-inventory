package com.github.theredbrain.betteradventuremode.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class HarmfulStatusEffect extends StatusEffect {

    public HarmfulStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 3381504); // TODO better colour
    }
}
