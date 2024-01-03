package com.github.theredbrain.bamcore.registry;

import com.github.theredbrain.bamcore.BetterAdventureMode;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class PredicateRegistry {

    static {
        TrinketsApi.registerTrinketPredicate(BetterAdventureMode.identifier("rings_predicate"), (stack, ref, entity) -> {
            // TODO
            String group = ref.inventory().getSlotType().getGroup();
//            stack.isIn(Tags.SPELLS)
//            var map = TrinketsApi.getTrinket(stack.getItem()).getModifiers(stack, ref, entity, SlotAttributes.getUuid(ref));
            if (Objects.equals(group, "rings_1")) {
                return TriState.TRUE;
            }
            return TriState.FALSE;
        });
        TrinketsApi.registerTrinketPredicate(BetterAdventureMode.identifier("spells_predicate"), (stack, ref, entity) -> {
            // TODO
            String group = ref.inventory().getSlotType().getGroup();
//            stack.isIn(Tags.SPELLS)
//            var map = TrinketsApi.getTrinket(stack.getItem()).getModifiers(stack, ref, entity, SlotAttributes.getUuid(ref));
            if (Objects.equals(group, "spell_slot_1")) {
                return TriState.TRUE;
            }
            return TriState.FALSE;
        });
        TrinketsApi.registerTrinketPredicate(BetterAdventureMode.identifier("mana_regeneration_items"), (stack, ref, entity) -> {
            AtomicBoolean bl = new AtomicBoolean(false);
            TrinketsApi.getTrinketComponent(entity).ifPresent(comp -> {

                bl.set(comp.isEquipped(stack.getItem()));
            });
            if (stack.isIn(Tags.MANA_REGENERATING_TRINKETS) && bl.get()) {
                return TriState.TRUE;
            }
            return TriState.FALSE;
        });
        TrinketsApi.registerTrinketPredicate(BetterAdventureMode.identifier("can_change_equipment"), (stack, ref, entity) -> {
            if (entity.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT)
                    || entity instanceof PlayerEntity playerEntity && !((DuckPlayerEntityMixin) playerEntity).bamcore$isAdventure()
                    || entity.getServer() == null
                    || !entity.getServer().getGameRules().getBoolean(GameRulesRegistry.REQUIRE_CIVILISATION_EFFECT_TO_CHANGE_GEAR_IN_ADVENTURE_MODE)) {
                return TriState.TRUE;
            }
            return TriState.FALSE;
        });
    }
    public static void init() {}
}
