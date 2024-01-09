package com.github.theredbrain.betteradventuremode.api.json_files_backend;

import com.github.theredbrain.betteradventuremode.api.util.ItemUtils;
import com.github.theredbrain.betteradventuremode.block.CraftingRootBlock;

import java.util.List;

public final class CraftingRecipe {

    private final List<ItemUtils.VirtualItemStack> ingredients;
    private final ItemUtils.VirtualItemStack result;
    private final String unlockAdvancement;
    private final CraftingRootBlock.Tab tab;
    private final int level;

    public CraftingRecipe(List<ItemUtils.VirtualItemStack> ingredients, ItemUtils.VirtualItemStack result, String unlockAdvancement, CraftingRootBlock.Tab tab, int level) {
        this.ingredients = ingredients;
        this.result = result;
        this.unlockAdvancement = unlockAdvancement;
        this.tab = tab;
        this.level = level;
    }

    public List<ItemUtils.VirtualItemStack> getIngredients() {
        return this.ingredients;
    }

    public ItemUtils.VirtualItemStack getResult() {
        return result;
    }

    public String getUnlockAdvancement() {
        return unlockAdvancement;
    }

    public CraftingRootBlock.Tab getTab() {
        return tab;
    }

    public int getLevel() {
        return level;
    }
}
