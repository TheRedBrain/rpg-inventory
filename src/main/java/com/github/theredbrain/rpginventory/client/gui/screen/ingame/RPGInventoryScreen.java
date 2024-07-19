package com.github.theredbrain.rpginventory.client.gui.screen.ingame;

import com.github.theredbrain.foodoverhaul.effect.FoodStatusEffect;
import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.client.gui.widget.ToggleInventoryScreenWidget;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.screen.DuckPlayerScreenHandlerMixin;
import com.github.theredbrain.slotcustomizationapi.api.SlotCustomization;
import com.google.common.collect.Ordering;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.emi.trinkets.Point;
import dev.emi.trinkets.TrinketPlayerScreenHandler;
import dev.emi.trinkets.TrinketScreen;
import dev.emi.trinkets.TrinketScreenManager;
import dev.emi.trinkets.api.SlotGroup;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class RPGInventoryScreen extends HandledScreen<PlayerScreenHandler> implements TrinketScreen {
	public static final Identifier ADVENTURE_INVENTORY_SIDES_BACKGROUND_TEXTURE = RPGInventory.identifier("textures/gui/container/adventure_inventory/adventure_inventory_sides_background.png");
	private static final Identifier INVENTORY_SLOT_TEXTURE = RPGInventory.identifier("textures/gui/container/adventure_inventory/adventure_inventory_spells_background_1.png");
	//    private static final Identifier EFFECT_BACKGROUND_SMALL_TEXTURE = new Identifier("container/inventory/effect_background_small");
//    private static final Identifier EFFECT_SCROLLER_BACKGROUND_TEXTURE = RPGInventory.identifier("container/adventure_inventory/scroller_background");
	private static final Identifier SCROLL_BAR_BACKGROUND_8_206_TEXTURE = RPGInventory.identifier("textures/gui/sprites/scroll_bar/scroll_bar_background_8_206.png");
	private static final Identifier SCROLL_BAR_BACKGROUND_8_32_TEXTURE = RPGInventory.identifier("textures/gui/sprites/scroll_bar/scroll_bar_background_8_32.png");
	private static final Identifier SCROLLER_VERTICAL_6_7_TEXTURE = RPGInventory.identifier("textures/gui/sprites/scroll_bar/scroller_vertical_6_7.png");
	private static final Identifier SCROLLER_VERTICAL_6_15_TEXTURE = RPGInventory.identifier("textures/gui/sprites/scroll_bar/scroller_vertical_6_15.png");
	private static final Text CRAFTING_LABEL_TEXT = Text.translatable("gui.adventure_inventory_screen.crafting_label");
	private static final Text SPELLS_LABEL_TEXT = Text.translatable("gui.adventure_inventory_screen.spells_label");
	private static final Text TOGGLE_SHOW_ATTRIBUTES_BUTTON_TOOLTIP_TEXT_OFF = Text.translatable("gui.adventureInventory.toggleShowAttributeScreenButton.off.tooltip");
	private static final Text TOGGLE_SHOW_ATTRIBUTES_BUTTON_TOOLTIP_TEXT_ON = Text.translatable("gui.adventureInventory.toggleShowAttributeScreenButton.on.tooltip");
	private float mouseX;
	private float mouseY;
	private final int sidesBackgroundWidth = 130;
	private ButtonWidget toggleShowAttributeScreenButton;
	private boolean showAttributeScreen;
	private int attributeListSize = 0;
	private int oldEffectsListSize = 0;
	private List<StatusEffectInstance> foodEffectsList = new ArrayList<>(Collections.emptyList());
	private List<StatusEffectInstance> negativeEffectsList = new ArrayList<>(Collections.emptyList());
	private List<StatusEffectInstance> positiveEffectsList = new ArrayList<>(Collections.emptyList());
	private List<StatusEffectInstance> neutralEffectsList = new ArrayList<>(Collections.emptyList());
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

	public RPGInventoryScreen(PlayerEntity player) {
		super(player.playerScreenHandler, player.getInventory(), Text.translatable("gui.adventure_inventory_screen.equipment_label"));
		this.backgroundWidth = 176;
		this.backgroundHeight = 220;
		this.titleX = 8;
		this.titleY = 6;
		this.playerInventoryTitleX = 8;
		this.playerInventoryTitleY = this.backgroundHeight - 93;
	}

	public void handledScreenTick() {
//        PlayerEntity player = this.handler.player();
//        this.updateAttributeScreen(player);
//        this.updateEffectsScreen(player);
		TrinketScreenManager.tick();
	}

	private void updateEffectsLists(PlayerEntity player) {
		List<StatusEffectInstance> effectsList = Ordering.natural().sortedCopy(player.getStatusEffects());
		List<StatusEffectInstance> visibleEffectsList = new ArrayList<>(Collections.emptyList());
		for (StatusEffectInstance statusEffectInstance : effectsList) {
			if (statusEffectInstance.shouldShowIcon()) {
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

			for (StatusEffectInstance statusEffectInstance : visibleEffectsList) {
				if (RPGInventory.isFoodOverhaulLoaded && statusEffectInstance.getEffectType() instanceof FoodStatusEffect) {
					this.foodEffectsList.add(statusEffectInstance);
				} else if (statusEffectInstance.getEffectType().getCategory() == StatusEffectCategory.HARMFUL) {
					this.negativeEffectsList.add(statusEffectInstance);
				} else if (statusEffectInstance.getEffectType().getCategory() == StatusEffectCategory.BENEFICIAL) {
					this.positiveEffectsList.add(statusEffectInstance);
				} else if (statusEffectInstance.getEffectType().getCategory() == StatusEffectCategory.NEUTRAL) {
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

	private void toggleShowAttributeScreen() {
		((ToggleInventoryScreenWidget) this.toggleShowAttributeScreenButton).setIsPressed(!this.showAttributeScreen);
		this.toggleShowAttributeScreenButton.setTooltip(this.showAttributeScreen ? Tooltip.of(TOGGLE_SHOW_ATTRIBUTES_BUTTON_TOOLTIP_TEXT_OFF) : Tooltip.of(TOGGLE_SHOW_ATTRIBUTES_BUTTON_TOOLTIP_TEXT_ON));
		this.showAttributeScreen = !this.showAttributeScreen;
	}

	@Override
	protected void init() {
		TrinketScreenManager.init(this);
		if (this.client != null && this.client.player != null) {
			if (this.client.interactionManager != null && this.client.interactionManager.hasCreativeInventory()) {
				this.client.setScreen(new CreativeInventoryScreen(this.client.player, this.client.player.networkHandler.getEnabledFeatures(), this.client.options.getOperatorItemsTab().getValue()));
				return;
			}
			((SlotCustomization) this.handler.slots.get(45)).slotcustomizationapi$setDisabledOverride(((DuckPlayerEntityMixin) this.client.player).rpginventory$isOffHandStackSheathed());
		}
		super.init();
		this.showAttributeScreen = RPGInventoryClient.clientConfig.show_attribute_screen_when_opening_inventory_screen;
		this.toggleShowAttributeScreenButton = this.addDrawableChild(new ToggleInventoryScreenWidget(this.x + 6, this.y + 19, this.showAttributeScreen, button -> this.toggleShowAttributeScreen()));
		this.toggleShowAttributeScreenButton.setTooltip(Tooltip.of(this.showAttributeScreen ? TOGGLE_SHOW_ATTRIBUTES_BUTTON_TOOLTIP_TEXT_ON : TOGGLE_SHOW_ATTRIBUTES_BUTTON_TOOLTIP_TEXT_OFF));

	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		TrinketScreenManager.update(mouseX, mouseY);
		super.render(context, mouseX, mouseY, delta);
		this.drawStatusEffects(context, mouseX, mouseY);
		this.drawAttributeScreen(context, mouseX, mouseY);
		this.drawMouseoverTooltip(context, mouseX, mouseY);
		this.mouseX = mouseX;
		this.mouseY = mouseY;
	}

	@Override
	protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
		super.drawForeground(context, mouseX, mouseY);
		TrinketScreenManager.drawActiveGroup(context);
	}

	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		int i = this.x;
		int j = this.y;
		int activeSpellSlotAmount = 0;
		var serverConfig = RPGInventory.serverConfig;
		if (this.client != null && this.client.player != null) {
			activeSpellSlotAmount = (int) ((DuckPlayerEntityMixin) this.client.player).rpginventory$getActiveSpellSlotAmount();
			updateEffectsLists(this.client.player);
		}
		context.drawTexture(RPGInventory.identifier("textures/gui/container/adventure_inventory/adventure_inventory_main_background.png"), i, j, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
		if (!serverConfig.disable_inventory_crafting_slots) {
			context.drawText(this.textRenderer, CRAFTING_LABEL_TEXT, i + serverConfig.inventory_crafting_slots_x_offset, j + serverConfig.inventory_crafting_slots_y_offset - 11, 4210752, false);
			context.drawTexture(RPGInventory.identifier("textures/gui/container/adventure_inventory/adventure_inventory_crafting_background.png"), i + serverConfig.inventory_crafting_slots_x_offset - 1, j + serverConfig.inventory_crafting_slots_y_offset - 1, 0, 0, 74, 36, 74, 36);
		}
		if (activeSpellSlotAmount > 0) {
			context.drawText(this.textRenderer, SPELLS_LABEL_TEXT, i + serverConfig.spell_slots_x_offset, j + serverConfig.spell_slots_y_offset - 11, 4210752, false);
			int width = activeSpellSlotAmount < 5 ? (activeSpellSlotAmount % 5) * 18 : 72;
			int height = activeSpellSlotAmount < 5 ? 18 : 36;
			context.drawTexture(RPGInventory.identifier("textures/gui/container/adventure_inventory/adventure_inventory_spells_background_" + activeSpellSlotAmount + ".png"), i + serverConfig.spell_slots_x_offset - 1, j + serverConfig.spell_slots_y_offset - 1, 0, 0, width, height, width, height);

		}
		context.drawTexture(INVENTORY_SLOT_TEXTURE, i + serverConfig.belts_group_x_offset - 1, j + serverConfig.belts_group_y_offset - 1, 0, 0, 18, 18, 18, 18);
		context.drawTexture(INVENTORY_SLOT_TEXTURE, i + serverConfig.shoulders_group_x_offset - 1, j + serverConfig.shoulders_group_y_offset - 1, 0, 0, 18, 18, 18, 18);
		context.drawTexture(INVENTORY_SLOT_TEXTURE, i + serverConfig.necklaces_group_x_offset - 1, j + serverConfig.necklaces_group_y_offset - 1, 0, 0, 18, 18, 18, 18);
		context.drawTexture(INVENTORY_SLOT_TEXTURE, i + serverConfig.rings_1_group_x_offset - 1, j + serverConfig.rings_1_group_y_offset - 1, 0, 0, 18, 18, 18, 18);
		context.drawTexture(INVENTORY_SLOT_TEXTURE, i + serverConfig.rings_2_group_x_offset - 1, j + serverConfig.rings_2_group_y_offset - 1, 0, 0, 18, 18, 18, 18);
		context.drawTexture(INVENTORY_SLOT_TEXTURE, i + serverConfig.gloves_group_x_offset - 1, j + serverConfig.gloves_group_y_offset - 1, 0, 0, 18, 18, 18, 18);
		context.drawTexture(INVENTORY_SLOT_TEXTURE, i + serverConfig.main_hand_group_x_offset - 1, j + serverConfig.main_hand_group_y_offset - 1, 0, 0, 18, 18, 18, 18);
		context.drawTexture(INVENTORY_SLOT_TEXTURE, i + serverConfig.alternative_main_hand_group_x_offset - 1, j + serverConfig.alternative_main_hand_group_y_offset - 1, 0, 0, 18, 18, 18, 18);
		context.drawTexture(INVENTORY_SLOT_TEXTURE, i + serverConfig.alternative_offhand_group_x_offset - 1, j + serverConfig.alternative_offhand_group_y_offset - 1, 0, 0, 18, 18, 18, 18);

		if (this.showAttributeScreen) {
			context.drawTexture(ADVENTURE_INVENTORY_SIDES_BACKGROUND_TEXTURE, i - this.sidesBackgroundWidth, j, 0, 0, this.sidesBackgroundWidth, this.backgroundHeight, this.sidesBackgroundWidth, this.backgroundHeight);
		}
		if (this.oldEffectsListSize > 0) {
			context.drawTexture(ADVENTURE_INVENTORY_SIDES_BACKGROUND_TEXTURE, i + this.backgroundWidth, j, 0, 0, this.sidesBackgroundWidth, this.backgroundHeight, this.sidesBackgroundWidth, this.backgroundHeight);
		}
		if (this.client != null && this.client.player != null) {
//            InventoryScreen.drawEntity(context, i + 26, j + 36, i + 75, j + 106, 30, 0.0625f, this.mouseX, this.mouseY, this.client.player);
			InventoryScreen.drawEntity(context, i + 51, j + 103, 30, (float) (i + 51) - this.mouseX, (float) (j + 103 - 50) - this.mouseY, this.client.player);
		}
		TrinketScreenManager.drawExtraGroups(context);
	}

	@Override
	public void drawSlot(DrawContext context, Slot slot) {
	private void drawAttributeScreen(DrawContext context, int mouseX, int mouseY) {
		if (!this.showAttributeScreen) {
			return;
		}
		int x = this.x - this.sidesBackgroundWidth + 7;
		int y = this.y + 7;
		List<Text> list = this.getAttributeList();
		this.attributeListSize = list.size();
		for (int i = this.attributeScrollPosition; i < Math.min(this.attributeListSize, this.attributeScrollPosition + 15); i++) {
			context.drawText(this.textRenderer, list.get(i), x, y + ((i - this.attributeScrollPosition) * 13), 0x404040, false);
		}
		if (list.size() > 15) {
//                context.drawGuiTexture(EFFECT_SCROLLER_BACKGROUND_TEXTURE, i + 109, j, 8, 32);
			context.drawTexture(SCROLL_BAR_BACKGROUND_8_206_TEXTURE, x + 108, y, 0, 0, 8, 206, 8, 206);
			int k = (int) (189.0f * this.attributeScrollAmount);
//                context.drawGuiTexture(EFFECT_SCROLLER_TEXTURE, i + 110, j + 1 + k, 6, 7);
			context.drawTexture(SCROLLER_VERTICAL_6_15_TEXTURE, x + 109, y + 1 + k, 0, 0, 6, 15, 6, 15);
		}
	}

	private void drawStatusEffects(DrawContext context, int mouseX, int mouseY) {
		int i = this.x + this.backgroundWidth + 7;
		int j = this.y + 7;
		if (this.oldEffectsListSize > 0) {
			context.drawText(this.textRenderer, Text.translatable("gui.adventureInventory.status_effects"), i + 1, j, 0x404040, false);
		}
		j += 13;
		if (!this.foodEffectsList.isEmpty()) {

			context.drawText(this.textRenderer, Text.translatable("gui.adventureInventory.status_effects.food_effects"), i + 1, j, 0x404040, false);
			j += 13;
			for (int k = 3 * this.foodScrollPosition; k < Math.min(this.foodEffectsList.size(), (3 * (1 + this.foodScrollPosition))); k++) {
				drawStatusEffectTexturesAndToolTips(context, i, j, k, mouseX, mouseY, this.foodEffectsList.get(k));
			}
			if (this.foodEffectsRowAmount > 1) {
//                context.drawGuiTexture(EFFECT_SCROLLER_BACKGROUND_TEXTURE, i + 109, j, 8, 32);
				context.drawTexture(SCROLL_BAR_BACKGROUND_8_32_TEXTURE, i + 109, j, 0, 0, 8, 32, 8, 32);
				int k = (int) (23.0f * this.foodScrollAmount);
//                context.drawGuiTexture(EFFECT_SCROLLER_TEXTURE, i + 110, j + 1 + k, 6, 7);
				context.drawTexture(SCROLLER_VERTICAL_6_7_TEXTURE, i + 110, j + 1 + k, 0, 0, 6, 7, 6, 7);
			}
			j += 37;
		} else {
			j += 50;
		}
		if (!this.negativeEffectsList.isEmpty()) {

			context.drawText(this.textRenderer, Text.translatable("gui.adventureInventory.status_effects.negative_effects"), i + 1, j, 0x404040, false);
			j += 13;
			for (int k = 3 * this.negativeScrollPosition; k < Math.min(this.negativeEffectsList.size(), (3 * (1 + this.negativeScrollPosition))); k++) {
				drawStatusEffectTexturesAndToolTips(context, i, j, k, mouseX, mouseY, this.negativeEffectsList.get(k));
			}
			if (this.negativeEffectsRowAmount > 1) {
//                context.drawGuiTexture(EFFECT_SCROLLER_BACKGROUND_TEXTURE, i + 109, j, 8, 32);
				context.drawTexture(SCROLL_BAR_BACKGROUND_8_32_TEXTURE, i + 109, j, 0, 0, 8, 32, 8, 32);
				int k = (int) (23.0f * this.negativeScrollAmount);
//                context.drawGuiTexture(EFFECT_SCROLLER_TEXTURE, i + 110, j + 1 + k, 6, 7);
				context.drawTexture(SCROLLER_VERTICAL_6_7_TEXTURE, i + 110, j + 1 + k, 0, 0, 6, 7, 6, 7);
			}
			j += 37;
		} else {
			j += 50;
		}
		if (!this.positiveEffectsList.isEmpty()) {

			context.drawText(this.textRenderer, Text.translatable("gui.adventureInventory.status_effects.positive_effects"), i + 1, j, 0x404040, false);
			j += 13;
			for (int k = 3 * this.positiveScrollPosition; k < Math.min(this.positiveEffectsList.size(), (3 * (1 + this.positiveScrollPosition))); k++) {
				drawStatusEffectTexturesAndToolTips(context, i, j, k, mouseX, mouseY, this.positiveEffectsList.get(k));
			}
			if (this.positiveEffectsRowAmount > 1) {
//                context.drawGuiTexture(EFFECT_SCROLLER_BACKGROUND_TEXTURE, i + 109, j, 8, 32);
				context.drawTexture(SCROLL_BAR_BACKGROUND_8_32_TEXTURE, i + 109, j, 0, 0, 8, 32, 8, 32);
				int k = (int) (23.0f * this.positiveScrollAmount);
//                context.drawGuiTexture(EFFECT_SCROLLER_TEXTURE, i + 110, j + 1 + k, 6, 7);
				context.drawTexture(SCROLLER_VERTICAL_6_7_TEXTURE, i + 110, j + 1 + k, 0, 0, 6, 7, 6, 7);
			}
			j += 37;
		} else {
			j += 50;
		}
		if (!this.neutralEffectsList.isEmpty()) {

			context.drawText(this.textRenderer, Text.translatable("gui.adventureInventory.status_effects.neutral_effects"), i + 1, j, 0x404040, false);
			j += 13;
			for (int k = 3 * this.neutralScrollPosition; k < Math.min(this.neutralEffectsList.size(), (3 * (1 + this.neutralScrollPosition))); k++) {
				drawStatusEffectTexturesAndToolTips(context, i, j, k, mouseX, mouseY, this.neutralEffectsList.get(k));
			}
			if (this.neutralEffectsRowAmount > 1) {
//                context.drawGuiTexture(EFFECT_SCROLLER_BACKGROUND_TEXTURE, i + 109, j, 8, 32);
				context.drawTexture(SCROLL_BAR_BACKGROUND_8_32_TEXTURE, i + 109, j, 0, 0, 8, 32, 8, 32);
				int k = (int) (23.0f * this.neutralScrollAmount);
//                context.drawGuiTexture(EFFECT_SCROLLER_TEXTURE, i + 110, j + 1 + k, 6, 7);
				context.drawTexture(SCROLLER_VERTICAL_6_7_TEXTURE, i + 110, j + 1 + k, 0, 0, 6, 7, 6, 7);
			}
		}

	}

	private void drawStatusEffectTexturesAndToolTips(DrawContext context, int x, int y, int z, int mouseX, int mouseY, StatusEffectInstance statusEffectInstance) {
		if (this.client != null) {
			StatusEffectSpriteManager statusEffectSpriteManager = this.client.getStatusEffectSpriteManager();
			int i = x + 3 + ((z % 3) * 35);
//        context.drawGuiTexture(EFFECT_BACKGROUND_SMALL_TEXTURE, i, y, 32, 32);
//            context.drawTexture(EFFECT_BACKGROUND_SMALL_TEXTURE, i, y, 0, 0, 32, 32);
			context.drawTexture(BACKGROUND_TEXTURE, i, y, 0, 198, 32, 32);
			Sprite sprite = statusEffectSpriteManager.getSprite(statusEffectInstance.getEffectType());
			context.drawSprite(i + 7, y + 7, 0, 18, 18, sprite);
			if (mouseX >= i && mouseX <= i + 32 && mouseY >= y && mouseY <= y + 32) {
				List<Text> list = getStatusEffectTooltip(statusEffectInstance);
				context.drawTooltip(this.textRenderer, list, Optional.empty(), mouseX, mouseY);
			}
		}
	}

	private List<Text> getStatusEffectTooltip(StatusEffectInstance statusEffectInstance) {
		List<Text> list = new ArrayList<>(List.of(getStatusEffectName(statusEffectInstance)));
		if (!(statusEffectInstance.isInfinite())) {
//            return List.of(getStatusEffectName(statusEffectInstance), );
			list.add(StatusEffectUtil.getDurationText(statusEffectInstance, 1.0f/*, this.client.world.getTickManager().getTickRate()*/));
		}
		Text description = getStatusEffectDescription(statusEffectInstance);
		if (!description.getString().isEmpty()) {
			list.add(description);
		}
		return list;
//        return List.of(getStatusEffectName(statusEffectInstance), StatusEffectUtil.getDurationText(statusEffectInstance, 1.0f/*, this.client.world.getTickManager().getTickRate()*/), getStatusEffectDescription(statusEffectInstance));
	}

	private Text getStatusEffectName(StatusEffectInstance statusEffectInstance) {
		MutableText mutableText = statusEffectInstance.getEffectType().getName().copy();
		if (statusEffectInstance.getAmplifier() >= 1 && statusEffectInstance.getAmplifier() <= 9) {
			mutableText.append(ScreenTexts.SPACE).append(Text.translatable("enchantment.level." + (statusEffectInstance.getAmplifier() + 1)));
		}
		return mutableText;
	}

	private Text getStatusEffectDescription(StatusEffectInstance statusEffectInstance) {
		String translationKey = statusEffectInstance.getEffectType().getTranslationKey() + ".description";
		Text description = Text.translatable(translationKey);
		if (description.getString().equals(translationKey)) {
			description = Text.empty();
		}
		return description;
	}

	private List<Text> getAttributeList() {
		String[] attribute_screen_configuration = RPGInventoryClient.clientConfig.attribute_screen_configuration;
		List<Text> list = new ArrayList<>(List.of());

		PlayerEntity player = null;
		if (this.client != null) {
			player = this.client.player;
		}
		if (player == null) {
			return list;
		}
		for (String string : attribute_screen_configuration) {
			String[] stringArray = string.split(":");
			if (stringArray.length == 1) {
				if (stringArray[0].equals("EMPTY_LINE")) {
					list.add(Text.empty());
				}
			} else if (stringArray.length == 2) {
				if (stringArray[0].equals("TRANSLATABLE_STRING")) {
					list.add(Text.translatable(stringArray[1]));
				} else if (stringArray[0].equals("STRING")) {
					list.add(Text.of(stringArray[1]));
				}
			} else if (stringArray.length == 3) {
				if (stringArray[0].equals("ATTRIBUTE_VALUE")) {

					Identifier attributeId = Identifier.of(stringArray[1], stringArray[2]);
					EntityAttribute attribute = Registries.ATTRIBUTE.get(attributeId);
					if (attribute != null && player.getAttributes().hasAttribute(attribute)) {
						MutableText mutableText = Text.translatable(attribute.getTranslationKey());
						mutableText.append(Text.of(": "));
						mutableText.append(Text.of(Double.toString(player.getAttributeValue(attribute))));
						list.add(mutableText);
					}
				}
			} else if (stringArray.length == 4) {
				if (stringArray[0].equals("CUSTOM_ATTRIBUTE_VALUE")) {
					MutableText mutableText = Text.translatable(stringArray[1]);

					Identifier attributeId = Identifier.of(stringArray[2], stringArray[3]);
					EntityAttribute attribute = Registries.ATTRIBUTE.get(attributeId);
					if (attribute != null && player.getAttributes().hasAttribute(attribute)) {
						mutableText.append(Text.of(Double.toString(player.getAttributeValue(attribute))));
						list.add(mutableText);
					}
				}
			} else if (stringArray.length == 6) {
				if (stringArray[0].equals("ATTRIBUTE_RELATION")) {
					MutableText mutableText = Text.translatable(stringArray[1]);

					Identifier firstAttributeId = Identifier.of(stringArray[2], stringArray[3]);
					Identifier secondAttributeId = Identifier.of(stringArray[4], stringArray[5]);
					EntityAttribute firstAttribute = Registries.ATTRIBUTE.get(firstAttributeId);
					EntityAttribute secondAttribute = Registries.ATTRIBUTE.get(secondAttributeId);
					if (firstAttribute != null && secondAttribute != null && player.getAttributes().hasAttribute(firstAttribute) && player.getAttributes().hasAttribute(secondAttribute)) {
						mutableText.append(Text.of(Double.toString(player.getAttributeValue(firstAttribute))));
						mutableText.append(Text.of("/"));
						mutableText.append(Text.of(Double.toString(player.getAttributeValue(secondAttribute))));
						list.add(mutableText);
					}
				}
			}
		}
		return list;
	}

	@Override
	protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
		return super.isClickOutsideBounds(mouseX, mouseY, left, top, button) && !(TrinketScreenManager.isClickInsideTrinketBounds(mouseX, mouseY));
	}

	@Override
	public void resize(MinecraftClient client, int width, int height) {
		boolean bool = this.showAttributeScreen;
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
		this.init(client, width, height);
		this.showAttributeScreen = bool;
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
		this.toggleShowAttributeScreenButton.setTooltip(this.showAttributeScreen ? Tooltip.of(TOGGLE_SHOW_ATTRIBUTES_BUTTON_TOOLTIP_TEXT_ON) : Tooltip.of(TOGGLE_SHOW_ATTRIBUTES_BUTTON_TOOLTIP_TEXT_OFF));
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		this.attributeMouseClicked = false;
		this.foodMouseClicked = false;
		this.negativeMouseClicked = false;
		this.positiveMouseClicked = false;
		this.neutralMouseClicked = false;
		int i = this.x + this.backgroundWidth + this.sidesBackgroundWidth - 15;
		int j;
		if (this.foodEffectsRowAmount > 1) {
			j = this.y + 34;
			if (mouseX >= (double) i && mouseX < (double) (i + 6) && mouseY >= (double) j && mouseY < (double) (j + 30)) {
				this.foodMouseClicked = true;
			}
		}
		if (this.negativeEffectsRowAmount > 1) {
			j = this.y + 84;
			if (mouseX >= (double) i && mouseX < (double) (i + 6) && mouseY >= (double) j && mouseY < (double) (j + 30)) {
				this.negativeMouseClicked = true;
			}
		}
		if (this.positiveEffectsRowAmount > 1) {
			j = this.y + 134;
			if (mouseX >= (double) i && mouseX < (double) (i + 6) && mouseY >= (double) j && mouseY < (double) (j + 30)) {
				this.positiveMouseClicked = true;
			}
		}
		if (this.neutralEffectsRowAmount > 1) {
			j = this.y + 184;
			if (mouseX >= (double) i && mouseX < (double) (i + 6) && mouseY >= (double) j && mouseY < (double) (j + 30)) {
				this.neutralMouseClicked = true;
			}
		}

		if (this.attributeListSize > 15 && this.showAttributeScreen) {
			i = this.x - 13;
			j = this.y + 7;
			if (mouseX >= (double) i && mouseX < (double) (i + 6) && mouseY >= (double) j && mouseY < (double) (j + 206)) {
				this.attributeMouseClicked = true;
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (this.foodEffectsRowAmount > 1 && this.foodMouseClicked) {
			int i = this.foodEffectsRowAmount - 1;
			float f = (float) deltaY / (float) i;
			this.foodScrollAmount = MathHelper.clamp(this.foodScrollAmount + f, 0.0f, 1.0f);
			this.foodScrollPosition = (int) ((double) (this.foodScrollAmount * (float) i));
		}
		if (this.negativeEffectsRowAmount > 1 && this.negativeMouseClicked) {
			int i = this.negativeEffectsRowAmount - 1;
			float f = (float) deltaY / (float) i;
			this.negativeScrollAmount = MathHelper.clamp(this.negativeScrollAmount + f, 0.0f, 1.0f);
			this.negativeScrollPosition = (int) ((double) (this.negativeScrollAmount * (float) i));
		}
		if (this.positiveEffectsRowAmount > 1 && this.positiveMouseClicked) {
			int i = this.positiveEffectsRowAmount - 1;
			float f = (float) deltaY / (float) i;
			this.positiveScrollAmount = MathHelper.clamp(this.positiveScrollAmount + f, 0.0f, 1.0f);
			this.positiveScrollPosition = (int) ((double) (this.positiveScrollAmount * (float) i));
		}
		if (this.neutralEffectsRowAmount > 1 && this.neutralMouseClicked) {
			int i = this.neutralEffectsRowAmount - 1;
			float f = (float) deltaY / (float) i;
			this.neutralScrollAmount = MathHelper.clamp(this.neutralScrollAmount + f, 0.0f, 1.0f);
			this.neutralScrollPosition = (int) ((double) (this.neutralScrollAmount * (float) i));
		}
		if (this.attributeListSize > 15 && this.attributeMouseClicked && this.showAttributeScreen) {
			int i = this.attributeListSize - 15;
			float f = (float) deltaY / (float) i;
			this.attributeScrollAmount = MathHelper.clamp(this.attributeScrollAmount + f, 0.0f, 1.0f);
			this.attributeScrollPosition = (int) ((double) (this.attributeScrollAmount * (float) i));
		}
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY/*, double horizontalAmount*/, double verticalAmount) {
		int scrollAreaStartX = this.x + this.backgroundWidth + 7;
		int scrollAreaWidth = 119;
		int scrollAreaStartY = this.y + 33;
		int scrollAreaHeight = 32;
		if (this.foodEffectsRowAmount > 1 && mouseX >= scrollAreaStartX && mouseX <= scrollAreaStartX + scrollAreaWidth && mouseY >= scrollAreaStartY && mouseY <= scrollAreaStartY + scrollAreaHeight) {
			int i = this.foodEffectsRowAmount - 1;
			float f = (float) verticalAmount / (float) i;
			this.foodScrollAmount = MathHelper.clamp(this.foodScrollAmount - f, 0.0f, 1.0f);
			this.foodScrollPosition = (int) ((double) (this.foodScrollAmount * (float) i));
		}
		scrollAreaStartY = this.y + 83;
		if (this.negativeEffectsRowAmount > 1 && mouseX >= scrollAreaStartX && mouseX <= scrollAreaStartX + scrollAreaWidth && mouseY >= scrollAreaStartY && mouseY <= scrollAreaStartY + scrollAreaHeight) {
			int i = this.negativeEffectsRowAmount - 1;
			float f = (float) verticalAmount / (float) i;
			this.negativeScrollAmount = MathHelper.clamp(this.negativeScrollAmount - f, 0.0f, 1.0f);
			this.negativeScrollPosition = (int) ((double) (this.negativeScrollAmount * (float) i));
		}
		scrollAreaStartY = this.y + 133;
		if (this.positiveEffectsRowAmount > 1 && mouseX >= scrollAreaStartX && mouseX <= scrollAreaStartX + scrollAreaWidth && mouseY >= scrollAreaStartY && mouseY <= scrollAreaStartY + scrollAreaHeight) {
			int i = this.positiveEffectsRowAmount - 1;
			float f = (float) verticalAmount / (float) i;
			this.positiveScrollAmount = MathHelper.clamp(this.positiveScrollAmount - f, 0.0f, 1.0f);
			this.positiveScrollPosition = (int) ((double) (this.positiveScrollAmount * (float) i));
		}
		scrollAreaStartY = this.y + 183;
		if (this.neutralEffectsRowAmount > 1 && mouseX >= scrollAreaStartX && mouseX <= scrollAreaStartX + scrollAreaWidth && mouseY >= scrollAreaStartY && mouseY <= scrollAreaStartY + scrollAreaHeight) {
			int i = this.neutralEffectsRowAmount - 1;
			float f = (float) verticalAmount / (float) i;
			this.neutralScrollAmount = MathHelper.clamp(this.neutralScrollAmount - f, 0.0f, 1.0f);
			this.neutralScrollPosition = (int) ((double) (this.neutralScrollAmount * (float) i));
		}
		scrollAreaStartX = this.x - this.sidesBackgroundWidth + 7;
		scrollAreaWidth = 116;
		scrollAreaStartY = this.y + 7;
		scrollAreaHeight = 206;
		if (this.attributeListSize > 15 && mouseX >= scrollAreaStartX && mouseX <= scrollAreaStartX + scrollAreaWidth && mouseY >= scrollAreaStartY && mouseY <= scrollAreaStartY + scrollAreaHeight) {
			int i = this.attributeListSize - 15;
			float f = (float) verticalAmount / (float) i;
			this.attributeScrollAmount = MathHelper.clamp(this.attributeScrollAmount - f, 0.0f, 1.0f);
			this.attributeScrollPosition = (int) ((double) (this.attributeScrollAmount * (float) i));
		}
		return true;
	}

	@Override
	public TrinketPlayerScreenHandler trinkets$getHandler() {
		return ((TrinketPlayerScreenHandler) this.handler);
	}

	@Override
	public Rect2i trinkets$getGroupRect(SlotGroup group) {
		Point pos = ((TrinketPlayerScreenHandler) handler).trinkets$getGroupPos(group);
		if (pos != null) {
			return new Rect2i(pos.x() - 1, pos.y() - 1, 17, 17);
		}
		return new Rect2i(0, 0, 0, 0);
	}

	@Override
	public Slot trinkets$getFocusedSlot() {
		return this.focusedSlot;
	}

	@Override
	public int trinkets$getX() {
		return this.x;
	}

	@Override
	public int trinkets$getY() {
		return this.y;
	}

	@Override
	public boolean trinkets$isRecipeBookOpen() {
		return this.showAttributeScreen;
	}
}
