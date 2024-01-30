package com.github.theredbrain.betteradventuremode.effect;

import com.github.theredbrain.betteradventuremode.entity.damage.DuckDamageSourcesMixin;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;

public class BleedingStatusEffect extends HarmfulStatusEffect {
    public BleedingStatusEffect() {
        super();
    }

    public void applyUpdateEffect(LivingEntity entity, int amplifier) { // TODO balance
        super.applyUpdateEffect(entity, amplifier);
        boolean isMoving = entity.prevX != entity.getX() || entity.prevY != entity.getY() || entity.prevZ != entity.getZ();
        float bleedingDamage = Math.max(1.0f, (float) (entity.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH) * 0.04f)) * (isMoving ? 2 : 1);
        if (entity.getHealth() > 1.0f && bleedingDamage > 1.0f) {
            entity.damage(((DuckDamageSourcesMixin)entity.getDamageSources()).betteradventuremode$bleeding(), bleedingDamage);
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
