package com.github.theredbrain.betteradventuremode.effect;

import com.github.theredbrain.betteradventuremode.entity.damage.DuckDamageSourcesMixin;
import net.minecraft.entity.LivingEntity;

public class CustomPoisonStatusEffect extends HarmfulStatusEffect { // TODO play test balance
    public CustomPoisonStatusEffect() {
        super();
    }

    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);
        float poisonDamage = amplifier + 1;
        entity.damage(((DuckDamageSourcesMixin)entity.getDamageSources()).betteradventuremode$poison(), poisonDamage);
    }

    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration % 40 == 1;
    }
}
