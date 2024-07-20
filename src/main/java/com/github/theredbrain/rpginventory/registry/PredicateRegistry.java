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
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class PredicateRegistry {

	static {
		TrinketsApi.registerTrinketPredicate(RPGInventory.identifier("can_change_equipment"), (stack, ref, entity) -> {

			StatusEffect civilisation_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.civilisation_status_effect_identifier));
			boolean hasCivilisationEffect = civilisation_status_effect != null && entity.hasStatusEffect(civilisation_status_effect);

			StatusEffect wilderness_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.wilderness_status_effect_identifier));
			boolean hasWildernessEffect = wilderness_status_effect != null && entity.hasStatusEffect(wilderness_status_effect);

			if (hasCivilisationEffect
					|| entity instanceof PlayerEntity playerEntity && playerEntity.isCreative()
					|| entity.getServer() == null
					|| (entity.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT) && !hasWildernessEffect)) {
				return TriState.TRUE;
			}
			return TriState.FALSE;
		});
		TrinketsApi.registerTrinketPredicate(RPGInventory.identifier("are_hand_slots_restricted_to_tags"), (stack, ref, entity) -> {
			SlotType slot = ref.inventory().getSlotType();
			if (
					stack.isIn(TagKey.of(RegistryKeys.ITEM, new Identifier("trinkets", slot.getGroup() + "/" + slot.getName())))
							|| stack.isIn(TagKey.of(RegistryKeys.ITEM, new Identifier("trinkets", "all")))
							|| !RPGInventory.serverConfig.are_hand_items_restricted_to_item_tags
			) {
				return TriState.TRUE;
			}
			return TriState.FALSE;
		});
		TrinketsApi.registerTrinketPredicate(RPGInventory.identifier("can_change_hand_slot"), (stack, ref, entity) -> {

			StatusEffect civilisation_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.civilisation_status_effect_identifier));
			boolean hasCivilisationEffect = civilisation_status_effect != null && entity.hasStatusEffect(civilisation_status_effect);

			StatusEffect wilderness_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.wilderness_status_effect_identifier));
			boolean hasWildernessEffect = wilderness_status_effect != null && entity.hasStatusEffect(wilderness_status_effect);

			boolean isPlayerCreative = false;
			boolean isHandSheathed = false;

			if (entity instanceof PlayerEntity playerEntity) {
				isPlayerCreative = playerEntity.isCreative();
				isHandSheathed = ((DuckPlayerEntityMixin) playerEntity).rpginventory$isHandStackSheathed();
			}

			if ((hasCivilisationEffect
					|| isPlayerCreative
					|| entity.getServer() == null
					|| (entity.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT) && !hasWildernessEffect)
			) && !isHandSheathed) {
				return TriState.TRUE;
			}
			return TriState.FALSE;
		});
		TrinketsApi.registerTrinketPredicate(RPGInventory.identifier("can_change_sheathed_hand_slot"), (stack, ref, entity) -> {

			StatusEffect civilisation_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.civilisation_status_effect_identifier));
			boolean hasCivilisationEffect = civilisation_status_effect != null && entity.hasStatusEffect(civilisation_status_effect);

			StatusEffect wilderness_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.wilderness_status_effect_identifier));
			boolean hasWildernessEffect = wilderness_status_effect != null && entity.hasStatusEffect(wilderness_status_effect);

			boolean isPlayerCreative = false;
			boolean isHandSheathed = false;

			if (entity instanceof PlayerEntity playerEntity) {
				isPlayerCreative = playerEntity.isCreative();
				isHandSheathed = ((DuckPlayerEntityMixin) playerEntity).rpginventory$isHandStackSheathed();
			}

			if ((hasCivilisationEffect
					|| isPlayerCreative
					|| entity.getServer() == null
					|| (entity.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT) && !hasWildernessEffect)
			) && isHandSheathed) {
				return TriState.TRUE;
			}
			return TriState.FALSE;
		});
		TrinketsApi.registerTrinketPredicate(RPGInventory.identifier("can_change_sheathed_offhand_slot"), (stack, ref, entity) -> {

			StatusEffect civilisation_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.civilisation_status_effect_identifier));
			boolean hasCivilisationEffect = civilisation_status_effect != null && entity.hasStatusEffect(civilisation_status_effect);

			StatusEffect wilderness_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.wilderness_status_effect_identifier));
			boolean hasWildernessEffect = wilderness_status_effect != null && entity.hasStatusEffect(wilderness_status_effect);

			boolean isPlayerCreative = false;
			boolean isOffhandSheathed = false;

			if (entity instanceof PlayerEntity playerEntity) {
				isPlayerCreative = playerEntity.isCreative();
				isOffhandSheathed = ((DuckPlayerEntityMixin) playerEntity).rpginventory$isOffHandStackSheathed();
			}

			if ((hasCivilisationEffect
					|| isPlayerCreative
					|| entity.getServer() == null
					|| (entity.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT) && !hasWildernessEffect)
			) && isOffhandSheathed) {
				return TriState.TRUE;
			}
			return TriState.FALSE;
		});
	}

	public static void init() {
	}
}
