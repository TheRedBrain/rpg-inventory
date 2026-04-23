package com.github.theredbrain.rpginventory.gui.screen.ingame;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.config.ClientConfig;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.gui.widget.ToggleInventoryScreenWidget;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.github.theredbrain.rpginventory.screen.DuckPlayerScreenHandlerMixin;
import com.google.common.collect.Ordering;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class RPGInventoryScreen extends AbstractContainerScreen<InventoryMenu> {
	public static final Identifier ADVENTURE_INVENTORY_MAIN_BACKGROUND_TEXTURE = RPGInventory.identifier("textures/gui/container/adventure_inventory/adventure_inventory_main_background.png");
	public static final Identifier ADVENTURE_INVENTORY_SIDES_BACKGROUND_TEXTURE = RPGInventory.identifier("textures/gui/container/adventure_inventory/adventure_inventory_sides_background.png");
	public static final Identifier SLOT_TEXTURE = Identifier.withDefaultNamespace("textures/gui/sprites/container/slot.png");
	private static final Identifier EFFECT_BACKGROUND_SMALL_TEXTURE = Identifier.withDefaultNamespace("container/inventory/effect_background_small");
	private static final Identifier CRAFTING_SLOTS_BACKGROUND_TEXTURE = RPGInventory.identifier("textures/gui/container/adventure_inventory/adventure_inventory_crafting_background.png");
	private static final Identifier SCROLL_BAR_BACKGROUND_8_206_TEXTURE = RPGInventory.identifier("textures/gui/sprites/scroll_bar/scroll_bar_background_8_206.png");
	private static final Identifier SCROLL_BAR_BACKGROUND_8_32_TEXTURE = RPGInventory.identifier("textures/gui/sprites/scroll_bar/scroll_bar_background_8_32.png");
	private static final Identifier SCROLLER_VERTICAL_6_7_TEXTURE = RPGInventory.identifier("textures/gui/sprites/scroll_bar/scroller_vertical_6_7.png");
	private static final Identifier SCROLLER_VERTICAL_6_15_TEXTURE = RPGInventory.identifier("textures/gui/sprites/scroll_bar/scroller_vertical_6_15.png");
	private static final Component CRAFTING_LABEL_TEXT = Component.translatable("gui.adventure_inventory_screen.crafting_label");
	private static final Component SPELLS_LABEL_TEXT = Component.translatable("gui.adventure_inventory_screen.spells_label");
	private static final Component TOGGLE_SHOW_ATTRIBUTES_BUTTON_TOOLTIP_TEXT_OFF = Component.translatable("gui.adventureInventory.toggleShowAttributeScreenButton.off.tooltip");
	private static final Component TOGGLE_SHOW_ATTRIBUTES_BUTTON_TOOLTIP_TEXT_ON = Component.translatable("gui.adventureInventory.toggleShowAttributeScreenButton.on.tooltip");
	private static final Component TOGGLE_SHOW_EFFECTS_BUTTON_TOOLTIP_TEXT_OFF = Component.translatable("gui.adventureInventory.toggleShowEffectScreenButton.off.tooltip");
	private static final Component TOGGLE_SHOW_EFFECTS_BUTTON_TOOLTIP_TEXT_ON = Component.translatable("gui.adventureInventory.toggleShowEffectScreenButton.on.tooltip");
	private static final Component OPEN_BACKPACK_BUTTON_LABEL_TEXT = Component.translatable("gui.adventureInventory.openBackpackButton");
	private static final Component OPEN_HAND_CRAFTING_BUTTON_LABEL_TEXT = Component.translatable("gui.adventureInventory.openHandCraftingButton");
	private static final int MAX_ATTRIBUTE_SCREEN_LINES = 15;
	private float mouseX;
	private float mouseY;
	private Button openBackpackButton;
	private Button openHandCraftingButton;
	private final int sidesBackgroundWidth = 130;
	private Button toggleShowAttributeScreenButton;
	protected boolean showAttributeScreen;
	private Button toggleShowEffectScreenButton;
	private boolean showEffectScreen;
	private int attributeListSize = 0;
	private int oldEffectsListSize = 0;
	private List<MobEffectInstance> foodEffectsList = new ArrayList<>(Collections.emptyList());
	private List<MobEffectInstance> negativeEffectsList = new ArrayList<>(Collections.emptyList());
	private List<MobEffectInstance> positiveEffectsList = new ArrayList<>(Collections.emptyList());
	private List<MobEffectInstance> neutralEffectsList = new ArrayList<>(Collections.emptyList());
	private int attributeScrollPosition = 0;
	private int foodScrollPosition = 0;
	private int negativeScrollPosition = 0;
	private int positiveScrollPosition = 0;
	private int neutralScrollPosition = 0;
	private int foodEffectsRowAmount = 1;
	private int negativeEffectsRowAmount = 1;
	private int positiveEffectsRowAmount = 1;
	private int neutralEffectsRowAmount = 1;
	private float attributeScrollAmount = 0.0f;
	private float foodScrollAmount = 0.0f;
	private float negativeScrollAmount = 0.0f;
	private float positiveScrollAmount = 0.0f;
	private float neutralScrollAmount = 0.0f;
	private boolean foodMouseClicked = false;
	private boolean attributeMouseClicked = false;
	private boolean negativeMouseClicked = false;
	private boolean positiveMouseClicked = false;
	private boolean neutralMouseClicked = false;

	public RPGInventoryScreen(Player player) {
		super(player.inventoryMenu, player.getInventory(), Component.translatable("gui.adventure_inventory_screen.equipment_label"), 176, 220);
		this.titleLabelX = 8;
		this.titleLabelY = 6;
		this.inventoryLabelX = 8;
		this.inventoryLabelY = this.imageHeight - 93;
	}

	private void updateEffectsLists(Player player) {
		List<MobEffectInstance> effectsList = Ordering.natural().immutableSortedCopy(player.getActiveEffects());
		List<MobEffectInstance> visibleEffectsList = new ArrayList<>(Collections.emptyList());
		for (MobEffectInstance statusEffectInstance : effectsList) {
			if (statusEffectInstance.showIcon()) {
				visibleEffectsList.add(statusEffectInstance);
			}
		}
		int visibleEffectsListSize = visibleEffectsList.size();
		if (visibleEffectsListSize == 0) {
			this.oldEffectsListSize = 0;
			this.foodEffectsList.clear();
			this.negativeEffectsList.clear();
			this.positiveEffectsList.clear();
			this.neutralEffectsList.clear();
			return;
		}
		boolean bl = false;

		// determine if the UI should be updated
		if (visibleEffectsListSize != this.oldEffectsListSize) {
			bl = true;
			this.oldEffectsListSize = visibleEffectsListSize;
		}

		if (bl) {
			this.foodEffectsList.clear();
			this.negativeEffectsList.clear();
			this.positiveEffectsList.clear();
			this.neutralEffectsList.clear();

			this.foodScrollPosition = 0;
			this.negativeScrollPosition = 0;
			this.positiveScrollPosition = 0;
			this.neutralScrollPosition = 0;

			this.foodScrollAmount = 0.0f;
			this.negativeScrollAmount = 0.0f;
			this.positiveScrollAmount = 0.0f;
			this.neutralScrollAmount = 0.0f;

			for (MobEffectInstance statusEffectInstance : visibleEffectsList) {
				if (statusEffectInstance.getEffect().is(Tags.FOOD_EFFECTS)) {
					this.foodEffectsList.add(statusEffectInstance);
				} else if (statusEffectInstance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
					this.negativeEffectsList.add(statusEffectInstance);
				} else if (statusEffectInstance.getEffect().value().getCategory() == MobEffectCategory.BENEFICIAL) {
					this.positiveEffectsList.add(statusEffectInstance);
				} else if (statusEffectInstance.getEffect().value().getCategory() == MobEffectCategory.NEUTRAL) {
					this.neutralEffectsList.add(statusEffectInstance);
				}
			}
			this.foodEffectsRowAmount = Math.max(1, this.calculateStatusEffectRowAmount(this.foodEffectsList.size()));
			this.negativeEffectsRowAmount = Math.max(1, this.calculateStatusEffectRowAmount(this.negativeEffectsList.size()));
			this.positiveEffectsRowAmount = Math.max(1, this.calculateStatusEffectRowAmount(this.positiveEffectsList.size()));
			this.neutralEffectsRowAmount = Math.max(1, this.calculateStatusEffectRowAmount(this.neutralEffectsList.size()));
		}
		visibleEffectsList.clear();
	}

	private int calculateStatusEffectRowAmount(int statusEffectListSize) {
		return statusEffectListSize / 3 + (statusEffectListSize % 3 > 0 ? 1 : 0);
	}

	private void openBackpack() {
		RPGInventoryClient.openBackPackScreen(this.minecraft);
	}

	private void openHandCraftingScreen() {
		RPGInventoryClient.openHandCraftingScreen(this.minecraft);
	}

	private void toggleShowAttributeScreen() {
		this.showAttributeScreen = !this.showAttributeScreen;
		((ToggleInventoryScreenWidget) this.toggleShowAttributeScreenButton).setIsPressed(this.showAttributeScreen);
		this.toggleShowAttributeScreenButton.setTooltip(this.showAttributeScreen ? Tooltip.create(TOGGLE_SHOW_ATTRIBUTES_BUTTON_TOOLTIP_TEXT_ON) : Tooltip.create(TOGGLE_SHOW_ATTRIBUTES_BUTTON_TOOLTIP_TEXT_OFF));
		((DuckPlayerScreenHandlerMixin) this.menu).rpginventory$setIsAttributeScreenVisible(this.showAttributeScreen);
	}

	private void toggleShowEffectScreen() {
		this.showEffectScreen = !this.showEffectScreen;
		((ToggleInventoryScreenWidget) this.toggleShowEffectScreenButton).setIsPressed(this.showEffectScreen);
		this.toggleShowEffectScreenButton.setTooltip(this.showEffectScreen ? Tooltip.create(TOGGLE_SHOW_EFFECTS_BUTTON_TOOLTIP_TEXT_ON) : Tooltip.create(TOGGLE_SHOW_EFFECTS_BUTTON_TOOLTIP_TEXT_OFF));
	}

	@Override
	protected void init() {
		if (this.minecraft.player != null) {
			if (this.minecraft.gameMode != null && this.minecraft.player.hasInfiniteMaterials()) {
				this.minecraft.setScreen(new CreativeModeInventoryScreen(this.minecraft.player, this.minecraft.player.connection.enabledFeatures(), this.minecraft.options.operatorItemsTab().get()));
				return;
			}
		}
		super.init();
		ClientConfig clientConfig = RPGInventoryClient.CLIENT_CONFIG;
		this.showAttributeScreen = clientConfig.rpgInventoryScreenSection.show_attribute_screen_when_opening_inventory_screen.get() && RPGInventory.isPlayerAttributeScreenLoaded;
		((DuckPlayerScreenHandlerMixin) this.menu).rpginventory$setIsAttributeScreenVisible(this.showAttributeScreen);
		this.toggleShowAttributeScreenButton = this.addRenderableWidget(new ToggleInventoryScreenWidget(this.leftPos + 6, this.topPos + 19, this.showAttributeScreen, false, button -> this.toggleShowAttributeScreen()));
		this.toggleShowAttributeScreenButton.setTooltip(Tooltip.create(this.showAttributeScreen ? TOGGLE_SHOW_ATTRIBUTES_BUTTON_TOOLTIP_TEXT_ON : TOGGLE_SHOW_ATTRIBUTES_BUTTON_TOOLTIP_TEXT_OFF));
		this.showEffectScreen = !clientConfig.rpgInventoryScreenSection.can_hide_status_effect_screen.get() || clientConfig.rpgInventoryScreenSection.show_effect_screen_when_opening_inventory_screen.get();
		this.toggleShowEffectScreenButton = this.addRenderableWidget(new ToggleInventoryScreenWidget(this.leftPos + this.imageWidth - 29, this.topPos + 19, this.showEffectScreen, true, button -> this.toggleShowEffectScreen()));
		this.toggleShowEffectScreenButton.setTooltip(Tooltip.create(this.showEffectScreen ? TOGGLE_SHOW_EFFECTS_BUTTON_TOOLTIP_TEXT_ON : TOGGLE_SHOW_EFFECTS_BUTTON_TOOLTIP_TEXT_OFF));
		this.openBackpackButton = this.addRenderableWidget(Button.builder(OPEN_BACKPACK_BUTTON_LABEL_TEXT, button -> this.openBackpack()).bounds(this.leftPos + clientConfig.rpgInventoryScreenSection.open_backpack_button_offset_x.get(), this.topPos + clientConfig.rpgInventoryScreenSection.open_backpack_button_offset_y.get(), 70, 20).build());
		this.openBackpackButton.visible = RPGInventory.SERVER_CONFIG.inventorySlots.disable_inventory_crafting_slots.get() && clientConfig.rpgInventoryScreenSection.enable_open_backpack_button.get() && RPGInventory.isBackpackAttributeLoaded;
		this.openHandCraftingButton = this.addRenderableWidget(Button.builder(OPEN_HAND_CRAFTING_BUTTON_LABEL_TEXT, button -> this.openHandCraftingScreen()).bounds(this.leftPos + clientConfig.rpgInventoryScreenSection.open_hand_crafting_button_offset_x.get(), this.topPos + clientConfig.rpgInventoryScreenSection.open_hand_crafting_button_offset_y.get(), 70, 20).build());
		this.openHandCraftingButton.visible = RPGInventory.SERVER_CONFIG.inventorySlots.disable_inventory_crafting_slots.get() && clientConfig.rpgInventoryScreenSection.enable_open_hand_crafting_button.get() && RPGInventory.isRPGCraftingLoaded;
		this.toggleShowAttributeScreenButton.visible = RPGInventory.isPlayerAttributeScreenLoaded;
	}

	@Override
	public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
		super.extractRenderState(graphics, mouseX, mouseY, a);
		this.drawStatusEffects(graphics, mouseX, mouseY);
		this.drawAttributeScreen(graphics, mouseX, mouseY);
		this.extractTooltip(graphics, mouseX, mouseY);
		this.mouseX = mouseX;
		this.mouseY = mouseY;
	}

	@Override
	public void extractBackground(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
		int i = this.leftPos;
		int j = this.topPos;
		int k;
		int m;
		int activeSpellSlotAmount = 0;
		int inventorySize = 0;
		int hotbarSize = 0;
		ClientConfig clientConfig = RPGInventoryClient.CLIENT_CONFIG;
		ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;
		if (this.minecraft.player != null) {
			activeSpellSlotAmount = (int) ((DuckPlayerEntityMixin) this.minecraft.player).rpginventory$getActiveSpellSlotAmount();

			hotbarSize = RPGInventory.getActiveHotbarSize(this.minecraft.player);
			inventorySize = RPGInventory.getActiveInventorySize(this.minecraft.player);
			updateEffectsLists(this.minecraft.player);
		}
		graphics.blit(ADVENTURE_INVENTORY_MAIN_BACKGROUND_TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		if (!serverConfig.inventorySlots.disable_inventory_crafting_slots.get()) {
			graphics.text(this.font, CRAFTING_LABEL_TEXT, i + serverConfig.inventorySlots.inventory_crafting_slots_x_offset.get(), j + serverConfig.inventorySlots.inventory_crafting_slots_y_offset.get() - 11, 4210752, false);
			graphics.blit(CRAFTING_SLOTS_BACKGROUND_TEXTURE, i + serverConfig.inventorySlots.inventory_crafting_slots_x_offset.get() - 1, j + serverConfig.inventorySlots.inventory_crafting_slots_y_offset.get() - 1, 0, 0, 74, 36, 74, 36);
		}

		if (activeSpellSlotAmount > 0) {
			graphics.text(this.font, SPELLS_LABEL_TEXT, i + clientConfig.rpgInventoryScreenSection.spell_slots_label_x_offset.get(), j + clientConfig.rpgInventoryScreenSection.spell_slots_label_y_offset.get(), 4210752, false);
		}

		graphics.blit(SLOT_TEXTURE, i + serverConfig.inventorySlots.head_slot_x_offset.get() - 1, j + serverConfig.inventorySlots.head_slot_y_offset.get() - 1, 0, 0, 18, 18, 18, 18);
		graphics.blit(SLOT_TEXTURE, i + serverConfig.inventorySlots.chest_slot_x_offset.get() - 1, j + serverConfig.inventorySlots.chest_slot_y_offset.get() - 1, 0, 0, 18, 18, 18, 18);
		graphics.blit(SLOT_TEXTURE, i + serverConfig.inventorySlots.legs_slot_x_offset.get() - 1, j + serverConfig.inventorySlots.legs_slot_y_offset.get() - 1, 0, 0, 18, 18, 18, 18);
		graphics.blit(SLOT_TEXTURE, i + serverConfig.inventorySlots.feet_slot_x_offset.get() - 1, j + serverConfig.inventorySlots.feet_slot_y_offset.get() - 1, 0, 0, 18, 18, 18, 18);
		graphics.blit(SLOT_TEXTURE, i + serverConfig.inventorySlots.offhand_slot_x_offset.get() - 1, j + serverConfig.inventorySlots.offhand_slot_y_offset.get() - 1, 0, 0, 18, 18, 18, 18);
		if (serverConfig.inventorySlots.is_belt_slot_enabled.get()) {
			graphics.blit(SLOT_TEXTURE, i + serverConfig.inventorySlots.belt_slot_x_offset.get() - 1, j + serverConfig.inventorySlots.belt_slot_y_offset.get() - 1, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_gloves_slot_enabled.get()) {
			graphics.blit(SLOT_TEXTURE, i + serverConfig.inventorySlots.gloves_slot_x_offset.get() - 1, j + serverConfig.inventorySlots.gloves_slot_y_offset.get() - 1, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_necklace_slot_enabled.get()) {
			graphics.blit(SLOT_TEXTURE, i + serverConfig.inventorySlots.necklace_slot_x_offset.get() - 1, j + serverConfig.inventorySlots.necklace_slot_y_offset.get() - 1, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_ring_1_slot_enabled.get()) {
			graphics.blit(SLOT_TEXTURE, i + serverConfig.inventorySlots.ring_1_slot_x_offset.get() - 1, j + serverConfig.inventorySlots.ring_1_slot_y_offset.get() - 1, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_ring_2_slot_enabled.get()) {
			graphics.blit(SLOT_TEXTURE, i + serverConfig.inventorySlots.ring_2_slot_x_offset.get() - 1, j + serverConfig.inventorySlots.ring_2_slot_y_offset.get() - 1, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_shoulders_slot_enabled.get()) {
			graphics.blit(SLOT_TEXTURE, i + serverConfig.inventorySlots.shoulders_slot_x_offset.get() - 1, j + serverConfig.inventorySlots.shoulders_slot_y_offset.get() - 1, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_relic_slot_enabled.get()) {
			graphics.blit(SLOT_TEXTURE, i + serverConfig.inventorySlots.relic_slot_x_offset.get() - 1, j + serverConfig.inventorySlots.relic_slot_y_offset.get() - 1, 0, 0, 18, 18, 18, 18);
		}
		if (activeSpellSlotAmount > 0) {
			graphics.blit(SLOT_TEXTURE, i + serverConfig.inventorySlots.spell_1_slot_x_offset.get() - 1, j + serverConfig.inventorySlots.spell_1_slot_y_offset.get() - 1, 0, 0, 18, 18, 18, 18);
		}
		if (activeSpellSlotAmount > 1) {
			graphics.blit(SLOT_TEXTURE, i + serverConfig.inventorySlots.spell_2_slot_x_offset.get() - 1, j + serverConfig.inventorySlots.spell_2_slot_y_offset.get() - 1, 0, 0, 18, 18, 18, 18);
		}
		if (activeSpellSlotAmount > 2) {
			graphics.blit(SLOT_TEXTURE, i + serverConfig.inventorySlots.spell_3_slot_x_offset.get() - 1, j + serverConfig.inventorySlots.spell_3_slot_y_offset.get() - 1, 0, 0, 18, 18, 18, 18);
		}
		if (activeSpellSlotAmount > 3) {
			graphics.blit(SLOT_TEXTURE, i + serverConfig.inventorySlots.spell_4_slot_x_offset.get() - 1, j + serverConfig.inventorySlots.spell_4_slot_y_offset.get() - 1, 0, 0, 18, 18, 18, 18);
		}
		if (activeSpellSlotAmount > 4) {
			graphics.blit(SLOT_TEXTURE, i + serverConfig.inventorySlots.spell_5_slot_x_offset.get() - 1, j + serverConfig.inventorySlots.spell_5_slot_y_offset.get() - 1, 0, 0, 18, 18, 18, 18);
		}
		if (activeSpellSlotAmount > 5) {
			graphics.blit(SLOT_TEXTURE, i + serverConfig.inventorySlots.spell_6_slot_x_offset.get() - 1, j + serverConfig.inventorySlots.spell_6_slot_y_offset.get() - 1, 0, 0, 18, 18, 18, 18);
		}
		if (activeSpellSlotAmount > 6) {
			graphics.blit(SLOT_TEXTURE, i + serverConfig.inventorySlots.spell_7_slot_x_offset.get() - 1, j + serverConfig.inventorySlots.spell_7_slot_y_offset.get() - 1, 0, 0, 18, 18, 18, 18);
		}
		if (activeSpellSlotAmount > 7) {
			graphics.blit(SLOT_TEXTURE, i + serverConfig.inventorySlots.spell_8_slot_x_offset.get() - 1, j + serverConfig.inventorySlots.spell_8_slot_y_offset.get() - 1, 0, 0, 18, 18, 18, 18);
		}

		if (RPGInventory.isHandSlotOverhaulActive()) {
			graphics.blit(SLOT_TEXTURE, i + serverConfig.inventorySlots.hand_slot_x_offset.get() - 1, j + serverConfig.inventorySlots.hand_slot_y_offset.get() - 1, 0, 0, 18, 18, 18, 18);
			if (serverConfig.handSlotOverhaul.enable_alternative_hand_slots.get()) {
				graphics.blit(SLOT_TEXTURE, i + serverConfig.inventorySlots.alternative_hand_slot_x_offset.get() - 1, j + serverConfig.inventorySlots.alternative_hand_slot_y_offset.get() - 1, 0, 0, 18, 18, 18, 18);
				graphics.blit(SLOT_TEXTURE, i + serverConfig.inventorySlots.alternative_offhand_slot_x_offset.get() - 1, j + serverConfig.inventorySlots.alternative_offhand_slot_y_offset.get() - 1, 0, 0, 18, 18, 18, 18);
			}
		}

		boolean showInactiveSlots = RPGInventoryClient.showInactiveInventorySlots();
		for (k = 0; k < (showInactiveSlots ? 27 : Math.min(inventorySize, 27)); ++k) {
			m = (k / 9);
			graphics.blit(SLOT_TEXTURE, i + 7 + (k - (m * 9)) * 18, j + 137 + (m * 18), 0, 0, 18, 18, 18, 18);
		}
		for (k = 0; k < (showInactiveSlots ? 9 : Math.min(hotbarSize, 9)); ++k) {
			graphics.blit(SLOT_TEXTURE, i + 7 + k * 18, j + 195, 0, 0, 18, 18, 18, 18);
		}

		if (this.showAttributeScreen) {
			graphics.blit(ADVENTURE_INVENTORY_SIDES_BACKGROUND_TEXTURE, i - this.sidesBackgroundWidth, j, 0, 0, this.sidesBackgroundWidth, this.imageHeight, this.sidesBackgroundWidth, this.imageHeight);
		}
		if (this.oldEffectsListSize > 0 && (this.showEffectScreen || !clientConfig.rpgInventoryScreenSection.can_hide_status_effect_screen.get())) {
			graphics.blit(ADVENTURE_INVENTORY_SIDES_BACKGROUND_TEXTURE, i + this.imageWidth, j, 0, 0, this.sidesBackgroundWidth, this.imageHeight, this.sidesBackgroundWidth, this.imageHeight);
		}
		this.toggleShowEffectScreenButton.visible = clientConfig.rpgInventoryScreenSection.can_hide_status_effect_screen.get() && this.oldEffectsListSize > 0;
		if (this.minecraft.player != null) {
			InventoryScreen.extractEntityInInventoryFollowsMouse(graphics, i + 26, j + 36, i + 75, j + 106, 30, 0.0625f, this.mouseX, this.mouseY, this.minecraft.player);
		}
	}

	private void drawAttributeScreen(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
		if (!this.showAttributeScreen) {
			return;
		}
		int x = this.leftPos - this.sidesBackgroundWidth + 7;
		int y = this.topPos + 7;
		int currentY;
		List<MutablePair<Component, List<Component>>> list = RPGInventoryClient.getPlayerAttributeScreenData(this.minecraft);
		this.attributeListSize = list.size();
		for (int i = this.attributeScrollPosition; i < Math.min(this.attributeListSize, this.attributeScrollPosition + 15); i++) {
			currentY = y + ((i - this.attributeScrollPosition) * 13);
			graphics.text(this.font, list.get(i).left, x, currentY, 0x404040, false);
			if (mouseX >= x && mouseX <= x + this.sidesBackgroundWidth - 7 && mouseY >= currentY && mouseY <= currentY + 13) {
				List<Component> tooltipList = list.get(i).right;
				if (!tooltipList.isEmpty()) {
					graphics.setTooltipForNextFrame(this.font, tooltipList, Optional.empty(), mouseX, mouseY);
				}
			}
		}
		if (list.size() > MAX_ATTRIBUTE_SCREEN_LINES) {
			graphics.blit(SCROLL_BAR_BACKGROUND_8_206_TEXTURE, x + 108, y, 0, 0, 8, 206, 8, 206);
			int k = (int) (189.0f * this.attributeScrollAmount);
			graphics.blit(SCROLLER_VERTICAL_6_15_TEXTURE, x + 109, y + 1 + k, 0, 0, 6, 15, 6, 15);
		}
	}

	private void drawStatusEffects(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
		int i = this.leftPos + this.imageWidth + 7;
		int j = this.topPos + 7;
		if (this.showEffectScreen) {
			if (this.oldEffectsListSize > 0) {
				graphics.text(this.font, Component.translatable("gui.adventureInventory.status_effects"), i + 1, j, 0x404040, false);
			}
			j += 13;
			if (!this.foodEffectsList.isEmpty()) {

				graphics.text(this.font, Component.translatable("gui.adventureInventory.status_effects.food_effects"), i + 1, j, 0x404040, false);
				j += 13;
				for (int k = 3 * this.foodScrollPosition; k < Math.min(this.foodEffectsList.size(), (3 * (1 + this.foodScrollPosition))); k++) {
					drawStatusEffectTexturesAndToolTips(graphics, i, j, k, mouseX, mouseY, this.foodEffectsList.get(k));
				}
				if (this.foodEffectsRowAmount > 1) {
					graphics.blit(SCROLL_BAR_BACKGROUND_8_32_TEXTURE, i + 109, j, 0, 0, 8, 32, 8, 32);
					int k = (int) (23.0f * this.foodScrollAmount);
					graphics.blit(SCROLLER_VERTICAL_6_7_TEXTURE, i + 110, j + 1 + k, 0, 0, 6, 7, 6, 7);
				}
				j += 37;
			}
			if (!this.negativeEffectsList.isEmpty()) {

				graphics.text(this.font, Component.translatable("gui.adventureInventory.status_effects.negative_effects"), i + 1, j, 0x404040, false);
				j += 13;
				for (int k = 3 * this.negativeScrollPosition; k < Math.min(this.negativeEffectsList.size(), (3 * (1 + this.negativeScrollPosition))); k++) {
					drawStatusEffectTexturesAndToolTips(graphics, i, j, k, mouseX, mouseY, this.negativeEffectsList.get(k));
				}
				if (this.negativeEffectsRowAmount > 1) {
					graphics.blit(SCROLL_BAR_BACKGROUND_8_32_TEXTURE, i + 109, j, 0, 0, 8, 32, 8, 32);
					int k = (int) (23.0f * this.negativeScrollAmount);
					graphics.blit(SCROLLER_VERTICAL_6_7_TEXTURE, i + 110, j + 1 + k, 0, 0, 6, 7, 6, 7);
				}
				j += 37;
			}
			if (!this.positiveEffectsList.isEmpty()) {

				graphics.text(this.font, Component.translatable("gui.adventureInventory.status_effects.positive_effects"), i + 1, j, 0x404040, false);
				j += 13;
				for (int k = 3 * this.positiveScrollPosition; k < Math.min(this.positiveEffectsList.size(), (3 * (1 + this.positiveScrollPosition))); k++) {
					drawStatusEffectTexturesAndToolTips(graphics, i, j, k, mouseX, mouseY, this.positiveEffectsList.get(k));
				}
				if (this.positiveEffectsRowAmount > 1) {
					graphics.blit(SCROLL_BAR_BACKGROUND_8_32_TEXTURE, i + 109, j, 0, 0, 8, 32, 8, 32);
					int k = (int) (23.0f * this.positiveScrollAmount);
					graphics.blit(SCROLLER_VERTICAL_6_7_TEXTURE, i + 110, j + 1 + k, 0, 0, 6, 7, 6, 7);
				}
				j += 37;
			}
			if (!this.neutralEffectsList.isEmpty()) {

				graphics.text(this.font, Component.translatable("gui.adventureInventory.status_effects.neutral_effects"), i + 1, j, 0x404040, false);
				j += 13;
				for (int k = 3 * this.neutralScrollPosition; k < Math.min(this.neutralEffectsList.size(), (3 * (1 + this.neutralScrollPosition))); k++) {
					drawStatusEffectTexturesAndToolTips(graphics, i, j, k, mouseX, mouseY, this.neutralEffectsList.get(k));
				}
				if (this.neutralEffectsRowAmount > 1) {
					graphics.blit(SCROLL_BAR_BACKGROUND_8_32_TEXTURE, i + 109, j, 0, 0, 8, 32, 8, 32);
					int k = (int) (23.0f * this.neutralScrollAmount);
					graphics.blit(SCROLLER_VERTICAL_6_7_TEXTURE, i + 110, j + 1 + k, 0, 0, 6, 7, 6, 7);
				}
			}
		}
	}

	private void drawStatusEffectTexturesAndToolTips(GuiGraphicsExtractor graphics, int x, int y, int z, int mouseX, int mouseY, MobEffectInstance statusEffectInstance) {
		int i = x + 3 + ((z % 3) * 35);
		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EFFECT_BACKGROUND_SMALL_TEXTURE, i, y, 32, 32);
		Identifier sprite = Gui.getMobEffectSprite(statusEffectInstance.getEffect());
		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, i + 7, y + 7, 0, 18, 18);
		if (mouseX >= i && mouseX <= i + 32 && mouseY >= y && mouseY <= y + 32) {
			List<Component> list = getStatusEffectTooltip(statusEffectInstance);
			graphics.setTooltipForNextFrame(this.font, list, Optional.empty(), mouseX, mouseY);
		}
	}

	private List<Component> getStatusEffectTooltip(MobEffectInstance statusEffectInstance) {
		List<Component> list = new ArrayList<>(List.of(getStatusEffectName(statusEffectInstance)));
		if (!(statusEffectInstance.isInfiniteDuration()) && this.minecraft != null && this.minecraft.level != null) {
			list.add(MobEffectUtil.formatDuration(statusEffectInstance, 1.0f, this.minecraft.level.tickRateManager().tickrate()));
		}
		Component description = getStatusEffectDescription(statusEffectInstance);
		if (!description.getString().isEmpty()) {
			list.add(description);
		}
		return list;
	}

	private Component getStatusEffectName(MobEffectInstance statusEffectInstance) {
		MutableComponent mutableText = statusEffectInstance.getEffect().value().getDisplayName().copy();
		if (statusEffectInstance.getAmplifier() >= 1 && statusEffectInstance.getAmplifier() <= 9) {
			mutableText.append(CommonComponents.SPACE).append(Component.translatable("enchantment.level." + (statusEffectInstance.getAmplifier() + 1)));
		}
		return mutableText;
	}

	private Component getStatusEffectDescription(MobEffectInstance statusEffectInstance) {
		String translationKey = statusEffectInstance.getEffect().value().getDescriptionId() + ".description";
		Component description = Component.translatable(translationKey);
		if (description.getString().equals(translationKey)) {
			description = Component.empty();
		}
		return description;
	}

	@Override
	public void resize(int width, int height) {
		boolean bool = this.showAttributeScreen;
		boolean bool1 = this.showEffectScreen;
		int number = this.attributeScrollPosition;
		int number1 = this.foodScrollPosition;
		int number2 = this.negativeScrollPosition;
		int number3 = this.positiveScrollPosition;
		int number4 = this.neutralScrollPosition;
		float number5 = this.attributeScrollAmount;
		float number6 = this.foodScrollAmount;
		float number7 = this.negativeScrollAmount;
		float number8 = this.positiveScrollAmount;
		float number9 = this.neutralScrollAmount;
		this.init(width, height);
		this.showAttributeScreen = bool;
		this.showEffectScreen = bool1;
		this.attributeScrollPosition = number;
		this.foodScrollPosition = number1;
		this.negativeScrollPosition = number2;
		this.positiveScrollPosition = number3;
		this.neutralScrollPosition = number4;
		this.attributeScrollAmount = number5;
		this.foodScrollAmount = number6;
		this.negativeScrollAmount = number7;
		this.positiveScrollAmount = number8;
		this.neutralScrollAmount = number9;
		((ToggleInventoryScreenWidget) this.toggleShowAttributeScreenButton).setIsPressed(this.showAttributeScreen);
		this.toggleShowAttributeScreenButton.setTooltip(this.showAttributeScreen ? Tooltip.create(TOGGLE_SHOW_ATTRIBUTES_BUTTON_TOOLTIP_TEXT_ON) : Tooltip.create(TOGGLE_SHOW_ATTRIBUTES_BUTTON_TOOLTIP_TEXT_OFF));
		((ToggleInventoryScreenWidget) this.toggleShowEffectScreenButton).setIsPressed(this.showEffectScreen);
		this.toggleShowEffectScreenButton.setTooltip(this.showEffectScreen ? Tooltip.create(TOGGLE_SHOW_EFFECTS_BUTTON_TOOLTIP_TEXT_ON) : Tooltip.create(TOGGLE_SHOW_EFFECTS_BUTTON_TOOLTIP_TEXT_OFF));
	}

	@Override
	public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
		this.attributeMouseClicked = false;
		this.foodMouseClicked = false;
		this.negativeMouseClicked = false;
		this.positiveMouseClicked = false;
		this.neutralMouseClicked = false;
		int i = this.leftPos + this.imageWidth + this.sidesBackgroundWidth - 15;
		int j;
		if (!RPGInventoryClient.CLIENT_CONFIG.rpgInventoryScreenSection.can_hide_status_effect_screen.get() || this.showEffectScreen) {
			if (this.foodEffectsRowAmount > 1) {
				j = this.topPos + 34;
				if (event.x() >= (double) i && event.x() < (double) (i + 6) && event.y() >= (double) j && event.y() < (double) (j + 30)) {
					this.foodMouseClicked = true;
				}
			}
			if (this.negativeEffectsRowAmount > 1) {
				j = this.topPos + 84;
				if (event.x() >= (double) i && event.x() < (double) (i + 6) && event.y() >= (double) j && event.y() < (double) (j + 30)) {
					this.negativeMouseClicked = true;
				}
			}
			if (this.positiveEffectsRowAmount > 1) {
				j = this.topPos + 134;
				if (event.x() >= (double) i && event.x() < (double) (i + 6) && event.y() >= (double) j && event.y() < (double) (j + 30)) {
					this.positiveMouseClicked = true;
				}
			}
			if (this.neutralEffectsRowAmount > 1) {
				j = this.topPos + 184;
				if (event.x() >= (double) i && event.x() < (double) (i + 6) && event.y() >= (double) j && event.y() < (double) (j + 30)) {
					this.neutralMouseClicked = true;
				}
			}
		}

		if (this.attributeListSize > 15 && this.showAttributeScreen) {
			i = this.leftPos - 13;
			j = this.topPos + 7;
			if (event.x() >= (double) i && event.x() < (double) (i + 6) && event.y() >= (double) j && event.y() < (double) (j + 206)) {
				this.attributeMouseClicked = true;
			}
		}
		return super.mouseClicked(event, doubleClick);
	}

	@Override
	public boolean mouseDragged(final MouseButtonEvent event, final double dx, final double dy) {
		if (!RPGInventoryClient.CLIENT_CONFIG.rpgInventoryScreenSection.can_hide_status_effect_screen.get() || this.showEffectScreen) {
			if (this.foodEffectsRowAmount > 1 && this.foodMouseClicked) {
				int i = this.foodEffectsRowAmount - 1;
				float f = (float) dy / (float) i;
				this.foodScrollAmount = Mth.clamp(this.foodScrollAmount + f, 0.0f, 1.0f);
				this.foodScrollPosition = (int) ((double) (this.foodScrollAmount * (float) i));
			}
			if (this.negativeEffectsRowAmount > 1 && this.negativeMouseClicked) {
				int i = this.negativeEffectsRowAmount - 1;
				float f = (float) dy / (float) i;
				this.negativeScrollAmount = Mth.clamp(this.negativeScrollAmount + f, 0.0f, 1.0f);
				this.negativeScrollPosition = (int) ((double) (this.negativeScrollAmount * (float) i));
			}
			if (this.positiveEffectsRowAmount > 1 && this.positiveMouseClicked) {
				int i = this.positiveEffectsRowAmount - 1;
				float f = (float) dy / (float) i;
				this.positiveScrollAmount = Mth.clamp(this.positiveScrollAmount + f, 0.0f, 1.0f);
				this.positiveScrollPosition = (int) ((double) (this.positiveScrollAmount * (float) i));
			}
			if (this.neutralEffectsRowAmount > 1 && this.neutralMouseClicked) {
				int i = this.neutralEffectsRowAmount - 1;
				float f = (float) dy / (float) i;
				this.neutralScrollAmount = Mth.clamp(this.neutralScrollAmount + f, 0.0f, 1.0f);
				this.neutralScrollPosition = (int) ((double) (this.neutralScrollAmount * (float) i));
			}
		}
		if (this.attributeListSize > 15 && this.attributeMouseClicked && this.showAttributeScreen) {
			int i = this.attributeListSize - 15;
			float f = (float) dy / (float) i;
			this.attributeScrollAmount = Mth.clamp(this.attributeScrollAmount + f, 0.0f, 1.0f);
			this.attributeScrollPosition = (int) ((double) (this.attributeScrollAmount * (float) i));
		}
		return super.mouseDragged(event, dx, dy);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		int scrollAreaStartX = this.leftPos + this.imageWidth + 7;
		int scrollAreaWidth = 119;
		int scrollAreaStartY = this.topPos + 33;
		int scrollAreaHeight = 32;
		if (!RPGInventoryClient.CLIENT_CONFIG.rpgInventoryScreenSection.can_hide_status_effect_screen.get() || this.showEffectScreen) {
			if (this.foodEffectsRowAmount > 1 && mouseX >= scrollAreaStartX && mouseX <= scrollAreaStartX + scrollAreaWidth && mouseY >= scrollAreaStartY && mouseY <= scrollAreaStartY + scrollAreaHeight) {
				int i = this.foodEffectsRowAmount - 1;
				float f = (float) scrollY / (float) i;
				this.foodScrollAmount = Mth.clamp(this.foodScrollAmount - f, 0.0f, 1.0f);
				this.foodScrollPosition = (int) ((double) (this.foodScrollAmount * (float) i));
			}
			scrollAreaStartY = this.topPos + 83;
			if (this.negativeEffectsRowAmount > 1 && mouseX >= scrollAreaStartX && mouseX <= scrollAreaStartX + scrollAreaWidth && mouseY >= scrollAreaStartY && mouseY <= scrollAreaStartY + scrollAreaHeight) {
				int i = this.negativeEffectsRowAmount - 1;
				float f = (float) scrollY / (float) i;
				this.negativeScrollAmount = Mth.clamp(this.negativeScrollAmount - f, 0.0f, 1.0f);
				this.negativeScrollPosition = (int) ((double) (this.negativeScrollAmount * (float) i));
			}
			scrollAreaStartY = this.topPos + 133;
			if (this.positiveEffectsRowAmount > 1 && mouseX >= scrollAreaStartX && mouseX <= scrollAreaStartX + scrollAreaWidth && mouseY >= scrollAreaStartY && mouseY <= scrollAreaStartY + scrollAreaHeight) {
				int i = this.positiveEffectsRowAmount - 1;
				float f = (float) scrollY / (float) i;
				this.positiveScrollAmount = Mth.clamp(this.positiveScrollAmount - f, 0.0f, 1.0f);
				this.positiveScrollPosition = (int) ((double) (this.positiveScrollAmount * (float) i));
			}
			scrollAreaStartY = this.topPos + 183;
			if (this.neutralEffectsRowAmount > 1 && mouseX >= scrollAreaStartX && mouseX <= scrollAreaStartX + scrollAreaWidth && mouseY >= scrollAreaStartY && mouseY <= scrollAreaStartY + scrollAreaHeight) {
				int i = this.neutralEffectsRowAmount - 1;
				float f = (float) scrollY / (float) i;
				this.neutralScrollAmount = Mth.clamp(this.neutralScrollAmount - f, 0.0f, 1.0f);
				this.neutralScrollPosition = (int) ((double) (this.neutralScrollAmount * (float) i));
			}
		}
		scrollAreaStartX = this.leftPos - this.sidesBackgroundWidth + 7;
		scrollAreaWidth = 116;
		scrollAreaStartY = this.topPos + 7;
		scrollAreaHeight = 206;
		if (this.attributeListSize > 15 && mouseX >= scrollAreaStartX && mouseX <= scrollAreaStartX + scrollAreaWidth && mouseY >= scrollAreaStartY && mouseY <= scrollAreaStartY + scrollAreaHeight) {
			int i = this.attributeListSize - 15;
			float f = (float) scrollY / (float) i;
			this.attributeScrollAmount = Mth.clamp(this.attributeScrollAmount - f, 0.0f, 1.0f);
			this.attributeScrollPosition = (int) ((double) (this.attributeScrollAmount * (float) i));
		}
		return true;
	}
}
