package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.block.entity.TriggeredSpawnerBlockEntity;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class UpdateTriggeredSpawnerBlockPacket implements FabricPacket {
    public static final PacketType<UpdateTriggeredSpawnerBlockPacket> TYPE = PacketType.create(
            BetterAdventureModeCore.identifier("update_triggered_spawner_block"),
            UpdateTriggeredSpawnerBlockPacket::new
    );

    public final BlockPos triggeredSpawnerBlockPosition;
    public final BlockPos entitySpawnPositionOffset;
    public final double entitySpawnOrientationYaw;
    public final double entitySpawnOrientationPitch;
    public final TriggeredSpawnerBlockEntity.SpawningMode spawningMode;
    public final String entityTypeId;


    public UpdateTriggeredSpawnerBlockPacket(BlockPos triggeredSpawnerBlockPosition, BlockPos entitySpawnPositionOffset, double entitySpawnOrientationYaw, double entitySpawnOrientationPitch, String spawningMode, String entityTypeId) {
        this.triggeredSpawnerBlockPosition = triggeredSpawnerBlockPosition;

        this.entitySpawnPositionOffset = entitySpawnPositionOffset;

        this.entitySpawnOrientationYaw = entitySpawnOrientationYaw;
        this.entitySpawnOrientationPitch = entitySpawnOrientationPitch;

        this.spawningMode = TriggeredSpawnerBlockEntity.SpawningMode.byName(spawningMode).orElseGet(() -> TriggeredSpawnerBlockEntity.SpawningMode.TRIGGERED);

        this.entityTypeId = entityTypeId;
    }

    public UpdateTriggeredSpawnerBlockPacket(PacketByteBuf buf) {
        this(
                buf.readBlockPos(),
                buf.readBlockPos(),
                buf.readDouble(),
                buf.readDouble(),
                buf.readString(),
                buf.readString()
        );
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.triggeredSpawnerBlockPosition);
        buf.writeBlockPos(this.entitySpawnPositionOffset);

        buf.writeDouble(this.entitySpawnOrientationYaw);
        buf.writeDouble(this.entitySpawnOrientationPitch);

        buf.writeString(this.spawningMode.asString());

        buf.writeString(this.entityTypeId);
    }
}
