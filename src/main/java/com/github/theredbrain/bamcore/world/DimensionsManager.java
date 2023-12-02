package com.github.theredbrain.bamcore.world;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import net.minecraft.block.Blocks;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.FlatChunkGenerator;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorLayer;
import qouteall.q_misc_util.LifecycleHack;
import qouteall.q_misc_util.api.DimensionAPI;
import qouteall.q_misc_util.dimension.DimensionTemplate;
import qouteall.q_misc_util.dimension.DynamicDimensionsImpl;

import java.util.List;
import java.util.Optional;

public class DimensionsManager {

    public static void init() {
        LifecycleHack.markNamespaceStable("bamcore");
        DimensionTemplate.registerDimensionTemplate(
                "housing", HOUSING_DIMENSION_TEMPLATE
        );
        DimensionTemplate.registerDimensionTemplate(
                "dungeon", DUNGEON_DIMENSION_TEMPLATE
        );
    }

    /**
     * @param bluePrintType true: dungeon, false: housing
     */
    public static void addAndSaveDynamicDimension(Identifier dimensionId, MinecraftServer server, boolean bluePrintType) {
        // may throw exception here

        if (bluePrintType) {
            DynamicDimensionsImpl.addDimensionDynamically(server, dimensionId, DUNGEON_DIMENSION_TEMPLATE.createLevelStem(server));
        } else {
            DynamicDimensionsImpl.addDimensionDynamically(server, dimensionId, HOUSING_DIMENSION_TEMPLATE.createLevelStem(server));
        }
    }

    public static void removeDynamicPlayerDimension(ServerPlayerEntity player, MinecraftServer server) {
        // may throw exception here

        Identifier dimId = BetterAdventureModeCore.identifier(player.getUuid().toString() + "_dungeons");
        RegistryKey<World> registryKey = RegistryKey.of(RegistryKeys.WORLD, dimId);
        ServerWorld serverWorld = server.getWorld(registryKey);

        if (serverWorld != null) {
            DimensionAPI.removeDimensionDynamically(serverWorld);
        }
    }

    public static final DimensionTemplate HOUSING_DIMENSION_TEMPLATE = new DimensionTemplate(
            DimensionTypes.OVERWORLD,
            (server, dimTypeHolder) -> {
                DynamicRegistryManager.Immutable registryAccess = server.getRegistryManager();

                Registry<Biome> biomeRegistry = registryAccess.get(RegistryKeys.BIOME);

                RegistryEntry.Reference<Biome> biomeReference = biomeRegistry.entryOf(RegistryKey.of(RegistryKeys.BIOME, BetterAdventureModeCore.identifier("housing_biome")));

                FlatChunkGeneratorConfig flatChunkGeneratorConfig =
                        new FlatChunkGeneratorConfig(
                                Optional.of(RegistryEntryList.of()),
                                biomeReference,
                                List.of()
                        );
                flatChunkGeneratorConfig.getLayers().add(new FlatChunkGeneratorLayer(1, Blocks.AIR));
                flatChunkGeneratorConfig.updateLayerBlocks();

                FlatChunkGenerator chunkGenerator = new FlatChunkGenerator(flatChunkGeneratorConfig);

                return new DimensionOptions(dimTypeHolder, chunkGenerator);
            }
    );

    public static final DimensionTemplate DUNGEON_DIMENSION_TEMPLATE = new DimensionTemplate(
            DimensionTypes.OVERWORLD,
            (server, dimTypeHolder) -> {
                DynamicRegistryManager.Immutable registryAccess = server.getRegistryManager();

                Registry<Biome> biomeRegistry = registryAccess.get(RegistryKeys.BIOME);

                RegistryEntry.Reference<Biome> biomeReference = biomeRegistry.entryOf(RegistryKey.of(RegistryKeys.BIOME, BetterAdventureModeCore.identifier("dungeon_biome")));

                FlatChunkGeneratorConfig flatChunkGeneratorConfig =
                        new FlatChunkGeneratorConfig(
                                Optional.of(RegistryEntryList.of()),
                                biomeReference,
                                List.of()
                        );
                flatChunkGeneratorConfig.getLayers().add(new FlatChunkGeneratorLayer(1, Blocks.AIR));
                flatChunkGeneratorConfig.updateLayerBlocks();

                FlatChunkGenerator chunkGenerator = new FlatChunkGenerator(flatChunkGeneratorConfig);

                return new DimensionOptions(dimTypeHolder, chunkGenerator);
            }
    );
}
