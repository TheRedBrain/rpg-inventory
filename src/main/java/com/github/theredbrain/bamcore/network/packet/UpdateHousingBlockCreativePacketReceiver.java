package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.block.entity.HousingBlockBlockEntity;
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

public class UpdateHousingBlockCreativePacketReceiver implements ServerPlayNetworking.PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos housingBlockPos = buf.readBlockPos();

        boolean showRestrictBlockBreakingArea = buf.readBoolean();

        int restrictBlockBreakingAreaDimensionsX = buf.readInt();
        int restrictBlockBreakingAreaDimensionsY = buf.readInt();
        int restrictBlockBreakingAreaDimensionsZ = buf.readInt();
        Vec3i restrictBlockBreakingAreaDimensions = new Vec3i(restrictBlockBreakingAreaDimensionsX, restrictBlockBreakingAreaDimensionsY, restrictBlockBreakingAreaDimensionsZ);
        BlockPos restrictBlockBreakingAreaPositionOffset = buf.readBlockPos();

        server.execute(() -> {
            World world = player.getWorld();

            boolean updateSuccessful = true;

            BlockEntity blockEntity = world.getBlockEntity(housingBlockPos);
            BlockState blockState = world.getBlockState(housingBlockPos);

            if (blockEntity instanceof HousingBlockBlockEntity housingBlockBlockEntity) {

                if (!housingBlockBlockEntity.setShowRestrictBlockBreakingArea(showRestrictBlockBreakingArea)) {
                    player.sendMessage(Text.translatable("housing_block.showRestrictBlockBreakingArea.invalid"), false);
                    updateSuccessful = false;
                }
                if (!housingBlockBlockEntity.setRestrictBlockBreakingAreaDimensions(restrictBlockBreakingAreaDimensions)) {
                    player.sendMessage(Text.translatable("housing_block.restrictBlockBreakingAreaDimensions.invalid"), false);
                    updateSuccessful = false;
                }
                if (!housingBlockBlockEntity.setRestrictBlockBreakingAreaPositionOffset(restrictBlockBreakingAreaPositionOffset)) {
                    player.sendMessage(Text.translatable("housing_block.restrictBlockBreakingAreaPositionOffset.invalid"), false);
                    updateSuccessful = false;
                }
                if (updateSuccessful) {
                    player.sendMessage(Text.translatable("housing_block.update_successful"), true);
                }
                housingBlockBlockEntity.markDirty();
                world.updateListeners(housingBlockPos, blockState, blockState, Block.NOTIFY_ALL);
            }
        });
    }
}
