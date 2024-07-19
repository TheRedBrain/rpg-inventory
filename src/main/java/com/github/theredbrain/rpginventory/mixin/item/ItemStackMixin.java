package com.github.theredbrain.rpginventory.mixin.item;

import com.github.theredbrain.rpginventory.registry.Tags;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

	@Shadow
	public abstract int getMaxDamage();

	@Shadow
	public abstract boolean isIn(TagKey<Item> tag);

	@Shadow
	public abstract int getDamage();

	@Shadow
	public abstract void setDamage(int damage);

	@Inject(method = "damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;setDamage(I)V"), cancellable = true)
	private void rpginventory$damage(int amount, Random random, ServerPlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
		if (this.getDamage() + amount >= this.getMaxDamage() && this.isIn(Tags.UNUSABLE_WHEN_LOW_DURABILITY)) {
			this.setDamage(this.getMaxDamage() - 1);
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "getAttributeModifiers", at = @At("HEAD"), cancellable = true)
	public void rpginventory$getAttributeModifiers(EquipmentSlot slot, CallbackInfoReturnable<Multimap<EntityAttribute, EntityAttributeModifier>> cir) {
		if (!(ItemUtils.isUsable((ItemStack) (Object) this))) {
			cir.setReturnValue(HashMultimap.create());
			cir.cancel();
		}
	}
}
