package com.github.theredbrain.betteradventuremode.block.entity;

import com.github.theredbrain.betteradventuremode.util.BlockRotationUtils;
import com.github.theredbrain.betteradventuremode.block.RotatedBlockWithEntity;
import com.github.theredbrain.betteradventuremode.block.Triggerable;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.registry.EntityRegistry;
import com.github.theredbrain.betteradventuremode.structure.pool.FixedRotationStructurePoolBasedGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.block.JigsawBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.JigsawBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

public class JigsawPlacerBlockBlockEntity extends RotatedBlockEntity implements Triggerable {
    public static final String TARGET_KEY = "target";
    public static final String POOL_KEY = "pool";
    public static final String JOINT_KEY = "joint";
    public static final String FINAL_STATE_KEY = "final_state";
    private Identifier target = new Identifier("empty");
    private RegistryKey<StructurePool> pool = RegistryKey.of(RegistryKeys.TEMPLATE_POOL, new Identifier("empty"));
    private JigsawBlockEntity.Joint joint = JigsawBlockEntity.Joint.ROLLABLE;
    private String finalState = "minecraft:air";
    private BlockPos triggeredBlockPositionOffset = new BlockPos(0, 0, 0);
    public JigsawPlacerBlockBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.STRUCTURE_PLACER_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putString(TARGET_KEY, this.target.toString());
        nbt.putString(POOL_KEY, this.pool.getValue().toString());
        nbt.putString(FINAL_STATE_KEY, this.finalState);
        nbt.putString(JOINT_KEY, this.joint.asString());

        nbt.putInt("triggeredBlockPositionOffsetX", this.triggeredBlockPositionOffset.getX());
        nbt.putInt("triggeredBlockPositionOffsetY", this.triggeredBlockPositionOffset.getY());
        nbt.putInt("triggeredBlockPositionOffsetZ", this.triggeredBlockPositionOffset.getZ());

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.target = new Identifier(nbt.getString(TARGET_KEY));
        this.pool = RegistryKey.of(RegistryKeys.TEMPLATE_POOL, new Identifier(nbt.getString(POOL_KEY)));
        this.finalState = nbt.getString(FINAL_STATE_KEY);
        this.joint = JigsawBlockEntity.Joint.byName(nbt.getString(JOINT_KEY)).orElseGet(() -> JigsawBlock.getFacing(this.getCachedState()).getAxis().isHorizontal() ? JigsawBlockEntity.Joint.ALIGNED : JigsawBlockEntity.Joint.ROLLABLE);

        int l = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetX"), -48, 48);
        int m = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetY"), -48, 48);
        int n = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetZ"), -48, 48);
        this.triggeredBlockPositionOffset = new BlockPos(l, m, n);

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
            ((DuckPlayerEntityMixin)player).betteradventuremode$openJigsawPlacerBlockScreen(this);
        }
        return true;
    }

    public Identifier getTarget() {
        return this.target;
    }

    public RegistryKey<StructurePool> getPool() {
        return this.pool;
    }

    public JigsawBlockEntity.Joint getJoint() {
        return this.joint;
    }

    public boolean setTarget(String target) {
        if (Identifier.isValid(target)) {
            this.target = new Identifier(target);
            return true;
        }
        return false;
    }

    public boolean setPool(String pool) {
        if (Identifier.isValid(pool)) {
            this.pool = RegistryKey.of(RegistryKeys.TEMPLATE_POOL, new Identifier(pool));
            return true;
        }
        return false;
    }

    public boolean setJoint(JigsawBlockEntity.Joint joint) {
        this.joint = joint;
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
            if (this.world instanceof ServerWorld serverWorld) {
                BlockPos blockPos = this.getPos().offset(this.getCachedState().get(JigsawBlock.ORIENTATION).getFacing());
                Registry<StructurePool> registry = world.getRegistryManager().get(RegistryKeys.TEMPLATE_POOL);
                RegistryEntry.Reference<StructurePool> registryEntry = registry.entryOf(this.pool);

                Direction rotation = this.getCachedState().get(JigsawBlock.ORIENTATION).getRotation();
                Direction facing = this.getCachedState().get(JigsawBlock.ORIENTATION).getFacing();

                FixedRotationStructurePoolBasedGenerator.generate(serverWorld, registryEntry, this.target, 7, /*blockPos*/new BlockPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ()), false, facing == Direction.EAST ? BlockRotation.CLOCKWISE_90 : facing == Direction.SOUTH ? BlockRotation.CLOCKWISE_180 : facing == Direction.WEST ? BlockRotation.COUNTERCLOCKWISE_90 : facing == Direction.NORTH ? BlockRotation.NONE : rotation == Direction.EAST ? BlockRotation.CLOCKWISE_90 : rotation == Direction.SOUTH ? BlockRotation.CLOCKWISE_180 : rotation == Direction.WEST ? BlockRotation.COUNTERCLOCKWISE_90 : BlockRotation.NONE);
            }
            // trigger next block
            BlockEntity blockEntity = world.getBlockEntity(new BlockPos(this.pos.getX() + this.triggeredBlockPositionOffset.getX(), this.pos.getY() + this.triggeredBlockPositionOffset.getY(), this.pos.getZ() + this.triggeredBlockPositionOffset.getZ()));
            if (blockEntity instanceof Triggerable triggerable && blockEntity != this) {
                triggerable.trigger();
            }
        }
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
