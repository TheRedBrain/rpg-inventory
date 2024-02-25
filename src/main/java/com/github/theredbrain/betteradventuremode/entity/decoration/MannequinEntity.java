package com.github.theredbrain.betteradventuremode.entity.decoration;

import com.github.theredbrain.betteradventuremode.util.MathUtils;
import com.github.theredbrain.betteradventuremode.client.render.entity.MannequinModelPart;
import com.github.theredbrain.betteradventuremode.entity.IRenderEquippedTrinkets;
import com.github.theredbrain.betteradventuremode.registry.DataHandlerRegistry;
import com.github.theredbrain.betteradventuremode.registry.ItemRegistry;
import com.github.theredbrain.betteradventuremode.registry.StatusEffectsRegistry;
import com.github.theredbrain.betteradventuremode.screen.MannequinScreenHandler;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.EulerAngle;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.*;

public class MannequinEntity extends LivingEntity implements IRenderEquippedTrinkets {

    private static final EulerAngle DEFAULT_HEAD_ROTATION = new EulerAngle(0.0F, 0.0F, 0.0F);
    private static final EulerAngle DEFAULT_BODY_ROTATION = new EulerAngle(0.0F, 0.0F, 0.0F);
    private static final EulerAngle DEFAULT_LEFT_ARM_ROTATION = new EulerAngle(0.0F, 0.0F, 0.0F);
    private static final EulerAngle DEFAULT_RIGHT_ARM_ROTATION = new EulerAngle(0.0F, 0.0F, 0.0F); // - pitch: in front, + pitch: behind
    private static final EulerAngle DEFAULT_LEFT_LEG_ROTATION = new EulerAngle(0.0F, 0.0F, 0.0F);
    private static final EulerAngle DEFAULT_RIGHT_LEG_ROTATION = new EulerAngle(0.0F, 0.0F, 0.0F);
    private static final Vector3f DEFAULT_HEAD_TRANSLATION = new Vector3f(0.0F, 0.0F, 0.0F);
    private static final Vector3f DEFAULT_BODY_TRANSLATION = new Vector3f(0.0F, 0.0F, 0.0F);
    private static final Vector3f DEFAULT_LEFT_ARM_TRANSLATION = new Vector3f(5.0F, 2.0F, 0.0F);
    private static final Vector3f DEFAULT_RIGHT_ARM_TRANSLATION = new Vector3f(-5.0F, 2.0F, 0.0F);
    private static final Vector3f DEFAULT_LEFT_LEG_TRANSLATION = new Vector3f(2.0F, 12.0F, 0.0F);
    private static final Vector3f DEFAULT_RIGHT_LEG_TRANSLATION = new Vector3f(-2.0F, 12.0F, 0.0F);
    private static final String[] DEFAULT_TEXTURES = {"minecraft:textures/entity/player/wide/alex.png", "minecraft:textures/entity/player/wide/ari.png", "minecraft:textures/entity/player/wide/efe.png", "minecraft:textures/entity/player/wide/kai.png", "minecraft:textures/entity/player/wide/makena.png", "minecraft:textures/entity/player/wide/noor.png", "minecraft:textures/entity/player/wide/steve.png", "minecraft:textures/entity/player/wide/sunny.png", "minecraft:textures/entity/player/wide/zuri.png"};

    private static final int LEFT_HANDED_FLAG_INDEX = 0;
    private static final int PUSHABLE_FLAG_INDEX = 1;
    private static final int IS_SNEAKING_FLAG_INDEX = 2;
    private static final int USING_ITEM_FLAG_INDEX = 3;
    private static final int IS_BABY_FLAG_INDEX = 4;
    private static final int HAS_VISUAL_FIRE_FLAG_INDEX = 5;
    private static final int IS_AFFECTED_BY_PISTONS_FLAG_INDEX = 6;

    public static final TrackedData<EulerAngle> TRACKER_HEAD_ROTATION;
    public static final TrackedData<EulerAngle> TRACKER_BODY_ROTATION;
    public static final TrackedData<EulerAngle> TRACKER_LEFT_ARM_ROTATION;
    public static final TrackedData<EulerAngle> TRACKER_RIGHT_ARM_ROTATION;
    public static final TrackedData<EulerAngle> TRACKER_LEFT_LEG_ROTATION;
    public static final TrackedData<EulerAngle> TRACKER_RIGHT_LEG_ROTATION;

    public static final TrackedData<Vector3f> TRACKER_HEAD_TRANSLATION;
    public static final TrackedData<Vector3f> TRACKER_BODY_TRANSLATION;
    public static final TrackedData<Vector3f> TRACKER_LEFT_ARM_TRANSLATION;
    public static final TrackedData<Vector3f> TRACKER_RIGHT_ARM_TRANSLATION;
    public static final TrackedData<Vector3f> TRACKER_LEFT_LEG_TRANSLATION;
    public static final TrackedData<Vector3f> TRACKER_RIGHT_LEG_TRANSLATION;

    public static final TrackedData<String> TEXTURE_IDENTIFIER_STRING;
    public static final TrackedData<Byte> MANNEQUIN_FLAGS;
    public static final TrackedData<SheathedWeaponMode> SHEATHED_WEAPON_MODE;
    public static final TrackedData<Byte> MODEL_PARTS_VISIBILITY_A;
    public static final TrackedData<Byte> MODEL_PARTS_VISIBILITY_B;

