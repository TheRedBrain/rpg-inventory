package com.github.theredbrain.betteradventuremode.effect;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.entity.damage.DuckDamageSourcesMixin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.InstantStatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import org.jetbrains.annotations.Nullable;

public class ShockedStatusEffect extends InstantStatusEffect {
    private final float damage;
    public ShockedStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 3381504); // TODO better colour
        this.damage = BetterAdventureMode.gamePlayBalanceConfig.shock_damage;
    }

    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);
        entity.damage(((DuckDamageSourcesMixin)entity.getDamageSources()).betteradventuremode$shocked(), this.damage);
    }

    @Override
    public void applyInstantEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target, int amplifier, double proximity) {
        target.damage(((DuckDamageSourcesMixin)target.getDamageSources()).betteradventuremode$shocked(), this.damage);
    }

}
