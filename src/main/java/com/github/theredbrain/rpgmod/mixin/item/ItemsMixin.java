package com.github.theredbrain.rpgmod.mixin.item;

import com.github.theredbrain.rpgmod.RPGMod;
import com.github.theredbrain.rpgmod.item.CustomModeledVanillaArmorItem;
import com.github.theredbrain.rpgmod.item.CustomArmorMaterials;
import com.github.theredbrain.rpgmod.item.CustomDyeableArmorItem;
import com.github.theredbrain.rpgmod.registry.Tags;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Items.class)
public class ItemsMixin {

    @Redirect(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=turtle_helmet")),
            at = @At(value = "NEW",
                    target = "net/minecraft/item/ArmorItem",
                    ordinal = 0
            )
    )
    private static ArmorItem redirectedTurtleHelmet(ArmorMaterial material, EquipmentSlot equipmentSlot, Item.Settings settings) {
        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.TURTLE, EquipmentSlot.HEAD, Tags.TURTLE_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_turtle_armor"), new Item.Settings());
    }

    @Redirect(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=leather_helmet")),
            at = @At(value = "NEW",
                    target = "net/minecraft/item/DyeableArmorItem",
                    ordinal = 0
            )
    )
    private static DyeableArmorItem redirectedLeatherHelmet(ArmorMaterial material, EquipmentSlot equipmentSlot, Item.Settings settings) {
        return new CustomDyeableArmorItem(CustomArmorMaterials.LEATHER, EquipmentSlot.HEAD, new Item.Settings());
    }

    @Redirect(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=leather_chestplate")),
            at = @At(value = "NEW",
                    target = "net/minecraft/item/DyeableArmorItem",
                    ordinal = 0
            )
    )
    private static DyeableArmorItem redirectedLeatherChestplate(ArmorMaterial material, EquipmentSlot equipmentSlot, Item.Settings settings) {
        return new CustomDyeableArmorItem(CustomArmorMaterials.LEATHER, EquipmentSlot.CHEST, new Item.Settings());
    }

    @Redirect(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=leather_leggings")),
            at = @At(value = "NEW",
                    target = "net/minecraft/item/DyeableArmorItem",
                    ordinal = 0
            )
    )
    private static DyeableArmorItem redirectedLeatherLeggings(ArmorMaterial material, EquipmentSlot equipmentSlot, Item.Settings settings) {
        return new CustomDyeableArmorItem(CustomArmorMaterials.LEATHER, EquipmentSlot.LEGS, new Item.Settings());
    }

    @Redirect(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=leather_boots")),
            at = @At(value = "NEW",
                    target = "net/minecraft/item/DyeableArmorItem",
                    ordinal = 0
            )
    )
    private static DyeableArmorItem redirectedLeatherBoots(ArmorMaterial material, EquipmentSlot equipmentSlot, Item.Settings settings) {
        return new CustomDyeableArmorItem(CustomArmorMaterials.LEATHER, EquipmentSlot.FEET, new Item.Settings());
    }

    @Redirect(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=chainmail_helmet")),
            at = @At(value = "NEW",
                    target = "net/minecraft/item/ArmorItem",
                    ordinal = 0
            )
    )
    private static ArmorItem redirectedChainmailHelmet(ArmorMaterial material, EquipmentSlot equipmentSlot, Item.Settings settings) {
        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.CHAINMAIL, EquipmentSlot.HEAD, Tags.CHAINMAIL_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_chainmail_armor"), new Item.Settings());
    }

    @Redirect(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=chainmail_chestplate")),
            at = @At(value = "NEW",
                    target = "net/minecraft/item/ArmorItem",
                    ordinal = 0
            )
    )
    private static ArmorItem redirectedChainmailChestplate(ArmorMaterial material, EquipmentSlot equipmentSlot, Item.Settings settings) {
        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.CHAINMAIL, EquipmentSlot.CHEST, Tags.CHAINMAIL_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_chainmail_armor"), new Item.Settings());
    }

