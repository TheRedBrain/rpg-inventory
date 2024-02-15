package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.block.entity.JigsawPlacerBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.JigsawBlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.MutablePair;

public class UpdateJigsawPlacerBlockPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UpdateJigsawPlacerBlockPacket> {
    @Override
    public void receive(UpdateJigsawPlacerBlockPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos jigsawPlacerBlockPosition = packet.jigsawPlacerBlockPosition;

        String target = packet.target;
        String pool = packet.pool;

        JigsawBlockEntity.Joint joint = packet.joint;

        BlockPos triggeredBlockPositionOffset = packet.triggeredBlockPositionOffset;

        boolean triggeredBlockResets = packet.triggeredBlockResets;

        World world = player.getWorld();

        boolean updateSuccessful = true;

        BlockEntity blockEntity = world.getBlockEntity(jigsawPlacerBlockPosition);
        BlockState blockState = world.getBlockState(jigsawPlacerBlockPosition);

        if (blockEntity instanceof JigsawPlacerBlockEntity jigsawPlacerBlockEntity) {
            if (!jigsawPlacerBlockEntity.setTarget(target)) {
                player.sendMessage(Text.translatable("jigsaw_placer_block.target.invalid"), false);
                updateSuccessful = false;
            }
            if (!jigsawPlacerBlockEntity.setPool(pool)) {
                player.sendMessage(Text.translatable("jigsaw_placer_block.pool.invalid"), false);
                updateSuccessful = false;
            }
            jigsawPlacerBlockEntity.setJoint(joint);
            jigsawPlacerBlockEntity.setTriggeredBlock(new MutablePair<>(triggeredBlockPositionOffset, triggeredBlockResets));
            if (updateSuccessful) {
                player.sendMessage(Text.translatable("jigsaw_placer_block.update_successful"), true);
            }
            jigsawPlacerBlockEntity.markDirty();
            world.updateListeners(jigsawPlacerBlockPosition, blockState, blockState, Block.NOTIFY_ALL);
        }
    }
}
