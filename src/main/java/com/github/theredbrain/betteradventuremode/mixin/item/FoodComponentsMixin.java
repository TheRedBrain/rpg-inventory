package com.github.theredbrain.betteradventuremode.mixin.item;

import com.github.theredbrain.betteradventuremode.registry.StatusEffectsRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.FoodComponents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FoodComponents.class)
public class FoodComponentsMixin {
    @Shadow
    @Final
    @Mutable
    public static FoodComponent APPLE = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.APPLE_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent BAKED_POTATO = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.BAKED_POTATO_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent BEEF = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.BEEF_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent BEETROOT = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.BEETROOT_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent BEETROOT_SOUP = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.BEETROOT_SOUP_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent BREAD = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.BREAD_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent CARROT = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.CARROT_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent CHICKEN = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.CHICKEN_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent CHORUS_FRUIT = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.CHORUS_FRUIT_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent COD = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.COD_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent COOKED_BEEF = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.COOKED_BEEF_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent COOKED_CHICKEN = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.COOKED_CHICKEN_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent COOKED_COD = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.COOKED_COD_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent COOKED_MUTTON = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.COOKED_MUTTON_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent COOKED_PORKCHOP = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.COOKED_PORKCHOP_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent COOKED_RABBIT = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.COOKED_RABBIT_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent COOKED_SALMON = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.COOKED_SALMON_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent COOKIE = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.COOKIE_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent DRIED_KELP = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.DRIED_KELP_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent ENCHANTED_GOLDEN_APPLE = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.ENCHANTED_GOLDEN_APPLE_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent GOLDEN_APPLE = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.GOLDEN_APPLE_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent GOLDEN_CARROT = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.GOLDEN_CARROT_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent HONEY_BOTTLE = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.HONEY_BOTTLE_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent MELON_SLICE = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.MELON_SLICE_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent MUSHROOM_STEW = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.MUSHROOM_STEW_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent MUTTON = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.MUTTON_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent POISONOUS_POTATO = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.POISONOUS_POTATO_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent PORKCHOP = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.PORKCHOP_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent POTATO = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.POTATO_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent PUFFERFISH = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.PUFFERFISH_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent PUMPKIN_PIE = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.PUMPKIN_PIE_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent RABBIT = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.RABBIT_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent RABBIT_STEW = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.RABBIT_STEW_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent ROTTEN_FLESH = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.ROTTEN_FLESH_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent SALMON = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.SALMON_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent SPIDER_EYE = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.SPIDER_EYE_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent SUSPICIOUS_STEW = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.SUSPICIOUS_STEW_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent SWEET_BERRIES = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.SWEET_BERRIES_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent GLOW_BERRIES = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.SWEET_BERRIES_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    @Shadow
    @Final
    @Mutable
    public static FoodComponent TROPICAL_FISH = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.TROPICAL_FISH_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
}
