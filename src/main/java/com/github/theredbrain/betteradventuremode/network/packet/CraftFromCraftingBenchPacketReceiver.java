package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.data.CraftingRecipe;
import com.github.theredbrain.betteradventuremode.util.ItemUtils;
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

        boolean useStorageInventory = packet.useStorageInventory;

        ScreenHandler screenHandler = player.currentScreenHandler;

        CraftingRecipe craftingRecipe = CraftingRecipeRegistry.getCraftingRecipe(new Identifier(recipeIdentifier));

        if (craftingRecipe != null && screenHandler instanceof CraftingBenchBlockScreenHandler craftingBenchBlockScreenHandler) {

            int playerInventorySize = craftingBenchBlockScreenHandler.getPlayerInventory().size();

            int stash0InventorySize = useStorageInventory && craftingBenchBlockScreenHandler.isStorageArea0ProviderInReach() ? 6 : 0;

            int stash1InventorySize = useStorageInventory && craftingBenchBlockScreenHandler.isStorageArea1ProviderInReach() ? 8 : 0;

            int stash2InventorySize = useStorageInventory && craftingBenchBlockScreenHandler.isStorageArea2ProviderInReach() ? 12 : 0;

            int stash3InventorySize = useStorageInventory && craftingBenchBlockScreenHandler.isStorageArea3ProviderInReach() ? 14 : 0;

            int stash4InventorySize = useStorageInventory && craftingBenchBlockScreenHandler.isStorageArea4ProviderInReach() ? 21 : 0;

            boolean bl = true;
            ItemStack itemStack;

            for (ItemUtils.VirtualItemStack ingredient : craftingRecipe.getIngredients()) {

                Item virtualItem = ItemUtils.getItemStackFromVirtualItemStack(ingredient).getItem();
                int ingredientCount = ingredient.getCount();
                int j;

                // TODO play test which inventory normally contains the most crafting ingredients and should be checked first

                for (j = 0; j < playerInventorySize; j++) {
                    if (craftingBenchBlockScreenHandler.getPlayerInventory().getStack(j).isOf(virtualItem)) {
                        itemStack = craftingBenchBlockScreenHandler.getPlayerInventory().getStack(j).copy();
                        int stackCount = itemStack.getCount();
                        if (stackCount >= ingredientCount) {
                            itemStack.setCount(stackCount - ingredientCount);
                            craftingBenchBlockScreenHandler.getPlayerInventory().setStack(j, itemStack);
                            ingredientCount = 0;
                            break;
                        } else {
                            craftingBenchBlockScreenHandler.getPlayerInventory().setStack(j, ItemStack.EMPTY);
                            ingredientCount = ingredientCount - stackCount;
                        }
                    }
                }
                if (ingredientCount == 0) {
                    break;
                }

                for (j = 0; j < stash0InventorySize; j++) {
                    if (craftingBenchBlockScreenHandler.getEnderChestInventory().getStack(j).isOf(virtualItem)) {
                        itemStack = craftingBenchBlockScreenHandler.getEnderChestInventory().getStack(j).copy();
                        int stackCount = itemStack.getCount();
                        if (stackCount >= ingredientCount) {
                            itemStack.setCount(stackCount - ingredientCount);
                            craftingBenchBlockScreenHandler.getEnderChestInventory().setStack(j, itemStack);
                            ingredientCount = 0;
                            break;
                        } else {
                            craftingBenchBlockScreenHandler.getEnderChestInventory().setStack(j, ItemStack.EMPTY);
                            ingredientCount = ingredientCount - stackCount;
                        }
                    }
                }
                if (ingredientCount == 0) {
                    break;
                }

                for (j = 0; j < stash1InventorySize; j++) {
                    if (craftingBenchBlockScreenHandler.getStashInventory().getStack(j).isOf(virtualItem)) {
                        itemStack = craftingBenchBlockScreenHandler.getStashInventory().getStack(j).copy();
                        int stackCount = itemStack.getCount();
                        if (stackCount >= ingredientCount) {
                            itemStack.setCount(stackCount - ingredientCount);
                            craftingBenchBlockScreenHandler.getStashInventory().setStack(j, itemStack);
                            ingredientCount = 0;
                            break;
                        } else {
                            craftingBenchBlockScreenHandler.getStashInventory().setStack(j, ItemStack.EMPTY);
                            ingredientCount = ingredientCount - stackCount;
                        }
                    }
                }
                if (ingredientCount == 0) {
                    break;
                }

                for (j = 0; j < stash2InventorySize; j++) {
                    if (craftingBenchBlockScreenHandler.getStashInventory().getStack(8 + j).isOf(virtualItem)) {
                        itemStack = craftingBenchBlockScreenHandler.getStashInventory().getStack(8 + j).copy();
                        int stackCount = itemStack.getCount();
                        if (stackCount >= ingredientCount) {
                            itemStack.setCount(stackCount - ingredientCount);
                            craftingBenchBlockScreenHandler.getStashInventory().setStack(8 + j, itemStack);
                            ingredientCount = 0;
                            break;
                        } else {
                            craftingBenchBlockScreenHandler.getStashInventory().setStack(8 + j, ItemStack.EMPTY);
                            ingredientCount = ingredientCount - stackCount;
                        }
                    }
                }
                if (ingredientCount == 0) {
                    break;
                }

                for (j = 0; j < stash3InventorySize; j++) {
                    if (craftingBenchBlockScreenHandler.getStashInventory().getStack(20 + j).isOf(virtualItem)) {
                        itemStack = craftingBenchBlockScreenHandler.getStashInventory().getStack(20 + j).copy();
                        int stackCount = itemStack.getCount();
                        if (stackCount >= ingredientCount) {
                            itemStack.setCount(stackCount - ingredientCount);
                            craftingBenchBlockScreenHandler.getStashInventory().setStack(20 + j, itemStack);
                            ingredientCount = 0;
                            break;
                        } else {
                            craftingBenchBlockScreenHandler.getStashInventory().setStack(20 + j, ItemStack.EMPTY);
                            ingredientCount = ingredientCount - stackCount;
                        }
                    }
                }
                if (ingredientCount == 0) {
                    break;
                }

                for (j = 0; j < stash4InventorySize; j++) {
                    if (craftingBenchBlockScreenHandler.getEnderChestInventory().getStack(6 + j).isOf(virtualItem)) {
                        itemStack = craftingBenchBlockScreenHandler.getEnderChestInventory().getStack(6 + j).copy();
                        int stackCount = itemStack.getCount();
                        if (stackCount >= ingredientCount) {
                            itemStack.setCount(stackCount - ingredientCount);
                            craftingBenchBlockScreenHandler.getEnderChestInventory().setStack(6 + j, itemStack);
                            ingredientCount = 0;
                            break;
                        } else {
                            craftingBenchBlockScreenHandler.getEnderChestInventory().setStack(6 + j, ItemStack.EMPTY);
                            ingredientCount = ingredientCount - stackCount;
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
                craftingBenchBlockScreenHandler.setShouldScreenCalculateCraftingStatus(1);
            }
        }
    }
}