package com.github.theredbrain.bamcore.mixin.client.render.entity.feature;
//
//import com.github.theredbrain.rpgmod.entity.ExtendedEquipmentSlot;
//import net.minecraft.client.render.VertexConsumerProvider;
//import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
//import net.minecraft.client.render.entity.model.BipedEntityModel;
//import net.minecraft.client.util.math.MatrixStack;
//import net.minecraft.entity.EquipmentSlot;
//import net.minecraft.entity.LivingEntity;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(ArmorFeatureRenderer.class)
//public abstract class ArmorFeatureRendererMixin<T extends LivingEntity, A extends BipedEntityModel<T>> {
//
//    @Shadow
//    private void renderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot armorSlot, int light, A model) {
//        throw new AssertionError();
//    }
//
//    @Shadow
//    private A getModel(EquipmentSlot slot) {
//        throw new AssertionError();
//    }
//
//    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at = @At("TAIL"))
//    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
//        this.renderArmor(matrixStack, vertexConsumerProvider, livingEntity, ExtendedEquipmentSlot.GLOVES, i, this.getModel(ExtendedEquipmentSlot.GLOVES));
//        this.renderArmor(matrixStack, vertexConsumerProvider, livingEntity, ExtendedEquipmentSlot.SHOULDERS, i, this.getModel(ExtendedEquipmentSlot.SHOULDERS));
////        this.renderArmor(matrixStack, vertexConsumerProvider, livingEntity, ExtendedEquipmentSlot.PLAYER_SKIN_ARMOR, i, this.getModel(ExtendedEquipmentSlot.PLAYER_SKIN_ARMOR));
//    }
//}
