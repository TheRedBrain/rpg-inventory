package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import dev.emi.trinkets.SurvivalTrinketSlot;
import dev.emi.trinkets.TrinketPlayerScreenHandler;
import dev.emi.trinkets.api.SlotType;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Objects;
import java.util.Optional;

public class SwapHandItemsPacketReceiver implements ServerPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        boolean mainHand = buf.readBoolean();

        server.execute(() -> {
            ItemStack itemStack = ItemStack.EMPTY;
            ItemStack alternateItemStack = ItemStack.EMPTY;

            Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
            if (trinkets.isPresent()) {
                if (mainHand) {
                    if (trinkets.get().getInventory().get("main_hand") != null) {
                        if (trinkets.get().getInventory().get("main_hand").get("main_hand") != null) {
                            itemStack = trinkets.get().getInventory().get("main_hand").get("main_hand").getStack(0);
                        }
                    }
                    if (trinkets.get().getInventory().get("alternative_main_hand") != null) {
                        if (trinkets.get().getInventory().get("alternative_main_hand").get("alternative_main_hand") != null) {
                            alternateItemStack = trinkets.get().getInventory().get("alternative_main_hand").get("alternative_main_hand").getStack(0);
                        }
                    }
                } else {
                    itemStack = player.getInventory().offHand.get(0);
                    if (trinkets.get().getInventory().get("alternative_off_hand") != null) {
                        if (trinkets.get().getInventory().get("alternative_off_hand").get("alternative_off_hand") != null) {
                            alternateItemStack = trinkets.get().getInventory().get("alternative_off_hand").get("alternative_off_hand").getStack(0);
                        }
                    }
                }
            }

            if (itemStack.isEmpty() && alternateItemStack.isEmpty()) {
                return;
            }
            if (((DuckPlayerEntityMixin) player).bamcore$getStamina() <= 0) {
                player.sendMessageToClient(Text.translatable("hud.message.staminaTooLow"), true);
                return;
            }

            if (mainHand) {
                if (trinkets.get().getInventory().get("main_hand") != null) {
                    if (trinkets.get().getInventory().get("main_hand").get("main_hand") != null) {
                        trinkets.get().getInventory().get("main_hand").get("main_hand").setStack(0, alternateItemStack);
                    }
                }
                if (trinkets.get().getInventory().get("alternative_main_hand") != null) {
                    if (trinkets.get().getInventory().get("alternative_main_hand").get("alternative_main_hand") != null) {
                        trinkets.get().getInventory().get("alternative_main_hand").get("alternative_main_hand").setStack(0, itemStack);
                    }
                }
            } else {
                player.getInventory().offHand.set(0, alternateItemStack);
                player.getInventory().markDirty();
                if (trinkets.get().getInventory().get("alternative_off_hand") != null) {
                    if (trinkets.get().getInventory().get("alternative_off_hand").get("alternative_off_hand") != null) {
                        trinkets.get().getInventory().get("alternative_off_hand").get("alternative_off_hand").setStack(0, itemStack);
                    }
                }
            }

            // TODO play sounds
        });
    }
}
