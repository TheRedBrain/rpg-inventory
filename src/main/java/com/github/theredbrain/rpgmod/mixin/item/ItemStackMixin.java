package com.github.theredbrain.rpgmod.mixin.item;

import com.github.theredbrain.rpgmod.item.CustomArmorItem;
import com.github.theredbrain.rpgmod.item.CustomDyeableArmorItem;
import com.github.theredbrain.rpgmod.item.DuckItemMixin;
import com.github.theredbrain.rpgmod.item.DuckItemStackMixin;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin implements DuckItemStackMixin {

    @Shadow
    public Item getItem() {
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
    }

    public TypedActionResult<ItemStack> adventureUse(World world, PlayerEntity user) {
        return ((DuckItemMixin)this.getItem()).adventureUse(world, user);
    }
}
