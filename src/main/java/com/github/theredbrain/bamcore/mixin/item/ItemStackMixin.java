package com.github.theredbrain.bamcore.mixin.item;

import com.github.theredbrain.bamcore.api.item.BetterAdventureMode_BasicWeaponItem;
import com.github.theredbrain.bamcore.api.item.BetterAdventureMode_BasicShieldItem;
import com.github.theredbrain.bamcore.api.item.CustomArmorItem;
import com.github.theredbrain.bamcore.api.item.CustomDyeableArmorItem;
import com.github.theredbrain.bamcore.api.util.BetterAdventureModCoreItemUtils;
import com.google.common.collect.*;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

    /**
     * @author TheRedBrain
     */
    @Inject(method = "getAttributeModifiers", at = @At("HEAD"), cancellable = true)
    public void bam$getAttributeModifiers(EquipmentSlot slot, CallbackInfoReturnable<Multimap<EntityAttribute, EntityAttributeModifier>> cir) {
        if (this.getItem() instanceof CustomArmorItem && !(((CustomArmorItem)this.getItem()).isProtecting((ItemStack) (Object) this))) {
            cir.setReturnValue(HashMultimap.create());
            cir.cancel();
        }
        if (this.getItem() instanceof CustomDyeableArmorItem && !(((CustomDyeableArmorItem)this.getItem()).isProtecting((ItemStack) (Object) this))) {
            cir.setReturnValue(HashMultimap.create());
            cir.cancel();
        }
        if (this.getItem() instanceof BetterAdventureMode_BasicWeaponItem && !(BetterAdventureModCoreItemUtils.isUsable((ItemStack) (Object) this))) {
            cir.setReturnValue(HashMultimap.create());
            cir.cancel();
        }
        if (this.getItem() instanceof BetterAdventureMode_BasicShieldItem && !(BetterAdventureModCoreItemUtils.isUsable((ItemStack) (Object) this))) {
            cir.setReturnValue(HashMultimap.create());
            cir.cancel();
        }
    }
}
