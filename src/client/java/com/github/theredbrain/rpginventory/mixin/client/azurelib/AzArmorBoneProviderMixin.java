package com.github.theredbrain.rpginventory.mixin.client.azurelib;

import com.github.theredbrain.rpginventory.render.azurelib.DuckAzArmorBoneProviderMixin;
import mod.azure.azurelib.rewrite.render.armor.bone.AzArmorBoneProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(AzArmorBoneProvider.class)
public interface AzArmorBoneProviderMixin extends DuckAzArmorBoneProviderMixin {
}
