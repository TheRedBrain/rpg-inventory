package com.github.theredbrain.bamcore.registry;

import com.github.theredbrain.bamcore.BetterAdventureModCore;
import com.github.theredbrain.bamcore.block.entity.*;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class EntityRegistry {
    public static final BlockEntityType<TeleporterBlockBlockEntity> TELEPORTER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureModCore.identifier("teleporter_block"),
            FabricBlockEntityTypeBuilder.create(TeleporterBlockBlockEntity::new, BlockRegistry.TELEPORTER_BLOCK).build());
    public static final BlockEntityType<StructurePlacerBlockBlockEntity> STRUCTURE_PLACER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureModCore.identifier("structure_placer_block"),
            FabricBlockEntityTypeBuilder.create(StructurePlacerBlockBlockEntity::new, BlockRegistry.STRUCTURE_PLACER_BLOCK).build());
    public static final BlockEntityType<AreaFillerBlockBlockEntity> AREA_FILLER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureModCore.identifier("area_filler_block"),
            FabricBlockEntityTypeBuilder.create(AreaFillerBlockBlockEntity::new, BlockRegistry.AREA_FILLER_BLOCK).build());
    public static final BlockEntityType<RedstoneTriggerBlockBlockEntity> REDSTONE_TRIGGER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureModCore.identifier("redstone_trigger_block"),
            FabricBlockEntityTypeBuilder.create(RedstoneTriggerBlockBlockEntity::new, BlockRegistry.REDSTONE_TRIGGER_BLOCK).build());
    public static final BlockEntityType<RelayTriggerBlockBlockEntity> RELAY_TRIGGER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureModCore.identifier("relay_trigger_block"),
            FabricBlockEntityTypeBuilder.create(RelayTriggerBlockBlockEntity::new, BlockRegistry.RELAY_TRIGGER_BLOCK).build());
    public static final BlockEntityType<DelayTriggerBlockBlockEntity> DELAY_TRIGGER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureModCore.identifier("delay_trigger_block"),
            FabricBlockEntityTypeBuilder.create(DelayTriggerBlockBlockEntity::new, BlockRegistry.DELAY_TRIGGER_BLOCK).build());
    public static final BlockEntityType<ChunkLoaderBlockBlockEntity> CHUNK_LOADER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureModCore.identifier("chunk_loader_block"),
            FabricBlockEntityTypeBuilder.create(ChunkLoaderBlockBlockEntity::new, BlockRegistry.CHUNK_LOADER_BLOCK).build());

    public static void init() {
    }
}
