package com.github.theredbrain.bamcore.mixin.entity.damage;

import com.github.theredbrain.bamcore.api.item.ICustomWeapon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DamageSources.class)
public abstract class DamageSourcesMixin {

    @Shadow public abstract DamageSource create(RegistryKey<DamageType> key, @Nullable Entity attacker);

    /**
     * @author TheRedBrain
     * @reason
     */
    @Overwrite
    public DamageSource playerAttack(PlayerEntity attacker) {
        ItemStack mainHandStack = attacker.getMainHandStack();
        if (mainHandStack.getItem() instanceof ICustomWeapon) {
            return this.create(((ICustomWeapon)mainHandStack.getItem()).getDamageTypeRegistryKey(), attacker);
        }
        return this.create(DamageTypes.PLAYER_ATTACK, attacker);
    }
}
