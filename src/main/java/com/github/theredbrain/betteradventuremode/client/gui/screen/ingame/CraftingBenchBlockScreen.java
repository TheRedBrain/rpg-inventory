package com.github.theredbrain.betteradventuremode.client.gui.screen.ingame;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.api.json_files_backend.CraftingRecipe;
import com.github.theredbrain.betteradventuremode.api.util.ItemUtils;
import com.github.theredbrain.betteradventuremode.client.gui.widget.ItemButtonWidget;
import com.github.theredbrain.betteradventuremode.registry.BlockRegistry;
import com.github.theredbrain.betteradventuremode.registry.CraftingRecipeRegistry;
import com.github.theredbrain.betteradventuremode.screen.CraftingBenchBlockScreenHandler;
import com.github.theredbrain.betteradventuremode.screen.DuckSlotMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
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
import net.minecraft.util.math.MathHelper;

import java.util.List;

@Environment(EnvType.CLIENT)
public class CraftingBenchBlockScreen extends HandledScreen<CraftingBenchBlockScreenHandler> {
    private static final Text TOGGLE_STANDARD_CRAFTING_TAB_0_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.toggle_standard_crafting_button_label.0");
    private static final Text TOGGLE_STANDARD_CRAFTING_TAB_1_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.toggle_standard_crafting_button_label.1");
    private static final Text TOGGLE_STANDARD_CRAFTING_TAB_2_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.toggle_standard_crafting_button_label.2");
    private static final Text TOGGLE_STANDARD_CRAFTING_TAB_3_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.toggle_standard_crafting_button_label.3");
    private static final Text TOGGLE_SPECIAL_CRAFTING_TAB_0_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.toggle_special_crafting_button_label.0");
    private static final Text TOGGLE_SPECIAL_CRAFTING_TAB_1_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.toggle_special_crafting_button_label.1");
    private static final Text TOGGLE_SPECIAL_CRAFTING_TAB_2_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.toggle_special_crafting_button_label.2");
    private static final Text TOGGLE_SPECIAL_CRAFTING_TAB_3_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.toggle_special_crafting_button_label.3");
    private static final Text STANDARD_CRAFT_TAB_0_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.standard_craft_button_label.0");
    private static final Text STANDARD_CRAFT_TAB_1_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.standard_craft_button_label.1");
    private static final Text STANDARD_CRAFT_TAB_2_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.standard_craft_button_label.2");
    private static final Text STANDARD_CRAFT_TAB_3_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.standard_craft_button_label.3");
    private static final Text SPECIAL_CRAFT_TAB_0_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.special_craft_button_label.0");
    private static final Text SPECIAL_CRAFT_TAB_1_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.special_craft_button_label.1");
    private static final Text SPECIAL_CRAFT_TAB_2_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.special_craft_button_label.2");
    private static final Text SPECIAL_CRAFT_TAB_3_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.special_craft_button_label.3");
    private static final Identifier RECIPE_SELECTED_TEXTURE = BetterAdventureMode.identifier("container/crafting_bench_screen/recipe_selected");
    private static final Identifier RECIPE_HIGHLIGHTED_TEXTURE = BetterAdventureMode.identifier("container/crafting_bench_screen/recipe_highlighted");
    private static final Identifier RECIPE_TEXTURE = BetterAdventureMode.identifier("container/crafting_bench_screen/recipe");
    public static final Identifier STORAGE_TAB_BACKGROUND_TEXTURE = BetterAdventureMode.identifier("textures/gui/container/crafting_bench/storage_tab_background.png");
    private static final Identifier SCROLLER_VERTICAL_6_7_TEXTURE = BetterAdventureMode.identifier("scroll_bar/scroller_vertical_6_7");
    private static final Identifier SCROLLER_VERTICAL_6_7_DISABLED_TEXTURE = BetterAdventureMode.identifier("scroll_bar/scroller_vertical_6_7_disabled");

    private ButtonWidget toggleTab0Button;
    private ButtonWidget toggleTab1Button;
    private ButtonWidget toggleTab2Button;
    private ButtonWidget toggleTab3Button;
    private ButtonWidget toggleStorageTabButton;
    private ButtonWidget toggleStandardCraftingButton;
    private ButtonWidget toggleSpecialCraftingButton;
    private ButtonWidget craftButton;
    private int currentTab;
    private CraftingBenchBlockScreenHandler.RecipeType currentRecipeType;
    private float scrollAmount;
    private boolean mouseClicked;
    private int scrollPosition;
    private PlayerEntity playerEntity;

