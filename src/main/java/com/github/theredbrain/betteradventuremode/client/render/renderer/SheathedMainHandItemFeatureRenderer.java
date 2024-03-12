package com.github.theredbrain.betteradventuremode.client.render.renderer;

import com.github.theredbrain.betteradventuremode.item.BasicWeaponItem;
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
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class SheathedMainHandItemFeatureRenderer<T extends LivingEntity> extends HeldItemFeatureRenderer<T, PlayerEntityModel<T>> {

    private final HeldItemRenderer heldItemRenderer;
    public SheathedMainHandItemFeatureRenderer(FeatureRendererContext<T, PlayerEntityModel<T>> context, HeldItemRenderer heldItemRenderer) {
        super(context, heldItemRenderer);
        this.heldItemRenderer = heldItemRenderer;
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {

        if (livingEntity instanceof IRenderEquippedTrinkets renderEquippedTrinkets) {

            ItemStack mainHandStack = renderEquippedTrinkets.betteradventuremode$getSheathedMainHandItemStack();

            if (!mainHandStack.isEmpty()) {
                matrixStack.push();
                ModelPart modelPart = this.getContextModel().body;
                modelPart.rotate(matrixStack);
                Item mainHandStackItem = mainHandStack.getItem();
                boolean hasStackedEquippedInChestSlot = livingEntity.hasStackEquipped(EquipmentSlot.CHEST);

                if (this.getContextModel().child) {
                    float m = 0.5F;
                    matrixStack.translate(0.0F, 0.75F, 0.0F);
                    matrixStack.scale(0.5F, 0.5F, 0.5F);
                }
                if (mainHandStackItem instanceof SwordItem || mainHandStackItem instanceof BasicWeaponItem) {
                    matrixStack.translate(-0.3D, 0.05D, 0.16D);
                    if (hasStackedEquippedInChestSlot) {
//                    matrixStack.translate(0.05F, 0.0F, 0.0F);
                        matrixStack.translate(0.0D, 0.0D, 0.06D);
                    }
                    matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
                    matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(35.0F));
//                matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-25.0F));
////                matrixStack.translate(0.0D, 0.0D, 0.16D);
//                matrixStack.scale(1.0F, -1.0F, -1.0F);
                    heldItemRenderer.renderItem(livingEntity, mainHandStack, ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, false, matrixStack, vertexConsumerProvider, i);
//            } else if (mainHandStackItem instanceof TridentItem) {
//                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(52.0F));
//                matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(40.0F));
//                matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-25.0F));
//                matrixStack.translate(-0.26D, 0.0D, 0.0D);
//                if (!livingEntity.hasStackEquipped(EquipmentSlot.CHEST)) {
//                    matrixStack.translate(0.05F, 0.0F, 0.0F);
//                }
//                matrixStack.scale(1.0F, -1.0F, -1.0F);
//                heldItemRenderer.renderItem(livingEntity, mainHandStack, ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, false, matrixStack, vertexConsumerProvider, i);
                } else if (mainHandStackItem instanceof CrossbowItem) {
                    matrixStack.translate(-0.3D, 0.1D, 0.16D);
//                matrixStack.translate(-0.3D, 0.0D, 0.16D);
                    if (hasStackedEquippedInChestSlot) {
//                    matrixStack.translate(0.05F, 0.0F, 0.0F);
                        matrixStack.translate(0.0D, 0.0D, 0.06D);
                    }
                    matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
                    matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-10.0F));
//                matrixStack.translate(0.0D, 0.0D, 0.16D);
////                matrixStack.scale(BackSlotMain.CONFIG.backslot_scaling, BackSlotMain.CONFIG.backslot_scaling, BackSlotMain.CONFIG.backslot_scaling);
////                if (backSlotStack.getItem() instanceof FishingRodItem || backSlotStack.getItem() instanceof OnAStickItem) {
////                    matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
////                    matrixStack.translate(0.0D, -0.3D, 0.0D);
////                }
//                if (livingEntity.hasStackEquipped(EquipmentSlot.CHEST)) {
//                    matrixStack.translate(0.0F, 0.0F, 0.06F);
//                }
                    heldItemRenderer.renderItem(livingEntity, mainHandStack, ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, false, matrixStack, vertexConsumerProvider, i);
                } else { // TODO bow
                    matrixStack.translate(-0.1D, 0.4D, 0.09D);
                    if (hasStackedEquippedInChestSlot) {
//                    matrixStack.translate(0.05F, 0.0F, 0.0F);
                        matrixStack.translate(0.0D, 0.0D, 0.06D);
                    }
                    matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
                    matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-12.0F));
                    matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(35.0F));
//                matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-25.0F));
////                matrixStack.translate(0.0D, 0.0D, 0.16D);
//                matrixStack.scale(1.0F, -1.0F, -1.0F);
                    heldItemRenderer.renderItem(livingEntity, mainHandStack, ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, false, matrixStack, vertexConsumerProvider, i);
                }

                matrixStack.pop();
            }
        }
    }
}
