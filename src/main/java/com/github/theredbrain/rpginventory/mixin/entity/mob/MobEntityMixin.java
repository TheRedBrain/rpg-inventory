package com.github.theredbrain.rpginventory.mixin.entity.mob;

import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlotType;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {

	@Shadow
	private ItemStack bodyArmor;

	@Shadow
	@Final
	private DefaultedList<ItemStack> armorItems;

	@Shadow
	@Final
	private DefaultedList<ItemStack> handItems;

	@Shadow
	protected float bodyArmorDropChance;

	@Shadow
	@Final
	protected float[] armorDropChances;

	@Shadow
	@Final
	protected float[] handDropChances;

	protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@WrapMethod(method = "getEquippedStack")
	public ItemStack rpginventory$getEquippedStack(EquipmentSlot slot, Operation<ItemStack> original) {
		if (slot.getType() == EquipmentSlot.Type.HAND) {
			return (ItemStack) this.handItems.get(slot.getEntitySlotId());
		} else if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
			return (ItemStack) this.armorItems.get(slot.getEntitySlotId());
		} else if (slot.getType() == EquipmentSlot.Type.ANIMAL_ARMOR) {
			return this.bodyArmor;
		} else if (slot.getType() == ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE) {
			return ItemStack.EMPTY;
		} else {
			return original.call(slot);
		}
	}

	@WrapMethod(method = "equipStack")
	public void rpginventory$equipStack(EquipmentSlot slot, ItemStack stack, Operation<Void> original) {
		this.processEquippedStack(stack);
		if (slot.getType() == EquipmentSlot.Type.HAND) {
			this.onEquipStack(slot, this.handItems.set(slot.getEntitySlotId(), stack), stack);
		} else if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
			this.onEquipStack(slot, this.armorItems.set(slot.getEntitySlotId(), stack), stack);
		} else if (slot.getType() == EquipmentSlot.Type.ANIMAL_ARMOR) {
			ItemStack itemStack = this.bodyArmor;
			this.bodyArmor = stack;
			this.onEquipStack(slot, itemStack, stack);
		} else if (slot.getType() != ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE) {
			original.call(slot, stack);
		}
	}

	@WrapMethod(method = "getDropChance")
	protected float rpginventory$getDropChance(EquipmentSlot slot, Operation<Float> original) {
		if (slot.getType() == EquipmentSlot.Type.HAND) {
			return this.handDropChances[slot.getEntitySlotId()];
		} else if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
			return this.armorDropChances[slot.getEntitySlotId()];
		} else if (slot.getType() == EquipmentSlot.Type.ANIMAL_ARMOR) {
			return this.bodyArmorDropChance;
		} else if (slot.getType() == ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE) {
			return 0.0F;
		} else {
			return original.call(slot);
		}
	}

	@WrapMethod(method = "setEquipmentDropChance")
	public void rpginventory$setEquipmentDropChance(EquipmentSlot slot, float dropChance, Operation<Void> original) {
		if (slot.getType() == EquipmentSlot.Type.HAND) {
			this.handDropChances[slot.getEntitySlotId()] = dropChance;
		} else if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
			this.armorDropChances[slot.getEntitySlotId()] = dropChance;
		} else if (slot.getType() == EquipmentSlot.Type.ANIMAL_ARMOR) {
			this.bodyArmorDropChance = dropChance;
		} else if (slot.getType() != ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE) {
			original.call(slot, dropChance);
		}
	}

}
