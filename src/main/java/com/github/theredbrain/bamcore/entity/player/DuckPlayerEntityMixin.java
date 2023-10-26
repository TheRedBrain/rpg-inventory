package com.github.theredbrain.bamcore.entity.player;

import com.github.theredbrain.bamcore.block.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;

public interface DuckPlayerEntityMixin {

    boolean bamcore$canConsumeItem(ItemStack itemStack);
    boolean bamcore$tryEatAdventureFood(StatusEffectInstance statusEffectInstance);

    float bamcore$getMaxEquipmentWeight();
    float bamcore$getEquipmentWeight();

    float bamcore$getHealthRegeneration();

    float bamcore$getManaRegeneration();
    float bamcore$getMaxMana();
    void bamcore$addMana(float amount);
    float bamcore$getMana();
    void bamcore$setMana(float mana);

    float bamcore$getStaminaRegeneration();
    float bamcore$getMaxStamina();
    void bamcore$addStamina(float amount);
    float bamcore$getStamina();
    void bamcore$setStamina(float mana);

    boolean bamcore$isAdventure();

    void bamcore$openStructurePlacerBlockScreen(StructurePlacerBlockBlockEntity structurePlacerBlock);
    void bamcore$openAreaFillerBlockScreen(AreaFillerBlockBlockEntity areaFillerBlock);
    void bamcore$openRedstoneTriggerBlockScreen(RedstoneTriggerBlockBlockEntity redstoneTriggerBlock);
    void bamcore$openRelayTriggerBlockScreen(RelayTriggerBlockBlockEntity relayTriggerBlock);
    void bamcore$openDelayTriggerBlockScreen(DelayTriggerBlockBlockEntity delayTriggerBlock);
    void bamcore$openChunkLoaderBlockScreen(ChunkLoaderBlockBlockEntity chunkLoaderBlock);
}
