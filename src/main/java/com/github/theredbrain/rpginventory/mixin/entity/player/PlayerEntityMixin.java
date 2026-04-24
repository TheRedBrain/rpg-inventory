package com.github.theredbrain.rpginventory.mixin.entity.player;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.DataAttachmentHelper;
import com.github.theredbrain.rpginventory.entity.DuckLivingEntityMixin;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.PlayerEntityHelper;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import com.google.common.collect.HashMultimap;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
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
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity implements DuckPlayerEntityMixin {

	@Shadow
	public abstract Inventory getInventory();

	@Shadow
	public abstract boolean isCreative();

	@Unique
	private boolean isAdventureHotbarCleanedUp = false;
//
//	@Unique
//	private static final EntityDataAccessor<Boolean> IS_HAND_STACK_SHEATHED = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);
//
//	@Unique
//	private static final EntityDataAccessor<Boolean> IS_OFFHAND_STACK_SHEATHED = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);
//
//	@Unique
//	private static final EntityDataAccessor<Boolean> IS_HAND_SLOT_OVERHAUL_ACTIVE = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);
//
//	@Unique
//	private static final EntityDataAccessor<Boolean> ARE_ALTERNATIVE_HAND_SLOTS_ACTIVE = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);
//
//	@Unique
//	private static final EntityDataAccessor<Integer> OLD_ACTIVE_SPELL_SLOT_AMOUNT = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);
//
//	@Unique
//	private static final EntityDataAccessor<Boolean> SHOULD_EJECT_EXCLUSIVE_EQUIPMENT = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

//	@Inject(method = "initDataTracker", at = @At("RETURN"))
//	protected void rpginventory$initDataTracker(SynchedEntityData.Builder builder, CallbackInfo ci) {
//		builder.define(IS_HAND_STACK_SHEATHED, false);
//		builder.define(IS_OFFHAND_STACK_SHEATHED, false);
//		builder.define(IS_HAND_SLOT_OVERHAUL_ACTIVE, true);
//		builder.define(ARE_ALTERNATIVE_HAND_SLOTS_ACTIVE, true);
//		builder.define(OLD_ACTIVE_SPELL_SLOT_AMOUNT, -1);
//		builder.define(SHOULD_EJECT_EXCLUSIVE_EQUIPMENT, false);
//
//	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void rpginventory$tick(CallbackInfo ci) {
		Player playerEntity = (Player) (Object) this;
		this.getAttributes().addTransientAttributeModifiers(getNaturalAttributeModifiers(this.level()));
		PlayerEntityHelper.rpginventory$updateEquipmentStatusEffects(playerEntity);
		if (!this.level().isClientSide()) {
			PlayerEntityHelper.rpginventory$ejectItemsFromInactiveSpellSlots(playerEntity);
			PlayerEntityHelper.rpginventory$ejectExclusiveEquipment(playerEntity);
			PlayerEntityHelper.rpginventory$ejectItemsFromInactiveHandSlots(playerEntity);
//            PlayerInventoryHelper.rpginventory$ejectNonHotbarItemsFromHotbar(playerEntity); TODO disabled for now, needs overhaul
		}
	}

//	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
//	public void rpginventory$readCustomDataFromNbt(CompoundTag nbt, CallbackInfo ci) {
//
//		this.rpginventory$setIsHandStackSheathed(nbt.contains("is_hand_stack_sheathed", Tag.TAG_BYTE));
//
//		this.rpginventory$setIsOffhandStackSheathed(nbt.contains("is_offhand_stack_sheathed", Tag.TAG_BYTE));
//
//		this.rpginventory$setIsHandSlotOverhaulActive(nbt.contains("is_hand_slot_overhaul_active", Tag.TAG_BYTE));
//
//		if (nbt.contains("old_active_spell_slot_amount", Tag.TAG_INT)) {
//			this.rpginventory$setOldActiveSpellSlotAmount(nbt.getInt("old_active_spell_slot_amount"));
//		} else {
//			this.rpginventory$setOldActiveSpellSlotAmount(-1);
//		}
//	}

