package com.github.theredbrain.bamcore.api.util;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.MutablePair;

public class PacketByteBufUtils {

    public static class PairStringStringListReader implements PacketByteBuf.PacketReader<Pair<String, String>> {
        @Override
        public Pair<String, String> apply(PacketByteBuf packetByteBuf) {
            return new Pair<>(packetByteBuf.readString(), packetByteBuf.readString());
        }
    }

    public static class PairStringStringListWriter implements PacketByteBuf.PacketWriter<Pair<String, String>> {
        @Override
        public void accept(PacketByteBuf packetByteBuf, Pair<String, String> stringStringPair) {
            packetByteBuf.writeString(stringStringPair.getLeft());
            packetByteBuf.writeString(stringStringPair.getRight());
        }
    }

    public static class PairStringPairBlockPosPairDoubleDoubleListReader implements PacketByteBuf.PacketReader<MutablePair<String, MutablePair<BlockPos, MutablePair<Double, Double>>>> {
        @Override
        public MutablePair<String, MutablePair<BlockPos, MutablePair<Double, Double>>> apply(PacketByteBuf packetByteBuf) {
            return new MutablePair<>(packetByteBuf.readString(), new MutablePair<>(packetByteBuf.readBlockPos(), new MutablePair<>(packetByteBuf.readDouble(), packetByteBuf.readDouble())));
        }
    }

    public static class PairStringPairBlockPosPairDoubleDoubleListWriter implements PacketByteBuf.PacketWriter<MutablePair<String, MutablePair<BlockPos, MutablePair<Double, Double>>>> {
        @Override
        public void accept(PacketByteBuf packetByteBuf, MutablePair<String, MutablePair<BlockPos, MutablePair<Double, Double>>> pairStringPairBlockPosPairDoubleDouble) {
            packetByteBuf.writeString(pairStringPairBlockPosPairDoubleDouble.getLeft());
            packetByteBuf.writeBlockPos(pairStringPairBlockPosPairDoubleDouble.getRight().getLeft());
            packetByteBuf.writeDouble(pairStringPairBlockPosPairDoubleDouble.getRight().getRight().getLeft());
            packetByteBuf.writeDouble(pairStringPairBlockPosPairDoubleDouble.getRight().getRight().getRight());
        }
    }

    public static class BlockPosListReader implements PacketByteBuf.PacketReader<BlockPos> {
        @Override
        public BlockPos apply(PacketByteBuf packetByteBuf) {
            return new BlockPos(packetByteBuf.readInt(), packetByteBuf.readInt(), packetByteBuf.readInt());
        }
    }

    public static class BlockPosListWriter implements PacketByteBuf.PacketWriter<BlockPos> {
        @Override
        public void accept(PacketByteBuf packetByteBuf, BlockPos blockPos) {
            packetByteBuf.writeInt(blockPos.getX());
            packetByteBuf.writeInt(blockPos.getY());
            packetByteBuf.writeInt(blockPos.getZ());
        }
    }

    public static class StringListReader implements PacketByteBuf.PacketReader<String> {
        @Override
        public String apply(PacketByteBuf packetByteBuf) {
            return packetByteBuf.readString();
        }
    }

    public static class StringListWriter implements PacketByteBuf.PacketWriter<String> {
        @Override
        public void accept(PacketByteBuf packetByteBuf, String string) {
            packetByteBuf.writeString(string);
        }
    }
}
