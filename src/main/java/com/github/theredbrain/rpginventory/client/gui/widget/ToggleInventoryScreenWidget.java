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
	private static final Identifier PAGE_FORWARD_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("widget/page_forward_highlighted");
	private static final Identifier PAGE_FORWARD_TEXTURE = Identifier.ofVanilla("widget/page_forward");
	private static final Identifier PAGE_BACKWARD_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("widget/page_backward_highlighted");
	private static final Identifier PAGE_BACKWARD_TEXTURE = Identifier.ofVanilla("widget/page_backward");
	private boolean isPressed;
	private final boolean opensToRight;

	public ToggleInventoryScreenWidget(int x, int y, boolean isPressed, boolean opensToRight, PressAction action) {
		super(x, y, 23, 13, ScreenTexts.EMPTY, action, DEFAULT_NARRATION_SUPPLIER);
		this.isPressed = isPressed;
		this.opensToRight = opensToRight;
	}

	public boolean getIsPressed() {
		return this.isPressed;
	}

	public void setIsPressed(boolean isPressed) {
		this.isPressed = isPressed;
	}

	@Override
	public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		Identifier identifier;
		if (this.opensToRight) {
			identifier = this.isPressed ? PAGE_BACKWARD_HIGHLIGHTED_TEXTURE : PAGE_FORWARD_TEXTURE;
		} else {
			identifier = this.isPressed ? PAGE_FORWARD_HIGHLIGHTED_TEXTURE : PAGE_BACKWARD_TEXTURE;
		}
		context.drawGuiTexture(identifier, this.getX(), this.getY(), 23, 13);
	}

	@Override
	public void playDownSound(SoundManager soundManager) {
		soundManager.play(PositionedSoundInstance.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 1.0F));
	}
}
