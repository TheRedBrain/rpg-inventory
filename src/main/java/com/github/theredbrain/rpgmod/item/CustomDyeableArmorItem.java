package com.github.theredbrain.rpgmod.item;

import com.github.theredbrain.rpgmod.util.AttributeModifierUUIDs;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;

import java.util.UUID;

public class CustomDyeableArmorItem extends DyeableArmorItem implements CustomArmour {
    public CustomDyeableArmorItem(ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, Item.Settings settings) {
        super(armorMaterial, equipmentSlot, settings);
    }

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

//    public static boolean dispenseArmor(BlockPointer pointer, ItemStack armor) {
//        BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
//        List<Entity> list = pointer.getWorld().getEntitiesByClass(LivingEntity.class, new Box(blockPos), EntityPredicates.EXCEPT_SPECTATOR.and(new EntityPredicates.Equipable(armor)));
//        if (list.isEmpty()) {
//            return false;
//        }
//        LivingEntity livingEntity = (LivingEntity)list.get(0);
//        EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(armor);
//        ItemStack itemStack = armor.split(1);
//        livingEntity.equipStack(equipmentSlot, itemStack);
//        if (livingEntity instanceof MobEntity) {
//            ((MobEntity)livingEntity).setEquipmentDropChance(equipmentSlot, 2.0f);
//            ((MobEntity)livingEntity).setPersistent();
//        }
//        return true;
//    }

//    public EquipmentSlot getSlotType() {
//        return this.slot;
//    }
//
//    @Override
//    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//        ItemStack itemStack = user.getStackInHand(hand);
//        EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(itemStack);
//        ItemStack itemStack2 = user.getEquippedStack(equipmentSlot);
//        if (itemStack2.isEmpty()) {
//            user.equipStack(equipmentSlot, itemStack.copy());
//            if (!world.isClient()) {
//                user.incrementStat(Stats.USED.getOrCreateStat(this));
//            }
//            itemStack.setCount(0);
//            return TypedActionResult.success(itemStack, world.isClient());
//        }
//        return TypedActionResult.fail(itemStack);
//    }
//
//    @Override
//    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
//        if (slot == this.slot) {
//            return this.attributeModifiers;
//        }
//        return super.getAttributeModifiers(slot);
//    }
//
//    public int getProtection() {
//        return this.protection;
//    }
//
//    @Override
//    @Nullable
//    public SoundEvent getEquipSound() {
//        return this.equipSound;
//    }

    static {
        MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"), UUID.fromString(AttributeModifierUUIDs.GLOVES_ARMOUR_ITEM_MODIFIER), UUID.fromString(AttributeModifierUUIDs.SHOULDERS_ARMOUR_ITEM_MODIFIER)};
    }
}
