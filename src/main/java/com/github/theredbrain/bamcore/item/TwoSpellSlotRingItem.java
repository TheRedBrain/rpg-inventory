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

public class TwoSpellSlotRingItem extends TrinketItem {
    public TwoSpellSlotRingItem(Settings settings) {
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
            case "rings_1":
                map.put(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT,
                        new EntityAttributeModifier(UUID.fromString(AttributeModifierUUIDs.RING_SLOT_1),
                                "active_spell_slot_amount", 3.0, EntityAttributeModifier.Operation.ADDITION));
                break;
            case "rings_2":
                map.put(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT,
                        new EntityAttributeModifier(UUID.fromString(AttributeModifierUUIDs.RING_SLOT_2),
                                "active_spell_slot_amount", 3.0, EntityAttributeModifier.Operation.ADDITION));
                break;
        }
        return map;
    }
}
