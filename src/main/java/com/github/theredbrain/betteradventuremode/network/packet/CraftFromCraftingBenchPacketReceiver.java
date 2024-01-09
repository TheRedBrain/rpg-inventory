package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.api.json_files_backend.CraftingRecipe;
import com.github.theredbrain.betteradventuremode.api.util.ItemUtils;
import com.github.theredbrain.betteradventuremode.registry.CraftingRecipeRegistry;
import com.github.theredbrain.betteradventuremode.screen.CraftingBenchBlockScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class CraftFromCraftingBenchPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<CraftFromCraftingBenchPacket> {
    @Override
    public void receive(CraftFromCraftingBenchPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        String recipeIdentifier = packet.recipeIdentifier;

        ScreenHandler screenHandler = player.currentScreenHandler;

        CraftingRecipe craftingRecipe = CraftingRecipeRegistry.getCraftingRecipe(new Identifier(recipeIdentifier));

        if (craftingRecipe != null && screenHandler instanceof CraftingBenchBlockScreenHandler craftingBenchBlockScreenHandler) {
            boolean bl = true;
            for (ItemUtils.VirtualItemStack ingredient : craftingRecipe.getIngredients()) {
                Item virtualItem = ItemUtils.getItemStackFromVirtualItemStack(ingredient).getItem();
                int ingredientCount = ingredient.getCount();

                int playerInventorySize = craftingBenchBlockScreenHandler.getPlayerInventory().size();

                int enderChestInventorySize = craftingBenchBlockScreenHandler.getEnderChestInventory().size();

                int inventorySize = craftingBenchBlockScreenHandler.isStorageTabProviderInReach() ? playerInventorySize + enderChestInventorySize : playerInventorySize;

                for (int j = 0; j < inventorySize; j++) {
                    if (j < playerInventorySize) {
                        if (craftingBenchBlockScreenHandler.getPlayerInventory().getStack(j).isOf(virtualItem)) {
                            ItemStack itemStack = craftingBenchBlockScreenHandler.slots.get(j).getStack().copy();
                            int stackCount = itemStack.getCount();
                            if (stackCount >= ingredientCount) {
                                itemStack.setCount(stackCount - ingredientCount);
                                craftingBenchBlockScreenHandler.slots.get(j).setStack(itemStack);
                                ingredientCount = 0;
                                break;
                            } else {
                                craftingBenchBlockScreenHandler.slots.get(j).setStack(ItemStack.EMPTY);
                                ingredientCount = ingredientCount - stackCount;
                            }
                        }
                    } else {
                        if (craftingBenchBlockScreenHandler.getEnderChestInventory().getStack(j - playerInventorySize).isOf(virtualItem)) {
                            ItemStack itemStack = craftingBenchBlockScreenHandler.getEnderChestInventory().getStack(j - playerInventorySize).copy();
                            int stackCount = itemStack.getCount();
                            if (stackCount >= ingredientCount) {
                                itemStack.setCount(stackCount - ingredientCount);
                                craftingBenchBlockScreenHandler.getEnderChestInventory().setStack(j - playerInventorySize, itemStack);
                                ingredientCount = 0;
                                break;
                            } else {
                                craftingBenchBlockScreenHandler.getEnderChestInventory().setStack(j - playerInventorySize, ItemStack.EMPTY);
                                ingredientCount = ingredientCount - stackCount;
                            }
                        }
                    }
                }
                if (ingredientCount > 0) {
                    bl = false;
                }
            }
            if (bl) {
                player.getInventory().offerOrDrop(ItemUtils.getItemStackFromVirtualItemStack(craftingRecipe.getResult()));
                craftingBenchBlockScreenHandler.calculateUnlockedRecipes();
                craftingBenchBlockScreenHandler.runContentsChangedListener();
            }
        }
    }
}