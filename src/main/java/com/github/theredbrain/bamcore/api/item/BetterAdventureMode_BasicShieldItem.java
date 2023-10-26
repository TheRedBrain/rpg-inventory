package com.github.theredbrain.bamcore.api.item;

import com.github.theredbrain.bamcore.api.util.BetterAdventureModCoreAttributeModifierUUIDs;
import com.github.theredbrain.bamcore.api.util.BetterAdventureModCoreItemUtils;
import com.github.theredbrain.bamcore.registry.EntityAttributesRegistry;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
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
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BetterAdventureMode_BasicShieldItem extends Item implements Equipment {
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
    private final float blockArmor;
    private final double blockForce;
    private final boolean canParry;
    private final double parryBonus;
    @Nullable
    private String translationKeyBroken;
    private final float weight;
    public BetterAdventureMode_BasicShieldItem(float blockArmor, double blockForce, boolean canParry, double parryBonus, float weight, Settings settings) {
        super(settings);
        this.blockArmor = blockArmor;
        this.blockForce = blockForce;
        this.canParry = canParry;
        this.parryBonus = parryBonus;
        this.weight = weight;
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
        if (((DuckPlayerEntityMixin)user).bamcore$getStamina() > 0) {
            return TypedActionResult.consume(itemStack);
        }
        return TypedActionResult.fail(itemStack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        PlayerEntity playerEntity;
        if (remainingUseTicks < 0 || !(user instanceof PlayerEntity)) {
            user.stopUsingItem();
            return;
        }
        playerEntity = (PlayerEntity)user;
        if (((DuckPlayerEntityMixin) playerEntity).bamcore$getStamina() <= 0) {
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
        return BetterAdventureModCoreItemUtils.isUsable(stack) ? this.getTranslationKey() : this.getOrCreateTranslationKeyBroken();
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
        builder.put(EntityAttributesRegistry.EQUIPMENT_WEIGHT, new EntityAttributeModifier(UUID.fromString(BetterAdventureModCoreAttributeModifierUUIDs.OFFHAND_EQUIPMENT_WEIGHT), "Shield modifier", (double)this.weight, EntityAttributeModifier.Operation.ADDITION));
        return builder.build();
    }

    public boolean canParry() {
        return this.canParry;
    }

    public float getBlockArmor() {
        return this.blockArmor;
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
}
