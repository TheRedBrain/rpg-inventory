package com.github.theredbrain.rpginventory.mixin.server.network;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.rpginventory.network.packet.SheathedWeaponsPacket;
import com.github.theredbrain.rpginventory.network.packet.SwappedHandItemsPacket;
import com.github.theredbrain.rpginventory.registry.ItemRegistry;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(value = ServerPlayer.class/*, priority = 950*/) // TODO test if priority is needed
public abstract class ServerPlayerEntityMixin extends Player implements DuckPlayerEntityMixin {

	@Shadow
	public abstract boolean isCreative();

	@Shadow
	public abstract void onEnterCombat();

	@Unique
	ItemStack handSlotStack = ItemStack.EMPTY;

	@Unique
	ItemStack alternateHandSlotStack = ItemStack.EMPTY;

	@Unique
	ItemStack offHandSlotStack = ItemStack.EMPTY;

	@Unique
	ItemStack alternateOffHandSlotStack = ItemStack.EMPTY;

	@Unique
	boolean isHandWeaponSheathed = false;

	@Unique
	boolean isOffHandWeaponSheathed = false;

	public ServerPlayerEntityMixin(Level world, BlockPos pos, float yaw, GameProfile gameProfile) {
		super(world, pos, yaw, gameProfile);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void rpginventory$tick(CallbackInfo ci) {
		if (!this.level().isClientSide()) {
			if (!((DuckPlayerInventoryMixin) this.getInventory()).rpginventory$getEmptyHand().is(ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON)) {
				((DuckPlayerInventoryMixin) this.getInventory()).rpginventory$setEmptyHand(ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON.getDefaultInstance());
			}
			if (!((DuckPlayerInventoryMixin) this.getInventory()).rpginventory$getEmptyOffhand().is(ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON)) {
				((DuckPlayerInventoryMixin) this.getInventory()).rpginventory$setEmptyOffhand(ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON.getDefaultInstance());
			}
			ItemStack newHandStack = ((DuckPlayerInventoryMixin) this.getInventory()).rpginventory$getHand();
			ItemStack newAlternativeHandStack = ((DuckPlayerInventoryMixin) this.getInventory()).rpginventory$getAlternativeHand();
			if (!ItemStack.isSameItem(handSlotStack, newHandStack) || !ItemStack.isSameItem(alternateHandSlotStack, newAlternativeHandStack)) {
				rpginventory$sendChangedHandSlotsPacket(true);
			}
			handSlotStack = newHandStack;
			alternateHandSlotStack = newAlternativeHandStack;
			ItemStack newOffHandStack = this.getItemBySlot(EquipmentSlot.OFFHAND);
			ItemStack newAlternativeOffHandStack = ((DuckPlayerInventoryMixin) this.getInventory()).rpginventory$getAlternativeOffhand();
			if (!ItemStack.isSameItem(offHandSlotStack, newOffHandStack) || !ItemStack.isSameItem(alternateOffHandSlotStack, newAlternativeOffHandStack)) {
				rpginventory$sendChangedHandSlotsPacket(false);
			}
			offHandSlotStack = newOffHandStack;
			alternateOffHandSlotStack = newAlternativeOffHandStack;
			boolean isHandWeaponSheathed = this.rpginventory$isHandStackSheathed();
			if (this.isHandWeaponSheathed != isHandWeaponSheathed) {
				rpginventory$sendSheathedWeaponsPacket(true, isHandWeaponSheathed);
				this.isHandWeaponSheathed = isHandWeaponSheathed;
			}
			boolean isOffHandWeaponSheathed = this.rpginventory$isOffhandStackSheathed();
			if (this.isOffHandWeaponSheathed != isOffHandWeaponSheathed) {
				rpginventory$sendSheathedWeaponsPacket(false, isOffHandWeaponSheathed);
				this.isOffHandWeaponSheathed = isOffHandWeaponSheathed;
			}
		}
	}

	@WrapMethod(
			method = "dropSelectedItem"
	)
	public boolean dropSelectedItem(boolean entireStack, Operation<Boolean> original) {
		if (RPGInventory.isHandSlotOverhaulActive()) {
			if (!this.rpginventory$isHandStackSheathed()) {
				Inventory playerInventory = this.getInventory();
				ItemStack itemStack = playerInventory.removeFromSelected(entireStack);
				this.containerMenu.setRemoteSlot(46, ((DuckPlayerInventoryMixin) playerInventory).rpginventory$getHand());
				return this.drop(itemStack, false, true) != null;
			}
		}
		return original.call(entireStack);
	}

	@Unique
	private void rpginventory$sendChangedHandSlotsPacket(boolean mainHand) {
		Collection<ServerPlayer> players = PlayerLookup.tracking((ServerLevel) this.level(), this.blockPosition());
		players.forEach(player -> ServerPlayNetworking.send(player, new SwappedHandItemsPacket(this.getId(), mainHand)));
	}

	@Unique
	private void rpginventory$sendSheathedWeaponsPacket(boolean mainHand, boolean isSheathed) {
		Collection<ServerPlayer> players = PlayerLookup.tracking((ServerLevel) this.level(), this.blockPosition());
		players.forEach(player -> ServerPlayNetworking.send(player, new SheathedWeaponsPacket(this.getId(), mainHand, isSheathed)));
	}
}
