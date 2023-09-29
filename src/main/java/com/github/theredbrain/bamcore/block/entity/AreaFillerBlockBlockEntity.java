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
import net.minecraft.util.math.Vec3i;

public class AreaFillerBlockBlockEntity extends BlockEntity implements Triggerable {
    private String fillerBlockIdentifier = "minecraft:air";
    private Vec3i filledAreaDimensions = Vec3i.ZERO;
    private BlockPos filledAreaPositionOffset = new BlockPos(0, 1, 0);
    private BlockPos triggeredBlockPositionOffset = new BlockPos(0, 0, 0);
    public AreaFillerBlockBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.AREA_FILLER_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putString("fillerBlockIdentifier", this.fillerBlockIdentifier);

        nbt.putInt("filledAreaDimensionsX", this.filledAreaDimensions.getX());
        nbt.putInt("filledAreaDimensionsY", this.filledAreaDimensions.getY());
        nbt.putInt("filledAreaDimensionsZ", this.filledAreaDimensions.getZ());

        nbt.putInt("filledAreaPositionOffsetX", this.filledAreaPositionOffset.getX());
        nbt.putInt("filledAreaPositionOffsetY", this.filledAreaPositionOffset.getY());
        nbt.putInt("filledAreaPositionOffsetZ", this.filledAreaPositionOffset.getZ());

        nbt.putInt("triggeredBlockPositionOffsetX", this.triggeredBlockPositionOffset.getX());
        nbt.putInt("triggeredBlockPositionOffsetY", this.triggeredBlockPositionOffset.getY());
        nbt.putInt("triggeredBlockPositionOffsetZ", this.triggeredBlockPositionOffset.getZ());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.fillerBlockIdentifier = nbt.getString("fillerBlockIdentifier");

        int i = MathHelper.clamp(nbt.getInt("filledAreaDimensionsX"), 0, 48);
        int j = MathHelper.clamp(nbt.getInt("filledAreaDimensionsY"), 0, 48);
        int k = MathHelper.clamp(nbt.getInt("filledAreaDimensionsZ"), 0, 48);
        this.filledAreaDimensions = new Vec3i(i, j, k);

        int l = MathHelper.clamp(nbt.getInt("filledAreaPositionOffsetX"), -48, 48);
        int m = MathHelper.clamp(nbt.getInt("filledAreaPositionOffsetY"), -48, 48);
        int n = MathHelper.clamp(nbt.getInt("filledAreaPositionOffsetZ"), -48, 48);
        this.filledAreaPositionOffset = new BlockPos(l, m, n);

        int o = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetX"), -48, 48);
        int p = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetY"), -48, 48);
        int q = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetZ"), -48, 48);
        this.triggeredBlockPositionOffset = new BlockPos(o, p, q);
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
            ((DuckPlayerEntityMixin)player).bamcore$openAreaFillerBlockScreen(this);
        }
        return true;
    }

    public String getFillerBlockIdentifier() {
        return fillerBlockIdentifier;
    }

    // TODO check if input is valid
    public boolean setFillerBlockIdentifier(String fillerBlockIdentifier) {
        this.fillerBlockIdentifier = fillerBlockIdentifier;
        return true;
    }

    public Vec3i getFilledAreaDimensions() {
        return filledAreaDimensions;
    }

    // TODO check if input is valid
    public boolean setFilledAreaDimensions(Vec3i filledAreaDimensions) {
        this.filledAreaDimensions = filledAreaDimensions;
        return true;
    }

    public BlockPos getFilledAreaPositionOffset() {
        return filledAreaPositionOffset;
    }

    // TODO check if input is valid
    public boolean setFilledAreaPositionOffset(BlockPos filledAreaPositionOffset) {
        this.filledAreaPositionOffset = filledAreaPositionOffset;
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
//            RPGMod.LOGGER.info("dimension to fill area in: " + world.getRegistryKey().getValue().toString());
            String currentDimension = world.getRegistryKey().getValue().toString();
            BlockPos startPos = new BlockPos(pos.getX() + this.filledAreaPositionOffset.getX(), pos.getY() + this.filledAreaPositionOffset.getY(), pos.getZ() + this.filledAreaPositionOffset.getZ());
            if (server != null) {
                String command = "execute in " + currentDimension + " run fill " + startPos.getX() + " " + startPos.getY() + " " + startPos.getZ() + " " + (startPos.getX() + this.filledAreaDimensions.getX()) + " " + (startPos.getY() + this.filledAreaDimensions.getY()) + " " + (startPos.getZ() + this.filledAreaDimensions.getZ()) + " " + this.fillerBlockIdentifier + " replace";
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
