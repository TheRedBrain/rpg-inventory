package com.github.theredbrain.rpgmod.entity.player;

import com.github.theredbrain.rpgmod.screen.AdventureInventoryScreenHandler;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;

public interface DuckPlayerEntityMixin {

    boolean canConsumeItem(ItemStack itemStack);
    boolean tryEatAdventureFood(StatusEffectInstance statusEffectInstance);

    float bam$getHealthRegeneration();

    float bam$getManaRegeneration();
    float bam$getMaxMana();
    void bam$addMana(float amount);
    float bam$getMana();
    void bam$setMana(float mana);

    float bam$getStaminaRegeneration();
    float bam$getMaxStamina();
    void bam$addStamina(float amount);
    float bam$getStamina();
    void bam$setStamina(float mana);

    boolean isInAdventureBuildingMode();
    void setInAdventureBuildingMode(boolean bl);

    AdventureInventoryScreenHandler getAdventureInventoryScreenHandler();

    boolean isAdventure();

//    boolean isAlternativeMainHandUsed();
//    void setAlternativeMainHandUsed(boolean bl);
//    boolean isAlternativeOffHandUsed();
//    void setAlternativeOffHandUsed(boolean bl);
}
