package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class UpdateTriggeredAdvancementCheckerBlockPacket implements FabricPacket {
    public static final PacketType<UpdateTriggeredAdvancementCheckerBlockPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("update_triggered_advancement_checker_block"),
            UpdateTriggeredAdvancementCheckerBlockPacket::new
    );

    public final BlockPos triggeredAdvancementCheckerBlockPosition;

    public final BlockPos firstTriggeredBlockPositionOffset;

    public final BlockPos secondTriggeredBlockPositionOffset;

    public final String checkedAdvancementIdentifier;

    public UpdateTriggeredAdvancementCheckerBlockPacket(BlockPos triggeredAdvancementCheckerBlockPosition, BlockPos firstTriggeredBlockPositionOffset, BlockPos secondTriggeredBlockPositionOffset, String checkedAdvancementIdentifier) {
        this.triggeredAdvancementCheckerBlockPosition = triggeredAdvancementCheckerBlockPosition;
        this.firstTriggeredBlockPositionOffset = firstTriggeredBlockPositionOffset;
        this.secondTriggeredBlockPositionOffset = secondTriggeredBlockPositionOffset;
        this.checkedAdvancementIdentifier = checkedAdvancementIdentifier;
    }

    public UpdateTriggeredAdvancementCheckerBlockPacket(PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readBlockPos(), buf.readBlockPos(), buf.readString());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.triggeredAdvancementCheckerBlockPosition);
        buf.writeBlockPos(this.firstTriggeredBlockPositionOffset);
        buf.writeBlockPos(this.secondTriggeredBlockPositionOffset);
        buf.writeString(this.checkedAdvancementIdentifier);
    }

}
