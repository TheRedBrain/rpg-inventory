package com.github.theredbrain.bamcore.components;
// TODO move to bamdimensions
//import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
//import net.minecraft.nbt.NbtCompound;
//
//public class LongComponent implements ILongComponent, PlayerComponent {
//    private long value = 0;
//
//    @Override
//    public long getValue() {
//        return this.value;
//    }
//
//    @Override
//    public void setValue(long value) {
//        this.value = value;
//    }
//
//    @Override
//    public void readFromNbt(NbtCompound tag) {
//        this.value = 0;
//        if (tag.getKeys().contains("value")) {
//            value = tag.getLong("value");
//        }
//    }
//
//    @Override
//    public void writeToNbt(NbtCompound tag) {
////        Set<String> tagKeys = tag.getKeys();
////        for (String key : tagKeys) {
////            tag.putString(key, "");
////        }
////        if (!value.isEmpty()) {
////            Set<String> valuesKeys = value.keySet();
////            for (String key : valuesKeys) {
//        tag.putLong("value", this.value);
////                tag.putBoolean(key + "_status", value.get(key).getRight());
////            }
////        }
//    }
//}