    public CraftingBenchBlockScreen(CraftingBenchBlockScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        handler.setContentsChangedListener(this::onCraft);
        this.playerEntity = inventory.player;
    }

    @Override
    protected void init() {
        this.backgroundWidth = 284;
        this.backgroundHeight = 233;

        this.playerInventoryTitleX = 62;
        this.playerInventoryTitleY = 139;
        super.init();

        this.toggleTab0Button = this.addDrawableChild(ItemButtonWidget.builder(BlockRegistry.CRAFTING_ROOT_BLOCK.asItem().getDefaultStack(), button -> this.toggleTab(0)).dimensions(this.x + 7, this.y + 17, 50, 20).build());
        this.toggleTab0Button.setTooltip(Tooltip.of(BlockRegistry.CRAFTING_ROOT_BLOCK.getName()));

        this.toggleTab1Button = this.addDrawableChild(ItemButtonWidget.builder(BlockRegistry.CRAFTING_TAB_1_PROVIDER_BLOCK.asItem().getDefaultStack(), button -> this.toggleTab(1)).dimensions(this.x + 7, this.y + 41, 50, 20).build());
        this.toggleTab1Button.setTooltip(Tooltip.of(BlockRegistry.CRAFTING_TAB_1_PROVIDER_BLOCK.getName()));

        this.toggleTab2Button = this.addDrawableChild(ItemButtonWidget.builder(BlockRegistry.CRAFTING_TAB_2_PROVIDER_BLOCK.asItem().getDefaultStack(), button -> this.toggleTab(2)).dimensions(this.x + 7, this.y + 65, 50, 20).build());
        this.toggleTab2Button.setTooltip(Tooltip.of(BlockRegistry.CRAFTING_TAB_2_PROVIDER_BLOCK.getName()));

        this.toggleTab3Button = this.addDrawableChild(ItemButtonWidget.builder(BlockRegistry.CRAFTING_TAB_3_PROVIDER_BLOCK.asItem().getDefaultStack(), button -> this.toggleTab(3)).dimensions(this.x + 7, this.y + 89, 50, 20).build());
        this.toggleTab3Button.setTooltip(Tooltip.of(BlockRegistry.CRAFTING_TAB_3_PROVIDER_BLOCK.getName()));

        this.toggleStorageTabButton = this.addDrawableChild(ItemButtonWidget.builder(BlockRegistry.STORAGE_PROVIDER_BLOCK.asItem().getDefaultStack(), button -> this.toggleTab(-1)).dimensions(this.x + 7, this.y + 116, 50, 20).build());
        this.toggleStorageTabButton.setTooltip(Tooltip.of(BlockRegistry.STORAGE_PROVIDER_BLOCK.getName()));

        this.toggleStandardCraftingButton = this.addDrawableChild(ButtonWidget.builder(TOGGLE_STANDARD_CRAFTING_TAB_0_BUTTON_LABEL_TEXT, button -> this.toggleRecipeType(true)).dimensions(this.x + 61, this.y + 17, 65, 20).build());
        this.toggleSpecialCraftingButton = this.addDrawableChild(ButtonWidget.builder(TOGGLE_SPECIAL_CRAFTING_TAB_0_BUTTON_LABEL_TEXT, button -> this.toggleRecipeType(false)).dimensions(this.x + 61, this.y + 41, 65, 20).build());
        this.craftButton = this.addDrawableChild(ButtonWidget.builder(STANDARD_CRAFT_TAB_0_BUTTON_LABEL_TEXT, button -> this.craft()).dimensions(this.x + 130, this.y + 116, 147, 20).build());

        this.handler.calculateUnlockedRecipes();
        this.updateWidgets();
    }

