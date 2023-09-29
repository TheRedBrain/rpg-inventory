package com.github.theredbrain.bamcore.item;

import com.github.theredbrain.bamcore.registry.EntityAttributesRegistry;
import com.github.theredbrain.bamcore.util.AttributeModifierUUIDs;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.spell_engine.api.item.trinket.SpellBookItem;

import java.util.UUID;

public class TwoSlotSpellItem extends SpellBookItem {
    public TwoSlotSpellItem(Identifier poolId, Settings settings) {
        super(poolId, settings);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        Multimap<EntityAttribute, EntityAttributeModifier> map = super.getModifiers(stack, slot, entity, uuid);

        switch (slot.inventory().getSlotType().getGroup()) {
            case "spell_slot_1":
                map.put(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT,
                        new EntityAttributeModifier(UUID.fromString(AttributeModifierUUIDs.SPELL_SLOT_1),
                                "active_spell_slot_amount", -1.0, EntityAttributeModifier.Operation.ADDITION));
                break;
            case "spell_slot_2":
                map.put(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT,
                        new EntityAttributeModifier(UUID.fromString(AttributeModifierUUIDs.SPELL_SLOT_2),
                                "active_spell_slot_amount", -1.0, EntityAttributeModifier.Operation.ADDITION));
                break;
            case "spell_slot_3":
                map.put(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT,
                        new EntityAttributeModifier(UUID.fromString(AttributeModifierUUIDs.SPELL_SLOT_3),
                                "active_spell_slot_amount", -1.0, EntityAttributeModifier.Operation.ADDITION));
                break;
            case "spell_slot_4":
                map.put(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT,
                        new EntityAttributeModifier(UUID.fromString(AttributeModifierUUIDs.SPELL_SLOT_4),
                                "active_spell_slot_amount", -1.0, EntityAttributeModifier.Operation.ADDITION));
                break;
            case "spell_slot_5":
                map.put(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT,
                        new EntityAttributeModifier(UUID.fromString(AttributeModifierUUIDs.SPELL_SLOT_5),
                                "active_spell_slot_amount", -1.0, EntityAttributeModifier.Operation.ADDITION));
                break;
            case "spell_slot_6":
                map.put(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT,
                        new EntityAttributeModifier(UUID.fromString(AttributeModifierUUIDs.SPELL_SLOT_6),
                                "active_spell_slot_amount", -1.0, EntityAttributeModifier.Operation.ADDITION));
                break;
            case "spell_slot_7":
                map.put(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT,
                        new EntityAttributeModifier(UUID.fromString(AttributeModifierUUIDs.SPELL_SLOT_7),
                                "active_spell_slot_amount", -1.0, EntityAttributeModifier.Operation.ADDITION));
                break;
            case "spell_slot_8":
                map.put(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT,
                        new EntityAttributeModifier(UUID.fromString(AttributeModifierUUIDs.SPELL_SLOT_8),
                                "active_spell_slot_amount", -1.0, EntityAttributeModifier.Operation.ADDITION));
                break;
        }
        return map;
    }

//    /**
//     * Returns the Entity Attribute Modifiers for a stack in a slot. Child implementations should
//     * remain pure
//     * <p>
//     * If modifiers do not change based on stack, slot, or entity, caching based on passed UUID
//     * should be considered
//     *
//     * @param uuid The UUID to use for creating attributes
//     */
//    @Override
//    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack,
//                                                                            SlotReference slot, LivingEntity entity, UUID uuid) {
//        Multimap<EntityAttribute, EntityAttributeModifier> map = Multimaps.newMultimap(Maps.newLinkedHashMap(), ArrayList::new);
//
//        if (stack.hasNbt() && stack.getNbt().contains("TrinketAttributeModifiers", 9)) {
//            NbtList list = stack.getNbt().getList("TrinketAttributeModifiers", 10);
//
//            for (int i = 0; i < list.size(); i++) {
//                NbtCompound tag = list.getCompound(i);
//
//                if (!tag.contains("Slot", NbtType.STRING) || tag.getString("Slot")
//                        .equals(slot.inventory().getSlotType().getGroup() + "/" + slot.inventory().getSlotType().getName())) {
//                    Optional<EntityAttribute> optional = Registries.ATTRIBUTE
//                            .getOrEmpty(Identifier.tryParse(tag.getString("AttributeName")));
//
//                    if (optional.isPresent()) {
//                        EntityAttributeModifier entityAttributeModifier = EntityAttributeModifier.fromNbt(tag);
//
//                        if (entityAttributeModifier != null
//                                && entityAttributeModifier.getId().getLeastSignificantBits() != 0L
//                                && entityAttributeModifier.getId().getMostSignificantBits() != 0L) {
//                            map.put(optional.get(), entityAttributeModifier);
//                        }
//                    }
//                }
//            }
//        }
//        return map;
//    }
}
