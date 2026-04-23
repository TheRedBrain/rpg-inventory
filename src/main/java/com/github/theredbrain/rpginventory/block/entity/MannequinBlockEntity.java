package com.github.theredbrain.rpginventory.block.entity;

import com.github.theredbrain.rpginventory.registry.EntityRegistry;
import com.github.theredbrain.rpginventory.registry.Tags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Nameable;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class MannequinBlockEntity extends BlockEntity implements Container, Nameable {
	public static final int INVENTORY_SIZE = 23;

	private NonNullList<ItemStack> inventory = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);

	@Nullable
	private Component customName;

	private boolean canChangeInventory = true;
	private boolean canEquip = true;

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

	public boolean isLockedForPlayer(Player player) {
		boolean hasPreventMannequinInteractionEffect = false;
		for (MobEffectInstance instance : player.getActiveEffects()) {
			if (instance.getEffect().is(Tags.PREVENTS_MANNEQUIN_INTERACTION)) {
				hasPreventMannequinInteractionEffect = true;
				break;
			}
		}
		return hasPreventMannequinInteractionEffect;
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}

	@Override
	public int getContainerSize() {
		return INVENTORY_SIZE;
	}

	@Override
	protected void loadAdditional(final ValueInput input) {
		super.loadAdditional(input);

		this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(input, this.inventory);

		this.customName = parseCustomNameSafe(input, "CustomName");

		this.canChangeInventory = input.getBooleanOr("canChangeInventory", true);

		this.canEquip = input.getBooleanOr("canEquip", true);
	}

	@Override
	protected void saveAdditional(final ValueOutput output) {
		super.saveAdditional(output);

		ContainerHelper.saveAllItems(output, this.inventory);

		output.storeNullable("CustomName", ComponentSerialization.CODEC, this.customName);

		output.putBoolean("canChangeInventory", this.canChangeInventory);

		output.putBoolean("canEquip", this.canEquip);
	}

	@Override
	public Component getName() {
		return this.customName != null ? this.customName : Component.translatable("container.mannequin");
	}

	@Override
	public Component getDisplayName() {
		return this.getName();
	}

	@Nullable
	@Override
	public Component getCustomName() {
		return this.customName;
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemStack : this.inventory) {
			if (!itemStack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public ItemStack getItem(int slot) {
		return this.inventory.get(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		ItemStack itemStack = ContainerHelper.removeItem(this.inventory, slot, amount);
		if (!itemStack.isEmpty()) {
			this.setChanged();
		}

		return itemStack;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return ContainerHelper.takeItem(this.inventory, slot);
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		this.inventory.set(slot, stack);
		stack.limitSize(this.getMaxStackSize(stack));
		this.setChanged();
	}

	@Override
	public boolean stillValid(Player player) {
		return Container.stillValidBlockEntity(this, player);
	}

	@Override
	public void clearContent() {
		this.inventory.clear();
	}

	public boolean canChangeInventory() {
		return this.canChangeInventory;
	}

	public boolean canEquip() {
		return this.canEquip;
	}
}
