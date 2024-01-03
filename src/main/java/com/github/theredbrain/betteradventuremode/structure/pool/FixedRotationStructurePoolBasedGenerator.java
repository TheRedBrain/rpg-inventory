package com.github.theredbrain.betteradventuremode.structure.pool;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.google.common.collect.Lists;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.*;
import net.minecraft.structure.pool.EmptyPoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.alias.StructurePoolAliasLookup;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.structure.Structure;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FixedRotationStructurePoolBasedGenerator extends StructurePoolBasedGenerator {

    public static Optional<Structure.StructurePosition> generate(Structure.Context context, RegistryEntry<StructurePool> structurePool, Optional<Identifier> id, int size, BlockPos pos, boolean useExpansionHack, Optional<Heightmap.Type> projectStartToHeightmap, int maxDistanceFromCenter, StructurePoolAliasLookup aliasLookup, BlockRotation blockRotation) {
        DynamicRegistryManager dynamicRegistryManager = context.dynamicRegistryManager();
        ChunkGenerator chunkGenerator = context.chunkGenerator();
        StructureTemplateManager structureTemplateManager = context.structureTemplateManager();
        HeightLimitView heightLimitView = context.world();
        ChunkRandom chunkRandom = context.random(); // generates always same jigsaw combination in the same chunk/position
        chunkRandom.setSeed(Random.create().nextLong()); // this randomizes the jigsaw generation even in the same chunk/position
        Registry<StructurePool> registry = dynamicRegistryManager.get(RegistryKeys.TEMPLATE_POOL);
//        BlockRotation blockRotation = BlockRotation.random(chunkRandom);
        StructurePool structurePool2 = (StructurePool)structurePool.getKey().flatMap((registryKey) -> {
            return registry.getOrEmpty(aliasLookup.lookup(registryKey));
        }).orElse((StructurePool)structurePool.value());
        StructurePoolElement structurePoolElement = structurePool2.getRandomElement(chunkRandom);
        if (structurePoolElement == EmptyPoolElement.INSTANCE) {
            return Optional.empty();
        } else {
            BlockPos blockPos;
            if (id.isPresent()) {
                Identifier identifier = (Identifier)id.get();
                Optional<BlockPos> optional = FixedRotationStructurePoolBasedGenerator.findStartingJigsawPos(structurePoolElement, identifier, pos, blockRotation, structureTemplateManager, chunkRandom);

//                Optional<BlockPos> optional = findStartingJigsawPos(structurePoolElement, identifier, pos, blockRotation, structureTemplateManager, chunkRandom);
                if (optional.isEmpty()) {
                    BetterAdventureMode.LOGGER.error("No starting jigsaw {} found in start pool {}", identifier, structurePool.getKey().map((key) -> {
                        return key.getValue().toString();
                    }).orElse("<unregistered>"));
                    return Optional.empty();
                }

                blockPos = (BlockPos)optional.get();
            } else {
                blockPos = pos;
            }

            Vec3i vec3i = blockPos.subtract(pos);
            BlockPos blockPos2 = pos.subtract(vec3i);
            PoolStructurePiece poolStructurePiece = new PoolStructurePiece(structureTemplateManager, structurePoolElement, blockPos2, structurePoolElement.getGroundLevelDelta(), blockRotation, structurePoolElement.getBoundingBox(structureTemplateManager, blockPos2, blockRotation));
            BlockBox blockBox = poolStructurePiece.getBoundingBox();
            int i = (blockBox.getMaxX() + blockBox.getMinX()) / 2;
            int j = (blockBox.getMaxZ() + blockBox.getMinZ()) / 2;
            int k;
            if (projectStartToHeightmap.isPresent()) {
                k = pos.getY() + chunkGenerator.getHeightOnGround(i, j, (Heightmap.Type)projectStartToHeightmap.get(), heightLimitView, context.noiseConfig());
            } else {
                k = blockPos2.getY();
            }

            int l = blockBox.getMinY() + poolStructurePiece.getGroundLevelDelta();
            poolStructurePiece.translate(0, k - l, 0);
            int m = k + vec3i.getY();
            return Optional.of(new Structure.StructurePosition(new BlockPos(i, m, j), (collector) -> {
                List<PoolStructurePiece> list = Lists.newArrayList();
                list.add(poolStructurePiece);
                if (size > 0) {
                    Box box = new Box((double)(i - maxDistanceFromCenter), (double)(m - maxDistanceFromCenter), (double)(j - maxDistanceFromCenter), (double)(i + maxDistanceFromCenter + 1), (double)(m + maxDistanceFromCenter + 1), (double)(j + maxDistanceFromCenter + 1));
                    VoxelShape voxelShape = VoxelShapes.combineAndSimplify(VoxelShapes.cuboid(box), VoxelShapes.cuboid(Box.from(blockBox)), BooleanBiFunction.ONLY_FIRST);
                    FixedRotationStructurePoolBasedGenerator.generate(context.noiseConfig(), size, useExpansionHack, chunkGenerator, structureTemplateManager, heightLimitView, chunkRandom, registry, poolStructurePiece, list, voxelShape, aliasLookup);
                    Objects.requireNonNull(collector);
                    list.forEach(collector::addPiece);
                }
            }));
        }
    }
//
//    public static Optional<Structure.StructurePosition> generate(Structure.Context context, RegistryEntry<StructurePool> structurePool, Optional<Identifier> id, int size, BlockPos pos, boolean useExpansionHack, Optional<Heightmap.Type> projectStartToHeightmap, int maxDistanceFromCenter, BlockRotation blockRotation) {
//        BlockPos blockPos;
//        DynamicRegistryManager dynamicRegistryManager = context.dynamicRegistryManager();
//        ChunkGenerator chunkGenerator = context.chunkGenerator();
//        StructureTemplateManager structureTemplateManager = context.structureTemplateManager();
//        HeightLimitView heightLimitView = context.world();
//        ChunkRandom chunkRandom = context.random(); // generates always same jigsaw combination in the same chunk/position
//        chunkRandom.setSeed(Random.create().nextLong()); // this randomizes the jigsaw generation even in the same chunk/position
//        Registry<StructurePool> registry = dynamicRegistryManager.get(RegistryKeys.TEMPLATE_POOL);
//        StructurePool structurePool2 = structurePool.value();
//        StructurePoolElement structurePoolElement = structurePool2.getRandomElement(chunkRandom);
//        if (structurePoolElement == EmptyPoolElement.INSTANCE) {
//            return Optional.empty();
//        }
//        if (id.isPresent()) {
//            Identifier identifier = id.get();
//            Optional<BlockPos> optional = FixedRotationStructurePoolBasedGenerator.findStartingJigsawPos(structurePoolElement, identifier, pos, blockRotation, structureTemplateManager, chunkRandom);
//            if (optional.isEmpty()) {
//                BetterAdventureModeCore.LOGGER.error("No starting jigsaw {} found in start pool {}", (Object)identifier, (Object)structurePool.getKey().map(key -> key.getValue().toString()).orElse("<unregistered>"));
//                return Optional.empty();
//            }
//            blockPos = optional.get();
//        } else {
//            blockPos = pos;
//        }
//        BlockPos vec3i = blockPos.subtract(pos);
//        BlockPos blockPos2 = pos.subtract(vec3i);
//        PoolStructurePiece poolStructurePiece = new PoolStructurePiece(structureTemplateManager, structurePoolElement, blockPos2, structurePoolElement.getGroundLevelDelta(), blockRotation, structurePoolElement.getBoundingBox(structureTemplateManager, blockPos2, blockRotation));
//        BlockBox blockBox = poolStructurePiece.getBoundingBox();
//        int i = (blockBox.getMaxX() + blockBox.getMinX()) / 2;
//        int j = (blockBox.getMaxZ() + blockBox.getMinZ()) / 2;
//        int k = projectStartToHeightmap.isPresent() ? pos.getY() + chunkGenerator.getHeightOnGround(i, j, projectStartToHeightmap.get(), heightLimitView, context.noiseConfig()) : blockPos2.getY();
//        int l = blockBox.getMinY() + poolStructurePiece.getGroundLevelDelta();
//        poolStructurePiece.translate(0, k - l, 0);
//        int m = k + vec3i.getY();
//        return Optional.of(new Structure.StructurePosition(new BlockPos(i, m, j), collector -> {
//            ArrayList<PoolStructurePiece> list = Lists.newArrayList();
//            list.add(poolStructurePiece);
//            if (size <= 0) {
//                return;
//            }
//            Box box = new Box(i - maxDistanceFromCenter, m - maxDistanceFromCenter, j - maxDistanceFromCenter, i + maxDistanceFromCenter + 1, m + maxDistanceFromCenter + 1, j + maxDistanceFromCenter + 1);
//            VoxelShape voxelShape = VoxelShapes.combineAndSimplify(VoxelShapes.cuboid(box), VoxelShapes.cuboid(Box.from(blockBox)), BooleanBiFunction.ONLY_FIRST);
//            StructurePoolBasedGenerator.generate(context.noiseConfig(), size, useExpansionHack, chunkGenerator, structureTemplateManager, heightLimitView, chunkRandom, registry, poolStructurePiece, list, voxelShape);
//            list.forEach(collector::addPiece);
//        }));
//    }

    public static boolean generate(ServerWorld world, RegistryEntry<StructurePool> structurePool, Identifier id, int size, BlockPos pos, boolean keepJigsaws, BlockRotation blockRotation) {
        ChunkGenerator chunkGenerator = world.getChunkManager().getChunkGenerator();
        StructureTemplateManager structureTemplateManager = world.getStructureTemplateManager();
        StructureAccessor structureAccessor = world.getStructureAccessor();
        Random random = world.getRandom();
        Structure.Context context = new Structure.Context(world.getRegistryManager(), chunkGenerator, chunkGenerator.getBiomeSource(), world.getChunkManager().getNoiseConfig(), structureTemplateManager, world.getSeed(), new ChunkPos(pos), world, biome -> true);
        Optional<Structure.StructurePosition> optional = FixedRotationStructurePoolBasedGenerator.generate(context, structurePool, Optional.of(id), size, pos, false, Optional.empty(), 128, StructurePoolAliasLookup.EMPTY, blockRotation);
        if (optional.isPresent()) {
            StructurePiecesCollector structurePiecesCollector = optional.get().generate();
            for (StructurePiece structurePiece : structurePiecesCollector.toList().pieces()) {
                if (!(structurePiece instanceof PoolStructurePiece poolStructurePiece)) continue;
                poolStructurePiece.generate((StructureWorldAccess)world, structureAccessor, chunkGenerator, random, BlockBox.infinite(), pos, keepJigsaws);
            }
            return true;
        }
        return false;
    }
}
