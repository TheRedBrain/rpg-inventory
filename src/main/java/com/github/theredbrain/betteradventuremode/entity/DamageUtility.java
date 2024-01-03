package com.github.theredbrain.betteradventuremode.entity;

import com.github.theredbrain.betteradventuremode.registry.Tags;
import net.minecraft.entity.damage.DamageSource;

public class DamageUtility {

    public static double getStaggerDamageMultiplierForDamageType(DamageSource damageSource) {
        if (damageSource.isIn(Tags.STAGGERS)) {
            if (damageSource.isIn(Tags.HAS_STAGGER_MULTIPLIER_OF_0_5)) {
                return 0.5;
            } else {
                return 1.0;
            }
        } else {
            return 0.0;
        }
    }
}
