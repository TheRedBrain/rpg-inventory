package com.github.theredbrain.bamcore.api.item;

import com.github.theredbrain.bamcore.api.util.BetterAdventureModCoreAttributeModifierUUIDs;
import com.github.theredbrain.bamcore.api.util.BetterAdventureModeCoreEntityAttributes;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class ModifySpellSlotAmountRingItem extends TrinketItem {
    private final double spellSlotAmount;
    public ModifySpellSlotAmountRingItem(int spellSlotAmount, Settings settings) {
        super(settings);
        this.spellSlotAmount = spellSlotAmount;
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
                map.put(BetterAdventureModeCoreEntityAttributes.ACTIVE_SPELL_SLOT_AMOUNT,
                        new EntityAttributeModifier(UUID.fromString(BetterAdventureModCoreAttributeModifierUUIDs.RING_SLOT_1),
                                "active_spell_slot_amount", this.spellSlotAmount, EntityAttributeModifier.Operation.ADDITION));
                break;
            case "rings_2":
                map.put(BetterAdventureModeCoreEntityAttributes.ACTIVE_SPELL_SLOT_AMOUNT,
                        new EntityAttributeModifier(UUID.fromString(BetterAdventureModCoreAttributeModifierUUIDs.RING_SLOT_2),
                                "active_spell_slot_amount", this.spellSlotAmount, EntityAttributeModifier.Operation.ADDITION));
                break;
        }
        return map;
    }
}