    public MannequinEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TRACKER_HEAD_ROTATION, DEFAULT_HEAD_ROTATION);
        this.dataTracker.startTracking(TRACKER_BODY_ROTATION, DEFAULT_BODY_ROTATION);
        this.dataTracker.startTracking(TRACKER_LEFT_ARM_ROTATION, DEFAULT_LEFT_ARM_ROTATION);
        this.dataTracker.startTracking(TRACKER_RIGHT_ARM_ROTATION, DEFAULT_RIGHT_ARM_ROTATION);
        this.dataTracker.startTracking(TRACKER_LEFT_LEG_ROTATION, DEFAULT_LEFT_LEG_ROTATION);
        this.dataTracker.startTracking(TRACKER_RIGHT_LEG_ROTATION, DEFAULT_RIGHT_LEG_ROTATION);
        this.dataTracker.startTracking(TRACKER_HEAD_TRANSLATION, DEFAULT_HEAD_TRANSLATION);
        this.dataTracker.startTracking(TRACKER_BODY_TRANSLATION, DEFAULT_BODY_TRANSLATION);
        this.dataTracker.startTracking(TRACKER_LEFT_ARM_TRANSLATION, DEFAULT_LEFT_ARM_TRANSLATION);
        this.dataTracker.startTracking(TRACKER_RIGHT_ARM_TRANSLATION, DEFAULT_RIGHT_ARM_TRANSLATION);
        this.dataTracker.startTracking(TRACKER_LEFT_LEG_TRANSLATION, DEFAULT_LEFT_LEG_TRANSLATION);
        this.dataTracker.startTracking(TRACKER_RIGHT_LEG_TRANSLATION, DEFAULT_RIGHT_LEG_TRANSLATION);
        this.dataTracker.startTracking(MODEL_PARTS_VISIBILITY_A, (byte)255);
        this.dataTracker.startTracking(MODEL_PARTS_VISIBILITY_B, (byte)15);
        this.dataTracker.startTracking(MANNEQUIN_FLAGS, (byte)0);
        this.dataTracker.startTracking(SHEATHED_WEAPON_MODE, SheathedWeaponMode.NONE);
        this.dataTracker.startTracking(TEXTURE_IDENTIFIER_STRING, "");
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putInt("modelPartsVisibilityA", this.dataTracker.get(MODEL_PARTS_VISIBILITY_A).intValue());

        nbt.putInt("modelPartsVisibilityB", this.dataTracker.get(MODEL_PARTS_VISIBILITY_B).intValue());

        nbt.putInt("mannequinFlags", this.dataTracker.get(MANNEQUIN_FLAGS).intValue());

        nbt.putString("sheathedWeaponMode", this.dataTracker.get(SHEATHED_WEAPON_MODE).asString());

        nbt.putString("textureIdentifierString", this.dataTracker.get(TEXTURE_IDENTIFIER_STRING));

        nbt.put("modelParts", this.modelPartsToNbt());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        if (nbt.contains("modelPartsVisibilityA")) {
            this.dataTracker.set(MODEL_PARTS_VISIBILITY_A, (byte) nbt.getInt("modelPartsVisibilityA"));
        }

        if (nbt.contains("modelPartsVisibilityB")) {
            this.dataTracker.set(MODEL_PARTS_VISIBILITY_B, (byte) nbt.getInt("modelPartsVisibilityB"));
        }

        if (nbt.contains("mannequinFlags")) {
            this.dataTracker.set(MANNEQUIN_FLAGS, (byte) nbt.getInt("mannequinFlags"));
        }

        if (nbt.contains("sheathedWeaponMode")) {
            this.dataTracker.set(SHEATHED_WEAPON_MODE, SheathedWeaponMode.byName(nbt.getString("sheathedWeaponMode")).orElse(SheathedWeaponMode.NONE));
        }

        if (nbt.contains("textureIdentifierString")) {
            this.dataTracker.set(TEXTURE_IDENTIFIER_STRING, nbt.getString("textureIdentifierString"));
        }

        if (nbt.contains("modelParts")) {
            this.readModelPartsNbt(nbt.getCompound("modelParts"));
        }
    }

    private NbtCompound modelPartsToNbt() {
        NbtCompound nbtCompound = new NbtCompound();

        EulerAngle rotation = this.dataTracker.get(TRACKER_HEAD_ROTATION);
        if (!DEFAULT_HEAD_ROTATION.equals(rotation)) {
            nbtCompound.put("headRotation", rotation.toNbt());
        }

        rotation = this.dataTracker.get(TRACKER_BODY_ROTATION);
        if (!DEFAULT_BODY_ROTATION.equals(rotation)) {
            nbtCompound.put("bodyRotation", rotation.toNbt());
        }

        rotation = this.dataTracker.get(TRACKER_LEFT_ARM_ROTATION);
        if (!DEFAULT_LEFT_ARM_ROTATION.equals(rotation)) {
            nbtCompound.put("leftArmRotation", rotation.toNbt());
        }

        rotation = this.dataTracker.get(TRACKER_RIGHT_ARM_ROTATION);
        if (!DEFAULT_RIGHT_ARM_ROTATION.equals(rotation)) {
            nbtCompound.put("rightArmRotation", rotation.toNbt());
        }

        rotation = this.dataTracker.get(TRACKER_LEFT_LEG_ROTATION);
        if (!DEFAULT_LEFT_LEG_ROTATION.equals(rotation)) {
            nbtCompound.put("leftLegRotation", rotation.toNbt());
        }

        rotation = this.dataTracker.get(TRACKER_RIGHT_LEG_ROTATION);
        if (!DEFAULT_RIGHT_LEG_ROTATION.equals(rotation)) {
            nbtCompound.put("rightLegRotation", rotation.toNbt());
        }

        Vector3f vector3f = this.dataTracker.get(TRACKER_HEAD_TRANSLATION);
        if (!DEFAULT_HEAD_TRANSLATION.equals(vector3f)) {
            nbtCompound.put("headTranslation", MathUtils.vector3fToNbt(vector3f));
        }

        vector3f = this.dataTracker.get(TRACKER_BODY_TRANSLATION);
        if (!DEFAULT_BODY_TRANSLATION.equals(vector3f)) {
            nbtCompound.put("bodyTranslation", MathUtils.vector3fToNbt(vector3f));
        }

        vector3f = this.dataTracker.get(TRACKER_LEFT_ARM_TRANSLATION);
        if (!DEFAULT_LEFT_ARM_TRANSLATION.equals(vector3f)) {
            nbtCompound.put("leftArmTranslation", MathUtils.vector3fToNbt(vector3f));
        }

        vector3f = this.dataTracker.get(TRACKER_RIGHT_ARM_TRANSLATION);
        if (!DEFAULT_RIGHT_ARM_TRANSLATION.equals(vector3f)) {
            nbtCompound.put("rightArmTranslation", MathUtils.vector3fToNbt(vector3f));
        }

        vector3f = this.dataTracker.get(TRACKER_LEFT_LEG_TRANSLATION);
        if (!DEFAULT_LEFT_LEG_TRANSLATION.equals(vector3f)) {
            nbtCompound.put("leftLegTranslation", MathUtils.vector3fToNbt(vector3f));
        }

        vector3f = this.dataTracker.get(TRACKER_RIGHT_LEG_TRANSLATION);
        if (!DEFAULT_RIGHT_LEG_TRANSLATION.equals(vector3f)) {
            nbtCompound.put("rightLegTranslation", MathUtils.vector3fToNbt(vector3f));
        }

        return nbtCompound;
    }

    private void readModelPartsNbt(NbtCompound nbt) {
        NbtList nbtList = nbt.getList("headRotation", NbtElement.FLOAT_TYPE);
        this.setHeadRotation(nbtList.isEmpty() ? DEFAULT_HEAD_ROTATION : new EulerAngle(nbtList));
        NbtList nbtList2 = nbt.getList("bodyRotation", NbtElement.FLOAT_TYPE);
        this.setBodyRotation(nbtList2.isEmpty() ? DEFAULT_BODY_ROTATION : new EulerAngle(nbtList2));
        NbtList nbtList3 = nbt.getList("leftArmRotation", NbtElement.FLOAT_TYPE);
        this.setLeftArmRotation(nbtList3.isEmpty() ? DEFAULT_LEFT_ARM_ROTATION : new EulerAngle(nbtList3));
        NbtList nbtList4 = nbt.getList("rightArmRotation", NbtElement.FLOAT_TYPE);
        this.setRightArmRotation(nbtList4.isEmpty() ? DEFAULT_RIGHT_ARM_ROTATION : new EulerAngle(nbtList4));
        NbtList nbtList5 = nbt.getList("leftLegRotation", NbtElement.FLOAT_TYPE);
        this.setLeftLegRotation(nbtList5.isEmpty() ? DEFAULT_LEFT_LEG_ROTATION : new EulerAngle(nbtList5));
        NbtList nbtList6 = nbt.getList("rightLegRotation", NbtElement.FLOAT_TYPE);
        this.setRightLegRotation(nbtList6.isEmpty() ? DEFAULT_RIGHT_LEG_ROTATION : new EulerAngle(nbtList6));

        NbtList nbtList7 = nbt.getList("headTranslation", NbtElement.FLOAT_TYPE);
        this.setHeadTranslation(nbtList6.isEmpty() ? DEFAULT_HEAD_TRANSLATION : MathUtils.vector3fFromNbt(nbtList7));
        NbtList nbtList8 = nbt.getList("bodyTranslation", NbtElement.FLOAT_TYPE);
        this.setBodyTranslation(nbtList6.isEmpty() ? DEFAULT_BODY_TRANSLATION : MathUtils.vector3fFromNbt(nbtList8));
        NbtList nbtList9 = nbt.getList("leftArmTranslation", NbtElement.FLOAT_TYPE);
        this.setLeftArmTranslation(nbtList6.isEmpty() ? DEFAULT_LEFT_ARM_TRANSLATION : MathUtils.vector3fFromNbt(nbtList9));
        NbtList nbtList10 = nbt.getList("rightArmTranslation", NbtElement.FLOAT_TYPE);
        this.setRightArmTranslation(nbtList6.isEmpty() ? DEFAULT_RIGHT_ARM_TRANSLATION : MathUtils.vector3fFromNbt(nbtList10));
        NbtList nbtList11 = nbt.getList("leftLegTranslation", NbtElement.FLOAT_TYPE);
        this.setLeftLegTranslation(nbtList6.isEmpty() ? DEFAULT_LEFT_LEG_TRANSLATION : MathUtils.vector3fFromNbt(nbtList11));
        NbtList nbtList12 = nbt.getList("rightLegTranslation", NbtElement.FLOAT_TYPE);
        this.setRightLegTranslation(nbtList6.isEmpty() ? DEFAULT_RIGHT_LEG_TRANSLATION : MathUtils.vector3fFromNbt(nbtList12));
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        if (this.getWorld().isClient) {
            return false;
        }
        if (this.isDead()) {
            return false;
        }
        if (source.isIn(DamageTypeTags.IS_FIRE) && this.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
            return false;
        }
        if (this.isSleeping() && !this.getWorld().isClient) {
            this.wakeUp();
        }
        this.applyDamage(source, amount);
        return true;
    }

    @Override
    public void applyDamage(DamageSource source, float amount) {
        if (source.isOf(DamageTypes.GENERIC_KILL)) {
            this.setHealth(this.getHealth() - amount);
            if (this.getHealth() <= 0) {
                Block.dropStack(this.getWorld(), this.getBlockPos(), new ItemStack(ItemRegistry.MANNEQUIN));
            }
        }
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return this.getArmorTrinkets();
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            return this.getSheathedWeaponMode() != SheathedWeaponMode.BOTH ? this.getMainHand() : ItemStack.EMPTY;
        } else if (slot == EquipmentSlot.OFFHAND) {
            return this.getSheathedWeaponMode() == SheathedWeaponMode.NONE ? this.getOffHand() : ItemStack.EMPTY;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {}

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        boolean canEdit = player.isCreativeLevelTwoOp() || player.hasStatusEffect(StatusEffectsRegistry.HOUSING_OWNER_EFFECT) || player.hasStatusEffect(StatusEffectsRegistry.HOUSING_CO_OWNER_EFFECT);
        if (canEdit || player.hasStatusEffect(StatusEffectsRegistry.HOUSING_TRUSTED_EFFECT)) {
            player.openHandledScreen(createMannequinScreenHandlerFactory(this.getId(), canEdit));
        }
        if (!player.getWorld().isClient) {
            return ActionResult.CONSUME;
        }
        return ActionResult.SUCCESS;
    }

    public static NamedScreenHandlerFactory createMannequinScreenHandlerFactory(int mannequinEntityId, boolean canEdit) {
        return new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                buf.writeInt(mannequinEntityId);
                buf.writeBoolean(canEdit);
            }

            @Override
            public Text getDisplayName() {
                return Text.literal("WIP");
            }

            @Nullable
            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                return new MannequinScreenHandler(syncId, playerInventory, mannequinEntityId, canEdit);
            }
        };
    }

    public Identifier getTextureIdentifier() {
        PlayerListEntry playerListEntry = null;
        ClientPlayNetworkHandler clientPlayNetworkHandler = MinecraftClient.getInstance().getNetworkHandler();
        if (clientPlayNetworkHandler != null) {
            playerListEntry = clientPlayNetworkHandler.getPlayerListEntry(this.getTextureIdentifierString());
        }
        return playerListEntry != null ? playerListEntry.getSkinTexture()/*.getSkinTextures().texture()*/ : !this.getTextureIdentifierString().equals("") && Identifier.isValid(this.getTextureIdentifierString()) ? Identifier.tryParse(this.getTextureIdentifierString()) : getRandomDefaultTexture(this.getUuid());
    }

    private static Identifier getRandomDefaultTexture(UUID uuid) {
        String string = DEFAULT_TEXTURES[Math.floorMod(uuid.hashCode(), DEFAULT_TEXTURES.length)];
        return Identifier.tryParse(string);
    }

    public String getTextureIdentifierString() {
        return this.dataTracker.get(TEXTURE_IDENTIFIER_STRING);
    }

    public void setTextureIdentifierString(String textureIdentifier) {
        this.dataTracker.set(TEXTURE_IDENTIFIER_STRING, textureIdentifier);
    }

