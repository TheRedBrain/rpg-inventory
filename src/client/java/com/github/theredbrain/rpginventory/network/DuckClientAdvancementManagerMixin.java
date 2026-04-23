package com.github.theredbrain.rpginventory.network;

import net.minecraft.advancements.AdvancementHolder;

public interface DuckClientAdvancementManagerMixin {
	boolean rpginventory$getAdvancementProgressDone(AdvancementHolder advancementEntry);
}
