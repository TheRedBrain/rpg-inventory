package com.github.theredbrain.betteradventuremode.mixin.entity.player;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.BetterAdventureModeClient;
import com.github.theredbrain.betteradventuremode.block.AbstractSetSpawnBlock;
import com.github.theredbrain.betteradventuremode.entity.IRenderEquippedTrinkets;
import com.github.theredbrain.betteradventuremode.effect.FoodStatusEffect;
import com.github.theredbrain.betteradventuremode.data.Dialogue;
import com.github.theredbrain.betteradventuremode.block.entity.*;
import com.github.theredbrain.betteradventuremode.entity.DuckLivingEntityMixin;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.betteradventuremode.item.IMakesEquipSound;
import com.github.theredbrain.betteradventuremode.registry.EntityAttributesRegistry;
import com.github.theredbrain.betteradventuremode.registry.GameRulesRegistry;
import com.github.theredbrain.betteradventuremode.registry.StatusEffectsRegistry;
import com.github.theredbrain.betteradventuremode.registry.Tags;
import com.github.theredbrain.betteradventuremode.util.ItemUtils;
import com.mojang.datafixers.util.Pair;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Mixin(value = PlayerEntity.class, priority = 950)
public abstract class PlayerEntityMixin extends LivingEntity implements DuckPlayerEntityMixin, DuckLivingEntityMixin, IRenderEquippedTrinkets {

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

    @Shadow public abstract void increaseStat(Identifier stat, int amount);

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow public abstract ActionResult interact(Entity entity, Hand hand);

    @Shadow public abstract boolean isCreative();

    @Unique
    private boolean isAdventureHotbarCleanedUp = false;

