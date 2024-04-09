package com.github.theredbrain.betteradventuremode.entity.player;

import com.github.theredbrain.betteradventuremode.data.Dialogue;
import com.github.theredbrain.betteradventuremode.block.entity.*;
import com.github.theredbrain.betteradventuremode.inventory.AdditionalPlayerInventory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public interface DuckPlayerEntityMixin {

    boolean betteradventuremode$canConsumeItem(ItemStack itemStack);
    boolean betteradventuremode$tryEatAdventureFood(StatusEffectInstance statusEffectInstance);

    float betteradventuremode$getMaxEquipmentWeight();
    float betteradventuremode$getEquipmentWeight();
    float betteradventuremode$getMaxFoodEffects();
    int betteradventuremode$getBackpackCapacity();

    boolean betteradventuremode$isAdventure();


    boolean betteradventuremode$isMainHandStackSheathed();
    void betteradventuremode$setIsMainHandStackSheathed(boolean isMainHandStackSheathed);
    boolean betteradventuremode$isOffHandStackSheathed();
    void betteradventuremode$setIsOffHandStackSheathed(boolean isOffHandStackSheathed);
    boolean betteradventuremode$useStashForCrafting();
    void betteradventuremode$setUseStashForCrafting(boolean useStashForCrafting);
    int betteradventuremode$oldActiveSpellSlotAmount();
    void betteradventuremode$setOldActiveSpellSlotAmount(int oldActiveSpellSlotAmount);
    int betteradventuremode$oldBackpackCapacity();
    void betteradventuremode$setOldBackpackCapacity(int backpack_capacity);
    AdditionalPlayerInventory betteradventuremode$getStashInventory();
    void betteradventuremode$setStashInventory(AdditionalPlayerInventory stashInventory);
    AdditionalPlayerInventory betteradventuremode$getBackpackInventory();
    void betteradventuremode$setBackpackInventory(AdditionalPlayerInventory backpackInventory);

    void betteradventuremode$sendAnnouncement(Text announcement);

    void betteradventuremode$openHousingScreen();
    void betteradventuremode$openJigsawPlacerBlockScreen(JigsawPlacerBlockEntity jigsawPlacerBlock);
    void betteradventuremode$openRedstoneTriggerBlockScreen(RedstoneTriggerBlockEntity redstoneTriggerBlock);
    void betteradventuremode$openRelayTriggerBlockScreen(RelayTriggerBlockEntity relayTriggerBlock);
    void betteradventuremode$openTriggeredCounterBlockScreen(TriggeredCounterBlockEntity triggeredCounterBlock);
    void betteradventuremode$openResetTriggerBlockScreen(ResetTriggerBlockEntity resetTriggerBlock);
    void betteradventuremode$openDelayTriggerBlockScreen(DelayTriggerBlockEntity delayTriggerBlock);
    void betteradventuremode$openUseRelayBlockScreen(UseRelayBlockEntity useRelayBlock);
    void betteradventuremode$openTriggeredSpawnerBlockScreen(TriggeredSpawnerBlockEntity triggeredSpawnerBlock);
    void betteradventuremode$openMimicBlockScreen(MimicBlockEntity mimicBlock);
    void betteradventuremode$openLocationControlBlockScreen(LocationControlBlockEntity locationControlBlock);
    void betteradventuremode$openDialogueScreen(DialogueBlockEntity dialogueBlockEntity, @Nullable Dialogue dialogue);
    void betteradventuremode$openEntranceDelegationBlockScreen(EntranceDelegationBlockEntity entranceDelegationBlockEntity);
    void betteradventuremode$openAreaBlockScreen(AreaBlockEntity areaBlockEntity);
    void betteradventuremode$openTriggeredAdvancementCheckerBlockScreen(TriggeredAdvancementCheckerBlockEntity triggeredAdvancementCheckerBlock);
    void betteradventuremode$openInteractiveLootBlockScreen(InteractiveLootBlockEntity interactiveLootBlockEntity);
}
