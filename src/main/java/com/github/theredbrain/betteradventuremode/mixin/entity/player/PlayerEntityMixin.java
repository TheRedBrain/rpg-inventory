package com.github.theredbrain.betteradventuremode.mixin.entity.player;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.block.AbstractSetSpawnBlock;
import com.github.theredbrain.betteradventuremode.item.BasicWeaponItem;
import com.github.theredbrain.betteradventuremode.effect.FoodStatusEffect;
import com.github.theredbrain.betteradventuremode.data.Dialogue;
import com.github.theredbrain.betteradventuremode.block.entity.*;
import com.github.theredbrain.betteradventuremode.entity.DuckLivingEntityMixin;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.betteradventuremode.registry.EntityAttributesRegistry;
import com.github.theredbrain.betteradventuremode.registry.GameRulesRegistry;
import com.github.theredbrain.betteradventuremode.registry.StatusEffectsRegistry;
import com.github.theredbrain.betteradventuremode.registry.Tags;
import com.mojang.datafixers.util.Pair;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Mixin(value = PlayerEntity.class, priority = 1050)
public abstract class PlayerEntityMixin extends LivingEntity implements DuckPlayerEntityMixin, DuckLivingEntityMixin {

    @Shadow
    @Final
    private PlayerInventory inventory;

    @Shadow
    @Final
    public PlayerScreenHandler playerScreenHandler;

    @Shadow public abstract PlayerInventory getInventory();

    @Shadow protected abstract void vanishCursedItems();

    @Shadow protected EnderChestInventory enderChestInventory;
    @Shadow @Final private PlayerAbilities abilities;

    @Shadow public abstract void incrementStat(Identifier stat);

    @Shadow public abstract void sendMessage(Text message, boolean overlay);

    @Shadow public abstract void addExhaustion(float exhaustion);

    @Unique
    private boolean isAdventureHotbarCleanedUp = false;

    @Unique
    private static final TrackedData<Boolean> USE_STASH_FOR_CRAFTING = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Unique
    private int oldActiveSpellSlotAmount = 0;

    @Unique
    protected SimpleInventory stashInventory = new SimpleInventory(34);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

//    /**
//     * @author TheRedBrain
//     */
//    @Inject(method = "<init>", at = @At("TAIL"))
//    public void PlayerEntity(World world, BlockPos pos, float yaw, GameProfile gameProfile, CallbackInfo ci) {
//        // inject into a constructor
//    }

