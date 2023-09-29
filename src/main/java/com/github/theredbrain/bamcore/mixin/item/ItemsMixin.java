package com.github.theredbrain.bamcore.mixin.item;
//
//import com.github.theredbrain.rpgmod.RPGMod;
//import com.github.theredbrain.rpgmod.item.CustomModeledVanillaArmorItem;
//import com.github.theredbrain.rpgmod.item.CustomArmorMaterials;
//import com.github.theredbrain.rpgmod.item.CustomDyeableArmorItem;
//import com.github.theredbrain.rpgmod.registry.Tags;
//import net.minecraft.item.*;
//import net.minecraft.util.Identifier;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Redirect;
//import org.spongepowered.asm.mixin.injection.Slice;
//
//@Mixin(Items.class)
//public class ItemsMixin {
//
//    @Redirect(method = "<clinit>",
//            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=turtle_helmet")),
//            at = @At(value = "NEW",
//                    target = "net/minecraft/item/ArmorItem",
//                    ordinal = 0
//            )
//    )
//    private static ArmorItem redirectedTurtleHelmet(ArmorMaterial material, ArmorItem.Type type, Item.Settings settings) {
//        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.TURTLE, ArmorItem.Type.HELMET, Tags.TURTLE_ARMOR_SET, RPGMod.identifier("armor/custom_turtle_armor"), new Item.Settings());
//    }
//
//    @Redirect(method = "<clinit>",
//            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=leather_helmet")),
//            at = @At(value = "NEW",
//                    target = "net/minecraft/item/DyeableArmorItem",
//                    ordinal = 0
//            )
//    )
//    private static DyeableArmorItem redirectedLeatherHelmet(ArmorMaterial material, ArmorItem.Type type, Item.Settings settings) {
//        return new CustomDyeableArmorItem(CustomArmorMaterials.LEATHER, ArmorItem.Type.HELMET, new Item.Settings());
//    }
//
//    @Redirect(method = "<clinit>",
//            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=leather_chestplate")),
//            at = @At(value = "NEW",
//                    target = "net/minecraft/item/DyeableArmorItem",
//                    ordinal = 0
//            )
//    )
//    private static DyeableArmorItem redirectedLeatherChestplate(ArmorMaterial material, ArmorItem.Type type, Item.Settings settings) {
//        return new CustomDyeableArmorItem(CustomArmorMaterials.LEATHER, ArmorItem.Type.CHESTPLATE, new Item.Settings());
//    }
//
//    @Redirect(method = "<clinit>",
//            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=leather_leggings")),
//            at = @At(value = "NEW",
//                    target = "net/minecraft/item/DyeableArmorItem",
//                    ordinal = 0
//            )
//    )
//    private static DyeableArmorItem redirectedLeatherLeggings(ArmorMaterial material, ArmorItem.Type type, Item.Settings settings) {
//        return new CustomDyeableArmorItem(CustomArmorMaterials.LEATHER, ArmorItem.Type.LEGGINGS, new Item.Settings());
//    }
//
//    @Redirect(method = "<clinit>",
//            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=leather_boots")),
//            at = @At(value = "NEW",
//                    target = "net/minecraft/item/DyeableArmorItem",
//                    ordinal = 0
//            )
//    )
//    private static DyeableArmorItem redirectedLeatherBoots(ArmorMaterial material, ArmorItem.Type type, Item.Settings settings) {
//        return new CustomDyeableArmorItem(CustomArmorMaterials.LEATHER, ArmorItem.Type.BOOTS, new Item.Settings());
//    }
//
//    @Redirect(method = "<clinit>",
//            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=chainmail_helmet")),
//            at = @At(value = "NEW",
//                    target = "net/minecraft/item/ArmorItem",
//                    ordinal = 0
//            )
//    )
//    private static ArmorItem redirectedChainmailHelmet(ArmorMaterial material, ArmorItem.Type type, Item.Settings settings) {
//        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.CHAINMAIL, ArmorItem.Type.HELMET, Tags.CHAINMAIL_ARMOR_SET, RPGMod.identifier("armor/custom_chainmail_armor"), new Item.Settings());
//    }
//
//    @Redirect(method = "<clinit>",
//            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=chainmail_chestplate")),
//            at = @At(value = "NEW",
//                    target = "net/minecraft/item/ArmorItem",
//                    ordinal = 0
//            )
//    )
//    private static ArmorItem redirectedChainmailChestplate(ArmorMaterial material, ArmorItem.Type type, Item.Settings settings) {
//        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.CHAINMAIL, ArmorItem.Type.CHESTPLATE, Tags.CHAINMAIL_ARMOR_SET, RPGMod.identifier("armor/custom_chainmail_armor"), new Item.Settings());
//    }
//
//    @Redirect(method = "<clinit>",
//            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=chainmail_leggings")),
//            at = @At(value = "NEW",
//                    target = "net/minecraft/item/ArmorItem",
//                    ordinal = 0
//            )
//    )
//    private static ArmorItem redirectedChainmailLeggings(ArmorMaterial material, ArmorItem.Type type, Item.Settings settings) {
//        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.CHAINMAIL, ArmorItem.Type.LEGGINGS, Tags.CHAINMAIL_ARMOR_SET, RPGMod.identifier("armor/custom_chainmail_armor"), new Item.Settings());
//    }
//
//    @Redirect(method = "<clinit>",
//            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=chainmail_boots")),
//            at = @At(value = "NEW",
//                    target = "net/minecraft/item/ArmorItem",
//                    ordinal = 0
//            )
//    )
//    private static ArmorItem redirectedChainmailBoots(ArmorMaterial material, ArmorItem.Type type, Item.Settings settings) {
//        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.CHAINMAIL, ArmorItem.Type.BOOTS, Tags.CHAINMAIL_ARMOR_SET, RPGMod.identifier("armor/custom_chainmail_armor"), new Item.Settings());
//    }
//
//    @Redirect(method = "<clinit>",
//            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=iron_helmet")),
//            at = @At(value = "NEW",
//                    target = "net/minecraft/item/ArmorItem",
//                    ordinal = 0
//            )
//    )
//    private static ArmorItem redirectedIronHelmet(ArmorMaterial material, ArmorItem.Type type, Item.Settings settings) {
//        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.IRON, ArmorItem.Type.HELMET, Tags.IRON_ARMOR_SET, RPGMod.identifier("armor/custom_iron_armor"), new Item.Settings());
//    }
//
//    @Redirect(method = "<clinit>",
//            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=iron_chestplate")),
//            at = @At(value = "NEW",
//                    target = "net/minecraft/item/ArmorItem",
//                    ordinal = 0
//            )
//    )
//    private static ArmorItem redirectedIronChestplate(ArmorMaterial material, ArmorItem.Type type, Item.Settings settings) {
//        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.IRON, ArmorItem.Type.CHESTPLATE, Tags.IRON_ARMOR_SET, RPGMod.identifier("armor/custom_iron_armor"), new Item.Settings());
//    }
//
//    @Redirect(method = "<clinit>",
//            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=iron_leggings")),
//            at = @At(value = "NEW",
//                    target = "net/minecraft/item/ArmorItem",
//                    ordinal = 0
//            )
//    )
//    private static ArmorItem redirectedIronLeggings(ArmorMaterial material, ArmorItem.Type type, Item.Settings settings) {
//        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.IRON, ArmorItem.Type.LEGGINGS, Tags.IRON_ARMOR_SET, RPGMod.identifier("armor/custom_iron_armor"), new Item.Settings());
//    }
//
//    @Redirect(method = "<clinit>",
//            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=iron_boots")),
//            at = @At(value = "NEW",
//                    target = "net/minecraft/item/ArmorItem",
//                    ordinal = 0
//            )
//    )
//    private static ArmorItem redirectedIronBoots(ArmorMaterial material, ArmorItem.Type type, Item.Settings settings) {
//        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.IRON, ArmorItem.Type.BOOTS, Tags.IRON_ARMOR_SET, RPGMod.identifier("armor/custom_iron_armor"), new Item.Settings());
//    }
//
//    @Redirect(method = "<clinit>",
//            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=diamond_helmet")),
//            at = @At(value = "NEW",
//                    target = "net/minecraft/item/ArmorItem",
//                    ordinal = 0
//            )
//    )
//    private static ArmorItem redirectedDiamondHelmet(ArmorMaterial material, ArmorItem.Type type, Item.Settings settings) {
//        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.DIAMOND, ArmorItem.Type.HELMET, Tags.DIAMOND_ARMOR_SET, RPGMod.identifier("armor/custom_diamond_armor"), new Item.Settings());
//    }
//
//    @Redirect(method = "<clinit>",
//            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=diamond_chestplate")),
//            at = @At(value = "NEW",
//                    target = "net/minecraft/item/ArmorItem",
//                    ordinal = 0
//            )
//    )
//    private static ArmorItem redirectedDiamondChestplate(ArmorMaterial material, ArmorItem.Type type, Item.Settings settings) {
//        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.DIAMOND, ArmorItem.Type.CHESTPLATE, Tags.DIAMOND_ARMOR_SET, RPGMod.identifier("armor/custom_diamond_armor"), new Item.Settings());
//    }
//
//    @Redirect(method = "<clinit>",
//            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=diamond_leggings")),
//            at = @At(value = "NEW",
//                    target = "net/minecraft/item/ArmorItem",
//                    ordinal = 0
//            )
//    )
//    private static ArmorItem redirectedDiamondLeggings(ArmorMaterial material, ArmorItem.Type type, Item.Settings settings) {
//        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.DIAMOND, ArmorItem.Type.LEGGINGS, Tags.DIAMOND_ARMOR_SET, RPGMod.identifier("armor/custom_diamond_armor"), new Item.Settings());
//    }
//
//    @Redirect(method = "<clinit>",
//            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=diamond_boots")),
//            at = @At(value = "NEW",
//                    target = "net/minecraft/item/ArmorItem",
//                    ordinal = 0
//            )
//    )
//    private static ArmorItem redirectedDiamondBoots(ArmorMaterial material, ArmorItem.Type type, Item.Settings settings) {
//        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.DIAMOND, ArmorItem.Type.BOOTS, Tags.DIAMOND_ARMOR_SET, RPGMod.identifier("armor/custom_diamond_armor"), new Item.Settings());
//    }
//
//    @Redirect(method = "<clinit>",
//            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=golden_helmet")),
//            at = @At(value = "NEW",
//                    target = "net/minecraft/item/ArmorItem",
//                    ordinal = 0
//            )
//    )
//    private static ArmorItem redirectedGoldHelmet(ArmorMaterial material, ArmorItem.Type type, Item.Settings settings) {
//        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.GOLD, ArmorItem.Type.HELMET, Tags.GOLDEN_ARMOR_SET, RPGMod.identifier("armor/custom_gold_armor"), new Item.Settings());
//    }
//
//    @Redirect(method = "<clinit>",
//            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=golden_chestplate")),
//            at = @At(value = "NEW",
//                    target = "net/minecraft/item/ArmorItem",
//                    ordinal = 0
//            )
//    )
//    private static ArmorItem redirectedGoldChestplate(ArmorMaterial material, ArmorItem.Type type, Item.Settings settings) {
//        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.GOLD, ArmorItem.Type.CHESTPLATE, Tags.GOLDEN_ARMOR_SET, RPGMod.identifier("armor/custom_gold_armor"), new Item.Settings());
//    }
//
//    @Redirect(method = "<clinit>",
//            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=golden_leggings")),
//            at = @At(value = "NEW",
//                    target = "net/minecraft/item/ArmorItem",
//                    ordinal = 0
//            )
//    )
//    private static ArmorItem redirectedGoldLeggings(ArmorMaterial material, ArmorItem.Type type, Item.Settings settings) {
//        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.GOLD, ArmorItem.Type.LEGGINGS, Tags.GOLDEN_ARMOR_SET, RPGMod.identifier("armor/custom_gold_armor"), new Item.Settings());
//    }
//
//    @Redirect(method = "<clinit>",
//            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=golden_boots")),
//            at = @At(value = "NEW",
//                    target = "net/minecraft/item/ArmorItem",
//                    ordinal = 0
//            )
//    )
//    private static ArmorItem redirectedGoldBoots(ArmorMaterial material, ArmorItem.Type type, Item.Settings settings) {
//        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.GOLD, ArmorItem.Type.BOOTS, Tags.GOLDEN_ARMOR_SET, RPGMod.identifier("armor/custom_gold_armor"), new Item.Settings());
//    }
//
//    @Redirect(method = "<clinit>",
//            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=netherite_helmet")),
//            at = @At(value = "NEW",
//                    target = "net/minecraft/item/ArmorItem",
//                    ordinal = 0
//            )
//    )
//    private static ArmorItem redirectedNetheriteHelmet(ArmorMaterial material, ArmorItem.Type type, Item.Settings settings) {
//        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.NETHERITE, ArmorItem.Type.HELMET, Tags.NETHERITE_ARMOR_SET, RPGMod.identifier("armor/custom_netherite_armor"), new Item.Settings());
//    }
//
//    @Redirect(method = "<clinit>",
//            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=netherite_chestplate")),
//            at = @At(value = "NEW",
//                    target = "net/minecraft/item/ArmorItem",
//                    ordinal = 0
//            )
//    )
//    private static ArmorItem redirectedNetheriteChestplate(ArmorMaterial material, ArmorItem.Type type, Item.Settings settings) {
//        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.NETHERITE, ArmorItem.Type.CHESTPLATE, Tags.NETHERITE_ARMOR_SET, RPGMod.identifier("armor/custom_netherite_armor"), new Item.Settings());
//    }
//
//    @Redirect(method = "<clinit>",
//            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=netherite_leggings")),
//            at = @At(value = "NEW",
//                    target = "net/minecraft/item/ArmorItem",
//                    ordinal = 0
//            )
//    )
//    private static ArmorItem redirectedNetheriteLeggings(ArmorMaterial material, ArmorItem.Type type, Item.Settings settings) {
//        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.NETHERITE, ArmorItem.Type.LEGGINGS, Tags.NETHERITE_ARMOR_SET, RPGMod.identifier("armor/custom_netherite_armor"), new Item.Settings());
//    }
//
//    @Redirect(method = "<clinit>",
//            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=netherite_boots")),
//            at = @At(value = "NEW",
//                    target = "net/minecraft/item/ArmorItem",
//                    ordinal = 0
//            )
//    )
//    private static ArmorItem redirectedNetheriteBoots(ArmorMaterial material, ArmorItem.Type type, Item.Settings settings) {
//        return new CustomModeledVanillaArmorItem(CustomArmorMaterials.NETHERITE, ArmorItem.Type.BOOTS, Tags.NETHERITE_ARMOR_SET, RPGMod.identifier("armor/custom_netherite_armor"), new Item.Settings());
//    }
//}
