package com.github.theredbrain.rpginventory.mixin.client.gui.screen.ingame;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.slotcustomizationapi.api.SlotCustomization;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;

@Environment(value = EnvType.CLIENT)
@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeScreenHandler> {

	@Shadow
	private static ItemGroup selectedTab;

	@Shadow
	public abstract List<Text> getTooltipFromItem(ItemStack stack);

	@Unique
	private static final Identifier TAB_ADVENTURE_INVENTORY_TEXTURE = RPGInventory.identifier("textures/gui/container/adventure_creative_inventory/tab_adventure_inventory.png");
	@Unique
	private static final Identifier SPELL_SLOTS_BACKGROUND = RPGInventory.identifier("textures/gui/container/adventure_creative_inventory/spell_slots_background.png");
	@Unique
	private static final Identifier SLOT_TEXTURE = Identifier.ofVanilla("textures/gui/sprites/container/slot.png");

	private CreativeInventoryScreenMixin() {
		super(null, null, null);
	}

	@Inject(method = "setSelectedTab", at = @At("TAIL"))
	private void rpginventory$post_setSelectedTab(ItemGroup group, CallbackInfo ci) {

		if (selectedTab.getType() == ItemGroup.Type.INVENTORY) {

			if (RPGInventory.SERVER_CONFIG.activate_rpg_inventory_screen.get()) {

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

				// reposition additional equipment slots
				((SlotCustomization) this.handler.slots.get(51)).slotcustomizationapi$setX(117);
				((SlotCustomization) this.handler.slots.get(51)).slotcustomizationapi$setY(6);
				((SlotCustomization) this.handler.slots.get(52)).slotcustomizationapi$setX(9);
				((SlotCustomization) this.handler.slots.get(52)).slotcustomizationapi$setY(33);
				((SlotCustomization) this.handler.slots.get(53)).slotcustomizationapi$setX(99);
				((SlotCustomization) this.handler.slots.get(53)).slotcustomizationapi$setY(6);
				((SlotCustomization) this.handler.slots.get(54)).slotcustomizationapi$setX(153);
				((SlotCustomization) this.handler.slots.get(54)).slotcustomizationapi$setY(6);
				((SlotCustomization) this.handler.slots.get(55)).slotcustomizationapi$setX(173);
				((SlotCustomization) this.handler.slots.get(55)).slotcustomizationapi$setY(19);
				((SlotCustomization) this.handler.slots.get(56)).slotcustomizationapi$setX(27);
				((SlotCustomization) this.handler.slots.get(56)).slotcustomizationapi$setY(6);
				((SlotCustomization) this.handler.slots.get(57)).slotcustomizationapi$setX(193);
				((SlotCustomization) this.handler.slots.get(57)).slotcustomizationapi$setY(8);
				((SlotCustomization) this.handler.slots.get(58)).slotcustomizationapi$setX(193);
				((SlotCustomization) this.handler.slots.get(58)).slotcustomizationapi$setY(26);
				((SlotCustomization) this.handler.slots.get(59)).slotcustomizationapi$setX(193);
				((SlotCustomization) this.handler.slots.get(59)).slotcustomizationapi$setY(44);
				((SlotCustomization) this.handler.slots.get(60)).slotcustomizationapi$setX(193);
				((SlotCustomization) this.handler.slots.get(60)).slotcustomizationapi$setY(62);
				((SlotCustomization) this.handler.slots.get(61)).slotcustomizationapi$setX(211);
				((SlotCustomization) this.handler.slots.get(61)).slotcustomizationapi$setY(8);
				((SlotCustomization) this.handler.slots.get(62)).slotcustomizationapi$setX(211);
				((SlotCustomization) this.handler.slots.get(62)).slotcustomizationapi$setY(26);
				((SlotCustomization) this.handler.slots.get(63)).slotcustomizationapi$setX(211);
				((SlotCustomization) this.handler.slots.get(63)).slotcustomizationapi$setY(44);
				((SlotCustomization) this.handler.slots.get(64)).slotcustomizationapi$setX(211);
				((SlotCustomization) this.handler.slots.get(64)).slotcustomizationapi$setY(62);
				((SlotCustomization) this.handler.slots.get(65)).slotcustomizationapi$setX(135);
				((SlotCustomization) this.handler.slots.get(65)).slotcustomizationapi$setY(6);
			} else {
				for (int i = 46; i < 66; i++) {
					((SlotCustomization) this.handler.slots.get(i)).slotcustomizationapi$setDisabledOverride(true);
				}
			}
		}
	}

	@Inject(at = @At("RETURN"), method = "drawBackground")
	private void rpginventory$drawBackground(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo info) {
		if (selectedTab.getType() == ItemGroup.Type.INVENTORY && RPGInventory.SERVER_CONFIG.activate_rpg_inventory_screen.get()) {
			int x = this.x + this.backgroundWidth;
			int y = this.y;
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
		if (selectedTab.getType() == ItemGroup.Type.INVENTORY && RPGInventory.SERVER_CONFIG.activate_rpg_inventory_screen.get()) {
			context.drawTexture(TAB_ADVENTURE_INVENTORY_TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
		}
	}

	@ModifyArgs(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;drawEntity(Lnet/minecraft/client/gui/DrawContext;IIIIIFFFLnet/minecraft/entity/LivingEntity;)V"))
	private void rpginventory$moveDrawnPlayerEntity(Args args, DrawContext context, float delta, int mouseX, int mouseY) {
		if (RPGInventory.SERVER_CONFIG.activate_rpg_inventory_screen.get()) {
			args.set(1, this.x + 64);
			args.set(3, this.x + 96);
		}
	}
}
