package com.github.theredbrain.betteradventuremode.util;

import com.github.theredbrain.betteradventuremode.registry.Tags;
import com.github.theredbrain.betteradventuremode.spell_engine.DuckSpellContainerMixin;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.SpellContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
//import net.spell_engine.api.spell.SpellContainer;

public class ItemUtils {

    /**
     * {@return whether this item should provide its attribute modifiers and if should be rendered}
     */
    public static boolean isUsable(ItemStack stack) {
        return stack.getDamage() < stack.getMaxDamage() - 1 || stack.isIn(Tags.EMPTY_HAND_WEAPONS);
    }

    public static SpellContainer setProxyPool(SpellContainer spellContainer, String proxyPool) {
        ((DuckSpellContainerMixin)spellContainer).betteradventuremode$setProxyPool(proxyPool);
        return spellContainer;
    }

    public static ItemStack getItemStackFromVirtualItemStack(ItemUtils.VirtualItemStack virtualItemStack) {
        NbtCompound nbt = new NbtCompound();

        // makes sure identifier is always a valid item identifier
        nbt.putString("id", Registries.ITEM.getId(Registries.ITEM.get(Identifier.tryParse(virtualItemStack.getId()))).toString());
        nbt.putByte("Count", (byte)virtualItemStack.getCount());
        VirtualNbtElement tag = virtualItemStack.getTag();
        if (tag != null) {
            nbt.put("tag", getNbtCompoundFromVirtualNbtElement(tag));
        }
        return ItemStack.fromNbt(nbt);
    }
    
    public static NbtCompound getNbtCompoundFromVirtualNbtElement(VirtualNbtElement virtualNbtElement) {
        NbtCompound nbtCompound = new NbtCompound();
        if (virtualNbtElement.getEntries() != null) {
            for (VirtualNbtElement element : virtualNbtElement.getEntries()) {
                String key = element.getKey();
                NbtType type = element.getType();
                if (type == NbtType.END_TYPE) {
                    continue;
                }
                if (type == NbtType.BYTE_TYPE) {
                    nbtCompound.putByte(key, parseByte(element.getValue()));
                } else if (type == NbtType.SHORT_TYPE) {
                    nbtCompound.putShort(key, parseShort(element.getValue()));
                } else if (type == NbtType.INT_TYPE) {
                    nbtCompound.putInt(key, parseInt(element.getValue()));
                } else if (type == NbtType.LONG_TYPE) {
                    nbtCompound.putLong(key, parseLong(element.getValue()));
                } else if (type == NbtType.FLOAT_TYPE) {
                    nbtCompound.putFloat(key, parseFloat(element.getValue()));
                } else if (type == NbtType.DOUBLE_TYPE) {
                    nbtCompound.putDouble(key, parseDouble(element.getValue()));
                } else if (type == NbtType.BYTE_ARRAY_TYPE) {
                    nbtCompound.put(key, new NbtByteArray(getByteListFromVirtualNbtElement(element)));
                } else if (type == NbtType.STRING_TYPE) {
                    nbtCompound.putString(key, element.getValue());
                } else if (type == NbtType.LIST_TYPE) {
                    nbtCompound.put(key, getNbtListFromVirtualNbtElement(element));
                } else if (type == NbtType.COMPOUND_TYPE) {
                    nbtCompound.put(key, getNbtCompoundFromVirtualNbtElement(element));
                } else if (type == NbtType.INT_ARRAY_TYPE) {
                    nbtCompound.put(key, new NbtIntArray(getIntegerListFromVirtualNbtElement(element)));
                } else if (type == NbtType.LONG_ARRAY_TYPE) {
                    nbtCompound.put(key, new NbtLongArray(getLongListFromVirtualNbtElement(element)));
                }
            }
        }
        return nbtCompound;
    }

    public static NbtList getNbtListFromVirtualNbtElement(VirtualNbtElement virtualNbtElement) {
        NbtList nbtList = new NbtList();
        NbtType nbtType = null;
        if (virtualNbtElement.getEntries() != null) {
            for (VirtualNbtElement element : virtualNbtElement.getEntries()) {
                NbtType type = element.getType();
                if (nbtType == null) {
                    nbtType = type;
                }
                if (type == NbtType.END_TYPE || nbtType != type) {
                    continue;
                }
                if (type == NbtType.BYTE_TYPE) {
                    nbtList.add(NbtByte.of(parseByte(element.getValue())));
                } else if (type == NbtType.SHORT_TYPE) {
                    nbtList.add(NbtShort.of(parseShort(element.getValue())));
                } else if (type == NbtType.INT_TYPE) {
                    nbtList.add(NbtInt.of(parseInt(element.getValue())));
                } else if (type == NbtType.LONG_TYPE) {
                    nbtList.add(NbtLong.of(parseLong(element.getValue())));
                } else if (type == NbtType.FLOAT_TYPE) {
                    nbtList.add(NbtFloat.of(parseFloat(element.getValue())));
                } else if (type == NbtType.DOUBLE_TYPE) {
                    nbtList.add(NbtDouble.of(parseDouble(element.getValue())));
                } else if (type == NbtType.BYTE_ARRAY_TYPE) {
                    nbtList.add(new NbtByteArray(getByteListFromVirtualNbtElement(element)));
                } else if (type == NbtType.STRING_TYPE) {
                    nbtList.add(NbtString.of(element.getValue()));
                } else if (type == NbtType.LIST_TYPE) {
                    nbtList.add(getNbtListFromVirtualNbtElement(element));
                } else if (type == NbtType.COMPOUND_TYPE) {
                    nbtList.add(getNbtCompoundFromVirtualNbtElement(element));
                } else if (type == NbtType.INT_ARRAY_TYPE) {
                    nbtList.add(new NbtIntArray(getIntegerListFromVirtualNbtElement(element)));
                } else if (type == NbtType.LONG_ARRAY_TYPE) {
                    nbtList.add(new NbtLongArray(getLongListFromVirtualNbtElement(element)));
                }
            }
        }
        return nbtList;
    }

