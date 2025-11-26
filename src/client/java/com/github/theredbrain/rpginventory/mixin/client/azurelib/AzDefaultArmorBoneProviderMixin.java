package com.github.theredbrain.rpginventory.mixin.client.azurelib;

import com.github.theredbrain.rpginventory.render.azurelib.DuckAzArmorBoneProviderMixin;
import mod.azure.azurelib.rewrite.model.AzBakedModel;
import mod.azure.azurelib.rewrite.model.AzBone;
import mod.azure.azurelib.rewrite.render.armor.bone.AzDefaultArmorBoneProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(AzDefaultArmorBoneProvider.class)
public class AzDefaultArmorBoneProviderMixin implements DuckAzArmorBoneProviderMixin {

	public @Nullable AzBone rpginventory$getLeftGloveBone(AzBakedModel model) {
		return model.getBoneOrNull("armorLeftBoot");
	}

	public @Nullable AzBone rpginventory$getRightGloveBone(AzBakedModel model) {
		return model.getBoneOrNull("armorLeftBoot");
	}

	public @Nullable AzBone rpginventory$getLeftShoulderBone(AzBakedModel model) {
		return model.getBoneOrNull("armorLeftBoot");
	}

	public @Nullable AzBone rpginventory$getRightShoulderBone(AzBakedModel model) {
		return model.getBoneOrNull("armorLeftBoot");
	}
}
