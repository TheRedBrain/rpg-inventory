package com.github.theredbrain.bamcore.mixin.entity.player;

import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.registry.StatusEffectsRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = HungerManager.class, priority = 950)
public class HungerManagerMixin {
    @Unique
    private int healthTickTimer = 0;
    @Unique
    private int staminaTickTimer = 0;
    @Unique
    private int manaTickTimer = 0;
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
        this.encumbrance = (double) ((DuckPlayerEntityMixin)player).bamcore$getEquipmentWeight() / Math.max(1, ((DuckPlayerEntityMixin)player).bamcore$getMaxEquipmentWeight());
        boolean isMoving = this.prevPosX != player.getX() || this.prevPosY != player.getY() || this.prevPosZ != player.getZ();
        boolean isBlocking = player.isBlocking();
        boolean isSprinting = player.isSprinting();
        boolean isInCivilization = player.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT);
        this.prevPosX = player.getX();
        this.prevPosY = player.getY();
        this.prevPosZ = player.getZ();
        this.healthTickTimer--;
        this.staminaTickTimer--;
        this.manaTickTimer--;

        if (isOverBurdened() && !player.hasStatusEffect(StatusEffectsRegistry.OVERBURDENED_EFFECT)) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.OVERBURDENED_EFFECT, -1, 0, false, false, true));
        } else if (!isOverBurdened() && player.hasStatusEffect(StatusEffectsRegistry.OVERBURDENED_EFFECT)) {
            player.removeStatusEffect(StatusEffectsRegistry.OVERBURDENED_EFFECT);
        }

        if (this.healthTickTimer <= 0) {
            if (player.getHealth() < player.getMaxHealth()) {
                float passiveRegeneration = ((DuckPlayerEntityMixin)player).bamcore$getHealthRegeneration();
                double isMovingHealthMultiplier = isSprinting ? 0 : isMoving ? 0.5 : 1;
                double isBlockingHealthMultiplier = isBlocking ? 0 : 1;
                double civilisationEffectHealthMultiplier = isInCivilization ? 5 : 1;
                // regenerate health
                player.heal((float) (passiveRegeneration * isMovingHealthMultiplier * isBlockingHealthMultiplier * civilisationEffectHealthMultiplier));
            } else if (player.getHealth() > player.getMaxHealth()) {
                // cap health to max health
                player.setHealth(player.getMaxHealth());
            }
            this.healthTickTimer = 20;
        }
        if (this.staminaTickTimer <= 0) {
            if (((DuckPlayerEntityMixin)player).bamcore$getStamina() < ((DuckPlayerEntityMixin)player).bamcore$getMaxStamina()) {
                float passiveRegeneration = ((DuckPlayerEntityMixin)player).bamcore$getStaminaRegeneration();
                double isMovingStaminaMultiplier = isSprinting ? 0 : isMoving ? 0.5 : 1;
                double isBlockingStaminaMultiplier = isBlocking ? 0.5 : 1;
                double equipmentWeightStaminaMultiplier = this.encumbrance <= 0.5 ? 1 : this.encumbrance <= 1 ? 0.8 : 0.5;
                double civilisationEffectStaminaMultiplier = isInCivilization ? 5 : 1;
                // regenerate stamina
                ((DuckPlayerEntityMixin)player).bamcore$addStamina((float) (passiveRegeneration * isMovingStaminaMultiplier * isBlockingStaminaMultiplier * equipmentWeightStaminaMultiplier * civilisationEffectStaminaMultiplier));
            } else if (((DuckPlayerEntityMixin)player).bamcore$getStamina() > ((DuckPlayerEntityMixin)player).bamcore$getMaxStamina()
                    || player.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT)) {
                // cap stamina to max stamina
                ((DuckPlayerEntityMixin)player).bamcore$setStamina(((DuckPlayerEntityMixin)player).bamcore$getMaxStamina());
            }
            // TODO regenerate poise
            this.staminaTickTimer = 20;
        }
        if (this.manaTickTimer <= 0) {
            if (((DuckPlayerEntityMixin)player).bamcore$getMana() < ((DuckPlayerEntityMixin)player).bamcore$getMaxMana()) {
                float passiveRegeneration = ((DuckPlayerEntityMixin)player).bamcore$getManaRegeneration();
                double civilisationEffectManaRegeneration = isInCivilization ? 10 : 0;
                // regenerate mana
                ((DuckPlayerEntityMixin)player).bamcore$addMana((float) (passiveRegeneration + civilisationEffectManaRegeneration));
            } else if (((DuckPlayerEntityMixin)player).bamcore$getMana() > ((DuckPlayerEntityMixin)player).bamcore$getMaxMana()
                    || player.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT)) {
                // cap mana to max mana
                ((DuckPlayerEntityMixin)player).bamcore$setMana(((DuckPlayerEntityMixin)player).bamcore$getMaxMana());
            }
            this.manaTickTimer = 20;
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
        nbt.putDouble("prevPosX", this.prevPosX);
        nbt.putDouble("prevPosY", this.prevPosY);
        nbt.putDouble("prevPosZ", this.prevPosZ);
        nbt.putDouble("encumbrance", this.encumbrance);
    }

    public int getHealthTickTimer() {
        return healthTickTimer;
    }

    public void setHealthTickTimer(int healthTickTimer) {
        this.healthTickTimer = healthTickTimer;
    }

    public int getManaTickTimer() {
        return manaTickTimer;
    }

    public void setManaTickTimer(int manaTickTimer) {
        this.manaTickTimer = manaTickTimer;
    }

    public int getStaminaTickTimer() {
        return staminaTickTimer;
    }

    public void setStaminaTickTimer(int staminaTickTimer) {
        this.staminaTickTimer = staminaTickTimer;
    }

    public boolean isOverBurdened() {
        return this.encumbrance > 100;
    }

    public double getEncumbrance() {
        return encumbrance;
    }
}
