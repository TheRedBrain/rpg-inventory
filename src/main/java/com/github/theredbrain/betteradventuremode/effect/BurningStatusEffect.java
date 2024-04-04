package com.github.theredbrain.betteradventuremode.effect;

import com.github.theredbrain.betteradventuremode.entity.damage.DuckDamageSourcesMixin;
import net.minecraft.entity.LivingEntity;

public class BurningStatusEffect extends HarmfulStatusEffect { // TODO play test balance
    public BurningStatusEffect() {
        super();
    }

    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);
        entity.damage(((DuckDamageSourcesMixin)entity.getDamageSources()).betteradventuremode$burning(), 2.0f);
    }

    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration % 50 == 1;
    }
}
