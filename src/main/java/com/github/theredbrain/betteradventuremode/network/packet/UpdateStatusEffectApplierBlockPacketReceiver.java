package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.block.entity.StatusEffectApplierBlockEntity;
import com.github.theredbrain.betteradventuremode.block.entity.UseRelayBlockEntity;
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

public class UpdateStatusEffectApplierBlockPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UpdateStatusEffectApplierBlockPacket> {
    @Override
    public void receive(UpdateStatusEffectApplierBlockPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos statusEffectApplierBlockPosition = packet.statusEffectApplierBlockPosition;
        boolean triggered = packet.triggered;
        boolean showApplicationArea = packet.showApplicationArea;
        Vec3i applicationAreaDimensions = packet.applicationAreaDimensions;
        BlockPos applicationAreaPositionOffset = packet.applicationAreaPositionOffset;
        String appliedStatusEffectIdentifier = packet.appliedStatusEffectIdentifier;
        int appliedStatusEffectAmplifier = packet.appliedStatusEffectAmplifier;
        boolean appliedStatusEffectAmbient = packet.appliedStatusEffectAmbient;
        boolean appliedStatusEffectShowParticles = packet.appliedStatusEffectShowParticles;
        boolean appliedStatusEffectShowIcon = packet.appliedStatusEffectShowIcon;

        World world = player.getWorld();

        boolean updateSuccessful = true;

        BlockEntity blockEntity = world.getBlockEntity(statusEffectApplierBlockPosition);
        BlockState blockState = world.getBlockState(statusEffectApplierBlockPosition);

        if (blockEntity instanceof StatusEffectApplierBlockEntity statusEffectApplierBlockEntity) {
            if (!statusEffectApplierBlockEntity.setTriggered(triggered)) {
                player.sendMessage(Text.translatable("status_effect_applier_block.triggered.invalid"), false);
                updateSuccessful = false;
            }
            if (!statusEffectApplierBlockEntity.setShowApplicationArea(showApplicationArea)) {
                player.sendMessage(Text.translatable("status_effect_applier_block.showApplicationArea.invalid"), false);
                updateSuccessful = false;
            }
            if (!statusEffectApplierBlockEntity.setApplicationAreaDimensions(applicationAreaDimensions)) {
                player.sendMessage(Text.translatable("status_effect_applier_block.applicationAreaDimensions.invalid"), false);
                updateSuccessful = false;
            }
            if (!statusEffectApplierBlockEntity.setApplicationAreaPositionOffset(applicationAreaPositionOffset)) {
                player.sendMessage(Text.translatable("status_effect_applier_block.applicationAreaPositionOffset.invalid"), false);
                updateSuccessful = false;
            }
            if (!statusEffectApplierBlockEntity.setAppliedStatusEffectIdentifier(appliedStatusEffectIdentifier)) {
                player.sendMessage(Text.translatable("status_effect_applier_block.appliedStatusEffectIdentifier.invalid"), false);
                updateSuccessful = false;
            }
            if (!statusEffectApplierBlockEntity.setAppliedStatusEffectAmplifier(appliedStatusEffectAmplifier)) {
                player.sendMessage(Text.translatable("status_effect_applier_block.appliedStatusEffectAmplifier.invalid"), false);
                updateSuccessful = false;
            }
            if (!statusEffectApplierBlockEntity.setAppliedStatusEffectAmbient(appliedStatusEffectAmbient)) {
                player.sendMessage(Text.translatable("status_effect_applier_block.appliedStatusEffectAmbient.invalid"), false);
                updateSuccessful = false;
            }
            if (!statusEffectApplierBlockEntity.setAppliedStatusEffectShowParticles(appliedStatusEffectShowParticles)) {
                player.sendMessage(Text.translatable("status_effect_applier_block.appliedStatusEffectShowParticles.invalid"), false);
                updateSuccessful = false;
            }
            if (!statusEffectApplierBlockEntity.setAppliedStatusEffectShowIcon(appliedStatusEffectShowIcon)) {
                player.sendMessage(Text.translatable("status_effect_applier_block.appliedStatusEffectShowIcon.invalid"), false);
                updateSuccessful = false;
            }
            if (updateSuccessful) {
                player.sendMessage(Text.translatable("status_effect_applier_block.update_successful"), true);
            }
            statusEffectApplierBlockEntity.markDirty();
            world.updateListeners(statusEffectApplierBlockPosition, blockState, blockState, Block.NOTIFY_ALL);
        }
    }
}
