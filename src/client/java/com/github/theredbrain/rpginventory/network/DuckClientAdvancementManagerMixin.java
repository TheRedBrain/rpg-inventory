package com.github.theredbrain.rpginventory.network;

import net.minecraft.advancement.AdvancementEntry;

public interface DuckClientAdvancementManagerMixin {
	boolean rpginventory$getAdvancementProgressDone(AdvancementEntry advancementEntry);
}
