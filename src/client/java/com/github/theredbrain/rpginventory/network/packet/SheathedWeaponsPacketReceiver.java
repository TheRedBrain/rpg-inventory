package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SheathedWeaponsPacketReceiver implements ClientPlayNetworking.PlayPayloadHandler<SheathedWeaponsPacket> {
	@Override
	public void receive(SheathedWeaponsPacket payload, ClientPlayNetworking.Context context) {

		if (RPGInventory.isHandSlotOverhaulActive()) {

			int entityId = payload.id();
			boolean mainHand = payload.mainHand();
			boolean isWeaponSheathed = payload.isSheathed();
			LocalPlayer clientPlayer = context.player();

			if (clientPlayer != null && clientPlayer.level().getEntity(entityId) != null) {
				Player player = (Player) clientPlayer.level().getEntity(entityId);
				ItemStack itemStack;
				if (player != null && player != clientPlayer) {
					if (mainHand) {
						((DuckPlayerEntityMixin) player).rpginventory$setIsHandStackSheathed(isWeaponSheathed);
						itemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getHand().copy();
						if (itemStack.isEmpty()) {
							itemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getSheathedHand().copy();
						}
						if (isWeaponSheathed) {
							((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setHand(ItemStack.EMPTY);
							((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedHand(itemStack);
						} else {
							((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setHand(itemStack);
							((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedHand(ItemStack.EMPTY);
						}
					} else {
						((DuckPlayerEntityMixin) player).rpginventory$setIsOffhandStackSheathed(isWeaponSheathed);
						itemStack = player.getItemBySlot(EquipmentSlot.OFFHAND).copy();
						if (itemStack.isEmpty()) {
							itemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getSheathedOffhand().copy();
						}
						if (isWeaponSheathed) {
							player.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
							((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedOffhand(itemStack);
						} else {
							player.setItemSlot(EquipmentSlot.OFFHAND, itemStack);
							((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedOffhand(ItemStack.EMPTY);
						}
					}
				}
			}
		}
	}
}
