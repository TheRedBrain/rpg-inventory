package com.github.theredbrain.bamcore.util;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.registry.Tags;
import dev.emi.trinkets.api.SlotAttributes;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.item.ItemStack;

import java.util.Objects;

public class ItemUtils {

    public static boolean isUsable(ItemStack stack) {
        return stack.getDamage() < stack.getMaxDamage() - 1 || stack.isIn(Tags.EMPTY_HAND_WEAPONS);
    }

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
    }
}
