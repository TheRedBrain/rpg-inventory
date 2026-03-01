package com.github.theredbrain.rpginventory.mixin.entity.player;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlotType;
import com.github.theredbrain.rpginventory.entity.RendersSheathedWeapons;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.rpginventory.entity.player.PlayerEntityHelper;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements DuckPlayerEntityMixin, RendersSheathedWeapons {

	@Shadow
	@Final
	PlayerInventory inventory;

	@Shadow
	public abstract PlayerInventory getInventory();

	@Shadow
	public abstract ItemStack getEquippedStack(EquipmentSlot slot);

	@Shadow
	public abstract boolean isCreative();

	@Unique
	private boolean isAdventureHotbarCleanedUp = false;

	@Unique
	private static final TrackedData<Boolean> IS_HAND_STACK_SHEATHED = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	@Unique
	private static final TrackedData<Boolean> IS_OFFHAND_STACK_SHEATHED = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	@Unique
	private static final TrackedData<Boolean> IS_HAND_SLOT_OVERHAUL_ACTIVE = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	@Unique
	private static final TrackedData<Boolean> ARE_ALTERNATIVE_HAND_SLOTS_ACTIVE = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	@Unique
	private static final TrackedData<Integer> OLD_ACTIVE_SPELL_SLOT_AMOUNT = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);

	@Unique
	private static final TrackedData<Boolean> SHOULD_EJECT_EXCLUSIVE_EQUIPMENT = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "initDataTracker", at = @At("RETURN"))
	protected void rpginventory$initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
		builder.add(IS_HAND_STACK_SHEATHED, false);
		builder.add(IS_OFFHAND_STACK_SHEATHED, false);
		builder.add(IS_HAND_SLOT_OVERHAUL_ACTIVE, true);
		builder.add(ARE_ALTERNATIVE_HAND_SLOTS_ACTIVE, true);
		builder.add(OLD_ACTIVE_SPELL_SLOT_AMOUNT, -1);
		builder.add(SHOULD_EJECT_EXCLUSIVE_EQUIPMENT, false);

	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void rpginventory$tick(CallbackInfo ci) {
		PlayerEntity playerEntity = (PlayerEntity) (Object) this;
		this.getAttributes().addTemporaryModifiers(getNaturalAttributeModifiers(this.getWorld()));
		PlayerEntityHelper.rpginventory$updateEquipmentStatusEffects(playerEntity);
		if (!this.getWorld().isClient) {
			PlayerEntityHelper.rpginventory$ejectItemsFromInactiveSpellSlots(playerEntity);
			PlayerEntityHelper.rpginventory$ejectExclusiveEquipment(playerEntity);
			PlayerEntityHelper.rpginventory$ejectItemsFromInactiveHandSlots(playerEntity);
//            PlayerInventoryHelper.rpginventory$ejectNonHotbarItemsFromHotbar(playerEntity); TODO disabled for now, needs overhaul
		}
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void rpginventory$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {

		this.rpginventory$setIsHandStackSheathed(nbt.contains("is_hand_stack_sheathed", NbtElement.BYTE_TYPE));

		this.rpginventory$setIsOffhandStackSheathed(nbt.contains("is_offhand_stack_sheathed", NbtElement.BYTE_TYPE));

		this.rpginventory$setIsHandSlotOverhaulActive(nbt.contains("is_hand_slot_overhaul_active", NbtElement.BYTE_TYPE));

		if (nbt.contains("old_active_spell_slot_amount", NbtElement.INT_TYPE)) {
			this.rpginventory$setOldActiveSpellSlotAmount(nbt.getInt("old_active_spell_slot_amount"));
		} else {
			this.rpginventory$setOldActiveSpellSlotAmount(-1);
		}
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	public void rpginventory$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {

		if (this.rpginventory$isHandStackSheathed()) {
			nbt.putBoolean("is_hand_stack_sheathed", true);
		} else {
			nbt.remove("is_hand_stack_sheathed");
		}

		if (this.rpginventory$isOffhandStackSheathed()) {
			nbt.putBoolean("is_offhand_stack_sheathed", true);
		} else {
			nbt.remove("is_offhand_stack_sheathed");
		}

		if (this.rpginventory$isHandSlotOverhaulActive()) {
			nbt.putBoolean("is_hand_slot_overhaul_active", true);
		} else {
			nbt.remove("is_hand_slot_overhaul_active");
		}

		int old_active_spell_slot_amount = this.rpginventory$oldActiveSpellSlotAmount();
		if (old_active_spell_slot_amount != -1) {
			nbt.putInt("old_active_spell_slot_amount", old_active_spell_slot_amount);
		} else {
			nbt.remove("old_active_spell_slot_amount");
		}
	}

	@WrapMethod(method = "equipStack")
	public void equipStack(EquipmentSlot slot, ItemStack stack, Operation<Void> original) {
		boolean isHandSlotOverhaulActive = RPGInventory.isHandSlotOverhaulActive();
		this.processEquippedStack(stack);
		if (slot == EquipmentSlot.MAINHAND) {
			if (stack.isIn(Tags.EMPTY_HAND_WEAPONS)) {
				this.onEquipStack(slot, ((DuckPlayerInventoryMixin) this.inventory).rpginventory$setEmptyHand(stack), stack);
			} else {
				this.onEquipStack(slot, ((DuckPlayerEntityMixin) this).rpginventory$isHandStackSheathed() || !isHandSlotOverhaulActive ? this.inventory.main.set(this.inventory.selectedSlot, stack) : ((DuckPlayerInventoryMixin) this.inventory).rpginventory$setHand(stack), stack);
			}
		} else if (slot == EquipmentSlot.OFFHAND) {
			if (stack.isIn(Tags.EMPTY_HAND_WEAPONS)) {
				this.onEquipStack(slot, ((DuckPlayerInventoryMixin) this.inventory).rpginventory$setEmptyOffhand(stack), stack);
			} else {
				this.onEquipStack(slot, ((DuckPlayerEntityMixin) this).rpginventory$isOffhandStackSheathed() && isHandSlotOverhaulActive ? ((DuckPlayerInventoryMixin) this.inventory).rpginventory$setSheathedOffhand(stack) : this.inventory.offHand.set(0, stack), stack);
			}
		} else if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
			this.onEquipStack(slot, this.inventory.armor.set(slot.getEntitySlotId(), stack), stack);
		} else if (slot.getType() == ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE) {
			this.onEquipStack(slot, ((DuckPlayerInventoryMixin) this.inventory).rpginventory$setAdditionalEquipmentStack(slot.getEntitySlotId(), stack), stack);
		} else {
			original.call(slot, stack);
		}
	}

	@Inject(method = "dropInventory", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;vanishCursedItems()V", ordinal = 0), cancellable = true)
	private void rpginventory$pre_vanishCursedItems(CallbackInfo ci) {
		if (this.hasStatusEffect(RPGInventory.KEEP_INVENTORY)) {
			PlayerEntityHelper.rpginventory$breakKeepInventoryItems((PlayerEntity) (Object) this);
			ci.cancel();
		}
	}

	@WrapMethod(method = "getEquippedStack")
	public ItemStack rpginventory$getEquippedStack(EquipmentSlot slot, Operation<ItemStack> original) {
		if (slot == EquipmentSlot.OFFHAND) {
			return ((DuckPlayerInventoryMixin) this.inventory).rpginventory$getOffHandStack();
		} else if (slot.getType() == ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE) {
			return ((DuckPlayerInventoryMixin) this.inventory).rpginventory$getAdditionalEquipmentStack(slot.getEntitySlotId());
		} else {
			return original.call(slot);
		}
	}

	@WrapMethod(method = "isArmorSlot")
	protected boolean rpginventory$isArmorSlot(EquipmentSlot slot, Operation<Boolean> original) {
		return original.call(slot) || slot.getType() == ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE;
	}

	@WrapMethod(method = "damageArmor")
	public void rpginventory$damageArmor(DamageSource source, float amount, Operation<Void> original) {
		this.damageEquipment(source, amount, new EquipmentSlot[]{EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD, ExtendedEquipmentSlot.GLOVES, ExtendedEquipmentSlot.SHOULDERS});
	}

	@WrapMethod(method = "getArmorItems")
	public Iterable<ItemStack> rpginventory$getArmorItems(Operation<Iterable<ItemStack>> original) {
		List<ItemStack> list = new ArrayList<>(List.of(((DuckPlayerInventoryMixin) this.inventory).rpginventory$getAdditionalEquipmentStack(1), ((DuckPlayerInventoryMixin) this.inventory).rpginventory$getAdditionalEquipmentStack(5)));
		for (ItemStack stack : original.call()) {
			list.add(stack);
		}
		return list;
	}

	@Override
	public void onEquipStack(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack) {
		super.onEquipStack(slot, oldStack, newStack);
		if (newStack.contains(RPGInventory.EXCLUSIVE_EQUIPMENT)) {
			this.rpginventory$setShouldEjectExclusiveEquipment(true);
		}
	}

	@Override
	public Iterable<ItemStack> getEquippedItems() {
		return Iterables.concat(this.getHandItems(), this.getAllArmorItems(), ((DuckPlayerInventoryMixin) this.inventory).rpginventory$getAdditionalNonArmorEquipmentItems());
	}

	@Override
	public float rpginventory$getActiveSpellSlotAmount() {
		return (float) Math.min(8, Math.max(0, this.getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT)));
	}

	@Override
	public ItemStack rpginventory$getSheathedHandItemStack() {
		ItemStack itemStack = ((DuckPlayerInventoryMixin) this.getInventory()).rpginventory$getSheathedHand();
		return rpginventory$isHandStackSheathed() && !itemStack.isIn(Tags.EMPTY_HAND_WEAPONS) && ItemUtils.isUsable(itemStack) && ItemUtils.isUsableByPlayer(itemStack, ((PlayerEntity) (Object) this)) ? itemStack : ItemStack.EMPTY;
	}

	@Override
	public ItemStack rpginventory$getSheathedOffHandItemStack() {
		ItemStack itemStack = ((DuckPlayerInventoryMixin) this.getInventory()).rpginventory$getSheathedOffhand();
		return rpginventory$isOffhandStackSheathed() && !itemStack.isIn(Tags.EMPTY_HAND_WEAPONS) && ItemUtils.isUsable(itemStack) && ItemUtils.isUsableByPlayer(itemStack, ((PlayerEntity) (Object) this)) ? itemStack : ItemStack.EMPTY;
	}

	@Override
	public boolean rpginventory$isHandStackSheathed() {
		return this.dataTracker.get(IS_HAND_STACK_SHEATHED);
	}

	@Override
	public void rpginventory$setIsHandStackSheathed(boolean isHandStackSheathed) {
		this.dataTracker.set(IS_HAND_STACK_SHEATHED, isHandStackSheathed);
	}

	@Override
	public boolean rpginventory$isOffhandStackSheathed() {
		return this.dataTracker.get(IS_OFFHAND_STACK_SHEATHED);
	}

	@Override
	public void rpginventory$setIsOffhandStackSheathed(boolean isOffhandStackSheathed) {
		this.dataTracker.set(IS_OFFHAND_STACK_SHEATHED, isOffhandStackSheathed);
	}

	@Override
	public boolean rpginventory$isHandSlotOverhaulActive() {
		return this.dataTracker.get(IS_HAND_SLOT_OVERHAUL_ACTIVE);
	}

	@Override
	public void rpginventory$setIsHandSlotOverhaulActive(boolean isHandSlotOverhaulActive) {
		this.dataTracker.set(IS_HAND_SLOT_OVERHAUL_ACTIVE, isHandSlotOverhaulActive);
	}

	@Override
	public boolean rpginventory$areAlternativeHandSlotsActive() {
		return this.dataTracker.get(ARE_ALTERNATIVE_HAND_SLOTS_ACTIVE);
	}

	@Override
	public void rpginventory$setAreAlternativeHandSlotsActive(boolean areAlternativeHandSlotsActive) {
		this.dataTracker.set(ARE_ALTERNATIVE_HAND_SLOTS_ACTIVE, areAlternativeHandSlotsActive);
	}

	@Override
	public int rpginventory$oldActiveSpellSlotAmount() {
		return this.dataTracker.get(OLD_ACTIVE_SPELL_SLOT_AMOUNT);
	}

	@Override
	public void rpginventory$setOldActiveSpellSlotAmount(int oldActiveSpellSlotAmount) {
		this.dataTracker.set(OLD_ACTIVE_SPELL_SLOT_AMOUNT, oldActiveSpellSlotAmount);
	}

	@Override
	public boolean rpginventory$shouldEjectExclusiveEquipment() {
		return this.dataTracker.get(SHOULD_EJECT_EXCLUSIVE_EQUIPMENT);
	}

	@Override
	public void rpginventory$setShouldEjectExclusiveEquipment(boolean shouldEjectExclusiveEquipment) {
		this.dataTracker.set(SHOULD_EJECT_EXCLUSIVE_EQUIPMENT, shouldEjectExclusiveEquipment);
	}

	@Override
	public boolean rpginventory$isAdventureHotbarCleanedUp() {
		return this.isAdventureHotbarCleanedUp;
	}

	@Override
	public void rpginventory$setIsAdventureHotbarCleanedUp(boolean isAdventureHotbarCleanedUp) {
		this.isAdventureHotbarCleanedUp = isAdventureHotbarCleanedUp;
	}

	@Unique
	private HashMultimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> getNaturalAttributeModifiers(World world) {
		HashMultimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> hashMultimap = HashMultimap.create();
		hashMultimap.put(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT, new EntityAttributeModifier(RPGInventory.identifier("natural_spell_slot_amount_modifier"), RPGInventory.SERVER_CONFIG.inventorySlots.default_spell_slot_amount.get(), EntityAttributeModifier.Operation.ADD_VALUE));
		return hashMultimap;
	}

}
