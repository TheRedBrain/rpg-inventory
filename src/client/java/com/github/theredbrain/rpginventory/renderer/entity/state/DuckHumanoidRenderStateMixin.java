package com.github.theredbrain.rpginventory.renderer.entity.state;

import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemStack;

public interface DuckHumanoidRenderStateMixin {

	ItemStack rpginventory$getLeftSheathedItemStack();

	void rpginventory$setLeftSheathedItemStack(ItemStack leftSheathedItemStack);

	ItemStackRenderState rpginventory$getLeftSheathedItemState();

	void rpginventory$setLeftSheathedItemState(ItemStackRenderState leftSheathedItemState);

	ItemStack rpginventory$getRightSheathedItemStack();

	void rpginventory$setRightSheathedItemStack(ItemStack rightSheathedItemStack);

	ItemStackRenderState rpginventory$getRightSheathedItemState();

	void rpginventory$setRightSheathedItemState(ItemStackRenderState rightSheathedItemState);

}
