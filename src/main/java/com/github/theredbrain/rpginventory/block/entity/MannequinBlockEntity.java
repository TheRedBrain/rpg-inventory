package com.github.theredbrain.rpginventory.block.entity;

import com.github.theredbrain.rpginventory.registry.EntityRegistry;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.github.theredbrain.rpginventory.screen.MannequinScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class MannequinBlockEntity extends LockableContainerBlockEntity {
	public static final int INVENTORY_SIZE = 22;

	private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);

	public MannequinBlockEntity(BlockPos pos, BlockState state) {
		super(EntityRegistry.MANNEQUIN_BLOCK_ENTITY, pos, state);
	}

//	public ItemStack getLoadoutStack(EquipmentSlot equipmentSlot) {
//		if (equipmentSlot == EquipmentSlot.HEAD) {
//			return this.inventory.get(0).copy();
//		} else if (equipmentSlot == EquipmentSlot.BODY) {
//			return this.inventory.get(1).copy();
//		} else if (equipmentSlot == EquipmentSlot.LEGS) {
//			return this.inventory.get(2).copy();
//		} else if (equipmentSlot == EquipmentSlot.FEET) {
//			return this.inventory.get(3).copy();
//		} else {
//			return ItemStack.EMPTY;
//		}
//	}

	@Override
	public boolean checkUnlocked(PlayerEntity player) {
		boolean hasPreventMannequinInteractionEffect = false;
		for (StatusEffectInstance instance : player.getStatusEffects()) {
			if (instance.getEffectType().isIn(Tags.PREVENTS_MANNEQUIN_INTERACTION)) {
				hasPreventMannequinInteractionEffect = true;
				break;
			}
		}
		return super.checkUnlocked(player) && !hasPreventMannequinInteractionEffect;
	}

	@Override
	public int getMaxCountPerStack() {
		return 1;
	}

	@Override
	public int size() {
		return INVENTORY_SIZE;
	}

	@Override
	protected Text getContainerName() {
		return Text.translatable("container.mannequin");
	}

	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		Inventories.readNbt(nbt, this.inventory, registryLookup);
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		Inventories.writeNbt(nbt, this.inventory, registryLookup);
	}

	@Override
	protected DefaultedList<ItemStack> getHeldStacks() {
		return this.inventory;
	}

	@Override
	protected void setHeldStacks(DefaultedList<ItemStack> inventory) {
		this.inventory = inventory;
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new MannequinScreenHandler(syncId, playerInventory, this);
	}
}
