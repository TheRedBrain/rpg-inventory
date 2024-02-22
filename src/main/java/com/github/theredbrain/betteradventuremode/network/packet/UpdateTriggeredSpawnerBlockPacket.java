package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.util.PacketByteBufUtils;
import com.github.theredbrain.betteradventuremode.block.entity.TriggeredSpawnerBlockEntity;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.List;

public class UpdateTriggeredSpawnerBlockPacket implements FabricPacket {
    public static final PacketType<UpdateTriggeredSpawnerBlockPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("update_triggered_spawner_block"),
            UpdateTriggeredSpawnerBlockPacket::new
    );

    public final BlockPos triggeredSpawnerBlockPosition;
    public final String spawnerBoundEntityName;
    public final String spawnerBoundEntityModelIdentifier;
    public final String spawnerBoundEntityTextureIdentifier;
    public final String spawnerBoundEntityAnimationsIdentifier;
    public final float spawnerBoundEntityBoundingBoxHeight;
    public final float spawnerBoundEntityBoundingBoxWidth;
    public final String spawnerBoundEntityLootTableIdentifier;
    public final BlockPos entitySpawnPositionOffset;
    public final TriggeredSpawnerBlockEntity.SpawningMode spawningMode;
    public final TriggeredSpawnerBlockEntity.EntityMode entityMode;
    public final String entityTypeId;
    public final List<MutablePair<String, EntityAttributeModifier>> entityAttributeModifiersList;
    public final BlockPos triggeredBlockPositionOffset;
    public final BlockPos useRelayBlockPositionOffset;


    public UpdateTriggeredSpawnerBlockPacket(BlockPos triggeredSpawnerBlockPosition, String spawnerBoundEntityName, String spawnerBoundEntityModelIdentifier, String spawnerBoundEntityTextureIdentifier, String spawnerBoundEntityAnimationsIdentifier, float spawnerBoundEntityBoundingBoxHeight, float spawnerBoundEntityBoundingBoxWidth, String spawnerBoundEntityLootTableIdentifier, BlockPos entitySpawnPositionOffset, String spawningMode, String entityMode, String entityTypeId, List<MutablePair<String, EntityAttributeModifier>> entityAttributeModifiersList, BlockPos triggeredBlockPositionOffset, BlockPos useRelayBlockPositionOffset) {
        this.triggeredSpawnerBlockPosition = triggeredSpawnerBlockPosition;

        this.spawnerBoundEntityName = spawnerBoundEntityName;
        this.spawnerBoundEntityModelIdentifier = spawnerBoundEntityModelIdentifier;
        this.spawnerBoundEntityTextureIdentifier = spawnerBoundEntityTextureIdentifier;
        this.spawnerBoundEntityAnimationsIdentifier = spawnerBoundEntityAnimationsIdentifier;
        this.spawnerBoundEntityBoundingBoxHeight = spawnerBoundEntityBoundingBoxHeight;
        this.spawnerBoundEntityBoundingBoxWidth = spawnerBoundEntityBoundingBoxWidth;
        this.spawnerBoundEntityLootTableIdentifier = spawnerBoundEntityLootTableIdentifier;

        this.entitySpawnPositionOffset = entitySpawnPositionOffset;

        this.spawningMode = TriggeredSpawnerBlockEntity.SpawningMode.byName(spawningMode).orElseGet(() -> TriggeredSpawnerBlockEntity.SpawningMode.ONCE);
        this.entityMode = TriggeredSpawnerBlockEntity.EntityMode.byName(entityMode).orElseGet(() -> TriggeredSpawnerBlockEntity.EntityMode.IDENTIFIER);

        this.entityTypeId = entityTypeId;

        this.entityAttributeModifiersList = entityAttributeModifiersList;

        this.triggeredBlockPositionOffset = triggeredBlockPositionOffset;

        this.useRelayBlockPositionOffset = useRelayBlockPositionOffset;
    }

    public UpdateTriggeredSpawnerBlockPacket(PacketByteBuf buf) {
        this(
                buf.readBlockPos(),
                buf.readString(),
                buf.readString(),
                buf.readString(),
                buf.readString(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readString(),
                buf.readBlockPos(),
                buf.readString(),
                buf.readString(),
                buf.readString(),
                buf.readList(new PacketByteBufUtils.MutablePairStringEntityAttributeModifierReader()),
                buf.readBlockPos(),
                buf.readBlockPos()
        );
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.triggeredSpawnerBlockPosition);

        buf.writeString(this.spawnerBoundEntityName);
        buf.writeString(this.spawnerBoundEntityModelIdentifier);
        buf.writeString(this.spawnerBoundEntityTextureIdentifier);
        buf.writeString(this.spawnerBoundEntityAnimationsIdentifier);
        buf.writeFloat(this.spawnerBoundEntityBoundingBoxHeight);
        buf.writeFloat(this.spawnerBoundEntityBoundingBoxWidth);
        buf.writeString(this.spawnerBoundEntityLootTableIdentifier);

        buf.writeBlockPos(this.entitySpawnPositionOffset);
        
        buf.writeString(this.spawningMode.asString());
        buf.writeString(this.entityMode.asString());

        buf.writeString(this.entityTypeId);

        buf.writeCollection(this.entityAttributeModifiersList, new PacketByteBufUtils.MutablePairStringEntityAttributeModifierWriter());

        buf.writeBlockPos(this.triggeredBlockPositionOffset);

        buf.writeBlockPos(this.useRelayBlockPositionOffset);
    }
}
