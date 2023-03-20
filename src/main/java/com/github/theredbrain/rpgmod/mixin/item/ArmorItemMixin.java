package com.github.theredbrain.rpgmod.mixin.item;
//
//import com.github.theredbrain.rpgmod.item.DuckArmorItemMixin;
//import com.github.theredbrain.rpgmod.screen.AdventureInventoryScreenHandler;
//import com.github.theredbrain.rpgmod.util.AttributeModifierUUIDs;
//import com.mojang.authlib.GameProfile;
//import net.minecraft.entity.EquipmentSlot;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.ArmorItem;
//import net.minecraft.item.ArmorMaterial;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.collection.DefaultedList;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import org.objectweb.asm.Opcodes;
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Mutable;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.*;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//import java.util.UUID;
//
//@Mixin(ArmorItem.class)
//public abstract class ArmorItemMixin implements DuckArmorItemMixin {
//
//    @Shadow
//    @Final
//    @Mutable
//    private static UUID[] MODIFIERS;
//    // init
//
//    private static final UUID[] NEW_MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"), UUID.fromString(AttributeModifierUUIDs.GLOVES_ARMOUR_ITEM_MODIFIER), UUID.fromString(AttributeModifierUUIDs.SHOULDERS_ARMOUR_ITEM_MODIFIER)};
//
//    /**
//     * @author TheRedBrain
//     */
//    @Inject(method = "<init>", at = @At("TAIL"))
//    public void ArmorItem(ArmorMaterial material, EquipmentSlot slot, Item.Settings settings, CallbackInfo ci) {
////        this.inAdventureBuildingMode = false;
////        this.adventureInventoryScreenHandler = new AdventureInventoryScreenHandler(this.inventory, !world.isClient, (PlayerEntity) (Object) this);
////        this.syncedHandStacks = DefaultedList.ofSize(4, ItemStack.EMPTY);
////        this.syncedArmorStacks = DefaultedList.ofSize(10, ItemStack.EMPTY);
////        this.alternativeMainHandUsed = false;
////        this.alternativeOffHandUsed = false;
//        // inject into a constructor
//    }
////    @Redirect(method="<init>", at=@At(value="NEW",  target="Lnet/minecraft/item/ArmorItem;<init>(Lnet/minecraft/item/ArmorMaterial;Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/ArmorItem;"))
//
////    private UUID[] redirect(ArmorItem self) {
////        return new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"), UUID.fromString(AttributeModifierUUIDs.GLOVES_ARMOUR_ITEM_MODIFIER), UUID.fromString(AttributeModifierUUIDs.SHOULDERS_ARMOUR_ITEM_MODIFIER)};
////    }
////    @ModifyConstant(method="<init>", constant=@Constant(ArmorItem.MODIFIERS))
////    private static UUID newModifiers(UUID oldvalue, ArmorMaterial material, EquipmentSlot slot, Item.Settings settings) {
////        return NEW_MODIFIERS[slot.getEntitySlotId()];
////    }
//    @ModifyVariable(method="<init>", at=@At(value="INVOKE_ASSIGN", target="Lnet/minecraft/item/ArmorMaterial;M()V"))
//    private void init( int old ) {
//        return 42;
//    }
//
//    // armor items are like elytra in that they can't break completely
//    public boolean isProtecting(ItemStack stack) {
//        return stack.getDamage() < stack.getMaxDamage() - 1;
//    }
//
//    public boolean isDamageable() {
//        return ((ArmorItem) (Object) this).getMaxDamage() > 1;
//    }
//}
