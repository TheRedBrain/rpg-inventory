package com.github.theredbrain.bamcore.screen;


import com.github.theredbrain.bamcore.registry.BlockRegistry;
import com.github.theredbrain.bamcore.registry.ScreenHandlerTypesRegistry;
import com.github.theredbrain.bamcore.registry.Tags;
import com.google.common.collect.Lists;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.StonecuttingRecipe;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CraftingBenchBlockScreenHandler extends ScreenHandler {

    private PlayerInventory playerInventory;
//    private Inventory inventory;
//    private TeleporterBlockBlockEntity teleporterBlockBlockEntity;

    private BlockPos blockPos;
    private boolean isEnderChestInventoryProviderInReach;
    private final ScreenHandlerContext context;
    long lastTakeTime;
    private final Property selectedRecipe = Property.create();
    private final World world;
    private List<StonecuttingRecipe> availableRecipes = Lists.newArrayList();
    private List<Ingredient> currentIngredients = Lists.newArrayList();

    private EnderChestInventory enderChestInventory;
    final Slot outputSlot;
    Runnable contentsChangedListener = () -> {};
    private List<ItemStack> inputStacks = new ArrayList<>(DefaultedList.ofSize(9, ItemStack.EMPTY));
    public Inventory input = new SimpleInventory(9){

        @Override
        public void markDirty() {
            super.markDirty();
            CraftingBenchBlockScreenHandler.this.onContentChanged(this);
            CraftingBenchBlockScreenHandler.this.contentsChangedListener.run();
        }
    };
    public Inventory expandedInput = new DoubleInventory(input, enderChestInventory);
    final CraftingResultInventory output = new CraftingResultInventory();

    public CraftingBenchBlockScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, playerInventory.player.getEnderChestInventory(), ScreenHandlerContext.EMPTY);
        this.blockPos = buf.readBlockPos();
        this.isEnderChestInventoryProviderInReach = buf.readBoolean();
    }

    public CraftingBenchBlockScreenHandler(int syncId, PlayerInventory playerInventory, EnderChestInventory enderChestInventory, ScreenHandlerContext context) {
        super(ScreenHandlerTypesRegistry.CRAFTING_BENCH_BLOCK_SCREEN_HANDLER, syncId);
        // TODO
        // set teleporterBlockBlockEntity
        this.playerInventory = playerInventory;
//        this.inventory = inventory;
        this.blockPos = BlockPos.ORIGIN;
        this.isEnderChestInventoryProviderInReach = false;
        this.context = context;
        this.world = playerInventory.player.getWorld();
        this.enderChestInventory = enderChestInventory;

        int k;
        int j;
        int i = -18;
        // hotbar
        for (j = 0; j < 9; ++j) {
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 161 + i) {

                @Override
                public boolean canInsert(ItemStack stack) {
                    return stack.isIn(Tags.ADVENTURE_HOTBAR_ITEMS);
                }
            });
        }
        // main inventory
        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
            }
        }
        // ender chest inventory
        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 9; ++k) {
                this.addSlot(new Slot(enderChestInventory, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }
        // crafting input
        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 3; ++k) {
                this.addSlot(new Slot(this.input, k + j * 3, 8 + k * 18, 18 + j * 18));
            }
        }
        // crafting output
        this.outputSlot = this.addSlot(new Slot(this.output, 1, 143, 33){

            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                if (CraftingBenchBlockScreenHandler.this.output.getLastRecipe() == null) return;
                Inventory inputInventory = getInputInventory();
                stack.onCraft(player.getWorld(), player, stack.getCount());
                List<ItemStack> ingredientStacks = this.getInputStacks(CraftingBenchBlockScreenHandler.this.output.getLastRecipe(), getInputInventory());
                CraftingBenchBlockScreenHandler.this.output.unlockLastRecipe(player, ingredientStacks);

                for (ItemStack itemStack : ingredientStacks) {
                    for (int i = 0; i < getInputInventory().size(); i++) {
                        if (getInputInventory().getStack(i).getItem() == itemStack.getItem()) {

                            if (inputInventory.getStack(i).getCount() > 1) {
                                inputInventory.getStack(i).setCount(inputInventory.getStack(i).getCount() - 1);
//                                int
                            }
                        }
                    }
//                    getInputInventory().
                }
                // TODO remove input items list content from input stacks
                // TODO clear input items list
//                ItemStack itemStack = CraftingBenchBlockScreenHandler.this.inputSlot.takeStack(1);
                // TODO check input stacks
                if (!getInputInventory().isEmpty()) {
                    CraftingBenchBlockScreenHandler.this.populateResult();
                }
                context.run((world, pos) -> {
                    long l = world.getTime();
                    if (CraftingBenchBlockScreenHandler.this.lastTakeTime != l) {
                        world.playSound(null, (BlockPos)pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundCategory.BLOCKS, 1.0f, 1.0f);
                        CraftingBenchBlockScreenHandler.this.lastTakeTime = l;
                    }
                });
                super.onTakeItem(player, stack);
            }

            private List<ItemStack> getInputStacks(Recipe<?> recipe, Inventory inputInventory) {
                List<ItemStack> inputItemStacks = new ArrayList<>(List.of());
                DefaultedList<Ingredient> ingredients = recipe.getIngredients();

                for (Ingredient ingredient : ingredients) {

                    for (int i = 0; i < inputInventory.size(); i++) {
                        if (ingredient.test(inputInventory.getStack(i))) {
                            inputItemStacks.add(inputInventory.getStack(i).getItem().getDefaultStack());
                            if (inputInventory.getStack(i).getCount() > 1) {
                                inputInventory.getStack(i).setCount(inputInventory.getStack(i).getCount() - 1);
                                ingredient = Ingredient.empty();
                            } else if (inputInventory.getStack(i).getCount() == 1) {
                                inputInventory.setStack(i, ItemStack.EMPTY);
                                ingredient = Ingredient.empty();
                            }
                            break;
                        }
                    }
                    if (ingredient != null) {
                        if(!ingredient.isEmpty()) {
                            return new ArrayList<>(List.of());
                        }
                    }
                }
                return inputItemStacks;
            }
        });
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        // TODO
        return null;
    }

    public int getSelectedRecipe() {
        return this.selectedRecipe.get();
    }

    public List<StonecuttingRecipe> getAvailableRecipes() {
        return this.availableRecipes;
    }

    public int getAvailableRecipeCount() {
        return this.availableRecipes.size();
    }

    public boolean canCraft() {
        return !this.getInputInventory().isEmpty() && !this.availableRecipes.isEmpty();
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return CraftingBenchBlockScreenHandler.canUse(this.context, player, BlockRegistry.CRAFTING_BENCH_BLOCK);
    }

    public void setContentsChangedListener(Runnable contentsChangedListener) {
        this.contentsChangedListener = contentsChangedListener;
    }

    private boolean isInBounds(int id) {
        return id >= 0 && id < this.availableRecipes.size();
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        boolean bl = false;
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = this.input.getStack(i);
            if (!itemStack.isOf(this.inputStacks.get(i).getItem())) {
                this.inputStacks.set(i, itemStack.copy());
                bl = true;
            }
        }
        if (bl) {
            this.updateInput(getInputInventory());
        }
    }

    private void updateInput(Inventory input) {
        this.availableRecipes.clear();
        this.selectedRecipe.set(-1);
        this.outputSlot.setStackNoCallbacks(ItemStack.EMPTY);
        if (!input.isEmpty()) {
            this.availableRecipes = this.world.getRecipeManager().getAllMatches(RecipeType.STONECUTTING, input, this.world);
        }
    }

    void populateResult() {
        if (!this.availableRecipes.isEmpty() && this.isInBounds(this.selectedRecipe.get())) {
            StonecuttingRecipe stonecuttingRecipe = this.availableRecipes.get(this.selectedRecipe.get());
            ItemStack itemStack = stonecuttingRecipe.craft(this.getInputInventory(), this.world.getRegistryManager());
            if (itemStack.isItemEnabled(this.world.getEnabledFeatures())) {
                this.output.setLastRecipe(stonecuttingRecipe);
                this.outputSlot.setStackNoCallbacks(itemStack);
                this.currentIngredients = stonecuttingRecipe.getIngredients();
            } else {
                this.outputSlot.setStackNoCallbacks(ItemStack.EMPTY);
            }
        } else {
            this.outputSlot.setStackNoCallbacks(ItemStack.EMPTY);
        }
        this.sendContentUpdates();
    }

    private Inventory getInputInventory() {
        return this.isEnderChestInventoryProviderInReach ? this.expandedInput : this.input;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, pos) -> this.dropInventory(player, this.input));
    }
}
