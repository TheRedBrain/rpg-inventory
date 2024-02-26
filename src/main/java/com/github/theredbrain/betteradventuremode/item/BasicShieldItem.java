package com.github.theredbrain.betteradventuremode.item;

import com.github.theredbrain.betteradventuremode.util.AttributeModifierUUIDs;
import com.github.theredbrain.betteradventuremode.util.ItemUtils;
import com.github.theredbrain.betteradventuremode.entity.DuckLivingEntityMixin;
import com.github.theredbrain.betteradventuremode.registry.EntityAttributesRegistry;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BasicShieldItem extends Item implements Equipment, IMakesEquipSound {
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
    private final float physicalDamageReduction;
    private final float fireDamageReduction;
    private final float frostDamageReduction;
    private final float lightningDamageReduction;
    private final double blockForce;
    private final boolean canParry;
    private final double parryBonus;
    @Nullable
    private String translationKeyBroken;
    private final float weight;
    @Nullable
    private SoundEvent equipSound;
    public BasicShieldItem(float physicalDamageReduction, float fireDamageReduction, float frostDamageReduction, float lightningDamageReduction, double blockForce, boolean canParry, double parryBonus, float weight, @Nullable SoundEvent equipSound, Settings settings) {
        super(settings);
        this.physicalDamageReduction = physicalDamageReduction;
        this.fireDamageReduction = fireDamageReduction;
        this.frostDamageReduction = frostDamageReduction;
        this.lightningDamageReduction = lightningDamageReduction;
        this.blockForce = blockForce;
        this.canParry = canParry;
        this.parryBonus = parryBonus;
        this.weight = weight;
        this.equipSound = equipSound;
        this.attributeModifiers = buildModifiers();
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        if (((DuckLivingEntityMixin)user).betteradventuremode$getStamina() > 0) {
            return TypedActionResult.consume(itemStack);
        }
        return TypedActionResult.fail(itemStack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (remainingUseTicks < 0) {
            user.stopUsingItem();
            return;
        }
        if (((DuckLivingEntityMixin) user).betteradventuremode$getStamina() <= 0) {
            user.stopUsingItem();
        }
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
        return ItemUtils.isUsable(stack) ? this.getTranslationKey() : this.getOrCreateTranslationKeyBroken();
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.OFFHAND) {
            return this.attributeModifiers;
        }
        return super.getAttributeModifiers(slot);
    }

    protected Multimap<EntityAttribute, EntityAttributeModifier> buildModifiers() {
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributesRegistry.EQUIPMENT_WEIGHT, new EntityAttributeModifier(UUID.fromString(AttributeModifierUUIDs.OFF_HAND_SLOT), "equipment_weight", (double)this.weight, EntityAttributeModifier.Operation.ADDITION));
        return builder.build();
    }

    public boolean canParry() {
        return this.canParry;
    }

    public float getPhysicalDamageReduction() {
        return this.physicalDamageReduction;
    }

    public float getFireDamageReduction() {
        return fireDamageReduction;
    }

    public float getFrostDamageReduction() {
        return frostDamageReduction;
    }

    public float getLightningDamageReduction() {
        return lightningDamageReduction;
    }

    public double getParryBonus() {
        return this.parryBonus;
    }

    public double getBlockForce() {
        return this.blockForce;
    }

    @Override
    public EquipmentSlot getSlotType() {
        return EquipmentSlot.OFFHAND;
    }

    @Override
    public @Nullable SoundEvent getEquipSound() {
        return this.equipSound;
    }
}
