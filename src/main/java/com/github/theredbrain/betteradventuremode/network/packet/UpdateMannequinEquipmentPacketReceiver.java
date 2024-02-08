package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.entity.decoration.MannequinEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class UpdateMannequinEquipmentPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UpdateMannequinEquipmentPacket> {
    @Override
    public void receive(UpdateMannequinEquipmentPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        int mannequinEntityId = packet.mannequinEntityId;

        ItemStack equipmentStack = packet.equipmentStack;

        int index = packet.index;

        World world = player.getWorld();

        Entity entity = world.getEntityById(mannequinEntityId);

        boolean updateSuccessful = true;

        if (entity instanceof MannequinEntity mannequinEntity) {
            if (!mannequinEntity.setTrinketItemStackByIndex(equipmentStack, index)) {
                player.sendMessage(Text.translatable("mannequin_entity.equipmentStack.invalid"), false);
                updateSuccessful = false;
            }
            if (updateSuccessful) {
                player.sendMessage(Text.translatable("mannequin_entity.update_successful"), true);
            }
        }
    }
}
