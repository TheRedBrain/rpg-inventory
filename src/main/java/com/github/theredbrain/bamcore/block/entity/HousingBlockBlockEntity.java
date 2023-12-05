package com.github.theredbrain.bamcore.block.entity;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.api.util.BlockRotationUtils;
import com.github.theredbrain.bamcore.api.util.UUIDUtilities;
import com.github.theredbrain.bamcore.block.RotatedBlockWithEntity;
import com.github.theredbrain.bamcore.block.Triggerable;
import com.github.theredbrain.bamcore.registry.ComponentsRegistry;
import com.github.theredbrain.bamcore.registry.EntityRegistry;
import com.github.theredbrain.bamcore.registry.StatusEffectsRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.*;

public class HousingBlockBlockEntity extends RotatedBlockEntity {
    private String owner = "";
    private boolean isOwnerSet = false;
    private List<String> coOwnerList = new ArrayList<>(List.of());
    private List<String> trustedList = new ArrayList<>(List.of());
    private List<String> guestList = new ArrayList<>(List.of());
    private boolean showInfluenceArea = false;
    private Vec3i influenceAreaDimensions = Vec3i.ZERO;
    private BlockPos influenceAreaPositionOffset = new BlockPos(0, 1, 0);
    private HousingBlockBlockEntity.OwnerMode ownerMode = OwnerMode.DIMENSION_OWNER; // 0: dimension owner, 1: first interaction
    private BlockPos triggeredBlockPositionOffset = new BlockPos(0, 1, 0);
    private MutablePair<BlockPos, MutablePair<Double, Double>> entrance = new MutablePair<>(new BlockPos(0, 1, 0), new MutablePair<>(0.0, 0.0));
    public HousingBlockBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.HOUSING_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putString("ownerUuid", this.owner);
        nbt.putBoolean("isOwnerSet", isOwnerSet);

        int coOwnerListSize = coOwnerList.size();
        nbt.putInt("coOwnerListSize", coOwnerListSize);
        for (int i = 0; i<coOwnerListSize; i++) {
            nbt.putString("coOwnerListEntry" + i, this.coOwnerList.get(i));
        }

        int trustedListSize = trustedList.size();
        nbt.putInt("trustedListSize", trustedListSize);
        for (int i = 0; i<trustedListSize; i++) {
            nbt.putString("trustedListEntry" + i, this.trustedList.get(i));
        }

        int guestListSize = guestList.size();
        nbt.putInt("guestListSize", guestListSize);
        for (int i = 0; i<guestListSize; i++) {
            nbt.putString("guestListEntry" + i, this.guestList.get(i));
        }

        nbt.putBoolean("showInfluenceArea", this.showInfluenceArea);

        nbt.putInt("influenceAreaDimensionsX", this.influenceAreaDimensions.getX());
        nbt.putInt("influenceAreaDimensionsY", this.influenceAreaDimensions.getY());
        nbt.putInt("influenceAreaDimensionsZ", this.influenceAreaDimensions.getZ());

        nbt.putInt("influenceAreaPositionOffsetX", this.influenceAreaPositionOffset.getX());
        nbt.putInt("influenceAreaPositionOffsetY", this.influenceAreaPositionOffset.getY());
        nbt.putInt("influenceAreaPositionOffsetZ", this.influenceAreaPositionOffset.getZ());

        nbt.putString("ownerMode", this.ownerMode.asString());

        nbt.putInt("triggeredBlockPositionOffsetX", this.triggeredBlockPositionOffset.getX());
        nbt.putInt("triggeredBlockPositionOffsetY", this.triggeredBlockPositionOffset.getY());
        nbt.putInt("triggeredBlockPositionOffsetZ", this.triggeredBlockPositionOffset.getZ());

        nbt.putInt("entrance_X", this.entrance.getLeft().getX());
        nbt.putInt("entrance_Y", this.entrance.getLeft().getY());
        nbt.putInt("entrance_Z", this.entrance.getLeft().getZ());
        nbt.putDouble("entrance_Yaw", this.entrance.getRight().getLeft());
        nbt.putDouble("entrance_Pitch", this.entrance.getRight().getRight());

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.owner = nbt.getString("ownerUuid");
        this.isOwnerSet = nbt.getBoolean("isOwnerSet");

        this.coOwnerList = new ArrayList<>(List.of());
        this.trustedList = new ArrayList<>(List.of());
        this.guestList = new ArrayList<>(List.of());

