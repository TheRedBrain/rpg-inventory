package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.block.entity.RedstoneTriggerBlockBlockEntity;
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

public class UpdateRedstoneTriggerBlockPacketReceiver implements ServerPlayNetworking.PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos redstoneTriggerBlockPos = buf.readBlockPos();

        BlockPos triggeredBlockPositionOffset = buf.readBlockPos();

        server.execute(() -> {
            World world = player.method_48926();

            boolean updateSuccessful = true;

            BlockEntity blockEntity = world.getBlockEntity(redstoneTriggerBlockPos);
            BlockState blockState = world.getBlockState(redstoneTriggerBlockPos);

            if (blockEntity instanceof RedstoneTriggerBlockBlockEntity redstoneTriggerBlockBlockEntity) {
                if (!redstoneTriggerBlockBlockEntity.setTriggeredBlockPositionOffset(triggeredBlockPositionOffset)) {
                    player.sendMessage(Text.translatable("triggered_block.triggeredBlockPositionOffset.invalid"), false);
                    updateSuccessful = false;
                }
                if (updateSuccessful) {
                    player.sendMessage(Text.translatable("redstone_trigger_block.update_successful"), true);
                }
                redstoneTriggerBlockBlockEntity.markDirty();
                world.updateListeners(redstoneTriggerBlockPos, blockState, blockState, Block.NOTIFY_ALL);
            }
        });
    }
}
