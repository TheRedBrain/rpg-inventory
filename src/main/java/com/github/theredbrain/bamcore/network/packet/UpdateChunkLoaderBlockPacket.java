package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class UpdateChunkLoaderBlockPacket implements FabricPacket {
    public static final PacketType<UpdateChunkLoaderBlockPacket> TYPE = PacketType.create(
            BetterAdventureModeCore.identifier("update_chunk_loader_block"),
            UpdateChunkLoaderBlockPacket::new
    );

    public final BlockPos chunkLoaderBlockPosition;
    public final boolean loadChunk;
    public final int startChunkX;
    public final int startChunkZ;
    public final int endChunkX;
    public final int endChunkZ;
    public final BlockPos triggeredBlockPositionOffset;

    public UpdateChunkLoaderBlockPacket(BlockPos chunkLoaderBlockPosition, boolean loadChunk, int startChunkX, int startChunkZ, int endChunkX, int endChunkZ, BlockPos triggeredBlockPositionOffset) {
        this.chunkLoaderBlockPosition = chunkLoaderBlockPosition;
        this.loadChunk = loadChunk;
        this.startChunkX = startChunkX;
        this.startChunkZ = startChunkZ;
        this.endChunkX = endChunkX;
        this.endChunkZ = endChunkZ;
        this.triggeredBlockPositionOffset = triggeredBlockPositionOffset;
    }

    public UpdateChunkLoaderBlockPacket(PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readBoolean(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readBlockPos());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.chunkLoaderBlockPosition);
        buf.writeBoolean(this.loadChunk);
        buf.writeInt(this.startChunkX);
        buf.writeInt(this.startChunkZ);
        buf.writeInt(this.endChunkX);
        buf.writeInt(this.endChunkZ);
        buf.writeBlockPos(this.triggeredBlockPositionOffset);
    }

}
