package com.github.theredbrain.rpginventory.mixin.client.gui.screen.ingame;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.client.gui.screen.ingame.RPGInventoryScreen;
import dev.emi.trinkets.Point;
import dev.emi.trinkets.TrinketPlayerScreenHandler;
import dev.emi.trinkets.TrinketScreen;
import dev.emi.trinkets.TrinketScreenManager;
import dev.emi.trinkets.api.SlotGroup;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value = EnvType.CLIENT)
@Mixin(value = RPGInventoryScreen.class, remap = false)
public abstract class RPGInventoryScreenMixin extends HandledScreen<PlayerScreenHandler> implements TrinketScreen {

	@Shadow
	private boolean showAttributeScreen;

	public RPGInventoryScreenMixin(PlayerScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	public void handledScreenTick() {
		TrinketScreenManager.tick();
	}

	@Inject(at = @At("HEAD"), method = "init()V")
	private void rpginventory$init(CallbackInfo info) {
		TrinketScreenManager.init(this);
		RPGInventory.info("mixin applied to RPGInventoryScreen");
	}

	@Inject(at = @At("HEAD"), method = "render")
	public void rpginventory$render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		TrinketScreenManager.update(mouseX, mouseY);
	}

	@Inject(at = @At("TAIL"), method = "drawBackground")
	protected void rpginventory$drawBackground(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
		TrinketScreenManager.drawExtraGroups(context);
	}

	@Override
	protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
		return super.isClickOutsideBounds(mouseX, mouseY, left, top, button) && !(TrinketScreenManager.isClickInsideTrinketBounds(mouseX, mouseY));
	}

	@Override
	protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
		super.drawForeground(context, mouseX, mouseY);
		TrinketScreenManager.drawActiveGroup(context);
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
