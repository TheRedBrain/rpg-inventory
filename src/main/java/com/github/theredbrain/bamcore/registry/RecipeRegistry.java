package com.github.theredbrain.bamcore.registry;

import com.github.theredbrain.bamcore.recipe.AbstractCraftingBenchRecipe;
import com.github.theredbrain.bamcore.recipe.CraftingBenchRecipe;
import net.minecraft.recipe.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RecipeRegistry {
    public static final RecipeType<StonecuttingRecipe> CRAFTING_BENCH_TYPE = RecipeType.register("crafting_bench");

    public static final RecipeSerializer<CraftingBenchRecipe> CRAFTING_BENCH_SERIALIZER = RecipeSerializer.register("crafting_bench", new AbstractCraftingBenchRecipe.Serializer<CraftingBenchRecipe>(CraftingBenchRecipe::new));

    public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
        return (S) Registry.register(Registries.RECIPE_SERIALIZER, id, serializer);
    }

    public static <T extends Recipe<?>> RecipeType<T> register(final String id) {
        return Registry.register(Registries.RECIPE_TYPE, new Identifier(id), new RecipeType<T>(){

            public String toString() {
                return id;
            }
        });
    }

    public static void init() {}
}
