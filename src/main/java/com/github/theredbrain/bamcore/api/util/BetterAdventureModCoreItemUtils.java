package com.github.theredbrain.bamcore.api.util;

import com.github.theredbrain.bamcore.registry.Tags;
import com.github.theredbrain.bamcore.spell_engine.DuckSpellContainerMixin;
import net.minecraft.item.ItemStack;
import net.spell_engine.api.spell.SpellContainer;

public class BetterAdventureModCoreItemUtils {

    public static boolean isUsable(ItemStack stack) {
        return stack.getDamage() < stack.getMaxDamage() - 1 || stack.isIn(Tags.EMPTY_HAND_WEAPONS);
    }

    public static SpellContainer setProxyPool(SpellContainer spellContainer, String proxyPool) {
        ((DuckSpellContainerMixin)spellContainer).setProxyPool(proxyPool);
        return spellContainer;
    }
}
