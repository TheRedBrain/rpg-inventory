package com.github.theredbrain.betteradventuremode.block.entity;

import com.github.theredbrain.betteradventuremode.util.BlockRotationUtils;
import com.github.theredbrain.betteradventuremode.block.Resetable;
import com.github.theredbrain.betteradventuremode.block.RotatedBlockWithEntity;
import com.github.theredbrain.betteradventuremode.block.Triggerable;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.*;

public class AreaBlockEntity extends RotatedBlockEntity implements Triggerable, Resetable {

    private boolean calculateAreaBox = true;
    private Box area = null;
    private boolean showArea = false;
    private Vec3i areaDimensions = Vec3i.ZERO;
    private BlockPos areaPositionOffset = new BlockPos(0, 1, 0);

    private String appliedStatusEffectIdentifier = "";
    private int appliedStatusEffectAmplifier = 0;
    private boolean appliedStatusEffectAmbient = false;
    private boolean appliedStatusEffectShowParticles = false;
    private boolean appliedStatusEffectShowIcon = false;

    private boolean hasTriggered = false;
    private boolean wasTriggered = false;
    private TriggerMode triggerMode = TriggerMode.ALWAYS;
    private TriggeredMode triggeredMode = TriggeredMode.ONCE;
    private MutablePair<BlockPos, Boolean> triggeredBlock = new MutablePair<>(new BlockPos(0, 0, 0), false);

    private MessageMode messageMode = MessageMode.OVERLAY;
    private String joinMessage = "";
    private String leaveMessage = "";
    private String triggeredMessage = "";

    private ArrayList<PlayerEntity> playerList = new ArrayList<>();
    private HashMap<UUID, Integer> playerMap = new HashMap<>();

    public AreaBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.AREA_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {

        nbt.putBoolean("showArea", this.showArea);

        if (this.area != null) {
            nbt.putDouble("areaMinX", this.area.minX);
            nbt.putDouble("areaMaxX", this.area.maxX);
            nbt.putDouble("areaMinY", this.area.minY);
            nbt.putDouble("areaMaxY", this.area.maxY);
            nbt.putDouble("areaMinZ", this.area.minZ);
            nbt.putDouble("areaMaxZ", this.area.maxZ);
        }

        nbt.putInt("areaDimensionsX", this.areaDimensions.getX());
        nbt.putInt("areaDimensionsY", this.areaDimensions.getY());
        nbt.putInt("areaDimensionsZ", this.areaDimensions.getZ());

        nbt.putInt("areaPositionOffsetX", this.areaPositionOffset.getX());
        nbt.putInt("areaPositionOffsetY", this.areaPositionOffset.getY());
        nbt.putInt("areaPositionOffsetZ", this.areaPositionOffset.getZ());


        nbt.putString("appliedStatusEffectIdentifier", this.appliedStatusEffectIdentifier);

        nbt.putInt("appliedStatusEffectAmplifier", this.appliedStatusEffectAmplifier);

        nbt.putBoolean("appliedStatusEffectAmbient", this.appliedStatusEffectAmbient);

        nbt.putBoolean("appliedStatusEffectShowParticles", this.appliedStatusEffectShowParticles);

        nbt.putBoolean("appliedStatusEffectShowIcon", this.appliedStatusEffectShowIcon);


        nbt.putBoolean("hasTriggered", this.hasTriggered);

        nbt.putBoolean("wasTriggered", this.wasTriggered);

        nbt.putString("triggerMode", this.triggerMode.asString());

        nbt.putString("triggeredMode", this.triggeredMode.asString());

        nbt.putInt("triggeredBlockPositionOffsetX", this.triggeredBlock.getLeft().getX());
        nbt.putInt("triggeredBlockPositionOffsetY", this.triggeredBlock.getLeft().getY());
        nbt.putInt("triggeredBlockPositionOffsetZ", this.triggeredBlock.getLeft().getZ());
        nbt.putBoolean("triggeredBlockResets", this.triggeredBlock.getRight());


        nbt.putString("messageMode", this.messageMode.asString());

        nbt.putString("joinMessage", this.joinMessage);

        nbt.putString("leaveMessage", this.leaveMessage);

        nbt.putString("triggeredMessage", this.triggeredMessage);


        int playerListSize = this.playerList.size();
        nbt.putInt("playerListSize", playerListSize);

        super.writeNbt(nbt);

    }

