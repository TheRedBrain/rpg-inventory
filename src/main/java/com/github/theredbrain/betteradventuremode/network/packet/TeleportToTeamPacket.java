package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class TeleportToTeamPacket implements FabricPacket {
    public static final PacketType<TeleportToTeamPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("teleport_to_team"),
            TeleportToTeamPacket::new
    );

    public final Identifier targetWorldIdentifier;

    public final BlockPos targetPosition;

    public final double targetYaw;
    public final double targetPitch;

    public TeleportToTeamPacket(Identifier targetWorldIdentifier, BlockPos targetPosition, double targetYaw, double targetPitch) {
        this.targetWorldIdentifier = targetWorldIdentifier;
        this.targetPosition = targetPosition;
        this.targetYaw = targetYaw;
        this.targetPitch = targetPitch;
    }

    public TeleportToTeamPacket(PacketByteBuf buf) {
        this(buf.readIdentifier(), buf.readBlockPos(), buf.readDouble(), buf.readDouble());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeIdentifier(this.targetWorldIdentifier);
        buf.writeBlockPos(this.targetPosition);
        buf.writeDouble(this.targetYaw);
        buf.writeDouble(this.targetPitch);
    }
}
