package com.github.theredbrain.bamcore.block.entity;

import com.github.theredbrain.bamcore.api.util.BlockRotationUtils;
import com.github.theredbrain.bamcore.block.RotatedBlockWithEntity;
import com.github.theredbrain.bamcore.registry.StatusEffectsRegistry;
import com.github.theredbrain.bamcore.registry.BlockRegistry;
import com.github.theredbrain.bamcore.registry.EntityRegistry;
import com.github.theredbrain.bamcore.screen.TeleporterBlockScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Pair;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.MutablePair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TeleporterBlockBlockEntity extends RotatedBlockEntity implements ExtendedScreenHandlerFactory, Inventory {

    private boolean showAdventureScreen = true; //
    private boolean showActivationArea = false;
    private Vec3i activationAreaDimensions = Vec3i.ZERO;
    private BlockPos activationAreaPositionOffset = new BlockPos(0, 1, 0);
    private BlockPos accessPositionOffset = new BlockPos(0, 0, 0);

    private boolean setAccessPosition = false; //

    private TeleportationMode teleportationMode = TeleportationMode.DIRECT;

    // direct teleportation mode
    private BlockPos directTeleportPositionOffset = new BlockPos(0, 0, 0);
    private double directTeleportOrientationYaw = 0.0;
    private double directTeleportOrientationPitch = 0.0;

    // specific location mode
    private SpawnPointType spawnPointType = SpawnPointType.WORLD_SPAWN;

    // dungeon mode
    private List<Pair<String, String>> locationsList = new ArrayList<>(List.of());

    // player house mode
    private List<String> housingLocationsList = new ArrayList<>(List.of());

    private boolean consumeKeyItemStack = false;
    private DefaultedList<ItemStack> requiredKeyItemStack = DefaultedList.ofSize(1, ItemStack.EMPTY);

    private String teleporterName = "gui.teleporter_block.teleporter_name_field.label";
    private String currentTargetOwnerLabel = "gui.teleporter_block.target_owner_field.label";
    private String currentTargetIdentifierLabel = "gui.teleporter_block.target_identifier_field.label";
    private String currentTargetEntranceLabel = "gui.teleporter_block.target_entrance_field.label";
    private String teleportButtonLabel = "gui.teleporter_block.teleport_button.label";
    private String cancelTeleportButtonLabel = "gui.teleporter_block.cancel_teleport_button.label";

    public TeleporterBlockBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.TELEPORTER_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putString("teleporterName", this.teleporterName);

        nbt.putBoolean("showAdventureScreen", this.showAdventureScreen);

        nbt.putBoolean("showActivationArea", this.showActivationArea);

        nbt.putInt("activationAreaDimensionsX", this.activationAreaDimensions.getX());
        nbt.putInt("activationAreaDimensionsY", this.activationAreaDimensions.getY());
        nbt.putInt("activationAreaDimensionsZ", this.activationAreaDimensions.getZ());

        nbt.putInt("activationAreaPositionOffsetX", this.activationAreaPositionOffset.getX());
        nbt.putInt("activationAreaPositionOffsetY", this.activationAreaPositionOffset.getY());
        nbt.putInt("activationAreaPositionOffsetZ", this.activationAreaPositionOffset.getZ());

        nbt.putInt("accessPositionOffsetX", this.accessPositionOffset.getX());
        nbt.putInt("accessPositionOffsetY", this.accessPositionOffset.getY());
        nbt.putInt("accessPositionOffsetZ", this.accessPositionOffset.getZ());

        nbt.putBoolean("setAccessPosition", this.setAccessPosition);

        nbt.putString("teleportationMode", this.teleportationMode.asString());

        nbt.putInt("directTeleportPositionOffsetX", this.directTeleportPositionOffset.getX());
        nbt.putInt("directTeleportPositionOffsetY", this.directTeleportPositionOffset.getY());
        nbt.putInt("directTeleportPositionOffsetZ", this.directTeleportPositionOffset.getZ());
        nbt.putDouble("directTeleportPositionOffsetYaw", this.directTeleportOrientationYaw);
        nbt.putDouble("directTeleportPositionOffsetPitch", this.directTeleportOrientationPitch);

        nbt.putString("locationType", this.spawnPointType.asString());

        nbt.putInt("locationsListSize", this.locationsList.size());

        for (int i = 0; i < this.locationsList.size(); i++) {
            nbt.putString("locationsListIdentifier_" + i, this.locationsList.get(i).getLeft());
            nbt.putString("locationsListEntrance_" + i, this.locationsList.get(i).getRight());
        }

        nbt.putBoolean("consumeKeyItemStack", this.consumeKeyItemStack);
        Inventories.writeNbt(nbt, this.requiredKeyItemStack);

        nbt.putString("currentTargetOwnerLabel", this.currentTargetOwnerLabel);
        nbt.putString("currentTargetIdentifierLabel", this.currentTargetIdentifierLabel);
        nbt.putString("currentTargetEntranceLabel", this.currentTargetEntranceLabel);
        nbt.putString("teleportButtonLabel", this.teleportButtonLabel);
        nbt.putString("cancelTeleportButtonLabel", this.cancelTeleportButtonLabel);

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.teleporterName = nbt.getString("teleporterName");

        this.showAdventureScreen = nbt.getBoolean("showAdventureScreen");

        this.showActivationArea = nbt.getBoolean("showActivationArea");

        int i = MathHelper.clamp(nbt.getInt("activationAreaDimensionsX"), 0, 48);
        int j = MathHelper.clamp(nbt.getInt("activationAreaDimensionsY"), 0, 48);
        int k = MathHelper.clamp(nbt.getInt("activationAreaDimensionsZ"), 0, 48);
        this.activationAreaDimensions = new Vec3i(i, j, k);

        int l = MathHelper.clamp(nbt.getInt("activationAreaPositionOffsetX"), -48, 48);
        int m = MathHelper.clamp(nbt.getInt("activationAreaPositionOffsetY"), -48, 48);
        int n = MathHelper.clamp(nbt.getInt("activationAreaPositionOffsetZ"), -48, 48);
        this.activationAreaPositionOffset = new BlockPos(l, m, n);

        int q = MathHelper.clamp(nbt.getInt("accessPositionOffsetX"), -48, 48);
        int r = MathHelper.clamp(nbt.getInt("accessPositionOffsetY"), -48, 48);
        int s = MathHelper.clamp(nbt.getInt("accessPositionOffsetZ"), -48, 48);
        this.accessPositionOffset = new BlockPos(q, r, s);

        this.setAccessPosition = nbt.getBoolean("setAccessPosition");

        this.teleportationMode = TeleportationMode.byName(nbt.getString("teleportationMode")).orElseGet(() -> TeleportationMode.DIRECT);

        this.directTeleportPositionOffset = new BlockPos(
                nbt.getInt("directTeleportPositionOffsetX"),
                nbt.getInt("directTeleportPositionOffsetY"),
                nbt.getInt("directTeleportPositionOffsetZ")
        );
        this.directTeleportOrientationYaw = nbt.getDouble("directTeleportPositionOffsetYaw");
        this.directTeleportOrientationPitch = nbt.getDouble("directTeleportPositionOffsetPitch");

        this.spawnPointType = SpawnPointType.byName(nbt.getString("locationType")).orElseGet(() -> SpawnPointType.WORLD_SPAWN);

        int locationsListSize = nbt.getInt("locationsListSize");
        this.locationsList.clear();
        for (int p = 0; p < locationsListSize; p++) {
            this.locationsList.add(new Pair<>(nbt.getString("locationsListIdentifier_" + p), nbt.getString("locationsListEntrance_" + p)));
        }

        this.consumeKeyItemStack = nbt.getBoolean("consumeKeyItemStack");
        this.requiredKeyItemStack = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.requiredKeyItemStack);

        this.currentTargetOwnerLabel = nbt.getString("currentTargetOwnerLabel");
        this.currentTargetIdentifierLabel = nbt.getString("currentTargetIdentifierLabel");
        this.currentTargetEntranceLabel = nbt.getString("currentTargetEntranceLabel");
        this.teleportButtonLabel = nbt.getString("teleportButtonLabel");
        this.cancelTeleportButtonLabel = nbt.getString("cancelTeleportButtonLabel");

        super.readNbt(nbt);
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }

    public static void tick(World world, BlockPos pos, BlockState state, TeleporterBlockBlockEntity blockEntity) {

        if (!world.isClient && world.getTime() % 20L == 0L) {
            TeleporterBlockBlockEntity.tryOpenScreenRemotely(world, pos, state, blockEntity);
        }
    }

    private static void tryOpenScreenRemotely(World world, BlockPos pos, BlockState state, TeleporterBlockBlockEntity blockEntity) {
        if (world.isClient) {
            return;
        }
        if (state.isOf(BlockRegistry.TELEPORTER_BLOCK) && world.getBlockEntity(pos) != null && world.getBlockEntity(pos).getType() == blockEntity.getType()) {
            BlockPos activationAreaPositionOffset = blockEntity.getActivationAreaPositionOffset();
            Vec3i activationAreaDimensions = blockEntity.getActivationAreaDimensions();
            BlockPos activationAreaStart = new BlockPos(pos.getX() + activationAreaPositionOffset.getX(), pos.getY() + activationAreaPositionOffset.getY(), pos.getZ() + activationAreaPositionOffset.getZ());
            BlockPos activationAreaEnd = new BlockPos(activationAreaStart.getX() + activationAreaDimensions.getX(), activationAreaStart.getY() + activationAreaDimensions.getY(), activationAreaStart.getZ() + activationAreaDimensions.getZ());
            Box activationArea = new Box(activationAreaStart, activationAreaEnd);
            List<PlayerEntity> list = world.getNonSpectatingEntities(PlayerEntity.class, activationArea);
            for (PlayerEntity playerEntity : list) {
                if (!playerEntity.hasStatusEffect(StatusEffectsRegistry.PORTAL_RESISTANCE_EFFECT)) {
                    // prevents continuous opening of a screen
                    playerEntity.setStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.PORTAL_RESISTANCE_EFFECT, -1), playerEntity);
                    playerEntity.openHandledScreen(state.createScreenHandlerFactory(world, pos));
                }
            }
        }
    }

    //region --- getter & setter ---
    public String getTeleporterName() {
        return teleporterName;
    }

    // TODO check if input is valid
    public boolean setTeleporterName(String teleporterName) {
        this.teleporterName = teleporterName;
        return true;
    }

    /**
     * Determines whether a pop-up window is shown where the player can confirm or deny the teleport.
     * Has an effect only in some modes
     */
    public boolean getShowAdventureScreen() {
        return showAdventureScreen;
    }

    // TODO check if input is valid
    public boolean setShowAdventureScreen(boolean showAdventureScreen) {
        this.showAdventureScreen = showAdventureScreen;
        return true;
    }

    public boolean getShowActivationArea() {
        return showActivationArea;
    }

    // TODO check if input is valid
    public boolean setShowActivationArea(boolean showActivationArea) {
        this.showActivationArea = showActivationArea;
        return true;
    }

    public Vec3i getActivationAreaDimensions() {
        return activationAreaDimensions;
    }

    // TODO check if input is valid
    public boolean setActivationAreaDimensions(Vec3i activationAreaDimensions) {
        this.activationAreaDimensions = activationAreaDimensions;
        return true;
    }

    public BlockPos getActivationAreaPositionOffset() {
        return activationAreaPositionOffset;
    }

    // TODO check if input is valid
    public boolean setActivationAreaPositionOffset(BlockPos activationAreaPositionOffset) {
        this.activationAreaPositionOffset = activationAreaPositionOffset;
        return true;
    }

    public BlockPos getAccessPositionOffset() {
        return accessPositionOffset;
    }

    // TODO check if input is valid
    public boolean setAccessPositionOffset(BlockPos accessPositionOffset) {
        this.accessPositionOffset = accessPositionOffset;
        return true;
    }

    /**
     * Determines whether the access position of the player should be updated.
     * Has an effect only in some modes
     */
    public boolean getSetAccessPosition() {
        return setAccessPosition;
    }

    // TODO check if input is valid
    public boolean setSetAccessPosition(boolean setAccessPosition) {
        this.setAccessPosition = setAccessPosition;
        return true;
    }

    public TeleportationMode getTeleportationMode() {
        return teleportationMode;
    }

    // TODO check if input is valid
    public boolean setTeleportationMode(TeleportationMode teleportationMode) {
        this.teleportationMode = teleportationMode;
        return true;
    }

    public BlockPos getDirectTeleportPositionOffset() {
        return directTeleportPositionOffset;
    }

    // TODO check if input is valid
    public boolean setDirectTeleportPositionOffset(BlockPos directTeleportPositionOffset) {
        this.directTeleportPositionOffset = directTeleportPositionOffset;
        return true;
    }

    public double getDirectTeleportOrientationYaw() {
        return directTeleportOrientationYaw;
    }

    // TODO check if input is valid
    public boolean setDirectTeleportOrientationYaw(double directTeleportOrientationYaw) {
        this.directTeleportOrientationYaw = directTeleportOrientationYaw;
        return true;
    }

    public double getDirectTeleportOrientationPitch() {
        return directTeleportOrientationPitch;
    }

    // TODO check if input is valid
    public boolean setDirectTeleportOrientationPitch(double directTeleportOrientationPitch) {
        this.directTeleportOrientationPitch = directTeleportOrientationPitch;
        return true;
    }

    public SpawnPointType getLocationType() {
        return spawnPointType;
    }

    // TODO check if input is valid
    public boolean setLocationType(SpawnPointType spawnPointType) {
        this.spawnPointType = spawnPointType;
        return true;
    }

    public List<Pair<String, String>> getLocationsList() {
        return this.locationsList;
    }

    // TODO check if input is valid
    public boolean setLocationsList(List<Pair<String, String>> dungeonLocationsList) {
        this.locationsList = dungeonLocationsList;
        return true;
    }

    public boolean getConsumeKeyItemStack() {
        return consumeKeyItemStack;
    }

    // TODO check if input is valid
    public boolean setConsumeKeyItemStack(boolean consumeKeyItemStack) {
        this.consumeKeyItemStack = consumeKeyItemStack;
        return true;
    }

