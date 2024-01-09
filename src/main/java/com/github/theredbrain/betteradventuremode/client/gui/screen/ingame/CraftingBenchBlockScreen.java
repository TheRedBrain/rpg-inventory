package com.github.theredbrain.betteradventuremode.client.gui.screen.ingame;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.api.json_files_backend.CraftingRecipe;
import com.github.theredbrain.betteradventuremode.api.util.ItemUtils;
import com.github.theredbrain.betteradventuremode.block.CraftingRootBlock;
import com.github.theredbrain.betteradventuremode.registry.CraftingRecipeRegistry;
import com.github.theredbrain.betteradventuremode.screen.CraftingBenchBlockScreenHandler;
import com.github.theredbrain.betteradventuremode.screen.DuckSlotMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class CraftingBenchBlockScreen extends HandledScreen<CraftingBenchBlockScreenHandler> {
    private static final Text CRAFT_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.craft_button_label");
    private static final Text UPGRADE_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.upgrade_button_label");
    private static final Identifier RECIPE_SELECTED_TEXTURE = BetterAdventureMode.identifier("container/crafting_bench_screen/recipe_selected");
    private static final Identifier RECIPE_HIGHLIGHTED_TEXTURE = BetterAdventureMode.identifier("container/crafting_bench_screen/recipe_highlighted");
    private static final Identifier RECIPE_TEXTURE = BetterAdventureMode.identifier("container/crafting_bench_screen/recipe");
    public static final Identifier CRAFTING_BENCH_DEFAULT_BACKGROUND_TEXTURE = BetterAdventureMode.identifier("textures/gui/container/crafting_bench_default_background.png");
    public static final Identifier CRAFTING_BENCH_STORAGE_BACKGROUND_TEXTURE = BetterAdventureMode.identifier("textures/gui/container/crafting_bench_storage_background.png");
    private static final Identifier SCROLLER_VERTICAL_6_7_TEXTURE = BetterAdventureMode.identifier("scroll_bar/scroller_vertical_6_7");
    private static final Identifier SCROLLER_VERTICAL_6_7_DISABLED_TEXTURE = BetterAdventureMode.identifier("scroll_bar/scroller_vertical_6_7_disabled");

    private ButtonWidget craftButton;
    private CraftingRootBlock.Tab currentTab;
    private final boolean isStorageTabEnabled;
    private final boolean isSmithyTabEnabled;
    private RecipeType currentRecipeType;
    private float scrollAmount;
    private boolean mouseClicked;
    private int scrollPosition;
    private PlayerEntity playerEntity;

    public CraftingBenchBlockScreen(CraftingBenchBlockScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        handler.setContentsChangedListener(this::onCraft);
        this.playerEntity = inventory.player;
        this.isStorageTabEnabled = handler.isStorageTabProviderInReach();
        this.isSmithyTabEnabled = handler.isSmithyTabProviderInReach();
//        this.availableCraftingBenchRecipes = handler.getAvailableCraftingBenchRecipes();
//        this.availableSmithyRecipes = handler.getAvailableSmithyRecipes();
    }

    @Override
    protected void init() {
        this.currentTab = handler.getCurrentTab();
        this.currentRecipeType = RecipeType.CRAFTING;

        for (int i = 36; i < this.handler.slots.size(); i++) {
            boolean bl = this.currentTab == CraftingRootBlock.Tab.STORAGE;
            if (i < 63) {
                ((DuckSlotMixin) this.handler.slots.get(i)).betteradventuremode$setDisabledOverride(!bl);
            } else {
                ((DuckSlotMixin) this.handler.slots.get(i)).betteradventuremode$setDisabledOverride(bl);
            }
        }


        this.backgroundWidth = 176;
        this.backgroundHeight = 227;
        this.x = (this.width - this.backgroundWidth) / 2;
        this.y = ((this.height - this.backgroundHeight) / 2);

        this.playerInventoryTitleY = 133;
        super.init();

        this.craftButton = this.addDrawableChild(ButtonWidget.builder(CRAFT_BUTTON_LABEL_TEXT, button -> this.craft()).dimensions(this.x + 76, this.y + 88, 93, 20).build());

        this.handler.calculateUnlockedRecipes();
        this.updateWidgets();
    }

    private void updateWidgets() {

        this.craftButton.visible = false;

        if (this.currentTab != CraftingRootBlock.Tab.STORAGE) {

            this.craftButton.visible = true;

            if (this.currentRecipeType == RecipeType.UPGRADING) {
                this.craftButton.setMessage(UPGRADE_BUTTON_LABEL_TEXT);
            } else {
                this.craftButton.setMessage(CRAFT_BUTTON_LABEL_TEXT);
            }

            calculateCraftingStatus();
        }
        this.scrollPosition = 0;
        this.scrollAmount = 0.0f;
    }

    private void craft() {
        // TODO temporary until onCraft() is called by this.handler.contentsChangedListener is properly called by CraftingPacketReceiver
        this.calculateCraftingStatus();
        if (!this.craftButton.active) {
            return;
        }

        if (this.client != null && this.client.interactionManager != null) {
            if (this.handler.onButtonClick(this.client.player, -1)) {
                MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_STONECUTTER_TAKE_RESULT, 1.0F));
                this.client.interactionManager.clickButton(this.handler.syncId, -1);
            }
        }
    }

    private void calculateCraftingStatus() {

        if (this.currentTab != CraftingRootBlock.Tab.STORAGE) {

            boolean craftButtonActive = false;

            List<Identifier> activeRecipeList = this.currentTab == CraftingRootBlock.Tab.SMITHY ? (this.currentRecipeType == RecipeType.UPGRADING ? this.handler.getSmithyUpgradingRecipesIdentifierList() : this.handler.getSmithyCraftingRecipesIdentifierList()) : (this.currentRecipeType == RecipeType.UPGRADING ? this.handler.getCraftingBenchUpgradingRecipesIdentifierList() : this.handler.getCraftingBenchCraftingRecipesIdentifierList());
            int selectedRecipe = this.handler.getSelectedRecipe();
            if (selectedRecipe >= 0) {
                CraftingRecipe craftingRecipe = CraftingRecipeRegistry.getCraftingRecipe(activeRecipeList.get(selectedRecipe));
                if (craftingRecipe != null) {
                    if (this.handler.getTabLevels().get(this.currentTab.asString()) >= craftingRecipe.getLevel()) {

                        int playerInventorySize = this.handler.getPlayerInventory().size();

                        int enderChestInventorySize = this.handler.getEnderChestInventory().size();

                        int inventorySize = this.handler.isStorageTabProviderInReach() ? playerInventorySize + enderChestInventorySize : playerInventorySize;

                        Inventory inventory = new SimpleInventory(inventorySize);
                        ItemStack itemStack;
                        for (int k = 0; k < inventorySize; k++) {
                            if (k < playerInventorySize) {
                                itemStack = this.handler.getPlayerInventory().getStack(k);
                            } else {
                                itemStack = this.handler.getEnderChestInventory().getStack(k - playerInventorySize);
                            }
                            inventory.setStack(k, itemStack.copy());
                        }

                        boolean bl = true;
                        for (ItemUtils.VirtualItemStack ingredient : craftingRecipe.getIngredients()) {
                            Item virtualItem = ItemUtils.getItemStackFromShopItemStack(ingredient).getItem();
                            int ingredientCount = ingredient.getCount();

                            for (int j = 0; j < inventorySize; j++) {
                                if (inventory.getStack(j).isOf(virtualItem)) {
                                    itemStack = inventory.getStack(j).copy();
                                    int stackCount = itemStack.getCount();
                                    if (stackCount >= ingredientCount) {
                                        itemStack.setCount(stackCount - ingredientCount);
                                        inventory.setStack(j, itemStack);
                                        ingredientCount = 0;
                                        break;
                                    } else {
                                        inventory.setStack(j, ItemStack.EMPTY);
                                        ingredientCount = ingredientCount - stackCount;
                                    }
                                }
                            }
                            if (ingredientCount > 0) {
                                bl = false;
                            }
                        }
                        if (bl) {
                            craftButtonActive = true;
                        }
                    }
                }
            }
            this.craftButton.active = craftButtonActive;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseClicked = false;
        if (this.currentTab != CraftingRootBlock.Tab.STORAGE) {
            int i = this.x + 8;
            int j = this.y + 53;
            int k = this.scrollPosition + 9;

            for(int l = this.scrollPosition; l < k; ++l) {
                int m = l - this.scrollPosition;
                double d = mouseX - (double)(i + m % 3 * 18);
                double e = mouseY - (double)(j + m / 3 * 18);
                if (d >= 0.0 && e >= 0.0 && d < 18.0 && e < 18.0 && this.client != null && this.client.interactionManager != null && this.handler.onButtonClick(this.client.player, l)) {
                    MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                    this.client.interactionManager.clickButton(this.handler.syncId, l);
                    this.updateWidgets();
                    return true;
                }
            }

            i = this.x + 65;
            j = this.y + 53;
            if (mouseX >= (double)i && mouseX < (double)(i + 6) && mouseY >= (double)j && mouseY < (double)(j + 54)) {
                this.mouseClicked = true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.mouseClicked && this.shouldScroll()) {
            int i = this.y + 52;
            int j = i + 54;
            this.scrollAmount = ((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
            this.scrollAmount = MathHelper.clamp(this.scrollAmount, 0.0F, 1.0F);
            this.scrollPosition = (int)((double)(this.scrollAmount * (float)this.getMaxScroll()) + 0.5) * 3;
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.shouldScroll()) {
            int i = this.getMaxScroll();
            float f = (float)verticalAmount / (float)i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount - f, 0.0F, 1.0F);
            this.scrollPosition = (int)((double)(this.scrollAmount * (float)i) + 0.5) * 3;
        }

        return true;
    }

    @Override
    public void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = this.x;
        int y = this.y;
        if (this.currentTab == CraftingRootBlock.Tab.STORAGE) {
            context.drawTexture(CRAFTING_BENCH_STORAGE_BACKGROUND_TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
        } else {
            context.drawTexture(CRAFTING_BENCH_DEFAULT_BACKGROUND_TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
            int k;
            int index = 0;
            int recipeCounter = this.currentTab == CraftingRootBlock.Tab.SMITHY ? this.handler.getSmithyCraftingRecipesSize() : this.handler.getCraftingBenchCraftingRecipesSize();
            List<Identifier> recipeList = this.currentTab == CraftingRootBlock.Tab.SMITHY ? this.handler.getSmithyCraftingRecipesIdentifierList() : this.handler.getCraftingBenchCraftingRecipesIdentifierList();
            for (int i = this.scrollPosition; i < Math.min(this.scrollPosition + 9, recipeCounter); i++) {
                if (i > recipeList.size()) {
                    break;
                }
                CraftingRecipe craftingRecipe = CraftingRecipeRegistry.getCraftingRecipe(recipeList.get(i));
                if (craftingRecipe != null) {

                    x = this.x + 8 + (index % 3 * 18);
                    y = this.y + 53 + (index / 3) * 18;

                    ItemStack resultItemStack = ItemUtils.getItemStackFromShopItemStack(craftingRecipe.getResult());
                    k = x + y * this.backgroundWidth;
                    Identifier identifier;
                    if (i == this.handler.getSelectedRecipe()) {
                        identifier = RECIPE_SELECTED_TEXTURE;
                    } else if (mouseX >= x && mouseY >= y && mouseX < x + 18 && mouseY < y + 18) {
                        identifier = RECIPE_HIGHLIGHTED_TEXTURE;
                    } else {
                        identifier = RECIPE_TEXTURE;
                    }
                    context.drawGuiTexture(identifier, x, y, 18, 18);
                    context.drawItemWithoutEntity(resultItemStack, x + 1, y + 1, k);
                    context.drawItemInSlot(this.textRenderer, resultItemStack, x + 1, y + 1);

                    index++;
                }
            }
            x = this.x;
            y = this.y;
            k = (int)(47.0F * this.scrollAmount);
            Identifier identifier = this.shouldScroll() ? SCROLLER_VERTICAL_6_7_TEXTURE : SCROLLER_VERTICAL_6_7_DISABLED_TEXTURE;
            context.drawGuiTexture(identifier, x + 65, y + 53 + k, 6, 7);

            int selectedRecipe = this.handler.getSelectedRecipe();
            if (selectedRecipe != -1) {
                CraftingRecipe craftingRecipe = CraftingRecipeRegistry.getCraftingRecipe(recipeList.get(selectedRecipe));
                ItemStack resultItemStack = ItemUtils.getItemStackFromShopItemStack(craftingRecipe.getResult());

                x = this.x + 80;
                y = this.y + 40;
                k = x + y * this.backgroundWidth;
                context.drawItemWithoutEntity(resultItemStack, x, y, k);
                context.drawItemInSlot(this.textRenderer, resultItemStack, x, y);

                context.drawText(this.textRenderer, resultItemStack.getName(), x + 20, y + 4, 4210752, false);
            }
        }
    }

    private boolean shouldScroll() {
        return (this.currentTab == CraftingRootBlock.Tab.CRAFTING_BENCH && this.handler.getCraftingBenchCraftingRecipesSize() > 9) || (this.currentTab == CraftingRootBlock.Tab.SMITHY && this.handler.getSmithyCraftingRecipesSize() > 9);
    }

    protected int getMaxScroll() {
        return ((this.currentTab == CraftingRootBlock.Tab.SMITHY ? this.handler.getSmithyCraftingRecipesSize() : this.handler.getCraftingBenchCraftingRecipesSize()) + 3 - 1) / 3 - 3;
    }

    private void onCraft() {
        BetterAdventureMode.info("onCraft");
        this.updateWidgets();
    }

    public static enum RecipeType implements StringIdentifiable {
        CRAFTING("crafting"),
        UPGRADING("upgrading");

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
