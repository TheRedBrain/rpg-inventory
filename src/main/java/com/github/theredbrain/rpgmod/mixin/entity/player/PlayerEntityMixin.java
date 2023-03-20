package com.github.theredbrain.rpgmod.mixin.entity.player;

import com.github.theredbrain.rpgmod.effect.FoodStatusEffect;
import com.github.theredbrain.rpgmod.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpgmod.entity.ExtendedEquipmentSlotType;
import com.github.theredbrain.rpgmod.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpgmod.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.rpgmod.registry.EntityAttributesRegistry;
import com.github.theredbrain.rpgmod.registry.StatusEffectsRegistry;
import com.github.theredbrain.rpgmod.screen.AdventureInventoryScreenHandler;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements DuckPlayerEntityMixin {

    @Shadow
    @Final
    private PlayerInventory inventory;

    @Shadow
    @Final
    public PlayerScreenHandler playerScreenHandler;

    @Shadow
    public ScreenHandler currentScreenHandler;

//    @Shadow
//    protected HungerManager hungerManager;
//
//    @Shadow
//    public int experiencePickUpDelay;
//
//    @Shadow
//    private int sleepTimer;
//    @Shadow
//    @Final
//    private DefaultedList<ItemStack> syncedArmorStacks = DefaultedList.ofSize(4, ItemStack.EMPTY);

//    @Shadow
//    private ItemStack selectedItem;
//
//    @Shadow
//    @Final
//    private ItemCooldownManager itemCooldownManager;
//    @Shadow public abstract boolean isCreative();
//
//    @Shadow // 293
//    protected boolean updateWaterSubmersionState() {
//        throw new AssertionError();
//    }
//
//    @Shadow // 298
//    private void updateTurtleHelmet() {
//        throw new AssertionError();
//    }
//
//    @Shadow // 309
//    private void updateCapeAngles() {
//        throw new AssertionError();
//    }
//
//    @Shadow // 340
//    protected void updatePose() {
//        throw new AssertionError();
//    }

//    @Shadow // 416
//    public void closeHandledScreen() {
//        throw new AssertionError();
//    }

//    @Shadow // 1241
//    public void wakeUp(boolean skipSleepTimer, boolean updateSleepingPlayers) {
//        throw new AssertionError();
//    }
//
//    @Shadow // 1323
//    public void incrementStat(Identifier stat) {
//        throw new AssertionError();
//    }
//
//    @Shadow // 1882
//    public void resetLastAttackedTicks() {
//        throw new AssertionError();
//    }

    private static final TrackedData<Float> MANA = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> STAMINA = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);

    private boolean inAdventureBuildingMode;
    private boolean alternativeMainHandUsed;
    private boolean alternativeOffHandUsed;
    private boolean permanentMount;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    private AdventureInventoryScreenHandler adventureInventoryScreenHandler;

    /**
     * @author TheRedBrain
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    public void PlayerEntity(World world, BlockPos pos, float yaw, GameProfile gameProfile, CallbackInfo ci) {
        this.inAdventureBuildingMode = false;
        this.adventureInventoryScreenHandler = new AdventureInventoryScreenHandler(this.inventory, !world.isClient, (PlayerEntity) (Object) this);
        this.currentScreenHandler = this.adventureInventoryScreenHandler;
        this.syncedHandStacks = DefaultedList.ofSize(4, ItemStack.EMPTY);
        this.syncedArmorStacks = DefaultedList.ofSize(10, ItemStack.EMPTY);
        this.alternativeMainHandUsed = false;
        this.alternativeOffHandUsed = false;
        this.permanentMount = false;
        // inject into a constructor
    }

    @Inject(method = "createPlayerAttributes", at = @At("RETURN"), cancellable = true)
    private static void createPlayerAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.setReturnValue(cir.getReturnValue().add(EntityAttributesRegistry.BAM_HEALTH_REGENERATION, 0.0F)
                .add(EntityAttributesRegistry.BAM_MANA_REGENERATION, 1.0F)
                .add(EntityAttributesRegistry.BAM_STAMINA_REGENERATION, 1.0F)
                .add(EntityAttributesRegistry.BAM_MAX_MANA, 0.0F)
                .add(EntityAttributesRegistry.BAM_MAX_STAMINA, 10.0F)
        );
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void bam$initDataTracker(CallbackInfo ci) {
        this.dataTracker.startTracking(MANA, 0.0F);
        this.dataTracker.startTracking(STAMINA, 0.0F);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void bam$tick(CallbackInfo ci) {
        if (!this.world.isClient && this.currentScreenHandler == this.playerScreenHandler) {
            this.currentScreenHandler = this.adventureInventoryScreenHandler;
        }
    }


    @Inject(method = "shouldDismount", at = @At("RETURN"), cancellable = true)
    protected void bam$shouldDismount(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() && !(((PlayerEntity) (Object) this).hasStatusEffect(StatusEffectsRegistry.PERMANENT_MOUNT_EFFECT)));
//        cir.cancel();
    }

    @Inject(method = "closeHandledScreen", at = @At("TAIL"), cancellable = true)
    protected void bam$closeHandledScreen(CallbackInfo ci) {
        this.currentScreenHandler = this.adventureInventoryScreenHandler;
        ci.cancel();
    }

    @Inject(method = "shouldCloseHandledScreenOnRespawn", at = @At("RETURN"), cancellable = true)
    public void bam$shouldCloseHandledScreenOnRespawn(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.currentScreenHandler != this.adventureInventoryScreenHandler);
        cir.cancel();
    }

    @Inject(method = "damage", at = @At("RETURN"))
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        boolean bl = cir.getReturnValue();
        if (bl) {
            this.stopUsingItem();
        }
    }

//    @Inject(method = "damageArmor", at = @At("TAIL"))
//    public void bam$damageArmor(DamageSource source, float amount, CallbackInfo ci) {
//        this.inventory.damageExtraArmor(source, amount, PlayerInventory.ARMOR_SLOTS);
//    }

    @Inject(method = "getEquippedStack", at = @At("RETURN"), cancellable = true)
    public void getEquippedStack(EquipmentSlot slot, CallbackInfoReturnable<ItemStack> cir) {
        if (slot == EquipmentSlot.OFFHAND) {
            cir.setReturnValue(((DuckPlayerInventoryMixin)this.inventory).bam$getOffHandStack());
            cir.cancel();
        }
        if (slot == ExtendedEquipmentSlot.PLAYER_SKIN_ARMOR) {
            cir.setReturnValue(((DuckPlayerInventoryMixin)this.inventory).bam$getPlayerSkinItem());
            cir.cancel();
        }
    }

    /**
     * @author TheRedBrain
     */
    @Overwrite
    public void equipStack(EquipmentSlot slot, ItemStack stack) {
        this.processEquippedStack(stack);
        if (slot == EquipmentSlot.MAINHAND) {
            if (this.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT)) {
                if (this.alternativeMainHandUsed) {
                    this.onEquipStack(slot, ((DuckPlayerInventoryMixin)this.inventory).bam$setMainHand(stack), stack);//set(this.inventory.selectedSlot, stack), stack);
                } else {
                    this.onEquipStack(slot, ((DuckPlayerInventoryMixin)this.inventory).bam$setAlternativeMainHand(stack), stack);
                }
            } else {
                this.onEquipStack(slot, this.inventory.main.set(this.inventory.selectedSlot, stack), stack);
            }
        } else if (slot == EquipmentSlot.OFFHAND) {
            if (this.alternativeOffHandUsed) {
                this.onEquipStack(slot, ((DuckPlayerInventoryMixin)this.inventory).bam$setAlternativeOffHand(stack), stack);
            } else {
                this.onEquipStack(slot, this.inventory.offHand.set(0, stack), stack);
            }
        } else if (slot.getType() == EquipmentSlot.Type.ARMOR) {
            this.onEquipStack(slot, this.inventory.armor.set(slot.getEntitySlotId(), stack), stack);
        } else if (slot.getType() == ExtendedEquipmentSlotType.ACCESSORY) {
            this.onEquipStack(slot, ((DuckPlayerInventoryMixin)this.inventory).bam$setAccessorySlot(slot.getEntitySlotId(), stack), stack);
        }
    }
