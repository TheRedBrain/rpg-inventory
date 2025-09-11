package com.github.theredbrain.rpginventory.entity.player;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.LivingEntityHelper;
import com.github.theredbrain.rpginventory.registry.Tags;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.Optional;
import java.util.function.Predicate;

public class PlayerEntityHelper {

	public static boolean rpginventory$onPVPDeath(ServerPlayerEntity serverPlayerEntity, RegistryEntry.Reference<StatusEffect> pvpStatusEffect) {

		StatusEffectInstance pvpEffectInstance = serverPlayerEntity.getStatusEffect(pvpStatusEffect);
		if (pvpEffectInstance != null) {
			for (StatusEffectInstance instance : serverPlayerEntity.getStatusEffects()) {
				if (!(instance.getEffectType().isIn(Tags.KEPT_ON_PVP_DEATH) || instance.getEffectType() == pvpStatusEffect)) {
					serverPlayerEntity.removeStatusEffect(instance.getEffectType());
				}
			}
			int newAmplifier = pvpEffectInstance.getAmplifier() - 1;
			if (newAmplifier >= 0) {
				resetPlayerStatus(serverPlayerEntity);
				serverPlayerEntity.addStatusEffect(new StatusEffectInstance(pvpEffectInstance.getEffectType(), pvpEffectInstance.getDuration(), newAmplifier, pvpEffectInstance.isAmbient(), pvpEffectInstance.shouldShowParticles(), pvpEffectInstance.shouldShowIcon()));
				teleportToPVPRespawnPosition(serverPlayerEntity, false);
			} else {
				resetPlayerStatus(serverPlayerEntity);
				serverPlayerEntity.removeStatusEffect(pvpEffectInstance.getEffectType());
				teleportToPVPRespawnPosition(serverPlayerEntity, true);
			}
			return true;
		}
		return false;
	}

	public static void resetPlayerStatus(ServerPlayerEntity serverPlayerEntity, boolean endOfBattle) {
		serverPlayerEntity.setHealth(serverPlayerEntity.getMaxHealth());
		RPGInventory.resetModdedPlayerStatus(serverPlayerEntity, endOfBattle);
	}

	public static void teleportToPVPRespawnPosition(ServerPlayerEntity serverPlayerEntity, boolean endOfBattle) {

		MinecraftServer server = serverPlayerEntity.getServer();
		if (server != null) {
			ServerWorld targetWorld = null;
			BlockPos targetPos = null;
			double targetYaw = 0.0;
			double targetPitch = 0.0;
			MutablePair<RegistryKey<World>, MutablePair<BlockPos, MutablePair<Double, Double>>> pvp_respawn_position = RPGInventory.getPVPRespawnPosition(serverPlayerEntity, endOfBattle);

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

	public static void rpginventory$updateEquipmentStatusEffects(PlayerEntity playerEntity) {

		Predicate<ItemStack> keep_inventory_on_death_item_equipped_predicate = stack -> stack.isIn(Tags.SACRIFICED_TO_KEEP_INVENTORY_ON_DEATH);

		boolean keep_inventory_on_death_item_equipped = RPGInventory.isTrinketEquipped(playerEntity, keep_inventory_on_death_item_equipped_predicate);

		keep_inventory_on_death_item_equipped = keep_inventory_on_death_item_equipped || LivingEntityHelper.rpginventory$hasEquipped(playerEntity, keep_inventory_on_death_item_equipped_predicate);

		Optional<RegistryEntry.Reference<StatusEffect>> keep_inventory_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.keep_inventory_status_effect_identifier.get());
		if (keep_inventory_status_effect.isPresent()) {
			if (keep_inventory_on_death_item_equipped) {
				if (!playerEntity.hasStatusEffect(keep_inventory_status_effect.get())) {
					playerEntity.addStatusEffect(new StatusEffectInstance(keep_inventory_status_effect.get(), -1, 0, false, false, false));
				}
			} else {
				playerEntity.removeStatusEffect(keep_inventory_status_effect.get());
			}
		}

		ItemStack itemStackMainHand = playerEntity.getEquippedStack(EquipmentSlot.MAINHAND);
		ItemStack itemStackOffHand = playerEntity.getEquippedStack(EquipmentSlot.OFFHAND);
		Optional<RegistryEntry.Reference<StatusEffect>> adventure_building_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.building_mode_status_effect_identifier.get());
		boolean hasAdventureBuildingEffect = adventure_building_status_effect.isPresent() && playerEntity.hasStatusEffect(adventure_building_status_effect.get());

		Optional<RegistryEntry.Reference<StatusEffect>> no_attack_item_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.no_attack_item_status_effect_identifier.get());
		if (no_attack_item_status_effect.isPresent()) {
			if (!itemStackMainHand.isIn(Tags.ATTACK_ITEMS) && !playerEntity.isCreative() && !hasAdventureBuildingEffect && !RPGInventory.SERVER_CONFIG.allow_attacking_with_non_attack_items.get()) {
				if (!playerEntity.hasStatusEffect(no_attack_item_status_effect.get())) {
					playerEntity.addStatusEffect(new StatusEffectInstance(no_attack_item_status_effect.get(), -1, 0, false, false, false));
				}
			} else {
				playerEntity.removeStatusEffect(no_attack_item_status_effect.get());
			}
		}