        int coOwnerListSize = nbt.getInt("coOwnerListSize");
        for (int i = 0; i<coOwnerListSize; i++) {
            this.coOwnerList.add(nbt.getString("coOwnerListEntry" + i));
        }

        int trustedListSize = nbt.getInt("trustedListSize");
        for (int i = 0; i<trustedListSize; i++) {
            this.trustedList.add(nbt.getString("trustedListEntry" + i));
        }

        int guestListSize = nbt.getInt("guestListSize");
        for (int i = 0; i<guestListSize; i++) {
            this.guestList.add(nbt.getString("guestListEntry" + i));
        }

        this.showInfluenceArea = nbt.getBoolean("showInfluenceArea");

        int i = MathHelper.clamp(nbt.getInt("influenceAreaDimensionsX"), 0, 48);
        int j = MathHelper.clamp(nbt.getInt("influenceAreaDimensionsY"), 0, 48);
        int k = MathHelper.clamp(nbt.getInt("influenceAreaDimensionsZ"), 0, 48);
        this.influenceAreaDimensions = new Vec3i(i, j, k);

        int l = MathHelper.clamp(nbt.getInt("influenceAreaPositionOffsetX"), -48, 48);
        int m = MathHelper.clamp(nbt.getInt("influenceAreaPositionOffsetY"), -48, 48);
        int n = MathHelper.clamp(nbt.getInt("influenceAreaPositionOffsetZ"), -48, 48);
        this.influenceAreaPositionOffset = new BlockPos(l, m, n);

        this.ownerMode = HousingBlockBlockEntity.OwnerMode.byName(nbt.getString("ownerMode")).orElseGet(() -> OwnerMode.DIMENSION_OWNER);

        int o = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetX"), -48, 48);
        int p = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetY"), -48, 48);
        int q = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetZ"), -48, 48);
        this.triggeredBlockPositionOffset = new BlockPos(o, p, q);

        int entrance_X = nbt.getInt("entrance_X");
        int entrance_Y = nbt.getInt("entrance_Y");
        int entrance_Z = nbt.getInt("entrance_Z");
        this.entrance.setLeft(new BlockPos(entrance_X, entrance_Y, entrance_Z));
        double entrance_Yaw = nbt.getDouble("entrance_Yaw");
        double entrance_Pitch = nbt.getDouble("entrance_Pitch");
        this.entrance.setRight(new MutablePair<>(entrance_Yaw, entrance_Pitch));

