package com.github.theredbrain.betteradventuremode.mixin.recipe.book;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.minecraft.recipe.book.RecipeBook;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(RecipeBook.class)
public class RecipeBookMixin {

    @Shadow @Final protected Set<Identifier> recipes;

    @Shadow @Final protected Set<Identifier> toBeDisplayed;

    @Inject(method = "copyFrom", at = @At("HEAD"), cancellable = true)
    public void betteradventuremode$copyFrom(RecipeBook book, CallbackInfo ci) {
        if (BetterAdventureMode.serverConfig.disable_recipe_book) {
            this.recipes.clear();
            this.toBeDisplayed.clear();
            ci.cancel();
        }
    }

    @Inject(method = "contains(Lnet/minecraft/util/Identifier;)Z", at = @At("HEAD"), cancellable = true)
    public void betteradventuremode$contains(Identifier id, CallbackInfoReturnable<Boolean> cir) {
        if (BetterAdventureMode.serverConfig.disable_recipe_book) {
            this.recipes.clear();
            this.toBeDisplayed.clear();
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

}