    @Redirect(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=chainmail_leggings")),
            at = @At(value = "NEW",
                    target = "net/minecraft/item/ArmorItem",
                    ordinal = 0
            )
    )
    private static ArmorItem redirectedChainmailLeggings(ArmorMaterial material, EquipmentSlot equipmentSlot, Item.Settings settings) {
        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.CHAINMAIL, EquipmentSlot.LEGS, Tags.CHAINMAIL_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_chainmail_armor"), new Item.Settings());
    }

    @Redirect(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=chainmail_boots")),
            at = @At(value = "NEW",
                    target = "net/minecraft/item/ArmorItem",
                    ordinal = 0
            )
    )
    private static ArmorItem redirectedChainmailBoots(ArmorMaterial material, EquipmentSlot equipmentSlot, Item.Settings settings) {
        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.CHAINMAIL, EquipmentSlot.FEET, Tags.CHAINMAIL_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_chainmail_armor"), new Item.Settings());
    }

    @Redirect(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=iron_helmet")),
            at = @At(value = "NEW",
                    target = "net/minecraft/item/ArmorItem",
                    ordinal = 0
            )
    )
    private static ArmorItem redirectedIronHelmet(ArmorMaterial material, EquipmentSlot equipmentSlot, Item.Settings settings) {
        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.IRON, EquipmentSlot.HEAD, Tags.IRON_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_iron_armor"), new Item.Settings());
    }

    @Redirect(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=iron_chestplate")),
            at = @At(value = "NEW",
                    target = "net/minecraft/item/ArmorItem",
                    ordinal = 0
            )
    )
    private static ArmorItem redirectedIronChestplate(ArmorMaterial material, EquipmentSlot equipmentSlot, Item.Settings settings) {
        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.IRON, EquipmentSlot.CHEST, Tags.IRON_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_iron_armor"), new Item.Settings());
    }

    @Redirect(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=iron_leggings")),
            at = @At(value = "NEW",
                    target = "net/minecraft/item/ArmorItem",
                    ordinal = 0
            )
    )
    private static ArmorItem redirectedIronLeggings(ArmorMaterial material, EquipmentSlot equipmentSlot, Item.Settings settings) {
        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.IRON, EquipmentSlot.LEGS, Tags.IRON_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_iron_armor"), new Item.Settings());
    }

    @Redirect(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=iron_boots")),
            at = @At(value = "NEW",
                    target = "net/minecraft/item/ArmorItem",
                    ordinal = 0
            )
    )
    private static ArmorItem redirectedIronBoots(ArmorMaterial material, EquipmentSlot equipmentSlot, Item.Settings settings) {
        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.IRON, EquipmentSlot.FEET, Tags.IRON_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_iron_armor"), new Item.Settings());
    }

    @Redirect(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=diamond_helmet")),
            at = @At(value = "NEW",
                    target = "net/minecraft/item/ArmorItem",
                    ordinal = 0
            )
    )
    private static ArmorItem redirectedDiamondHelmet(ArmorMaterial material, EquipmentSlot equipmentSlot, Item.Settings settings) {
        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.DIAMOND, EquipmentSlot.HEAD, Tags.DIAMOND_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_diamond_armor"), new Item.Settings());
    }

    @Redirect(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=diamond_chestplate")),
            at = @At(value = "NEW",
                    target = "net/minecraft/item/ArmorItem",
                    ordinal = 0
            )
    )
    private static ArmorItem redirectedDiamondChestplate(ArmorMaterial material, EquipmentSlot equipmentSlot, Item.Settings settings) {
        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.DIAMOND, EquipmentSlot.CHEST, Tags.DIAMOND_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_diamond_armor"), new Item.Settings());
    }

    @Redirect(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=diamond_leggings")),
            at = @At(value = "NEW",
                    target = "net/minecraft/item/ArmorItem",
                    ordinal = 0
            )
    )
    private static ArmorItem redirectedDiamondLeggings(ArmorMaterial material, EquipmentSlot equipmentSlot, Item.Settings settings) {
        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.DIAMOND, EquipmentSlot.LEGS, Tags.DIAMOND_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_diamond_armor"), new Item.Settings());
    }

