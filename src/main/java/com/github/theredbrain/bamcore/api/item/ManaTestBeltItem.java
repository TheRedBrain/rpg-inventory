package com.github.theredbrain.bamcore.api.item;

import com.github.theredbrain.bamcore.api.util.BetterAdventureModCoreAttributeModifierUUIDs;
import com.github.theredbrain.bamcore.registry.EntityAttributesRegistry;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class ManaTestBeltItem extends TrinketItem {
    public ManaTestBeltItem(Settings settings) {
        super(settings);
    }
    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        Multimap<EntityAttribute, EntityAttributeModifier> map = super.getModifiers(stack, slot, entity, uuid);
        map.put(EntityAttributesRegistry.MAX_MANA,
                new EntityAttributeModifier(UUID.fromString(BetterAdventureModCoreAttributeModifierUUIDs.BELT_SLOT),
                        "max_mana", 20.0, EntityAttributeModifier.Operation.ADDITION));
        map.put(EntityAttributesRegistry.MANA_REGENERATION,
                new EntityAttributeModifier(UUID.fromString(BetterAdventureModCoreAttributeModifierUUIDs.BELT_SLOT),
                        "mana_regeneration", 2.0, EntityAttributeModifier.Operation.ADDITION));
        return map;
    }
}
