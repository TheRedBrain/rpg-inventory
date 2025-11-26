package com.github.theredbrain.rpginventory.mixin.client.azurelibarmor;

import com.github.theredbrain.rpginventory.render.azurelibarmor.DuckAzArmorBoneProviderMixin;
import mod.azure.azurelibarmor.rewrite.render.armor.bone.AzArmorBoneProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(AzArmorBoneProvider.class)
public interface AzArmorBoneProviderMixin extends DuckAzArmorBoneProviderMixin {
}
