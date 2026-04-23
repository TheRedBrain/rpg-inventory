package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SwappedHandItemsPacketReceiver implements ClientPlayNetworking.PlayPayloadHandler<SwappedHandItemsPacket> {
	@Override
	public void receive(SwappedHandItemsPacket payload, ClientPlayNetworking.Context context) {

		if (RPGInventory.isHandSlotOverhaulActive()) {

			int entityId = payload.id();
			boolean mainHand = payload.mainHand();
			LocalPlayer clientPlayer = context.player();

			if (clientPlayer != null && clientPlayer.level().getEntity(entityId) != null) {
				Player player = (Player) clientPlayer.level().getEntity(entityId);
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
						itemStack = player.getItemBySlot(EquipmentSlot.OFFHAND).copy();
						((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setAlternativeOffhand(itemStack);
						player.setItemSlot(EquipmentSlot.OFFHAND, alternativeItemStack);
					}
				}
			}
		}
	}
}
