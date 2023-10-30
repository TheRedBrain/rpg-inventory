package com.github.theredbrain.bamcore.mixin.entity;

import com.github.theredbrain.bamcore.registry.StatusEffectsRegistry;
import com.github.theredbrain.bamcore.entity.DamageUtility;
import com.github.theredbrain.bamcore.entity.DuckLivingEntityMixin;
import com.github.theredbrain.bamcore.registry.GameRulesRegistry;
import com.github.theredbrain.bamcore.registry.Tags;
import dev.emi.trinkets.api.*;
import dev.emi.trinkets.api.event.TrinketDropCallback;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.AttributeContainer;
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
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(value = LivingEntity.class, priority = 950)
public abstract class LivingEntityMixin extends Entity implements DuckLivingEntityMixin {

    @Shadow
    public abstract float getMaxHealth();

    @Shadow
    private @Nullable DamageSource lastDamageSource;
    @Shadow
    private long lastDamageTime;

    @Shadow
    protected abstract void playHurtSound(DamageSource source);

    @Shadow
    public abstract void onDeath(DamageSource damageSource);

    @Shadow
    protected abstract @Nullable SoundEvent getDeathSound();

    @Shadow
    public abstract boolean isDead();

    @Shadow
    protected abstract boolean tryUseTotem(DamageSource source);

    @Shadow
    protected abstract float getSoundVolume();

    @Shadow
    public abstract float getSoundPitch();

    @Shadow
    public abstract void tiltScreen(double deltaX, double deltaZ);

    @Shadow
    public abstract void takeKnockback(double strength, double x, double z);

    @Shadow
    public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow
    public abstract boolean isSleeping();

    @Shadow
    public abstract void wakeUp();

