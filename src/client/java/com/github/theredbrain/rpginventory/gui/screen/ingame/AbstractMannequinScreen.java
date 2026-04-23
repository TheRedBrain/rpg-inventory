package com.github.theredbrain.rpginventory.gui.screen.ingame;

import com.github.theredbrain.rpginventory.screen.AbstractMannequinScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;

@Environment(EnvType.CLIENT)
public abstract class AbstractMannequinScreen<T extends AbstractMannequinScreenHandler> extends AbstractContainerScreen<T> {
	public static final Identifier SLOT_TEXTURE = Identifier.withDefaultNamespace("textures/gui/sprites/container/slot.png");
	public static final Component EQUIP_BUTTON_LABEL = Component.translatable("gui.mannequin.equip_button_label");
	public static final Component UNEQUIP_BUTTON_LABEL = Component.translatable("gui.mannequin.unequip_button_label");

	public AbstractMannequinScreen(final T menu, final Inventory inventory, final Component title, final int imageWidth, final int imageHeight) {
		super(menu, inventory, title, imageWidth, imageHeight);
	}

	public AbstractMannequinScreen(final T menu, final Inventory inventory, final Component title) {
		super(menu, inventory, title);
	}

	@Override
	protected void init() {

		this.titleLabelX = 98;
		this.titleLabelY = 6;
		this.inventoryLabelX = 8;
		this.inventoryLabelY = 6;

		super.init();
	}

	protected void buttonCallback(int index) {
		if (this.minecraft.gameMode != null && this.minecraft.player != null && this.menu.clickMenuButton(this.minecraft.player, index)) {

			Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));

			this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, index);
		}
	}

}
