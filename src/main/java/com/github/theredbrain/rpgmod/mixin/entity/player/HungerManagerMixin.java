package com.github.theredbrain.rpgmod.mixin.entity.player;

import com.github.theredbrain.rpgmod.RPGMod;
import com.github.theredbrain.rpgmod.entity.player.DuckPlayerEntityMixin;
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
    /*
    @Unique
    private double prevPosX = 0.0;
    @Unique
    private double prevPosY = 0.0;
    @Unique
    private double prevPosZ = 0.0;
    */

    /**
     *
     * @author TheRedBrain
     * @reason hunger and health mechanics are completely overhauled
     */
    @Overwrite
    public void update(PlayerEntity player) {
        /*
        TODO attribute pool regeneration is affected by
            is player moving
            status effects
        boolean isMoving = this.prevPosX != player.getX() || this.prevPosY != player.getY() || this.prevPosZ != player.getZ();
        this.prevPosX = player.getX();
        this.prevPosY = player.getY();
        this.prevPosZ = player.getZ();
        */
        this.healthTickTimer++;
        this.staminaTickTimer++;
        this.manaTickTimer++;
        if (this.healthTickTimer >= 20) {
            if (player.getHealth() < player.getMaxHealth()) {
                player.heal(((DuckPlayerEntityMixin)player).bam$getHealthRegeneration());
            } else if (player.getHealth() > player.getMaxHealth()) {
                player.setHealth(player.getMaxHealth());
            }
            this.healthTickTimer = 0;
        }
        if (this.staminaTickTimer >= 20) {
            if (((DuckPlayerEntityMixin)player).bam$getStamina() < ((DuckPlayerEntityMixin)player).bam$getMaxStamina()) {
                ((DuckPlayerEntityMixin)player).bam$addStamina(((DuckPlayerEntityMixin)player).bam$getStaminaRegeneration());
            } else if (((DuckPlayerEntityMixin)player).bam$getStamina() > ((DuckPlayerEntityMixin)player).bam$getMaxStamina()) {
                ((DuckPlayerEntityMixin)player).bam$setStamina(((DuckPlayerEntityMixin)player).bam$getMaxStamina());
            }
            this.staminaTickTimer = 0;
        }
        if (this.manaTickTimer >= 20) {
            if (((DuckPlayerEntityMixin)player).bam$getMana() < ((DuckPlayerEntityMixin)player).bam$getMaxMana()) {
                ((DuckPlayerEntityMixin)player).bam$addMana(((DuckPlayerEntityMixin)player).bam$getManaRegeneration());
            } else if (((DuckPlayerEntityMixin)player).bam$getMana() > ((DuckPlayerEntityMixin)player).bam$getMaxMana()) {
                ((DuckPlayerEntityMixin)player).bam$setMana(((DuckPlayerEntityMixin)player).bam$getMaxMana());
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
        nbt.putFloat("manaTickTimer", this.manaTickTimer);
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
}
