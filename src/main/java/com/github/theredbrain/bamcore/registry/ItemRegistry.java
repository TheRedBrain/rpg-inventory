package com.github.theredbrain.bamcore.registry;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.api.item.BetterAdventureMode_BasicShieldItem;
import com.github.theredbrain.bamcore.api.item.BetterAdventureMode_BasicWeaponItem;
import com.github.theredbrain.bamcore.api.item.ModifyEntityAttributeRingItem;
import com.github.theredbrain.bamcore.api.util.BetterAdventureModeCoreEntityAttributes;
import com.github.theredbrain.bamcore.api.util.BetterAdventureModeDamageTypes;
import com.github.theredbrain.bamcore.item.*;
import dev.emi.trinkets.api.TrinketItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.Nullable;

public class ItemRegistry {

    public static final Item DEFAULT_EMPTY_HAND_WEAPON = registerItem("default_empty_hand_weapon", new EmptyHandWeapon(1, -3.0F, 1, new Item.Settings()), null);
    public static final Item TEST_BELT = registerItem("test_belt", new TrinketItem(new FabricItemSettings().maxCount(1)), ItemGroups.COMBAT);
    public static final Item THREE_SPELL_SLOT_RING = registerItem("three_spell_slot_ring", new ModifyEntityAttributeRingItem(BetterAdventureModeCoreEntityAttributes.ACTIVE_SPELL_SLOT_AMOUNT, "active_spell_slot_amount", 3, EntityAttributeModifier.Operation.ADDITION, new FabricItemSettings().maxCount(1)), ItemGroups.COMBAT);
    public static final Item TEST_BUCKLER = registerItem("test_buckler", new BetterAdventureMode_BasicShieldItem(2, 0.2, true, 3.0, 2, new FabricItemSettings().maxDamage(336)), ItemGroups.COMBAT);
    public static final Item TEST_NORMAL_SHIELD = registerItem("test_normal_shield", new BetterAdventureMode_BasicShieldItem(3, 0.5, true, 1.5, 3, new FabricItemSettings().maxDamage(336)), ItemGroups.COMBAT);
    public static final Item TEST_TOWER_SHIELD = registerItem("test_tower_shield", new BetterAdventureMode_BasicShieldItem(5, 1.0, false, 1.0, 5, new FabricItemSettings().maxDamage(336)), ItemGroups.COMBAT);
    public static final Item TEST_SWORD = registerItem("test_sword", new BetterAdventureMode_BasicWeaponItem(BetterAdventureModeDamageTypes.PLAYER_SLASHING_DAMAGE_TYPE, BetterAdventureModeDamageTypes.PLAYER_PIERCING_DAMAGE_TYPE, 4, -3.0F, 3, 2, new FabricItemSettings().maxDamage(336)), ItemGroups.COMBAT);


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
