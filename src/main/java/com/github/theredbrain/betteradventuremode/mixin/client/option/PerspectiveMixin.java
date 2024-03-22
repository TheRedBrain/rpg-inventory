package com.github.theredbrain.betteradventuremode.mixin.client.option;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.BetterAdventureModeClient;
import net.minecraft.client.option.Perspective;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Perspective.class)
public class PerspectiveMixin {

    @Shadow @Final private static Perspective[] VALUES;

    @Inject(method = "next", at = @At("RETURN"), cancellable = true)
    public void betterAdventureMode$next(CallbackInfoReturnable<Perspective> cir) {
        cir.setReturnValue(BetterAdventureModeClient.clientConfig.enable_360_degree_third_person && BetterAdventureMode.serverConfig.allow_360_degree_third_person ? VALUES[((Perspective) (Object) this) == Perspective.FIRST_PERSON || BetterAdventureMode.serverConfig.disable_first_person ? 1 : 0] : cir.getReturnValue());
    }
}
