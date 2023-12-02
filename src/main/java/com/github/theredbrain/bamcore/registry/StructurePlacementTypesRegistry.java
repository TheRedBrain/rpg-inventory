package com.github.theredbrain.bamcore.registry;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.world.gen.chunk.placement.FixedStructurePlacement;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.chunk.placement.StructurePlacementType;

public class StructurePlacementTypesRegistry {
    public static StructurePlacementType<FixedStructurePlacement> FIXED = () -> {
        return FixedStructurePlacement.CODEC;
    };

    public static void register() {
        Registry.register(Registries.STRUCTURE_PLACEMENT, BetterAdventureModeCore.identifier("fixed"), FIXED);
    }
}
