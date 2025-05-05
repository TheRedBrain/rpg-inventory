package com.github.theredbrain.rpginventory;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class RPGInventoryMixinConfigPlugin implements IMixinConfigPlugin {

	private static boolean needsLoad = true;

	private static boolean applyTrinketsMixins = false;

	private static void loadIfNeeded() {
		if (needsLoad) {
			if (FabricLoader.getInstance().isModLoaded("trinkets")) {
				applyTrinketsMixins = true;
			}
			needsLoad = false;
		}
	}

	static boolean shouldApplyTrinketsMixins() {
		loadIfNeeded();
		return applyTrinketsMixins;
	}

	@Override
	public void onLoad(String s) {

	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String s, String s1) {

		if (s1.equals("com.github.theredbrain.rpginventory.mixin.trinkets.SurvivalTrinketSlotMixin") || s1.equals("com.github.theredbrain.rpginventory.mixin.screen.PlayerScreenHandlerMixin_TrinketsReplacement") || s1.equals("com.github.theredbrain.rpginventory.mixin.client.gui.screen.ingame.CreativeInventoryScreenMixin_TrinketsReplacement")) {
			return shouldApplyTrinketsMixins();
		}
		return true;
	}

	@Override
	public void acceptTargets(Set<String> set, Set<String> set1) {

	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

	}

	@Override
	public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

	}
}
