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

public class UpdateLocationControlBlockPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UpdateLocationControlBlockPacket> {
    @Override
    public void receive(UpdateLocationControlBlockPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos locationControlBlockPosition = packet.locationControlBlockPosition;

        BlockPos mainEntrancePositionOffset = packet.mainEntrancePositionOffset;
        double mainEntranceYaw = packet.mainEntranceYaw;
        double mainEntrancePitch = packet.mainEntrancePitch;

        HashMap<String, MutablePair<BlockPos, MutablePair<Double, Double>>> sideEntrances = new HashMap<>(Map.of());

        for (MutablePair<String, MutablePair<BlockPos, MutablePair<Double, Double>>> sideEntrance : packet.sideEntrancesList) {
            sideEntrances.put(sideEntrance.getLeft(), sideEntrance.getRight());
        }

        BlockPos triggeredBlockPositionOffset = packet.triggeredBlockPositionOffset;

        boolean shouldAlwaysReset = packet.shouldAlwaysReset;

        World world = player.getWorld();

        BlockEntity blockEntity = world.getBlockEntity(locationControlBlockPosition);
        BlockState blockState = world.getBlockState(locationControlBlockPosition);

        if (blockEntity instanceof LocationControlBlockEntity locationControlBlockEntity) {

            locationControlBlockEntity.setMainEntrance(new MutablePair<>(mainEntrancePositionOffset, new MutablePair<>(mainEntranceYaw, mainEntrancePitch)));
            locationControlBlockEntity.setSideEntrances(sideEntrances);
            locationControlBlockEntity.setTriggeredBlockPositionOffset(triggeredBlockPositionOffset);
            locationControlBlockEntity.setShouldAlwaysReset(shouldAlwaysReset);

            player.sendMessage(Text.translatable("location_control_block.update_successful"), true);
            locationControlBlockEntity.markDirty();
            world.updateListeners(locationControlBlockPosition, blockState, blockState, Block.NOTIFY_ALL);
        }
    }
}
