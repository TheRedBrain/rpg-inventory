package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class SwappedHandItemsPacketReceiver implements ClientPlayNetworking.PlayPayloadHandler<SwappedHandItemsPacket> {
	@Override
	public void receive(SwappedHandItemsPacket payload, ClientPlayNetworking.Context context) {

		int entityId = payload.id();
		boolean mainHand = payload.mainHand();
		ClientPlayerEntity clientPlayer = context.player();

		if (clientPlayer != null && clientPlayer.getWorld().getEntityById(entityId) != null) {
			PlayerEntity player = (PlayerEntity) clientPlayer.getWorld().getEntityById(entityId);
			ItemStack alternativeItemStack;
			ItemStack itemStack;
			if (player != null && player != clientPlayer) {
				if (mainHand) {
					alternativeItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getAlternativeHand().copy();
					itemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getHand().copy();
					((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setAlternativeHand(itemStack);
					((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setHand(alternativeItemStack);
				} else {
					alternativeItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getAlternativeOffhand().copy();
					itemStack = player.getEquippedStack(EquipmentSlot.OFFHAND).copy();
					((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setAlternativeOffhand(itemStack);
					player.equipStack(EquipmentSlot.OFFHAND, alternativeItemStack);
				}
			}
		}
	}
}
