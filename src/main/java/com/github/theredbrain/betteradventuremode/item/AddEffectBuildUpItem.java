package com.github.theredbrain.betteradventuremode.item;

import com.github.theredbrain.betteradventuremode.entity.DuckLivingEntityMixin;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class AddEffectBuildUpItem extends Item {
    private final int index;
    private final boolean isPositive;
    public AddEffectBuildUpItem(Settings settings, int index, boolean isPositive) {
        super(settings);
        this.index = index;
        this.isPositive = isPositive;
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof ServerPlayerEntity serverPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }

        if (user instanceof PlayerEntity && !((PlayerEntity)user).getAbilities().creativeMode) {
            stack.decrement(1);
        }

        if (!world.isClient) {
            if (index == 0) {
                ((DuckLivingEntityMixin)user).betteradventuremode$addBleedingBuildUp(10.0f * (isPositive ? -1 : 1));
            } else if (index == 1) {
                ((DuckLivingEntityMixin)user).betteradventuremode$addBurnBuildUp(10.0f * (isPositive ? -1 : 1));
            } else if (index == 2) {
                ((DuckLivingEntityMixin)user).betteradventuremode$addFreezeBuildUp(10.0f * (isPositive ? -1 : 1));
            } else if (index == 3) {
                ((DuckLivingEntityMixin)user).betteradventuremode$addPoisonBuildUp(10.0f * (isPositive ? -1 : 1));
            } else if (index == 4) {
                ((DuckLivingEntityMixin)user).betteradventuremode$addShockBuildUp(10.0f * (isPositive ? -1 : 1));
            } else if (index == 5) {
                ((DuckLivingEntityMixin)user).betteradventuremode$addStaggerBuildUp(10.0f * (isPositive ? -1 : 1));
            }
        }

        return stack;
    }

    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.EAT;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }
}
