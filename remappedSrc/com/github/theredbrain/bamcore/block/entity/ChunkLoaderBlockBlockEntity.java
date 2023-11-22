package com.github.theredbrain.bamcore.block.entity;

import com.github.theredbrain.bamcore.api.util.BlockRotationUtils;
import com.github.theredbrain.bamcore.block.RotatedBlockWithEntity;
import com.github.theredbrain.bamcore.block.Triggerable;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class ChunkLoaderBlockBlockEntity extends RotatedBlockEntity implements Triggerable {

    private int startChunkX = 0;
    private int startChunkZ = 0;
    private int endChunkX = 0;
    private int endChunkZ = 0;
    private boolean loadChunk = false;
    private BlockPos triggeredBlockPositionOffset = new BlockPos(0, 0, 0);

    public ChunkLoaderBlockBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.CHUNK_LOADER_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("startChunkX", this.startChunkX);
        nbt.putInt("startChunkZ", this.startChunkZ);
        nbt.putInt("endChunkX", this.endChunkX);
        nbt.putInt("endChunkZ", this.endChunkZ);

        nbt.putBoolean("loadChunk", this.loadChunk);

        nbt.putInt("triggeredBlockPositionOffsetX", this.triggeredBlockPositionOffset.getX());
        nbt.putInt("triggeredBlockPositionOffsetY", this.triggeredBlockPositionOffset.getY());
        nbt.putInt("triggeredBlockPositionOffsetZ", this.triggeredBlockPositionOffset.getZ());

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.startChunkX = nbt.getInt("startChunkX");
        this.startChunkZ = nbt.getInt("startChunkZ");
        this.endChunkX = nbt.getInt("endChunkX");
        this.endChunkZ = nbt.getInt("endChunkZ");

        this.loadChunk = nbt.getBoolean("loadChunk");

        int o = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetX"), -48, 48);
        int p = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetY"), -48, 48);
        int q = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetZ"), -48, 48);
        this.triggeredBlockPositionOffset = new BlockPos(o, p, q);

        super.readNbt(nbt);
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }

    public boolean openScreen(PlayerEntity player) {
        if (!player.isCreativeLevelTwoOp()) {
            return false;
        }
        if (player.getEntityWorld().isClient) {
            ((DuckPlayerEntityMixin)player).bamcore$openChunkLoaderBlockScreen(this);
        }
        return true;
    }

    @Override
    public void trigger() {
        if (this.world != null) {
            MinecraftServer server = world.getServer();
//            RPGMod.LOGGER.info("dimension to load chunks in: " + world.getRegistryKey().getValue().toString());
            String currentDimension = world.getRegistryKey().getValue().toString();
//            BlockPos startPos = new BlockPos(pos.getX() + this.filledAreaPositionOffset.getX(), pos.getY() + this.filledAreaPositionOffset.getY(), pos.getZ() + this.filledAreaPositionOffset.getZ());
            if (server != null) {

                if (this.loadChunk) {
                    // force load chunks
                    String forceLoadAddCommand = "execute in " + currentDimension + " run forceload add " + this.startChunkX + " " + this.startChunkZ + " " + this.endChunkX + " " + this.endChunkZ;
//                    RPGMod.LOGGER.info("forceload add command: " + forceLoadAddCommand);
                    server.getCommandManager().executeWithPrefix(server.getCommandSource(), forceLoadAddCommand);
                } else {
                    // force unload chunks
                    String forceLoadRemoveCommand = "execute in " + currentDimension + " run forceload remove " + this.startChunkX + " " + this.startChunkZ + " " + this.endChunkX + " " + this.endChunkZ;
//                    RPGMod.LOGGER.info("forceload remove command: " + forceLoadRemoveCommand);
                    server.getCommandManager().executeWithPrefix(server.getCommandSource(), forceLoadRemoveCommand);
                }
            }

            // trigger next block
            BlockEntity blockEntity = world.getBlockEntity(new BlockPos(this.pos.getX() + this.triggeredBlockPositionOffset.getX(), this.pos.getY() + this.triggeredBlockPositionOffset.getY(), this.pos.getZ() + this.triggeredBlockPositionOffset.getZ()));
            if (blockEntity instanceof Triggerable triggerable && blockEntity != this) {
                triggerable.trigger();
            }
        }
    }

    public int getStartChunkX() {
        return startChunkX;
    }

    // TODO check if input is valid
    public boolean setStartChunkX(int startChunkX) {
        this.startChunkX = startChunkX;
        return true;
    }

    public int getStartChunkZ() {
        return startChunkZ;
    }

    // TODO check if input is valid
    public boolean setStartChunkZ(int startChunkZ) {
        this.startChunkZ = startChunkZ;
        return true;
    }

    public int getEndChunkX() {
        return endChunkX;
    }

    // TODO check if input is valid
    public boolean setEndChunkX(int endChunkX) {
        this.endChunkX = endChunkX;
        return true;
    }

    public int getEndChunkZ() {
        return endChunkZ;
    }

    // TODO check if input is valid
    public boolean setEndChunkZ(int endChunkZ) {
        this.endChunkZ = endChunkZ;
        return true;
    }

    public boolean getLoadChunk() {
        return loadChunk;
    }

    // TODO check if input is valid
    public boolean setLoadChunk(boolean loadChunk) {
        this.loadChunk = loadChunk;
        return true;
    }

    public BlockPos getTriggeredBlockPositionOffset() {
        return triggeredBlockPositionOffset;
    }

    // TODO check if input is valid
    public boolean setTriggeredBlockPositionOffset(BlockPos triggeredBlockPositionOffset) {
        this.triggeredBlockPositionOffset = triggeredBlockPositionOffset;
        return true;
    }

    @Override
    protected void onRotate(BlockState state) {
        if (state.getBlock() instanceof RotatedBlockWithEntity) {
            if (state.get(RotatedBlockWithEntity.ROTATED) != this.rotated) {
                BlockRotation blockRotation = BlockRotationUtils.calculateRotationFromDifferentRotatedStates(state.get(RotatedBlockWithEntity.ROTATED), this.rotated);
                this.triggeredBlockPositionOffset = BlockRotationUtils.rotateOffsetBlockPos(this.triggeredBlockPositionOffset, blockRotation);
                this.rotated = state.get(RotatedBlockWithEntity.ROTATED);
            }
            if (state.get(RotatedBlockWithEntity.X_MIRRORED) != this.x_mirrored) {
                this.triggeredBlockPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.triggeredBlockPositionOffset, BlockMirror.FRONT_BACK);
                this.x_mirrored = state.get(RotatedBlockWithEntity.X_MIRRORED);
            }
            if (state.get(RotatedBlockWithEntity.Z_MIRRORED) != this.z_mirrored) {
                this.triggeredBlockPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.triggeredBlockPositionOffset, BlockMirror.LEFT_RIGHT);
                this.z_mirrored = state.get(RotatedBlockWithEntity.Z_MIRRORED);
            }
        }
    }
}
