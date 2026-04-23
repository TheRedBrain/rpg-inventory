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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity implements DuckPlayerEntityMixin, RendersSheathedWeapons {

	@Shadow
	@Final
	Inventory inventory;

	@Shadow
	public abstract Inventory getInventory();

	@Shadow
	public abstract boolean isCreative();

	@Unique
	private boolean isAdventureHotbarCleanedUp = false;

	@Unique
	private static final EntityDataAccessor<Boolean> IS_HAND_STACK_SHEATHED = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);

	@Unique
	private static final EntityDataAccessor<Boolean> IS_OFFHAND_STACK_SHEATHED = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);

	@Unique
	private static final EntityDataAccessor<Boolean> IS_HAND_SLOT_OVERHAUL_ACTIVE = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);

	@Unique
	private static final EntityDataAccessor<Boolean> ARE_ALTERNATIVE_HAND_SLOTS_ACTIVE = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);

	@Unique
	private static final EntityDataAccessor<Integer> OLD_ACTIVE_SPELL_SLOT_AMOUNT = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);

	@Unique
	private static final EntityDataAccessor<Boolean> SHOULD_EJECT_EXCLUSIVE_EQUIPMENT = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = "initDataTracker", at = @At("RETURN"))
	protected void rpginventory$initDataTracker(SynchedEntityData.Builder builder, CallbackInfo ci) {
		builder.define(IS_HAND_STACK_SHEATHED, false);
		builder.define(IS_OFFHAND_STACK_SHEATHED, false);
		builder.define(IS_HAND_SLOT_OVERHAUL_ACTIVE, true);
		builder.define(ARE_ALTERNATIVE_HAND_SLOTS_ACTIVE, true);
		builder.define(OLD_ACTIVE_SPELL_SLOT_AMOUNT, -1);
		builder.define(SHOULD_EJECT_EXCLUSIVE_EQUIPMENT, false);

	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void rpginventory$tick(CallbackInfo ci) {
		Player playerEntity = (Player) (Object) this;
		this.getAttributes().addTransientAttributeModifiers(getNaturalAttributeModifiers(this.level()));
		PlayerEntityHelper.rpginventory$updateEquipmentStatusEffects(playerEntity);
		if (!this.level().isClientSide) {
			PlayerEntityHelper.rpginventory$ejectItemsFromInactiveSpellSlots(playerEntity);
			PlayerEntityHelper.rpginventory$ejectExclusiveEquipment(playerEntity);
			PlayerEntityHelper.rpginventory$ejectItemsFromInactiveHandSlots(playerEntity);
//            PlayerInventoryHelper.rpginventory$ejectNonHotbarItemsFromHotbar(playerEntity); TODO disabled for now, needs overhaul
		}
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void rpginventory$readCustomDataFromNbt(CompoundTag nbt, CallbackInfo ci) {

		this.rpginventory$setIsHandStackSheathed(nbt.contains("is_hand_stack_sheathed", Tag.TAG_BYTE));

		this.rpginventory$setIsOffhandStackSheathed(nbt.contains("is_offhand_stack_sheathed", Tag.TAG_BYTE));

		this.rpginventory$setIsHandSlotOverhaulActive(nbt.contains("is_hand_slot_overhaul_active", Tag.TAG_BYTE));

		if (nbt.contains("old_active_spell_slot_amount", Tag.TAG_INT)) {
			this.rpginventory$setOldActiveSpellSlotAmount(nbt.getInt("old_active_spell_slot_amount"));
		} else {
			this.rpginventory$setOldActiveSpellSlotAmount(-1);
		}
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	public void rpginventory$writeCustomDataToNbt(CompoundTag nbt, CallbackInfo ci) {

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
		this.verifyEquippedItem(stack);
		if (slot == EquipmentSlot.MAINHAND) {
			if (stack.is(Tags.EMPTY_HAND_WEAPONS)) {
				this.onEquipItem(slot, ((DuckPlayerInventoryMixin) this.inventory).rpginventory$setEmptyHand(stack), stack);
			} else {
				this.onEquipItem(slot, ((DuckPlayerEntityMixin) this).rpginventory$isHandStackSheathed() || !isHandSlotOverhaulActive ? this.inventory.items.set(this.inventory.selected, stack) : ((DuckPlayerInventoryMixin) this.inventory).rpginventory$setHand(stack), stack);
			}
		} else if (slot == EquipmentSlot.OFFHAND) {
			if (stack.is(Tags.EMPTY_HAND_WEAPONS)) {
				this.onEquipItem(slot, ((DuckPlayerInventoryMixin) this.inventory).rpginventory$setEmptyOffhand(stack), stack);
			} else {
				this.onEquipItem(slot, ((DuckPlayerEntityMixin) this).rpginventory$isOffhandStackSheathed() && isHandSlotOverhaulActive ? ((DuckPlayerInventoryMixin) this.inventory).rpginventory$setSheathedOffhand(stack) : this.inventory.offhand.set(0, stack), stack);
			}
		} else if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
			this.onEquipItem(slot, this.inventory.armor.set(slot.getIndex(), stack), stack);
		} else if (slot.getType() == ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE) {
			this.onEquipItem(slot, ((DuckPlayerInventoryMixin) this.inventory).rpginventory$setAdditionalEquipmentStack(slot.getIndex(), stack), stack);
		} else {
			original.call(slot, stack);
		}
	}

	@Inject(method = "dropInventory", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;vanishCursedItems()V", ordinal = 0), cancellable = true)
	private void rpginventory$pre_vanishCursedItems(CallbackInfo ci) {
		if (this.hasEffect(RPGInventory.KEEP_INVENTORY)) {
			PlayerEntityHelper.rpginventory$breakKeepInventoryItems((Player) (Object) this);
			ci.cancel();
		}
	}

	@WrapMethod(method = "getEquippedStack")
	public ItemStack rpginventory$getEquippedStack(EquipmentSlot slot, Operation<ItemStack> original) {
		if (slot == EquipmentSlot.OFFHAND) {
			return ((DuckPlayerInventoryMixin) this.inventory).rpginventory$getOffHandStack();
		} else if (slot.getType() == ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE) {
			return ((DuckPlayerInventoryMixin) this.inventory).rpginventory$getAdditionalEquipmentStack(slot.getIndex());
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
		this.doHurtEquipment(source, amount, new EquipmentSlot[]{EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD, ExtendedEquipmentSlot.GLOVES, ExtendedEquipmentSlot.SHOULDERS});
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
	public void onEquipItem(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack) {
		super.onEquipItem(slot, oldStack, newStack);
		if (newStack.has(RPGInventory.EXCLUSIVE_EQUIPMENT)) {
			this.rpginventory$setShouldEjectExclusiveEquipment(true);
		}
	}

	@Override
	public Iterable<ItemStack> getAllSlots() {
		return Iterables.concat(this.getHandSlots(), this.getArmorAndBodyArmorSlots(), ((DuckPlayerInventoryMixin) this.inventory).rpginventory$getAdditionalNonArmorEquipmentItems());
	}

	@Override
	public float rpginventory$getActiveSpellSlotAmount() {
		return (float) Math.min(8, Math.max(0, this.getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT)));
	}

	@Override
	public ItemStack rpginventory$getSheathedHandItemStack() {
		ItemStack itemStack = ((DuckPlayerInventoryMixin) this.getInventory()).rpginventory$getSheathedHand();
		return rpginventory$isHandStackSheathed() && !itemStack.is(Tags.EMPTY_HAND_WEAPONS) && ItemUtils.isUsable(itemStack) && ItemUtils.isUsableByPlayer(itemStack, ((Player) (Object) this)) ? itemStack : ItemStack.EMPTY;
	}

	@Override
	public ItemStack rpginventory$getSheathedOffHandItemStack() {
		ItemStack itemStack = ((DuckPlayerInventoryMixin) this.getInventory()).rpginventory$getSheathedOffhand();
		return rpginventory$isOffhandStackSheathed() && !itemStack.is(Tags.EMPTY_HAND_WEAPONS) && ItemUtils.isUsable(itemStack) && ItemUtils.isUsableByPlayer(itemStack, ((Player) (Object) this)) ? itemStack : ItemStack.EMPTY;
	}

	@Override
	public boolean rpginventory$isHandStackSheathed() {
		return this.entityData.get(IS_HAND_STACK_SHEATHED);
	}

	@Override
	public void rpginventory$setIsHandStackSheathed(boolean isHandStackSheathed) {
		this.entityData.set(IS_HAND_STACK_SHEATHED, isHandStackSheathed);
	}

	@Override
	public boolean rpginventory$isOffhandStackSheathed() {
		return this.entityData.get(IS_OFFHAND_STACK_SHEATHED);
	}

	@Override
	public void rpginventory$setIsOffhandStackSheathed(boolean isOffhandStackSheathed) {
		this.entityData.set(IS_OFFHAND_STACK_SHEATHED, isOffhandStackSheathed);
	}

	@Override
	public boolean rpginventory$isHandSlotOverhaulActive() {
		return this.entityData.get(IS_HAND_SLOT_OVERHAUL_ACTIVE);
	}

	@Override
	public void rpginventory$setIsHandSlotOverhaulActive(boolean isHandSlotOverhaulActive) {
		this.entityData.set(IS_HAND_SLOT_OVERHAUL_ACTIVE, isHandSlotOverhaulActive);
	}

	@Override
	public boolean rpginventory$areAlternativeHandSlotsActive() {
		return this.entityData.get(ARE_ALTERNATIVE_HAND_SLOTS_ACTIVE);
	}

	@Override
	public void rpginventory$setAreAlternativeHandSlotsActive(boolean areAlternativeHandSlotsActive) {
		this.entityData.set(ARE_ALTERNATIVE_HAND_SLOTS_ACTIVE, areAlternativeHandSlotsActive);
	}

	@Override
	public int rpginventory$oldActiveSpellSlotAmount() {
		return this.entityData.get(OLD_ACTIVE_SPELL_SLOT_AMOUNT);
	}

	@Override
	public void rpginventory$setOldActiveSpellSlotAmount(int oldActiveSpellSlotAmount) {
		this.entityData.set(OLD_ACTIVE_SPELL_SLOT_AMOUNT, oldActiveSpellSlotAmount);
	}

	@Override
	public boolean rpginventory$shouldEjectExclusiveEquipment() {
		return this.entityData.get(SHOULD_EJECT_EXCLUSIVE_EQUIPMENT);
	}

	@Override
	public void rpginventory$setShouldEjectExclusiveEquipment(boolean shouldEjectExclusiveEquipment) {
		this.entityData.set(SHOULD_EJECT_EXCLUSIVE_EQUIPMENT, shouldEjectExclusiveEquipment);
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
	private HashMultimap<Holder<Attribute>, AttributeModifier> getNaturalAttributeModifiers(Level world) {
		HashMultimap<Holder<Attribute>, AttributeModifier> hashMultimap = HashMultimap.create();
		hashMultimap.put(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT, new AttributeModifier(RPGInventory.identifier("natural_spell_slot_amount_modifier"), RPGInventory.SERVER_CONFIG.inventorySlots.default_spell_slot_amount.get(), AttributeModifier.Operation.ADD_VALUE));
		return hashMultimap;
	}

}
