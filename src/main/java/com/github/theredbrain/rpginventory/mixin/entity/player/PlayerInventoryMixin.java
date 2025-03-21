package com.github.theredbrain.rpginventory.mixin.entity.player;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.rpginventory.registry.ItemRegistry;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
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

import java.util.List;
import java.util.Optional;

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

	@Unique
	private DefaultedList<ItemStack> rpginventory$handSlot;

	@Unique
	private DefaultedList<ItemStack> rpginventory$sheathedHandSlots;

	@Unique
	private DefaultedList<ItemStack> rpginventory$emptyHandSlots;

	@Unique
	private DefaultedList<ItemStack> rpginventory$alternativeHandSlots;

	/**
	 * @author TheRedBrain
	 */
	@Inject(method = "<init>", at = @At("TAIL"))
	public void PlayerInventory(PlayerEntity player, CallbackInfo ci) {
		this.rpginventory$handSlot = DefaultedList.ofSize(1, ItemStack.EMPTY);
		this.rpginventory$sheathedHandSlots = DefaultedList.ofSize(2, ItemStack.EMPTY);
		this.rpginventory$emptyHandSlots = DefaultedList.ofSize(2, ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON.getDefaultStack());
		this.rpginventory$alternativeHandSlots = DefaultedList.ofSize(2, ItemStack.EMPTY);
		this.combinedInventory = ImmutableList.of(this.main, this.armor, this.offHand, this.rpginventory$handSlot, this.rpginventory$sheathedHandSlots, this.rpginventory$emptyHandSlots, this.rpginventory$alternativeHandSlots);
	}

	@ModifyReturnValue(method = "getMainHandStack", at = @At("RETURN"))
	public ItemStack rpginventory$getMainHandStack(ItemStack original) {
		if (RPGInventory.SERVER_CONFIG.enable_hand_slot_overhaul.get()) {
			ItemStack emptyHandStack = rpginventory$getEmptyHand();
			ItemStack handStack = rpginventory$getHand();
			if (!((DuckPlayerEntityMixin) player).rpginventory$isHandStackSheathed()) {
				return ItemUtils.isUsable(handStack) ? handStack : emptyHandStack;
			}
			return emptyHandStack;
		} else {
			return ItemUtils.isUsable(original) ? original : ItemStack.EMPTY;
		}
	}

	/**
	 *  TODO find more compatible way
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
				nbtCompound.putByte("Slot", (byte)i);
				nbtList.add(this.main.get(i).encode(this.player.getRegistryManager(), nbtCompound));
			}
		}

		for (i = 0; i < this.armor.size(); i++) {
			if (!this.armor.get(i).isEmpty()) {
				nbtCompound = new NbtCompound();
				nbtCompound.putByte("Slot", (byte)(i + 100));
				nbtList.add(this.armor.get(i).encode(this.player.getRegistryManager(), nbtCompound));
			}
		}

		for (int ixx = 0; ixx < this.offHand.size(); ixx++) {
			if (!this.offHand.get(ixx).isEmpty()) {
				nbtCompound = new NbtCompound();
				nbtCompound.putByte("Slot", (byte)(ixx + 150));
				nbtList.add(this.offHand.get(ixx).encode(this.player.getRegistryManager(), nbtCompound));
			}
		}

		for (i = 0; i < this.rpginventory$handSlot.size(); i++) {
			if (!this.rpginventory$handSlot.get(i).isEmpty()) {
				nbtCompound = new NbtCompound();
				nbtCompound.putByte("Slot", (byte)(i + 160));
				nbtList.add(this.rpginventory$handSlot.get(i).encode(this.player.getRegistryManager(), nbtCompound));
			}
		}

		for (i = 0; i < this.rpginventory$sheathedHandSlots.size(); i++) {
			if (!this.rpginventory$sheathedHandSlots.get(i).isEmpty()) {
				nbtCompound = new NbtCompound();
				nbtCompound.putByte("Slot", (byte)(i + 170));
				nbtList.add(this.rpginventory$sheathedHandSlots.get(i).encode(this.player.getRegistryManager(), nbtCompound));
			}
		}

		for (i = 0; i < this.rpginventory$emptyHandSlots.size(); i++) {
			if (!this.rpginventory$emptyHandSlots.get(i).isEmpty()) {
				nbtCompound = new NbtCompound();
				nbtCompound.putByte("Slot", (byte)(i + 180));
				nbtList.add(this.rpginventory$emptyHandSlots.get(i).encode(this.player.getRegistryManager(), nbtCompound));
			}
		}

		for (i = 0; i < this.rpginventory$alternativeHandSlots.size(); i++) {
			if (!this.rpginventory$alternativeHandSlots.get(i).isEmpty()) {
				nbtCompound = new NbtCompound();
				nbtCompound.putByte("Slot", (byte)(i + 190));
				nbtList.add(this.rpginventory$alternativeHandSlots.get(i).encode(this.player.getRegistryManager(), nbtCompound));
			}
		}

		return nbtList;
	}

	/**
	 *  TODO find more compatible way
	 * @author TheRedBrain
	 * @reason save additional hand slots
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

		for (int i = 0; i < nbtList.size(); i++) {
			NbtCompound nbtCompound = nbtList.getCompound(i);
			int j = nbtCompound.getByte("Slot") & 255;
			ItemStack itemStack = (ItemStack)ItemStack.fromNbt(this.player.getRegistryManager(), nbtCompound).orElse(ItemStack.EMPTY);
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
			}
		}
	}

	@ModifyReturnValue(method = "size", at = @At("RETURN"))
	public int rpginventory$size(int original) {
		return original + this.rpginventory$handSlot.size() + this.rpginventory$sheathedHandSlots.size() + this.rpginventory$emptyHandSlots.size() + this.rpginventory$alternativeHandSlots.size();
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

	}

	@Override
	public ItemStack rpginventory$getOffHandStack() {
		ItemStack emptyOffHandStack = rpginventory$getEmptyOffhand();
		ItemStack offHandStack = this.offHand.get(0);
		if (!RPGInventory.SERVER_CONFIG.enable_hand_slot_overhaul.get()) {
			return ItemUtils.isUsable(offHandStack) ? offHandStack : ItemStack.EMPTY;
		}
		if (!((DuckPlayerEntityMixin) player).rpginventory$isOffhandStackSheathed()) {
			return ItemUtils.isUsable(offHandStack) && !offHandStack.isEmpty() ? offHandStack : emptyOffHandStack;
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
		if (RPGInventory.SERVER_CONFIG.enable_hand_slot_overhaul.get()) {
			return false;
		} else {
			return original.call(instance, existingStack, stack);
		}
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

	public ItemStack rpginventory$getGlovesStack() {
		ItemStack glovesStack = ItemStack.EMPTY;
		Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
		if (trinkets.isPresent()) {
			if (trinkets.get().getInventory().get("gloves") != null) {
				if (trinkets.get().getInventory().get("gloves").get("gloves") != null) {
					glovesStack = trinkets.get().getInventory().get("gloves").get("gloves").getStack(0);
				}
			}
		}
		return glovesStack;
	}

	public ItemStack rpginventory$setGlovesStack(ItemStack itemStack) {
		ItemStack oldStack = rpginventory$getGlovesStack();
		Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
		if (trinkets.isPresent()) {
			if (trinkets.get().getInventory().get("boots") != null) {
				if (trinkets.get().getInventory().get("boots").get("boots") != null) {
					trinkets.get().getInventory().get("boots").get("boots").setStack(0, itemStack);
				}
			}
		}
		return oldStack;
	}

	public ItemStack rpginventory$getShouldersStack() {
		ItemStack shouldersStack = ItemStack.EMPTY;
		Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
		if (trinkets.isPresent()) {
			if (trinkets.get().getInventory().get("shoulders") != null) {
				if (trinkets.get().getInventory().get("shoulders").get("shoulders") != null) {
					shouldersStack = trinkets.get().getInventory().get("shoulders").get("shoulders").getStack(0);
				}
			}
		}
		return shouldersStack;
	}

	public ItemStack rpginventory$setShouldersStack(ItemStack itemStack) {
		ItemStack oldStack = rpginventory$getShouldersStack();
		Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
		if (trinkets.isPresent()) {
			if (trinkets.get().getInventory().get("boots") != null) {
				if (trinkets.get().getInventory().get("boots").get("boots") != null) {
					trinkets.get().getInventory().get("boots").get("boots").setStack(0, itemStack);
				}
			}
		}
		return oldStack;
	}

	public ItemStack rpginventory$getRing1Stack() {
		ItemStack rings1Stack = ItemStack.EMPTY;
		Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
		if (trinkets.isPresent()) {
			if (trinkets.get().getInventory().get("rings_1") != null) {
				if (trinkets.get().getInventory().get("rings_1").get("ring") != null) {
					rings1Stack = trinkets.get().getInventory().get("rings_1").get("ring").getStack(0);
				}
			}
		}
		return rings1Stack;
	}

	public ItemStack rpginventory$setRing1Stack(ItemStack itemStack) {
		ItemStack oldStack = rpginventory$getRing1Stack();
		Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
		if (trinkets.isPresent()) {
			if (trinkets.get().getInventory().get("rings_1") != null) {
				if (trinkets.get().getInventory().get("rings_1").get("ring") != null) {
					trinkets.get().getInventory().get("rings_1").get("ring").setStack(0, itemStack);
				}
			}
		}
		return oldStack;
	}

	public ItemStack rpginventory$getRing2Stack() {
		ItemStack rings2Stack = ItemStack.EMPTY;
		Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
		if (trinkets.isPresent()) {
			if (trinkets.get().getInventory().get("rings_2") != null) {
				if (trinkets.get().getInventory().get("rings_2").get("ring") != null) {
					rings2Stack = trinkets.get().getInventory().get("rings_2").get("ring").getStack(0);
				}
			}
		}
		return rings2Stack;
	}

	public ItemStack rpginventory$setRing2Stack(ItemStack itemStack) {
		ItemStack oldStack = rpginventory$getRing2Stack();
		Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
		if (trinkets.isPresent()) {
			if (trinkets.get().getInventory().get("rings_2") != null) {
				if (trinkets.get().getInventory().get("rings_2").get("ring") != null) {
					trinkets.get().getInventory().get("rings_2").get("ring").setStack(0, itemStack);
				}
			}
		}
		return oldStack;
	}

	public ItemStack rpginventory$getBeltStack() {
		ItemStack beltsStack = ItemStack.EMPTY;
		Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
		if (trinkets.isPresent()) {
			if (trinkets.get().getInventory().get("belts") != null) {
				if (trinkets.get().getInventory().get("belts").get("belt") != null) {
					beltsStack = trinkets.get().getInventory().get("belts").get("belt").getStack(0);
				}
			}
		}
		return beltsStack;
	}

	public ItemStack rpginventory$setBeltStack(ItemStack itemStack) {
		ItemStack oldStack = rpginventory$getBeltStack();
		Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
		if (trinkets.isPresent()) {
			if (trinkets.get().getInventory().get("belts") != null) {
				if (trinkets.get().getInventory().get("belts").get("belt") != null) {
					trinkets.get().getInventory().get("belts").get("belt").setStack(0, itemStack);
				}
			}
		}
		return oldStack;
	}

	public ItemStack rpginventory$getNecklaceStack() {
		ItemStack necklacesStack = ItemStack.EMPTY;
		Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
		if (trinkets.isPresent()) {
			if (trinkets.get().getInventory().get("necklaces") != null) {
				if (trinkets.get().getInventory().get("necklaces").get("necklace") != null) {
					necklacesStack = trinkets.get().getInventory().get("necklaces").get("necklace").getStack(0);
				}
			}
		}
		return necklacesStack;
	}

	public ItemStack rpginventory$setNecklaceStack(ItemStack itemStack) {
		ItemStack oldStack = rpginventory$getNecklaceStack();
		Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
		if (trinkets.isPresent()) {
			if (trinkets.get().getInventory().get("necklaces") != null) {
				if (trinkets.get().getInventory().get("necklaces").get("necklace") != null) {
					trinkets.get().getInventory().get("necklaces").get("necklace").setStack(0, itemStack);
				}
			}
		}
		return oldStack;
	}


	public ItemStack rpginventory$getSpellSlotStack(int spellSlotNumber) {
		ItemStack spellSlotStack = ItemStack.EMPTY;
		Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
		if (trinkets.isPresent()) {
			if (trinkets.get().getInventory().get("spell_slot_" + spellSlotNumber) != null) {
				if (trinkets.get().getInventory().get("spell_slot_" + spellSlotNumber).get("spell") != null) {
					spellSlotStack = trinkets.get().getInventory().get("spell_slot_" + spellSlotNumber).get("spell").getStack(0);
				}
			}
		}
		return spellSlotStack;
	}

	public ItemStack rpginventory$setSpellSlotStack(ItemStack itemStack, int spellSlotNumber) {
		ItemStack oldStack = rpginventory$getSpellSlotStack(spellSlotNumber);
		Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
		if (trinkets.isPresent()) {
			if (trinkets.get().getInventory().get("spell_slot_" + spellSlotNumber) != null) {
				if (trinkets.get().getInventory().get("spell_slot_" + spellSlotNumber).get("spell") != null) {
					trinkets.get().getInventory().get("spell_slot_" + spellSlotNumber).get("spell").setStack(0, itemStack);
				}
			}
		}
		return oldStack;
	}

	public List<ItemStack> rpginventory$getArmor() {
		List<ItemStack> list = new java.util.ArrayList<>(List.of(this.rpginventory$getGlovesStack(), this.rpginventory$getShouldersStack()));
		list.addAll(this.armor);
		return list;
	}
}