    @Shadow
    protected int despawnCounter;
    @Shadow
    @Final
    public LimbAnimator limbAnimator;
    @Shadow
    protected float lastDamageTaken;
    @Shadow
    public int hurtTime;
    @Shadow
    public int maxHurtTime;

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot var1);

    @Shadow
    public abstract void damageHelmet(DamageSource source, float amount);

    @Shadow
    protected int playerHitTimer;
    @Shadow
    @Nullable
    protected PlayerEntity attackingPlayer;

    @Shadow
    public abstract void setAttacker(@Nullable LivingEntity attacker);

    @Shadow
    public abstract boolean addStatusEffect(StatusEffectInstance effect);

    @Shadow
    protected abstract float applyArmorToDamage(DamageSource source, float amount);

    @Shadow
    public abstract float getAbsorptionAmount();

    @Shadow
    public abstract void setAbsorptionAmount(float amount);

    @Shadow
    public abstract DamageTracker getDamageTracker();

    @Shadow
    public abstract void setHealth(float health);

    @Shadow
    public abstract float getHealth();

    @Shadow
    public abstract @Nullable StatusEffectInstance getStatusEffect(StatusEffect effect);

    @Unique
    private static final TrackedData<Float> POISE = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.FLOAT);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "initDataTracker", at = @At("RETURN"))
    protected void bamcore$initDataTracker(CallbackInfo ci) {
        this.dataTracker.startTracking(POISE, 0.0F);

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
                    dropFromEntity(stack);
                    // Fallthrough
                case DESTROY:
                    inventory.setStack(ref.index(), ItemStack.EMPTY);
                    break;
                default:
                    break;
            }
        }));
    }

    private void dropFromEntity(ItemStack stack) {
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

    /**
     * @author TheRedBrain
     * @reason
     */
    @Overwrite
    public boolean damage(DamageSource source, float amount) {
        boolean bl3;
        Entity entity2;
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
        this.despawnCounter = 0;
        float f = amount;
        boolean bl = false;

        // TODO rework shields
//        float g = 0.0f;
//        if (amount > 0.0f && this.blockedByShield(source)) {
//            Entity entity;
//            this.damageShield(amount);
//            g = amount;
//            amount = 0.0f;
//            if (!source.isIn(DamageTypeTags.IS_PROJECTILE) && (entity = source.getSource()) instanceof LivingEntity) {
//                LivingEntity livingEntity = (LivingEntity)entity;
//                this.takeShieldHit(livingEntity);
//            }
//            bl = true;
//        }


        if (source.isIn(DamageTypeTags.IS_FREEZING) && this.getType().isIn(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
            amount *= 5.0f;
        }
        this.limbAnimator.setSpeed(1.5f);
        boolean bl2 = true;

        if ((float) this.timeUntilRegen > 10.0f && !source.isIn(DamageTypeTags.BYPASSES_COOLDOWN)) {
            if (amount <= this.lastDamageTime) {
                return false;
            }
            this.applyDamage(source, amount - this.lastDamageTaken);
            this.lastDamageTaken = amount;
            bl2 = false;
        } else {
            this.lastDamageTaken = amount;
            this.timeUntilRegen = 20;
            this.applyDamage(source, amount);
            this.hurtTime = this.maxHurtTime = 10;
        }
        if (source.isIn(DamageTypeTags.DAMAGES_HELMET) && !this.getEquippedStack(EquipmentSlot.HEAD).isEmpty()) {
            this.damageHelmet(source, amount);
            amount *= 0.75f;
        }
        if ((entity2 = source.getAttacker()) != null) {
            WolfEntity wolfEntity;
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
            } else if (entity2 instanceof WolfEntity && (wolfEntity = (WolfEntity) entity2).isTamed()) {
                PlayerEntity playerEntity2;
                this.playerHitTimer = 100;
                LivingEntity livingEntity = wolfEntity.getOwner();
                this.attackingPlayer = livingEntity instanceof PlayerEntity ? (playerEntity2 = (PlayerEntity) livingEntity) : null;
            }
        }
        if (bl2) {
            if (bl) {
                this.getWorld().sendEntityStatus(this, EntityStatuses.BLOCK_WITH_SHIELD);
            } else {
                this.getWorld().sendEntityDamage(this, source);
            }
            if (!(source.isIn(DamageTypeTags.NO_IMPACT) || bl && !(amount > 0.0f))) {
                this.scheduleVelocityUpdate();
            }
            if (entity2 != null && !source.isIn(DamageTypeTags.IS_EXPLOSION)) {
                double d = entity2.getX() - this.getX();
                double e = entity2.getZ() - this.getZ();
                while (d * d + e * e < 1.0E-4) {
                    d = (Math.random() - Math.random()) * 0.01;
                    e = (Math.random() - Math.random()) * 0.01;
                }
                this.takeKnockback(0.4f, d, e);
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
        boolean bl4 = bl3 = !bl || amount > 0.0f;
        if (bl3) {
            this.lastDamageSource = source;
            this.lastDamageTime = this.getWorld().getTime();
        }
        if (((LivingEntity) (Object) this) instanceof ServerPlayerEntity) {
            Criteria.ENTITY_HURT_PLAYER.trigger(((ServerPlayerEntity) (Object) this), source, f, amount, bl);
//            if (g > 0.0f && g < 3.4028235E37f) {
//                ((ServerPlayerEntity) (Object) this).increaseStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(g * 10.0f));
//            }
        }
        if (entity2 instanceof ServerPlayerEntity) {
            Criteria.PLAYER_HURT_ENTITY.trigger((ServerPlayerEntity) entity2, this, source, f, amount, bl);
        }
        return bl3;
    }

    /**
     * @author TheRedBrain
     * @reason
     */
    @Overwrite
    public float modifyAppliedDamage(DamageSource source, float amount) {
        int i;
        int j;
        float f;
        float g;
        float h;
        if (source.isIn(DamageTypeTags.BYPASSES_EFFECTS)) {
            return amount;
        }
        if (this.hasStatusEffect(StatusEffects.RESISTANCE) && !source.isIn(DamageTypeTags.BYPASSES_RESISTANCE) && (h = (g = amount) - (amount = Math.max((f = amount * (float) (j = 25 - (i = (this.getStatusEffect(StatusEffects.RESISTANCE).getAmplifier() + 1) * 5))) / 25.0f, 0.0f))) > 0.0f && h < 3.4028235E37f) {
            if (((LivingEntity) (Object) this) instanceof ServerPlayerEntity) {
                ((ServerPlayerEntity) (Object) this).increaseStat(Stats.DAMAGE_RESISTED, Math.round(h * 10.0f));
            } else if (source.getAttacker() instanceof ServerPlayerEntity) {
                ((ServerPlayerEntity) source.getAttacker()).increaseStat(Stats.DAMAGE_DEALT_RESISTED, Math.round(h * 10.0f));
            }
        }
        if (amount <= 0.0f) {
            return 0.0f;
        }
        if (source.isIn(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
            return amount;
        }
        boolean feather_falling_trinket_equipped = false;
        if (source.isIn(DamageTypeTags.IS_FALL)) {
            Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent((LivingEntity) (Object) this);
            if (trinkets.isPresent()) {
                Predicate<ItemStack> predicate = stack -> stack.isIn(Tags.FEATHER_FALLING_TRINKETS);
                feather_falling_trinket_equipped = trinkets.get().isEquipped(predicate);
            }
        }
        i = EnchantmentHelper.getProtectionAmount(this.getArmorItems(), source) + (feather_falling_trinket_equipped ? 12 : 0);
        if (i > 0) {
            amount = DamageUtil.getInflictedDamage(amount, i);
        }
        return amount;
    }

    /**
     * @author TheRedBrain
     * @reason
     */
    @Overwrite
    public void applyDamage(DamageSource source, float amount) {
        Entity entity;
        if (this.isInvulnerableTo(source)) {
            return;
        }

//        BetterAdventureModeCore.LOGGER.info("try apply stagger");
        // apply stagger
        if (source.isIn(Tags.STAGGERS) && !(this.getStaggerLimitMultiplier() == -1 || this.hasStatusEffect(StatusEffectsRegistry.STAGGERED))) {
            float appliedStagger = (float) (amount * DamageUtility.getStaggerDamageMultiplierForDamageType(source));
//            BetterAdventureModeCore.LOGGER.info("appliedStagger: " + appliedStagger);
            this.bamcore$addPoise(appliedStagger);
//            BetterAdventureModeCore.LOGGER.info("this.bamcore$getPoise(): " + this.bamcore$getPoise());
//            BetterAdventureModeCore.LOGGER.info("this.getMaxHealth() * this.getStaggerLimitMultiplier(): " + this.getMaxHealth() * this.getStaggerLimitMultiplier());
            if (this.bamcore$getPoise() >= this.getMaxHealth() * this.getStaggerLimitMultiplier()) {
//                BetterAdventureModeCore.LOGGER.info(getEntityName() + " was staggered");
                this.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.STAGGERED, this.getStaggerDuration(), 0, false, false, true));
            }
        }

        amount = this.applyArmorToDamage(source, amount);
        float f = amount = this.modifyAppliedDamage(source, amount);
        amount = Math.max(amount - this.getAbsorptionAmount(), 0.0f);
        this.setAbsorptionAmount(this.getAbsorptionAmount() - (f - amount));
        float g = f - amount;
        if (g > 0.0f && g < 3.4028235E37f && (entity = source.getAttacker()) instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) entity;
            serverPlayerEntity.increaseStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(g * 10.0f));
        }
        if (amount == 0.0f) {
            return;
        }
        this.getDamageTracker().onDamage(source, amount);
        this.setHealth(this.getHealth() - amount);
        this.setAbsorptionAmount(this.getAbsorptionAmount() - amount);
        this.emitGameEvent(GameEvent.ENTITY_DAMAGE);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void bamcore$tick(CallbackInfo ci) {
        if (this.bamcore$getPoise() > 0) {
            this.bamcore$addPoise((float) -(this.getMaxHealth() * this.getStaggerLimitMultiplier() * 0.01));
        }
    }

    @Override
    public void bamcore$addPoise(float amount) {
        float f = this.bamcore$getPoise();
        this.bamcore$setPoise(f + amount);
    }

    @Override
    public float bamcore$getPoise() {
        return this.dataTracker.get(POISE);
    }

    @Override
    public void bamcore$setPoise(float poise) {
        this.dataTracker.set(POISE, MathHelper.clamp(poise, 0, this.getMaxHealth()));
    }

    /**
     * Returns the duration of the Staggered status effect when applied to this entity
     */
    @Override
    public int getStaggerDuration() {
        return 20;
    }

    @Override
    public double getStaggerLimitMultiplier() {
        return 0.5;
    }
}
