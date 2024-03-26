package com.github.theredbrain.betteradventuremode.block.entity;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.block.Resetable;
import com.github.theredbrain.betteradventuremode.util.BlockRotationUtils;
import com.github.theredbrain.betteradventuremode.block.RotatedBlockWithEntity;
import com.github.theredbrain.betteradventuremode.block.Triggerable;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.*;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RelayTriggerBlockEntity extends RotatedBlockEntity implements Triggerable {
    private RelayTriggerBlockEntity.SelectionMode selectionMode = SelectionMode.LIST;
    private boolean showArea = false;
    private boolean resetsArea = false;
    private Vec3i areaDimensions = Vec3i.ZERO;
    private BlockPos areaPositionOffset = new BlockPos(0, 1, 0);

    private List<MutablePair<MutablePair<BlockPos, Boolean>, Integer>> triggeredBlocks = new ArrayList<>(List.of());
    private RelayTriggerBlockEntity.TriggerMode triggerMode = TriggerMode.NORMAL;
    private int triggerAmount = 1;
    public RelayTriggerBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.RELAY_TRIGGER_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {

        nbt.putString("selectionMode", this.selectionMode.asString());

        nbt.putBoolean("showArea", this.showArea);

        nbt.putBoolean("resetsArea", this.resetsArea);

        nbt.putInt("areaDimensionsX", this.areaDimensions.getX());
        nbt.putInt("areaDimensionsY", this.areaDimensions.getY());
        nbt.putInt("areaDimensionsZ", this.areaDimensions.getZ());

        nbt.putInt("areaPositionOffsetX", this.areaPositionOffset.getX());
        nbt.putInt("areaPositionOffsetY", this.areaPositionOffset.getY());
        nbt.putInt("areaPositionOffsetZ", this.areaPositionOffset.getZ());

        nbt.putInt("triggeredBlocksSize", triggeredBlocks.size());
        for (int i = 0; i < this.triggeredBlocks.size(); i++) {
            BlockPos triggeredBlock = this.triggeredBlocks.get(i).left.left;
            nbt.putInt("triggeredBlockPositionOffsetX_" + i, triggeredBlock.getX());
            nbt.putInt("triggeredBlockPositionOffsetY_" + i, triggeredBlock.getY());
            nbt.putInt("triggeredBlockPositionOffsetZ_" + i, triggeredBlock.getZ());
            nbt.putBoolean("triggeredBlockResets_" + i, this.triggeredBlocks.get(i).left.right);
            nbt.putInt("triggeredBlockChance_" + i, this.triggeredBlocks.get(i).right);
        }

        nbt.putString("triggerMode", this.triggerMode.asString());

        nbt.putInt("triggerAmount", this.triggerAmount);

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {

        this.selectionMode = SelectionMode.byName(nbt.getString("selectionMode")).orElseGet(() -> SelectionMode.LIST);

        this.showArea = nbt.getBoolean("showArea");

        this.resetsArea = nbt.getBoolean("resetsArea");

        int i = MathHelper.clamp(nbt.getInt("areaDimensionsX"), 0, 48);
        int j = MathHelper.clamp(nbt.getInt("areaDimensionsY"), 0, 48);
        int k = MathHelper.clamp(nbt.getInt("areaDimensionsZ"), 0, 48);
        this.areaDimensions = new Vec3i(i, j, k);

        i = MathHelper.clamp(nbt.getInt("areaPositionOffsetX"), -48, 48);
        j = MathHelper.clamp(nbt.getInt("areaPositionOffsetY"), -48, 48);
        k = MathHelper.clamp(nbt.getInt("areaPositionOffsetZ"), -48, 48);
        this.areaPositionOffset = new BlockPos(i, j, k);

        int triggeredBlocksSize = nbt.getInt("triggeredBlocksSize");
        this.triggeredBlocks = new ArrayList<>(List.of());
        for (i = 0; i < triggeredBlocksSize; i++) {
            int x = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetX_" + i), -48, 48);
            int y = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetY_" + i), -48, 48);
            int z = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetZ_" + i), -48, 48);
            boolean bl = nbt.getBoolean("triggeredBlockResets_" + i);
            int chance = MathHelper.clamp(nbt.getInt("triggeredBlockChance_" + i), 0, 100);
            this.triggeredBlocks.add(new MutablePair<>(new MutablePair<>(new BlockPos(x, y, z), bl), chance));
        }

        this.triggerMode = TriggerMode.byName(nbt.getString("triggerMode")).orElseGet(() -> TriggerMode.NORMAL);

        this.triggerAmount = nbt.getInt("triggerAmount");

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
            ((DuckPlayerEntityMixin)player).betteradventuremode$openRelayTriggerBlockScreen(this);
        }
        return true;
    }

    public SelectionMode getSelectionMode() {
        return this.selectionMode;
    }

    public void setSelectionMode(SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
    }

    public boolean getShowArea() {
        return showArea;
    }

    public void setShowArea(boolean showArea) {
        this.showArea = showArea;
    }

    public boolean getResetsArea() {
        return resetsArea;
    }

    public void setResetsArea(boolean resetsArea) {
        this.resetsArea = resetsArea;
    }

    public Vec3i getAreaDimensions() {
        return areaDimensions;
    }

    public void setAreaDimensions(Vec3i areaDimensions) {
        this.areaDimensions = areaDimensions;
    }

    public BlockPos getAreaPositionOffset() {
        return areaPositionOffset;
    }

    public void setAreaPositionOffset(BlockPos areaPositionOffset) {
        this.areaPositionOffset = areaPositionOffset;
    }

    public List<MutablePair<MutablePair<BlockPos, Boolean>, Integer>> getTriggeredBlocks() {
        return triggeredBlocks;
    }

    public void setTriggeredBlocks(List<MutablePair<MutablePair<BlockPos, Boolean>, Integer>> triggeredBlocks) {
        this.triggeredBlocks = triggeredBlocks;
    }

    public TriggerMode getTriggerMode() {
        return triggerMode;
    }

    public void setTriggerMode(TriggerMode triggerMode) {
        this.triggerMode = triggerMode;
    }

    public int getTriggerAmount() {
        return triggerAmount;
    }

    public void setTriggerAmount(int triggerAmount) {
        this.triggerAmount = triggerAmount;
    }

    @Override
    public void trigger() {
        if (this.world != null) {
            BlockEntity blockEntity;
            if (this.selectionMode == SelectionMode.LIST) {
                if (this.triggerMode == TriggerMode.NORMAL) {
                    for (MutablePair<MutablePair<BlockPos, Boolean>, Integer> triggeredBlock : this.triggeredBlocks) {
                        BlockPos triggeredBlockPos = triggeredBlock.left.left;
                        blockEntity = world.getBlockEntity(new BlockPos(this.pos.getX() + triggeredBlockPos.getX(), this.pos.getY() + triggeredBlockPos.getY(), this.pos.getZ() + triggeredBlockPos.getZ()));
                        if (blockEntity == this) {
                            continue;
                        }
                        if (triggeredBlock.getLeft().getRight()) {
                            if (blockEntity instanceof Resetable resetable) {
                                resetable.reset();
                            }
                        } else {
                            if (blockEntity instanceof Triggerable triggerable) {
                                triggerable.trigger();
                            }
                        }
                    }
                } else if (this.triggerMode == TriggerMode.RANDOM) {
                    for (MutablePair<MutablePair<BlockPos, Boolean>, Integer> triggeredBlock : this.triggeredBlocks) {
                        int chance = this.world.random.nextInt(100);
                        if (chance <= triggeredBlock.right) {
                            BlockPos triggeredBlockPos = triggeredBlock.left.left;
                            blockEntity = world.getBlockEntity(new BlockPos(this.pos.getX() + triggeredBlockPos.getX(), this.pos.getY() + triggeredBlockPos.getY(), this.pos.getZ() + triggeredBlockPos.getZ()));
                            if (blockEntity == this) {
                                continue;
                            }
                            if (triggeredBlock.getLeft().getRight()) {
                                if (blockEntity instanceof Resetable resetable) {
                                    resetable.reset();
                                }
                            } else {
                                if (blockEntity instanceof Triggerable triggerable) {
                                    triggerable.trigger();
                                }
                            }
                        }
                    }
                } else if (this.triggerMode == TriggerMode.BINOMIAL_URN) { // TODO
                    BetterAdventureMode.info("this mode is WIP");
                } else if (this.triggerMode == TriggerMode.HYPER_GEOMETRIC_URN) { // TODO
                    BetterAdventureMode.info("this mode is WIP");
                }
            } else if (this.selectionMode == SelectionMode.AREA) {
                Vec3i activationAreaDimensions = this.getAreaDimensions();
                BlockPos blockPos = new BlockPos(this.pos.getX() + this.areaPositionOffset.getX(), this.pos.getY() + this.areaPositionOffset.getY(), this.pos.getZ() + this.areaPositionOffset.getZ());
                for (int i = 0; i < activationAreaDimensions.getX(); i++) {
                    for (int j = 0; j < activationAreaDimensions.getY(); j++) {
                        for (int k = 0; k < activationAreaDimensions.getZ(); k++) {
                            blockEntity = world.getBlockEntity(new BlockPos(blockPos.getX() + i, blockPos.getY() + j, blockPos.getZ() + k));
                            if (blockEntity == this) {
                                continue;
                            }
                            if (this.resetsArea) {
                                if (blockEntity instanceof Resetable resetable) {
                                    resetable.reset();
                                }
                            } else {
                                if (blockEntity instanceof Triggerable triggerable) {
                                    triggerable.trigger();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onRotate(BlockState state) {
        if (state.getBlock() instanceof RotatedBlockWithEntity) {
            if (state.get(RotatedBlockWithEntity.ROTATED) != this.rotated) {
                BlockRotation blockRotation = BlockRotationUtils.calculateRotationFromDifferentRotatedStates(state.get(RotatedBlockWithEntity.ROTATED), this.rotated);

                MutablePair<BlockPos, Vec3i> offsetArea = BlockRotationUtils.rotateOffsetArea(this.areaPositionOffset, this.areaDimensions, blockRotation);
                this.areaPositionOffset = offsetArea.getLeft();
                this.areaDimensions = offsetArea.getRight();

                List<MutablePair<MutablePair<BlockPos, Boolean>, Integer>> newTriggeredBlocks = new ArrayList<>(List.of());
                for (MutablePair<MutablePair<BlockPos, Boolean>, Integer> triggeredBlock : this.triggeredBlocks) {
                    newTriggeredBlocks.add(new MutablePair<>(new MutablePair<>(BlockRotationUtils.rotateOffsetBlockPos(triggeredBlock.getLeft().getLeft(), blockRotation), triggeredBlock.getLeft().getRight()), triggeredBlock.getRight()));
                }
                this.triggeredBlocks = newTriggeredBlocks;

                this.rotated = state.get(RotatedBlockWithEntity.ROTATED);
            }
            if (state.get(RotatedBlockWithEntity.X_MIRRORED) != this.x_mirrored) {

                MutablePair<BlockPos, Vec3i> offsetArea = BlockRotationUtils.mirrorOffsetArea(this.areaPositionOffset, this.areaDimensions, BlockMirror.FRONT_BACK);
                this.areaPositionOffset = offsetArea.getLeft();
                this.areaDimensions = offsetArea.getRight();

                List<MutablePair<MutablePair<BlockPos, Boolean>, Integer>> newTriggeredBlocks = new ArrayList<>(List.of());
                for (MutablePair<MutablePair<BlockPos, Boolean>, Integer> triggeredBlock : this.triggeredBlocks) {
                    newTriggeredBlocks.add(new MutablePair<>(new MutablePair<>(BlockRotationUtils.mirrorOffsetBlockPos(triggeredBlock.getLeft().getLeft(), BlockMirror.FRONT_BACK), triggeredBlock.getLeft().getRight()), triggeredBlock.getRight()));
                }
                this.triggeredBlocks = newTriggeredBlocks;

                this.x_mirrored = state.get(RotatedBlockWithEntity.X_MIRRORED);
            }
            if (state.get(RotatedBlockWithEntity.Z_MIRRORED) != this.z_mirrored) {

                MutablePair<BlockPos, Vec3i> offsetArea = BlockRotationUtils.mirrorOffsetArea(this.areaPositionOffset, this.areaDimensions, BlockMirror.LEFT_RIGHT);
                this.areaPositionOffset = offsetArea.getLeft();
                this.areaDimensions = offsetArea.getRight();

                List<MutablePair<MutablePair<BlockPos, Boolean>, Integer>> newTriggeredBlocks = new ArrayList<>(List.of());
                for (MutablePair<MutablePair<BlockPos, Boolean>, Integer> triggeredBlock : this.triggeredBlocks) {
                    newTriggeredBlocks.add(new MutablePair<>(new MutablePair<>(BlockRotationUtils.mirrorOffsetBlockPos(triggeredBlock.getLeft().getLeft(), BlockMirror.LEFT_RIGHT), triggeredBlock.getLeft().getRight()), triggeredBlock.getRight()));
                }
                this.triggeredBlocks = newTriggeredBlocks;

                this.z_mirrored = state.get(RotatedBlockWithEntity.Z_MIRRORED);
            }
        }
    }

    public static enum SelectionMode implements StringIdentifiable
    {
        LIST("list"),
        AREA("area");

        private final String name;

        private SelectionMode(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        public static Optional<RelayTriggerBlockEntity.SelectionMode> byName(String name) {
            return Arrays.stream(RelayTriggerBlockEntity.SelectionMode.values()).filter(selectionMode -> selectionMode.asString().equals(name)).findFirst();
        }

        public Text asText() {
            return Text.translatable("gui.relay_trigger_block.selectionMode." + this.name);
        }
    }

    public static enum TriggerMode implements StringIdentifiable
    {
        NORMAL("normal"),
        RANDOM("random"),
        BINOMIAL_URN("binomial_urn"),
        HYPER_GEOMETRIC_URN("hyper_geometric_urn");

        private final String name;

        private TriggerMode(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        public static Optional<RelayTriggerBlockEntity.TriggerMode> byName(String name) {
            return Arrays.stream(RelayTriggerBlockEntity.TriggerMode.values()).filter(triggerMode -> triggerMode.asString().equals(name)).findFirst();
        }

        public Text asText() {
            return Text.translatable("gui.relay_trigger_block.triggerMode." + this.name);
        }
    }
}
