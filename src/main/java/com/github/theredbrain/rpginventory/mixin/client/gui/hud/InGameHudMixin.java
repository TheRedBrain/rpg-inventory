package com.github.theredbrain.rpginventory.mixin.client.gui.hud;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.config.ClientConfig;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
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

	@Shadow @Final private static Identifier HOTBAR_SELECTION_TEXTURE;
	@Unique
	private static final Identifier UNSHEATHED_RIGHT_HAND_SLOT_SELECTOR_TEXTURE = RPGInventory.identifier("textures/gui/sprites/hud/unsheathed_right_hand_slot_selector.png");
	@Unique
	private static final Identifier HOTBAR_HAND_SLOTS_TEXTURE = RPGInventory.identifier("textures/gui/sprites/hud/hotbar_hand_slots.png");
	@Unique
	private static final Identifier HOTBAR_ALTERNATE_HAND_SLOTS_TEXTURE = RPGInventory.identifier("textures/gui/sprites/hud/hotbar_alternate_hand_slots.png");

	@Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"))
	private void rpginventory$invoke_renderHotbar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {

		int i = context.getScaledWindowWidth() / 2;
		// slots for all hand items
//            context.drawGuiTexture(HOTBAR_HAND_SLOTS_TEXTURE, i - 91 - 49, context.getScaledWindowHeight() - 23 - raisedDistance, 49, 24);
		context.drawGuiTexture(HOTBAR_HAND_SLOTS_TEXTURE, i + RPGInventoryClient.clientConfig.hand_slots_x_offset, context.getScaledWindowHeight() + RPGInventoryClient.clientConfig.hand_slots_y_offset, 49, 24);
//            context.drawGuiTexture(HOTBAR_ALTERNATE_HAND_SLOTS_TEXTURE, i + 91, context.getScaledWindowHeight() - 23 - raisedDistance, 49, 24);
		context.drawGuiTexture(HOTBAR_ALTERNATE_HAND_SLOTS_TEXTURE, i + RPGInventoryClient.clientConfig.alternative_hand_slots_x_offset, context.getScaledWindowHeight() + RPGInventoryClient.clientConfig.alternative_hand_slots_y_offset, 49, 24);

	}

	@Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"))
	private void rpginventory$post_renderHotbar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		PlayerEntity playerEntity = this.getCameraPlayer();
		if (playerEntity != null) {
			ClientConfig clientConfig = RPGInventoryClient.clientConfig;
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

			int x = context.getScaledWindowWidth() / 2 + clientConfig.hand_slots_x_offset;
			int y = context.getScaledWindowHeight() + 4 + clientConfig.hand_slots_y_offset;
			boolean offhand_slot_is_right = clientConfig.offhand_item_is_right;
			this.renderHotbarItem(context, x + 23, y, tickCounter, playerEntity, offhand_slot_is_right ? itemStackOffHand : itemStackHand, l++);
			this.renderHotbarItem(context, x + 3, y, tickCounter, playerEntity, offhand_slot_is_right ? itemStackHand : itemStackOffHand, l++);

			// sheathed hand indicator
			if ((!isHandSheathed && offhand_slot_is_right) || (!isOffhandSheathed && !offhand_slot_is_right)) {
				context.drawGuiTexture(HOTBAR_SELECTION_TEXTURE, x - 1, y - 4, 24, 24);
			}
			if ((!isOffhandSheathed && offhand_slot_is_right) || (!isHandSheathed && !offhand_slot_is_right)) {
				context.drawGuiTexture(UNSHEATHED_RIGHT_HAND_SLOT_SELECTOR_TEXTURE, x + 19, y - 4, 24, 24);
			}


			x = context.getScaledWindowWidth() / 2 + clientConfig.alternative_hand_slots_x_offset;
			y = context.getScaledWindowHeight() + 4 + clientConfig.alternative_hand_slots_y_offset;
			boolean alternative_offhand_slot_is_right = clientConfig.alternative_offhand_item_is_right;
			this.renderHotbarItem(context, x + 10, y, tickCounter, playerEntity, alternative_offhand_slot_is_right ? itemStackAlternativeHand : itemStackAlternativeOffHand, l++);
			this.renderHotbarItem(context, x + 30, y, tickCounter, playerEntity, alternative_offhand_slot_is_right ? itemStackAlternativeOffHand : itemStackAlternativeHand, l);
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
		return isHandSheathed || RPGInventoryClient.clientConfig.always_show_selected_hotbar_slot;
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
		return RPGInventoryClient.clientConfig.show_armor_bar ? instance.getArmor() : 0;
	}

}