    @Redirect(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=diamond_boots")),
            at = @At(value = "NEW",
                    target = "net/minecraft/item/ArmorItem",
                    ordinal = 0
            )
    )
    private static ArmorItem redirectedDiamondBoots(ArmorMaterial material, EquipmentSlot equipmentSlot, Item.Settings settings) {
        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.DIAMOND, EquipmentSlot.FEET, Tags.DIAMOND_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_diamond_armor"), new Item.Settings());
    }

    @Redirect(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=golden_helmet")),
            at = @At(value = "NEW",
                    target = "net/minecraft/item/ArmorItem",
                    ordinal = 0
            )
    )
    private static ArmorItem redirectedGoldHelmet(ArmorMaterial material, EquipmentSlot equipmentSlot, Item.Settings settings) {
        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.GOLD, EquipmentSlot.HEAD, Tags.GOLDEN_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_gold_armor"), new Item.Settings());
    }

    @Redirect(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=golden_chestplate")),
            at = @At(value = "NEW",
                    target = "net/minecraft/item/ArmorItem",
                    ordinal = 0
            )
    )
    private static ArmorItem redirectedGoldChestplate(ArmorMaterial material, EquipmentSlot equipmentSlot, Item.Settings settings) {
        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.GOLD, EquipmentSlot.CHEST, Tags.GOLDEN_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_gold_armor"), new Item.Settings());
    }

    @Redirect(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=golden_leggings")),
            at = @At(value = "NEW",
                    target = "net/minecraft/item/ArmorItem",
                    ordinal = 0
            )
    )
    private static ArmorItem redirectedGoldLeggings(ArmorMaterial material, EquipmentSlot equipmentSlot, Item.Settings settings) {
        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.GOLD, EquipmentSlot.LEGS, Tags.GOLDEN_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_gold_armor"), new Item.Settings());
    }

    @Redirect(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=golden_boots")),
            at = @At(value = "NEW",
                    target = "net/minecraft/item/ArmorItem",
                    ordinal = 0
            )
    )
    private static ArmorItem redirectedGoldBoots(ArmorMaterial material, EquipmentSlot equipmentSlot, Item.Settings settings) {
        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.GOLD, EquipmentSlot.FEET, Tags.GOLDEN_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_gold_armor"), new Item.Settings());
    }

    @Redirect(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=netherite_helmet")),
            at = @At(value = "NEW",
                    target = "net/minecraft/item/ArmorItem",
                    ordinal = 0
            )
    )
    private static ArmorItem redirectedNetheriteHelmet(ArmorMaterial material, EquipmentSlot equipmentSlot, Item.Settings settings) {
        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.NETHERITE, EquipmentSlot.HEAD, Tags.NETHERITE_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_netherite_armor"), new Item.Settings());
    }

    @Redirect(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=netherite_chestplate")),
            at = @At(value = "NEW",
                    target = "net/minecraft/item/ArmorItem",
                    ordinal = 0
            )
    )
    private static ArmorItem redirectedNetheriteChestplate(ArmorMaterial material, EquipmentSlot equipmentSlot, Item.Settings settings) {
        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.NETHERITE, EquipmentSlot.CHEST, Tags.NETHERITE_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_netherite_armor"), new Item.Settings());
    }

    @Redirect(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=netherite_leggings")),
            at = @At(value = "NEW",
                    target = "net/minecraft/item/ArmorItem",
                    ordinal = 0
            )
    )
    private static ArmorItem redirectedNetheriteLeggings(ArmorMaterial material, EquipmentSlot equipmentSlot, Item.Settings settings) {
        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.NETHERITE, EquipmentSlot.LEGS, Tags.NETHERITE_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_netherite_armor"), new Item.Settings());
    }

    @Redirect(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=netherite_boots")),
            at = @At(value = "NEW",
                    target = "net/minecraft/item/ArmorItem",
                    ordinal = 0
            )
    )
    private static ArmorItem redirectedNetheriteBoots(ArmorMaterial material, EquipmentSlot equipmentSlot, Item.Settings settings) {
        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.NETHERITE, EquipmentSlot.FEET, Tags.NETHERITE_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_netherite_armor"), new Item.Settings());
    }
}
