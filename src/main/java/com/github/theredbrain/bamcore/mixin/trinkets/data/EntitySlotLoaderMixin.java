package com.github.theredbrain.bamcore.mixin.trinkets.data;

import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import dev.emi.trinkets.TrinketPlayerScreenHandler;
import dev.emi.trinkets.data.EntitySlotLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EntitySlotLoader.class)
public class EntitySlotLoaderMixin {

//    @Shadow protected abstract PacketByteBuf getSlotsPacket();
//
//    /**
//     * @author TheRedBrain
//     * @reason account for AdventureInventoryScreenHandler
//     */
    @Inject(method = "sync(Ljava/util/List;)V", at = @At("TAIL"), remap = false)
    public void bam$syncAdventureInventory(List<? extends ServerPlayerEntity> players, CallbackInfo ci) {
//        PacketByteBuf buf = getSlotsPacket();
//        players.forEach(player -> ServerPlayNetworking.send(player, TrinketsNetwork.SYNC_SLOTS, buf));
//        players.forEach(player -> ((TrinketPlayerScreenHandler) player.playerScreenHandler).trinkets$updateTrinketSlots(true));
        players.forEach(player -> ((TrinketPlayerScreenHandler) ((DuckPlayerEntityMixin) player).bamcore$getInventoryScreenHandler()).trinkets$updateTrinketSlots(true));
    }
}
