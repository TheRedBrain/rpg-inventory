package com.github.theredbrain.betteradventuremode.client.render.renderer;

import com.github.theredbrain.betteradventuremode.entity.IRenderEquippedTrinkets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class SheathedOffHandItemFeatureRenderer<T extends LivingEntity> extends HeldItemFeatureRenderer<T, PlayerEntityModel<T>> {

    private final HeldItemRenderer heldItemRenderer;
    public SheathedOffHandItemFeatureRenderer(FeatureRendererContext<T, PlayerEntityModel<T>> context, HeldItemRenderer heldItemRenderer) {
        super(context, heldItemRenderer);
        this.heldItemRenderer = heldItemRenderer;
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {

        if (livingEntity instanceof IRenderEquippedTrinkets renderEquippedTrinkets) {

            ItemStack offHandStack = renderEquippedTrinkets.betteradventuremode$getSheathedOffHandItemStack();

            if (!offHandStack.isEmpty()) {
                matrixStack.push();
                ModelPart modelPart = this.getContextModel().body;
                modelPart.rotate(matrixStack);
                Item offHandItem = offHandStack.getItem();
                boolean hasStackedEquippedInChestSlot = livingEntity.hasStackEquipped(EquipmentSlot.CHEST);

                if (this.getContextModel().child) {
                    float m = 0.5F;
                    matrixStack.translate(0.0F, 0.75F, 0.0F);
                    matrixStack.scale(0.5F, 0.5F, 0.5F);
                }
                if (offHandItem instanceof SwordItem) {
                    matrixStack.translate(0.2D, 0.0D, 0.15D);
                    if (hasStackedEquippedInChestSlot) {
                        //                    matrixStack.translate(0.05F, 0.0F, 0.0F);
                        matrixStack.translate(0.0D, 0.0D, 0.06D);
                    }
                    matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
                    matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-15.0F));
//                matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0F));
//                if (!livingEntity.hasStackEquipped(EquipmentSlot.CHEST)) {
//                    matrixStack.translate(0.05F, 0.0F, 0.0F);
//                }
//                matrixStack.scale(1.0F, -1.0F, -1.0F);
                    heldItemRenderer.renderItem(livingEntity, offHandStack, ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, false, matrixStack, vertexConsumerProvider, i);
                } else if (offHandItem instanceof ShieldItem) {
                    matrixStack.translate(0.2D, 0.4D, 0.0D);
                    if (hasStackedEquippedInChestSlot) {
                        //                    matrixStack.translate(0.05F, 0.0F, 0.0F);
                        matrixStack.translate(0.0D, 0.0D, 0.06D);
                    }
                    matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90.0F));
                    matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(15.0F));
//                matrixStack.translate(0.0D, 0.0D, 0.16D);
////                matrixStack.scale(BackSlotMain.CONFIG.backslot_scaling, BackSlotMain.CONFIG.backslot_scaling, BackSlotMain.CONFIG.backslot_scaling);
////                if (backSlotStack.getItem() instanceof FishingRodItem || backSlotStack.getItem() instanceof OnAStickItem) {
////                    matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
////                    matrixStack.translate(0.0D, -0.3D, 0.0D);
////                }
//                if (livingEntity.hasStackEquipped(EquipmentSlot.CHEST)) {
//                    matrixStack.translate(0.0F, 0.0F, 0.06F);
//                }
                    heldItemRenderer.renderItem(livingEntity, offHandStack, ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, false, matrixStack, vertexConsumerProvider, i);
                }

                matrixStack.pop();
            }
        }
    }
}
