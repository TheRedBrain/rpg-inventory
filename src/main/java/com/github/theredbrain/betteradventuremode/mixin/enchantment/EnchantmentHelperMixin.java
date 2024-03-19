package com.github.theredbrain.betteradventuremode.mixin.enchantment;

import com.github.theredbrain.betteradventuremode.entity.DuckLivingEntityMixin;
import com.github.theredbrain.betteradventuremode.registry.Tags;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

    @Inject(method = "getSwiftSneakSpeedBoost", at = @At("RETURN"), cancellable = true)
    private static void betteradventuremode$getSwiftSneakSpeedBoost(LivingEntity entity, CallbackInfoReturnable<Float> cir) {
        boolean swift_sneak_level_3_item_equipped = false;
        Predicate<ItemStack> predicate = stack -> stack.isIn(Tags.GRANTS_SWIFT_SNEAK_LEVEL_3);
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(entity);
        if (trinkets.isPresent()) {
            swift_sneak_level_3_item_equipped = trinkets.get().isEquipped(predicate);
        }
        swift_sneak_level_3_item_equipped = swift_sneak_level_3_item_equipped || ((DuckLivingEntityMixin)entity).betteradventuremode$hasEquipped(predicate);
        cir.setReturnValue(swift_sneak_level_3_item_equipped ? 0.45F : cir.getReturnValue());
    }

    @Inject(method = "getRespiration", at = @At("RETURN"), cancellable = true)
    private static void betteradventuremode$getRespiration(LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        boolean respiration_level_3_item_equipped = false;
        Predicate<ItemStack> predicate = stack -> stack.isIn(Tags.GRANTS_RESPIRATION_LEVEL_3);
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(entity);
        if (trinkets.isPresent()) {
            respiration_level_3_item_equipped = trinkets.get().isEquipped(predicate);
        }
        respiration_level_3_item_equipped = respiration_level_3_item_equipped || ((DuckLivingEntityMixin)entity).betteradventuremode$hasEquipped(predicate);
        cir.setReturnValue(respiration_level_3_item_equipped ? 3 : cir.getReturnValue());
    }

    @Inject(method = "getDepthStrider", at = @At("RETURN"), cancellable = true)
    private static void betteradventuremode$getDepthStrider(LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        boolean depth_strider_level_3_item_equipped = false;
        Predicate<ItemStack> predicate = stack -> stack.isIn(Tags.GRANTS_DEPTH_STRIDER_LEVEL_3);
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(entity);
        if (trinkets.isPresent()) {
            depth_strider_level_3_item_equipped = trinkets.get().isEquipped(predicate);
        }
        depth_strider_level_3_item_equipped = depth_strider_level_3_item_equipped || ((DuckLivingEntityMixin)entity).betteradventuremode$hasEquipped(predicate);
        cir.setReturnValue(depth_strider_level_3_item_equipped ? 3 : cir.getReturnValue());
    }

    @Inject(method = "getLooting", at = @At("RETURN"), cancellable = true)
    private static void betteradventuremode$getLooting(LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        boolean looting_level_3_item_equipped = false;
        Predicate<ItemStack> predicate = stack -> stack.isIn(Tags.GRANTS_LOOTING_LEVEL_3);
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(entity);
        if (trinkets.isPresent()) {
            looting_level_3_item_equipped = trinkets.get().isEquipped(predicate);
        }
        looting_level_3_item_equipped = looting_level_3_item_equipped || ((DuckLivingEntityMixin)entity).betteradventuremode$hasEquipped(predicate);
        cir.setReturnValue(looting_level_3_item_equipped ? 3 : cir.getReturnValue());
    }
}
