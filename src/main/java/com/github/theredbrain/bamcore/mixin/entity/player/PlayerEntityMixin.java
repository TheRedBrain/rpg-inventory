package com.github.theredbrain.bamcore.mixin.entity.player;

import com.github.theredbrain.bamcore.block.entity.AreaFillerBlockBlockEntity;
import com.github.theredbrain.bamcore.block.entity.ChunkLoaderBlockBlockEntity;
import com.github.theredbrain.bamcore.block.entity.StructurePlacerBlockBlockEntity;
import com.github.theredbrain.bamcore.api.effect.FoodStatusEffect;
import com.github.theredbrain.bamcore.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.bamcore.registry.EntityAttributesRegistry;
import com.github.theredbrain.bamcore.registry.StatusEffectsRegistry;
import com.github.theredbrain.bamcore.registry.Tags;
import com.github.theredbrain.bamcore.screen.AdventureInventoryScreenHandler;
import com.github.theredbrain.bamcore.screen.slot.AdventureTrinketSlot;
import com.google.common.collect.Iterables;
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
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
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

    @Shadow public abstract PlayerInventory getInventory();

    private static final TrackedData<Float> MANA = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> STAMINA = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);

    @Unique
    private int oldActiveSpellSlotAmount = 0;

    @Unique
    private boolean isAdventureHotbarCleanedUp = false;

    @Unique
    private AdventureInventoryScreenHandler adventureInventoryScreenHandler;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * @author TheRedBrain
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    public void PlayerEntity(World world, BlockPos pos, float yaw, GameProfile gameProfile, CallbackInfo ci) {
        this.adventureInventoryScreenHandler = new AdventureInventoryScreenHandler(this.inventory, !world.isClient, (PlayerEntity) (Object) this);
        this.currentScreenHandler = this.adventureInventoryScreenHandler;
        // inject into a constructor
    }

    @Inject(method = "createPlayerAttributes", at = @At("RETURN"), cancellable = true)
    private static void bamcore$createPlayerAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.setReturnValue(cir.getReturnValue()
                .add(EntityAttributesRegistry.MAX_EQUIPMENT_WEIGHT, 10.0F) // TODO balance
                .add(EntityAttributesRegistry.EQUIPMENT_WEIGHT, 0.0F)
                .add(EntityAttributesRegistry.HEALTH_REGENERATION, 0.0F) // TODO balance
                .add(EntityAttributesRegistry.MANA_REGENERATION, 0.0F) // TODO balance
                .add(EntityAttributesRegistry.STAMINA_REGENERATION, 1.0F) // TODO balance
                .add(EntityAttributesRegistry.MAX_MANA, 0.0F) // TODO balance
                .add(EntityAttributesRegistry.MAX_STAMINA, 10.0F) // TODO balance
                .add(EntityAttributesRegistry.MAX_POISE, 10.0F) // TODO balance
                .add(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT, 2.0F) // TODO balance
        );
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void bamcore$initDataTracker(CallbackInfo ci) {
        this.dataTracker.startTracking(MANA, 0.0F);
        this.dataTracker.startTracking(STAMINA, 0.0F);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void bamcore$tick(CallbackInfo ci) {
        if (!this.getWorld().isClient && this.currentScreenHandler == this.playerScreenHandler) {
            this.currentScreenHandler = this.adventureInventoryScreenHandler;
        }
        if (!this.getWorld().isClient) {
            this.ejectItemsFromInactiveSpellSlots();
            this.ejectNonHotbarItemsFromHotbar();
        }
    }

    @Inject(method = "updateTurtleHelmet", at = @At("TAIL"))
    private void bamcore$updateTurtleHelmet(CallbackInfo ci) {
        ItemStack itemStackMainHand = this.getEquippedStack(EquipmentSlot.MAINHAND);
        ItemStack itemStackOffHand = this.getEquippedStack(EquipmentSlot.OFFHAND);
        if (!itemStackMainHand.isIn(Tags.ATTACK_ITEMS) && this.bamcore$isAdventure()) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.NO_ATTACK_ITEMS_EFFECT, -1, 0, false, false, true));
        } else {
            this.removeStatusEffect(StatusEffectsRegistry.NO_ATTACK_ITEMS_EFFECT);
        }
        if (itemStackMainHand.isIn(Tags.TWO_HANDED_ITEMS) && !itemStackOffHand.isIn(Tags.EMPTY_HAND_WEAPONS) && this.bamcore$isAdventure()) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.NEED_EMPTY_OFFHAND_EFFECT, -1, 0, false, false, true));
        } else {
            this.removeStatusEffect(StatusEffectsRegistry.NEED_EMPTY_OFFHAND_EFFECT);
        }
    }


    @Inject(method = "shouldDismount", at = @At("RETURN"), cancellable = true)
    protected void bamcore$shouldDismount(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() && !(((PlayerEntity) (Object) this).hasStatusEffect(StatusEffectsRegistry.PERMANENT_MOUNT_EFFECT)));
