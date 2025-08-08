package com.github.theredbrain.rpginventory.render.renderer;

import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.config.ClientConfig;
import com.github.theredbrain.rpginventory.entity.RendersSheathedWeapons;
import com.github.theredbrain.rpginventory.registry.Tags;
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
import net.minecraft.registry.Registries;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class SheathedHandItemFeatureRenderer<T extends LivingEntity> extends HeldItemFeatureRenderer<T, PlayerEntityModel<T>> {

	private final HeldItemRenderer heldItemRenderer;

	public SheathedHandItemFeatureRenderer(FeatureRendererContext<T, PlayerEntityModel<T>> context, HeldItemRenderer heldItemRenderer) {
		super(context, heldItemRenderer);
		this.heldItemRenderer = heldItemRenderer;
	}

	@Override
	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {

		if (livingEntity instanceof RendersSheathedWeapons renderEquippedTrinkets) {

			ItemStack handStack = renderEquippedTrinkets.rpginventory$getSheathedHandItemStack();

			if (!handStack.isEmpty() && !handStack.isIn(Tags.NOT_SHOWN_WHEN_IN_SHEATHED_HAND)) {
				Item handStackItem = handStack.getItem();
				boolean hasStackedEquippedInChestSlot = livingEntity.hasStackEquipped(EquipmentSlot.CHEST);
				double initial_translation_x = -0.3;
				double initial_translation_y = 0.05;
				double initial_translation_z = 0.16;
				double equipped_chest_offset_x = 0.0;
				double equipped_chest_offset_y = 0.0;
				double equipped_chest_offset_z = 0.06;
				float rotation_positive_z = 0.0F;
				float rotation_positive_y = 90.0F;
				float rotation_positive_x = 35.0F;
				String itemId = Registries.ITEM.getId(handStackItem).toString();
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
				matrixStack.push();
				ModelPart modelPart = this.getContextModel().body;
				modelPart.rotate(matrixStack);
				if (this.getContextModel().child) {
					matrixStack.translate(0.0F, 0.75F, 0.0F);
					matrixStack.scale(0.5F, 0.5F, 0.5F);
				}
				matrixStack.translate(initial_translation_x, initial_translation_y, initial_translation_z);
				if (hasStackedEquippedInChestSlot) {
					matrixStack.translate(equipped_chest_offset_x, equipped_chest_offset_y, equipped_chest_offset_z);
				}
				matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation_positive_z));
				matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation_positive_y));
				matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotation_positive_x));
				heldItemRenderer.renderItem(livingEntity, handStack, ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, false, matrixStack, vertexConsumerProvider, i);
				matrixStack.pop();
			}
		}
	}
}
