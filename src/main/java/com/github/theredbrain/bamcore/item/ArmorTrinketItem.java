package com.github.theredbrain.bamcore.item;

import com.github.theredbrain.bamcore.registry.EntityAttributesRegistry;
import com.github.theredbrain.bamcore.util.AttributeModifierUUIDs;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class ArmorTrinketItem extends TrinketItem {
    public ArmorTrinketItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isDamageable() {
        return this.getMaxDamage() > 1;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        Multimap<EntityAttribute, EntityAttributeModifier> map = super.getModifiers(stack, slot, entity, uuid);
        switch (slot.inventory().getSlotType().getGroup()) {
            case "gloves":
                map.put(EntityAttributesRegistry.HEALTH_REGENERATION,
                        new EntityAttributeModifier(UUID.fromString(AttributeModifierUUIDs.GLOVE_SLOT),
                                "Health Regeneration", 2.0, EntityAttributeModifier.Operation.ADDITION));
                break;
            case "shoulders":
                map.put(EntityAttributesRegistry.HEALTH_REGENERATION,
                        new EntityAttributeModifier(UUID.fromString(AttributeModifierUUIDs.SHOULDERS_SLOT),
                                "Health Regeneration", 2.0, EntityAttributeModifier.Operation.ADDITION));
                break;
        }
        return map;
    }
}
