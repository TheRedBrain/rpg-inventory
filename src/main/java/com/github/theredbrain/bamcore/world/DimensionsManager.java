package com.github.theredbrain.bamcore.world;
// TODO move to bamdimensions
//import com.github.theredbrain.bamcore.BetterAdventureModeCore;
//import net.minecraft.registry.RegistryKey;
//import net.minecraft.registry.RegistryKeys;
//import net.minecraft.server.MinecraftServer;
//import net.minecraft.server.network.ServerPlayerEntity;
//import net.minecraft.server.world.ServerWorld;
//import net.minecraft.util.Identifier;
//import net.minecraft.world.World;
//import net.minecraft.world.dimension.DimensionOptions;
//import net.minecraft.world.gen.chunk.ChunkGenerator;
//import qouteall.q_misc_util.LifecycleHack;
//import qouteall.q_misc_util.api.DimensionAPI;
//import qouteall.q_misc_util.dimension.DynamicDimensionsImpl;
//
//public class DimensionsManager {
//
//    public static void init() {
//        LifecycleHack.markNamespaceStable("bamcore");
//    }
//
//    public static void addAndSaveDynamicDimension(Identifier dimensionId, MinecraftServer server) {
//        // may throw exception here
//
////        Identifier blueprintDimId = RPGMod.identifier("blueprint_dungeons");
////        RegistryKey<World> registryKey = RegistryKey.of(RegistryKeys.WORLD, blueprintDimId);
////        ServerWorld serverWorld = server.getWorld(registryKey);
//
//        ServerWorld serverWorld = server.getOverworld();
//
//        if (serverWorld != null) {
//            ChunkGenerator generator = serverWorld.getChunkManager().getChunkGenerator();
////            Identifier dimId = RPGMod.identifier(player.getUuid().toString() + "_dungeons");
//
//            DynamicDimensionsImpl.addDimensionDynamically(dimensionId, new DimensionOptions(serverWorld.getDimensionEntry(), generator));
//
//            RegistryKey<World> newRegistryKey = RegistryKey.of(RegistryKeys.WORLD, dimensionId);
//            DimensionAPI.saveDimensionConfiguration(newRegistryKey);
//        }
//    }
//
//    public static void removeDynamicPlayerDimension(ServerPlayerEntity player, MinecraftServer server) {
//        // may throw exception here
//
//
//        Identifier dimId = BetterAdventureModeCore.identifier(player.getUuid().toString() + "_dungeons");
//        RegistryKey<World> registryKey = RegistryKey.of(RegistryKeys.WORLD, dimId);
//        ServerWorld serverWorld = server.getWorld(registryKey);
//
//        if (serverWorld != null) {
//            DimensionAPI.removeDimensionDynamically(serverWorld);
//
//            DimensionAPI.deleteDimensionConfiguration(serverWorld.getRegistryKey());
//
//        }
//    }
//}
