package com.github.theredbrain.rpgmod.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class CivilisationStatusEffect extends StatusEffect {

    public CivilisationStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, 3381504); // TODO better colour
    }
}
