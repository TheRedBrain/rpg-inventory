package com.github.theredbrain.betteradventuremode.mixin.entity.player;

import com.github.theredbrain.betteradventuremode.entity.player.DuckHungerManagerMixin;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.registry.StatusEffectsRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = HungerManager.class, priority = 950)
public class HungerManagerMixin implements DuckHungerManagerMixin {
    @Unique
    private int healthTickTimer = 0;
    @Unique
    private int staminaTickTimer = 0;
    @Unique
    private int manaTickTimer = 0;
    @Unique
    private int staminaRegenerationDelayTimer = 0;
    @Unique
    private boolean delayStaminaRegeneration = false;
    @Unique
    private double prevPosX = 0.0;
    @Unique
    private double prevPosY = 0.0;
    @Unique
    private double prevPosZ = 0.0;
    @Unique
    private double encumbrance = 0.0;

    // TODO config update: all attribute modifiers are (server) config driven -> much easier balancing
    /**
     *
     * @author TheRedBrain
     * @reason hunger and health mechanics are completely overhauled
     */
    @Overwrite
    public void update(PlayerEntity player) {
        this.encumbrance = (double) ((DuckPlayerEntityMixin)player).betteradventuremode$getEquipmentWeight() / Math.max(1, ((DuckPlayerEntityMixin)player).betteradventuremode$getMaxEquipmentWeight());
        boolean isMoving = this.prevPosX != player.getX() || this.prevPosY != player.getY() || this.prevPosZ != player.getZ();
        boolean isBlocking = player.isBlocking();
        boolean isSprinting = player.isSprinting();
        boolean isInCivilization = player.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT);
        this.prevPosX = player.getX();
        this.prevPosY = player.getY();
        this.prevPosZ = player.getZ();
        this.healthTickTimer++;
        this.staminaTickTimer++;
        this.manaTickTimer++;