//	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
//	public void rpginventory$writeCustomDataToNbt(CompoundTag nbt, CallbackInfo ci) {
//
//		if (this.rpginventory$isHandStackSheathed()) {
//			nbt.putBoolean("is_hand_stack_sheathed", true);
//		} else {
//			nbt.remove("is_hand_stack_sheathed");
//		}
//
//		if (this.rpginventory$isOffhandStackSheathed()) {
//			nbt.putBoolean("is_offhand_stack_sheathed", true);
//		} else {
//			nbt.remove("is_offhand_stack_sheathed");
//		}
//
//		if (this.rpginventory$isHandSlotOverhaulActive()) {
//			nbt.putBoolean("is_hand_slot_overhaul_active", true);
//		} else {
//			nbt.remove("is_hand_slot_overhaul_active");
//		}
//
//		int old_active_spell_slot_amount = this.rpginventory$oldActiveSpellSlotAmount();
//		if (old_active_spell_slot_amount != -1) {
//			nbt.putInt("old_active_spell_slot_amount", old_active_spell_slot_amount);
//		} else {
//			nbt.remove("old_active_spell_slot_amount");
//		}
//	}

	public ItemStack getItemInHand(final InteractionHand hand) {
		if (hand == InteractionHand.MAIN_HAND) {

			ItemStack handStack;
			if (!RPGInventory.isHandSlotOverhaulActive()) {
				handStack = this.getInventory().getSelectedItem();
				return ItemUtils.isUsable(handStack) && ItemUtils.isUsableByPlayer(handStack, ((Player) (Object) this)) ? handStack : ItemStack.EMPTY;
			}
			ItemStack emptyHandStack = this.getItemBySlot(ExtendedEquipmentSlot.EMPTY_HAND);
			handStack = this.getItemBySlot(EquipmentSlot.MAINHAND);
			if (!((DuckLivingEntityMixin) this).rpginventory$isHandStackSheathed()) {
				return ItemUtils.isUsable(handStack) && ItemUtils.isUsableByPlayer(handStack, ((Player) (Object) this)) && !handStack.isEmpty() ? handStack : emptyHandStack;
			}
		} else if (hand == InteractionHand.OFF_HAND) {

			ItemStack offHandStack = this.getItemBySlot(EquipmentSlot.OFFHAND);
			if (!RPGInventory.isHandSlotOverhaulActive()) {
				return ItemUtils.isUsable(offHandStack) && ItemUtils.isUsableByPlayer(offHandStack, ((Player) (Object) this)) ? offHandStack : ItemStack.EMPTY;
			}
			ItemStack emptyOffHandStack = this.getItemBySlot(ExtendedEquipmentSlot.EMPTY_OFF_HAND);
			if (!((DuckLivingEntityMixin) this).rpginventory$isOffhandStackSheathed()) {
				return ItemUtils.isUsable(offHandStack) && ItemUtils.isUsableByPlayer(offHandStack, ((Player) (Object) this)) && !offHandStack.isEmpty() ? offHandStack : emptyOffHandStack;
			}
			return ItemStack.EMPTY;

		} else {
			throw new IllegalArgumentException("Invalid hand " + hand);
		}
		return ItemStack.EMPTY;
	}

	public void setItemInHand(final InteractionHand hand, final ItemStack itemStack) {
		if (hand == InteractionHand.MAIN_HAND) {
			this.setItemSlot(EquipmentSlot.MAINHAND, itemStack);
		} else {
			if (hand != InteractionHand.OFF_HAND) {
				throw new IllegalArgumentException("Invalid hand " + hand);
			}

			this.setItemSlot(EquipmentSlot.OFFHAND, itemStack);
		}
	}

