package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.block.entity.LocationControlBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.HashMap;
import java.util.Map;

public class SetManualResetLocationControlBlockPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<SetManualResetLocationControlBlockPacket> {
    @Override
    public void receive(SetManualResetLocationControlBlockPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos locationControlBlockPosition = packet.locationControlBlockPosition;

        boolean manualReset = packet.manualReset;

        World world = player.getWorld();

        BlockEntity blockEntity = world.getBlockEntity(locationControlBlockPosition);
        BlockState blockState = world.getBlockState(locationControlBlockPosition);

        if (blockEntity instanceof LocationControlBlockEntity locationControlBlockEntity) {

            locationControlBlockEntity.setManualReset(manualReset);

            locationControlBlockEntity.markDirty();
            world.updateListeners(locationControlBlockPosition, blockState, blockState, Block.NOTIFY_ALL);
        }
    }
}
