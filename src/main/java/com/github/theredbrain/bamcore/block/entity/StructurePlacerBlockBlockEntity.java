package com.github.theredbrain.bamcore.block.entity;

import com.github.theredbrain.bamcore.block.Triggerable;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class StructurePlacerBlockBlockEntity extends BlockEntity implements Triggerable {
    private String placedStructureIdentifier = "minecraft:air";
    private BlockPos placementPositionOffset = new BlockPos(0, 1, 0);
    private BlockPos triggeredBlockPositionOffset = new BlockPos(0, 0, 0);
    public StructurePlacerBlockBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.STRUCTURE_PLACER_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putString("placedStructureIdentifier", this.placedStructureIdentifier);

        nbt.putInt("placementPositionOffsetX", this.placementPositionOffset.getX());
        nbt.putInt("placementPositionOffsetY", this.placementPositionOffset.getY());
        nbt.putInt("placementPositionOffsetZ", this.placementPositionOffset.getZ());

        nbt.putInt("triggeredBlockPositionOffsetX", this.triggeredBlockPositionOffset.getX());
        nbt.putInt("triggeredBlockPositionOffsetY", this.triggeredBlockPositionOffset.getY());
        nbt.putInt("triggeredBlockPositionOffsetZ", this.triggeredBlockPositionOffset.getZ());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.placedStructureIdentifier = nbt.getString("placedStructureIdentifier");

        int i = MathHelper.clamp(nbt.getInt("placementPositionOffsetX"), -48, 48);
        int j = MathHelper.clamp(nbt.getInt("placementPositionOffsetY"), -48, 48);
        int k = MathHelper.clamp(nbt.getInt("placementPositionOffsetZ"), -48, 48);
        this.placementPositionOffset = new BlockPos(i, j, k);

        int l = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetX"), -48, 48);
        int m = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetY"), -48, 48);
        int n = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetZ"), -48, 48);
        this.triggeredBlockPositionOffset = new BlockPos(l, m, n);
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
            ((DuckPlayerEntityMixin)player).bamcore$openStructurePlacerBlockScreen(this);
        }
        return true;
    }

    public String getPlacedStructureIdentifier() {
        return placedStructureIdentifier;
    }

    // TODO check if input is valid
    public boolean setPlacedStructureIdentifier(String placedStructureIdentifier) {
        this.placedStructureIdentifier = placedStructureIdentifier;
        return true;
    }

    public BlockPos getPlacementPositionOffset() {
        return placementPositionOffset;
    }

    // TODO check if input is valid
    public boolean setPlacementPositionOffset(BlockPos placementPositionOffset) {
        this.placementPositionOffset = placementPositionOffset;
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
    public void trigger() {
        if (this.world != null) {
            MinecraftServer server = world.getServer();
            if (server != null) {
//                RPGMod.LOGGER.info("dimension to place structure in: " + world.getRegistryKey().getValue().toString());
                String currentDimension = world.getRegistryKey().getValue().toString();
                String command = "execute in " + currentDimension + " run place structure " + this.placedStructureIdentifier + " " + (this.pos.getX() + this.placementPositionOffset.getX()) + " " + (this.pos.getY() + this.placementPositionOffset.getY()) + " " + (this.pos.getZ() + this.placementPositionOffset.getZ());
//                RPGMod.LOGGER.info(command);
                server.getCommandManager().executeWithPrefix(server.getCommandSource(), command);
            }

            // trigger next block
            BlockEntity blockEntity = world.getBlockEntity(new BlockPos(this.pos.getX() + this.triggeredBlockPositionOffset.getX(), this.pos.getY() + this.triggeredBlockPositionOffset.getY(), this.pos.getZ() + this.triggeredBlockPositionOffset.getZ()));
            if (blockEntity instanceof Triggerable triggerable && blockEntity != this) {
                triggerable.trigger();
            }
        }
    }
}