    @Unique
    private static final TrackedData<Boolean> IS_MAIN_HAND_STACK_SHEATHED = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Unique
    private static final TrackedData<Boolean> IS_OFF_HAND_STACK_SHEATHED = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Unique
    private static final TrackedData<Boolean> USE_STASH_FOR_CRAFTING = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Unique
    private static final TrackedData<Integer> OLD_ACTIVE_SPELL_SLOT_AMOUNT = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);

    @Unique
    protected SimpleInventory stashInventory = new SimpleInventory(34);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "createPlayerAttributes", at = @At("RETURN"))
    private static void betteradventuremode$createPlayerAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.getReturnValue()
                .add(EntityAttributesRegistry.EQUIPMENT_WEIGHT)
                .add(EntityAttributesRegistry.MAX_EQUIPMENT_WEIGHT)
                .add(EntityAttributesRegistry.MAX_FOOD_EFFECTS)
                .add(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT, 2.0F)
                .add(EntityAttributesRegistry.STAMINA_REGENERATION, 1.0F)
                .add(EntityAttributesRegistry.MAX_STAMINA, 10.0F)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 5.0F)
        ;
    }

    @Inject(method = "initDataTracker", at = @At("RETURN"))
    protected void betteradventuremode$initDataTracker(CallbackInfo ci) {
        this.dataTracker.startTracking(IS_MAIN_HAND_STACK_SHEATHED, false);
        this.dataTracker.startTracking(IS_OFF_HAND_STACK_SHEATHED, false);
        this.dataTracker.startTracking(USE_STASH_FOR_CRAFTING, true);
        this.dataTracker.startTracking(OLD_ACTIVE_SPELL_SLOT_AMOUNT, -1);

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

        boolean mana_regenerating_item_equipped = false;
        boolean fall_damage_prevention_item_equipped = false;
        boolean first_person_enabling_item_equipped = false;
        boolean keep_inventory_on_death_item_equipped = false;
        int damage_doubling_items_amount = 0;

        Predicate<ItemStack> mana_regenerating_item_equipped_predicate = stack -> stack.isIn(Tags.ENABLES_MANA_REGENERATION);
        Predicate<ItemStack> fall_damage_prevention_item_equipped_predicate = stack -> stack.isIn(Tags.PREVENTS_NON_LETHAL_FALL_DAMAGE);
        Predicate<ItemStack> first_person_enabling_item_equipped_predicate = stack -> stack.isIn(Tags.ENABLES_FIRST_PERSON_PERSPECTIVE);
        Predicate<ItemStack> is_damage_doubling_item_equipped_predicate = stack -> stack.isIn(Tags.DOUBLES_INCOMING_DAMAGE);
        Predicate<ItemStack> keep_inventory_on_death_item_equipped_predicate = stack -> stack.isIn(Tags.KEEPS_INVENTORY_ON_DEATH);

        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            mana_regenerating_item_equipped = trinkets.get().isEquipped(mana_regenerating_item_equipped_predicate);
            fall_damage_prevention_item_equipped = trinkets.get().isEquipped(fall_damage_prevention_item_equipped_predicate);
            first_person_enabling_item_equipped = trinkets.get().isEquipped(first_person_enabling_item_equipped_predicate);
            keep_inventory_on_death_item_equipped = trinkets.get().isEquipped(keep_inventory_on_death_item_equipped_predicate);
            damage_doubling_items_amount += trinkets.get().getEquipped(is_damage_doubling_item_equipped_predicate).size();
        }
        mana_regenerating_item_equipped = mana_regenerating_item_equipped || betteradventuremode$hasEquipped(mana_regenerating_item_equipped_predicate);
        fall_damage_prevention_item_equipped = fall_damage_prevention_item_equipped || betteradventuremode$hasEquipped(fall_damage_prevention_item_equipped_predicate);
        first_person_enabling_item_equipped = first_person_enabling_item_equipped || betteradventuremode$hasEquipped(first_person_enabling_item_equipped_predicate);
        keep_inventory_on_death_item_equipped = keep_inventory_on_death_item_equipped || betteradventuremode$hasEquipped(keep_inventory_on_death_item_equipped_predicate);
        damage_doubling_items_amount += betteradventuremode$getAmountEquipped(is_damage_doubling_item_equipped_predicate);

        ItemStack itemStackMainHand = this.getEquippedStack(EquipmentSlot.MAINHAND);
        ItemStack itemStackOffHand = this.getEquippedStack(EquipmentSlot.OFFHAND);
        if (!itemStackMainHand.isIn(Tags.ATTACK_ITEMS) && !this.isCreative() && !this.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT) && !BetterAdventureMode.serverConfig.allow_attacking_with_non_attack_items) {
            if (!this.hasStatusEffect(StatusEffectsRegistry.NO_ATTACK_ITEMS_EFFECT)) {
                this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.NO_ATTACK_ITEMS_EFFECT, -1, 0, false, false, false));
            }
        } else {
            this.removeStatusEffect(StatusEffectsRegistry.NO_ATTACK_ITEMS_EFFECT);
        }
        if (itemStackMainHand.isIn(Tags.TWO_HANDED_ITEMS) && (this.betteradventuremode$isMainHandStackSheathed() || !this.betteradventuremode$isOffHandStackSheathed()) && !this.isCreative() && !this.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT)) {
            if (!this.hasStatusEffect(StatusEffectsRegistry.NEEDS_TWO_HANDING_EFFECT)) {
                this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.NEEDS_TWO_HANDING_EFFECT, -1, 0, false, false, false));
            }
        } else {
            this.removeStatusEffect(StatusEffectsRegistry.NEEDS_TWO_HANDING_EFFECT);
        }
        if (mana_regenerating_item_equipped) {
            if (!this.hasStatusEffect(StatusEffectsRegistry.MANA_REGENERATION_EFFECT)) {
                this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.MANA_REGENERATION_EFFECT, -1, 0, false, false, true));
            }
        } else {
            this.removeStatusEffect(StatusEffectsRegistry.MANA_REGENERATION_EFFECT);
        }
        if (itemStackMainHand.isIn(Tags.ENABLES_CHANGING_PITCH)) {
            if (!this.hasStatusEffect(StatusEffectsRegistry.CHANGING_PITCH_ENABLED_EFFECT)) {
                this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.CHANGING_PITCH_ENABLED_EFFECT, -1, 0, false, false, false));
            }
        } else {
            this.removeStatusEffect(StatusEffectsRegistry.CHANGING_PITCH_ENABLED_EFFECT);
        }
        if ((itemStackMainHand.isIn(Tags.ENABLES_CHANGING_PITCH) || (this.isUsingItem() && this.getActiveItem().isIn(Tags.ENABLES_CHANGING_PITCH_ON_USING))) && first_person_enabling_item_equipped) {
            if (!this.hasStatusEffect(StatusEffectsRegistry.FIRST_PERSON_PERSPECTIVE_ENABLED_EFFECT)) {
                this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.FIRST_PERSON_PERSPECTIVE_ENABLED_EFFECT, -1, 0, false, false, false));
            }
        } else {
            this.removeStatusEffect(StatusEffectsRegistry.FIRST_PERSON_PERSPECTIVE_ENABLED_EFFECT);
        }
        if (fall_damage_prevention_item_equipped) {
            if (!this.hasStatusEffect(StatusEffectsRegistry.FALL_IMMUNE)) {
                this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.FALL_IMMUNE, -1, 0, false, false, true));
            }
        } else {
            this.removeStatusEffect(StatusEffectsRegistry.FALL_IMMUNE);
        }
        if (keep_inventory_on_death_item_equipped) {
            if (!this.hasStatusEffect(StatusEffectsRegistry.KEEP_INVENTORY_ON_DEATH)) {
                this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.KEEP_INVENTORY_ON_DEATH, -1, 0, false, false, false));
            }
        } else {
            this.removeStatusEffect(StatusEffectsRegistry.KEEP_INVENTORY_ON_DEATH);
        }
        if (damage_doubling_items_amount > 0) {
            StatusEffectInstance statusEffectInstance = this.getStatusEffect(StatusEffectsRegistry.CALAMITY);
            if (statusEffectInstance != null) {
                if (damage_doubling_items_amount + 1 != statusEffectInstance.getAmplifier()) {
                    this.removeStatusEffect(StatusEffectsRegistry.CALAMITY);
                    this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.CALAMITY, -1, damage_doubling_items_amount - 1, false, false, true));
                }
            } else {
                this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.CALAMITY, -1, damage_doubling_items_amount - 1, false, false, true));
            }
        } else {
            this.removeStatusEffect(StatusEffectsRegistry.CALAMITY);
        }

    }

    /**
     * @author TheRedBrain
     * @reason WIP
     */
    @Overwrite
    public void dropInventory() {
        super.dropInventory();
        if (!this.getWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
            if (this.hasStatusEffect(StatusEffectsRegistry.KEEP_INVENTORY_ON_DEATH)) {
                this.betteradventuremode$breakKeepInventoryItems();
                return;
            }
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

        if (nbt.contains("is_main_hand_stack_sheathed", NbtElement.BYTE_TYPE)) {
            this.betteradventuremode$setIsMainHandStackSheathed(nbt.getBoolean("is_main_hand_stack_sheathed"));
        }

        if (nbt.contains("is_off_hand_stack_sheathed", NbtElement.BYTE_TYPE)) {
            this.betteradventuremode$setIsOffHandStackSheathed(nbt.getBoolean("is_off_hand_stack_sheathed"));
        }

        if (nbt.contains("use_stash_for_crafting", NbtElement.BYTE_TYPE)) {
            this.betteradventuremode$setUseStashForCrafting(nbt.getBoolean("use_stash_for_crafting"));
        }

        if (nbt.contains("old_active_spell_slot_amount", NbtElement.INT_TYPE)) {
            this.betteradventuremode$setOldActiveSpellSlotAmount(nbt.getInt("old_active_spell_slot_amount"));
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void betteradventuremode$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {

        nbt.put("stash_items", this.stashInventory.toNbtList());

        nbt.putBoolean("is_main_hand_stack_sheathed", this.betteradventuremode$isMainHandStackSheathed());

        nbt.putBoolean("is_off_hand_stack_sheathed", this.betteradventuremode$isOffHandStackSheathed());

        nbt.putBoolean("use_stash_for_crafting", this.betteradventuremode$useStashForCrafting());

        nbt.putInt("old_active_spell_slot_amount", this.betteradventuremode$oldActiveSpellSlotAmount());
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
    @Inject(method = "applyDamage", at = @At("HEAD"), cancellable = true)
    public void betteradventuremode$applyDamage(DamageSource source, float amount, CallbackInfo ci) {
        if (!BetterAdventureMode.serverConfig.useVanillaDamageCalculation) {
            super.applyDamage(source, amount);
            ci.cancel();
        }
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

    // effectively disables the vanilla jump crit mechanic
    @Redirect(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;hasVehicle()Z"
            )
    )
    public boolean betteradventuremode$redirect_hasVehicle(PlayerEntity instance) {
        return BetterAdventureMode.serverConfig.disable_jump_crit_mechanic;
    }

    /**
     * @author TheRedBrain
     * @reason
     */
    @Overwrite
    public void equipStack(EquipmentSlot slot, ItemStack stack) {
        this.processEquippedStack(stack);
        if (slot == EquipmentSlot.MAINHAND) {
            this.onEquipStack(slot, ((DuckPlayerInventoryMixin)this.inventory).betteradventuremode$setMainHand(stack), stack);
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
        return ((DuckPlayerInventoryMixin)this.inventory).betteradventuremode$getArmor();
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
            this.betteradventuremode$addStamina(-1.0F);
        } else {
            if (!BetterAdventureMode.serverConfig.disable_vanilla_food_system) {
                this.addExhaustion(0.05F);
            }
            this.betteradventuremode$addStamina(-1.0F);
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

    /**
     * @author TheRedBrain
     * @reason
     */
    @Overwrite
    public void increaseTravelMotionStats(double dx, double dy, double dz) {
        if (!this.hasVehicle()) {
            int i;
            if (this.isSwimming()) {
                i = Math.round((float)Math.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F);
                if (i > 0) {
                    this.increaseStat(Stats.SWIM_ONE_CM, i);
//                    this.addExhaustion(0.01F * (float)i * 0.01F);
                    ((DuckLivingEntityMixin)this).betteradventuremode$addStamina(-0.2F);
                }
            } else if (this.isSubmergedIn(FluidTags.WATER)) {
                i = Math.round((float)Math.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F);
                if (i > 0) {
                    this.increaseStat(Stats.WALK_UNDER_WATER_ONE_CM, i);
//                    this.addExhaustion(0.01F * (float)i * 0.01F);
                    ((DuckLivingEntityMixin)this).betteradventuremode$addStamina(-0.4F);
                }
            } else if (this.isTouchingWater()) {
                i = Math.round((float)Math.sqrt(dx * dx + dz * dz) * 100.0F);
                if (i > 0) {
                    this.increaseStat(Stats.WALK_ON_WATER_ONE_CM, i);
//                    this.addExhaustion(0.01F * (float)i * 0.01F);
                    ((DuckLivingEntityMixin)this).betteradventuremode$addStamina(-0.1F);
                }
            } else if (this.isClimbing()) {
                if (dy > 0.0) {
                    this.increaseStat(Stats.CLIMB_ONE_CM, (int)Math.round(dy * 100.0));
                    ((DuckLivingEntityMixin)this).betteradventuremode$addStamina(this.hasStatusEffect(StatusEffectsRegistry.OVERBURDENED_EFFECT) ? -4 : -1);
                }
            } else if (this.isOnGround()) {
                i = Math.round((float)Math.sqrt(dx * dx + dz * dz) * 100.0F);
                if (i > 0) {
                    if (this.isSprinting()) {
                        this.increaseStat(Stats.SPRINT_ONE_CM, i);
//                        this.addExhaustion(0.1F * (float)i * 0.01F);
                        ((DuckLivingEntityMixin)this).betteradventuremode$addStamina(-0.1F);
                    } else if (this.isInSneakingPose()) {
                        this.increaseStat(Stats.CROUCH_ONE_CM, i);
//                        this.addExhaustion(0.0F * (float)i * 0.01F);
//                        ((DuckPlayerEntityMixin)this).bamcore$addStamina(-2);
                    } else {
                        this.increaseStat(Stats.WALK_ONE_CM, i);
//                        this.addExhaustion(0.0F * (float)i * 0.01F);
                    }
                }
            } else if (this.isFallFlying()) {
                i = Math.round((float)Math.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F);
                this.increaseStat(Stats.AVIATE_ONE_CM, i);
            } else {
                i = Math.round((float)Math.sqrt(dx * dx + dz * dz) * 100.0F);
                if (i > 25) {
                    this.increaseStat(Stats.FLY_ONE_CM, i);
                }
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
                if (currentEffect.getEffectType() == statusEffectInstance.getEffectType() && !statusEffectInstance.isDurationBelow(BetterAdventureMode.serverConfig.food_effect_duration_threshold_to_allow_eating)) {
                    this.sendMessage(Text.translatable("hud.message.foodEatenAlready"), true);
                    return false;
                } else if (currentEffect.getEffectType() instanceof FoodStatusEffect) {
                    currentEatenFoods++;
                }
            }
            return currentEatenFoods < this.betteradventuremode$getMaxFoodEffects();
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

    @Override
    public float betteradventuremode$getMaxFoodEffects() {
        return (float) this.getAttributeValue(EntityAttributesRegistry.MAX_FOOD_EFFECTS);
    }

    /**
     * Returns whether this player is in adventure mode.
     */
    @Override
    public abstract boolean betteradventuremode$isAdventure();

    @Override
    public ItemStack betteradventuremode$getSheathedMainHandItemStack() {
        ItemStack itemStack = ((DuckPlayerInventoryMixin)this.getInventory()).betteradventuremode$getSheathedMainHand();
        return betteradventuremode$isMainHandStackSheathed() && !itemStack.isIn(Tags.EMPTY_HAND_WEAPONS) && ItemUtils.isUsable(itemStack) ? itemStack : ItemStack.EMPTY;
    }

    @Override
    public ItemStack betteradventuremode$getSheathedOffHandItemStack() {
        ItemStack itemStack = ((DuckPlayerInventoryMixin)this.getInventory()).betteradventuremode$getSheathedOffHand();
        return betteradventuremode$isOffHandStackSheathed() && !itemStack.isIn(Tags.EMPTY_HAND_WEAPONS) && ItemUtils.isUsable(itemStack) ? itemStack : ItemStack.EMPTY;
    }

    @Override
    public boolean betteradventuremode$isMainHandStackSheathed() {
        return this.dataTracker.get(IS_MAIN_HAND_STACK_SHEATHED);
    }

    @Override
    public void betteradventuremode$setIsMainHandStackSheathed(boolean isMainHandStackSheathed) {
        this.dataTracker.set(IS_MAIN_HAND_STACK_SHEATHED, isMainHandStackSheathed);

        // TODO move
        Item mainHandStackItem = this.getInventory().getMainHandStack().getItem();
        if (mainHandStackItem instanceof IMakesEquipSound noiseMakingItem && !this.isSpectator()) {
            SoundEvent equipSound = noiseMakingItem.getEquipSound();
            if (equipSound != null) {

                if (!this.getWorld().isClient() && !this.isSilent()) {
                    this.getWorld().playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(), equipSound, this.getSoundCategory(), 1.0F, 1.0F);
                }
                this.emitGameEvent(GameEvent.EQUIP);
            }
        }
    }

    @Override
    public boolean betteradventuremode$isOffHandStackSheathed() {
        return this.dataTracker.get(IS_OFF_HAND_STACK_SHEATHED);
    }

    @Override
    public void betteradventuremode$setIsOffHandStackSheathed(boolean isOffHandStackSheathed) {
        this.dataTracker.set(IS_OFF_HAND_STACK_SHEATHED, isOffHandStackSheathed);

        // TODO move
        Item offHandStackItem = ((DuckPlayerInventoryMixin) this.getInventory()).betteradventuremode$getOffHandStack().getItem();
        if (offHandStackItem instanceof IMakesEquipSound noiseMakingItem && !this.isSpectator()) {
            SoundEvent equipSound = noiseMakingItem.getEquipSound();
            if (equipSound != null) {

                if (!this.getWorld().isClient() && !this.isSilent()) {
                    this.getWorld().playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(), equipSound, this.getSoundCategory(), 1.0F, 1.0F);
                }
                this.emitGameEvent(GameEvent.EQUIP);
            }
        }
    }

    @Override
    public boolean betteradventuremode$useStashForCrafting() {
        return this.dataTracker.get(USE_STASH_FOR_CRAFTING);
    }

    @Override
    public void betteradventuremode$setUseStashForCrafting(boolean useStashForCrafting) {
        this.dataTracker.set(USE_STASH_FOR_CRAFTING, useStashForCrafting);
    }

    @Override
    public int betteradventuremode$oldActiveSpellSlotAmount() {
        return this.dataTracker.get(OLD_ACTIVE_SPELL_SLOT_AMOUNT);
    }

    @Override
    public void betteradventuremode$setOldActiveSpellSlotAmount(int oldActiveSpellSlotAmount) {
        this.dataTracker.set(OLD_ACTIVE_SPELL_SLOT_AMOUNT, oldActiveSpellSlotAmount);
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
    private void betteradventuremode$breakKeepInventoryItems() {
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(this);
        if (trinkets.isPresent()) {
            List<net.minecraft.util.Pair<SlotReference, ItemStack>> trinketList = trinkets.get().getAllEquipped();
            for (net.minecraft.util.Pair<SlotReference, ItemStack> trinket : trinketList) {
                if (trinket.getRight().isIn(Tags.KEEPS_INVENTORY_ON_DEATH)) {
                    trinket.getLeft().inventory().clear();
                }
            }
        }
        for (int i = 0; i < this.inventory.armor.size(); i++) {
            if (this.inventory.armor.get(i).isIn(Tags.KEEPS_INVENTORY_ON_DEATH)) {
                this.inventory.armor.set(i, ItemStack.EMPTY);
            }
        }
        if (this.inventory.offHand.get(0).isIn(Tags.KEEPS_INVENTORY_ON_DEATH)) {
            this.inventory.offHand.set(0, ItemStack.EMPTY);
        }
    }

    @Unique
    private void ejectItemsFromInactiveSpellSlots() {
        int activeSpellSlotAmount = (int) this.getAttributeInstance(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT).getValue();

        if (this.betteradventuremode$oldActiveSpellSlotAmount() != activeSpellSlotAmount) {
            for (int j = activeSpellSlotAmount + 1; j < 9; j++) {
                PlayerInventory playerInventory = this.getInventory();

                if (!((DuckPlayerInventoryMixin)playerInventory).betteradventuremode$getSpellSlotStack(j).isEmpty()) {
                    playerInventory.offerOrDrop(((DuckPlayerInventoryMixin)playerInventory).betteradventuremode$setSpellSlotStack(ItemStack.EMPTY, j));
                    if (((PlayerEntity) (Object) this) instanceof ServerPlayerEntity serverPlayerEntity) {
                        serverPlayerEntity.sendMessage(Text.translatable("hud.message.spellsRemovedFromInactiveSpellSlots"), false);
                    }
                }
            }

            this.betteradventuremode$setOldActiveSpellSlotAmount(activeSpellSlotAmount);
        }
    }

    @Unique
    private void ejectNonHotbarItemsFromHotbar() { // FIXME is only called once?
        if (!this.isCreative() && !this.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT) && !((this.getServer() != null && this.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT) && !this.hasStatusEffect(StatusEffectsRegistry.WILDERNESS_EFFECT)) || this.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT))) {
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
        return this.betteradventuremode$getEquipmentWeight() / Math.max(1, this.betteradventuremode$getMaxEquipmentWeight());
    }

    @Override
    public float betteradventuremode$getRegeneratedHealth() { // TODO balance
        return (float) (
                ((this.getServer() != null && this.getServer().getGameRules().getBoolean(GameRules.NATURAL_REGENERATION)? 1.0F : 0.0F) + this.betteradventuremode$getHealthRegeneration()) * (this.getEncumbrance() > 1 ? 0.75 : 1)
        );
    }

    @Override
    public float betteradventuremode$getRegeneratedStamina() { // TODO balance
        double encumbrance = this.getEncumbrance();
        return (float) (
                this.betteradventuremode$getStaminaRegeneration() *
                (this.isSprinting() ? 0 : this.betteradventuremode$isMoving() ? 0.5 : 1) *
                (this.isBlocking() ? 0.5 : 1) *
                (encumbrance <= 0.5 ? 1 : encumbrance <= 1 ? 0.75 : 0.5)
        );
    }

    @Override
    public float betteradventuremode$getRegeneratedMana() { // TODO balance
        if (this.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || this.hasStatusEffect(StatusEffectsRegistry.MANA_REGENERATION_EFFECT)) {
            StatusEffectInstance manaRegenerationEffectInstance = this.getStatusEffect(StatusEffectsRegistry.MANA_REGENERATION_EFFECT);
            return (float) (
                    this.betteradventuremode$getManaRegeneration() *
                            (manaRegenerationEffectInstance != null ? (manaRegenerationEffectInstance.getAmplifier() > 0 ? 1 : 0.5) : 0.5)
            );
        }
        return 0.0F;
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
    public void betteradventuremode$openAreaBlockScreen(AreaBlockEntity areaBlockEntity) {
    }

    @Override
    public void betteradventuremode$openTriggeredAdvancementCheckerBlockScreen(TriggeredAdvancementCheckerBlockEntity triggeredAdvancementCheckerBlock) {
    }

    @Override
    public void betteradventuremode$openInteractiveLootBlockScreen(InteractiveLootBlockEntity interactiveLootBlockEntity) {
    }
}
