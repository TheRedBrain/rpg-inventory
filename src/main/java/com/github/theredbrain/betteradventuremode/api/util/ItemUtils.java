package com.github.theredbrain.betteradventuremode.api.util;

import com.github.theredbrain.betteradventuremode.registry.Tags;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
//import net.spell_engine.api.spell.SpellContainer;

public class ItemUtils {

    /**
     * {@return whether this item should provide its attribute modifiers and if should be rendered}
     */
    public static boolean isUsable(ItemStack stack) {
        return stack.getDamage() < stack.getMaxDamage() - 1 || stack.isIn(Tags.EMPTY_HAND_WEAPONS);
    }

    // TODO SpellEngine 1.20.2
//    public static SpellContainer setProxyPool(SpellContainer spellContainer, String proxyPool) {
//        ((DuckSpellContainerMixin)spellContainer).setProxyPool(proxyPool);
//        return spellContainer;
//    }

    public static ItemStack getItemStackFromShopItemStack(ItemUtils.VirtualItemStack shopItemStack) {
        NbtCompound nbt = new NbtCompound();

        // makes sure identifier is always a valid item identifier
        nbt.putString("id", Registries.ITEM.getId(Registries.ITEM.get(Identifier.tryParse(shopItemStack.getId()))).toString());
        nbt.putByte("Count", (byte)shopItemStack.getCount());
        NbtCompound tag = shopItemStack.getTag();
        if (tag != null) {
            nbt.put("tag", tag.copy());
        }
        return ItemStack.fromNbt(nbt);
    }

    /**
     * is used to define an itemStack in a data-driven context
     */
    public final class VirtualItemStack implements Comparable<VirtualItemStack> {

        private final String id;

        private final int count;

        private final @Nullable NbtCompound tag;

        public VirtualItemStack(String id, int count, @Nullable NbtCompound tag) {
            this.id = id;
            this.count = count;
            this.tag = tag;
        }

        public String getId() {
            return this.id;
        }

        public int getCount() {
            return this.count;
        }

        @Nullable
        public NbtCompound getTag() {
            return this.tag;
        }

        @Override
        public int compareTo(@NotNull ItemUtils.VirtualItemStack that) {
            int i = this.id.compareTo(that.id);
            if (i == 0) {
                i = Integer.compare(this.count, that.count);
                if (i == 0) {
                    if (this.count > 0) {
                        return 0;
                    }
                    NbtCompound thisTag = this.tag;
                    NbtCompound thatTag = that.tag;
                    if (thisTag == null && thatTag == null) {
                        return 0;
                    } else if (thisTag == null) {
                        return -1;
                    } else if (thatTag == null) {
                        return 1;
                    } else {
                        i = Integer.compare(thisTag.hashCode(), thatTag.hashCode());
                    }
                }
            }
            return i;
        }
    }
}