        super.readNbt(nbt);
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }

    public static void tick(World world, BlockPos pos, BlockState state, HousingBlockBlockEntity blockEntity) {
        if (world.getTime() % 20L == 0L) {
            if (blockEntity.hasWorld() && !blockEntity.isOwnerSet) {
                if (Objects.equals(blockEntity.owner, "") && blockEntity.ownerMode == OwnerMode.DIMENSION_OWNER) {
                    blockEntity.owner = initOwner(blockEntity.world);
                    BetterAdventureModeCore.LOGGER.info(blockEntity.owner);
                    blockEntity.isOwnerSet = true;
                }
            }

            Box box = new Box(
                    blockEntity.pos.getX() + blockEntity.influenceAreaPositionOffset.getX(),
                    blockEntity.pos.getY() + blockEntity.influenceAreaPositionOffset.getY(),
                    blockEntity.pos.getZ() + blockEntity.influenceAreaPositionOffset.getZ(),
                    blockEntity.pos.getX() + blockEntity.influenceAreaPositionOffset.getX() + blockEntity.influenceAreaDimensions.getX(),
                    blockEntity.pos.getY() + blockEntity.influenceAreaPositionOffset.getY() + blockEntity.influenceAreaDimensions.getY(),
                    blockEntity.pos.getZ() + blockEntity.influenceAreaPositionOffset.getZ() + blockEntity.influenceAreaDimensions.getZ()
            );
            List<PlayerEntity> list = world.getNonSpectatingEntities(PlayerEntity.class, box);
            Iterator var11 = list.iterator();

            PlayerEntity playerEntity;
            while (var11.hasNext()) {
                playerEntity = (PlayerEntity) var11.next();

                String playerName = playerEntity.getName().getString();
                String playerUuid = playerEntity.getUuidAsString();
                if (Objects.equals(playerUuid, blockEntity.getOwner())/* || playerEntity.isCreative()*/) {
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.HOUSING_OWNER_EFFECT, 100, 0, true, false, true));
                } else if (blockEntity.getCoOwnerList().contains(playerName)) {
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.HOUSING_CO_OWNER_EFFECT, 100, 0, true, false, true));
                } else if (blockEntity.getTrustedList().contains(playerName)) {
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.HOUSING_TRUSTED_EFFECT, 100, 0, true, false, true));
                } else if (blockEntity.getGuestList().contains(playerName)) {
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.HOUSING_GUEST_EFFECT, 100, 0, true, false, true));
                } else {
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.HOUSING_STRANGER_EFFECT, 100, 0, true, false, true));
                }
                ComponentsRegistry.CURRENT_HOUSING_BLOCK_POS.get(playerEntity).setValue(blockEntity.pos);
            }
        }
    }

    public MutablePair<BlockPos, MutablePair<Double, Double>> getTargetEntrance() {
        return new MutablePair<>(this.entrance.getLeft().add(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ())/*new BlockPos(this.entrance.getLeft().getX() + this.getPos().getX(), this.entrance.getLeft().getY() + this.getPos().getY(), this.entrance.getLeft().getZ() + this.getPos().getZ())*/, this.entrance.getRight());
    }

    public String getOwner() {
        return this.owner;
    }

    public boolean setOwner(String owner) {
        this.owner = owner;
        return true;
    }

    public List<String> getCoOwnerList() {
        return this.coOwnerList;
    }

    public boolean setCoOwnerList(List<String> coOwnerList) {
        this.coOwnerList = coOwnerList;
        return true;
    }

    public List<String> getTrustedList() {
        return this.trustedList;
    }

    public boolean setTrustedList(List<String> trustedList) {
        this.trustedList = trustedList;
        return true;
    }

    public List<String> getGuestList() {
        return this.guestList;
    }

    public boolean setGuestList(List<String> guestList) {
        this.guestList = guestList;
        return true;
    }

    public boolean getShowInfluenceArea() {
        return this.showInfluenceArea;
    }

    // TODO check if input is valid
    public boolean setShowInfluenceArea(boolean showInfluenceArea) {
        this.showInfluenceArea = showInfluenceArea;
        return true;
    }

    public Vec3i getInfluenceAreaDimensions() {
        return this.influenceAreaDimensions;
    }

    // TODO check if input is valid
    public boolean setInfluenceAreaDimensions(Vec3i influenceAreaDimensions) {
        this.influenceAreaDimensions = influenceAreaDimensions;
        return true;
    }

    public BlockPos getRestrictBlockBreakingAreaPositionOffset() {
        return this.influenceAreaPositionOffset;
    }

    // TODO check if input is valid
    public boolean setRestrictBlockBreakingAreaPositionOffset(BlockPos influenceAreaPositionOffset) {
        this.influenceAreaPositionOffset = influenceAreaPositionOffset;
        return true;
    }

    public BlockPos getTriggeredBlockPositionOffset() {
        return this.triggeredBlockPositionOffset;
    }

    // TODO check if input is valid
    public boolean setTriggeredBlockPositionOffset(BlockPos triggeredBlockPositionOffset) {
        this.triggeredBlockPositionOffset = triggeredBlockPositionOffset;
        return true;
    }

    public HousingBlockBlockEntity.OwnerMode getOwnerMode() {
        return this.ownerMode;
    }

    // TODO check if input is valid
    public boolean setOwnerMode(HousingBlockBlockEntity.OwnerMode ownerMode) {
        this.ownerMode = ownerMode;
        return true;
    }

    public MutablePair<BlockPos, MutablePair<Double, Double>> getEntrance() {
        return entrance;
    }

    public boolean setEntrance(MutablePair<BlockPos, MutablePair<Double, Double>> entrance) {
        this.entrance = entrance;
        return true;
    }

    public void setIsOwnerSet(boolean isOwnerSet) {
        this.isOwnerSet = isOwnerSet;
    }

    private static String initOwner(World world) {
        if (world != null) {
            String worldRegistryKey = world.getRegistryKey().getValue().getPath();
            BetterAdventureModeCore.LOGGER.info("initOwner worldRegistryKey: " + worldRegistryKey);
            String[] parts = worldRegistryKey.split("_");
            if (world.getServer() != null) {
                String uuidString = parts[0];
                BetterAdventureModeCore.LOGGER.info("initOwner uuidString: " + uuidString);
                if (UUIDUtilities.isStringValidUUID(uuidString)) {
                    PlayerEntity playerEntity = world.getServer().getPlayerManager().getPlayer(UUID.fromString(uuidString));
                    if (playerEntity != null) {
                        String ownerName = playerEntity.getName().getString();
                        BetterAdventureModeCore.LOGGER.info("initOwner ownerName: " + ownerName);
                        return ownerName;
                    }
                }
            }
        }
        return "";
    }

    public boolean influenceAreaContains(BlockPos pos) {
        return (double)(pos.getX() + 1) > (this.pos.getX() + this.influenceAreaPositionOffset.getX())
                && (double)pos.getX() < (this.pos.getX() + this.influenceAreaPositionOffset.getX() + this.influenceAreaDimensions.getX())
                && (double)(pos.getY() + 1) > (this.pos.getY() + this.influenceAreaPositionOffset.getY())
                && (double)pos.getY() < (this.pos.getY() + this.influenceAreaPositionOffset.getY() + this.influenceAreaDimensions.getY())
                && (double)(pos.getZ() + 1) > (this.pos.getZ() + this.influenceAreaPositionOffset.getZ())
                && (double)pos.getZ() < (this.pos.getZ() + this.influenceAreaPositionOffset.getZ() + this.influenceAreaDimensions.getZ());
    }

    public void trigger() {
        if (this.world != null) {
            BlockEntity blockEntity = world.getBlockEntity(new BlockPos(this.pos.getX() + this.triggeredBlockPositionOffset.getX(), this.pos.getY() + this.triggeredBlockPositionOffset.getY(), this.pos.getZ() + this.triggeredBlockPositionOffset.getZ()));
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
                this.triggeredBlockPositionOffset = BlockRotationUtils.rotateOffsetBlockPos(this.triggeredBlockPositionOffset, blockRotation);
                this.influenceAreaPositionOffset = BlockRotationUtils.rotateOffsetBlockPos(this.influenceAreaPositionOffset, blockRotation);
                this.influenceAreaDimensions = BlockRotationUtils.rotateOffsetVec3i(this.influenceAreaDimensions, blockRotation);
                this.entrance = BlockRotationUtils.rotateEntrance(this.entrance, blockRotation);
                this.rotated = state.get(RotatedBlockWithEntity.ROTATED);
            }
            if (state.get(RotatedBlockWithEntity.X_MIRRORED) != this.x_mirrored) {
                this.triggeredBlockPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.triggeredBlockPositionOffset, BlockMirror.FRONT_BACK);
                this.influenceAreaPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.influenceAreaPositionOffset, BlockMirror.FRONT_BACK);
                this.influenceAreaDimensions = BlockRotationUtils.mirrorOffsetVec3i(this.influenceAreaDimensions, BlockMirror.FRONT_BACK);
                this.entrance = BlockRotationUtils.mirrorEntrance(this.entrance, BlockMirror.FRONT_BACK);
                this.x_mirrored = state.get(RotatedBlockWithEntity.X_MIRRORED);
            }
            if (state.get(RotatedBlockWithEntity.Z_MIRRORED) != this.z_mirrored) {
                this.triggeredBlockPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.triggeredBlockPositionOffset, BlockMirror.LEFT_RIGHT);
                this.influenceAreaPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.influenceAreaPositionOffset, BlockMirror.LEFT_RIGHT);
                this.influenceAreaDimensions = BlockRotationUtils.mirrorOffsetVec3i(this.influenceAreaDimensions, BlockMirror.LEFT_RIGHT);
                this.entrance = BlockRotationUtils.mirrorEntrance(this.entrance, BlockMirror.LEFT_RIGHT);
                this.z_mirrored = state.get(RotatedBlockWithEntity.Z_MIRRORED);
            }
        }
    }

    public static enum OwnerMode implements StringIdentifiable
    {
        DIMENSION_OWNER("dimension_owner"),
        INTERACTION("interaction");

        private final String name;

        private OwnerMode(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        public static Optional<HousingBlockBlockEntity.OwnerMode> byName(String name) {
            return Arrays.stream(HousingBlockBlockEntity.OwnerMode.values()).filter(ownerMode -> ownerMode.asString().equals(name)).findFirst();
        }

        public Text asText() {
            return Text.translatable("gui.housing_block.ownerMode." + this.name);
        }
    }
}
