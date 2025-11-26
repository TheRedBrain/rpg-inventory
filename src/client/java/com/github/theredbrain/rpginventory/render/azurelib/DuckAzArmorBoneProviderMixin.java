package com.github.theredbrain.rpginventory.render.azurelib;

import mod.azure.azurelib.rewrite.model.AzBakedModel;
import mod.azure.azurelib.rewrite.model.AzBone;
import org.jetbrains.annotations.Nullable;

public interface DuckAzArmorBoneProviderMixin {

	@Nullable AzBone rpginventory$getLeftGloveBone(AzBakedModel var1);

	@Nullable AzBone rpginventory$getRightGloveBone(AzBakedModel var1);

	@Nullable AzBone rpginventory$getLeftShoulderBone(AzBakedModel var1);

	@Nullable AzBone rpginventory$getRightShoulderBone(AzBakedModel var1);
}
