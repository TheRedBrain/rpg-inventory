package com.github.theredbrain.rpginventory.mixin.entity.player;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.rpginventory.registry.ItemRegistry;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin implements DuckPlayerInventoryMixin {

	@Shadow
	@Final
	public DefaultedList<ItemStack> main;

	@Shadow
	@Final
	@Mutable
	public DefaultedList<ItemStack> armor;
	@Shadow
	@Final
	public PlayerEntity player;

	@Shadow
	@Final
	public DefaultedList<ItemStack> offHand;

	@Mutable
	@Shadow
	@Final
	private List<DefaultedList<ItemStack>> combinedInventory;

	@Shadow
	public abstract ItemStack getMainHandStack();

	@Unique
	private DefaultedList<ItemStack> rpginventory$handSlot;

	@Unique
	private DefaultedList<ItemStack> rpginventory$sheathedHandSlots;

	@Unique
	private DefaultedList<ItemStack> rpginventory$emptyHandSlots;

	@Unique
	private DefaultedList<ItemStack> rpginventory$alternativeHandSlots;

	@Unique
	private DefaultedList<ItemStack> rpginventory$additionalSlots;

	/**
	 * @author TheRedBrain
	 */
	@Inject(method = "<init>", at = @At("TAIL"))
	public void PlayerInventory(PlayerEntity player, CallbackInfo ci) {
		this.rpginventory$handSlot = DefaultedList.ofSize(1, ItemStack.EMPTY);
		this.rpginventory$sheathedHandSlots = DefaultedList.ofSize(2, ItemStack.EMPTY);
		this.rpginventory$emptyHandSlots = DefaultedList.ofSize(2, ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON.getDefaultStack());
		this.rpginventory$alternativeHandSlots = DefaultedList.ofSize(2, ItemStack.EMPTY);
		this.rpginventory$additionalSlots = DefaultedList.ofSize(16, ItemStack.EMPTY);
		this.combinedInventory = ImmutableList.of(this.main, this.armor, this.offHand, this.rpginventory$handSlot, this.rpginventory$sheathedHandSlots, this.rpginventory$emptyHandSlots, this.rpginventory$alternativeHandSlots, this.rpginventory$additionalSlots);
	}

	@ModifyReturnValue(method = "getMainHandStack", at = @At("RETURN"))
	public ItemStack rpginventory$getMainHandStack(ItemStack original) {
		if (RPGInventory.isHandSlotOverhaulActive()) {
			ItemStack emptyHandStack = rpginventory$getEmptyHand();
			ItemStack handStack = rpginventory$getHand();
			if (!((DuckPlayerEntityMixin) player).rpginventory$isHandStackSheathed()) {
				return ItemUtils.isUsable(handStack) && ItemUtils.isUsableByPlayer(handStack, this.player) && !handStack.isEmpty() ? handStack : emptyHandStack;
			}
		}
		return ItemUtils.isUsable(original) && ItemUtils.isUsableByPlayer(original, this.player) ? original : ItemStack.EMPTY;
	}

	/**
	 * TODO find more compatible way
	 *
	 * @author TheRedBrain
	 * @reason save additional hand slots
	 */
	@Overwrite
	public NbtList writeNbt(NbtList nbtList) {
		NbtCompound nbtCompound;
		int i;
		for (i = 0; i < this.main.size(); i++) {
			if (!this.main.get(i).isEmpty()) {
				nbtCompound = new NbtCompound();
				nbtCompound.putByte("Slot", (byte) i);
				nbtList.add(this.main.get(i).encode(this.player.getRegistryManager(), nbtCompound));
			}
		}

		for (i = 0; i < this.armor.size(); i++) {
			if (!this.armor.get(i).isEmpty()) {
				nbtCompound = new NbtCompound();
				nbtCompound.putByte("Slot", (byte) (i + 100));
				nbtList.add(this.armor.get(i).encode(this.player.getRegistryManager(), nbtCompound));
			}
		}

		for (int ixx = 0; ixx < this.offHand.size(); ixx++) {
			if (!this.offHand.get(ixx).isEmpty()) {
				nbtCompound = new NbtCompound();
				nbtCompound.putByte("Slot", (byte) (ixx + 150));
				nbtList.add(this.offHand.get(ixx).encode(this.player.getRegistryManager(), nbtCompound));
			}
		}

		for (i = 0; i < this.rpginventory$handSlot.size(); i++) {
			if (!this.rpginventory$handSlot.get(i).isEmpty()) {
				nbtCompound = new NbtCompound();
				nbtCompound.putByte("Slot", (byte) (i + 160));
				nbtList.add(this.rpginventory$handSlot.get(i).encode(this.player.getRegistryManager(), nbtCompound));
			}
		}

		for (i = 0; i < this.rpginventory$sheathedHandSlots.size(); i++) {
			if (!this.rpginventory$sheathedHandSlots.get(i).isEmpty()) {
				nbtCompound = new NbtCompound();
				nbtCompound.putByte("Slot", (byte) (i + 170));
				nbtList.add(this.rpginventory$sheathedHandSlots.get(i).encode(this.player.getRegistryManager(), nbtCompound));
			}
		}

		for (i = 0; i < this.rpginventory$emptyHandSlots.size(); i++) {
			if (!this.rpginventory$emptyHandSlots.get(i).isEmpty()) {
				nbtCompound = new NbtCompound();
				nbtCompound.putByte("Slot", (byte) (i + 180));
				nbtList.add(this.rpginventory$emptyHandSlots.get(i).encode(this.player.getRegistryManager(), nbtCompound));
			}
		}

		for (i = 0; i < this.rpginventory$alternativeHandSlots.size(); i++) {
			if (!this.rpginventory$alternativeHandSlots.get(i).isEmpty()) {
				nbtCompound = new NbtCompound();
				nbtCompound.putByte("Slot", (byte) (i + 190));
				nbtList.add(this.rpginventory$alternativeHandSlots.get(i).encode(this.player.getRegistryManager(), nbtCompound));
			}
		}

		for (i = 0; i < this.rpginventory$additionalSlots.size(); i++) {
			if (!this.rpginventory$additionalSlots.get(i).isEmpty()) {
				nbtCompound = new NbtCompound();
				nbtCompound.putByte("Slot", (byte) (i + 200));
				nbtList.add(this.rpginventory$additionalSlots.get(i).encode(this.player.getRegistryManager(), nbtCompound));
			}
		}

		return nbtList;
	}

	/**
	 * TODO find more compatible way
	 *
	 * @author TheRedBrain
	 * @reason save additional slots
	 */
	@Overwrite
	public void readNbt(NbtList nbtList) {
		this.main.clear();
		this.armor.clear();
		this.offHand.clear();
		this.rpginventory$handSlot.clear();
		this.rpginventory$sheathedHandSlots.clear();
		this.rpginventory$emptyHandSlots.clear();
		this.rpginventory$alternativeHandSlots.clear();
		this.rpginventory$additionalSlots.clear();

		for (int i = 0; i < nbtList.size(); i++) {
			NbtCompound nbtCompound = nbtList.getCompound(i);
			int j = nbtCompound.getByte("Slot") & 255;
			ItemStack itemStack = (ItemStack) ItemStack.fromNbt(this.player.getRegistryManager(), nbtCompound).orElse(ItemStack.EMPTY);
			if (j >= 0 && j < this.main.size()) {
				this.main.set(j, itemStack);
			} else if (j >= 100 && j < this.armor.size() + 100) {
				this.armor.set(j - 100, itemStack);
			} else if (j >= 150 && j < this.offHand.size() + 150) {
				this.offHand.set(j - 150, itemStack);
			} else if (j >= 160 && j < this.rpginventory$handSlot.size() + 160) {
				this.rpginventory$handSlot.set(j - 160, itemStack);
			} else if (j >= 170 && j < this.rpginventory$sheathedHandSlots.size() + 170) {
				this.rpginventory$sheathedHandSlots.set(j - 170, itemStack);
			} else if (j >= 180 && j < this.rpginventory$emptyHandSlots.size() + 180) {
				this.rpginventory$emptyHandSlots.set(j - 180, itemStack);
			} else if (j >= 190 && j < this.rpginventory$alternativeHandSlots.size() + 190) {
				this.rpginventory$alternativeHandSlots.set(j - 190, itemStack);
			} else if (j >= 190 && j < this.rpginventory$additionalSlots.size() + 200) {
				this.rpginventory$additionalSlots.set(j - 200, itemStack);
			}
		}
	}

	@ModifyReturnValue(method = "size", at = @At("RETURN"))
	public int rpginventory$size(int original) {
		return original + this.rpginventory$handSlot.size() + this.rpginventory$sheathedHandSlots.size() + this.rpginventory$emptyHandSlots.size() + this.rpginventory$alternativeHandSlots.size() + this.rpginventory$additionalSlots.size();
	}

	@Inject(method = "isEmpty", at = @At("HEAD"), cancellable = true)
	public void rpginventory$isEmpty(CallbackInfoReturnable<Boolean> cir) {

		for (ItemStack itemStack : this.rpginventory$handSlot) {
			if (!itemStack.isEmpty()) {
				cir.setReturnValue(false);
				cir.cancel();
			}
		}

		for (ItemStack itemStack : this.rpginventory$sheathedHandSlots) {
			if (!itemStack.isEmpty()) {
				cir.setReturnValue(false);
				cir.cancel();
			}
		}

		for (ItemStack itemStack : this.rpginventory$alternativeHandSlots) {
			if (!itemStack.isEmpty()) {
				cir.setReturnValue(false);
				cir.cancel();
			}
		}

		for (ItemStack itemStack : this.rpginventory$additionalSlots) {
			if (!itemStack.isEmpty()) {
				cir.setReturnValue(false);
				cir.cancel();
			}
		}

	}

	@Inject(method = "setStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;set(ILjava/lang/Object;)Ljava/lang/Object;"))
	public void rpginventory$setStack(int slot, ItemStack stack, CallbackInfo ci) {
		if (stack.contains(RPGInventory.BOUNDS_TO_PLAYER) && (!this.player.isCreative() || RPGInventory.SERVER_CONFIG.enable_item_bounding_in_creative.get())) {
			stack.remove(RPGInventory.BOUNDS_TO_PLAYER);
			if (!stack.contains(RPGInventory.PLAYER_BOUND)) {
				stack.set(RPGInventory.PLAYER_BOUND, new ProfileComponent(this.player.getGameProfile()));
			}
		}
		// fallback safety to avoid empty hand items in the regular inventory
		if (!(slot == 44 || slot == 45) && stack.isIn(Tags.EMPTY_HAND_WEAPONS)) {
			stack = ItemStack.EMPTY;
		}
	}

	@WrapMethod(method = "dropAll")
	public void rpginventory$wrap_dropAll(Operation<Void> original) {
		for (List<ItemStack> list : this.combinedInventory) {
			for (int i = 0; i < list.size(); i++) {
				ItemStack itemStack = (ItemStack) list.get(i);
				boolean isLoadOutItem = itemStack.contains(RPGInventory.LOAD_OUT_ITEM);
				if ((isLoadOutItem && RPGInventory.SERVER_CONFIG.should_keep_loadout_items_on_death.get()) || itemStack.contains(RPGInventory.IS_KEPT_ON_DEATH) || itemStack.isIn(Tags.EMPTY_HAND_WEAPONS)) {
					continue;
				}
				if ((isLoadOutItem && !RPGInventory.SERVER_CONFIG.should_keep_loadout_items_on_death.get()) || itemStack.contains(RPGInventory.IS_DESTROYED_ON_DEATH) || RPGInventory.SERVER_CONFIG.destroy_dropped_items_on_death.get()) {
					list.set(i, ItemStack.EMPTY);
					continue;
				}
				if (!itemStack.isEmpty()) {
					this.player.dropItem(itemStack, true, false);
					list.set(i, ItemStack.EMPTY);
				}
			}
		}
	}

	@Override
	public ItemStack rpginventory$getOffHandStack() {
		ItemStack emptyOffHandStack = rpginventory$getEmptyOffhand();
		ItemStack offHandStack = this.offHand.get(0);
		if (!RPGInventory.isHandSlotOverhaulActive()) {
			return ItemUtils.isUsable(offHandStack) && ItemUtils.isUsableByPlayer(offHandStack, this.player) ? offHandStack : ItemStack.EMPTY;
		}
		if (!((DuckPlayerEntityMixin) player).rpginventory$isOffhandStackSheathed()) {
			return ItemUtils.isUsable(offHandStack) && ItemUtils.isUsableByPlayer(offHandStack, this.player) && !offHandStack.isEmpty() ? offHandStack : emptyOffHandStack;
		}
		return ItemStack.EMPTY;
	}

	// picked up items are no longer placed into the offhand slot
	@WrapOperation(
			method = "getOccupiedSlotWithRoomForStack",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/player/PlayerInventory;canStackAddMore(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z",
					ordinal = 1
			)
	)
	public boolean rpginventory$wrap_canStackAddMore(PlayerInventory instance, ItemStack existingStack, ItemStack stack, Operation<Boolean> original) {
		if (RPGInventory.isHandSlotOverhaulActive()) {
			return false;
		} else {
			return original.call(instance, existingStack, stack);
		}
	}

	@WrapMethod(
			method = "getBlockBreakingSpeed"
	)
	public float rpginventory$wrap_getBlockBreakingSpeed(BlockState block, Operation<Float> original) {
		if (RPGInventory.isHandSlotOverhaulActive()) {
			return this.getMainHandStack().getMiningSpeedMultiplier(block);
		} else {
			return original.call(block);
		}
	}

	@WrapMethod(
			method = "dropSelectedItem"
	)
	public ItemStack rpginventory$wrap_dropSelectedItem(boolean entireStack, Operation<ItemStack> original) {
		if (RPGInventory.isHandSlotOverhaulActive()) {
			if (!((DuckPlayerEntityMixin) this.player).rpginventory$isHandStackSheathed() && !this.rpginventory$getHand().isEmpty()) {
				return Inventories.splitStack(this.rpginventory$handSlot, 0, entireStack ? this.rpginventory$getHand().getCount() : 1);
			}
		}
		return original.call(entireStack);
	}

	public ItemStack rpginventory$getHand() {
		return this.rpginventory$handSlot.get(0);
	}

	public ItemStack rpginventory$setHand(ItemStack itemStack) {
		ItemStack oldStack = rpginventory$getHand();
		this.rpginventory$handSlot.set(0, itemStack);
		return oldStack;
	}

	public ItemStack rpginventory$getAlternativeHand() {
		return this.rpginventory$alternativeHandSlots.get(0);
	}

	public ItemStack rpginventory$setAlternativeHand(ItemStack itemStack) {
		ItemStack oldStack = rpginventory$getAlternativeHand();
		this.rpginventory$alternativeHandSlots.set(0, itemStack);
		return oldStack;
	}

	public ItemStack rpginventory$getAlternativeOffhand() {
		return this.rpginventory$alternativeHandSlots.get(1);
	}

	public ItemStack rpginventory$setAlternativeOffhand(ItemStack itemStack) {
		ItemStack oldStack = rpginventory$getAlternativeOffhand();
		this.rpginventory$alternativeHandSlots.set(1, itemStack);
		return oldStack;
	}

	public ItemStack rpginventory$getEmptyHand() {
		return this.rpginventory$emptyHandSlots.get(0);
	}

	public ItemStack rpginventory$setEmptyHand(ItemStack itemStack) {
		ItemStack oldStack = rpginventory$getEmptyHand();
		this.rpginventory$emptyHandSlots.set(0, itemStack);
		return oldStack;
	}

	public ItemStack rpginventory$getEmptyOffhand() {
		return this.rpginventory$emptyHandSlots.get(1);
	}

	public ItemStack rpginventory$setEmptyOffhand(ItemStack itemStack) {
		ItemStack oldStack = rpginventory$getEmptyOffhand();
		this.rpginventory$emptyHandSlots.set(1, itemStack);
		return oldStack;
	}

	public ItemStack rpginventory$getSheathedHand() {
		return this.rpginventory$sheathedHandSlots.get(0);
	}

	public ItemStack rpginventory$setSheathedHand(ItemStack itemStack) {
		ItemStack oldStack = rpginventory$getSheathedHand();
		this.rpginventory$sheathedHandSlots.set(0, itemStack);
		return oldStack;
	}

	public ItemStack rpginventory$getSheathedOffhand() {
		return this.rpginventory$sheathedHandSlots.get(1);
	}

	public ItemStack rpginventory$setSheathedOffhand(ItemStack itemStack) {
		ItemStack oldStack = rpginventory$getSheathedOffhand();
		this.rpginventory$sheathedHandSlots.set(1, itemStack);
		return oldStack;
	}

	public ItemStack rpginventory$getAdditionalEquipmentStack(int index) {
		return this.rpginventory$additionalSlots.get(index);
	}

	public ItemStack rpginventory$setAdditionalEquipmentStack(int index, ItemStack itemStack) {
		ItemStack oldStack = rpginventory$getAdditionalEquipmentStack(index);
		this.rpginventory$additionalSlots.set(index, itemStack);
		return oldStack;
	}

	public List<ItemStack> rpginventory$getAdditionalNonArmorEquipmentItems() {
		List<ItemStack> list = new ArrayList<>(List.of(this.rpginventory$getAdditionalEquipmentStack(0), this.rpginventory$getAdditionalEquipmentStack(2), this.rpginventory$getAdditionalEquipmentStack(3), this.rpginventory$getAdditionalEquipmentStack(4)));
		for (int i = 6; i < 16; i++) {
			list.add(this.rpginventory$getAdditionalEquipmentStack(i));
		}
		return list;
	}

	public List<ItemStack> rpginventory$getSpellProvidingEquipmentItems() {
		List<ItemStack> list = new ArrayList<>();
		for (int i = 0; i < 16; i++) {
			list.add(this.rpginventory$getAdditionalEquipmentStack(i));
		}
		return list;
	}
}
