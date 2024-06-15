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

import java.util.Objects;

public class PredicateRegistry {

    static {
        TrinketsApi.registerTrinketPredicate(RPGInventory.identifier("rings_predicate"), (stack, ref, entity) -> {
            // TODO
            String group = ref.inventory().getSlotType().getGroup();
//            stack.isIn(Tags.SPELLS)
//            var map = TrinketsApi.getTrinket(stack.getItem()).getModifiers(stack, ref, entity, SlotAttributes.getUuid(ref));
            if (Objects.equals(group, "rings_1")) {
                return TriState.TRUE;
            }
            return TriState.FALSE;
        });
        TrinketsApi.registerTrinketPredicate(RPGInventory.identifier("spells_predicate"), (stack, ref, entity) -> {
            // TODO
            String group = ref.inventory().getSlotType().getGroup();
//            stack.isIn(Tags.SPELLS)
//            var map = TrinketsApi.getTrinket(stack.getItem()).getModifiers(stack, ref, entity, SlotAttributes.getUuid(ref));
            if (Objects.equals(group, "spell_slot_1")) {
                return TriState.TRUE;
            }
            return TriState.FALSE;
        });
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
        TrinketsApi.registerTrinketPredicate(RPGInventory.identifier("can_change_main_hand_slot"), (stack, ref, entity) -> {

            StatusEffect civilisation_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.civilisation_status_effect_identifier));
            boolean hasCivilisationEffect = civilisation_status_effect != null && entity.hasStatusEffect(civilisation_status_effect);

            StatusEffect wilderness_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.wilderness_status_effect_identifier));
            boolean hasWildernessEffect = wilderness_status_effect != null && entity.hasStatusEffect(wilderness_status_effect);

            boolean isPlayerCreative = false;
            boolean isMainHandSheathed = false;

            if (entity instanceof PlayerEntity playerEntity) {
                isPlayerCreative = playerEntity.isCreative();
                isMainHandSheathed = ((DuckPlayerEntityMixin)playerEntity).rpginventory$isMainHandStackSheathed();
            }

            if ((hasCivilisationEffect
                    || isPlayerCreative
                    || entity.getServer() == null
                    || (entity.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT) && !hasWildernessEffect)
            ) && !isMainHandSheathed) {
                return TriState.TRUE;
            }
            return TriState.FALSE;
        });
        TrinketsApi.registerTrinketPredicate(RPGInventory.identifier("can_change_sheathed_main_hand_slot"), (stack, ref, entity) -> {

            StatusEffect civilisation_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.civilisation_status_effect_identifier));
            boolean hasCivilisationEffect = civilisation_status_effect != null && entity.hasStatusEffect(civilisation_status_effect);

            StatusEffect wilderness_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.wilderness_status_effect_identifier));
            boolean hasWildernessEffect = wilderness_status_effect != null && entity.hasStatusEffect(wilderness_status_effect);

            boolean isPlayerCreative = false;
            boolean isMainHandSheathed = false;

            if (entity instanceof PlayerEntity playerEntity) {
                isPlayerCreative = playerEntity.isCreative();
                isMainHandSheathed = ((DuckPlayerEntityMixin)playerEntity).rpginventory$isMainHandStackSheathed();
            }

            if ((hasCivilisationEffect
                    || isPlayerCreative
                    || entity.getServer() == null
                    || (entity.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT) && !hasWildernessEffect)
            ) && isMainHandSheathed) {
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
                isOffhandSheathed = ((DuckPlayerEntityMixin)playerEntity).rpginventory$isOffHandStackSheathed();
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
    public static void init() {}
}
