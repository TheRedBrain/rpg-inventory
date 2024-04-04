package com.github.theredbrain.betteradventuremode.mixin.entity;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.item.BasicShieldItem;
import com.github.theredbrain.betteradventuremode.registry.EntityAttributesRegistry;
import com.github.theredbrain.betteradventuremode.registry.StatusEffectsRegistry;
import com.github.theredbrain.betteradventuremode.entity.DuckLivingEntityMixin;
import com.github.theredbrain.betteradventuremode.registry.GameRulesRegistry;
import com.github.theredbrain.betteradventuremode.registry.Tags;
import dev.emi.trinkets.api.*;
import dev.emi.trinkets.api.event.TrinketDropCallback;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(value = LivingEntity.class, priority = 950)
public abstract class LivingEntityMixin extends Entity implements DuckLivingEntityMixin {

    @Shadow
    public abstract float getMaxHealth();

    @Shadow
    public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow
    public abstract boolean addStatusEffect(StatusEffectInstance effect);

    @Shadow
    public abstract DamageTracker getDamageTracker();

    @Shadow
    public abstract void setHealth(float health);

    @Shadow
    public abstract float getHealth();

    @Shadow
    public abstract @Nullable StatusEffectInstance getStatusEffect(StatusEffect effect);

    @Shadow
    public abstract void damageArmor(DamageSource source, float amount);

    @Shadow
    public abstract int getArmor();

    @Shadow
    public abstract double getAttributeValue(EntityAttribute attribute);

    @Shadow
    public abstract ItemStack getOffHandStack();

    @Shadow
    public abstract void stopUsingItem();

    @Shadow
    public abstract boolean removeStatusEffect(StatusEffect type);

    @Shadow
    public abstract boolean addStatusEffect(StatusEffectInstance effect, @Nullable Entity source);

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow public abstract boolean isUsingItem();

    @Shadow protected ItemStack activeItemStack;

    @Shadow protected abstract float applyArmorToDamage(DamageSource source, float amount);

    @Shadow public abstract float getAbsorptionAmount();

    @Shadow public abstract void setAbsorptionAmount(float amount);

    @Shadow public abstract boolean isDead();

    @Shadow public abstract boolean isSleeping();

    @Shadow public abstract void wakeUp();

    @Shadow protected int despawnCounter;
    @Shadow @Final public LimbAnimator limbAnimator;

    @Shadow public abstract void setAttacker(@Nullable LivingEntity attacker);

    @Shadow protected float lastDamageTaken;
    @Shadow public int hurtTime;
    @Shadow public int maxHurtTime;

    @Shadow public abstract void damageHelmet(DamageSource source, float amount);

    @Shadow @Nullable protected PlayerEntity attackingPlayer;
    @Shadow protected int playerHitTimer;

    @Shadow public abstract void takeKnockback(double strength, double x, double z);

    @Shadow public abstract void tiltScreen(double deltaX, double deltaZ);

    @Shadow protected abstract boolean tryUseTotem(DamageSource source);

    @Shadow protected abstract @Nullable SoundEvent getDeathSound();

    @Shadow public abstract void onDeath(DamageSource damageSource);

    @Shadow protected abstract float getSoundVolume();

    @Shadow public abstract float getSoundPitch();

    @Shadow protected abstract void playHurtSound(DamageSource source);

    @Shadow private @Nullable DamageSource lastDamageSource;
    @Shadow private long lastDamageTime;

    @Shadow public abstract boolean blockedByShield(DamageSource source);

    @Shadow protected abstract void takeShieldHit(LivingEntity attacker);

    @Shadow public abstract void damageShield(float amount);

    @Shadow public abstract void heal(float amount);

    @Shadow protected abstract float modifyAppliedDamage(DamageSource source, float amount);

    @Unique
    private int healthTickTimer = 0;
    @Unique
    private int staminaTickTimer = 0;
    @Unique
    private int manaTickTimer = 0;
    @Unique
    private int bleedingTickTimer = 0;
    @Unique
    private int burnTickTimer = 0;
    @Unique
    private int freezeTickTimer = 0;
    @Unique
    private int staggerTickTimer = 0;
    @Unique
    private int poisonTickTimer = 0;
    @Unique
    private int shockTickTimer = 0;
    @Unique
    private int healthRegenerationDelayTimer = 0;
    @Unique
    private int staminaRegenerationDelayTimer = 0;
    @Unique
    private boolean delayStaminaRegeneration = false;
    @Unique
    private int manaRegenerationDelayTimer = 0;
    @Unique
    private int blockingTime = 0;
    @Unique
    private double oldPosX = 0.0;
    @Unique
    private double oldPosY = 0.0;
    @Unique
    private double oldPosZ = 0.0;
    @Unique
    private boolean isMoving = false;

