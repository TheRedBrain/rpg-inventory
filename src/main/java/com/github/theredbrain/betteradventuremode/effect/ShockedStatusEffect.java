package com.github.theredbrain.betteradventuremode.effect;

import com.github.theredbrain.betteradventuremode.entity.damage.DuckDamageSourcesMixin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.InstantStatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import org.jetbrains.annotations.Nullable;

public class ShockedStatusEffect extends InstantStatusEffect { // TODO play test balance
    public ShockedStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 3381504); // TODO better colour
    }

    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);
        entity.damage(((DuckDamageSourcesMixin)entity.getDamageSources()).betteradventuremode$shocked(), 10.0f);
    }

    @Override
    public void applyInstantEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target, int amplifier, double proximity) {
        target.damage(((DuckDamageSourcesMixin)target.getDamageSources()).betteradventuremode$shocked(), 10.0f);
    }

}
