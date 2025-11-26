package com.github.theredbrain.rpginventory.mixin.client.azurelibarmor;

import com.github.theredbrain.rpginventory.render.azurelibarmor.DuckAzArmorBoneProviderMixin;
import mod.azure.azurelibarmor.rewrite.model.AzBakedModel;
import mod.azure.azurelibarmor.rewrite.model.AzBone;
import mod.azure.azurelibarmor.rewrite.render.armor.bone.AzDefaultArmorBoneProvider;
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
