package com.github.theredbrain.rpgmod.mixin.item;

import com.github.theredbrain.rpgmod.item.DuckToolItemMixin;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MiningToolItem.class)
public class MiningToolItemMixin {

    /**
     * @author
     */
    @Overwrite
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (((DuckToolItemMixin) this).isUsable(stack)) {
            stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
            return true;
        }
        return false;
    }

    /**
     * @author TheRedBrain
     */
    @Overwrite
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (((DuckToolItemMixin) this).isUsable(stack) && !world.isClient && state.getHardness(world, pos) != 0.0f) {
            stack.damage(1, miner, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
            return true;
        }
        return false;
    }
}
