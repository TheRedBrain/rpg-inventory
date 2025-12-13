package com.github.theredbrain.rpginventory.gui.screen.ingame;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.screen.VanillaMannequinScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class VanillaMannequinScreen extends AbstractMannequinScreen<VanillaMannequinScreenHandler> {

	public static final Identifier MANNEQUIN_BACKGROUND_TEXTURE = RPGInventory.identifier("textures/gui/container/vanilla_mannequin.png");

	public VanillaMannequinScreen(VanillaMannequinScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	protected void init() {

		super.init();

		ButtonWidget equipButton = this.addDrawableChild(ButtonWidget.builder(EQUIP_BUTTON_LABEL, button -> this.buttonCallback(0)).dimensions(this.x + 7, this.y + 59, 72, 20).build());
		this.addDrawableChild(ButtonWidget.builder(UNEQUIP_BUTTON_LABEL, button -> this.buttonCallback(1)).dimensions(this.x + 97, this.y + 59, 72, 20).build());

		equipButton.active = this.handler.canEquip();
	}

	@Override
	public void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		int i = this.x;
		int j = this.y;
		int k;
		int m;
		int inventorySize = 0;
		int hotbarSize = 0;
		if (this.client != null && this.client.player != null) {
			hotbarSize = RPGInventory.getActiveHotbarSize(this.client.player);
			inventorySize = RPGInventory.getActiveInventorySize(this.client.player);
		}

		context.drawTexture(MANNEQUIN_BACKGROUND_TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);

		boolean showInactiveSlots = RPGInventoryClient.showInactiveInventorySlots();
		for (k = 0; k < (showInactiveSlots ? 27 : Math.min(inventorySize, 27)); ++k) {
			m = (k / 9);
			context.drawTexture(SLOT_TEXTURE, i + 7 + (k - (m * 9)) * 18, j + 83 + (m * 18), 0, 0, 18, 18, 18, 18);
		}
		for (k = 0; k < (showInactiveSlots ? 9 : Math.min(hotbarSize, 9)); ++k) {
			context.drawTexture(SLOT_TEXTURE, i + 7 + k * 18, j + 141, 0, 0, 18, 18, 18, 18);
		}
	}
}
