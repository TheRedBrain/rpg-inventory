package com.github.theredbrain.rpginventory.gui.hud;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.config.ClientConfig;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;

public class InGameHudHelper {
	private static final Identifier HOTBAR_TEXTURE = Identifier.ofVanilla("hud/hotbar");
	private static final Identifier HOTBAR_OFFHAND_LEFT_TEXTURE = Identifier.ofVanilla("hud/hotbar_offhand_left");
	private static final Identifier HOTBAR_OFFHAND_RIGHT_TEXTURE = Identifier.ofVanilla("hud/hotbar_offhand_right");
	private static final Identifier HOTBAR_ATTACK_INDICATOR_BACKGROUND_TEXTURE = Identifier.ofVanilla("hud/hotbar_attack_indicator_background");
	private static final Identifier HOTBAR_ATTACK_INDICATOR_PROGRESS_TEXTURE = Identifier.ofVanilla("hud/hotbar_attack_indicator_progress");

	private static final Identifier HOTBAR_SELECTION_FIXED_TEXTURE = RPGInventory.identifier("hud/hotbar_selection_fixed");
	private static final Identifier UNSHEATHED_RIGHT_HAND_SLOT_SELECTOR_TEXTURE = RPGInventory.identifier("hud/unsheathed_right_hand_slot_selector");
	private static final Identifier HOTBAR_HAND_SLOTS_TEXTURE = RPGInventory.identifier("hud/hotbar_hand_slots");
	private static final Identifier HOTBAR_ALTERNATE_HAND_SLOTS_TEXTURE = RPGInventory.identifier("hud/hotbar_alternate_hand_slots");

