package com.github.theredbrain.rpgmod.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class PermanentMountStatusEffect extends StatusEffect {

    public PermanentMountStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, 3381504); // TODO better colour
    }
}