//    public String getNameString() {
//        return this.dataTracker.get(NAME_STRING);
//    }
//
//    public void setNameString(String name) {
//        this.dataTracker.set(NAME_STRING, name);
//    }

    public boolean isModelPartVisible(MannequinModelPart modelPart) {
        if (modelPart.isFirstHalf()) {
            return ((Byte) this.getDataTracker().get(MODEL_PARTS_VISIBILITY_A) & modelPart.getBitFlag()) == modelPart.getBitFlag();
        } else {
            return ((Byte)this.getDataTracker().get(MODEL_PARTS_VISIBILITY_B) & modelPart.getBitFlag()) == modelPart.getBitFlag();
        }
    }

    public void setModelPartVisibility(int index, boolean value) {
        byte a = (Byte)this.dataTracker.get(MODEL_PARTS_VISIBILITY_A);
        byte b = (Byte)this.dataTracker.get(MODEL_PARTS_VISIBILITY_B);
        if (index < 8) {
            if (value) {
                this.dataTracker.set(MODEL_PARTS_VISIBILITY_A, (byte)(a | 1 << index));
            } else {
                this.dataTracker.set(MODEL_PARTS_VISIBILITY_A, (byte)(a & ~(1 << index)));
            }
        } else {
            index -= 8;
            if (value) {
                this.dataTracker.set(MODEL_PARTS_VISIBILITY_B, (byte)(b | 1 << index));
            } else {
                this.dataTracker.set(MODEL_PARTS_VISIBILITY_B, (byte)(b & ~(1 << index)));
            }
        }
    }

    private boolean getMannequinFlag(int index) {
        return ((Byte)this.dataTracker.get(MANNEQUIN_FLAGS) & 1 << index) != 0;
    }

    private void setMannequinFlag(int index, boolean value) {
        byte b = (Byte)this.dataTracker.get(MANNEQUIN_FLAGS);
        if (value) {
            this.dataTracker.set(MANNEQUIN_FLAGS, (byte)(b | 1 << index));
        } else {
            this.dataTracker.set(MANNEQUIN_FLAGS, (byte)(b & ~(1 << index)));
        }
    }

    @Override
    public boolean isInSneakingPose() {
        return this.getMannequinFlag(IS_SNEAKING_FLAG_INDEX);
    }

    public void setIsSneaking(boolean isSneaking) {
        this.setMannequinFlag(IS_SNEAKING_FLAG_INDEX, isSneaking);
    }

    @Override
    public boolean isPushable() {
        return this.getMannequinFlag(PUSHABLE_FLAG_INDEX);
    }

    public void setIsPushable(boolean isPushable) {
        this.setMannequinFlag(PUSHABLE_FLAG_INDEX, isPushable);
    }

    @Override
    public boolean isUsingItem() {
        return this.getMannequinFlag(USING_ITEM_FLAG_INDEX);
    }

    public void setIsUsingItem(boolean isUsingItem) {
        this.setMannequinFlag(USING_ITEM_FLAG_INDEX, isUsingItem);
    }

    @Override
    public Arm getMainArm() {
        return this.isLeftHanded() ? Arm.LEFT : Arm.RIGHT;
    }

    public boolean isLeftHanded() {
        return this.getMannequinFlag(LEFT_HANDED_FLAG_INDEX);
    }

    public void setIsLeftHanded(boolean leftHanded) {
        this.setMannequinFlag(LEFT_HANDED_FLAG_INDEX, leftHanded);
    }

    @Override
    public boolean isBaby() {
        return this.getMannequinFlag(IS_BABY_FLAG_INDEX);
    }

    public void setIsBaby(boolean isBaby) {
        this.setMannequinFlag(IS_BABY_FLAG_INDEX, isBaby);
    }

    @Override
    public boolean doesRenderOnFire() {
        return this.hasVisualFire();
    }
    public boolean hasVisualFire() {
        return this.getMannequinFlag(HAS_VISUAL_FIRE_FLAG_INDEX);
    }

    public void setHasVisualFire(boolean hasVisualFire) {
        this.setMannequinFlag(HAS_VISUAL_FIRE_FLAG_INDEX, hasVisualFire);
    }

    @Override
    public PistonBehavior getPistonBehavior() {
        return this.isAffectedByPistons() ? PistonBehavior.NORMAL : PistonBehavior.IGNORE;
    }

    public boolean isAffectedByPistons() {
        return this.getMannequinFlag(IS_AFFECTED_BY_PISTONS_FLAG_INDEX);
    }

    public void setIsAffectedByPistons(boolean isAffectedByPistons) {
        this.setMannequinFlag(IS_AFFECTED_BY_PISTONS_FLAG_INDEX, isAffectedByPistons);
    }

