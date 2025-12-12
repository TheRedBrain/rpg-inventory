package com.github.theredbrain.rpginventory.gui.screen.ingame;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.screen.MannequinScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class MannequinScreen extends HandledScreen<MannequinScreenHandler> {
	public static final Identifier SLOT_TEXTURE = Identifier.ofVanilla("textures/gui/sprites/container/slot.png");
	public static final Identifier MANNEQUIN_BACKGROUND_TEXTURE = RPGInventory.identifier("textures/gui/container/mannequin.png");
	public static final Text EQUIP_BUTTON_LABEL = Text.translatable("gui.mannequin.equip_button_label");
	public static final Text UNEQUIP_BUTTON_LABEL = Text.translatable("gui.mannequin.unequip_button_label");

	public MannequinScreen(MannequinScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
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

		ButtonWidget equipButton = this.addDrawableChild(ButtonWidget.builder(EQUIP_BUTTON_LABEL, button -> this.buttonCallback(0)).dimensions(this.x + 7, this.y + 125, 72, 20).build());
		this.addDrawableChild(ButtonWidget.builder(UNEQUIP_BUTTON_LABEL, button -> this.buttonCallback(1)).dimensions(this.x + 97, this.y + 125, 72, 20).build());

		equipButton.active = this.handler.canEquip();
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

		boolean showInactiveSlots = RPGInventoryClient.showInactiveInventorySlots();
		for (k = 0; k < (showInactiveSlots ? 27 : Math.min(inventorySize, 27)); ++k) {
			m = (k / 9);
			context.drawTexture(SLOT_TEXTURE, i + 7 + (k - (m * 9)) * 18, j + 146 + (m * 18), 0, 0, 18, 18, 18, 18);
		}
		for (k = 0; k < (showInactiveSlots ? 9 : Math.min(hotbarSize, 9)); ++k) {
			context.drawTexture(SLOT_TEXTURE, i + 7 + k * 18, j + 204, 0, 0, 18, 18, 18, 18);
		}
	}
}
