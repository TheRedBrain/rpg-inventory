package com.github.theredbrain.betteradventuremode.mixin.item;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Shadow public abstract boolean isFood();

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (BetterAdventureMode.serverConfig.disable_vanilla_food_system) {
            if (this.isFood()) {
                ItemStack itemStack = user.getStackInHand(hand);
                if (((DuckPlayerEntityMixin) user).betteradventuremode$canConsumeItem(itemStack)) {
                    user.setCurrentHand(hand);
                    cir.setReturnValue(TypedActionResult.consume(itemStack));
                } else {
                    cir.setReturnValue(TypedActionResult.fail(itemStack));
                }
                cir.cancel();
            }
        }
    }
}
