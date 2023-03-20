package com.github.theredbrain.rpgmod.mixin.server.network;

import com.github.theredbrain.rpgmod.item.DuckItemStackMixin;
import com.github.theredbrain.rpgmod.server.network.DuckServerPlayerInteractionManagerMixin;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin implements DuckServerPlayerInteractionManagerMixin {

    @Shadow
    protected ServerWorld world;

    @Shadow
    private GameMode gameMode;

    @Shadow
    public boolean isCreative() {
        throw new AssertionError();
    }

    @Override
    public ActionResult consumeItem(ServerPlayerEntity player, ServerWorld serverWorld, ItemStack stack) {
        if (!(this.gameMode == GameMode.ADVENTURE)) {
            return ActionResult.PASS;
        }
        if (player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
            return ActionResult.PASS;
        }
        int i = stack.getCount();
        int j = stack.getDamage();
        TypedActionResult<ItemStack> typedActionResult = ((DuckItemStackMixin) (Object) stack).adventureUse(world, player);
        ItemStack itemStack = typedActionResult.getValue();
        if (itemStack == stack && itemStack.getCount() == i && itemStack.getMaxUseTime() <= 0 && itemStack.getDamage() == j) {
            return typedActionResult.getResult();
        }
        if (typedActionResult.getResult() == ActionResult.FAIL && itemStack.getMaxUseTime() > 0 && !player.isUsingItem()) {
            return typedActionResult.getResult();
        }
        if (stack != itemStack) {
            player.getInventory().main.set(player.getInventory().selectedSlot, itemStack);//setStackInHand(hand, itemStack);
        }
        if (this.isCreative()) {
            itemStack.setCount(i);
            if (itemStack.isDamageable() && itemStack.getDamage() != j) {
                itemStack.setDamage(j);
            }
        }
        if (itemStack.isEmpty()) {
            player.getInventory().main.set(player.getInventory().selectedSlot, ItemStack.EMPTY);
        }
        if (!player.isUsingItem()) {
            player.playerScreenHandler.syncState();
        }
        return typedActionResult.getResult();
    }
}
