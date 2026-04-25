package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.DuckLivingEntityMixin;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SheathedWeaponsPacketReceiver implements ClientPlayNetworking.PlayPayloadHandler<SheathedWeaponsPacket> {
	@Override
	public void receive(SheathedWeaponsPacket payload, ClientPlayNetworking.Context context) {

		LocalPlayer clientPlayer = context.player();

		if (((DuckPlayerEntityMixin) clientPlayer).rpginventory$isHandSlotOverhaulActive()) {

			int entityId = payload.id();
			boolean mainHand = payload.mainHand();
			boolean isWeaponSheathed = payload.isSheathed();

			if (clientPlayer.level().getEntity(entityId) != null) {
				Player player = (Player) clientPlayer.level().getEntity(entityId);
				ItemStack itemStack;
				if (player != null && player != clientPlayer) {
					if (mainHand) {
						((DuckLivingEntityMixin) player).rpginventory$setIsHandStackSheathed(isWeaponSheathed);
						itemStack = player.getItemBySlot(EquipmentSlot.MAINHAND).copy();
						if (itemStack.isEmpty()) {
							itemStack = player.getItemBySlot(ExtendedEquipmentSlot.SHEATHED_HAND).copy();
						}
						if (isWeaponSheathed) {
							player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
							player.setItemSlot(ExtendedEquipmentSlot.SHEATHED_HAND, itemStack);
						} else {
							player.setItemSlot(EquipmentSlot.MAINHAND, itemStack);
							player.setItemSlot(ExtendedEquipmentSlot.SHEATHED_HAND, ItemStack.EMPTY);
						}
					} else {
						((DuckLivingEntityMixin) player).rpginventory$setIsOffhandStackSheathed(isWeaponSheathed);
						itemStack = player.getItemBySlot(EquipmentSlot.OFFHAND).copy();
						if (itemStack.isEmpty()) {
							itemStack = player.getItemBySlot(ExtendedEquipmentSlot.SHEATHED_OFF_HAND).copy();
						}
						if (isWeaponSheathed) {
							player.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
							player.setItemSlot(ExtendedEquipmentSlot.SHEATHED_OFF_HAND, itemStack);
						} else {
							player.setItemSlot(EquipmentSlot.OFFHAND, itemStack);
							player.setItemSlot(ExtendedEquipmentSlot.SHEATHED_OFF_HAND, ItemStack.EMPTY);
						}
					}
				}
			}
		}
	}
}
