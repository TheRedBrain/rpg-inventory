package com.github.theredbrain.bamcore.mixin.entity;

import net.minecraft.entity.DamageUtil;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(DamageUtil.class)
public class DamageUtilMixin {

    /**
     * @author TheRedBrain
     * @reason overhaul armor and armor toughness
     */
    @Overwrite
    public static float getDamageLeft(float damage, float armor, float armorToughness) {

        // armorToughness now directly determines how effective armor is
        // it can not increase armor beyond the initial value
        float effectiveArmor = armor * MathHelper.clamp(armorToughness, 0, 1);

        // effective armor reduces damage by its amount
        // armor can't reduce damage to zero
        return Math.max(1, damage - effectiveArmor);
    }
}
