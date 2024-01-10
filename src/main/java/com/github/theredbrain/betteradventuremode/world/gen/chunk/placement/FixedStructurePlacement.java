package com.github.theredbrain.betteradventuremode.world.gen.chunk.placement;

import com.github.theredbrain.betteradventuremode.registry.StructurePlacementTypesRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.gen.chunk.placement.StructurePlacement;
import net.minecraft.world.gen.chunk.placement.StructurePlacementCalculator;
import net.minecraft.world.gen.chunk.placement.StructurePlacementType;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class FixedStructurePlacement extends StructurePlacement {
    public static final Codec<FixedStructurePlacement> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(BlockPos.CODEC.listOf().fieldOf("positions").forGetter((placement) -> {
            return placement.positions;
        })).and(buildCodec(instance)).apply(instance, FixedStructurePlacement::new);
    });
    private final List<BlockPos> positions;
    private final List<BlockPos> blacklist;

    public FixedStructurePlacement(List<BlockPos> positions, Vec3i locateOffset, FrequencyReductionMethod frequencyReductionMethod, float frequency, int salt, Optional<ExclusionZone> exclusionZone) {
        super(locateOffset, FrequencyReductionMethod.DEFAULT, 1.0F, 0, Optional.empty());
        this.positions = positions;
        this.blacklist = new LinkedList();
    }

    public List<BlockPos> getPositions() {
        return this.positions;
    }

    public List<BlockPos> getBlacklist() {
        return this.blacklist;
    }

    protected boolean isStartChunk(StructurePlacementCalculator calculator, int chunkX, int chunkZ) {
        if (this.positions.isEmpty()) {
            return false;
        } else {
            ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
            Iterator var5 = this.positions.iterator();

            while(var5.hasNext()) {
                BlockPos pos = (BlockPos)var5.next();
                if (!this.blacklist.contains(pos)) {
                    long x = (long)chunkPos.x * 16L;
                    long z = (long)chunkPos.z * 16L;
                    boolean bl1 = (long)pos.getX() >= x;
                    boolean bl2 = (long)pos.getX() <= x + 15L;
                    boolean bl3 = (long)pos.getZ() >= z;
                    boolean bl4 = (long)pos.getZ() <= z + 15L;
                    if (bl1 && bl2 && bl3 && bl4) {
                        this.blacklist.add(pos);
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public boolean shouldGenerate(StructurePlacementCalculator calculator, int chunkX, int chunkZ) {
        return this.isStartChunk(calculator, chunkX, chunkZ);
    }

    public StructurePlacementType<?> getType() {
        return StructurePlacementTypesRegistry.FIXED;
    }
}
