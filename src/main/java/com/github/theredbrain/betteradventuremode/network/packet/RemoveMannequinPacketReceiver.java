package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.entity.decoration.MannequinEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class RemoveMannequinPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<RemoveMannequinPacket> {
    @Override
    public void receive(RemoveMannequinPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        int mannequinEntityId = packet.mannequinEntityId;

        int flag = packet.flag;

        World world = player.getWorld();

        Entity entity = world.getEntityById(mannequinEntityId);

        if (entity instanceof MannequinEntity mannequinEntity) {
            if (flag == 0) {
                mannequinEntity.kill();
            } else if (flag == 1) { // TODO mannequin entity
                player.sendMessage(Text.translatable("mannequin_entity.change_model_to_slim"), true);
            } else if (flag == 2) { // TODO mannequin entity
                player.sendMessage(Text.translatable("mannequin_entity.change_model_to_wide"), true);
            }
        }
    }
}
