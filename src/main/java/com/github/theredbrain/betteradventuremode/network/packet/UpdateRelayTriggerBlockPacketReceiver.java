package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.block.entity.RelayTriggerBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.List;

public class UpdateRelayTriggerBlockPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UpdateRelayTriggerBlockPacket> {

    @Override
    public void receive(UpdateRelayTriggerBlockPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos relayTriggerBlockPos = packet.relayTriggerBlockPosition;

        RelayTriggerBlockEntity.SelectionMode selectionMode = packet.selectionMode;

        boolean showArea = packet.showArea;
        boolean resetsArea = packet.resetsArea;
        Vec3i areaDimensions = packet.areaDimensions;
        BlockPos areaPositionOffset = packet.areaPositionOffset;

        List<MutablePair<MutablePair<BlockPos, Boolean>, Integer>> triggeredBlocks = packet.triggeredBlocks;
        RelayTriggerBlockEntity.TriggerMode triggerMode = packet.triggerMode;
        int triggerAmount = packet.triggerAmount;

        World world = player.getWorld();

        BlockEntity blockEntity = world.getBlockEntity(relayTriggerBlockPos);
        BlockState blockState = world.getBlockState(relayTriggerBlockPos);

        if (blockEntity instanceof RelayTriggerBlockEntity relayTriggerBlockEntity) {
            relayTriggerBlockEntity.setSelectionMode(selectionMode);
            relayTriggerBlockEntity.setShowArea(showArea);
            relayTriggerBlockEntity.setResetsArea(resetsArea);
            relayTriggerBlockEntity.setAreaDimensions(areaDimensions);
            relayTriggerBlockEntity.setAreaPositionOffset(areaPositionOffset);
            relayTriggerBlockEntity.setTriggeredBlocks(triggeredBlocks);
            relayTriggerBlockEntity.setTriggerMode(triggerMode);
            relayTriggerBlockEntity.setTriggerAmount(triggerAmount);
            player.sendMessage(Text.translatable("hud.message.script_block.update_successful"), true);

            relayTriggerBlockEntity.markDirty();
            world.updateListeners(relayTriggerBlockPos, blockState, blockState, Block.NOTIFY_ALL);
        }
    }
}
