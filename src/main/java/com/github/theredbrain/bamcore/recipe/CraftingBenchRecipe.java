package com.github.theredbrain.bamcore.recipe;

import com.github.theredbrain.bamcore.registry.RecipeRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class CraftingBenchRecipe extends AbstractCraftingBenchRecipe {

    public CraftingBenchRecipe(Identifier id, String group, DefaultedList<Ingredient> input, ItemStack output) {
        super(RecipeRegistry.CRAFTING_BENCH_TYPE, RecipeRegistry.CRAFTING_BENCH_SERIALIZER, id, group, input, output);
    }
}
