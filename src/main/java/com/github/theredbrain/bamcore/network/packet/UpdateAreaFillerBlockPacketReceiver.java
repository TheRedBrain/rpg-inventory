package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.block.entity.AreaFillerBlockBlockEntity;
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
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class UpdateAreaFillerBlockPacketReceiver implements ServerPlayNetworking.PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos areaFillerBlockPos = buf.readBlockPos();

        String fillerBlockIdentifier = buf.readString();

        int filledAreaDimensionsX = buf.readInt();
        int filledAreaDimensionsY = buf.readInt();
        int filledAreaDimensionsZ = buf.readInt();

        Vec3i filledAreaDimensions = new Vec3i(filledAreaDimensionsX, filledAreaDimensionsY, filledAreaDimensionsZ);
        
        BlockPos filledAreaPositionOffset = buf.readBlockPos();

        BlockPos triggeredBlockPositionOffset = buf.readBlockPos();

        server.execute(() -> {
            World world = player.getWorld();

            boolean updateSuccessful = true;

            BlockEntity blockEntity = world.getBlockEntity(areaFillerBlockPos);
            BlockState blockState = world.getBlockState(areaFillerBlockPos);

            if (blockEntity instanceof AreaFillerBlockBlockEntity areaFillerBlockBlockEntity) {
                if (!areaFillerBlockBlockEntity.setFillerBlockIdentifier(fillerBlockIdentifier)) {
                    player.sendMessage(Text.translatable("area_filler_block.fillerBlockIdentifier.invalid"), false);
                    updateSuccessful = false;
                }
                if (!areaFillerBlockBlockEntity.setFilledAreaDimensions(filledAreaDimensions)) {
                    player.sendMessage(Text.translatable("area_filler_block.filledAreaDimensions.invalid"), false);
                    updateSuccessful = false;
                }
                if (!areaFillerBlockBlockEntity.setFilledAreaPositionOffset(filledAreaPositionOffset)) {
                    player.sendMessage(Text.translatable("area_filler_block.filledAreaPositionOffset.invalid"), false);
                    updateSuccessful = false;
                }
                if (!areaFillerBlockBlockEntity.setTriggeredBlockPositionOffset(triggeredBlockPositionOffset)) {
                    player.sendMessage(Text.translatable("triggered_block.triggeredBlockPositionOffset.invalid"), false);
                    updateSuccessful = false;
                }
                if (updateSuccessful) {
                    player.sendMessage(Text.translatable("area_filler_block.update_successful"), true);
                }
                areaFillerBlockBlockEntity.markDirty();
                world.updateListeners(areaFillerBlockPos, blockState, blockState, Block.NOTIFY_ALL);
            }
        });
    }
}