//
//    @Inject(method = "getArmorItems", at = @At("RETURN"), cancellable = true)
//    public void getArmorItems(CallbackInfoReturnable<Iterable<ItemStack>> cir) {
//        DefaultedList<ItemStack> allArmourItems = (DefaultedList<ItemStack>) cir.getReturnValue();
//        allArmourItems.addAll(((DuckPlayerInventoryMixin)this.inventory).getExtraArmour());
//        cir.setReturnValue(allArmourItems);
//    }

    // custom check for adventure food
    public boolean canConsumeItem(ItemStack itemStack) {
        List<Pair<StatusEffectInstance, Float>> list = itemStack.getItem().getFoodComponent().getStatusEffects();
        if (itemStack.isFood() && list != null) {
            for (Pair<StatusEffectInstance, Float> pair : list) {
                if (world.isClient || pair.getFirst() == null) continue;
                return tryEatAdventureFood(pair.getFirst());
            }
        }
        return false;
    }

    public boolean tryEatAdventureFood(StatusEffectInstance statusEffectInstance) {
        if (getStatusEffects().isEmpty()) {
            return true;
        } else {
            int currentEatenFoods = 0;
            List<StatusEffectInstance> currentEffects = getStatusEffects().stream().toList();
            for (StatusEffectInstance currentEffect : currentEffects) {
                if (currentEffect.getEffectType() == statusEffectInstance.getEffectType()) {
                    return false;
                } else if (currentEffect.getEffectType() instanceof FoodStatusEffect) {
                    currentEatenFoods++;
                }
            }
            // TODO get entityAttribute maxFoodEffects
            return currentEatenFoods < 3;
        }
    }

    public float bam$getHealthRegeneration() {
        return (float) this.getAttributeValue(EntityAttributesRegistry.BAM_HEALTH_REGENERATION);
    }

    public float bam$getManaRegeneration() {
        return (float) this.getAttributeValue(EntityAttributesRegistry.BAM_MANA_REGENERATION);
    }

    public float bam$getMaxMana() {
        return (float) this.getAttributeValue(EntityAttributesRegistry.BAM_MAX_MANA);
    }

    public void bam$addMana(float amount) {
        float f = this.bam$getMana();
        this.bam$setMana(f + amount);
    }

    public float bam$getMana() {
        return this.dataTracker.get(MANA);
    }

    public void bam$setMana(float mana) {
        this.dataTracker.set(MANA, MathHelper.clamp(mana, 0, this.bam$getMaxMana()));
    }

    public float bam$getStaminaRegeneration() {
        return (float) this.getAttributeValue(EntityAttributesRegistry.BAM_STAMINA_REGENERATION);
    }
    public float bam$getMaxStamina() {
        return (float) this.getAttributeValue(EntityAttributesRegistry.BAM_MAX_STAMINA);
    }

    public void bam$addStamina(float amount) {
        float f = this.bam$getStamina();
        this.bam$setStamina(f + amount);
    }

    public float bam$getStamina() {
        return this.dataTracker.get(STAMINA);
    }

    public void bam$setStamina(float mana) {
        this.dataTracker.set(STAMINA, MathHelper.clamp(mana, 0, this.bam$getMaxStamina()));
    }

    public boolean isInAdventureBuildingMode() {
        return this.inAdventureBuildingMode;
    }

    public void setInAdventureBuildingMode(boolean bl) {
       this.inAdventureBuildingMode = bl;
    }

    public boolean hasPermanentMount() {
        return this.permanentMount;
    }

    public void setPermanentMount(boolean bl) {
       this.permanentMount = bl;
    }

    public AdventureInventoryScreenHandler getAdventureInventoryScreenHandler() {
        return adventureInventoryScreenHandler;
    }

    /**
     * Returns whether this player is in adventure mode.
     */
    public abstract boolean isAdventure();

//    public boolean isAlternativeMainHandUsed() {
//        return this.alternativeMainHandUsed;
//    }
//
//    public void setAlternativeMainHandUsed(boolean bl) {
//        this.alternativeMainHandUsed = bl;
//    }
//
//    public boolean isAlternativeOffHandUsed() {
//        return this.alternativeOffHandUsed;
//    }
//
//    public void setAlternativeOffHandUsed(boolean bl) {
//        this.alternativeOffHandUsed = bl;
//    }
}
