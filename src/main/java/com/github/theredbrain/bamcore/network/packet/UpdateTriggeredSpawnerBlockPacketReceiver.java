package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.block.entity.TriggeredSpawnerBlockEntity;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class UpdateTriggeredSpawnerBlockPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UpdateTriggeredSpawnerBlockPacket> {
    @Override
    public void receive(UpdateTriggeredSpawnerBlockPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos triggeredSpawnerBlockPosition = packet.triggeredSpawnerBlockPosition;

        String spawnerBoundEntityName = packet.spawnerBoundEntityName;
        String spawnerBoundEntityModelIdentifier = packet.spawnerBoundEntityModelIdentifier;
        String spawnerBoundEntityTextureIdentifier = packet.spawnerBoundEntityTextureIdentifier;
        String spawnerBoundEntityAnimationsIdentifier = packet.spawnerBoundEntityAnimationsIdentifier;
        double spawnerBoundEntityBoundingBoxHeight = packet.spawnerBoundEntityBoundingBoxHeight;
        double spawnerBoundEntityBoundingBoxWidth = packet.spawnerBoundEntityBoundingBoxWidth;
        String spawnerBoundEntityLootTableIdentifier = packet.spawnerBoundEntityLootTableIdentifier;

        BlockPos entitySpawnPositionOffset = packet.entitySpawnPositionOffset;

        TriggeredSpawnerBlockEntity.SpawningMode spawningMode = packet.spawningMode;
        TriggeredSpawnerBlockEntity.EntityMode entityMode = packet.entityMode;

        String entityTypeId = "";
        if (entityMode == TriggeredSpawnerBlockEntity.EntityMode.IDENTIFIER) {
            entityTypeId = packet.entityTypeId;
        } else if (entityMode == TriggeredSpawnerBlockEntity.EntityMode.SPAWNER_BOUND_ENTITY) {
            entityTypeId = "bamcore:spawner_bound_entity";
        }

        List<MutablePair<String, EntityAttributeModifier>> entityAttributeModifiersList = packet.entityAttributeModifiersList;
        Multimap<EntityAttribute, EntityAttributeModifier> entityAttributeModifiers = Multimaps.newMultimap(Maps.newLinkedHashMap(), ArrayList::new);
        for (MutablePair<String, EntityAttributeModifier> entityAttributeModifiersListEntry : entityAttributeModifiersList) {
            Optional<EntityAttribute> optional = Registries.ATTRIBUTE.getOrEmpty(Identifier.tryParse(entityAttributeModifiersListEntry.getLeft()));
            optional.ifPresent(entityAttribute -> entityAttributeModifiers.put(entityAttribute, entityAttributeModifiersListEntry.getRight()));
        }

        BlockPos triggeredBlockPositionOffset = packet.triggeredBlockPositionOffset;

        World world = player.getWorld();

        boolean updateSuccessful = true;

        BlockEntity blockEntity = world.getBlockEntity(triggeredSpawnerBlockPosition);
        BlockState blockState = world.getBlockState(triggeredSpawnerBlockPosition);

        if (blockEntity instanceof TriggeredSpawnerBlockEntity triggeredSpawnerBlockEntity) {
            if (!triggeredSpawnerBlockEntity.setSpawnerBoundEntityName(spawnerBoundEntityName)) {
                player.sendMessage(Text.translatable("triggered_spawner_block.spawnerBoundEntityName.invalid"), false);
                updateSuccessful = false;
            }
            if (!triggeredSpawnerBlockEntity.setSpawnerBoundEntityModelIdentifier(spawnerBoundEntityModelIdentifier)) {
                player.sendMessage(Text.translatable("triggered_spawner_block.spawnerBoundEntityModelIdentifier.invalid"), false);
                updateSuccessful = false;
            }
            if (!triggeredSpawnerBlockEntity.setSpawnerBoundEntityTextureIdentifier(spawnerBoundEntityTextureIdentifier)) {
                player.sendMessage(Text.translatable("triggered_spawner_block.spawnerBoundEntityTextureIdentifier.invalid"), false);
                updateSuccessful = false;
            }
            if (!triggeredSpawnerBlockEntity.setSpawnerBoundEntityAnimationsIdentifier(spawnerBoundEntityAnimationsIdentifier)) {
                player.sendMessage(Text.translatable("triggered_spawner_block.spawnerBoundEntityAnimationsIdentifier.invalid"), false);
                updateSuccessful = false;
            }
            if (!triggeredSpawnerBlockEntity.setSpawnerBoundEntityBoundingBoxHeight(spawnerBoundEntityBoundingBoxHeight)) {
                player.sendMessage(Text.translatable("triggered_spawner_block.spawnerBoundEntityBoundingBoxHeight.invalid"), false);
                updateSuccessful = false;
            }
            if (!triggeredSpawnerBlockEntity.setSpawnerBoundEntityBoundingBoxWidth(spawnerBoundEntityBoundingBoxWidth)) {
                player.sendMessage(Text.translatable("triggered_spawner_block.spawnerBoundEntityBoundingBoxWidth.invalid"), false);
                updateSuccessful = false;
            }
            if (!triggeredSpawnerBlockEntity.setSpawnerBoundEntityLootTableIdentifier(spawnerBoundEntityLootTableIdentifier)) {
                player.sendMessage(Text.translatable("triggered_spawner_block.spawnerBoundEntityLootTableIdentifier.invalid"), false);
                updateSuccessful = false;
            }
            if (!triggeredSpawnerBlockEntity.setEntitySpawnPositionOffset(entitySpawnPositionOffset)) {
                player.sendMessage(Text.translatable("triggered_spawner_block.entitySpawnPositionOffset.invalid"), false);
                updateSuccessful = false;
            }
            if (!triggeredSpawnerBlockEntity.setSpawningMode(spawningMode)) {
                player.sendMessage(Text.translatable("triggered_spawner_block.spawningMode.invalid"), false);
                updateSuccessful = false;
            }
            if (!triggeredSpawnerBlockEntity.setEntityMode(entityMode)) {
                player.sendMessage(Text.translatable("triggered_spawner_block.entityMode.invalid"), false);
                updateSuccessful = false;
            }
            if (!triggeredSpawnerBlockEntity.setEntityType(entityTypeId)) {
                player.sendMessage(Text.translatable("triggered_spawner_block.entityTypeId.invalid"), false);
                updateSuccessful = false;
            }
            if (!triggeredSpawnerBlockEntity.setEntityAttributeModifiers(entityAttributeModifiers)) {
                player.sendMessage(Text.translatable("triggered_spawner_block.entityAttributeModifiers.invalid"), false);
                updateSuccessful = false;
            }
            if (!triggeredSpawnerBlockEntity.setTriggeredBlockPositionOffset(triggeredBlockPositionOffset)) {
                player.sendMessage(Text.translatable("triggered_block.triggeredBlockPositionOffset.invalid"), false);
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
