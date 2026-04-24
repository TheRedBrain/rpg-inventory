package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
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

			if (clientPlayer.level().getEntity(entityId) != null) {
				Player player = (Player) clientPlayer.level().getEntity(entityId);
				ItemStack alternativeItemStack;
				ItemStack itemStack;
				if (player != null && player != clientPlayer) {
					if (mainHand) {
						alternativeItemStack = player.getItemBySlot(ExtendedEquipmentSlot.ALTERNATIVE_HAND).copy();
						itemStack = player.getItemBySlot(EquipmentSlot.MAINHAND).copy();
						player.setItemSlot(ExtendedEquipmentSlot.ALTERNATIVE_HAND, itemStack);
						player.setItemSlot(EquipmentSlot.MAINHAND, alternativeItemStack);
					} else {
						alternativeItemStack = player.getItemBySlot(ExtendedEquipmentSlot.ALTERNATIVE_OFF_HAND).copy();
						itemStack = player.getItemBySlot(EquipmentSlot.OFFHAND).copy();
						player.setItemSlot(ExtendedEquipmentSlot.ALTERNATIVE_OFF_HAND, itemStack);
						player.setItemSlot(EquipmentSlot.OFFHAND, alternativeItemStack);
					}
				}
			}
		}
	}
}
