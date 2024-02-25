package com.github.theredbrain.betteradventuremode.client.gui.screen.ingame;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.data.CraftingRecipe;
import com.github.theredbrain.betteradventuremode.util.ItemUtils;
import com.github.theredbrain.betteradventuremode.client.gui.widget.ItemButtonWidget;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.network.packet.ToggleUseStashForCraftingPacket;
import com.github.theredbrain.betteradventuremode.network.packet.UpdateCraftingBenchScreenHandlerPropertyPacket;
import com.github.theredbrain.betteradventuremode.registry.BlockRegistry;
import com.github.theredbrain.betteradventuremode.registry.CraftingRecipeRegistry;
import com.github.theredbrain.betteradventuremode.screen.CraftingBenchBlockScreenHandler;
import com.github.theredbrain.betteradventuremode.screen.DuckSlotMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
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
    private static final Text TOGGLE_USE_STASH_FOR_CRAFTING_ON_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.toggle_use_stash_for_crafting_button_label.on");
    private static final Text TOGGLE_USE_STASH_FOR_CRAFTING_OFF_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.toggle_use_stash_for_crafting_button_label.off");
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
    public static final Identifier STASH_AREA_0_BACKGROUND_TEXTURE = BetterAdventureMode.identifier("textures/gui/container/crafting_bench/stash_area_0_background.png");
    public static final Identifier STASH_AREA_1_BACKGROUND_TEXTURE = BetterAdventureMode.identifier("textures/gui/container/crafting_bench/stash_area_1_background.png");
    public static final Identifier STASH_AREA_2_BACKGROUND_TEXTURE = BetterAdventureMode.identifier("textures/gui/container/crafting_bench/stash_area_2_background.png");
    public static final Identifier STASH_AREA_3_BACKGROUND_TEXTURE = BetterAdventureMode.identifier("textures/gui/container/crafting_bench/stash_area_3_background.png");
    public static final Identifier STASH_AREA_4_BACKGROUND_TEXTURE = BetterAdventureMode.identifier("textures/gui/container/crafting_bench/stash_area_4_background.png");
    private static final Identifier SCROLLER_VERTICAL_6_7_TEXTURE = BetterAdventureMode.identifier("scroll_bar/scroller_vertical_6_7");
    private static final Identifier SCROLLER_VERTICAL_6_7_DISABLED_TEXTURE = BetterAdventureMode.identifier("scroll_bar/scroller_vertical_6_7_disabled");

    private ButtonWidget toggleUseStashForCraftingButton;

    private ButtonWidget toggleTab0Button;
    private ButtonWidget toggleTab1Button;
    private ButtonWidget toggleTab2Button;
    private ButtonWidget toggleTab3Button;
    private ButtonWidget toggleStorageTabButton;
    private ButtonWidget toggleStandardCraftingButton;
    private ButtonWidget toggleSpecialCraftingButton;
    private ButtonWidget craftButton;
    private int currentTab;
    private float scrollAmount;
    private boolean mouseClicked;
    private boolean useStashForCrafting;
    private boolean isStorageArea0ProviderInReach;
    private boolean isStorageArea1ProviderInReach;
    private boolean isStorageArea2ProviderInReach;
    private boolean isStorageArea3ProviderInReach;
    private boolean isStorageArea4ProviderInReach;
    private int scrollPosition;
    private final PlayerEntity playerEntity;

    public CraftingBenchBlockScreen(CraftingBenchBlockScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.playerEntity = inventory.player;
    }

    @Override
    protected void init() {
        this.backgroundWidth = 284;
        this.backgroundHeight = 233;

        this.playerInventoryTitleX = 62;
        this.playerInventoryTitleY = 139;
        super.init();

        this.isStorageArea0ProviderInReach = this.handler.isStorageArea0ProviderInReach();
        this.isStorageArea1ProviderInReach = this.handler.isStorageArea1ProviderInReach();
        this.isStorageArea2ProviderInReach = this.handler.isStorageArea2ProviderInReach();
        this.isStorageArea3ProviderInReach = this.handler.isStorageArea3ProviderInReach();
        this.isStorageArea4ProviderInReach = this.handler.isStorageArea4ProviderInReach();

        this.useStashForCrafting = ((DuckPlayerEntityMixin) this.playerEntity).betteradventuremode$useStashForCrafting();
        this.toggleUseStashForCraftingButton = this.addDrawableChild(ButtonWidget.builder(useStashForCrafting ? TOGGLE_USE_STASH_FOR_CRAFTING_ON_BUTTON_LABEL_TEXT : TOGGLE_USE_STASH_FOR_CRAFTING_OFF_BUTTON_LABEL_TEXT, button -> this.toggleUseStorageForCrafting()).dimensions(this.x + 61, this.y + 17, 104, 20).build());

        this.toggleTab0Button = this.addDrawableChild(ItemButtonWidget.builder(BlockRegistry.CRAFTING_ROOT_BLOCK.asItem().getDefaultStack(), button -> this.toggleTab(0)).dimensions(this.x + 7, this.y + 17, 50, 20).build());
        this.toggleTab0Button.setTooltip(Tooltip.of(BlockRegistry.CRAFTING_ROOT_BLOCK.getName()));

        this.toggleTab1Button = this.addDrawableChild(ItemButtonWidget.builder(BlockRegistry.CRAFTING_TAB_1_PROVIDER_BLOCK.asItem().getDefaultStack(), button -> this.toggleTab(1)).dimensions(this.x + 7, this.y + 41, 50, 20).build());
        this.toggleTab1Button.setTooltip(Tooltip.of(BlockRegistry.CRAFTING_TAB_1_PROVIDER_BLOCK.getName()));

        this.toggleTab2Button = this.addDrawableChild(ItemButtonWidget.builder(BlockRegistry.CRAFTING_TAB_2_PROVIDER_BLOCK.asItem().getDefaultStack(), button -> this.toggleTab(2)).dimensions(this.x + 7, this.y + 65, 50, 20).build());
        this.toggleTab2Button.setTooltip(Tooltip.of(BlockRegistry.CRAFTING_TAB_2_PROVIDER_BLOCK.getName()));

        this.toggleTab3Button = this.addDrawableChild(ItemButtonWidget.builder(BlockRegistry.CRAFTING_TAB_3_PROVIDER_BLOCK.asItem().getDefaultStack(), button -> this.toggleTab(3)).dimensions(this.x + 7, this.y + 89, 50, 20).build());
        this.toggleTab3Button.setTooltip(Tooltip.of(BlockRegistry.CRAFTING_TAB_3_PROVIDER_BLOCK.getName()));

        this.toggleStorageTabButton = this.addDrawableChild(ItemButtonWidget.builder(BlockRegistry.STORAGE_AREA_0_PROVIDER_BLOCK.asItem().getDefaultStack(), button -> this.toggleTab(-1)).dimensions(this.x + 7, this.y + 116, 50, 20).build());
        this.toggleStorageTabButton.setTooltip(Tooltip.of(BlockRegistry.STORAGE_AREA_0_PROVIDER_BLOCK.getName()));

        this.toggleStandardCraftingButton = this.addDrawableChild(ButtonWidget.builder(TOGGLE_STANDARD_CRAFTING_TAB_0_BUTTON_LABEL_TEXT, button -> this.toggleRecipeType(true)).dimensions(this.x + 61, this.y + 17, 65, 20).build());
        this.toggleSpecialCraftingButton = this.addDrawableChild(ButtonWidget.builder(TOGGLE_SPECIAL_CRAFTING_TAB_0_BUTTON_LABEL_TEXT, button -> this.toggleRecipeType(false)).dimensions(this.x + 61, this.y + 41, 65, 20).build());
        this.craftButton = this.addDrawableChild(ButtonWidget.builder(STANDARD_CRAFT_TAB_0_BUTTON_LABEL_TEXT, button -> this.craft()).dimensions(this.x + 130, this.y + 116, 147, 20).build());

        this.handler.calculateUnlockedRecipes();
        this.updateWidgets();

        ClientPlayNetworking.send(new UpdateCraftingBenchScreenHandlerPropertyPacket(
                1
        ));
    }

    @Override
    protected void handledScreenTick() {
        if (this.handler.shouldScreenCalculateCraftingStatus() != 0) {
            ClientPlayNetworking.send(new UpdateCraftingBenchScreenHandlerPropertyPacket(
                    0
            ));
            this.calculateCraftingStatus();
        }
    }

    private void updateWidgets() {
        this.currentTab = handler.getCurrentTab();
        boolean isCurrentRecipeTypeStandard = handler.getCurrentRecipeType() == CraftingBenchBlockScreenHandler.RecipeType.STANDARD;

        for (int i = 36; i < this.handler.slots.size(); i++) {
            boolean showStorageSlots = this.currentTab == -1;
            if (i < 42) {
                ((DuckSlotMixin) this.handler.slots.get(i)).betteradventuremode$setDisabledOverride(!(showStorageSlots && this.isStorageArea0ProviderInReach));
            } else if (i < 50) {
                ((DuckSlotMixin) this.handler.slots.get(i)).betteradventuremode$setDisabledOverride(!(showStorageSlots && this.isStorageArea1ProviderInReach));
            } else if (i < 62) {
                ((DuckSlotMixin) this.handler.slots.get(i)).betteradventuremode$setDisabledOverride(!(showStorageSlots && this.isStorageArea2ProviderInReach));
            } else if (i < 76) {
                ((DuckSlotMixin) this.handler.slots.get(i)).betteradventuremode$setDisabledOverride(!(showStorageSlots && this.isStorageArea3ProviderInReach));
            } else {
                ((DuckSlotMixin) this.handler.slots.get(i)).betteradventuremode$setDisabledOverride(!(showStorageSlots && this.isStorageArea4ProviderInReach));
            }
        }

        this.toggleUseStashForCraftingButton.visible = false;

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

            this.toggleStandardCraftingButton.visible = true;
            this.toggleSpecialCraftingButton.visible = true;
            this.craftButton.visible = true;

            this.toggleStandardCraftingButton.active = !isCurrentRecipeTypeStandard;
            this.toggleSpecialCraftingButton.active = isCurrentRecipeTypeStandard;

            Text toggleStandardCraftingButtonMessage = Text.empty();
            Text toggleSpecialCraftingButtonMessage = Text.empty();
            Text craftButtonMessage = Text.empty();

            if (this.currentTab == 0) {
                toggleStandardCraftingButtonMessage = TOGGLE_STANDARD_CRAFTING_TAB_0_BUTTON_LABEL_TEXT;
                toggleSpecialCraftingButtonMessage = TOGGLE_SPECIAL_CRAFTING_TAB_0_BUTTON_LABEL_TEXT;
                craftButtonMessage = isCurrentRecipeTypeStandard ? STANDARD_CRAFT_TAB_0_BUTTON_LABEL_TEXT : SPECIAL_CRAFT_TAB_0_BUTTON_LABEL_TEXT;
            } else if (this.currentTab == 1) {
                toggleStandardCraftingButtonMessage = TOGGLE_STANDARD_CRAFTING_TAB_1_BUTTON_LABEL_TEXT;
                toggleSpecialCraftingButtonMessage = TOGGLE_SPECIAL_CRAFTING_TAB_1_BUTTON_LABEL_TEXT;
                craftButtonMessage = isCurrentRecipeTypeStandard ? STANDARD_CRAFT_TAB_1_BUTTON_LABEL_TEXT : SPECIAL_CRAFT_TAB_1_BUTTON_LABEL_TEXT;
            } else if (this.currentTab == 2) {
                toggleStandardCraftingButtonMessage = TOGGLE_STANDARD_CRAFTING_TAB_2_BUTTON_LABEL_TEXT;
                toggleSpecialCraftingButtonMessage = TOGGLE_SPECIAL_CRAFTING_TAB_2_BUTTON_LABEL_TEXT;
                craftButtonMessage = isCurrentRecipeTypeStandard ? STANDARD_CRAFT_TAB_2_BUTTON_LABEL_TEXT : SPECIAL_CRAFT_TAB_2_BUTTON_LABEL_TEXT;
            } else if (this.currentTab == 3) {
                toggleStandardCraftingButtonMessage = TOGGLE_STANDARD_CRAFTING_TAB_3_BUTTON_LABEL_TEXT;
                toggleSpecialCraftingButtonMessage = TOGGLE_SPECIAL_CRAFTING_TAB_3_BUTTON_LABEL_TEXT;
                craftButtonMessage = isCurrentRecipeTypeStandard ? STANDARD_CRAFT_TAB_3_BUTTON_LABEL_TEXT : SPECIAL_CRAFT_TAB_3_BUTTON_LABEL_TEXT;
            }

            this.toggleStandardCraftingButton.setMessage(toggleStandardCraftingButtonMessage);
            this.toggleSpecialCraftingButton.setMessage(toggleSpecialCraftingButtonMessage);
            this.craftButton.setMessage(craftButtonMessage);

        } else {

            this.toggleUseStashForCraftingButton.visible = true;

        }

        this.scrollPosition = 0;
        this.scrollAmount = 0.0f;
    }

    private void toggleUseStorageForCrafting() {
        this.useStashForCrafting = !this.useStashForCrafting;
        ClientPlayNetworking.send(new ToggleUseStashForCraftingPacket(
                this.useStashForCrafting
        ));
        this.toggleUseStashForCraftingButton.setMessage(this.useStashForCrafting ? TOGGLE_USE_STASH_FOR_CRAFTING_ON_BUTTON_LABEL_TEXT : TOGGLE_USE_STASH_FOR_CRAFTING_OFF_BUTTON_LABEL_TEXT);
        this.updateWidgets();

        ClientPlayNetworking.send(new UpdateCraftingBenchScreenHandlerPropertyPacket(
                1
        ));
    }

    private void toggleTab(int tabIndex) {
        this.handler.setCurrentTab(tabIndex);
        this.handler.setCurrentRecipeType(true);
        this.updateWidgets();

        ClientPlayNetworking.send(new UpdateCraftingBenchScreenHandlerPropertyPacket(
                1
        ));
    }

    private void toggleRecipeType(boolean isStandard) {
        this.handler.setCurrentRecipeType(isStandard);
        this.updateWidgets();

        ClientPlayNetworking.send(new UpdateCraftingBenchScreenHandlerPropertyPacket(
                1
        ));
    }

    private void craft() {
        if (this.client != null && this.client.interactionManager != null) {
            if (this.handler.onButtonClick(this.client.player, -1)) {
                MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_STONECUTTER_TAKE_RESULT, 1.0F));
                this.client.interactionManager.clickButton(this.handler.syncId, -1);
            }
        }
    }

    private void calculateCraftingStatus() {

        BetterAdventureMode.info("calculateCraftingStatus");

        if (this.currentTab >= 0) {

            boolean craftButtonActive = false;

            List<Identifier> activeRecipeList = this.handler.getCurrentCraftingRecipesList();
            int selectedRecipe = this.handler.getSelectedRecipe();
            if (selectedRecipe >= 0) {
                CraftingRecipe craftingRecipe = CraftingRecipeRegistry.getCraftingRecipe(activeRecipeList.get(selectedRecipe));
                if (craftingRecipe != null) {
                    if (this.handler.getTabLevels()[this.currentTab] >= craftingRecipe.getLevel()) {

                        int playerInventorySize = this.handler.getPlayerInventory().size();

                        int stash0InventorySize = this.useStashForCrafting && this.isStorageArea0ProviderInReach ? 6 : 0;

                        int stash1InventorySize = this.useStashForCrafting && this.isStorageArea1ProviderInReach ? 8 : 0;

                        int stash2InventorySize = this.useStashForCrafting && this.isStorageArea2ProviderInReach ? 12 : 0;

                        int stash3InventorySize = this.useStashForCrafting && this.isStorageArea3ProviderInReach ? 14 : 0;

                        int stash4InventorySize = this.useStashForCrafting && this.isStorageArea4ProviderInReach ? 21 : 0;

                        Inventory playerInventoryCopy = new SimpleInventory(playerInventorySize);
                        Inventory stash0InventoryCopy = new SimpleInventory(stash0InventorySize);
                        Inventory stash1InventoryCopy = new SimpleInventory(stash1InventorySize);
                        Inventory stash2InventoryCopy = new SimpleInventory(stash2InventorySize);
                        Inventory stash3InventoryCopy = new SimpleInventory(stash3InventorySize);
                        Inventory stash4InventoryCopy = new SimpleInventory(stash4InventorySize);

                        int j;
                        for (j = 0; j < playerInventorySize; j++) {
                            playerInventoryCopy.setStack(j, this.handler.getPlayerInventory().getStack(j).copy());
                        }

                        for (j = 0; j < stash0InventorySize; j++) {
                            stash0InventoryCopy.setStack(j, this.handler.getEnderChestInventory().getStack(j).copy());
                        }

                        for (j = 0; j < stash1InventorySize; j++) {
                            stash1InventoryCopy.setStack(j, this.handler.getStashInventory().getStack(j).copy());
                        }

                        for (j = 0; j < stash2InventorySize; j++) {
                            stash2InventoryCopy.setStack(j, this.handler.getStashInventory().getStack(8 + j).copy());
                        }

                        for (j = 0; j < stash3InventorySize; j++) {
                            stash3InventoryCopy.setStack(j, this.handler.getStashInventory().getStack(20 + j).copy());
                        }

                        for (j = 0; j < stash4InventorySize; j++) {
                            stash4InventoryCopy.setStack(j, this.handler.getEnderChestInventory().getStack(6 + j).copy());
                        }

                        boolean bl = true;
                        ItemStack itemStack;

                        for (ItemUtils.VirtualItemStack ingredient : craftingRecipe.getIngredients()) {

                            Item virtualItem = ItemUtils.getItemStackFromVirtualItemStack(ingredient).getItem();
                            int ingredientCount = ingredient.getCount();

                            // TODO play test which inventory normally contains the most crafting ingredients and should be checked first

                            for (j = 0; j < playerInventorySize; j++) {
                                if (playerInventoryCopy.getStack(j).isOf(virtualItem)) {
                                    itemStack = playerInventoryCopy.getStack(j).copy();
                                    int stackCount = itemStack.getCount();
                                    if (stackCount >= ingredientCount) {
                                        itemStack.setCount(stackCount - ingredientCount);
                                        playerInventoryCopy.setStack(j, itemStack);
                                        ingredientCount = 0;
                                        break;
                                    } else {
                                        playerInventoryCopy.setStack(j, ItemStack.EMPTY);
                                        ingredientCount = ingredientCount - stackCount;
                                    }
                                }
                            }
                            if (ingredientCount == 0) {
                                break;
                            }

                            for (j = 0; j < stash0InventorySize; j++) {
                                if (stash0InventoryCopy.getStack(j).isOf(virtualItem)) {
                                    itemStack = stash0InventoryCopy.getStack(j).copy();
                                    int stackCount = itemStack.getCount();
                                    if (stackCount >= ingredientCount) {
                                        itemStack.setCount(stackCount - ingredientCount);
                                        stash0InventoryCopy.setStack(j, itemStack);
                                        ingredientCount = 0;
                                        break;
                                    } else {
                                        stash0InventoryCopy.setStack(j, ItemStack.EMPTY);
                                        ingredientCount = ingredientCount - stackCount;
                                    }
                                }
                            }
                            if (ingredientCount == 0) {
                                break;
                            }

                            for (j = 0; j < stash1InventorySize; j++) {
                                if (stash1InventoryCopy.getStack(j).isOf(virtualItem)) {
                                    itemStack = stash1InventoryCopy.getStack(j).copy();
                                    int stackCount = itemStack.getCount();
                                    if (stackCount >= ingredientCount) {
                                        itemStack.setCount(stackCount - ingredientCount);
                                        stash1InventoryCopy.setStack(j, itemStack);
                                        ingredientCount = 0;
                                        break;
                                    } else {
                                        stash1InventoryCopy.setStack(j, ItemStack.EMPTY);
                                        ingredientCount = ingredientCount - stackCount;
                                    }
                                }
                            }
                            if (ingredientCount == 0) {
                                break;
                            }

                            for (j = 0; j < stash2InventorySize; j++) {
                                if (stash2InventoryCopy.getStack(j).isOf(virtualItem)) {
                                    itemStack = stash2InventoryCopy.getStack(j).copy();
                                    int stackCount = itemStack.getCount();
                                    if (stackCount >= ingredientCount) {
                                        itemStack.setCount(stackCount - ingredientCount);
                                        stash2InventoryCopy.setStack(j, itemStack);
                                        ingredientCount = 0;
                                        break;
                                    } else {
                                        stash2InventoryCopy.setStack(j, ItemStack.EMPTY);
                                        ingredientCount = ingredientCount - stackCount;
                                    }
                                }
                            }
                            if (ingredientCount == 0) {
                                break;
                            }

                            for (j = 0; j < stash3InventorySize; j++) {
                                if (stash3InventoryCopy.getStack(j).isOf(virtualItem)) {
                                    itemStack = stash3InventoryCopy.getStack(j).copy();
                                    int stackCount = itemStack.getCount();
                                    if (stackCount >= ingredientCount) {
                                        itemStack.setCount(stackCount - ingredientCount);
                                        stash3InventoryCopy.setStack(j, itemStack);
                                        ingredientCount = 0;
                                        break;
                                    } else {
                                        stash3InventoryCopy.setStack(j, ItemStack.EMPTY);
                                        ingredientCount = ingredientCount - stackCount;
                                    }
                                }
                            }
                            if (ingredientCount == 0) {
                                break;
                            }

                            for (j = 0; j < stash4InventorySize; j++) {
                                if (stash4InventoryCopy.getStack(j).isOf(virtualItem)) {
                                    itemStack = stash4InventoryCopy.getStack(j).copy();
                                    int stackCount = itemStack.getCount();
                                    if (stackCount >= ingredientCount) {
                                        itemStack.setCount(stackCount - ingredientCount);
                                        stash4InventoryCopy.setStack(j, itemStack);
                                        ingredientCount = 0;
                                        break;
                                    } else {
                                        stash4InventoryCopy.setStack(j, ItemStack.EMPTY);
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
//                    this.updateWidgets();

                    ClientPlayNetworking.send(new UpdateCraftingBenchScreenHandlerPropertyPacket(
                            1
                    ));
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
    public boolean mouseScrolled(double mouseX, double mouseY/*, double horizontalAmount*/, double verticalAmount) {
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
            if (this.isStorageArea0ProviderInReach) {
                context.drawTexture(STASH_AREA_0_BACKGROUND_TEXTURE, x + 169, y + 18, 0, 0, 108, 18, 108, 18);
            }
            if (this.isStorageArea1ProviderInReach) {
                context.drawTexture(STASH_AREA_1_BACKGROUND_TEXTURE, x + 205, y + 41, 0, 0, 72, 36, 72, 36);
            }
            if (this.isStorageArea2ProviderInReach) {
                context.drawTexture(STASH_AREA_2_BACKGROUND_TEXTURE, x + 205, y + 82, 0, 0, 72, 54, 72, 54);
            }
            if (this.isStorageArea3ProviderInReach) {
                context.drawTexture(STASH_AREA_3_BACKGROUND_TEXTURE, x + 68, y + 41, 0, 0, 126, 36, 126, 36);
            }
            if (this.isStorageArea4ProviderInReach) {
                context.drawTexture(STASH_AREA_4_BACKGROUND_TEXTURE, x + 68, y + 82, 0, 0, 126, 54, 126, 54);
            }
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
//                    context.drawGuiTexture(identifier, x, y, 18, 18);
                    context.drawTexture(identifier, x, y, 0, 0, 18, 18);
                    context.drawItemWithoutEntity(resultItemStack, x + 1, y + 1/*, k*/);
                    context.drawItemInSlot(this.textRenderer, resultItemStack, x + 1, y + 1);

                    index++;
                }
            }
            x = this.x;
            y = this.y;
            k = (int)(65.0F * this.scrollAmount);
            Identifier identifier = this.shouldScroll() ? SCROLLER_VERTICAL_6_7_TEXTURE : SCROLLER_VERTICAL_6_7_DISABLED_TEXTURE;
//            context.drawGuiTexture(identifier, x + 119, y + 63 + k, 6, 7);
            context.drawTexture(identifier, x + 119, y + 63 + k, 0, 0, 6, 7);

            int selectedRecipe = this.handler.getSelectedRecipe();
            if (selectedRecipe != -1) {
                CraftingRecipe craftingRecipe = CraftingRecipeRegistry.getCraftingRecipe(recipeList.get(selectedRecipe));
                ItemStack resultItemStack = ItemUtils.getItemStackFromVirtualItemStack(craftingRecipe.getResult());

                x = this.x + 135;
                y = this.y + 20;
                k = x + y * this.backgroundWidth;
                context.drawItemWithoutEntity(resultItemStack, x, y/*, k*/);
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
}
