package com.github.theredbrain.betteradventuremode.item;

import com.github.theredbrain.betteradventuremode.util.AttributeModifierUUIDs;
import com.github.theredbrain.betteradventuremode.registry.EntityAttributesRegistry;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class MultiSlotSpellBookItem extends TrinketItem/*SpellBookItem*/ { // TODO SpellEngine
    private final int slot_amount_addition;
    public MultiSlotSpellBookItem(int slot_amount_addition, Identifier poolId, Settings settings) {
//        super(poolId, settings);
        super(settings);
        this.slot_amount_addition = slot_amount_addition;
    }
    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        Multimap<EntityAttribute, EntityAttributeModifier> map = super.getModifiers(stack, slot, entity, uuid);

        switch (slot.inventory().getSlotType().getGroup()) {
            case "spell_slot_1":
                map.put(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT,
                        new EntityAttributeModifier(UUID.fromString(AttributeModifierUUIDs.SPELL_SLOT_1),
                                "active_spell_slot_amount", this.slot_amount_addition, EntityAttributeModifier.Operation.ADDITION));
                break;
            case "spell_slot_2":
                map.put(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT,
                        new EntityAttributeModifier(UUID.fromString(AttributeModifierUUIDs.SPELL_SLOT_2),
                                "active_spell_slot_amount", this.slot_amount_addition, EntityAttributeModifier.Operation.ADDITION));
                break;
            case "spell_slot_3":
                map.put(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT,
                        new EntityAttributeModifier(UUID.fromString(AttributeModifierUUIDs.SPELL_SLOT_3),
                                "active_spell_slot_amount", this.slot_amount_addition, EntityAttributeModifier.Operation.ADDITION));
                break;
            case "spell_slot_4":
                map.put(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT,
                        new EntityAttributeModifier(UUID.fromString(AttributeModifierUUIDs.SPELL_SLOT_4),
                                "active_spell_slot_amount", this.slot_amount_addition, EntityAttributeModifier.Operation.ADDITION));
                break;
            case "spell_slot_5":
                map.put(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT,
                        new EntityAttributeModifier(UUID.fromString(AttributeModifierUUIDs.SPELL_SLOT_5),
                                "active_spell_slot_amount", this.slot_amount_addition, EntityAttributeModifier.Operation.ADDITION));
                break;
            case "spell_slot_6":
                map.put(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT,
                        new EntityAttributeModifier(UUID.fromString(AttributeModifierUUIDs.SPELL_SLOT_6),
                                "active_spell_slot_amount", this.slot_amount_addition, EntityAttributeModifier.Operation.ADDITION));
                break;
            case "spell_slot_7":
                map.put(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT,
                        new EntityAttributeModifier(UUID.fromString(AttributeModifierUUIDs.SPELL_SLOT_7),
                                "active_spell_slot_amount", this.slot_amount_addition, EntityAttributeModifier.Operation.ADDITION));
                break;
            case "spell_slot_8":
                map.put(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT,
                        new EntityAttributeModifier(UUID.fromString(AttributeModifierUUIDs.SPELL_SLOT_8),
                                "active_spell_slot_amount", this.slot_amount_addition, EntityAttributeModifier.Operation.ADDITION));
                break;
        }
        return map;
    }

}
