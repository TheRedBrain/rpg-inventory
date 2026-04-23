package com.github.theredbrain.rpginventory.gui.screen.ingame;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.screen.VanillaMannequinScreenHandler;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class VanillaMannequinScreen extends AbstractMannequinScreen<VanillaMannequinScreenHandler> {

	public static final Identifier MANNEQUIN_BACKGROUND_TEXTURE = RPGInventory.identifier("textures/gui/container/vanilla_mannequin.png");

	public VanillaMannequinScreen(VanillaMannequinScreenHandler handler, Inventory inventory, Component title) {
		super(handler, inventory, title);
	}

	@Override
	protected void init() {

		super.init();

		Button equipButton = this.addRenderableWidget(Button.builder(EQUIP_BUTTON_LABEL, button -> this.buttonCallback(0)).bounds(this.leftPos + 7, this.topPos + 59, 72, 20).build());
		this.addRenderableWidget(Button.builder(UNEQUIP_BUTTON_LABEL, button -> this.buttonCallback(1)).bounds(this.leftPos + 97, this.topPos + 59, 72, 20).build());

		equipButton.active = this.menu.canEquip();
	}

	@Override
	public void extractBackground(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
		int i = this.leftPos;
		int j = this.topPos;
		int k;
		int m;
		int inventorySize = 0;
		int hotbarSize = 0;
		if (this.minecraft.player != null) {
			hotbarSize = RPGInventory.getActiveHotbarSize(this.minecraft.player);
			inventorySize = RPGInventory.getActiveInventorySize(this.minecraft.player);
		}

		graphics.blit(MANNEQUIN_BACKGROUND_TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

		boolean showInactiveSlots = RPGInventoryClient.showInactiveInventorySlots();
		for (k = 0; k < (showInactiveSlots ? 27 : Math.min(inventorySize, 27)); ++k) {
			m = (k / 9);
			graphics.blit(SLOT_TEXTURE, i + 7 + (k - (m * 9)) * 18, j + 83 + (m * 18), 0, 0, 18, 18, 18, 18);
		}
		for (k = 0; k < (showInactiveSlots ? 9 : Math.min(hotbarSize, 9)); ++k) {
			graphics.blit(SLOT_TEXTURE, i + 7 + k * 18, j + 141, 0, 0, 18, 18, 18, 18);
		}
	}
}