//	@Override
//	public ItemStack rpginventory$getCurrentMainHandItem() {
//		if (RPGInventory.isHandSlotOverhaulActive()) {
//			ItemStack emptyHandStack = rpginventory$getEmptyHand();
//			ItemStack handStack = rpginventory$getHand();
//			if (!((DuckPlayerEntityMixin) player).rpginventory$isHandStackSheathed()) {
//				return ItemUtils.isUsable(handStack) && ItemUtils.isUsableByPlayer(handStack, this.player) && !handStack.isEmpty() ? handStack : emptyHandStack;
//			}
//		}
//		ItemStack itemStack = this.getItemInHand()
//		return ItemUtils.isUsable(original) && ItemUtils.isUsableByPlayer(original, this.player) ? original : ItemStack.EMPTY;
//	}
//
//	@Override
//	public ItemStack rpginventory$getOffHandStack() {
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

//	@WrapMethod(method = "equipStack")
//	public void equipStack(EquipmentSlot slot, ItemStack stack, Operation<Void> original) {
//		boolean isHandSlotOverhaulActive = RPGInventory.isHandSlotOverhaulActive();
//		this.verifyEquippedItem(stack);
//		if (slot == EquipmentSlot.MAINHAND) {
//			if (stack.is(Tags.EMPTY_HAND_WEAPONS)) {
//				this.onEquipItem(slot, ((DuckPlayerInventoryMixin) this.inventory).rpginventory$setEmptyHand(stack), stack);
//			} else {
//				this.onEquipItem(slot, ((DuckPlayerEntityMixin) this).rpginventory$isHandStackSheathed() || !isHandSlotOverhaulActive ? this.inventory.items.set(this.inventory.selected, stack) : ((DuckPlayerInventoryMixin) this.inventory).rpginventory$setHand(stack), stack);
//			}
//		} else if (slot == EquipmentSlot.OFFHAND) {
//			if (stack.is(Tags.EMPTY_HAND_WEAPONS)) {
//				this.onEquipItem(slot, ((DuckPlayerInventoryMixin) this.inventory).rpginventory$setEmptyOffhand(stack), stack);
//			} else {
//				this.onEquipItem(slot, ((DuckPlayerEntityMixin) this).rpginventory$isOffhandStackSheathed() && isHandSlotOverhaulActive ? ((DuckPlayerInventoryMixin) this.inventory).rpginventory$setSheathedOffhand(stack) : this.inventory.offhand.set(0, stack), stack);
//			}
//		} else if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
//			this.onEquipItem(slot, this.inventory.armor.set(slot.getIndex(), stack), stack);
//		} else if (slot.getType() == ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE) {
//			this.onEquipItem(slot, ((DuckPlayerInventoryMixin) this.inventory).rpginventory$setAdditionalEquipmentStack(slot.getIndex(), stack), stack);
//		} else {
//			original.call(slot, stack);
//		}
//	}

	@WrapOperation(method = "getDestroySpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getDestroySpeed(Lnet/minecraft/world/level/block/state/BlockState;)F")
	)
	public float rpginventory$wrap_getBlockBreakingSpeed(ItemStack instance, BlockState state, Operation<Float> original) {
		if (RPGInventory.isHandSlotOverhaulActive()) {
			return this.getItemInHand(InteractionHand.MAIN_HAND).getDestroySpeed(state);
		} else {
			return original.call(instance, state);
		}
	}

	@WrapMethod(method = "dropEquipment")
	protected void dropEquipment(ServerLevel level, Operation<Void> original) {
		if (this.hasEffect(RPGInventory.KEEP_INVENTORY)) {
			PlayerEntityHelper.rpginventory$breakKeepInventoryItems((Player) (Object) this);
		} else {
			original.call(level);
		}
	}

//	@WrapMethod(method = "getEquippedStack")
//	public ItemStack rpginventory$getEquippedStack(EquipmentSlot slot, Operation<ItemStack> original) {
//		if (slot == EquipmentSlot.OFFHAND) {
//			return ((DuckPlayerInventoryMixin) this.inventory).rpginventory$getOffHandStack();
//		} else if (slot.getType() == ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE) {
//			return ((DuckPlayerInventoryMixin) this.inventory).rpginventory$getAdditionalEquipmentStack(slot.getIndex());
//		} else {
//			return original.call(slot);
//		}
//	}

