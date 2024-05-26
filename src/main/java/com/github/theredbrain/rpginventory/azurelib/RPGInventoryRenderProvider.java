package com.github.theredbrain.rpginventory.azurelib;

//import mod.azure.azurelib.common.api.common.animatable.GeoItem;
//import mod.azure.azurelib.common.internal.client.RenderProvider;
import mod.azure.azurelib.animatable.GeoItem;
import mod.azure.azurelib.animatable.client.RenderProvider;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface RPGInventoryRenderProvider extends RenderProvider {
    RPGInventoryRenderProvider BETTER_ADVENTURE_MODE_DEFAULT = new RPGInventoryRenderProvider() {};

    static RPGInventoryRenderProvider of(ItemStack itemStack) {
        return of(itemStack.getItem());
    }

    static RPGInventoryRenderProvider of(Item item) {
        if(item instanceof GeoItem geoItem){
            return (RPGInventoryRenderProvider)geoItem.getRenderProvider().get();
        }

        return BETTER_ADVENTURE_MODE_DEFAULT;
    }

    default Model getGenericTrinketModel(LivingEntity livingEntity, ItemStack itemStack, String slotGroup, String slotName,  BipedEntityModel<LivingEntity> original, boolean slim) {
        BipedEntityModel<LivingEntity> replacement = getHumanoidTrinketModel(livingEntity, itemStack, slotGroup, slotName, original);

        if (replacement != original) {
            original.copyBipedStateTo(replacement);
            return replacement;
        }

        return original;
    }
    default BipedEntityModel<LivingEntity> getHumanoidTrinketModel(LivingEntity livingEntity, ItemStack itemStack, String slotGroup, String slotName, BipedEntityModel<LivingEntity> original) {
        return original;
    }
}
