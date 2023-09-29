package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.block.entity.StructurePlacerBlockBlockEntity;
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

public class UpdateStructurePlacerBlockPacketReceiver implements ServerPlayNetworking.PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos structurePlacerBlockPos = buf.readBlockPos();

        String placedStructureIdentifier = buf.readString();

        BlockPos placementPositionOffset = buf.readBlockPos();

        BlockPos triggeredBlockPositionOffset = buf.readBlockPos();

        server.execute(() -> {
            World world = player.getWorld();

            boolean updateSuccessful = true;

            BlockEntity blockEntity = world.getBlockEntity(structurePlacerBlockPos);
            BlockState blockState = world.getBlockState(structurePlacerBlockPos);

            if (blockEntity instanceof StructurePlacerBlockBlockEntity structurePlacerBlockBlockEntity) {
                if (!structurePlacerBlockBlockEntity.setPlacedStructureIdentifier(placedStructureIdentifier)) {
                    player.sendMessage(Text.translatable("structure_placer_block.placedStructureIdentifier.invalid"), false);
                    updateSuccessful = false;
                }
                if (!structurePlacerBlockBlockEntity.setPlacementPositionOffset(placementPositionOffset)) {
                    player.sendMessage(Text.translatable("structure_placer_block.placementPositionOffset.invalid"), false);
                    updateSuccessful = false;
                }
                if (!structurePlacerBlockBlockEntity.setTriggeredBlockPositionOffset(triggeredBlockPositionOffset)) {
                    player.sendMessage(Text.translatable("triggered_block.triggeredBlockPositionOffset.invalid"), false);
                    updateSuccessful = false;
                }
                if (updateSuccessful) {
                    player.sendMessage(Text.translatable("structure_placer_block.update_successful"), true);
                }
                structurePlacerBlockBlockEntity.markDirty();
                world.updateListeners(structurePlacerBlockPos, blockState, blockState, Block.NOTIFY_ALL);
            }
        });
    }
}
