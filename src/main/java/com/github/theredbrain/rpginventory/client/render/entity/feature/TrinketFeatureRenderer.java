package com.github.theredbrain.rpginventory.client.render.entity.feature;

import com.github.theredbrain.rpginventory.item.AbstractModelledTrinketItem;
import com.github.theredbrain.rpginventory.azurelib.RPGInventoryRenderProvider;
import com.google.common.collect.Maps;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.Optional;

@Environment(value = EnvType.CLIENT)
public class TrinketFeatureRenderer<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>>
        extends FeatureRenderer<T, M> {
    private static final Map<String, Identifier> TRINKET_TEXTURE_CACHE = Maps.newHashMap();
    private final A model;
    private final boolean slim;

    public TrinketFeatureRenderer(FeatureRendererContext<T, M> context, A model, boolean slim) {
        super(context);
        this.model = model;
        this.slim = slim;
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        this.renderTrinkets(matrixStack, vertexConsumerProvider, livingEntity, "gloves" , "gloves", this.model, this.slim, i);
        this.renderTrinkets(matrixStack, vertexConsumerProvider, livingEntity, "shoulders" , "shoulders", this.model, this.slim, i);
        this.renderTrinkets(matrixStack, vertexConsumerProvider, livingEntity, "rings_1" , "ring", this.model, this.slim, i);
        this.renderTrinkets(matrixStack, vertexConsumerProvider, livingEntity, "rings_2" , "ring", this.model, this.slim, i);
        this.renderTrinkets(matrixStack, vertexConsumerProvider, livingEntity, "necklaces" , "necklace", this.model, this.slim, i);
        this.renderTrinkets(matrixStack, vertexConsumerProvider, livingEntity, "belts" , "belt", this.model, this.slim, i);
    }

    private void renderTrinkets(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, String slotGroup, String slotName, A model, boolean slim, int light) {
        ItemStack itemStack = ItemStack.EMPTY;

        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(entity);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get(slotGroup) != null) {
                if (trinkets.get().getInventory().get(slotGroup).get(slotName) != null) {
                    itemStack = trinkets.get().getInventory().get(slotGroup).get(slotName).getStack(0);
                }
            }
        }
        Item item = itemStack.getItem();
        if (!(item instanceof AbstractModelledTrinketItem)) {
            return;
        }

        this.renderTrinket(matrices, vertexConsumers, entity, slotGroup, slotName, light, itemStack, model, slim, 1.0f, 1.0f, 1.0f);
    }

    private void renderTrinket(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, String slotGroup, String slotName, int light, ItemStack itemStack, A model, boolean slim, float red, float green, float blue) {
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getArmorCutoutNoCull(this.getTrinketTexture((AbstractModelledTrinketItem)itemStack.getItem(), slim)));
        RPGInventoryRenderProvider.of(itemStack).getGenericTrinketModel(entity, itemStack, slotGroup, slotName, (BipedEntityModel<LivingEntity>)model, slim).render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, 1.0f);
    }

    private Identifier getTrinketTexture(AbstractModelledTrinketItem abstractModelledTrinketItem, boolean slim) {
        String string = abstractModelledTrinketItem.assetSubpath.getNamespace() + ":textures/item/" + abstractModelledTrinketItem.assetSubpath.getPath() + (slim ? "_slim" : "") + ".png";
        return TRINKET_TEXTURE_CACHE.computeIfAbsent(string, Identifier::new);
    }
}
