package com.github.theredbrain.rpginventory.compat;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.registry.Tags;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Pair;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class TrinketsCompat {

	public static boolean isTrinketEquipped(LivingEntity livingEntity, Predicate<ItemStack> itemStackPredicate) {

		boolean bl = false;

		Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(livingEntity);
		if (trinkets.isPresent()) {
			bl = trinkets.get().isEquipped(itemStackPredicate);
		}
		return bl;
	}

	public static void breakKeepInventoryTrinkets(LivingEntity livingEntity) {
		Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(livingEntity);
		if (trinkets.isPresent()) {
			List<Pair<SlotReference, ItemStack>> trinketList = trinkets.get().getAllEquipped();
			for (net.minecraft.util.Pair<SlotReference, ItemStack> trinket : trinketList) {
				if (trinket.getRight().isIn(Tags.SACRIFICED_TO_KEEP_INVENTORY_ON_DEATH)) {
					trinket.getLeft().inventory().clear();
				}
			}
		}
	}

	static {
		TrinketsApi.registerTrinketPredicate(RPGInventory.identifier("can_change_equipment"), (stack, ref, entity) -> {

			Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.civilisation_status_effect_identifier.get());
			boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && entity.hasStatusEffect(civilisation_status_effect.get());

			Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.wilderness_status_effect_identifier.get());
			boolean hasWildernessEffect = wilderness_status_effect.isPresent() && entity.hasStatusEffect(wilderness_status_effect.get());

			if (hasCivilisationEffect
					|| entity instanceof PlayerEntity playerEntity && playerEntity.isCreative()
					|| entity.getServer() == null
					|| (RPGInventory.SERVER_CONFIG.allow_equipment_changes.get() && !hasWildernessEffect)) {
				return TriState.TRUE;
			}
			return TriState.FALSE;
		});
	}

	public static void init() {
	}
}
