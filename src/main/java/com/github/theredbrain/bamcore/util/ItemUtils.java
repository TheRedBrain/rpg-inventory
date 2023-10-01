package com.github.theredbrain.bamcore.util;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.registry.Tags;
import dev.emi.trinkets.api.SlotAttributes;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.item.ItemStack;

public class ItemUtils {

    public static boolean isUsable(ItemStack stack) {
        return stack.getDamage() < stack.getMaxDamage() - 1 || stack.isIn(Tags.EMPTY_HAND_WEAPONS);
    }

    static {
        TrinketsApi.registerTrinketPredicate(BetterAdventureModeCore.identifier("bamcore_predicate"), (stack, ref, entity) -> {
            // TODO
//            String group = ref.inventory().getSlotType().getGroup();
//            stack.isIn(Tags.SPELLS)
            var map = TrinketsApi.getTrinket(stack.getItem()).getModifiers(stack, ref, entity, SlotAttributes.getUuid(ref));
            if (!map.isEmpty()) {
                return TriState.TRUE;
            }
            return TriState.DEFAULT;
        });
    }
}
