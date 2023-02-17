package com.github.theredbrain.rpgmod.item;

import com.github.theredbrain.rpgmod.block.AbstractInteractiveAdventureBlock;
import com.github.theredbrain.rpgmod.effect.FoodStatusEffect;
import com.github.theredbrain.rpgmod.entity.PlayerEntityMixinDuck;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class AdventureFoodConsumable extends Item {

//    private final int maxEatenFoods = 3;
    private final int duration;
    private final FoodStatusEffect foodStatusEffect;
    public AdventureFoodConsumable(FoodStatusEffect foodStatusEffect, int duration, Settings settings) {
        super(settings);
        this.duration = duration;
        this.foodStatusEffect = foodStatusEffect;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (((PlayerEntityMixinDuck) user).tryEatAdventureFood(this.foodStatusEffect, this.duration)) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
        return TypedActionResult.fail(itemStack);
    }
//            world.setBlockState(pos, state.with(INTACT, false));
//            return ActionResult.CONSUME;
//        } else {
//            return ActionResult.PASS;
//        }
//            ItemStack itemStack = user.getStackInHand(hand);
//            if (user.getStatusEffects().isEmpty()) {
//                user.setCurrentHand(hand);
//                return TypedActionResult.consume(itemStack);
//            } else {
//                int currentEatenFoods = 0;
//                for (int i = 0; i < this.getFoodComponent().getStatusEffects().size(); i++){
//                    if (!user.getStatusEffects().contains(this.getFoodComponent().getStatusEffects().get(i).getFirst())) {
//
//                        List<StatusEffectInstance> currentEffects = user.getStatusEffects().stream().toList();
//                        for (StatusEffectInstance currentEffect : currentEffects) {
//                            if (currentEffect.getEffectType() == this.getFoodComponent().getStatusEffects().get(i).getFirst().getEffectType()) {
//                                return TypedActionResult.fail(itemStack);
//                            } else if (currentEffect.getEffectType() instanceof FoodStatusEffect) {
//                                currentEatenFoods++;
//                            }
//                        }
//                    }
//                }
//                if (currentEatenFoods < 3) {
//                    user.setCurrentHand(hand);
//                    return TypedActionResult.consume(itemStack);
//                }
//                return TypedActionResult.fail(itemStack);
//            }
//        } else {
//            return TypedActionResult.pass(user.getStackInHand(hand));
//        }
//    }
}
