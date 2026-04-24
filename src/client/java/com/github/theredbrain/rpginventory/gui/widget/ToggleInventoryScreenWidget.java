package com.github.theredbrain.rpginventory.gui.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;

@Environment(EnvType.CLIENT)
public class ToggleInventoryScreenWidget extends Button {
	private static final Identifier PAGE_FORWARD_HIGHLIGHTED_TEXTURE = Identifier.withDefaultNamespace("widget/page_forward_highlighted");
	private static final Identifier PAGE_FORWARD_TEXTURE = Identifier.withDefaultNamespace("widget/page_forward");
	private static final Identifier PAGE_BACKWARD_HIGHLIGHTED_TEXTURE = Identifier.withDefaultNamespace("widget/page_backward_highlighted");
	private static final Identifier PAGE_BACKWARD_TEXTURE = Identifier.withDefaultNamespace("widget/page_backward");
	private boolean isPressed;
	private final boolean opensToRight;

	public ToggleInventoryScreenWidget(int x, int y, boolean isPressed, boolean opensToRight, OnPress action) {
		super(x, y, 23, 13, CommonComponents.EMPTY, action, DEFAULT_NARRATION);
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
	public void playDownSound(SoundManager soundManager) {
		soundManager.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
	}

	@Override
	protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {

		Identifier identifier;
		if (this.opensToRight) {
			identifier = this.isPressed ? PAGE_BACKWARD_HIGHLIGHTED_TEXTURE : PAGE_FORWARD_TEXTURE;
		} else {
			identifier = this.isPressed ? PAGE_FORWARD_HIGHLIGHTED_TEXTURE : PAGE_BACKWARD_TEXTURE;
		}
		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, identifier, this.getX(), this.getY(), 23, 13);
	}
}
