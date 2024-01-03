package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.block.entity.JigsawPlacerBlockBlockEntity;
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

        World world = player.getWorld();

        boolean updateSuccessful = true;

        BlockEntity blockEntity = world.getBlockEntity(jigsawPlacerBlockPosition);
        BlockState blockState = world.getBlockState(jigsawPlacerBlockPosition);

        if (blockEntity instanceof JigsawPlacerBlockBlockEntity jigsawPlacerBlockBlockEntity) {
//                if (!jigsawPlacerBlockBlockEntity.setName(name)) {
//                    player.sendMessage(Text.translatable("jigsaw_placer_block.name.invalid"), false);
//                    updateSuccessful = false;
//                }
            if (!jigsawPlacerBlockBlockEntity.setTarget(target)) {
                player.sendMessage(Text.translatable("jigsaw_placer_block.target.invalid"), false);
                updateSuccessful = false;
            }
            if (!jigsawPlacerBlockBlockEntity.setPool(pool)) {
                player.sendMessage(Text.translatable("jigsaw_placer_block.pool.invalid"), false);
                updateSuccessful = false;
            }
            if (!jigsawPlacerBlockBlockEntity.setJoint(joint)) {
                player.sendMessage(Text.translatable("jigsaw_placer_block.joint.invalid"), false);
                updateSuccessful = false;
            }
            if (!jigsawPlacerBlockBlockEntity.setTriggeredBlockPositionOffset(triggeredBlockPositionOffset)) {
                player.sendMessage(Text.translatable("triggered_block.triggeredBlockPositionOffset.invalid"), false);
                updateSuccessful = false;
            }
            if (updateSuccessful) {
                player.sendMessage(Text.translatable("jigsaw_placer_block.update_successful"), true);
            }
            jigsawPlacerBlockBlockEntity.markDirty();
            world.updateListeners(jigsawPlacerBlockPosition, blockState, blockState, Block.NOTIFY_ALL);
        }
    }
}
