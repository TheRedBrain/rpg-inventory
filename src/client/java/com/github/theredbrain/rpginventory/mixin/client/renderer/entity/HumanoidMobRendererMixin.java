package com.github.theredbrain.rpginventory.mixin.client.renderer.entity;

import com.github.theredbrain.rpginventory.entity.DuckLivingEntityMixin;
import com.github.theredbrain.rpginventory.renderer.entity.layers.SheathedItemLayer;
import com.github.theredbrain.rpginventory.renderer.entity.state.DuckHumanoidRenderStateMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(HumanoidMobRenderer.class)
public abstract class HumanoidMobRendererMixin<T extends Mob, S extends HumanoidRenderState, M extends HumanoidModel<S>> extends AgeableMobRenderer<T, S, M> {

	public HumanoidMobRendererMixin(EntityRendererProvider.Context context, M adultModel, M babyModel, float shadow) {
		super(context, adultModel, babyModel, shadow);
	}

	@Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;Lnet/minecraft/client/model/HumanoidModel;Lnet/minecraft/client/model/HumanoidModel;FLnet/minecraft/client/renderer/entity/layers/CustomHeadLayer$Transforms;)V", at = @At("TAIL"))
	private void rpginventory$init(EntityRendererProvider.Context context, M model, M babyModel, float shadow, CustomHeadLayer.Transforms customHeadTransforms, CallbackInfo ci) {
		this.addLayer(new SheathedItemLayer<>(this));
	}

	@Inject(method = "extractHumanoidRenderState", at = @At("TAIL"))
	private static void rpginventory$extractHumanoidRenderState(
			LivingEntity entity, HumanoidRenderState state, float partialTicks, ItemModelResolver itemModelResolver, CallbackInfo ci
	) {
		itemModelResolver.updateForLiving(((DuckHumanoidRenderStateMixin) state).rpginventory$getLeftSheathedItemState(), ((DuckLivingEntityMixin) entity).rpginventory$getSheathedItemStackByArm(HumanoidArm.LEFT), ItemDisplayContext.THIRD_PERSON_LEFT_HAND, entity);
		itemModelResolver.updateForLiving(((DuckHumanoidRenderStateMixin) state).rpginventory$getRightSheathedItemState(), ((DuckLivingEntityMixin) entity).rpginventory$getSheathedItemStackByArm(HumanoidArm.RIGHT), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, entity);
		((DuckHumanoidRenderStateMixin) state).rpginventory$setLeftSheathedItemStack(((DuckLivingEntityMixin) entity).rpginventory$getSheathedItemStackByArm(HumanoidArm.LEFT).copy());
		((DuckHumanoidRenderStateMixin) state).rpginventory$setRightSheathedItemStack(((DuckLivingEntityMixin) entity).rpginventory$getSheathedItemStackByArm(HumanoidArm.RIGHT).copy());
	}
}
