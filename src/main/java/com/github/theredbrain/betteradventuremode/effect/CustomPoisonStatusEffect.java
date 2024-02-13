package com.github.theredbrain.betteradventuremode.effect;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.entity.damage.DuckDamageSourcesMixin;
import net.minecraft.entity.LivingEntity;

public class CustomPoisonStatusEffect extends HarmfulStatusEffect {
    private final float damage;
    private final int update_modulus;
    private final int update_checked_remainder;
    public CustomPoisonStatusEffect() {
        super();
        this.damage = BetterAdventureMode.gamePlayBalanceConfig.poison_damage;
        this.update_modulus = BetterAdventureMode.gamePlayBalanceConfig.poison_update_modulus;
        this.update_checked_remainder = BetterAdventureMode.gamePlayBalanceConfig.poison_update_checked_remainder;
    }

    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);
        float poisonDamage = amplifier + this.damage;
        entity.damage(((DuckDamageSourcesMixin)entity.getDamageSources()).betteradventuremode$poison(), poisonDamage);
    }

    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration % this.update_modulus == this.update_checked_remainder;
    }
}
