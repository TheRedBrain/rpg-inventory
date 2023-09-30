package com.github.theredbrain.bamcore.api.item;

import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class AdventureFoodConsumable extends Item {

//    private int useTime;

    public AdventureFoodConsumable(Settings settings/*, int useTime*/) {
        super(settings);
//        this.useTime = useTime;
    }

//    public int getMaxUseTime(ItemStack stack) {
//        return this.useTime;
//    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (this.isFood()) {
            ItemStack itemStack = user.getStackInHand(hand);
            if (((DuckPlayerEntityMixin)user).bamcore$canConsumeItem(itemStack)) {
                user.setCurrentHand(hand);
                return TypedActionResult.consume(itemStack);
            }
            return TypedActionResult.fail(itemStack);
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
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
