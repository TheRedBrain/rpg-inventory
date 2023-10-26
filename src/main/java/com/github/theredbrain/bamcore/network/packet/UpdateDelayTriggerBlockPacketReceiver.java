package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.block.entity.DelayTriggerBlockBlockEntity;
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

public class UpdateDelayTriggerBlockPacketReceiver implements ServerPlayNetworking.PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos delayTriggerBlockPos = buf.readBlockPos();

        BlockPos triggeredBlockPositionOffset = buf.readBlockPos();

        int triggerDelay = buf.readInt();

        server.execute(() -> {
            World world = player.getWorld();

            boolean updateSuccessful = true;

            BlockEntity blockEntity = world.getBlockEntity(delayTriggerBlockPos);
            BlockState blockState = world.getBlockState(delayTriggerBlockPos);

            if (blockEntity instanceof DelayTriggerBlockBlockEntity delayTriggerBlockBlockEntity) {
                if (!delayTriggerBlockBlockEntity.setTriggeredBlockPositionOffset(triggeredBlockPositionOffset)) {
                    player.sendMessage(Text.translatable("triggered_block.triggeredBlockPositionOffset.invalid"), false);
                    updateSuccessful = false;
                }
                if (!delayTriggerBlockBlockEntity.setTriggerDelay(triggerDelay)) {
                    player.sendMessage(Text.translatable("delay_trigger_block.triggerDelay.invalid"), false);
                    updateSuccessful = false;
                }
                if (updateSuccessful) {
                    player.sendMessage(Text.translatable("delay_trigger_block.update_successful"), true);
                }
                delayTriggerBlockBlockEntity.markDirty();
                world.updateListeners(delayTriggerBlockPos, blockState, blockState, Block.NOTIFY_ALL);
            }
        });
    }
}
