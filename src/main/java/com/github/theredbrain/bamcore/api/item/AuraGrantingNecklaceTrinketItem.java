package com.github.theredbrain.bamcore.api.item;

import com.github.theredbrain.bamcore.api.effect.AuraStatusEffect;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class AuraGrantingNecklaceTrinketItem extends TrinketItem {
    private final AuraStatusEffect auraStatusEffect;
    public AuraGrantingNecklaceTrinketItem(AuraStatusEffect auraStatusEffect, Settings settings) {
        super(settings);
        this.auraStatusEffect = auraStatusEffect;
    }

    public AuraStatusEffect getAuraStatusEffect() {
        return auraStatusEffect;
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (entity.hasStatusEffect(this.auraStatusEffect)) {
            entity.removeStatusEffect(this.auraStatusEffect);
        }
    }
}
