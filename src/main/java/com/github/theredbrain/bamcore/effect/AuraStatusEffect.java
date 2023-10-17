package com.github.theredbrain.bamcore.effect;

import com.github.theredbrain.bamcore.api.util.BetterAdventureModeCoreStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class AuraStatusEffect extends StatusEffect {

    private final StatusEffectInstance appliedStatusEffectInstance;

    public AuraStatusEffect(StatusEffectInstance appliedStatusEffectInstance) {
        super(StatusEffectCategory.BENEFICIAL, 3381504); // TODO better colour
        this.appliedStatusEffectInstance = appliedStatusEffectInstance;
    }

    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        World world = entity.getWorld();
        BlockPos entityBlockPos = entity.getBlockPos();
        if (world.getTime() % 80L == 0L) {
            if (!world.isClient) {
                Box box = (new Box(entityBlockPos)).expand(10);
                List<LivingEntity> list = world.getNonSpectatingEntities(LivingEntity.class, box);
                Iterator var11 = list.iterator();

                LivingEntity livingEntity;
                while(var11.hasNext()) {
                    livingEntity = (LivingEntity)var11.next();
                    livingEntity.addStatusEffect(this.appliedStatusEffectInstance);
                }
            }
        }
    }
}
