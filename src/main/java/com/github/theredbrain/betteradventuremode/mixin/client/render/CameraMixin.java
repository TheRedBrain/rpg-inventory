package com.github.theredbrain.betteradventuremode.mixin.client.render;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.BetterAdventureModeClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow
    private float pitch;
    @Shadow
    private float yaw;

    @Shadow public abstract boolean isThirdPerson();

    @Inject(method = "update", at = @At(value = "INVOKE", target = "net/minecraft/client/render/Camera.moveBy(DDD)V", ordinal = 0))
    private void inject_betterAdventureMode$update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (BetterAdventureModeClient.clientConfig.enable_360_degree_third_person && BetterAdventureMode.serverConfig.allow_360_degree_third_person && this.isThirdPerson()) {
            this.pitch = BetterAdventureModeClient.INSTANCE.cameraPitch;
            this.yaw = BetterAdventureModeClient.INSTANCE.cameraYaw;
        }
    }

    @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "net/minecraft/client/render/Camera.setRotation(FF)V", ordinal = 0))
    private void modify_rotation_args_betterAdventureMode$update(Args args) {
        if (BetterAdventureModeClient.clientConfig.enable_360_degree_third_person && BetterAdventureMode.serverConfig.allow_360_degree_third_person && this.isThirdPerson()) {
            args.set(0, BetterAdventureModeClient.INSTANCE.cameraYaw);
            args.set(1, BetterAdventureModeClient.INSTANCE.cameraPitch);
        }
    }
}
