package com.github.theredbrain.betteradventuremode.mixin.entity.player;

import com.github.theredbrain.betteradventuremode.api.block.AbstractSetSpawnBlock;
import com.github.theredbrain.betteradventuremode.api.item.BasicShieldItem;
import com.github.theredbrain.betteradventuremode.api.item.BasicWeaponItem;
import com.github.theredbrain.betteradventuremode.api.effect.FoodStatusEffect;
import com.github.theredbrain.betteradventuremode.api.json_files_backend.Dialogue;
import com.github.theredbrain.betteradventuremode.block.entity.*;
import com.github.theredbrain.betteradventuremode.entity.DamageUtility;
import com.github.theredbrain.betteradventuremode.entity.DuckLivingEntityMixin;
import com.github.theredbrain.betteradventuremode.entity.player.DuckHungerManagerMixin;
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
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.world.Difficulty;
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

    @Shadow public abstract void increaseStat(Identifier stat, int amount);

    @Shadow protected abstract void dropShoulderEntities();

    @Shadow public abstract void sendMessage(Text message, boolean overlay);

    @Shadow protected HungerManager hungerManager;
    private static final TrackedData<Float> MANA = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> STAMINA = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);

    @Unique
    private boolean isAdventureHotbarCleanedUp = false;

    @Unique
    private int blockingTime = 0;

    @Unique
    private int oldActiveSpellSlotAmount = 0;

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
    private static void bamcore$createPlayerAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.setReturnValue(cir.getReturnValue()
                .add(EntityAttributesRegistry.MAX_EQUIPMENT_WEIGHT, 10.0F) // TODO balance
                .add(EntityAttributesRegistry.EQUIPMENT_WEIGHT, 0.0F)
                .add(EntityAttributesRegistry.HEALTH_REGENERATION, 0.0F) // TODO balance
                .add(EntityAttributesRegistry.MANA_REGENERATION, 0.0F) // TODO balance
                .add(EntityAttributesRegistry.STAMINA_REGENERATION, 1.0F) // TODO balance
                .add(EntityAttributesRegistry.MAX_MANA, 0.0F) // TODO balance
                .add(EntityAttributesRegistry.MAX_STAMINA, 20.0F) // TODO balance
                .add(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT, 2.0F) // TODO balance
        );
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void bamcore$initDataTracker(CallbackInfo ci) {
        this.dataTracker.startTracking(MANA, 0.0F);
        this.dataTracker.startTracking(STAMINA, 20.0F);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void bamcore$tick(CallbackInfo ci) {
        if (this.isBlocking()) {
            this.blockingTime++;
        } else if (this.blockingTime > 0) {
            this.blockingTime = 0;
        }
        if (!this.getWorld().isClient) {
            this.ejectItemsFromInactiveSpellSlots();
            this.ejectNonHotbarItemsFromHotbar();
        }
    }

    @Inject(method = "updateTurtleHelmet", at = @At("TAIL"))
    private void bamcore$updateTurtleHelmet(CallbackInfo ci) {
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

    /**
     * @author TheRedBrain
     * @reason
     */
    @Overwrite
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        if (this.abilities.invulnerable && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        }
        this.despawnCounter = 0;
        if (this.isDead()) {
            return false;
        }
        if (!this.getWorld().isClient) {
            this.dropShoulderEntities();
        }
        if (source.isScaledWithDifficulty()) {
            if (this.getWorld().getDifficulty() == Difficulty.PEACEFUL) {
                amount = 0.0f;
            }
            if (this.getWorld().getDifficulty() == Difficulty.EASY) {
                amount = Math.min(amount / 2.0f + 1.0f, amount);
            }
            if (this.getWorld().getDifficulty() == Difficulty.HARD) {
                amount = amount * 3.0f / 2.0f;
            }
        }

        // shield overhaul
        ItemStack shieldItemStack = this.getOffHandStack();
        if (this.isBlocking() && this.blockedByShield(source) && this.betteradventuremode$getStamina() > 0 && shieldItemStack.getItem() instanceof BasicShieldItem) {
            if (this.blockingTime <= 20 && this.betteradventuremode$getStamina() >= 20 && source.getAttacker() != null && source.getAttacker() instanceof LivingEntity && ((BasicShieldItem) shieldItemStack.getItem()).canParry()) {
                // try to parry the attack
                float blockedDamage = this.calculateBlockedDamage(amount, shieldItemStack, true);
                this.betteradventuremode$addStamina(-4);

                if (this.betteradventuremode$getStamina() >= 0) {

                    boolean isStaggered = false;
                    // apply stagger based on left over damage
                    if (source.isIn(Tags.STAGGERS) && !(this.betteradventuremode$getStaggerLimitMultiplier() == -1 || this.hasStatusEffect(StatusEffectsRegistry.STAGGERED))) {
                        float appliedStagger = (float) (Math.max((amount - blockedDamage), 0) * DamageUtility.getStaggerDamageMultiplierForDamageType(source));
                        this.betteradventuremode$addPoise(appliedStagger);
                        if (this.betteradventuremode$getPoise() >= this.getMaxHealth() * this.betteradventuremode$getStaggerLimitMultiplier()) {
                            this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.STAGGERED, this.betteradventuremode$getStaggerDuration(), 0, false, false, true));
                            isStaggered = true;
                        }
                    }

                    // parry was successful
                    if (!isStaggered) {
                        amount -= blockedDamage;

                        // only one attack can be parried ?
                        this.blockingTime = this.blockingTime + 20;

                        // attacker is staggered
                        int attackerStaggerDuration = ((DuckLivingEntityMixin) source.getAttacker()).betteradventuremode$getStaggerDuration();
                        if (attackerStaggerDuration != -1) {
                            ((LivingEntity) source.getAttacker()).addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.STAGGERED, attackerStaggerDuration, 0, false, true, true));
                        }

                        this.getWorld().sendEntityStatus(this, EntityStatuses.BLOCK_WITH_SHIELD);
                    } else {
                        this.getWorld().sendEntityStatus(this, EntityStatuses.BREAK_SHIELD);
                    }
                }
            } else {
                // try to block the attack
                float blockedDamage = this.calculateBlockedDamage(amount, shieldItemStack, false);
                this.betteradventuremode$addStamina(-2);

                if (this.betteradventuremode$getStamina() >= 0) {

                    boolean isStaggered = false;
                    // apply stagger based on left over damage
                    if (source.isIn(Tags.STAGGERS) && !(this.betteradventuremode$getStaggerLimitMultiplier() == -1 || this.hasStatusEffect(StatusEffectsRegistry.STAGGERED))) {
                        float appliedStagger = (float) (Math.max((amount - blockedDamage), 0) * DamageUtility.getStaggerDamageMultiplierForDamageType(source));
                        this.betteradventuremode$addPoise(appliedStagger);
                        if (this.betteradventuremode$getPoise() >= this.getMaxHealth() * this.betteradventuremode$getStaggerLimitMultiplier()) {
                            this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.STAGGERED, this.betteradventuremode$getStaggerDuration(), 0, false, false, true));
                            isStaggered = true;
                        }
                    }

                    // block was successful
                    if (!isStaggered) {
                        amount -= blockedDamage;

                        //
                        if (source.getAttacker() != null || source.getAttacker() instanceof LivingEntity) {
                            LivingEntity attacker = (LivingEntity) source.getAttacker();
                            attacker.takeKnockback(((BasicShieldItem)shieldItemStack.getItem()).getBlockForce(), attacker.getX() - this.getX(), attacker.getZ() - this.getZ());
                        }
                        if (blockedDamage > 0.0f && blockedDamage < 3.4028235E37f) {
                            ((ServerPlayerEntity) (Object) this).increaseStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(blockedDamage * 10.0f));
                        }

                        this.getWorld().sendEntityStatus(this, EntityStatuses.BLOCK_WITH_SHIELD);
                    } else {
                        this.getWorld().sendEntityStatus(this, EntityStatuses.BREAK_SHIELD);
                    }
                }
            }
        }
        if (amount == 0.0f) {
            return false;
        }
        boolean bl = super.damage(source, amount);

        // taking damage interrupts eating food, drinking potions, etc
        if (bl) {
            this.stopUsingItem();
        }
        return bl;
    }

    // TODO come up with a better formula
    private float calculateBlockedDamage(float damageAmount, ItemStack blockingItem, boolean isParry) {
        float blockedDamage;
        float blockArmor = (float) (((BasicShieldItem) blockingItem.getItem()).getBlockArmor() * (isParry ? ((BasicShieldItem) blockingItem.getItem()).getParryBonus() : 1));
        if (damageAmount <= blockArmor) {
            blockedDamage = damageAmount;
        } else {
            blockedDamage = blockArmor;
        }
        return blockedDamage;
    }

    /**
     * @author TheRedBrain
     * @reason
     */
    @Overwrite
    public void applyDamage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return;
        }
