package com.github.theredbrain.betteradventuremode.block.entity;

import com.github.theredbrain.betteradventuremode.util.BlockRotationUtils;
import com.github.theredbrain.betteradventuremode.util.UUIDUtilities;
import com.github.theredbrain.betteradventuremode.block.RotatedBlockWithEntity;
import com.github.theredbrain.betteradventuremode.block.Triggerable;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.registry.EntityRegistry;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.UUID;

public class TriggeredAdvancementCheckerBlockEntity extends RotatedBlockEntity implements Triggerable {
    private BlockPos firstTriggeredBlockPositionOffset = new BlockPos(0, 1, 0);
    private BlockPos secondTriggeredBlockPositionOffset = new BlockPos(0, -1, 0);
    private String checkedAdvancementIdentifier = "";

    public TriggeredAdvancementCheckerBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.TRIGGERED_ADVANCEMENT_CHECKER_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {

        nbt.putInt("firstTriggeredBlockPositionOffsetX", this.firstTriggeredBlockPositionOffset.getX());
        nbt.putInt("firstTriggeredBlockPositionOffsetY", this.firstTriggeredBlockPositionOffset.getY());
        nbt.putInt("firstTriggeredBlockPositionOffsetZ", this.firstTriggeredBlockPositionOffset.getZ());

        nbt.putInt("secondTriggeredBlockPositionOffsetX", this.secondTriggeredBlockPositionOffset.getX());
        nbt.putInt("secondTriggeredBlockPositionOffsetY", this.secondTriggeredBlockPositionOffset.getY());
        nbt.putInt("secondTriggeredBlockPositionOffsetZ", this.secondTriggeredBlockPositionOffset.getZ());

        nbt.putString("checkedAdvancementIdentifier", this.checkedAdvancementIdentifier);

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {

        int i = MathHelper.clamp(nbt.getInt("firstTriggeredBlockPositionOffsetX"), -48, 48);
        int j = MathHelper.clamp(nbt.getInt("firstTriggeredBlockPositionOffsetY"), -48, 48);
        int k = MathHelper.clamp(nbt.getInt("firstTriggeredBlockPositionOffsetZ"), -48, 48);
        this.firstTriggeredBlockPositionOffset = new BlockPos(i, j, k);

        i = MathHelper.clamp(nbt.getInt("secondTriggeredBlockPositionOffsetX"), -48, 48);
        j = MathHelper.clamp(nbt.getInt("secondTriggeredBlockPositionOffsetY"), -48, 48);
        k = MathHelper.clamp(nbt.getInt("secondTriggeredBlockPositionOffsetZ"), -48, 48);
        this.secondTriggeredBlockPositionOffset = new BlockPos(i, j, k);

        this.checkedAdvancementIdentifier = nbt.getString("checkedAdvancementIdentifier");

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
            ((DuckPlayerEntityMixin)player).betteradventuremode$openTriggeredAdvancementCheckerBlockScreen(this);
        }
        return true;
    }

    public BlockPos getFirstTriggeredBlockPositionOffset() {
        return this.firstTriggeredBlockPositionOffset;
    }

    // TODO check if input is valid
    public boolean setFirstTriggeredBlockPositionOffset(BlockPos firstTriggeredBlockPositionOffset) {
        this.firstTriggeredBlockPositionOffset = firstTriggeredBlockPositionOffset;
        return true;
    }

    public BlockPos getSecondTriggeredBlockPositionOffset() {
        return this.secondTriggeredBlockPositionOffset;
    }

    // TODO check if input is valid
    public boolean setSecondTriggeredBlockPositionOffset(BlockPos secondTriggeredBlockPositionOffset) {
        this.secondTriggeredBlockPositionOffset = secondTriggeredBlockPositionOffset;
        return true;
    }

    public String getCheckedAdvancementIdentifier() {
        return this.checkedAdvancementIdentifier;
    }

    public boolean setCheckedAdvancementIdentifier(String checkedAdvancementIdentifier) {
        this.checkedAdvancementIdentifier = checkedAdvancementIdentifier;
        return true;
    }

    @Override
    public void trigger() {
        if (this.world != null) {
            String worldName = this.world.getRegistryKey().getValue().getPath();
            MinecraftServer server = this.world.getServer();
            BlockPos triggeredBlockPos = new BlockPos(this.pos.getX() + this.secondTriggeredBlockPositionOffset.getX(), this.pos.getY() + this.secondTriggeredBlockPositionOffset.getY(), this.pos.getZ() + this.secondTriggeredBlockPositionOffset.getZ());
            if (server != null && UUIDUtilities.isStringValidUUID(worldName)) {
                ServerPlayerEntity serverPlayerEntity = server.getPlayerManager().getPlayer(UUID.fromString(worldName));
                if (serverPlayerEntity != null) {
                    PlayerAdvancementTracker advancementTracker = server.getPlayerManager().getAdvancementTracker(serverPlayerEntity);
                    if (advancementTracker != null) {
                        AdvancementEntry checkedAdvancementEntry = server.getAdvancementLoader().get(Identifier.tryParse(this.checkedAdvancementIdentifier));
                        if (checkedAdvancementEntry != null && advancementTracker.getProgress(checkedAdvancementEntry).isDone()) {
                            triggeredBlockPos = new BlockPos(this.pos.getX() + this.firstTriggeredBlockPositionOffset.getX(), this.pos.getY() + this.firstTriggeredBlockPositionOffset.getY(), this.pos.getZ() + this.firstTriggeredBlockPositionOffset.getZ());
                        }
                    }
                }
            }
            BlockEntity blockEntity = world.getBlockEntity(triggeredBlockPos);
            if (blockEntity instanceof Triggerable triggerable) {
                triggerable.trigger();
            }
        }
    }

    @Override
    protected void onRotate(BlockState state) {
        if (state.getBlock() instanceof RotatedBlockWithEntity) {
            if (state.get(RotatedBlockWithEntity.ROTATED) != this.rotated) {
                BlockRotation blockRotation = BlockRotationUtils.calculateRotationFromDifferentRotatedStates(state.get(RotatedBlockWithEntity.ROTATED), this.rotated);

                this.firstTriggeredBlockPositionOffset = BlockRotationUtils.rotateOffsetBlockPos(this.firstTriggeredBlockPositionOffset, blockRotation);

                this.secondTriggeredBlockPositionOffset = BlockRotationUtils.rotateOffsetBlockPos(this.secondTriggeredBlockPositionOffset, blockRotation);

                this.rotated = state.get(RotatedBlockWithEntity.ROTATED);
            }
            if (state.get(RotatedBlockWithEntity.X_MIRRORED) != this.x_mirrored) {

                this.firstTriggeredBlockPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.firstTriggeredBlockPositionOffset, BlockMirror.FRONT_BACK);

                this.secondTriggeredBlockPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.secondTriggeredBlockPositionOffset, BlockMirror.FRONT_BACK);

                this.x_mirrored = state.get(RotatedBlockWithEntity.X_MIRRORED);
            }
            if (state.get(RotatedBlockWithEntity.Z_MIRRORED) != this.z_mirrored) {

                this.firstTriggeredBlockPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.firstTriggeredBlockPositionOffset, BlockMirror.LEFT_RIGHT);

                this.secondTriggeredBlockPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.secondTriggeredBlockPositionOffset, BlockMirror.LEFT_RIGHT);

                this.z_mirrored = state.get(RotatedBlockWithEntity.Z_MIRRORED);
            }
        }
    }
}