	public static void rpginventory$renderOverhauledHotbar(InGameHud inGameHud, DrawContext context, RenderTickCounter tickCounter) {

		PlayerEntity playerEntity = ((DuckInGameHudMixin) inGameHud).rpginventory$cameraPlayerAccessor();
		MinecraftClient minecraftClient = ((DuckInGameHudMixin) inGameHud).rpginventory$clientAccessor();
		if (playerEntity != null) {
			ClientConfig clientConfig = RPGInventoryClient.CLIENT_CONFIG;
			ItemStack itemStack = playerEntity.getOffHandStack();
			Arm arm = playerEntity.getMainArm().getOpposite();
			int i = context.getScaledWindowWidth() / 2;
			RenderSystem.enableBlend();
			context.getMatrices().push();
			context.getMatrices().translate(0.0F, 0.0F, -90.0F);

			int hotbar_start_x = RPGInventoryClient.drawAlternativeHotbar(context, playerEntity, HOTBAR_TEXTURE);

			boolean isHandSlotOverhaulActive = RPGInventory.isHandSlotOverhaulActive();
			if (((DuckPlayerEntityMixin) playerEntity).rpginventory$isHandStackSheathed() || clientConfig.hotBarOverhaul.always_show_selected_hotbar_slot.get() || !isHandSlotOverhaulActive) {
				context.drawGuiTexture(
						HOTBAR_SELECTION_FIXED_TEXTURE, hotbar_start_x - 1 + playerEntity.getInventory().selectedSlot * 20, context.getScaledWindowHeight() - 22 - 1, 24, 24
				);
			}

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

			int x;
			int y;

			if (isHandSlotOverhaulActive) {
				if (clientConfig.hotBarOverhaul.show_empty_hand_slots.get() || !(itemStackHand.isEmpty() || itemStackHand.isIn(Tags.EMPTY_HAND_WEAPONS)) || !(itemStackOffHand.isEmpty() || itemStackOffHand.isIn(Tags.EMPTY_HAND_WEAPONS))) {
					x = context.getScaledWindowWidth() / 2 + clientConfig.hotBarOverhaul.hand_slots_offset_x.get();
					y = context.getScaledWindowHeight() + clientConfig.hotBarOverhaul.hand_slots_offset_y.get();

					context.drawGuiTexture(HOTBAR_HAND_SLOTS_TEXTURE, x, y, 49, 24);

					boolean offhand_slot_is_right = clientConfig.hotBarOverhaul.offhand_item_is_right.get();

					// sheathed hand indicator
					if ((!isHandSheathed && offhand_slot_is_right) || (!isOffhandSheathed && !offhand_slot_is_right)) {
						context.drawGuiTexture(HOTBAR_SELECTION_FIXED_TEXTURE, x - 1, y, 24, 24);
					}
					if ((!isOffhandSheathed && offhand_slot_is_right) || (!isHandSheathed && !offhand_slot_is_right)) {
						context.drawGuiTexture(UNSHEATHED_RIGHT_HAND_SLOT_SELECTOR_TEXTURE, x + 19, y, 24, 24);
					}
				}

				if (clientConfig.hotBarOverhaul.show_empty_alternative_hand_slots.get() || !(itemStackAlternativeHand.isEmpty() || itemStackAlternativeHand.isIn(Tags.EMPTY_HAND_WEAPONS)) || !(itemStackAlternativeOffHand.isEmpty() || itemStackAlternativeOffHand.isIn(Tags.EMPTY_HAND_WEAPONS))) {
					x = context.getScaledWindowWidth() / 2 + clientConfig.hotBarOverhaul.alternative_hand_slots_offset_x.get();
					y = context.getScaledWindowHeight() + clientConfig.hotBarOverhaul.alternative_hand_slots_offset_y.get();

					context.drawGuiTexture(HOTBAR_ALTERNATE_HAND_SLOTS_TEXTURE, x, y, 49, 24);
				}
			} else {
				if (!itemStack.isEmpty()) {
					if (arm == Arm.LEFT) {
						context.drawGuiTexture(HOTBAR_OFFHAND_LEFT_TEXTURE, i - 91 - 29, context.getScaledWindowHeight() - 23, 29, 24);
					} else {
						context.drawGuiTexture(HOTBAR_OFFHAND_RIGHT_TEXTURE, i - 91 + 182, context.getScaledWindowHeight() - 23, 29, 24);
					}
				}
			}

			context.getMatrices().pop();
			RenderSystem.disableBlend();
			int l = 1;

			int activeHotbarSize = RPGInventory.getActiveHotbarSize(playerEntity);
			for (int m = 0; m < activeHotbarSize; m++) {
				int n = hotbar_start_x + 1 + m * 20 + 2;
				int o = context.getScaledWindowHeight() - 16 - 3;
				((DuckInGameHudMixin) inGameHud).rpginventory$renderHotbarItem_Invoker(context, n, o, tickCounter, playerEntity, playerEntity.getInventory().main.get(m), l++);
			}

			if (isHandSlotOverhaulActive) {
				x = context.getScaledWindowWidth() / 2 + clientConfig.hotBarOverhaul.hand_slots_offset_x.get();
				y = context.getScaledWindowHeight() + clientConfig.hotBarOverhaul.hand_slots_offset_y.get();

				boolean offhand_slot_is_right = clientConfig.hotBarOverhaul.offhand_item_is_right.get();
				((DuckInGameHudMixin) inGameHud).rpginventory$renderHotbarItem_Invoker(context, x + 23, y + 4, tickCounter, playerEntity, offhand_slot_is_right ? itemStackOffHand : itemStackHand, l++);
				((DuckInGameHudMixin) inGameHud).rpginventory$renderHotbarItem_Invoker(context, x + 3, y + 4, tickCounter, playerEntity, offhand_slot_is_right ? itemStackHand : itemStackOffHand, l++);

				x = context.getScaledWindowWidth() / 2 + clientConfig.hotBarOverhaul.alternative_hand_slots_offset_x.get();
				y = context.getScaledWindowHeight() + clientConfig.hotBarOverhaul.alternative_hand_slots_offset_y.get();

				boolean alternative_offhand_slot_is_right = clientConfig.hotBarOverhaul.alternative_offhand_item_is_right.get();
				((DuckInGameHudMixin) inGameHud).rpginventory$renderHotbarItem_Invoker(context, x + 10, y + 4, tickCounter, playerEntity, alternative_offhand_slot_is_right ? itemStackAlternativeHand : itemStackAlternativeOffHand, l++);
				((DuckInGameHudMixin) inGameHud).rpginventory$renderHotbarItem_Invoker(context, x + 30, y + 4, tickCounter, playerEntity, alternative_offhand_slot_is_right ? itemStackAlternativeOffHand : itemStackAlternativeHand, l);
			} else {
				if (!itemStack.isEmpty()) {
					int m = context.getScaledWindowHeight() - 16 - 3;
					if (arm == Arm.LEFT) {
						((DuckInGameHudMixin) inGameHud).rpginventory$renderHotbarItem_Invoker(context, i - 91 - 26, m, tickCounter, playerEntity, itemStack, l++);
					} else {
						((DuckInGameHudMixin) inGameHud).rpginventory$renderHotbarItem_Invoker(context, i - 91 + 182 + 10, m, tickCounter, playerEntity, itemStack, l++);
					}
				}
			}

			if (minecraftClient.options.getAttackIndicator().getValue() == AttackIndicator.HOTBAR) {
				ClientPlayerEntity clientPlayerEntity = minecraftClient.player;
				if (clientPlayerEntity != null) {
					RenderSystem.enableBlend();
					float f = clientPlayerEntity.getAttackCooldownProgress(0.0F);
					if (f < 1.0F) {
						int n = context.getScaledWindowHeight() - 20;
						int o = i + 91 + 6;
						if (arm == Arm.RIGHT) {
							o = i - 91 - 22;
						}

						int p = (int) (f * 19.0F);
						context.drawGuiTexture(HOTBAR_ATTACK_INDICATOR_BACKGROUND_TEXTURE, o, n, 18, 18);
						context.drawGuiTexture(HOTBAR_ATTACK_INDICATOR_PROGRESS_TEXTURE, 18, 18, 0, 18 - p, o, n + 18 - p, 18, p);
					}
				}

				RenderSystem.disableBlend();
			}
		}
	}

}
