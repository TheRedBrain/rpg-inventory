package com.github.theredbrain.betteradventuremode.effect;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.entity.damage.DuckDamageSourcesMixin;
import net.minecraft.entity.LivingEntity;

public class BurningStatusEffect extends HarmfulStatusEffect {
    private final float damage;
    private final int update_modulus;
    private final int update_checked_remainder;
    public BurningStatusEffect() {
        super();
        this.damage = BetterAdventureMode.gamePlayBalanceConfig.burning_damage;
        this.update_modulus = BetterAdventureMode.gamePlayBalanceConfig.burning_update_modulus;
        this.update_checked_remainder = BetterAdventureMode.gamePlayBalanceConfig.burning_update_checked_remainder;
    }

    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);
        entity.damage(((DuckDamageSourcesMixin)entity.getDamageSources()).betteradventuremode$burning(), this.damage);
    }

    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration % this.update_modulus == this.update_checked_remainder;
    }
}
