package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.api.effect.AuraStatusEffect;
import com.github.theredbrain.betteradventuremode.api.item.AuraGrantingNecklaceTrinketItem;
import com.github.theredbrain.betteradventuremode.entity.DuckLivingEntityMixin;
import com.github.theredbrain.betteradventuremode.registry.Tags;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Optional;

public class ToggleNecklaceAbilityPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<ToggleNecklaceAbilityPacket> {
    @Override
    public void receive(ToggleNecklaceAbilityPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        ItemStack necklaceItemStack = ItemStack.EMPTY;

        MinecraftServer server = player.server;

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
        } else if (((DuckLivingEntityMixin) player).betteradventuremode$getMana() > 0) {
            if (necklaceItemStack.isIn(Tags.TELEPORT_HOME_NECKLACES)) {
                // TODO options
                //  - give a status effect
                //  --- maybe spawns particles
                //  --- when effect ends player is teleported
                //  - open little confirm screen
                //  --- maybe a countdown
                //  no instant teleport in dangerous situations
                //  don't respawn but teleport -> doesn't remove status effects
                //  or maybe removed effects is good? -> extra cost
                server.getPlayerManager().respawnPlayer(player, true);
            } else if (necklaceItemStack.getItem() instanceof AuraGrantingNecklaceTrinketItem) {
                AuraStatusEffect auraStatusEffect = ((AuraGrantingNecklaceTrinketItem) necklaceItemStack.getItem()).getAuraStatusEffect();
                if (player.hasStatusEffect(auraStatusEffect)) {
                    player.removeStatusEffect(auraStatusEffect);
                } else {
                    player.addStatusEffect(new StatusEffectInstance(auraStatusEffect, -1, 0, false, false, true));
                }
            }
        } else {
            player.sendMessageToClient(Text.translatable("hud.message.manaTooLow"), true);
        }
    }
}