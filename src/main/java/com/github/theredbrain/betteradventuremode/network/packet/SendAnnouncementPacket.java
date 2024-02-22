package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class SendAnnouncementPacket implements FabricPacket {
    public static final PacketType<SendAnnouncementPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("send_announcement"),
            SendAnnouncementPacket::new
    );

    public final Text announcement;

    public SendAnnouncementPacket(Text announcement) {
        this.announcement = announcement;
    }

    public SendAnnouncementPacket(PacketByteBuf buf) {
        this(buf.readText());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeText(this.announcement);
    }
}