    private void updateWidgets() {
        this.currentTab = handler.getCurrentTab();
        this.currentRecipeType = handler.getCurrentRecipeType();

        for (int i = 36; i < this.handler.slots.size(); i++) {
            boolean bl = this.currentTab == -1;
            if (i < 63) {
                ((DuckSlotMixin) this.handler.slots.get(i)).betteradventuremode$setDisabledOverride(!bl);
            } else {
                ((DuckSlotMixin) this.handler.slots.get(i)).betteradventuremode$setDisabledOverride(bl);
            }
        }

        this.toggleStandardCraftingButton.visible = false;
        this.toggleSpecialCraftingButton.visible = false;
        this.craftButton.visible = false;

        this.toggleTab1Button.visible = false;
        this.toggleTab2Button.visible = false;
        this.toggleTab3Button.visible = false;
        this.toggleStorageTabButton.visible = this.handler.isStorageTabProviderInReach();

        this.toggleTab0Button.active = this.currentTab != 0;
        this.toggleTab1Button.active = this.currentTab != 1;
        this.toggleTab2Button.active = this.currentTab != 2;
        this.toggleTab3Button.active = this.currentTab != 3;
        this.toggleStorageTabButton.active = this.currentTab != -1;

        int y = this.y + 41;

        if (this.handler.isTab1ProviderInReach()) {
            this.toggleTab1Button.visible = true;
            this.toggleTab1Button.setY(y);
            y += 24;
        }
        if (this.handler.isTab2ProviderInReach()) {
            this.toggleTab2Button.visible = true;
            this.toggleTab2Button.setY(y);
            y += 24;
        }
        if (this.handler.isTab3ProviderInReach()) {
            this.toggleTab3Button.visible = true;
            this.toggleTab3Button.setY(y);
        }

        if (this.currentTab >= 0) {

            this.toggleStandardCraftingButton.visible = true;;
            this.toggleSpecialCraftingButton.visible = true;;
            this.craftButton.visible = true;

            this.toggleStandardCraftingButton.active = this.currentRecipeType != CraftingBenchBlockScreenHandler.RecipeType.STANDARD;
            this.toggleSpecialCraftingButton.active = this.currentRecipeType != CraftingBenchBlockScreenHandler.RecipeType.SPECIAL;

            Text toggleStandardCraftingButtonMessage = Text.empty();
            Text toggleSpecialCraftingButtonMessage = Text.empty();
            Text craftButtonMessage = Text.empty();

            if (this.currentTab == 0) {
                toggleStandardCraftingButtonMessage = TOGGLE_STANDARD_CRAFTING_TAB_0_BUTTON_LABEL_TEXT;
                toggleSpecialCraftingButtonMessage = TOGGLE_SPECIAL_CRAFTING_TAB_0_BUTTON_LABEL_TEXT;
                craftButtonMessage = this.currentRecipeType == CraftingBenchBlockScreenHandler.RecipeType.SPECIAL ? SPECIAL_CRAFT_TAB_0_BUTTON_LABEL_TEXT : STANDARD_CRAFT_TAB_0_BUTTON_LABEL_TEXT;
            } else if (this.currentTab == 1) {
                toggleStandardCraftingButtonMessage = TOGGLE_STANDARD_CRAFTING_TAB_1_BUTTON_LABEL_TEXT;
                toggleSpecialCraftingButtonMessage = TOGGLE_SPECIAL_CRAFTING_TAB_1_BUTTON_LABEL_TEXT;
                craftButtonMessage = this.currentRecipeType == CraftingBenchBlockScreenHandler.RecipeType.SPECIAL ? SPECIAL_CRAFT_TAB_1_BUTTON_LABEL_TEXT : STANDARD_CRAFT_TAB_1_BUTTON_LABEL_TEXT;
            } else if (this.currentTab == 2) {
                toggleStandardCraftingButtonMessage = TOGGLE_STANDARD_CRAFTING_TAB_2_BUTTON_LABEL_TEXT;
                toggleSpecialCraftingButtonMessage = TOGGLE_SPECIAL_CRAFTING_TAB_2_BUTTON_LABEL_TEXT;
                craftButtonMessage = this.currentRecipeType == CraftingBenchBlockScreenHandler.RecipeType.SPECIAL ? SPECIAL_CRAFT_TAB_2_BUTTON_LABEL_TEXT : STANDARD_CRAFT_TAB_2_BUTTON_LABEL_TEXT;
            } else if (this.currentTab == 3) {
                toggleStandardCraftingButtonMessage = TOGGLE_STANDARD_CRAFTING_TAB_3_BUTTON_LABEL_TEXT;
                toggleSpecialCraftingButtonMessage = TOGGLE_SPECIAL_CRAFTING_TAB_3_BUTTON_LABEL_TEXT;
                craftButtonMessage = this.currentRecipeType == CraftingBenchBlockScreenHandler.RecipeType.SPECIAL ? SPECIAL_CRAFT_TAB_3_BUTTON_LABEL_TEXT : STANDARD_CRAFT_TAB_3_BUTTON_LABEL_TEXT;
            }

            this.toggleStandardCraftingButton.setMessage(toggleStandardCraftingButtonMessage);
            this.toggleSpecialCraftingButton.setMessage(toggleSpecialCraftingButtonMessage);
            this.craftButton.setMessage(craftButtonMessage);

            this.calculateCraftingStatus();
        }
        this.scrollPosition = 0;
        this.scrollAmount = 0.0f;
    }

