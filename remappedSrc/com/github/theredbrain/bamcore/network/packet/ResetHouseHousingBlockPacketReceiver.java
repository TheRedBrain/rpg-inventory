package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.block.entity.HousingBlockBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ResetHouseHousingBlockPacketReceiver implements ServerPlayNetworking.PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        BlockPos housingBlockPos = buf.readBlockPos();

        server.execute(() -> {
            World world = player.method_48926();

            BlockEntity blockEntity = world.getBlockEntity(housingBlockPos);

            // TODO teleport all players inside to their spawn?
            if (blockEntity instanceof HousingBlockBlockEntity housingBlockBlockEntity) {
                housingBlockBlockEntity.trigger();
            }
        });
    }
}
