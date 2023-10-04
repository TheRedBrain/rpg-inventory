package com.github.theredbrain.bamcore.registry;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.util.TriState;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class PredicateRegistry {

    static {
        TrinketsApi.registerTrinketPredicate(BetterAdventureModeCore.identifier("rings_predicate"), (stack, ref, entity) -> {
            // TODO
            String group = ref.inventory().getSlotType().getGroup();
//            stack.isIn(Tags.SPELLS)
//            var map = TrinketsApi.getTrinket(stack.getItem()).getModifiers(stack, ref, entity, SlotAttributes.getUuid(ref));
            if (Objects.equals(group, "rings_1")) {
                return TriState.TRUE;
            }
            return TriState.FALSE;
        });
        TrinketsApi.registerTrinketPredicate(BetterAdventureModeCore.identifier("spells_predicate"), (stack, ref, entity) -> {
            // TODO
            String group = ref.inventory().getSlotType().getGroup();
//            stack.isIn(Tags.SPELLS)
//            var map = TrinketsApi.getTrinket(stack.getItem()).getModifiers(stack, ref, entity, SlotAttributes.getUuid(ref));
            if (Objects.equals(group, "spell_slot_1")) {
                return TriState.TRUE;
            }
            return TriState.FALSE;
        });
        TrinketsApi.registerTrinketPredicate(BetterAdventureModeCore.identifier("mana_regeneration_items"), (stack, ref, entity) -> {
            AtomicBoolean bl = new AtomicBoolean(false);
            TrinketsApi.getTrinketComponent(entity).ifPresent(comp -> {

                bl.set(comp.isEquipped(stack.getItem()));
            });
            if (stack.isIn(Tags.MANA_REGENERATING_ITEMS) && bl.get()) {
                return TriState.TRUE;
            }
            return TriState.FALSE;
        });
    }
    public static void init() {}
}
