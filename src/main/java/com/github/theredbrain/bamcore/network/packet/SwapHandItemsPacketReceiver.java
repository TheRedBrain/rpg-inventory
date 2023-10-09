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
//            int handSlotId = -1;
//            int alternateHandSlotId = -1;

//            PlayerScreenHandler screenHandler = player.playerScreenHandler;
//            PlayerInventory playerInventory = player.getInventory();
//            boolean hasEnoughStamina = ((DuckPlayerEntityMixin)player).bamcore$getStamina() > 0;
//            int trinketSlotStart = ((TrinketPlayerScreenHandler) screenHandler).trinkets$getTrinketSlotStart();
//            int trinketSlotEnd = ((TrinketPlayerScreenHandler) screenHandler).trinkets$getTrinketSlotEnd();
            Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
            if (trinkets.isPresent()) {
//                for (int i = trinketSlotStart; i < trinketSlotEnd; i++) {
//                    Slot s = screenHandler.slots.get(i);
//                    if (!(s instanceof SurvivalTrinketSlot ts)) {
//                        continue;
//                    }
//                    SlotType type = ts.getType();

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
//                        if (Objects.equals(type.getGroup(), "alternative_main_hand") && Objects.equals(type.getName(), "alternative_main_hand")) {
//                            alternateHandSlotId = i;
//                        } else if (Objects.equals(type.getGroup(), "main_hand") && Objects.equals(type.getName(), "main_hand")) {
////                            this.trinketSlotIds.put("main_hand", i);
//                            handSlotId = i;
//                        }
                    } else {
                        itemStack = player.getInventory().offHand.get(0);
                        if (trinkets.get().getInventory().get("alternative_off_hand") != null) {
                            if (trinkets.get().getInventory().get("alternative_off_hand").get("alternative_off_hand") != null) {
                                alternateItemStack = trinkets.get().getInventory().get("alternative_off_hand").get("alternative_off_hand").getStack(0);
                            }
                        }
                    }
//                }
            }

//            ItemStack itemStack = screenHandler.getSlot(handSlotId).getStack();
//            ItemStack alternateItemStack = screenHandler.getSlot(alternateHandSlotId).getStack();

            if (itemStack.isEmpty() && alternateItemStack.isEmpty()) {
                return;
            }
            if (((DuckPlayerEntityMixin) player).bamcore$getStamina() <= 0) {
                player.sendMessageToClient(Text.translatable("hud.message.staminaTooLow"), true);
                return;
            }
//            playerInventory.setStack(handSlotId, alternateItemStack);
//            playerInventory.setStack(alternateHandSlotId, itemStack);
//            screenHandler.getSlot(handSlotId).setStack(alternateItemStack);
//            screenHandler.getSlot(alternateHandSlotId).setStack(itemStack);
//            playerInventory.markDirty();

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
//                        if (Objects.equals(type.getGroup(), "alternative_main_hand") && Objects.equals(type.getName(), "alternative_main_hand")) {
//                            alternateHandSlotId = i;
//                        } else if (Objects.equals(type.getGroup(), "main_hand") && Objects.equals(type.getName(), "main_hand")) {
////                            this.trinketSlotIds.put("main_hand", i);
//                            handSlotId = i;
//                        }
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
