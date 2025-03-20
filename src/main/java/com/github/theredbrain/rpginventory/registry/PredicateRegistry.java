package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import dev.emi.trinkets.api.SlotType;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class PredicateRegistry {

	static {
		TrinketsApi.registerTrinketPredicate(RPGInventory.identifier("can_change_equipment"), (stack, ref, entity) -> {

			Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.civilisation_status_effect_identifier.get());
			boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && entity.hasStatusEffect(civilisation_status_effect.get());

			Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.wilderness_status_effect_identifier.get());
			boolean hasWildernessEffect = wilderness_status_effect.isPresent() && entity.hasStatusEffect(wilderness_status_effect.get());

			if (hasCivilisationEffect
					|| entity instanceof PlayerEntity playerEntity && playerEntity.isCreative()
					|| entity.getServer() == null
					|| (entity.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT) && !hasWildernessEffect)) {
				return TriState.TRUE;
			}
			return TriState.FALSE;
		});
	}

	public static void init() {
	}
}
