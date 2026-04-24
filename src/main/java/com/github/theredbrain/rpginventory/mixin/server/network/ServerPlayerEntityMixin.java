package com.github.theredbrain.rpginventory.mixin.server.network;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.DuckLivingEntityMixin;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.network.packet.SheathedWeaponsPacket;
import com.github.theredbrain.rpginventory.network.packet.SwappedHandItemsPacket;
import com.github.theredbrain.rpginventory.registry.ItemRegistry;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(value = ServerPlayer.class/*, priority = 950*/) // TODO test if priority is needed
public abstract class ServerPlayerEntityMixin extends Player implements DuckLivingEntityMixin {

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

	public ServerPlayerEntityMixin(final MinecraftServer server, final ServerLevel level, final GameProfile gameProfile, final ClientInformation clientInformation) {
		super(level, gameProfile);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void rpginventory$tick(CallbackInfo ci) {
		if (!this.level().isClientSide()) {
			if (!this.getItemBySlot(ExtendedEquipmentSlot.EMPTY_HAND).is(ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON)) {
				this.setItemSlot(ExtendedEquipmentSlot.EMPTY_HAND, ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON.getDefaultInstance());
			}
			if (!this.getItemBySlot(ExtendedEquipmentSlot.EMPTY_OFF_HAND).is(ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON)) {
				this.setItemSlot(ExtendedEquipmentSlot.EMPTY_OFF_HAND, ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON.getDefaultInstance());
			}
			ItemStack newHandStack = this.getItemBySlot(EquipmentSlot.MAINHAND);
			ItemStack newAlternativeHandStack = this.getItemBySlot(ExtendedEquipmentSlot.ALTERNATIVE_HAND);
			if (!ItemStack.isSameItem(handSlotStack, newHandStack) || !ItemStack.isSameItem(alternateHandSlotStack, newAlternativeHandStack)) {
				rpginventory$sendChangedHandSlotsPacket(true);
			}
			handSlotStack = newHandStack;
			alternateHandSlotStack = newAlternativeHandStack;
			ItemStack newOffHandStack = this.getItemBySlot(EquipmentSlot.OFFHAND);
			ItemStack newAlternativeOffHandStack = this.getItemBySlot(ExtendedEquipmentSlot.ALTERNATIVE_OFF_HAND);
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

	@WrapMethod(method = "drop(Z)V")
	public void rpginventory$wrap_drop(boolean all, Operation<Boolean> original) {
		if (RPGInventory.isHandSlotOverhaulActive() && !this.rpginventory$isHandStackSheathed()) {
			ItemStack itemStack = this.getItemBySlot(EquipmentSlot.MAINHAND).copy();
			this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
			this.containerMenu.setRemoteSlot(46, itemStack);
			this.drop(itemStack, false, true);
		}
		original.call(all);
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
