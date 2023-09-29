package com.github.theredbrain.bamcore.mixin.client.network;

import com.github.theredbrain.bamcore.block.entity.*;
import com.github.theredbrain.bamcore.client.gui.screen.ingame.*;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin implements DuckPlayerEntityMixin {

    @Shadow @Final protected MinecraftClient client;

    @Override
    public void bamcore$openStructurePlacerBlockScreen(StructurePlacerBlockBlockEntity structurePlacerBlock) {
        this.client.setScreen(new StructurePlacerBlockScreen(structurePlacerBlock));
    }

    @Override
    public void bamcore$openAreaFillerBlockScreen(AreaFillerBlockBlockEntity areaFillerBlock) {
        this.client.setScreen(new AreaFillerBlockScreen(areaFillerBlock));
    }

    @Override
    public void bamcore$openRedstoneTriggerBlockScreen(RedstoneTriggerBlockBlockEntity redstoneTriggerBlock) {
        this.client.setScreen(new RedstoneTriggerBlockScreen(redstoneTriggerBlock));
    }

    @Override
    public void bamcore$openRelayTriggerBlockScreen(RelayTriggerBlockBlockEntity relayTriggerBlock) {
        this.client.setScreen(new RelayTriggerBlockScreen(relayTriggerBlock));
    }

    @Override
    public void bamcore$openDelayTriggerBlockScreen(DelayTriggerBlockBlockEntity delayTriggerBlock) {
        this.client.setScreen(new DelayTriggerBlockScreen(delayTriggerBlock));
    }

    public void bamcore$openChunkLoaderBlockScreen(ChunkLoaderBlockBlockEntity chunkLoaderBlock) {
        this.client.setScreen(new ChunkLoaderBlockScreen(chunkLoaderBlock));
    }
}