    @Unique
    private static final TrackedData<Float> BLEEDING_BUILD_UP = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.FLOAT);

    @Unique
    private static final TrackedData<Float> BURN_BUILD_UP = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.FLOAT);

    @Unique
    private static final TrackedData<Float> FREEZE_BUILD_UP = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.FLOAT);

    @Unique
    private static final TrackedData<Float> MANA = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.FLOAT);

    @Unique
    private static final TrackedData<Float> STAGGER_BUILD_UP = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.FLOAT);

    @Unique
    private static final TrackedData<Float> POISON_BUILD_UP = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.FLOAT);

    @Unique
    private static final TrackedData<Float> SHOCK_BUILD_UP = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.FLOAT);

    @Unique
    private static final TrackedData<Float> STAMINA = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.FLOAT);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "initDataTracker", at = @At("RETURN"))
    protected void betteradventuremode$initDataTracker(CallbackInfo ci) {
        this.dataTracker.startTracking(BLEEDING_BUILD_UP, 0.0F);
        this.dataTracker.startTracking(BURN_BUILD_UP, 0.0F);
        this.dataTracker.startTracking(FREEZE_BUILD_UP, 0.0F);
        this.dataTracker.startTracking(MANA, 0.0F);
        this.dataTracker.startTracking(POISON_BUILD_UP, 0.0F);
        this.dataTracker.startTracking(STAGGER_BUILD_UP, 0.0F);
        this.dataTracker.startTracking(STAMINA, 20.0F);
        this.dataTracker.startTracking(SHOCK_BUILD_UP, 0.0F);

    }

    @Inject(method = "createLivingAttributes", at = @At("RETURN"))
    private static void betteradventuremode$createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.getReturnValue()
                .add(EntityAttributesRegistry.HEALTH_REGENERATION)
                .add(EntityAttributesRegistry.MANA_REGENERATION)
                .add(EntityAttributesRegistry.STAMINA_REGENERATION)
                .add(EntityAttributesRegistry.MAX_MANA)
                .add(EntityAttributesRegistry.MAX_STAMINA)

                .add(EntityAttributesRegistry.HEALTH_REGENERATION_DELAY_THRESHOLD)
                .add(EntityAttributesRegistry.STAMINA_REGENERATION_DELAY_THRESHOLD)
                .add(EntityAttributesRegistry.MANA_REGENERATION_DELAY_THRESHOLD)

                .add(EntityAttributesRegistry.HEALTH_TICK_THRESHOLD)
                .add(EntityAttributesRegistry.STAMINA_TICK_THRESHOLD)
                .add(EntityAttributesRegistry.MANA_TICK_THRESHOLD)

                .add(EntityAttributesRegistry.ADDITIONAL_BASHING_DAMAGE)
                .add(EntityAttributesRegistry.INCREASED_BASHING_DAMAGE)

                .add(EntityAttributesRegistry.ADDITIONAL_PIERCING_DAMAGE)
                .add(EntityAttributesRegistry.INCREASED_PIERCING_DAMAGE)
                
                .add(EntityAttributesRegistry.ADDITIONAL_SLASHING_DAMAGE)
                .add(EntityAttributesRegistry.INCREASED_SLASHING_DAMAGE)
                
                .add(EntityAttributesRegistry.MAX_BLEEDING_BUILD_UP)
                .add(EntityAttributesRegistry.BLEEDING_DURATION)
                .add(EntityAttributesRegistry.BLEEDING_TICK_THRESHOLD)
                .add(EntityAttributesRegistry.BLEEDING_BUILD_UP_REDUCTION)

                .add(EntityAttributesRegistry.ADDITIONAL_FROST_DAMAGE)
                .add(EntityAttributesRegistry.INCREASED_FROST_DAMAGE)
                .add(EntityAttributesRegistry.FROST_RESISTANCE)
                .add(EntityAttributesRegistry.MAX_FREEZE_BUILD_UP)
                .add(EntityAttributesRegistry.FREEZE_DURATION)
                .add(EntityAttributesRegistry.FREEZE_TICK_THRESHOLD)
                .add(EntityAttributesRegistry.FREEZE_BUILD_UP_REDUCTION)

                .add(EntityAttributesRegistry.ADDITIONAL_FIRE_DAMAGE)
                .add(EntityAttributesRegistry.INCREASED_FIRE_DAMAGE)
                .add(EntityAttributesRegistry.FIRE_RESISTANCE)
                .add(EntityAttributesRegistry.MAX_BURN_BUILD_UP)
                .add(EntityAttributesRegistry.BURN_DURATION)
                .add(EntityAttributesRegistry.BURN_TICK_THRESHOLD)
                .add(EntityAttributesRegistry.BURN_BUILD_UP_REDUCTION)
                
                .add(EntityAttributesRegistry.ADDITIONAL_LIGHTNING_DAMAGE)
                .add(EntityAttributesRegistry.INCREASED_LIGHTNING_DAMAGE)
                .add(EntityAttributesRegistry.LIGHTNING_RESISTANCE)
                .add(EntityAttributesRegistry.MAX_SHOCK_BUILD_UP)
                .add(EntityAttributesRegistry.SHOCK_DURATION)
                .add(EntityAttributesRegistry.SHOCK_TICK_THRESHOLD)
                .add(EntityAttributesRegistry.SHOCK_BUILD_UP_REDUCTION)
                
                .add(EntityAttributesRegistry.ADDITIONAL_POISON_DAMAGE)
                .add(EntityAttributesRegistry.INCREASED_POISON_DAMAGE)
                .add(EntityAttributesRegistry.POISON_RESISTANCE)
                .add(EntityAttributesRegistry.MAX_POISON_BUILD_UP)
                .add(EntityAttributesRegistry.POISON_DURATION)
                .add(EntityAttributesRegistry.POISON_TICK_THRESHOLD)
                .add(EntityAttributesRegistry.POISON_BUILD_UP_REDUCTION)

                .add(EntityAttributesRegistry.MAX_STAGGER_BUILD_UP)
                .add(EntityAttributesRegistry.STAGGER_DURATION)
                .add(EntityAttributesRegistry.STAGGER_TICK_THRESHOLD)
                .add(EntityAttributesRegistry.STAGGER_BUILD_UP_REDUCTION)
        ;
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void betteradventuremode$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {

        if (nbt.contains("bleeding_build_up", NbtElement.NUMBER_TYPE)) {
            this.betteradventuremode$setBleedingBuildUp(nbt.getFloat("bleeding_build_up"));
        }

        if (nbt.contains("burn_build_up", NbtElement.NUMBER_TYPE)) {
            this.betteradventuremode$setBurnBuildUp(nbt.getFloat("burn_build_up"));
        }

        if (nbt.contains("freeze_build_up", NbtElement.NUMBER_TYPE)) {
            this.betteradventuremode$setFreezeBuildUp(nbt.getFloat("freeze_build_up"));
        }

        if (nbt.contains("mana", NbtElement.NUMBER_TYPE)) {
            this.betteradventuremode$setMana(nbt.getFloat("mana"));
        }

        if (nbt.contains("poison_build_up", NbtElement.NUMBER_TYPE)) {
            this.betteradventuremode$setPoisonBuildUp(nbt.getFloat("poison_build_up"));
        }

        if (nbt.contains("stagger_build_up", NbtElement.NUMBER_TYPE)) {
            this.betteradventuremode$setStaggerBuildUp(nbt.getFloat("stagger_build_up"));
        }

        if (nbt.contains("stamina", NbtElement.NUMBER_TYPE)) {
            this.betteradventuremode$setStamina(nbt.getFloat("stamina"));
        }

        if (nbt.contains("shock_build_up", NbtElement.NUMBER_TYPE)) {
            this.betteradventuremode$setShockBuildUp(nbt.getFloat("shock_build_up"));
        }

    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void betteradventuremode$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {

        nbt.putFloat("bleeding_build_up", this.betteradventuremode$getBleedingBuildUp());

        nbt.putFloat("burn_build_up", this.betteradventuremode$getBurnBuildUp());

        nbt.putFloat("freeze_build_up", this.betteradventuremode$getFreezeBuildUp());

        nbt.putFloat("mana", this.betteradventuremode$getMana());

        nbt.putFloat("poison_build_up", this.betteradventuremode$getPoisonBuildUp());

        nbt.putFloat("stagger_build_up", this.betteradventuremode$getStaggerBuildUp());

        nbt.putFloat("stamina", this.betteradventuremode$getStamina());

        nbt.putFloat("shock_build_up", this.betteradventuremode$getShockBuildUp());

    }

    @Inject(method = "heal", at = @At("RETURN"))
    public void betteradventuremode$heal(float amount, CallbackInfo ci) {
        if (amount < 0) {
            this.healthRegenerationDelayTimer = 0;
            this.healthTickTimer = 0;
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
        } else if (this.getWorld().isClient) {
            return false;
        } else if (this.isDead()) {
            return false;
        }

        if (BetterAdventureMode.serverConfig.useVanillaDamageCalculation) {
            if (source.isIn(DamageTypeTags.IS_FIRE) && this.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
                return false;
            } else {
                if (this.isSleeping() && !this.getWorld().isClient) {
                    this.wakeUp();
                }

                this.despawnCounter = 0;
                float f = amount;
                boolean bl = false;
                float g = 0.0F;
                if (amount > 0.0F && this.blockedByShield(source)) {
                    this.damageShield(amount);
                    g = amount;
                    amount = 0.0F;
                    if (!source.isIn(DamageTypeTags.IS_PROJECTILE)) {
                        Entity entity = source.getSource();
                        if (entity instanceof LivingEntity) {
                            LivingEntity livingEntity = (LivingEntity) entity;
                            this.takeShieldHit(livingEntity);
                        }
                    }

                    bl = true;
                }

                if (source.isIn(DamageTypeTags.IS_FREEZING) && this.getType().isIn(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
                    amount *= 5.0F;
                }

                this.limbAnimator.setSpeed(1.5F);
                boolean bl2 = true;
                if ((float) this.timeUntilRegen > 10.0F && !source.isIn(DamageTypeTags.BYPASSES_COOLDOWN)) {
                    if (amount <= this.lastDamageTaken) {
                        return false;
                    }

                    this.applyDamage(source, amount - this.lastDamageTaken);
                    this.lastDamageTaken = amount;
                    bl2 = false;
                } else {
                    this.lastDamageTaken = amount;
                    this.timeUntilRegen = 20;
                    this.applyDamage(source, amount);
                    this.maxHurtTime = 10;
                    this.hurtTime = this.maxHurtTime;
                }

                if (source.isIn(DamageTypeTags.DAMAGES_HELMET) && !this.getEquippedStack(EquipmentSlot.HEAD).isEmpty()) {
                    this.damageHelmet(source, amount);
                    amount *= 0.75F;
                }

                Entity entity2 = source.getAttacker();
                if (entity2 != null) {
                    if (entity2 instanceof LivingEntity) {
                        LivingEntity livingEntity2 = (LivingEntity) entity2;
                        if (!source.isIn(DamageTypeTags.NO_ANGER)) {
                            this.setAttacker(livingEntity2);
                        }
                    }

                    if (entity2 instanceof PlayerEntity) {
                        PlayerEntity playerEntity = (PlayerEntity) entity2;
                        this.playerHitTimer = 100;
                        this.attackingPlayer = playerEntity;
                    } else if (entity2 instanceof WolfEntity) {
                        WolfEntity wolfEntity = (WolfEntity) entity2;
                        if (wolfEntity.isTamed()) {
                            this.playerHitTimer = 100;
                            LivingEntity var11 = wolfEntity.getOwner();
                            if (var11 instanceof PlayerEntity) {
                                PlayerEntity playerEntity2 = (PlayerEntity) var11;
                                this.attackingPlayer = playerEntity2;
                            } else {
                                this.attackingPlayer = null;
                            }
                        }
                    }
                }

                if (bl2) {
                    if (bl) {
                        this.getWorld().sendEntityStatus(this, EntityStatuses.BLOCK_WITH_SHIELD);
                    } else {
                        this.getWorld().sendEntityDamage(this, source);
                    }

                    if (!source.isIn(DamageTypeTags.NO_IMPACT) && (!bl || amount > 0.0F)) {
                        this.scheduleVelocityUpdate();
                    }

                    if (entity2 != null && !source.isIn(DamageTypeTags.IS_EXPLOSION)) {
                        double d = entity2.getX() - this.getX();

                        double e;
                        for (e = entity2.getZ() - this.getZ(); d * d + e * e < 1.0E-4; e = (Math.random() - Math.random()) * 0.01) {
                            d = (Math.random() - Math.random()) * 0.01;
                        }

                        this.takeKnockback(0.4000000059604645, d, e);
                        if (!bl) {
                            this.tiltScreen(d, e);
                        }
                    }
                }

                if (this.isDead()) {
                    if (!this.tryUseTotem(source)) {
                        SoundEvent soundEvent = this.getDeathSound();
                        if (bl2 && soundEvent != null) {
                            this.playSound(soundEvent, this.getSoundVolume(), this.getSoundPitch());
                        }

                        this.onDeath(source);
                    }
                } else if (bl2) {
                    this.playHurtSound(source);
                }

                boolean bl3 = !bl || amount > 0.0F;
                if (bl3) {
                    this.lastDamageSource = source;
                    this.lastDamageTime = this.getWorld().getTime();
                }

                if (((LivingEntity) (Object) this) instanceof ServerPlayerEntity) {
                    Criteria.ENTITY_HURT_PLAYER.trigger((ServerPlayerEntity) (Object) this, source, f, amount, bl);
                    if (g > 0.0F && g < 3.4028235E37F) {
                        ((ServerPlayerEntity) (Object) this).increaseStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(g * 10.0F));
                    }
                }

                if (entity2 instanceof ServerPlayerEntity) {
                    Criteria.PLAYER_HURT_ENTITY.trigger((ServerPlayerEntity) entity2, this, source, f, amount, bl);
                }

                return bl3;
            }
        } else {
//            if (source.isIn(DamageTypeTags.IS_FIRE) && this.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
//                return false;
//            } else {
                if (this.isSleeping() && !this.getWorld().isClient) {
                    this.wakeUp();
                }

                this.despawnCounter = 0;
                float f = amount;
                boolean bl = false;
                float g = 0.0F;
//                if (amount > 0.0F && this.blockedByShield(source)) {
//                    this.damageShield(amount);
//                    g = amount;
//                    amount = 0.0F;
//                    if (!source.isIn(DamageTypeTags.IS_PROJECTILE)) {
//                        Entity entity = source.getSource();
//                        if (entity instanceof LivingEntity) {
//                            LivingEntity livingEntity = (LivingEntity) entity;
//                            this.takeShieldHit(livingEntity);
//                        }
//                    }
//
//                    bl = true;
//                }

//                if (source.isIn(DamageTypeTags.IS_FREEZING) && this.getType().isIn(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
//                    amount *= 5.0F;
//                }

                this.limbAnimator.setSpeed(1.5F);
                boolean bl2 = true;
                if ((float) this.timeUntilRegen > 10.0F && !source.isIn(DamageTypeTags.BYPASSES_COOLDOWN)) {
                    if (amount <= this.lastDamageTaken) {
                        return false;
                    }

                    this.applyDamage(source, amount - this.lastDamageTaken);
                    this.lastDamageTaken = amount;
                    bl2 = false;
                } else {
                    this.lastDamageTaken = amount;
                    this.timeUntilRegen = 20;
                    this.applyDamage(source, amount);
                    this.maxHurtTime = 10;
                    this.hurtTime = this.maxHurtTime;
                }

//                if (source.isIn(DamageTypeTags.DAMAGES_HELMET) && !this.getEquippedStack(EquipmentSlot.HEAD).isEmpty()) {
//                    this.damageHelmet(source, amount);
//                    amount *= 0.75F;
//                }

                Entity entity2 = source.getAttacker();
                if (entity2 != null) {
                    if (entity2 instanceof LivingEntity livingEntity2) {
                        if (!source.isIn(DamageTypeTags.NO_ANGER)) {
                            this.setAttacker(livingEntity2);
                        }
                    }

                    if (entity2 instanceof PlayerEntity playerEntity) {
                        this.playerHitTimer = 100;
                        this.attackingPlayer = playerEntity;
                    } else if (entity2 instanceof WolfEntity wolfEntity) {
                        if (wolfEntity.isTamed()) {
                            this.playerHitTimer = 100;
                            LivingEntity var11 = wolfEntity.getOwner();
                            if (var11 instanceof PlayerEntity playerEntity2) {
                                this.attackingPlayer = playerEntity2;
                            } else {
                                this.attackingPlayer = null;
                            }
                        }
                    }
                }

                if (bl2) {
//                    if (bl) {
//                        this.getWorld().sendEntityStatus(this, EntityStatuses.BLOCK_WITH_SHIELD);
//                    } else {
                        this.getWorld().sendEntityDamage(this, source);
//                    }

                    if (!source.isIn(DamageTypeTags.NO_IMPACT) && /*(!bl || */amount > 0.0F)/*)*/ {
                        this.scheduleVelocityUpdate();
                    }

                    if (entity2 != null && !source.isIn(DamageTypeTags.IS_EXPLOSION)) {
                        double d = entity2.getX() - this.getX();

                        double e;
                        for (e = entity2.getZ() - this.getZ(); d * d + e * e < 1.0E-4; e = (Math.random() - Math.random()) * 0.01) {
                            d = (Math.random() - Math.random()) * 0.01;
                        }

                        this.takeKnockback(0.4000000059604645, d, e);
                        if (!bl) {
                            this.tiltScreen(d, e);
                        }
                    }
                }

                if (this.isDead()) {
                    if (!this.tryUseTotem(source)) {
                        SoundEvent soundEvent = this.getDeathSound();
                        if (bl2 && soundEvent != null) {
                            this.playSound(soundEvent, this.getSoundVolume(), this.getSoundPitch());
                        }

                        this.onDeath(source);
                    }
                } else if (bl2) {
                    this.playHurtSound(source);
                }

                boolean bl3 = /*!bl || */amount > 0.0F;
                if (bl3) {
                    this.lastDamageSource = source;
                    this.lastDamageTime = this.getWorld().getTime();
                }

                if (((LivingEntity) (Object) this) instanceof ServerPlayerEntity) {
                    Criteria.ENTITY_HURT_PLAYER.trigger((ServerPlayerEntity) (Object) this, source, f, amount, bl);
                    if (g > 0.0F && g < 3.4028235E37F) {
                        ((ServerPlayerEntity) (Object) this).increaseStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(g * 10.0F));
                    }
                }

                if (entity2 instanceof ServerPlayerEntity) {
                    Criteria.PLAYER_HURT_ENTITY.trigger((ServerPlayerEntity) entity2, this, source, f, amount, bl);
                }

                return bl3;
//            }
        }
    }

    /**
     * @author TheRedBrain
     * @reason inject gamerule destroyDroppedItemsOnDeath into Trinkets drop logic
     */
    @Overwrite
    public void dropInventory() {
        LivingEntity entity = (LivingEntity) (Object) this;

        boolean keepInv = entity.getWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY);

        boolean destroyDroppedItems;
        if (entity.getServer() != null && entity instanceof PlayerEntity) {
            destroyDroppedItems = entity.getServer().getGameRules().getBoolean(GameRulesRegistry.DESTROY_DROPPED_ITEMS_ON_DEATH);
        } else {
            destroyDroppedItems = false;
        }
        TrinketsApi.getTrinketComponent(entity).ifPresent(trinkets -> trinkets.forEach((ref, stack) -> {
            if (stack.isEmpty()) {
                return;
            }

            TrinketEnums.DropRule dropRule = TrinketsApi.getTrinket(stack.getItem()).getDropRule(stack, ref, entity);

            dropRule = TrinketDropCallback.EVENT.invoker().drop(dropRule, stack, ref, entity);

            TrinketInventory inventory = ref.inventory();

            if (dropRule == TrinketEnums.DropRule.DEFAULT) {
                dropRule = inventory.getSlotType().getDropRule();
            }

            if (dropRule == TrinketEnums.DropRule.DEFAULT) {
                if (keepInv && entity.getType() == EntityType.PLAYER) {
                    dropRule = TrinketEnums.DropRule.KEEP;
                } else {
                    if (EnchantmentHelper.hasVanishingCurse(stack) || destroyDroppedItems) {
                        dropRule = TrinketEnums.DropRule.DESTROY;
                    } else {
                        dropRule = TrinketEnums.DropRule.DROP;
                    }
                }
            }

            switch (dropRule) {
                case DROP:
                    betteradventuremode$dropFromEntity(stack);
                    // Fallthrough
                case DESTROY:
                    inventory.setStack(ref.index(), ItemStack.EMPTY);
                    break;
                default:
                    break;
            }
        }));
    }

    /**
     * @author TheRedBrain
     * @reason complete overhaul of the damage calculation
     */
    @Overwrite
    public void applyDamage(DamageSource source, float amount) {
        if (BetterAdventureMode.serverConfig.useVanillaDamageCalculation) {
            if (!this.isInvulnerableTo(source)) {
                amount = this.applyArmorToDamage(source, amount);
                amount = this.modifyAppliedDamage(source, amount);
                float f = amount;
                amount = Math.max(amount - this.getAbsorptionAmount(), 0.0F);
                this.setAbsorptionAmount(this.getAbsorptionAmount() - (f - amount));
                float g = f - amount;
                if (g > 0.0F && g < 3.4028235E37F) {
                    Entity var6 = source.getAttacker();
                    if (var6 instanceof ServerPlayerEntity serverPlayerEntity) {
                        serverPlayerEntity.increaseStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(g * 10.0F));
                    }
                }

                if (amount != 0.0F) {
                    this.getDamageTracker().onDamage(source, amount);
                    this.setHealth(this.getHealth() - amount);
                    this.setAbsorptionAmount(this.getAbsorptionAmount() - amount);
                    this.emitGameEvent(GameEvent.ENTITY_DAMAGE);
                }
            }
        } else {
            // TODO account for resistanceEffect
            LivingEntity attacker = null;
            if (source.getAttacker() instanceof LivingEntity) {
                attacker = (LivingEntity) source.getAttacker();
            }
            if (this.isInvulnerableTo(source)) {
                return;
            }

            if (source.isIn(Tags.IS_VANILLA) && BetterAdventureMode.serverConfig.show_debug_log) {
                BetterAdventureMode.info("This vanilla damage type was used: " + source.getType().toString());
            }

            if (source.isIn(DamageTypeTags.IS_FALL) && this.hasStatusEffect(StatusEffectsRegistry.FALL_IMMUNE)) {
                return;
            }

            if (this.hasStatusEffect(StatusEffectsRegistry.STAGGERED)) {
                amount = amount * 2;
            }

            StatusEffectInstance calamityEffectInstance = this.getStatusEffect(StatusEffectsRegistry.CALAMITY);
            if (calamityEffectInstance != null) {
                amount = amount * 2 + calamityEffectInstance.getAmplifier();
            }

            if (!source.isIn(DamageTypeTags.BYPASSES_ARMOR)) {
                this.damageArmor(source, amount);
            }

            float applied_damage = 0;
            float true_amount = 0;
            if (source.isIn(Tags.IS_TRUE_DAMAGE)) {
                true_amount = amount;
                amount = 0;
            }

            if (amount > 0) {
                float bashing_multiplier = source.isIn(Tags.HAS_BASHING_DIVISION_OF_1) ? 1.0f : source.isIn(Tags.HAS_BASHING_DIVISION_OF_0_9) ? 0.9f : source.isIn(Tags.HAS_BASHING_DIVISION_OF_0_8) ? 0.8f : source.isIn(Tags.HAS_BASHING_DIVISION_OF_0_7) ? 0.7f : source.isIn(Tags.HAS_BASHING_DIVISION_OF_0_6) ? 0.6f : source.isIn(Tags.HAS_BASHING_DIVISION_OF_0_5) ? 0.5f : source.isIn(Tags.HAS_BASHING_DIVISION_OF_0_4) ? 0.4f : source.isIn(Tags.HAS_BASHING_DIVISION_OF_0_3) ? 0.3f : source.isIn(Tags.HAS_BASHING_DIVISION_OF_0_2) ? 0.2f : source.isIn(Tags.HAS_BASHING_DIVISION_OF_0_1) ? 0.1f : 0;
                float bashing_amount = amount * bashing_multiplier;

                float piercing_multiplier = source.isIn(Tags.HAS_PIERCING_DIVISION_OF_1) ? 1.0f : source.isIn(Tags.HAS_PIERCING_DIVISION_OF_0_9) ? 0.9f : source.isIn(Tags.HAS_PIERCING_DIVISION_OF_0_8) ? 0.8f : source.isIn(Tags.HAS_PIERCING_DIVISION_OF_0_7) ? 0.7f : source.isIn(Tags.HAS_PIERCING_DIVISION_OF_0_6) ? 0.6f : source.isIn(Tags.HAS_PIERCING_DIVISION_OF_0_5) ? 0.5f : source.isIn(Tags.HAS_PIERCING_DIVISION_OF_0_4) ? 0.4f : source.isIn(Tags.HAS_PIERCING_DIVISION_OF_0_3) ? 0.3f : source.isIn(Tags.HAS_PIERCING_DIVISION_OF_0_2) ? 0.2f : source.isIn(Tags.HAS_PIERCING_DIVISION_OF_0_1) ? 0.1f : 0;
                float piercing_amount = amount * piercing_multiplier;

                float slashing_multiplier = source.isIn(Tags.HAS_SLASHING_DIVISION_OF_1) ? 1.0f : source.isIn(Tags.HAS_SLASHING_DIVISION_OF_0_9) ? 0.9f : source.isIn(Tags.HAS_SLASHING_DIVISION_OF_0_8) ? 0.8f : source.isIn(Tags.HAS_SLASHING_DIVISION_OF_0_7) ? 0.7f : source.isIn(Tags.HAS_SLASHING_DIVISION_OF_0_6) ? 0.6f : source.isIn(Tags.HAS_SLASHING_DIVISION_OF_0_5) ? 0.5f : source.isIn(Tags.HAS_SLASHING_DIVISION_OF_0_4) ? 0.4f : source.isIn(Tags.HAS_SLASHING_DIVISION_OF_0_3) ? 0.3f : source.isIn(Tags.HAS_SLASHING_DIVISION_OF_0_2) ? 0.2f : source.isIn(Tags.HAS_SLASHING_DIVISION_OF_0_1) ? 0.1f : 0;
                float slashing_amount = amount * slashing_multiplier;

                float poison_multiplier = source.isIn(Tags.HAS_POISON_DIVISION_OF_1) ? 1.0f : source.isIn(Tags.HAS_POISON_DIVISION_OF_0_9) ? 0.9f : source.isIn(Tags.HAS_POISON_DIVISION_OF_0_8) ? 0.8f : source.isIn(Tags.HAS_POISON_DIVISION_OF_0_7) ? 0.7f : source.isIn(Tags.HAS_POISON_DIVISION_OF_0_6) ? 0.6f : source.isIn(Tags.HAS_POISON_DIVISION_OF_0_5) ? 0.5f : source.isIn(Tags.HAS_POISON_DIVISION_OF_0_4) ? 0.4f : source.isIn(Tags.HAS_POISON_DIVISION_OF_0_3) ? 0.3f : source.isIn(Tags.HAS_POISON_DIVISION_OF_0_2) ? 0.2f : source.isIn(Tags.HAS_POISON_DIVISION_OF_0_1) ? 0.1f : 0;
                float poison_amount = amount * poison_multiplier;

                float fire_multiplier = source.isIn(Tags.HAS_FIRE_DIVISION_OF_1) ? 1.0f : source.isIn(Tags.HAS_FIRE_DIVISION_OF_0_9) ? 0.9f : source.isIn(Tags.HAS_FIRE_DIVISION_OF_0_8) ? 0.8f : source.isIn(Tags.HAS_FIRE_DIVISION_OF_0_7) ? 0.7f : source.isIn(Tags.HAS_FIRE_DIVISION_OF_0_6) ? 0.6f : source.isIn(Tags.HAS_FIRE_DIVISION_OF_0_5) ? 0.5f : source.isIn(Tags.HAS_FIRE_DIVISION_OF_0_4) ? 0.4f : source.isIn(Tags.HAS_FIRE_DIVISION_OF_0_3) ? 0.3f : source.isIn(Tags.HAS_FIRE_DIVISION_OF_0_2) ? 0.2f : source.isIn(Tags.HAS_FIRE_DIVISION_OF_0_1) ? 0.1f : 0;
                float fire_amount = amount * fire_multiplier;

                float frost_multiplier = source.isIn(Tags.HAS_FROST_DIVISION_OF_1) ? 1.0f : source.isIn(Tags.HAS_FROST_DIVISION_OF_0_9) ? 0.9f : source.isIn(Tags.HAS_FROST_DIVISION_OF_0_8) ? 0.8f : source.isIn(Tags.HAS_FROST_DIVISION_OF_0_7) ? 0.7f : source.isIn(Tags.HAS_FROST_DIVISION_OF_0_6) ? 0.6f : source.isIn(Tags.HAS_FROST_DIVISION_OF_0_5) ? 0.5f : source.isIn(Tags.HAS_FROST_DIVISION_OF_0_4) ? 0.4f : source.isIn(Tags.HAS_FROST_DIVISION_OF_0_3) ? 0.3f : source.isIn(Tags.HAS_FROST_DIVISION_OF_0_2) ? 0.2f : source.isIn(Tags.HAS_FROST_DIVISION_OF_0_1) ? 0.1f : 0;
                float frost_amount = amount * frost_multiplier;

                float lightning_multiplier = source.isIn(Tags.HAS_LIGHTNING_DIVISION_OF_1) ? 1.0f : source.isIn(Tags.HAS_LIGHTNING_DIVISION_OF_0_9) ? 0.9f : source.isIn(Tags.HAS_LIGHTNING_DIVISION_OF_0_8) ? 0.8f : source.isIn(Tags.HAS_LIGHTNING_DIVISION_OF_0_7) ? 0.7f : source.isIn(Tags.HAS_LIGHTNING_DIVISION_OF_0_6) ? 0.6f : source.isIn(Tags.HAS_LIGHTNING_DIVISION_OF_0_5) ? 0.5f : source.isIn(Tags.HAS_LIGHTNING_DIVISION_OF_0_4) ? 0.4f : source.isIn(Tags.HAS_LIGHTNING_DIVISION_OF_0_3) ? 0.3f : source.isIn(Tags.HAS_LIGHTNING_DIVISION_OF_0_2) ? 0.2f : source.isIn(Tags.HAS_LIGHTNING_DIVISION_OF_0_1) ? 0.1f : 0;
                float lightning_amount = amount * lightning_multiplier;

                if (attacker != null) {
                    bashing_amount = (float) ((attacker.getAttributeValue(EntityAttributesRegistry.ADDITIONAL_BASHING_DAMAGE) + bashing_amount) * attacker.getAttributeValue(EntityAttributesRegistry.INCREASED_BASHING_DAMAGE));
                    piercing_amount = (float) ((attacker.getAttributeValue(EntityAttributesRegistry.ADDITIONAL_PIERCING_DAMAGE) + piercing_amount) * attacker.getAttributeValue(EntityAttributesRegistry.INCREASED_PIERCING_DAMAGE));
                    slashing_amount = (float) ((attacker.getAttributeValue(EntityAttributesRegistry.ADDITIONAL_SLASHING_DAMAGE) + slashing_amount) * attacker.getAttributeValue(EntityAttributesRegistry.INCREASED_SLASHING_DAMAGE));
                    fire_amount = (float) ((attacker.getAttributeValue(EntityAttributesRegistry.ADDITIONAL_FIRE_DAMAGE) + fire_amount) * attacker.getAttributeValue(EntityAttributesRegistry.INCREASED_FIRE_DAMAGE));
                    frost_amount = (float) ((attacker.getAttributeValue(EntityAttributesRegistry.ADDITIONAL_FROST_DAMAGE) + frost_amount) * attacker.getAttributeValue(EntityAttributesRegistry.INCREASED_FROST_DAMAGE));
                    lightning_amount = (float) ((attacker.getAttributeValue(EntityAttributesRegistry.ADDITIONAL_LIGHTNING_DAMAGE) + lightning_amount) * attacker.getAttributeValue(EntityAttributesRegistry.INCREASED_LIGHTNING_DAMAGE));
                    poison_amount = (float) ((attacker.getAttributeValue(EntityAttributesRegistry.ADDITIONAL_POISON_DAMAGE) + poison_amount) * attacker.getAttributeValue(EntityAttributesRegistry.INCREASED_POISON_DAMAGE));
                }

                // region shield blocks
                ItemStack shieldItemStack = this.getOffHandStack();
                if (this.isBlocking() && this.blockedByShield(source) && this.betteradventuremode$getStamina() > 0 && shieldItemStack.getItem() instanceof BasicShieldItem basicShieldItem) {
                    boolean tryParry = this.betteradventuremode$canParry() && this.blockingTime <= 20 && this.betteradventuremode$getStamina() >= 2 && source.getAttacker() != null && source.getAttacker() instanceof LivingEntity && ((BasicShieldItem) shieldItemStack.getItem()).canParry();
                    // try to parry the attack
                    double parryBonus = tryParry ? basicShieldItem.getParryBonus() : 1;
                    float blockedBashingDamage = (float) (bashing_amount - basicShieldItem.getPhysicalDamageReduction() * parryBonus);
                    float blockedPiercingDamage = (float) (piercing_amount - basicShieldItem.getPhysicalDamageReduction() * parryBonus);
                    float blockedSlashingDamage = (float) (slashing_amount - basicShieldItem.getPhysicalDamageReduction() * parryBonus);
                    float blockedFireDamage = (float) (fire_amount - basicShieldItem.getFireDamageReduction() * parryBonus);
                    float blockedFrostDamage = (float) (frost_amount - basicShieldItem.getFrostDamageReduction() * parryBonus);
                    float blockedLightningDamage = (float) (lightning_amount - basicShieldItem.getLightningDamageReduction() * parryBonus);

                    this.betteradventuremode$addStamina(tryParry ? -4 : -2);

                    if (this.betteradventuremode$getStamina() >= 0) {

                        boolean isStaggered = false;
                        // apply stagger based on left over damage
                        float appliedStagger = (float) Math.max(((bashing_amount - blockedBashingDamage) * 0.75 + (piercing_amount - blockedPiercingDamage) * 0.5 + (slashing_amount - blockedSlashingDamage) * 0.5 + (lightning_amount - blockedLightningDamage) * 0.5), 0);
                        if (appliedStagger > 0) {
                            this.betteradventuremode$addStaggerBuildUp(appliedStagger);
                            isStaggered = this.hasStatusEffect(StatusEffectsRegistry.STAGGERED);
                        }

                        // parry was successful
                        if (!isStaggered) {
                            bashing_amount -= blockedBashingDamage;
                            piercing_amount -= blockedPiercingDamage;
                            slashing_amount -= blockedSlashingDamage;
                            fire_amount -= blockedFireDamage;
                            frost_amount -= blockedFrostDamage;
                            lightning_amount -= blockedLightningDamage;

                            if (tryParry) {

                                // TODO only one attack can be parried ?
//                        this.blockingTime = this.blockingTime + 20;

                                if (attacker != null) {
                                    // attacker is staggered
                                    ((DuckLivingEntityMixin) attacker).betteradventuremode$addStaggerBuildUp(((DuckLivingEntityMixin) attacker).betteradventuremode$getMaxStaggerBuildUp());
                                }

                            } else {

                                //
                                if (attacker != null) {
                                    attacker.takeKnockback(((BasicShieldItem) shieldItemStack.getItem()).getBlockForce(), attacker.getX() - this.getX(), attacker.getZ() - this.getZ());
                                }

                            }
                            float totalBlockedDamage = blockedBashingDamage + blockedPiercingDamage + blockedSlashingDamage + blockedFireDamage + blockedFrostDamage + blockedLightningDamage;
                            if (((LivingEntity) (Object) this) instanceof ServerPlayerEntity serverPlayerEntity && totalBlockedDamage > 0.0f && totalBlockedDamage < 3.4028235E37f) {
                                serverPlayerEntity.increaseStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(totalBlockedDamage * 10.0f));
                            }

                            this.getWorld().sendEntityStatus(this, EntityStatuses.BLOCK_WITH_SHIELD);
                        } else {
                            this.getWorld().sendEntityStatus(this, EntityStatuses.BREAK_SHIELD);
                        }
                    }
//        } else if (!source.isIn(Tags.IS_TRUE_DAMAGE)) {
//            this.stopUsingItem();
                }
                // endregion shield blocks

                // region apply resistances/armor
                // armorToughness now directly determines how effective armor is
                // it can not increase armor beyond the initial value
                // effective armor reduces damage by its amount
                // armor can't reduce damage to zero
                float effectiveArmor = this.getArmor() * MathHelper.clamp((float) this.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS), 0, 1);
                if (piercing_amount * 1.25 <= effectiveArmor) {
                    effectiveArmor -= piercing_amount * 1.25;
                    piercing_amount = 0;
                } else {
                    // TODO think about this more
                    piercing_amount -= effectiveArmor;
                    effectiveArmor = 0;
                }

                if (bashing_amount <= effectiveArmor) {
                    effectiveArmor -= bashing_amount;
                    bashing_amount = 0;
                } else {
                    bashing_amount -= effectiveArmor;
                    effectiveArmor = 0;
                }

                if (fire_amount <= effectiveArmor) {
                    effectiveArmor -= fire_amount;
                    fire_amount = 0;
                } else {
                    fire_amount -= effectiveArmor;
                    effectiveArmor = 0;
                }

                if (slashing_amount <= effectiveArmor) {
                    effectiveArmor -= slashing_amount;
                    slashing_amount = 0;
                } else {
                    slashing_amount -= effectiveArmor;
                    slashing_amount = (float) (slashing_amount * 1.25); // slashing damage not blocked by armor deals more damage
                    effectiveArmor = 0;
                }

                poison_amount = (float) (poison_amount - (poison_amount * this.getAttributeValue(EntityAttributesRegistry.POISON_RESISTANCE)) / 100);

                fire_amount = (float) (fire_amount - (fire_amount * this.getAttributeValue(EntityAttributesRegistry.FIRE_RESISTANCE)) / 100);

                frost_amount = (float) (frost_amount - (frost_amount * this.getAttributeValue(EntityAttributesRegistry.FROST_RESISTANCE)) / 100);

                lightning_amount = (float) (lightning_amount - (lightning_amount * this.getAttributeValue(EntityAttributesRegistry.LIGHTNING_RESISTANCE)) / 100);
                // endregion apply resistances/armor


                applied_damage = piercing_amount + bashing_amount + slashing_amount;

                // taking damage interrupts eating food, drinking potions, etc
                if (applied_damage > 0.0f && !this.isBlocking()) {
                    this.stopUsingItem();
                }

                // apply bleeding
                float appliedBleeding = (float) ((piercing_amount * 0.5) + (slashing_amount * 0.5));
                if (appliedBleeding > 0 && source.isIn(Tags.APPLIES_BLEEDING)) {
                    this.betteradventuremode$addBleedingBuildUp(appliedBleeding);
                }

                // apply burning
                if (fire_amount > 0) {
                    float burnBuildUpMultiplier = this.hasStatusEffect(StatusEffectsRegistry.WET) ? 0.5f : 1.0f;//TODO should wet effect impact burning build up?
                    this.betteradventuremode$addBurnBuildUp(fire_amount * burnBuildUpMultiplier);
                }

                // apply chilled and frozen
                if (frost_amount > 0) {
                    // apply chilled
                    int chilledDuration = (int) Math.ceil(frost_amount);
                    StatusEffectInstance statusEffectInstance = this.getStatusEffect(StatusEffectsRegistry.CHILLED);
                    if (statusEffectInstance != null) {
                        chilledDuration = chilledDuration + statusEffectInstance.getDuration();
                    }
                    this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.CHILLED, chilledDuration, 0, false, false, true));

                    // apply frozen
                    float freezeBuildUp = frost_amount * (this.hasStatusEffect(StatusEffectsRegistry.WET) ? 2 : 1);//TODO should wet effect impact freeze build up?
                    if (freezeBuildUp > 0) {
                        this.betteradventuremode$addFreezeBuildUp(freezeBuildUp);
                    }
                }

                // apply stagger
                float appliedStagger = (float) ((bashing_amount * 0.75) + (piercing_amount * 0.5) + (slashing_amount * 0.5) + (lightning_amount * 0.5));
                if (appliedStagger > 0) {
                    this.betteradventuremode$addStaggerBuildUp(appliedStagger);
                }

                // apply poison
                if (poison_amount > 0) {
                    this.betteradventuremode$addPoisonBuildUp(poison_amount);
                }

                // apply shocked
                if (lightning_amount > 0) {
                    this.betteradventuremode$addShockBuildUp(lightning_amount);
                }
            }

            applied_damage = applied_damage + true_amount;
            if (applied_damage != 0.0F) {
                this.getDamageTracker().onDamage(source, applied_damage);
                this.setHealth(this.getHealth() - applied_damage);
                if (((LivingEntity) (Object) this) instanceof ServerPlayerEntity serverPlayerEntity && applied_damage < 3.4028235E37f) {
                    serverPlayerEntity.increaseStat(Stats.DAMAGE_TAKEN, Math.round(applied_damage * 10.0f));
                }
                this.emitGameEvent(GameEvent.ENTITY_DAMAGE);
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void betteradventuremode$tick(CallbackInfo ci) {
        if (!this.getWorld().isClient) {

            this.isMoving = this.oldPosX != this.getX() || this.oldPosY != this.getY() || this.oldPosZ != this.getZ();
            this.oldPosX = this.getX();
            this.oldPosY = this.getY();
            this.oldPosZ = this.getZ();

            if (this.isBlocking()) {
                this.blockingTime++;
            } else if (this.blockingTime > 0) {
                this.blockingTime = 0;
            }
            this.healthTickTimer++;
            this.staminaTickTimer++;
            this.manaTickTimer++;

//            if (this.getHealth() <= 0 && this.delayHealthRegeneration) {
//                this.healthRegenerationDelayTimer = 0;
//                this.delayHealthRegeneration = false;
//            }
//            if (this.getHealth() > 0 && !this.delayHealthRegeneration) {
//                this.delayHealthRegeneration = true;
//            }
            if (this.healthRegenerationDelayTimer <= this.betteradventuremode$getHealthRegenerationDelayThreshold()) {
                this.healthRegenerationDelayTimer++;
            }

            if (this.healthTickTimer >= this.betteradventuremode$getHealthTickThreshold() && this.healthRegenerationDelayTimer >= this.betteradventuremode$getHealthRegenerationDelayThreshold()) {
                if (this.getHealth() < this.getMaxHealth()) {
                    this.heal(this.betteradventuremode$getRegeneratedHealth());
                } else if (this.getHealth() > this.getMaxHealth()) {
                    this.setHealth(this.getMaxHealth());
                }
                this.healthTickTimer = 0;
            }

            if (this.betteradventuremode$getStamina() <= 0 && this.delayStaminaRegeneration) {
                this.staminaRegenerationDelayTimer = 0;
                this.delayStaminaRegeneration = false;
            }
            if (this.betteradventuremode$getStamina() > 0 && !this.delayStaminaRegeneration) {
                this.delayStaminaRegeneration = true;
            }
            if (this.staminaRegenerationDelayTimer <= this.betteradventuremode$getStaminaRegenerationDelayThreshold()) {
                this.staminaRegenerationDelayTimer++;
            }

            if (this.staminaTickTimer >= this.betteradventuremode$getStaminaTickThreshold() && this.staminaRegenerationDelayTimer >= this.betteradventuremode$getStaminaRegenerationDelayThreshold()) {
                if (this.betteradventuremode$getStamina() < this.betteradventuremode$getMaxStamina()) {
                    this.betteradventuremode$addStamina(this.betteradventuremode$getRegeneratedStamina());
                } else if (this.betteradventuremode$getStamina() > this.betteradventuremode$getMaxStamina()) {
                    this.betteradventuremode$setStamina(this.betteradventuremode$getMaxStamina());
                }
                this.staminaTickTimer = 0;
            }

//            if (this.betteradventuremode$getMana() <= 0 && this.delayManaRegeneration) {
//                this.manaRegenerationDelayTimer = 0;
//                this.delayManaRegeneration = false;
//            }
//            if (this.betteradventuremode$getMana() > 0 && !this.delayManaRegeneration) {
//                this.delayManaRegeneration = true;
//            }
            if (this.manaRegenerationDelayTimer <= this.betteradventuremode$getManaRegenerationDelayThreshold()) {
                this.manaRegenerationDelayTimer++;
            }

            if (this.manaTickTimer >= this.betteradventuremode$getManaTickThreshold() && this.manaRegenerationDelayTimer >= this.betteradventuremode$getManaRegenerationDelayThreshold()) {
                if (this.betteradventuremode$getMana() < this.betteradventuremode$getMaxMana()) {
                    ((DuckLivingEntityMixin) this).betteradventuremode$addMana(this.betteradventuremode$getRegeneratedMana());
                } else if (this.betteradventuremode$getMana() > this.betteradventuremode$getMaxMana()) {
                    this.betteradventuremode$setMana(this.betteradventuremode$getMaxMana());
                }
                this.manaTickTimer = 0;
            }

            if (this.betteradventuremode$getBleedingBuildUp() > 0) {
                this.bleedingTickTimer++;
                if (this.bleedingTickTimer >= this.betteradventuremode$getBleedingTickThreshold()) {
                    if (this.betteradventuremode$getBleedingBuildUp() >= this.betteradventuremode$getMaxBleedingBuildUp()) {
                        this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.BLEEDING, this.betteradventuremode$getBleedingDuration(), 0, false, false, true));
                        this.betteradventuremode$setBleedingBuildUp(-this.betteradventuremode$getMaxBleedingBuildUp());
                    } else {
                        this.betteradventuremode$addBleedingBuildUp( - this.betteradventuremode$getBleedingBuildUpReduction());
                    }
                    this.bleedingTickTimer = 0;
                }
            }

            if (this.betteradventuremode$getBurnBuildUp() > 0) {
                this.burnTickTimer++;
                if (this.burnTickTimer >= this.betteradventuremode$getBurnTickThreshold()) {
                    if (this.betteradventuremode$getBurnBuildUp() >= this.betteradventuremode$getMaxBurnBuildUp()) {
                        int burnDuration = this.betteradventuremode$getBurnDuration();
                        StatusEffectInstance statusEffectInstance = this.getStatusEffect(StatusEffectsRegistry.BURNING);
                        if (statusEffectInstance != null) {
                            burnDuration = burnDuration + statusEffectInstance.getDuration();
                        }
                        this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.BURNING, burnDuration, 0, false, false, true));
                        this.betteradventuremode$setBurnBuildUp(0);
                    } else {
                        this.betteradventuremode$addBurnBuildUp( - this.betteradventuremode$getBurnBuildUpReduction());
                    }
                    this.burnTickTimer = 0;
                }
            }

            if (this.betteradventuremode$getFreezeBuildUp() > 0) {
                this.freezeTickTimer++;
                if (this.freezeTickTimer >= this.betteradventuremode$getFreezeTickThreshold()) {
                    if (this.betteradventuremode$getFreezeBuildUp() >= this.betteradventuremode$getMaxFreezeBuildUp()) {
                        this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.FROZEN, this.betteradventuremode$getFreezeDuration(), 0, false, false, true));
                        this.betteradventuremode$setFreezeBuildUp(0);
                    } else {
                        this.betteradventuremode$addFreezeBuildUp( - this.betteradventuremode$getFreezeBuildUpReduction());
                    }
                    this.freezeTickTimer = 0;
                }
            }

            if (this.betteradventuremode$getStaggerBuildUp() > 0) {
                this.staggerTickTimer++;
                if (this.staggerTickTimer >= this.betteradventuremode$getStaggerTickThreshold()) {
                    if (this.betteradventuremode$getStaggerBuildUp() >= this.betteradventuremode$getMaxStaggerBuildUp()) {
                        this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.STAGGERED, this.betteradventuremode$getStaggerDuration(), 0, false, false, true));
                        this.betteradventuremode$setStaggerBuildUp(0);
                    } else {
                        this.betteradventuremode$addStaggerBuildUp( - this.betteradventuremode$getStaggerBuildUpReduction());
                    }
                    this.staggerTickTimer = 0;
                }
            }

            if (this.betteradventuremode$getPoisonBuildUp() > 0) {
                this.poisonTickTimer++;
                if (this.poisonTickTimer >= this.betteradventuremode$getPoisonTickThreshold()) {
                    if (this.betteradventuremode$getPoisonBuildUp() >= this.betteradventuremode$getMaxPoisonBuildUp()) {
                        int poisonAmplifier = 0;
                        StatusEffectInstance statusEffectInstance = this.getStatusEffect(StatusEffectsRegistry.POISON);
                        if (statusEffectInstance != null) {
                            poisonAmplifier = statusEffectInstance.getAmplifier() + 1;
                        }
                        this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.POISON, this.betteradventuremode$getPoisonDuration(), poisonAmplifier, false, false, true));
                        this.betteradventuremode$setPoisonBuildUp(0);
                    } else {
                        this.betteradventuremode$addPoisonBuildUp( - this.betteradventuremode$getPoisonBuildUpReduction());
                    }
                    this.poisonTickTimer = 0;
                }
            }

            if (this.betteradventuremode$getShockBuildUp() > 0) {
                this.shockTickTimer++;
                if (this.shockTickTimer >= this.betteradventuremode$getShockTickThreshold()) {
                    if (this.betteradventuremode$getShockBuildUp() >= this.betteradventuremode$getMaxShockBuildUp()) {
                        this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.SHOCKED, this.betteradventuremode$getShockDuration(), 0, false, false, false));
                        this.betteradventuremode$setShockBuildUp(0);
                    } else {
                        this.betteradventuremode$addShockBuildUp( - this.betteradventuremode$getShockBuildUpReduction());
                    }
                    this.shockTickTimer = 0;
                }
            }

            StatusEffectInstance burningStatusEffectInstance = this.getStatusEffect(StatusEffectsRegistry.BURNING);
            if (burningStatusEffectInstance != null && this.isTouchingWaterOrRain()) {
                int oldBurnDuration = burningStatusEffectInstance.getDuration();
                this.removeStatusEffect(StatusEffectsRegistry.BURNING);
                this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.BURNING, oldBurnDuration - 5, 0, false, false, true));
            }

        }
    }

    /**
     * @author TheRedBrain
     * @reason WIP
     */
    @Overwrite
    public boolean isBlocking() {
        if (this.isUsingItem() && !this.activeItemStack.isEmpty()) {
            Item item = this.activeItemStack.getItem();
            if (item.getUseAction(this.activeItemStack) != UseAction.BLOCK) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean doesRenderOnFire() {
        return super.doesRenderOnFire() || (this.hasStatusEffect(StatusEffectsRegistry.BURNING) && !this.isSpectator());
    }

    @Override
    public boolean betteradventuremode$hasEquipped(Predicate<ItemStack> predicate) {
        if (predicate.test(this.getEquippedStack(EquipmentSlot.MAINHAND))) {
            return true;
        }
        if (predicate.test(this.getEquippedStack(EquipmentSlot.OFFHAND))) {
            return true;
        }
        if (predicate.test(this.getEquippedStack(EquipmentSlot.FEET))) {
            return true;
        }
        if (predicate.test(this.getEquippedStack(EquipmentSlot.LEGS))) {
            return true;
        }
        if (predicate.test(this.getEquippedStack(EquipmentSlot.CHEST))) {
            return true;
        }
        if (predicate.test(this.getEquippedStack(EquipmentSlot.HEAD))) {
            return true;
        }
        return false;
    }

    @Override
    public int betteradventuremode$getAmountEquipped(Predicate<ItemStack> predicate) {
        int i = 0;
        if (predicate.test(this.getEquippedStack(EquipmentSlot.MAINHAND))) {
            i += 1;
        }
        if (predicate.test(this.getEquippedStack(EquipmentSlot.OFFHAND))) {
            i += 1;
        }
        if (predicate.test(this.getEquippedStack(EquipmentSlot.FEET))) {
            i += 1;
        }
        if (predicate.test(this.getEquippedStack(EquipmentSlot.LEGS))) {
            i += 1;
        }
        if (predicate.test(this.getEquippedStack(EquipmentSlot.CHEST))) {
            i += 1;
        }
        if (predicate.test(this.getEquippedStack(EquipmentSlot.HEAD))) {
            i += 1;
        }
        return i;
    }

    @Override
    public int betteradventuremode$getHealthRegenerationDelayThreshold() {
        return (int) this.getAttributeValue(EntityAttributesRegistry.HEALTH_REGENERATION_DELAY_THRESHOLD);
    }

    @Override
    public int betteradventuremode$getStaminaRegenerationDelayThreshold() {
        return (int) this.getAttributeValue(EntityAttributesRegistry.STAMINA_REGENERATION_DELAY_THRESHOLD);
    }

    @Override
    public int betteradventuremode$getManaRegenerationDelayThreshold() {
        return (int) this.getAttributeValue(EntityAttributesRegistry.MANA_REGENERATION_DELAY_THRESHOLD);
    }

    @Override
    public int betteradventuremode$getHealthTickThreshold() {
        return (int) this.getAttributeValue(EntityAttributesRegistry.HEALTH_TICK_THRESHOLD);
    }

    @Override
    public int betteradventuremode$getStaminaTickThreshold() {
        return (int) this.getAttributeValue(EntityAttributesRegistry.STAMINA_TICK_THRESHOLD);
    }

    @Override
    public int betteradventuremode$getManaTickThreshold() {
        return (int) this.getAttributeValue(EntityAttributesRegistry.MANA_TICK_THRESHOLD);
    }

    @Override
    public float betteradventuremode$getRegeneratedHealth() {
        return this.betteradventuremode$getHealthRegeneration();
    }

    @Override
    public float betteradventuremode$getRegeneratedStamina() {
        return this.betteradventuremode$getStaminaRegeneration();
    }

    @Override
    public float betteradventuremode$getRegeneratedMana() {
        return this.betteradventuremode$getManaRegeneration();
    }

    // region bleeding build up
    @Override
    public void betteradventuremode$addBleedingBuildUp(float amount) {
        if (this.betteradventuremode$getMaxBleedingBuildUp() != -1.0f && !this.hasStatusEffect(StatusEffectsRegistry.BLEEDING)) {
            float f = this.betteradventuremode$getBleedingBuildUp();
            this.betteradventuremode$setBleedingBuildUp(f + amount);
            if (amount > 0) {
                this.bleedingTickTimer = this.betteradventuremode$getBleedingTickThreshold();
            }
        }
    }

    @Override
    public float betteradventuremode$getBleedingBuildUp() {
        return this.dataTracker.get(BLEEDING_BUILD_UP);
    }

    @Override
    public void betteradventuremode$setBleedingBuildUp(float bleedingBuildUp) {
        this.dataTracker.set(BLEEDING_BUILD_UP, MathHelper.clamp(bleedingBuildUp, 0, this.betteradventuremode$getMaxBleedingBuildUp()));
    }

    @Override
    public float betteradventuremode$getMaxBleedingBuildUp() {
        return (float) this.getAttributeValue(EntityAttributesRegistry.MAX_BLEEDING_BUILD_UP);
    }

    @Override
    public int betteradventuremode$getBleedingDuration() {
        return (int) this.getAttributeValue(EntityAttributesRegistry.BLEEDING_DURATION);
    }

    @Override
    public int betteradventuremode$getBleedingTickThreshold() {
        return (int) this.getAttributeValue(EntityAttributesRegistry.BLEEDING_TICK_THRESHOLD);
    }

    @Override
    public int betteradventuremode$getBleedingBuildUpReduction() {
        return (int) this.getAttributeValue(EntityAttributesRegistry.BLEEDING_BUILD_UP_REDUCTION);
    }
    // endregion bleeding build up

    // region burn build up
    @Override
    public void betteradventuremode$addBurnBuildUp(float amount) {
        if (this.betteradventuremode$getMaxBurnBuildUp() != -1.0f) {
            float f = this.betteradventuremode$getBurnBuildUp();
            this.betteradventuremode$setBurnBuildUp(f + amount);
            if (amount > 0) {
                this.burnTickTimer = this.betteradventuremode$getBurnTickThreshold();
            }
        }
    }

    @Override
    public float betteradventuremode$getBurnBuildUp() {
        return this.dataTracker.get(BURN_BUILD_UP);
    }

    @Override
    public void betteradventuremode$setBurnBuildUp(float burnBuildUp) {
        this.dataTracker.set(BURN_BUILD_UP, MathHelper.clamp(burnBuildUp, 0, this.betteradventuremode$getMaxBurnBuildUp()));
    }

    @Override
    public float betteradventuremode$getMaxBurnBuildUp() {
        return (float) this.getAttributeValue(EntityAttributesRegistry.MAX_BURN_BUILD_UP);
    }

    @Override
    public int betteradventuremode$getBurnDuration() {
        return (int) this.getAttributeValue(EntityAttributesRegistry.BURN_DURATION);
    }

    @Override
    public int betteradventuremode$getBurnTickThreshold() {
        return (int) this.getAttributeValue(EntityAttributesRegistry.BURN_TICK_THRESHOLD);
    }

    @Override
    public int betteradventuremode$getBurnBuildUpReduction() {
        return (int) this.getAttributeValue(EntityAttributesRegistry.BURN_BUILD_UP_REDUCTION);
    }
    // endregion burn build up

    // region freeze build up
    @Override
    public void betteradventuremode$addFreezeBuildUp(float amount) {
        if (this.betteradventuremode$getMaxFreezeBuildUp() != -1.0f && !this.hasStatusEffect(StatusEffectsRegistry.FROZEN)) {
            float f = this.betteradventuremode$getFreezeBuildUp();
            this.betteradventuremode$setFreezeBuildUp(f + amount);
            // TODO play test
            if (this.betteradventuremode$getFreezeBuildUp() >= this.betteradventuremode$getMaxFreezeBuildUp()) {
                this.freezeTickTimer = this.betteradventuremode$getFreezeTickThreshold();
            } else {
                this.freezeTickTimer = 0;
            }
        }
    }

    @Override
    public float betteradventuremode$getFreezeBuildUp() {
        return this.dataTracker.get(FREEZE_BUILD_UP);
    }

    @Override
    public void betteradventuremode$setFreezeBuildUp(float freezeBuildUp) {
        this.dataTracker.set(FREEZE_BUILD_UP, MathHelper.clamp(freezeBuildUp, 0, this.betteradventuremode$getMaxFreezeBuildUp()));
    }

    @Override
    public float betteradventuremode$getMaxFreezeBuildUp() {
        return (float) this.getAttributeValue(EntityAttributesRegistry.MAX_FREEZE_BUILD_UP);
    }

    @Override
    public int betteradventuremode$getFreezeDuration() {
        return (int) this.getAttributeValue(EntityAttributesRegistry.FREEZE_DURATION);
    }

    @Override
    public int betteradventuremode$getFreezeTickThreshold() {
        return (int) this.getAttributeValue(EntityAttributesRegistry.FREEZE_TICK_THRESHOLD);
    }

    @Override
    public int betteradventuremode$getFreezeBuildUpReduction() {
        return (int) this.getAttributeValue(EntityAttributesRegistry.FREEZE_BUILD_UP_REDUCTION);
    }
    // endregion freeze build up

    // region stagger build up
    @Override
    public void betteradventuremode$addStaggerBuildUp(float amount) {
        if (this.betteradventuremode$getMaxStaggerBuildUp() != -1.0f && !this.hasStatusEffect(StatusEffectsRegistry.STAGGERED)) {
            float f = this.betteradventuremode$getStaggerBuildUp();
            this.betteradventuremode$setStaggerBuildUp(f + amount);
            if (amount > 0) {
                this.staggerTickTimer = this.betteradventuremode$getStaggerTickThreshold();
            }
        }
    }

    @Override
    public float betteradventuremode$getStaggerBuildUp() {
        return this.dataTracker.get(STAGGER_BUILD_UP);
    }

    @Override
    public void betteradventuremode$setStaggerBuildUp(float staggerBuildUp) {
        this.dataTracker.set(STAGGER_BUILD_UP, MathHelper.clamp(staggerBuildUp, 0, this.betteradventuremode$getMaxStaggerBuildUp()));
    }

    @Override
    public float betteradventuremode$getMaxStaggerBuildUp() {
        return (float) this.getAttributeValue(EntityAttributesRegistry.MAX_STAGGER_BUILD_UP);
    }

    @Override
    public int betteradventuremode$getStaggerDuration() {
        return (int) this.getAttributeValue(EntityAttributesRegistry.STAGGER_DURATION);
    }

    @Override
    public int betteradventuremode$getStaggerTickThreshold() {
        return (int) this.getAttributeValue(EntityAttributesRegistry.STAGGER_TICK_THRESHOLD);
    }

    @Override
    public int betteradventuremode$getStaggerBuildUpReduction() {
        return (int) this.getAttributeValue(EntityAttributesRegistry.STAGGER_BUILD_UP_REDUCTION);
    }
    // endregion stagger build up

    // region poison build up
    @Override
    public void betteradventuremode$addPoisonBuildUp(float amount) {
        if (this.betteradventuremode$getMaxPoisonBuildUp() != -1.0f) {
            float f = this.betteradventuremode$getPoisonBuildUp();
            this.betteradventuremode$setPoisonBuildUp(f + amount);
            if (amount > 0) {
                this.poisonTickTimer = this.betteradventuremode$getPoisonTickThreshold();
            }
        }
    }

    @Override
    public float betteradventuremode$getPoisonBuildUp() {
        return this.dataTracker.get(POISON_BUILD_UP);
    }

    @Override
    public void betteradventuremode$setPoisonBuildUp(float poisonBuildUp) {
        this.dataTracker.set(POISON_BUILD_UP, MathHelper.clamp(poisonBuildUp, 0, this.betteradventuremode$getMaxPoisonBuildUp()));
    }

    @Override
    public float betteradventuremode$getMaxPoisonBuildUp() {
        return (float) this.getAttributeValue(EntityAttributesRegistry.MAX_POISON_BUILD_UP);
    }

    @Override
    public int betteradventuremode$getPoisonDuration() {
        return (int) this.getAttributeValue(EntityAttributesRegistry.POISON_DURATION);
    }

    @Override
    public int betteradventuremode$getPoisonTickThreshold() {
        return (int) this.getAttributeValue(EntityAttributesRegistry.POISON_TICK_THRESHOLD);
    }

    @Override
    public int betteradventuremode$getPoisonBuildUpReduction() {
        return (int) this.getAttributeValue(EntityAttributesRegistry.POISON_BUILD_UP_REDUCTION);
    }
    // endregion poison build up

    // region shock build up
    @Override
    public void betteradventuremode$addShockBuildUp(float amount) {
        if (this.betteradventuremode$getMaxShockBuildUp() != -1.0f) {
            float f = this.betteradventuremode$getShockBuildUp();
            this.betteradventuremode$setShockBuildUp(f + amount);
            if (amount > 0) {
                this.shockTickTimer = this.betteradventuremode$getShockTickThreshold();
            }
        }
    }

    @Override
    public float betteradventuremode$getShockBuildUp() {
        return this.dataTracker.get(SHOCK_BUILD_UP);
    }

    @Override
    public void betteradventuremode$setShockBuildUp(float shockBuildUp) {
        this.dataTracker.set(SHOCK_BUILD_UP, MathHelper.clamp(shockBuildUp, 0, this.betteradventuremode$getMaxShockBuildUp()));
    }

    @Override
    public float betteradventuremode$getMaxShockBuildUp() {
        return (float) this.getAttributeValue(EntityAttributesRegistry.MAX_SHOCK_BUILD_UP);
    }

    @Override
    public int betteradventuremode$getShockDuration() {
        return (int) this.getAttributeValue(EntityAttributesRegistry.SHOCK_DURATION);
    }

    @Override
    public int betteradventuremode$getShockTickThreshold() {
        return (int) this.getAttributeValue(EntityAttributesRegistry.SHOCK_TICK_THRESHOLD);
    }

    @Override
    public int betteradventuremode$getShockBuildUpReduction() {
        return (int) this.getAttributeValue(EntityAttributesRegistry.SHOCK_BUILD_UP_REDUCTION);
    }
    // endregion shock build up

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
            this.manaRegenerationDelayTimer = 0;
            this.manaTickTimer = 0;
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
            this.staminaTickTimer = 0;
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

    @Override
    public boolean betteradventuremode$canParry() {
        return false;
    }

    @Override
    public boolean betteradventuremode$isMoving() {
        return this.isMoving;
    }

    @Unique
    private void betteradventuremode$dropFromEntity(ItemStack stack) {
        ItemEntity entity = dropStack(stack);
        // Mimic player drop behavior for only players
        if (entity != null && ((Entity) this) instanceof PlayerEntity) {
            entity.setPos(entity.getX(), this.getEyeY() - 0.3, entity.getZ());
            entity.setPickupDelay(40);
            float magnitude = this.random.nextFloat() * 0.5f;
            float angle = this.random.nextFloat() * ((float) Math.PI * 2);
            entity.setVelocity(-MathHelper.sin(angle) * magnitude, 0.2f, MathHelper.cos(angle) * magnitude);
        }
    }
}
