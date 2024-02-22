/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 */
package com.github.theredbrain.betteradventuremode.client.render.block.entity;

import com.github.theredbrain.betteradventuremode.block.entity.AreaBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

// TODO clean up
@Environment(value=EnvType.CLIENT)
public class StatusEffectApplierBlockEntityRenderer
implements BlockEntityRenderer<AreaBlockEntity> {
    public StatusEffectApplierBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(AreaBlockEntity areaBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        double o;
        double n;
        double m;
        double k;
        if (!MinecraftClient.getInstance().player.isCreativeLevelTwoOp() && !MinecraftClient.getInstance().player.isSpectator()) {
            return;
        }
        BlockPos blockPos = areaBlockEntity.getAreaPositionOffset();
        Vec3i vec3i = areaBlockEntity.getAreaDimensions();
        if (vec3i.getX() < 1 || vec3i.getY() < 1 || vec3i.getZ() < 1) {
            return;
        }
        double d = blockPos.getX();
        double e = blockPos.getZ();
        double g = blockPos.getY();
        double h = g + (double)vec3i.getY();
        k = vec3i.getX(); // temp
        double l = vec3i.getZ(); // temp
        m = k < 0.0 ? d + 1.0 : d; // temp
        n = l < 0.0 ? e + 1.0 : e; // temp
        o = m + k; // temp
        double p = n + l; // temp
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getLines());
        if (areaBlockEntity.showArea()) {
            WorldRenderer.drawBox(matrixStack, vertexConsumer, m, g, n, o, h, p, 0.9f, 0.9f, 0.9f, 1.0f, 0.5f, 0.5f, 0.5f);
            this.renderInvisibleBlocks(areaBlockEntity, vertexConsumer, blockPos, matrixStack);
        }
    }

    private void renderInvisibleBlocks(AreaBlockEntity entity, VertexConsumer vertices, BlockPos pos, MatrixStack matrices) {
        World blockView = entity.getWorld();
        BlockPos blockPos = entity.getPos();
        BlockPos blockPos2 = blockPos.add(pos);
        for (BlockPos blockPos3 : BlockPos.iterate(blockPos2, blockPos2.add(entity.getAreaDimensions()).add(-1, -1, -1))) {
            boolean bl5;
            BlockState blockState = blockView.getBlockState(blockPos3);
            boolean bl = blockState.isAir();
            boolean bl2 = blockState.isOf(Blocks.STRUCTURE_VOID);
            boolean bl3 = blockState.isOf(Blocks.BARRIER);
            boolean bl4 = blockState.isOf(Blocks.LIGHT);
            boolean bl6 = bl5 = bl2 || bl3 || bl4;
            if (!bl && !bl5) continue;
            float f = bl ? 0.05f : 0.0f;
            double d = (float)(blockPos3.getX() - blockPos.getX()) + 0.45f - f;
            double e = (float)(blockPos3.getY() - blockPos.getY()) + 0.45f - f;
            double g = (float)(blockPos3.getZ() - blockPos.getZ()) + 0.45f - f;
            double h = (float)(blockPos3.getX() - blockPos.getX()) + 0.55f + f;
            double i = (float)(blockPos3.getY() - blockPos.getY()) + 0.55f + f;
            double j = (float)(blockPos3.getZ() - blockPos.getZ()) + 0.55f + f;
            if (bl) {
                WorldRenderer.drawBox(matrices, vertices, d, e, g, h, i, j, 0.5f, 0.5f, 1.0f, 1.0f, 0.5f, 0.5f, 1.0f);
                continue;
            }
            if (bl2) {
                WorldRenderer.drawBox(matrices, vertices, d, e, g, h, i, j, 1.0f, 0.75f, 0.75f, 1.0f, 1.0f, 0.75f, 0.75f);
                continue;
            }
            if (bl3) {
                WorldRenderer.drawBox(matrices, vertices, d, e, g, h, i, j, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f);
                continue;
            }
            if (!bl4) continue;
            WorldRenderer.drawBox(matrices, vertices, d, e, g, h, i, j, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f);
        }
    }

    @Override
    public boolean rendersOutsideBoundingBox(AreaBlockEntity areaBlockEntity) {
        return true;
    }

    @Override
    public int getRenderDistance() {
        return 96;
    }
}

