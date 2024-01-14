package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.block.entity.*;
import com.github.theredbrain.betteradventuremode.entity.mob.SpawnerBoundEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class EntityRegistry {

    public static final EntityType<SpawnerBoundEntity> SPAWNER_BOUND_ENTITY = Registry.register(Registries.ENTITY_TYPE,
            BetterAdventureMode.identifier("spawner_bound_entity"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, SpawnerBoundEntity::new).build());

    //region Content Blocks
    public static final BlockEntityType<BonfireBlockBlockEntity> BONFIRE_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureMode.identifier("bonfire"),
            FabricBlockEntityTypeBuilder.create(BonfireBlockBlockEntity::new, BlockRegistry.BONFIRE_BLOCK).build());
    //endregion Content Blocks

    //region Script Blocks
    public static final BlockEntityType<TriggeredCounterBlockEntity> TRIGGERED_COUNTER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureMode.identifier("triggered_counter_block"),
            FabricBlockEntityTypeBuilder.create(TriggeredCounterBlockEntity::new, BlockRegistry.TRIGGERED_COUNTER_BLOCK).build());
    public static final BlockEntityType<DialogueBlockEntity> DIALOGUE_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureMode.identifier("dialogue_block"),
            FabricBlockEntityTypeBuilder.create(DialogueBlockEntity::new, BlockRegistry.DIALOGUE_BLOCK).build());
    public static final BlockEntityType<ShopBlockEntity> SHOP_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureMode.identifier("shop_block"),
            FabricBlockEntityTypeBuilder.create(ShopBlockEntity::new, BlockRegistry.SHOP_BLOCK).build());
    public static final BlockEntityType<MimicBlockEntity> MIMIC_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureMode.identifier("mimic_block"),
            FabricBlockEntityTypeBuilder.create(MimicBlockEntity::new, BlockRegistry.MIMIC_BLOCK).build());
    public static final BlockEntityType<TriggeredSpawnerBlockEntity> TRIGGERED_SPAWNER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureMode.identifier("triggered_spawner_block"),
            FabricBlockEntityTypeBuilder.create(TriggeredSpawnerBlockEntity::new, BlockRegistry.TRIGGERED_SPAWNER_BLOCK).build());
    public static final BlockEntityType<LocationControlBlockEntity> LOCATION_CONTROL_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureMode.identifier("location_control_block"),
            FabricBlockEntityTypeBuilder.create(LocationControlBlockEntity::new, BlockRegistry.LOCATION_CONTROL_BLOCK).build());
    public static final BlockEntityType<HousingBlockBlockEntity> HOUSING_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureMode.identifier("housing_block"),
            FabricBlockEntityTypeBuilder.create(HousingBlockBlockEntity::new, BlockRegistry.HOUSING_BLOCK).build());
    public static final BlockEntityType<TeleporterBlockBlockEntity> TELEPORTER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureMode.identifier("teleporter_block"),
            FabricBlockEntityTypeBuilder.create(TeleporterBlockBlockEntity::new, BlockRegistry.TELEPORTER_BLOCK).build());
    public static final BlockEntityType<JigsawPlacerBlockBlockEntity> STRUCTURE_PLACER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureMode.identifier("structure_placer_block"),
            FabricBlockEntityTypeBuilder.create(JigsawPlacerBlockBlockEntity::new, BlockRegistry.JIGSAW_PLACER_BLOCK).build());
    public static final BlockEntityType<RedstoneTriggerBlockBlockEntity> REDSTONE_TRIGGER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureMode.identifier("redstone_trigger_block"),
            FabricBlockEntityTypeBuilder.create(RedstoneTriggerBlockBlockEntity::new, BlockRegistry.REDSTONE_TRIGGER_BLOCK).build());
    public static final BlockEntityType<RelayTriggerBlockBlockEntity> RELAY_TRIGGER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureMode.identifier("relay_trigger_block"),
            FabricBlockEntityTypeBuilder.create(RelayTriggerBlockBlockEntity::new, BlockRegistry.RELAY_TRIGGER_BLOCK).build());
    public static final BlockEntityType<ResetTriggerBlockEntity> RESET_TRIGGER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureMode.identifier("reset_trigger_block"),
            FabricBlockEntityTypeBuilder.create(ResetTriggerBlockEntity::new, BlockRegistry.RESET_TRIGGER_BLOCK).build());
    public static final BlockEntityType<DelayTriggerBlockBlockEntity> DELAY_TRIGGER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureMode.identifier("delay_trigger_block"),
            FabricBlockEntityTypeBuilder.create(DelayTriggerBlockBlockEntity::new, BlockRegistry.DELAY_TRIGGER_BLOCK).build());
    public static final BlockEntityType<EntranceDelegationBlockEntity> ENTRANCE_DELEGATION_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureMode.identifier("entrance_delegation_block"),
            FabricBlockEntityTypeBuilder.create(EntranceDelegationBlockEntity::new, BlockRegistry.ENTRANCE_DELEGATION_BLOCK).build());
    public static final BlockEntityType<StatusEffectApplierBlockEntity> STATUS_EFFECT_APPLIER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureMode.identifier("status_effect_applier_block"),
            FabricBlockEntityTypeBuilder.create(StatusEffectApplierBlockEntity::new, BlockRegistry.STATUS_EFFECT_APPLIER_BLOCK).build());
    public static final BlockEntityType<TriggeredAdvancementCheckerBlockEntity> TRIGGERED_ADVANCEMENT_CHECKER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureMode.identifier("triggered_advancement_checker_block"),
            FabricBlockEntityTypeBuilder.create(TriggeredAdvancementCheckerBlockEntity::new, BlockRegistry.TRIGGERED_ADVANCEMENT_CHECKER_BLOCK).build());
    public static final BlockEntityType<UseRelayBlockEntity> USE_RELAY_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            BetterAdventureMode.identifier("use_relay_block"),
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
    //endregion Script Blocks

    public static void init() {
    }
}
