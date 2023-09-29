package com.github.theredbrain.bamcore.effect;

import net.minecraft.entity.effect.StatusEffectCategory;

public class ChickenMealFoodEffect extends FoodStatusEffect {
    public ChickenMealFoodEffect() {
        super(StatusEffectCategory.BENEFICIAL, 3381504); // TODO better colour
    }

//    private EntityAttributeModifier maxHealthModifier = new EntityAttributeModifier(UUID.fromString(AttributeModifierUUIDs.MAX_HEALTH_CHICKEN_MEAL_FOOD_EFFECT), "max_health_chicken_meal_food_effect", 6.0, EntityAttributeModifier.Operation.ADDITION);
//
//    private Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers = (Multimap<EntityAttribute, EntityAttributeModifier>) Maps.newHashMap().put(Maps.newHashMap().put(EntityAttributes.GENERIC_MAX_HEALTH, maxHealthModifier));

    // This method is called every tick to check whether it should apply the status effect or not
//    @Override
//    public boolean canApplyUpdateEffect(int duration, int amplifier) {
//        // In our case, we just make it return true so that it applies the status effect every tick.
//        return true;
//    }

    // TODO clearing effect with console does not remove extra health
    // This method is called when it applies the status effect. We implement custom functionality here.
//    @Override
//    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
////        entity.getAttributes().hasAttribute
////        if (entity instanceof PlayerEntity) {
////            ((PlayerEntity) entity).getAttributes().addTemporaryModifiers(); // Higher amplifier gives you EXP faster
////        }
//    }

//    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
//        super.onRemoved(entity, attributes, amplifier);
//        if (entity instanceof PlayerEntity) {
//            ((PlayerEntity)entity).getAttributes().removeModifiers(attributeModifiers);
//        }
//    }
//
//    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
//        super.onApplied(entity, attributes, amplifier);
//        if (entity instanceof PlayerEntity) {
//            ((PlayerEntity)entity).getAttributes().addTemporaryModifiers(attributeModifiers);
//        }
//
//    }
//            this, this.getAttributes(), effect.getAmplifier()) {
//
//    }
}
