package com.github.theredbrain.rpgmod.item;

import com.github.theredbrain.rpgmod.util.AttributeModifierUUIDs;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class CustomArmorItem extends ArmorItem implements CustomArmour {

    @Nullable
    private String translationKeyBroken;

    /**
     * custom armor which can be equipped in two additional slots (shoulders and hands)
     * it also can not break, but is no longer functional when its durability is 1
     */
    public CustomArmorItem(ArmorMaterial material, EquipmentSlot slot, Item.Settings settings) {
        super(material, slot, settings);
    }

    /**
     * {@return whether this item is providing its attribute modifiers and if it's rendered}
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

    static {
        MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"), UUID.fromString(AttributeModifierUUIDs.GLOVES_ARMOUR_ITEM_MODIFIER), UUID.fromString(AttributeModifierUUIDs.SHOULDERS_ARMOUR_ITEM_MODIFIER)};
    }
}
