package com.github.theredbrain.rpginventory.gui.screen.ingame;

import com.github.theredbrain.rpgcrafting.RPGCraftingClient;
import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.screen.DuckSlotMixin;
import com.github.theredbrain.rpginventory.screen.MannequinScreenHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class MannequinScreen extends HandledScreen<MannequinScreenHandler> {
	private static final int RECIPE_FIELD_HEIGTH = 4;
	private static final int RECIPE_FIELD_WIDTH = 3;
	private static final Text HAND_CRAFT_BUTTON_LABEL_TEXT = Text.translatable("gui.hand_crafting.hand_craft_button_label");
	private static final Identifier RECIPE_SELECTED_TEXTURE = Identifier.ofVanilla("container/stonecutter/recipe_selected");
	private static final Identifier RECIPE_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("container/stonecutter/recipe_highlighted");
	private static final Identifier RECIPE_TEXTURE = Identifier.ofVanilla("container/stonecutter/recipe");
	public static final Identifier SLOT_TEXTURE = Identifier.ofVanilla("textures/gui/sprites/container/slot.png");
	public static final Identifier MANNEQUIN_BACKGROUND_TEXTURE = RPGInventory.identifier("textures/gui/container/mannequin.png");
	public static final Identifier BUTTON_TEXTURE = Identifier.ofVanilla("widget/button");
	public static final Identifier BUTTON_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("widget/button_highlighted");
	public static final Text EQUIP_BUTTON_LABEL = Text.translatable("gui.mannequin.equip_button_label");
	public static final Text UNEQUIP_BUTTON_LABEL = Text.translatable("gui.mannequin.unequip_button_label");
	//	public static final Identifier SLOT_TEXTURE = Identifier.ofVanilla("textures/gui/sprites/container/slot.png");
//	private static final Identifier SCROLLER_VERTICAL_6_7_TEXTURE = RPGCrafting.identifier("scroll_bar/scroller_vertical_6_7");
//	private static final Identifier SCROLLER_VERTICAL_6_7_DISABLED_TEXTURE = RPGCrafting.identifier("scroll_bar/scroller_vertical_6_7_disabled");
//	private final int hotbarSize;
//	private final int inventorySize;

//	private List<RecipeEntry<RPGCraftingRecipe>> recipeList = new ArrayList<>();

	private ButtonWidget equipButton;
	private ButtonWidget unEquipButton;

//	private MutableText craftingResultDescription = Text.empty();

	private float scrollAmount;
	private boolean mouseClicked;
	private int scrollPosition;
//	private final PlayerEntity playerEntity;

	public MannequinScreen(MannequinScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
//		this.playerEntity = inventory.player;
//		this.hotbarSize = RPGInventory.getActiveHotbarSize(inventory.player);
//		this.inventorySize = RPGInventory.getActiveInventorySize(inventory.player);
	}

	private void buttonCallback(int index) {
		if (this.client != null && this.client.interactionManager != null && this.handler.onButtonClick(this.client.player, index)) {

			MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));

			this.client.interactionManager.clickButton(this.handler.syncId, index);
		}
	}

	@Override
	protected void init() {
		this.backgroundWidth = 176;
		this.backgroundHeight = 229;

		this.titleX = 98;
		this.titleY = 6;
		this.playerInventoryTitleX = 8;
		this.playerInventoryTitleY = 6;

		super.init();

		this.addDrawableChild(ButtonWidget.builder(EQUIP_BUTTON_LABEL, button -> this.buttonCallback(0)).dimensions(this.x + 7, this.y + 125, 72, 20).build());
		this.addDrawableChild(ButtonWidget.builder(UNEQUIP_BUTTON_LABEL, button -> this.buttonCallback(1)).dimensions(this.x + 97, this.y + 125, 72, 20).build());
	}

	@Override
	protected void drawMouseoverTooltip(DrawContext context, int x, int y) {
		super.drawMouseoverTooltip(context, x, y);
		if (RPGInventoryClient.CLIENT_CONFIG.rpgInventoryScreenSection.show_slot_tooltips.get() && this.handler.getCursorStack().isEmpty() && this.focusedSlot != null && !this.focusedSlot.hasStack()) {
			if (this.focusedSlot instanceof DuckSlotMixin slotWithTooltip) {
				List<Text> list = slotWithTooltip.rpginventory$getSlotTooltipText();
				if (!list.isEmpty()) {
					context.drawTooltip(this.textRenderer, list, Optional.empty(), x, y);
				}
			}
		}
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(context, mouseX, mouseY);
	}

	@Override
	public void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		int i = this.x;
		int j = this.y;
		int k;
		int m;
		int activeSpellSlotAmount = 0;
		int inventorySize = 0;
		int hotbarSize = 0;
		if (this.client != null && this.client.player != null) {
			activeSpellSlotAmount = (int) ((DuckPlayerEntityMixin) this.client.player).rpginventory$getActiveSpellSlotAmount();

			hotbarSize = RPGInventory.getActiveHotbarSize(this.client.player);
			inventorySize = RPGInventory.getActiveInventorySize(this.client.player);
		}

		context.drawTexture(MANNEQUIN_BACKGROUND_TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);

		ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;
		if (serverConfig.inventorySlots.is_belt_slot_enabled.get()) {
			context.drawTexture(SLOT_TEXTURE, i + 61, j + 70, 0, 0, 18, 18, 18, 18);
			context.drawTexture(SLOT_TEXTURE, i + 61 + 90, j + 70, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_gloves_slot_enabled.get()) {
			context.drawTexture(SLOT_TEXTURE, i + 61, j + 52, 0, 0, 18, 18, 18, 18);
			context.drawTexture(SLOT_TEXTURE, i + 61 + 90, j + 52, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_necklace_slot_enabled.get()) {
			context.drawTexture(SLOT_TEXTURE, i + 43, j + 16, 0, 0, 18, 18, 18, 18);
			context.drawTexture(SLOT_TEXTURE, i + 43 + 90, j + 16, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_ring_1_slot_enabled.get()) {
			context.drawTexture(SLOT_TEXTURE, i + 61, j + 34, 0, 0, 18, 18, 18, 18);
			context.drawTexture(SLOT_TEXTURE, i + 61 + 90, j + 34, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_ring_2_slot_enabled.get()) {
			context.drawTexture(SLOT_TEXTURE, i + 43, j + 34, 0, 0, 18, 18, 18, 18);
			context.drawTexture(SLOT_TEXTURE, i + 43 + 90, j + 34, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_shoulders_slot_enabled.get()) {
			context.drawTexture(SLOT_TEXTURE, i + 25, j + 16, 0, 0, 18, 18, 18, 18);
			context.drawTexture(SLOT_TEXTURE, i + 25 + 90, j + 16, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_relic_slot_enabled.get()) {
			context.drawTexture(SLOT_TEXTURE, i + 61, j + 16, 0, 0, 18, 18, 18, 18);
			context.drawTexture(SLOT_TEXTURE, i + 61 + 90, j + 16, 0, 0, 18, 18, 18, 18);
		}

		if (activeSpellSlotAmount > 0) {
			context.drawTexture(SLOT_TEXTURE, i + 7, j + 88, 0, 0, 18, 18, 18, 18);
		}
		if (activeSpellSlotAmount > 1) {
			context.drawTexture(SLOT_TEXTURE, i + 25, j + 88, 0, 0, 18, 18, 18, 18);
		}
		if (activeSpellSlotAmount > 2) {
			context.drawTexture(SLOT_TEXTURE, i + 43, j + 88, 0, 0, 18, 18, 18, 18);
		}
		if (activeSpellSlotAmount > 3) {
			context.drawTexture(SLOT_TEXTURE, i + 61, j + 88, 0, 0, 18, 18, 18, 18);
		}
		if (activeSpellSlotAmount > 4) {
			context.drawTexture(SLOT_TEXTURE, i + 7, j + 106, 0, 0, 18, 18, 18, 18);
		}
		if (activeSpellSlotAmount > 5) {
			context.drawTexture(SLOT_TEXTURE, i + 25, j + 106, 0, 0, 18, 18, 18, 18);
		}
		if (activeSpellSlotAmount > 6) {
			context.drawTexture(SLOT_TEXTURE, i + 43, j + 106, 0, 0, 18, 18, 18, 18);
		}
		if (activeSpellSlotAmount > 7) {
			context.drawTexture(SLOT_TEXTURE, i + 61, j + 106, 0, 0, 18, 18, 18, 18);
		}

		if (RPGInventory.isHandSlotOverhaulActive()) {
			context.drawTexture(SLOT_TEXTURE, i + 25, j + 52, 0, 0, 18, 18, 18, 18);
			context.drawTexture(SLOT_TEXTURE, i + 25, j + 70, 0, 0, 18, 18, 18, 18);
			context.drawTexture(SLOT_TEXTURE, i + 43, j + 70, 0, 0, 18, 18, 18, 18);

			context.drawTexture(SLOT_TEXTURE, i + 90 + 25, j + 52, 0, 0, 18, 18, 18, 18);
			context.drawTexture(SLOT_TEXTURE, i + 90 + 25, j + 70, 0, 0, 18, 18, 18, 18);
			context.drawTexture(SLOT_TEXTURE, i + 90 + 43, j + 70, 0, 0, 18, 18, 18, 18);
		}

//		int r = mouseX - x;
//		int s = mouseY - y;
//
//		for (int i = 0; i < 2; i++) {
////			q = 6839882;
//			if (r >= 7.0 + i * 90 && s >= 125.0 && r < 7.0 + 72.0 + i * 90 && s < 125.0 + 20.0) {
//				context.drawGuiTexture(BUTTON_HIGHLIGHTED_TEXTURE, x + 7 + i * 90, y + 125, 72, 20);
////				q = 16777088;
//			} else {
//				context.drawGuiTexture(BUTTON_TEXTURE, x + 7 + i * 90, y + 125, 72, 20);
//			}
//			RenderSystem.disableBlend();
//			context.drawTextWithShadow(this.textRenderer, BUTTON_TEXTS[i], x + 9 + i * 90, y + 130, 16777215);
//		}
		boolean showInactiveSlots = RPGInventoryClient.showInactiveInventorySlots();
		for (k = 0; k < (showInactiveSlots ? 27 : Math.min(inventorySize, 27)); ++k) {
			m = (k / 9);
			context.drawTexture(SLOT_TEXTURE, i + 7 + (k - (m * 9)) * 18, j + 146 + (m * 18), 0, 0, 18, 18, 18, 18);
		}
		for (k = 0; k < (showInactiveSlots ? 9 : Math.min(hotbarSize, 9)); ++k) {
			context.drawTexture(SLOT_TEXTURE, i + 7 + k * 18, j + 204, 0, 0, 18, 18, 18, 18);
		}


//		int index = 0;
//		List<RecipeEntry<RPGCraftingRecipe>> recipeList = this.recipeList;
//		int recipeCounter = recipeList.size();
//		for (int i = this.scrollPosition; i < Math.min(this.scrollPosition + (RECIPE_FIELD_HEIGTH * RECIPE_FIELD_WIDTH), recipeCounter); i++) {
//			if (i > recipeList.size()) {
//				break;
//			}
//			RPGCraftingRecipe craftingRecipe = recipeList.get(i).value();
//			if (craftingRecipe != null && this.client != null && this.client.world != null) {
//
//				x = this.x + 62 + (index % RECIPE_FIELD_WIDTH * 18);
//				y = this.y + 63 + (index / RECIPE_FIELD_WIDTH) * 18;
//
//				ItemStack resultItemStack = craftingRecipe.getResult(this.client.world.getRegistryManager());
//				Identifier identifier;
//				if (i == this.handler.getSelectedRecipe()) {
//					identifier = RECIPE_SELECTED_TEXTURE;
//				} else if (mouseX >= x && mouseY >= y && mouseX < x + 18 && mouseY < y + 18) {
//					identifier = RECIPE_HIGHLIGHTED_TEXTURE;
//				} else {
//					identifier = RECIPE_TEXTURE;
//				}
//				context.drawGuiTexture(identifier, x, y, 18, 18);
//				context.drawItemWithoutEntity(resultItemStack, x + 1, y + 1);
//				context.drawItemInSlot(this.textRenderer, resultItemStack, x + 1, y + 1);
//
//				index++;
//			}
//		}
//		x = this.x;
//		y = this.y;
//		k = (int) (65.0F * this.scrollAmount);
//		Identifier identifier = this.shouldScroll() ? SCROLLER_VERTICAL_6_7_TEXTURE : SCROLLER_VERTICAL_6_7_DISABLED_TEXTURE;
//		context.drawGuiTexture(identifier, x + 119, y + 63 + k, 6, 7);
//
//		int selectedRecipe = this.handler.getSelectedRecipe();
//		if (selectedRecipe != -1 && this.client != null && this.client.world != null && selectedRecipe < recipeList.size()) {
//
//			ItemStack resultItemStack = this.handler.getCraftingResultInventory().getStack(0);
//			Text resultName;
//			int count = resultItemStack.getCount();
//			if (count > 1) {
//				resultName = Text.translatable("gui.rpg_crafting.recipe_result.results_title", resultItemStack.getName(), resultItemStack.getCount());
//			} else {
//				resultName = resultItemStack.getName();
//			}
//			context.drawText(this.textRenderer, resultName, x + 155, y + 26, 16777215, false);
//
//			if (this.craftingResultDescription != Text.EMPTY) {
//				context.drawTextWrapped(this.textRenderer, this.craftingResultDescription, x + 139, y + 42, 132, 16777215);
//			}
//
//			context.drawText(this.textRenderer, Text.translatable("gui.rpg_crafting.recipe_result.ingredients_title").formatted(Formatting.UNDERLINE), x + 135, y + 80, 16777215, false);
//
//		}
	}
}
