package com.github.theredbrain.rpginventory.render.renderer;

import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.config.ClientConfig;
import com.github.theredbrain.rpginventory.entity.RendersSheathedWeapons;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class SheathedHandItemFeatureRenderer<T extends LivingEntity> extends ItemInHandLayer<T, PlayerModel<T>> {

	private final ItemInHandRenderer heldItemRenderer;

	public SheathedHandItemFeatureRenderer(RenderLayerParent<T, PlayerModel<T>> context, ItemInHandRenderer heldItemRenderer) {
		super(context, heldItemRenderer);
		this.heldItemRenderer = heldItemRenderer;
	}

	@Override
	public void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {

		if (livingEntity instanceof RendersSheathedWeapons renderEquippedTrinkets) {

			ItemStack handStack = renderEquippedTrinkets.rpginventory$getSheathedHandItemStack();

			if (!handStack.isEmpty() && !handStack.is(Tags.NOT_SHOWN_WHEN_IN_SHEATHED_HAND)) {
				Item handStackItem = handStack.getItem();
				boolean hasStackedEquippedInChestSlot = livingEntity.hasItemInSlot(EquipmentSlot.CHEST);
				double initial_translation_x = -0.3;
				double initial_translation_y = 0.05;
				double initial_translation_z = 0.16;
				double equipped_chest_offset_x = 0.0;
				double equipped_chest_offset_y = 0.0;
				double equipped_chest_offset_z = 0.06;
				float rotation_positive_z = 0.0F;
				float rotation_positive_y = 90.0F;
				float rotation_positive_x = 35.0F;
				String itemId = BuiltInRegistries.ITEM.getKey(handStackItem).toString();
				ClientConfig.ItemConfiguration itemConfiguration = RPGInventoryClient.CLIENT_CONFIG.sheathed_hand_item_positions.get(itemId);
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
				matrixStack.pushPose();
				ModelPart modelPart = this.getParentModel().body;
				modelPart.translateAndRotate(matrixStack);
				if (this.getParentModel().young) {
					matrixStack.translate(0.0F, 0.75F, 0.0F);
					matrixStack.scale(0.5F, 0.5F, 0.5F);
				}
				matrixStack.translate(initial_translation_x, initial_translation_y, initial_translation_z);
				if (hasStackedEquippedInChestSlot) {
					matrixStack.translate(equipped_chest_offset_x, equipped_chest_offset_y, equipped_chest_offset_z);
				}
				matrixStack.mulPose(Axis.ZP.rotationDegrees(rotation_positive_z));
				matrixStack.mulPose(Axis.YP.rotationDegrees(rotation_positive_y));
				matrixStack.mulPose(Axis.XP.rotationDegrees(rotation_positive_x));
				heldItemRenderer.renderItem(livingEntity, handStack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, false, matrixStack, vertexConsumerProvider, i);
				matrixStack.popPose();
			}
		}
	}
}