    @Override
    public void readNbt(NbtCompound nbt) {

        this.showArea = nbt.getBoolean("showArea");

        if (nbt.contains("areaMinX") && nbt.contains("areaMinY") && nbt.contains("areaMinZ") && nbt.contains("areaMaxX") && nbt.contains("areaMaxY") && nbt.contains("areaMaxZ")) {
            this.area = new Box(nbt.getDouble("areaMinX"), nbt.getDouble("areaMinY"), nbt.getDouble("areaMinZ"), nbt.getDouble("areaMaxX"), nbt.getDouble("areaMaxY"), nbt.getDouble("areaMaxZ"));
            this.calculateAreaBox = true;
        }

        int i = MathHelper.clamp(nbt.getInt("areaDimensionsX"), 0, 48);
        int j = MathHelper.clamp(nbt.getInt("areaDimensionsY"), 0, 48);
        int k = MathHelper.clamp(nbt.getInt("areaDimensionsZ"), 0, 48);
        this.areaDimensions = new Vec3i(i, j, k);

        int l = MathHelper.clamp(nbt.getInt("areaPositionOffsetX"), -48, 48);
        int m = MathHelper.clamp(nbt.getInt("areaPositionOffsetY"), -48, 48);
        int n = MathHelper.clamp(nbt.getInt("areaPositionOffsetZ"), -48, 48);
        this.areaPositionOffset = new BlockPos(l, m, n);


        this.appliedStatusEffectIdentifier = nbt.getString("appliedStatusEffectIdentifier");

        this.appliedStatusEffectAmplifier = nbt.getInt("appliedStatusEffectAmplifier");

        this.appliedStatusEffectAmbient = nbt.getBoolean("appliedStatusEffectAmbient");

        this.appliedStatusEffectShowParticles = nbt.getBoolean("appliedStatusEffectShowParticles");

        this.appliedStatusEffectShowIcon = nbt.getBoolean("appliedStatusEffectShowIcon");


        this.hasTriggered = nbt.getBoolean("hasTriggered");

        this.wasTriggered = nbt.getBoolean("wasTriggered");

        this.triggerMode = TriggerMode.byName(nbt.getString("triggerMode")).orElse(TriggerMode.ALWAYS);

        this.triggeredMode = TriggeredMode.byName(nbt.getString("triggeredMode")).orElse(TriggeredMode.ONCE);

        int x = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetX"), -48, 48);
        int y = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetY"), -48, 48);
        int z = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetZ"), -48, 48);
        this.triggeredBlock = new MutablePair<>(new BlockPos(x, y, z), nbt.getBoolean("triggeredBlockResets"));


        this.messageMode = MessageMode.byName(nbt.getString("messageMode")).orElse(MessageMode.OVERLAY);

        this.joinMessage = nbt.getString("joinMessage");

        this.leaveMessage = nbt.getString("leaveMessage");

        this.triggeredMessage = nbt.getString("triggeredMessage");

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
            ((DuckPlayerEntityMixin) player).betteradventuremode$openAreaBlockScreen(this);
        }
        return true;
    }

    public static void tick(World world, BlockPos pos, BlockState state, AreaBlockEntity areaBlockEntity) {
        if (!world.isClient && world.getTime() % 80L == 0L && areaBlockEntity.wasTriggered) {
            if (areaBlockEntity.calculateAreaBox || areaBlockEntity.area == null) {
                BlockPos areaPositionOffset = areaBlockEntity.areaPositionOffset;
                Vec3i areaDimensions = areaBlockEntity.areaDimensions;
                Vec3d areaStart = new Vec3d(pos.getX() + areaPositionOffset.getX(), pos.getY() + areaPositionOffset.getY(), pos.getZ() + areaPositionOffset.getZ());
                Vec3d areaEnd = new Vec3d(areaStart.getX() + areaDimensions.getX(), areaStart.getY() + areaDimensions.getY(), areaStart.getZ() + areaDimensions.getZ());
                areaBlockEntity.area = new Box(areaStart, areaEnd);
                areaBlockEntity.calculateAreaBox = false;
            }

            boolean shouldTriggerBlock = false;

            StatusEffect statusEffect = Registries.STATUS_EFFECT.get(Identifier.tryParse(areaBlockEntity.appliedStatusEffectIdentifier));

            List<PlayerEntity> newPlayerList = world.getNonSpectatingEntities(PlayerEntity.class, areaBlockEntity.area);
            List<PlayerEntity> tempList = new ArrayList<>();

            Iterator<PlayerEntity> playerListIterator = areaBlockEntity.playerList.iterator();
            Iterator<PlayerEntity> newPlayerListIterator = newPlayerList.iterator();
            PlayerEntity playerEntity;
            while (playerListIterator.hasNext()) {
                playerEntity = (PlayerEntity) playerListIterator.next();
                if (newPlayerList.contains(playerEntity)) {
                    tempList.add(playerEntity);
                    newPlayerList.remove(playerEntity);
                    if (statusEffect != null && areaBlockEntity.wasTriggered) {
                        playerEntity.addStatusEffect(
                                new StatusEffectInstance(
                                        statusEffect,
                                        100,
                                        areaBlockEntity.appliedStatusEffectAmplifier,
                                        areaBlockEntity.appliedStatusEffectAmbient,
                                        areaBlockEntity.appliedStatusEffectShowParticles,
                                        areaBlockEntity.appliedStatusEffectShowIcon
                                )
                        );
                    }
                } else {
                    if (!areaBlockEntity.leaveMessage.equals("")) {
                        if (areaBlockEntity.messageMode == MessageMode.ANNOUNCEMENT) {
                            ((DuckPlayerEntityMixin)playerEntity).betteradventuremode$sendAnnouncement(Text.translatable(areaBlockEntity.leaveMessage));
                        } else {
                            playerEntity.sendMessage(Text.translatable(areaBlockEntity.leaveMessage), areaBlockEntity.messageMode == MessageMode.OVERLAY);
                        }
                    }
                }
            }
            while (newPlayerListIterator.hasNext()) {
                playerEntity = (PlayerEntity) newPlayerListIterator.next();
                if (!areaBlockEntity.joinMessage.equals("")) {
                    if (areaBlockEntity.messageMode == MessageMode.ANNOUNCEMENT) {
                        ((DuckPlayerEntityMixin)playerEntity).betteradventuremode$sendAnnouncement(Text.translatable(areaBlockEntity.joinMessage));
                    } else {
                        playerEntity.sendMessage(Text.translatable(areaBlockEntity.joinMessage), areaBlockEntity.messageMode == MessageMode.OVERLAY);
                    }
                }
                if (statusEffect != null) {
                    playerEntity.addStatusEffect(
                            new StatusEffectInstance(
                                    statusEffect,
                                    100,
                                    areaBlockEntity.appliedStatusEffectAmplifier,
                                    areaBlockEntity.appliedStatusEffectAmbient,
                                    areaBlockEntity.appliedStatusEffectShowParticles,
                                    areaBlockEntity.appliedStatusEffectShowIcon
                            )
                    );
                }
                shouldTriggerBlock = true;
            }
            if (shouldTriggerBlock) {
                areaBlockEntity.triggerBlock();
            }
        }
    }

    @Override
    public void reset() {
        if (this.hasTriggered) {
            this.hasTriggered = false;
        }
        if (this.wasTriggered) {
            this.wasTriggered = false;
        }
    }

    @Override
    public void trigger() {
        if ((this.triggeredMode == TriggeredMode.ONCE && !this.wasTriggered) || this.triggeredMode == TriggeredMode.CONTINUOUS) {
            this.sendMessage(this.triggeredMessage);
        }
        if (!this.wasTriggered) {
            this.wasTriggered = true;
        }
    }

    private void triggerBlock() {
        int x = this.triggeredBlock.getLeft().getX();
        int y = this.triggeredBlock.getLeft().getY();
        int z = this.triggeredBlock.getLeft().getZ();
        if (this.world != null && (x != 0 || y != 0 || z != 0)) {
            BlockEntity blockEntity = world.getBlockEntity(new BlockPos(this.pos.getX() + x, this.pos.getY() + y, this.pos.getZ() + z));
            if (blockEntity != this) {
                boolean triggeredBlockResets = this.triggeredBlock.getRight();
                if (triggeredBlockResets && blockEntity instanceof Resetable resetable) {
                    resetable.reset();
                } else if (!triggeredBlockResets && blockEntity instanceof Triggerable triggerable) {
                    triggerable.trigger();
                }
            }
        }
    }

    private void sendMessage(String message) {
        if (this.world instanceof ServerWorld) {
            if (this.calculateAreaBox || this.area == null) {
                BlockPos messageAreaPositionOffset = this.areaPositionOffset;
                Vec3i messageAreaDimensions = this.areaDimensions;
                Vec3d messageAreaStart = new Vec3d(pos.getX() + messageAreaPositionOffset.getX(), pos.getY() + messageAreaPositionOffset.getY(), pos.getZ() + messageAreaPositionOffset.getZ());
                Vec3d messageAreaEnd = new Vec3d(messageAreaStart.getX() + messageAreaDimensions.getX(), messageAreaStart.getY() + messageAreaDimensions.getY(), messageAreaStart.getZ() + messageAreaDimensions.getZ());
                this.area = new Box(messageAreaStart, messageAreaEnd);
                this.calculateAreaBox = false;
            }
            List<PlayerEntity> list = world.getNonSpectatingEntities(PlayerEntity.class, this.area);
            for (PlayerEntity playerEntity : list) {
                if (this.messageMode == MessageMode.ANNOUNCEMENT) {
                    ((DuckPlayerEntityMixin)playerEntity).betteradventuremode$sendAnnouncement(Text.translatable(message));
                } else {
                    playerEntity.sendMessage(Text.translatable(message), this.messageMode == MessageMode.OVERLAY);
                }
            }
        }
    }

    //region --- getter & setter ---
    public boolean showArea() {
        return showArea;
    }

    public void setShowArea(boolean showArea) {
        this.showArea = showArea;
    }

    public Vec3i getAreaDimensions() {
        return areaDimensions;
    }

    // TODO check if input is valid
    public boolean setAreaDimensions(Vec3i areaDimensions) {
        this.areaDimensions = areaDimensions;
        this.calculateAreaBox = true;
        return true;
    }

    public BlockPos getAreaPositionOffset() {
        return areaPositionOffset;
    }

    // TODO check if input is valid
    public boolean setAreaPositionOffset(BlockPos areaPositionOffset) {
        this.areaPositionOffset = areaPositionOffset;
        this.calculateAreaBox = true;
        return true;
    }

    public String getAppliedStatusEffectIdentifier() {
        return this.appliedStatusEffectIdentifier;
    }

    public boolean setAppliedStatusEffectIdentifier(String appliedStatusEffectIdentifier) {
        if (Registries.STATUS_EFFECT.get(Identifier.tryParse(appliedStatusEffectIdentifier)) != null) {
            this.appliedStatusEffectIdentifier = appliedStatusEffectIdentifier;
            return true;
        }
        return false;
    }

    public int getAppliedStatusEffectAmplifier() {
        return appliedStatusEffectAmplifier;
    }

    public boolean setAppliedStatusEffectAmplifier(int appliedStatusEffectAmplifier) {
        if (appliedStatusEffectAmplifier >= 0 && appliedStatusEffectAmplifier < 127) {
            this.appliedStatusEffectAmplifier = appliedStatusEffectAmplifier;
            return true;
        }
        return false;
    }

    public boolean getAppliedStatusEffectAmbient() {
        return appliedStatusEffectAmbient;
    }

    public void setAppliedStatusEffectAmbient(boolean appliedStatusEffectAmbient) {
        this.appliedStatusEffectAmbient = appliedStatusEffectAmbient;
    }

    public boolean getAppliedStatusEffectShowParticles() {
        return appliedStatusEffectShowParticles;
    }

    public void setAppliedStatusEffectShowParticles(boolean appliedStatusEffectShowParticles) {
        this.appliedStatusEffectShowParticles = appliedStatusEffectShowParticles;
    }

    public boolean getAppliedStatusEffectShowIcon() {
        return appliedStatusEffectShowIcon;
    }

    public void setAppliedStatusEffectShowIcon(boolean appliedStatusEffectShowIcon) {
        this.appliedStatusEffectShowIcon = appliedStatusEffectShowIcon;
    }

    public MutablePair<BlockPos, Boolean> getTriggeredBlock() {
        return this.triggeredBlock;
    }

    public void setTriggeredBlock(MutablePair<BlockPos, Boolean> triggeredBlock) {
        this.triggeredBlock = triggeredBlock;
    }

    public boolean getWasTriggered() {
        return this.wasTriggered;
    }

    public void setWasTriggered(boolean wasTriggered) {
        this.wasTriggered = wasTriggered;
    }

    public String getJoinMessage() {
        return this.joinMessage;
    }

    public void setJoinMessage(String joinMessage) {
        this.joinMessage = joinMessage;
    }

    public String getLeaveMessage() {
        return this.leaveMessage;
    }

    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
    }

    public String getTriggeredMessage() {
        return this.triggeredMessage;
    }

    public void setTriggeredMessage(String triggeredMessage) {
        this.triggeredMessage = triggeredMessage;
    }

    public MessageMode getMessageMode() {
        return this.messageMode;
    }

    public void setMessageMode(MessageMode messageMode) {
        this.messageMode = messageMode;
    }

    public TriggerMode getTriggerMode() {
        return this.triggerMode;
    }

    public void setTriggerMode(TriggerMode triggerMode) {
        this.triggerMode = triggerMode;
    }

    public TriggeredMode getTriggeredMode() {
        return this.triggeredMode;
    }

    public void setTriggeredMode(TriggeredMode triggeredMode) {
        this.triggeredMode = triggeredMode;
    }
    //endregion --- getter & setter ---

    @Override
    protected void onRotate(BlockState state) {
        if (state.getBlock() instanceof RotatedBlockWithEntity) {
            if (state.get(RotatedBlockWithEntity.ROTATED) != this.rotated) {
                BlockRotation blockRotation = BlockRotationUtils.calculateRotationFromDifferentRotatedStates(state.get(RotatedBlockWithEntity.ROTATED), this.rotated);

                MutablePair<BlockPos, Vec3i> offsetArea = BlockRotationUtils.rotateOffsetArea(this.areaPositionOffset, this.areaDimensions, blockRotation);
                this.areaPositionOffset = offsetArea.getLeft();
                this.areaDimensions = offsetArea.getRight();

                this.triggeredBlock.setLeft(BlockRotationUtils.rotateOffsetBlockPos(this.triggeredBlock.getLeft(), blockRotation));

                this.rotated = state.get(RotatedBlockWithEntity.ROTATED);
            }
            if (state.get(RotatedBlockWithEntity.X_MIRRORED) != this.x_mirrored) {

                MutablePair<BlockPos, Vec3i> offsetArea = BlockRotationUtils.mirrorOffsetArea(this.areaPositionOffset, this.areaDimensions, BlockMirror.FRONT_BACK);
                this.areaPositionOffset = offsetArea.getLeft();
                this.areaDimensions = offsetArea.getRight();

                this.triggeredBlock.setLeft(BlockRotationUtils.mirrorOffsetBlockPos(this.triggeredBlock.getLeft(), BlockMirror.FRONT_BACK));

                this.x_mirrored = state.get(RotatedBlockWithEntity.X_MIRRORED);
            }
            if (state.get(RotatedBlockWithEntity.Z_MIRRORED) != this.z_mirrored) {

                MutablePair<BlockPos, Vec3i> offsetArea = BlockRotationUtils.mirrorOffsetArea(this.areaPositionOffset, this.areaDimensions, BlockMirror.LEFT_RIGHT);
                this.areaPositionOffset = offsetArea.getLeft();
                this.areaDimensions = offsetArea.getRight();

                this.triggeredBlock.setLeft(BlockRotationUtils.mirrorOffsetBlockPos(this.triggeredBlock.getLeft(), BlockMirror.LEFT_RIGHT));

                this.z_mirrored = state.get(RotatedBlockWithEntity.Z_MIRRORED);
            }
        }
    }

    public static enum MessageMode implements StringIdentifiable
    {
        ANNOUNCEMENT("announcement"),
        CHAT("chat"),
        OVERLAY("overlay");

        private final String name;

        private MessageMode(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        public static Optional<MessageMode> byName(String name) {
            return Arrays.stream(MessageMode.values()).filter(messageMode -> messageMode.asString().equals(name)).findFirst();
        }

        public Text asText() {
            return Text.translatable("gui.area_block.message_mode." + this.name);
        }
    }

    public static enum TriggerMode implements StringIdentifiable
    {
        ALWAYS("always"),
        ONCE("once"),
        TIMED("timed");

        private final String name;

        private TriggerMode(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        public static Optional<TriggerMode> byName(String name) {
            return Arrays.stream(TriggerMode.values()).filter(triggerMode -> triggerMode.asString().equals(name)).findFirst();
        }

        public Text asText() {
            return Text.translatable("gui.area_block.trigger_mode." + this.name);
        }
    }

    public static enum TriggeredMode implements StringIdentifiable
    {
        CONTINUOUS("continuous"),
        ONCE("once");

        private final String name;

        private TriggeredMode(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        public static Optional<TriggeredMode> byName(String name) {
            return Arrays.stream(TriggeredMode.values()).filter(triggeredMode -> triggeredMode.asString().equals(name)).findFirst();
        }

        public Text asText() {
            return Text.translatable("gui.area_block.triggered_mode." + this.name);
        }
    }
}
