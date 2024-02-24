package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.block.entity.AreaBlockEntity;
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

public class UpdateAreaBlockPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UpdateAreaBlockPacket> {
    @Override
    public void receive(UpdateAreaBlockPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos areaBlockPosition = packet.areaBlockPosition;

        boolean showArea = packet.showArea;
        Vec3i areaDimensions = packet.applicationAreaDimensions;
        BlockPos areaPositionOffset = packet.applicationAreaPositionOffset;

        String appliedStatusEffectIdentifier = packet.appliedStatusEffectIdentifier;
        int appliedStatusEffectAmplifier = packet.appliedStatusEffectAmplifier;
        boolean appliedStatusEffectAmbient = packet.appliedStatusEffectAmbient;
        boolean appliedStatusEffectShowParticles = packet.appliedStatusEffectShowParticles;
        boolean appliedStatusEffectShowIcon = packet.appliedStatusEffectShowIcon;


        BlockPos triggeredBlockPositionOffset = packet.triggeredBlockPositionOffset;
        boolean triggeredBlockResets = packet.triggeredBlockResets;
        boolean wasTriggered = packet.wasTriggered;

        String joinMessage = packet.joinMessage;
        String leaveMessage = packet.leaveMessage;
        String triggeredMessage = packet.triggeredMessage;

        AreaBlockEntity.MessageMode messageMode = packet.messageMode;
        AreaBlockEntity.TriggerMode triggerMode = packet.triggerMode;
        AreaBlockEntity.TriggeredMode triggeredMode = packet.triggeredMode;
        int timer = packet.timer;

        World world = player.getWorld();

        boolean updateSuccessful = true;

        BlockEntity blockEntity = world.getBlockEntity(areaBlockPosition);
        BlockState blockState = world.getBlockState(areaBlockPosition);

        if (blockEntity instanceof AreaBlockEntity areaBlockEntity) {
            areaBlockEntity.reset();
            areaBlockEntity.setShowArea(showArea);
            if (!areaBlockEntity.setAreaDimensions(areaDimensions)) {
                player.sendMessage(Text.translatable("area_block.areaDimensions.invalid"), false);
                updateSuccessful = false;
            }
            if (!areaBlockEntity.setAreaPositionOffset(areaPositionOffset)) {
                player.sendMessage(Text.translatable("area_block.areaPositionOffset.invalid"), false);
                updateSuccessful = false;
            }
            if (!areaBlockEntity.setAppliedStatusEffectIdentifier(appliedStatusEffectIdentifier)) {
                player.sendMessage(Text.translatable("area_block.appliedStatusEffectIdentifier.invalid"), false);
                updateSuccessful = false;
            }
            if (!areaBlockEntity.setAppliedStatusEffectAmplifier(appliedStatusEffectAmplifier)) {
                player.sendMessage(Text.translatable("area_block.appliedStatusEffectAmplifier.invalid"), false);
                updateSuccessful = false;
            }
            areaBlockEntity.setAppliedStatusEffectAmbient(appliedStatusEffectAmbient);
            areaBlockEntity.setAppliedStatusEffectShowParticles(appliedStatusEffectShowParticles);
            areaBlockEntity.setAppliedStatusEffectShowIcon(appliedStatusEffectShowIcon);

            areaBlockEntity.setTriggeredBlock(new MutablePair<>(triggeredBlockPositionOffset, triggeredBlockResets));
            areaBlockEntity.setWasTriggered(wasTriggered);

            areaBlockEntity.setJoinMessage(joinMessage);
            areaBlockEntity.setLeaveMessage(leaveMessage);
            areaBlockEntity.setTriggeredMessage(triggeredMessage);

            areaBlockEntity.setMessageMode(messageMode);
            areaBlockEntity.setTriggerMode(triggerMode);
            areaBlockEntity.setTriggeredMode(triggeredMode);
            areaBlockEntity.setMaxTimer(timer);

            if (updateSuccessful) {
                player.sendMessage(Text.translatable("area_block.update_successful"), true);
            }
            areaBlockEntity.markDirty();
            world.updateListeners(areaBlockPosition, blockState, blockState, Block.NOTIFY_ALL);
        }
    }
}
