package com.github.theredbrain.bamcore.effect;

import net.minecraft.entity.effect.StatusEffectCategory;

public class BrownMushroomFoodEffect extends FoodStatusEffect {
    public BrownMushroomFoodEffect() {
        super(StatusEffectCategory.BENEFICIAL, 3381504); // TODO better colour
    }

    // This method is called every tick to check whether it should apply the status effect or not
//    @Override
//    public boolean canApplyUpdateEffect(int duration, int amplifier) {
//        // In our case, we just make it return true so that it applies the status effect every tick.
//        return true;
//    }
//
//    // This method is called when it applies the status effect. We implement custom functionality here.
//    @Override
//    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
////        entity.getAttributes().addTemporaryModifiers();
////        if (entity instanceof PlayerEntity) {
//////            ((PlayerEntity) entity); // Higher amplifier gives you EXP faster
////        }
//    }
}
