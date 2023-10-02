package com.github.theredbrain.bamcore.mixin.entity.player;

import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.api.util.BetterAdventureModeCoreStatusEffects;
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
        boolean isInCivilization = player.hasStatusEffect(BetterAdventureModeCoreStatusEffects.CIVILISATION_EFFECT);
        this.prevPosX = player.getX();
        this.prevPosY = player.getY();
        this.prevPosZ = player.getZ();
        this.healthTickTimer++;
        this.staminaTickTimer++;
        this.manaTickTimer++;

        if (isOverBurdened() && !player.hasStatusEffect(BetterAdventureModeCoreStatusEffects.OVERBURDENED_EFFECT)) {
            player.addStatusEffect(new StatusEffectInstance(BetterAdventureModeCoreStatusEffects.OVERBURDENED_EFFECT, -1, 0, false, false, true));
        } else if (!isOverBurdened() && player.hasStatusEffect(BetterAdventureModeCoreStatusEffects.OVERBURDENED_EFFECT)) {
            player.removeStatusEffect(BetterAdventureModeCoreStatusEffects.OVERBURDENED_EFFECT);
        }

        int healthTickThreshold = 20;
        if (this.healthTickTimer >= healthTickThreshold) {
            if (player.getHealth() < player.getMaxHealth()) {
                float passiveRegeneration = ((DuckPlayerEntityMixin)player).bamcore$getHealthRegeneration();
                double civilisationEffectHealthMultiplier = isInCivilization ? 5 : 1;
                double carryWeightHealthMultiplier = isOverBurdened() ? 0.75 : 1;
                // regenerate health
                player.heal((float) (passiveRegeneration * carryWeightHealthMultiplier * civilisationEffectHealthMultiplier));
            } else if (player.getHealth() > player.getMaxHealth()) {
                // cap health to max health
                player.setHealth(player.getMaxHealth());
            }
            this.healthTickTimer = 0;
        }

        int staminaTickThreshold = 20;
        if (this.staminaTickTimer >= staminaTickThreshold) {
            if (((DuckPlayerEntityMixin)player).bamcore$getStamina() < ((DuckPlayerEntityMixin)player).bamcore$getMaxStamina()) {
                float passiveRegeneration = ((DuckPlayerEntityMixin)player).bamcore$getStaminaRegeneration();
                double isMovingStaminaRegenerationMultiplier = isSprinting ? 0 : isMoving ? 0.5 : 1;
                double isBlockingStaminaRegenerationMultiplier = isBlocking ? 0.5 : 1;
                double equipmentWeightStaminaRegenerationMultiplier = this.encumbrance <= 0.5 ? 1 : this.encumbrance <= 1 ? 0.75 : 0.5;
                double civilisationEffectStaminaRegenerationMultiplier = isInCivilization ? 5 : 1;
                // regenerate stamina
                ((DuckPlayerEntityMixin)player).bamcore$addStamina((float) (passiveRegeneration * isMovingStaminaRegenerationMultiplier * isBlockingStaminaRegenerationMultiplier * equipmentWeightStaminaRegenerationMultiplier * civilisationEffectStaminaRegenerationMultiplier));
            } else if (((DuckPlayerEntityMixin)player).bamcore$getStamina() > ((DuckPlayerEntityMixin)player).bamcore$getMaxStamina()) {
                // cap stamina to max stamina
                ((DuckPlayerEntityMixin)player).bamcore$setStamina(((DuckPlayerEntityMixin)player).bamcore$getMaxStamina());
            }
            // TODO regenerate poise
            this.staminaTickTimer = 0;
        }

        int manaTickThreshold = 20;
        if (this.manaTickTimer >= manaTickThreshold) {
            if (((DuckPlayerEntityMixin)player).bamcore$getMana() < ((DuckPlayerEntityMixin)player).bamcore$getMaxMana()) {
                float passiveRegeneration = ((DuckPlayerEntityMixin)player).bamcore$getManaRegeneration();
                double civilisationEffectManaRegenerationMultiplier = isInCivilization ? 10 : 1;
                double manaCatalystEffectManaRegenerationMultiplier = player.hasStatusEffect(BetterAdventureModeCoreStatusEffects.MANA_REGENERATION_EFFECT) ? (player.getStatusEffect(BetterAdventureModeCoreStatusEffects.MANA_REGENERATION_EFFECT).getAmplifier() > 0 ? 1 : 0.5) : 1;
                // regenerate mana
                if (player.hasStatusEffect(BetterAdventureModeCoreStatusEffects.CIVILISATION_EFFECT) || player.hasStatusEffect(BetterAdventureModeCoreStatusEffects.MANA_REGENERATION_EFFECT)) {
                    ((DuckPlayerEntityMixin) player).bamcore$addMana((float) (passiveRegeneration + civilisationEffectManaRegenerationMultiplier));
                }
            } else if (((DuckPlayerEntityMixin)player).bamcore$getMana() > ((DuckPlayerEntityMixin)player).bamcore$getMaxMana()) {
                // cap mana to max mana
                ((DuckPlayerEntityMixin)player).bamcore$setMana(((DuckPlayerEntityMixin)player).bamcore$getMaxMana());
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
        return this.encumbrance > 1;
    }

    public double getEncumbrance() {
        return encumbrance;
    }
}
