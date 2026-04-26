package com.github.theredbrain.rpginventory.entity.player;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.DuckLivingEntityMixin;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.entity.LivingEntityHelper;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.google.common.collect.HashMultimap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class PlayerEntityHelper {

	public static final int EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET = 44;

	public static boolean rpginventory$onPVPDeath(DamageSource source, Player playerEntity, Holder<MobEffect> pvpStatusEffect) {

		MobEffectInstance pvpEffectInstance = playerEntity.getEffect(pvpStatusEffect);
		if (pvpEffectInstance != null) {
			PlayerTeam team = playerEntity.getTeam();
			List<Holder<MobEffect>> effectsToBeRemoved = new ArrayList<>();

			for (MobEffectInstance instance : playerEntity.getActiveEffects()) {
				if (!(instance.getEffect().is(Tags.KEPT_ON_PVP_DEATH) || instance.getEffect() == pvpStatusEffect)) {
					effectsToBeRemoved.add(instance.getEffect());
				}
			}
			for (Holder<MobEffect> entry : effectsToBeRemoved) {
				playerEntity.removeEffect(entry);
			}
			int newAmplifier = pvpEffectInstance.getAmplifier() - 1;
			boolean playerRemovedFromBattle = source.is(Tags.REMOVES_PLAYER_FROM_PVP);
			boolean endOfBattle = newAmplifier < 0;

			resetPlayerStatus(playerEntity, endOfBattle || playerRemovedFromBattle);
			if (endOfBattle || playerRemovedFromBattle) {
				playerEntity.removeEffect(pvpStatusEffect);
			} else {
				playerEntity.forceAddEffect(new MobEffectInstance(pvpStatusEffect, pvpEffectInstance.getDuration(), newAmplifier, pvpEffectInstance.isAmbient(), pvpEffectInstance.isVisible(), pvpEffectInstance.showIcon()), null);
			}
			teleportToPVPRespawnPosition(team, playerEntity, endOfBattle || playerRemovedFromBattle);
			if (!source.is(Tags.PREVENTS_PVP_DEATH_MESSAGE)) {
				sendPVPDeathMessage(playerEntity, endOfBattle, playerRemovedFromBattle);
			}
			// TODO pvp deaths/kills statistic, score boards for active match
//			serverPlayerEntity.incrementStat(Stats.DEATHS.USED.getOrCreateStat(Items.TOTEM_OF_UNDYING));
			return true;
		}
		return false;
	}

	public static void sendPVPDeathMessage(Player playerEntity, boolean endOfBattle, boolean playerRemovedFromBattle) {
		if (!playerRemovedFromBattle && playerEntity instanceof ServerPlayer serverPlayerEntity) {
			ServerLevel serverLevel = serverPlayerEntity.level();
			if (serverLevel.getGameRules().get(GameRules.SHOW_DEATH_MESSAGES)) {
				Component pvpSuffix = endOfBattle ? Component.translatable("death.pvp.suffix") : Component.empty();
				Component text = Component.translatable("death.pvp.prefix", serverPlayerEntity.getCombatTracker().getDeathMessage(), pvpSuffix);
				Team abstractTeam = serverPlayerEntity.getTeam();
				if (abstractTeam == null || abstractTeam.getDeathMessageVisibility() == Team.Visibility.ALWAYS) {
					serverLevel.getServer().getPlayerList().broadcastSystemMessage(text, false);
				} else if (abstractTeam.getDeathMessageVisibility() == Team.Visibility.HIDE_FOR_OTHER_TEAMS) {
					serverLevel.getServer().getPlayerList().broadcastSystemToTeam(serverPlayerEntity, text);
				} else if (abstractTeam.getDeathMessageVisibility() == Team.Visibility.HIDE_FOR_OWN_TEAM) {
					serverLevel.getServer().getPlayerList().broadcastSystemToAllExceptTeam(serverPlayerEntity, text);
				}
			}
		}

	}

	public static void resetPlayerStatus(Player playerEntity, boolean endOfBattle) {
		RPGInventory.resetPlayerStatus(playerEntity, endOfBattle);
	}

	public static void teleportToPVPRespawnPosition(PlayerTeam team, Player playerEntity, boolean endOfBattle) {

		if (playerEntity instanceof ServerPlayer serverPlayerEntity) {
			MinecraftServer server = serverPlayerEntity.level().getServer();
			ServerLevel targetWorld = null;
			BlockPos targetPos = null;
			double targetYaw = 0.0;
			double targetPitch = 0.0;
			LevelData.RespawnData pvp_respawn_position = RPGInventory.getPVPRespawnPosition(team, serverPlayerEntity, endOfBattle);

			if (pvp_respawn_position != null) {
				targetWorld = server.getLevel(pvp_respawn_position.globalPos().dimension());
				targetPos = pvp_respawn_position.globalPos().pos();
				targetYaw = pvp_respawn_position.yaw();
				targetPitch = pvp_respawn_position.pitch();
			}

			if (targetWorld == null) {
				targetWorld = server.getLevel(server.getRespawnData().globalPos().dimension());
				targetPos = server.getRespawnData().globalPos().pos();
				targetYaw = server.getRespawnData().yaw();
				targetPitch = server.getRespawnData().pitch();
			}

			if (targetWorld != null) {
				serverPlayerEntity.fallDistance = 0;
				serverPlayerEntity.teleportTo(targetWorld, (targetPos.getX() + 0.5), (targetPos.getY() + 0.01), (targetPos.getZ() + 0.5), Set.of(), (float) targetYaw, (float) targetPitch, true);
				serverPlayerEntity.closeContainer();
			}
		}
	}

	public static void rpginventory$updateEquipmentStatusEffects(Player playerEntity) {

		Predicate<ItemStack> keep_inventory_on_death_item_equipped_predicate = stack -> stack.is(Tags.SACRIFICED_TO_KEEP_INVENTORY_ON_DEATH);

		boolean keep_inventory_on_death_item_equipped = RPGInventory.isTrinketEquipped(playerEntity, keep_inventory_on_death_item_equipped_predicate);

		keep_inventory_on_death_item_equipped = keep_inventory_on_death_item_equipped || LivingEntityHelper.rpginventory$hasEquipped(playerEntity, keep_inventory_on_death_item_equipped_predicate);

		if (keep_inventory_on_death_item_equipped) {
			if (!playerEntity.hasEffect(RPGInventory.KEEP_INVENTORY)) {
				playerEntity.addEffect(new MobEffectInstance(RPGInventory.KEEP_INVENTORY, -1, 0, false, false, false));
			}
		} else {
			playerEntity.removeEffect(RPGInventory.KEEP_INVENTORY);
		}

		ItemStack itemStackMainHand = playerEntity.getItemBySlot(EquipmentSlot.MAINHAND);
		ItemStack itemStackOffHand = playerEntity.getItemBySlot(EquipmentSlot.OFFHAND);
		Optional<Holder.Reference<MobEffect>> adventure_building_status_effect = BuiltInRegistries.MOB_EFFECT.get(RPGInventory.SERVER_CONFIG.statusEffects.building_mode_status_effect_identifier.get());
		boolean hasAdventureBuildingEffect = adventure_building_status_effect.isPresent() && playerEntity.hasEffect(adventure_building_status_effect.get());

		if (!itemStackMainHand.is(Tags.ATTACK_ITEMS) && !playerEntity.isCreative() && !hasAdventureBuildingEffect && !RPGInventory.SERVER_CONFIG.allow_attacking_with_non_attack_items.get()) {
			if (!playerEntity.hasEffect(RPGInventory.NO_ATTACK_ITEM)) {
				playerEntity.addEffect(new MobEffectInstance(RPGInventory.NO_ATTACK_ITEM, -1, 0, false, false, false));
			}
		} else {
			playerEntity.removeEffect(RPGInventory.NO_ATTACK_ITEM);
		}

		if (itemStackMainHand.is(Tags.TWO_HANDED_ITEMS) && !itemStackOffHand.isEmpty() && !playerEntity.isCreative() && !hasAdventureBuildingEffect && RPGInventory.SERVER_CONFIG.enable_two_handed_items_restriction.get()) {
			if (!playerEntity.hasEffect(RPGInventory.NEEDS_TWO_HANDING)) {
				playerEntity.addEffect(new MobEffectInstance(RPGInventory.NEEDS_TWO_HANDING, -1, 0, false, false, false));
			}
		} else {
			playerEntity.removeEffect(RPGInventory.NEEDS_TWO_HANDING);
		}
	}

	public static void rpginventory$ejectItemsFromInactiveHandSlots(Player playerEntity) {
		boolean isHandSlotOverhaulActive = RPGInventory.isHandSlotOverhaulActive();

		if (((DuckPlayerEntityMixin) playerEntity).rpginventory$isHandSlotOverhaulActive() != isHandSlotOverhaulActive) {
			if (!isHandSlotOverhaulActive) {
				Inventory playerInventory = playerEntity.getInventory();
				boolean bl = false;

				ItemStack itemStack = playerEntity.getItemBySlot(EquipmentSlot.MAINHAND);
				if (!itemStack.isEmpty()) {
					if (!playerEntity.level().isClientSide()) {
						playerInventory.placeItemBackInInventory(itemStack.copy());
					}
					playerEntity.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
					bl = true;
				}

				itemStack = playerEntity.getItemBySlot(ExtendedEquipmentSlot.SHEATHED_HAND);
				if (!itemStack.isEmpty()) {
					if (!playerEntity.level().isClientSide()) {
						playerInventory.placeItemBackInInventory(itemStack.copy());
					}
					playerEntity.setItemSlot(ExtendedEquipmentSlot.SHEATHED_HAND, ItemStack.EMPTY);
					bl = true;
				}

				itemStack = playerEntity.getItemBySlot(ExtendedEquipmentSlot.SHEATHED_OFF_HAND);
				if (!itemStack.isEmpty()) {
					if (!playerEntity.level().isClientSide()) {
						playerInventory.placeItemBackInInventory(itemStack.copy());
					}
					playerEntity.setItemSlot(ExtendedEquipmentSlot.SHEATHED_OFF_HAND, ItemStack.EMPTY);
					bl = true;
				}

				itemStack = playerEntity.getItemBySlot(ExtendedEquipmentSlot.ALTERNATIVE_HAND);
				if (!itemStack.isEmpty()) {
					if (!playerEntity.level().isClientSide()) {
						playerInventory.placeItemBackInInventory(itemStack.copy());
					}
					playerEntity.setItemSlot(ExtendedEquipmentSlot.ALTERNATIVE_HAND, ItemStack.EMPTY);
					bl = true;
				}

				itemStack = playerEntity.getItemBySlot(ExtendedEquipmentSlot.ALTERNATIVE_OFF_HAND);
				if (!itemStack.isEmpty()) {
					if (!playerEntity.level().isClientSide()) {
						playerInventory.placeItemBackInInventory(itemStack.copy());
					}
					playerEntity.setItemSlot(ExtendedEquipmentSlot.ALTERNATIVE_OFF_HAND, ItemStack.EMPTY);
					bl = true;
				}

				if (bl && playerEntity instanceof ServerPlayer serverPlayerEntity) {
					serverPlayerEntity.sendSystemMessage(Component.translatable("hud.message.items_removed_from_inactive_hand_slots"), false);
				}
				((DuckLivingEntityMixin) playerEntity).rpginventory$setIsHandStackSheathed(true);
				((DuckLivingEntityMixin) playerEntity).rpginventory$setIsOffhandStackSheathed(true);
				((DuckPlayerEntityMixin) playerEntity).rpginventory$setAreAlternativeHandSlotsActive(false);
			} else {
				((DuckLivingEntityMixin) playerEntity).rpginventory$setIsHandStackSheathed(false);
				((DuckLivingEntityMixin) playerEntity).rpginventory$setIsOffhandStackSheathed(false);
			}
			((DuckPlayerEntityMixin) playerEntity).rpginventory$setIsHandSlotOverhaulActive(isHandSlotOverhaulActive);
		}
		if (isHandSlotOverhaulActive) {
			boolean areAlternativeHandSlotsActive = RPGInventory.SERVER_CONFIG.handSlotOverhaul.enable_alternative_hand_slots.get();

			if (((DuckPlayerEntityMixin) playerEntity).rpginventory$areAlternativeHandSlotsActive() != areAlternativeHandSlotsActive) {
				if (!areAlternativeHandSlotsActive) {
					Inventory playerInventory = playerEntity.getInventory();
					boolean bl = false;

					ItemStack itemStack = playerEntity.getItemBySlot(ExtendedEquipmentSlot.ALTERNATIVE_HAND);
					if (!itemStack.isEmpty()) {
						playerInventory.placeItemBackInInventory(itemStack.copy());
						playerEntity.setItemSlot(ExtendedEquipmentSlot.ALTERNATIVE_HAND, ItemStack.EMPTY);
						bl = true;
					}

					itemStack = playerEntity.getItemBySlot(ExtendedEquipmentSlot.ALTERNATIVE_OFF_HAND);
					if (!itemStack.isEmpty()) {
						playerInventory.placeItemBackInInventory(itemStack.copy());
						playerEntity.setItemSlot(ExtendedEquipmentSlot.ALTERNATIVE_OFF_HAND, ItemStack.EMPTY);
						bl = true;
					}

					if (bl && playerEntity instanceof ServerPlayer serverPlayerEntity) {
						serverPlayerEntity.sendSystemMessage(Component.translatable("hud.message.items_removed_from_inactive_hand_slots"), false);
					}
				}
				((DuckPlayerEntityMixin) playerEntity).rpginventory$setAreAlternativeHandSlotsActive(areAlternativeHandSlotsActive);
			}
		}
	}

	public static void rpginventory$ejectItemsFromInactiveSpellSlots(Player playerEntity) {
		int activeSpellSlotAmount = (int) ((DuckPlayerEntityMixin) playerEntity).rpginventory$getActiveSpellSlotAmount();

		if (((DuckPlayerEntityMixin) playerEntity).rpginventory$oldActiveSpellSlotAmount() != activeSpellSlotAmount) {
			Inventory playerInventory = playerEntity.getInventory();

			EquipmentSlot equipmentSlot = ExtendedEquipmentSlot.SPELL_1;
			int spellSlotIndex = equipmentSlot.getIndex(EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET);
			ItemStack itemStack;
			boolean removedStack = false;

			for (int i = activeSpellSlotAmount; i < 8; i++) {

				equipmentSlot = Inventory.EQUIPMENT_SLOT_MAPPING.get(spellSlotIndex + i);
				itemStack = playerEntity.getItemBySlot(equipmentSlot).copy();
				if (itemStack.isEmpty()) {
					continue;
				}
				if (!playerEntity.level().isClientSide()) {
					playerInventory.placeItemBackInInventory(itemStack.copy());
				}
				playerEntity.setItemSlot(equipmentSlot, ItemStack.EMPTY);
				removedStack = true;
				break;
			}
			if (removedStack) {
				if (playerEntity instanceof ServerPlayer serverPlayerEntity) {
					serverPlayerEntity.sendSystemMessage(Component.translatable("hud.message.items_removed_from_inactive_spell_slots"), false);
				}
			}

			((DuckPlayerEntityMixin) playerEntity).rpginventory$setOldActiveSpellSlotAmount(activeSpellSlotAmount);
		}
	}

	public static void rpginventory$ejectExclusiveEquipment(Player playerEntity) {
		if (!((DuckPlayerEntityMixin) playerEntity).rpginventory$shouldEjectExclusiveEquipment()) {
			return;
		}
		Inventory playerInventory = playerEntity.getInventory();
		List<String> existingExclusiveEquipmentGroups = new ArrayList<>();

		// check class item first to allow for class specific items
		EquipmentSlot equipmentSlot = ExtendedEquipmentSlot.CLASS_ITEM;
		ItemStack itemStack = playerEntity.getItemBySlot(equipmentSlot).copy();
		if (!itemStack.isEmpty()) {
			existingExclusiveEquipmentGroups.addAll(RPGInventory.getExclusiveEquipmentGroups(itemStack));
		}

		for (int i = playerInventory.getNonEquipmentItems().size(); i < playerInventory.getContainerSize(); i++) {
			equipmentSlot = Inventory.EQUIPMENT_SLOT_MAPPING.get(i);
			if (ExtendedEquipmentSlot.ignoredByExclusiveEquipmentCheck(equipmentSlot)) {
				continue;
			}
			itemStack = playerEntity.getItemBySlot(equipmentSlot).copy();
			if (itemStack.isEmpty()) {
				continue;
			}
			List<String> currentExclusiveEquipmentGroups = RPGInventory.getExclusiveEquipmentGroups(itemStack);
			if (currentExclusiveEquipmentGroups.isEmpty()) {
				continue;
			}
			boolean removedStack = false;
			for (String string : currentExclusiveEquipmentGroups) {
				if (existingExclusiveEquipmentGroups.contains(string)) {
					if (!playerEntity.level().isClientSide()) {
						playerInventory.placeItemBackInInventory(itemStack.copy());
					}
					playerEntity.setItemSlot(equipmentSlot, ItemStack.EMPTY);
					removedStack = true;
					break;
				}
			}
			if (removedStack) {
				continue;
			}
			existingExclusiveEquipmentGroups.addAll(currentExclusiveEquipmentGroups);
			((DuckPlayerEntityMixin) playerEntity).rpginventory$setShouldEjectExclusiveEquipment(false);
		}
	}

	public static void rpginventory$ejectNonHotbarItemsFromHotbar(Player playerEntity) { // FIXME is only called once?
		Optional<Holder.Reference<MobEffect>> adventure_building_status_effect = BuiltInRegistries.MOB_EFFECT.get(RPGInventory.SERVER_CONFIG.statusEffects.building_mode_status_effect_identifier.get());
		boolean hasAdventureBuildingEffect = adventure_building_status_effect.isPresent() && playerEntity.hasEffect(adventure_building_status_effect.get());

		if (!playerEntity.isCreative() && !hasAdventureBuildingEffect && !((RPGInventory.SERVER_CONFIG.allow_equipment_changes.get() && !playerEntity.hasEffect(RPGInventory.WILDERNESS)) || playerEntity.hasEffect(RPGInventory.CIVILISATION))) {
			if (!((DuckPlayerEntityMixin) playerEntity).rpginventory$isAdventureHotbarCleanedUp()) {
				for (int i = 0; i < 9; i++) {
					Inventory playerInventory = playerEntity.getInventory();
					Slot slot = playerEntity.inventoryMenu.slots.get(i + 36);

					if (!slot.container.getItem(slot.getContainerSlot()).is(Tags.ADVENTURE_HOTBAR_ITEMS)) {
						playerInventory.placeItemBackInInventory(slot.container.removeItemNoUpdate(slot.getContainerSlot()));
					}
				}
				((DuckPlayerEntityMixin) playerEntity).rpginventory$setIsAdventureHotbarCleanedUp(true);
			}
		} else {
			if (((DuckPlayerEntityMixin) playerEntity).rpginventory$isAdventureHotbarCleanedUp()) {
				((DuckPlayerEntityMixin) playerEntity).rpginventory$setIsAdventureHotbarCleanedUp(false);
			}
		}
	}

	public static void rpginventory$breakKeepInventoryItems(Player playerEntity) {

		RPGInventory.breakKeepInventoryTrinkets(playerEntity);

		Inventory playerInventory = playerEntity.getInventory();
		((DuckPlayerInventoryMixin) playerInventory).rpginventory$breakKeepInventoryItems();

		for (int i = playerInventory.getNonEquipmentItems().size(); i < playerInventory.getContainerSize(); i++) {
			ItemStack itemStack = playerInventory.getItem(i);
			if (itemStack.is(Tags.SACRIFICED_TO_KEEP_INVENTORY_ON_DEATH)) {
				playerInventory.removeItemNoUpdate(i);
			}
		}
	}

	public static void updateNaturalAttributeModifiers(Player player) {
		HashMultimap<Holder<Attribute>, AttributeModifier> toBeAdded = HashMultimap.create();
		HashMultimap<Holder<Attribute>, AttributeModifier> toBeRemoved = HashMultimap.create();
		addAttributeModifier(toBeAdded, toBeRemoved, RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT, RPGInventory.identifier("natural_spell_slot_amount_modifier"), RPGInventory.SERVER_CONFIG.inventorySlots.default_spell_slot_amount.get());
		if (!toBeRemoved.isEmpty()) {
			player.getAttributes().removeAttributeModifiers(toBeRemoved);
		}
		if (!toBeAdded.isEmpty()) {
			player.getAttributes().addTransientAttributeModifiers(toBeAdded);
		}
	}

	private static void addAttributeModifier(
			HashMultimap<Holder<Attribute>, AttributeModifier> toBeAdded,
			HashMultimap<Holder<Attribute>, AttributeModifier> toBeRemoved,
			Holder<Attribute> attributeHolder,
			Identifier identifier,
			double amount
	) {
		AttributeModifier attributeModifier = new AttributeModifier(identifier, amount, AttributeModifier.Operation.ADD_VALUE);
		if (amount == 0) {
			toBeRemoved.put(attributeHolder, attributeModifier);
		} else {
			toBeAdded.put(attributeHolder, attributeModifier);
		}
	}

}
