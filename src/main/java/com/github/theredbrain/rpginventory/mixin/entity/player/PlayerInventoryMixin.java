package com.github.theredbrain.rpginventory.mixin.entity.player;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.entity.player.DuckEntityEquipmentMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.rpginventory.entity.player.PlayerEntityHelper;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public abstract class PlayerInventoryMixin implements DuckPlayerInventoryMixin {

	@Shadow
	@Final
	public static Int2ObjectMap<EquipmentSlot> EQUIPMENT_SLOT_MAPPING;

	@Shadow
	@Final
	public Player player;

	@Shadow
	@Final
	private NonNullList<ItemStack> items;

	@Shadow
	@Final
	private EntityEquipment equipment;

	@Inject(method = "setItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;set(ILjava/lang/Object;)Ljava/lang/Object;"))
	public void rpginventory$setItemInInventory(int slot, ItemStack stack, CallbackInfo ci) {
		if (stack.has(RPGInventory.BOUNDS_TO_PLAYER) && (!this.player.isCreative() || RPGInventory.SERVER_CONFIG.enable_item_bounding_in_creative.get())) {
			stack.remove(RPGInventory.BOUNDS_TO_PLAYER);
			if (!stack.has(RPGInventory.PLAYER_BOUND)) {
				stack.set(RPGInventory.PLAYER_BOUND, ResolvableProfile.createResolved(this.player.getGameProfile()));
			}
		}
		// fallback safety to avoid empty hand items in the regular inventory
		if (stack.is(Tags.EMPTY_HAND_WEAPONS)) {
			stack = ItemStack.EMPTY;
		}
	}

	@Inject(method = "setItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityEquipment;set(Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;"))
	public void rpginventory$setItemInEquipment(int slot, ItemStack stack, CallbackInfo ci, @Local(name = "equipmentSlot") EquipmentSlot equipmentSlot) {
		if (stack.has(RPGInventory.BOUNDS_TO_PLAYER) && (!this.player.isCreative() || RPGInventory.SERVER_CONFIG.enable_item_bounding_in_creative.get())) {
			stack.remove(RPGInventory.BOUNDS_TO_PLAYER);
			if (!stack.has(RPGInventory.PLAYER_BOUND)) {
				stack.set(RPGInventory.PLAYER_BOUND, ResolvableProfile.createResolved(this.player.getGameProfile()));
			}
		}
		// fallback safety to avoid empty hand items in the regular inventory
		if (!(ExtendedEquipmentSlot.isEmptyHandSlot(equipmentSlot)) && stack.is(Tags.EMPTY_HAND_WEAPONS)) {
			stack = ItemStack.EMPTY;
		}
	}

	@WrapMethod(method = "dropAll")
	public void rpginventory$wrap_dropAll(Operation<Void> original) {
		for (int i = 0; i < this.items.size(); i++) {
			ItemStack itemStack = this.items.get(i);
			if (itemStack.has(RPGInventory.IS_KEPT_ON_DEATH) || itemStack.is(Tags.EMPTY_HAND_WEAPONS)) {
				continue;
			}
			if (itemStack.has(RPGInventory.IS_DESTROYED_ON_DEATH) || RPGInventory.SERVER_CONFIG.destroy_dropped_items_on_death.get()) {
				this.items.set(i, ItemStack.EMPTY);
				continue;
			}
			if (!itemStack.isEmpty()) {
				this.player.drop(itemStack, true, false);
				this.items.set(i, ItemStack.EMPTY);
			}
		}

		this.equipment.dropAll(this.player);
	}

	@Override
	public void rpginventory$breakKeepInventoryItems() {
		((DuckEntityEquipmentMixin) this.equipment).rpginventory$breakKeepInventoryItems();
	}

	// picked up items are no longer placed into the offhand slot
	@WrapOperation(
			method = "getSlotWithRemainingSpace",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/player/Inventory;hasRemainingSpaceForItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z",
					ordinal = 1
			)
	)
	public boolean rpginventory$wrap_hasRemainingSpaceForItem(Inventory instance, ItemStack slotItemStack, ItemStack newItemStack, Operation<Boolean> original) {
		if (((DuckPlayerEntityMixin) this.player).rpginventory$isHandSlotOverhaulActive()) {
			return false;
		} else {
			return original.call(instance, slotItemStack, newItemStack);
		}
	}

	static {
		EQUIPMENT_SLOT_MAPPING.put(43, EquipmentSlot.MAINHAND);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.BELT.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), ExtendedEquipmentSlot.BELT);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.GLOVES.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), ExtendedEquipmentSlot.GLOVES);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.NECKLACE.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), ExtendedEquipmentSlot.NECKLACE);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.RING_1.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), ExtendedEquipmentSlot.RING_1);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.RING_2.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), ExtendedEquipmentSlot.RING_2);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.SHOULDERS.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), ExtendedEquipmentSlot.SHOULDERS);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.SPELL_1.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), ExtendedEquipmentSlot.SPELL_1);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.SPELL_2.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), ExtendedEquipmentSlot.SPELL_2);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.SPELL_3.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), ExtendedEquipmentSlot.SPELL_3);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.SPELL_4.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), ExtendedEquipmentSlot.SPELL_4);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.SPELL_5.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), ExtendedEquipmentSlot.SPELL_5);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.SPELL_6.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), ExtendedEquipmentSlot.SPELL_6);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.SPELL_7.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), ExtendedEquipmentSlot.SPELL_7);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.SPELL_8.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), ExtendedEquipmentSlot.SPELL_8);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.RELIC.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), ExtendedEquipmentSlot.RELIC);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.CLASS_ITEM.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), ExtendedEquipmentSlot.CLASS_ITEM);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.EMPTY_HAND.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), ExtendedEquipmentSlot.EMPTY_HAND);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.EMPTY_OFF_HAND.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), ExtendedEquipmentSlot.EMPTY_OFF_HAND);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.SHEATHED_HAND.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), ExtendedEquipmentSlot.SHEATHED_HAND);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.SHEATHED_OFF_HAND.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), ExtendedEquipmentSlot.SHEATHED_OFF_HAND);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.ALTERNATIVE_HAND.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), ExtendedEquipmentSlot.ALTERNATIVE_HAND);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.ALTERNATIVE_OFF_HAND.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), ExtendedEquipmentSlot.ALTERNATIVE_OFF_HAND);
	}
}
