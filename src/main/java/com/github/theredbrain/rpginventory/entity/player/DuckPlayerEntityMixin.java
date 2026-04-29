package com.github.theredbrain.rpginventory.entity.player;

public interface DuckPlayerEntityMixin {

	float rpginventory$getActiveSpellSlotAmount();

	boolean rpginventory$isHandSlotOverhaulActive();

	void rpginventory$setIsHandSlotOverhaulActive(boolean isHandSlotOverhaulActive);

	boolean rpginventory$areAlternativeHandSlotsActive();

	void rpginventory$setAreAlternativeHandSlotsActive(boolean areAlternativeHandSlotsActive);

	int rpginventory$oldActiveSpellSlotAmount();

	void rpginventory$setOldActiveSpellSlotAmount(int oldActiveSpellSlotAmount);

	boolean rpginventory$shouldEjectExclusiveEquipment();

	void rpginventory$setShouldEjectExclusiveEquipment(boolean shouldEjectExclusiveEquipment);

	boolean rpginventory$isAdventureHotbarCleanedUp();

	void rpginventory$setIsAdventureHotbarCleanedUp(boolean isAdventureHotbarCleanedUp);
}
