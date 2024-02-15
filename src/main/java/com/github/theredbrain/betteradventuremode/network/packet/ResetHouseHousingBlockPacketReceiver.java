package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.block.entity.HousingBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ResetHouseHousingBlockPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<ResetHouseHousingBlockPacket> {
    @Override
    public void receive(ResetHouseHousingBlockPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        BlockPos housingBlockPosition = packet.housingBlockPosition;

        World world = player.getWorld();

        BlockEntity blockEntity = world.getBlockEntity(housingBlockPosition);

        // TODO teleport all players inside to their spawn?
        if (blockEntity instanceof HousingBlockEntity housingBlockEntity) {
            housingBlockEntity.trigger();
        }
    }
}
