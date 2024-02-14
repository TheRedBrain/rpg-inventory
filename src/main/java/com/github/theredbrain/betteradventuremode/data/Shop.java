package com.github.theredbrain.betteradventuremode.data;

import com.github.theredbrain.betteradventuremode.util.ItemUtils;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.SortedSet;

public final class Shop {

    private final List<Deal> dealList;

    public Shop(List<Deal> dealList) {
        this.dealList = dealList;
    }

    public List<Deal> getDealList() {
        return this.dealList;
    }

    public final class Deal {

//        private final List<ItemUtils.VirtualItemStack> offer;
        private final ItemUtils.VirtualItemStack offer;

        private final SortedSet<ItemUtils.VirtualItemStack> price;

        private final int maxStockCount;

        private final String lockAdvancement;

        private final String unlockAdvancement;

        private final boolean showLockedDeal;

        public Deal(/*List<ItemUtils.VirtualItemStack> offer, */ItemUtils.VirtualItemStack offer, SortedSet<ItemUtils.VirtualItemStack> price, int maxStockCount, String lockAdvancement, String unlockAdvancement, boolean showLockedDeal) {
            this.offer = offer;
            this.price = price;
            this.maxStockCount = maxStockCount;
            this.lockAdvancement = lockAdvancement;
            this.unlockAdvancement = unlockAdvancement;
            this.showLockedDeal = showLockedDeal;
        }

//        public List<ItemUtils.VirtualItemStack> getOffer() {
//            return this.offer;
//        }
        public ItemUtils.VirtualItemStack getOffer() {
            return this.offer;
        }

        public SortedSet<ItemUtils.VirtualItemStack> getPrice() {
            return this.price;
        }

        public List<ItemUtils.VirtualItemStack> getPriceList() {
            return this.price.stream().toList();
        }

        public int getMaxStockCount() {
            return this.maxStockCount;
        }

        public String getUnlockAdvancement() {
            if (Identifier.isValid(this.unlockAdvancement)) {
                return this.unlockAdvancement;
            }
            return "";
        }

        public String getLockAdvancement() {
            if (Identifier.isValid(this.lockAdvancement)) {
                return this.lockAdvancement;
            }
            return "";
        }

        public boolean showLockedDeal() {
            return this.showLockedDeal;
        }
    }
}
