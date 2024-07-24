package com.github.theredbrain.rpginventory.mixin.server.network;

import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.rpginventory.network.packet.SheathedWeaponsPacket;
import com.github.theredbrain.rpginventory.network.packet.SwappedHandItemsPacket;
import com.github.theredbrain.rpginventory.registry.ItemRegistry;
import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(value = ServerPlayerEntity.class/*, priority = 950*/) // TODO test if priority is needed
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements DuckPlayerEntityMixin {

	@Shadow public abstract boolean isCreative();

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

	public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
		super(world, pos, yaw, gameProfile);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void rpginventory$tick(CallbackInfo ci) {
		if (!this.getWorld().isClient) {
			if (!((DuckPlayerInventoryMixin) this.getInventory()).rpginventory$getEmptyHand().isOf(ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON)) {
				((DuckPlayerInventoryMixin) this.getInventory()).rpginventory$setEmptyHand(ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON.getDefaultStack());
			}
			if (!((DuckPlayerInventoryMixin) this.getInventory()).rpginventory$getEmptyOffhand().isOf(ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON)) {
				((DuckPlayerInventoryMixin) this.getInventory()).rpginventory$setEmptyOffhand(ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON.getDefaultStack());
			}
			ItemStack newHandStack = ((DuckPlayerInventoryMixin) this.getInventory()).rpginventory$getHand();
			ItemStack newAlternativeHandStack = ((DuckPlayerInventoryMixin) this.getInventory()).rpginventory$getAlternativeHand();
			if (!ItemStack.areItemsEqual(handSlotStack, newHandStack) || !ItemStack.areItemsEqual(alternateHandSlotStack, newAlternativeHandStack)) {
				rpginventory$sendChangedHandSlotsPacket(true);
			}
			handSlotStack = newHandStack;
			alternateHandSlotStack = newAlternativeHandStack;
			ItemStack newOffHandStack = this.getEquippedStack(EquipmentSlot.OFFHAND);
			ItemStack newAlternativeOffHandStack = ((DuckPlayerInventoryMixin) this.getInventory()).rpginventory$getAlternativeOffhand();
			if (!ItemStack.areItemsEqual(offHandSlotStack, newOffHandStack) || !ItemStack.areItemsEqual(alternateOffHandSlotStack, newAlternativeOffHandStack)) {
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

	@Unique
	private void rpginventory$sendChangedHandSlotsPacket(boolean mainHand) {
		Collection<ServerPlayerEntity> players = PlayerLookup.tracking((ServerWorld) this.getWorld(), this.getBlockPos());
		players.forEach(player -> ServerPlayNetworking.send(player, new SwappedHandItemsPacket(this.getId(), mainHand)));
	}

	@Unique
	private void rpginventory$sendSheathedWeaponsPacket(boolean mainHand, boolean isSheathed) {
		Collection<ServerPlayerEntity> players = PlayerLookup.tracking((ServerWorld) this.getWorld(), this.getBlockPos());
		players.forEach(player -> ServerPlayNetworking.send(player, new SheathedWeaponsPacket(this.getId(), mainHand, isSheathed)));
	}
}
