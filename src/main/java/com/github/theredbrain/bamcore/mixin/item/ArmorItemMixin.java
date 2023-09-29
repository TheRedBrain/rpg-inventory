package com.github.theredbrain.bamcore.mixin.item;

//import com.github.theredbrain.rpgmod.item.ExtendedArmorItemType;
//import com.github.theredbrain.rpgmod.util.AttributeModifierUUIDs;
//import net.minecraft.item.ArmorItem;
//import net.minecraft.util.Util;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//
//import java.util.EnumMap;
//import java.util.UUID;
//
//@Mixin(ArmorItem.class)
//public abstract class ArmorItemMixin {
//
////    @Shadow
////    public static EnumMap<ArmorItem.Type, UUID> MODIFIERS = (EnumMap)Util.make(new EnumMap(ArmorItem.Type.class), (uuidMap) -> {
////        uuidMap.put(ArmorItem.Type.BOOTS, UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"));
////        uuidMap.put(ArmorItem.Type.LEGGINGS, UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"));
////        uuidMap.put(ArmorItem.Type.CHESTPLATE, UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"));
////        uuidMap.put(ArmorItem.Type.HELMET, UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"));
////        uuidMap.put(ExtendedArmorItemType.GLOVES, UUID.fromString(AttributeModifierUUIDs.GLOVES_ARMOUR_ITEM_MODIFIER));
////        uuidMap.put(ExtendedArmorItemType.SHOULDERS, UUID.fromString(AttributeModifierUUIDs.SHOULDERS_ARMOUR_ITEM_MODIFIER));
////    });
//    // init
//
////    public static EnumMap<ArmorItem.Type, UUID> MODIFIERS = Util.make(new EnumMap(ArmorItem.Type.class), uuidMap -> {
////        uuidMap.put(ArmorItem.Type.BOOTS, UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"));
////        uuidMap.put(ArmorItem.Type.LEGGINGS, UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"));
////        uuidMap.put(ArmorItem.Type.CHESTPLATE, UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"));
////        uuidMap.put(ArmorItem.Type.HELMET, UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"));
////    });
////    private static final UUID[] MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"), UUID.fromString(AttributeModifierUUIDs.GLOVES_ARMOUR_ITEM_MODIFIER), UUID.fromString(AttributeModifierUUIDs.SHOULDERS_ARMOUR_ITEM_MODIFIER)};
//
////    /**
////     * @author TheRedBrain
////     */
////    @Inject(method = "<init>", at = @At("TAIL"))
////    private static void ArmorItem(CallbackInfo ci) {
//////        this.inAdventureBuildingMode = false;
//////        this.adventureInventoryScreenHandler = new AdventureInventoryScreenHandler(this.inventory, !world.isClient, (PlayerEntity) (Object) this);
//////        this.syncedHandStacks = DefaultedList.ofSize(4, ItemStack.EMPTY);
//////        this.syncedArmorStacks = DefaultedList.ofSize(10, ItemStack.EMPTY);
//////        this.alternativeMainHandUsed = false;
//////        this.alternativeOffHandUsed = false;
////        // inject into a constructor
//////        MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"), UUID.fromString(AttributeModifierUUIDs.GLOVES_ARMOUR_ITEM_MODIFIER), UUID.fromString(AttributeModifierUUIDs.SHOULDERS_ARMOUR_ITEM_MODIFIER)};
////        MODIFIERS = (EnumMap)Util.make(new EnumMap(ArmorItem.Type.class), (uuidMap) -> {
////            uuidMap.put(ArmorItem.Type.BOOTS, UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"));
////            uuidMap.put(ArmorItem.Type.LEGGINGS, UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"));
////            uuidMap.put(ArmorItem.Type.CHESTPLATE, UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"));
////            uuidMap.put(ArmorItem.Type.HELMET, UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"));
////            uuidMap.put(ExtendedArmorItemType.GLOVES, UUID.fromString(AttributeModifierUUIDs.GLOVES_ARMOUR_ITEM_MODIFIER));
////            uuidMap.put(ExtendedArmorItemType.SHOULDERS, UUID.fromString(AttributeModifierUUIDs.SHOULDERS_ARMOUR_ITEM_MODIFIER));
////        });
////    }
////    @Redirect(method="<init>", at=@At(value="NEW",  target="Lnet/minecraft/item/ArmorItem;<init>(Lnet/minecraft/item/ArmorMaterial;Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/ArmorItem;"))
//
////    private UUID[] redirect(ArmorItem self) {
////        return new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"), UUID.fromString(AttributeModifierUUIDs.GLOVES_ARMOUR_ITEM_MODIFIER), UUID.fromString(AttributeModifierUUIDs.SHOULDERS_ARMOUR_ITEM_MODIFIER)};
////    }
////    @ModifyConstant(method="<init>", constant=@Constant(ArmorItem.MODIFIERS))
////    private static UUID newModifiers(UUID oldvalue, ArmorMaterial material, EquipmentSlot slot, Item.Settings settings) {
////        return NEW_MODIFIERS[slot.getEntitySlotId()];
////    }
////    @ModifyVariable(method="<init>", at=@At(value="INVOKE_ASSIGN", target="Lnet/minecraft/item/ArmorMaterial;M()V"))
////    private void init( int old ) {
////        return 42;
////    }
////
////    // armor items are like elytra in that they can't break completely
////    public boolean isProtecting(ItemStack stack) {
////        return stack.getDamage() < stack.getMaxDamage() - 1;
////    }
////
////    public boolean isDamageable() {
////        return ((ArmorItem) (Object) this).getMaxDamage() > 1;
////    }
//}
