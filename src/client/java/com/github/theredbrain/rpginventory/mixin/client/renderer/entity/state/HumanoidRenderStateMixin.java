package com.github.theredbrain.rpginventory.mixin.client.renderer.entity.state;

import com.github.theredbrain.rpginventory.renderer.entity.state.DuckHumanoidRenderStateMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(HumanoidRenderState.class)
public class HumanoidRenderStateMixin implements DuckHumanoidRenderStateMixin {
	@Unique
	public ItemStack leftSheathedItemStack = ItemStack.EMPTY;
	@Unique
	public final ItemStackRenderState leftSheathedItemState = new ItemStackRenderState();
	@Unique
	public ItemStack rightSheathedItemStack = ItemStack.EMPTY;
	@Unique
	public final ItemStackRenderState rightSheathedItemState = new ItemStackRenderState();

	@Override
	public ItemStack rpginventory$getLeftSheathedItemStack() {
		return this.leftSheathedItemStack;
	}

	@Override
	public void rpginventory$setLeftSheathedItemStack(ItemStack leftSheathedItemStack) {
		this.leftSheathedItemStack = leftSheathedItemStack;
	}

	@Override
	public ItemStackRenderState rpginventory$getLeftSheathedItemState() {
		return this.leftSheathedItemState;
	}

	@Override
	public void rpginventory$setLeftSheathedItemState(ItemStackRenderState leftSheathedItemState) {

	}

	@Override
	public ItemStack rpginventory$getRightSheathedItemStack() {
		return this.rightSheathedItemStack;
	}

	@Override
	public void rpginventory$setRightSheathedItemStack(ItemStack rightSheathedItemStack) {
		this.rightSheathedItemStack = rightSheathedItemStack;
	}

	@Override
	public ItemStackRenderState rpginventory$getRightSheathedItemState() {
		return this.rightSheathedItemState;
	}

	@Override
	public void rpginventory$setRightSheathedItemState(ItemStackRenderState rightSheathedItemState) {

	}
}
