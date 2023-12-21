package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.block.entity.TriggeredSpawnerBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UpdateTriggeredSpawnerBlockPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UpdateTriggeredSpawnerBlockPacket> {
    @Override
    public void receive(UpdateTriggeredSpawnerBlockPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos triggeredSpawnerBlockPosition = packet.triggeredSpawnerBlockPosition;

        BlockPos entitySpawnPositionOffset = packet.entitySpawnPositionOffset;

//        double entitySpawnOrientationYaw = packet.entitySpawnOrientationYaw;
//
//        double entitySpawnOrientationPitch = packet.entitySpawnOrientationPitch;

        TriggeredSpawnerBlockEntity.SpawningMode spawningMode = packet.spawningMode;

        String entityTypeId = packet.entityTypeId;

        World world = player.getWorld();

        boolean updateSuccessful = true;

        BlockEntity blockEntity = world.getBlockEntity(triggeredSpawnerBlockPosition);
        BlockState blockState = world.getBlockState(triggeredSpawnerBlockPosition);

        if (blockEntity instanceof TriggeredSpawnerBlockEntity triggeredSpawnerBlockEntity) {
            if (!triggeredSpawnerBlockEntity.setEntitySpawnPositionOffset(entitySpawnPositionOffset)) {
                player.sendMessage(Text.translatable("triggered_spawner_block.entitySpawnPositionOffset.invalid"), false);
                updateSuccessful = false;
            }
//            if (!triggeredSpawnerBlockEntity.setEntitySpawnOrientationYaw(entitySpawnOrientationYaw)) {
//                player.sendMessage(Text.translatable("triggered_spawner_block.entitySpawnOrientationYaw.invalid"), false);
//                updateSuccessful = false;
//            }
//            if (!triggeredSpawnerBlockEntity.setEntitySpawnOrientationPitch(entitySpawnOrientationPitch)) {
//                player.sendMessage(Text.translatable("triggered_spawner_block.entitySpawnOrientationPitch.invalid"), false);
//                updateSuccessful = false;
//            }
            if (!triggeredSpawnerBlockEntity.setSpawningMode(spawningMode)) {
                player.sendMessage(Text.translatable("triggered_spawner_block.spawningMode.invalid"), false);
                updateSuccessful = false;
            }
            if (!triggeredSpawnerBlockEntity.setEntityType(entityTypeId)) {
                player.sendMessage(Text.translatable("triggered_spawner_block.entityTypeId.invalid"), false);
                updateSuccessful = false;
            }
            if (updateSuccessful) {
                player.sendMessage(Text.translatable("triggered_spawner_block.update_successful"), true);
            }
            triggeredSpawnerBlockEntity.markDirty();
            world.updateListeners(triggeredSpawnerBlockPosition, blockState, blockState, Block.NOTIFY_ALL);
        }
    }
}
