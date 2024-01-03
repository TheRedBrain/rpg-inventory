package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.block.entity.RelayTriggerBlockBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class UpdateRelayTriggerBlockPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UpdateRelayTriggerBlockPacket> {

    @Override
    public void receive(UpdateRelayTriggerBlockPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos relayTriggerBlockPos = packet.relayTriggerBlockPosition;
        List<BlockPos> triggeredBlocks = packet.triggeredBlocks;

        World world = player.getWorld();

        boolean updateSuccessful = true;

        BlockEntity blockEntity = world.getBlockEntity(relayTriggerBlockPos);
        BlockState blockState = world.getBlockState(relayTriggerBlockPos);

        if (blockEntity instanceof RelayTriggerBlockBlockEntity relayTriggerBlockBlockEntity) {
            if (!relayTriggerBlockBlockEntity.setTriggeredBlocks(triggeredBlocks)) {
                player.sendMessage(Text.translatable("triggered_block.triggeredBlocks.invalid"), false);
                updateSuccessful = false;
            }
            if (updateSuccessful) {
                player.sendMessage(Text.translatable("relay_trigger_block.update_successful"), true);
            }
            relayTriggerBlockBlockEntity.markDirty();
            world.updateListeners(relayTriggerBlockPos, blockState, blockState, Block.NOTIFY_ALL);
        }
    }
}
