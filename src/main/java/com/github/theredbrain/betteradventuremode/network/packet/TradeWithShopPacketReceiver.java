package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.data.Shop;
import com.github.theredbrain.betteradventuremode.util.ItemUtils;
import com.github.theredbrain.betteradventuremode.registry.ShopsRegistry;
import com.github.theredbrain.betteradventuremode.screen.ShopBlockScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class TradeWithShopPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<TradeWithShopPacket> {
    @Override
    public void receive(TradeWithShopPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        String shopIdentifier = packet.shopIdentifier;
        int id = packet.id;

        ScreenHandler screenHandler = player.currentScreenHandler;
        List<Shop.Deal> dealsList = new ArrayList<>(List.of());
        Shop shop = null;
        if (!shopIdentifier.equals("")) {
            shop = ShopsRegistry.getShop(new Identifier(shopIdentifier));
        }
        if (shop != null) {
            dealsList = shop.getDealList();
        }

        Shop.Deal currentDeal = dealsList.get(id);
        if (currentDeal != null && screenHandler instanceof ShopBlockScreenHandler shopBlockScreenHandler) {
            boolean bl = true;
            for (ItemUtils.VirtualItemStack price : currentDeal.getPriceList()) {
                Item virtualItem = ItemUtils.getItemStackFromVirtualItemStack(price).getItem();
                int priceCount = price.getCount();
                for (int j = 0; j < shopBlockScreenHandler.inventory.size(); j++) {
                    if (shopBlockScreenHandler.inventory.getStack(j).isOf(virtualItem)) {
                        ItemStack itemStack = shopBlockScreenHandler.slots.get(j + 36).getStack().copy();
                        int stackCount = itemStack.getCount();
                        if (stackCount >= priceCount) {
                            itemStack.setCount(stackCount - priceCount);
                            shopBlockScreenHandler.slots.get(j + 36).setStack(itemStack);
                            priceCount = 0;
                            break;
                        } else {
                            shopBlockScreenHandler.slots.get(j + 36).setStack(ItemStack.EMPTY);
                            priceCount = priceCount - stackCount;
                        }
                    }
                }
                if (priceCount > 0) {
                    bl = false;
                }
            }
            if (bl) {
                player.getInventory().offerOrDrop(ItemUtils.getItemStackFromVirtualItemStack(currentDeal.getOffer()));
            }
        }
    }
}