//        BetterAdventureModeCore.LOGGER.info("try apply stagger");
        // apply stagger
        if (source.isIn(Tags.STAGGERS) && !(this.betteradventuremode$getStaggerLimitMultiplier() == -1 || this.hasStatusEffect(StatusEffectsRegistry.STAGGERED))) {
            float appliedStagger = (float) (amount * DamageUtility.getStaggerDamageMultiplierForDamageType(source));
//            BetterAdventureModeCore.LOGGER.info("appliedStagger: " + appliedStagger);
            this.betteradventuremode$addPoise(appliedStagger);
            if (this.betteradventuremode$getPoise() >= this.getMaxHealth() * this.betteradventuremode$getStaggerLimitMultiplier()) {
//                BetterAdventureModeCore.LOGGER.info("appliedStagger: " + appliedStagger);
                this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.STAGGERED, this.betteradventuremode$getStaggerDuration(), 0, false, false, true));
            }
        }

        amount = this.applyArmorToDamage(source, amount);
        float f = amount = this.modifyAppliedDamage(source, amount);
        amount = Math.max(amount - this.getAbsorptionAmount(), 0.0f);
        this.setAbsorptionAmount(this.getAbsorptionAmount() - (f - amount));
        float g = f - amount;
        if (g > 0.0f && g < 3.4028235E37f) {
            this.increaseStat(Stats.DAMAGE_ABSORBED, Math.round(g * 10.0f));
        }
        if (amount == 0.0f) {
            return;
        }
        // exhaustion is not used
