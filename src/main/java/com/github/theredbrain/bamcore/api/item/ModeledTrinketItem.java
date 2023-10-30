package com.github.theredbrain.bamcore.api.item;

import dev.emi.trinkets.api.TrinketItem;
import mod.azure.azurelibarmor.animatable.GeoItem;
import mod.azure.azurelibarmor.animatable.client.RenderProvider;
import mod.azure.azurelibarmor.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelibarmor.core.animation.AnimatableManager;
import mod.azure.azurelibarmor.renderer.GeoArmorRenderer;
import mod.azure.azurelibarmor.util.AzureLibUtil;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModeledTrinketItem extends TrinketItem/* implements GeoItem*/ {
    private final Identifier assetSubpath;
//    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
//    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    public ModeledTrinketItem(Identifier assetSubpath, /*double armor, double armorToughness, double weight, */Settings settings) {
        super(settings);
//        this.armor = armor;
//        this.armorToughness = armorToughness;
//        this.weight = weight;
        this.assetSubpath = assetSubpath;
    }

//    @Override
//    public void createRenderer(Consumer<Object> consumer) {
//        consumer.accept(new RenderProvider() {
//            private GeoArmorRenderer<?> renderer;
//            @Override
//            public BipedEntityModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, BipedEntityModel<LivingEntity> original) {
//                if (this.renderer == null) {
//                    this.renderer = new ModeledTrinketRenderer(ModeledTrinketItem.this.assetSubpath);
//                }
//                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
//                return this.renderer;
//            }
//        });
//    }
//
//    @Override
//    public Supplier<Object> getRenderProvider() {
//        return renderProvider;
//    }
//
//    @Override
//    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}
//
//    @Override
//    public AnimatableInstanceCache getAnimatableInstanceCache() {
//        return cache;
//    }
}
