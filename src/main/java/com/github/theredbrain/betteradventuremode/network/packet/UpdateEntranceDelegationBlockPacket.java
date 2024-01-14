package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class UpdateEntranceDelegationBlockPacket implements FabricPacket {
    public static final PacketType<UpdateEntranceDelegationBlockPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("update_entrance_delegation_block"),
            UpdateEntranceDelegationBlockPacket::new
    );

    public final BlockPos entranceDelegationBlockPosition;
    public final BlockPos delegatedEntrancePositionOffset;
    public final double delegatedEntranceYaw;
    public final double delegatedEntrancePitch;

    public UpdateEntranceDelegationBlockPacket(BlockPos entranceDelegationBlockPosition, BlockPos delegatedEntrancePositionOffset, double delegatedEntranceYaw, double delegatedEntrancePitch) {
        this.entranceDelegationBlockPosition = entranceDelegationBlockPosition;
        this.delegatedEntrancePositionOffset = delegatedEntrancePositionOffset;
        this.delegatedEntranceYaw = delegatedEntranceYaw;
        this.delegatedEntrancePitch = delegatedEntrancePitch;
    }

    public UpdateEntranceDelegationBlockPacket(PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readBlockPos(), buf.readDouble(), buf.readDouble());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.entranceDelegationBlockPosition);
        buf.writeBlockPos(this.delegatedEntrancePositionOffset);
        buf.writeDouble(this.delegatedEntranceYaw);
        buf.writeDouble(this.delegatedEntrancePitch);
    }

}
