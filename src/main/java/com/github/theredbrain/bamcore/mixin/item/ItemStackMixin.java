package com.github.theredbrain.bamcore.mixin.item;

import com.github.theredbrain.bamcore.api.item.BetterAdventureMode_BasicWeaponItem;
import com.github.theredbrain.bamcore.api.item.BetterAdventureMode_BasicShieldItem;
import com.github.theredbrain.bamcore.api.util.BetterAdventureModCoreItemUtils;
import com.github.theredbrain.bamcore.registry.Tags;
import com.google.common.collect.*;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.UnbreakingEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow
    public Item getItem() {
        throw new AssertionError();
    }

    @Shadow
    public Text getName() {
        throw new AssertionError();
    }

    @Shadow
    public int getDamage() {
        throw new AssertionError();
    }

    @Shadow public abstract boolean isDamageable();

    @Shadow public abstract void setDamage(int damage);

    @Shadow public abstract int getMaxDamage();

    /**
     * @author TheRedBrain
     */
    @Inject(method = "getAttributeModifiers", at = @At("HEAD"), cancellable = true)
    public void bam$getAttributeModifiers(EquipmentSlot slot, CallbackInfoReturnable<Multimap<EntityAttribute, EntityAttributeModifier>> cir) {
        if (this.getItem() instanceof BetterAdventureMode_BasicWeaponItem && !(BetterAdventureModCoreItemUtils.isUsable((ItemStack) (Object) this))) {
            cir.setReturnValue(HashMultimap.create());
            cir.cancel();
        }
        if (this.getItem() instanceof BetterAdventureMode_BasicShieldItem && !(BetterAdventureModCoreItemUtils.isUsable((ItemStack) (Object) this))) {
            cir.setReturnValue(HashMultimap.create());
            cir.cancel();
        }
    }

    /**
     * @author TheRedBrain
     * @reason
     */
    @Overwrite
    public boolean damage(int amount, Random random, @Nullable ServerPlayerEntity player) {
        boolean unbreaking_trinket_equipped = false;
        if (player != null) {
            Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
            if (trinkets.isPresent()) {
                Predicate<ItemStack> predicate = stack -> stack.isIn(Tags.UNBREAKING_TRINKETS);
                unbreaking_trinket_equipped = trinkets.get().isEquipped(predicate);
            }
        }

        int i;
        if (!this.isDamageable()) {
            return false;
        }
        if (amount > 0) {
            i = unbreaking_trinket_equipped ? 3 : EnchantmentHelper.getLevel(Enchantments.UNBREAKING, (ItemStack) (Object) this);
            int j = 0;
            for (int k = 0; i > 0 && k < amount; ++k) {
                if (!UnbreakingEnchantment.shouldPreventDamage((ItemStack) (Object) this, i, random)) continue;
                ++j;
            }
            if ((amount -= j) <= 0) {
                return false;
            }
        }
        if (player != null && amount != 0) {
            Criteria.ITEM_DURABILITY_CHANGED.trigger(player, (ItemStack) (Object) this, this.getDamage() + amount);
        }
        i = this.getDamage() + amount;
        this.setDamage(i);
        return i >= this.getMaxDamage();
    }
}
