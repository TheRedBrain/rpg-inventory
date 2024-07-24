package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class SheathedWeaponsPacketReceiver implements ClientPlayNetworking.PlayPayloadHandler<SheathedWeaponsPacket> {
	@Override
	public void receive(SheathedWeaponsPacket payload, ClientPlayNetworking.Context context) {

		int entityId = payload.id();
		boolean mainHand = payload.mainHand();
		boolean isWeaponSheathed = payload.isSheathed();
		ClientPlayerEntity clientPlayer = context.player();

		if (clientPlayer != null && clientPlayer.getWorld().getEntityById(entityId) != null) {
			PlayerEntity player = (PlayerEntity) clientPlayer.getWorld().getEntityById(entityId);
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
					itemStack = player.getEquippedStack(EquipmentSlot.OFFHAND).copy();
					if (itemStack.isEmpty()) {
						itemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getSheathedOffhand().copy();
					}
					if (isWeaponSheathed) {
						player.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
						((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedOffhand(itemStack);
					} else {
						player.equipStack(EquipmentSlot.OFFHAND, itemStack);
						((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedOffhand(ItemStack.EMPTY);
					}
				}
			}
		}
	}
}
