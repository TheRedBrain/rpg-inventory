package com.github.theredbrain.rpginventory.gui.screen.ingame;

import com.github.theredbrain.rpginventory.screen.AbstractMannequinScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public abstract class AbstractMannequinScreen<T extends AbstractMannequinScreenHandler> extends HandledScreen<T> {
	public static final Identifier SLOT_TEXTURE = Identifier.ofVanilla("textures/gui/sprites/container/slot.png");
	public static final Text EQUIP_BUTTON_LABEL = Text.translatable("gui.mannequin.equip_button_label");
	public static final Text UNEQUIP_BUTTON_LABEL = Text.translatable("gui.mannequin.unequip_button_label");

	public AbstractMannequinScreen(T handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	protected void init() {

		this.titleX = 98;
		this.titleY = 6;
		this.playerInventoryTitleX = 8;
		this.playerInventoryTitleY = 6;

		super.init();
	}

	protected void buttonCallback(int index) {
		if (this.client != null && this.client.interactionManager != null && this.handler.onButtonClick(this.client.player, index)) {

			MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));

			this.client.interactionManager.clickButton(this.handler.syncId, index);
		}
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(context, mouseX, mouseY);
	}

}
