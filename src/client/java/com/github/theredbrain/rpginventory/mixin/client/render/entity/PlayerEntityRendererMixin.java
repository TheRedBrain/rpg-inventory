package com.github.theredbrain.rpginventory.mixin.client.render.entity;

import com.github.theredbrain.rpginventory.registry.Tags;
import com.github.theredbrain.rpginventory.render.renderer.SheathedHandItemFeatureRenderer;
import com.github.theredbrain.rpginventory.render.renderer.SheathedOffHandItemFeatureRenderer;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(PlayerRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {


	public PlayerEntityRendererMixin(EntityRendererProvider.Context ctx, PlayerModel<AbstractClientPlayer> model, float shadowRadius) {
		super(ctx, model, shadowRadius);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void rpginventory$init(EntityRendererProvider.Context ctx, boolean slim, CallbackInfo info) {
		this.addLayer(new SheathedHandItemFeatureRenderer<>(this, ctx.getItemInHandRenderer()));
		this.addLayer(new SheathedOffHandItemFeatureRenderer<>(this, ctx.getItemInHandRenderer()));
	}

	/**
	 * @author TheRedBrain
	 */
	@Inject(method = "getArmPose", at = @At("HEAD"), cancellable = true)
	private static void rpginventory$pre_getArmPose(AbstractClientPlayer player, InteractionHand hand, CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {
		ItemStack itemStack = player.getItemInHand(hand);
		if (itemStack.isEmpty() || itemStack.is(Tags.EMPTY_HAND_WEAPONS) || !ItemUtils.isUsable(itemStack) || !ItemUtils.isUsableByPlayer(itemStack, player)) {
			cir.setReturnValue(HumanoidModel.ArmPose.EMPTY);
			cir.cancel();
		}
	}
}
