package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.block.entity.RelayTriggerBlockBlockEntity;
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

import java.util.ArrayList;
import java.util.List;

public class UpdateRelayTriggerBlockPacketReceiver implements ServerPlayNetworking.PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos relayTriggerBlockPos = buf.readBlockPos();

        int creativeScreenScrollingContentSize = buf.readInt();
        List<BlockPos> triggeredBlocks = new ArrayList<>(List.of());

        for (int i = 0; i < creativeScreenScrollingContentSize; i++) {
            triggeredBlocks.add(buf.readBlockPos());
        }

        server.execute(() -> {
            World world = player.getWorld();

            boolean updateSuccessful = true;

            BlockEntity blockEntity = world.getBlockEntity(relayTriggerBlockPos);
            BlockState blockState = world.getBlockState(relayTriggerBlockPos);

            if (blockEntity instanceof RelayTriggerBlockBlockEntity relayTriggerBlockBlockEntity) {
                if (!relayTriggerBlockBlockEntity.setTriggeredBlocks(triggeredBlocks)) {
                    player.sendMessage(Text.translatable("triggered_block.triggeredBlocks.invalid"), false);
                    updateSuccessful = false;
                }
                if (updateSuccessful) {
                    player.sendMessage(Text.translatable("relay_trigger_block.update_successful"), true);
                }
                relayTriggerBlockBlockEntity.markDirty();
                world.updateListeners(relayTriggerBlockPos, blockState, blockState, Block.NOTIFY_ALL);
            }
        });
    }
}
