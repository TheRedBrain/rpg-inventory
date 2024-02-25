package com.github.theredbrain.betteradventuremode.world;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.gen.FlatLevelGeneratorPreset;
import net.minecraft.world.gen.chunk.FlatChunkGenerator;
import qouteall.q_misc_util.MiscHelper;
import qouteall.q_misc_util.api.DimensionAPI;
//import qouteall.dimlib.DimensionTemplate;
//import qouteall.dimlib.api.DimensionAPI;

public class DimensionsManager {

    public static void init() {
//        DimensionAPI.suppressExperimentalWarningForNamespace("betteradventuremode");
//        DimensionAPI.registerDimensionTemplate(
//                "player_locations", PLAYER_LOCATIONS_DIMENSION_TEMPLATE
//        );
    }

    public static void addAndSaveDynamicDimension(Identifier dimensionId, MinecraftServer server) {
        // may throw exception here
        DynamicRegistryManager manager = MiscHelper.getServer().getRegistryManager();

        Registry<DimensionType> dimensionTypeRegistry = manager.get(RegistryKeys.DIMENSION_TYPE);

        RegistryEntry.Reference<DimensionType> dimensionTypeReference = dimensionTypeRegistry.entryOf(RegistryKey.of(RegistryKeys.DIMENSION_TYPE, new Identifier("overworld")));

        Registry<FlatLevelGeneratorPreset> flatLevelGeneratorPresetRegistry = manager.get(RegistryKeys.FLAT_LEVEL_GENERATOR_PRESET);

        RegistryEntry.Reference<FlatLevelGeneratorPreset> flatLevelGeneratorPresetReference = flatLevelGeneratorPresetRegistry.entryOf(RegistryKey.of(RegistryKeys.FLAT_LEVEL_GENERATOR_PRESET, BetterAdventureMode.identifier("player_locations_dimension")));

        FlatChunkGenerator chunkGenerator = new FlatChunkGenerator(flatLevelGeneratorPresetReference.value().settings());
// add the dimension
        DimensionAPI.addDimensionDynamically(
                dimensionId,
                new DimensionOptions(
                        dimensionTypeReference,
                        chunkGenerator
    )
);
//        DimensionAPI.addDimensionDynamically(server, dimensionId, PLAYER_LOCATIONS_DIMENSION_TEMPLATE.createLevelStem(server));
    }

//    public static final DimensionTemplate PLAYER_LOCATIONS_DIMENSION_TEMPLATE = new DimensionTemplate(
//            DimensionTypes.OVERWORLD,
//            (server, dimTypeHolder) -> {
//                DynamicRegistryManager.Immutable registryAccess = server.getRegistryManager();
//
//                Registry<FlatLevelGeneratorPreset> flatLevelGeneratorPresetRegistry = registryAccess.get(RegistryKeys.FLAT_LEVEL_GENERATOR_PRESET);
//
//                RegistryEntry.Reference<FlatLevelGeneratorPreset> flatLevelGeneratorPresetReference = flatLevelGeneratorPresetRegistry.entryOf(RegistryKey.of(RegistryKeys.FLAT_LEVEL_GENERATOR_PRESET, BetterAdventureMode.identifier("player_locations_dimension")));
//
//                FlatChunkGenerator chunkGenerator = new FlatChunkGenerator(flatLevelGeneratorPresetReference.value().settings());
//
//                return new DimensionOptions(dimTypeHolder, chunkGenerator);
//            }
//    );
}
