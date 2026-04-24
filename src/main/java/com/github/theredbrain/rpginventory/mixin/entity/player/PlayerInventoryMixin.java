package com.github.theredbrain.rpginventory.mixin.entity.player;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.entity.player.DuckEntityEquipmentMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
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

//	/**
//	 *
//	 * @author TheRedBrain
//	 * @reason save additional hand slots
//	 */
//	@Overwrite
//	public ListTag writeNbt(ListTag nbtList) {
//		CompoundTag nbtCompound;
//		int i;
//		for (i = 0; i < this.main.size(); i++) {
//			if (!this.main.get(i).isEmpty()) {
//				nbtCompound = new CompoundTag();
//				nbtCompound.putByte("Slot", (byte) i);
//				nbtList.add(this.main.get(i).save(this.player.registryAccess(), nbtCompound));
//			}
//		}
//
//		for (i = 0; i < this.armor.size(); i++) {
//			if (!this.armor.get(i).isEmpty()) {
//				nbtCompound = new CompoundTag();
//				nbtCompound.putByte("Slot", (byte) (i + 100));
//				nbtList.add(this.armor.get(i).save(this.player.registryAccess(), nbtCompound));
//			}
//		}
//
//		for (int ixx = 0; ixx < this.offHand.size(); ixx++) {
//			if (!this.offHand.get(ixx).isEmpty()) {
//				nbtCompound = new CompoundTag();
//				nbtCompound.putByte("Slot", (byte) (ixx + 150));
//				nbtList.add(this.offHand.get(ixx).save(this.player.registryAccess(), nbtCompound));
//			}
//		}
//
//		for (i = 0; i < this.rpginventory$handSlot.size(); i++) {
//			if (!this.rpginventory$handSlot.get(i).isEmpty()) {
//				nbtCompound = new CompoundTag();
//				nbtCompound.putByte("Slot", (byte) (i + 160));
//				nbtList.add(this.rpginventory$handSlot.get(i).save(this.player.registryAccess(), nbtCompound));
//			}
//		}
//
//		for (i = 0; i < this.rpginventory$sheathedHandSlots.size(); i++) {
//			if (!this.rpginventory$sheathedHandSlots.get(i).isEmpty()) {
//				nbtCompound = new CompoundTag();
//				nbtCompound.putByte("Slot", (byte) (i + 170));
//				nbtList.add(this.rpginventory$sheathedHandSlots.get(i).save(this.player.registryAccess(), nbtCompound));
//			}
//		}
//
//		for (i = 0; i < this.rpginventory$emptyHandSlots.size(); i++) {
//			if (!this.rpginventory$emptyHandSlots.get(i).isEmpty()) {
//				nbtCompound = new CompoundTag();
//				nbtCompound.putByte("Slot", (byte) (i + 180));
//				nbtList.add(this.rpginventory$emptyHandSlots.get(i).save(this.player.registryAccess(), nbtCompound));
//			}
//		}
//
//		for (i = 0; i < this.rpginventory$alternativeHandSlots.size(); i++) {
//			if (!this.rpginventory$alternativeHandSlots.get(i).isEmpty()) {
//				nbtCompound = new CompoundTag();
//				nbtCompound.putByte("Slot", (byte) (i + 190));
//				nbtList.add(this.rpginventory$alternativeHandSlots.get(i).save(this.player.registryAccess(), nbtCompound));
//			}
//		}
//
//		for (i = 0; i < this.rpginventory$additionalSlots.size(); i++) {
//			if (!this.rpginventory$additionalSlots.get(i).isEmpty()) {
//				nbtCompound = new CompoundTag();
//				nbtCompound.putByte("Slot", (byte) (i + 200));
//				nbtList.add(this.rpginventory$additionalSlots.get(i).save(this.player.registryAccess(), nbtCompound));
//			}
//		}
//
//		return nbtList;
//	}
//
//	/**
//	 *
//	 * @author TheRedBrain
//	 * @reason save additional slots
//	 */
//	@Overwrite
//	public void readNbt(ListTag nbtList) {
//		this.main.clear();
//		this.armor.clear();
//		this.offHand.clear();
//		this.rpginventory$handSlot.clear();
//		this.rpginventory$sheathedHandSlots.clear();
//		this.rpginventory$emptyHandSlots.clear();
//		this.rpginventory$alternativeHandSlots.clear();
//		this.rpginventory$additionalSlots.clear();
//
//		for (int i = 0; i < nbtList.size(); i++) {
//			CompoundTag nbtCompound = nbtList.getCompound(i);
//			int j = nbtCompound.getByte("Slot") & 255;
//			ItemStack itemStack = (ItemStack) ItemStack.parse(this.player.registryAccess(), nbtCompound).orElse(ItemStack.EMPTY);
//			if (j >= 0 && j < this.main.size()) {
//				this.main.set(j, itemStack);
//			} else if (j >= 100 && j < this.armor.size() + 100) {
//				this.armor.set(j - 100, itemStack);
//			} else if (j >= 150 && j < this.offHand.size() + 150) {
//				this.offHand.set(j - 150, itemStack);
//			} else if (j >= 160 && j < this.rpginventory$handSlot.size() + 160) {
//				this.rpginventory$handSlot.set(j - 160, itemStack);
//			} else if (j >= 170 && j < this.rpginventory$sheathedHandSlots.size() + 170) {
//				this.rpginventory$sheathedHandSlots.set(j - 170, itemStack);
//			} else if (j >= 180 && j < this.rpginventory$emptyHandSlots.size() + 180) {
//				this.rpginventory$emptyHandSlots.set(j - 180, itemStack);
//			} else if (j >= 190 && j < this.rpginventory$alternativeHandSlots.size() + 190) {
//				this.rpginventory$alternativeHandSlots.set(j - 190, itemStack);
//			} else if (j >= 190 && j < this.rpginventory$additionalSlots.size() + 200) {
//				this.rpginventory$additionalSlots.set(j - 200, itemStack);
//			}
//		}
//	}