//    public boolean isKeyItemStackSlotVisible() {
//        return !(requiredKeyItemStack.get(0).isEmpty());
//    }
//
//    public DefaultedList<ItemStack> getRequiredKeyItemStack() {
//        return requiredKeyItemStack;
//    }
//
//    public void setRequiredKeyItemStack(DefaultedList<ItemStack> requiredKeyItemStack) {
//        this.requiredKeyItemStack = requiredKeyItemStack;
//    }

    public String getCurrentTargetOwnerLabel() {
        return this.currentTargetOwnerLabel;
    }

    // TODO check if input is valid
    public boolean setCurrentTargetOwnerLabel(String currentTargetOwnerLabel) {
        this.currentTargetOwnerLabel = currentTargetOwnerLabel;
        return true;
    }

    public String getCurrentTargetIdentifierLabel() {
        return this.currentTargetIdentifierLabel;
    }

    // TODO check if input is valid
    public boolean setCurrentTargetIdentifierLabel(String currentTargetIdentifierLabel) {
        this.currentTargetIdentifierLabel = currentTargetIdentifierLabel;
        return true;
    }

    public String getCurrentTargetEntranceLabel() {
        return this.currentTargetEntranceLabel;
    }

    // TODO check if input is valid
    public boolean setCurrentTargetEntranceLabel(String currentTargetEntranceLabel) {
        this.currentTargetEntranceLabel = currentTargetEntranceLabel;
        return true;
    }

    public String getTeleportButtonLabel() {
        return this.teleportButtonLabel;
    }

    // TODO check if input is valid
    public boolean setTeleportButtonLabel(String teleportButtonLabel) {
        this.teleportButtonLabel = teleportButtonLabel;
        return true;
    }

    public String getCancelTeleportButtonLabel() {
        return this.cancelTeleportButtonLabel;
    }

    // TODO check if input is valid
    public boolean setCancelTeleportButtonLabel(String cancelTeleportButtonLabel) {
        this.cancelTeleportButtonLabel = cancelTeleportButtonLabel;
        return true;
    }
    //endregion

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        if (player.isCreativeLevelTwoOp()) {
            return new TeleporterBlockScreenHandler(syncId, playerInventory, this, true);
        } else {
            return new TeleporterBlockScreenHandler(syncId, playerInventory, this, false);
        }
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(this.teleporterName);
    }

    @Override
    public int size() {
        return this.requiredKeyItemStack.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : this.requiredKeyItemStack) {
            if (itemStack.isEmpty()) continue;
            return false;
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.requiredKeyItemStack.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack itemStack = Inventories.splitStack(this.requiredKeyItemStack, slot, amount);
        if (!itemStack.isEmpty()) {
            this.markDirty();
        }
        return itemStack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack itemStack = this.requiredKeyItemStack.get(slot);
        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        this.requiredKeyItemStack.set(slot, ItemStack.EMPTY);
        return itemStack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.requiredKeyItemStack.set(slot, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }
        this.markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return Inventory.canPlayerUse(this, player, 48);
    }

    @Override
    public void clear() {
        this.requiredKeyItemStack.clear();
        this.markDirty();
    }

    @Override
    protected void onRotate(BlockState state) {
        if (state.getBlock() instanceof RotatedBlockWithEntity) {
            if (state.get(RotatedBlockWithEntity.ROTATED) != this.rotated) {
                BlockRotation blockRotation = BlockRotationUtils.calculateRotationFromDifferentRotatedStates(state.get(RotatedBlockWithEntity.ROTATED), this.rotated);
                this.accessPositionOffset = BlockRotationUtils.rotateOffsetBlockPos(this.accessPositionOffset, blockRotation);
                this.directTeleportPositionOffset = BlockRotationUtils.rotateOffsetBlockPos(this.directTeleportPositionOffset, blockRotation);

                this.directTeleportOrientationYaw = BlockRotationUtils.rotateYaw(this.directTeleportOrientationYaw, blockRotation);

                MutablePair<BlockPos, Vec3i> offsetArea = BlockRotationUtils.rotateOffsetArea(this.activationAreaPositionOffset, this.activationAreaDimensions, blockRotation);
                this.activationAreaPositionOffset = offsetArea.getLeft();
                this.activationAreaDimensions = offsetArea.getRight();

                this.rotated = state.get(RotatedBlockWithEntity.ROTATED);
            }
            if (state.get(RotatedBlockWithEntity.X_MIRRORED) != this.x_mirrored) {
                this.accessPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.accessPositionOffset, BlockMirror.FRONT_BACK);
                this.directTeleportPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.directTeleportPositionOffset, BlockMirror.FRONT_BACK);

                this.directTeleportOrientationYaw = BlockRotationUtils.mirrorYaw(this.directTeleportOrientationYaw, BlockMirror.FRONT_BACK);

                MutablePair<BlockPos, Vec3i> offsetArea = BlockRotationUtils.mirrorOffsetArea(this.activationAreaPositionOffset, this.activationAreaDimensions, BlockMirror.FRONT_BACK);
                this.activationAreaPositionOffset = offsetArea.getLeft();
                this.activationAreaDimensions = offsetArea.getRight();

                this.x_mirrored = state.get(RotatedBlockWithEntity.X_MIRRORED);
            }
            if (state.get(RotatedBlockWithEntity.Z_MIRRORED) != this.z_mirrored) {
                this.accessPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.accessPositionOffset, BlockMirror.LEFT_RIGHT);
                this.directTeleportPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.directTeleportPositionOffset, BlockMirror.LEFT_RIGHT);

                this.directTeleportOrientationYaw = BlockRotationUtils.mirrorYaw(this.directTeleportOrientationYaw, BlockMirror.LEFT_RIGHT);

                MutablePair<BlockPos, Vec3i> offsetArea = BlockRotationUtils.mirrorOffsetArea(this.activationAreaPositionOffset, this.activationAreaDimensions, BlockMirror.LEFT_RIGHT);
                this.activationAreaPositionOffset = offsetArea.getLeft();
                this.activationAreaDimensions = offsetArea.getRight();

                this.z_mirrored = state.get(RotatedBlockWithEntity.Z_MIRRORED);
            }
        }
    }

    public static enum TeleportationMode implements StringIdentifiable
    {
        DIRECT("direct"),
        SPAWN_POINTS("spawn_points"),
        LOCATIONS("locations");

        private final String name;

        private TeleportationMode(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        public static Optional<TeleportationMode> byName(String name) {
            return Arrays.stream(TeleportationMode.values()).filter(teleportationMode -> teleportationMode.asString().equals(name)).findFirst();
        }

        public Text asText() {
            return Text.translatable("gui.teleporter_block.teleportationMode." + this.name);
        }
    }

    public static enum SpawnPointType implements StringIdentifiable
    {
        WORLD_SPAWN("world_spawn"),
        PLAYER_SPAWN("player_spawn"),
        PLAYER_LOCATION_ACCESS_POSITION("player_location_access_position");

        private final String name;

        private SpawnPointType(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        public static Optional<SpawnPointType> byName(String name) {
            return Arrays.stream(SpawnPointType.values()).filter(spawnPointType -> spawnPointType.asString().equals(name)).findFirst();
        }

        public Text asText() {
            return Text.translatable("gui.teleporter_block.spawn_point_type." + this.name);
        }
    }
}

