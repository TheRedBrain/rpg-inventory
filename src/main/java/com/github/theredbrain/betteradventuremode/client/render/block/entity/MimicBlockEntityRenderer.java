package com.github.theredbrain.betteradventuremode.client.render.block.entity;

import com.github.theredbrain.betteradventuremode.block.entity.MimicBlockEntity;
import com.github.theredbrain.betteradventuremode.registry.BlockRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.random.Random;

@Environment(value=EnvType.CLIENT)
public class MimicBlockEntityRenderer
implements BlockEntityRenderer<MimicBlockEntity> {
    public MimicBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(MimicBlockEntity mimicBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayers.getBlockLayer(mimicBlockEntity.getCurrentMimicBlockState()));
        blockRenderManager.renderBlock(mimicBlockEntity.isDebugModeActive() ? BlockRegistry.MIMIC_FALLBACK_BLOCK.getDefaultState() : mimicBlockEntity.getCurrentMimicBlockState(), mimicBlockEntity.getPos(), mimicBlockEntity.getWorld(), matrixStack, vertexConsumer, true, Random.create());
    }

    @Override
    public int getRenderDistance() {
        return 96;
    }
}