//    public boolean hasNoGravity() {
//        return (Boolean)this.dataTracker.get(NO_GRAVITY);
//    }
//
//    public void setNoGravity(boolean noGravity) {
//        this.dataTracker.set(NO_GRAVITY, noGravity);
//    }

//    public void setCustomNameVisible(boolean visible) {
//        this.dataTracker.set(NAME_VISIBLE, visible);
//    }
//
//    public boolean isCustomNameVisible() {
//        return (Boolean)this.dataTracker.get(NAME_VISIBLE);
//    }
//
//    public void setCustomName(@Nullable Text name) {
//        this.dataTracker.set(CUSTOM_NAME, Optional.ofNullable(name));
//    }
//
//    @Nullable
//    public Text getCustomName() {
//        return (Text)((Optional)this.dataTracker.get(CUSTOM_NAME)).orElse((Object)null);
//    }

    public String getCustomNameString() {
        Text customName = this.getCustomName();
        return customName != null ? customName.getString() : "";
    }

    public void setCustomNameString(String customNameString) {
        this.setCustomName(customNameString.equals("") ? null : Text.translatable(customNameString));
    }

    public SheathedWeaponMode getSheathedWeaponMode() {
        return this.dataTracker.get(SHEATHED_WEAPON_MODE);
    }

    public void setSheathedWeaponMode(SheathedWeaponMode sheathedWeaponMode) {
        this.dataTracker.set(SHEATHED_WEAPON_MODE, sheathedWeaponMode);
    }

    // region IRenderEquippedTrinkets
    public boolean isMainHandItemSheathed() {
        return this.getSheathedWeaponMode() == SheathedWeaponMode.BOTH;
    }

    public boolean isOffHandItemSheathed() {
        return this.getSheathedWeaponMode() != SheathedWeaponMode.NONE;
    }

    public ItemStack getMainHandItemStack() {
        return this.getMainHand();
    }

    public ItemStack getOffHandItemStack() {
        return this.getOffHand();
    }
    // endregion IRenderEquippedTrinkets

    // region trinket stacks
    public List<ItemStack> getArmorTrinkets() {
//        return List.of(this.getBootsStack(), this.getLeggingsStack(), this.getGlovesStack(), this.getChestPlateStack(), this.getShouldersStack(), this.getHelmetStack());
        return List.of();
    }

    public ItemStack getTrinketItemStackByIndex(int index) {
        return switch (index) {
            case 0 -> this.getHelmetStack();
            case 1 -> this.getShouldersStack();
            case 2 -> this.getChestPlateStack();
            case 3 -> this.getBeltStack();
            case 4 -> this.getLeggingsStack();
            case 5 -> this.getMainHand();
            case 6 -> this.getNecklaceStack();
            case 7 -> this.getRing1Stack();
            case 8 -> this.getRing2Stack();
            case 9 -> this.getGlovesStack();
            case 10 -> this.getBootsStack();
            case 11 -> this.getOffHand();
            case 12 -> this.getAlternativeMainHand();
            case 13 -> this.getAlternativeOffHand();
            case 14 -> this.getSpellSlotStack(1);
            case 15 -> this.getSpellSlotStack(2);
            case 16 -> this.getSpellSlotStack(3);
            case 17 -> this.getSpellSlotStack(4);
            case 18 -> this.getSpellSlotStack(5);
            case 19 -> this.getSpellSlotStack(6);
            case 20 -> this.getSpellSlotStack(7);
            case 21 -> this.getSpellSlotStack(8);
            default -> ItemStack.EMPTY;
        };
    }

    public boolean setTrinketItemStackByIndex(ItemStack itemStack, int index) {
        return switch (index) {
            case 0 -> this.setHelmetStack(itemStack);
            case 1 -> this.setShouldersStack(itemStack);
            case 2 -> this.setChestPlateStack(itemStack);
            case 3 -> this.setBeltStack(itemStack);
            case 4 -> this.setLeggingsStack(itemStack);
            case 5 -> this.setMainHand(itemStack);
            case 6 -> this.setNecklaceStack(itemStack);
            case 7 -> this.setRing1Stack(itemStack);
            case 8 -> this.setRing2Stack(itemStack);
            case 9 -> this.setGlovesStack(itemStack);
            case 10 -> this.setBootsStack(itemStack);
            case 11 -> this.setOffHand(itemStack);
            case 12 -> this.setAlternativeMainHand(itemStack);
            case 13 -> this.setAlternativeOffHand(itemStack);
            case 14 -> this.setSpellSlotStack(itemStack, 1);
            case 15 -> this.setSpellSlotStack(itemStack, 2);
            case 16 -> this.setSpellSlotStack(itemStack, 3);
            case 17 -> this.setSpellSlotStack(itemStack, 4);
            case 18 -> this.setSpellSlotStack(itemStack, 5);
            case 19 -> this.setSpellSlotStack(itemStack, 6);
            case 20 -> this.setSpellSlotStack(itemStack, 7);
            case 21 -> this.setSpellSlotStack(itemStack, 8);
            default -> false;
        };
    }

    public ItemStack getMainHand() {
        ItemStack mainHandStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("main_hand") != null) {
                if (trinkets.get().getInventory().get("main_hand").get("main_hand") != null) {
                    mainHandStack = trinkets.get().getInventory().get("main_hand").get("main_hand").getStack(0);
                }
            }
        }
        return mainHandStack;
    }

    public boolean setMainHand(ItemStack itemStack) {
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("main_hand") != null) {
                if (trinkets.get().getInventory().get("main_hand").get("main_hand") != null) {
                    trinkets.get().getInventory().get("main_hand").get("main_hand").setStack(0, itemStack);
                    return true;
                }
            }
        }
        return false;
    }

    public ItemStack getAlternativeMainHand() {
        ItemStack mainHandStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("alternative_main_hand") != null) {
                if (trinkets.get().getInventory().get("alternative_main_hand").get("alternative_main_hand") != null) {
                    mainHandStack = trinkets.get().getInventory().get("alternative_main_hand").get("alternative_main_hand").getStack(0);
                }
            }
        }
        return mainHandStack;
    }

    public boolean setAlternativeMainHand(ItemStack itemStack) {
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("main_hand") != null) {
                if (trinkets.get().getInventory().get("alternative_main_hand").get("alternative_main_hand") != null) {
                    trinkets.get().getInventory().get("alternative_main_hand").get("alternative_main_hand").setStack(0, itemStack);
                    return true;
                }
            }
        }
        return false;
    }

    public ItemStack getOffHand() {
        ItemStack offHandStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("off_hand") != null) {
                if (trinkets.get().getInventory().get("off_hand").get("off_hand") != null) {
                    offHandStack = trinkets.get().getInventory().get("off_hand").get("off_hand").getStack(0);
                }
            }
        }
        return offHandStack;
    }

    public boolean setOffHand(ItemStack itemStack) {
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("off_hand") != null) {
                if (trinkets.get().getInventory().get("off_hand").get("off_hand") != null) {
                    trinkets.get().getInventory().get("off_hand").get("off_hand").setStack(0, itemStack);
                    return true;
                }
            }
        }
        return false;
    }

    public ItemStack getAlternativeOffHand() {
        ItemStack offHandStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("alternative_off_hand") != null) {
                if (trinkets.get().getInventory().get("alternative_off_hand").get("alternative_off_hand") != null) {
                    offHandStack = trinkets.get().getInventory().get("alternative_off_hand").get("alternative_off_hand").getStack(0);
                }
            }
        }
        return offHandStack;
    }

    public boolean setAlternativeOffHand(ItemStack itemStack) {
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("alternative_off_hand") != null) {
                if (trinkets.get().getInventory().get("alternative_off_hand").get("alternative_off_hand") != null) {
                    trinkets.get().getInventory().get("alternative_off_hand").get("alternative_off_hand").setStack(0, itemStack);
                    return true;
                }
            }
        }
        return false;
    }

    public ItemStack getHelmetStack() {
        ItemStack headStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("helmets") != null) {
                if (trinkets.get().getInventory().get("helmets").get("helmet") != null) {
                    headStack = trinkets.get().getInventory().get("helmets").get("helmet").getStack(0);
                }
            }
        }
        return headStack;
    }

    public boolean setHelmetStack(ItemStack itemStack) {
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("helmets") != null) {
                if (trinkets.get().getInventory().get("helmets").get("helmet") != null) {
                    trinkets.get().getInventory().get("helmets").get("helmet").setStack(0, itemStack);
                    return true;
                }
            }
        }
        return false;
    }

    public ItemStack getChestPlateStack() {
        ItemStack chestStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("chest_plates") != null) {
                if (trinkets.get().getInventory().get("chest_plates").get("chest_plate") != null) {
                    chestStack = trinkets.get().getInventory().get("chest_plates").get("chest_plate").getStack(0);
                }
            }
        }
        return chestStack;
    }

    public boolean setChestPlateStack(ItemStack itemStack) {
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("chest_plates") != null) {
                if (trinkets.get().getInventory().get("chest_plates").get("chest_plate") != null) {
                    trinkets.get().getInventory().get("chest_plates").get("chest_plate").setStack(0, itemStack);
                    return true;
                }
            }
        }
        return false;
    }

    public ItemStack getLeggingsStack() {
        ItemStack legsStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("leggings") != null) {
                if (trinkets.get().getInventory().get("leggings").get("leggings") != null) {
                    legsStack = trinkets.get().getInventory().get("leggings").get("leggings").getStack(0);
                }
            }
        }
        return legsStack;
    }

    public boolean setLeggingsStack(ItemStack itemStack) {
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("leggings") != null) {
                if (trinkets.get().getInventory().get("leggings").get("leggings") != null) {
                    trinkets.get().getInventory().get("leggings").get("leggings").setStack(0, itemStack);
                    return true;
                }
            }
        }
        return false;
    }

    public ItemStack getBootsStack() {
        ItemStack feetStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("boots") != null) {
                if (trinkets.get().getInventory().get("boots").get("boots") != null) {
                    feetStack = trinkets.get().getInventory().get("boots").get("boots").getStack(0);
                }
            }
        }
        return feetStack;
    }

    public boolean setBootsStack(ItemStack itemStack) {
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("boots") != null) {
                if (trinkets.get().getInventory().get("boots").get("boots") != null) {
                    trinkets.get().getInventory().get("boots").get("boots").setStack(0, itemStack);
                    return true;
                }
            }
        }
        return false;
    }

    public ItemStack getGlovesStack() {
        ItemStack glovesStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("gloves") != null) {
                if (trinkets.get().getInventory().get("gloves").get("gloves") != null) {
                    glovesStack = trinkets.get().getInventory().get("gloves").get("gloves").getStack(0);
                }
            }
        }
        return glovesStack;
    }

    public boolean setGlovesStack(ItemStack itemStack) {
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("gloves") != null) {
                if (trinkets.get().getInventory().get("gloves").get("gloves") != null) {
                    trinkets.get().getInventory().get("gloves").get("gloves").setStack(0, itemStack);
                    return true;
                }
            }
        }
        return false;
    }

    public ItemStack getShouldersStack() {
        ItemStack shouldersStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("shoulders") != null) {
                if (trinkets.get().getInventory().get("shoulders").get("shoulders") != null) {
                    shouldersStack = trinkets.get().getInventory().get("shoulders").get("shoulders").getStack(0);
                }
            }
        }
        return shouldersStack;
    }

    public boolean setShouldersStack(ItemStack itemStack) {
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("shoulders") != null) {
                if (trinkets.get().getInventory().get("shoulders").get("shoulders") != null) {
                    trinkets.get().getInventory().get("shoulders").get("shoulders").setStack(0, itemStack);
                    return true;
                }
            }
        }
        return false;
    }

    public ItemStack getRing1Stack() {
        ItemStack rings1Stack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("rings_1") != null) {
                if (trinkets.get().getInventory().get("rings_1").get("ring") != null) {
                    rings1Stack = trinkets.get().getInventory().get("rings_1").get("ring").getStack(0);
                }
            }
        }
        return rings1Stack;
    }

    public boolean setRing1Stack(ItemStack itemStack) {
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("rings_1") != null) {
                if (trinkets.get().getInventory().get("rings_1").get("ring") != null) {
                    trinkets.get().getInventory().get("rings_1").get("ring").setStack(0, itemStack);
                    return true;
                }
            }
        }
        return false;
    }

    public ItemStack getRing2Stack() {
        ItemStack rings2Stack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("rings_2") != null) {
                if (trinkets.get().getInventory().get("rings_2").get("ring") != null) {
                    rings2Stack = trinkets.get().getInventory().get("rings_2").get("ring").getStack(0);
                }
            }
        }
        return rings2Stack;
    }

    public boolean setRing2Stack(ItemStack itemStack) {
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("rings_2") != null) {
                if (trinkets.get().getInventory().get("rings_2").get("ring") != null) {
                    trinkets.get().getInventory().get("rings_2").get("ring").setStack(0, itemStack);
                    return true;
                }
            }
        }
        return false;
    }

    public ItemStack getBeltStack() {
        ItemStack beltsStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("belts") != null) {
                if (trinkets.get().getInventory().get("belts").get("belt") != null) {
                    beltsStack = trinkets.get().getInventory().get("belts").get("belt").getStack(0);
                }
            }
        }
        return beltsStack;
    }

    public boolean setBeltStack(ItemStack itemStack) {
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("belts") != null) {
                if (trinkets.get().getInventory().get("belts").get("belt") != null) {
                    trinkets.get().getInventory().get("belts").get("belt").setStack(0, itemStack);
                    return true;
                }
            }
        }
        return false;
    }

    public ItemStack getNecklaceStack() {
        ItemStack necklacesStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("necklaces") != null) {
                if (trinkets.get().getInventory().get("necklaces").get("necklace") != null) {
                    necklacesStack = trinkets.get().getInventory().get("necklaces").get("necklace").getStack(0);
                }
            }
        }
        return necklacesStack;
    }

    public boolean setNecklaceStack(ItemStack itemStack) {
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("necklaces") != null) {
                if (trinkets.get().getInventory().get("necklaces").get("necklace") != null) {
                    trinkets.get().getInventory().get("necklaces").get("necklace").setStack(0, itemStack);
                    return true;
                }
            }
        }
        return false;
    }

    public ItemStack getSpellSlotStack(int spellSlotNumber) {
        ItemStack spellSlotStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("spell_slot_" + spellSlotNumber) != null) {
                if (trinkets.get().getInventory().get("spell_slot_" + spellSlotNumber).get("spell") != null) {
                    spellSlotStack = trinkets.get().getInventory().get("spell_slot_" + spellSlotNumber).get("spell").getStack(0);
                }
            }
        }
        return spellSlotStack;
    }

    public boolean setSpellSlotStack(ItemStack itemStack, int spellSlotNumber) {
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("spell_slot_" + spellSlotNumber) != null) {
                if (trinkets.get().getInventory().get("spell_slot_" + spellSlotNumber).get("spell") != null) {
                    trinkets.get().getInventory().get("spell_slot_" + spellSlotNumber).get("spell").setStack(0, itemStack);
                    return true;
                }
            }
        }
        return false;
    }
    // endregion trinket stacks

    // region rotations
    public void setHeadRotation(EulerAngle angle) {
        this.dataTracker.set(TRACKER_HEAD_ROTATION, angle);
    }

    public void setBodyRotation(EulerAngle angle) {
        this.dataTracker.set(TRACKER_BODY_ROTATION, angle);
    }

    public void setLeftArmRotation(EulerAngle angle) {
        this.dataTracker.set(TRACKER_LEFT_ARM_ROTATION, angle);
    }

    public void setRightArmRotation(EulerAngle angle) {
        this.dataTracker.set(TRACKER_RIGHT_ARM_ROTATION, angle);
    }

    public void setLeftLegRotation(EulerAngle angle) {
        this.dataTracker.set(TRACKER_LEFT_LEG_ROTATION, angle);
    }

    public void setRightLegRotation(EulerAngle angle) {
        this.dataTracker.set(TRACKER_RIGHT_LEG_ROTATION, angle);
    }

    public EulerAngle getHeadRotation() {
        return this.dataTracker.get(TRACKER_HEAD_ROTATION);
    }

    public EulerAngle getBodyRotation() {
        return this.dataTracker.get(TRACKER_BODY_ROTATION);
    }

    public EulerAngle getLeftArmRotation() {
        return this.dataTracker.get(TRACKER_LEFT_ARM_ROTATION);
    }

    public EulerAngle getRightArmRotation() {
        return this.dataTracker.get(TRACKER_RIGHT_ARM_ROTATION);
    }

    public EulerAngle getLeftLegRotation() {
        return this.dataTracker.get(TRACKER_LEFT_LEG_ROTATION);
    }

    public EulerAngle getRightLegRotation() {
        return this.dataTracker.get(TRACKER_RIGHT_LEG_ROTATION);
    }
    // endregion rotations

    // region translations
    public void setHeadTranslation(Vector3f vector) {
        this.dataTracker.set(TRACKER_HEAD_TRANSLATION, vector);
    }

    public void setBodyTranslation(Vector3f vector) {
        this.dataTracker.set(TRACKER_BODY_TRANSLATION, vector);
    }

    public void setLeftArmTranslation(Vector3f vector) {
        this.dataTracker.set(TRACKER_LEFT_ARM_TRANSLATION, vector);
    }

    public void setRightArmTranslation(Vector3f vector) {
        this.dataTracker.set(TRACKER_RIGHT_ARM_TRANSLATION, vector);
    }

    public void setLeftLegTranslation(Vector3f vector) {
        this.dataTracker.set(TRACKER_LEFT_LEG_TRANSLATION, vector);
    }

    public void setRightLegTranslation(Vector3f vector) {
        this.dataTracker.set(TRACKER_RIGHT_LEG_TRANSLATION, vector);
    }

    public Vector3f getHeadTranslation() {
        return this.dataTracker.get(TRACKER_HEAD_TRANSLATION);
    }

    public Vector3f getBodyTranslation() {
        return this.dataTracker.get(TRACKER_BODY_TRANSLATION);
    }

    public Vector3f getLeftArmTranslation() {
        return this.dataTracker.get(TRACKER_LEFT_ARM_TRANSLATION);
    }

    public Vector3f getRightArmTranslation() {
        return this.dataTracker.get(TRACKER_RIGHT_ARM_TRANSLATION);
    }

    public Vector3f getLeftLegTranslation() {
        return this.dataTracker.get(TRACKER_LEFT_LEG_TRANSLATION);
    }

    public Vector3f getRightLegTranslation() {
        return this.dataTracker.get(TRACKER_RIGHT_LEG_TRANSLATION);
    }
    // endregion translations

    public static enum SheathedWeaponMode implements StringIdentifiable {
        NONE("none"),
        OFF_HAND("off_hand"),
        BOTH("both");

        private final String name;

        private SheathedWeaponMode(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        public static Optional<SheathedWeaponMode> byName(String name) {
            return Arrays.stream(SheathedWeaponMode.values()).filter(sheathedWeaponMode -> sheathedWeaponMode.asString().equals(name)).findFirst();
        }

        public Text asText() {
            return Text.translatable("gui.mannequin_entity.sheathedWeaponMode." + this.name);
        }
    }

    static {
        TRACKER_HEAD_ROTATION = DataTracker.registerData(MannequinEntity.class, TrackedDataHandlerRegistry.ROTATION);
        TRACKER_BODY_ROTATION = DataTracker.registerData(MannequinEntity.class, TrackedDataHandlerRegistry.ROTATION);
        TRACKER_LEFT_ARM_ROTATION = DataTracker.registerData(MannequinEntity.class, TrackedDataHandlerRegistry.ROTATION);
        TRACKER_RIGHT_ARM_ROTATION = DataTracker.registerData(MannequinEntity.class, TrackedDataHandlerRegistry.ROTATION);
        TRACKER_LEFT_LEG_ROTATION = DataTracker.registerData(MannequinEntity.class, TrackedDataHandlerRegistry.ROTATION);
        TRACKER_RIGHT_LEG_ROTATION = DataTracker.registerData(MannequinEntity.class, TrackedDataHandlerRegistry.ROTATION);
        TRACKER_HEAD_TRANSLATION = DataTracker.registerData(MannequinEntity.class, TrackedDataHandlerRegistry.VECTOR3F);
        TRACKER_BODY_TRANSLATION = DataTracker.registerData(MannequinEntity.class, TrackedDataHandlerRegistry.VECTOR3F);
        TRACKER_LEFT_ARM_TRANSLATION = DataTracker.registerData(MannequinEntity.class, TrackedDataHandlerRegistry.VECTOR3F);
        TRACKER_RIGHT_ARM_TRANSLATION = DataTracker.registerData(MannequinEntity.class, TrackedDataHandlerRegistry.VECTOR3F);
        TRACKER_LEFT_LEG_TRANSLATION = DataTracker.registerData(MannequinEntity.class, TrackedDataHandlerRegistry.VECTOR3F);
        TRACKER_RIGHT_LEG_TRANSLATION = DataTracker.registerData(MannequinEntity.class, TrackedDataHandlerRegistry.VECTOR3F);
        MODEL_PARTS_VISIBILITY_A = DataTracker.registerData(MannequinEntity.class, TrackedDataHandlerRegistry.BYTE);
        MODEL_PARTS_VISIBILITY_B = DataTracker.registerData(MannequinEntity.class, TrackedDataHandlerRegistry.BYTE);
        MANNEQUIN_FLAGS = DataTracker.registerData(MannequinEntity.class, TrackedDataHandlerRegistry.BYTE);
        SHEATHED_WEAPON_MODE = DataTracker.registerData(MannequinEntity.class, DataHandlerRegistry.SHEATHED_WEAPON_MODE);
        TEXTURE_IDENTIFIER_STRING = DataTracker.registerData(MannequinEntity.class, TrackedDataHandlerRegistry.STRING);
    }
}