    public static List<Byte> getByteListFromVirtualNbtElement(VirtualNbtElement virtualNbtElement) {
        List<Byte> byteList = new ArrayList<>();
        if (virtualNbtElement.getEntries() != null) {
            for (VirtualNbtElement element : virtualNbtElement.getEntries()) {
                NbtType type = element.getType();
                if (type == NbtType.BYTE_TYPE) {
                    byteList.add(parseByte(element.getValue()));
                }
            }
        }
        return byteList;
    }

    public static List<Integer> getIntegerListFromVirtualNbtElement(VirtualNbtElement virtualNbtElement) {
        List<Integer> integerList = new ArrayList<>();
        if (virtualNbtElement.getEntries() != null) {
            for (VirtualNbtElement element : virtualNbtElement.getEntries()) {
                NbtType type = element.getType();
                if (type == NbtType.INT_TYPE) {
                    integerList.add(parseInt(element.getValue()));
                }
            }
        }
        return integerList;
    }

    public static List<Long> getLongListFromVirtualNbtElement(VirtualNbtElement virtualNbtElement) {
        List<Long> longList = new ArrayList<>();
        if (virtualNbtElement.getEntries() != null) {
            for (VirtualNbtElement element : virtualNbtElement.getEntries()) {
                NbtType type = element.getType();
                if (type == NbtType.LONG_TYPE) {
                    longList.add(parseLong(element.getValue()));
                }
            }
        }
        return longList;
    }

    /**
     * is used to define an itemStack in a data-driven context
     */
    public final class VirtualItemStack implements Comparable<VirtualItemStack> {

        // TODO make id @Nullable, add @Nullable Identifier itemTag
        private final String id;

        private final int count;

        private final @Nullable VirtualNbtElement tag;

        public VirtualItemStack(String id, int count, @Nullable VirtualNbtElement tag) {
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
        public VirtualNbtElement getTag() {
            return this.tag;
        }

        @Override
        public int compareTo(@NotNull ItemUtils.VirtualItemStack that) {
            int i = this.id.compareTo(that.id);
            if (i == 0) {
                i = Integer.compare(this.count, that.count);
                if (i == 0) {
                    VirtualNbtElement thisTag = this.tag;
                    VirtualNbtElement thatTag = that.tag;
                    if (thisTag == null && thatTag == null) {
                        return 0;
                    } else if (thisTag == null) {
                        return -1;
                    } else if (thatTag == null) {
                        return 1;
                    } else {
                        i = this.tag.compareTo(that.tag);
                    }
                }
            }
            return i;
        }
    }

    public class VirtualNbtElement implements Comparable<VirtualNbtElement> {
        protected final String key;
        protected final String value;
        @Nullable
        private final SortedSet<VirtualNbtElement> entries;
        protected final NbtType type;

        public VirtualNbtElement(String key, String value, @Nullable SortedSet<VirtualNbtElement> entries, NbtType type) {
            this.key = key;
            this.value = value;
            this.entries = entries;
            this.type = type;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return this.value;
        }

        @Nullable
        public SortedSet<VirtualNbtElement> getEntries() {
            return entries;
        }

        public NbtType getType() {
            return type;
        }

        @Nullable
        public VirtualNbtElement get(String key) {
            if (entries != null) {
                for (VirtualNbtElement element : entries) {
                    if (element.getKey().equals(key)) {
                        return element;
                    }
                }
            }
            return null;
        }

        public NbtType getType(String key) {
            if (entries != null) {
                for (VirtualNbtElement element : entries) {
                    if (element.getKey().equals(key)) {
                        return element.getType();
                    }
                }
            }
            return NbtType.END_TYPE;
        }

        public boolean contains(String key) {
            if (entries != null) {
                for (VirtualNbtElement element : entries) {
                    if (element.getKey().equals(key)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean contains(String key, NbtType type) {
            return this.getType(key) == type;
        }

        @Override
        public int compareTo(@NotNull ItemUtils.VirtualNbtElement that) {
            int i = this.key.compareTo(that.key);
            if (i == 0) {
                i = this.type.compareTo(that.type);
                if (i == 0) {
                    i = this.value.compareTo(that.value);
                }
            }
            return i;
        }
    }

    public static byte parseByte(String string) {
        try {
            return Byte.parseByte(string);
        } catch (NumberFormatException numberFormatException) {
            return 0;
        }
    }

    public static short parseShort(String string) {
        try {
            return Short.parseShort(string);
        } catch (NumberFormatException numberFormatException) {
            return 0;
        }
    }

    public static int parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException numberFormatException) {
            return 0;
        }
    }

    public static long parseLong(String string) {
        try {
            return Long.parseLong(string);
        } catch (NumberFormatException numberFormatException) {
            return 0;
        }
    }

    public static float parseFloat(String string) {
        try {
            return Float.parseFloat(string);
        } catch (NumberFormatException numberFormatException) {
            return 0.0f;
        }
    }

    public static double parseDouble(String string) {
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException numberFormatException) {
            return 0.0;
        }
    }

    public enum NbtType {
        END_TYPE,
        BYTE_TYPE,
        SHORT_TYPE,
        INT_TYPE,
        LONG_TYPE,
        FLOAT_TYPE,
        DOUBLE_TYPE,
        BYTE_ARRAY_TYPE,
        STRING_TYPE,
        LIST_TYPE,
        COMPOUND_TYPE,
        INT_ARRAY_TYPE,
        LONG_ARRAY_TYPE;
    }
}