//        this.addExhaustion(source.getExhaustion());
        this.getDamageTracker().onDamage(source, amount);
        this.setHealth(this.getHealth() - amount);
        if (amount < 3.4028235E37f) {
            this.increaseStat(Stats.DAMAGE_TAKEN, Math.round(amount * 10.0f));
        }
        this.emitGameEvent(GameEvent.ENTITY_DAMAGE);
    }

    @Inject(method = {"attack"}, at = @At("HEAD"), cancellable = true)
    public void bamcore$attack(Entity target, CallbackInfo ci) {
        ItemStack mainHandStack = this.getMainHandStack();
        if (mainHandStack.getItem() instanceof BasicWeaponItem && this.betteradventuremode$getStamina() + ((BasicWeaponItem)mainHandStack.getItem()).getStaminaCost() <= 0) {
            this.sendMessage(Text.translatable("hud.message.staminaTooLow"), true);
            ci.cancel();
        }
    }

    // effectively disables the vanilla jump crit mechanic
    @Redirect(
            method = {"attack"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;hasVehicle()Z"
            )
    )
    public boolean bamcore$redirect_isSprinting(PlayerEntity instance) {
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
        if (this.hasStatusEffect(StatusEffectsRegistry.OVERBURDENED_EFFECT) || ((DuckPlayerEntityMixin)this).betteradventuremode$getStamina() <= 0) {
            return;
        }
        super.jump();
        this.incrementStat(Stats.JUMP);
        if (this.isSprinting()) {
//            this.addExhaustion(0.2F);
            ((DuckPlayerEntityMixin)this).betteradventuremode$addStamina(-2);
        } else {
//            this.addExhaustion(0.05F);
            ((DuckPlayerEntityMixin)this).betteradventuremode$addStamina(-1);
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
                this.setSwimming(/*this.isSprinting()*/!this.hasStatusEffect(StatusEffectsRegistry.OVERBURDENED_EFFECT) && this.isTouchingWater() && !this.hasVehicle());
            } else {
                this.setSwimming(/*this.isSprinting()*/!this.hasStatusEffect(StatusEffectsRegistry.OVERBURDENED_EFFECT) && this.isSubmergedInWater() && !this.hasVehicle() && this.getWorld().getFluidState(this.getBlockPos()).isIn(FluidTags.WATER));
            }
        }

    }


