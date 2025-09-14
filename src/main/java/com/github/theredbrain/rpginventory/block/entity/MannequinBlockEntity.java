package com.github.theredbrain.rpginventory.block.entity;

import com.github.theredbrain.rpginventory.registry.EntityRegistry;
import com.github.theredbrain.rpginventory.registry.Tags;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Nameable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class MannequinBlockEntity extends BlockEntity implements Inventory, Nameable {
	public static final int INVENTORY_SIZE = 23;

	private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);

	@Nullable
	private Text customName;

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

	public boolean isLockedForPlayer(PlayerEntity player) {
		boolean hasPreventMannequinInteractionEffect = false;
		for (StatusEffectInstance instance : player.getStatusEffects()) {
			if (instance.getEffectType().isIn(Tags.PREVENTS_MANNEQUIN_INTERACTION)) {
				hasPreventMannequinInteractionEffect = true;
				break;
			}
		}
		return hasPreventMannequinInteractionEffect;
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
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		Inventories.readNbt(nbt, this.inventory, registryLookup);
		if (nbt.contains("CustomName", NbtElement.STRING_TYPE)) {
			this.customName = tryParseCustomName(nbt.getString("CustomName"), registryLookup);
		}
		if (nbt.contains("canChangeInventory")) {
			this.canChangeInventory = nbt.getBoolean("canChangeInventory");
		} else {
			this.canChangeInventory = true;
		}
		if (nbt.contains("canEquip")) {
			this.canEquip = nbt.getBoolean("canEquip");
		} else {
			this.canEquip = true;
		}
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		Inventories.writeNbt(nbt, this.inventory, registryLookup);
		if (this.customName != null) {
			nbt.putString("CustomName", Text.Serialization.toJsonString(this.customName, registryLookup));
		}
		if (this.canChangeInventory) {
			nbt.remove("canChangeInventory");
		} else {
			nbt.putBoolean("canChangeInventory", false);
		}

		if (this.canEquip) {
			nbt.remove("canEquip");
		} else {
			nbt.putBoolean("canEquip", false);
		}
	}

	@Override
	public Text getName() {
		return this.customName != null ? this.customName : Text.translatable("container.mannequin");
	}

	@Override
	public Text getDisplayName() {
		return this.getName();
	}

	@Nullable
	@Override
	public Text getCustomName() {
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
	public ItemStack getStack(int slot) {
		return this.inventory.get(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		ItemStack itemStack = Inventories.splitStack(this.inventory, slot, amount);
		if (!itemStack.isEmpty()) {
			this.markDirty();
		}

		return itemStack;
	}

	@Override
	public ItemStack removeStack(int slot) {
		return Inventories.removeStack(this.inventory, slot);
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		this.inventory.set(slot, stack);
		stack.capCount(this.getMaxCount(stack));
		this.markDirty();
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return Inventory.canPlayerUse(this, player);
	}

	@Override
	public void clear() {
		this.inventory.clear();
	}

	public boolean canChangeInventory() {
		return this.canChangeInventory;
	}

	public boolean canEquip() {
		return this.canEquip;
	}
}
