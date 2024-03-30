package com.github.theredbrain.betteradventuremode.entity;

import net.minecraft.util.math.BlockPos;

public interface IsSpawnerBound {
    void setAnimationIdentifierString(String animationIdentifierString);
    void setBoundSpawnerBlockPos(BlockPos boundSpawnerBlockPos);
    void setBoundingBoxHeight(float boundingBoxHeight);
    void setBoundingBoxWidth(float boundingBoxWidth);
    void setModelIdentifierString(String modelIdentifierString);
    void setUseRelayBlockPos(BlockPos relayBlockPos);
    void setTextureIdentifierString(String textureIdentifierString);

}
