package com.github.theredbrain.rpginventory.gui.screen.ingame;

import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.screen.DuckSlotMixin;
import dev.emi.trinkets.Point;
import dev.emi.trinkets.TrinketPlayerScreenHandler;
import dev.emi.trinkets.TrinketScreen;
import dev.emi.trinkets.TrinketScreenManager;
import dev.emi.trinkets.api.SlotGroup;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class RPGInventoryTrinketScreen extends RPGInventoryScreen implements TrinketScreen {

	public RPGInventoryTrinketScreen(PlayerEntity player) {
		super(player);
	}

	@Override
	public void handledScreenTick() {
		super.handledScreenTick();
		TrinketScreenManager.tick();
	}

	@Override
	protected void init() {
		TrinketScreenManager.init(this);
		super.init();
	}

	@Override
	protected void drawMouseoverTooltip(DrawContext context, int x, int y) {
		super.drawMouseoverTooltip(context, x, y);
		if (RPGInventoryClient.CLIENT_CONFIG.rpgInventoryScreenSection.show_slot_tooltips.get() && this.handler.getCursorStack().isEmpty() && this.focusedSlot != null && !this.focusedSlot.hasStack()) {
			if (this.focusedSlot instanceof DuckSlotMixin slotWithTooltip) {
				List<Text> list = slotWithTooltip.rpginventory$getSlotTooltipText();
				if (!list.isEmpty()) {
					context.drawTooltip(this.textRenderer, list, Optional.empty(), x, y);
				}
			}
		}
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		TrinketScreenManager.update(mouseX, mouseY);
		super.render(context, mouseX, mouseY, delta);
	}

	@Override
	protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
		super.drawForeground(context, mouseX, mouseY);
		TrinketScreenManager.drawActiveGroup(context);
	}

	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		super.drawBackground(context, delta, mouseX, mouseY);
		TrinketScreenManager.drawExtraGroups(context);
	}

	@Override
	protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
		return super.isClickOutsideBounds(mouseX, mouseY, left, top, button) && !(TrinketScreenManager.isClickInsideTrinketBounds(mouseX, mouseY));
	}

	@Override
	public TrinketPlayerScreenHandler trinkets$getHandler() {
		return ((TrinketPlayerScreenHandler) this.handler);
	}

	@Override
	public Rect2i trinkets$getGroupRect(SlotGroup group) {
		Point pos = ((TrinketPlayerScreenHandler) handler).trinkets$getGroupPos(group);
		if (pos != null) {
			return new Rect2i(pos.x() - 1, pos.y() - 1, 17, 17);
		}
		return new Rect2i(0, 0, 0, 0);
	}

	@Override
	public Slot trinkets$getFocusedSlot() {
		return this.focusedSlot;
	}

	@Override
	public int trinkets$getX() {
		return this.x;
	}

	@Override
	public int trinkets$getY() {
		return this.y;
	}

	@Override
	public boolean trinkets$isRecipeBookOpen() {
		return this.showAttributeScreen;
	}
}
