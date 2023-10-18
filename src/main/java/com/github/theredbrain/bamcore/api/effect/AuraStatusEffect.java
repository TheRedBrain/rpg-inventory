package com.github.theredbrain.bamcore.api.effect;

import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class AuraStatusEffect extends StatusEffect {

    private final StatusEffect appliedStatusEffect;

    public AuraStatusEffect(StatusEffect appliedStatusEffect) {
        super(StatusEffectCategory.BENEFICIAL, 3381504); // TODO better colour
        this.appliedStatusEffect = appliedStatusEffect;
    }

    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        World world = entity.getWorld();
        if (entity instanceof PlayerEntity playerEntity && ((DuckPlayerEntityMixin)playerEntity).bamcore$getMana() <= 0) {
            entity.removeStatusEffect(this);
        }
        if (world.getTime() % 80L == 0L && !world.isClient) {
            BlockPos entityBlockPos = entity.getBlockPos();
            Box box = new Box(entityBlockPos).expand(10);
            List<LivingEntity> list = world.getNonSpectatingEntities(LivingEntity.class, box);
            Iterator var11 = list.iterator();

            LivingEntity livingEntity;
            while(var11.hasNext()) {
                livingEntity = (LivingEntity)var11.next();
                livingEntity.addStatusEffect(new StatusEffectInstance(this.appliedStatusEffect, 100, 0, true, true));
            }
        }
    }

    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
