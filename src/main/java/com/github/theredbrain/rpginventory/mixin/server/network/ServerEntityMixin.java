package com.github.theredbrain.rpginventory.mixin.server.network;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.rpginventory.network.packet.SheathedWeaponsPacket;
import com.github.theredbrain.rpginventory.network.packet.SwappedHandItemsPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerEntity.class)
public class ServerEntityMixin {

	@Shadow
	@Final
	private Entity entity;

	@Inject(method = "addPairing", at = @At(value = "TAIL"))
	public void rpginventory$addPairing(ServerPlayer serverPlayer, CallbackInfo info) {
		if (this.entity instanceof Player && RPGInventory.isHandSlotOverhaulActive()) {
			Player player = (Player) entity;
			if (!((DuckPlayerInventoryMixin) serverPlayer.getInventory()).rpginventory$getHand().isEmpty() || !((DuckPlayerInventoryMixin) serverPlayer.getInventory()).rpginventory$getAlternativeHand().isEmpty()) {
				ServerPlayNetworking.send((ServerPlayer) player, new SwappedHandItemsPacket(serverPlayer.getId(), true));
			}
			if (!serverPlayer.getItemBySlot(EquipmentSlot.OFFHAND).isEmpty() || !((DuckPlayerInventoryMixin) serverPlayer.getInventory()).rpginventory$getAlternativeOffhand().isEmpty()) {
				ServerPlayNetworking.send((ServerPlayer) player, new SwappedHandItemsPacket(serverPlayer.getId(), false));
			}
			if (!((DuckPlayerInventoryMixin) serverPlayer.getInventory()).rpginventory$getHand().isEmpty() || !((DuckPlayerInventoryMixin) serverPlayer.getInventory()).rpginventory$getSheathedHand().isEmpty()) {
				ServerPlayNetworking.send((ServerPlayer) player, new SheathedWeaponsPacket(serverPlayer.getId(), true, ((DuckPlayerEntityMixin) player).rpginventory$isHandStackSheathed()));
			}
			if (!serverPlayer.getItemBySlot(EquipmentSlot.OFFHAND).isEmpty() || !((DuckPlayerInventoryMixin) serverPlayer.getInventory()).rpginventory$getSheathedOffhand().isEmpty()) {
				ServerPlayNetworking.send((ServerPlayer) player, new SheathedWeaponsPacket(serverPlayer.getId(), false, ((DuckPlayerEntityMixin) player).rpginventory$isOffhandStackSheathed()));
			}
		}
	}
}
