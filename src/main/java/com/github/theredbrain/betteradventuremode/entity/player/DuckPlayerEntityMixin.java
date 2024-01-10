package com.github.theredbrain.betteradventuremode.entity.player;

import com.github.theredbrain.betteradventuremode.api.json_files_backend.Dialogue;
import com.github.theredbrain.betteradventuremode.block.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface DuckPlayerEntityMixin {

    boolean betteradventuremode$canConsumeItem(ItemStack itemStack);
    boolean betteradventuremode$tryEatAdventureFood(StatusEffectInstance statusEffectInstance);

    float betteradventuremode$getMaxEquipmentWeight();
    float betteradventuremode$getEquipmentWeight();

    float betteradventuremode$getHealthRegeneration();

    float betteradventuremode$getManaRegeneration();
    float betteradventuremode$getMaxMana();
    void betteradventuremode$addMana(float amount);
    float betteradventuremode$getMana();
    void betteradventuremode$setMana(float mana);

    float betteradventuremode$getStaminaRegeneration();
    float betteradventuremode$getMaxStamina();
    void betteradventuremode$addStamina(float amount);
    float betteradventuremode$getStamina();
    void betteradventuremode$setStamina(float mana);

    boolean betteradventuremode$isAdventure();

    void betteradventuremode$openHousingScreen();
    void betteradventuremode$openJigsawPlacerBlockScreen(JigsawPlacerBlockBlockEntity jigsawPlacerBlock);
    void betteradventuremode$openRedstoneTriggerBlockScreen(RedstoneTriggerBlockBlockEntity redstoneTriggerBlock);
    void betteradventuremode$openRelayTriggerBlockScreen(RelayTriggerBlockBlockEntity relayTriggerBlock);
    void betteradventuremode$openTriggeredCounterBlockScreen(TriggeredCounterBlockEntity triggeredCounterBlock);
    void betteradventuremode$openResetTriggerBlockScreen(ResetTriggerBlockEntity resetTriggerBlock);
    void betteradventuremode$openDelayTriggerBlockScreen(DelayTriggerBlockBlockEntity delayTriggerBlock);
    void betteradventuremode$openUseRelayBlockScreen(UseRelayBlockEntity useRelayBlock);
    void betteradventuremode$openTriggeredSpawnerBlockScreen(TriggeredSpawnerBlockEntity triggeredSpawnerBlock);
    void betteradventuremode$openMimicBlockScreen(MimicBlockEntity mimicBlock);
    void betteradventuremode$openLocationControlBlockScreen(LocationControlBlockEntity locationControlBlock);
    void betteradventuremode$openDialogueScreen(DialogueBlockEntity dialogueBlockEntity, @Nullable Dialogue dialogue);
}