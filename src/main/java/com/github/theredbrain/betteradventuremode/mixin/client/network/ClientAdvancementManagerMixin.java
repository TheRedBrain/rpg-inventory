package com.github.theredbrain.betteradventuremode.mixin.client.network;

import com.github.theredbrain.betteradventuremode.client.network.DuckClientAdvancementManagerMixin;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.client.network.ClientAdvancementManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(ClientAdvancementManager.class)
public class ClientAdvancementManagerMixin implements DuckClientAdvancementManagerMixin {
    @Shadow @Final private Map<AdvancementEntry, AdvancementProgress> advancementProgresses;

    @Override
    public AdvancementProgress bamcore$getAdvancementProgress(AdvancementEntry advancementEntry) {
        return this.advancementProgresses.get(advancementEntry);
    }
}