//	@ModifyReturnValue(method = "size", at = @At("RETURN"))
//	public int rpginventory$size(int original) {
//		return original + this.rpginventory$handSlot.size() + this.rpginventory$sheathedHandSlots.size() + this.rpginventory$emptyHandSlots.size() + this.rpginventory$alternativeHandSlots.size() + this.rpginventory$additionalSlots.size();
//	}

//	@Inject(method = "isEmpty", at = @At("HEAD"), cancellable = true)
//	public void rpginventory$isEmpty(CallbackInfoReturnable<Boolean> cir) {
//
//		for (ItemStack itemStack : this.rpginventory$handSlot) {
//			if (!itemStack.isEmpty()) {
//				cir.setReturnValue(false);
//				cir.cancel();
//			}
//		}
//
//		for (ItemStack itemStack : this.rpginventory$sheathedHandSlots) {
//			if (!itemStack.isEmpty()) {
//				cir.setReturnValue(false);
//				cir.cancel();
//			}
//		}
//
//		for (ItemStack itemStack : this.rpginventory$alternativeHandSlots) {
//			if (!itemStack.isEmpty()) {
//				cir.setReturnValue(false);
//				cir.cancel();
//			}
//		}
//
//		for (ItemStack itemStack : this.rpginventory$additionalSlots) {
//			if (!itemStack.isEmpty()) {
//				cir.setReturnValue(false);
//				cir.cancel();
//			}
//		}
//
//	}

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

//	@Override
//	public ItemStack rpginventory$getCurrentMainHandStack() {
//		ItemStack emptyOffHandStack = rpginventory$getEmptyOffhand();
//		ItemStack offHandStack = this.offHand.get(0);
//		if (!RPGInventory.isHandSlotOverhaulActive()) {
//			return ItemUtils.isUsable(offHandStack) && ItemUtils.isUsableByPlayer(offHandStack, this.player) ? offHandStack : ItemStack.EMPTY;
//		}
//		if (!((DuckPlayerEntityMixin) player).rpginventory$isOffhandStackSheathed()) {
//			return ItemUtils.isUsable(offHandStack) && ItemUtils.isUsableByPlayer(offHandStack, this.player) && !offHandStack.isEmpty() ? offHandStack : emptyOffHandStack;
//		}
//		return ItemStack.EMPTY;
//	}

