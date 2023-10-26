package com.github.theredbrain.bamcore.block.entity;

import com.github.theredbrain.bamcore.block.Triggerable;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class DelayTriggerBlockBlockEntity extends BlockEntity implements Triggerable {
    private BlockPos triggeredBlockPositionOffset = new BlockPos(0, 1, 0);

    private int triggerDelay = 0;

    private boolean ticking = false;
    private int remainingTicks = 0;
    public DelayTriggerBlockBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.DELAY_TRIGGER_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.putInt("triggeredBlockPositionOffsetX", this.triggeredBlockPositionOffset.getX());
        nbt.putInt("triggeredBlockPositionOffsetY", this.triggeredBlockPositionOffset.getY());
        nbt.putInt("triggeredBlockPositionOffsetZ", this.triggeredBlockPositionOffset.getZ());

        nbt.putInt("triggerDelay", this.triggerDelay);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        int l = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetX"), -48, 48);
        int m = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetY"), -48, 48);
        int n = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetZ"), -48, 48);
        this.triggeredBlockPositionOffset = new BlockPos(l, m, n);

        this.triggerDelay = nbt.getInt("triggerDelay");
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }

    public static void tick(World world, BlockPos pos, BlockState state, DelayTriggerBlockBlockEntity blockEntity) {

        if (!world.isClient && blockEntity.ticking) {
            blockEntity.remainingTicks--;
//            RPGMod.LOGGER.info("tick");
            if (blockEntity.remainingTicks <= 0) {
                blockEntity.ticking = false;
                blockEntity.remainingTicks = 0;
                blockEntity.triggerTriggeredBlock();
            }
        }
    }

    public boolean openScreen(PlayerEntity player) {
        if (!player.isCreativeLevelTwoOp()) {
            return false;
        }
        if (player.getEntityWorld().isClient) {
            ((DuckPlayerEntityMixin)player).bamcore$openDelayTriggerBlockScreen(this);
        }
        return true;
    }

    public void trigger() {
        if (this.world != null) {
//            RPGMod.LOGGER.info("set ticking to true");
            this.ticking = true;
            this.remainingTicks = this.triggerDelay;
        }
    }

    public void triggerTriggeredBlock() {
        if (this.world != null) {
            BlockEntity blockEntity = world.getBlockEntity(new BlockPos(this.pos.getX() + this.triggeredBlockPositionOffset.getX(), this.pos.getY() + this.triggeredBlockPositionOffset.getY(), this.pos.getZ() + this.triggeredBlockPositionOffset.getZ()));
            if (blockEntity instanceof Triggerable triggerable) {
                triggerable.trigger();
            }
        }
    }

    public BlockPos getTriggeredBlockPositionOffset() {
        return triggeredBlockPositionOffset;
    }

    // TODO check if input is valid
    public boolean setTriggeredBlockPositionOffset(BlockPos triggeredBlockPositionOffset) {
        this.triggeredBlockPositionOffset = triggeredBlockPositionOffset;
        return true;
    }

    public int getTriggerDelay() {
        return triggerDelay;
    }

    // TODO check if input is valid
    public boolean setTriggerDelay(int triggerDelay) {
        this.triggerDelay = triggerDelay;
        return true;
    }
}
