package com.github.theredbrain.betteradventuremode.entity.player;

import com.github.theredbrain.betteradventuremode.data.Dialogue;
import com.github.theredbrain.betteradventuremode.block.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface DuckPlayerEntityMixin {

    boolean betteradventuremode$canConsumeItem(ItemStack itemStack);
    boolean betteradventuremode$tryEatAdventureFood(StatusEffectInstance statusEffectInstance);

    float betteradventuremode$getMaxEquipmentWeight();
    float betteradventuremode$getEquipmentWeight();

    boolean betteradventuremode$isAdventure();


    boolean betteradventuremode$useStashForCrafting();
    void betteradventuremode$setUseStashForCrafting(boolean useStashForCrafting);
    SimpleInventory betteradventuremode$getStashInventory();
    void betteradventuremode$setStashInventory(SimpleInventory stashInventory);

    void betteradventuremode$openHousingScreen();
    void betteradventuremode$openJigsawPlacerBlockScreen(JigsawPlacerBlockEntity jigsawPlacerBlock);
    void betteradventuremode$openRedstoneTriggerBlockScreen(RedstoneTriggerBlockEntity redstoneTriggerBlock);
    void betteradventuremode$openRelayTriggerBlockScreen(RelayTriggerBlockEntity relayTriggerBlock);
    void betteradventuremode$openTriggeredCounterBlockScreen(TriggeredCounterBlockEntity triggeredCounterBlock);
    void betteradventuremode$openResetTriggerBlockScreen(ResetTriggerBlockEntity resetTriggerBlock);
    void betteradventuremode$openDelayTriggerBlockScreen(DelayTriggerBlockEntity delayTriggerBlock);
    void betteradventuremode$openUseRelayBlockScreen(UseRelayBlockEntity useRelayBlock);
    void betteradventuremode$openTriggeredMessageBlockScreen(TriggeredMessageBlockEntity triggeredMessageBlock);
    void betteradventuremode$openTriggeredSpawnerBlockScreen(TriggeredSpawnerBlockEntity triggeredSpawnerBlock);
    void betteradventuremode$openMimicBlockScreen(MimicBlockEntity mimicBlock);
    void betteradventuremode$openLocationControlBlockScreen(LocationControlBlockEntity locationControlBlock);
    void betteradventuremode$openDialogueScreen(DialogueBlockEntity dialogueBlockEntity, @Nullable Dialogue dialogue);
    void betteradventuremode$openEntranceDelegationBlockScreen(EntranceDelegationBlockEntity entranceDelegationBlockEntity);
    void betteradventuremode$openStatusEffectApplierBlockScreen(StatusEffectApplierBlockEntity statusEffectApplierBlockEntity);
    void betteradventuremode$openTriggeredAdvancementCheckerBlockScreen(TriggeredAdvancementCheckerBlockEntity triggeredAdvancementCheckerBlock);
}
