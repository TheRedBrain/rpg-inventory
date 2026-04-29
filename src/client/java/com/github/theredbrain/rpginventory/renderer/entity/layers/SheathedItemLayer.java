package com.github.theredbrain.rpginventory.renderer.entity.layers;

import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.config.ClientConfig;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.github.theredbrain.rpginventory.renderer.entity.state.DuckHumanoidRenderStateMixin;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class SheathedItemLayer<S extends HumanoidRenderState, M extends HumanoidModel<S>> extends RenderLayer<S, M> {

	public SheathedItemLayer(final RenderLayerParent<S, M> renderer) {
		super(renderer);
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
		this.submitSheathedItem(state, ((DuckHumanoidRenderStateMixin) state).rpginventory$getRightSheathedItemState(), ((DuckHumanoidRenderStateMixin) state).rpginventory$getRightSheathedItemStack(), HumanoidArm.RIGHT, poseStack, submitNodeCollector, lightCoords);
		this.submitSheathedItem(state, ((DuckHumanoidRenderStateMixin) state).rpginventory$getLeftSheathedItemState(), ((DuckHumanoidRenderStateMixin) state).rpginventory$getLeftSheathedItemStack(), HumanoidArm.LEFT, poseStack, submitNodeCollector, lightCoords);
	}


	protected void submitSheathedItem(
			final S state,
			final ItemStackRenderState item,
			final ItemStack itemStack,
			final HumanoidArm arm,
			final PoseStack poseStack,
			final SubmitNodeCollector submitNodeCollector,
			final int lightCoords
	) {

		if (itemStack.isEmpty()
				|| (arm == HumanoidArm.LEFT && itemStack.is(Tags.NOT_SHOWN_WHEN_IN_SHEATHED_LEFT_HAND))
				|| (arm == HumanoidArm.RIGHT && itemStack.is(Tags.NOT_SHOWN_WHEN_IN_SHEATHED_RIGHT_HAND))
		) {
			return;
		}

		boolean hasStackedEquippedInChestSlot = !state.chestEquipment.isEmpty();

		double initial_translation_x = 0.0;
		double initial_translation_y = 0.0;
		double initial_translation_z = 0.0;
		double equipped_chest_offset_x = 0.0;
		double equipped_chest_offset_y = 0.0;
		double equipped_chest_offset_z = 0.0;
		float rotation_positive_z = 0.0F;
		float rotation_positive_y = 0.0F;
		float rotation_positive_x = 0.0F;
		ClientConfig.ItemConfiguration itemConfiguration = null;
		String itemId = BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString();

		if (arm == HumanoidArm.LEFT) {
			initial_translation_x = 0.2;
			initial_translation_y = 0.0;
			initial_translation_z = 0.15;
			equipped_chest_offset_x = 0.0;
			equipped_chest_offset_y = 0.0;
			equipped_chest_offset_z = 0.06;
			rotation_positive_z = 0.0F;
			rotation_positive_y = 90.0F;
			rotation_positive_x = -15.0F;
			itemConfiguration = RPGInventoryClient.CLIENT_CONFIG.sheathed_offhand_item_positions.get(itemId);
		} else if (arm == HumanoidArm.RIGHT) {
			initial_translation_x = -0.3;
			initial_translation_y = 0.05;
			initial_translation_z = 0.16;
			equipped_chest_offset_x = 0.0;
			equipped_chest_offset_y = 0.0;
			equipped_chest_offset_z = 0.06;
			rotation_positive_z = 0.0F;
			rotation_positive_y = 90.0F;
			rotation_positive_x = 35.0F;
			itemConfiguration = RPGInventoryClient.CLIENT_CONFIG.sheathed_hand_item_positions.get(itemId);
		}

		if (itemConfiguration != null) {
			initial_translation_x = itemConfiguration.initial_translation_x;
			initial_translation_y = itemConfiguration.initial_translation_y;
			initial_translation_z = itemConfiguration.initial_translation_z;
			equipped_chest_offset_x = itemConfiguration.equipped_chest_offset_x;
			equipped_chest_offset_y = itemConfiguration.equipped_chest_offset_y;
			equipped_chest_offset_z = itemConfiguration.equipped_chest_offset_z;
			rotation_positive_z = itemConfiguration.rotation_positive_z;
			rotation_positive_y = itemConfiguration.rotation_positive_y;
			rotation_positive_x = itemConfiguration.rotation_positive_x;
		}

		poseStack.pushPose();
		ModelPart modelPart = this.getParentModel().body;
		modelPart.translateAndRotate(poseStack);
		if (this.useBabyOffset(state)) {
			poseStack.translate(0.0F, 0.75F, 0.0F);
			poseStack.scale(0.5F, 0.5F, 0.5F);
		}
		poseStack.translate(initial_translation_x, initial_translation_y, initial_translation_z);
		if (hasStackedEquippedInChestSlot) {
			poseStack.translate(equipped_chest_offset_x, equipped_chest_offset_y, equipped_chest_offset_z);
		}
		poseStack.mulPose(Axis.ZP.rotationDegrees(rotation_positive_z));
		poseStack.mulPose(Axis.YP.rotationDegrees(rotation_positive_y));
		poseStack.mulPose(Axis.XP.rotationDegrees(rotation_positive_x));

		item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
		poseStack.popPose();
	}

	private boolean useBabyOffset(final S state) {
		return state.isBaby && state.entityType != EntityType.ARMOR_STAND;
	}
}
