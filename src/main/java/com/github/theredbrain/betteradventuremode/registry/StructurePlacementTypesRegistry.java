package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.world.gen.chunk.placement.FixedStructurePlacement;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.chunk.placement.StructurePlacementType;

public class StructurePlacementTypesRegistry {
    public static StructurePlacementType<FixedStructurePlacement> FIXED = () -> {
        return FixedStructurePlacement.CODEC;
    };

    public static void register() {
        Registry.register(Registries.STRUCTURE_PLACEMENT, BetterAdventureMode.identifier("fixed"), FIXED);
    }
}