//        cir.cancel();
    }

    @Inject(method = "closeHandledScreen", at = @At("TAIL"), cancellable = true)
    protected void bamcore$closeHandledScreen(CallbackInfo ci) {
        this.currentScreenHandler = this.adventureInventoryScreenHandler;
        ci.cancel();
    }

    @Inject(method = "shouldCloseHandledScreenOnRespawn", at = @At("RETURN"), cancellable = true)
    public void bamcore$shouldCloseHandledScreenOnRespawn(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.currentScreenHandler != this.adventureInventoryScreenHandler);
        cir.cancel();
    }

    // taking damage interrupts eating food, drinking potions, etc
    @Inject(method = "damage", at = @At("RETURN"))
    public void bamcore$damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        boolean bl = cir.getReturnValue();
        if (bl) {
            this.stopUsingItem();
        }
    }

    @Inject(method = "getEquippedStack", at = @At("RETURN"), cancellable = true)
    public void bamcore$getEquippedStack(EquipmentSlot slot, CallbackInfoReturnable<ItemStack> cir) {
        if (slot == EquipmentSlot.OFFHAND) {
            cir.setReturnValue(((DuckPlayerInventoryMixin)this.inventory).bamcore$getOffHandStack());
            cir.cancel();
        }
    }

    /**
     * @author TheRedBrain
     * @reason
     */
    @Overwrite
    public void equipStack(EquipmentSlot slot, ItemStack stack) {
        this.processEquippedStack(stack);
        if (slot == EquipmentSlot.MAINHAND) {
            this.onEquipStack(slot, ((DuckPlayerInventoryMixin)this.inventory).bamcore$setMainHand(stack), stack);
        } else if (slot == EquipmentSlot.OFFHAND) {
            this.onEquipStack(slot, this.inventory.offHand.set(0, stack), stack);
        } else if (slot == ExtendedEquipmentSlot.ALT_MAINHAND) {
            this.onEquipStack(slot, ((DuckPlayerInventoryMixin)this.inventory).bamcore$setAlternativeMainHand(stack), stack);
        } else if (slot == ExtendedEquipmentSlot.ALT_OFFHAND) {
            this.onEquipStack(slot, ((DuckPlayerInventoryMixin)this.inventory).bamcore$setAlternativeOffHand(stack), stack);
        } else if (slot.getType() == EquipmentSlot.Type.ARMOR) {
            this.onEquipStack(slot, this.inventory.armor.set(slot.getEntitySlotId(), stack), stack);
        }
    }

    // custom check for adventure food
    public boolean bamcore$canConsumeItem(ItemStack itemStack) {
        List<Pair<StatusEffectInstance, Float>> list = itemStack.getItem().getFoodComponent().getStatusEffects();
        if (itemStack.isFood() && list != null) {
            for (Pair<StatusEffectInstance, Float> pair : list) {
                if (getWorld().isClient || pair.getFirst() == null) continue;
                return bamcore$tryEatAdventureFood(pair.getFirst());
            }
        }
        return false;
    }

    public boolean bamcore$tryEatAdventureFood(StatusEffectInstance statusEffectInstance) {
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

    @Override
    public float bamcore$getMaxEquipmentWeight() {
        return (float) this.getAttributeValue(EntityAttributesRegistry.MAX_EQUIPMENT_WEIGHT);
    }

    @Override
    public float bamcore$getEquipmentWeight() {
        return (float) this.getAttributeValue(EntityAttributesRegistry.EQUIPMENT_WEIGHT);
    }

    @Override
    public float bamcore$getHealthRegeneration() {
        return (float) this.getAttributeValue(EntityAttributesRegistry.HEALTH_REGENERATION);
    }

    @Override
    public float bamcore$getManaRegeneration() {
        return (float) this.getAttributeValue(EntityAttributesRegistry.MANA_REGENERATION);
    }

    @Override
    public float bamcore$getMaxMana() {
        return (float) this.getAttributeValue(EntityAttributesRegistry.MAX_MANA);
    }

    @Override
    public void bamcore$addMana(float amount) {
        float f = this.bamcore$getMana();
        this.bamcore$setMana(f + amount);
    }

    @Override
    public float bamcore$getMana() {
        return this.dataTracker.get(MANA);
    }

    @Override
    public void bamcore$setMana(float mana) {
        this.dataTracker.set(MANA, MathHelper.clamp(mana, 0, this.bamcore$getMaxMana()));
    }

    @Override
    public float bamcore$getStaminaRegeneration() {
        return (float) this.getAttributeValue(EntityAttributesRegistry.STAMINA_REGENERATION);
    }

    @Override
    public float bamcore$getMaxStamina() {
        return (float) this.getAttributeValue(EntityAttributesRegistry.MAX_STAMINA);
    }

    @Override
    public void bamcore$addStamina(float amount) {
        float f = this.bamcore$getStamina();
        this.bamcore$setStamina(f + amount);
    }

    @Override
    public float bamcore$getStamina() {
        return this.dataTracker.get(STAMINA);
    }

    @Override
    public void bamcore$setStamina(float mana) {
        this.dataTracker.set(STAMINA, MathHelper.clamp(mana, 0, this.bamcore$getMaxStamina()));
    }

    public ScreenHandler bamcore$getInventoryScreenHandler() {
        return adventureInventoryScreenHandler;
    }

    /**
     * Returns whether this player is in adventure mode.
     */
    @Override
    public abstract boolean bamcore$isAdventure();

    @Override
    public void bamcore$openStructurePlacerBlockScreen(StructurePlacerBlockBlockEntity structurePlacerBlock) {
    }

    @Override
    public void bamcore$openAreaFillerBlockScreen(AreaFillerBlockBlockEntity areaFillerBlock) {
    }

    @Override
    public void bamcore$openChunkLoaderBlockScreen(ChunkLoaderBlockBlockEntity chunkLoaderBlock) {
    }

    @Override
    public Iterable<ItemStack> getItemsEquipped() {
        Iterable<ItemStack> alternateHandItems = Arrays.asList(this.getInventory().getStack(42), this.getInventory().getStack(43));
        return Iterables.concat(this.getHandItems(), this.getArmorItems(), alternateHandItems);
    }

    private void ejectItemsFromInactiveSpellSlots() {
        int activeSpellSlotAmount = (int) this.getAttributeInstance(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT).getValue();

        if (this.oldActiveSpellSlotAmount != activeSpellSlotAmount) {
            int[] spellSlotIds = ((AdventureInventoryScreenHandler) this.bamcore$getInventoryScreenHandler()).getSpellSlotIds();
            // eject items from inactive slots
            for (int j = activeSpellSlotAmount; j < 8; j++) {
                PlayerInventory playerInventory = this.getInventory();
                int slotId = spellSlotIds[j];
                AdventureTrinketSlot ats = (AdventureTrinketSlot) this.bamcore$getInventoryScreenHandler().slots.get(slotId);

                if (!ats.inventory.getStack(ats.getIndex()).isEmpty()) {
                    playerInventory.offerOrDrop(ats.inventory.removeStack(ats.getIndex()));
                    // TODO message to player
                }
            }

            this.oldActiveSpellSlotAmount = activeSpellSlotAmount;
        }
    }

    private void ejectNonHotbarItemsFromHotbar() {
        if (this.bamcore$isAdventure() && !this.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT)) {
            if (!this.isAdventureHotbarCleanedUp) {
                // eject items from inactive slots
                for (int i = 0; i < 9; i++) {
                    PlayerInventory playerInventory = this.getInventory();
                    Slot slot = this.bamcore$getInventoryScreenHandler().slots.get(i);

                    if (!slot.inventory.getStack(slot.getIndex()).isIn(Tags.ADVENTURE_HOTBAR_ITEMS)) {
                        playerInventory.offerOrDrop(slot.inventory.removeStack(slot.getIndex()));
                    }
                }
                this.isAdventureHotbarCleanedUp = true;
            }
        } else {
            this.isAdventureHotbarCleanedUp = false;
        }
    }

}
