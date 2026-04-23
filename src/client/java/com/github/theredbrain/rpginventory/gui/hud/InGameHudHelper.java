package com.github.theredbrain.rpginventory.gui.hud;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.config.ClientConfig;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.rpginventory.registry.Tags;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class InGameHudHelper {
	private static final Identifier HOTBAR_TEXTURE = Identifier.withDefaultNamespace("hud/hotbar");
	private static final Identifier HOTBAR_OFFHAND_LEFT_TEXTURE = Identifier.withDefaultNamespace("hud/hotbar_offhand_left");
	private static final Identifier HOTBAR_OFFHAND_RIGHT_TEXTURE = Identifier.withDefaultNamespace("hud/hotbar_offhand_right");
	private static final Identifier HOTBAR_ATTACK_INDICATOR_BACKGROUND_TEXTURE = Identifier.withDefaultNamespace("hud/hotbar_attack_indicator_background");
	private static final Identifier HOTBAR_ATTACK_INDICATOR_PROGRESS_TEXTURE = Identifier.withDefaultNamespace("hud/hotbar_attack_indicator_progress");

	private static final Identifier HOTBAR_SELECTION_FIXED_TEXTURE = RPGInventory.identifier("hud/hotbar_selection_fixed");
	private static final Identifier UNSHEATHED_RIGHT_HAND_SLOT_SELECTOR_TEXTURE = RPGInventory.identifier("hud/unsheathed_right_hand_slot_selector");
	private static final Identifier HOTBAR_HAND_SLOTS_TEXTURE = RPGInventory.identifier("hud/hotbar_hand_slots");
	private static final Identifier HOTBAR_ALTERNATE_HAND_SLOTS_TEXTURE = RPGInventory.identifier("hud/hotbar_alternate_hand_slots");

	public static void rpginventory$renderOverhauledHotbar(Gui inGameHud, GuiGraphicsExtractor graphics, DeltaTracker tickCounter) {

		Player playerEntity = ((DuckInGameHudMixin) inGameHud).rpginventory$cameraPlayerAccessor();
		Minecraft minecraftClient = ((DuckInGameHudMixin) inGameHud).rpginventory$clientAccessor();
		if (playerEntity != null) {
			ClientConfig clientConfig = RPGInventoryClient.CLIENT_CONFIG;
			ItemStack itemStack = playerEntity.getOffhandItem();
			HumanoidArm arm = playerEntity.getMainArm().getOpposite();
			int i = graphics.guiWidth() / 2;

			int hotbar_start_x = RPGInventoryClient.drawAlternativeHotbar(graphics, playerEntity, HOTBAR_TEXTURE);

			boolean isHandSlotOverhaulActive = RPGInventory.isHandSlotOverhaulActive();
			if (((DuckPlayerEntityMixin) playerEntity).rpginventory$isHandStackSheathed() || clientConfig.hotBarOverhaul.always_show_selected_hotbar_slot.get() || !isHandSlotOverhaulActive) {
				graphics.blitSprite(
						RenderPipelines.GUI_TEXTURED, HOTBAR_SELECTION_FIXED_TEXTURE, hotbar_start_x - 1 + playerEntity.getInventory().getSelectedSlot() * 20, graphics.guiHeight() - 22 - 1, 24, 24
				);
			}

			ItemStack itemStackHand = ((DuckPlayerInventoryMixin) playerEntity.getInventory()).rpginventory$getHand();
			ItemStack itemStackOffHand = playerEntity.getOffhandItem();
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
				if (clientConfig.hotBarOverhaul.show_empty_hand_slots.get() || !(itemStackHand.isEmpty() || itemStackHand.is(Tags.EMPTY_HAND_WEAPONS)) || !(itemStackOffHand.isEmpty() || itemStackOffHand.is(Tags.EMPTY_HAND_WEAPONS))) {
					x = graphics.guiWidth() / 2 + clientConfig.hotBarOverhaul.hand_slots_offset_x.get();
					y = graphics.guiHeight() + clientConfig.hotBarOverhaul.hand_slots_offset_y.get();

					graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_HAND_SLOTS_TEXTURE, x, y, 49, 24);

					boolean offhand_slot_is_right = clientConfig.hotBarOverhaul.offhand_item_is_right.get();

					// sheathed hand indicator
					if ((!isHandSheathed && offhand_slot_is_right) || (!isOffhandSheathed && !offhand_slot_is_right)) {
						graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SELECTION_FIXED_TEXTURE, x - 1, y, 24, 24);
					}
					if ((!isOffhandSheathed && offhand_slot_is_right) || (!isHandSheathed && !offhand_slot_is_right)) {
						graphics.blitSprite(RenderPipelines.GUI_TEXTURED, UNSHEATHED_RIGHT_HAND_SLOT_SELECTOR_TEXTURE, x + 19, y, 24, 24);
					}
				}

				if (clientConfig.hotBarOverhaul.show_empty_alternative_hand_slots.get() || !(itemStackAlternativeHand.isEmpty() || itemStackAlternativeHand.is(Tags.EMPTY_HAND_WEAPONS)) || !(itemStackAlternativeOffHand.isEmpty() || itemStackAlternativeOffHand.is(Tags.EMPTY_HAND_WEAPONS))) {
					x = graphics.guiWidth() / 2 + clientConfig.hotBarOverhaul.alternative_hand_slots_offset_x.get();
					y = graphics.guiHeight() + clientConfig.hotBarOverhaul.alternative_hand_slots_offset_y.get();

					graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_ALTERNATE_HAND_SLOTS_TEXTURE, x, y, 49, 24);
				}
			} else {
				if (!itemStack.isEmpty()) {
					if (arm == HumanoidArm.LEFT) {
						graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_OFFHAND_LEFT_TEXTURE, i - 91 - 29, graphics.guiHeight() - 23, 29, 24);
					} else {
						graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_OFFHAND_RIGHT_TEXTURE, i - 91 + 182, graphics.guiHeight() - 23, 29, 24);
					}
				}
			}

			int l = 1;

			int activeHotbarSize = RPGInventory.getActiveHotbarSize(playerEntity);
			for (int m = 0; m < activeHotbarSize; m++) {
				int n = hotbar_start_x + 1 + m * 20 + 2;
				int o = graphics.guiHeight() - 16 - 3;
				((DuckInGameHudMixin) inGameHud).rpginventory$extractSlot_Invoker(graphics, n, o, tickCounter, playerEntity, playerEntity.getInventory().getItem(m), l++);
			}

			if (isHandSlotOverhaulActive) {
				x = graphics.guiWidth() / 2 + clientConfig.hotBarOverhaul.hand_slots_offset_x.get();
				y = graphics.guiHeight() + clientConfig.hotBarOverhaul.hand_slots_offset_y.get();

				boolean offhand_slot_is_right = clientConfig.hotBarOverhaul.offhand_item_is_right.get();
				((DuckInGameHudMixin) inGameHud).rpginventory$extractSlot_Invoker(graphics, x + 23, y + 4, tickCounter, playerEntity, offhand_slot_is_right ? itemStackOffHand : itemStackHand, l++);
				((DuckInGameHudMixin) inGameHud).rpginventory$extractSlot_Invoker(graphics, x + 3, y + 4, tickCounter, playerEntity, offhand_slot_is_right ? itemStackHand : itemStackOffHand, l++);

				x = graphics.guiWidth() / 2 + clientConfig.hotBarOverhaul.alternative_hand_slots_offset_x.get();
				y = graphics.guiHeight() + clientConfig.hotBarOverhaul.alternative_hand_slots_offset_y.get();

				boolean alternative_offhand_slot_is_right = clientConfig.hotBarOverhaul.alternative_offhand_item_is_right.get();
				((DuckInGameHudMixin) inGameHud).rpginventory$extractSlot_Invoker(graphics, x + 10, y + 4, tickCounter, playerEntity, alternative_offhand_slot_is_right ? itemStackAlternativeHand : itemStackAlternativeOffHand, l++);
				((DuckInGameHudMixin) inGameHud).rpginventory$extractSlot_Invoker(graphics, x + 30, y + 4, tickCounter, playerEntity, alternative_offhand_slot_is_right ? itemStackAlternativeOffHand : itemStackAlternativeHand, l);
			} else {
				if (!itemStack.isEmpty()) {
					int m = graphics.guiHeight() - 16 - 3;
					if (arm == HumanoidArm.LEFT) {
						((DuckInGameHudMixin) inGameHud).rpginventory$extractSlot_Invoker(graphics, i - 91 - 26, m, tickCounter, playerEntity, itemStack, l++);
					} else {
						((DuckInGameHudMixin) inGameHud).rpginventory$extractSlot_Invoker(graphics, i - 91 + 182 + 10, m, tickCounter, playerEntity, itemStack, l++);
					}
				}
			}

			if (minecraftClient.options.attackIndicator().get() == AttackIndicatorStatus.HOTBAR) {
				LocalPlayer clientPlayerEntity = minecraftClient.player;
				if (clientPlayerEntity != null) {
					float f = clientPlayerEntity.getAttackStrengthScale(0.0F);
					if (f < 1.0F) {
						int n = graphics.guiHeight() - 20;
						int o = i + 91 + 6;
						if (arm == HumanoidArm.RIGHT) {
							o = i - 91 - 22;
						}

						int p = (int) (f * 19.0F);
						graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_ATTACK_INDICATOR_BACKGROUND_TEXTURE, o, n, 18, 18);
						graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_ATTACK_INDICATOR_PROGRESS_TEXTURE, 18, 18, 0, 18 - p, o, n + 18 - p, 18, p);
					}
				}
			}
		}
	}

}
