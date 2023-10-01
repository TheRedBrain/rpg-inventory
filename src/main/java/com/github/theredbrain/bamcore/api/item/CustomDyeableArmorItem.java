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

public class CustomDyeableArmorItem extends DyeableArmorItem implements CustomArmour {

    @Nullable
    private String translationKeyBroken;
    private final Multimap<EntityAttribute, EntityAttributeModifier> customAttributeModifiers;
    private final int poise;
    private final int weight;
    public CustomDyeableArmorItem(int poise, int weight, ArmorMaterial armorMaterial, Type type, Item.Settings settings) {
        super(armorMaterial, type, settings);
        this.poise = poise;
        this.weight = weight;
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        UUID uUID = MODIFIERS.get((Object)type);
        builder.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(uUID, "Armor modifier", (double)this.getProtection(), EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, new EntityAttributeModifier(uUID, "Armor toughness", (double)this.getToughness(), EntityAttributeModifier.Operation.ADDITION));
        builder.put(BetterAdventureModeEntityAttributes.EQUIPMENT_WEIGHT, new EntityAttributeModifier(uUID, "Equipment weight", (double)this.weight, EntityAttributeModifier.Operation.ADDITION));
        builder.put(BetterAdventureModeEntityAttributes.MAX_POISE, new EntityAttributeModifier(uUID, "Poise", (double)this.poise, EntityAttributeModifier.Operation.ADDITION));
//        if (material == ArmorMaterials.NETHERITE) {
//            builder.put(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, new EntityAttributeModifier(uUID, "Armor knockback resistance", (double)this.knockbackResistance, EntityAttributeModifier.Operation.ADDITION));
//        }
        this.customAttributeModifiers = builder.build();
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
        return ((CustomDyeableArmorItem)stack.getItem()).isProtecting(stack) ? this.getTranslationKey() : this.getOrCreateTranslationKeyBroken();
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot == this.type.getEquipmentSlot()) {
            return this.customAttributeModifiers;
        }
        return ImmutableMultimap.of();
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

//    static {
//        MODIFIERS = (EnumMap) Util.make(new EnumMap(Type.class), (uuidMap) -> {
//            uuidMap.put(ArmorItem.Type.BOOTS, UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"));
//            uuidMap.put(ArmorItem.Type.LEGGINGS, UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"));
//            uuidMap.put(ArmorItem.Type.CHESTPLATE, UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"));
//            uuidMap.put(ArmorItem.Type.HELMET, UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"));
//            uuidMap.put(ArmorItem.Type.HELMET, UUID.fromString(AttributeModifierUUIDs.GLOVES_ARMOUR_ITEM_MODIFIER));
//            uuidMap.put(ArmorItem.Type.HELMET, UUID.fromString(AttributeModifierUUIDs.SHOULDERS_ARMOUR_ITEM_MODIFIER));
//        });
//    }
}
