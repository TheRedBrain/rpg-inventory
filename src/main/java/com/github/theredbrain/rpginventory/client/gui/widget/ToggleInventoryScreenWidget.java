package com.github.theredbrain.rpginventory.client.gui.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ToggleInventoryScreenWidget extends ButtonWidget {
	public static final Identifier BOOK_TEXTURE = new Identifier("textures/gui/book.png");
	private boolean isPressed;

	public ToggleInventoryScreenWidget(int x, int y, boolean isPressed, PressAction action) {
		super(x, y, 23, 13, ScreenTexts.EMPTY, action, DEFAULT_NARRATION_SUPPLIER);
		this.isPressed = isPressed;
	}

	public boolean getIsPressed() {
		return this.isPressed;
	}

	public void setIsPressed(boolean isPressed) {
		this.isPressed = isPressed;
	}

	@Override
	public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
		int i = 0;
		int j = 205;
		if (this.isHovered()) {
			i += 23;
		}

		if (this.isPressed) {
			j -= 13;
		}

		context.drawTexture(BOOK_TEXTURE, this.getX(), this.getY(), i, j, 23, 13);
	}

	@Override
	public void playDownSound(SoundManager soundManager) {
		soundManager.play(PositionedSoundInstance.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 1.0F));
	}
}
