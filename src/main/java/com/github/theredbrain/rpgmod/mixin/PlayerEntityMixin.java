package com.github.theredbrain.rpgmod.mixin;

import com.github.theredbrain.rpgmod.effect.FoodStatusEffect;
import com.github.theredbrain.rpgmod.entity.PlayerEntityMixinDuck;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityMixinDuck {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

//    @Shadow
//    getStatusEffects() {
//
//    }
//
//    addStatusEffect() {
//
//    }

    public boolean tryEatAdventureFood(StatusEffect statusEffect, int duration) {

        if (getStatusEffects().isEmpty()) {
            addStatusEffect(new StatusEffectInstance(statusEffect, duration, 0, false, false, true));
            // TODO sounds
//            world.setBlockState(pos, state.with(INTACT, false));
            return true;
        } else {
            int currentEatenFoods = 0;
            List<StatusEffectInstance> currentEffects = getStatusEffects().stream().toList();
            for (StatusEffectInstance currentEffect : currentEffects) {
                if (currentEffect.getEffectType() == statusEffect) {
                    return false;
                } else if (currentEffect.getEffectType() instanceof FoodStatusEffect) {
                    currentEatenFoods++;
                }
            }

            if (currentEatenFoods < 3) {
                addStatusEffect(new StatusEffectInstance(statusEffect, duration, 0, false, false, true));
                // TODO sounds
//                world.setBlockState(pos, state.with(INTACT, false));
                return true;
            }
        }
        return false;
    }
}
