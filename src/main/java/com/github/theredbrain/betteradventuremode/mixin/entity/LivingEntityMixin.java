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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(value = LivingEntity.class)
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
    public abstract boolean isBlocking();

    @Shadow
    public abstract void stopUsingItem();

    @Shadow
    public abstract boolean removeStatusEffect(StatusEffect type);

    @Shadow
    public abstract boolean addStatusEffect(StatusEffectInstance effect, @Nullable Entity source);

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

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
    private int staminaRegenerationDelayTimer = 0;
    @Unique
    private boolean delayStaminaRegeneration = false;
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
                .add(EntityAttributesRegistry.ADDITIONAL_BASHING_DAMAGE)
                .add(EntityAttributesRegistry.ADDITIONAL_PIERCING_DAMAGE)
                .add(EntityAttributesRegistry.ADDITIONAL_SLASHING_DAMAGE)
                .add(EntityAttributesRegistry.ADDITIONAL_FIRE_DAMAGE)
                .add(EntityAttributesRegistry.ADDITIONAL_FROST_DAMAGE)
                .add(EntityAttributesRegistry.ADDITIONAL_LIGHTNING_DAMAGE)
                .add(EntityAttributesRegistry.ADDITIONAL_POISON_DAMAGE)
                .add(EntityAttributesRegistry.INCREASED_BASHING_DAMAGE)
                .add(EntityAttributesRegistry.INCREASED_PIERCING_DAMAGE)
                .add(EntityAttributesRegistry.INCREASED_SLASHING_DAMAGE)
                .add(EntityAttributesRegistry.INCREASED_FIRE_DAMAGE)
                .add(EntityAttributesRegistry.INCREASED_FROST_DAMAGE)
                .add(EntityAttributesRegistry.INCREASED_LIGHTNING_DAMAGE)
                .add(EntityAttributesRegistry.INCREASED_POISON_DAMAGE)
                .add(EntityAttributesRegistry.POISON_RESISTANCE)
                .add(EntityAttributesRegistry.FIRE_RESISTANCE)
                .add(EntityAttributesRegistry.FROST_RESISTANCE)
                .add(EntityAttributesRegistry.LIGHTNING_RESISTANCE);
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
     * @reason
     */
    @Overwrite
    public void heal(float amount) {
        float f = this.getHealth();
        if (f > 0.0f) {
            this.setHealth(f + amount);
        }
        if (amount < 0) {
            this.healthTickTimer = 0;
        }
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

    /**
     * @author TheRedBrain
     * @reason
     */
    @Overwrite
    public boolean blockedByShield(DamageSource source) {
        return false;
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
                Predicate<ItemStack> predicate = stack -> stack.isIn(Tags.GRANTS_FEATHER_FALLING_LEVEL_4);
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
        // TODO account for fallDamage/featherFalling and resistanceEffect, also protection enchantments?
        LivingEntity attacker = null;
        if (source.getAttacker() instanceof LivingEntity) {
            attacker = (LivingEntity) source.getAttacker();
        }
        if (this.isInvulnerableTo(source)) {
            return;
        }

        if (this.hasStatusEffect(StatusEffectsRegistry.STAGGERED)) {
            amount = amount * 2;
        }

        if (!source.isIn(DamageTypeTags.BYPASSES_ARMOR)) {
            this.damageArmor(source, amount);
        }

        float vanilla_amount = 0;
        if (source.isIn(Tags.IS_VANILLA)) {
            if (BetterAdventureMode.serverConfig.show_debug_log) {
                BetterAdventureMode.info("This vanilla damage type was used: " + source.getType().toString());
            }
            vanilla_amount = amount;
        }

        float true_amount = 0;
        if (source.isIn(Tags.IS_TRUE_DAMAGE)) {
            true_amount = amount;
        }

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
        } else if (!source.isIn(Tags.IS_TRUE_DAMAGE)) {
            this.stopUsingItem();
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


        float applied_damage = piercing_amount + bashing_amount + slashing_amount + vanilla_amount;

        // taking damage interrupts eating food, drinking potions, etc
        if (applied_damage > 0.0f && !this.isBlocking()) {
            this.stopUsingItem();
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
            if (this.healthTickTimer >= this.betteradventuremode$getHealthTickThreshold()) {
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

            if (this.staminaTickTimer >= this.betteradventuremode$getStaminaTickThreshold() && staminaRegenerationDelayTimer >= this.betteradventuremode$getStaminaRegenerationDelayThreshold()) {
                if (this.betteradventuremode$getStamina() < this.betteradventuremode$getMaxStamina()) {
                    this.betteradventuremode$addStamina(this.betteradventuremode$getRegeneratedStamina());
                } else if (this.betteradventuremode$getStamina() > this.betteradventuremode$getMaxStamina()) {
                    this.betteradventuremode$setStamina(this.betteradventuremode$getMaxStamina());
                }
                this.staminaTickTimer = 0;
            }

            if (this.manaTickTimer >= this.betteradventuremode$getManaTickThreshold()) {
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

    @Override
    public boolean doesRenderOnFire() {
        return super.doesRenderOnFire() || (this.hasStatusEffect(StatusEffectsRegistry.BURNING) && !this.isSpectator());
    }

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
    public int betteradventuremode$getStaminaRegenerationDelayThreshold() {
        return 60;
    }

    @Override
    public int betteradventuremode$getHealthTickThreshold() {
        return 20;
    }

    @Override
    public int betteradventuremode$getStaminaTickThreshold() {
        return 20;
    }

    @Override
    public int betteradventuremode$getManaTickThreshold() {
        return 20;
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
        if (!(this.betteradventuremode$getMaxBleedingBuildUp() == -1.0f || this.hasStatusEffect(StatusEffectsRegistry.BLEEDING))) {
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
        return BetterAdventureMode.gamePlayBalanceConfig.default_max_bleeding_build_up;
    }

    @Override
    public int betteradventuremode$getBleedingDuration() {
        return BetterAdventureMode.gamePlayBalanceConfig.default_bleeding_duration;
    }

    @Override
    public int betteradventuremode$getBleedingTickThreshold() {
        return BetterAdventureMode.gamePlayBalanceConfig.default_bleeding_tick_threshold;
    }

    @Override
    public int betteradventuremode$getBleedingBuildUpReduction() {
        return BetterAdventureMode.gamePlayBalanceConfig.default_bleeding_build_up_reduction;
    }
    // endregion bleeding build up

    // region burn build up
    @Override
    public void betteradventuremode$addBurnBuildUp(float amount) {
        if (!(this.betteradventuremode$getMaxBurnBuildUp() == -1.0f)) {
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
        return BetterAdventureMode.gamePlayBalanceConfig.default_max_burning_build_up;
    }

    @Override
    public int betteradventuremode$getBurnDuration() {
        return BetterAdventureMode.gamePlayBalanceConfig.default_burning_duration;
    }

    @Override
    public int betteradventuremode$getBurnTickThreshold() {
        return BetterAdventureMode.gamePlayBalanceConfig.default_burning_tick_threshold;
    }

    @Override
    public int betteradventuremode$getBurnBuildUpReduction() {
        return BetterAdventureMode.gamePlayBalanceConfig.default_burning_build_up_reduction;
    }
    // endregion burn build up

    // region freeze build up
    @Override
    public void betteradventuremode$addFreezeBuildUp(float amount) {
        if (!(this.betteradventuremode$getMaxFreezeBuildUp() == -1.0f || this.hasStatusEffect(StatusEffectsRegistry.FROZEN))) {
            float f = this.betteradventuremode$getFreezeBuildUp();
            this.betteradventuremode$setFreezeBuildUp(f + amount);
            if (amount > 0) {
                this.freezeTickTimer = this.betteradventuremode$getFreezeTickThreshold();
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
        return BetterAdventureMode.gamePlayBalanceConfig.default_max_freeze_build_up;
    }

    @Override
    public int betteradventuremode$getFreezeDuration() {
        return BetterAdventureMode.gamePlayBalanceConfig.default_freeze_duration;
    }

    @Override
    public int betteradventuremode$getFreezeTickThreshold() {
        return BetterAdventureMode.gamePlayBalanceConfig.default_freeze_tick_threshold;
    }

    @Override
    public int betteradventuremode$getFreezeBuildUpReduction() {
        return BetterAdventureMode.gamePlayBalanceConfig.default_freeze_build_up_reduction;
    }
    // endregion freeze build up

    // region stagger build up
    @Override
    public void betteradventuremode$addStaggerBuildUp(float amount) {
        if (!(this.betteradventuremode$getMaxStaggerBuildUp() == -1.0f || this.hasStatusEffect(StatusEffectsRegistry.STAGGERED))) {
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
//        return this.getMaxHealth() * 0.5f;
        return BetterAdventureMode.gamePlayBalanceConfig.default_max_stagger_build_up;
    }

    @Override
    public int betteradventuremode$getStaggerDuration() {
        return BetterAdventureMode.gamePlayBalanceConfig.default_stagger_duration;
    }

    @Override
    public int betteradventuremode$getStaggerTickThreshold() {
        return BetterAdventureMode.gamePlayBalanceConfig.default_stagger_tick_threshold;
    }

    @Override
    public int betteradventuremode$getStaggerBuildUpReduction() {
        return BetterAdventureMode.gamePlayBalanceConfig.default_stagger_build_up_reduction;
    }
    // endregion stagger build up

    // region poison build up
    @Override
    public void betteradventuremode$addPoisonBuildUp(float amount) {
        if (!(this.betteradventuremode$getMaxPoisonBuildUp() == -1.0f)) {
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
        return BetterAdventureMode.gamePlayBalanceConfig.default_max_poison_build_up;
    }

    @Override
    public int betteradventuremode$getPoisonDuration() {
        return BetterAdventureMode.gamePlayBalanceConfig.default_poison_duration;
    }

    @Override
    public int betteradventuremode$getPoisonTickThreshold() {
        return BetterAdventureMode.gamePlayBalanceConfig.default_poison_tick_threshold;
    }

    @Override
    public int betteradventuremode$getPoisonBuildUpReduction() {
        return BetterAdventureMode.gamePlayBalanceConfig.default_poison_build_up_reduction;
    }
    // endregion poison build up

    // region shock build up
    @Override
    public void betteradventuremode$addShockBuildUp(float amount) {
        if (!(this.betteradventuremode$getMaxShockBuildUp() == -1.0f)) {
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
        return BetterAdventureMode.gamePlayBalanceConfig.default_max_shock_build_up;
    }

    @Override
    public int betteradventuremode$getShockDuration() {
        return 1;
    }

    @Override
    public int betteradventuremode$getShockTickThreshold() {
        return BetterAdventureMode.gamePlayBalanceConfig.default_shock_tick_threshold;
    }

    @Override
    public int betteradventuremode$getShockBuildUpReduction() {
        return BetterAdventureMode.gamePlayBalanceConfig.default_shock_build_up_reduction;
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
}
