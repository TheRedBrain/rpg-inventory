package com.github.theredbrain.rpginventory.entity.player;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.entity.LivingEntityHelper;
import com.github.theredbrain.rpginventory.registry.Tags;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class PlayerEntityHelper {

	private static final int EXCLUSIVE_EQUIPMENT_SLOT_AMOUNT = 20;

	public static boolean rpginventory$onPVPDeath(DamageSource source, PlayerEntity playerEntity, RegistryEntry<StatusEffect> pvpStatusEffect) {

		StatusEffectInstance pvpEffectInstance = playerEntity.getStatusEffect(pvpStatusEffect);
		if (pvpEffectInstance != null) {
			Team team = playerEntity.getScoreboardTeam();
			List<RegistryEntry<StatusEffect>> effectsToBeRemoved = new ArrayList<>();

			for (StatusEffectInstance instance : playerEntity.getStatusEffects()) {
				if (!(instance.getEffectType().isIn(Tags.KEPT_ON_PVP_DEATH) || instance.getEffectType() == pvpStatusEffect)) {
					effectsToBeRemoved.add(instance.getEffectType());
				}
			}
			for (RegistryEntry<StatusEffect> entry : effectsToBeRemoved) {
				playerEntity.removeStatusEffect(entry);
			}
			int newAmplifier = pvpEffectInstance.getAmplifier() - 1;
			boolean playerRemovedFromBattle = source.isIn(Tags.REMOVES_PLAYER_FROM_PVP);
			boolean endOfBattle = newAmplifier < 0;

			resetPlayerStatus(playerEntity, endOfBattle || playerRemovedFromBattle);
			if (endOfBattle || playerRemovedFromBattle) {
				playerEntity.removeStatusEffect(pvpStatusEffect);
			} else {
				playerEntity.setStatusEffect(new StatusEffectInstance(pvpStatusEffect, pvpEffectInstance.getDuration(), newAmplifier, pvpEffectInstance.isAmbient(), pvpEffectInstance.shouldShowParticles(), pvpEffectInstance.shouldShowIcon()), null);
			}
			teleportToPVPRespawnPosition(team, playerEntity, endOfBattle || playerRemovedFromBattle);
			if (!source.isIn(Tags.PREVENTS_PVP_DEATH_MESSAGE)) {
				sendPVPDeathMessage(playerEntity, endOfBattle, playerRemovedFromBattle);
			}
			// TODO pvp deaths/kills statistic, score boards for active match
//			serverPlayerEntity.incrementStat(Stats.DEATHS.USED.getOrCreateStat(Items.TOTEM_OF_UNDYING));
			return true;
		}
		return false;
	}

	public static void sendPVPDeathMessage(PlayerEntity playerEntity, boolean endOfBattle, boolean playerRemovedFromBattle) {
		boolean bl = playerEntity.getWorld().getGameRules().getBoolean(GameRules.SHOW_DEATH_MESSAGES);
		if (bl && !playerRemovedFromBattle && playerEntity instanceof ServerPlayerEntity serverPlayerEntity) {
			Text pvpSuffix = endOfBattle ? Text.translatable("death.pvp.suffix") : Text.empty();
			Text text = Text.translatable("death.pvp.prefix", serverPlayerEntity.getDamageTracker().getDeathMessage(), pvpSuffix);
			AbstractTeam abstractTeam = serverPlayerEntity.getScoreboardTeam();
			if (abstractTeam == null || abstractTeam.getDeathMessageVisibilityRule() == AbstractTeam.VisibilityRule.ALWAYS) {
				serverPlayerEntity.server.getPlayerManager().broadcast(text, false);
			} else if (abstractTeam.getDeathMessageVisibilityRule() == AbstractTeam.VisibilityRule.HIDE_FOR_OTHER_TEAMS) {
				serverPlayerEntity.server.getPlayerManager().sendToTeam(serverPlayerEntity, text);
			} else if (abstractTeam.getDeathMessageVisibilityRule() == AbstractTeam.VisibilityRule.HIDE_FOR_OWN_TEAM) {
				serverPlayerEntity.server.getPlayerManager().sendToOtherTeams(serverPlayerEntity, text);
			}
		}

	}

	public static void resetPlayerStatus(PlayerEntity playerEntity, boolean endOfBattle) {
		RPGInventory.resetPlayerStatus(playerEntity, endOfBattle);
	}

	public static void teleportToPVPRespawnPosition(Team team, PlayerEntity playerEntity, boolean endOfBattle) {

		if (playerEntity instanceof ServerPlayerEntity serverPlayerEntity) {
			MinecraftServer server = serverPlayerEntity.getServer();
			if (server != null) {
				ServerWorld targetWorld = null;
				BlockPos targetPos = null;
				double targetYaw = 0.0;
				double targetPitch = 0.0;
				MutablePair<RegistryKey<World>, MutablePair<BlockPos, MutablePair<Double, Double>>> pvp_respawn_position = RPGInventory.getPVPRespawnPosition(team, serverPlayerEntity, endOfBattle);

				if (pvp_respawn_position != null) {
					targetWorld = server.getWorld(pvp_respawn_position.getLeft());
					targetPos = pvp_respawn_position.getRight().getLeft();
					targetYaw = pvp_respawn_position.getRight().getRight().getLeft();
					targetPitch = pvp_respawn_position.getRight().getRight().getRight();
				}

				if (targetWorld == null || targetPos == null) {
					targetWorld = server.getOverworld();
					targetPos = server.getOverworld().getSpawnPos();
					targetYaw = server.getOverworld().getSpawnAngle();
					targetPitch = 0.0;
				}

				if (targetWorld != null && targetPos != null) {
					serverPlayerEntity.fallDistance = 0;
					serverPlayerEntity.teleport(targetWorld, (targetPos.getX() + 0.5), (targetPos.getY() + 0.01), (targetPos.getZ() + 0.5), (float) targetYaw, (float) targetPitch);
					serverPlayerEntity.closeHandledScreen();
				}
			}
		}
	}

	public static void rpginventory$updateEquipmentStatusEffects(PlayerEntity playerEntity) {

		Predicate<ItemStack> keep_inventory_on_death_item_equipped_predicate = stack -> stack.isIn(Tags.SACRIFICED_TO_KEEP_INVENTORY_ON_DEATH);

		boolean keep_inventory_on_death_item_equipped = RPGInventory.isTrinketEquipped(playerEntity, keep_inventory_on_death_item_equipped_predicate);

		keep_inventory_on_death_item_equipped = keep_inventory_on_death_item_equipped || LivingEntityHelper.rpginventory$hasEquipped(playerEntity, keep_inventory_on_death_item_equipped_predicate);

		if (keep_inventory_on_death_item_equipped) {
			if (!playerEntity.hasStatusEffect(RPGInventory.KEEP_INVENTORY)) {
				playerEntity.addStatusEffect(new StatusEffectInstance(RPGInventory.KEEP_INVENTORY, -1, 0, false, false, false));
			}
		} else {
			playerEntity.removeStatusEffect(RPGInventory.KEEP_INVENTORY);
		}

		ItemStack itemStackMainHand = playerEntity.getEquippedStack(EquipmentSlot.MAINHAND);
		ItemStack itemStackOffHand = playerEntity.getEquippedStack(EquipmentSlot.OFFHAND);
		Optional<RegistryEntry.Reference<StatusEffect>> adventure_building_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.building_mode_status_effect_identifier.get());
		boolean hasAdventureBuildingEffect = adventure_building_status_effect.isPresent() && playerEntity.hasStatusEffect(adventure_building_status_effect.get());

		if (!itemStackMainHand.isIn(Tags.ATTACK_ITEMS) && !playerEntity.isCreative() && !hasAdventureBuildingEffect && !RPGInventory.SERVER_CONFIG.allow_attacking_with_non_attack_items.get()) {
			if (!playerEntity.hasStatusEffect(RPGInventory.NO_ATTACK_ITEM)) {
				playerEntity.addStatusEffect(new StatusEffectInstance(RPGInventory.NO_ATTACK_ITEM, -1, 0, false, false, false));
			}
		} else {
			playerEntity.removeStatusEffect(RPGInventory.NO_ATTACK_ITEM);
		}

		if (itemStackMainHand.isIn(Tags.TWO_HANDED_ITEMS) && !itemStackOffHand.isEmpty() && !playerEntity.isCreative() && !hasAdventureBuildingEffect && RPGInventory.SERVER_CONFIG.enable_two_handed_items_restriction.get()) {
			if (!playerEntity.hasStatusEffect(RPGInventory.NEEDS_TWO_HANDING)) {
				playerEntity.addStatusEffect(new StatusEffectInstance(RPGInventory.NEEDS_TWO_HANDING, -1, 0, false, false, false));
			}
		} else {
			playerEntity.removeStatusEffect(RPGInventory.NEEDS_TWO_HANDING);
		}
	}

	public static void rpginventory$ejectItemsFromInactiveSpellSlots(PlayerEntity playerEntity) {
		int activeSpellSlotAmount = (int) ((DuckPlayerEntityMixin) playerEntity).rpginventory$getActiveSpellSlotAmount();

		if (((DuckPlayerEntityMixin) playerEntity).rpginventory$oldActiveSpellSlotAmount() != activeSpellSlotAmount) {
			PlayerInventory playerInventory = playerEntity.getInventory();
			for (int j = activeSpellSlotAmount; j < 8; j++) {

				if (!((DuckPlayerInventoryMixin) playerInventory).rpginventory$getAdditionalEquipmentStack(6 + j).isEmpty()) {
					playerInventory.offerOrDrop(((DuckPlayerInventoryMixin) playerInventory).rpginventory$setAdditionalEquipmentStack(6 + j, ItemStack.EMPTY));
					if (playerEntity instanceof ServerPlayerEntity serverPlayerEntity) {
						serverPlayerEntity.sendMessage(Text.translatable("hud.message.spellsRemovedFromInactiveSpellSlots"), false);
					}
				}
			}

			((DuckPlayerEntityMixin) playerEntity).rpginventory$setOldActiveSpellSlotAmount(activeSpellSlotAmount);
		}
	}

	public static void rpginventory$ejectItemsFromInactiveHandSlots(PlayerEntity playerEntity) {
		boolean isHandSlotOverhaulActive = RPGInventory.isHandSlotOverhaulActive();

		if (((DuckPlayerEntityMixin) playerEntity).rpginventory$isHandSlotOverhaulActive() != isHandSlotOverhaulActive) {
			if (!isHandSlotOverhaulActive) {
				((DuckPlayerEntityMixin) playerEntity).rpginventory$setIsHandStackSheathed(true);
				((DuckPlayerEntityMixin) playerEntity).rpginventory$setIsOffhandStackSheathed(true);
				PlayerInventory playerInventory = playerEntity.getInventory();
				boolean bl = false;

				if (!((DuckPlayerInventoryMixin) playerInventory).rpginventory$getHand().isEmpty()) {
					playerInventory.offerOrDrop(((DuckPlayerInventoryMixin) playerInventory).rpginventory$setHand(ItemStack.EMPTY));
					bl = true;
				}

				if (!((DuckPlayerInventoryMixin) playerInventory).rpginventory$getSheathedHand().isEmpty()) {
					playerInventory.offerOrDrop(((DuckPlayerInventoryMixin) playerInventory).rpginventory$setSheathedHand(ItemStack.EMPTY));
					bl = true;
				}

				if (!((DuckPlayerInventoryMixin) playerInventory).rpginventory$getSheathedOffhand().isEmpty()) {
					playerInventory.offerOrDrop(((DuckPlayerInventoryMixin) playerInventory).rpginventory$setSheathedOffhand(ItemStack.EMPTY));
					bl = true;
				}

				if (!((DuckPlayerInventoryMixin) playerInventory).rpginventory$getAlternativeHand().isEmpty()) {
					playerInventory.offerOrDrop(((DuckPlayerInventoryMixin) playerInventory).rpginventory$setAlternativeHand(ItemStack.EMPTY));
					bl = true;
				}

				if (!((DuckPlayerInventoryMixin) playerInventory).rpginventory$getAlternativeOffhand().isEmpty()) {
					playerInventory.offerOrDrop(((DuckPlayerInventoryMixin) playerInventory).rpginventory$setAlternativeOffhand(ItemStack.EMPTY));
					bl = true;
				}

				if (bl && playerEntity instanceof ServerPlayerEntity serverPlayerEntity) {
					serverPlayerEntity.sendMessage(Text.translatable("hud.message.itemsRemovedFromInactiveHandSlots"), false);
				}
				((DuckPlayerEntityMixin) playerEntity).rpginventory$setAreAlternativeHandSlotsActive(false);
			}
			((DuckPlayerEntityMixin) playerEntity).rpginventory$setIsHandSlotOverhaulActive(isHandSlotOverhaulActive);
		}
		if (isHandSlotOverhaulActive) {
			boolean areAlternativeHandSlotsActive = RPGInventory.SERVER_CONFIG.handSlotOverhaul.enable_alternative_hand_slots.get();

			if (((DuckPlayerEntityMixin) playerEntity).rpginventory$areAlternativeHandSlotsActive() != areAlternativeHandSlotsActive) {
				if (!areAlternativeHandSlotsActive) {
					PlayerInventory playerInventory = playerEntity.getInventory();
					boolean bl = false;

					if (!((DuckPlayerInventoryMixin) playerInventory).rpginventory$getAlternativeHand().isEmpty()) {
						playerInventory.offerOrDrop(((DuckPlayerInventoryMixin) playerInventory).rpginventory$setAlternativeHand(ItemStack.EMPTY));
						bl = true;
					}

					if (!((DuckPlayerInventoryMixin) playerInventory).rpginventory$getAlternativeOffhand().isEmpty()) {
						playerInventory.offerOrDrop(((DuckPlayerInventoryMixin) playerInventory).rpginventory$setAlternativeOffhand(ItemStack.EMPTY));
						bl = true;
					}

					if (bl && playerEntity instanceof ServerPlayerEntity serverPlayerEntity) {
						serverPlayerEntity.sendMessage(Text.translatable("hud.message.itemsRemovedFromInactiveHandSlots"), false);
					}
				}
				((DuckPlayerEntityMixin) playerEntity).rpginventory$setAreAlternativeHandSlotsActive(areAlternativeHandSlotsActive);
			}
		}
	}

	public static void rpginventory$ejectExclusiveEquipment(PlayerEntity playerEntity) {
		if (((DuckPlayerEntityMixin) playerEntity).rpginventory$shouldEjectExclusiveEquipment()) {
			PlayerInventory playerInventory = playerEntity.getInventory();
			List<String> existingExclusiveEquipmentGroups = new ArrayList<>();
			for (int i = 0; i < EXCLUSIVE_EQUIPMENT_SLOT_AMOUNT; i++) {
				ItemStack itemStack = getEquipmentStack(playerEntity, i).copy();
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
						playerInventory.offerOrDrop(itemStack);
						setEquipmentStack(playerEntity, i, ItemStack.EMPTY);
						removedStack = true;
						break;
					}
				}
				if (removedStack) {
					continue;
				}
				existingExclusiveEquipmentGroups.addAll(currentExclusiveEquipmentGroups);
			}
			((DuckPlayerEntityMixin) playerEntity).rpginventory$setShouldEjectExclusiveEquipment(false);
		}
	}

	private static ItemStack getEquipmentStack(PlayerEntity playerEntity, int index) {
		return switch (index) {
			case 0 -> playerEntity.getEquippedStack(ExtendedEquipmentSlot.CLASS_ITEM);
			case 1 -> playerEntity.getEquippedStack(EquipmentSlot.HEAD);
			case 2 -> playerEntity.getEquippedStack(EquipmentSlot.CHEST);
			case 3 -> playerEntity.getEquippedStack(EquipmentSlot.LEGS);
			case 4 -> playerEntity.getEquippedStack(EquipmentSlot.FEET);
			case 5 -> playerEntity.getEquippedStack(ExtendedEquipmentSlot.SHOULDERS);
			case 6 -> playerEntity.getEquippedStack(ExtendedEquipmentSlot.GLOVES);
			case 7 -> playerEntity.getEquippedStack(ExtendedEquipmentSlot.BELT);
			case 8 -> playerEntity.getEquippedStack(ExtendedEquipmentSlot.NECKLACE);
			case 9 -> playerEntity.getEquippedStack(ExtendedEquipmentSlot.RING_1);
			case 10 -> playerEntity.getEquippedStack(ExtendedEquipmentSlot.RING_2);
			case 11 -> playerEntity.getEquippedStack(ExtendedEquipmentSlot.RELIC);
			case 12 -> playerEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_1);
			case 13 -> playerEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_2);
			case 14 -> playerEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_3);
			case 15 -> playerEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_4);
			case 16 -> playerEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_5);
			case 17 -> playerEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_6);
			case 18 -> playerEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_7);
			case 19 -> playerEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_8);
			default -> ItemStack.EMPTY;
		};
	}

	private static void setEquipmentStack(PlayerEntity playerEntity, int index, ItemStack stack) {
		switch (index) {
			case 0 -> playerEntity.equipStack(ExtendedEquipmentSlot.CLASS_ITEM, stack);
			case 1 -> playerEntity.equipStack(EquipmentSlot.HEAD, stack);
			case 2 -> playerEntity.equipStack(EquipmentSlot.CHEST, stack);
			case 3 -> playerEntity.equipStack(EquipmentSlot.LEGS, stack);
			case 4 -> playerEntity.equipStack(EquipmentSlot.FEET, stack);
			case 5 -> playerEntity.equipStack(ExtendedEquipmentSlot.SHOULDERS, stack);
			case 6 -> playerEntity.equipStack(ExtendedEquipmentSlot.GLOVES, stack);
			case 7 -> playerEntity.equipStack(ExtendedEquipmentSlot.BELT, stack);
			case 8 -> playerEntity.equipStack(ExtendedEquipmentSlot.NECKLACE, stack);
			case 9 -> playerEntity.equipStack(ExtendedEquipmentSlot.RING_1, stack);
			case 10 -> playerEntity.equipStack(ExtendedEquipmentSlot.RING_2, stack);
			case 11 -> playerEntity.equipStack(ExtendedEquipmentSlot.RELIC, stack);
			case 12 -> playerEntity.equipStack(ExtendedEquipmentSlot.SPELL_1, stack);
			case 13 -> playerEntity.equipStack(ExtendedEquipmentSlot.SPELL_2, stack);
			case 14 -> playerEntity.equipStack(ExtendedEquipmentSlot.SPELL_3, stack);
			case 15 -> playerEntity.equipStack(ExtendedEquipmentSlot.SPELL_4, stack);
			case 16 -> playerEntity.equipStack(ExtendedEquipmentSlot.SPELL_5, stack);
			case 17 -> playerEntity.equipStack(ExtendedEquipmentSlot.SPELL_6, stack);
			case 18 -> playerEntity.equipStack(ExtendedEquipmentSlot.SPELL_7, stack);
			case 19 -> playerEntity.equipStack(ExtendedEquipmentSlot.SPELL_8, stack);
		}

	}

	public static void rpginventory$ejectNonHotbarItemsFromHotbar(PlayerEntity playerEntity) { // FIXME is only called once?
		Optional<RegistryEntry.Reference<StatusEffect>> adventure_building_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.building_mode_status_effect_identifier.get());
		boolean hasAdventureBuildingEffect = adventure_building_status_effect.isPresent() && playerEntity.hasStatusEffect(adventure_building_status_effect.get());

		if (!playerEntity.isCreative() && !hasAdventureBuildingEffect && !((RPGInventory.SERVER_CONFIG.allow_equipment_changes.get() && !playerEntity.hasStatusEffect(RPGInventory.WILDERNESS)) || playerEntity.hasStatusEffect(RPGInventory.CIVILISATION))) {
			if (!((DuckPlayerEntityMixin) playerEntity).rpginventory$isAdventureHotbarCleanedUp()) {
				for (int i = 0; i < 9; i++) {
					PlayerInventory playerInventory = playerEntity.getInventory();
					Slot slot = playerEntity.playerScreenHandler.slots.get(i + 36);

					if (!slot.inventory.getStack(slot.getIndex()).isIn(Tags.ADVENTURE_HOTBAR_ITEMS)) {
						playerInventory.offerOrDrop(slot.inventory.removeStack(slot.getIndex()));
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

	public static void rpginventory$breakKeepInventoryItems(PlayerEntity playerEntity) {

		RPGInventory.breakKeepInventoryTrinkets(playerEntity);

		PlayerInventory playerInventory = playerEntity.getInventory();
		for (int i = 0; i < playerInventory.armor.size(); i++) {
			if (playerInventory.armor.get(i).isIn(Tags.SACRIFICED_TO_KEEP_INVENTORY_ON_DEATH)) {
				playerInventory.armor.set(i, ItemStack.EMPTY);
			}
		}
		if (playerInventory.offHand.get(0).isIn(Tags.SACRIFICED_TO_KEEP_INVENTORY_ON_DEATH)) {
			playerInventory.offHand.set(0, ItemStack.EMPTY);
		}
		if (playerInventory instanceof DuckPlayerInventoryMixin rpg_inventory) {

			if (rpg_inventory.rpginventory$getHand().isIn(Tags.SACRIFICED_TO_KEEP_INVENTORY_ON_DEATH)) {
				rpg_inventory.rpginventory$setHand(ItemStack.EMPTY);
			}
			if (rpg_inventory.rpginventory$getAlternativeHand().isIn(Tags.SACRIFICED_TO_KEEP_INVENTORY_ON_DEATH)) {
				rpg_inventory.rpginventory$setAlternativeHand(ItemStack.EMPTY);
			}
			if (rpg_inventory.rpginventory$getAlternativeOffhand().isIn(Tags.SACRIFICED_TO_KEEP_INVENTORY_ON_DEATH)) {
				rpg_inventory.rpginventory$setAlternativeOffhand(ItemStack.EMPTY);
			}
			if (rpg_inventory.rpginventory$getSheathedHand().isIn(Tags.SACRIFICED_TO_KEEP_INVENTORY_ON_DEATH)) {
				rpg_inventory.rpginventory$setSheathedHand(ItemStack.EMPTY);
			}
			if (rpg_inventory.rpginventory$getSheathedOffhand().isIn(Tags.SACRIFICED_TO_KEEP_INVENTORY_ON_DEATH)) {
				rpg_inventory.rpginventory$setSheathedOffhand(ItemStack.EMPTY);
			}
			for (int i = 0; i < 14; i++) {
				if (rpg_inventory.rpginventory$getAdditionalEquipmentStack(i).isIn(Tags.SACRIFICED_TO_KEEP_INVENTORY_ON_DEATH)) {
					rpg_inventory.rpginventory$setAdditionalEquipmentStack(i, ItemStack.EMPTY);
				}
			}
		}
	}
}