		Optional<RegistryEntry.Reference<StatusEffect>> needs_two_handing_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.needs_two_handing_status_effect_identifier.get());
		if (needs_two_handing_status_effect.isPresent()) {
			if (itemStackMainHand.isIn(Tags.TWO_HANDED_ITEMS) && !itemStackOffHand.isEmpty() && !playerEntity.isCreative() && !hasAdventureBuildingEffect) {
				if (!playerEntity.hasStatusEffect(needs_two_handing_status_effect.get())) {
					playerEntity.addStatusEffect(new StatusEffectInstance(needs_two_handing_status_effect.get(), -1, 0, false, false, false));
				}
			} else {
				playerEntity.removeStatusEffect(needs_two_handing_status_effect.get());
			}
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
			}
			((DuckPlayerEntityMixin) playerEntity).rpginventory$setIsHandSlotOverhaulActive(isHandSlotOverhaulActive);
		}
	}

	public static void rpginventory$ejectSecondUniqueRing(PlayerEntity playerEntity) {
		PlayerInventory playerInventory = playerEntity.getInventory();
		ItemStack firstRingStack = ((DuckPlayerInventoryMixin) playerInventory).rpginventory$getAdditionalEquipmentStack(3);
		ItemStack secondRingStack = ((DuckPlayerInventoryMixin) playerInventory).rpginventory$getAdditionalEquipmentStack(4);
		if (firstRingStack.isIn(Tags.UNIQUE_RINGS) && firstRingStack.getItem() == secondRingStack.getItem()) {
			playerInventory.offerOrDrop(((DuckPlayerInventoryMixin) playerInventory).rpginventory$setAdditionalEquipmentStack(4, ItemStack.EMPTY));

		}
	}

	public static void rpginventory$ejectNonHotbarItemsFromHotbar(PlayerEntity playerEntity) { // FIXME is only called once?
		Optional<RegistryEntry.Reference<StatusEffect>> adventure_building_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.building_mode_status_effect_identifier.get());
		boolean hasAdventureBuildingEffect = adventure_building_status_effect.isPresent() && playerEntity.hasStatusEffect(adventure_building_status_effect.get());

		Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.civilisation_status_effect_identifier.get());
		boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && playerEntity.hasStatusEffect(civilisation_status_effect.get());

		Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.wilderness_status_effect_identifier.get());
		boolean hasWildernessEffect = wilderness_status_effect.isPresent() && playerEntity.hasStatusEffect(wilderness_status_effect.get());

		if (!playerEntity.isCreative() && !hasAdventureBuildingEffect && !((RPGInventory.SERVER_CONFIG.allow_equipment_changes.get() && !hasWildernessEffect) || hasCivilisationEffect)) {
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
