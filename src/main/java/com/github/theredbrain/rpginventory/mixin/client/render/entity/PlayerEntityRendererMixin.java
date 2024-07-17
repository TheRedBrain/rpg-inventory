package com.github.theredbrain.rpginventory.mixin.client.render.entity;

import com.github.theredbrain.rpginventory.client.render.renderer.SheathedMainHandItemFeatureRenderer;
import com.github.theredbrain.rpginventory.client.render.renderer.SheathedOffHandItemFeatureRenderer;
import com.github.theredbrain.rpginventory.registry.Tags;
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

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {


	public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
		super(ctx, model, shadowRadius);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void rpginventory$init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo info) {
		this.addFeature(new SheathedMainHandItemFeatureRenderer<>(this, ctx.getHeldItemRenderer()));
		this.addFeature(new SheathedOffHandItemFeatureRenderer<>(this, ctx.getHeldItemRenderer()));
	}

	/**
	 * @author TheRedBrain
	 */
	@Inject(method = "getArmPose", at = @At("HEAD"), cancellable = true)
	private static void rpginventory$pre_getArmPose(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
		ItemStack itemStack = player.getStackInHand(hand);
		if (itemStack.isEmpty() || itemStack.isIn(Tags.EMPTY_HAND_WEAPONS)) {
			cir.setReturnValue(BipedEntityModel.ArmPose.EMPTY);
			cir.cancel();
		}
	}
}
