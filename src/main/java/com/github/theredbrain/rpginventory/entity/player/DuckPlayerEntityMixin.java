package com.github.theredbrain.rpginventory.entity.player;

public interface DuckPlayerEntityMixin {
	float rpginventory$getActiveSpellSlotAmount();

	boolean rpginventory$isHandStackSheathed();

	void rpginventory$setIsHandStackSheathed(boolean isHandStackSheathed);

	boolean rpginventory$isOffhandStackSheathed();

	void rpginventory$setIsOffhandStackSheathed(boolean isOffhandStackSheathed);

	int rpginventory$oldActiveSpellSlotAmount();

	void rpginventory$setOldActiveSpellSlotAmount(int oldActiveSpellSlotAmount);
}
