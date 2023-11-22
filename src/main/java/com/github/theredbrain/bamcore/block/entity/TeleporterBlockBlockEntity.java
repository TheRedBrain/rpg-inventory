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
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TeleporterBlockBlockEntity extends RotatedBlockEntity implements ExtendedScreenHandlerFactory, Inventory {

    private String teleporterName = "teleporterName";
    private boolean showActivationArea = false;
    private Vec3i activationAreaDimensions = Vec3i.ZERO;
    private BlockPos activationAreaPositionOffset = new BlockPos(0, 1, 0);

    private boolean showAdventureScreen = true; //

    private int teleportationMode = 0;

    // direct teleportation mode
    private BlockPos directTeleportPositionOffset = new BlockPos(0, 0, 0);
    private double directTeleportPositionOffsetYaw = 0.0;
    private double directTeleportPositionOffsetPitch = 0.0;

    // specific location mode
    private int specificLocationType = 0;

    // dungeon mode
    // player house mode
    private List<Pair<String, String>> locationsList = new ArrayList<>(List.of());

    private boolean consumeKeyItemStack = false;
    private DefaultedList<ItemStack> requiredKeyItemStack = DefaultedList.ofSize(1, ItemStack.EMPTY);

    private String teleportButtonLabel = "gui.teleport";
    private String cancelTeleportButtonLabel = "gui.cancel";

    public TeleporterBlockBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.TELEPORTER_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putString("teleporterName", this.teleporterName);
        nbt.putBoolean("showActivationArea", this.showActivationArea);

        nbt.putInt("activationAreaDimensionsX", this.activationAreaDimensions.getX());
        nbt.putInt("activationAreaDimensionsY", this.activationAreaDimensions.getY());
        nbt.putInt("activationAreaDimensionsZ", this.activationAreaDimensions.getZ());

        nbt.putInt("activationAreaPositionOffsetX", this.activationAreaPositionOffset.getX());
        nbt.putInt("activationAreaPositionOffsetY", this.activationAreaPositionOffset.getY());
        nbt.putInt("activationAreaPositionOffsetZ", this.activationAreaPositionOffset.getZ());

        nbt.putBoolean("showAdventureScreen", this.showAdventureScreen);

        nbt.putInt("teleportationMode", this.teleportationMode);

        nbt.putInt("directTeleportPositionOffsetX", this.directTeleportPositionOffset.getX());
        nbt.putInt("directTeleportPositionOffsetY", this.directTeleportPositionOffset.getY());
        nbt.putInt("directTeleportPositionOffsetZ", this.directTeleportPositionOffset.getZ());
        nbt.putDouble("directTeleportPositionOffsetYaw", this.directTeleportPositionOffsetYaw);
        nbt.putDouble("directTeleportPositionOffsetPitch", this.directTeleportPositionOffsetPitch);

        nbt.putInt("specificLocationType", this.specificLocationType);

        for (int i = 0; i < this.locationsList.size(); i++) {
            nbt.putString("locationsListIdentifier_" + i, this.locationsList.get(i).getLeft());
            nbt.putString("locationsListEntrance_" + i, this.locationsList.get(i).getRight());
        }

        nbt.putBoolean("consumeKeyItemStack", this.consumeKeyItemStack);
        Inventories.writeNbt(nbt, this.requiredKeyItemStack);

        nbt.putString("teleportButtonLabel", this.teleportButtonLabel);
        nbt.putString("cancelTeleportButtonLabel", this.cancelTeleportButtonLabel);

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.teleporterName = nbt.getString("teleporterName");
        this.showActivationArea = nbt.getBoolean("showActivationArea");

        int i = MathHelper.clamp(nbt.getInt("activationAreaDimensionsX"), 0, 48);
        int j = MathHelper.clamp(nbt.getInt("activationAreaDimensionsY"), 0, 48);
        int k = MathHelper.clamp(nbt.getInt("activationAreaDimensionsZ"), 0, 48);
        this.activationAreaDimensions = new Vec3i(i, j, k);

        int l = MathHelper.clamp(nbt.getInt("activationAreaPositionOffsetX"), -48, 48);
        int m = MathHelper.clamp(nbt.getInt("activationAreaPositionOffsetY"), -48, 48);
        int n = MathHelper.clamp(nbt.getInt("activationAreaPositionOffsetZ"), -48, 48);
        this.activationAreaPositionOffset = new BlockPos(l, m, n);

        this.showAdventureScreen = nbt.getBoolean("showAdventureScreen");

        this.teleportationMode = nbt.getInt("teleportationMode");

        this.directTeleportPositionOffset = new BlockPos(
                nbt.getInt("directTeleportPositionOffsetX"),
                nbt.getInt("directTeleportPositionOffsetY"),
                nbt.getInt("directTeleportPositionOffsetZ")
        );
        this.directTeleportPositionOffsetYaw = nbt.getDouble("directTeleportPositionOffsetYaw");
        this.directTeleportPositionOffsetPitch = nbt.getDouble("directTeleportPositionOffsetPitch");

        this.specificLocationType = nbt.getInt("specificLocationType");

        int locationsListSize = nbt.getInt("locationsListSize");
        this.locationsList.clear();
        for (int p = 0; p < locationsListSize; p++) {
            this.locationsList.add(new Pair<>(nbt.getString("locationsListIdentifier_" + p), nbt.getString("locationsListEntrance_" + p)));
        }

        this.consumeKeyItemStack = nbt.getBoolean("consumeKeyItemStack");
        this.requiredKeyItemStack = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.requiredKeyItemStack);

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

    /**
     * @return 0: direct teleport, 1: specific location, 2: dungeon mode, 3: house mode
     */
    public int getTeleportationMode() {
        return teleportationMode;
    }

    // TODO check if input is valid
    public boolean setTeleportationMode(int teleportationMode) {
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

    public double getDirectTeleportPositionOffsetYaw() {
        return directTeleportPositionOffsetYaw;
    }

    // TODO check if input is valid
    public boolean setDirectTeleportPositionOffsetYaw(double directTeleportPositionOffsetYaw) {
        this.directTeleportPositionOffsetYaw = directTeleportPositionOffsetYaw;
        return true;
    }

    public double getDirectTeleportPositionOffsetPitch() {
        return directTeleportPositionOffsetPitch;
    }

    // TODO check if input is valid
    public boolean setDirectTeleportPositionOffsetPitch(double directTeleportPositionOffsetPitch) {
        this.directTeleportPositionOffsetPitch = directTeleportPositionOffsetPitch;
        return true;
    }

    /**
     * @return 0: world spawn, 1: player spawn, 2: housing/dungeon access position
     */
    public int getSpecificLocationType() {
        return specificLocationType;
    }

    // TODO check if input is valid
    public boolean setSpecificLocationType(int specificLocationType) {
        this.specificLocationType = specificLocationType;
        return true;
    }

    public List<Pair<String, String>> getLocationsList() {
        return locationsList;
    }

    // TODO check if input is valid
    public boolean setLocationsList(List<Pair<String, String>> locationsList) {
        this.locationsList = locationsList;
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

    public String getCancelTeleportButtonLabel() {
        return cancelTeleportButtonLabel;
    }

    // TODO check if input is valid
    public boolean setCancelTeleportButtonLabel(String cancelTeleportButtonLabel) {
        this.cancelTeleportButtonLabel = cancelTeleportButtonLabel;
        return true;
    }

    public String getTeleportButtonLabel() {
        return teleportButtonLabel;
    }

    // TODO check if input is valid
    public boolean setTeleportButtonLabel(String teleportButtonLabel) {
        this.teleportButtonLabel = teleportButtonLabel;
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
        return Text.literal(this.teleporterName);
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
                this.activationAreaPositionOffset = BlockRotationUtils.rotateOffsetBlockPos(this.activationAreaPositionOffset, blockRotation);
                this.directTeleportPositionOffset = BlockRotationUtils.rotateOffsetBlockPos(this.directTeleportPositionOffset, blockRotation);

                this.directTeleportPositionOffsetYaw = BlockRotationUtils.rotateYaw(this.directTeleportPositionOffsetYaw, blockRotation);

                this.activationAreaDimensions = BlockRotationUtils.rotateOffsetVec3i(this.activationAreaDimensions, blockRotation);
                this.rotated = state.get(RotatedBlockWithEntity.ROTATED);
            }
            if (state.get(RotatedBlockWithEntity.X_MIRRORED) != this.x_mirrored) {
                this.activationAreaPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.activationAreaPositionOffset, BlockMirror.FRONT_BACK);
                this.directTeleportPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.directTeleportPositionOffset, BlockMirror.FRONT_BACK);

                this.directTeleportPositionOffsetYaw = BlockRotationUtils.mirrorYaw(this.directTeleportPositionOffsetYaw, BlockMirror.FRONT_BACK);

                this.activationAreaDimensions = BlockRotationUtils.mirrorOffsetVec3i(this.activationAreaDimensions, BlockMirror.FRONT_BACK);
                this.x_mirrored = state.get(RotatedBlockWithEntity.X_MIRRORED);
            }
            if (state.get(RotatedBlockWithEntity.Z_MIRRORED) != this.z_mirrored) {
                this.activationAreaPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.activationAreaPositionOffset, BlockMirror.LEFT_RIGHT);
                this.directTeleportPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.directTeleportPositionOffset, BlockMirror.LEFT_RIGHT);

                this.directTeleportPositionOffsetYaw = BlockRotationUtils.mirrorYaw(this.directTeleportPositionOffsetYaw, BlockMirror.LEFT_RIGHT);

                this.activationAreaDimensions = BlockRotationUtils.mirrorOffsetVec3i(this.activationAreaDimensions, BlockMirror.LEFT_RIGHT);
                this.z_mirrored = state.get(RotatedBlockWithEntity.Z_MIRRORED);
            }
        }
    }
}

