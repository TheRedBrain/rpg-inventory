package com.github.theredbrain.betteradventuremode.effect;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.entity.DuckLivingEntityMixin;
import com.github.theredbrain.betteradventuremode.entity.damage.DuckDamageSourcesMixin;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;

public class BleedingStatusEffect extends HarmfulStatusEffect {
    private final int update_modulus;
    private final int update_checked_remainder;
    public BleedingStatusEffect() {
        super();
        this.update_modulus = BetterAdventureMode.gamePlayBalanceConfig.bleeding_update_modulus;
        this.update_checked_remainder = BetterAdventureMode.gamePlayBalanceConfig.bleeding_update_checked_remainder;
    }

    public void applyUpdateEffect(LivingEntity entity, int amplifier) { // TODO balance
        super.applyUpdateEffect(entity, amplifier);
        if (!entity.getEntityWorld().isClient) {
            boolean isMoving = ((DuckLivingEntityMixin)entity).betteradventuremode$isMoving();
            float bleedingDamage = Math.max(1.0f, (float) (entity.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH) * 0.1f)) * (isMoving ? 2 : 1);
            entity.damage(((DuckDamageSourcesMixin)entity.getDamageSources()).betteradventuremode$bleeding(), bleedingDamage);
        }
    }

    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration % this.update_modulus == this.update_checked_remainder;
    }
}