//    /**
//     * @author TheRedBrain
//     * @reason
//     */
//    @Overwrite
//    public void increaseTravelMotionStats(double dx, double dy, double dz) {
//        if (!this.hasVehicle()) {
//            int i;
//            if (this.isSwimming()) {
//                i = Math.round((float)Math.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F);
//                if (i > 0) {
//                    this.increaseStat(Stats.SWIM_ONE_CM, i);
////                    this.addExhaustion(0.01F * (float)i * 0.01F);
//                    ((DuckPlayerEntityMixin)this).bamcore$addStamina(-0.2F);
//                }
//            } else if (this.isSubmergedIn(FluidTags.WATER)) {
//                i = Math.round((float)Math.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F);
//                if (i > 0) {
//                    this.increaseStat(Stats.WALK_UNDER_WATER_ONE_CM, i);
////                    this.addExhaustion(0.01F * (float)i * 0.01F);
//                    ((DuckPlayerEntityMixin)this).bamcore$addStamina(-0.4F);
//                }
//            } else if (this.isTouchingWater()) {
//                i = Math.round((float)Math.sqrt(dx * dx + dz * dz) * 100.0F);
//                if (i > 0) {
//                    this.increaseStat(Stats.WALK_ON_WATER_ONE_CM, i);
////                    this.addExhaustion(0.01F * (float)i * 0.01F);
//                    ((DuckPlayerEntityMixin)this).bamcore$addStamina(-0.1F);
//                }
//            } else if (this.isClimbing()) {
//                if (dy > 0.0) {
//                    this.increaseStat(Stats.CLIMB_ONE_CM, (int)Math.round(dy * 100.0));
//                    ((DuckPlayerEntityMixin)this).bamcore$addStamina(this.hasStatusEffect(StatusEffectsRegistry.OVERBURDENED_EFFECT) ? -4 : -1);
//                }
//            } else if (this.isOnGround()) {
//                i = Math.round((float)Math.sqrt(dx * dx + dz * dz) * 100.0F);
//                if (i > 0) {
//                    if (this.isSprinting()) {
//                        this.increaseStat(Stats.SPRINT_ONE_CM, i);
////                        this.addExhaustion(0.1F * (float)i * 0.01F);
//                        ((DuckPlayerEntityMixin)this).bamcore$addStamina(-0.1F);
//                    } else if (this.isInSneakingPose()) {
//                        this.increaseStat(Stats.CROUCH_ONE_CM, i);
////                        this.addExhaustion(0.0F * (float)i * 0.01F);
////                        ((DuckPlayerEntityMixin)this).bamcore$addStamina(-2);
//                    } else {
//                        this.increaseStat(Stats.WALK_ONE_CM, i);
////                        this.addExhaustion(0.0F * (float)i * 0.01F);
//                    }
//                }
//            } else if (this.isFallFlying()) {
//                i = Math.round((float)Math.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F);
//                this.increaseStat(Stats.AVIATE_ONE_CM, i);
//            } else {
//                i = Math.round((float)Math.sqrt(dx * dx + dz * dz) * 100.0F);
//                if (i > 25) {
//                    this.increaseStat(Stats.FLY_ONE_CM, i);
//                }
//            }
//
//        }
//    }

    @Override
    public void heal(float amount) {
        float f = this.getHealth();
        if (f > 0.0f) {
            this.setHealth(f + amount);
        }
        if (amount < 0) {
            ((DuckHungerManagerMixin)this.hungerManager).betteradventuremode$setHealthTickTimer(0);
        }
    }

    // custom check for adventure food
    @Unique
    public boolean betteradventuremode$canConsumeItem(ItemStack itemStack) {
        List<Pair<StatusEffectInstance, Float>> list = itemStack.getItem().getFoodComponent().getStatusEffects();
        if (itemStack.isFood() && list != null) {
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

    @Override
    public float betteradventuremode$getHealthRegeneration() {
        return (float) this.getAttributeValue(EntityAttributesRegistry.HEALTH_REGENERATION);
    }

    @Override
    public float betteradventuremode$getManaRegeneration() {
        return (float) this.getAttributeValue(EntityAttributesRegistry.MANA_REGENERATION);
    }

    @Override
    public float betteradventuremode$getMaxMana() {
        return (float) this.getAttributeValue(EntityAttributesRegistry.MAX_MANA);
    }

    @Override
    public void betteradventuremode$addMana(float amount) {
        float f = this.betteradventuremode$getMana();
        this.betteradventuremode$setMana(f + amount);
        if (amount < 0) {
            ((DuckHungerManagerMixin)this.hungerManager).betteradventuremode$setManaTickTimer(0);
        }
    }

    @Override
    public float betteradventuremode$getMana() {
        return this.dataTracker.get(MANA);
    }

    @Override
    public void betteradventuremode$setMana(float mana) {
        this.dataTracker.set(MANA, MathHelper.clamp(mana, 0, this.betteradventuremode$getMaxMana()));
    }

    @Override
    public float betteradventuremode$getStaminaRegeneration() {
        return (float) this.getAttributeValue(EntityAttributesRegistry.STAMINA_REGENERATION);
    }

    @Override
    public float betteradventuremode$getMaxStamina() {
        return (float) this.getAttributeValue(EntityAttributesRegistry.MAX_STAMINA);
    }

    @Override
    public void betteradventuremode$addStamina(float amount) {
        float f = this.betteradventuremode$getStamina();
        this.betteradventuremode$setStamina(f + amount);
        if (amount < 0) {
            ((DuckHungerManagerMixin)this.hungerManager).betteradventuremode$setStaminaTickTimer(0);
        }
    }

    @Override
    public float betteradventuremode$getStamina() {
        return this.dataTracker.get(STAMINA);
    }

    @Override
    public void betteradventuremode$setStamina(float stamina) {
        this.dataTracker.set(STAMINA, MathHelper.clamp(stamina, -100/*TODO balance min stamina*/, this.betteradventuremode$getMaxStamina()));
    }

    /**
     * Returns whether this player is in adventure mode.
     */
    @Override
    public abstract boolean betteradventuremode$isAdventure();

    @Unique
    private void ejectItemsFromInactiveSpellSlots() {
        int activeSpellSlotAmount = (int) this.getAttributeInstance(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT).getValue();

        if (this.oldActiveSpellSlotAmount != activeSpellSlotAmount) {
            for (int j = activeSpellSlotAmount; j < 8; j++) {
                PlayerInventory playerInventory = this.getInventory();

                if (!((DuckPlayerInventoryMixin)playerInventory).betteradventuremode$getSpellSlotStack(j).isEmpty()) {
                    playerInventory.offerOrDrop(((DuckPlayerInventoryMixin)playerInventory).betteradventuremode$setSpellSlotStack(ItemStack.EMPTY, j));
                    if (((PlayerEntity) (Object) this) instanceof ServerPlayerEntity serverPlayerEntity) {
                        serverPlayerEntity.sendMessageToClient(Text.translatable("hud.message.spellsRemovedFromInactiveSpellSlots"), true);
                    }
                }
            }

            this.oldActiveSpellSlotAmount = activeSpellSlotAmount;
        }
    }

    @Unique
    private void ejectNonHotbarItemsFromHotbar() {
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
    @Override
    public int betteradventuremode$getStaggerDuration() {
        return 24;
    }

    @Override
    public double betteradventuremode$getStaggerLimitMultiplier() {
        return 0.4;
    }

    @Override
    public void betteradventuremode$openHousingScreen() {
    }

    @Override
    public void betteradventuremode$openJigsawPlacerBlockScreen(JigsawPlacerBlockBlockEntity jigsawPlacerBlock) {
    }

    @Override
    public void betteradventuremode$openRedstoneTriggerBlockScreen(RedstoneTriggerBlockBlockEntity redstoneTriggerBlock) {
    }

    @Override
    public void betteradventuremode$openRelayTriggerBlockScreen(RelayTriggerBlockBlockEntity relayTriggerBlock) {
    }

    @Override
    public void betteradventuremode$openTriggeredCounterBlockScreen(TriggeredCounterBlockEntity triggeredCounterBlock) {
    }

    @Override
    public void betteradventuremode$openResetTriggerBlockScreen(ResetTriggerBlockEntity resetTriggerBlock) {
    }

    @Override
    public void betteradventuremode$openDelayTriggerBlockScreen(DelayTriggerBlockBlockEntity delayTriggerBlock) {
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
}
