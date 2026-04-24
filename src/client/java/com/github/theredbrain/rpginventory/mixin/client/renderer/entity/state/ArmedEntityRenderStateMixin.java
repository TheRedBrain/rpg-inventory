package com.github.theredbrain.rpginventory.mixin.client.renderer.entity.state;

import com.github.theredbrain.rpginventory.entity.DuckLivingEntityMixin;
import com.github.theredbrain.rpginventory.renderer.entity.state.DuckArmedEntityRenderStateMixin;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmedEntityRenderState.class)
public class ArmedEntityRenderStateMixin implements DuckArmedEntityRenderStateMixin {
	@Unique
	public ItemStack leftSheathedItemStack = ItemStack.EMPTY;
	@Unique
	public final ItemStackRenderState leftSheathedItemState = new ItemStackRenderState();
	@Unique
	public ItemStack rightSheathedItemStack = ItemStack.EMPTY;
	@Unique
	public final ItemStackRenderState rightSheathedItemState = new ItemStackRenderState();


	@Inject(method = "extractArmedEntityRenderState", at = @At("TAIL"))
	private static void extractArmedEntityRenderState(
			LivingEntity entity, ArmedEntityRenderState state, ItemModelResolver itemModelResolver, float partialTicks, CallbackInfo ci
	) {
		itemModelResolver.updateForLiving(((DuckArmedEntityRenderStateMixin)state).rpginventory$getLeftSheathedItemState(), ((DuckLivingEntityMixin)entity).rpginventory$getSheathedItemStackByArm(HumanoidArm.LEFT), ItemDisplayContext.THIRD_PERSON_LEFT_HAND, entity);
		itemModelResolver.updateForLiving(((DuckArmedEntityRenderStateMixin)state).rpginventory$getRightSheathedItemState(), ((DuckLivingEntityMixin)entity).rpginventory$getSheathedItemStackByArm(HumanoidArm.RIGHT), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, entity);
		((DuckArmedEntityRenderStateMixin)state).rpginventory$setLeftSheathedItemStack(((DuckLivingEntityMixin)entity).rpginventory$getSheathedItemStackByArm(HumanoidArm.LEFT).copy());
		((DuckArmedEntityRenderStateMixin)state).rpginventory$setRightSheathedItemStack(((DuckLivingEntityMixin)entity).rpginventory$getSheathedItemStackByArm(HumanoidArm.RIGHT).copy());
	}

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
