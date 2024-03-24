package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.block.entity.InteractiveLootBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UpdateInteractiveLootBlockPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UpdateInteractiveLootBlockPacket> {
    @Override
    public void receive(UpdateInteractiveLootBlockPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos interactiveLootBlockPosition = packet.interactiveLootBlockPosition;

        String lootTableIdentifierString = packet.lootTableIdentifierString;

        World world = player.getWorld();

        BlockEntity blockEntity = world.getBlockEntity(interactiveLootBlockPosition);
        BlockState blockState = world.getBlockState(interactiveLootBlockPosition);

        if (blockEntity instanceof InteractiveLootBlockEntity interactiveLootBlockEntity) {
            interactiveLootBlockEntity.setLootTableIdentifierString(lootTableIdentifierString);
                player.sendMessage(Text.translatable("interactive_loot_block.update_successful"), true);
            interactiveLootBlockEntity.markDirty();
            world.updateListeners(interactiveLootBlockPosition, blockState, blockState, Block.NOTIFY_ALL);
        }
    }
}
