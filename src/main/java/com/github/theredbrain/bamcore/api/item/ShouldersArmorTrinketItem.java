package com.github.theredbrain.bamcore.api.item;

import com.github.theredbrain.bamcore.api.util.BetterAdventureModeCoreEntityAttributes;
import com.github.theredbrain.bamcore.api.util.BetterAdventureModCoreAttributeModifierUUIDs;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ShouldersArmorTrinketItem extends TrinketItem {

    @Nullable
    private String translationKeyBroken;
    private final double armor;
    private final double armorToughness;
    private final double weight;
    public ShouldersArmorTrinketItem(double armor, double armorToughness, double weight, Settings settings) {
        super(settings);
        this.armor = armor;
        this.armorToughness = armorToughness;
        this.weight = weight;
    }

    /**
     * {@return whether this item should provide its attribute modifiers and if should be rendered}
     */
    public boolean isProtecting(ItemStack stack) {
        return stack.getDamage() < stack.getMaxDamage() - 1;
    }

    @Override
    public boolean isDamageable() {
        return this.getMaxDamage() > 1;
    }

    /**
     * Gets or creates the translation key of this item when it is not protecting.
     */
    private String getOrCreateTranslationKeyBroken() {
        if (this.translationKeyBroken == null) {
            this.translationKeyBroken = Util.createTranslationKey("item", new Identifier(Registries.ITEM.getId(this).getNamespace() + ":" + Registries.ITEM.getId(this).getPath() + "_broken"));
        }
        return this.translationKeyBroken;
    }

    /**
     * Gets the translation key of this item using the provided item stack for context.
     */
    @Override
    public String getTranslationKey(ItemStack stack) {
        return ((ShouldersArmorTrinketItem)stack.getItem()).isProtecting(stack) ? this.getTranslationKey() : this.getOrCreateTranslationKeyBroken();
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        Multimap<EntityAttribute, EntityAttributeModifier> map = super.getModifiers(stack, slot, entity, uuid);
        if (slot.inventory().getSlotType().getGroup().equals("shoulders")) {
            if (this.isProtecting(stack)) {
                map.put(EntityAttributes.GENERIC_ARMOR,
                        new EntityAttributeModifier(UUID.fromString(BetterAdventureModCoreAttributeModifierUUIDs.SHOULDERS_SLOT),
                                "Armor", this.armor, EntityAttributeModifier.Operation.ADDITION));
                map.put(EntityAttributes.GENERIC_ARMOR_TOUGHNESS,
                        new EntityAttributeModifier(UUID.fromString(BetterAdventureModCoreAttributeModifierUUIDs.SHOULDERS_SLOT),
                                "Armor Toughness", this.armorToughness, EntityAttributeModifier.Operation.ADDITION));
                map.put(BetterAdventureModeCoreEntityAttributes.EQUIPMENT_WEIGHT,
                        new EntityAttributeModifier(UUID.fromString(BetterAdventureModCoreAttributeModifierUUIDs.SHOULDERS_SLOT),
                                "Equipment Weight", this.weight, EntityAttributeModifier.Operation.ADDITION));
            }
        }
        return map;
    }
}
