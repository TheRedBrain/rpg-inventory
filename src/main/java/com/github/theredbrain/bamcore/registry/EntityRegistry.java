package com.github.theredbrain.bamcore.registry;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.block.entity.*;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class EntityRegistry {

    //region Content Blocks
    public static final BlockEntityType<BonfireBlockBlockEntity> BONFIRE_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureModeCore.identifier("bonfire"),
            FabricBlockEntityTypeBuilder.create(BonfireBlockBlockEntity::new, BlockRegistry.BONFIRE_BLOCK).build());
    //endregion Content Blocks

    //region Operator Blocks
    public static final BlockEntityType<MimicBlockEntity> MIMIC_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureModeCore.identifier("mimic_block"),
            FabricBlockEntityTypeBuilder.create(MimicBlockEntity::new, BlockRegistry.MIMIC_BLOCK).build());
    public static final BlockEntityType<TriggeredSpawnerBlockEntity> TRIGGERED_SPAWNER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureModeCore.identifier("triggered_spawner_block"),
            FabricBlockEntityTypeBuilder.create(TriggeredSpawnerBlockEntity::new, BlockRegistry.TRIGGERED_SPAWNER_BLOCK).build());
    public static final BlockEntityType<DungeonControlBlockEntity> DUNGEON_CONTROL_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureModeCore.identifier("dungeon_control_block"),
            FabricBlockEntityTypeBuilder.create(DungeonControlBlockEntity::new, BlockRegistry.DUNGEON_CONTROL_BLOCK).build());
    public static final BlockEntityType<HousingBlockBlockEntity> HOUSING_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureModeCore.identifier("housing_block"),
            FabricBlockEntityTypeBuilder.create(HousingBlockBlockEntity::new, BlockRegistry.HOUSING_BLOCK).build());
    public static final BlockEntityType<TeleporterBlockBlockEntity> TELEPORTER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureModeCore.identifier("teleporter_block"),
            FabricBlockEntityTypeBuilder.create(TeleporterBlockBlockEntity::new, BlockRegistry.TELEPORTER_BLOCK).build());
    public static final BlockEntityType<JigsawPlacerBlockBlockEntity> STRUCTURE_PLACER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureModeCore.identifier("structure_placer_block"),
            FabricBlockEntityTypeBuilder.create(JigsawPlacerBlockBlockEntity::new, BlockRegistry.JIGSAW_PLACER_BLOCK).build());
    public static final BlockEntityType<RedstoneTriggerBlockBlockEntity> REDSTONE_TRIGGER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureModeCore.identifier("redstone_trigger_block"),
            FabricBlockEntityTypeBuilder.create(RedstoneTriggerBlockBlockEntity::new, BlockRegistry.REDSTONE_TRIGGER_BLOCK).build());
    public static final BlockEntityType<RelayTriggerBlockBlockEntity> RELAY_TRIGGER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureModeCore.identifier("relay_trigger_block"),
            FabricBlockEntityTypeBuilder.create(RelayTriggerBlockBlockEntity::new, BlockRegistry.RELAY_TRIGGER_BLOCK).build());
    public static final BlockEntityType<ResetTriggerBlockEntity> RESET_TRIGGER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureModeCore.identifier("reset_trigger_block"),
            FabricBlockEntityTypeBuilder.create(ResetTriggerBlockEntity::new, BlockRegistry.RESET_TRIGGER_BLOCK).build());
    public static final BlockEntityType<DelayTriggerBlockBlockEntity> DELAY_TRIGGER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureModeCore.identifier("delay_trigger_block"),
            FabricBlockEntityTypeBuilder.create(DelayTriggerBlockBlockEntity::new, BlockRegistry.DELAY_TRIGGER_BLOCK).build());
    public static final BlockEntityType<UseRelayBlockEntity> USE_RELAY_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureModeCore.identifier("use_relay_block"),
            FabricBlockEntityTypeBuilder.create(UseRelayBlockEntity::new,
                    BlockRegistry.USE_RELAY_BLOCK,
                    BlockRegistry.USE_RELAY_OAK_DOOR,
                    BlockRegistry.USE_RELAY_IRON_DOOR,
                    BlockRegistry.USE_RELAY_SPRUCE_DOOR,
                    BlockRegistry.USE_RELAY_BIRCH_DOOR,
                    BlockRegistry.USE_RELAY_JUNGLE_DOOR,
                    BlockRegistry.USE_RELAY_ACACIA_DOOR,
                    BlockRegistry.USE_RELAY_CHERRY_DOOR,
                    BlockRegistry.USE_RELAY_DARK_OAK_DOOR,
                    BlockRegistry.USE_RELAY_MANGROVE_DOOR,
                    BlockRegistry.USE_RELAY_BAMBOO_DOOR,
                    BlockRegistry.USE_RELAY_CRIMSON_DOOR,
                    BlockRegistry.USE_RELAY_WARPED_DOOR,
                    BlockRegistry.USE_RELAY_OAK_TRAPDOOR,
                    BlockRegistry.USE_RELAY_IRON_TRAPDOOR,
                    BlockRegistry.USE_RELAY_SPRUCE_TRAPDOOR,
                    BlockRegistry.USE_RELAY_BIRCH_TRAPDOOR,
                    BlockRegistry.USE_RELAY_JUNGLE_TRAPDOOR,
                    BlockRegistry.USE_RELAY_ACACIA_TRAPDOOR,
                    BlockRegistry.USE_RELAY_CHERRY_TRAPDOOR,
                    BlockRegistry.USE_RELAY_DARK_OAK_TRAPDOOR,
                    BlockRegistry.USE_RELAY_MANGROVE_TRAPDOOR,
                    BlockRegistry.USE_RELAY_BAMBOO_TRAPDOOR,
                    BlockRegistry.USE_RELAY_CRIMSON_TRAPDOOR,
                    BlockRegistry.USE_RELAY_WARPED_TRAPDOOR).build());
    //endregion Operator Blocks

    public static void init() {
    }
}
