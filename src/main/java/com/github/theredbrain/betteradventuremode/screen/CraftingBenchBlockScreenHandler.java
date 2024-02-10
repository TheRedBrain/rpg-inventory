package com.github.theredbrain.betteradventuremode.screen;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.api.json_files_backend.CraftingRecipe;
import com.github.theredbrain.betteradventuremode.client.network.DuckClientAdvancementManagerMixin;
import com.github.theredbrain.betteradventuremode.network.packet.CraftFromCraftingBenchPacket;
import com.github.theredbrain.betteradventuremode.registry.CraftingRecipeRegistry;
import com.github.theredbrain.betteradventuremode.registry.ScreenHandlerTypesRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.client.network.ClientAdvancementManager;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CraftingBenchBlockScreenHandler extends ScreenHandler {


    private BlockPos blockPos;
    private boolean isStorageTabProviderInReach;
    private boolean isTab1ProviderInReach;
    private boolean isTab2ProviderInReach;
    private boolean isTab3ProviderInReach;
    long lastTakeTime;
    private final Property selectedRecipe = Property.create();
    private final World world;
    private List<Identifier> craftingRecipesIdentifierList;
    private List<Identifier> tab0StandardCraftingRecipesIdentifierList = new ArrayList<>(List.of());
    private List<Identifier> tab0SpecialCraftingRecipesIdentifierList = new ArrayList<>(List.of());
    private List<Identifier> tab1StandardCraftingRecipesIdentifierList = new ArrayList<>(List.of());
    private List<Identifier> tab1SpecialCraftingRecipesIdentifierList = new ArrayList<>(List.of());
    private List<Identifier> tab2StandardCraftingRecipesIdentifierList = new ArrayList<>(List.of());
    private List<Identifier> tab2SpecialCraftingRecipesIdentifierList = new ArrayList<>(List.of());
    private List<Identifier> tab3StandardCraftingRecipesIdentifierList = new ArrayList<>(List.of());
    private List<Identifier> tab3SpecialCraftingRecipesIdentifierList = new ArrayList<>(List.of());
    private List<Ingredient> currentIngredients = new ArrayList<>(List.of());
    private int[] tabLevels;
    private int currentTab;
    private RecipeType currentRecipeType;
    private final PlayerInventory playerInventory;
    private final EnderChestInventory enderChestInventory;
    Runnable contentsChangedListener = () -> {
    };

    public CraftingBenchBlockScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, playerInventory.player.getEnderChestInventory(), buf.readBlockPos(), buf.readInt(), buf.readByte(), buf.readIntArray());
    }

    public CraftingBenchBlockScreenHandler(int syncId, PlayerInventory playerInventory, EnderChestInventory enderChestInventory, BlockPos blockPos, int  initialTab, byte tabProvidersInReach, int[] tabLevels) {
        super(ScreenHandlerTypesRegistry.CRAFTING_BENCH_BLOCK_SCREEN_HANDLER, syncId);
        this.playerInventory = playerInventory;
        this.world = playerInventory.player.getWorld();
        this.blockPos = blockPos;
        this.currentTab = initialTab;
        this.currentRecipeType = RecipeType.STANDARD;
        this.isStorageTabProviderInReach = (tabProvidersInReach & 1 << 0) != 0;
        this.isTab1ProviderInReach = (tabProvidersInReach & 1 << 1) != 0;
        this.isTab2ProviderInReach = (tabProvidersInReach & 1 << 2) != 0;
        this.isTab3ProviderInReach = (tabProvidersInReach & 1 << 3) != 0;
        this.tabLevels = tabLevels;
        this.enderChestInventory = enderChestInventory;

        this.craftingRecipesIdentifierList = CraftingRecipeRegistry.getCraftingRecipeIdentifiers();

        int i;
        // hotbar 0 - 8
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 62 + i * 18, 209));
        }
        // main inventory 9 - 35
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + (i + 1) * 9, 62 + j * 18, 151 + i * 18));
            }
        }
        // ender chest inventory 36 - 62
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(enderChestInventory, j + i * 9, 62 + j * 18, 83 + i * 18));
            }
        }
        this.addProperty(this.selectedRecipe);
        this.selectedRecipe.set(-1);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        // TODO
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (id == -1) {
            if (this.selectedRecipe.get() > -1 && this.currentTab >= 0) {
                List<Identifier> currentRecipeList = this.getCurrentCraftingRecipesList();
                ClientPlayNetworking.send(
                        new CraftFromCraftingBenchPacket(
                                currentRecipeList.get(this.selectedRecipe.get()).toString()
                        )
                );
            }
        } else if (this.isInBounds(id)) {
            this.selectedRecipe.set(id);
        }
        return true;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public int getSelectedRecipe() {
        return this.selectedRecipe.get();
    }

    public int[] getTabLevels() {
        return tabLevels;
    }

    public EnderChestInventory getEnderChestInventory() {
        return enderChestInventory;
    }

    public PlayerInventory getPlayerInventory() {
        return playerInventory;
    }

    public boolean isStorageTabProviderInReach() {
        return this.isStorageTabProviderInReach;
    }

    public boolean isTab1ProviderInReach() {
        return this.isTab1ProviderInReach;
    }

    public boolean isTab2ProviderInReach() {
        return this.isTab2ProviderInReach;
    }

    public boolean isTab3ProviderInReach() {
        return this.isTab3ProviderInReach;
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(int currentTab) {
        this.currentTab = currentTab;
        this.selectedRecipe.set(-1);
    }

    public RecipeType getCurrentRecipeType() {
        return currentRecipeType;
    }

    public void setCurrentRecipeType(boolean isStandard) {
        this.currentRecipeType = isStandard ? CraftingBenchBlockScreenHandler.RecipeType.STANDARD : CraftingBenchBlockScreenHandler.RecipeType.SPECIAL;
        this.selectedRecipe.set(-1);
    }

    public List<Identifier> getCurrentCraftingRecipesList() {
        return this.currentTab == 3 ? (this.currentRecipeType == CraftingBenchBlockScreenHandler.RecipeType.SPECIAL ? this.tab3SpecialCraftingRecipesIdentifierList : this.tab3StandardCraftingRecipesIdentifierList) : this.currentTab == 2 ? (this.currentRecipeType == CraftingBenchBlockScreenHandler.RecipeType.SPECIAL ? this.tab2SpecialCraftingRecipesIdentifierList : this.tab2StandardCraftingRecipesIdentifierList) : this.currentTab == 1 ? (this.currentRecipeType == CraftingBenchBlockScreenHandler.RecipeType.SPECIAL ? this.tab1SpecialCraftingRecipesIdentifierList : this.tab1StandardCraftingRecipesIdentifierList) : this.currentTab == 0 ? (this.currentRecipeType == CraftingBenchBlockScreenHandler.RecipeType.SPECIAL ? this.tab0SpecialCraftingRecipesIdentifierList : this.tab0StandardCraftingRecipesIdentifierList) : new ArrayList<>();

    }

    public void setContentsChangedListener(Runnable contentsChangedListener) {
//        BetterAdventureMode.info("setContentsChangedListener");
        this.contentsChangedListener = contentsChangedListener;
    }

    public void runContentsChangedListener() {
//        BetterAdventureMode.info("runContentsChangedListener()");
        this.contentsChangedListener.run();
    }

    private boolean isInBounds(int id) {
        if (this.currentTab >= 0) {
            boolean bl = id < this.getCurrentCraftingRecipesList().size();
            return id >= 0 && bl;
        }
        return false;
    }

    public void calculateUnlockedRecipes() {

//        BetterAdventureMode.info("calculateUnlockedRecipes()");

        ClientAdvancementManager advancementHandler = null;
        String unlockAdvancementIdentifier;

        if (this.world.isClient && this.playerInventory.player instanceof ClientPlayerEntity clientPlayerEntity) {
            advancementHandler = clientPlayerEntity.networkHandler.getAdvancementHandler();
        }

        if (advancementHandler != null) {
            this.tab0StandardCraftingRecipesIdentifierList.clear();
            this.tab1StandardCraftingRecipesIdentifierList.clear();
            this.tab2StandardCraftingRecipesIdentifierList.clear();
            this.tab3StandardCraftingRecipesIdentifierList.clear();
            this.tab0SpecialCraftingRecipesIdentifierList.clear();
            this.tab1SpecialCraftingRecipesIdentifierList.clear();
            this.tab2SpecialCraftingRecipesIdentifierList.clear();
            this.tab3SpecialCraftingRecipesIdentifierList.clear();

            for (int i = 0; i < this.craftingRecipesIdentifierList.size(); i++) {

                CraftingRecipe craftingRecipe = CraftingRecipeRegistry.getCraftingRecipe(this.craftingRecipesIdentifierList.get(i));
                unlockAdvancementIdentifier = craftingRecipe.getUnlockAdvancement();

                AdvancementEntry unlockAdvancementEntry = null;
                if (!unlockAdvancementIdentifier.equals("")) {
                    unlockAdvancementEntry = advancementHandler.get(Identifier.tryParse(unlockAdvancementIdentifier));
                }
                boolean bl = !world.getGameRules().getBoolean(GameRules.DO_LIMITED_CRAFTING) || unlockAdvancementIdentifier.equals("") || (unlockAdvancementEntry != null && ((DuckClientAdvancementManagerMixin) advancementHandler.getManager()).betteradventuremode$getAdvancementProgress(unlockAdvancementEntry).isDone());

                if (bl) {
                    int tab = craftingRecipe.getTab();
                    RecipeType recipeType = craftingRecipe.getRecipeType();
                    if (tab == 0) {
                        if (recipeType == RecipeType.STANDARD) {
                            this.tab0StandardCraftingRecipesIdentifierList.add(this.craftingRecipesIdentifierList.get(i));
                        } else {
                            this.tab0SpecialCraftingRecipesIdentifierList.add(this.craftingRecipesIdentifierList.get(i));
                        }
                    } else if (tab == 1) {
                        if (recipeType == RecipeType.STANDARD) {
                            this.tab1StandardCraftingRecipesIdentifierList.add(this.craftingRecipesIdentifierList.get(i));
                        } else {
                            this.tab1SpecialCraftingRecipesIdentifierList.add(this.craftingRecipesIdentifierList.get(i));
                        }
                    } else if (tab == 2) {
                        if (recipeType == RecipeType.STANDARD) {
                            this.tab2StandardCraftingRecipesIdentifierList.add(this.craftingRecipesIdentifierList.get(i));
                        } else {
                            this.tab2SpecialCraftingRecipesIdentifierList.add(this.craftingRecipesIdentifierList.get(i));
                        }
                    } else if (tab == 3) {
                        if (recipeType == RecipeType.STANDARD) {
                            this.tab3StandardCraftingRecipesIdentifierList.add(this.craftingRecipesIdentifierList.get(i));
                        } else {
                            this.tab3SpecialCraftingRecipesIdentifierList.add(this.craftingRecipesIdentifierList.get(i));
                        }
                    }
                }
            }
        }
    }

    public static enum RecipeType implements StringIdentifiable {
        STANDARD("standard"),
        SPECIAL("special");

        private final String name;

        private RecipeType(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        public static Optional<RecipeType> byName(String name) {
            return Arrays.stream(RecipeType.values()).filter(recipeType -> recipeType.asString().equals(name)).findFirst();
        }

        public Text asText() {
            return Text.translatable("gui.crafting_bench_block.recipeType." + this.name);
        }
    }
}
