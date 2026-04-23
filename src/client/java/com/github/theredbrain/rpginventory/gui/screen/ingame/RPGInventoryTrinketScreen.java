package com.github.theredbrain.rpginventory.gui.screen.ingame;
//
//import dev.emi.trinkets.Point;
//import dev.emi.trinkets.TrinketPlayerScreenHandler;
//import dev.emi.trinkets.TrinketScreen;
//import dev.emi.trinkets.TrinketScreenManager;
//import dev.emi.trinkets.api.SlotGroup;
//import net.fabricmc.api.EnvType;
//import net.fabricmc.api.Environment;
//import net.minecraft.client.gui.GuiGraphics;
//import net.minecraft.client.renderer.Rect2i;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.Slot;
//
//@Environment(EnvType.CLIENT)
//public class RPGInventoryTrinketScreen extends RPGInventoryScreen implements TrinketScreen {
//
//	public RPGInventoryTrinketScreen(Player player) {
//		super(player);
//	}
//
//	@Override
//	public void containerTick() {
//		super.containerTick();
//		TrinketScreenManager.tick();
//	}
//
//	@Override
//	protected void init() {
//		TrinketScreenManager.init(this);
//		super.init();
//	}
//
//	@Override
//	public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
//		TrinketScreenManager.update(mouseX, mouseY);
//		super.render(context, mouseX, mouseY, delta);
//	}
//
//	@Override
//	protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
//		super.renderLabels(context, mouseX, mouseY);
//		TrinketScreenManager.drawActiveGroup(context);
//	}
//
//	@Override
//	protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
//		super.renderBg(context, delta, mouseX, mouseY);
//		TrinketScreenManager.drawExtraGroups(context);
//	}
//
//	@Override
//	protected boolean hasClickedOutside(double mouseX, double mouseY, int left, int top, int button) {
//		return super.hasClickedOutside(mouseX, mouseY, left, top, button) && !(TrinketScreenManager.isClickInsideTrinketBounds(mouseX, mouseY));
//	}
//
//	@Override
//	public TrinketPlayerScreenHandler trinkets$getHandler() {
//		return ((TrinketPlayerScreenHandler) this.menu);
//	}
//
//	@Override
//	public Rect2i trinkets$getGroupRect(SlotGroup group) {
//		Point pos = ((TrinketPlayerScreenHandler) menu).trinkets$getGroupPos(group);
//		if (pos != null) {
//			return new Rect2i(pos.x() - 1, pos.y() - 1, 17, 17);
//		}
//		return new Rect2i(0, 0, 0, 0);
//	}
//
//	@Override
//	public Slot trinkets$getFocusedSlot() {
//		return this.hoveredSlot;
//	}
//
//	@Override
//	public int trinkets$getX() {
//		return this.leftPos;
//	}
//
//	@Override
//	public int trinkets$getY() {
//		return this.topPos;
//	}
//
//	@Override
//	public boolean trinkets$isRecipeBookOpen() {
//		return this.showAttributeScreen;
//	}
//}
