package com.github.theredbrain.betteradventuremode.client.render.renderer;

import com.github.theredbrain.betteradventuremode.entity.passive.SpawnerBoundVillagerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.feature.VillagerClothingFeatureRenderer;
import net.minecraft.client.render.entity.feature.VillagerHeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.VillagerResemblingModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SpawnerBoundVillagerEntityRenderer extends MobEntityRenderer<SpawnerBoundVillagerEntity, VillagerResemblingModel<SpawnerBoundVillagerEntity>> {
	private static final Identifier TEXTURE = new Identifier("textures/entity/villager/villager.png");

	public SpawnerBoundVillagerEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new VillagerResemblingModel<>(context.getPart(EntityModelLayers.VILLAGER)), 0.5F);
		this.addFeature(new HeadFeatureRenderer<>(this, context.getModelLoader(), context.getHeldItemRenderer()));
		this.addFeature(new VillagerClothingFeatureRenderer<>(this, context.getResourceManager(), "villager"));
		this.addFeature(new VillagerHeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
	}

	public Identifier getTexture(SpawnerBoundVillagerEntity spawnerBoundVillagerEntity) {
		return TEXTURE;
	}

	protected void scale(SpawnerBoundVillagerEntity spawnerBoundVillagerEntity, MatrixStack matrixStack, float f) {
		float g = 0.9375F;
		if (spawnerBoundVillagerEntity.isBaby()) {
			g *= 0.5F;
			this.shadowRadius = 0.25F;
		} else {
			this.shadowRadius = 0.5F;
		}

		matrixStack.scale(g, g, g);
	}
}