//	@WrapMethod(method = "isArmorSlot")
//	protected boolean rpginventory$isArmorSlot(EquipmentSlot slot, Operation<Boolean> original) {
//		return original.call(slot) || slot.getType() == ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE;
//	}

	@WrapMethod(method = "hurtArmor")
	public void rpginventory$hurtArmor(DamageSource damageSource, float damage, Operation<Void> original) {
		if (RPGInventory.SERVER_CONFIG.activate_rpg_inventory_screen.get()) {
			this.doHurtEquipment(damageSource, damage, new EquipmentSlot[]{EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD, ExtendedEquipmentSlot.GLOVES, ExtendedEquipmentSlot.SHOULDERS});
		} else {
			original.call(damageSource, damage);
		}
	}

//	@WrapMethod(method = "getArmorItems")
//	public Iterable<ItemStack> rpginventory$getArmorItems(Operation<Iterable<ItemStack>> original) {
//		List<ItemStack> list = new ArrayList<>(List.of(((DuckPlayerInventoryMixin) this.inventory).rpginventory$getAdditionalEquipmentStack(1), ((DuckPlayerInventoryMixin) this.inventory).rpginventory$getAdditionalEquipmentStack(5)));
//		for (ItemStack stack : original.call()) {
//			list.add(stack);
//		}
//		return list;
//	}

	@Override
	public void onEquipItem(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack) {
		super.onEquipItem(slot, oldStack, newStack);
		if (newStack.has(RPGInventory.EXCLUSIVE_EQUIPMENT)) {
			this.rpginventory$setShouldEjectExclusiveEquipment(true);
		}
	}

//	@Override
//	public Iterable<ItemStack> getAllSlots() {
//		return Iterables.concat(this.getHandSlots(), this.getArmorAndBodyArmorSlots(), ((DuckPlayerInventoryMixin) this.inventory).rpginventory$getAdditionalNonArmorEquipmentItems());
//	}

	@Override
	public float rpginventory$getActiveSpellSlotAmount() {
		return (float) Math.min(8, Math.max(0, this.getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT)));
	}

	@Override
	public boolean rpginventory$isHandSlotOverhaulActive() {
		return DataAttachmentHelper.isHandSlotOverhaulActive((Player) (Object) this);
	}

	@Override
	public void rpginventory$setIsHandSlotOverhaulActive(boolean isHandSlotOverhaulActive) {
		DataAttachmentHelper.setIsHandSlotOverhaulActive((Player) (Object) this, isHandSlotOverhaulActive);
	}

	@Override
	public boolean rpginventory$areAlternativeHandSlotsActive() {
		return DataAttachmentHelper.areAlternativeHandSlotsActive((Player) (Object) this);
	}

	@Override
	public void rpginventory$setAreAlternativeHandSlotsActive(boolean areAlternativeHandSlotsActive) {
		DataAttachmentHelper.setAreAlternativeHandSlotsActive((Player) (Object) this, areAlternativeHandSlotsActive);
	}

	@Override
	public int rpginventory$oldActiveSpellSlotAmount() {
		return DataAttachmentHelper.getOldActiveSpellSlotAmount((Player) (Object) this);
	}

	@Override
	public void rpginventory$setOldActiveSpellSlotAmount(int oldActiveSpellSlotAmount) {
		DataAttachmentHelper.setOldActiveSpellSlotAmount((Player) (Object) this, oldActiveSpellSlotAmount);
	}

	@Override
	public boolean rpginventory$shouldEjectExclusiveEquipment() {
		return DataAttachmentHelper.shouldEjectExclusiveEquipment((Player) (Object) this);
	}

	@Override
	public void rpginventory$setShouldEjectExclusiveEquipment(boolean shouldEjectExclusiveEquipment) {
		DataAttachmentHelper.setShouldEjectExclusiveEquipment((Player) (Object) this, shouldEjectExclusiveEquipment);
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