        if (betteradventuremode$isOverBurdened() && !player.hasStatusEffect(StatusEffectsRegistry.OVERBURDENED_EFFECT)) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.OVERBURDENED_EFFECT, -1, 0, false, false, true));
        } else if (!betteradventuremode$isOverBurdened() && player.hasStatusEffect(StatusEffectsRegistry.OVERBURDENED_EFFECT)) {
            player.removeStatusEffect(StatusEffectsRegistry.OVERBURDENED_EFFECT);
        }

        int healthTickThreshold = 20;
        if (this.healthTickTimer >= healthTickThreshold) {
            if (player.getHealth() < player.getMaxHealth()) {
                float passiveRegeneration = ((DuckPlayerEntityMixin)player).betteradventuremode$getHealthRegeneration();
                double civilisationEffectHealthMultiplier = isInCivilization ? 5 : 1;
                double carryWeightHealthMultiplier = betteradventuremode$isOverBurdened() ? 0.75 : 1;
                // regenerate health
                player.heal((float) (passiveRegeneration * carryWeightHealthMultiplier * civilisationEffectHealthMultiplier));
            } else if (player.getHealth() > player.getMaxHealth()) {
                // cap health to max health
                player.setHealth(player.getMaxHealth());
            }
            this.healthTickTimer = 0;
        }

        int staminaRegenerationDelayThreshold = 60;
        if (((DuckPlayerEntityMixin)player).betteradventuremode$getStamina() <= 0 && this.delayStaminaRegeneration) {
            this.staminaRegenerationDelayTimer = 0;
            this.delayStaminaRegeneration = false;
        }
        if (((DuckPlayerEntityMixin)player).betteradventuremode$getStamina() > 0 && !this.delayStaminaRegeneration) {
            this.delayStaminaRegeneration = true;
        }
        if (this.staminaRegenerationDelayTimer <= staminaRegenerationDelayThreshold) {
            this.staminaRegenerationDelayTimer++;
        }

        int staminaTickThreshold = 20;
        if (this.staminaTickTimer >= staminaTickThreshold && staminaRegenerationDelayTimer >= staminaRegenerationDelayThreshold) {
            if (((DuckPlayerEntityMixin)player).betteradventuremode$getStamina() < ((DuckPlayerEntityMixin)player).betteradventuremode$getMaxStamina()) {
                float passiveRegeneration = ((DuckPlayerEntityMixin)player).betteradventuremode$getStaminaRegeneration();
                double isMovingStaminaRegenerationMultiplier = isSprinting ? 0 : isMoving ? 0.5 : 1;
                double isBlockingStaminaRegenerationMultiplier = isBlocking ? 0.5 : 1;
                double equipmentWeightStaminaRegenerationMultiplier = this.encumbrance <= 0.5 ? 1 : this.encumbrance <= 1 ? 0.75 : 0.5;
                double civilisationEffectStaminaRegenerationMultiplier = isInCivilization ? 5 : 1;
                // regenerate stamina
                ((DuckPlayerEntityMixin)player).betteradventuremode$addStamina((float) (passiveRegeneration * isMovingStaminaRegenerationMultiplier * isBlockingStaminaRegenerationMultiplier * equipmentWeightStaminaRegenerationMultiplier * civilisationEffectStaminaRegenerationMultiplier));
            } else if (((DuckPlayerEntityMixin)player).betteradventuremode$getStamina() > ((DuckPlayerEntityMixin)player).betteradventuremode$getMaxStamina()) {
                // cap stamina to max stamina
                ((DuckPlayerEntityMixin)player).betteradventuremode$setStamina(((DuckPlayerEntityMixin)player).betteradventuremode$getMaxStamina());
            }
            // TODO regenerate poise
            this.staminaTickTimer = 0;
        }

        int manaTickThreshold = 20;
        if (this.manaTickTimer >= manaTickThreshold) {
            if (((DuckPlayerEntityMixin)player).betteradventuremode$getMana() < ((DuckPlayerEntityMixin)player).betteradventuremode$getMaxMana()) {
                float passiveRegeneration = ((DuckPlayerEntityMixin)player).betteradventuremode$getManaRegeneration();
                double civilisationEffectManaRegenerationMultiplier = isInCivilization ? 10 : 1;
                double manaRegenerationEffectMultiplier = player.hasStatusEffect(StatusEffectsRegistry.MANA_REGENERATION_EFFECT) ? (player.getStatusEffect(StatusEffectsRegistry.MANA_REGENERATION_EFFECT).getAmplifier() > 0 ? 1 : 0.5) : 0.5;
                // regenerate mana
                if (player.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || player.hasStatusEffect(StatusEffectsRegistry.MANA_REGENERATION_EFFECT)) {
                    ((DuckPlayerEntityMixin) player).betteradventuremode$addMana((float) (passiveRegeneration * civilisationEffectManaRegenerationMultiplier * manaRegenerationEffectMultiplier));
                }
            } else if (((DuckPlayerEntityMixin)player).betteradventuremode$getMana() > ((DuckPlayerEntityMixin)player).betteradventuremode$getMaxMana()) {
                // cap mana to max mana
                ((DuckPlayerEntityMixin)player).betteradventuremode$setMana(((DuckPlayerEntityMixin)player).betteradventuremode$getMaxMana());
            }
            this.manaTickTimer = 0;
        }

    }

    /**
     *
     * @author TheRedBrain
     * @reason hunger and health mechanics are completely overhauled
     */
    @Overwrite
    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("healthTickTimer", NbtElement.NUMBER_TYPE)) {
            this.healthTickTimer = nbt.getInt("healthTickTimer");
        }
        if (nbt.contains("staminaTickTimer", NbtElement.NUMBER_TYPE)) {
            this.staminaTickTimer = nbt.getInt("staminaTickTimer");
        }
        if (nbt.contains("manaTickTimer", NbtElement.NUMBER_TYPE)) {
            this.manaTickTimer = nbt.getInt("manaTickTimer");
        }
        if (nbt.contains("staminaRegenerationDelayTimer", NbtElement.NUMBER_TYPE)) {
            this.staminaRegenerationDelayTimer = nbt.getInt("staminaRegenerationDelayTimer");
        }
        if (nbt.contains("delayStaminaRegeneration", NbtElement.BYTE_TYPE)) {
            this.delayStaminaRegeneration = nbt.getBoolean("delayStaminaRegeneration");
        }
        if (nbt.contains("prevPosX", NbtElement.NUMBER_TYPE)) {
            this.prevPosX = nbt.getDouble("prevPosX");
        }
        if (nbt.contains("prevPosY", NbtElement.NUMBER_TYPE)) {
            this.prevPosY = nbt.getDouble("prevPosY");
        }
        if (nbt.contains("prevPosZ", NbtElement.NUMBER_TYPE)) {
            this.prevPosZ = nbt.getDouble("prevPosZ");
        }
        if (nbt.contains("encumbrance", NbtElement.NUMBER_TYPE)) {
            this.encumbrance = nbt.getDouble("encumbrance");
        }
    }

    /**
     *
     * @author TheRedBrain
     * @reason hunger and health mechanics are completely overhauled
     */
    @Overwrite
    public void writeNbt(NbtCompound nbt) {
        nbt.putInt("healthTickTimer", this.healthTickTimer);
        nbt.putInt("staminaTickTimer", this.staminaTickTimer);
        nbt.putInt("manaTickTimer", this.manaTickTimer);
        nbt.putInt("staminaRegenerationDelayTimer", this.staminaRegenerationDelayTimer);
        nbt.putBoolean("delayStaminaRegeneration", this.delayStaminaRegeneration);
        nbt.putDouble("prevPosX", this.prevPosX);
        nbt.putDouble("prevPosY", this.prevPosY);
        nbt.putDouble("prevPosZ", this.prevPosZ);
        nbt.putDouble("encumbrance", this.encumbrance);
    }

    public int betteradventuremode$getHealthTickTimer() {
        return healthTickTimer;
    }

    public void betteradventuremode$setHealthTickTimer(int healthTickTimer) {
        this.healthTickTimer = healthTickTimer;
    }

    public int betteradventuremode$getManaTickTimer() {
        return manaTickTimer;
    }

    public void betteradventuremode$setManaTickTimer(int manaTickTimer) {
        this.manaTickTimer = manaTickTimer;
    }

    public int betteradventuremode$getStaminaTickTimer() {
        return staminaTickTimer;
    }

    public void betteradventuremode$setStaminaTickTimer(int staminaTickTimer) {
        this.staminaTickTimer = staminaTickTimer;
    }

    public int betteradventuremode$getStaminaRegenerationDelayTimer() {
        return staminaRegenerationDelayTimer;
    }

    public void betteradventuremode$setStaminaRegenerationDelayTimer(int staminaRegenerationDelayTimer) {
        this.staminaRegenerationDelayTimer = staminaRegenerationDelayTimer;
    }

    public boolean betteradventuremode$isOverBurdened() {
        return this.encumbrance > 1;
    }

    public double betteradventuremode$getEncumbrance() {
        return encumbrance;
    }
}
