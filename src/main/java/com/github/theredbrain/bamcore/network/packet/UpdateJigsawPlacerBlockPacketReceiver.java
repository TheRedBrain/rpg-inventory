package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.block.entity.JigsawPlacerBlockBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.JigsawBlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UpdateJigsawPlacerBlockPacketReceiver implements ServerPlayNetworking.PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos jigsawPlacerBlockPos = buf.readBlockPos();

//        String name = buf.readString();
        String target = buf.readString();
        String pool = buf.readString();

        JigsawBlockEntity.Joint joint = JigsawBlockEntity.Joint.byName(buf.readString()).orElse(JigsawBlockEntity.Joint.ALIGNED);

        BlockPos triggeredBlockPositionOffset = buf.readBlockPos();

        server.execute(() -> {
            World world = player.getWorld();

            boolean updateSuccessful = true;

            BlockEntity blockEntity = world.getBlockEntity(jigsawPlacerBlockPos);
            BlockState blockState = world.getBlockState(jigsawPlacerBlockPos);

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
                world.updateListeners(jigsawPlacerBlockPos, blockState, blockState, Block.NOTIFY_ALL);
            }
        });
    }
}
