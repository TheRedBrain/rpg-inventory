package com.github.theredbrain.rpginventory.mixin.client.render.entity;

import com.github.theredbrain.rpginventory.registry.Tags;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin<AvatarlikeEntity extends Avatar & ClientAvatarEntity> extends LivingEntityRenderer<AvatarlikeEntity, AvatarRenderState, PlayerModel> {


	public AvatarRendererMixin(EntityRendererProvider.Context context, PlayerModel model, float shadow) {
		super(context, model, shadow);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void rpginventory$init(EntityRendererProvider.Context context, boolean slimSteve, CallbackInfo info) {
//		this.addLayer(new SheathedHandItemFeatureRenderer<>(this, ctx.getItemInHandRenderer()));
//		this.addLayer(new SheathedOffHandItemFeatureRenderer<>(this, ctx.getEntityRenderDispatcher().getItemInHandRenderer())));
	}

	@WrapMethod(method = "getArmPose(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;")
	private static HumanoidModel.ArmPose rpginventory$wrap_getArmPose(Avatar avatar, ItemStack itemInHand, InteractionHand hand, Operation<HumanoidModel.ArmPose> original) {
		if (!itemInHand.isEmpty() && (itemInHand.is(Tags.EMPTY_HAND_WEAPONS) || !ItemUtils.isUsable(itemInHand) || (avatar instanceof Player player && !ItemUtils.isUsableByPlayer(itemInHand, player)))) {
			return HumanoidModel.ArmPose.EMPTY;
		}
		return original.call(avatar, itemInHand, hand);
	}
}
