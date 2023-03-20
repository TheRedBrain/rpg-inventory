package com.github.theredbrain.rpgmod.mixin.item;

import com.github.theredbrain.rpgmod.item.DuckItemMixin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Item.class)
public class ItemMixin implements DuckItemMixin {

    @Shadow
    public boolean isFood() {
        throw new AssertionError();
    }

    @Shadow
    public FoodComponent getFoodComponent() {
        throw new AssertionError();
    }

    public TypedActionResult<ItemStack> adventureUse(World world, PlayerEntity user) {
        if (this.isFood()) {
            ItemStack itemStack = user.getInventory().main.get(user.getInventory().selectedSlot);//getStackInHand(hand);
            if (user.canConsume(this.getFoodComponent().isAlwaysEdible())) {
//                user.setCurrentHand(hand);
                return TypedActionResult.consume(itemStack);
            }
            return TypedActionResult.fail(itemStack);
        }
        return TypedActionResult.pass(user.getInventory().main.get(user.getInventory().selectedSlot));
    }
}
