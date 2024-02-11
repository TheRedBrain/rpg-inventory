package com.github.theredbrain.betteradventuremode.mixin.server.network;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.UnlockRecipesS2CPacket;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerRecipeBook;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ServerRecipeBook.class)
public class ServerRecipeBookMixin {

    @Inject(method = "sendUnlockRecipesPacket", at = @At("HEAD"), cancellable = true)
    private void betteradventuremode$sendUnlockRecipesPacket(UnlockRecipesS2CPacket.Action action, ServerPlayerEntity player, List<Identifier> recipeIds, CallbackInfo ci) {
        if (BetterAdventureMode.serverConfig.disable_recipe_book) {
            ci.cancel();
        }
    }

    @Inject(method = "toNbt", at = @At("HEAD"), cancellable = true)
    public void betteradventuremode$toNbt(CallbackInfoReturnable<NbtCompound> cir) {
        if (BetterAdventureMode.serverConfig.disable_recipe_book) {
            cir.setReturnValue(new NbtCompound());
            cir.cancel();
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"), cancellable = true)
    public void betteradventuremode$readNbt(NbtCompound nbt, RecipeManager recipeManager, CallbackInfo ci) {
        if (BetterAdventureMode.serverConfig.disable_recipe_book) {
            ci.cancel();
        }
    }

    @Inject(method = "sendInitRecipesPacket", at = @At("HEAD"), cancellable = true)
    public void betteradventuremode$sendInitRecipesPacket(ServerPlayerEntity player, CallbackInfo ci) {
        if (BetterAdventureMode.serverConfig.disable_recipe_book) {
            ci.cancel();
        }
    }
}
