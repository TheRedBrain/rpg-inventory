package com.github.theredbrain.betteradventuremode.mixin.client.render.entity;

import com.github.theredbrain.betteradventuremode.client.render.entity.feature.TrinketFeatureRenderer;
import com.github.theredbrain.betteradventuremode.client.render.renderer.SheathedMainHandItemFeatureRenderer;
import com.github.theredbrain.betteradventuremode.client.render.renderer.SheathedOffHandItemFeatureRenderer;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.registry.Tags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {


    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void betteradventuremode$init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo info) {
        this.addFeature(new TrinketFeatureRenderer<>(this, this.model, slim));
        this.addFeature(new SheathedMainHandItemFeatureRenderer<>(this, ctx.getHeldItemRenderer()));
        this.addFeature(new SheathedOffHandItemFeatureRenderer<>(this, ctx.getHeldItemRenderer()));
    } 

    /**
     * @author TheRedBrain
     */
    @Inject(method = "getArmPose", at = @At("HEAD"), cancellable = true)
    private static void betteradventuremode$pre_getArmPose(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isEmpty() || itemStack.isIn(Tags.EMPTY_HAND_WEAPONS)) {
            cir.setReturnValue(BipedEntityModel.ArmPose.EMPTY);
            cir.cancel();
        }
        if (!player.handSwinging && !((DuckPlayerEntityMixin)player).betteradventuremode$isMainHandStackSheathed() && ((DuckPlayerEntityMixin)player).betteradventuremode$isOffHandStackSheathed()) {
            cir.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_HOLD);
            cir.cancel();
        }
    }
}
