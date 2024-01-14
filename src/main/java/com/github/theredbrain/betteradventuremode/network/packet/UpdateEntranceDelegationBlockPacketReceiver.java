package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.block.entity.EntranceDelegationBlockEntity;
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

public class UpdateEntranceDelegationBlockPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UpdateEntranceDelegationBlockPacket> {
    @Override
    public void receive(UpdateEntranceDelegationBlockPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos entranceDelegationBlockPosition = packet.entranceDelegationBlockPosition;

        BlockPos delegatedEntrancePositionOffset = packet.delegatedEntrancePositionOffset;
        double delegatedEntranceYaw = packet.delegatedEntranceYaw;
        double delegatedEntrancePitch = packet.delegatedEntrancePitch;

        World world = player.getWorld();

        boolean updateSuccessful = true;

        BlockEntity blockEntity = world.getBlockEntity(entranceDelegationBlockPosition);
        BlockState blockState = world.getBlockState(entranceDelegationBlockPosition);

        if (blockEntity instanceof EntranceDelegationBlockEntity entranceDelegationBlockEntity) {
            if (!entranceDelegationBlockEntity.setDelegatedEntrance(new MutablePair<>(delegatedEntrancePositionOffset, new MutablePair<>(delegatedEntranceYaw, delegatedEntrancePitch)))) {
                player.sendMessage(Text.translatable("entrance_delegation_block.delegatedEntrance.invalid"), false);
                updateSuccessful = false;
            }
            if (updateSuccessful) {
                player.sendMessage(Text.translatable("entrance_delegation_block.update_successful"), true);
            }
            entranceDelegationBlockEntity.markDirty();
            world.updateListeners(entranceDelegationBlockPosition, blockState, blockState, Block.NOTIFY_ALL);
        }
    }
}
