package com.github.theredbrain.betteradventuremode.screen;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.api.json_files_backend.CraftingRecipe;
import com.github.theredbrain.betteradventuremode.block.CraftingRootBlock;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CraftingBenchBlockScreenHandler extends ScreenHandler {


    private BlockPos blockPos;
    private boolean isStorageTabProviderInReach;
    private boolean isSmithyTabProviderInReach;
    long lastTakeTime;
    private final Property selectedRecipe = Property.create();
    private final World world;
    private List<Identifier> craftingRecipesIdentifierList;
    private List<Identifier> craftingBenchCraftingRecipesIdentifierList = new ArrayList<>(List.of());
    private List<Identifier> craftingBenchUpgradingRecipesIdentifierList = new ArrayList<>(List.of());
    private List<Identifier> smithyCraftingRecipesIdentifierList = new ArrayList<>(List.of());
    private List<Identifier> smithyUpgradingRecipesIdentifierList = new ArrayList<>(List.of());
    private List<Ingredient> currentIngredients = new ArrayList<>(List.of());
    private int craftingBenchCraftingRecipesSize = 0;
    private int craftingBenchUpgradingRecipesSize = 0;
    private int smithyCraftingRecipesSize = 0;
    private int smithyUpgradingRecipesSize = 0;
    private Map<String, Integer> tabLevels;
    private CraftingRootBlock.Tab currentTab;
    private final PlayerInventory playerInventory;
    private final EnderChestInventory enderChestInventory;
    Runnable contentsChangedListener = () -> {
    };

    public CraftingBenchBlockScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, playerInventory.player.getEnderChestInventory(), buf.readBlockPos(), CraftingRootBlock.Tab.byName(buf.readString()).orElseGet(() -> CraftingRootBlock.Tab.CRAFTING_BENCH), buf.readBoolean(), buf.readBoolean(), buf.readMap(PacketByteBuf::readString, PacketByteBuf::readInt));
    }

    public CraftingBenchBlockScreenHandler(int syncId, PlayerInventory playerInventory, EnderChestInventory enderChestInventory, BlockPos blockPos, CraftingRootBlock.Tab initialTab, boolean isStorageTabProviderInReach, boolean isSmithyTabProviderInReach, Map<String, Integer> tabLevels) {
        super(ScreenHandlerTypesRegistry.CRAFTING_BENCH_BLOCK_SCREEN_HANDLER, syncId);
        this.playerInventory = playerInventory;
        this.world = playerInventory.player.getWorld();
        this.blockPos = blockPos;
        this.currentTab = initialTab;
        this.isStorageTabProviderInReach = isStorageTabProviderInReach;
        this.isSmithyTabProviderInReach = isSmithyTabProviderInReach;
        this.tabLevels = tabLevels;
        this.enderChestInventory = enderChestInventory;

        this.craftingRecipesIdentifierList = CraftingRecipeRegistry.getCraftingRecipeIdentifiers();

        int i;
        // hotbar 0 - 8
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 203));
        }
        // main inventory 9 - 35
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + (i + 1) * 9, 8 + j * 18, 145 + i * 18));
            }
        }
        // ender chest inventory 36 - 62
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(enderChestInventory, j + i * 9, 8 + j * 18, 77 + i * 18));
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
            if (this.selectedRecipe.get() > -1 && this.currentTab != CraftingRootBlock.Tab.STORAGE) {
                List<Identifier> currentRecipeList = this.currentTab == CraftingRootBlock.Tab.SMITHY ? this.smithyCraftingRecipesIdentifierList : this.craftingBenchCraftingRecipesIdentifierList;
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

    public Map<String, Integer> getTabLevels() {
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

    public boolean isSmithyTabProviderInReach() {
        return this.isSmithyTabProviderInReach;
    }

    public CraftingRootBlock.Tab getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(CraftingRootBlock.Tab currentTab) {
        this.currentTab = currentTab;
    }

    public int getCraftingBenchCraftingRecipesSize() {
        return craftingBenchCraftingRecipesSize;
    }

    public int getCraftingBenchUpgradingRecipesSize() {
        return craftingBenchUpgradingRecipesSize;
    }

    public int getSmithyCraftingRecipesSize() {
        return smithyCraftingRecipesSize;
    }

    public int getSmithyUpgradingRecipesSize() {
        return smithyUpgradingRecipesSize;
    }

    public List<Identifier> getCraftingBenchCraftingRecipesIdentifierList() {
        return craftingBenchCraftingRecipesIdentifierList;
    }

    public List<Identifier> getCraftingBenchUpgradingRecipesIdentifierList() {
        return craftingBenchUpgradingRecipesIdentifierList;
    }

    public List<Identifier> getSmithyCraftingRecipesIdentifierList() {
        return smithyCraftingRecipesIdentifierList;
    }

    public List<Identifier> getSmithyUpgradingRecipesIdentifierList() {
        return smithyUpgradingRecipesIdentifierList;
    }

    public void setContentsChangedListener(Runnable contentsChangedListener) {
        BetterAdventureMode.info("setContentsChangedListener");
        this.contentsChangedListener = contentsChangedListener;
    }

    public void runContentsChangedListener() {
        BetterAdventureMode.info("runContentsChangedListener()");
        this.contentsChangedListener.run();
    }

    private boolean isInBounds(int id) {
        if (this.currentTab != CraftingRootBlock.Tab.STORAGE) {
            boolean bl = this.currentTab == CraftingRootBlock.Tab.SMITHY ? id < this.smithyCraftingRecipesSize : id < this.craftingBenchCraftingRecipesSize;
            return id >= 0 && bl;
        }
        return false;
    }

    public void calculateUnlockedRecipes() {

        BetterAdventureMode.info("calculateUnlockedRecipes()");

        ClientAdvancementManager advancementHandler = null;
        String unlockAdvancementIdentifier;

        if (this.world.isClient && this.playerInventory.player instanceof ClientPlayerEntity clientPlayerEntity) {

            advancementHandler = clientPlayerEntity.networkHandler.getAdvancementHandler();
            this.craftingBenchCraftingRecipesIdentifierList.clear();
            this.smithyCraftingRecipesIdentifierList.clear();
        }

        if (advancementHandler != null) {
            for (int i = 0; i < this.craftingRecipesIdentifierList.size(); i++) {

                CraftingRecipe craftingRecipe = CraftingRecipeRegistry.getCraftingRecipe(this.craftingRecipesIdentifierList.get(i));
                unlockAdvancementIdentifier = craftingRecipe.getUnlockAdvancement();

                AdvancementEntry unlockAdvancementEntry = null;
                if (!unlockAdvancementIdentifier.equals("")) {
                    unlockAdvancementEntry = advancementHandler.get(Identifier.tryParse(unlockAdvancementIdentifier));
                }
                boolean bl = !world.getGameRules().getBoolean(GameRules.DO_LIMITED_CRAFTING) || unlockAdvancementIdentifier.equals("") || (unlockAdvancementEntry != null && ((DuckClientAdvancementManagerMixin) advancementHandler.getManager()).betteradventuremode$getAdvancementProgress(unlockAdvancementEntry).isDone());

                if (bl) {
                    CraftingRootBlock.Tab tab = craftingRecipe.getTab();
                    if (tab == CraftingRootBlock.Tab.CRAFTING_BENCH) {
                        this.craftingBenchCraftingRecipesIdentifierList.add(this.craftingRecipesIdentifierList.get(i));
                    } else if (tab == CraftingRootBlock.Tab.SMITHY) {
                        this.smithyCraftingRecipesIdentifierList.add(this.craftingRecipesIdentifierList.get(i));
                    }
                }
            }
        }

        this.craftingBenchCraftingRecipesSize = this.craftingBenchCraftingRecipesIdentifierList.size();
        this.smithyCraftingRecipesSize = this.smithyCraftingRecipesIdentifierList.size();
    }
}