//	@Override
//	public ItemStack rpginventory$getCurrentOffHandStack() {
//		ItemStack emptyOffHandStack = rpginventory$getEmptyOffhand();
//		ItemStack offHandStack = this.offHand.get(0);
//		if (!RPGInventory.isHandSlotOverhaulActive()) {
//			return ItemUtils.isUsable(offHandStack) && ItemUtils.isUsableByPlayer(offHandStack, this.player) ? offHandStack : ItemStack.EMPTY;
//		}
//		if (!((DuckPlayerEntityMixin) player).rpginventory$isOffhandStackSheathed()) {
//			return ItemUtils.isUsable(offHandStack) && ItemUtils.isUsableByPlayer(offHandStack, this.player) && !offHandStack.isEmpty() ? offHandStack : emptyOffHandStack;
//		}
//		return ItemStack.EMPTY;
//	}

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
		if (RPGInventory.isHandSlotOverhaulActive()) {
			return false;
		} else {
			return original.call(instance, slotItemStack, newItemStack);
		}
	}

//	@WrapMethod(
//			method = "dropSelectedItem"
//	)
//	public ItemStack rpginventory$wrap_dropSelectedItem(boolean entireStack, Operation<ItemStack> original) {
//		if (RPGInventory.isHandSlotOverhaulActive()) {
//			if (!((DuckPlayerEntityMixin) this.player).rpginventory$isHandStackSheathed() && !this.rpginventory$getHand().isEmpty()) {
//				return ContainerHelper.removeItem(this.rpginventory$handSlot, 0, entireStack ? this.rpginventory$getHand().getCount() : 1);
//			}
//		}
//		return original.call(entireStack);
//	}

