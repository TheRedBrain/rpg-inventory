package com.github.theredbrain.betteradventuremode.item;

import com.github.theredbrain.betteradventuremode.util.AttributeModifierUUIDs;
import com.github.theredbrain.betteradventuremode.util.ItemUtils;
import com.github.theredbrain.betteradventuremode.azurelib.BetterAdventureModeRenderProvider;
import com.github.theredbrain.betteradventuremode.client.render.renderer.ArmorTrinketRenderer;
import com.github.theredbrain.betteradventuremode.client.render.renderer.ModeledTrinketRenderer;
import com.github.theredbrain.betteradventuremode.registry.EntityAttributesRegistry;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;

public class ArmorTrinketItem extends ModeledTrinketItem {

    @Nullable
    private String translationKeyBroken;
    private final double armor;
    private final double armorToughness;
    private final double weight;

    public ArmorTrinketItem(double armor, double armorToughness, double weight, Identifier assetSubpath, Settings settings) {
        super(assetSubpath, settings);
        this.armor = armor;
        this.armorToughness = armorToughness;
        this.weight = weight;
    }

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
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        Multimap<EntityAttribute, EntityAttributeModifier> map = super.getModifiers(stack, slot, entity, uuid);
        String group = slot.inventory().getSlotType().getGroup();
        UUID slotUuid = group.equals("boots") ? UUID.fromString(AttributeModifierUUIDs.BOOTS_SLOT) : group.equals("leggings") ? UUID.fromString(AttributeModifierUUIDs.LEGGINGS_SLOT) : group.equals("gloves") ? UUID.fromString(AttributeModifierUUIDs.GLOVES_SLOT) : group.equals("chest_plates") ? UUID.fromString(AttributeModifierUUIDs.CHESTPLATE_SLOT) : group.equals("shoulders") ? UUID.fromString(AttributeModifierUUIDs.SHOULDERS_SLOT) : group.equals("helmets") ? UUID.fromString(AttributeModifierUUIDs.HELMET_SLOT) : null;

        if (slotUuid != null && ItemUtils.isUsable(stack)) {
            map.put(EntityAttributes.GENERIC_ARMOR,
                    new EntityAttributeModifier(slotUuid, "Armor", this.armor, EntityAttributeModifier.Operation.ADDITION));
            map.put(EntityAttributes.GENERIC_ARMOR_TOUGHNESS,
                    new EntityAttributeModifier(slotUuid, "Armor Toughness", this.armorToughness, EntityAttributeModifier.Operation.ADDITION));
            map.put(EntityAttributesRegistry.EQUIPMENT_WEIGHT,
                    new EntityAttributeModifier(slotUuid, "Equipment Weight", this.weight, EntityAttributeModifier.Operation.ADDITION));
        }
        return map;
    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new BetterAdventureModeRenderProvider() {
            private ModeledTrinketRenderer<?> renderer;
            @Override
            public BipedEntityModel<LivingEntity> getGenericTrinketModel(LivingEntity livingEntity, ItemStack itemStack, String slotGroup, String slotName, BipedEntityModel<LivingEntity> original, boolean slim) {
                if (this.renderer == null) {
                    this.renderer = new ArmorTrinketRenderer(ArmorTrinketItem.this.assetSubpath, slim);
                }
                this.renderer.prepForRender(livingEntity, itemStack, slotGroup, slotName, original);
                return this.renderer;
            }
        });
    }
}
