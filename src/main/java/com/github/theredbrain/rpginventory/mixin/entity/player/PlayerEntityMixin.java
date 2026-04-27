package com.github.theredbrain.rpginventory.mixin.entity.player;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.DataAttachmentHelper;
import com.github.theredbrain.rpginventory.entity.DuckLivingEntityMixin;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.PlayerEntityHelper;
import com.github.theredbrain.rpginventory.registry.ItemRegistry;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
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

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void rpginventory$tick(CallbackInfo ci) {
		Player playerEntity = (Player) (Object) this;
		if (!this.getItemBySlot(ExtendedEquipmentSlot.EMPTY_HAND).is(ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON)) {
			this.setItemSlot(ExtendedEquipmentSlot.EMPTY_HAND, ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON.getDefaultInstance());
		}
		if (!this.getItemBySlot(ExtendedEquipmentSlot.EMPTY_OFF_HAND).is(ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON)) {
			this.setItemSlot(ExtendedEquipmentSlot.EMPTY_OFF_HAND, ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON.getDefaultInstance());
		}
		PlayerEntityHelper.updateNaturalAttributeModifiers(playerEntity);
		PlayerEntityHelper.rpginventory$updateEquipmentStatusEffects(playerEntity);
		PlayerEntityHelper.rpginventory$ejectItemsFromInactiveSpellSlots(playerEntity);
		PlayerEntityHelper.rpginventory$ejectExclusiveEquipment(playerEntity);
		PlayerEntityHelper.rpginventory$ejectItemsFromInactiveHandSlots(playerEntity);
	}

	@Override
	public ItemStack getMainHandItem() {
		return this.getItemInHand(InteractionHand.MAIN_HAND);
	}

	@Override
	public ItemStack getOffhandItem() {
		return this.getItemInHand(InteractionHand.OFF_HAND);
	}

	@Override
	public ItemStack getItemInHand(final InteractionHand hand) {
		Player player = (Player) (Object) this;
		if (hand == InteractionHand.MAIN_HAND) {

			ItemStack handStack = this.getInventory().getSelectedItem();
			ItemStack emptyHandStack = this.rpginventory$isHandSlotOverhaulActive() ? this.getItemBySlot(ExtendedEquipmentSlot.EMPTY_HAND) : ItemStack.EMPTY;
			if (((DuckLivingEntityMixin) this).rpginventory$isHandStackSheathed()) {
				return ItemUtils.isUsable(handStack) && ItemUtils.isUsableByPlayer(handStack, player) ? handStack : emptyHandStack;
			}
			handStack = this.getItemBySlot(EquipmentSlot.MAINHAND);
			return ItemUtils.isUsable(handStack) && ItemUtils.isUsableByPlayer(handStack, player) && !handStack.isEmpty() ? handStack : emptyHandStack;

		} else if (hand == InteractionHand.OFF_HAND) {

			ItemStack offHandStack = this.getItemBySlot(EquipmentSlot.OFFHAND);
			ItemStack emptyOffHandStack = this.rpginventory$isHandSlotOverhaulActive() ? this.getItemBySlot(ExtendedEquipmentSlot.EMPTY_OFF_HAND) : ItemStack.EMPTY;
			if (!this.rpginventory$isHandSlotOverhaulActive()) {
				return ItemUtils.isUsable(offHandStack) && ItemUtils.isUsableByPlayer(offHandStack, player) && !offHandStack.isEmpty() ? offHandStack : emptyOffHandStack;
			}
			if (((DuckLivingEntityMixin) this).rpginventory$isOffhandStackSheathed()) {
				return emptyOffHandStack;
			}
			return ItemUtils.isUsable(offHandStack) && ItemUtils.isUsableByPlayer(offHandStack, player) && !offHandStack.isEmpty() ? offHandStack : emptyOffHandStack;
		} else {
			throw new IllegalArgumentException("Invalid hand " + hand);
		}
	}

	@Override
	public void setItemInHand(final InteractionHand hand, final ItemStack itemStack) {
		if (hand == InteractionHand.MAIN_HAND) {
			if (this.rpginventory$isHandSlotOverhaulActive() && !((DuckLivingEntityMixin) this).rpginventory$isHandStackSheathed()) {
				this.setItemSlot(EquipmentSlot.MAINHAND, itemStack);
				return;
			}
			this.getInventory().setSelectedItem(itemStack);
		} else {
			if (hand != InteractionHand.OFF_HAND) {
				throw new IllegalArgumentException("Invalid hand " + hand);
			}

			if (this.rpginventory$isHandSlotOverhaulActive() && ((DuckLivingEntityMixin) this).rpginventory$isOffhandStackSheathed()) {

				this.setItemSlot(ExtendedEquipmentSlot.SHEATHED_OFF_HAND, itemStack);
				return;
			}
			this.setItemSlot(EquipmentSlot.OFFHAND, itemStack);
		}
	}

	@WrapOperation(method = "getDestroySpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getDestroySpeed(Lnet/minecraft/world/level/block/state/BlockState;)F")
	)
	public float rpginventory$wrap_getBlockBreakingSpeed(ItemStack instance, BlockState state, Operation<Float> original) {
		if (this.rpginventory$isHandSlotOverhaulActive()) {
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

	@WrapMethod(method = "hurtArmor")
	public void rpginventory$hurtArmor(DamageSource damageSource, float damage, Operation<Void> original) {
		if (RPGInventory.SERVER_CONFIG.activate_rpg_inventory_screen.get()) {
			this.doHurtEquipment(damageSource, damage, new EquipmentSlot[]{EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD, ExtendedEquipmentSlot.GLOVES, ExtendedEquipmentSlot.SHOULDERS});
		} else {
			original.call(damageSource, damage);
		}
	}

	@Override
	public void onEquipItem(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack) {
		super.onEquipItem(slot, oldStack, newStack);
		if (newStack.has(RPGInventory.EXCLUSIVE_EQUIPMENT)) {
			this.rpginventory$setShouldEjectExclusiveEquipment(true);
		}
	}

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

}
