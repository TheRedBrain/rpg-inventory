package com.github.theredbrain.rpginventory.mixin.server.network;

import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.rpginventory.network.packet.SheathedWeaponsPacket;
import com.github.theredbrain.rpginventory.network.packet.SwappedHandItemsPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityTrackerEntry.class)
public class EntityTrackerEntryMixin {

	@Shadow
	@Final
	private Entity entity;

	@Inject(method = "startTracking", at = @At(value = "TAIL"))
	public void rpginventory$startTracking(ServerPlayerEntity serverPlayer, CallbackInfo info) {
		if (this.entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entity;
			if (!((DuckPlayerInventoryMixin) serverPlayer.getInventory()).rpginventory$getHand().isEmpty() || !((DuckPlayerInventoryMixin) serverPlayer.getInventory()).rpginventory$getAlternativeHand().isEmpty()) {
				ServerPlayNetworking.send((ServerPlayerEntity) player, new SwappedHandItemsPacket(serverPlayer.getId(), true));
			}
			if (!serverPlayer.getEquippedStack(EquipmentSlot.OFFHAND).isEmpty() || !((DuckPlayerInventoryMixin) serverPlayer.getInventory()).rpginventory$getAlternativeOffhand().isEmpty()) {
				ServerPlayNetworking.send((ServerPlayerEntity) player, new SwappedHandItemsPacket(serverPlayer.getId(), false));
			}
			if (!((DuckPlayerInventoryMixin) serverPlayer.getInventory()).rpginventory$getHand().isEmpty() || !((DuckPlayerInventoryMixin) serverPlayer.getInventory()).rpginventory$getSheathedHand().isEmpty()) {
				ServerPlayNetworking.send((ServerPlayerEntity) player, new SheathedWeaponsPacket(serverPlayer.getId(), true, ((DuckPlayerEntityMixin) player).rpginventory$isHandStackSheathed()));
			}
			if (!serverPlayer.getEquippedStack(EquipmentSlot.OFFHAND).isEmpty() || !((DuckPlayerInventoryMixin) serverPlayer.getInventory()).rpginventory$getSheathedOffhand().isEmpty()) {
				ServerPlayNetworking.send((ServerPlayerEntity) player, new SheathedWeaponsPacket(serverPlayer.getId(), false, ((DuckPlayerEntityMixin) player).rpginventory$isOffhandStackSheathed()));
			}
		}
	}
}
