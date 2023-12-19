package com.github.theredbrain.bamcore.client.network;

import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementProgress;

public interface DuckClientAdvancementManagerMixin {
    AdvancementProgress bamcore$getAdvancementProgress(AdvancementEntry advancementEntry);
}
