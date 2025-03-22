package com.github.theredbrain.rpginventory.mixin.item;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {

	@Nullable
	@Unique
	private String rpginventory$translationKeyBroken;

	/**
	 * Gets or creates the translation key of this item when it is not protecting.
	 */
	@Unique
	private String rpginventory$getOrCreateTranslationKeyBroken() {
		if (this.rpginventory$translationKeyBroken == null) {
			this.rpginventory$translationKeyBroken = Util.createTranslationKey("item", Identifier.of(Registries.ITEM.getId((Item) (Object) this).getNamespace() + ":" + Registries.ITEM.getId((Item) (Object) this).getPath() + "_broken"));
		}
		return this.rpginventory$translationKeyBroken;
	}

	/**
	 * Gets the translation key of this item using the provided item stack for context.
	 */
	@Inject(method = "getTranslationKey(Lnet/minecraft/item/ItemStack;)Ljava/lang/String;", at = @At("RETURN"), cancellable = true)
	public void rpginventory$getTranslationKey(ItemStack stack, CallbackInfoReturnable<String> cir) {
		cir.setReturnValue(ItemUtils.isUsable(stack) ? cir.getReturnValue() : this.rpginventory$getOrCreateTranslationKeyBroken());
	}

	@Inject(method = "onCraftByPlayer", at = @At("HEAD"))
	public void rpginventory$onCraftByPlayer(ItemStack stack, World world, PlayerEntity player, CallbackInfo ci) {
		if (stack.contains(RPGInventory.SAVES_CRAFTING_PLAYER)) {
			stack.remove(RPGInventory.SAVES_CRAFTING_PLAYER);
			stack.set(RPGInventory.PLAYER_CRAFTED, new ProfileComponent(player.getGameProfile()));
		}
	}
}
