package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.entity.decoration.MannequinEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class ExportImportMannequinEquipmentPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<ExportImportMannequinEquipmentPacket> {
    @Override
    public void receive(ExportImportMannequinEquipmentPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        int mannequinEntityId = packet.mannequinEntityId;

        boolean isExport = packet.isExport;

        World world = player.getWorld();

        Entity entity = world.getEntityById(mannequinEntityId);

        if (entity instanceof MannequinEntity mannequinEntity) {
            // TODO mannequin entity
            player.sendMessage(isExport ? Text.literal("exported equipment") : Text.literal("imported equipment"));
        }
    }
}
