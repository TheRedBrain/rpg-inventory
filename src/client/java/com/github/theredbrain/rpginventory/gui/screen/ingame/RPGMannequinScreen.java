package com.github.theredbrain.rpginventory.gui.screen.ingame;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.screen.RPGMannequinScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class RPGMannequinScreen extends AbstractMannequinScreen<RPGMannequinScreenHandler> {

	public static final Identifier MANNEQUIN_BACKGROUND_TEXTURE = RPGInventory.identifier("textures/gui/container/rpg_mannequin.png");

	public RPGMannequinScreen(RPGMannequinScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	protected void init() {
		this.backgroundWidth = 176;
		this.backgroundHeight = 229;

		super.init();

		ButtonWidget equipButton = this.addDrawableChild(ButtonWidget.builder(EQUIP_BUTTON_LABEL, button -> this.buttonCallback(0)).dimensions(this.x + 7, this.y + 125, 72, 20).build());
		this.addDrawableChild(ButtonWidget.builder(UNEQUIP_BUTTON_LABEL, button -> this.buttonCallback(1)).dimensions(this.x + 97, this.y + 125, 72, 20).build());

		equipButton.active = this.handler.canEquip();
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
		if (serverConfig.inventorySlots.is_belt_mannequin_slot_enabled.get()) {
			context.drawTexture(SLOT_TEXTURE, i + 61, j + 70, 0, 0, 18, 18, 18, 18);
			context.drawTexture(SLOT_TEXTURE, i + 61 + 90, j + 70, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_gloves_mannequin_slot_enabled.get()) {
			context.drawTexture(SLOT_TEXTURE, i + 61, j + 52, 0, 0, 18, 18, 18, 18);
			context.drawTexture(SLOT_TEXTURE, i + 61 + 90, j + 52, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_necklace_mannequin_slot_enabled.get()) {
			context.drawTexture(SLOT_TEXTURE, i + 43, j + 16, 0, 0, 18, 18, 18, 18);
			context.drawTexture(SLOT_TEXTURE, i + 43 + 90, j + 16, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_ring_1_mannequin_slot_enabled.get()) {
			context.drawTexture(SLOT_TEXTURE, i + 61, j + 34, 0, 0, 18, 18, 18, 18);
			context.drawTexture(SLOT_TEXTURE, i + 61 + 90, j + 34, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_ring_2_mannequin_slot_enabled.get()) {
			context.drawTexture(SLOT_TEXTURE, i + 43, j + 34, 0, 0, 18, 18, 18, 18);
			context.drawTexture(SLOT_TEXTURE, i + 43 + 90, j + 34, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_shoulders_mannequin_slot_enabled.get()) {
			context.drawTexture(SLOT_TEXTURE, i + 25, j + 16, 0, 0, 18, 18, 18, 18);
			context.drawTexture(SLOT_TEXTURE, i + 25 + 90, j + 16, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_relic_mannequin_slot_enabled.get()) {
			context.drawTexture(SLOT_TEXTURE, i + 61, j + 16, 0, 0, 18, 18, 18, 18);
			context.drawTexture(SLOT_TEXTURE, i + 61 + 90, j + 16, 0, 0, 18, 18, 18, 18);
		}

		if (serverConfig.inventorySlots.is_spell_1_mannequin_slot_enabled.get()) {
			if (activeSpellSlotAmount > 0) {
				context.drawTexture(SLOT_TEXTURE, i + 7, j + 88, 0, 0, 18, 18, 18, 18);
			}
			context.drawTexture(SLOT_TEXTURE, i + 7 + 90, j + 88, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_spell_2_mannequin_slot_enabled.get()) {
			if (activeSpellSlotAmount > 1) {
				context.drawTexture(SLOT_TEXTURE, i + 25, j + 88, 0, 0, 18, 18, 18, 18);
			}
			context.drawTexture(SLOT_TEXTURE, i + 25 + 90, j + 88, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_spell_3_mannequin_slot_enabled.get()) {
			if (activeSpellSlotAmount > 2) {
				context.drawTexture(SLOT_TEXTURE, i + 43, j + 88, 0, 0, 18, 18, 18, 18);
			}
			context.drawTexture(SLOT_TEXTURE, i + 43 + 90, j + 88, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_spell_4_mannequin_slot_enabled.get()) {
			if (activeSpellSlotAmount > 3) {
				context.drawTexture(SLOT_TEXTURE, i + 61, j + 88, 0, 0, 18, 18, 18, 18);
			}
			context.drawTexture(SLOT_TEXTURE, i + 61 + 90, j + 88, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_spell_5_mannequin_slot_enabled.get()) {
			if (activeSpellSlotAmount > 4) {
				context.drawTexture(SLOT_TEXTURE, i + 7, j + 106, 0, 0, 18, 18, 18, 18);
			}
			context.drawTexture(SLOT_TEXTURE, i + 7 + 90, j + 106, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_spell_6_mannequin_slot_enabled.get()) {
			if (activeSpellSlotAmount > 5) {
				context.drawTexture(SLOT_TEXTURE, i + 25, j + 106, 0, 0, 18, 18, 18, 18);
			}
			context.drawTexture(SLOT_TEXTURE, i + 25 + 90, j + 106, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_spell_7_mannequin_slot_enabled.get()) {
			if (activeSpellSlotAmount > 6) {
				context.drawTexture(SLOT_TEXTURE, i + 43, j + 106, 0, 0, 18, 18, 18, 18);
			}
			context.drawTexture(SLOT_TEXTURE, i + 43 + 90, j + 106, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_spell_8_mannequin_slot_enabled.get()) {
			if (activeSpellSlotAmount > 7) {
				context.drawTexture(SLOT_TEXTURE, i + 61, j + 106, 0, 0, 18, 18, 18, 18);
			}
			context.drawTexture(SLOT_TEXTURE, i + 61 + 90, j + 106, 0, 0, 18, 18, 18, 18);
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
