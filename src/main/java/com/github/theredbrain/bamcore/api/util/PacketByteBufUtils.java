package com.github.theredbrain.bamcore.api.util;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.MutablePair;

public class PacketByteBufUtils {

    public static class PairStringEntityAttributeModifierListReader implements PacketByteBuf.PacketReader<MutablePair<String, EntityAttributeModifier>> {
        @Override
        public MutablePair<String, EntityAttributeModifier> apply(PacketByteBuf packetByteBuf) {
            return new MutablePair<>(packetByteBuf.readString(), new EntityAttributeModifier(
                    packetByteBuf.readUuid(),
                    packetByteBuf.readString(),
                    packetByteBuf.readDouble(),
                    EntityAttributeModifier.Operation.fromId(packetByteBuf.readInt())
            ));
        }
    }

    public static class PairStringEntityAttributeModifierListWriter implements PacketByteBuf.PacketWriter<MutablePair<String, EntityAttributeModifier>> {
        @Override
        public void accept(PacketByteBuf packetByteBuf, MutablePair<String, EntityAttributeModifier> stringEntityAttributeModifierPair) {
            packetByteBuf.writeString(stringEntityAttributeModifierPair.getLeft());
            packetByteBuf.writeUuid(stringEntityAttributeModifierPair.getRight().getId());
            packetByteBuf.writeString(stringEntityAttributeModifierPair.getRight().getName());
            packetByteBuf.writeDouble(stringEntityAttributeModifierPair.getRight().getValue());
            packetByteBuf.writeInt(stringEntityAttributeModifierPair.getRight().getOperation().getId());
        }
    }

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

    public static class PairStringBlockPosListReader implements PacketByteBuf.PacketReader<MutablePair<String, BlockPos>> {
        @Override
        public MutablePair<String, BlockPos> apply(PacketByteBuf packetByteBuf) {
            return new MutablePair<>(packetByteBuf.readString(), new BlockPos(packetByteBuf.readInt(), packetByteBuf.readInt(), packetByteBuf.readInt()));
        }
    }

    public static class PairStringBlockPosListWriter implements PacketByteBuf.PacketWriter<MutablePair<String, BlockPos>> {
        @Override
        public void accept(PacketByteBuf packetByteBuf, MutablePair<String, BlockPos> pairStringBlockPos) {
            packetByteBuf.writeString(pairStringBlockPos.getLeft());
            packetByteBuf.writeInt(pairStringBlockPos.getRight().getX());
            packetByteBuf.writeInt(pairStringBlockPos.getRight().getY());
            packetByteBuf.writeInt(pairStringBlockPos.getRight().getZ());
        }
    }

    public static class PairStringPairStringStringListReader implements PacketByteBuf.PacketReader<MutablePair<String, MutablePair<String, String>>> {
        @Override
        public MutablePair<String, MutablePair<String, String>> apply(PacketByteBuf packetByteBuf) {
            return new MutablePair<>(packetByteBuf.readString(), new MutablePair<>(packetByteBuf.readString(), packetByteBuf.readString()));
        }
    }

    public static class PairStringPairStringStringListWriter implements PacketByteBuf.PacketWriter<MutablePair<String, MutablePair<String, String>>> {
        @Override
        public void accept(PacketByteBuf packetByteBuf, MutablePair<String, MutablePair<String, String>> pairStringPairStringString) {
            packetByteBuf.writeString(pairStringPairStringString.getLeft());
            packetByteBuf.writeString(pairStringPairStringString.getRight().getLeft());
            packetByteBuf.writeString(pairStringPairStringString.getRight().getRight());
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
