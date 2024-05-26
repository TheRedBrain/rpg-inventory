package com.github.theredbrain.rpginventory.mixin.entity.player;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.IRenderEquippedTrinkets;
import com.github.theredbrain.rpginventory.entity.DuckLivingEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.rpginventory.registry.GameRulesRegistry;
import com.github.theredbrain.rpginventory.registry.StatusEffectsRegistry;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerEntity.class, priority = 950)
public abstract class PlayerEntityMixin extends LivingEntity implements DuckPlayerEntityMixin, DuckLivingEntityMixin, IRenderEquippedTrinkets {

    @Shadow
    @Final
    private PlayerInventory inventory;

    @Shadow
    @Final
    public PlayerScreenHandler playerScreenHandler;

    @Shadow public abstract PlayerInventory getInventory();

    @Shadow @Final private PlayerAbilities abilities;

    @Shadow public abstract void sendMessage(Text message, boolean overlay);

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow public abstract boolean isCreative();

    @Unique
    private boolean isAdventureHotbarCleanedUp = false;

    @Unique
    private static final TrackedData<Boolean> IS_MAIN_HAND_STACK_SHEATHED = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Unique
    private static final TrackedData<Boolean> IS_OFF_HAND_STACK_SHEATHED = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Unique
    private static final TrackedData<Integer> OLD_ACTIVE_SPELL_SLOT_AMOUNT = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "createPlayerAttributes", at = @At("RETURN"))
    private static void rpginventory$createPlayerAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.getReturnValue()
                .add(RPGInventory.EQUIPMENT_WEIGHT)
                .add(RPGInventory.MAX_EQUIPMENT_WEIGHT)
                .add(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT, 2.0F)
        ;
    }

    @Inject(method = "initDataTracker", at = @At("RETURN"))
    protected void rpginventory$initDataTracker(CallbackInfo ci) {
        this.dataTracker.startTracking(IS_MAIN_HAND_STACK_SHEATHED, false);
        this.dataTracker.startTracking(IS_OFF_HAND_STACK_SHEATHED, false);
        this.dataTracker.startTracking(OLD_ACTIVE_SPELL_SLOT_AMOUNT, -1);

    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void rpginventory$tick(CallbackInfo ci) {
        if (!this.getWorld().isClient) {
            this.ejectItemsFromInactiveSpellSlots();
            this.ejectNonHotbarItemsFromHotbar();
        }
    }

    @Inject(method = "updateTurtleHelmet", at = @At("TAIL"))
    private void rpginventory$updateTurtleHelmet(CallbackInfo ci) {

        ItemStack itemStackMainHand = this.getEquippedStack(EquipmentSlot.MAINHAND);
        ItemStack itemStackOffHand = this.getEquippedStack(EquipmentSlot.OFFHAND);
        StatusEffect adventure_building_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.building_mode_status_effect_identifier));
        boolean hasAdventureBuildingEffect = adventure_building_status_effect != null && this.hasStatusEffect(adventure_building_status_effect);
        if (!itemStackMainHand.isIn(Tags.ATTACK_ITEMS) && !this.isCreative() && !hasAdventureBuildingEffect && !RPGInventory.serverConfig.allow_attacking_with_non_attack_items) {
            if (!this.hasStatusEffect(StatusEffectsRegistry.NO_ATTACK_ITEMS_EFFECT)) {
                this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.NO_ATTACK_ITEMS_EFFECT, -1, 0, false, false, false));
            }
        } else {
            this.removeStatusEffect(StatusEffectsRegistry.NO_ATTACK_ITEMS_EFFECT);
        }
        if (itemStackMainHand.isIn(Tags.TWO_HANDED_ITEMS) && (this.rpginventory$isMainHandStackSheathed() || !this.rpginventory$isOffHandStackSheathed()) && !this.isCreative() && !hasAdventureBuildingEffect) {
            if (!this.hasStatusEffect(StatusEffectsRegistry.NEEDS_TWO_HANDING_EFFECT)) {
                this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.NEEDS_TWO_HANDING_EFFECT, -1, 0, false, false, false));
            }
        } else {
            this.removeStatusEffect(StatusEffectsRegistry.NEEDS_TWO_HANDING_EFFECT);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void rpginventory$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {

        if (nbt.contains("is_main_hand_stack_sheathed", NbtElement.BYTE_TYPE)) {
            this.rpginventory$setIsMainHandStackSheathed(nbt.getBoolean("is_main_hand_stack_sheathed"));
        }

        if (nbt.contains("is_off_hand_stack_sheathed", NbtElement.BYTE_TYPE)) {
            this.rpginventory$setIsOffHandStackSheathed(nbt.getBoolean("is_off_hand_stack_sheathed"));
        }

        if (nbt.contains("old_active_spell_slot_amount", NbtElement.INT_TYPE)) {
            this.rpginventory$setOldActiveSpellSlotAmount(nbt.getInt("old_active_spell_slot_amount"));
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void rpginventory$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {

        nbt.putBoolean("is_main_hand_stack_sheathed", this.rpginventory$isMainHandStackSheathed());

        nbt.putBoolean("is_off_hand_stack_sheathed", this.rpginventory$isOffHandStackSheathed());

        nbt.putInt("old_active_spell_slot_amount", this.rpginventory$oldActiveSpellSlotAmount());
    }

    /**
     * @author TheRedBrain
     * @reason
     */
    @Overwrite
    public void equipStack(EquipmentSlot slot, ItemStack stack) {
        this.processEquippedStack(stack);
        if (slot == EquipmentSlot.MAINHAND) {
            this.onEquipStack(slot, ((DuckPlayerInventoryMixin)this.inventory).rpginventory$setMainHand(stack), stack);
        } else if (slot == EquipmentSlot.OFFHAND) {
            this.onEquipStack(slot, this.inventory.offHand.set(0, stack), stack);
        } else if (slot.getType() == EquipmentSlot.Type.ARMOR) {
            this.onEquipStack(slot, this.inventory.armor.set(slot.getEntitySlotId(), stack), stack);
        }
    }

    /**
     * @author TheRedBrain
     * @reason
     */
    @Overwrite
    public Iterable<ItemStack> getArmorItems() {
        return ((DuckPlayerInventoryMixin)this.inventory).rpginventory$getArmor();
    }

    @Inject(method = "jump", at = @At("HEAD"), cancellable = true)
    public void rpginventory$jump(CallbackInfo ci) {
        if (this.hasStatusEffect(StatusEffectsRegistry.OVERBURDENED_EFFECT)) {
            ci.cancel();
        }
    }

    /**
     * @author TheRedBrain
     * @reason
     */
    @Overwrite
    public void updateSwimming() {
        if (this.abilities.flying) {
            this.setSwimming(false);
        } else {
            if (this.isSwimming()) {
                this.setSwimming(!this.hasStatusEffect(StatusEffectsRegistry.OVERBURDENED_EFFECT) && this.isTouchingWater() && !this.hasVehicle());
            } else {
                this.setSwimming(!this.hasStatusEffect(StatusEffectsRegistry.OVERBURDENED_EFFECT) && this.isSubmergedInWater() && !this.hasVehicle() && this.getWorld().getFluidState(this.getBlockPos()).isIn(FluidTags.WATER));
            }
        }

    }

    @Override
    public float rpginventory$getMaxEquipmentWeight() {
        return (float) this.getAttributeValue(RPGInventory.MAX_EQUIPMENT_WEIGHT);
    }

    @Override
    public float rpginventory$getEquipmentWeight() {
        return (float) this.getAttributeValue(RPGInventory.EQUIPMENT_WEIGHT);
    }

    @Override
    public float rpginventory$getActiveSpellSlotAmount() {
        return (float) this.getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT);
    }

    @Override
    public ItemStack rpginventory$getSheathedMainHandItemStack() {
        ItemStack itemStack = ((DuckPlayerInventoryMixin)this.getInventory()).rpginventory$getSheathedMainHand();
        return rpginventory$isMainHandStackSheathed() && !itemStack.isIn(Tags.EMPTY_HAND_WEAPONS) && ItemUtils.isUsable(itemStack) ? itemStack : ItemStack.EMPTY;
    }

    @Override
    public ItemStack rpginventory$getSheathedOffHandItemStack() {
        ItemStack itemStack = ((DuckPlayerInventoryMixin)this.getInventory()).rpginventory$getSheathedOffHand();
        return rpginventory$isOffHandStackSheathed() && !itemStack.isIn(Tags.EMPTY_HAND_WEAPONS) && ItemUtils.isUsable(itemStack) ? itemStack : ItemStack.EMPTY;
    }

    @Override
    public boolean rpginventory$isMainHandStackSheathed() {
        return this.dataTracker.get(IS_MAIN_HAND_STACK_SHEATHED);
    }

    @Override
    public void rpginventory$setIsMainHandStackSheathed(boolean isMainHandStackSheathed) {
        this.dataTracker.set(IS_MAIN_HAND_STACK_SHEATHED, isMainHandStackSheathed);

//        // TODO move
//        Item mainHandStackItem = this.getInventory().getMainHandStack().getItem();
//        if (mainHandStackItem instanceof IMakesEquipSound noiseMakingItem && !this.isSpectator()) {
//            SoundEvent equipSound = noiseMakingItem.getEquipSound();
//            if (equipSound != null) {
//
//                if (!this.getWorld().isClient() && !this.isSilent()) {
//                    this.getWorld().playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(), equipSound, this.getSoundCategory(), 1.0F, 1.0F);
//                }
//                this.emitGameEvent(GameEvent.EQUIP);
//            }
//        }
    }

    @Override
    public boolean rpginventory$isOffHandStackSheathed() {
        return this.dataTracker.get(IS_OFF_HAND_STACK_SHEATHED);
    }

    @Override
    public void rpginventory$setIsOffHandStackSheathed(boolean isOffHandStackSheathed) {
        this.dataTracker.set(IS_OFF_HAND_STACK_SHEATHED, isOffHandStackSheathed);

//        // TODO move
//        Item offHandStackItem = ((DuckPlayerInventoryMixin) this.getInventory()).rpginventory$getOffHandStack().getItem();
//        if (offHandStackItem instanceof IMakesEquipSound noiseMakingItem && !this.isSpectator()) {
//            SoundEvent equipSound = noiseMakingItem.getEquipSound();
//            if (equipSound != null) {
//
//                if (!this.getWorld().isClient() && !this.isSilent()) {
//                    this.getWorld().playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(), equipSound, this.getSoundCategory(), 1.0F, 1.0F);
//                }
//                this.emitGameEvent(GameEvent.EQUIP);
//            }
//        }
    }

    @Override
    public int rpginventory$oldActiveSpellSlotAmount() {
        return this.dataTracker.get(OLD_ACTIVE_SPELL_SLOT_AMOUNT);
    }

    @Override
    public void rpginventory$setOldActiveSpellSlotAmount(int oldActiveSpellSlotAmount) {
        this.dataTracker.set(OLD_ACTIVE_SPELL_SLOT_AMOUNT, oldActiveSpellSlotAmount);
    }

    @Unique
    private void ejectItemsFromInactiveSpellSlots() {
        int activeSpellSlotAmount = (int) this.rpginventory$getActiveSpellSlotAmount();

        if (this.rpginventory$oldActiveSpellSlotAmount() != activeSpellSlotAmount) {
            for (int j = activeSpellSlotAmount + 1; j < 9; j++) {
                PlayerInventory playerInventory = this.getInventory();

                if (!((DuckPlayerInventoryMixin)playerInventory).rpginventory$getSpellSlotStack(j).isEmpty()) {
                    playerInventory.offerOrDrop(((DuckPlayerInventoryMixin)playerInventory).rpginventory$setSpellSlotStack(ItemStack.EMPTY, j));
                    if (((PlayerEntity) (Object) this) instanceof ServerPlayerEntity serverPlayerEntity) {
                        serverPlayerEntity.sendMessage(Text.translatable("hud.message.spellsRemovedFromInactiveSpellSlots"), false);
                    }
                }
            }

            this.rpginventory$setOldActiveSpellSlotAmount(activeSpellSlotAmount);
        }
    }

    @Unique
    private void ejectNonHotbarItemsFromHotbar() { // FIXME is only called once?
        StatusEffect adventure_building_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.building_mode_status_effect_identifier));
        boolean hasAdventureBuildingEffect = adventure_building_status_effect != null && this.hasStatusEffect(adventure_building_status_effect);
        if (!this.isCreative() && !hasAdventureBuildingEffect && !((this.getServer() != null && this.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT) && !this.hasStatusEffect(StatusEffectsRegistry.WILDERNESS_EFFECT)) || this.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT))) {
            if (!this.isAdventureHotbarCleanedUp) {
                for (int i = 0; i < 9; i++) {
                    PlayerInventory playerInventory = this.getInventory();
                    Slot slot = this.playerScreenHandler.slots.get(i + 36);

                    if (!slot.inventory.getStack(slot.getIndex()).isIn(Tags.ADVENTURE_HOTBAR_ITEMS)) {
                        playerInventory.offerOrDrop(slot.inventory.removeStack(slot.getIndex()));
                    }
                }
                this.isAdventureHotbarCleanedUp = true;
            }
        } else {
            if (this.isAdventureHotbarCleanedUp) {
                this.isAdventureHotbarCleanedUp = false;
            }
        }
    }

    @Unique
    private double getEncumbrance() { // TODO balance
        return this.rpginventory$getEquipmentWeight() / Math.max(1, this.rpginventory$getMaxEquipmentWeight());
    }
}
