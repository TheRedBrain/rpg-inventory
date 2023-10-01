package com.github.theredbrain.bamcore.api.item;

import com.github.theredbrain.bamcore.item.CustomArmour;
import com.github.theredbrain.bamcore.api.util.BetterAdventureModeEntityAttributes;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class CustomArmorItem extends ArmorItem implements CustomArmour {

    @Nullable
    private String translationKeyBroken;
    private final Multimap<EntityAttribute, EntityAttributeModifier> customAttributeModifiers;
//    private final int poise; // TODO poise
    private final double weight;

    /**
     * custom armor which can be equipped in two additional slots (shoulders and hands)
     * it also can not break, but is no longer functional when its durability is 1
     */
    public CustomArmorItem(/*int poise, TODO poise*/double weight, ArmorMaterial material, Type type, Item.Settings settings) {
        super(material, type, settings);
//        this.poise = poise; // TODO poise
        this.weight = weight;
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        UUID uUID = MODIFIERS.get((Object)type);
        builder.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(uUID, "Armor modifier", (double)this.getProtection(), EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, new EntityAttributeModifier(uUID, "Armor toughness", (double)this.getToughness(), EntityAttributeModifier.Operation.ADDITION));
        builder.put(BetterAdventureModeEntityAttributes.EQUIPMENT_WEIGHT, new EntityAttributeModifier(uUID, "Equipment weight", (double)this.weight, EntityAttributeModifier.Operation.ADDITION));
//        builder.put(EntityAttributesRegistry.MAX_POISE, new EntityAttributeModifier(uUID, "Poise", (double)this.poise, EntityAttributeModifier.Operation.ADDITION)); // TODO poise
//        if (material == ArmorMaterials.NETHERITE) {
//            builder.put(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, new EntityAttributeModifier(uUID, "Armor knockback resistance", (double)this.knockbackResistance, EntityAttributeModifier.Operation.ADDITION));
//        }
        this.customAttributeModifiers = builder.build();
    }

    /**
     * {@return whether this item should provide its attribute modifiers and if should be rendered}
     */
    @Override
    public boolean isProtecting(ItemStack stack) {
        return stack.getDamage() < stack.getMaxDamage() - 1;
    }

    /**
     * {@return whether this items can lose durability}
     */
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
        return ((CustomArmorItem)stack.getItem()).isProtecting(stack) ? this.getTranslationKey() : this.getOrCreateTranslationKeyBroken();
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot == this.type.getEquipmentSlot()) {
            return this.customAttributeModifiers;
        }
        return ImmutableMultimap.of();
    }

//    static {
////        MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"), UUID.fromString(AttributeModifierUUIDs.GLOVES_ARMOUR_ITEM_MODIFIER), UUID.fromString(AttributeModifierUUIDs.SHOULDERS_ARMOUR_ITEM_MODIFIER)};
//        MODIFIERS = (EnumMap)Util.make(new EnumMap(Type.class), (uuidMap) -> {
//            uuidMap.put(ArmorItem.Type.BOOTS, UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"));
//            uuidMap.put(ArmorItem.Type.LEGGINGS, UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"));
//            uuidMap.put(ArmorItem.Type.CHESTPLATE, UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"));
//            uuidMap.put(ArmorItem.Type.HELMET, UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"));
//            uuidMap.put(ExtendedArmorItemType.GLOVES, UUID.fromString(AttributeModifierUUIDs.GLOVES_ARMOUR_ITEM_MODIFIER));
//            uuidMap.put(ExtendedArmorItemType.SHOULDERS, UUID.fromString(AttributeModifierUUIDs.SHOULDERS_ARMOUR_ITEM_MODIFIER));
//        });
//    }
}
