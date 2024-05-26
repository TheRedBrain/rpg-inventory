package com.github.theredbrain.rpginventory.item;

import com.github.theredbrain.rpginventory.azurelib.RPGInventoryRenderProvider;
import com.github.theredbrain.rpginventory.client.render.renderer.AccessoryTrinketRenderer;
import com.github.theredbrain.rpginventory.client.render.renderer.ModeledTrinketRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class AccessoryTrinketItem extends ModeledTrinketItem {

    public AccessoryTrinketItem(Identifier assetSubpath, Settings settings) {
        super(assetSubpath, settings);
    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RPGInventoryRenderProvider() {
            private ModeledTrinketRenderer<?> renderer;
            @Override
            public BipedEntityModel<LivingEntity> getGenericTrinketModel(LivingEntity livingEntity, ItemStack itemStack, String slotGroup, String slotName, BipedEntityModel<LivingEntity> original, boolean slim) {
                if (this.renderer == null) {
                    this.renderer = new AccessoryTrinketRenderer(AccessoryTrinketItem.this.assetSubpath, slim);
                }
                this.renderer.prepForRender(livingEntity, itemStack, slotGroup, slotName, original);
                return this.renderer;
            }
        });
    }
}
