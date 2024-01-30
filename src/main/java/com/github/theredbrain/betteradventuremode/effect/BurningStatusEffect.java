package com.github.theredbrain.betteradventuremode.effect;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.entity.damage.DuckDamageSourcesMixin;
import net.minecraft.entity.LivingEntity;

public class BurningStatusEffect extends HarmfulStatusEffect {
    public BurningStatusEffect() {
        super();
    }

    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);
        BetterAdventureMode.info("burning applyUpdateEffect");
        entity.damage(((DuckDamageSourcesMixin)entity.getDamageSources()).betteradventuremode$burning(), 8.0f); // TODO balance
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
