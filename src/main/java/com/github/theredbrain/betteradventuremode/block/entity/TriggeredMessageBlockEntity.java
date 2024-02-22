package com.github.theredbrain.betteradventuremode.block.entity;
//
//import com.github.theredbrain.betteradventuremode.block.Resetable;
//import com.github.theredbrain.betteradventuremode.block.RotatedBlockWithEntity;
//import com.github.theredbrain.betteradventuremode.block.Triggerable;
//import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
//import com.github.theredbrain.betteradventuremode.registry.EntityRegistry;
//import com.github.theredbrain.betteradventuremode.util.BlockRotationUtils;
//import net.minecraft.block.BlockState;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.nbt.NbtCompound;
//import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
//import net.minecraft.server.world.ServerWorld;
//import net.minecraft.text.Text;
//import net.minecraft.util.BlockMirror;
//import net.minecraft.util.BlockRotation;
//import net.minecraft.util.StringIdentifiable;
//import net.minecraft.util.math.*;
//import org.apache.commons.lang3.tuple.MutablePair;
//
//import java.util.*;
//
//public class TriggeredMessageBlockEntity extends RotatedBlockEntity implements Triggerable, Resetable {
//
//    private boolean showMessageArea = false;
//    private Vec3i messageAreaDimensions = Vec3i.ZERO;
//    private BlockPos messageAreaPositionOffset = new BlockPos(0, 1, 0);
//    private boolean calculateMessageBox = true;
//    private Box messageArea = null;
//
//    private boolean overlay = false;
//
//    String message = "";
//
//    private TriggerMode triggerMode = TriggerMode.ONCE;
//
//    private boolean triggered = false;
//
//    public TriggeredMessageBlockEntity(BlockPos pos, BlockState state) {
//        super(EntityRegistry.TRIGGERED_MESSAGE_BLOCK_ENTITY, pos, state);
//    }
//
//    @Override
//    protected void writeNbt(NbtCompound nbt) {
//
//        super.writeNbt(nbt);
//
//        nbt.putBoolean("showMessageArea", this.showMessageArea);
//
//        nbt.putInt("messageAreaDimensionsX", this.messageAreaDimensions.getX());
//        nbt.putInt("messageAreaDimensionsY", this.messageAreaDimensions.getY());
//        nbt.putInt("messageAreaDimensionsZ", this.messageAreaDimensions.getZ());
//
//        nbt.putInt("messageAreaPositionOffsetX", this.messageAreaPositionOffset.getX());
//        nbt.putInt("messageAreaPositionOffsetY", this.messageAreaPositionOffset.getY());
//        nbt.putInt("messageAreaPositionOffsetZ", this.messageAreaPositionOffset.getZ());
//
//        nbt.putString("message", this.message);
//
//        nbt.putBoolean("overlay", this.overlay);
//
//        nbt.putString("triggerMode", this.triggerMode.asString());
//
//        nbt.putBoolean("triggered", this.triggered);
//    }
//
//    @Override
//    public void readNbt(NbtCompound nbt) {
//
//        super.readNbt(nbt);
//
//        this.showMessageArea = nbt.getBoolean("showMessageArea");
//
//        int i = MathHelper.clamp(nbt.getInt("messageAreaDimensionsX"), 0, 48);
//        int j = MathHelper.clamp(nbt.getInt("messageAreaDimensionsY"), 0, 48);
//        int k = MathHelper.clamp(nbt.getInt("messageAreaDimensionsZ"), 0, 48);
//        this.messageAreaDimensions = new Vec3i(i, j, k);
//
//        int l = MathHelper.clamp(nbt.getInt("messageAreaPositionOffsetX"), -48, 48);
//        int m = MathHelper.clamp(nbt.getInt("messageAreaPositionOffsetY"), -48, 48);
//        int n = MathHelper.clamp(nbt.getInt("messageAreaPositionOffsetZ"), -48, 48);
//        this.messageAreaPositionOffset = new BlockPos(l, m, n);
//
//        this.message = nbt.getString("message");
//
//        this.overlay = nbt.getBoolean("overlay");
//
//        this.triggerMode = TriggerMode.byName(nbt.getString("triggerMode")).orElseGet(() -> TriggerMode.ONCE);
//
//        this.triggered = nbt.getBoolean("triggered");
//    }
//
//    public BlockEntityUpdateS2CPacket toUpdatePacket() {
//        return BlockEntityUpdateS2CPacket.create(this);
//    }
//
//    @Override
//    public NbtCompound toInitialChunkDataNbt() {
//        return this.createNbt();
//    }
//
//    public boolean openScreen(PlayerEntity player) {
//        if (!player.isCreativeLevelTwoOp()) {
//            return false;
//        }
//        if (player.getEntityWorld().isClient) {
//            ((DuckPlayerEntityMixin)player).betteradventuremode$openTriggeredMessageBlockScreen(this);
//        }
//        return true;
//    }
//
//    //region getter/setter
//
//    public boolean getShowMessageArea() {
//        return showMessageArea;
//    }
//
//    public void setShowMessageArea(boolean showMessageArea) {
//        this.showMessageArea = showMessageArea;
//    }
//
//    public Vec3i getMessageAreaDimensions() {
//        return messageAreaDimensions;
//    }
//
//    // TODO check if input is valid
//    public boolean setMessageAreaDimensions(Vec3i messageAreaDimensions) {
//        this.messageAreaDimensions = messageAreaDimensions;
//        this.calculateMessageBox = true;
//        return true;
//    }
//
//    public BlockPos getMessageAreaPositionOffset() {
//        return messageAreaPositionOffset;
//    }
//
//    // TODO check if input is valid
//    public boolean setMessageAreaPositionOffset(BlockPos messageAreaPositionOffset) {
//        this.messageAreaPositionOffset = messageAreaPositionOffset;
//        this.calculateMessageBox = true;
//        return true;
//    }
//
//    public String getMessage() {
//        return this.message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public boolean getOverlay() {
//        return this.overlay;
//    }
//
//    public void setOverlay(boolean overlay) {
//        this.overlay = overlay;
//    }
//
//    public TriggerMode getTriggerMode() {
//        return this.triggerMode;
//    }
//
//    public void setTriggerMode(TriggerMode triggerMode) {
//        this.triggerMode = triggerMode;
//    }
//
//    //endregion getter/setter
//
//    @Override
//    protected void onRotate(BlockState state) {
//        if (state.getBlock() instanceof RotatedBlockWithEntity) {
//            if (state.get(RotatedBlockWithEntity.ROTATED) != this.rotated) {
//                BlockRotation blockRotation = BlockRotationUtils.calculateRotationFromDifferentRotatedStates(state.get(RotatedBlockWithEntity.ROTATED), this.rotated);
//
//                MutablePair<BlockPos, Vec3i> offsetArea = BlockRotationUtils.rotateOffsetArea(this.messageAreaPositionOffset, this.messageAreaDimensions, blockRotation);
//                this.messageAreaPositionOffset = offsetArea.getLeft();
//                this.messageAreaDimensions = offsetArea.getRight();
//
//                this.rotated = state.get(RotatedBlockWithEntity.ROTATED);
//            }
//            if (state.get(RotatedBlockWithEntity.X_MIRRORED) != this.x_mirrored) {
//
//                MutablePair<BlockPos, Vec3i> offsetArea = BlockRotationUtils.mirrorOffsetArea(this.messageAreaPositionOffset, this.messageAreaDimensions, BlockMirror.FRONT_BACK);
//                this.messageAreaPositionOffset = offsetArea.getLeft();
//                this.messageAreaDimensions = offsetArea.getRight();
//
//                this.x_mirrored = state.get(RotatedBlockWithEntity.X_MIRRORED);
//            }
//            if (state.get(RotatedBlockWithEntity.Z_MIRRORED) != this.z_mirrored) {
//
//                MutablePair<BlockPos, Vec3i> offsetArea = BlockRotationUtils.mirrorOffsetArea(this.messageAreaPositionOffset, this.messageAreaDimensions, BlockMirror.LEFT_RIGHT);
//                this.messageAreaPositionOffset = offsetArea.getLeft();
//                this.messageAreaDimensions = offsetArea.getRight();
//
//                this.z_mirrored = state.get(RotatedBlockWithEntity.Z_MIRRORED);
//            }
//        }
//    }
//
//    @Override
//    public void reset() {
//        if (this.triggered) {
//            this.triggered = false;
//        }
//    }
//
//    @Override
//    public void trigger() {
//        if (this.triggerMode == TriggerMode.ONCE && !this.triggered) {
//            this.sendMessage();
//                this.triggered = true;
//
//        } else if (this.triggerMode == TriggerMode.CONTINUOUS) {
//            this.sendMessage();
//                this.triggered = true;
//
//        }
//    }
//
//    private void sendMessage() {
//        if (this.world instanceof ServerWorld) {
//            if (this.calculateMessageBox || this.messageArea == null) {
//                BlockPos messageAreaPositionOffset = this.messageAreaPositionOffset;
//                Vec3i messageAreaDimensions = this.messageAreaDimensions;
//                Vec3d messageAreaStart = new Vec3d(pos.getX() + messageAreaPositionOffset.getX(), pos.getY() + messageAreaPositionOffset.getY(), pos.getZ() + messageAreaPositionOffset.getZ());
//                Vec3d messageAreaEnd = new Vec3d(messageAreaStart.getX() + messageAreaDimensions.getX(), messageAreaStart.getY() + messageAreaDimensions.getY(), messageAreaStart.getZ() + messageAreaDimensions.getZ());
//                this.messageArea = new Box(messageAreaStart, messageAreaEnd);
//                this.calculateMessageBox = false;
//            }
//            List<PlayerEntity> list = world.getNonSpectatingEntities(PlayerEntity.class, this.messageArea);
//            for (PlayerEntity playerEntity : list) {
//                    playerEntity.sendMessage(Text.translatable(this.message), this.overlay);
//                    ((DuckPlayerEntityMixin)playerEntity).betteradventuremode$sendAnnouncement(Text.translatable(this.message));
//            }
//        }
//    }
//
//    public static enum TriggerMode implements StringIdentifiable
//    {
//        CONTINUOUS("continuous"),
//        ONCE("once");
//
//        private final String name;
//
//        private TriggerMode(String name) {
//            this.name = name;
//        }
//
//        @Override
//        public String asString() {
//            return this.name;
//        }
//
//        public static Optional<TriggerMode> byName(String name) {
//            return Arrays.stream(TriggerMode.values()).filter(triggerMode -> triggerMode.asString().equals(name)).findFirst();
//        }
//
//        public Text asText() {
//            return Text.translatable("gui.triggered_message_block.trigger_mode." + this.name);
//        }
//    }
//}
