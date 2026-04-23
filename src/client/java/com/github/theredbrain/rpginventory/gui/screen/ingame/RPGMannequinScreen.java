package com.github.theredbrain.rpginventory.gui.screen.ingame;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.screen.RPGMannequinScreenHandler;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class RPGMannequinScreen extends AbstractMannequinScreen<RPGMannequinScreenHandler> {

	public static final Identifier MANNEQUIN_BACKGROUND_TEXTURE = RPGInventory.identifier("textures/gui/container/rpg_mannequin.png");

	public RPGMannequinScreen(RPGMannequinScreenHandler handler, Inventory inventory, Component title) {
		super(handler, inventory, title, 176, 229);
	}

	@Override
	protected void init() {

		super.init();

		Button equipButton = this.addRenderableWidget(Button.builder(EQUIP_BUTTON_LABEL, button -> this.buttonCallback(0)).bounds(this.leftPos + 7, this.topPos + 125, 72, 20).build());
		this.addRenderableWidget(Button.builder(UNEQUIP_BUTTON_LABEL, button -> this.buttonCallback(1)).bounds(this.leftPos + 97, this.topPos + 125, 72, 20).build());

		equipButton.active = this.menu.canEquip();
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
		if (this.minecraft.player != null) {
			activeSpellSlotAmount = (int) ((DuckPlayerEntityMixin) this.minecraft.player).rpginventory$getActiveSpellSlotAmount();

			hotbarSize = RPGInventory.getActiveHotbarSize(this.minecraft.player);
			inventorySize = RPGInventory.getActiveInventorySize(this.minecraft.player);
		}

		graphics.blit(MANNEQUIN_BACKGROUND_TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

		ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;
		if (serverConfig.inventorySlots.is_belt_mannequin_slot_enabled.get()) {
			graphics.blit(SLOT_TEXTURE, i + 61, j + 70, 0, 0, 18, 18, 18, 18);
			graphics.blit(SLOT_TEXTURE, i + 61 + 90, j + 70, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_gloves_mannequin_slot_enabled.get()) {
			graphics.blit(SLOT_TEXTURE, i + 61, j + 52, 0, 0, 18, 18, 18, 18);
			graphics.blit(SLOT_TEXTURE, i + 61 + 90, j + 52, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_necklace_mannequin_slot_enabled.get()) {
			graphics.blit(SLOT_TEXTURE, i + 43, j + 16, 0, 0, 18, 18, 18, 18);
			graphics.blit(SLOT_TEXTURE, i + 43 + 90, j + 16, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_ring_1_mannequin_slot_enabled.get()) {
			graphics.blit(SLOT_TEXTURE, i + 61, j + 34, 0, 0, 18, 18, 18, 18);
			graphics.blit(SLOT_TEXTURE, i + 61 + 90, j + 34, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_ring_2_mannequin_slot_enabled.get()) {
			graphics.blit(SLOT_TEXTURE, i + 43, j + 34, 0, 0, 18, 18, 18, 18);
			graphics.blit(SLOT_TEXTURE, i + 43 + 90, j + 34, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_shoulders_mannequin_slot_enabled.get()) {
			graphics.blit(SLOT_TEXTURE, i + 25, j + 16, 0, 0, 18, 18, 18, 18);
			graphics.blit(SLOT_TEXTURE, i + 25 + 90, j + 16, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_relic_mannequin_slot_enabled.get()) {
			graphics.blit(SLOT_TEXTURE, i + 61, j + 16, 0, 0, 18, 18, 18, 18);
			graphics.blit(SLOT_TEXTURE, i + 61 + 90, j + 16, 0, 0, 18, 18, 18, 18);
		}

		if (serverConfig.inventorySlots.is_spell_1_mannequin_slot_enabled.get()) {
			if (activeSpellSlotAmount > 0) {
				graphics.blit(SLOT_TEXTURE, i + 7, j + 88, 0, 0, 18, 18, 18, 18);
			}
			graphics.blit(SLOT_TEXTURE, i + 7 + 90, j + 88, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_spell_2_mannequin_slot_enabled.get()) {
			if (activeSpellSlotAmount > 1) {
				graphics.blit(SLOT_TEXTURE, i + 25, j + 88, 0, 0, 18, 18, 18, 18);
			}
			graphics.blit(SLOT_TEXTURE, i + 25 + 90, j + 88, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_spell_3_mannequin_slot_enabled.get()) {
			if (activeSpellSlotAmount > 2) {
				graphics.blit(SLOT_TEXTURE, i + 43, j + 88, 0, 0, 18, 18, 18, 18);
			}
			graphics.blit(SLOT_TEXTURE, i + 43 + 90, j + 88, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_spell_4_mannequin_slot_enabled.get()) {
			if (activeSpellSlotAmount > 3) {
				graphics.blit(SLOT_TEXTURE, i + 61, j + 88, 0, 0, 18, 18, 18, 18);
			}
			graphics.blit(SLOT_TEXTURE, i + 61 + 90, j + 88, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_spell_5_mannequin_slot_enabled.get()) {
			if (activeSpellSlotAmount > 4) {
				graphics.blit(SLOT_TEXTURE, i + 7, j + 106, 0, 0, 18, 18, 18, 18);
			}
			graphics.blit(SLOT_TEXTURE, i + 7 + 90, j + 106, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_spell_6_mannequin_slot_enabled.get()) {
			if (activeSpellSlotAmount > 5) {
				graphics.blit(SLOT_TEXTURE, i + 25, j + 106, 0, 0, 18, 18, 18, 18);
			}
			graphics.blit(SLOT_TEXTURE, i + 25 + 90, j + 106, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_spell_7_mannequin_slot_enabled.get()) {
			if (activeSpellSlotAmount > 6) {
				graphics.blit(SLOT_TEXTURE, i + 43, j + 106, 0, 0, 18, 18, 18, 18);
			}
			graphics.blit(SLOT_TEXTURE, i + 43 + 90, j + 106, 0, 0, 18, 18, 18, 18);
		}
		if (serverConfig.inventorySlots.is_spell_8_mannequin_slot_enabled.get()) {
			if (activeSpellSlotAmount > 7) {
				graphics.blit(SLOT_TEXTURE, i + 61, j + 106, 0, 0, 18, 18, 18, 18);
			}
			graphics.blit(SLOT_TEXTURE, i + 61 + 90, j + 106, 0, 0, 18, 18, 18, 18);
		}

		if (RPGInventory.isHandSlotOverhaulActive()) {
			graphics.blit(SLOT_TEXTURE, i + 25, j + 52, 0, 0, 18, 18, 18, 18);
			graphics.blit(SLOT_TEXTURE, i + 25, j + 70, 0, 0, 18, 18, 18, 18);
			graphics.blit(SLOT_TEXTURE, i + 43, j + 70, 0, 0, 18, 18, 18, 18);

			graphics.blit(SLOT_TEXTURE, i + 90 + 25, j + 52, 0, 0, 18, 18, 18, 18);
			graphics.blit(SLOT_TEXTURE, i + 90 + 25, j + 70, 0, 0, 18, 18, 18, 18);
			graphics.blit(SLOT_TEXTURE, i + 90 + 43, j + 70, 0, 0, 18, 18, 18, 18);
		}

		boolean showInactiveSlots = RPGInventoryClient.showInactiveInventorySlots();
		for (k = 0; k < (showInactiveSlots ? 27 : Math.min(inventorySize, 27)); ++k) {
			m = (k / 9);
			graphics.blit(SLOT_TEXTURE, i + 7 + (k - (m * 9)) * 18, j + 146 + (m * 18), 0, 0, 18, 18, 18, 18);
		}
		for (k = 0; k < (showInactiveSlots ? 9 : Math.min(hotbarSize, 9)); ++k) {
			graphics.blit(SLOT_TEXTURE, i + 7 + k * 18, j + 204, 0, 0, 18, 18, 18, 18);
		}
	}
}
