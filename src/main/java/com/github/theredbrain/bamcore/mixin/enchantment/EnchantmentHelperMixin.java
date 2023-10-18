package com.github.theredbrain.bamcore.mixin.enchantment;

import com.github.theredbrain.bamcore.registry.Tags;
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

    @Inject(method = "getDepthStrider", at = @At("RETURN"), cancellable = true)
    private static void bamcore$hasDepthStriderTrinket(LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        boolean depth_strider_trinket_equipped = false;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(entity);
        if (trinkets.isPresent()) {
            Predicate<ItemStack> predicate = stack -> stack.isIn(Tags.DEPTH_STRIDER_TRINKETS);
            depth_strider_trinket_equipped = trinkets.get().isEquipped(predicate);
        }
        cir.setReturnValue(depth_strider_trinket_equipped ? 3 : cir.getReturnValue());
    }
    @Inject(method = "getLooting", at = @At("RETURN"), cancellable = true)
    private static void bamcore$hasLootingTrinket(LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        boolean looting_trinket_equipped = false;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(entity);
        if (trinkets.isPresent()) {
            Predicate<ItemStack> predicate = stack -> stack.isIn(Tags.LOOTING_TRINKETS);
            looting_trinket_equipped = trinkets.get().isEquipped(predicate);
        }
        cir.setReturnValue(looting_trinket_equipped ? 3 : cir.getReturnValue());
    }
}