    private void toggleTab(int tabIndex) {
        this.handler.setCurrentTab(tabIndex);
        this.updateWidgets();
    }

    private void toggleRecipeType(boolean isStandard) {
        this.handler.setCurrentRecipeType(isStandard);
        this.updateWidgets();
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

        if (this.currentTab >= 0) {

            boolean craftButtonActive = false;

            List<Identifier> activeRecipeList = this.handler.getCurrentCraftingRecipesList();
            int selectedRecipe = this.handler.getSelectedRecipe();
            if (selectedRecipe >= 0) {
                CraftingRecipe craftingRecipe = CraftingRecipeRegistry.getCraftingRecipe(activeRecipeList.get(selectedRecipe));
                if (craftingRecipe != null) {
                    if (this.handler.getTabLevels()[this.currentTab] >= craftingRecipe.getLevel()) {

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
                            Item virtualItem = ItemUtils.getItemStackFromVirtualItemStack(ingredient).getItem();
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
        if (this.currentTab >= 0) {
            int i = this.x + 62;
            int j = this.y + 63;
            int k = this.scrollPosition + 12;

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

            i = this.x + 119;
            j = this.y + 63;
            if (mouseX >= (double)i && mouseX < (double)(i + 6) && mouseY >= (double)j && mouseY < (double)(j + 72)) {
                this.mouseClicked = true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.mouseClicked && this.shouldScroll()) {
            int i = this.y + 62;
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
        if (this.currentTab == -1) {
            context.drawTexture(STORAGE_TAB_BACKGROUND_TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
        } else {
            context.drawTexture(BetterAdventureMode.identifier("textures/gui/container/crafting_bench/tab_" + this.currentTab + "_background.png"), x, y, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
            int k;
            int index = 0;
            List<Identifier> recipeList = this.handler.getCurrentCraftingRecipesList();
            int recipeCounter = recipeList.size();
            for (int i = this.scrollPosition; i < Math.min(this.scrollPosition + 12, recipeCounter); i++) {
                if (i > recipeList.size()) {
                    break;
                }
                CraftingRecipe craftingRecipe = CraftingRecipeRegistry.getCraftingRecipe(recipeList.get(i));
                if (craftingRecipe != null) {

                    x = this.x + 62 + (index % 3 * 18);
                    y = this.y + 63 + (index / 3) * 18;

                    ItemStack resultItemStack = ItemUtils.getItemStackFromVirtualItemStack(craftingRecipe.getResult());
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
            k = (int)(65.0F * this.scrollAmount);
            Identifier identifier = this.shouldScroll() ? SCROLLER_VERTICAL_6_7_TEXTURE : SCROLLER_VERTICAL_6_7_DISABLED_TEXTURE;
            context.drawGuiTexture(identifier, x + 119, y + 63 + k, 6, 7);

            int selectedRecipe = this.handler.getSelectedRecipe();
            if (selectedRecipe != -1) {
                CraftingRecipe craftingRecipe = CraftingRecipeRegistry.getCraftingRecipe(recipeList.get(selectedRecipe));
                ItemStack resultItemStack = ItemUtils.getItemStackFromVirtualItemStack(craftingRecipe.getResult());

                x = this.x + 135;
                y = this.y + 20;
                k = x + y * this.backgroundWidth;
                context.drawItemWithoutEntity(resultItemStack, x, y, k);
                context.drawItemInSlot(this.textRenderer, resultItemStack, x, y);

                context.drawText(this.textRenderer, resultItemStack.getName(), x + 20, y + 4, 4210752, false);
            }
        }
    }

    private boolean shouldScroll() {
        return this.handler.getCurrentCraftingRecipesList().size() > 12;
    }

    protected int getMaxScroll() {
        return (this.handler.getCurrentCraftingRecipesList().size() + 3 - 1) / 3 - 3; // TODO ?
    }

    private void onCraft() {
        BetterAdventureMode.info("onCraft");
        this.updateWidgets();
    }
}
