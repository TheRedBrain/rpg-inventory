package com.github.theredbrain.rpginventory.mixin.entity;

import com.github.theredbrain.rpginventory.entity.DuckLivingEntityMixin;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;

import java.util.function.Predicate;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements DuckLivingEntityMixin {

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    
    @Override
    public boolean rpginventory$hasEquipped(Predicate<ItemStack> predicate) {
        if (predicate.test(this.getEquippedStack(EquipmentSlot.MAINHAND))) {
            return true;
        }
        if (predicate.test(this.getEquippedStack(EquipmentSlot.OFFHAND))) {
            return true;
        }
        if (predicate.test(this.getEquippedStack(EquipmentSlot.FEET))) {
            return true;
        }
        if (predicate.test(this.getEquippedStack(EquipmentSlot.LEGS))) {
            return true;
        }
        if (predicate.test(this.getEquippedStack(EquipmentSlot.CHEST))) {
            return true;
        }
        if (predicate.test(this.getEquippedStack(EquipmentSlot.HEAD))) {
            return true;
        }
        return false;
    }

    @Override
    public int rpginventory$getAmountEquipped(Predicate<ItemStack> predicate) {
        int i = 0;
        if (predicate.test(this.getEquippedStack(EquipmentSlot.MAINHAND))) {
            i += 1;
        }
        if (predicate.test(this.getEquippedStack(EquipmentSlot.OFFHAND))) {
            i += 1;
        }
        if (predicate.test(this.getEquippedStack(EquipmentSlot.FEET))) {
            i += 1;
        }
        if (predicate.test(this.getEquippedStack(EquipmentSlot.LEGS))) {
            i += 1;
        }
        if (predicate.test(this.getEquippedStack(EquipmentSlot.CHEST))) {
            i += 1;
        }
        if (predicate.test(this.getEquippedStack(EquipmentSlot.HEAD))) {
            i += 1;
        }
        return i;
    }
}