    @Inject(method = "createPlayerAttributes", at = @At("RETURN"), cancellable = true)
    private static void betteradventuremode$createPlayerAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.setReturnValue(cir.getReturnValue()
                .add(EntityAttributesRegistry.MAX_EQUIPMENT_WEIGHT, 10.0F) // TODO balance
                .add(EntityAttributesRegistry.EQUIPMENT_WEIGHT, 0.0F)
                .add(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT, 2.0F) // TODO balance
        );
    }

    @Inject(method = "initDataTracker", at = @At("RETURN"))
    protected void betteradventuremode$initDataTracker(CallbackInfo ci) {
        this.dataTracker.startTracking(USE_STASH_FOR_CRAFTING, true);

    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void betteradventuremode$tick(CallbackInfo ci) {
        if (!this.getWorld().isClient) {
            this.ejectItemsFromInactiveSpellSlots();
            this.ejectNonHotbarItemsFromHotbar();
        }
    }

    @Inject(method = "updateTurtleHelmet", at = @At("TAIL"))
    private void betteradventuremode$updateTurtleHelmet(CallbackInfo ci) {
        boolean mana_regenerating_trinket_equipped = false;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            Predicate<ItemStack> predicate = stack -> stack.isIn(Tags.MANA_REGENERATING_TRINKETS);
            mana_regenerating_trinket_equipped = trinkets.get().isEquipped(predicate);
        }
        ItemStack itemStackMainHand = this.getEquippedStack(EquipmentSlot.MAINHAND);
        ItemStack itemStackOffHand = this.getEquippedStack(EquipmentSlot.OFFHAND);
        if (!itemStackMainHand.isIn(Tags.ATTACK_ITEMS) && this.betteradventuremode$isAdventure() && !this.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT)) {
            if (!this.hasStatusEffect(StatusEffectsRegistry.NO_ATTACK_ITEMS_EFFECT)) {
                this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.NO_ATTACK_ITEMS_EFFECT, -1, 0, false, false, false));
            }
        } else {
            this.removeStatusEffect(StatusEffectsRegistry.NO_ATTACK_ITEMS_EFFECT);
        }
        if (itemStackMainHand.isIn(Tags.TWO_HANDED_ITEMS) && !this.hasStatusEffect(StatusEffectsRegistry.TWO_HANDED_EFFECT) && this.betteradventuremode$isAdventure() && !this.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT)) {
            if (!this.hasStatusEffect(StatusEffectsRegistry.NEED_EMPTY_OFFHAND_EFFECT)) {
                this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.NEED_EMPTY_OFFHAND_EFFECT, -1, 0, false, false, false));
            }
        } else {
            this.removeStatusEffect(StatusEffectsRegistry.NEED_EMPTY_OFFHAND_EFFECT);
        }
        if (mana_regenerating_trinket_equipped) {
            if (!this.hasStatusEffect(StatusEffectsRegistry.MANA_REGENERATION_EFFECT)) {
                this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.MANA_REGENERATION_EFFECT, -1, 0, false, false, true));
            }
        } else {
            this.removeStatusEffect(StatusEffectsRegistry.MANA_REGENERATION_EFFECT);
        }
    }

    /**
     * @author TheRedBrain
     * @reason
     */
    @Overwrite
    public void dropInventory() {
        super.dropInventory();
        if (!this.getWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
            this.vanishCursedItems();
            if (this.getWorld().getGameRules().getBoolean(GameRulesRegistry.DESTROY_DROPPED_ITEMS_ON_DEATH)) {
                this.inventory.clear();
            } else {
                this.inventory.dropAll();
            }
            if (this.getWorld().getGameRules().getBoolean(GameRulesRegistry.CLEAR_ENDER_CHEST_ON_DEATH)) {
                this.enderChestInventory.clear();
            }
        }

    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void betteradventuremode$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {

        if (nbt.contains("stash_items", NbtElement.LIST_TYPE)) {
            this.stashInventory.readNbtList(nbt.getList("stash_items", NbtElement.COMPOUND_TYPE));
        }

        if (nbt.contains("use_stash_for_crafting", NbtElement.BYTE_TYPE)) {
            this.betteradventuremode$setUseStashForCrafting(nbt.getBoolean("use_stash_for_crafting"));
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void betteradventuremode$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {

        nbt.put("stash_items", this.stashInventory.toNbtList());

        nbt.putBoolean("use_stash_for_crafting", this.betteradventuremode$useStashForCrafting());
    }

//    // TODO move to bamentity
//    @Inject(method = "shouldDismount", at = @At("RETURN"), cancellable = true)
//    protected void bamcore$shouldDismount(CallbackInfoReturnable<Boolean> cir) {
//        cir.setReturnValue(cir.getReturnValue() && !(((PlayerEntity) (Object) this).hasStatusEffect(BetterAdventureModeCoreStatusEffects.PERMANENT_MOUNT_EFFECT)));
////        cir.cancel();
//    }

    /**
     * @author TheRedBrain
     * @reason
     */
    @Overwrite
    public void applyDamage(DamageSource source, float amount) {
        super.applyDamage(source, amount);
    }

    /**
     * @author TheRedBrain
     * @reason
     */
    @Overwrite
    public static Optional<Vec3d> findRespawnPosition(ServerWorld world, BlockPos pos, float angle, boolean forced, boolean alive) {
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        if (block instanceof AbstractSetSpawnBlock) {
            return AbstractSetSpawnBlock.findRespawnPosition(EntityType.PLAYER, world, pos);
        } else if (block instanceof RespawnAnchorBlock && (forced || (Integer)blockState.get(RespawnAnchorBlock.CHARGES) > 0) && RespawnAnchorBlock.isNether(world) && world.getGameRules().getBoolean(GameRulesRegistry.CAN_SET_SPAWN_ON_RESPAWN_ANCHOR)) {
            Optional<Vec3d> optional = RespawnAnchorBlock.findRespawnPosition(EntityType.PLAYER, world, pos);
            if (!forced && !alive && optional.isPresent()) {
                world.setBlockState(pos, (BlockState)blockState.with(RespawnAnchorBlock.CHARGES, (Integer)blockState.get(RespawnAnchorBlock.CHARGES) - 1), Block.NOTIFY_ALL);
            }

            return optional;
        } else if (block instanceof BedBlock && BedBlock.isBedWorking(world) && world.getGameRules().getBoolean(GameRulesRegistry.CAN_SET_SPAWN_ON_BEDS)) {
            return BedBlock.findWakeUpPosition(EntityType.PLAYER, world, pos, (Direction)blockState.get(BedBlock.FACING), angle);
        } else if (!forced) {
            return Optional.empty();
        } else {
            boolean bl = block.canMobSpawnInside(blockState);
            BlockState blockState2 = world.getBlockState(pos.up());
            boolean bl2 = blockState2.getBlock().canMobSpawnInside(blockState2);
            return bl && bl2 ? Optional.of(new Vec3d((double)pos.getX() + 0.5, (double)pos.getY() + 0.1, (double)pos.getZ() + 0.5)) : Optional.empty();
        }
    }

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    public void betteradventuremode$attack(Entity target, CallbackInfo ci) {
        ItemStack mainHandStack = this.getMainHandStack();
        if (mainHandStack.getItem() instanceof BasicWeaponItem && this.betteradventuremode$getStamina() + ((BasicWeaponItem)mainHandStack.getItem()).getStaminaCost() <= 0) {
            this.sendMessage(Text.translatable("hud.message.staminaTooLow"), true);
            ci.cancel();
        }
    }

    // effectively disables the vanilla jump crit mechanic
    @Redirect(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;hasVehicle()Z"
            )
    )
    public boolean betteradventuremode$redirect_hasVehicle(PlayerEntity instance) {
        return true;
    }

    /**
     * @author TheRedBrain
     * @reason TODO
     */
    @Overwrite
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            return this.inventory.getMainHandStack();
        }
        if (slot == EquipmentSlot.OFFHAND) {
            return ((DuckPlayerInventoryMixin)this.inventory).betteradventuremode$getOffHandStack();
        }
        if (slot.getType() == EquipmentSlot.Type.ARMOR) {
            ItemStack stack = ItemStack.EMPTY;
                if (slot.getEntitySlotId() == 0) {
                    return ((DuckPlayerInventoryMixin)this.inventory).betteradventuremode$getFeetStack();
                } else if (slot.getEntitySlotId() == 1) {
                    return ((DuckPlayerInventoryMixin)this.inventory).betteradventuremode$getLegsStack();
                } else if (slot.getEntitySlotId() == 2) {
                    return ((DuckPlayerInventoryMixin)this.inventory).betteradventuremode$getChestStack();
                } else if (slot.getEntitySlotId() == 3) {
                    return ((DuckPlayerInventoryMixin)this.inventory).betteradventuremode$getHeadStack();
                }
            return stack;
        }
        return ItemStack.EMPTY;
    }

    /**
     * @author TheRedBrain
     * @reason TODO
     */
    @Overwrite
    public void equipStack(EquipmentSlot slot, ItemStack stack) {
        this.processEquippedStack(stack);
        if (slot == EquipmentSlot.MAINHAND) {
            this.onEquipStack(slot, ((DuckPlayerInventoryMixin)this.inventory).betteradventuremode$setMainHand(stack), stack);
        } else if (slot == EquipmentSlot.OFFHAND) {
            this.onEquipStack(slot, ((DuckPlayerInventoryMixin)this.inventory).betteradventuremode$setOffHand(stack), stack);
        }
        else if (slot.getType() == EquipmentSlot.Type.ARMOR) {
            if (slot.getEntitySlotId() == 0) {
                this.onEquipStack(slot, ((DuckPlayerInventoryMixin)this.inventory).betteradventuremode$setFeetStack(stack), stack);
            } else if (slot.getEntitySlotId() == 1) {
                this.onEquipStack(slot, ((DuckPlayerInventoryMixin)this.inventory).betteradventuremode$setLegsStack(stack), stack);
            } else if (slot.getEntitySlotId() == 2) {
                this.onEquipStack(slot, ((DuckPlayerInventoryMixin)this.inventory).betteradventuremode$setChestStack(stack), stack);
            } else if (slot.getEntitySlotId() == 3) {
                this.onEquipStack(slot, ((DuckPlayerInventoryMixin)this.inventory).betteradventuremode$setHeadStack(stack), stack);
            }
        }
    }

    /**
     * @author TheRedBrain
     * @reason
     */
    @Overwrite
    public Iterable<ItemStack> getArmorItems() {
        return ((DuckPlayerInventoryMixin)this.inventory).betteradventuremode$getArmorTrinkets();
    }

    /**
     * @author TheRedBrain
     * @reason
     */
    @Overwrite
    public void jump() {
        if (this.hasStatusEffect(StatusEffectsRegistry.OVERBURDENED_EFFECT) || this.betteradventuremode$getStamina() <= 0) {
            return;
        }
        super.jump();
        this.incrementStat(Stats.JUMP);
        if (this.isSprinting()) {
            if (!BetterAdventureMode.serverConfig.disable_vanilla_food_system) {
                this.addExhaustion(0.2F);
            }
            this.betteradventuremode$addStamina(-2);
        } else {
            if (!BetterAdventureMode.serverConfig.disable_vanilla_food_system) {
                this.addExhaustion(0.05F);
            }
            this.betteradventuremode$addStamina(-1);
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

    // custom check for adventure food
    @Unique
    public boolean betteradventuremode$canConsumeItem(ItemStack itemStack) {
        if (itemStack.getItem().getFoodComponent() != null) {
            List<Pair<StatusEffectInstance, Float>> list = itemStack.getItem().getFoodComponent().getStatusEffects();
            for (Pair<StatusEffectInstance, Float> pair : list) {
                if (getWorld().isClient || pair.getFirst() == null) continue;
                return betteradventuremode$tryEatAdventureFood(pair.getFirst());
            }
        }
        return false;
    }

    @Unique
    public boolean betteradventuremode$tryEatAdventureFood(StatusEffectInstance statusEffectInstance) {
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
    public float betteradventuremode$getMaxEquipmentWeight() {
        return (float) this.getAttributeValue(EntityAttributesRegistry.MAX_EQUIPMENT_WEIGHT);
    }

    @Override
    public float betteradventuremode$getEquipmentWeight() {
        return (float) this.getAttributeValue(EntityAttributesRegistry.EQUIPMENT_WEIGHT);
    }

    /**
     * Returns whether this player is in adventure mode.
     */
    @Override
    public abstract boolean betteradventuremode$isAdventure();

    @Override
    public boolean betteradventuremode$useStashForCrafting() {
        return this.dataTracker.get(USE_STASH_FOR_CRAFTING);
    }

    @Override
    public void betteradventuremode$setUseStashForCrafting(boolean useStashForCrafting) {
        this.dataTracker.set(USE_STASH_FOR_CRAFTING, useStashForCrafting);
    }

    @Override
    public SimpleInventory betteradventuremode$getStashInventory() {
        return this.stashInventory;
    }

    @Override
    public void betteradventuremode$setStashInventory(SimpleInventory stashInventory) {
        this.stashInventory = stashInventory;
    }

    @Unique
    private void ejectItemsFromInactiveSpellSlots() {
        int activeSpellSlotAmount = (int) this.getAttributeInstance(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT).getValue();

        if (this.oldActiveSpellSlotAmount != activeSpellSlotAmount) {
            for (int j = activeSpellSlotAmount; j < 8; j++) {
                PlayerInventory playerInventory = this.getInventory();

                if (!((DuckPlayerInventoryMixin)playerInventory).betteradventuremode$getSpellSlotStack(j).isEmpty()) {
                    playerInventory.offerOrDrop(((DuckPlayerInventoryMixin)playerInventory).betteradventuremode$setSpellSlotStack(ItemStack.EMPTY, j));
                    if (((PlayerEntity) (Object) this) instanceof ServerPlayerEntity serverPlayerEntity) {
                        serverPlayerEntity.sendMessage(Text.translatable("hud.message.spellsRemovedFromInactiveSpellSlots"), false);
                    }
                }
            }

            this.oldActiveSpellSlotAmount = activeSpellSlotAmount;
        }
    }

    @Unique
    private void ejectNonHotbarItemsFromHotbar() { // FIXME is only called once?
        if (this.betteradventuremode$isAdventure() && !this.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT)) {
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
    private double getEncumbrance() {
        return this.betteradventuremode$getEquipmentWeight() / Math.max(1, this.betteradventuremode$getMaxEquipmentWeight());
    }

    @Override
    public float betteradventuremode$getRegeneratedHealth() { // TODO balance
        return (float) (
                this.betteradventuremode$getHealthRegeneration() *
                (this.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) ? 5 : 1) *
                (this.getEncumbrance() > 1 ? 0.75 : 1)
        );
    }

    @Override
    public float betteradventuremode$getRegeneratedStamina() { // TODO balance
        double encumbrance = this.getEncumbrance();
        return (float) (
                this.betteradventuremode$getStaminaRegeneration() *
                (this.isSprinting() ? 0 : this.betteradventuremode$isMoving() ? 0.5 : 1) *
                (this.isBlocking() ? 0.5 : 1) *
                (encumbrance <= 0.5 ? 1 : encumbrance <= 1 ? 0.75 : 0.5) *
                (this.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) ? 5 : 1)
        );
    }

    @Override
    public float betteradventuremode$getRegeneratedMana() { // TODO balance
        StatusEffectInstance manaRegenerationEffectInstance = this.getStatusEffect(StatusEffectsRegistry.MANA_REGENERATION_EFFECT);
        return (float) (
                this.betteradventuremode$getManaRegeneration() *
                (this.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) ? 10 : 1) *
                (manaRegenerationEffectInstance != null ? (manaRegenerationEffectInstance.getAmplifier() > 0 ? 1 : 0.5) : 0.5) *
                (this.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || this.hasStatusEffect(StatusEffectsRegistry.MANA_REGENERATION_EFFECT) ? 1 : 0)
        );
    }

    @Override
    public int betteradventuremode$getStaggerDuration() {
        return 200;//24;
    }

    @Override
    public float betteradventuremode$getMaxStaggerBuildUp() {
//        return this.getMaxHealth() * 0.4f;
        return 20.0f;
    }

    @Override
    public boolean betteradventuremode$canParry() {
        return true;
    }

    @Override
    public void betteradventuremode$openHousingScreen() {
    }

    @Override
    public void betteradventuremode$openJigsawPlacerBlockScreen(JigsawPlacerBlockEntity jigsawPlacerBlock) {
    }

    @Override
    public void betteradventuremode$openRedstoneTriggerBlockScreen(RedstoneTriggerBlockEntity redstoneTriggerBlock) {
    }

    @Override
    public void betteradventuremode$openRelayTriggerBlockScreen(RelayTriggerBlockEntity relayTriggerBlock) {
    }

    @Override
    public void betteradventuremode$openTriggeredCounterBlockScreen(TriggeredCounterBlockEntity triggeredCounterBlock) {
    }

    @Override
    public void betteradventuremode$openResetTriggerBlockScreen(ResetTriggerBlockEntity resetTriggerBlock) {
    }

    @Override
    public void betteradventuremode$openDelayTriggerBlockScreen(DelayTriggerBlockEntity delayTriggerBlock) {
    }

    @Override
    public void betteradventuremode$openUseRelayBlockScreen(UseRelayBlockEntity useRelayBlock) {
    }

    @Override
    public void betteradventuremode$openTriggeredMessageBlockScreen(TriggeredMessageBlockEntity triggeredMessageBlock) {
    }

    @Override
    public void betteradventuremode$openTriggeredSpawnerBlockScreen(TriggeredSpawnerBlockEntity triggeredSpawnerBlock) {
    }

    @Override
    public void betteradventuremode$openMimicBlockScreen(MimicBlockEntity mimicBlock) {
    }

    @Override
    public void betteradventuremode$openLocationControlBlockScreen(LocationControlBlockEntity locationControlBlock) {
    }

    @Override
    public void betteradventuremode$openDialogueScreen(DialogueBlockEntity dialogueBlockEntity, @Nullable Dialogue dialogue) {
    }

    @Override
    public void betteradventuremode$openEntranceDelegationBlockScreen(EntranceDelegationBlockEntity entranceDelegationBlockEntity) {
    }

    @Override
    public void betteradventuremode$openStatusEffectApplierBlockScreen(StatusEffectApplierBlockEntity statusEffectApplierBlockEntity) {
    }

    @Override
    public void betteradventuremode$openTriggeredAdvancementCheckerBlockScreen(TriggeredAdvancementCheckerBlockEntity triggeredAdvancementCheckerBlock) {
    }
}
