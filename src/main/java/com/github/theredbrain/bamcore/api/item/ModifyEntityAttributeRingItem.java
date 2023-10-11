package com.github.theredbrain.bamcore.api.item;

import com.github.theredbrain.bamcore.api.util.BetterAdventureModCoreAttributeModifierUUIDs;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class ModifyEntityAttributeRingItem extends TrinketItem {
    private final EntityAttribute attribute;
    private final String modifierName;
    private final double amount;
    private final EntityAttributeModifier.Operation operation;
    public ModifyEntityAttributeRingItem(EntityAttribute attribute, String modifierName, double amount, EntityAttributeModifier.Operation operation, Settings settings) {
        super(settings);
        this.attribute = attribute;
        this.modifierName = modifierName;
        this.amount = amount;
        this.operation = operation;
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
                map.put(this.attribute,
                        new EntityAttributeModifier(UUID.fromString(BetterAdventureModCoreAttributeModifierUUIDs.RING_SLOT_1),
                                this.modifierName, this.amount, this.operation));
                break;
            case "rings_2":
                map.put(this.attribute,
                        new EntityAttributeModifier(UUID.fromString(BetterAdventureModCoreAttributeModifierUUIDs.RING_SLOT_2),
                                this.modifierName, this.amount, this.operation));
                break;
        }
        return map;
    }
}
