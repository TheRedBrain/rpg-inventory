package com.github.theredbrain.betteradventuremode.mixin.spell_engine.internals;

import com.github.theredbrain.betteradventuremode.spell_engine.DuckSpellContainerMixin;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.SpellContainer;
import net.spell_engine.api.spell.SpellPool;
import net.spell_engine.internals.SpellContainerHelper;
import net.spell_engine.internals.SpellRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.*;
import java.util.stream.Stream;

@Mixin(SpellContainerHelper.class)
public abstract class SpellContainerHelperMixin {

    /**
     *
     * @author TheRedBrain
     * @reason
     */
    @Overwrite
    public static SpellContainer containerWithProxy(SpellContainer proxyContainer, PlayerEntity player) {
        Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(player);
        if (proxyContainer != null && proxyContainer.is_proxy && component.isPresent()) {
            SpellPool validSpellPool = SpellPool.empty;
            if (((DuckSpellContainerMixin) proxyContainer).betteradventuremode$getProxyPool() != null && !(((DuckSpellContainerMixin) proxyContainer).betteradventuremode$getProxyPool().isEmpty())) {
                validSpellPool = SpellRegistry.spellPool(new Identifier(((DuckSpellContainerMixin) proxyContainer).betteradventuremode$getProxyPool()));
            }
            TrinketComponent trinketComponent = (TrinketComponent)component.get();
            List<TrinketInventory> trinketInventories = List.of(
                    (TrinketInventory)((Map)trinketComponent.getInventory().get("spell_slot_1")).get("spell"),
                    (TrinketInventory)((Map)trinketComponent.getInventory().get("spell_slot_2")).get("spell"),
                    (TrinketInventory)((Map)trinketComponent.getInventory().get("spell_slot_3")).get("spell"),
                    (TrinketInventory)((Map)trinketComponent.getInventory().get("spell_slot_4")).get("spell"),
                    (TrinketInventory)((Map)trinketComponent.getInventory().get("spell_slot_5")).get("spell"),
                    (TrinketInventory)((Map)trinketComponent.getInventory().get("spell_slot_6")).get("spell"),
                    (TrinketInventory)((Map)trinketComponent.getInventory().get("spell_slot_7")).get("spell"),
                    (TrinketInventory)((Map)trinketComponent.getInventory().get("spell_slot_8")).get("spell")
            );
            List<String> mergedSpellIds = Stream.of(proxyContainer.spell_ids).flatMap(Collection::stream).distinct().toList();
            for (TrinketInventory trinketInventory : trinketInventories) {

                ItemStack spellBookStack = trinketInventory.getStack(0);
                if (!spellBookStack.isEmpty()) {
                    SpellContainer spellBookContainer = SpellContainerHelper.containerFromItemStack(spellBookStack);
                    if (spellBookContainer != null) {
                        List<String> oldList = mergedSpellIds;
                        List<String> newSpellsUnfiltered = spellBookContainer.spell_ids;
                        List<String> newSpells = List.of();
                        if (validSpellPool == SpellPool.empty) {
                            newSpells = newSpellsUnfiltered;
                        } else {
                            for (String spell : newSpellsUnfiltered) {
                                List<String> oldSpells = newSpells;
                                if (validSpellPool.spellIds().contains(new Identifier(spell))) {
                                    newSpells = Stream.of(oldSpells, List.of(spell)).flatMap(Collection::stream).distinct().toList();
                                }
                            }
                        }
                        mergedSpellIds = Stream.of(oldList, newSpells).flatMap(Collection::stream).distinct().toList();
                    }
                }
            }
            if (!mergedSpellIds.isEmpty()) {
                return new SpellContainer(false, (String)null, 0, mergedSpellIds);
            }
        }

        return proxyContainer;
    }
}
