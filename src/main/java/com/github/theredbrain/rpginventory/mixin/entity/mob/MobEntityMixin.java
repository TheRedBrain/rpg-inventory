package com.github.theredbrain.rpginventory.mixin.entity.mob;
//
//import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlotType;
//import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
//import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
//import net.minecraft.core.NonNullList;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.EquipmentSlot;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.Mob;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//
//@Mixin(Mob.class)
//public abstract class MobEntityMixin extends LivingEntity {
//
//	@Shadow
//	private ItemStack bodyArmor;
//
//	@Shadow
//	@Final
//	private NonNullList<ItemStack> armorItems;
//
//	@Shadow
//	@Final
//	private NonNullList<ItemStack> handItems;
//
//	@Shadow
//	protected float bodyArmorDropChance;
//
//	@Shadow
//	@Final
//	protected float[] armorDropChances;
//
//	@Shadow
//	@Final
//	protected float[] handDropChances;
//
//	protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
//		super(entityType, world);
//	}
//
//	@WrapMethod(method = "getEquippedStack")
//	public ItemStack rpginventory$getEquippedStack(EquipmentSlot slot, Operation<ItemStack> original) {
//		if (slot.getType() == EquipmentSlot.Type.HAND) {
//			return (ItemStack) this.handItems.get(slot.getIndex());
//		} else if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
//			return (ItemStack) this.armorItems.get(slot.getIndex());
//		} else if (slot.getType() == EquipmentSlot.Type.ANIMAL_ARMOR) {
//			return this.bodyArmor;
//		} else if (slot.getType() == ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE) {
//			return ItemStack.EMPTY;
//		} else {
//			return original.call(slot);
//		}
//	}
//
//	@WrapMethod(method = "equipStack")
//	public void rpginventory$equipStack(EquipmentSlot slot, ItemStack stack, Operation<Void> original) {
//		this.verifyEquippedItem(stack);
//		if (slot.getType() == EquipmentSlot.Type.HAND) {
//			this.onEquipItem(slot, this.handItems.set(slot.getIndex(), stack), stack);
//		} else if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
//			this.onEquipItem(slot, this.armorItems.set(slot.getIndex(), stack), stack);
//		} else if (slot.getType() == EquipmentSlot.Type.ANIMAL_ARMOR) {
//			ItemStack itemStack = this.bodyArmor;
//			this.bodyArmor = stack;
//			this.onEquipItem(slot, itemStack, stack);
//		} else if (slot.getType() != ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE) {
//			original.call(slot, stack);
//		}
//	}
//
//	@WrapMethod(method = "getDropChance")
//	protected float rpginventory$getDropChance(EquipmentSlot slot, Operation<Float> original) {
//		if (slot.getType() == EquipmentSlot.Type.HAND) {
//			return this.handDropChances[slot.getIndex()];
//		} else if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
//			return this.armorDropChances[slot.getIndex()];
//		} else if (slot.getType() == EquipmentSlot.Type.ANIMAL_ARMOR) {
//			return this.bodyArmorDropChance;
//		} else if (slot.getType() == ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE) {
//			return 0.0F;
//		} else {
//			return original.call(slot);
//		}
//	}
//
//	@WrapMethod(method = "setEquipmentDropChance")
//	public void rpginventory$setEquipmentDropChance(EquipmentSlot slot, float dropChance, Operation<Void> original) {
//		if (slot.getType() == EquipmentSlot.Type.HAND) {
//			this.handDropChances[slot.getIndex()] = dropChance;
//		} else if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
//			this.armorDropChances[slot.getIndex()] = dropChance;
//		} else if (slot.getType() == EquipmentSlot.Type.ANIMAL_ARMOR) {
//			this.bodyArmorDropChance = dropChance;
//		} else if (slot.getType() != ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE) {
//			original.call(slot, dropChance);
//		}
//	}
//
//}
