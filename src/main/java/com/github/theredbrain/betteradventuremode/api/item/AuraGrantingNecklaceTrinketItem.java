package com.github.theredbrain.betteradventuremode.api.item;

import com.github.theredbrain.betteradventuremode.api.effect.AuraStatusEffect;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class AuraGrantingNecklaceTrinketItem extends AccessoryTrinketItem {
    private final AuraStatusEffect auraStatusEffect;
    public AuraGrantingNecklaceTrinketItem(AuraStatusEffect auraStatusEffect, Identifier assetSubpath, Settings settings) {
        super(assetSubpath, settings);
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
