package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.api.util.PacketByteBufUtils;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.List;

public class UpdateTriggeredCounterBlockPacket implements FabricPacket {
    public static final PacketType<UpdateTriggeredCounterBlockPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("update_triggered_counter_block"),
            UpdateTriggeredCounterBlockPacket::new
    );

    public final BlockPos triggeredCounterBlockPosition;

    public final List<MutablePair<Integer, BlockPos>> triggeredBlocksList;

    public UpdateTriggeredCounterBlockPacket(BlockPos triggeredCounterBlockPosition, List<MutablePair<Integer, BlockPos>> triggeredBlocksList) {
        this.triggeredCounterBlockPosition = triggeredCounterBlockPosition;
        this.triggeredBlocksList = triggeredBlocksList;
    }

    public UpdateTriggeredCounterBlockPacket(PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readList(new PacketByteBufUtils.MutablePairIntegerBlockPosReader()));
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.triggeredCounterBlockPosition);
        buf.writeCollection(this.triggeredBlocksList, new PacketByteBufUtils.MutablePairIntegerBlockPosWriter());
    }

}
