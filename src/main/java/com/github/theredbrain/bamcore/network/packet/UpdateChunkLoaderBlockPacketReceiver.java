package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.block.entity.ChunkLoaderBlockBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UpdateChunkLoaderBlockPacketReceiver implements ServerPlayNetworking.PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos chunkLoaderBlockPos = buf.readBlockPos();

        boolean loadChunk = buf.readBoolean();

        int startChunkX = buf.readInt();
        int startChunkZ = buf.readInt();
        int endChunkX = buf.readInt();
        int endChunkZ = buf.readInt();

        BlockPos triggeredBlockPositionOffset = buf.readBlockPos();

        server.execute(() -> {
            World world = player.getWorld();

            boolean updateSuccessful = true;

            BlockEntity blockEntity = world.getBlockEntity(chunkLoaderBlockPos);
            BlockState blockState = world.getBlockState(chunkLoaderBlockPos);

            if (blockEntity instanceof ChunkLoaderBlockBlockEntity chunkLoaderBlockBlockEntity) {
                if (!chunkLoaderBlockBlockEntity.setLoadChunk(loadChunk)) {
                    player.sendMessage(Text.translatable("chunk_loader_block.loadChunk.invalid"), false);
                    updateSuccessful = false;
                }
                if (!chunkLoaderBlockBlockEntity.setStartChunkX(startChunkX)) {
                    player.sendMessage(Text.translatable("chunk_loader_block.startChunkX.invalid"), false);
                    updateSuccessful = false;
                }
                if (!chunkLoaderBlockBlockEntity.setStartChunkZ(startChunkZ)) {
                    player.sendMessage(Text.translatable("chunk_loader_block.startChunkZ.invalid"), false);
                    updateSuccessful = false;
                }
                if (!chunkLoaderBlockBlockEntity.setEndChunkX(endChunkX)) {
                    player.sendMessage(Text.translatable("chunk_loader_block.endChunkX.invalid"), false);
                    updateSuccessful = false;
                }
                if (!chunkLoaderBlockBlockEntity.setEndChunkZ(endChunkZ)) {
                    player.sendMessage(Text.translatable("chunk_loader_block.endChunkZ.invalid"), false);
                    updateSuccessful = false;
                }
                if (!chunkLoaderBlockBlockEntity.setTriggeredBlockPositionOffset(triggeredBlockPositionOffset)) {
                    player.sendMessage(Text.translatable("triggered_block.triggeredBlockPositionOffset.invalid"), false);
                    updateSuccessful = false;
                }
                if (updateSuccessful) {
                    player.sendMessage(Text.translatable("chunk_loader_block.update_successful"), true);
                }
                chunkLoaderBlockBlockEntity.markDirty();
                world.updateListeners(chunkLoaderBlockPos, blockState, blockState, Block.NOTIFY_ALL);
            }
        });
    }
}
