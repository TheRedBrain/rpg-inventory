package com.github.theredbrain.betteradventuremode.entity.player;

public interface DuckPlayerEntityMixin {

    float betteradventuremode$getMaxEquipmentWeight();
    float betteradventuremode$getEquipmentWeight();
    float betteradventuremode$getActiveSpellSlotAmount();

    boolean betteradventuremode$isMainHandStackSheathed();
    void betteradventuremode$setIsMainHandStackSheathed(boolean isMainHandStackSheathed);
    boolean betteradventuremode$isOffHandStackSheathed();
    void betteradventuremode$setIsOffHandStackSheathed(boolean isOffHandStackSheathed);
    int betteradventuremode$oldActiveSpellSlotAmount();
    void betteradventuremode$setOldActiveSpellSlotAmount(int oldActiveSpellSlotAmount);
}
