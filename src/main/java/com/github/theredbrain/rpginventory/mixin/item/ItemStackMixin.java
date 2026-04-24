package com.github.theredbrain.rpginventory.mixin.item;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.apache.commons.lang3.function.TriConsumer;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemInstance {

	@Shadow
	public abstract int getMaxDamage();

	@Shadow
	public abstract int getDamageValue();

	@Shadow
	public abstract void hurtWithoutBreaking(int amount, Player player);

	@Shadow
	public abstract boolean is(Predicate<Holder<Item>> item);

	@WrapMethod(method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/server/level/ServerPlayer;Ljava/util/function/Consumer;)V")
	private void rpginventory$damage(int amount, ServerLevel level, @Nullable ServerPlayer player, Consumer<Item> onBreak, Operation<Void> original) {
		if (this.getDamageValue() + amount >= this.getMaxDamage() && (this.is(Tags.UNUSABLE_WHEN_LOW_DURABILITY) || ((DataComponentHolder) this).has(RPGInventory.UNUSABLE_WHEN_LOW_DURABILITY))) {
			this.hurtWithoutBreaking(amount, player);
		}
	}

	@WrapMethod(method = "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlotGroup;Lorg/apache/commons/lang3/function/TriConsumer;)V")
	public void rpginventory$getAttributeModifiers_fromAttributeModifierSlot(EquipmentSlotGroup slot, TriConsumer<Holder<Attribute>, AttributeModifier, ItemAttributeModifiers.Display> consumer, Operation<Void> original) {
		if (ItemUtils.isUsable((ItemStack) (Object) this)) {
			original.call(slot, consumer);
		}
	}

	@WrapMethod(method = "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V")
	public void rpginventory$getAttributeModifiers_fromEquipmentSlot(EquipmentSlot slot, BiConsumer<Holder<Attribute>, AttributeModifier> consumer, Operation<Void> original) {
		if (ItemUtils.isUsable((ItemStack) (Object) this)) {
			original.call(slot, consumer);
		}
	}
}
