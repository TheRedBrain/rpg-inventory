package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.block.entity.MimicBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UpdateMimicBlockPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UpdateMimicBlockPacket> {
    @Override
    public void receive(UpdateMimicBlockPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos mimicBlockPosition = packet.mimicBlockPosition;

        BlockPos activeMimicBlockPositionOffset = packet.activeMimicBlockPositionOffset;
        BlockPos inactiveMimicBlockPositionOffset = packet.inactiveMimicBlockPositionOffset;

        World world = player.getWorld();

        boolean updateSuccessful = true;

        BlockEntity blockEntity = world.getBlockEntity(mimicBlockPosition);
        BlockState blockState = world.getBlockState(mimicBlockPosition);

        if (blockEntity instanceof MimicBlockEntity mimicBlockEntity) {
            if (!mimicBlockEntity.setActiveMimicBlockPositionOffset(activeMimicBlockPositionOffset)) {
                player.sendMessage(Text.translatable("mimic_block.activeMimicBlockPositionOffset.invalid"), false);
                updateSuccessful = false;
            }
            if (!mimicBlockEntity.setInactiveMimicBlockPositionOffset(inactiveMimicBlockPositionOffset)) {
                player.sendMessage(Text.translatable("mimic_block.inactiveMimicBlockPositionOffset.invalid"), false);
                updateSuccessful = false;
            }
            if (updateSuccessful) {
                player.sendMessage(Text.translatable("mimic_block.update_successful"), true);
            }
            mimicBlockEntity.markDirty();
            world.updateListeners(mimicBlockPosition, blockState, blockState, Block.NOTIFY_ALL);
        }
    }
}
