package com.github.theredbrain.betteradventuremode.mixin.bettercombat;

import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.item.BasicShieldItem;
import com.github.theredbrain.betteradventuremode.registry.StatusEffectsRegistry;
import com.github.theredbrain.betteradventuremode.registry.Tags;
import net.bettercombat.api.WeaponAttributes.Condition;
import net.bettercombat.api.WeaponAttributes;
import net.bettercombat.logic.PlayerAttackHelper;
import net.bettercombat.logic.WeaponRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(PlayerAttackHelper.class)
public class PlayerAttackHelperMixin {

    @Shadow
    public static boolean isDualWielding(PlayerEntity player) {
        throw new AssertionError();
    }

    @Inject(method = "isDualWielding", at = @At("RETURN"), cancellable = true)
    private static void bam$isDualWielding(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() && !(!player.getMainHandStack().isIn(Tags.EMPTY_HAND_WEAPONS) && player.getOffHandStack().isIn(Tags.EMPTY_HAND_WEAPONS)));
        cir.cancel();
    }

    /**
     * @author TheRedBrain
     * @reason account for empty hand weapons and toggleable two-handing
     */
    @Overwrite
    private static boolean evaluateCondition(Condition condition, PlayerEntity player, boolean isOffHandAttack) {
        if (condition == null) {
            return true;
        } else {
            ItemStack offhandStack;
            switch(condition) {
                case NOT_DUAL_WIELDING:
                    return !isDualWielding(player);
                case DUAL_WIELDING_ANY:
                    return isDualWielding(player);
                case DUAL_WIELDING_SAME:
                    return isDualWielding(player) && player.getMainHandStack().getItem() == player.getOffHandStack().getItem();
                case DUAL_WIELDING_SAME_CATEGORY:
                    if (!isDualWielding(player)) {
                        return false;
                    } else {
                        WeaponAttributes mainHandAttributes = WeaponRegistry.getAttributes(player.getMainHandStack());
                        WeaponAttributes offHandAttributes = WeaponRegistry.getAttributes(player.getOffHandStack());
                        if (mainHandAttributes.category() != null && !mainHandAttributes.category().isEmpty() && offHandAttributes.category() != null && !offHandAttributes.category().isEmpty()) {
                            return mainHandAttributes.category().equals(offHandAttributes.category());
                        }

                        return false;
                    }
                case NO_OFFHAND_ITEM:
                    offhandStack = player.getOffHandStack();
                    if (offhandStack != null && !(offhandStack.isEmpty() || offhandStack.isIn(Tags.EMPTY_HAND_WEAPONS))) {
                        return false;
                    }

                    return true;
                case OFF_HAND_SHIELD:
                    offhandStack = player.getOffHandStack();
                    if (offhandStack == null && !((offhandStack.getItem() instanceof ShieldItem) || (offhandStack.getItem() instanceof BasicShieldItem))) {
                        return false;
                    }

                    return true;
                case MAIN_HAND_ONLY:
                    return !isOffHandAttack;
                case OFF_HAND_ONLY:
                    return isOffHandAttack;
                case MOUNTED:
                    // repurposed for two-handed attacks
                    return !((DuckPlayerEntityMixin)player).betteradventuremode$isMainHandStackSheathed() && ((DuckPlayerEntityMixin)player).betteradventuremode$isOffHandStackSheathed();
                case NOT_MOUNTED:
                    // repurposed for non-two-handed attacks
                    return ((DuckPlayerEntityMixin)player).betteradventuremode$isMainHandStackSheathed() || !((DuckPlayerEntityMixin)player).betteradventuremode$isOffHandStackSheathed();
                default:
                    return true;
            }
        }
    }
}
