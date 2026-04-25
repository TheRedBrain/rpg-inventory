package com.github.theredbrain.rpginventory.mixin.server.network;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.DuckLivingEntityMixin;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.network.packet.SheathedWeaponsPacket;
import com.github.theredbrain.rpginventory.network.packet.SwappedHandItemsPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
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

	// TODO test if the hand sheathed check needs to be for thisServerPlayer
	// TODO test if this is necessary at all
	@Inject(method = "addPairing", at = @At(value = "TAIL"))
	public void rpginventory$addPairing(ServerPlayer player, CallbackInfo info) {
		if (this.entity instanceof ServerPlayer thisServerPlayer && RPGInventory.isHandSlotOverhaulActive()) {
			if (!player.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() || !player.getItemBySlot(ExtendedEquipmentSlot.ALTERNATIVE_HAND).isEmpty()) {
				ServerPlayNetworking.send(thisServerPlayer, new SwappedHandItemsPacket(player.getId(), true));
			}
			if (!player.getItemBySlot(EquipmentSlot.OFFHAND).isEmpty() || !player.getItemBySlot(ExtendedEquipmentSlot.ALTERNATIVE_OFF_HAND).isEmpty()) {
				ServerPlayNetworking.send(thisServerPlayer, new SwappedHandItemsPacket(player.getId(), false));
			}
			if (!player.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() || !player.getItemBySlot(ExtendedEquipmentSlot.SHEATHED_HAND).isEmpty()) {
				ServerPlayNetworking.send(thisServerPlayer, new SheathedWeaponsPacket(player.getId(), true, ((DuckLivingEntityMixin) player).rpginventory$isHandStackSheathed()));
			}
			if (!player.getItemBySlot(EquipmentSlot.OFFHAND).isEmpty() || !player.getItemBySlot(ExtendedEquipmentSlot.SHEATHED_OFF_HAND).isEmpty()) {
				ServerPlayNetworking.send(thisServerPlayer, new SheathedWeaponsPacket(player.getId(), false, ((DuckLivingEntityMixin) player).rpginventory$isOffhandStackSheathed()));
			}
		}
	}
}
