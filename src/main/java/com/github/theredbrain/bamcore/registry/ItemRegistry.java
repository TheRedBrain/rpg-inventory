package com.github.theredbrain.bamcore.registry;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.api.item.ModifySpellSlotAmountRingItem;
import com.github.theredbrain.bamcore.item.*;
import dev.emi.trinkets.api.TrinketItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.Nullable;

public class ItemRegistry {

    public static final Item DEFAULT_EMPTY_HAND_WEAPON = registerItem("default_empty_hand_weapon", new EmptyHandWeapon(0, -3.5F, new Item.Settings().maxCount(1)), null);
    public static final Item TEST_BELT = registerItem("test_belt", new TrinketItem(new FabricItemSettings().maxCount(1)), ItemGroups.COMBAT);
    public static final Item THREE_SPELL_SLOT_RING = registerItem("three_spell_slot_ring", new ModifySpellSlotAmountRingItem(3, new FabricItemSettings().maxCount(1)), ItemGroups.COMBAT);


    private static Item registerItem(String name, Item item, @Nullable RegistryKey<ItemGroup> itemGroup) {

        if (itemGroup != null) {
            ItemGroupEvents.modifyEntriesEvent(itemGroup).register(content -> {
                content.add(item);
            });
        }
        return Registry.register(Registries.ITEM, BetterAdventureModeCore.identifier(name), item);
    }

    public static void init() {
    }
}
