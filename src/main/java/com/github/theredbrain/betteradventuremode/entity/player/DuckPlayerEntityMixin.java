package com.github.theredbrain.betteradventuremode.entity.player;

import com.github.theredbrain.betteradventuremode.api.json_files_backend.Dialogue;
import com.github.theredbrain.betteradventuremode.block.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

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

    void bamcore$openHousingScreen();
    void bamcore$openJigsawPlacerBlockScreen(JigsawPlacerBlockBlockEntity jigsawPlacerBlock);
    void bamcore$openRedstoneTriggerBlockScreen(RedstoneTriggerBlockBlockEntity redstoneTriggerBlock);
    void bamcore$openRelayTriggerBlockScreen(RelayTriggerBlockBlockEntity relayTriggerBlock);
    void bamcore$openTriggeredCounterBlockScreen(TriggeredCounterBlockEntity triggeredCounterBlock);
    void bamcore$openResetTriggerBlockScreen(ResetTriggerBlockEntity resetTriggerBlock);
    void bamcore$openDelayTriggerBlockScreen(DelayTriggerBlockBlockEntity delayTriggerBlock);
    void bamcore$openUseRelayBlockScreen(UseRelayBlockEntity useRelayBlock);
    void bamcore$openTriggeredSpawnerBlockScreen(TriggeredSpawnerBlockEntity triggeredSpawnerBlock);
    void bamcore$openMimicBlockScreen(MimicBlockEntity mimicBlock);
    void bamcore$openLocationControlBlockScreen(LocationControlBlockEntity locationControlBlock);
    void bamcore$openDialogueScreen(DialogueBlockEntity dialogueBlockEntity, @Nullable Dialogue dialogue);
}
