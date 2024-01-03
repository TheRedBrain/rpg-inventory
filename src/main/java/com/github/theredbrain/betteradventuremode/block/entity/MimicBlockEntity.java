package com.github.theredbrain.betteradventuremode.block.entity;

import com.github.theredbrain.betteradventuremode.api.util.BlockRotationUtils;
import com.github.theredbrain.betteradventuremode.block.MimicBlock;
import com.github.theredbrain.betteradventuremode.block.Resetable;
import com.github.theredbrain.betteradventuremode.block.RotatedBlockWithEntity;
import com.github.theredbrain.betteradventuremode.block.Triggerable;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.registry.BlockRegistry;
import com.github.theredbrain.betteradventuremode.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class MimicBlockEntity extends RotatedBlockEntity implements Triggerable, Resetable {
    private BlockPos activeMimicBlockPositionOffset = new BlockPos(0, 1, 0);
    private BlockPos inactiveMimicBlockPositionOffset = new BlockPos(0, -1, 0);

    public MimicBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.MIMIC_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {

        nbt.putInt("activeMimicBlockPositionOffsetX", this.activeMimicBlockPositionOffset.getX());
        nbt.putInt("activeMimicBlockPositionOffsetY", this.activeMimicBlockPositionOffset.getY());
        nbt.putInt("activeMimicBlockPositionOffsetZ", this.activeMimicBlockPositionOffset.getZ());

        nbt.putInt("inactiveMimicBlockPositionOffsetX", this.inactiveMimicBlockPositionOffset.getX());
        nbt.putInt("inactiveMimicBlockPositionOffsetY", this.inactiveMimicBlockPositionOffset.getY());
        nbt.putInt("inactiveMimicBlockPositionOffsetZ", this.inactiveMimicBlockPositionOffset.getZ());

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {

        int x = MathHelper.clamp(nbt.getInt("activeMimicBlockPositionOffsetX"), -48, 48);
        int y = MathHelper.clamp(nbt.getInt("activeMimicBlockPositionOffsetY"), -48, 48);
        int z = MathHelper.clamp(nbt.getInt("activeMimicBlockPositionOffsetZ"), -48, 48);
        this.activeMimicBlockPositionOffset = new BlockPos(x, y, z);

        x = MathHelper.clamp(nbt.getInt("inactiveMimicBlockPositionOffsetX"), -48, 48);
        y = MathHelper.clamp(nbt.getInt("inactiveMimicBlockPositionOffsetY"), -48, 48);
        z = MathHelper.clamp(nbt.getInt("inactiveMimicBlockPositionOffsetZ"), -48, 48);
        this.inactiveMimicBlockPositionOffset = new BlockPos(x, y, z);

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
            ((DuckPlayerEntityMixin)player).betteradventuremode$openMimicBlockScreen(this);
        }
        return true;
    }

    public BlockPos getActiveMimicBlockPositionOffset() {
        return activeMimicBlockPositionOffset;
    }

    public boolean setActiveMimicBlockPositionOffset(BlockPos activeMimicBlockPositionOffset) {
        if (activeMimicBlockPositionOffset.getX() == 0 && activeMimicBlockPositionOffset.getY() == 0 && activeMimicBlockPositionOffset.getZ() == 0) {
            return false;
        }
        this.activeMimicBlockPositionOffset = activeMimicBlockPositionOffset;
        return true;
    }

    public BlockPos getInactiveMimicBlockPositionOffset() {
        return inactiveMimicBlockPositionOffset;
    }

    public boolean setInactiveMimicBlockPositionOffset(BlockPos inactiveMimicBlockPositionOffset) {
        if (inactiveMimicBlockPositionOffset.getX() == 0 && inactiveMimicBlockPositionOffset.getY() == 0 && inactiveMimicBlockPositionOffset.getZ() == 0) {
            return false;
        }
        this.inactiveMimicBlockPositionOffset = inactiveMimicBlockPositionOffset;
        return true;
    }

    @Override
    protected void onRotate(BlockState state) {
        if (state.getBlock() instanceof RotatedBlockWithEntity) {
            if (state.get(RotatedBlockWithEntity.ROTATED) != this.rotated) {
                BlockRotation blockRotation = BlockRotationUtils.calculateRotationFromDifferentRotatedStates(state.get(RotatedBlockWithEntity.ROTATED), this.rotated);
                this.activeMimicBlockPositionOffset = BlockRotationUtils.rotateOffsetBlockPos(this.activeMimicBlockPositionOffset, blockRotation);
                this.inactiveMimicBlockPositionOffset = BlockRotationUtils.rotateOffsetBlockPos(this.inactiveMimicBlockPositionOffset, blockRotation);
                this.rotated = state.get(RotatedBlockWithEntity.ROTATED);
            }
            if (state.get(RotatedBlockWithEntity.X_MIRRORED) != this.x_mirrored) {
                this.activeMimicBlockPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.activeMimicBlockPositionOffset, BlockMirror.FRONT_BACK);
                this.inactiveMimicBlockPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.inactiveMimicBlockPositionOffset, BlockMirror.FRONT_BACK);
                this.x_mirrored = state.get(RotatedBlockWithEntity.X_MIRRORED);
            }
            if (state.get(RotatedBlockWithEntity.Z_MIRRORED) != this.z_mirrored) {
                this.activeMimicBlockPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.activeMimicBlockPositionOffset, BlockMirror.LEFT_RIGHT);
                this.inactiveMimicBlockPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.inactiveMimicBlockPositionOffset, BlockMirror.LEFT_RIGHT);
                this.z_mirrored = state.get(RotatedBlockWithEntity.Z_MIRRORED);
            }
        }
    }

    @Override
    public void reset() {
        if (this.world != null) {
            BlockEntity blockEntity = this.world.getBlockEntity(this.pos);
            if (blockEntity != null && blockEntity.getCachedState().getBlock() instanceof MimicBlock) {
                this.world.setBlockState(this.pos, blockEntity.getCachedState().with(MimicBlock.TRIGGERED, false));
            }
        }
    }

    @Override
    public void trigger() {
        if (this.world != null) {
            BlockEntity blockEntity = this.world.getBlockEntity(this.pos);
            if (blockEntity != null && blockEntity.getCachedState().getBlock() instanceof MimicBlock) {
                this.world.setBlockState(this.pos, blockEntity.getCachedState().with(MimicBlock.TRIGGERED, true));
            }
        }
    }

    public BlockState getCurrentMimicBlockState() {
        BlockState fallbackMimicBlockState = BlockRegistry.MIMIC_FALLBACK_BLOCK.getDefaultState();
        if (this.world != null && this.world.getBlockState(this.pos).isOf(BlockRegistry.MIMIC_BLOCK)) {
            BlockPos mimicBlockPos;
            if (this.world.getBlockState(this.pos).get(MimicBlock.TRIGGERED)) {
                mimicBlockPos = pos.add(this.activeMimicBlockPositionOffset.getX(), this.activeMimicBlockPositionOffset.getY(), this.activeMimicBlockPositionOffset.getZ());
            } else {
                mimicBlockPos = pos.add(this.inactiveMimicBlockPositionOffset.getX(), this.inactiveMimicBlockPositionOffset.getY(), this.inactiveMimicBlockPositionOffset.getZ());
            }
            BlockState mimicBlockState = world.getBlockState(mimicBlockPos);
            if (!mimicBlockState.isOf(BlockRegistry.MIMIC_BLOCK)) {
                return mimicBlockState;
            }
        }
        return fallbackMimicBlockState;
    }

    public boolean isDebugModeActive() {
        boolean debugRender = false;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            debugRender = player.isCreativeLevelTwoOp() && player.getInventory().getMainHandStack().isOf(BlockRegistry.MIMIC_BLOCK.asItem());
        }
        return debugRender;
    }
}
