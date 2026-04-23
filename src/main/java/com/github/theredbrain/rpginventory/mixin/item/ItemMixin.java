package com.github.theredbrain.rpginventory.mixin.item;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public abstract class ItemMixin {

	@Nullable
	@Unique
	private String rpginventory$descriptionIdBroken;

	/**
	 * Gets or creates the translation key of this item when it is not protecting.
	 */
	@Unique
	private String rpginventory$getOrCreateDescriptionIdBroken() {
		if (this.rpginventory$descriptionIdBroken == null) {
			this.rpginventory$descriptionIdBroken = Util.makeDescriptionId("item", Identifier.parse(BuiltInRegistries.ITEM.getKey((Item) (Object) this).getNamespace() + ":" + BuiltInRegistries.ITEM.getKey((Item) (Object) this).getPath() + "_broken"));
		}
		return this.rpginventory$descriptionIdBroken;
	}

	@WrapMethod(method = "getName")
	public Component rpginventory$getName(ItemStack itemStack, Operation<Component> original) {
		if (ItemUtils.isUsable(itemStack)) {
			return original.call(itemStack);
		} else {
			return Component.translatable(this.rpginventory$getOrCreateDescriptionIdBroken());
		}
	}

	@Inject(method = "onCraftedBy", at = @At("HEAD"))
	public void rpginventory$onCraftBy(ItemStack itemStack, Player player, CallbackInfo ci) {
		if (itemStack.remove(RPGInventory.SAVES_CRAFTING_PLAYER) != null) {
			itemStack.set(RPGInventory.PLAYER_CRAFTED, ResolvableProfile.createResolved(player.getGameProfile()));
		}
	}
}
