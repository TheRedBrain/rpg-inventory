package com.github.theredbrain.rpginventory.mixin.item;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

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

	@Inject(method = "damage(ILnet/minecraft/server/world/ServerWorld;Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Consumer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;setDamage(I)V"), cancellable = true)
	private void rpginventory$damage(int amount, ServerLevel world, @Nullable ServerPlayer player, Consumer<Item> breakCallback, CallbackInfo ci) {
		if (this.getDamage() + amount >= this.getMaxDamage() && (this.isIn(Tags.UNUSABLE_WHEN_LOW_DURABILITY) || ((DataComponentHolder) this).has(RPGInventory.UNUSABLE_WHEN_LOW_DURABILITY))) {
			this.setDamage(this.getMaxDamage() - 1);
			ci.cancel();
		}
	}

	@WrapMethod(method = "applyAttributeModifier(Lnet/minecraft/component/type/AttributeModifierSlot;Ljava/util/function/BiConsumer;)V")
	public void rpginventory$getAttributeModifiers_fromAttributeModifierSlot(EquipmentSlotGroup slot, BiConsumer<Holder<Attribute>, AttributeModifier> attributeModifierConsumer, Operation<Void> original) {
		if (ItemUtils.isUsable((ItemStack) (Object) this)) {
			original.call(slot, attributeModifierConsumer);
		}
	}

	@WrapMethod(method = "applyAttributeModifiers(Lnet/minecraft/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V")
	public void rpginventory$getAttributeModifiers_fromEquipmentSlot(EquipmentSlot slot, BiConsumer<Holder<Attribute>, AttributeModifier> attributeModifierConsumer, Operation<Void> original) {
		if (ItemUtils.isUsable((ItemStack) (Object) this)) {
			original.call(slot, attributeModifierConsumer);
		}
	}
}