//	@Override
//	public ItemStack rpginventory$getHand() {
//		return this.rpginventory$handSlot.get(0);
//	}
//
//	@Override
//	public ItemStack rpginventory$setHand(ItemStack itemStack) {
//		ItemStack oldStack = rpginventory$getHand();
//		this.rpginventory$handSlot.set(0, itemStack);
//		return oldStack;
//	}
//
//	@Override
//	public ItemStack rpginventory$getAlternativeHand() {
//		return this.rpginventory$alternativeHandSlots.get(0);
//	}
//
//	@Override
//	public ItemStack rpginventory$setAlternativeHand(ItemStack itemStack) {
//		ItemStack oldStack = rpginventory$getAlternativeHand();
//		this.rpginventory$alternativeHandSlots.set(0, itemStack);
//		return oldStack;
//	}
//
//	@Override
//	public ItemStack rpginventory$getAlternativeOffhand() {
//		return this.rpginventory$alternativeHandSlots.get(1);
//	}
//
//	@Override
//	public ItemStack rpginventory$setAlternativeOffhand(ItemStack itemStack) {
//		ItemStack oldStack = rpginventory$getAlternativeOffhand();
//		this.rpginventory$alternativeHandSlots.set(1, itemStack);
//		return oldStack;
//	}
//
//	@Override
//	public ItemStack rpginventory$getEmptyHand() {
//		return this.rpginventory$emptyHandSlots.get(0);
//	}
//
//	@Override
//	public ItemStack rpginventory$setEmptyHand(ItemStack itemStack) {
//		ItemStack oldStack = rpginventory$getEmptyHand();
//		this.rpginventory$emptyHandSlots.set(0, itemStack);
//		return oldStack;
//	}
//
//	@Override
//	public ItemStack rpginventory$getEmptyOffhand() {
//		return this.rpginventory$emptyHandSlots.get(1);
//	}
//
//	@Override
//	public ItemStack rpginventory$setEmptyOffhand(ItemStack itemStack) {
//		ItemStack oldStack = rpginventory$getEmptyOffhand();
//		this.rpginventory$emptyHandSlots.set(1, itemStack);
//		return oldStack;
//	}
//
//	@Override
//	public ItemStack rpginventory$getSheathedHand() {
//		return this.rpginventory$sheathedHandSlots.get(0);
//	}
//
//	@Override
//	public ItemStack rpginventory$setSheathedHand(ItemStack itemStack) {
//		ItemStack oldStack = rpginventory$getSheathedHand();
//		this.rpginventory$sheathedHandSlots.set(0, itemStack);
//		return oldStack;
//	}
//
//	@Override
//	public ItemStack rpginventory$getSheathedOffhand() {
//		return this.rpginventory$sheathedHandSlots.get(1);
//	}
//
//	@Override
//	public ItemStack rpginventory$setSheathedOffhand(ItemStack itemStack) {
//		ItemStack oldStack = rpginventory$getSheathedOffhand();
//		this.rpginventory$sheathedHandSlots.set(1, itemStack);
//		return oldStack;
//	}
//
//	@Override
//	public ItemStack rpginventory$getAdditionalEquipmentStack(int index) {
//		return this.rpginventory$additionalSlots.get(index);
//	}
//
//	@Override
//	public ItemStack rpginventory$setAdditionalEquipmentStack(int index, ItemStack itemStack) {
//		ItemStack oldStack = rpginventory$getAdditionalEquipmentStack(index);
//		this.rpginventory$additionalSlots.set(index, itemStack);
//		return oldStack;
//	}
//
//	@Override
//	public List<ItemStack> rpginventory$getAdditionalNonArmorEquipmentItems() {
//		List<ItemStack> list = new ArrayList<>(List.of(this.rpginventory$getAdditionalEquipmentStack(0), this.rpginventory$getAdditionalEquipmentStack(2), this.rpginventory$getAdditionalEquipmentStack(3), this.rpginventory$getAdditionalEquipmentStack(4)));
//		for (int i = 6; i < 16; i++) {
//			list.add(this.rpginventory$getAdditionalEquipmentStack(i));
//		}
//		return list;
//	}
//
//	@Override
//	public List<ItemStack> rpginventory$getSpellProvidingEquipmentItems() {
//		List<ItemStack> list = new ArrayList<>();
//		for (int i = 0; i < 16; i++) {
//			list.add(this.rpginventory$getAdditionalEquipmentStack(i));
//		}
//		return list;
//	}

	static {
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.BELT.getIndex(43), ExtendedEquipmentSlot.BELT);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.GLOVES.getIndex(43), ExtendedEquipmentSlot.GLOVES);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.NECKLACE.getIndex(43), ExtendedEquipmentSlot.NECKLACE);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.RING_1.getIndex(43), ExtendedEquipmentSlot.RING_1);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.RING_2.getIndex(43), ExtendedEquipmentSlot.RING_2);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.SHOULDERS.getIndex(43), ExtendedEquipmentSlot.SHOULDERS);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.SPELL_1.getIndex(43), ExtendedEquipmentSlot.SPELL_1);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.SPELL_2.getIndex(43), ExtendedEquipmentSlot.SPELL_2);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.SPELL_3.getIndex(43), ExtendedEquipmentSlot.SPELL_3);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.SPELL_4.getIndex(43), ExtendedEquipmentSlot.SPELL_4);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.SPELL_5.getIndex(43), ExtendedEquipmentSlot.SPELL_5);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.SPELL_6.getIndex(43), ExtendedEquipmentSlot.SPELL_6);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.SPELL_7.getIndex(43), ExtendedEquipmentSlot.SPELL_7);
		EQUIPMENT_SLOT_MAPPING.put(ExtendedEquipmentSlot.SPELL_8.getIndex(43), ExtendedEquipmentSlot.SPELL_8);
	}
}
