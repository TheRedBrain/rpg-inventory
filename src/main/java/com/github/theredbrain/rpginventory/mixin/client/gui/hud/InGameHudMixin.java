package com.github.theredbrain.rpginventory.mixin.client.gui.hud;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.config.ClientConfig;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

	@Shadow
	protected abstract PlayerEntity getCameraPlayer();

	@Shadow protected abstract void renderHotbarItem(DrawContext context, int x, int y, RenderTickCounter tickCounter, PlayerEntity player, ItemStack stack, int seed);

	@Unique
	private static final Identifier HOTBAR_SELECTION_FIXED_TEXTURE = RPGInventory.identifier("hud/hotbar_selection_fixed");
	@Unique
	private static final Identifier UNSHEATHED_RIGHT_HAND_SLOT_SELECTOR_TEXTURE = RPGInventory.identifier("hud/unsheathed_right_hand_slot_selector");
	@Unique
	private static final Identifier HOTBAR_HAND_SLOTS_TEXTURE = RPGInventory.identifier("hud/hotbar_hand_slots");
	@Unique
	private static final Identifier HOTBAR_ALTERNATE_HAND_SLOTS_TEXTURE = RPGInventory.identifier("hud/hotbar_alternate_hand_slots");

	@Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"))
	private void rpginventory$post_renderHotbar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		PlayerEntity playerEntity = this.getCameraPlayer();
		if (playerEntity != null) {
			ClientConfig.GeneralClientConfig generalClientConfig = RPGInventoryClient.clientConfigHolder.getConfig().generalClientConfig;
			ItemStack itemStackHand = ((DuckPlayerInventoryMixin) playerEntity.getInventory()).rpginventory$getHand();
			ItemStack itemStackOffHand = playerEntity.getInventory().offHand.get(0);
			ItemStack itemStackAlternativeHand = ((DuckPlayerInventoryMixin) playerEntity.getInventory()).rpginventory$getAlternativeHand();
			ItemStack itemStackAlternativeOffHand = ((DuckPlayerInventoryMixin) playerEntity.getInventory()).rpginventory$getAlternativeOffhand();
			boolean isHandSheathed = ((DuckPlayerEntityMixin) playerEntity).rpginventory$isHandStackSheathed();
			boolean isOffhandSheathed = ((DuckPlayerEntityMixin) playerEntity).rpginventory$isOffhandStackSheathed();
			if (isHandSheathed) {
				itemStackHand = ((DuckPlayerInventoryMixin) playerEntity.getInventory()).rpginventory$getSheathedHand();
			}
			if (isOffhandSheathed) {
				itemStackOffHand = ((DuckPlayerInventoryMixin) playerEntity.getInventory()).rpginventory$getSheathedOffhand();
			}

			int l = 10;
			int x;
			int y;

			if (generalClientConfig.show_empty_hand_slots || !(itemStackHand.isEmpty() || itemStackHand.isIn(Tags.EMPTY_HAND_WEAPONS)) || !(itemStackOffHand.isEmpty() || itemStackOffHand.isIn(Tags.EMPTY_HAND_WEAPONS))) {
				x = context.getScaledWindowWidth() / 2 + generalClientConfig.hand_slots_offset_x;
				y = context.getScaledWindowHeight() + generalClientConfig.hand_slots_offset_y;

				context.drawGuiTexture(HOTBAR_HAND_SLOTS_TEXTURE, x, y, 49, 24);

				boolean offhand_slot_is_right = generalClientConfig.offhand_item_is_right;
				this.renderHotbarItem(context, x + 23, y + 4, tickCounter, playerEntity, offhand_slot_is_right ? itemStackOffHand : itemStackHand, l++);
				this.renderHotbarItem(context, x + 3, y + 4, tickCounter, playerEntity, offhand_slot_is_right ? itemStackHand : itemStackOffHand, l++);

				// sheathed hand indicator
				if ((!isHandSheathed && offhand_slot_is_right) || (!isOffhandSheathed && !offhand_slot_is_right)) {
					context.drawGuiTexture(HOTBAR_SELECTION_FIXED_TEXTURE, x - 1, y, 24, 24);
				}
				if ((!isOffhandSheathed && offhand_slot_is_right) || (!isHandSheathed && !offhand_slot_is_right)) {
					context.drawGuiTexture(UNSHEATHED_RIGHT_HAND_SLOT_SELECTOR_TEXTURE, x + 19, y, 24, 24);
				}
			}

			if (generalClientConfig.show_empty_alternative_hand_slots || !(itemStackAlternativeHand.isEmpty() || itemStackAlternativeHand.isIn(Tags.EMPTY_HAND_WEAPONS)) || !(itemStackAlternativeOffHand.isEmpty() || itemStackAlternativeOffHand.isIn(Tags.EMPTY_HAND_WEAPONS))) {
				x = context.getScaledWindowWidth() / 2 + generalClientConfig.alternative_hand_slots_offset_x;
				y = context.getScaledWindowHeight() + generalClientConfig.alternative_hand_slots_offset_y;

				context.drawGuiTexture(HOTBAR_ALTERNATE_HAND_SLOTS_TEXTURE, x, y, 49, 24);

				boolean alternative_offhand_slot_is_right = generalClientConfig.alternative_offhand_item_is_right;
				this.renderHotbarItem(context, x + 10, y + 4, tickCounter, playerEntity, alternative_offhand_slot_is_right ? itemStackAlternativeHand : itemStackAlternativeOffHand, l++);
				this.renderHotbarItem(context, x + 30, y + 4, tickCounter, playerEntity, alternative_offhand_slot_is_right ? itemStackAlternativeOffHand : itemStackAlternativeHand, l);
			}
		}
	}

	@WrapWithCondition(
			method = "renderHotbar",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", ordinal = 1)
	)
	private boolean rpginventory$checkIfSelectedHotbarSlotIndicatorShouldBeRendered(DrawContext instance, Identifier texture, int x, int y, int width, int height) {
		boolean isHandSheathed = false;
		PlayerEntity playerEntity = this.getCameraPlayer();
		if (playerEntity != null) {
			isHandSheathed = ((DuckPlayerEntityMixin) playerEntity).rpginventory$isHandStackSheathed();
		}
		return isHandSheathed || RPGInventoryClient.clientConfigHolder.getConfig().generalClientConfig.always_show_selected_hotbar_slot;
	}

	// effectively disables rendering of the normal offhand slot in the HUD
	@Redirect(
			method = "renderHotbar",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/player/PlayerEntity;getOffHandStack()Lnet/minecraft/item/ItemStack;"
			)
	)
	public ItemStack overhauleddamage$redirect_getOffHandStack(PlayerEntity instance) {
		return ItemStack.EMPTY;
	}

	// disables rendering of the armor bar when disabled in the client config
	@Redirect(
			method = "renderArmor",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/player/PlayerEntity;getArmor()I"
			)
	)
	private static int overhauleddamage$redirect_getArmor(PlayerEntity instance) {
		return RPGInventoryClient.clientConfigHolder.getConfig().generalClientConfig.show_armor_bar ? instance.getArmor() : 0;
	}
}
