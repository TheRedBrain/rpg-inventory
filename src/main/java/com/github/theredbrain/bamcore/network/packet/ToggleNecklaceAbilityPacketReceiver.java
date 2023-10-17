package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.api.util.BetterAdventureModeCoreStatusEffects;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.registry.Tags;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Optional;
import java.util.function.Predicate;

public class ToggleNecklaceAbilityPacketReceiver implements ServerPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        server.execute(() -> {

            ItemStack necklaceItemStack = ItemStack.EMPTY;

            Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
            if (trinkets.isPresent()) {
                if (trinkets.get().getInventory().get("necklaces") != null) {
                    if (trinkets.get().getInventory().get("necklaces").get("necklace") != null) {
                        necklaceItemStack = trinkets.get().getInventory().get("necklaces").get("necklace").getStack(0);
                    }
                }
            }

            if (necklaceItemStack == ItemStack.EMPTY) {
                player.sendMessageToClient(Text.translatable("hud.message.noNecklaceEquipped"), true);
            } else if (necklaceItemStack.isIn(Tags.TELEPORT_HOME_NECKLACES)) {
                server.getPlayerManager().respawnPlayer(player, true);
            } else if (necklaceItemStack.isIn(Tags.HEALTH_REGENERATION_AURA_NECKLACES)) {
                if (player.hasStatusEffect(BetterAdventureModeCoreStatusEffects.HEALTH_REGENERATION_AURA_EFFECT)) {
                    player.removeStatusEffect(BetterAdventureModeCoreStatusEffects.HEALTH_REGENERATION_AURA_EFFECT);
                } else {
                    player.addStatusEffect(new StatusEffectInstance(BetterAdventureModeCoreStatusEffects.HEALTH_REGENERATION_AURA_EFFECT, -1, 0, false, false, true));
                }
            }
        });
    }
}