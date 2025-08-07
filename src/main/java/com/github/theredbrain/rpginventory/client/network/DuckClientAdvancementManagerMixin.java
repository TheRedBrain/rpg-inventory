package com.github.theredbrain.rpginventory.client.network;

import net.minecraft.advancement.AdvancementEntry;

public interface DuckClientAdvancementManagerMixin {
	boolean scriptblocks$getAdvancementProgressDone(AdvancementEntry advancementEntry);
}
