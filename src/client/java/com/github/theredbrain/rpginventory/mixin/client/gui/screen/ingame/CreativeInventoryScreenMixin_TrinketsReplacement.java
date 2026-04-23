package com.github.theredbrain.rpginventory.mixin.client.gui.screen.ingame;
//
//import com.github.theredbrain.rpginventory.RPGInventory;
//import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
//import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//import dev.emi.trinkets.CreativeTrinketSlot;
//import dev.emi.trinkets.Point;
//import dev.emi.trinkets.SurvivalTrinketSlot;
//import dev.emi.trinkets.TrinketPlayerScreenHandler;
//import dev.emi.trinkets.TrinketScreen;
//import dev.emi.trinkets.TrinketScreenManager;
//import dev.emi.trinkets.TrinketsClient;
//import dev.emi.trinkets.api.SlotGroup;
//import dev.emi.trinkets.api.TrinketsApi;
//import net.fabricmc.api.EnvType;
//import net.fabricmc.api.Environment;
//import net.minecraft.client.gui.GuiGraphics;
//import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
//import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
//import net.minecraft.client.renderer.Rect2i;
//import net.minecraft.core.NonNullList;
//import net.minecraft.world.inventory.Slot;
//import net.minecraft.world.item.CreativeModeTab;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
///**
// * @author Emi
// */
//@Environment(value = EnvType.CLIENT)
//@Mixin(CreativeModeInventoryScreen.class)
//public abstract class CreativeInventoryScreenMixin_TrinketsReplacement extends EffectRenderingInventoryScreen<CreativeModeInventoryScreen.ItemPickerMenu> implements TrinketScreen {
//	@Shadow
//	private static CreativeModeTab selectedTab;
//
//	@Shadow
//	protected abstract void setSelectedTab(CreativeModeTab group);
//
//	private CreativeInventoryScreenMixin_TrinketsReplacement() {
//		super(null, null, null);
//	}
//
//	/**
//	 * Modified and expanded code by @Emi
//	 *
//	 * @author TheRedBrain
//	 */
//	@WrapOperation(at = @At(value = "INVOKE", target = "net/minecraft/util/collection/DefaultedList.size()I"), method = "setSelectedTab")
//	private int size(NonNullList<Slot> instance, Operation<Integer> original) {
//		// account for custom equipment slots
//		return 66;
//	}
//
//	@Inject(at = @At("HEAD"), method = "setSelectedTab")
//	private void rpginventory$setSelectedTab(CreativeModeTab g, CallbackInfo info) {
//		if (g.getType() != CreativeModeTab.Type.INVENTORY) {
//			TrinketScreenManager.removeSelections();
//		}
//	}
//
//	@Inject(at = @At(value = "INVOKE", target = "net/minecraft/screen/slot/Slot.<init>(Lnet/minecraft/inventory/Inventory;III)V"), method = "setSelectedTab")
//	private void addCreativeTrinketSlots(CreativeModeTab g, CallbackInfo info) {
//		TrinketPlayerScreenHandler handler = trinkets$getHandler();
//		for (int i = handler.trinkets$getTrinketSlotStart(); i < handler.trinkets$getTrinketSlotEnd(); i++) {
//			Slot slot = this.minecraft.player.inventoryMenu.slots.get(i);
//			if (slot instanceof SurvivalTrinketSlot ts) {
//				SlotGroup group = TrinketsApi.getPlayerSlots(this.minecraft.player).get(ts.getType().getGroup());
//				Rect2i rect = trinkets$getGroupRect(group);
//				Point pos = trinkets$getHandler().trinkets$getGroupPos(group);
//				if (pos == null) {
//					return;
//				}
//				int xOff = rect.getX() + 1 - pos.x();
//				int yOff = rect.getY() + 1 - pos.y();
//				((CreativeModeInventoryScreen.ItemPickerMenu) this.menu).slots.add(new CreativeTrinketSlot(ts, ts.getContainerSlot(), ts.x + xOff, ts.y + yOff));
//			}
//		}
//	}
//
//	@Inject(at = @At("HEAD"), method = "init")
//	private void init(CallbackInfo info) {
//		TrinketScreenManager.init(this);
//	}
//
//	@Inject(at = @At("HEAD"), method = "removed")
//	private void removed(CallbackInfo info) {
//		TrinketScreenManager.removeSelections();
//	}
//
//	@Inject(at = @At("TAIL"), method = "handledScreenTick")
//	private void tick(CallbackInfo info) {
//		TrinketScreenManager.tick();
//	}
//
//	@Inject(at = @At("HEAD"), method = "render")
//	private void render(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo info) {
//		if (selectedTab.getType() == CreativeModeTab.Type.INVENTORY) {
//			TrinketScreenManager.update(mouseX, mouseY);
//		}
//	}
//
//	@Inject(at = @At("RETURN"), method = "drawBackground")
//	private void drawBackground(GuiGraphics context, float delta, int mouseX, int mouseY, CallbackInfo info) {
//		if (selectedTab.getType() == CreativeModeTab.Type.INVENTORY) {
//			TrinketScreenManager.drawExtraGroups(context);
//		}
//	}
//
//	@Inject(at = @At("TAIL"), method = "drawForeground")
//	private void drawForeground(GuiGraphics context, int mouseX, int mouseY, CallbackInfo info) {
//		if (selectedTab.getType() == CreativeModeTab.Type.INVENTORY) {
//			TrinketScreenManager.drawActiveGroup(context);
//		}
//	}
//
//	@Inject(at = @At("HEAD"), method = "isClickOutsideBounds", cancellable = true)
//	private void isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button, CallbackInfoReturnable<Boolean> info) {
//		if (selectedTab.getType() == CreativeModeTab.Type.INVENTORY && TrinketScreenManager.isClickInsideTrinketBounds(mouseX, mouseY)) {
//			info.setReturnValue(false);
//		}
//	}
//
//	@Inject(at = @At("HEAD"), method = "isClickInTab", cancellable = true)
//	private void isClickInTab(CreativeModeTab group, double mouseX, double mouseY, CallbackInfoReturnable<Boolean> info) {
//		if (TrinketsClient.activeGroup != null) {
//			info.setReturnValue(false);
//		}
//	}
//
//	@Inject(at = @At("HEAD"), method = "renderTabTooltipIfHovered", cancellable = true)
//	private void renderTabTooltipIfHovered(GuiGraphics context, CreativeModeTab group, int mouseX, int mouseY, CallbackInfoReturnable<Boolean> info) {
//		if (TrinketsClient.activeGroup != null) {
//			info.setReturnValue(false);
//		}
//	}
//
//	@Override
//	public TrinketPlayerScreenHandler trinkets$getHandler() {
//		return (TrinketPlayerScreenHandler) this.minecraft.player.inventoryMenu;
//	}
//
//	@Override
//	public Rect2i trinkets$getGroupRect(SlotGroup group) {
//		int groupNum = trinkets$getHandler().trinkets$getGroupNum(group);
//		if (RPGInventory.SERVER_CONFIG.activate_rpg_inventory_screen.get()) {
//
//			if (groupNum < 0) {
//				return switch (groupNum) {
//					// head
//					case -5 -> new Rect2i(8, 5, 17, 17);
//					// chest
//					case -6 -> new Rect2i(44, 5, 17, 17);
//					// legs
//					case -7 -> new Rect2i(26, 32, 17, 17);
//					// feet
//					case -8 -> new Rect2i(44, 32, 17, 17);
//					// offhand
//					case -45 -> new Rect2i(116, 32, 17, 17);
//					// main hand
//					case -46 -> new Rect2i(98, 32, 17, 17);
//					// sheathed main hand
//					case -47 -> new Rect2i(98, 32, 17, 17);
//					// sheathed offhand
//					case -48 -> new Rect2i(116, 32, 17, 17);
//					// alternative main hand
//					case -49 -> new Rect2i(134, 32, 17, 17);
//					// alternative offhand
//					case -50 -> new Rect2i(152, 32, 17, 17);
//					default -> new Rect2i(0, 0, 0, 0);
//				};
//			}
//			Point pos = trinkets$getHandler().trinkets$getGroupPos(group);
//			if (pos != null) {
//				return new Rect2i(pos.x() - 1, pos.y() - 1, 17, 17);
//			}
//			return new Rect2i(0, 0, 0, 0);
//		} else {
//			if (groupNum <= 3) {
//				// Look what else do you want me to do
//				return switch (groupNum) {
//					case -45 -> new Rect2i(34, 19, 17, 17);
//					case -8 -> new Rect2i(107, 32, 17, 17);
//					case -7 -> new Rect2i(107, 5, 17, 17);
//					case -6 -> new Rect2i(53, 32, 17, 17);
//					case -5 -> new Rect2i(53, 5, 17, 17);
//					case 1 -> new Rect2i(15, 19, 17, 17);
//					case 2 -> new Rect2i(126, 19, 17, 17);
//					case 3 -> new Rect2i(145, 19, 17, 17);
//					default -> new Rect2i(0, 0, 0, 0);
//				};
//			} else {
//				Point pos = this.trinkets$getHandler().trinkets$getGroupPos(group);
//				return pos != null ? new Rect2i(pos.x() - 1, pos.y() - 1, 17, 17) : new Rect2i(0, 0, 0, 0);
//			}
//		}
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
//		return false;
//	}
//
//	@Override
//	public void trinkets$updateTrinketSlots() {
//		setSelectedTab(selectedTab);
//	}
//}
