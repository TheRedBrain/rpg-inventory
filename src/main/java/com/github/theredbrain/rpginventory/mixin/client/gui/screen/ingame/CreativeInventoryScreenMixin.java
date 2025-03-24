package com.github.theredbrain.rpginventory.mixin.client.gui.screen.ingame;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.slotcustomizationapi.api.SlotCustomization;
import dev.emi.trinkets.CreativeTrinketSlot;
import dev.emi.trinkets.Point;
import dev.emi.trinkets.SurvivalTrinketSlot;
import dev.emi.trinkets.TrinketPlayerScreenHandler;
import dev.emi.trinkets.TrinketScreen;
import dev.emi.trinkets.TrinketScreenManager;
import dev.emi.trinkets.TrinketsClient;
import dev.emi.trinkets.api.SlotGroup;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Environment(value = EnvType.CLIENT)
@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeScreenHandler> implements TrinketScreen {

	@Shadow
	private static ItemGroup selectedTab;

	@Shadow
	protected abstract void setSelectedTab(ItemGroup group);

	@Unique
	private static final Identifier TAB_ADVENTURE_INVENTORY_TEXTURE = RPGInventory.identifier("textures/gui/container/adventure_creative_inventory/tab_adventure_inventory.png");
	@Unique
	private static final Identifier SPELL_SLOTS_BACKGROUND = RPGInventory.identifier("textures/gui/container/adventure_creative_inventory/spell_slots_background.png");
	@Unique
	private static final Identifier SLOT_TEXTURE = Identifier.ofVanilla("textures/gui/sprites/container/slot.png");

	private CreativeInventoryScreenMixin() {
		super(null, null, null);
	}

	/**
	 * @author Emi
	 */
	@Redirect(at = @At(value = "INVOKE", target = "net/minecraft/util/collection/DefaultedList.size()I"), method = "setSelectedTab")
	private int size(DefaultedList<ItemStack> list) {
		return 51;
	}

	/**
	 * @author Emi
	 */
	@Inject(method = "setSelectedTab", at = @At("HEAD"))
	private void rpginventory$pre_setSelectedTab(ItemGroup g, CallbackInfo info) {
		if (g.getType() != ItemGroup.Type.INVENTORY) {
			TrinketScreenManager.removeSelections();
		}
	}

	/**
	 * Modified and expanded code by @Emi
	 */
	@Inject(method = "setSelectedTab", at = @At("TAIL"))
	private void rpginventory$post_setSelectedTab(ItemGroup group, CallbackInfo ci) {

		if (selectedTab.getType() == ItemGroup.Type.INVENTORY) {
			// reposition vanilla armor slots
			((SlotCustomization) this.handler.slots.get(5)).slotcustomizationapi$setX(9);
			((SlotCustomization) this.handler.slots.get(5)).slotcustomizationapi$setY(6);
			((SlotCustomization) this.handler.slots.get(6)).slotcustomizationapi$setX(45);
			((SlotCustomization) this.handler.slots.get(6)).slotcustomizationapi$setY(6);
			((SlotCustomization) this.handler.slots.get(7)).slotcustomizationapi$setX(27);
			((SlotCustomization) this.handler.slots.get(7)).slotcustomizationapi$setY(33);
			((SlotCustomization) this.handler.slots.get(8)).slotcustomizationapi$setX(45);
			((SlotCustomization) this.handler.slots.get(8)).slotcustomizationapi$setY(33);

			// reposition vanilla offhand slot
			((SlotCustomization) this.handler.slots.get(45)).slotcustomizationapi$setX(117);
			((SlotCustomization) this.handler.slots.get(45)).slotcustomizationapi$setY(33);

			// reposition additional hand slots
			((SlotCustomization) this.handler.slots.get(46)).slotcustomizationapi$setX(99);
			((SlotCustomization) this.handler.slots.get(46)).slotcustomizationapi$setY(33);
			((SlotCustomization) this.handler.slots.get(47)).slotcustomizationapi$setX(99);
			((SlotCustomization) this.handler.slots.get(47)).slotcustomizationapi$setY(33);
			((SlotCustomization) this.handler.slots.get(48)).slotcustomizationapi$setX(117);
			((SlotCustomization) this.handler.slots.get(48)).slotcustomizationapi$setY(33);
			((SlotCustomization) this.handler.slots.get(49)).slotcustomizationapi$setX(135);
			((SlotCustomization) this.handler.slots.get(49)).slotcustomizationapi$setY(33);
			((SlotCustomization) this.handler.slots.get(50)).slotcustomizationapi$setX(153);
			((SlotCustomization) this.handler.slots.get(50)).slotcustomizationapi$setY(33);
		}
	}

	/**
	 * @author Emi
	 */
	@Inject(at = @At(value = "INVOKE", target = "net/minecraft/screen/slot/Slot.<init>(Lnet/minecraft/inventory/Inventory;III)V"), method = "setSelectedTab")
	private void rpginventory$addCreativeTrinketSlots(ItemGroup g, CallbackInfo info) {
		TrinketPlayerScreenHandler handler = trinkets$getHandler();
		for (int i = handler.trinkets$getTrinketSlotStart(); i < handler.trinkets$getTrinketSlotEnd(); i++) {
			Slot slot = this.client.player.playerScreenHandler.slots.get(i);
			if (slot instanceof SurvivalTrinketSlot ts) {
				SlotGroup group = TrinketsApi.getPlayerSlots(this.client.player).get(ts.getType().getGroup());
				Rect2i rect = trinkets$getGroupRect(group);
				Point pos = trinkets$getHandler().trinkets$getGroupPos(group);
				if (pos == null) {
					return;
				}
				int xOff = rect.getX() + 1 - pos.x();
				int yOff = rect.getY() + 1 - pos.y();
				((CreativeInventoryScreen.CreativeScreenHandler) this.handler).slots.add(new CreativeTrinketSlot(ts, ts.getIndex(), ts.x + xOff, ts.y + yOff));
			}
		}
	}

	/**
	 * @author Emi
	 */
	@Inject(at = @At("HEAD"), method = "init")
	private void rpginventory$init(CallbackInfo info) {
		TrinketScreenManager.init(this);
	}

	/**
	 * @author Emi
	 */
	@Inject(at = @At("HEAD"), method = "removed")
	private void rpginventory$removed(CallbackInfo info) {
		TrinketScreenManager.removeSelections();
	}

	/**
	 * @author Emi
	 */
	@Inject(at = @At("TAIL"), method = "handledScreenTick")
	private void rpginventory$handledScreenTick(CallbackInfo info) {
		TrinketScreenManager.tick();
	}

	/**
	 * @author Emi
	 */
	@Inject(at = @At("HEAD"), method = "render")
	private void rpginventory$render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo info) {
		if (selectedTab.getType() == ItemGroup.Type.INVENTORY) {
			TrinketScreenManager.update(mouseX, mouseY);
		}
	}

	/**
	 * Modified and expanded code by @Emi
	 */
	@Inject(at = @At("RETURN"), method = "drawBackground")
	private void rpginventory$drawBackground(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo info) {
		if (selectedTab.getType() == ItemGroup.Type.INVENTORY) {
			TrinketScreenManager.drawExtraGroups(context);
			int x = this.trinkets$getX() + this.backgroundWidth;
			int y = this.trinkets$getY();
			context.drawTexture(SPELL_SLOTS_BACKGROUND, x - 4, y, 0, 0, 44, 86, 44, 86);

			int inventorySize = 0;
			int hotbarSize = 0;
			if (this.client != null && this.client.player != null) {
				hotbarSize = RPGInventory.getActiveHotbarSize(this.client.player);
				inventorySize = RPGInventory.getActiveInventorySize(this.client.player);
			}

			int i = this.x;
			int j = this.y;
			int k;
			int m;
			boolean showInactiveSlots = RPGInventoryClient.showInactiveInventorySlots();
			for (k = 0; k < (showInactiveSlots ? 27 : Math.min(inventorySize, 27)); ++k) {
				m = (k / 9);
				context.drawTexture(SLOT_TEXTURE, i + 8 + (k - (m * 9)) * 18, j + 53 + (m * 18), 0, 0, 18, 18, 18, 18);
			}
			for (k = 0; k < (showInactiveSlots ? 9 : Math.min(hotbarSize, 9)); ++k) {
				context.drawTexture(SLOT_TEXTURE, i + 8 + k * 18, j + 111, 0, 0, 18, 18, 18, 18);
			}

		}
	}

	@Inject(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;render(Lnet/minecraft/client/gui/DrawContext;IIF)V"))
	private void rpginventory$drawAdventureInventoryBackground(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
		if (selectedTab.getType() == ItemGroup.Type.INVENTORY) {
			context.drawTexture(TAB_ADVENTURE_INVENTORY_TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
		}
	}

	@ModifyArgs(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;drawEntity(Lnet/minecraft/client/gui/DrawContext;IIIIIFFFLnet/minecraft/entity/LivingEntity;)V"))
	private void rpginventory$moveDrawnPlayerEntity(Args args, DrawContext context, float delta, int mouseX, int mouseY) {
		args.set(1, this.x + 64);
		args.set(3, this.x + 96);
	}

	/**
	 * @author Emi
	 */
	@Inject(at = @At("TAIL"), method = "drawForeground")
	private void rpginventory$drawForeground(DrawContext context, int mouseX, int mouseY, CallbackInfo info) {
		if (selectedTab.getType() == ItemGroup.Type.INVENTORY) {
			TrinketScreenManager.drawActiveGroup(context);
		}
	}

	/**
	 * @author Emi
	 */
	@Inject(at = @At("HEAD"), method = "isClickOutsideBounds", cancellable = true)
	private void rpginventory$isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button, CallbackInfoReturnable<Boolean> info) {
		if (selectedTab.getType() == ItemGroup.Type.INVENTORY && TrinketScreenManager.isClickInsideTrinketBounds(mouseX, mouseY)) {
			info.setReturnValue(false);
		}
	}

	/**
	 * @author Emi
	 */
	@Inject(at = @At("HEAD"), method = "isClickInTab", cancellable = true)
	private void rpginventory$isClickInTab(ItemGroup group, double mouseX, double mouseY, CallbackInfoReturnable<Boolean> info) {
		if (TrinketsClient.activeGroup != null) {
			info.setReturnValue(false);
		}
	}

	/**
	 * @author Emi
	 */
	@Inject(at = @At("HEAD"), method = "renderTabTooltipIfHovered", cancellable = true)
	private void rpginventory$renderTabTooltipIfHovered(DrawContext context, ItemGroup group, int mouseX, int mouseY, CallbackInfoReturnable<Boolean> info) {
		if (TrinketsClient.activeGroup != null) {
			info.setReturnValue(false);
		}
	}

	/**
	 * @author Emi
	 */
	@Override
	public TrinketPlayerScreenHandler trinkets$getHandler() {
		return (TrinketPlayerScreenHandler) this.client.player.playerScreenHandler;
	}

	/**
	 * Modified and expanded code by @Emi
	 */
	@Override
	public Rect2i trinkets$getGroupRect(SlotGroup group) {
		String groupName = group.getName();
		ServerConfig.InventorySlots.SlotGroupPosition slotGroupPosition = RPGInventory.SERVER_CONFIG.inventorySlots.slot_group_positions.get(groupName);
		if (slotGroupPosition != null) {
			return new Rect2i(slotGroupPosition.creative_x, slotGroupPosition.creative_y, 17, 17);
		}
		int groupNum = trinkets$getHandler().trinkets$getGroupNum(group);
		if (groupNum <= 0) {
			return switch (groupNum) {
				// head
				case -5 -> new Rect2i(8, 5, 17, 17);
				// chest
				case -6 -> new Rect2i(44, 5, 17, 17);
				// legs
				case -7 -> new Rect2i(26, 32, 17, 17);
				// feet
				case -8 -> new Rect2i(44, 32, 17, 17);
				// offhand
				case -45 -> new Rect2i(116, 32, 17, 17);
				// main hand
				case -46 -> new Rect2i(98, 32, 17, 17);
				// sheathed main hand
				case -47 -> new Rect2i(98, 32, 17, 17);
				// sheathed offhand
				case -48 -> new Rect2i(116, 32, 17, 17);
				// alternative main hand
				case -49 -> new Rect2i(134, 32, 17, 17);
				// alternative offhand
				case -50 -> new Rect2i(152, 32, 17, 17);
				default -> new Rect2i(0, 0, 0, 0);
			};
		}
		Point pos = trinkets$getHandler().trinkets$getGroupPos(group);
		if (pos != null) {
			return new Rect2i(pos.x() - 1, pos.y() - 1, 17, 17);
		}
		return new Rect2i(0, 0, 0, 0);
	}

	/**
	 * @author Emi
	 */
	@Override
	public Slot trinkets$getFocusedSlot() {
		return this.focusedSlot;
	}

	/**
	 * @author Emi
	 */
	@Override
	public int trinkets$getX() {
		return this.x;
	}

	/**
	 * @author Emi
	 */
	@Override
	public int trinkets$getY() {
		return this.y;
	}

	/**
	 * @author Emi
	 */
	@Override
	public boolean trinkets$isRecipeBookOpen() {
		return false;
	}

	/**
	 * @author Emi
	 */
	@Override
	public void trinkets$updateTrinketSlots() {
		setSelectedTab(selectedTab);
	}
}
