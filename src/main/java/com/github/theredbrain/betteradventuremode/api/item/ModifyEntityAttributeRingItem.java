package com.github.theredbrain.betteradventuremode.api.item;

import com.github.theredbrain.betteradventuremode.api.util.AttributeModifierUUIDs;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class ModifyEntityAttributeRingItem extends AccessoryTrinketItem {
    private final EntityAttribute attribute;
    private final String modifierName;
    private final double amount;
    private final EntityAttributeModifier.Operation operation;
    public ModifyEntityAttributeRingItem(EntityAttribute attribute, String modifierName, double amount, EntityAttributeModifier.Operation operation, Identifier assetSubpath, Settings settings) {
        super(assetSubpath, settings);
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
                        new EntityAttributeModifier(UUID.fromString(AttributeModifierUUIDs.RING_SLOT_1),
                                this.modifierName, this.amount, this.operation));
                break;
            case "rings_2":
                map.put(this.attribute,
                        new EntityAttributeModifier(UUID.fromString(AttributeModifierUUIDs.RING_SLOT_2),
                                this.modifierName, this.amount, this.operation));
                break;
        }
        return map;
    }
}
