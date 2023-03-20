package com.github.theredbrain.rpgmod.item;

import com.github.theredbrain.rpgmod.entity.ExtendedEquipmentSlot;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Wearable;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AccessoryRingItem extends Item implements Wearable {
    private final SoundEvent equipSound;

    public AccessoryRingItem(SoundEvent equipSound, Settings settings) {
        super(settings);
        this.equipSound = equipSound;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        EquipmentSlot equipmentSlot = ExtendedEquipmentSlot.FIRST_RING;
        ItemStack itemStack2 = user.getEquippedStack(equipmentSlot);
        EquipmentSlot equipmentSlot2 = ExtendedEquipmentSlot.SECOND_RING;
        ItemStack itemStack3 = user.getEquippedStack(equipmentSlot2);
        if (itemStack2.isEmpty()) {
            user.equipStack(equipmentSlot, itemStack.copy());
            if (!world.isClient()) {
                user.incrementStat(Stats.USED.getOrCreateStat(this));
            }
            itemStack.setCount(0);
            return TypedActionResult.success(itemStack, world.isClient());
        } else if (itemStack3.isEmpty()) {
            user.equipStack(equipmentSlot2, itemStack.copy());
            if (!world.isClient()) {
                user.incrementStat(Stats.USED.getOrCreateStat(this));
            }
            itemStack.setCount(0);
            return TypedActionResult.success(itemStack, world.isClient());
        }
        return TypedActionResult.fail(itemStack);
    }

    @Override
    @Nullable
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }
}
